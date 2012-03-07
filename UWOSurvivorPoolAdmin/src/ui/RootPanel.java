package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import application.Survivor;
import application.SurvivorDriver;
/**
 * The root panel for the GUI. 
 * Initialize the GUI by creating a new object of this type. 
 * Ensure the this is done only from the Swing event dispatch thread.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 * @version 0.2 Coby Viner, March 3, 2012
 */
public class RootPanel extends JFrame implements ActionListener {
	/**
	 * Not serializable. Do not attempt to serialize.
	 */
	private static final long serialVersionUID = 1L;
	private static final ImageIcon	ABOUT_ICON 				= new ImageIcon(Constants.ICON_PATH + "bluequestionmark-wikicommons.png");
	//TODO Create a nice logo
	private static final ImageIcon	LOGO 					= new ImageIcon(Constants.ICON_PATH + "bluequestionmark-wikicommons.png");
	private static final ImageIcon	RESET_ICON 				= new ImageIcon(Constants.ICON_PATH + "eraser - httpwww.pdclipart.org.png");
	private static final ImageIcon 	QUIT_ICON 				= new ImageIcon(Constants.ICON_PATH + "redx-wikicommons.png");
	private static final String 	SUNSET 					= "Sunset";
	private static final String 	DAWN 					= "Dawn";
	private static final String 	DEFAULT 				= "Default";
	private static final String 	CLEAR_DATA 				= "Clear all saved data";
	private static final String 	QUIT 					= "Quit";
	private static final String 	ABOUT 					= "About";
	public static final String 		START_GAME 				= "Start Game";
	private static final String 	ABOUT_MESSAGE 			= "<html><font size=\"35\">" + application.ProjectInfo.TITLE + "<html><font size=\"6\"><br/>" + application.ProjectInfo.getCopyWriteInfo();
	private static final String 	BET_AMOUNT_START_MESSAGE= "Please enter the desired bet amount and press OK to start the game. \nNote that entering anything other than a number will cause this process to abort.";
	private static final String 	NIMBUS_UNSUPPORTED 		= "The Nimbus Look and Feel is not supported on this system. Accordingly, the functionality of colour schemes is severely reduced. Please update to the latest JRE to re-enable this functionality.";

	/**
	 * The size of the GUI.
	 * This is set to XGA minus 68 in width for compatibility with the vast majority of systems.
	 */
	private static final Dimension 	size 					= new Dimension(1024, 700);
	//TODO Refactor code so that survivor is only needed here!
	private final Survivor survivor;

	private String nimbus;
	private Set<Component> backgroundColourableComponents;
	private Set<Component> complementaryAmenableForegroundColourableComponents;
	private Set<AbstractButton> colourSchemeButtons;
	
	private JTabbedPane tabbedPane;
	private int startGameTabIndex;

