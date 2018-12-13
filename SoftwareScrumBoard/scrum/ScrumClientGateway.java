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

/**
 * 
 * @author Alex Ayala, Steven Eisemann, Nathan Rao, Nick Rocco
 * Class used for establishing connection to server and sending and receiving messages to and from server
 *
 */

public class ScrumClientGateway {

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

    // Send a new update to the server
    public void sendUpdate(String comment) {
    	outputLock.lock();
        outputToServer.println(ScrumChatConstants.SEND_UPDATE);
        outputToServer.println(comment);
        outputToServer.flush();
        outputLock.unlock();
    }

    // Send component creation messages to server
    public String createObject(String comment) {
    	String identifier = "null";
    	outputLock.lock();
        outputToServer.println(ScrumChatConstants.SEND_UPDATE);
        outputToServer.println(comment);
        outputToServer.flush();
        outputToServer.println(ScrumChatConstants.GET_NEW_ID);
        outputToServer.flush();
        inputLock.lock();
        try {
            identifier = inputFromServer.readLine();
        } catch (IOException ex) {
            Platform.runLater(() -> System.out.println("Error in getComment: " + ex.toString() + "\n"));
        }
        inputLock.unlock();
        outputLock.unlock();
        return identifier;
    }

    // Sends an edit request on a component to the server
    public String editRequest(String comment) {
    	outputLock.lock();
        outputToServer.println(ScrumChatConstants.SEND_EDIT_REQUEST);
        outputToServer.println(comment);
        outputToServer.flush();
        outputToServer.println(ScrumChatConstants.GET_EDIT_REQUEST);
        outputToServer.println(comment);
        outputToServer.flush();
        String update = "false";
        inputLock.lock();
        try {
            update = inputFromServer.readLine();
        } catch (IOException ex) {
            Platform.runLater(() -> System.out.println("Error in getComment: " + ex.toString() + "\n"));
        }
        inputLock.unlock();
        outputLock.unlock();
        return update;
    }

    // Retrieve updates from server
    public ArrayList<String> getUpdates() {
    	outputLock.lock();
        outputToServer.println(ScrumChatConstants.GET_UPDATE);
        outputToServer.flush();
        String update;
        ArrayList<String> updates = new ArrayList<String>();
    	inputLock.lock();
        try {
            update = inputFromServer.readLine();
            if(update.isEmpty() == false) {
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

