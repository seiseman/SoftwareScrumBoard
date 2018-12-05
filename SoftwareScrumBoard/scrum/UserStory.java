package scrum;

import javafx.scene.control.TextField;

public class UserStory {

	String description;
	String story;
	String status;
	String comments;
	int points;
	String assignee;
	int completionDay;
	TextField textBox;
	
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
	
}
