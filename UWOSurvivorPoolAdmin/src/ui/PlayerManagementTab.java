package ui;

import java.awt.BorderLayout;
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

import application.Player;
import application.Survivor;

/**
 * The player management tab.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 * @version 0.2 Coby Viner, March 3, 2012
 */
public class PlayerManagementTab extends JPanel implements ActionListener, TableModelListener {
	/**
	 * Not serializable. Do not attempt to serialize.
	 */
	private static final long		serialVersionUID 		= 1L;
	public static final String		TITLE 					= "Player Management";
	public static final ImageIcon	ICON					= new ImageIcon(Constants.ICON_PATH + "players - pdclipart.org.png");
	public static final String		TOOLTIP 				= "Manage the pool's players";
	private static final String 	PLAYER_DELETION_CONF	= "Are you sure that you wish to delete the selected player(s)? ";;

	private PlayerTableModel tableModel;
	private JTable table;
	//TODO Refactor
	private Survivor survivor;

	/**
	 * Constructor.
	 * @param survivor	The survivor object and this tab's parent
	 */
	public PlayerManagementTab(Survivor survivor) {
		this.survivor = survivor;
		this.setLayout(new BorderLayout());

		this.tableModel = new PlayerTableModel(survivor.isGameStarted());
		this.table = new JTable(this.tableModel);
		this.table.getModel().addTableModelListener(this);
		//Set the resize mode so that the table's width remains constant
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.table.setAutoCreateRowSorter(true);
		//Set up input validation
		StringEditor nameEditor = new StringEditor(1, 20);
		this.table.getColumnModel().getColumn(PlayerTableModel.FIRST_NAME_COLUMN).setCellEditor(nameEditor);
		this.table.getColumnModel().getColumn(PlayerTableModel.LAST_NAME_COLUMN).setCellEditor(nameEditor);

		JScrollPane scrollPane = new JScrollPane(table);
		this.table.setFillsViewportHeight(true);

		this.add(scrollPane, BorderLayout.CENTER);

		JButton add = new JButton(Constants.ADD);
		add.addActionListener(this);
		JButton delete = new JButton(Constants.DELETE);
		delete.addActionListener(this);
		JPanel tableEditOptions = new JPanel();
		tableEditOptions.setLayout(new FlowLayout());
		tableEditOptions.add(add);
		tableEditOptions.add(delete);

		this.add(tableEditOptions, BorderLayout.PAGE_END);
		
		for (Player p : survivor.players) {
			this.addPlayer(p, -1);
		}
	}

	/**
	 * Adds a player. Delegates to {@link #addPlayer(Player, int)}.
	 * @param selectedRow	The row to add the contestant after. If selectedRow<0 set as insertion before all rows.
	 */
	private void addPlayer(int selectedRow) {
		String firstName	= "New"; 
		String lastName		= "Contestant";
		Player player 		= new Player(firstName, lastName, tableModel.getFreeID(firstName, lastName));
		survivor.addPlayer(player);
		addPlayer(player, selectedRow);
	}

	/**
	 * Adds a player.
	 * @param player		The player to add
	 * @param selectedRow	The row to add the player after. If selectedRow<0 set as insertion before all rows.
	 */
	private void addPlayer(Player player, int selectedRow) { 
		if (selectedRow < 0) {
			if (player != null) {
				tableModel.insertRow(player);
			} else {
				tableModel.insertRow();
			}
		} else {
			if (player != null) {
				tableModel.insertRow(player, selectedRow);
			} else {
				tableModel.insertRow(selectedRow);
			}
		}
	}

	/**
	 * Deletes the selected players.
	 * @param selectedIndices	The selected players
	 */
	private void deletePlayer(int[] selectedRows) {
		if (selectedRows.length == 0) {
			JOptionPane.showMessageDialog(this, Constants.NONE_SELECTED_MESSAGE, Constants.NONE_SELECTED_TITLE, JOptionPane.ERROR_MESSAGE);
		} else {
			int confirmationResponse = JOptionPane.showInternalConfirmDialog(this.getParent().getParent(), PLAYER_DELETION_CONF);
			if (confirmationResponse == JOptionPane.YES_OPTION) {
				int itemsRemoved = 0;
				for (int i = 0; i < selectedRows.length; i++) {
					int row = selectedRows[i] - itemsRemoved;
					tableModel.removeRow(row);
					itemsRemoved++;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.ADD)) {
			addPlayer(table.getSelectedRow());
		} else if (e.getActionCommand().equals(Constants.DELETE)) {
			deletePlayer(table.getSelectedRows());
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
			int[] sel = new int[e.getLastRow()-e.getFirstRow()];
			for (int row = e.getFirstRow(); row < e.getLastRow(); row++) {
				sel[row] = row;
			}
			deletePlayer(sel);
			for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
				Player p = new Player(tableModel.getValueAt(row, PlayerTableModel.FIRST_NAME_COLUMN).toString(), tableModel.getValueAt(row, PlayerTableModel.LAST_NAME_COLUMN).toString(), tableModel.getValueAt(row, PlayerTableModel.ID_COLUMN).toString());

				addPlayer(p,row);
				survivor.addPlayer(p);
			}
		}
	}
}
