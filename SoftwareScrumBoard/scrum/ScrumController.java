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
	
	// TODO. We want one for EVERY component
	private boolean id1Owner = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
        gateway = new ScrumClientGateway();

		toDoEdit.setOnMouseClicked(e-> {
			if(gateway.editRequest("id1").compareTo("true") == 0) {
				toDoEdit.setEditable(true);
				//toDoEdit.requestFocus();
				id1Owner = true;
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
				if(id1Owner) {
					String comment = "id1 " + toDoEdit.getText();
					gateway.sendUpdate(comment);
					toDoEdit.setEditable(false);
					id1Owner = false;
				}
			}
		});
		
        // Start the transcript check thread
        //new Thread(new UpdateReceiver(gateway, toDoEdit)).start();
	}
	
	public void helloWorld () {
		System.out.println("hello world");
	}
}