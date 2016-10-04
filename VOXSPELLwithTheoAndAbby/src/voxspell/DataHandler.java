package voxspell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import voxspell.Festival.FestivalSpeed;
import voxspell.Festival.FestivalVoice;
import voxspell.StatsChooser.StatsType;
import voxspell.Voxspell.PanelID;

/**
 * Data handling class
 * 
 * Responsible for all interactions(input and output) with external spelling word data files
 * (but not video file, which is handled elsewhere)
 * Also responsible for returning spelling data/word data as well as 
 * manipulation of data structure for things like generating random
 * words to quiz.
 * 
 * @author theooswanditosw164
 */
public class DataHandler {
	private static Voxspell parent_frame;

	private static DataHandler instance=null; //since singleton class

	static String user;
	static String spelling_list_name;
	static String video_name;

	static ArrayList<String> users;
	static String global_top;
	static double personal_best;

	//filenames
	private static String program_stats;
	private static String users_list;
	private static String user_settings;
	private static String festival_scheme; //name of scheme file used to say words
	private static String spelling_list; //NZ official spelling list
	private static String statsfile; //File holding stats for words attempted
	private static String reviewlist; //Holds words that have been failed and not mastered/faulted after
	private static String list_settings; //name of file that holds various settings

	static int words_in_quiz; //number of words in each quiz
	static ArrayList<String> level_names;
	
	static boolean has_sample_sentences;
	static ArrayList<ArrayList<String>> sample_sentences;

	static ArrayList<ArrayList<String>> wordlist_words; //words from wordlist file
	private static ArrayList<ArrayList<String>> reviewlist_words; //words from reviewlist file

	//Persistent and session are all for the current list
	private static ArrayList<ArrayList<String>> persistent_allwords; //all words from wordlist + reviewlist
	private static ArrayList<ArrayList<Integer>> persistent_master_count; //counts of times mastered of words above
	private static ArrayList<ArrayList<Integer>> persistent_faulted_count; //counts of times faulted of words above
	private static ArrayList<ArrayList<Integer>> persistent_failed_count; //counts of times failed of words above

	private static ArrayList<ArrayList<String>> session_words; //words attempted this session of program
	private static ArrayList<ArrayList<Integer>> session_master_count; //times word in current session mastered
	private static ArrayList<ArrayList<Integer>> session_faulted_count; //times word in current session faulted
	private static ArrayList<ArrayList<Integer>> session_failed_count; // times word in current session failed

	private static ArrayList<String> latest_mastered_words; //list of mastered words from last quiz (for QuizComplete table)
	private static ArrayList<String> latest_faulted_words; //list of faulted words from last quiz (for QuizComplete table)
	private static ArrayList<String> latest_failed_words; //list of failed words from last quiz (for QuizComplete table)
	static int latest_quiz_length;
	private static boolean levelled_up=false; //flag for whether user had decided to level up

	static int current_level; //initialised so if settings file is empty/wiped

	/**
	 * Constructor for single instance, reference parent frame and starts reading files
	 */
	private DataHandler(Voxspell parent){
		parent_frame=parent;

		//default to visitor
		user="Visitor";
		
		readUsers();
		readProgramStatsFile();
		readUserFiles();
		readListSpecificFiles();
	}

	/**
	 * Gets single instance of DataHandler
	 * @param parent 	parent frame
	 * @return instance of itself (or creates one if first time called)
	 */
	public static DataHandler getInstance(Voxspell parent){
		if (instance==null){
			instance=new DataHandler(parent);
		}
		return instance;
	}

