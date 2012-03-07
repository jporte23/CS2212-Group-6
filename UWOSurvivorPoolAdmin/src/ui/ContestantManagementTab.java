package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import application.Contestant;
import application.Player;
import application.Survivor;

/**
 * The contestant management tab.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 * @version 0.2 Coby Viner, March 3, 2012
 */
public class ContestantManagementTab extends JPanel  implements ActionListener, TableModelListener {
	/**
	 * Not serializable. Do not attempt to serialize.
	 */
	private static final long 		serialVersionUID 				= 1L;
	public static final int 		MIN_CONTESTANTS					= 6;
	public static final int 		MAX_CONTESTANTS					= 15;
	public static final String 		TITLE 							= "Contestant Management";
	public static final ImageIcon	ICON							= new ImageIcon(Constants.ICON_PATH + "contestants - pdclipart.org.png");
	public static final String 		TOOLTIP							= "Manage the current contestants";
	private static final String 	CONTESTANT_DELETION_CONF		= "Are you sure that you wish to delete the selected contestant(s)? ";
	private static final String 	TOO_MANY_CONTESTANTS_TITLE 		= "Too Many Contestants";
	private static final String 	TOO_MANY_CONTESTANTS_MESSAGE	= "You cannot add more than " + MAX_CONTESTANTS + " contestants. Please remove or modify existing contestants instead.";
	private static final String 	BELOW_MINIMUM_MESSAGE 			= "You cannot remove any more contestants. The minimum number of contestants is " + MIN_CONTESTANTS + ". Please add or modify existing contestants instead.";
	private static final String 	BELOW_MINIMUM_TITLE 			= "Too few Contestants";

	private ContestantTableModel tableModel;
	private JTable table;
	private int rowHeight;
	//TODO Refactor
	private Survivor survivor;

	/**
	 * Constructor.
	 * @param survivor	The survivor object and this tab's parent
	 */
	public ContestantManagementTab(Survivor survivor) {
		this.survivor = survivor;
		this.setLayout(new BorderLayout());

		this.tableModel = new ContestantTableModel(survivor.isGameStarted(), MIN_CONTESTANTS);
		this.table = new JTable(this.tableModel);
		this.table.getModel().addTableModelListener(this);
		//Set the resize mode so that the table's width remains constant
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//auto create sorter
		this.table.setAutoCreateRowSorter(true);
		//set the editor for the photo column
		PhotoCellEditor photoCellEditor = new PhotoCellEditor(this);
		this.table.getColumnModel().getColumn(ContestantTableModel.PHOTO_COLUMN).setCellEditor(photoCellEditor);
		//Set up input validation
		StringEditor nameEditor = new StringEditor(1, 20);
		this.table.getColumnModel().getColumn(ContestantTableModel.FIRST_NAME_COLUMN).setCellEditor(nameEditor);
		this.table.getColumnModel().getColumn(ContestantTableModel.LAST_NAME_COLUMN).setCellEditor(nameEditor);
		StringEditor tribeEditor = new StringEditor(1, 30);
		this.table.getColumnModel().getColumn(ContestantTableModel.TRIBE_COLUMN).setCellEditor(tribeEditor);

		this.rowHeight = table.getRowHeight();

		this.setToDefaultPhoto();

		JScrollPane scrollPane = new JScrollPane(table);
		this.table.setFillsViewportHeight(true);

		this.add(scrollPane, BorderLayout.CENTER);

		JPanel tableEditOptions = new JPanel();
		tableEditOptions.setLayout(new FlowLayout());

		JButton add = new JButton(Constants.ADD);
		add.addActionListener(this);
		tableEditOptions.add(add);

		JButton delete = new JButton(Constants.DELETE);
		delete.addActionListener(this);
		tableEditOptions.add(delete);

		this.add(tableEditOptions, BorderLayout.PAGE_END);
		
		for (Contestant c : survivor.contestants) {
			this.addContestant(c, -1);
		}
	}

