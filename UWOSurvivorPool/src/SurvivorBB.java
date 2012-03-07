import net.rim.device.api.ui.UiApplication;

public class SurvivorBB extends UiApplication {
	public static void main(String[] args) {
		SurvivorBB splash = new SurvivorBB();
		splash.enterEventDispatcher();
	}

	public SurvivorBB() {
		pushScreen(new LogonScreen());
	}

}