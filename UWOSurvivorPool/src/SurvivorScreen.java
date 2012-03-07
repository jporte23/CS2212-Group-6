import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.*;
//import net.rim.device.api.ui.component.table.SimpleList;
import net.rim.device.api.ui.container.*;

public class SurvivorScreen extends MainScreen implements FieldChangeListener {

	private ButtonField buttonSave;
	private ButtonField buttonCancel;
	private ButtonField buttonBonus;

	public SurvivorScreen() {
		super(Manager.NO_VERTICAL_SCROLL);

		setTitle("Survivor Game");

		buttonSave = new ButtonField("Save");
		buttonCancel = new ButtonField("Cancel");
		buttonBonus = new ButtonField("Bonus Question");
		buttonSave.setChangeListener(this);
		buttonCancel.setChangeListener(this);
		buttonBonus.setChangeListener(this);

		add(new LabelField("Contestants List", LabelField.FIELD_HCENTER));
		add(new SeparatorField());
		
		RadioButtonGroup rbg = new RadioButtonGroup();
		
		add(new RadioButtonField("  Jonathan Di Nardo",rbg,true));
		add(new RadioButtonField("  Moammer Frayjoun",rbg,false));
		add(new RadioButtonField("  Connor Graham",rbg,false));
		add(new RadioButtonField("  Jordan Porter",rbg,false));
		add(new RadioButtonField("  Coby Viner",rbg,false));

		add(new SeparatorField());

		HorizontalFieldManager hfm = new HorizontalFieldManager();
		this.add(hfm);

		hfm.add(buttonSave);
		hfm.add(buttonCancel);
		hfm.add(buttonBonus);
	}

	public void fieldChanged(Field field, int context) {
		if (field == buttonCancel) {
			this.close();
		}
		if (field == buttonBonus) {
			UiApplication.getUiApplication().pushScreen(new QuestionScreen());
		}
	}
}