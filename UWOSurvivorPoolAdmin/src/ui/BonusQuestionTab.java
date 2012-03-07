package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import application.Survivor;

/**
 * The bonus question tab.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 */
public class BonusQuestionTab extends JPanel implements ActionListener, ChangeListener {
	/**
	 * Not serializable. Do not attempt to serialize.
	 */
	private static final long 		serialVersionUID 		= 1L;
	public static final String		TITLE					= "Bonus Questions";
	public static final ImageIcon	ICON 					= new ImageIcon(Constants.ICON_PATH + "bonus_questions - pdclipart.org.png");
	public static final String		TOOLTIP					= "Create and modify bonus questions";
	private static final String		SHORT_ANSWER			= "Short Answer";
	private static final String		MULTIPLE_CHOICE			= "Multiple Choice";
	private static final String 	QUESTION_DELETION_CONF 	= "Are you sure that you wish to delete the selected bonus question(s)? ";
	private static final String 	INVALID_INPUT 			= "You must only input alphanumeric characters. The input length must be between 1 and 200 characters.";

	private boolean multipleChoiceMode;
	private DefaultListModel questionsListModel;
	private JList questionsList;
	private JPanel modifyArea, multipleChoiceAnswerPanel, listEditOptions;
	private JRadioButton[] multipleChoiceOptions;
	private JRadioButton shortAnswer, multipleChoice;
	private JTextField titleInput;
	private JTextArea questionInput, answerInput;
	private JLabel answerLabel;
	
	/**
	 * Constructor.
	 * @param survivor	The survivor object and this tab's parent
	 */
	public BonusQuestionTab(Survivor survivor) {
		this.setLayout(new BorderLayout());

		questionsListModel = new DefaultListModel();
		populateQuestionList();
		questionsList = new JList(questionsListModel);
		questionsList.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(questionsList);
		listScroller.setHorizontalScrollBar(null);
		listScroller.setPreferredSize(new Dimension(250, 80));

		this.add(listScroller, BorderLayout.LINE_START);
		
		modifyArea = new JPanel();
		modifyArea.setLayout(new BoxLayout(modifyArea, BoxLayout.PAGE_AXIS));

		JLabel round = new JLabel("Round: " + survivor.getCurrentRound());
		round.setFont(Constants.DEFAULT_FONT);
		round.setHorizontalAlignment(JLabel.CENTER);
		round.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(round, BorderLayout.PAGE_START);
		
		InputVerifier inputVerifier = new StringVerifier(1, 200, AllowableInputType.ALPHANUMERIC, INVALID_INPUT, this);
		
		JLabel titleLabel = new JLabel("Title: ");
		titleInput = new JTextField();
		titleInput.setMaximumSize(new Dimension(500, 100));
		titleInput.setInputVerifier(inputVerifier);
		JLabel questionType = new JLabel("Question Type: ");
		multipleChoiceMode = false;
		shortAnswer = new JRadioButton(SHORT_ANSWER, true);
		shortAnswer.addActionListener(this);
		multipleChoice = new JRadioButton(MULTIPLE_CHOICE, false);
		multipleChoice.addActionListener(this);
		
		JLabel questionLabel = new JLabel("Question: ");
		questionInput = new JTextArea();
		questionInput.setInputVerifier(inputVerifier);
		answerLabel = new JLabel("Answer: ");
		answerInput = new JTextArea();
		answerInput.setInputVerifier(inputVerifier);

		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(titleLabel);
		titleInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(titleInput);
		questionType.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(questionType);
		shortAnswer.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(shortAnswer);
		multipleChoice.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(multipleChoice);
		questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(questionLabel);
		questionInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(questionInput);
		answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(answerLabel);
		answerInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(answerInput);
		
		JButton add = new JButton(Constants.ADD);
		add.addActionListener(this);
		JButton modify = new JButton(Constants.MODIFY);
		modify.addActionListener(this);
		JButton delete = new JButton(Constants.DELETE);
		delete.addActionListener(this);

		//TODO Add list function by round
		
		listEditOptions = new JPanel();
		listEditOptions.setLayout(new FlowLayout());
		listEditOptions.add(add);
		listEditOptions.add(modify);
		listEditOptions.add(delete);

		listEditOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyArea.add(listEditOptions);
			
		this.add(modifyArea, BorderLayout.CENTER);
	}

	/**
	 * Populates the question list with demo data.
	 */
	private void populateQuestionList() {
		questionsListModel.addElement("Lorem ipsum dolor sit amet,");		
		questionsListModel.addElement(" consectetur adipiscing elit.");
		questionsListModel.addElement("Integer rutrum urna ut sem condimentum ");
		questionsListModel.addElement(" volutpat sed sit amet metus.");
	}

	/**
	 * Sets the modality to short answer.
	 */
	private void setModalitySA() {
		multipleChoiceMode = false;
		modifyArea.remove(multipleChoiceAnswerPanel);
		modifyArea.remove(answerLabel);
		modifyArea.remove(answerInput);
		modifyArea.remove(listEditOptions);

		modifyArea.add(answerLabel);
		modifyArea.add(answerInput);
		modifyArea.add(listEditOptions);

		this.repaint();
	}
	
