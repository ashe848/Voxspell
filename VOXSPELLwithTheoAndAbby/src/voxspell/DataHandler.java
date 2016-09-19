package voxspell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import voxspell.Festival.FestivalSpeed;
import voxspell.Festival.FestivalVoice;
import voxspell.StatsChooser.StatsType;
import voxspell.Voxspell.PanelID;

public class DataHandler {
	private static Voxspell parent_frame;

	private static DataHandler instance=null;

	private static String festival_scheme;
	private static String spelling_list;
	private static String statsfile;
	private static String reviewlist;
	private static String settings;

	private static ArrayList<ArrayList<String>> wordlist_words;
	private static ArrayList<ArrayList<String>> reviewlist_words;

	private static ArrayList<ArrayList<String>> persistent_allwords;
	private static ArrayList<ArrayList<Integer>> persistent_master_count;
	private static ArrayList<ArrayList<Integer>> persistent_faulted_count;
	private static ArrayList<ArrayList<Integer>> persistent_failed_count;

	private static ArrayList<ArrayList<String>> session_words;
	private static ArrayList<ArrayList<Integer>> session_master_count;
	private static ArrayList<ArrayList<Integer>> session_faulted_count;
	private static ArrayList<ArrayList<Integer>> session_failed_count;

	private static ArrayList<String> latest_mastered_words;
	private static ArrayList<String> latest_faulted_words;
	private static ArrayList<String> latest_failed_words;

	private static int current_level=0; //initialised so if settings file is empty/wiped

	/**
	 * Constructor
	 */
	private DataHandler(Voxspell parent){
		parent_frame=parent;

		readFiles();
	}

	public static DataHandler getInstance(Voxspell parent){
		if (instance==null){
			instance=new DataHandler(parent);
		}
		return instance;
	}



	/*
	 * FOR READING FILES
	 */
	/**
	 * Method that reads contents of files and stores them into data structure.
	 * Files read from:
	 * 		NZ Spelling list text file		contents put into wordlist_words arraylist of arraylist(string) by level
	 * 		Statistics file					contents put into persistent{allwords, masteredcount, faultedcount, failedcount}
	 * 		Reviewlist words file			contents put into reviewlist_words arraylist of arraylist(String) by level
	 * 		Settings file					contents put into current_level field
	 */
	private static void readFiles(){
		setupFiles();
		declareDataStructures();
		readInWordlist();
		initialiseDataStructures();
		readInStatsFile();
		addWordsNotInStats();
		readInReviewList();
		readInSettings();
	}

