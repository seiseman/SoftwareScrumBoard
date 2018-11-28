package scrum;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScrumApplication extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		 	Parent parent = FXMLLoader.load(getClass().getResource("ScrumBoard.fxml"));
	        primaryStage.setTitle("Scrum Board Application");
	        Scene scene = new Scene(parent,1000,800);
	        primaryStage.setScene(scene);
	        primaryStage.show();		
	}
	
    public static void main(String[] args) {
        launch(args);
    }

}
