package scrum;

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
		
		final TextField stories = new TextField();
		stories.setText("Stories");
		stories.setFont(Font.font("Verdana", 20));
		stories.setPrefWidth(196);
		stories.setLayoutX(10);
		stories.setLayoutY(120);
		stories.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		root.getChildren().add(stories);
		
		final TextField storiesEdit = new TextField();
		storiesEdit.setFont(Font.font("Verdana", 20));
		storiesEdit.setPrefWidth(196);
		storiesEdit.setPrefHeight(630);
		storiesEdit.setLayoutX(10);
		storiesEdit.setLayoutY(160);
		storiesEdit.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		storiesEdit.setEditable(true);
		root.getChildren().add(storiesEdit);
		
		final TextField toDo = new TextField();
		toDo.setText("To Do");
		toDo.setFont(Font.font("Verdana", 20));
		toDo.setPrefWidth(196);
		toDo.setLayoutX(206);
		toDo.setLayoutY(120);
		toDo.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		root.getChildren().add(toDo);
		
		final TextField toDoEdit = new TextField();
		toDoEdit.setFont(Font.font("Verdana", 20));
		toDoEdit.setPrefWidth(196);
		toDoEdit.setPrefHeight(630);
		toDoEdit.setLayoutX(206);
		toDoEdit.setLayoutY(160);
		toDoEdit.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		toDoEdit.setEditable(true);
		root.getChildren().add(toDoEdit);
		
		final TextField inProgress = new TextField();
		inProgress.setText("In Progress");
		inProgress.setFont(Font.font("Verdana", 20));
		inProgress.setPrefWidth(196);
		inProgress.setLayoutX(402);
		inProgress.setLayoutY(120);
		inProgress.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		root.getChildren().add(inProgress);
		
		final TextField inProgressEdit = new TextField();
		inProgressEdit.setFont(Font.font("Verdana", 20));
		inProgressEdit.setPrefWidth(196);
		inProgressEdit.setPrefHeight(630);
		inProgressEdit.setLayoutX(402);
		inProgressEdit.setLayoutY(160);
		inProgressEdit.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		inProgressEdit.setEditable(true);
		root.getChildren().add(inProgressEdit);
		
		final TextField testing = new TextField();
		testing.setText("Testing");
		testing.setFont(Font.font("Verdana", 20));
		testing.setPrefWidth(196);
		testing.setLayoutX(598);
		testing.setLayoutY(120);
		testing.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		root.getChildren().add(testing);
		
		final TextField testingEdit = new TextField();
		testingEdit.setFont(Font.font("Verdana", 20));
		testingEdit.setPrefWidth(196);
		testingEdit.setPrefHeight(630);
		testingEdit.setLayoutX(598);
		testingEdit.setLayoutY(160);
		testingEdit.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		testingEdit.setEditable(true);
		root.getChildren().add(testingEdit);
		
		final TextField done = new TextField();
		done.setText("Done");
		done.setFont(Font.font("Verdana", 20));
		done.setPrefWidth(196);
		done.setLayoutX(794);
		done.setLayoutY(120);
		done.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		root.getChildren().add(done);
		
		final TextField doneEdit = new TextField();
		doneEdit.setFont(Font.font("Verdana", 20));
		doneEdit.setPrefWidth(196);
		doneEdit.setPrefHeight(630);
		doneEdit.setLayoutX(794);
		doneEdit.setLayoutY(160);
		doneEdit.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		doneEdit.setEditable(true);
		root.getChildren().add(doneEdit);

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