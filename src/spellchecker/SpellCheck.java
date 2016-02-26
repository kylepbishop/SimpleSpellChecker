/**
 * This class holds the Dictionary class as well as the SpellCheck class
 * The Dictionary Class is a data structure to hold a list of alphabetic
 * ordered words.
 * The SpellCheck class checks input text files against a dictionary.
 */
package spellchecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.io.File;
import java.util.Scanner;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

/**
 * Dictionary is a data structure comprised of an array that holds ArrayList
 * <String>s
 */
class Dictionary
{
	private static final int DICTIONARYSIZE = 26;
	private static final int INCORRECT = -1;
	private ArrayList<String>[] words;

	/**
	 * The constructor for the Dictionary structure, goes through the array and
	 * sets up each ArrayList as empty
	 */
	public Dictionary()
	{
		words = new ArrayList[DICTIONARYSIZE];
		for (int category = 0; category < words.length; category++)
		{
			words[category] = new ArrayList<String>();
		}
	}

	/**
	 * getCategoryOf is a function to be run on a word it returns which index of
	 * the array that holds the ArrayLists in which the word belongs
	 * 
	 * @param word
	 * @return
	 */
	private int getCategoryOf(String word)
	{
		if (word.length() > 0)
		{
			switch (word.charAt(0))
			{
			case 'a':
				return 0;
			case 'b':
				return 1;
			case 'c':
				return 2;
			case 'd':
				return 3;
			case 'e':
				return 4;
			case 'f':
				return 5;
			case 'g':
				return 6;
			case 'h':
				return 7;
			case 'i':
				return 8;
			case 'j':
				return 9;
			case 'k':
				return 10;
			case 'l':
				return 11;
			case 'm':
				return 12;
			case 'n':
				return 13;
			case 'o':
				return 14;
			case 'p':
				return 15;
			case 'q':
				return 16;
			case 'r':
				return 17;
			case 's':
				return 18;
			case 't':
				return 19;
			case 'u':
				return 20;
			case 'v':
				return 21;
			case 'w':
				return 22;
			case 'x':
				return 23;
			case 'y':
				return 24;
			case 'z':
				return 25;
			}
		}
		return INCORRECT;
	}

	/**
	 * addWord takes a String wordToAdd as a parameter and adds it into the
	 * proper ArrayList
	 * 
	 * @param wordToAdd
	 */
	public void addWord(String wordToAdd)
	{
		String lowerCaseWord = wordToAdd.toLowerCase();
		lowerCaseWord = lowerCaseWord.replaceAll("[^A-Za-z0-9]", "");
		if (getCategoryOf(lowerCaseWord) != INCORRECT)
		{
			words[getCategoryOf(lowerCaseWord)].add(lowerCaseWord);
		}
	}

	/**
	 * The sort function goes through the array and sorts each individual
	 * ArrayList
	 */
	public void sort()
	{
		for (int category = 0; category < DICTIONARYSIZE; category++)
		{
			Collections.sort(words[category]);
		}
	}

	/**
	 * removeDuplicates goes through the array and removes the duplicates within
	 * each ArrayList
	 */
	public void removeDuplicates()
	{
		for (int category = 0; category < DICTIONARYSIZE; category++)
		{
			Set tempSet = new HashSet(words[category]);
			words[category].clear();
			words[category] = new ArrayList(tempSet);
		}
	}

