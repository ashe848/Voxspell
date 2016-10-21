package backendio;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access" })

/**
 * Singleton class responsible for pre-quiz logic
 * @author Abby S
 */
public class PreQuizHandler {
	private static Voxspell parent_frame;

	private static PreQuizHandler instance=null;

	/**
	 * private constructor for single instance, references parent frame
	 */
	private PreQuizHandler(Voxspell parent){
		parent_frame=parent;
	}

	/**
	 * Gets single instance of FileReadingHandler
	 * @param parent 	parent frame
	 * @return instance of itself (or creates one if first time called)
	 */
	public static PreQuizHandler getInstance(Voxspell parent){
		if (instance==null){
			instance=new PreQuizHandler(parent);
		}
		return instance;
	}

	/**
	 * Method that generates random words for quiz
	 * @param number_of_words		words in quiz specified, will go to number of words in file if less than
	 * @return		array of randomised words to do test on
	 * 
	 * From Theo's Assignment 2
	 */
	public ArrayList<String> getWordsForSpellingQuiz(int number_of_words, PanelID id){
		//setup relevant bank of words to choose from depending on quiz type
		ArrayList<String> relevant_bank_of_words;
		ArrayList<String> to_return = new ArrayList<String>();

		switch(id){
		case Quiz:
			relevant_bank_of_words = parent_frame.getDataHandler().getWordlistWords().get(parent_frame.getDataHandler().getCurrentLevel());
			break; 
		default://Review
			relevant_bank_of_words = parent_frame.getDataHandler().getReviewlistWords().get(parent_frame.getDataHandler().getCurrentLevel());
			break;
		}

		if(relevant_bank_of_words.size() == 0){
			JOptionPane.showMessageDialog(null, "Empty list for level: "+parent_frame.getDataHandler().getLevelNames().get(parent_frame.getDataHandler().getCurrentLevel())+"\nWill return to main menu" ,"Nothing to test for " + id.toString(),JOptionPane.ERROR_MESSAGE);
			parent_frame.changePanel(PanelID.MainMenu);
		} else {
			//randomising of words without messing with order of original array (no deep copy needed)
			int[] indices = new int[relevant_bank_of_words.size()];
			for(int i = 0; i<relevant_bank_of_words.size(); i++){
				indices[i] = i;
			}

			shuffleIndicesArray(indices);

			for(int i = 0; i<relevant_bank_of_words.size() && i<number_of_words; i++){
				to_return.add(relevant_bank_of_words.get(indices[i]));
			}
		}
		return to_return;
	}

	/**
	 * Shuffles indices of array, so it doesn't mess with pointers. No deep copying needed
	 * @param indices		array to shuffle
	 * 
	 * From Theo's Assignment 2
	 */
	private void shuffleIndicesArray(int[] indices){
		Random rand = new Random();

		//swaps 2 random elements 100 times
		for(int i = 0; i<100; i++){
			int position1 = rand.nextInt(indices.length);
			int position2 = rand.nextInt(indices.length);

			int temp = indices[position1];
			indices[position1] = indices[position2];
			indices[position2] = temp;
		}
	}
}