package scrum;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Server {
	private ServerSocket ss;
	private int clientID = 0;
	private LinkedBlockingQueue<UpdateMessage> q;
	private LinkedBlockingQueue<String> fakeQ; //this is only here so we dont need to update messages yet, we'll just use strings

	Server() {
		q = new LinkedBlockingQueue<>();
		fakeQ = new LinkedBlockingQueue<>();
		new Thread( () -> {
		try {
	        // Create a server socket
	        ss = new ServerSocket(8000);

	        while (true) {
	          // Listen for a new connection request
	          Socket socket = ss.accept();
	          // Increment clientNo
	          clientID++;

/*	          Platform.runLater( () -> {
	            // Display the client number
	            textArea.appendText("Starting thread for client " + clientNo +
	              " at " + new Date() + '\n');
	            });
*/
	          // Create and start a new thread for listening to the connection
	          new Thread(new ClientListen(socket,clientID, fakeQ)).start();

	        //Create and start a new thread for writing to the connection
	          new Thread(new ClientWriter(socket, clientID, fakeQ)).start();

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

class ClientWriter implements Runnable {
	private Socket socket;
	private int clientID;
	LinkedBlockingQueue<String> q;

	public ClientWriter(Socket socket, int clientID, LinkedBlockingQueue<String> queue) {
		q = queue;
    	this.socket = socket;
    	this.clientID = clientID;
	}

	public void run() {
		try {
			PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());
			while(true) {
				try {
					int message = Integer.parseInt(q.poll()); //This needs to be an UpdateMessage or some other custom type
					switch(message) {
						case ScrumChatConstants.SEND_EDIT_REQUEST: {
							//should this actually be something server looks for?
						}
						case ScrumChatConstants.SEND_UPDATE: {
							//format a message to update a client about what has changed
						}
						case ScrumChatConstants.GET_EDIT_REQUEST: {
							//pop another message from the queue
							//this message should be the object to be edited
						}
						case ScrumChatConstants.GET_UPDATE: {
							//should the server care about this?
						}
					}

				} catch (NumberFormatException e) {

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

class ClientListen implements Runnable {
    private Socket socket; // A connected socket
    //private Transcript transcript; // Reference to shared transcript
    //private TextArea textArea;
    //private String handle;
    private int clientID;
    LinkedBlockingQueue<String> q;

    public ClientListen(Socket socket, int clientID, LinkedBlockingQueue<String> queue) {
    	q = queue;
    	this.socket = socket;
    	this.clientID = clientID;
    }

    public void run() {
    	try {
            // Create reading and writing streams
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());

            // Continuously serve the client
            while (true) {
            	String message = inputFromClient.readLine();
            	q.add(message);
            	System.out.println();
            }
    	}
    	catch(IOException ex) {
            //Platform.runLater(()->textArea.appendText("Exception in client thread: "+ex.toString()+"\n"));
        }
        try {
        	socket.close();
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
    }
}