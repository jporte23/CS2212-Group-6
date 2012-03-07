import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;

public class QuestionScreen extends MainScreen implements FieldChangeListener {
	private ButtonField buttonSave;
	private ButtonField buttonCancel;

	public QuestionScreen() {
		super();
		setTitle("Survivor Logon");

		buttonSave = new ButtonField("Save");
		buttonCancel = new ButtonField("Cancel");
		buttonSave.setChangeListener(this);
		buttonCancel.setChangeListener(this);

		BasicEditField enterQuestion = new BasicEditField("Question: ",
				"The Bonus Question goes here!", 200,
				BasicEditField.FILTER_DEFAULT);
		BasicEditField enterAnswer = new BasicEditField("Answer: ", "", 200,
				BasicEditField.FIELD_LEFT);

		this.add(enterQuestion);
		this.add(enterAnswer);
		HorizontalFieldManager hfm = new HorizontalFieldManager();
		this.add(hfm);

		hfm.add(buttonSave);
		hfm.add(buttonCancel);
	}

	public void fieldChanged(Field field, int context) {
		if (field == buttonCancel) {
			this.close();
		}
	}
}
