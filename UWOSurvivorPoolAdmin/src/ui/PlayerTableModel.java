package ui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import application.Player;

/**
 * The table model for the storage of player data.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 */
public class PlayerTableModel extends AbstractTableModel implements TableModelListener {
	public static final int ID_COLUMN		= 0;
	public static final int FIRST_NAME_COLUMN = 1;
	public static final int LAST_NAME_COLUMN = 2;
	public static final int SCORE_COLUMN	= 3;
	private boolean isGameStarted;

	private final String[] columnNames = {"ID", "First Name", "Last Name", "Score"};
	private ArrayList<ArrayList<Object>> data;
	/**
	 * Keyed by taken ID.
	 * Valued by row.
	 */
	private HashMap<String, Integer> takenIDs;

	/**
	 * Not serializable. Do not attempt to serialize.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor. Initializes data.
	 * @param isGameStarted	Specifies if the game has started
	 */
	public PlayerTableModel(boolean isGameStarted) {
		this.isGameStarted	= isGameStarted;
		this.data 			= new ArrayList<ArrayList<Object>>();
		this.takenIDs 		= new HashMap<String, Integer>();

//		ArrayList<Object> row1 = new ArrayList<Object>();
//		ArrayList<Object> row2 = new ArrayList<Object>();
//
//		String firstName = "John";
//		String lastName	 = "Doe";
//		row1.add("");
//		row1.add(firstName);
//		row1.add(lastName);
//		row1.add(150);
//		firstName = "Jane";
//		lastName  = "Smith";	
//		row2.add("");
//		row2.add(firstName);
//		row2.add(lastName);
//		row2.add(50);
//		this.data.add(row1);
//		this.data.add(row2);
//
		resolveIDConflicts(true);

		/*
		 * This model listens to its own changes to prevent ID conflicts.
		 * It is imperative that this is not done until the table is
		 * fully initialized, in a conflict free configuration.
		 */
		this.addTableModelListener(this);
	}

	/**
	 * Removes the row at row from the model. 
	 * Notification of the row being removed will be sent to all the listeners.
	 * @param row	the row index of the row to be removed
	 * @throws IndexOutOfBoundsException	if the row was invalid
	 */
	public void removeRow(int row) throws IndexOutOfBoundsException {
		if (row < 0 || row > getRowCount()) throw new IndexOutOfBoundsException("" + row);
		//deleted entry's ID is no longer used
		takenIDs.remove(getValueAt(row, 0));
		data.remove(row);
		fireTableRowsDeleted(row, row);
	}

	/**
	 * Inserts a row at the end of the table.
	 * Delegates to {@link #insertRow(int)}.
	 */
	public void insertRow() {
		insertRow(getRowCount());
	}

	/**
	 * Inserts a row after the given row index. Delegates to {@link #insertRow(Player, int)}.
	 * @param row							The row to insert another after
	 * @throws IndexOutOfBoundsException	If the row was invalid
	 */
	public void insertRow(int row) throws IndexOutOfBoundsException {
		String firstName	= "New"; 
		String lastName		= "Contestant";
		Player player 		= new Player(firstName, lastName, getFreeID(firstName, lastName));
		insertRow(player, row);
	}

	/**
	 * Inserts a player at the end. Delegates to {@link #insertRow(Player, int)}.
	 * @param player	The player to insert
	 */
	public void insertRow(Player player) {
		insertRow(player, getRowCount());
	}

	/**
	 * Inserts a player at the after the given row index.
	 * @param player						The player to insert
	 * @param row							The row to insert after
	 * @throws IndexOutOfBoundsException	If the row was invalid
	 */
	public void insertRow(Player player, int row) throws IndexOutOfBoundsException {
		if (row < 0 || row > getRowCount()) throw new IndexOutOfBoundsException("" + row);

		ArrayList<Object> a = new ArrayList<Object>();
		String id = player.getpID();
		a.add(id);
		a.add(player.getFirstName());
		a.add(player.getLastName());
		a.add(player.getPoints());

		data.add(row, a);

		fireTableRowsInserted(row, row);
		resolveIDConflict(id, row);
	}