	/**
	 * checks resource and data files
	 */
	private static void setupFiles() {
		//if resources folder can't be found, abort program now instead of get exceptions thrown everywhere
		//no barrier against if a png was deleted (could test for all contents and abort program, but that's not the purpose of this project)
		File resources_folder = new File(parent_frame.getResourceFileLocation());
		if (!resources_folder.exists()) {
			JOptionPane.showMessageDialog(null, "Fatal Error\nThe necessary resources folder has been removed\nAborting", "Fatal Error", JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}
		
		festival_scheme = parent_frame.getResourceFileLocation()+"festival.scm";
		spelling_list = parent_frame.getResourceFileLocation()+"NZCER-spelling-lists.txt";
		reviewlist = parent_frame.getResourceFileLocation()+"reviewlist";
		statsfile = parent_frame.getResourceFileLocation()+"statsfile";
		settings = parent_frame.getResourceFileLocation()+"settings";

		//creates files that can be empty at this point (them not existing is an issue that won't crash the program)
		try {	
			File festival_scheme_file = new File(festival_scheme);
			if (!festival_scheme_file.exists()) {
				festival_scheme_file.createNewFile();				
			}
			
			File review_list = new File(reviewlist);
			if (!review_list.exists()) {
				review_list.createNewFile();				
			}

			File stats_file = new File(statsfile);
			if (!stats_file.exists()) {
				stats_file.createNewFile();				
			}

			File settings_file = new File(settings);
			if (!settings_file.exists()) {
				settings_file.createNewFile();				
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fatal error concerning data files\nAborting", "Fatal Error", JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * Sets up data structure infrasture
	 */
	private static void declareDataStructures() {
		wordlist_words = new ArrayList<ArrayList<String>>();
		reviewlist_words = new ArrayList<ArrayList<String>>();

		persistent_allwords = new ArrayList<ArrayList<String>>();
		persistent_master_count = new ArrayList<ArrayList<Integer>>();
		persistent_faulted_count = new ArrayList<ArrayList<Integer>>();
		persistent_failed_count = new ArrayList<ArrayList<Integer>>();

		session_words = new ArrayList<ArrayList<String>>();
		session_master_count = new ArrayList<ArrayList<Integer>>();
		session_faulted_count = new ArrayList<ArrayList<Integer>>();
		session_failed_count = new ArrayList<ArrayList<Integer>>();
	}

	/**
	 * wordlist into wordlist arraylist of arraylists by level
	 */
	private static void readInWordlist() {
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(spelling_list));
			String string_input;
			ArrayList<String> temp_string_array = new ArrayList<String>();

			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					if (string_input.charAt(0)=='%'){ //new level, offload previous level
						wordlist_words.add(temp_string_array);
						temp_string_array= new ArrayList<String>();	
					} else { //add to list for current level
						temp_string_array.add(string_input.trim());
					}
				}
			}
			wordlist_words.add(temp_string_array); //offload final level

			current_BR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * initialise ArrayLists for data structure based on how many levevls in wordlist
	 */
	private static void initialiseDataStructures() {
		for(int i=0; i<wordlist_words.size();i++){
			persistent_allwords.add(new ArrayList<String>());
			persistent_master_count.add(new ArrayList<Integer>());
			persistent_faulted_count.add(new ArrayList<Integer>());
			persistent_failed_count.add(new ArrayList<Integer>());
			session_words.add(new ArrayList<String>());
			session_master_count.add(new ArrayList<Integer>());
			session_faulted_count.add(new ArrayList<Integer>());
			session_failed_count.add(new ArrayList<Integer>());
			reviewlist_words.add(new ArrayList<String>());
		}
	}

	/**
	 * statsfile contains all words in and their stats (will be 0s if not attempted)
	 */
	private static void readInStatsFile() {
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(statsfile));

			int word_level, mastered_count, faulted_count, failed_count;
			String string_input;
			String word;
			String[] split_line;

			while ((string_input = current_BR.readLine()) != null) {
				//if not empty line
				if (!string_input.isEmpty()){
					word = "";
					split_line = string_input.split(" ");
					word_level = Integer.valueOf(split_line[0]);
					mastered_count  = Integer.valueOf(split_line[1]);
					faulted_count = Integer.valueOf(split_line[2]);
					failed_count = Integer.valueOf(split_line[3]);

					//for words that include spaces
					for(int i=4; i<split_line.length; i++){
						word+=split_line[i];
						word+=" ";
					}
					word=word.trim();//trim trailing white space

					//add word and it's stats to the data structures
					persistent_allwords.get(word_level).add(word);
					persistent_master_count.get(word_level).add(mastered_count);
					persistent_faulted_count.get(word_level).add(faulted_count);
					persistent_failed_count.get(word_level).add(failed_count);
				}
			}

			current_BR.close(); //close statsfile
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * adds words from wordlist that not in statsfile into persistent structures
	 * if wordlist hasn't been used before (and therefore there statsfile is empty)
	 * or if wordlist has had additional words added to it since last loaded
	 */
	private static void addWordsNotInStats() {
		for(int word_level = 0; word_level<wordlist_words.size(); word_level++){
			for(String w: wordlist_words.get(word_level)){
				if(!persistent_allwords.get(word_level).contains(w)){
					persistent_allwords.get(word_level).add(w);
					persistent_master_count.get(word_level).add(0);
					persistent_faulted_count.get(word_level).add(0);
					persistent_failed_count.get(word_level).add(0);
				}
			}
		}
	}

	/**
	 * add words to review from file into data structure based on level
	 */
	private static void readInReviewList() {
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(reviewlist));

			int level = 0;
			String string_input;
			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					if (string_input.charAt(0)=='%'){
						level++;
					} else {
						reviewlist_words.get(level).add(string_input);
					}
				}
			}

