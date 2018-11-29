package scrum;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class ScrumController implements Initializable {

	@FXML
	private TextField toDoEdit;
	
	private ScrumClientGateway gateway;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
        gateway = new ScrumClientGateway();

		toDoEdit.setOnMouseClicked(e-> {
			if(gateway.editRequest("id1").compareTo("true") == 0) {
				toDoEdit.setEditable(true);
				toDoEdit.requestFocus();
			}
        });
		/*toDoEdit.setOnAction(e-> {
			// TODO 1 is id. we have to establish ids later too
			String comment = "id1 " + toDoEdit.getText();
			gateway.sendUpdate(comment);
			toDoEdit.setEditable(false);
        });*/
        
		toDoEdit.focusedProperty().addListener((ov, oldV, newV) -> {
			if (!newV) { // focus lost
				String comment = "id1 " + toDoEdit.getText();
				gateway.sendUpdate(comment);
				toDoEdit.setEditable(false);
			}
		});
		
        // Start the transcript check thread
        new Thread(new UpdateReceiver(gateway, toDoEdit)).start();
	}
	
	public void helloWorld () {
		System.out.println("hello world");
	}

}

class UpdateReceiver implements Runnable, ScrumChatConstants {
    private ScrumClientGateway gateway; // Gateway to the server
    private TextField text;
    
    /** Construct a thread */
    public UpdateReceiver(ScrumClientGateway gateway,TextField text) {
      this.gateway = gateway;
      this.text = text;
    }

    /** Run a thread */
    public void run() {
      ArrayList<String> updates;
      while(true) {
    	  updates = gateway.getUpdates();
          if(updates.size() > 0) {
        	  /* This will need to change to be more general */
        	  String newComment = updates.remove(0);
        	  if(newComment.compareTo("END") != 0) {
        		  Platform.runLater(()->text.setText(newComment.split(" ")[1]));
        	  }
          } else {
              try {
                  Thread.sleep(250);
              } catch(InterruptedException ex) {}
          }
      }
    }
  }
