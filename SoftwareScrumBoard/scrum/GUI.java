package scrum;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

/**
 * 
 * @author Alex Ayala, Steven Eisemann, Nathan Rao, Nick Rocco
 * Class used for maintaining user interaction. Also starts client application
 *
 */

public class GUI extends Application{

	static double xoff = 50.0;
	static double yoff = 37.5;
	static ScrumClientGateway gateway;
	ReentrantLock listLock;

	static Random randomGenerator = new Random();
	Pane root;
	Stage stage;
	Scene scene;
	int width = 1000;
	int height = 800;
	int scale = 25;
	static ObservableList<Node> ObservableList;
	static UserStory selected;

	//Information for different tabs and changing scenes
	Button createStory, productBacklog, sprintBacklog, burndown, viewDetails;
	Scene createScene, productScene, sprintScene, burndownScene, detailsScene;
	Pane createPane, productPane, sprintPane, burndownPane, detailsPane;

	//Create story information
	Button saveStory;

	//Details buttons
	Button deleteStory;

	ArrayList<UserStory> stories;

	Rectangle[] GCSStatusArray;

	public static void main(String[] args) {
		launch(args);
	}
	
	// Set up handlers for the new text field (dragBox)
	public static void setUpTextField(TextField dragBox, UserStory newStory) {
		
		// Set up moving text box when dragged
		dragBox.setOnMouseDragged(e -> {
			if(newStory.getIsOwner()) {
				dragBox.setLayoutX(e.getSceneX() - xoff);
				dragBox.setLayoutY(e.getSceneY() - yoff);
			}
		});

		// Sends update upon releasing object. Updates story object with changes accordingly.
		dragBox.setOnMouseReleased(e -> {
			if(newStory.getIsOwner()) {
				dragBox.setLayoutX(e.getSceneX() - xoff);
				dragBox.setLayoutX(e.getSceneX() - yoff);
				double x = dragBox.getLayoutX();
				double y = dragBox.getLayoutY();
				if(x >= 0 && x < 206) {
					newStory.setStatus("Stories");
					newStory.setCompletionDay(30);
				}
				else if(x >= 206 && x < 402) {
					newStory.setStatus("To Do");
					newStory.setCompletionDay(30);
				}
				else if(x >= 402 && x < 598) {
					newStory.setStatus("In Progress");
					newStory.setCompletionDay(30);
				}
				else if (x >= 598 && x < 794) {
					newStory.setStatus("Testing");
					newStory.setCompletionDay(30);
				}
				else {
					int low = 1;
					int high = 30;
					newStory.setStatus("Done");
					newStory.setCompletionDay(randomGenerator.nextInt(high-low) + low);
				}
				String comment = "Update##" + newStory.toString();
				gateway.sendUpdate(comment);
				newStory.setIsOwner(false);
			}
		});

		// On mouse down, try getting permission from server to edit the story
		dragBox.setOnMousePressed(e -> {
			if(gateway.editRequest(newStory.getId()).compareTo("true") == 0) {
				selected = newStory;
				newStory.setIsOwner(true);
			}
		});
		ObservableList.add(newStory.getTextField());
	}

	// Initialize GUI
	@Override
	public void start(Stage primaryStage) throws Exception {
		listLock = new ReentrantLock();
		gateway = new ScrumClientGateway();
		stories = new ArrayList<UserStory>();
		root = new AnchorPane();
		ObservableList = root.getChildren();

		productPane = new AnchorPane();
		productScene = new Scene(productPane, width/2, height/2);
		createPane = new AnchorPane();
		createScene = new Scene(createPane, 300, 185);
		sprintPane = new AnchorPane();
		sprintScene = new Scene(sprintPane, width/2, height/2);
		burndownPane = new AnchorPane();
		burndownScene = new Scene(burndownPane, width/2, height/2);
		detailsPane = new AnchorPane();
		detailsScene = new Scene(detailsPane, width/2, height/2);

		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);

		// Set up product backlog button
		productBacklog = new Button("Product Backlog");
		productBacklog.setLayoutX(800);
		productBacklog.setLayoutY(0);
		productBacklog.setPrefWidth(200);
		productBacklog.setPrefHeight(24);
		root.getChildren().add(productBacklog);

