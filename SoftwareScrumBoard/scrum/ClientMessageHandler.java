package scrum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author Alex Ayala, Steven Eisemann, Nathan Rao, Nick Rocco
 * Class used by the server to handle messages received from clients and sending messages back to clients
 *
 */

public class ClientMessageHandler implements Runnable {
	private Socket socket;
	private String clientID;
	private static FileWriter fw;
	private static BufferedWriter bw;
	private static PrintWriter out;
	ArrayList<LinkedBlockingQueue<String>> updateQueues;
	LinkedBlockingQueue<String> log;
	ReentrantLock ql;
	ReentrantLock ol;
	ReentrantLock il;
	ReentrantLock ll;
	HashMap<String, String> owners;
	int newestId;

	// Constructor
	public ClientMessageHandler(Socket socket, int clientID, ArrayList<LinkedBlockingQueue<String>> updateQueues,
			HashMap<String, String> owners, ReentrantLock queuesLock, ReentrantLock ownersLock, ReentrantLock idLock,
			LinkedBlockingQueue<String> log, ReentrantLock logLock) {
		this.updateQueues = updateQueues;
		this.socket = socket;
		this.clientID = String.valueOf(clientID);
		this.ql = queuesLock;
		this.ol = ownersLock;
		this.il = idLock;
		this.ll = logLock;
		this.log = log;
		this.owners = owners;
		try {
			fw = new FileWriter("scrum/persistance.txt", true);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
		} catch (Exception e) {
			System.out.println("Failed to create streams");
		}
		this.ql.lock();

		// Adds the clients queue to the list of queues
		updateQueues.add(new LinkedBlockingQueue<String>());

		// Fill the client's specific update queue with the contents of the log
		for (String s : log) {
			updateQueues.get(Integer.parseInt(this.clientID)).add(s);
		}
		this.ql.unlock();
	}

	// Sets owner of specified object to requesting client if no one else owns it
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

	// Handles update message by pushing message to all clients' update queues
	// except the sending client. Removes sending client as owner of the object
	public void handleUpdate(String message) {
		String messageType = message.split("##")[0]; // Update or New;
		String component = message.split("##")[1]; // Component id; "null" if new
		ql.lock();
		ol.lock();

		// Define the id of the created object if it's a New message
		if(messageType.compareTo("New") == 0) {
			il.lock();
			component = String.valueOf(Server.id);
			newestId = Server.id;
			message = message.replace("null", component);
			Server.id += 1;
			il.unlock();
		}
		// If this was an Update message, remove the existing owner of the component
		else {
			String owner = owners.get(component);
			if(owner != null) {
				if(owner.compareTo(clientID) == 0) {
					owners.put(component, "none");
				}
			}
		}
		ol.unlock();
		
		// Push update messages to queues
		for(LinkedBlockingQueue<String> q: updateQueues) {
			if(q != updateQueues.get(Integer.parseInt(clientID))) {
				q.offer(message);
			}
		}
		ll.lock();
		
		// Add message to log and write to log file
		try {
			log.put(message);
			out.println(message);
			out.flush();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ll.unlock();
		ql.unlock();
	}

	// Sets up message to send back to client requesting edit ownership of an object
	public String sendBackEditRequest(String message) {
		String success = "false";
		ol.lock();
		String owner = owners.get(message);
		if(owner != null) {
			if(owner.compareTo(clientID) == 0) {
				success = "true";
			}
		}
		ol.unlock();
		return success;
	}

	// Fetches update message from queue to send back to client
	public String sendBackUpdates() {
		String message = "";
		String popped = "";
		ql.lock();
		popped = updateQueues.get(Integer.parseInt(clientID)).poll();
		if(popped != null) {
			message = popped;
		}
		ql.unlock();
		return message;
	}

	// Start loop to retrieve messages from client
	public void run() {
		try {
			BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());
			String message;
			while(true) {
				int messageType = Integer.parseInt(inputFromClient.readLine());

				switch(messageType) {
				// Get edit permission request from client
				case ScrumChatConstants.SEND_EDIT_REQUEST: {
					message = inputFromClient.readLine();
					handleEditRequest(message);
					break;
				}
				// Get update messages from client
				case ScrumChatConstants.SEND_UPDATE: {
					message = inputFromClient.readLine();
					handleUpdate(message);
					break;
				}
				// Send the newest id to the client to assign an id to new component
				case ScrumChatConstants.GET_NEW_ID: {
					outputToClient.println(newestId);
					outputToClient.flush();
					break;
				}
				// Sends whether or not the client can edit back
				case ScrumChatConstants.GET_EDIT_REQUEST: {
					message = inputFromClient.readLine();
					outputToClient.println(sendBackEditRequest(message));
					outputToClient.flush();
					break;
				}
				// Send update messages to client
				case ScrumChatConstants.GET_UPDATE: {
					outputToClient.println(sendBackUpdates());
					outputToClient.flush();
					break;
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