	/**
	 * saveFile takes a String fileName as a parameter and stores the contents
	 * of the Dictionary structure and saves it into a text file located at the
	 * fileName given
	 * 
	 * @param fileName
	 */
	public void saveFile(String fileName)
	{
		try
		{
			BufferedWriter buffWriter = new BufferedWriter(
					new FileWriter(fileName));
			for (int category = 0; category < DICTIONARYSIZE; category++)
			{
				for (String word : words[category])
				{
					buffWriter.write(word);
					buffWriter.newLine();
				}
			}
			buffWriter.flush();
			buffWriter.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * compareTo takes another Dictionary structure as a parameter and compares
	 * the contents of both Dictionaries returning a new Dictionary structure
	 * that contains all the words that were in the otherDictionary but not this
	 * Dictionary
	 * 
	 * @param otherDictionary
	 * @return
	 */
	public Dictionary compareTo(Dictionary otherDictionary)
	{
		Dictionary notInDictionary = new Dictionary();
		for (int category = 0; category < DICTIONARYSIZE; category++)
		{
			notInDictionary.getWords()[category] = new ArrayList<>(
					otherDictionary.getWords()[category]);
			notInDictionary.getWords()[category].removeAll(words[category]);
		}
		return notInDictionary;
	}

	/**
	 * combineWith takes a Dictionary otherDictionary as a parameter and
	 * combines the two Dictionaries then returns the combination
	 * 
	 * @param otherDictionary
	 * @return
	 */
	public Dictionary combineWith(Dictionary otherDictionary)
	{
		for (int category = 0; category < DICTIONARYSIZE; category++)
		{
			for (int otherCategory = 0; otherCategory < otherDictionary
					.getWords()[category].size(); otherCategory++)
			{
				words[category].add(otherDictionary.getWords()[category]
						.get(otherCategory));
			}
		}
		return this;
	}

	/**
	 * compareTo takes in a Dictionary input and a Dictionary
	 * existingMisspelledWords as parameters and adds any words that are in
	 * input and not in this Dictionary to the existingMisspelledWords
	 * Dictionary then returns the existingMisspelledWords Dictionary
	 * 
	 * @param input
	 * @param existingMisspelledWords
	 * @return
	 */
	public Dictionary compareTo(Dictionary input,
			Dictionary existingMisspelledWords)
	{
		for (int category = 0; category < DICTIONARYSIZE; category++)
		{
			existingMisspelledWords.combineWith(input);
			existingMisspelledWords.getWords()[category]
					.removeAll(words[category]);
		}
		return existingMisspelledWords;
	}

	/**
	 * Returns a string version of this Dictionary
	 */
	public String toString()
	{
		String result = "";
		for (int category = 0; category < DICTIONARYSIZE; category++)
		{
			for (String word : words[category])
			{
				result = result + word + "\n";
			}
		}
		return result;
	}

	/**
	 * returns a String array made of the contents of this Dictionary
	 * 
	 * @return
	 */
	public String[] toArray()
	{
		int count = 0;
		for (int currentCategory = 0; currentCategory < DICTIONARYSIZE; currentCategory++)
		{
			for (String word : words[currentCategory])
			{
				count++;
			}
		}
		String[] dictArray = new String[count];
		int currentWord = 0;
		for (int currentCategory = 0; currentCategory < DICTIONARYSIZE; currentCategory++)
		{
			for (String word : words[currentCategory])
			{
				dictArray[currentWord] = word;
				currentWord++;
			}
		}
		return dictArray;
	}

	/**
	 * returns the array of ArrayLists
	 * 
	 * @return
	 */
	public ArrayList<String>[] getWords()
	{
		return words;
	}

	/**
	 * takes an array of ArrayLists and replaces this Dictionary's array of
	 * ArrayLists with it
	 * 
	 * @param words
	 */
	public void setWords(ArrayList<String>[] words)
	{
		this.words = words;
	}
}

/**
 * The SpellCheck class uses the Dictionary class to perform a spell check on a
 * text file
 */
public class SpellCheck
{
	private static final int DICTIONARYSIZE = 26;
	private static final String DICTIONARYNAME = "dictionary.txt";
	private Dictionary dict;
	private Dictionary inputFileDict;
	private Dictionary notAddedDict;
	private Dictionary addedDict;
	private String notAddedFileName;
	private String addedFileName;
	private String inputFileName;
	private boolean inputSet = false;
	private boolean output1Set = false;
	private boolean output2Set = false;
	private Dictionary misspelledWords;

	/**
	 * SpellCheck constructor sets up an empty Dictionary called dict the
	 * Dictionary that all input will be compared against then it loads and
	 * sorts the dictionary. Afterward it creates empty Dictionary Structures to
	 * contain the input and two output files
	 */
	public SpellCheck()
	{
		dict = new Dictionary();
		loadDictionary();
		sortDictionary();
		setInputFileDict(new Dictionary());
		setNotAddedDict(new Dictionary());
		setAddedDict(new Dictionary());
	}

	/**
	 * saves the words added output file
	 */
	public void saveAddedOutputFile()
	{
		getAddedDict().saveFile(getAddedFileName());
	}

	/**
	 * saves the words not added output file
	 */
	public void saveNotAddedOutputFile()
	{
		getNotAddedDict().saveFile(getNotAddedFileName());
	}

	/**
	 * takes the parameter String fileName and sets the name of the output file
	 * words not added to fileName
	 * 
	 * @param fileName
	 */
	public void setNameOfOutputWordsNotAdded(String fileName)
	{
		setNotAddedFileName(fileName);
	}

	/**
	 * takes the parameter String fileName and sets the name of the output file
	 * words added to fileName
	 * 
	 * @param fileName
	 */
	public void setNameOfOutputWordsAdded(String fileName)
	{
		setAddedFileName(fileName);
	}

	/**
	 * compares the input Dictionary to the dictionary Dictionary file sets the
	 * misspelledWords Dictionary to the results
	 */
	public void generateMisspelledWords()
	{
		setMisspelledWords(compareDictionaryTo(getInputFileDict()));
	}

	/**
	 * sorts the actual dictionary Dictionary
	 */
	public void sortDictionary()
	{
		dict.sort();
	}

	/**
	 * function devised for the GUI, it takes a String word as a parameter and
	 * acts as though it is not misspelled so it adds it to the dictionary
	 * Dictionary and the added words Dictionary
	 * 
	 * @param word
	 */
	public void noButton(String word)
	{
		dict.addWord(word);
		getAddedDict().addWord(word);
	}

	/**
	 * devised for the GUI, takes a String word as a parameter and acts as
	 * though it is misspelled so it adds it only to the words not added
	 * Dictionary
	 * 
	 * @param word
	 */
	public void yesButton(String word)
	{
		getNotAddedDict().addWord(word);
	}

	/**
	 * compares an inputFile Dictionary against the dictionary Dictionary
	 * returns the differences, used for when there is no existing misspelled
	 * words Dictionary
	 * 
	 * @param inputFile
	 * @return
	 */
	public Dictionary compareDictionaryTo(Dictionary inputFile)
	{
		return dict.compareTo(inputFile);
	}

	/**
	 * compares an inputFile Dictionary against the dictionary Dictionary
	 * returns the differences, used for when there is already an existing
	 * misspelledWords Dictionary
	 * 
	 * @param input
	 * @param existingMisspelled
	 * @return
	 */
	public Dictionary compareDictionaryTo(Dictionary input,
			Dictionary existingMisspelled)
	{
		return dict.compareTo(input, existingMisspelled);
	}

	/**
	 * compares an inputFile Dictionary against the dictionary Dictionary
	 * returns the differences, used for when there is already an existing
	 * misspelledWords Dictionary
	 * 
	 * @param input
	 * @param existingMisspelled
	 * @return
	 */
	public Dictionary loadInputFile(String fileName)
	{
		Dictionary inputDict = null;
		try
		{
			File inputFile = new File(fileName);
			Scanner input = new Scanner(inputFile);
			inputDict = new Dictionary();
			while (input.hasNext())
			{
				inputDict.addWord(input.next());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return inputDict;
	}

	/**
	 * loads the dictionary Dictionary from the source directory
	 */
	public void loadDictionary()
	{
		try
		{
			File file = new File("dictionary.txt");
			if (!file.exists())
			{
				file.createNewFile();
			}
			Scanner fileInput = new Scanner(file);
			while (fileInput.hasNext())
			{
				dict.addWord(fileInput.next());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * saves the dictionary Dictionary to a text file
	 */
	public void saveDictionary()
	{
		dict.saveFile("dictionary.txt");
	}

	/**
	 * returns a string version of the dictionary Dictionary
	 */
	public String toDictionaryString()
	{
		return dict.toString();
	}

	/**
	 * function that returns the misspelledWords Dictionary
	 * 
	 * @return
	 */
	public Dictionary getMisspelledWords()
	{
		return misspelledWords;
	}

	/**
	 * returns the input Dictionary
	 * 
	 * @return
	 */
	public Dictionary getInputFileDict()
	{
		return inputFileDict;
	}

	/**
	 * sets the input Dictionary to the passed parameter Dictionary
	 * inputFileDict
	 * 
	 * @param inputFileDict
	 */
	public void setInputFileDict(Dictionary inputFileDict)
	{
		this.inputFileDict = inputFileDict;
	}

	/**
	 * gets the file name of the output file words not added
	 * 
	 * @return
	 */
	public String getNotAddedFileName()
	{
		return notAddedFileName;
	}

	/**
	 * sets the file name of the output file words not added
	 * 
	 * @param notAddedFileName
	 */
	public void setNotAddedFileName(String notAddedFileName)
	{
		this.notAddedFileName = notAddedFileName;
	}

	/**
	 * gets the file name of the output file words added
	 * 
	 * @return
	 */
	public String getAddedFileName()
	{
		return addedFileName;
	}

	/**
	 * sets the file name of the output file words added
	 * 
	 * @param addedFileName
	 */
	public void setAddedFileName(String addedFileName)
	{
		this.addedFileName = addedFileName;
	}

	/**
	 * gets the Dictionary of the output file words not added
	 * 
	 * @return
	 */
	public Dictionary getNotAddedDict()
	{
		return notAddedDict;
	}

	/**
	 * sets the Dictionary of the output file words not added
	 * 
	 * @param notAddedDict
	 */
	public void setNotAddedDict(Dictionary notAddedDict)
	{
		this.notAddedDict = notAddedDict;
	}

	/**
	 * gets the Dictionary of the output file words added
	 * 
	 * @return
	 */
	public Dictionary getAddedDict()
	{
		return addedDict;
	}

	/**
	 * sets the Dictionary of the output file words added
	 * 
	 * @param addedDict
	 */
	public void setAddedDict(Dictionary addedDict)
	{
		this.addedDict = addedDict;
	}

	/**
	 * sets misspelledWords Dictionary to the passed parameter Dictionary
	 * misspelledWords
	 * 
	 * @param misspelledWords
	 */
	public void setMisspelledWords(Dictionary misspelledWords)
	{
		this.misspelledWords = misspelledWords;
	}
}