	/**
	 * Constructor to initialize the GUI. 
	 * This should only be invoked from the Swing event thread.
	 * Properties must be initialized first.
	 * In particular, {@link Survivor#currentColourScheme} must be set.
	 * @param survivor	The Survivor object associated with this object
	 */
	public RootPanel(final Survivor survivor) {	
		this.backgroundColourableComponents = new HashSet<Component> ();
		this.complementaryAmenableForegroundColourableComponents = new HashSet<Component> ();
		this.colourSchemeButtons = new HashSet<AbstractButton> ();

		this.survivor = survivor;
		
		//Do nothing on close to allow close to be handles identically to File>Quit.
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		/*
		 * This is added as an anonymous inner class to
		 * prevent having to extend WindowAdapter or implement WindowListener.
		 */
		this.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		this.setTitle(application.ProjectInfo.TITLE);
		this.setSize(size);
		this.setMinimumSize(size);
		this.setLayout(new BorderLayout());

		/*
		 * Attempts to set the Look and Feel to Nimbus.
		 * Note that a modicum of backwards compatibility is present if Nimbus cannot be found.
		 * It is ideal that it is available, since it severely reduces colour schemes.
		 */
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					this.nimbus = info.getClassName();
					UIManager.setLookAndFeel(this.nimbus);
					break;
				}
			}
		} catch (Exception e) {}

		tabbedPane = new JTabbedPane();

		ContestantManagementTab contestantManagementTab = new ContestantManagementTab(survivor);
		tabbedPane.addTab(ContestantManagementTab.TITLE, ContestantManagementTab.ICON, contestantManagementTab, ContestantManagementTab.TOOLTIP);
		tabbedPane.setMnemonicAt(tabbedPane.getTabCount()-1, KeyEvent.VK_C);
		
		PlayerManagementTab playerManagementTab = new PlayerManagementTab(survivor);
		tabbedPane.addTab(PlayerManagementTab.TITLE, PlayerManagementTab.ICON, playerManagementTab, PlayerManagementTab.TOOLTIP);
		tabbedPane.setMnemonicAt(tabbedPane.getTabCount()-1, KeyEvent.VK_P);

		tabbedPane.addTab(BonusQuestionTab.TITLE, BonusQuestionTab.ICON, new BonusQuestionTab(survivor), BonusQuestionTab.TOOLTIP);
		tabbedPane.setMnemonicAt(tabbedPane.getTabCount()-1, KeyEvent.VK_B);

		tabbedPane.addTab(StandingsTab.TITLE, StandingsTab.ICON, new StandingsTab(), StandingsTab.TOOLTIP);
		tabbedPane.setMnemonicAt(tabbedPane.getTabCount()-1, KeyEvent.VK_S);

		JButton startGame = new JButton(START_GAME);
		startGame.addActionListener(this);
		startGame.addActionListener(contestantManagementTab);
		startGame.addActionListener(playerManagementTab);
		startGame.setFont(Constants.BOLD_FONT);
		startGame.setMnemonic(KeyEvent.VK_G);
		colourSchemeButtons.add(startGame);
		tabbedPane.add(new JPanel(new GridLayout(1,1)));
		this.startGameTabIndex = tabbedPane.getComponentCount() - 1;
		tabbedPane.setTabComponentAt(this.startGameTabIndex, startGame);
		tabbedPane.setEnabledAt(this.startGameTabIndex, false);
		backgroundColourableComponents.add(tabbedPane);

		for (Component c : tabbedPane.getComponents()) {
			backgroundColourableComponents.add(c);
		}

		this.add(tabbedPane);
		io.io.readProperties();

		JMenuBar menuBar = new JMenuBar();
		backgroundColourableComponents.add(menuBar);

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		this.backgroundColourableComponents.add(file);
		this.complementaryAmenableForegroundColourableComponents.add(file);
		menuBar.add(file);

		JMenuItem menuItem = new JMenuItem(CLEAR_DATA, RESET_ICON);
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.addActionListener(this);
		this.backgroundColourableComponents.add(menuItem);
		file.add(menuItem);

		file.addSeparator();

		JLabel label = new JLabel("Colour Scheme");
		file.add(label);

		ButtonGroup group = new ButtonGroup();

		JRadioButtonMenuItem  rbMenuItemDefault = new JRadioButtonMenuItem(DEFAULT);
		rbMenuItemDefault.addActionListener(this);
		this.colourSchemeButtons.add(rbMenuItemDefault);
		this.backgroundColourableComponents.add(rbMenuItemDefault);
		group.add(rbMenuItemDefault);
		file.add(rbMenuItemDefault);

		JRadioButtonMenuItem  rbMenuItemSunset = new JRadioButtonMenuItem(SUNSET);
		rbMenuItemSunset.addActionListener(this);
		this.colourSchemeButtons.add(rbMenuItemSunset);
		this.backgroundColourableComponents.add(rbMenuItemSunset);
		group.add(rbMenuItemSunset);
		file.add(rbMenuItemSunset);

		JRadioButtonMenuItem rbMenuItemDawn = new JRadioButtonMenuItem(DAWN);
		rbMenuItemDawn.addActionListener(this);
		this.colourSchemeButtons.add(rbMenuItemDawn);
		this.backgroundColourableComponents.add(rbMenuItemDawn);
		group.add(rbMenuItemDawn);
		file.add(rbMenuItemDawn);

		/*
		 * Set both the colour scheme and the corresponding selection 
		 * of the radio button in the file menu.
		 * This data is read from the configuration file, if available.
		 */
		setCurrentColourSchemeSelected();
		setColourScheme(survivor.getCurrentColourScheme());

		menuItem = new JMenuItem(QUIT, QUIT_ICON);
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		menuItem.addActionListener(this);
		this.backgroundColourableComponents.add(menuItem);
		file.add(menuItem);
		file.addSeparator();

		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		this.backgroundColourableComponents.add(help);
		this.complementaryAmenableForegroundColourableComponents.add(help);
		menuBar.add(help);

		menuItem = new JMenuItem(ABOUT, ABOUT_ICON);
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		menuItem.addActionListener(this);
		this.backgroundColourableComponents.add(menuItem);
		help.add(menuItem);
		help.addSeparator();


		this.add(menuBar, BorderLayout.PAGE_START);

		this.setVisible(true);
	}

	/**
	 * Exits the program.
	 * This method should be used when exiting in all circumstances.
	 * This is thread safe as it executes on Swing's event dispatch thread.
	 * Note that all other non-daemon threads should be 
	 * terminated before invocation of this method.
	 */
	private void exit() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			/* (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				dispose();
				survivor.save();
				SurvivorDriver.exit();
			}
		});
	}

	/**
	 * Sets the colour scheme back to default.
	 * This delegates to {@link #setColourScheme(ColourScheme)}.
	 */
	private void setDefaultColourScheme() {
		if (nimbus != null) {
			setColourScheme(ColourScheme.DEFAULT);
		}
	}

	/**
	 * Sets the colour scheme based upon a {@link ColourScheme}.
	 * The colour scheme changes only after all queued messages are displayed.
	 * Note that this is not thread safe and should execute on the Swing event dispatching thread.
	 * This should not be invoked directly - use {@link #setColourScheme(ColourScheme)} or {@link #setDefaultColourScheme()} instead.
	 * @param colourScheme	The colour scheme to set
	 */
	private void setColourScheme(final ColourScheme colourScheme) {
		final Color c1				= colourScheme.getC1();
		final Color c2				= colourScheme.getC2(); 
		final Color c3				= colourScheme.getC3();
		final Color c4				= colourScheme.getC4();
		final Color readableText	= colourScheme.getReadableText();

		if (nimbus != null) {
			UIManager.put("nimbusBase", c1);
			UIManager.put("nimbusBlueGrey", c2);
			UIManager.put("control", c3);
			UIManager.put("textBackground", c1);
			UIManager.put("textForeground", readableText);
			UIManager.put("infoText", readableText);
			UIManager.put("menuText", readableText);
			UIManager.put("TextArea.background", c4);

			try {
				UIManager.setLookAndFeel(nimbus);
			} catch (Exception ex) {}
		} else {
			JOptionPane.showMessageDialog(this, NIMBUS_UNSUPPORTED, "Nimbus Error", JOptionPane.ERROR_MESSAGE);

			for(Component c : backgroundColourableComponents) {
				c.setBackground(c3);
			}
		}

		for(Component c : complementaryAmenableForegroundColourableComponents) {
			c.setForeground(readableText);
		}
		//update the current colour scheme
		updateColourScheme();
		survivor.setColourScheme(colourScheme);
		//maintain consistency with the colour scheme that is shown to be selected
		setCurrentColourSchemeSelected();
	}

	/**
	 * Updates the colour scheme by initializing the UI to the new Look and Fell and repainting.
	 * This is a separate method since it cannot be directly invoked within an inner class 
	 * due to the necessity of passing this object as a parameter.
	 * Interestingly, {@link javax.swing.SwingUtilities#updateComponentTreeUI(java.awt.Component)}
	 * will throw a NullPointerException if this is not explicitly handles on the Swing event dispatch thread.
	 * This is due to all children components of this appearing null otherwise.
	 */
	private void updateColourScheme() {
		javax.swing.SwingUtilities.updateComponentTreeUI(this);

		//Needed to ensure that the text updates correctly and simultaneously with the background
		Set<Component> uiComponentsToUpdate = new HashSet<Component>();
		uiComponentsToUpdate.addAll(backgroundColourableComponents);
		uiComponentsToUpdate.addAll(complementaryAmenableForegroundColourableComponents);
		for (Component c: uiComponentsToUpdate) {
			javax.swing.SwingUtilities.updateComponentTreeUI(c);
			c.repaint();
		}

		//TODO Get tabbed panel members' text to update
		this.repaint();
	}

	/**
	 * Makes the buttons in the menu correctly represent the currently selected colour scheme.
	 */
	private void setCurrentColourSchemeSelected() {
		if (survivor.getCurrentColourScheme() == null) {
			for (AbstractButton b : this.colourSchemeButtons) {
				if (b.getText().toUpperCase().contains(DEFAULT.toUpperCase())) {
					b.setSelected(true);
				} else {
					b.setSelected(false);
				}
			}
			return;
		}
		for (AbstractButton b : this.colourSchemeButtons) {
			if (b.getText().toUpperCase().equals(survivor.getCurrentColourScheme().toString())) {
				b.setSelected(true);
			} else {
				b.setSelected(false);
			}
		}
		this.repaint();
	}

	/**
	 * A very basic dialogue creator. 
	 * This allows for greater customization like setting modality.
	 * @param owner			The owner
	 * @param minSize		The minimum size
	 * @param title			The title
	 * @param message		The message to display
	 * @param option		The options - i.e. the buttons added defined by {@link JDialog} option types. Currently only {@link JOptionPane#OK_OPTION} is supported.
	 * @param icon			The icon to display
	 * @param modalityType	The modality type @see ModalityType
	 */
	private void showDialouge(Window owner, Dimension minSize, String title, String message, int option, Icon icon, ModalityType modalityType) {
		final JDialog d = new JDialog(owner, title, modalityType);
		d.setLayout(new BorderLayout());
		d.setSize(minSize);
		d.setMinimumSize(minSize);
		//set the dialouge's location to be centred at the centre of it's owner
		d.setLocation(((owner.getX() + owner.getWidth())/2)-d.getWidth()/2, ((owner.getY() + owner.getHeight())/2)-d.getHeight()/2);
		JLabel l = new JLabel(message, icon, SwingConstants.CENTER);
		//Add dialogue's text containing children for text recolouring
		l.setForeground(survivor.getCurrentColourScheme().getReadableText());
		complementaryAmenableForegroundColourableComponents.add(l);
		d.add(l, BorderLayout.CENTER);

		if (option == JOptionPane.OK_OPTION) {
			JButton ok = new JButton("OK");
			//make the button respond when ENTER is pressed
			ok.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
			ok.getInputMap().put(KeyStroke.getKeyStroke("released ENTER"), "released");
			//Add dialogue's text containing children for text recolouring
			complementaryAmenableForegroundColourableComponents.add(ok);
			ok.setForeground(survivor.getCurrentColourScheme().getReadableText());
			ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					d.dispose();
				}
			});

			d.add(ok, BorderLayout.PAGE_END);
		}

		//		d.setBackground(hangman.currentColourScheme.getC3());
		//The dialogue is included in the below sets for proper colouring
		backgroundColourableComponents.add(d);

		d.setVisible(true);
	}

	/* (non-Javadoc)
	 * Handles actions in the menu.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(CLEAR_DATA)) {
			io.io.clearData();
		} else if (ColourScheme.isColourScheme(e.getActionCommand())) {
			if (e.getActionCommand().equals(SUNSET)) { 
				setColourScheme(ColourScheme.SUNSET);
			} else if (e.getActionCommand().equals(DAWN)) {
				setColourScheme(ColourScheme.DAWN);
			} else if (e.getActionCommand().equals(DEFAULT)) {
				setDefaultColourScheme();
			} 
		} else if(e.getActionCommand().equals(QUIT)) {
			/*
			 * Fire a window closing event manually to ensure that this 
			 * is identical to the user(s) hitting the "X".
			 */
			this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else if (e.getActionCommand().equals(ABOUT)) {
			showDialouge(this, new Dimension(550, 300), ABOUT, ABOUT_MESSAGE, JOptionPane.OK_OPTION, LOGO, ModalityType.APPLICATION_MODAL);
		} else if(e.getActionCommand().equals(START_GAME)) {
			String betAmountString = JOptionPane.showInputDialog(this, BET_AMOUNT_START_MESSAGE);
			if (betAmountString != null && betAmountString.matches("\\d")) {
				int betAmount = Integer.parseInt(betAmountString);
				survivor.startGame(betAmount);
				
				JLabel round = new JLabel("Round: " + survivor.getCurrentRound() + " of " + survivor.getTotalRounds());
				round.setFont(Constants.BOLD_FONT);
				tabbedPane.setTabComponentAt(startGameTabIndex, round);
			}
		}
	}
}