	/**
	 * Sets all photos to default.
	 */
	private void setToDefaultPhoto() {
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			setToDefaultPhoto(i);
		}
	}

	/**
	 * Sets the photo at the given row to default.
	 * @param row	The row to set the photo at
	 */
	private void setToDefaultPhoto(int row) {
		tableModel.setValueAt(PhotoCellEditor.getDefaultImage(), row, ContestantTableModel.PHOTO_COLUMN);
	}

	/**
	 * Adds a contestant. Delegates to {@link #addContestant(Contestant, int)}.
	 * @param selectedRow	The row to add the contestant after. If selectedRow<0 set as insertion before all rows.
	 */
	private void addContestant(int selectedRow) {
		String firstName	= "New"; 
		String lastName		= "Contestant";
		String tribe		= "Unassigned";
		Contestant contestant 		= new Contestant(firstName, lastName, tableModel.getFreeID(), tribe);
		survivor.addContestant(contestant);
		addContestant(contestant, selectedRow);
	}

	/**
	 * Adds a contestant.
	 * @param contestant	The contestant to add
	 * @param selectedRow	The row to add the contestant after. If selectedRow<0 set as insertion before all rows.
	 */
	private void addContestant(Contestant contestant, int selectedRow) {
		if (tableModel.getRowCount() < MAX_CONTESTANTS) {
			if (selectedRow < 0) {
				if (contestant != null) {
					tableModel.insertRow(contestant);
				} else {
					tableModel.insertRow();
					setToDefaultPhoto(tableModel.getRowCount() - 1);
				}
			} else {
				if (contestant != null) {
					tableModel.insertRow(contestant, selectedRow);
				} else {
					tableModel.insertRow(selectedRow);
					setToDefaultPhoto(selectedRow);
				}
			}
			resizeRows();
		} else {
			JOptionPane.showMessageDialog(this, TOO_MANY_CONTESTANTS_MESSAGE, TOO_MANY_CONTESTANTS_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Deletes the selected contestants.
	 * @param selectedIndices	The selected contestants
	 */
	private void deleteContestant(int[] selectedRows) {
		if (selectedRows.length == 0) {
			JOptionPane.showMessageDialog(this, Constants.NONE_SELECTED_MESSAGE, Constants.NONE_SELECTED_TITLE, JOptionPane.ERROR_MESSAGE);
		} else if (tableModel.getRowCount() - selectedRows.length < 6) {
			JOptionPane.showMessageDialog(this, BELOW_MINIMUM_MESSAGE, BELOW_MINIMUM_TITLE, JOptionPane.ERROR_MESSAGE);
		} else {
			int confirmationResponse = JOptionPane.showInternalConfirmDialog(this.getParent().getParent(), CONTESTANT_DELETION_CONF);
			if (confirmationResponse == JOptionPane.YES_OPTION) {
				int itemsRemoved = 0;
				for (int i = 0; i < selectedRows.length; i++) {
					int row = selectedRows[i] - itemsRemoved;
					tableModel.removeRow(row);
					itemsRemoved++;
				}
				resizeRows();
			}
		}
	}

	/**
	 * Resizes the rows to compensate for their growth or shrinkage.
	 */
	private void resizeRows() {
		for (int row = 0; row < table.getRowCount(); row++) {
			rowHeight = table.getRowHeight();
			for (int column = 0; column < table.getColumnCount(); column++) {
				Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
				rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
			}

			table.setRowHeight(row, rowHeight);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.ADD)) {
			addContestant(table.getSelectedRow());
		} else if (e.getActionCommand().equals(Constants.DELETE)) {
			deleteContestant(table.getSelectedRows());
		} else if (e.getActionCommand().equals(RootPanel.START_GAME)) {
			tableModel.setGameStarted();
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	//XXX FIX
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
			int col = e.getColumn();
			//The ID column
			if (col == ContestantTableModel.PHOTO_COLUMN) {
				resizeRows();
			}
			//TODO create or modify player from data
			//			if (tableModel.getRowCount() > 6) { //XXX FIX
			//				int[] sel = new int[e.getLastRow()-e.getFirstRow()];
			//				for (int row = e.getFirstRow(); row < e.getLastRow(); row++) {
			//					sel[row] = row;
			//				}
			//				deleteContestant(sel);
			//				for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
			//					Contestant c = new Contestant(tableModel.getValueAt(row, ContestantTableModel.FIRST_NAME_COLUMN).toString(), tableModel.getValueAt(row, ContestantTableModel.LAST_NAME_COLUMN).toString(), tableModel.getValueAt(row, ContestantTableModel.ID_COLUMN).toString(), tableModel.getValueAt(row, ContestantTableModel.TRIBE_COLUMN).toString());
			//					addContestant(c, row);
			//					survivor.addContestant(c);
			//				}
			//			}
		}
	}
}
