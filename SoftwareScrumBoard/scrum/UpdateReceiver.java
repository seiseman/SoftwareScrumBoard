package scrum;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;

class UpdateReceiver implements Runnable, ScrumChatConstants {
    private ScrumClientGateway gateway; // Gateway to the server
    private ArrayList<UserStory> stories;
    private ObservableList<Node> ObservableList;
    private ReentrantLock listLock;
    
    /** Construct a thread */
    public UpdateReceiver(ScrumClientGateway gateway, ArrayList<UserStory> stories, ObservableList<Node> ObservableList,
    					  ReentrantLock listLock) {
      this.gateway = gateway;
      this.stories = stories;
      this.ObservableList = ObservableList;
      this.listLock = listLock;
    }

    /** Run a thread */
    public void run() {
      ArrayList<String> updates;
      while(true) {
    	  updates = gateway.getUpdates();
          if(updates.size() > 0) {
        	  /* This will need to change to be more general */
        	  String newComment = updates.remove(0);
        	  String identifier = newComment.split(" ")[0];
        	  boolean isFound = false;
        	  listLock.lock();
        	  for(UserStory s: stories) {
        		  if(s.getId().compareTo(identifier) == 0) { 
        			  Platform.runLater(()->s.consumeUpdate(newComment));
        			  isFound = true;
        			  break;
        		  }
        	  }
        	  if(!isFound) {
        		  UserStory newStory = new UserStory(newComment);
        		  System.out.println("HEY DOG");
        		  stories.add(newStory);
        		  ObservableList.add(newStory.getTextField());
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
