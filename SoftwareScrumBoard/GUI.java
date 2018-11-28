package project.GUI;

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
	Grid grid;
	int secondsDown;
	ObservableList<Node> ObservableList;

	Rectangle[] GCSStatusArray;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Stage stage = primaryStage;
		root = new AnchorPane();
		grid = new Grid();
		ObservableList = root.getChildren();
		grid.drawGrid(ObservableList, scale);
		
		final TextField ChaosMonkey = new TextField();
		ChaosMonkey.setText("Chaos Monkey");
		ChaosMonkey.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		ChaosMonkey.setPrefWidth(375);
		ChaosMonkey.setLayoutX(10);
		ChaosMonkey.setLayoutY(10);
		ChaosMonkey.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		ChaosMonkey.setEditable(false);
		root.getChildren().add(ChaosMonkey);
	
		permanentGCS = new Button("permanent");
		temporaryGCS = new Button("temporary ");
		permanentGCS.setLayoutX(500);
		permanentGCS.setLayoutY(10);
		temporaryGCS.setLayoutX(500);
		temporaryGCS.setLayoutY(40);
		root.getChildren().add(permanentGCS);
		root.getChildren().add(temporaryGCS);
		
		permanentGCS.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				permanentStation = true;
				
			}
			
		});
		
		temporaryGCS.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				temporaryStation = true;
				Random rand = new Random();
				secondsDown = rand.nextInt(5)+1;
			}
			
		});
		
		
		
		final TextField statusBar = new TextField();
		statusBar.setText("Status");
		statusBar.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		statusBar.setPrefWidth(375);
		statusBar.setPrefHeight(65);
		statusBar.setLayoutX(10);
		statusBar.setLayoutY(80);
		statusBar.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		statusBar.setEditable(false);
		root.getChildren().add(statusBar);
		
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
