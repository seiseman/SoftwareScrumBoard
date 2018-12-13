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

/**
 * 
 * @author Alex Ayala, Steven Eisemann, Nathan Rao, Nick Rocco
 * Class used for running the server
 *
 */

public class Server {
	private ServerSocket ss;
	private int clientID = 0;
	private ArrayList<LinkedBlockingQueue<String>> q;
	private LinkedBlockingQueue<String> log;
	private HashMap<String, String> owners;
	private ReentrantLock queuesLock;
	private ReentrantLock ownersLock;
	private ReentrantLock idLock;
	private ReentrantLock logLock;
	private BufferedReader in;
	public static int id = 1;

	Server() {
		// Set up input stream to load log with saved data
		try {
			in = new BufferedReader(new FileReader("scrum/persistance.txt"));
		} catch (Exception e) {
			System.out.println("Failed to create streams");
		}

		// Initialize locks, owners, and queues of update messages
		queuesLock = new ReentrantLock();
		ownersLock = new ReentrantLock();
		idLock = new ReentrantLock();
		logLock = new ReentrantLock();
		log = new LinkedBlockingQueue<String>();
		q = new  ArrayList<LinkedBlockingQueue<String>>();
		owners = new HashMap<String,String>();

		// Load the log with the contents of the input file
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

	// Start server
	public static void main(String[] args) {
		Server s = new Server();
	}

	// Load the log with the contents of the input file
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
		
		// Finds the highest component id so there's no id reuse
		int greatestId = 1;
		for (String s : log) {
			String[] parsed = s.split("##");
			if(Integer.parseInt(parsed[1]) == greatestId)
			{
				greatestId++;
			}
		}
		id = greatestId;
	}
}