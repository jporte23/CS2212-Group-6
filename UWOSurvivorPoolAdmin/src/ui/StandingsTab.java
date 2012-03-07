package ui;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The standings tab.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 27, 2012
 */
public class StandingsTab extends JPanel {
	/**
	 * Not serializable. Do not attempt to serialize.
	 */
	private static final long		serialVersionUID 	= 1L;
	public static final String 		TITLE				= "Standings";
	public static final ImageIcon 	ICON				= new ImageIcon(Constants.ICON_PATH + "standings - pdclipart.org.png");
	public static final String 		TOOLTIP				= "View the pool's current standings";
	private static final int		HTML_PAGE_WIDTH		= 950;
	private static final int		HTML_PAGE_HEIGHT	= 800;
	
	/**
	 * Constructor.
	 */
	public StandingsTab() {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		try {
			//set the page to be the HTMl page obtained
			editorPane.setPage(application.Standings.getStandings());
		} catch (IOException e) {
			e.printStackTrace();
		}

		//embed the HTML page in a scroll pane
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editorScrollPane.setPreferredSize(new Dimension(HTML_PAGE_WIDTH, HTML_PAGE_HEIGHT));
		editorScrollPane.setMinimumSize(new Dimension(HTML_PAGE_WIDTH, HTML_PAGE_HEIGHT));
		this.add(editorScrollPane);
	}
}
