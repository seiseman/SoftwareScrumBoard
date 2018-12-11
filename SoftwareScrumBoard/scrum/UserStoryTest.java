package scrum;
import org.junit.Test;

import javafx.scene.control.TextField;

public class UserStoryTest extends JavaFXFix {
	@Test
	public void test() {
		TextField textbox = new TextField();
		UserStory us = new UserStory("testing" , "testing story", "in progress", "no comments", 4, "nick", textbox, 30);
		assert(us.getDescription() == "testing");
		assert(us.getAssignee() == "nick");
		assert(us.getPoints() == 4);
		us.setStory("this is a different story now");
		assert(us.getStory() == "this is a different story now");
	}

}