			current_BR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readInSettings() {
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(settings));

			//set defaults for festival
			FestivalSpeed speed=FestivalSpeed.normal;
			FestivalVoice voice=FestivalVoice.Kiwi;

			String string_input;
			while ((string_input = current_BR.readLine()) != null) {
				String[] split_line = string_input.split(" ");
				current_level=Integer.parseInt(split_line[0]);

				//not first time launching, so will have saved festival settings
				if(current_level<getNumberOfLevels() && current_level>0){
					//get festival speed
					String speed_string=split_line[1];
					switch(speed_string){
					case "1.0":
						speed=FestivalSpeed.fast;
						break;
					case "2.0":
						speed=FestivalSpeed.slow;
						break;
					}

					//get festival voice
					String voice_string=split_line[2];
					switch (voice_string) {
					case "kal_diphone":
						voice=FestivalVoice.American;
						break;
					}		
				} else {
					current_level=0;
				}
			}

			current_BR.close();

			parent_frame.getFestival().setFestivalSpeed(speed);
			parent_frame.getFestival().setFestivalVoice(voice);
			if (current_level==0){
				chooseLevel("");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/*
	 * FOR WRITING TO FILES
	 */
	/**
	 * writes to stats file in the format
	 * <word level> <mastered count> <faulted count> <failed count> <the word>
	 */
	private void writeStats(){
		try {
			FileWriter fw = new FileWriter(new File(statsfile), false);
			for (int i=0; i<getNumberOfLevels(); i++){
				for (Object[] o:returnWordDataForLevel(i, StatsType.Persistent)){
					fw.write(o[0]+" "+ o[2]+" "+o[3]+" "+o[4]+" "+o[1]+"\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes to reviewlist with %Level number between the levels
	 */
	private void writeToReview(){
		try {
			FileWriter fw = new FileWriter(new File(reviewlist), false);
			for (int i=1; i<getNumberOfLevels(); i++){
				fw.write("%Level "+i+"\n");
				for (String w:reviewlist_words.get(i)){
					fw.write(w+"\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes to settings file in the format
	 * <current level> <festival speed> <festival voice>
	 */
	public static void writeToSettings(){
		try {
			FileWriter fw = new FileWriter(new File(settings), false);
			fw.write(current_level+" "+parent_frame.getFestival().getFestivalSpeed().getSpeedValue()+" "+parent_frame.getFestival().getFestivalVoice().getVoiceValue());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes to scheme file to be executed by Festival in batch mode
	 * sets voice, speed, and text to say
	 */
	public void writeToScheme(String speech, FestivalSpeed speed, FestivalVoice voice){
		try {
			FileWriter fw = new FileWriter(new File(festival_scheme), false);
			fw.write("(voice_" + voice.getVoiceValue() +")\n");
			fw.write("(Parameter.set 'Duration_Stretch " + speed.getSpeedValue() +")\n");
			fw.write("(SayText \""+speech+"\")");			
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * wipe files and calls read files to read in empty into the data structures
	 * will be as if this was the user's first launch
	 */
	public void clearFiles() {
		try {
			FileWriter fw = new FileWriter(new File(reviewlist), false);
			fw.close();
			fw = new FileWriter(new File(statsfile), false);
			fw.close();
			fw = new FileWriter(new File(settings), false);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		current_level=0;
		readFiles();

		parent_frame.changePanel(PanelID.MainMenu);
	}



	/*
	 * Pre-quiz logic
	 */
	/**
	 * Method that generates random words for quiz
	 * @param number_of_words		words in quiz specified, will go to number of words in file if less than
	 * @return		array of randomised words to do test on
	 * 
	 * Taken from Theo's Assignment 2
	 */
	public ArrayList<String> getWordsForSpellingQuiz(int number_of_words, PanelID id){
		//setup relevant bank of words to choose from depending on quiz type
		ArrayList<String> relevant_bank_of_words;
		ArrayList<String> to_return = new ArrayList<String>();

		switch(id){
		case Quiz:
			relevant_bank_of_words = wordlist_words.get(current_level);
			break; 
		default://Review
			relevant_bank_of_words = reviewlist_words.get(current_level);
			break;
		}

		if(relevant_bank_of_words.size() == 0){
			JOptionPane.showMessageDialog(null, "EMPTY " + id.toString() + " FILE, nothing to test\nWill return to main menu" ,"Error",JOptionPane.WARNING_MESSAGE);
			parent_frame.changePanel(PanelID.MainMenu);
		} else {
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
	 * Taken from Theo's Assignment 2
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




	/*
	 * Post-quiz logic
	 */
	public enum WordResult {
		Mastered, Faulted, Failed;
	}

	/**
	 * returns the list of words and their results from the just-completed quiz
	 * @return
	 */
	public ArrayList<ArrayList<String>> getLatestWordResults(){
		ArrayList<ArrayList<String>> to_return=new ArrayList<ArrayList<String>>();
		to_return.add(latest_mastered_words);
		to_return.add(latest_faulted_words);
		to_return.add(latest_failed_words);
		return to_return;
	}

	/**
	 * processes results from a completed quiz
	 * @param mastered_words
	 * @param faulted_words
	 * @param failed_words
	 * @param quiz_type
	 */
	public void processQuizResults(ArrayList<String> mastered_words, ArrayList<String> faulted_words, ArrayList<String> failed_words, PanelID quiz_type){
		enterResultsIntoDataStructure(mastered_words, WordResult.Mastered);
		enterResultsIntoDataStructure(faulted_words, WordResult.Faulted);
		enterResultsIntoDataStructure(failed_words, WordResult.Failed);

		if (quiz_type==PanelID.Review){
			removeFromReviewList(mastered_words);
			removeFromReviewList(faulted_words);
		} else {
			addToReviewList(failed_words);
		}

		latest_mastered_words=mastered_words;
		latest_faulted_words=faulted_words;
		latest_failed_words=failed_words;
		writeStats();
		writeToReview();
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
			persistent_data_structure=persistent_master_count;
			session_data_structure=session_master_count;
			break;
		case Faulted:
			persistent_data_structure=persistent_faulted_count;
			session_data_structure=session_faulted_count;
			break;
		default:
			persistent_data_structure=persistent_failed_count;
			session_data_structure=session_failed_count;
			break;
		}
		for (String w:quizzed_words){
			//for persistent data structure
			int index=persistent_allwords.get(current_level).indexOf(w);
			int current_value;
			current_value=persistent_data_structure.get(current_level).get(index);
			persistent_data_structure.get(current_level).set(index, current_value+1);

			//for session data structure
			if (session_words.get(current_level).contains(w)){
				index=session_words.get(current_level).indexOf(w);
				current_value=session_data_structure.get(current_level).get(index);
				session_data_structure.get(current_level).set(index, current_value+1);
			} else {
				session_words.get(current_level).add(w);
				session_master_count.get(current_level).add(0);
				session_faulted_count.get(current_level).add(0);
				session_failed_count.get(current_level).add(0);

				index=session_words.get(current_level).indexOf(w);
				session_data_structure.get(current_level).set(index, 1);
			}	
		}
	}

	private void removeFromReviewList(ArrayList<String> not_faulted_words){
		for (String w:not_faulted_words){
			if (reviewlist_words.get(current_level).contains(w)){
				reviewlist_words.get(current_level).remove(w);
			}
		}
	}

	private void addToReviewList(ArrayList<String> failed_words){
		for (String w:failed_words){
			if (!reviewlist_words.get(current_level).contains(w)){
				reviewlist_words.get(current_level).add(w);
			}
		}
	}

	/**
	 * number of words attempted in current level
	 * @return
	 */
	private int getAttemptedCount() {
		int attempted_count=0;
		for (Object[] o:returnWordDataForLevel(current_level, StatsType.Persistent)){
			if(!(o[2].equals(0)&&o[3].equals(0)&&o[4].equals(0))){
				attempted_count++;
			}
		}
		return attempted_count;
	}

	/**
	 * whether at least 50% of words in current level is completed
	 * @return
	 */
	public boolean halfAttempted() {
		int attempted_count=getAttemptedCount();

		if(attempted_count>=persistent_allwords.get(current_level).size()/2){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * return whether there are no words to review in current level (i.e. none failed)
	 * @return
	 */
	public boolean noReview() {
		if(reviewlist_words.get(current_level).size()==0){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * level up
	 */
	public void increaseLevel(){
		current_level++;
	}




	/*
	 * Accessing stored data
	 */
	/**
	 * Returns number of levels (including the empty level 0)
	 * @return
	 */
	public static int getNumberOfLevels(){
		return persistent_allwords.size();
	}

	/**
	 * Returns current level
	 * @return
	 */
	public int getCurrentLevel(){
		return current_level;
	}

	/**
	 * Returns the levels as an Integer array
	 * @return
	 */
	public static Integer[] getLevelArray() {
		//-1 to exclude level 0
		Integer[] levels = new Integer[getNumberOfLevels()-1];
		for(int level=1; level<getNumberOfLevels(); level++){
			levels[level-1]=level;
		}
		return levels;
	}

	/**
	 * level chooser pop up asking user to select a level to quiz at
	 * @param additional_message
	 */
	public static void chooseLevel(String additional_message) {
		Integer[] levels = getLevelArray();
		Integer choice = (Integer)JOptionPane.showInputDialog(parent_frame.getContentPane(), additional_message+"Please select a level to start at", "Which level?", JOptionPane.QUESTION_MESSAGE, null, levels, null);
		if (choice==null){
			System.exit(0);
		} else {
			current_level=choice;
			writeToSettings();
		}
	}

	/**
	 * selects type and level for the word data asked for
	 * @param level
	 * @param type
	 * @return
	 */
	public ArrayList<Object[]> returnWordDataForLevel(int level, StatsChooser.StatsType type){
		ArrayList<ArrayList<String>> words;
		ArrayList<ArrayList<Integer>> master_count, faulted_count, failed_count;
		switch (type) {
		case Persistent:
			words=persistent_allwords;
			master_count=persistent_master_count;
			faulted_count=persistent_faulted_count;
			failed_count=persistent_failed_count;
			break;
		default:
			words=session_words;
			master_count=session_master_count;
			faulted_count=session_faulted_count;
			failed_count=session_failed_count;
			break;
		}

		return getWordDataForLevel(level, words, master_count, faulted_count, failed_count);
	}

	/**
	 * returns an Object array containing
	 * <word level> <the word> <mastered count> <faulted count> <failed count>
	 */
	private ArrayList<Object[]> getWordDataForLevel(int level, ArrayList<ArrayList<String>> words, ArrayList<ArrayList<Integer>> master_count, ArrayList<ArrayList<Integer>> faulted_count, ArrayList<ArrayList<Integer>> failed_count){
		ArrayList<Object[]> to_return = new ArrayList<Object[]>();
		for (int i=0; i<words.get(level).size(); i++){
			to_return.add(new Object[]{Integer.valueOf(level), words.get(level).get(i),
					master_count.get(level).get(i), 
					faulted_count.get(level).get(i), 
					failed_count.get(level).get(i)});
		}
		return to_return;
	}

	/**
	 * gets accuracy rates message for current level to be displayed to the user at all relevant times
	 * @return
	 */
	public String getAccuracyRates(){
		String to_return="";
		to_return+="[level "+this.getCurrentLevel();
		to_return+="]\t[Attempted: "+getAttemptedCount()+"/"+persistent_allwords.get(current_level).size();
		to_return+="]\t["+reviewlist_words.get(current_level).size()+" failed out of "+persistent_allwords.get(current_level).size()+"]";

		return to_return;
	}
}