	/**
	 * Sets the modality to multiple choice.
	 */
	private void setModalityMC() {
		multipleChoiceMode = true;
		modifyArea.remove(answerLabel);
		modifyArea.remove(answerInput);
		modifyArea.remove(listEditOptions);

		multipleChoiceAnswerPanel = new JPanel();
		JLabel numAnswersLabel = new JLabel("Number of Answers: ");
		multipleChoiceAnswerPanel.add(numAnswersLabel);
		JSlider numAnswers = new JSlider(2, 8, 4);
		numAnswers.setMajorTickSpacing(1);
		numAnswers.setPaintTicks(true);
		numAnswers.setPaintLabels(true);
		numAnswers.setSnapToTicks(true);
		numAnswers.addChangeListener(this);
		multipleChoiceAnswerPanel.add(numAnswers);

		JLabel optionsLabel = new JLabel("Answers: ");
		multipleChoiceAnswerPanel.add(optionsLabel);
		multipleChoiceOptions = new JRadioButton[numAnswers.getMaximum()];
		multipleChoiceOptions[0] = new JRadioButton("A", true);
		multipleChoiceOptions[0].addActionListener(this);
		multipleChoiceAnswerPanel.add(multipleChoiceOptions[0]);
		for (int i = 1; i < numAnswers.getMaximum(); i++) {
			multipleChoiceOptions[i] = new JRadioButton(String.valueOf((char)(65 + i)), false);
			multipleChoiceOptions[i].addActionListener(this);
			multipleChoiceAnswerPanel.add(multipleChoiceOptions[i]);
		}

		setNumAnswers(numAnswers.getValue());

		modifyArea.add(multipleChoiceAnswerPanel);
		modifyArea.add(answerInput);
		modifyArea.add(listEditOptions);

		this.repaint();
	}

	/**
	 * Sets the number of answers in MC modality.
	 * @param value	The number of answers
	 */
	private void setNumAnswers(int value) {
		for (int i = multipleChoiceOptions.length-1; i >= value; i--) {
			multipleChoiceOptions[i].setEnabled(false);
			/*
			 * iI an option is selected that will be disabled, 
			 * reset to select A to maintain a valid state.
			 */
			if (multipleChoiceOptions[i].isSelected()) {
				multipleChoiceOptions[i].setSelected(false);
				multipleChoiceOptions[0].setSelected(true);
			}
		}

		for (int i = 0; i < value; i++) {
			multipleChoiceOptions[i].setEnabled(true);
		}
	}

	/**
	 * Deletes the selected bonus questions.
	 * @param selectedIndices	The selected bonus questions
	 */
	private void deleteBonusQuestion(int[] selectedIndices) {
		if (selectedIndices.length == 0) {
			JOptionPane.showMessageDialog(this, Constants.NONE_SELECTED_MESSAGE, Constants.NONE_SELECTED_TITLE, JOptionPane.ERROR_MESSAGE);
		} else {
			int confirmationResponse = JOptionPane.showInternalConfirmDialog(this.getParent().getParent(), QUESTION_DELETION_CONF);
			if (confirmationResponse == JOptionPane.YES_OPTION) {
				int itemsRemoved = 0;
				for (int i = 0; i < selectedIndices.length; i++) {
					questionsListModel.remove(selectedIndices[i] - itemsRemoved);
					itemsRemoved++;
				}
			}
		}
	}

	/**
	 * Modifies the first selected bonus question.
	 * @param selectedIndex	The first selected bonus question
	 */
	private void modifyBonusQuestion(int selectedIndex) {
		// TODO Auto-generated method stub
		this.repaint();
	}

	/**
	 * Adds a new bonus question.
	 */
	private void addBonusQuestion() {
		questionsListModel.addElement(constructBonusQuestion());
	}

	/**
	 * Constructs a new bonus question.
	 * @return
	 */
	private Object constructBonusQuestion() {
		return "BonusQuestion " + questionsListModel.size();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.ADD)) {
			addBonusQuestion();
		} else if (e.getActionCommand().equals(Constants.MODIFY)) {
			modifyBonusQuestion(questionsList.getSelectedIndex());
		} else if (e.getActionCommand().equals(Constants.DELETE)) {
			deleteBonusQuestion(questionsList.getSelectedIndices());
		} else if (e.getActionCommand().equals(SHORT_ANSWER)) {
			shortAnswer.setSelected(true);
			multipleChoice.setSelected(false);
			if(this.multipleChoiceMode == true) {
				setModalitySA();
			}
		} else if (e.getActionCommand().equals(MULTIPLE_CHOICE)) {
			multipleChoice.setSelected(true);
			shortAnswer.setSelected(false);
			if(this.multipleChoiceMode == false) {
				setModalityMC();
			}
		} else if (e.getActionCommand().matches("[A-H]")) {
			//deselect all MC letters
			for (int i = 0; i < multipleChoiceOptions.length; i++) {
				multipleChoiceOptions[i].setSelected(false);
			}

			//select the correct letter
			multipleChoiceOptions[((int)e.getActionCommand().charAt(0))-65].setSelected(true);
			//TODO make modifications only affect selected letter
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (!source.getValueIsAdjusting()) {
			setNumAnswers((int)source.getValue());
		}   
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	@Override
	public void repaint() {
		if (this.getParent() != null) {
			this.getParent().repaint();
		}
	}
}
