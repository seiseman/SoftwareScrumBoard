package scrum;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;

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
	Button editStory, deleteStory;

	ArrayList<UserStory> stories;

	Rectangle[] GCSStatusArray;

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void setUpTextField(TextField dragBox, UserStory newStory) {
		dragBox.setOnMouseDragged(e -> {
			if(newStory.getIsOwner()) {
				dragBox.setLayoutX(e.getSceneX() - xoff);
				dragBox.setLayoutY(e.getSceneY() - yoff);
			}
		});

		dragBox.setOnMouseReleased(e -> {
			if(newStory.getIsOwner()) {
				dragBox.setLayoutX(e.getSceneX() - xoff);
				dragBox.setLayoutX(e.getSceneX() - yoff);
				double x = dragBox.getLayoutX();
				double y = dragBox.getLayoutY();
				if(x >= 0 && x < 206) {
					newStory.setStatus("Stories");
				}
				else if(x >= 206 && x < 402) {
					newStory.setStatus("To Do");
				}
				else if(x >= 402 && x < 598) {
					newStory.setStatus("In Progress");
				}
				else if (x >= 598 && x < 794) {
					newStory.setStatus("Testing");
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

		dragBox.setOnMouseClicked(e -> {
			selected = newStory;
			if(gateway.editRequest(newStory.getId()).compareTo("true") == 0) {
				newStory.setIsOwner(true);
			}
		});
		ObservableList.add(newStory.getTextField());
	}

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

		productBacklog = new Button("Product Backlog");
		productBacklog.setLayoutX(800);
		productBacklog.setLayoutY(0);
		productBacklog.setPrefWidth(200);
		productBacklog.setPrefHeight(24);
		root.getChildren().add(productBacklog);

		productBacklog.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
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

		sprintBacklog = new Button("Sprint Backlog");
		sprintBacklog.setLayoutX(800);
		sprintBacklog.setLayoutY(25);
		sprintBacklog.setPrefWidth(200);
		sprintBacklog.setPrefHeight(24);
		root.getChildren().add(sprintBacklog);

		sprintBacklog.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
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

		burndown = new Button("Burndown Chart");
		burndown.setLayoutX(800);
		burndown.setLayoutY(50);
		burndown.setPrefWidth(200);
		burndown.setPrefHeight(24);
		root.getChildren().add(burndown);

		burndown.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
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
					//series.getData().add(new XYChart.Data(u.completionDay, u.points));
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

		viewDetails = new Button("View/Edit Details");
		viewDetails.setLayoutX(800);
		viewDetails.setLayoutY(75);
		viewDetails.setPrefWidth(200);
		viewDetails.setPrefHeight(24);
		root.getChildren().add(viewDetails);

		viewDetails.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(selected != null) {
					if(gateway.editRequest(selected.getId()).compareTo("true") == 0) {
						System.out.println("hey");
						saveStory = new Button("Save");
						saveStory.setLayoutX(200);
						saveStory.setLayoutY(10);
						saveStory.setPrefWidth(75);
						saveStory.setPrefHeight(25);
						detailsPane.getChildren().add(saveStory);

						final TextField story = new TextField();
						story.setPromptText("Enter your story here");
						//story.getText();
						story.setLayoutX(10);
						story.setLayoutY(10);
						story.setPrefHeight(25);
						story.setText(selected.getStory());
						detailsPane.getChildren().add(story);

						final TextField description = new TextField();
						description.setPromptText("Enter your description here");
						//description.getText();
						description.setLayoutX(10);
						description.setLayoutY(45);
						description.setPrefHeight(25);
						description.setText(selected.getDescription());
						detailsPane.getChildren().add(description);

						final TextField points = new TextField();
						points.setPromptText("Enter the points here");
						//points.getText();
						points.setLayoutX(10);
						points.setLayoutY(80);
						points.setPrefHeight(25);
						points.setText(String.valueOf(selected.getPoints()));
						detailsPane.getChildren().add(points);

						final TextField assignee = new TextField();
						assignee.setPromptText("Enter the assignee here");
						//assignee.getText();
						assignee.setLayoutX(10);
						assignee.setLayoutY(115);
						assignee.setPrefHeight(25);
						assignee.setText(selected.getAssignee());
						detailsPane.getChildren().add(assignee);

						final TextField comments = new TextField();
						comments.setPromptText("Enter comments here");
						//comments.getText();
						comments.setLayoutX(10);
						comments.setLayoutY(150);
						comments.setPrefHeight(25);
						comments.setText(selected.getComments());
						detailsPane.getChildren().add(comments);

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
							selected.setPoints(Integer.parseInt(points.getText()));
							selected.setAssignee(assignee.getText());
							selected.updateTextField();
							String comment = "Update##" + selected.toString();
							
							/*		selected.getId() + "##"
														+ description.getText() + "##"
														+ story.getText() + "##"
														+ selected.getStatus() + "##"
														+ comments.getText() + "##"
														+ points.getText() + "##"
														+ assignee.getText() + "##"
														+ selected.getCompletionDay();		*/											;
							gateway.sendUpdate(comment);
							selected.setIsOwner(false);
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

		createStory = new Button("Create Story");
		createStory.setLayoutX(690);
		createStory.setLayoutY(10);
		createStory.setPrefWidth(100);
		createStory.setPrefHeight(80);
		root.getChildren().add(createStory);

		createStory.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				saveStory = new Button("Save");
				saveStory.setLayoutX(200);
				saveStory.setLayoutY(10);
				saveStory.setPrefWidth(75);
				saveStory.setPrefHeight(25);
				createPane.getChildren().add(saveStory);

				final TextField story = new TextField();
				story.setPromptText("Enter your story here");
				//story.getText();
				story.setLayoutX(10);
				story.setLayoutY(10);
				story.setPrefHeight(25);
				createPane.getChildren().add(story);

				final TextField description = new TextField();
				description.setPromptText("Enter your description here");
				//description.getText();
				description.setLayoutX(10);
				description.setLayoutY(45);
				description.setPrefHeight(25);
				createPane.getChildren().add(description);

				final TextField points = new TextField();
				points.setPromptText("Enter the points here");
				//points.getText();
				points.setLayoutX(10);
				points.setLayoutY(80);
				points.setPrefHeight(25);
				createPane.getChildren().add(points);

				final TextField assignee = new TextField();
				assignee.setPromptText("Enter the assignee here");
				//assignee.getText();
				assignee.setLayoutX(10);
				assignee.setLayoutY(115);
				assignee.setPrefHeight(25);
				createPane.getChildren().add(assignee);

				final TextField comments = new TextField();
				comments.setPromptText("Enter comments here");
				//comments.getText();
				comments.setLayoutX(10);
				comments.setLayoutY(150);
				comments.setPrefHeight(25);
				createPane.getChildren().add(comments);

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

					dragBox.setOnMouseDragged(e -> {
						if(newStory.getIsOwner()) {
							dragBox.setLayoutX(e.getSceneX() - xoff);
							dragBox.setLayoutY(e.getSceneY() - yoff);
						}
					});

					dragBox.setOnMouseReleased(e -> {
						if(newStory.getIsOwner()) {
							dragBox.setLayoutX(e.getSceneX() - xoff);
							dragBox.setLayoutX(e.getSceneX() - yoff);
							double x = dragBox.getLayoutX();
							double y = dragBox.getLayoutY();
							if(x >= 0 && x < 206) {
								newStory.setStatus("Stories");
							}
							else if(x >= 206 && x < 402) {
								newStory.setStatus("To Do");
							}
							else if(x >= 402 && x < 598) {
								newStory.setStatus("In Progress");
							}
							else if (x >= 598 && x < 794) {
								newStory.setStatus("Testing");
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

					dragBox.setOnMouseClicked(e -> {
						selected = newStory;
						if(gateway.editRequest(newStory.getId()).compareTo("true") == 0) {
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
		primaryStage.setScene(scene);
		primaryStage.setTitle("Scrum Board");
		primaryStage.show();

		new Thread(new UpdateReceiver(gateway, stories, ObservableList, listLock)).start();
	}

}
