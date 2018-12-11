package scrum;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Server {
	private ServerSocket ss;
	private int clientID = 0;
	private ArrayList<LinkedBlockingQueue<String>> q; //this is only here so we dont need to update messages yet, we'll just use strings
	private LinkedBlockingQueue<String> log; // TODO: We will do this later. joining client should update to current version. keep track of and send all previous updates.
	private HashMap<String, String> owners;
	private ReentrantLock queuesLock;
	private ReentrantLock ownersLock;
	private ReentrantLock idLock;
	private ReentrantLock logLock;
	private BufferedReader in;
	//private FileOutputStream out;
	public static int id = 1;

	//TODO It is recommended practice to always immediately follow a call to lock with a try block, most typically in a before/after construction such as:

	Server() {
		try {
			in = new BufferedReader(new FileReader("scrum/persistance.txt"));
			//out = new FileOutputStream("scrum/persistance.txt");
		} catch (Exception e) {
			System.out.println("Failed to create streams");
		}
		queuesLock = new ReentrantLock();
		ownersLock = new ReentrantLock();
		idLock = new ReentrantLock();
		logLock = new ReentrantLock();
		log = new LinkedBlockingQueue<String>();
		q = new  ArrayList<LinkedBlockingQueue<String>>();
		owners = new HashMap<String,String>();
		bootFile();
		new Thread( () -> {
			try {
				// Create a server socket
				ss = new ServerSocket(8000);

				while (true) {
					// Listen for a new connection request
					Socket socket = ss.accept();

					// Create and start a new thread for listening to the connection
					new Thread(new ClientMessageHandler(socket,clientID, q, owners, queuesLock, ownersLock, idLock, log, logLock)).start();
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

	public void bootFile() {
		try {
		String line = in.readLine();
		while (line != null) {
			log.add(line);
			line = in.readLine();
		}
		} catch (IOException e) {
			System.out.println("persistance reading error");
		}
		System.out.println(log.size());
		for (String s : log) {
			System.out.println(s);
		}
	}
}

class ClientMessageHandler implements Runnable {
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
		//adds the clients queue to the list of queues
		updateQueues.add(new LinkedBlockingQueue<String>());

		//now fill the queue with the log
		for (String s : log) {
			updateQueues.get(Integer.parseInt(this.clientID)).add(s);
		}
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
		String messageType = message.split("##")[0]; // Update or New;
		String component = message.split("##")[1]; // Component id; "null" if new
		ql.lock();
		ol.lock();

		// Define the id of the created object
		if(messageType.compareTo("New") == 0) {
			il.lock();
			component = String.valueOf(Server.id);
			newestId = Server.id;
			System.out.println(message);
			message = message.replace("null", component);
			System.out.println(message);
			Server.id += 1;
			il.unlock();
		}
		else {
			String owner = owners.get(component);
			if(owner != null) {
				if(owner.compareTo(clientID) == 0) {
					owners.put(component, "none");
				}
			}
		}
		ol.unlock();
		for(LinkedBlockingQueue<String> q: updateQueues) {
			if(q != updateQueues.get(Integer.parseInt(clientID))) {
				q.offer(message);
			}
		}
		ll.lock();
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
		System.out.println("works: " + success);
		return success;
	}

	// TODO eventually we want to be able to send ALL updates. so we want a delimeter and loop in client
	public String sendBackUpdates() {
		String message = "";
		String popped = "";
		ql.lock();
		popped = updateQueues.get(Integer.parseInt(clientID)).poll();
	//	System.out.println("my message: " + popped);
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
				int messageType = Integer.parseInt(inputFromClient.readLine());

				switch(messageType) {
				case ScrumChatConstants.SEND_EDIT_REQUEST: {
					message = inputFromClient.readLine();
					System.out.println("SEND REQUEST: " + message);
					handleEditRequest(message);
					break;
				}
				// TWO CASES. NEW OBJECT and CHANGED OBJECT
				case ScrumChatConstants.SEND_UPDATE: {
					message = inputFromClient.readLine();
					handleUpdate(message);
					break;
				}
				case ScrumChatConstants.GET_NEW_ID: {
					outputToClient.println(newestId);
					outputToClient.flush();
					break;
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