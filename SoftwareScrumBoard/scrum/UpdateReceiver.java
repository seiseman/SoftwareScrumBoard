package scrum;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Platform;

/**
 * 
 * @author Alex Ayala, Steven Eisemann, Nathan Rao, Nick Rocco
 * Class used to constantly request updates from server
 *
 */

public class UpdateReceiver implements Runnable, ScrumChatConstants {
    private ScrumClientGateway gateway; // Gateway to the server
    private ArrayList<UserStory> stories;
    private ReentrantLock listLock;
    
    /** Construct a thread */
    public UpdateReceiver(ScrumClientGateway gateway, ArrayList<UserStory> stories,
    					  ReentrantLock listLock) {
      this.gateway = gateway;
      this.stories = stories;
      this.listLock = listLock;
    }

    /** Run a thread */
    public void run() {
      ArrayList<String> updates;
      while(true) {
    	  // Retrieve updates from server
    	  updates = gateway.getUpdates();
    	  
    	  // If there are updates to consume
          if(updates.size() > 0) {
        	  String newComment = updates.remove(0);
        	  String identifier = newComment.split("##")[1];
        	  boolean isFound = false;
        	  UserStory toDelete = null;
        	  boolean willDelete = false;
        	  listLock.lock();
        	  // Find the updated story (if it's there) and update accordingly if it's found
        	  for(UserStory s: stories) {
        		  if(s.getId().compareTo(identifier) == 0) { 
        			  Platform.runLater(()->s.consumeUpdate(newComment));
        			  isFound = true;
        			  if(newComment.split("##")[0].compareTo("Delete") == 0) {
        				  toDelete = s;
        				  willDelete = true;
        			  }
        			  break;
        		  }
        	  }
        	  // Delete component if that's the update
        	  if(willDelete) {
        		  stories.remove(toDelete);
        		  toDelete.getTextField().setVisible(false);
        		  Platform.runLater(()-> GUI.selected = null);
        	  }
        	  // Create new object if it wasn't found in stories list
        	  else if(!isFound) {
        		  UserStory newStory = new UserStory(newComment);
        		  stories.add(newStory);
        		  Platform.runLater(()-> GUI.setUpTextField(newStory.getTextField(), newStory));
        	  }
        	  listLock.unlock();
          } else {
              try {
                  Thread.sleep(250);
              } catch(InterruptedException ex) {}
          }
      }
    }
  }
