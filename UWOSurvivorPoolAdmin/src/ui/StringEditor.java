package ui;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * This editor governs all {@link JTable}s of type string in this GUI.
 * It is responsible for ensuring input verification and user-friendly reversion.
 * Note that this code was modified from the Oracle Java Swing tutorial on this topic.
 * @see	ui.StringVerifier
 * @author Coby Viner
 * @version 0.1 Coby Viner, March 3, 2012
 */
public class StringEditor extends DefaultCellEditor {
	/**
	 * Not serializable. Do not attempt to serialize.
	 */
	private static final long		serialVersionUID 		= 1L;
	private JFormattedTextField ftf;
	private int min, max;

	/**
	 * Constructor.
	 * @param min	The minimum string length
	 * @param max	The maximum string length
	 */
	public StringEditor(int min, int max) {
		super(new JFormattedTextField());

		this.min = min;
		this.max = max;

		ftf = (JFormattedTextField)getComponent();

		ftf.setInputVerifier(new StringVerifier(min, max, AllowableInputType.LETTERS));
		ftf.setHorizontalAlignment(SwingConstants.TRAILING);
		ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);

		//React when the user presses Enter while the editor is
		//active.  (Tab is handled as specified by
		//JFormattedTextField's focusLostBehavior property.)
		ftf.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),"check");
		ftf.getActionMap().put("check", new AbstractAction() {
			/**
			 * Not serializable. Do not attempt to serialize.
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!ftf.getInputVerifier().verify(ftf)) { //The text is invalid.
					if (userSaysRevert()) { //reverted
						ftf.postActionEvent(); //inform the editor
					}
				} else try {              //The text is valid,
					ftf.commitEdit();     //so use it.
					ftf.postActionEvent(); //stop editing
				} catch (java.text.ParseException exc) { }
			}
		});
	}

	/** 
	 * Lets the user know that the text they entered is 
	 * bad. Returns true if the user elects to revert to
	 * the last good value.  Otherwise, returns false, 
	 * indicating that the user wants to continue editing.
	 */
	protected boolean userSaysRevert() {
		Toolkit.getDefaultToolkit().beep();
		ftf.selectAll();
		Object[] options = {"Edit", "Revert"};
		int answer = JOptionPane.showOptionDialog(
				SwingUtilities.getWindowAncestor(ftf),
				"The value must be a string of letters between "
						+ min + " and "
						+ max + ".\n"
						+ "You can either continue editing "
						+ "or revert to the last valid value.",
						"Invalid Text Entered",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						options,
						options[1]);

		if (answer == JOptionPane.NO_OPTION) { //revert
			ftf.setValue(ftf.getValue());
			return true;
		}
		return false;
	}
	
	//Override to invoke setValue on the formatted text field.
		/* (non-Javadoc)
		 * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
		 */
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected,
				int row, int column) {
			JFormattedTextField ftf =
					(JFormattedTextField)super.getTableCellEditorComponent(
							table, value, isSelected, row, column);
			ftf.setValue(value);
			return ftf;
		}

		/*
		 * Override to check whether the edit is valid,
		 * setting the value if it is and complaining if
		 * it isn't.  If it's OK for the editor to go
		 * away, we need to invoke the superclass's version 
		 * of this method so that everything gets cleaned up.
		 */
		/* (non-Javadoc)
		 * @see javax.swing.DefaultCellEditor#stopCellEditing()
		 */
		@Override
		public boolean stopCellEditing() {
			JFormattedTextField ftf = (JFormattedTextField)getComponent();
			if (ftf.getInputVerifier().verify(ftf)) {
				try {
					ftf.commitEdit();
				} catch (java.text.ParseException exc) { }

			} else { //text is invalid
				if (!userSaysRevert()) { //user wants to edit
					return false; //don't let the editor go away
				} 
			}
			return super.stopCellEditing();
		}
}
