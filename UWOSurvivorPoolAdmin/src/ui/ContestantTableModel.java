package ui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import application.Contestant;

/**
 * The table model for the storage of contestant data.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 */
public class ContestantTableModel extends AbstractTableModel implements TableModelListener {
	public static final int ID_COLUMN = 0;
	public static final int FIRST_NAME_COLUMN = 1;
	public static final int LAST_NAME_COLUMN = 2;
	public static final int PHOTO_COLUMN	= 3;
	public static final int TRIBE_COLUMN	= 4;
	private final String[] columnNames 		= {"ID", "First Name", "Last Name", "Photo", "Tribe Name", "Round Eliminated"};

	private ArrayList<ArrayList<Object>> data;
	private boolean isGameStarted;
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
	 */
	public ContestantTableModel(boolean isGameStarted, int numContestants) {
		this.isGameStarted	= isGameStarted;
		this.data 			= new ArrayList<ArrayList<Object>>(numContestants);
		this.takenIDs 		= new HashMap<String, Integer>();

		for (int i = 0; i < numContestants; i++) {
			ArrayList<Object> row1 = new ArrayList<Object>();
			
			row1.add("");
			row1.add("New");
			row1.add("Contestant");
			row1.add("");
			row1.add("Unassigned");
			row1.add(0);
			
			this.data.add(row1);
		}

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
	 * Inserts a new row at the end of the table.
	 * Delegates to {@link #insertRow(int)}.
	 */
	public void insertRow() {
		insertRow(getRowCount());
	}

	/**
	 * Inserts a new row after the given row index. Delegates to {@link #insertRow(Contestant, int)}.
	 * @param row								The row to insert after
	 * @throws IndexOutOfBoundsException		If the row was invalid or if there are already 15 contestants
	 */
	public void insertRow(int row) throws IndexOutOfBoundsException {
		Contestant contestant = new Contestant("New", "Contestant", getFreeID(), "Unassigned");
		insertRow(contestant, row);
	}


	/**
	 * Inserts a contestant at the end. Delegates to {@link #insertRow(Contestant, int)}.
	 * @param contestant	The contestant to insert
	 */
	public void insertRow(Contestant contestant) {
		insertRow(contestant, getRowCount());
	}

	/**
	 * Inserts a contestant at the after the given row index.
	 * @param contestant					The contestant to insert
	 * @param row							The row to insert after
	 * @throws IndexOutOfBoundsException	If the row was invalid or if there are already 15 contestants
	 */
	public void insertRow(Contestant contestant, int row) throws IndexOutOfBoundsException{
		if (row < 0 || row > getRowCount()) throw new IndexOutOfBoundsException("" + row);

		if (getRowCount() > ContestantManagementTab.MAX_CONTESTANTS) {
			throw new UnsupportedOperationException("Cannot add more than " + ContestantManagementTab.MAX_CONTESTANTS + " contestants");
		}

		ArrayList<Object> a = new ArrayList<Object>();
		String id = contestant.getcID();
		a.add(id);
		a.add(contestant.getFirstName());
		a.add(contestant.getLastName());
		a.add(""); //TODO PHOTO
		a.add(contestant.getTribe());
		a.add(contestant.getRoundEliminated()); 

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
				id = getFreeID();
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

	//TODO Refactor
	/**
	 * Gets a unique ID.
	 * Delegates to {@link #getFreeID(String)} for the actual conflict resolution.
	 * @return			An ID that has not yet been used in the table
	 */
	protected String getFreeID() {
		String idCandidate = "a1";

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
		int number = 1;
		while (takenIDs.containsKey(idCandidate.toLowerCase() + number)) {
			number++;
			if (number > 9) {
				number = 1;
				idCandidate = String.valueOf((char)((int)idCandidate.charAt(0) + 1));
			}
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
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<? extends Object> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		if (isGameStarted && col != TRIBE_COLUMN) {
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