		// Open product backlog window upon click. Populate table based on stories that aren't done
		productBacklog.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TableView<UserStory> table = new TableView<UserStory>();
				table.setEditable(true);
				TableColumn titleCol = new TableColumn("Title");
				titleCol.setMinWidth(100);
				titleCol.setCellValueFactory(new PropertyValueFactory<UserStory, String>("story"));
				ObservableList<UserStory> tableStories = FXCollections.observableArrayList();
				for (UserStory s: stories) {
					if (s.getStatus() != "Done") {
						tableStories.add(s);
					}
				}
				table.setItems(tableStories);
				table.getColumns().addAll(titleCol);
				productPane.getChildren().add(table);

				stage.setScene(productScene);
				stage.setTitle("Product Backlog");
				stage.showAndWait();
			}

		});

		// Set up sprint backlog button
		sprintBacklog = new Button("Sprint Backlog");
		sprintBacklog.setLayoutX(800);
		sprintBacklog.setLayoutY(25);
		sprintBacklog.setPrefWidth(200);
		sprintBacklog.setPrefHeight(24);
		root.getChildren().add(sprintBacklog);

		// Open sprint backlog window upon click. Populate table based on stories
		sprintBacklog.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TableView<UserStory> table = new TableView<UserStory>();
				table.setEditable(true);
				TableColumn titleCol = new TableColumn("Title");
				titleCol.setMinWidth(100);
				titleCol.setCellValueFactory(new PropertyValueFactory<UserStory, String>("story"));
				ObservableList<UserStory> tableStories = FXCollections.observableArrayList();
				for (UserStory s: stories) {
					tableStories.add(s);
				}
				table.setItems(tableStories);
				table.getColumns().addAll(titleCol);

				sprintPane.getChildren().add(table);
				stage.setScene(sprintScene);
				stage.setTitle("Sprint Backlog");
				stage.showAndWait();
			}
		});

		// Set up burndown chart button
		burndown = new Button("Burndown Chart");
		burndown.setLayoutX(800);
		burndown.setLayoutY(50);
		burndown.setPrefWidth(200);
		burndown.setPrefHeight(24);
		root.getChildren().add(burndown);

		// Open burndown window upon click. Populate graph based on completed stories
		burndown.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NumberAxis xAxis = new NumberAxis();
				NumberAxis yAxis = new NumberAxis();
				xAxis.setLabel("Days in Sprint");
				LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
				lineChart.setTitle("Project Burndown");
				XYChart.Series series = new XYChart.Series();
				series.setName("data");
				int totalCompDays = 0;
				int totalPoints = 0;
				for (UserStory u : stories) {
					totalPoints += u.points;
				}
				series.getData().add(new XYChart.Data(1, totalPoints));
				for (int x = 2; x<=30; x++) {
					boolean comp = false;
					for (UserStory u : stories) {
						if (u.completionDay == x) {
							if (totalPoints > 0) {
								totalPoints = totalPoints - u.points;
								comp = true;
							}
						}
					}
					if (comp) {
						series.getData().add(new XYChart.Data(x, totalPoints));
					}
				}
				Scene scene = new Scene(lineChart, 600, 400);
				lineChart.getData().add(series);
				stage.setScene(scene);
				stage.setTitle("Burndown Chart");
				stage.showAndWait();
			}
		});

		// Set up view/edit details button
		viewDetails = new Button("View/Edit Details");
		viewDetails.setLayoutX(800);
		viewDetails.setLayoutY(75);
		viewDetails.setPrefWidth(200);
		viewDetails.setPrefHeight(24);
		root.getChildren().add(viewDetails);

		// Open window to view/edit/delete story upon click if given permission by server
		viewDetails.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(selected != null) {
					if(gateway.editRequest(selected.getId()).compareTo("true") == 0) {
						saveStory = new Button("Save");
						saveStory.setLayoutX(200);
						saveStory.setLayoutY(10);
						saveStory.setPrefWidth(75);
						saveStory.setPrefHeight(25);
						detailsPane.getChildren().add(saveStory);
						
						deleteStory = new Button("Delete");
						deleteStory.setLayoutX(200);
						deleteStory.setLayoutY(45);
						deleteStory.setPrefWidth(75);
						deleteStory.setPrefHeight(25);
						detailsPane.getChildren().add(deleteStory);

						final TextField story = new TextField();
						story.setPromptText("Enter your story here");
						story.setLayoutX(10);
						story.setLayoutY(10);
						story.setPrefHeight(25);
						story.setText(selected.getStory());
						detailsPane.getChildren().add(story);

						final TextField description = new TextField();
						description.setPromptText("Enter your description here");
						description.setLayoutX(10);
						description.setLayoutY(45);
						description.setPrefHeight(25);
						description.setText(selected.getDescription());
						detailsPane.getChildren().add(description);

						final TextField points = new TextField();
						points.setPromptText("Enter the points here");
						points.setLayoutX(10);
						points.setLayoutY(80);
						points.setPrefHeight(25);
						points.setText(String.valueOf(selected.getPoints()));
						detailsPane.getChildren().add(points);

						final TextField assignee = new TextField();
						assignee.setPromptText("Enter the assignee here");
						assignee.setLayoutX(10);
						assignee.setLayoutY(115);
						assignee.setPrefHeight(25);
						assignee.setText(selected.getAssignee());
						detailsPane.getChildren().add(assignee);

						final TextField comments = new TextField();
						comments.setPromptText("Enter comments here");
						comments.setLayoutX(10);
						comments.setLayoutY(150);
						comments.setPrefHeight(25);
						comments.setText(selected.getComments());
						detailsPane.getChildren().add(comments);

						// Upon click, save the story w/ new edits and send an update message to server
						saveStory.setOnAction(d-> {
							TextField dragBox = new TextField();
							int pointsInt;
							try {
								pointsInt = Integer.parseInt(points.getText());
							} catch(NumberFormatException e) {
								pointsInt = 1;
							}
							selected.setDescription(description.getText());
							selected.setStory(story.getText());
							selected.setComments(comments.getText());
							selected.setPoints(pointsInt);
							selected.setAssignee(assignee.getText());
							selected.updateTextField();
							String comment = "Update##" + selected.toString();
							
							gateway.sendUpdate(comment);
							selected.setIsOwner(false);
							selected = null;
							stage.close();
						});
						
						// Upon click, delete the story and send an update message to server
						deleteStory.setOnAction(d-> {
							String comment = "Delete##" + selected.toString();
							selected.getTextField().setVisible(false);
							gateway.sendUpdate(comment);
							selected.setIsOwner(false);
							selected = null;
							stage.close();
						});

						stage.setScene(detailsScene);
						stage.setTitle("User Story Details");
						stage.showAndWait();

					}
				}
			}
		});

		final TextField scrumTitle = new TextField();
		scrumTitle.setText("Scrum Board");
		scrumTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
		scrumTitle.setPrefWidth(680);
		scrumTitle.setPrefHeight(80);
		scrumTitle.setLayoutX(10);
		scrumTitle.setLayoutY(10);
		scrumTitle.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		scrumTitle.setEditable(false);
		root.getChildren().add(scrumTitle);

		// Set up create story button
		createStory = new Button("Create Story");
		createStory.setLayoutX(690);
		createStory.setLayoutY(10);
		createStory.setPrefWidth(100);
		createStory.setPrefHeight(80);
		root.getChildren().add(createStory);

		// Upon click, open new story window
		createStory.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				saveStory = new Button("Save");
				saveStory.setLayoutX(200);
				saveStory.setLayoutY(10);
				saveStory.setPrefWidth(75);
				saveStory.setPrefHeight(25);
				createPane.getChildren().add(saveStory);

				final TextField story = new TextField();
				story.setPromptText("Enter your story here");
				story.setLayoutX(10);
				story.setLayoutY(10);
				story.setPrefHeight(25);
				createPane.getChildren().add(story);

				final TextField description = new TextField();
				description.setPromptText("Enter your description here");
				description.setLayoutX(10);
				description.setLayoutY(45);
				description.setPrefHeight(25);
				createPane.getChildren().add(description);

				final TextField points = new TextField();
				points.setPromptText("Enter the points here");
				points.setLayoutX(10);
				points.setLayoutY(80);
				points.setPrefHeight(25);
				createPane.getChildren().add(points);

				final TextField assignee = new TextField();
				assignee.setPromptText("Enter the assignee here");
				assignee.setLayoutX(10);
				assignee.setLayoutY(115);
				assignee.setPrefHeight(25);
				createPane.getChildren().add(assignee);

				final TextField comments = new TextField();
				comments.setPromptText("Enter comments here");
				comments.setLayoutX(10);
				comments.setLayoutY(150);
				comments.setPrefHeight(25);
				createPane.getChildren().add(comments);

				// Upon click, create new story and set handlers accordingly. Send update message to server
				saveStory.setOnAction(d-> {
					TextField dragBox = new TextField();
					int pointsInt;
					try {
						pointsInt = Integer.parseInt(points.getText());
					} catch(NumberFormatException e) {
						pointsInt = 1;
					}
					UserStory newStory = new UserStory(description.getText(), story.getText(), "Stories", comments.getText(),
							pointsInt, assignee.getText(), dragBox, 30);
					dragBox.setEditable(false);
					dragBox.setFont(Font.font("Verdana", 20));
					dragBox.setPrefWidth(100);
					dragBox.setPrefHeight(75);
					dragBox.setStyle("-fx-background-color: white;-fx-border-color:black;");
					dragBox.setLayoutX(20);
					dragBox.setLayoutY(180);

					// Make story move if dragged and owner
					dragBox.setOnMouseDragged(e -> {
						if(newStory.getIsOwner()) {
							dragBox.setLayoutX(e.getSceneX() - xoff);
							dragBox.setLayoutY(e.getSceneY() - yoff);
						}
					});

					// Send update message to server when mouse released of new position information
					dragBox.setOnMouseReleased(e -> {
						if(newStory.getIsOwner()) {
							dragBox.setLayoutX(e.getSceneX() - xoff);
							dragBox.setLayoutX(e.getSceneX() - yoff);
							double x = dragBox.getLayoutX();
							double y = dragBox.getLayoutY();
							if(x >= 0 && x < 206) {
								newStory.setStatus("Stories");
								newStory.setCompletionDay(30);
							}
							else if(x >= 206 && x < 402) {
								newStory.setStatus("To Do");
								newStory.setCompletionDay(30);
							}
							else if(x >= 402 && x < 598) {
								newStory.setStatus("In Progress");
								newStory.setCompletionDay(30);
							}
							else if (x >= 598 && x < 794) {
								newStory.setStatus("Testing");
								newStory.setCompletionDay(30);
							}
							else {
								int low = 1;
								int high = 30;
								newStory.setStatus("Done");
								newStory.setCompletionDay(randomGenerator.nextInt(high-low) + low);
							}
							String comment = "Update##" + newStory.toString();
							gateway.sendUpdate(comment);
							newStory.setIsOwner(false);
						}
					});

					// On mouse pressed, try gaining permission from server to edit
					dragBox.setOnMousePressed(e -> {
						if(gateway.editRequest(newStory.getId()).compareTo("true") == 0) {
							selected = newStory;
							newStory.setIsOwner(true);
						}
					});
					listLock.lock();
					ObservableList.add(newStory.getTextField());
					String comment = "New##" + newStory.toString();
					newStory.setId(gateway.createObject(comment));
					stories.add(newStory);
					listLock.unlock();
					stage.close();
				});

				stage.setScene(createScene);
				stage.setTitle("Create New User Story");
				stage.showAndWait();

			}
		});

		// Set up story lanes
		final TextField storiesTitle = new TextField();
		storiesTitle.setText("Stories");
		storiesTitle.setFont(Font.font("Verdana", 20));
		storiesTitle.setPrefWidth(196);
		storiesTitle.setLayoutX(10);
		storiesTitle.setLayoutY(120);
		storiesTitle.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		root.getChildren().add(storiesTitle);

		final TextField storiesEdit = new TextField();
		storiesEdit.setFont(Font.font("Verdana", 20));
		storiesEdit.setPrefWidth(196);
		storiesEdit.setPrefHeight(630);
		storiesEdit.setLayoutX(10);
		storiesEdit.setLayoutY(160);
		storiesEdit.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
		storiesEdit.setEditable(false);
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
		toDoEdit.setEditable(false);
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
		inProgressEdit.setEditable(false);
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
		testingEdit.setEditable(false);
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
		doneEdit.setEditable(false);
		root.getChildren().add(doneEdit);

		scene = new Scene(root, width, height);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Scrum Board");
		primaryStage.show();

		// Start thread to start receiving updates from server
		new Thread(new UpdateReceiver(gateway, stories, listLock)).start();
	}

}