	/**
	 * Sets IDs to their correct format and 
	 * resolves all ID conflicts in the table.
	 * This should be called after table construction.
	 * Delegates to {@link #resolveIDConflict(String, int)}.
	 * @param autogenerateIDs	Flag that determines if IDs are automatically
	 * 							created from existing first and last names.
	 * 							Set to true iff no valid IDs are in the table.
	 */
	private void resolveIDConflicts(boolean autogenerateIDs) {
		for (int i = 0; i < getRowCount(); i++) {
			String id = "";
			if (autogenerateIDs) {
				id = getFreeID(getValueAt(i, 1).toString(), getValueAt(i, 2).toString());
				takenIDs.put(id.toLowerCase(), i);
				setValueAt(id, i, 0);
			} else {
				id = getValueAt(i, 0).toString();
				resolveIDConflict(id, i);
			}
		}
	}

	/**
	 * This must be called any time an ID is updated 
	 * to ensure conflict free id assignment.
	 * @param id	The current ID
	 * @param row	The row of the ID
	 */
	private void resolveIDConflict(String id, int row) {
		//TODO Remove IDs that are no longer in use due to update?
		Integer rowOfTakenID = takenIDs.get(id.toLowerCase());
		if (rowOfTakenID != null) {
			//only re-map ID if a true collision occurs
			if (rowOfTakenID != row) {
				String idToSet = getFreeID(getValueAt(row, 0).toString());
				takenIDs.put(idToSet.toLowerCase(), row);
				setValueAt(idToSet, row, 0);
			}		
		} else {
			//The ID is new - it is now taken.
			takenIDs.put(id.toLowerCase(), row);
		}
	}

	/**
	 * Gets a unique ID constructed from a first and lastname.
	 * Delegates to {@link #getFreeID(String)} for the actual conflict resolution.
	 * @param firstName	The player's first name
	 * @param lastName	The player's last name
	 * @return			An ID that has not yet been used in the table
	 */
	protected String getFreeID(String firstName, String lastName) {
		String idCandidate = "";

		if (!firstName.isEmpty() && lastName.isEmpty()) {
			idCandidate += firstName.charAt(0);
		} else if (!lastName.isEmpty() && firstName.isEmpty()) {
			idCandidate = lastName.substring(0, Math.min(lastName.length(), 5));
		} else if (!(firstName.isEmpty() && lastName.isEmpty())) {
			idCandidate = firstName.charAt(0) + lastName.substring(0, Math.min(lastName.length(), 5));
		}

		return getFreeID(idCandidate);
	}

	/**
	 * Gets a unique ID. Resolves conflicts by appending a number.
	 * @param	currentID		The previous conflicting ID to append a number to
	 * @return					An ID that has not yet been used in the table
	 */
	private String getFreeID(String currentID) {
		String idCandidate = "";
		//trim off any numbers
		for (int i = 0; i < currentID.length(); i++) {
			char c = currentID.charAt(i);
			if (!Character.isDigit(c)) {
				idCandidate += c;
			}
		}

		//The ID is not returned until it is unique.
		if (!takenIDs.containsKey(idCandidate.toLowerCase()))	return idCandidate;
		int number = 1;
		while (takenIDs.containsKey(idCandidate.toLowerCase() + number)) {
			number++;
		}

		return idCandidate + number;
	}

	/**
	 * Sets the isGameStarted flag to true.
	 */
	public void setGameStarted() {
		this.isGameStarted = true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<? extends Object> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		//ID is editable only before the game starts, while score is never editable
		if ((isGameStarted && col == ID_COLUMN) || col == SCORE_COLUMN) {
			return false;
		} else {
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		data.get(row).set(col, value);
		fireTableCellUpdated(row, col);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			//The ID column
			if (col == 0) {
				/*
				 * Circumvent an infinite regression, since the ID modification 
				 * will fire the event that this is detecting.
				 */
				this.removeTableModelListener(this);
				resolveIDConflict(getValueAt(row, col).toString(), e.getFirstRow());
				this.addTableModelListener(this);
			}
			
			
		}
	}
}
