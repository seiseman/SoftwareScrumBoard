package scrum;

import java.util.concurrent.locks.ReentrantLock;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ScrumClientGateway implements ScrumChatConstants {

    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private ReentrantLock inputLock;
    private ReentrantLock outputLock;

    // Establish the connection to the server.
    public ScrumClientGateway() {
    	inputLock = new ReentrantLock();
    	outputLock = new ReentrantLock();
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an output stream to send data to the server
            outputToServer = new PrintWriter(socket.getOutputStream());

            // Create an input stream to read data from the server
            inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException ex) {
            Platform.runLater(() -> System.out.println("Exception in gateway constructor: " + ex.toString() + "\n"));
        }
    }

    // Send a new comment to the server.
    public void sendUpdate(String comment) {
    	outputLock.lock();
        outputToServer.println(SEND_UPDATE);
        outputToServer.println(comment);
        System.out.println("Send Update: " + comment);
        outputToServer.flush();
        outputLock.unlock();
    }

    public String editRequest(String comment) {
    	outputLock.lock();
        outputToServer.println(SEND_EDIT_REQUEST);
        outputToServer.println(comment);
        outputToServer.flush();
        outputToServer.println(GET_EDIT_REQUEST);
        outputToServer.println(comment);
        System.out.println("Send Request: " + comment);
        outputToServer.flush();
        String update = "false";
        inputLock.lock();
        try {
            update = inputFromServer.readLine();
            System.out.println("Edit request: " + update);
        } catch (IOException ex) {
            Platform.runLater(() -> System.out.println("Error in getComment: " + ex.toString() + "\n"));
        }
        inputLock.unlock();
        outputLock.unlock();
        return update;
    }
    
    // Fetch comment n of the transcript from the server.
    public ArrayList<String> getUpdates() {
    	outputLock.lock();
        outputToServer.println(GET_UPDATE);
        outputToServer.flush();
        String update;
        ArrayList<String> updates = new ArrayList<String>();
    	inputLock.lock();
        try {
            update = inputFromServer.readLine();
            if(update.isEmpty() == false) {
                System.out.println("Get Update: " + update);
            	updates.add(update);
            }
        } catch (IOException ex) {
            Platform.runLater(() -> System.out.println("Error in getComment: " + ex.toString() + "\n"));
        }
        inputLock.unlock();
        outputLock.unlock();
        return updates;
    }
}

