package scrum;
import java.util.ArrayList;

import org.junit.Test;

public class IntegrationTest1 extends JavaFXFix {
	@Test
	public void test() {
		Server s = new Server();
		ScrumClientGateway gateway = new ScrumClientGateway();
		ScrumClientGateway gateway2 = new ScrumClientGateway();
		gateway.sendUpdate("Update##1##asdf##sdf##To Do##fds##5##asff##30##256.5##177.5");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> updates = gateway2.getUpdates();
		assert(updates.get(0).compareTo("Update##1##asdf##sdf##To Do##fds##5##asff##30##256.5##177.5") == 0);
		String update1 = gateway.editRequest("1");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String update2 = gateway2.editRequest("1");
		assert(update1.compareTo("true") == 0);
		assert(update2.compareTo("false") == 0);
	}

}
