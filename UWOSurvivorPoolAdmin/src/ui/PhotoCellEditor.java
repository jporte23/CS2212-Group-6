package ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * The editor that manages the photo field in {@link ui.ContestantManagementTab}.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 */
public class PhotoCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	/**
	 * Not serializable. Do not attempt to serialize.
	 */
	private static final long 		serialVersionUID 	= 1L;
	private static final String 	SELECT_PHOTO 		= "select photo";
	private static final String 	DEFAULT_IMAGE_PATH 	= Constants.IMAGE_PATH + "male_user_icon - pdclipart.org.png";
	private static ImageIcon 		DEFAULT_IMAGE;

	JButton button;
	JFileChooser fileChooser;
	Component parent;
	ImageIcon image;

	/**
	 * Constructor.
	 * @param parent	The parent component for the file chooser dialogue
	 */
	public PhotoCellEditor(Component parent) {
		PhotoCellEditor.DEFAULT_IMAGE = new ImageIcon(DEFAULT_IMAGE_PATH);

		this.parent = parent;
		this.fileChooser = new JFileChooser();
		this.image = new ImageIcon(DEFAULT_IMAGE_PATH);

		this.button = new JButton();
		this.button.setActionCommand(SELECT_PHOTO);
		this.button.addActionListener(this);
		this.button.setBorderPainted(false);
	}

	/**
	 * Gets the default image.
	 * @return	The default image
	 */
	public static ImageIcon getDefaultImage() {
		return PhotoCellEditor.DEFAULT_IMAGE;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (SELECT_PHOTO.equals(e.getActionCommand())) {
			int returnVal = fileChooser.showOpenDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File imageFile = fileChooser.getSelectedFile();
				image = new ImageIcon(imageFile.getAbsolutePath());
			}
			fireEditingStopped(); //Make the renderer reappear
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return image;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		image = DEFAULT_IMAGE;
		return button;
	}
}