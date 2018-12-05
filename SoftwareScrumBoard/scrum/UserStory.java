package scrum;

import java.util.ArrayList;

public class UserStory {

	String name;
	String description;
	String story;
	int status;
	String title;
	ArrayList<String> comments;
	
	public UserStory() {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getStory() {
		return this.story;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public ArrayList<String> getComments() {
		return this.comments;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public void setDescription(String d) {
		this.description = d;
	}
	
	public void setStory(String s) {
		this.story = s;
	}
	
	public void setStatus(int s) {
		this.status = s;
	}
	
	public void setTitle(String t) {
		this.title = t;
	}
	
	public void addComment(String c) {
		this.comments.add(c);
	}
	
}