	/*
	 * Reading Files
	 */
	/**
	 * @author Abby S
	 */
	private static void readUsers() {
		users=new ArrayList<>();
		users_list=parent_frame.getResourceFileLocation()+"users";
		try {
			File users_list_file = new File(users_list);
			if (!users_list_file.exists()) {
				users_list_file.createNewFile();
			}else {
				BufferedReader current_BR = new BufferedReader(new FileReader(users_list_file));
				String string_input;
				while ((string_input = current_BR.readLine()) != null) {
					users.add(string_input.trim());
				}
				current_BR.close();
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Abby S
	 */
	private static void readProgramStatsFile() {
		global_top="0";
		program_stats=parent_frame.getResourceFileLocation()+"programstats";
		try {
			File program_file = new File(program_stats);
			if (!program_file.exists()) {
				program_file.createNewFile();
			} else {
				BufferedReader current_BR = new BufferedReader(new FileReader(program_file));
				String string_input;
				while ((string_input = current_BR.readLine()) != null) {
					global_top=string_input;
				}
				current_BR.close();
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @author Abby S
	 */
	static void readUserFiles() {
		try {
			//set defaults
			FestivalSpeed speed=FestivalSpeed.normal;
			FestivalVoice voice=FestivalVoice.Kiwi;
			spelling_list_name="NZCER-spelling-lists.txt";
			video_name="ffmpeg_reward_video.avi";
			words_in_quiz=10;
			personal_best=0;

			//if resources folder can't be found, abort program now instead of get exceptions thrown everywhere
			//no barrier against if a png was deleted (could test for all contents and abort program, but that's not the purpose of this project)
			File resources_folder = new File(parent_frame.getResourceFileLocation());
			if (!resources_folder.exists()) {
				JOptionPane.showMessageDialog(null, "Fatal Error\nThe necessary resources folder has been removed\nAborting", "Fatal Error", JOptionPane.WARNING_MESSAGE);
				System.exit(1);
			}
			
			File default_spelling_list = new File(System.getProperty("user.dir")+"/spellinglists/"+spelling_list_name);
			if (!default_spelling_list.exists()) {
				JOptionPane.showMessageDialog(null, "Fatal Error\nThe necessary NZCER-spelling-lists.txt has been removed\n(should be in spellinglists folder)\nPlease put it back and restart Voxspell", "Fatal Error", JOptionPane.WARNING_MESSAGE);
				System.exit(1);
			}
			
			File default_reward_video = new File(System.getProperty("user.dir")+"/rewardvideos/"+parent_frame.getDataHandler().video_name);
			if (!default_reward_video.exists()) {
				JOptionPane.showMessageDialog(null, "Fatal Error\nThe necessary ffmpeg_reward_video.avi has been removed\n(should be in rewardvideos folder)\nPlease put it back and restart Voxspell", "Fatal Error", JOptionPane.WARNING_MESSAGE);
				System.exit(1);
			}

			File user_folder = new File(parent_frame.getResourceFileLocation()+user+"/");
			if (!user_folder.exists()) {
				user_folder.mkdir();
			}

			user_settings=parent_frame.getResourceFileLocation()+user+"/"+user+"_settings";
			File user_settings_file = new File(user_settings);
			if (!user_settings_file.exists()) {
				user_settings_file.createNewFile();		
			} else {
				BufferedReader current_BR = new BufferedReader(new FileReader(user_settings_file));

				String string_input;
				while ((string_input = current_BR.readLine()) != null) {
					String[] split_line = string_input.split(" ");

					spelling_list_name =split_line[0];
					File saved_spelling_list = new File(System.getProperty("user.dir")+"/spellinglists/"+spelling_list_name);
					if (!saved_spelling_list.exists()) {
						spelling_list_name="NZCER-spelling-lists.txt";
					}
					
					//get festival speed
					String speed_string=split_line[1];
					switch(speed_string){
					case "1.0":
						speed=FestivalSpeed.fast;
						break;
					case "2.0":
						speed=FestivalSpeed.slow;
						break;
					default:
						speed=FestivalSpeed.normal;
					}

					//get festival voice
					String voice_string=split_line[2];
					switch (voice_string) {
					case "kal_diphone":
						voice=FestivalVoice.American;
						break;
						//TODO add rab
					default:
						voice=FestivalVoice.Kiwi;
					}

					words_in_quiz=Integer.parseInt(split_line[3]);

					personal_best=Double.parseDouble(split_line[4]);
					
					video_name =split_line[5];
					File saved_reward_video = new File(System.getProperty("user.dir")+"/rewardvideos/"+video_name);
					if (!saved_reward_video.exists()) {
						video_name="ffmpeg_reward_video.avi";
					}
				}
				current_BR.close();
			}
			parent_frame.getFestival().setFestivalSpeed(speed);
			parent_frame.getFestival().setFestivalVoice(voice);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that reads contents of files and stores them into data structure.
	 * Files read from:
	 * 		NZ Spelling list text file		contents put into wordlist_words arraylist of arraylist(string) by level
	 * 		Statistics file					contents put into persistent{allwords, masteredcount, faultedcount, failedcount}
	 * 		Reviewlist words file			contents put into reviewlist_words arraylist of arraylist(String) by level
	 * 		Settings file					contents put into current_level field
	 */
	static void readListSpecificFiles(){
		setupListSpecificFiles();
		declareDataStructures();
		readInWordlist();
		initialiseDataStructures();
		readInStatsFile();
		addWordsNotInStats();
		readInReviewList();
		readInListSettings();
	}

	/**
	 * checks resource and data files
	 */
	private static void setupListSpecificFiles() {
		//		System.out.println(System.getProperty("user.dir"));
		spelling_list = System.getProperty("user.dir")+"/spellinglists/"+spelling_list_name;
		festival_scheme = parent_frame.getResourceFileLocation()+"festival.scm";
		reviewlist = parent_frame.getResourceFileLocation()+user+"/"+user+"_"+spelling_list_name+"_reviewlist";
		statsfile = parent_frame.getResourceFileLocation()+user+"/"+user+"_"+spelling_list_name+"_statsfile";
		list_settings = parent_frame.getResourceFileLocation()+user+"/"+user+"_"+spelling_list_name+"_settings";

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

			File list_settings_file = new File(list_settings);
			if (!list_settings_file.exists()) {
				list_settings_file.createNewFile();				
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fatal error concerning data files\nAborting", "Fatal Error", JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * Sets up data structure infrastructure
	 */
	private static void declareDataStructures() {
		level_names=new ArrayList<>();
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
			level_names.add(" ");

			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					if (string_input.charAt(0)=='%'){ //new level, offload previous level
						level_names.add(string_input.substring(1));
						wordlist_words.add(temp_string_array);
						temp_string_array= new ArrayList<String>();	
					} else { //add to list for current level
						temp_string_array.add(string_input.trim());
					}
				}
			}
			wordlist_words.add(temp_string_array); //offload final level

			current_BR.close();
			
				File sample_sentences_file = new File(System.getProperty("user.dir")+"/samplesentences/"+spelling_list_name);
				if (sample_sentences_file.exists()) {
					has_sample_sentences=true;
					readInSampleSentences();
				} else {
					has_sample_sentences=false;
				}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Abby S
	 */
	private static void readInSampleSentences() {
		sample_sentences = new ArrayList<ArrayList<String>>();
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/samplesentences/"+spelling_list_name));
			String string_input;
			ArrayList<String> temp_string_array = new ArrayList<String>();

			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					if (string_input.charAt(0)=='%'){ //new level, offload previous level
						sample_sentences.add(temp_string_array);
						temp_string_array= new ArrayList<String>();	
					} else { //add to list for current level
						temp_string_array.add(string_input);
					}
				}
			}
			sample_sentences.add(temp_string_array); //offload final level

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
					//go to next level in data structure
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

	/**
	 * Gets contents of settings files and sets
	 * Settings file structure:
	 * 		<current level> <festival speed> <festival voice>
	 */
	private static void readInListSettings() {
		//sets default
		current_level=0;
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(list_settings));

			String string_input;
			while ((string_input = current_BR.readLine()) != null) {
				String[] split_line = string_input.split(" ");
				current_level=Integer.parseInt(split_line[0]);

				//not first time launching, so will have saved festival settings
				if(current_level<getNumberOfLevels() && current_level>0){
					//other list specific stuff
				} else {
					current_level=0;
				}
			}
			current_BR.close();

			//first launch or cleared data
			if (current_level==0){
				chooseLevel("", false);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





	/**
	 * @author Abby S
	 */
	static boolean errorCheckSelectedFile(File selected) {
		if(selected.getName().contains(" ")){
			return false;
		}
		
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(selected));

			String input_line = current_BR.readLine();
			
			//must have % on first line followed by something (not empty or spaces) for level name
			/*
			 * If the file chosen isn't a text file, input_lines would be something like the below:
			 * RIFF?02AVI LIST?"hdrlavih8
			 * and therefore fail the checks and return false. It appears that no exception is thrown.
			 */
			if (input_line.isEmpty() || input_line.charAt(0)!='%' || input_line.trim().equals("%")){
				current_BR.close();
				return false;
			} 
			
			input_line=current_BR.readLine();	
			//must have something on next line so at least 1 "word" to quiz
			if(input_line==null || input_line.trim().isEmpty()) {
				current_BR.close();
				return false;
			} else {
				current_BR.close();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	/*
	 * Writing to files
	 */
	/**
	 * writes to stats file in the format
	 * <word level> <mastered count> <faulted count> <failed count> <word>
	 */
	private void writeStats(){
		try {
			FileWriter fw = new FileWriter(new File(statsfile), false);
			for (int i=0; i<getNumberOfLevels(); i++){
				for (Object[] o:returnWordDataForLevel(i, StatsType.Persistent)){
					fw.write(o[0]+" "+ o[3]+" "+o[4]+" "+o[5]+" "+o[2]+"\n");
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
				fw.write("%"+level_names.get(i)+"\n");
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
	public static void writeToSettingsFiles(){
		try {
			FileWriter fw_list_settings = new FileWriter(new File(list_settings), false);
			fw_list_settings.write(""+current_level);
			fw_list_settings.close();

			FileWriter fw_user_sesttings = new FileWriter(new File(user_settings), false);
			fw_user_sesttings.write(spelling_list_name+" "+parent_frame.getFestival().getFestivalSpeed().getSpeedValue()+" "+parent_frame.getFestival().getFestivalVoice().getVoiceValue()+" "+words_in_quiz+" "+personal_best+" "+video_name);
			fw_user_sesttings.close();
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
	 * @author Abby S
	 */
	public void writeToProgramFiles(){
		try {
			FileWriter fw = new FileWriter(new File(users_list), false);
			for (String u:users){
				fw.write(u+"\n");
			}
			fw.close();
			
			fw = new FileWriter(new File(program_stats), false);
			fw.write(global_top);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Abby S
	 */
	void writeCustomList(String list_name, ArrayList<String> words_to_add, ArrayList<String> sentences_to_add){
		try {
			FileWriter fw = new FileWriter(new File(System.getProperty("user.dir")+"/spellinglists/"+list_name+".txt"), false);
			fw.write("%"+list_name+"\n");
			for (String w:words_to_add){
				fw.write(w+"\n");
			}
			fw.close();
			
			fw = new FileWriter(new File(System.getProperty("user.dir")+"/samplesentences/"+list_name+".txt"), false);
			fw.write("%"+list_name+"\n");
			for (String s:sentences_to_add){
				fw.write(s+"\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @author Abby S
	 */
	public void resetListStats() {
		try {
			FileWriter fw = new FileWriter(new File(reviewlist), false);
			fw.close();
			fw = new FileWriter(new File(statsfile), false);
			fw.close();
			fw = new FileWriter(new File(list_settings), false);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		current_level=0;
		readListSpecificFiles();

		parent_frame.changePanel(PanelID.MainMenu);
	}
	
	/**
	 * wipe files and calls read files to read in empty into the data structures
	 * will be as if this was the user's first launch
	 * 
	 * based on http://www.adam-bien.com/roller/abien/entry/java_7_deleting_recursively_a
	 * 
	 * @author Abby S
	 */
	public void resetUser() throws IOException {
		users.remove(user);
		writeToProgramFiles();
		
		Path user_folder_path=Paths.get(parent_frame.getResourceFileLocation()+user+"/", "");
		Files.walkFileTree(user_folder_path, new SimpleFileVisitor<Path>(){
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
		});
		File user_folder = new File(parent_frame.getResourceFileLocation()+user+"/");
        user_folder.delete();
        
        user="Visitor";
        readUserFiles();
        readListSpecificFiles();
        parent_frame.changePanel(PanelID.MainMenu);
	}
	
	public void resetToDefaults() {
		File user_settings_file = new File(user_settings);
		user_settings_file.delete();
		readUserFiles();
		
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

		latest_mastered_words=mastered_words;
		latest_faulted_words=faulted_words;
		latest_failed_words=failed_words;
		latest_quiz_length=quiz_length;
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

	/**
	 * Removes given list of words from reviewlist
	 */
	private void removeFromReviewList(ArrayList<String> not_failed_words){
		for (String w:not_failed_words){
			if (reviewlist_words.get(current_level).contains(w)){
				reviewlist_words.get(current_level).remove(w);
			}
		}
	}

	/**
	 * Adds list of failed words into reviewlist
	 */
	void addToReviewList(ArrayList<String> failed_words){
		for (String w:failed_words){
			if (!reviewlist_words.get(current_level).contains(w)){
				reviewlist_words.get(current_level).add(w);
			}
		}
	}

	/**
	 * @Return number of words attempted in current level
	 */
	private int getAttemptedCount() {
		int attempted_count=0;
		for (Object[] o:returnWordDataForLevel(current_level, StatsType.Persistent)){
			if(!(o[5].equals(0)&&o[3].equals(0)&&o[4].equals(0))){
				attempted_count++;
			}
		}
		return attempted_count;
	}

	/**
	 * Increments current level
	 */
	public void increaseLevel(){
		current_level++;
	}

	/**
	 * @return whether user decided to level up
	 */
	public boolean getLevelledUp(){
		return levelled_up;
	}

	/**
	 * sets on user deciding to level up
	 * @param flag
	 */
	public void setLevelledUp(boolean flag){
		levelled_up=flag;
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
	public static String[] getLevelArray() {
		//-1 to exclude level 0
		String[] levels = new String[getNumberOfLevels()-1];
		for(int level=1; level<getNumberOfLevels(); level++){
			levels[level-1]=level_names.get(level);
		}
		return levels;
	}

	/**
	 * level chooser pop up asking user to select a level to quiz at
	 * @param additional_message
	 * @return whether user chose a level
	 */
	public static boolean chooseLevel(String additional_message, boolean back_to_quiz_complete) {
		String[] levels = getLevelArray();
		String choice = (String)JOptionPane.showInputDialog(parent_frame.getContentPane(), additional_message+"Please select a level to start at\nIf you find it too difficult, can change in Settings", "Which level?", JOptionPane.QUESTION_MESSAGE, null, levels, null);
		if (choice==null){
			if(back_to_quiz_complete){
				parent_frame.changePanel(PanelID.QuizComplete);
			} else {
				System.exit(0);
			}
			return false;
		} else {
			current_level=level_names.indexOf(choice);
			writeToSettingsFiles();
			return true;
		}
	}

	/**
	 * selects type and level for the word data asked for
	 * @param level		level of data requested
	 * @param type		type of data requested
	 * @return the pointers to arrays requested
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
		default: //Session
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
			to_return.add(new Object[]{Integer.valueOf(level), level_names.get(level), words.get(level).get(i),
					master_count.get(level).get(i), 
					faulted_count.get(level).get(i), 
					failed_count.get(level).get(i)});
		}
		return to_return;
	}

	/**
	 * gets accuracy rates message for current level to be displayed to the user at all relevant times
	 * @return string representation of accuracy rate
	 */
	public String getAccuracyRates(){
		String to_return=user;
		to_return+="[level "+level_names.get(getCurrentLevel());
		to_return+="]\t[Attempted: "+getAttemptedCount()+"/"+persistent_allwords.get(current_level).size();
		to_return+="]\t["+reviewlist_words.get(current_level).size()+" failed out of "+persistent_allwords.get(current_level).size()+"]";

		return to_return;
	}
}