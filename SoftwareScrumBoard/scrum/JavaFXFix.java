package scrum;

import org.junit.BeforeClass;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFXFix extends Application {
	
	@BeforeClass
	public static void setUpClass() throws InterruptedException {
	    // Initialize Java FX

	    System.out.printf("About to launch FX App\n");
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(JavaFXFix.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	    System.out.printf("FX App thread started\n");
	    Thread.sleep(500);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
