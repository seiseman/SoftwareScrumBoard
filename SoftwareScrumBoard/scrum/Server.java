package scrum;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Server {
	private ServerSocket ss;
	private int clientID = 0;

	Server() {
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
	          // Create and start a new thread for the connection
	          new Thread(new HandleAClient(socket,clientID)).start();
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

class HandleAClient implements Runnable {
    private Socket socket; // A connected socket
    //private Transcript transcript; // Reference to shared transcript
    //private TextArea textArea;
    private String handle;
    private int clientID;

    public HandleAClient(Socket socket, int clientID) {
      this.socket = socket;
      this.clientID = clientID;
      //this.transcript = transcript;
      //this.textArea = textArea;
    }

    public void run() {
    	try {
            // Create reading and writing streams
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());

            // Continuously serve the client
            while (true) {
            	System.out.println(inputFromClient.readLine());
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