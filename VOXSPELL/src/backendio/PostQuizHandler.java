package backendio;

import java.util.ArrayList;

import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access" })

/**
 * Singleton class responsible for post-quiz logic
 * @author Abby S
 */
public class PostQuizHandler {
	private static Voxspell parent_frame;

	private static PostQuizHandler instance=null;

	/**
	 * Private constructor for single instance, reference parent frame
	 */
	private PostQuizHandler(Voxspell parent){
		parent_frame=parent;
	}

	/**
	 * Gets single instance of FileReadingHandler
	 * @param parent 	parent frame
	 * @return instance of itself (or creates one if first time called)
	 */
	public static PostQuizHandler getInstance(Voxspell parent){
		if (instance==null){
			instance=new PostQuizHandler(parent);
		}
		return instance;
	}

	/**
	 * Results for a quizzed word
	 */
	enum WordResult {
		Mastered, Faulted, Failed;
	}

	/**
	 * returns the list of words and their results from the just-completed quiz
	 * @return
	 */
	public ArrayList<ArrayList<String>> getLatestWordResults(){
		ArrayList<ArrayList<String>> to_return=new ArrayList<ArrayList<String>>();
		to_return.add(parent_frame.getDataHandler().getLatestMasteredWords());
		to_return.add(parent_frame.getDataHandler().getLatestFaultedWords());
		to_return.add(parent_frame.getDataHandler().getLatestFailedWords());
		return to_return;
	}

	/**
	 * processes results from a completed quiz
	 * @param mastered_words
	 * @param faulted_words
	 * @param failed_words
	 * @param quiz_type
	 */
	public void processQuizResults(ArrayList<String> mastered_words, ArrayList<String> faulted_words, ArrayList<String> failed_words, PanelID quiz_type, int quiz_length){
		enterResultsIntoDataStructure(mastered_words, WordResult.Mastered);
		enterResultsIntoDataStructure(faulted_words, WordResult.Faulted);
		enterResultsIntoDataStructure(failed_words, WordResult.Failed);

		if (quiz_type==PanelID.Review){
			removeFromReviewList(mastered_words);
			removeFromReviewList(faulted_words);
		} else {
			addToReviewList(failed_words);
		}

		parent_frame.getDataHandler().setLatestMasteredWords(mastered_words);
		parent_frame.getDataHandler().setLatestFaultedWords(faulted_words);
		parent_frame.getDataHandler().setLatestFailedWords(failed_words);
		parent_frame.getDataHandler().setLatestQuizLength(quiz_length);
		parent_frame.getFileWritingHandler().writeStats();
		parent_frame.getFileWritingHandler().writeToReview();
	}

	/**
	 * enters mastered, faulted, and failed results into data structure
	 * @param quizzed_words
	 * @param word_result_type
	 */
	private void enterResultsIntoDataStructure(ArrayList<String> quizzed_words, WordResult word_result_type){
		ArrayList<ArrayList<Integer>> persistent_data_structure;
		ArrayList<ArrayList<Integer>> session_data_structure;
		switch(word_result_type){
		case Mastered:
			persistent_data_structure=parent_frame.getDataHandler().getPersistentMasterCount();
			session_data_structure=parent_frame.getDataHandler().getSessionMasterCount();
			break;
		case Faulted:
			persistent_data_structure=parent_frame.getDataHandler().getPersistentFaultedCount();
			session_data_structure=parent_frame.getDataHandler().getSessionFaultedCount();
			break;
		default:
			persistent_data_structure=parent_frame.getDataHandler().getPersistentFailedCount();
			session_data_structure=parent_frame.getDataHandler().getSessionFailedCount();
			break;
		}

		for (String w:quizzed_words){
			//for persistent data structure
			int index=parent_frame.getDataHandler().getPersistentAllwords().get(parent_frame.getDataHandler().getCurrentLevel()).indexOf(w);
			int current_value;
			current_value=persistent_data_structure.get(parent_frame.getDataHandler().getCurrentLevel()).get(index);
			persistent_data_structure.get(parent_frame.getDataHandler().getCurrentLevel()).set(index, current_value+1);

			//for session data structure
			if (parent_frame.getDataHandler().getSessionWords().get(parent_frame.getDataHandler().getCurrentLevel()).contains(w)){
				index=parent_frame.getDataHandler().getSessionWords().get(parent_frame.getDataHandler().getCurrentLevel()).indexOf(w);
				current_value=session_data_structure.get(parent_frame.getDataHandler().getCurrentLevel()).get(index);
				session_data_structure.get(parent_frame.getDataHandler().getCurrentLevel()).set(index, current_value+1);
			} else {
				parent_frame.getDataHandler().getSessionWords().get(parent_frame.getDataHandler().getCurrentLevel()).add(w);
				parent_frame.getDataHandler().getSessionMasterCount().get(parent_frame.getDataHandler().getCurrentLevel()).add(0);
				parent_frame.getDataHandler().getSessionFaultedCount().get(parent_frame.getDataHandler().getCurrentLevel()).add(0);
				parent_frame.getDataHandler().getSessionFailedCount().get(parent_frame.getDataHandler().getCurrentLevel()).add(0);

				index=parent_frame.getDataHandler().getSessionWords().get(parent_frame.getDataHandler().getCurrentLevel()).indexOf(w);
				session_data_structure.get(parent_frame.getDataHandler().getCurrentLevel()).set(index, 1);
			}	
		}
	}

	/**
	 * Removes given list of words from reviewlist
	 */
	private void removeFromReviewList(ArrayList<String> not_failed_words){
		for (String w:not_failed_words){
			if (parent_frame.getDataHandler().getReviewlistWords().get(parent_frame.getDataHandler().getCurrentLevel()).contains(w)){
				parent_frame.getDataHandler().getReviewlistWords().get(parent_frame.getDataHandler().getCurrentLevel()).remove(w);
			}
		}
	}

	/**
	 * Adds list of words into reviewlist (failed words or via the add to review function in quiz)
	 */
	public void addToReviewList(ArrayList<String> failed_words){
		for (String w:failed_words){
			if (!parent_frame.getDataHandler().getReviewlistWords().get(parent_frame.getDataHandler().getCurrentLevel()).contains(w)){
				parent_frame.getDataHandler().getReviewlistWords().get(parent_frame.getDataHandler().getCurrentLevel()).add(w);
			}
		}
	}
}