package scrum;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Server {
	private ServerSocket ss;
	private int clientID = 0;
	private LinkedBlockingQueue<UpdateMessage> q;
	private ArrayList<LinkedBlockingQueue<String>> fakeQ; //this is only here so we dont need to update messages yet, we'll just use strings
	private LinkedBlockingQueue<String> log; // TODO: We will do this later. joining client should update to current version. keep track of and send all previous updates.
	private HashMap<String, String> owners;
	private ReentrantLock queuesLock;
	private ReentrantLock ownersLock;

	//TODO It is recommended practice to always immediately follow a call to lock with a try block, most typically in a before/after construction such as:

	Server() {
		queuesLock = new ReentrantLock();
		ownersLock = new ReentrantLock();
		q = new LinkedBlockingQueue<>();
		fakeQ = new  ArrayList<LinkedBlockingQueue<String>>();
		owners = new HashMap<String,String>();
		new Thread( () -> {
			try {
				// Create a server socket
				ss = new ServerSocket(8000);

				while (true) {
					// Listen for a new connection request
					Socket socket = ss.accept();
					// Increment clientNo
					/*	          Platform.runLater( () -> {
	            // Display the client number
	            textArea.appendText("Starting thread for client " + clientNo +
	              " at " + new Date() + '\n');
	            });
					 */
					// Create and start a new thread for listening to the connection
					new Thread(new ClientMessageHandler(socket,clientID, fakeQ, owners, queuesLock, ownersLock)).start();
					clientID++;

				}
			}
			catch(IOException ex) {
				System.err.println(ex);
			}
		}).start();
	}

	public static void main(String[] args) {
		Server s = new Server();
	}
}

class ClientMessageHandler implements Runnable {
	private Socket socket;
	private String clientID;
	ArrayList<LinkedBlockingQueue<String>> updateQueues;
	ReentrantLock ql;
	ReentrantLock ol;
	HashMap<String, String> owners;

	public ClientMessageHandler(Socket socket, int clientID, ArrayList<LinkedBlockingQueue<String>> updateQueues, HashMap<String, String> owners, ReentrantLock queuesLock, ReentrantLock ownersLock) {
		this.updateQueues = updateQueues;
		this.socket = socket;
		this.clientID = String.valueOf(clientID);
		this.ql = queuesLock;
		this.ol = ownersLock;
		this.owners = owners;
		this.ql.lock();
		updateQueues.add(new LinkedBlockingQueue<String>());
		this.ql.unlock();
	}
	public void handleEditRequest(String message) {
		ol.lock();
		String owner = owners.get(message);
		if(owner == null) {
			owners.put(message, clientID);
		} else if(owner.compareTo("none")== 0) {
			owners.put(message, clientID);
		}
		ol.unlock();
	}
	
	public void handleUpdate(String message) {
		String component = message.split(" ")[0];
		ql.lock();
		ol.lock();
		String owner = owners.get(component);
		if(owner != null) {
			if(owner.compareTo(clientID) == 0) {
				owners.put(component, "none");
			}
		}
		ol.unlock();
		for(LinkedBlockingQueue<String> q: updateQueues) {
			q.offer(message);
		}
		ql.unlock();
	}
	
	public String sendBackEditRequest(String message) {
		String success = "false";
		ol.lock();
		String owner = owners.get(message);
		if(owner != null) {
			if(owner.compareTo(clientID) == 0) {
				success = "true";
				owners.put(message, "none");
			}
		}
		ol.unlock();
		System.out.println("works: " + success);
		return success;
	}
	
	// TODO eventually we want to be able to send ALL updates. so we want a delimeter and loop in client
	public String sendBackUpdates() {
		String message = "";
		String popped = "";
		ql.lock();
		popped = updateQueues.get(Integer.parseInt(clientID)).poll();
		System.out.println("my message: " + popped);
		if(popped != null) {
			message = popped;
		}
		ql.unlock();
		return message;
	}
	
	public void run() {
		try {
			BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());
			String message;
			while(true) {
				//int message = Integer.parseInt(q.poll()); //This needs to be an UpdateMessage or some other custom type
				int messageType = Integer.parseInt(inputFromClient.readLine());
				
				switch(messageType) {
				case ScrumChatConstants.SEND_EDIT_REQUEST: {
					message = inputFromClient.readLine();
					System.out.println("SEND REQUEST: " + message);
					handleEditRequest(message);
					break;
					//should this actually be something server looks for?
				}
				case ScrumChatConstants.SEND_UPDATE: {
					message = inputFromClient.readLine();
					handleUpdate(message);
					break;
					//format a message to update a client about what has changed
				}
				case ScrumChatConstants.GET_EDIT_REQUEST: {
					message = inputFromClient.readLine();
					System.out.println("GET REQUEST: " + message);
					outputToClient.println(sendBackEditRequest(message));
	                 outputToClient.flush();
	                 break;
					//pop another message from the queue
					//this message should be the object to be edited
				}
				case ScrumChatConstants.GET_UPDATE: {
					outputToClient.println(sendBackUpdates());
	                outputToClient.flush();
	                break;
					//should the server care about this?
				}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}