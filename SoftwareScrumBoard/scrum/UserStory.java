package scrum;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class UserStory {

	String description;
	String story;
	String status;
	String comments;
	int points;
	String assignee;
	int completionDay;
	TextField textBox;
	String id;
	boolean isOwner;

	public UserStory(String description, String story, String status,
			String comments, int points, String assignee, TextField textBox, int completionDay) {
		this.description = description;
		this.story = story;
		this.status = status;
		this.comments = comments;
		this.points = points;
		this.assignee = assignee;
		this.textBox = textBox;
		this.textBox.setText(this.story);
		this.textBox.setEditable(false);
		this.completionDay = completionDay;
		this.id = "null";
		this.isOwner = false;
	}

	public UserStory(String update) {
		String[] splitUpdates = update.split("##");
		id = splitUpdates[1];
		description = splitUpdates[2];
		story = splitUpdates[3];
		status = splitUpdates[4];
		comments = splitUpdates[5];
		points = Integer.parseInt(splitUpdates[6]);
		assignee = splitUpdates[7];
		completionDay = Integer.parseInt(splitUpdates[8]);
		textBox = new TextField();
		textBox.setEditable(false);
		textBox.setFont(Font.font("Verdana", 20));
		textBox.setPrefWidth(100);
		textBox.setPrefHeight(75);
		textBox.setStyle("-fx-background-color: white;-fx-border-color:black;");
		textBox.setLayoutX(20);
		textBox.setLayoutY(180);
		textBox.setText(story);
	}
	
	public void updateTextField() {
		this.textBox.setText(this.story);
	}

	public boolean getIsOwner() {
		return isOwner;
	}

	public String getId() {
		return this.id;
	}

	public String getDescription() {
		return this.description;
	}

	public String getStory() {
		return this.story;
	}

	public String getStatus() {
		return this.status;
	}

	public String getComments() {
		return this.comments;
	}

	public int getPoints() {
		return this.points;
	}

	public String getAssignee() {
		return this.assignee;
	}

	public int getCompletionDay() {
		return this.completionDay;
	}

	public TextField getTextField() {
		return this.textBox;
	}

	public void setDescription(String d) {
		this.description = d;
	}

	public void setStory(String s) {
		this.story = s;
	}

	public void setStatus(String s) {
		this.status = s;
	}

	public void setComments(String c) {
		comments = c;
	}

	public void setPoints(int p) {
		this.points = p;
	}

	public void setAssignee(String a) {
		this.assignee = a;
	}

	public void setCompletionDay(int cd) {
		this.completionDay = cd;
	}

	public void setTextBox(TextField tb) {
		this.textBox = tb;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIsOwner(boolean own) {
		this.isOwner = own;
	}

	public String toString() {
		String str = id + "##" + description + "##" + story + "##" + status + "##" +
					 comments + "##" + points + "##" + assignee + "##" + completionDay + "##" + textBox.getLayoutX() + "##"
					 + textBox.getLayoutY();
		return str;
	}

	public void consumeUpdate(String update) {
		String[] splitUpdates = update.split("##");
		description = splitUpdates[2];
		story = splitUpdates[3];
		status = splitUpdates[4];
		comments = splitUpdates[5];
		points = Integer.parseInt(splitUpdates[6]);
		assignee = splitUpdates[7];
		completionDay = Integer.parseInt(splitUpdates[8]);
		textBox.setText(story);
		textBox.setLayoutX(Double.parseDouble(splitUpdates[9]));
		textBox.setLayoutY(Double.parseDouble(splitUpdates[10]));

	}

}
