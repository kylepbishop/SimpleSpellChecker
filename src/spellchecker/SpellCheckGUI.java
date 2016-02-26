/**
 * The GUI for the SpellChecker application
 */

package spellchecker;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class SpellCheckGUI extends JFrame
{
	private JFrame frame;
	private JTextField txtWordsnotadded;
	private JTextField txtWordsadded;
	private static SpellCheck spellCheck;
	DefaultListModel listOfWords = new DefaultListModel();
	DefaultListModel listOfInputs = new DefaultListModel();
	boolean fileSelected = false;
	boolean outputNamed = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		spellCheck = new SpellCheck();
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					SpellCheckGUI window = new SpellCheckGUI();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SpellCheckGUI()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		/*
		 * Create all elements of the UI
		 */
		JPanel panelResults = new JPanel();
		JPanel filePanel = new JPanel();
		JLabel lblSpellcheckComplete = new JLabel("Spellcheck complete.");
		JLabel lblChooseFile = new JLabel("Choose a file:");
		JButton btnChoose = new JButton("Choose");
		JList inputList = new JList(listOfInputs);
		JScrollPane inputFileListScroll = new JScrollPane();
		JButton btnRemove = new JButton("Remove");
		JButton btnDone = new JButton("Done");
		JLabel lblWordsNotAdded = new JLabel(
				"Words not added output file name:");
		JButton btnUpdate = new JButton("Update");
		JLabel lblWordsAdded = new JLabel("Words added output file name:");
		JPanel misspelledPanel = new JPanel();
		JLabel lblMisspelled = new JLabel("Misspelled?");
		JLabel lblWord = new JLabel("Word list");
		JList list = new JList(listOfWords);
		JButton btnNo = new JButton("No");
		JButton btnYes = new JButton("Yes");
		JScrollPane scrollPane = new JScrollPane(list);
		JButton btnExit = new JButton("Exit");
		JLabel lblSpellcheckMoreFiles = new JLabel("Spellcheck more files?");
		JButton btnContinue = new JButton("Yes");

		/*
		 * set up the functionality of the UI
		 */
		frame = new JFrame();
		frame.setBounds(100, 100, 554, 224);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		panelResults.setVisible(false);
		JButton btnSpellCheck = new JButton("Run Spell Check");
		btnSpellCheck.setBounds(7, 150, 465, 23);
		btnSpellCheck.setVisible(false);
		btnSpellCheck.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				fileSelected = false;
				outputNamed = false;
				while (inputList.getModel().getSize() > 0)
				{
					spellCheck.setMisspelledWords(new Dictionary());
					spellCheck.setMisspelledWords(spellCheck
							.compareDictionaryTo(spellCheck.getInputFileDict(),
									spellCheck.getMisspelledWords()));
				}
				spellCheck.setMisspelledWords(spellCheck
						.compareDictionaryTo(spellCheck.getInputFileDict()));
				spellCheck.getMisspelledWords().sort();
				spellCheck.getMisspelledWords().removeDuplicates();
				for (int alphaLoc = 0; alphaLoc < 26; alphaLoc++)
				{
					for (String word : spellCheck.getMisspelledWords()
							.getWords()[alphaLoc])
					{
						listOfWords.addElement(word);
					}
				}
				list.setSelectedIndex(list.getFirstVisibleIndex());
				btnSpellCheck.setVisible(false);
				filePanel.setVisible(false);
				if (listOfWords.isEmpty())
				{
					lblSpellcheckComplete
							.setText("There were no misspelled words");
					panelResults.setVisible(true);
				}
				else
				{
					misspelledPanel.setVisible(true);
				}
			}
		});
		frame.getContentPane().add(btnSpellCheck);
		panelResults.setBounds(7, 7, 465, 171);
		frame.getContentPane().add(panelResults);
		filePanel.setBounds(7, 7, 465, 171);
		frame.getContentPane().add(filePanel);
		filePanel.setLayout(new MigLayout("", "[168px][204px][71px]", "[27px][4px][23px][4px][33px][20px][4px][20px]"));
		filePanel.add(lblChooseFile, "cell 0 0,alignx right,aligny center");
		filePanel.add(inputFileListScroll, "cell 1 0 1 5,grow");
		inputFileListScroll.setViewportView(inputList);
		filePanel.add(btnChoose, "cell 2 0,growx,aligny bottom");
		btnRemove.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (!inputList.isSelectionEmpty())
				{
					listOfInputs.remove(inputList.getSelectedIndex());
				}
			}
		});
		filePanel.add(btnRemove, "cell 2 2,alignx left,aligny top");
		btnDone.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (!listOfInputs.isEmpty())
				{
					try
					{
						btnDone.setVisible(false);
						btnChoose.setVisible(false);
						btnRemove.setVisible(false);
						inputFileListScroll.setVisible(false);
						lblChooseFile.setVisible(false);
						spellCheck.setInputFileDict(spellCheck.loadInputFile(
								(String) listOfInputs.elementAt(0)));
						listOfInputs.remove(0);
						while (!listOfInputs.isEmpty())
						{
							spellCheck.setInputFileDict(spellCheck
									.getInputFileDict()
									.combineWith(spellCheck
											.loadInputFile((String) listOfInputs
													.elementAt(0))));
							listOfInputs.remove(0);
						}
						fileSelected = true;
						if (outputNamed && fileSelected)
						{
							btnSpellCheck.setVisible(true);
						}
					} catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		filePanel.add(btnDone, "cell 2 4,growx,aligny top");
		filePanel.add(lblWordsNotAdded, "cell 0 5,alignx left,aligny center");
		txtWordsnotadded = new JTextField();
		filePanel.add(txtWordsnotadded, "cell 1 5,growx,aligny top");
		txtWordsnotadded.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent evt)
			{
				spellCheck.setNotAddedFileName(txtWordsnotadded.getText());
				spellCheck.setNotAddedDict(new Dictionary());
			}
		});
		txtWordsnotadded.setText("wordsNotAdded.txt");
		txtWordsnotadded.setColumns(10);
		filePanel.add(btnUpdate, "cell 2 5 1 3,growx,aligny center");
		btnUpdate.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				spellCheck.setNotAddedFileName(txtWordsnotadded.getText());
				spellCheck.setNotAddedDict(new Dictionary());
				spellCheck.setAddedFileName(txtWordsadded.getText());
				spellCheck.setAddedDict(new Dictionary());
				outputNamed = true;
				txtWordsnotadded.setVisible(false);
				txtWordsadded.setVisible(false);
				lblWordsAdded.setVisible(false);
				lblWordsNotAdded.setVisible(false);
				btnUpdate.setVisible(false);
				if (outputNamed && fileSelected)
				{
					btnSpellCheck.setVisible(true);
				}
			}
		});
		filePanel.add(lblWordsAdded, "cell 0 7,alignx right,aligny center");
		txtWordsadded = new JTextField();
		filePanel.add(txtWordsadded, "cell 1 7,growx,aligny top");
		txtWordsadded.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent evt)
			{
				spellCheck.setAddedFileName(txtWordsadded.getText());
				spellCheck.setAddedDict(new Dictionary());
			}
		});
		txtWordsadded.setText("wordsAdded.txt");
		txtWordsadded.setColumns(10);
		btnChoose.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				FileFilter filter = new FileNameExtensionFilter("Text Files","txt");
				final JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileFilter(filter);
				int returnVal = jFileChooser.showOpenDialog(SpellCheckGUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File file = jFileChooser.getSelectedFile();
					listOfInputs.addElement(file.getPath());
				}
			}
		});
		misspelledPanel.setBounds(7, 7, 465, 171);
		misspelledPanel.setVisible(false);
		frame.getContentPane().add(misspelledPanel);
		misspelledPanel.setLayout(new MigLayout("", "[53px][275px][66px][45px]", "[23px][130px]"));
		misspelledPanel.add(lblMisspelled, "cell 0 0,alignx left,aligny center");
		misspelledPanel.add(lblWord, "cell 1 0,growx,aligny center");
		misspelledPanel.add(btnYes, "cell 2 0,growx,aligny top");
		btnYes.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				spellCheck.yesButton((String) listOfWords
						.getElementAt(list.getSelectedIndex()));
				listOfWords.removeElementAt(list.getSelectedIndex());
				list.setSelectedIndex(list.getFirstVisibleIndex());
				if (list.getModel().getSize() == 0)
				{
					spellCheck.getNotAddedDict()
							.saveFile(spellCheck.getNotAddedFileName());
					spellCheck.getAddedDict()
							.saveFile(spellCheck.getAddedFileName());
					spellCheck.saveDictionary();
					panelResults.setVisible(true);
					misspelledPanel.setVisible(false);
				}
			}
		});
		misspelledPanel.add(btnNo, "cell 3 0,alignx left,aligny top");
		btnNo.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				spellCheck.noButton((String) listOfWords
						.getElementAt(list.getSelectedIndex()));
				listOfWords.removeElementAt(list.getSelectedIndex());
				list.setSelectedIndex(0);
				if (list.getModel().getSize() == 0)
				{
					spellCheck.getNotAddedDict()
							.saveFile(spellCheck.getNotAddedFileName());
					spellCheck.getAddedDict()
							.saveFile(spellCheck.getAddedFileName());
					spellCheck.saveDictionary();
					panelResults.setVisible(true);
					misspelledPanel.setVisible(false);
				}
			}
		});
		misspelledPanel.add(scrollPane, "cell 1 1,growx,aligny top");
		scrollPane.setViewportView(list);
		btnExit.setBounds(475, 150, 57, 23);
		btnExit.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				System.exit(0);
			}
		});
		frame.getContentPane().add(btnExit);
		panelResults.setLayout(new MigLayout("", "[103px][49px]", "[14px][23px]"));
		panelResults.add(lblSpellcheckComplete, "cell 0 0,alignx left,aligny top");
		panelResults.add(lblSpellcheckMoreFiles, "cell 0 1,alignx left,aligny center");
		btnContinue.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				lblChooseFile.setVisible(true);
				inputFileListScroll.setVisible(true);
				btnChoose.setVisible(true);
				btnRemove.setVisible(true);
				btnDone.setVisible(true);
				txtWordsnotadded.setVisible(true);
				txtWordsadded.setVisible(true);
				lblWordsAdded.setVisible(true);
				lblWordsNotAdded.setVisible(true);
				btnUpdate.setVisible(true);
				filePanel.setVisible(true);
				misspelledPanel.setVisible(false);
				panelResults.setVisible(false);
				lblSpellcheckComplete.setText("Spellcheck complete.");
			}
		});
		panelResults.add(btnContinue, "cell 1 1,alignx left,aligny top");
	}
}
