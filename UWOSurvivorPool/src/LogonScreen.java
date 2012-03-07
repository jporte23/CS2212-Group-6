import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;

public class LogonScreen extends MainScreen implements FieldChangeListener {
	private ButtonField buttonLogon;
	private ButtonField buttonCancel;

	public LogonScreen() {
		super();
		setTitle("Survivor Logon");

		buttonLogon = new ButtonField("Logon");
		buttonCancel = new ButtonField("Cancel");
		buttonLogon.setChangeListener(this);
		buttonCancel.setChangeListener(this);

		BasicEditField enterUserID = new BasicEditField("User ID: ", "", 8,
				BasicEditField.FILTER_LOWERCASE);
		PasswordEditField enterPassword = new PasswordEditField("Password: ",
				"", 16, PasswordEditField.FIELD_LEFT);

		this.add(enterUserID);
		this.add(enterPassword);

		HorizontalFieldManager hfm = new HorizontalFieldManager();
		this.add(hfm);

		hfm.add(buttonLogon);
		hfm.add(buttonCancel);
	}

	public void fieldChanged(Field field, int context) {
		if (field == buttonCancel) {
			this.close();
		}
		if (field == buttonLogon) {
			UiApplication.getUiApplication().pushScreen(new SurvivorScreen());
			this.close();
		}
	}
}
