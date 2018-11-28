import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class GUI extends Application{

	Pane root;
	Scene scene;
	int width = 1000;
	int height = 800;
	int scale = 25;
	int secondsDown;
	ObservableList<Node> ObservableList;

	Button userStories;
	Button productBacklog;
	Button sprintBacklog;
	Button scrumBoard;
	Button burndown;
	boolean test = false;

	Rectangle[] GCSStatusArray;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Stage stage = primaryStage;
		root = new AnchorPane();
		ObservableList = root.getChildren();

		scrumBoard = new Button("Scrum Board");
		scrumBoard.setLayoutX(600);
		scrumBoard.setLayoutY(0);
		scrumBoard.setPrefWidth(200);
		root.getChildren().add(scrumBoard);
		
		scrumBoard.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				test = true;
			}
		});
		
		final TextField scrum = new TextField();
		scrum.setText("Scrum Board");
		scrum.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		scrum.setPrefWidth(980);
		scrum.setLayoutX(10);
		scrum.setLayoutY(40);
		scrum.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		//statusBar.setEditable(false);
		root.getChildren().add(scrum);
		
		userStories = new Button("User Stories");
		userStories.setLayoutX(0);
		userStories.setLayoutY(0);
		userStories.setPrefWidth(200);
		root.getChildren().add(userStories);

		userStories.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				test = true;
			}

		});

		productBacklog = new Button("Product Backlog");
		productBacklog.setLayoutX(200);
		productBacklog.setLayoutY(0);
		productBacklog.setPrefWidth(200);
		root.getChildren().add(productBacklog);
		
		productBacklog.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				test = true;
			}

		});
		
		sprintBacklog = new Button("Sprint Backlog");
		sprintBacklog.setLayoutX(400);
		sprintBacklog.setLayoutY(0);
		sprintBacklog.setPrefWidth(200);
		root.getChildren().add(sprintBacklog);
		
		sprintBacklog.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				test = true;
			}
		});
		
		burndown = new Button("Burndown Chart");
		burndown.setLayoutX(800);
		burndown.setLayoutY(0);
		burndown.setPrefWidth(200);
		root.getChildren().add(burndown);
		
		burndown.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				test = true;
			}
		});

		/*
		GCSStatusArray = new Rectangle[2];
		GCSStatusArray[0] = new Rectangle(85,30);
		GCSStatusArray[0].setFill(Color.SPRINGGREEN);
		GCSStatusArray[0].setLayoutX(500);
		GCSStatusArray[0].setLayoutY(80);
		root.getChildren().add(GCSStatusArray[0]);
		GCSStatusArray[1] = new Rectangle(85,30);
		GCSStatusArray[1].setFill(Color.SPRINGGREEN);
		GCSStatusArray[1].setLayoutX(500);
		GCSStatusArray[1].setLayoutY(115);
		root.getChildren().add(GCSStatusArray[1]);
		*/

		scene = new Scene(root, width, height);
		stage.setScene(scene);
		stage.setTitle("NNX Drone Simulation");
		stage.show();

		new AnimationTimer() {
			int count = 0;
			@Override
			public void handle(long now) {

			}
		}.start();

		new AnimationTimer() {
			int count = 0;
			@Override
			public void handle(long now) {

			}
		}.start();

		new AnimationTimer() {
			int count = 0;
			@Override
			public void handle(long now) {
				// TODO Auto-generated method stub

			}

		}.start();

		new AnimationTimer() {
			int count = 0;
			@Override
			public void handle(long now) {
				// TODO Auto-generated method stub
			}

		}.start();
	}

}
