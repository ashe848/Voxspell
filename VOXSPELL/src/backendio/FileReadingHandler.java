package backendio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import audio.Festival.FestivalSpeed;
import audio.Festival.FestivalVoice;
import vox.Voxspell;

@SuppressWarnings({ "static-access" })

/**
 * 
 * @author Abby S
 *
 */
public class FileReadingHandler {
	private static Voxspell parent_frame;

	private static FileReadingHandler instance=null; //since singleton class
	
	/**
	 * Constructor for single instance, reference parent frame and starts reading files
	 */
	private FileReadingHandler(Voxspell parent){
		parent_frame=parent;
	}

	/**
	 * Gets single instance of FileReadingHandler
	 * @param parent 	parent frame
	 * @return instance of itself (or creates one if first time called)
	 */
	public static FileReadingHandler getInstance(Voxspell parent){
		if (instance==null){
			instance=new FileReadingHandler(parent);
		}
		return instance;
	}
	
	/**
	 * 
	 */
	void readInFiles() {
		readUsers();
		readProgramStatsFile();
		readUserFiles();
		readListSpecificFiles();
	}
	
	/**
	 * Reads in list of users
	 * @author Abby S
	 */
	private static void readUsers() {
		parent_frame.getDataHandler().setUsers(new ArrayList<String>());
		parent_frame.getDataHandler().setUsersList(parent_frame.getResourceFileLocation()+"users");
		try {
			File users_list_file = new File(parent_frame.getDataHandler().getUsersList());
			if (!users_list_file.exists()) {
				//creates file if doesn't already exist
				users_list_file.createNewFile();
			}else {
				BufferedReader current_BR = new BufferedReader(new FileReader(users_list_file));
				String string_input;
				while ((string_input = current_BR.readLine()) != null) {
					parent_frame.getDataHandler().getUsers().add(string_input.trim());
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
	 * Reads in program-wide stats
	 * @author Abby S
	 */
	private static void readProgramStatsFile() {
		parent_frame.getDataHandler().setGlobalTop("0.0 DNE DNE"); //default value
		parent_frame.getDataHandler().setProgramStats(parent_frame.getResourceFileLocation()+"programstats");
		try {
			File program_file = new File(parent_frame.getDataHandler().getProgramStats());
			if (!program_file.exists()) {
				//creates file if doesn't already exist
				program_file.createNewFile();
				parent_frame.getFileWritingHandler().writeToProgramFiles();
			} else {
				BufferedReader current_BR = new BufferedReader(new FileReader(program_file));
				String string_input;
				while ((string_input = current_BR.readLine()) != null) {
					parent_frame.getDataHandler().setGlobalTop(string_input);
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
	 * Reads in files for this user
	 * @author Abby S
	 */
	public static void readUserFiles() {
		try {
			//set defaults
			FestivalSpeed speed=FestivalSpeed.normal;
			FestivalVoice voice=FestivalVoice.Kiwi;
			parent_frame.getDataHandler().setSpellingListName("NZCER-spelling-lists.txt");
			parent_frame.getDataHandler().setVideoName("ffmpeg_reward_video.avi");
			parent_frame.getDataHandler().setNumWordsInQuiz(10);
			parent_frame.getDataHandler().setPersonalBest(0);

			checkFileExistence();

			parent_frame.getDataHandler().setUserSettings(parent_frame.getResourceFileLocation()+parent_frame.getDataHandler().getUser()+"/"+parent_frame.getDataHandler().getUser()+"_settings");
			File user_settings_file = new File(parent_frame.getDataHandler().getUserSettings());
			if (!user_settings_file.exists()) {
				//creates file if doesn't already exist
				user_settings_file.createNewFile();		
			} else {
				BufferedReader current_BR = new BufferedReader(new FileReader(user_settings_file));

				String string_input;
				while ((string_input = current_BR.readLine()) != null) {
					String[] split_line = string_input.split(" ");

					parent_frame.getDataHandler().setSpellingListName(split_line[0]);
					File saved_spelling_list = new File(System.getProperty("user.dir")+"/spellinglists/"+parent_frame.getDataHandler().getSpellingListName());
					if (!saved_spelling_list.exists()) {
						//if list has been deleted, set to default
						parent_frame.getDataHandler().setSpellingListName("NZCER-spelling-lists.txt");
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
					case "rab_diphone":
						voice=FestivalVoice.British;
						break;
					default:
						voice=FestivalVoice.Kiwi;
					}

					parent_frame.getDataHandler().setNumWordsInQuiz(Integer.parseInt(split_line[3]));

					parent_frame.getDataHandler().setPersonalBest(Double.parseDouble(split_line[4]));

					parent_frame.getDataHandler().setVideoName(split_line[5]);
					File saved_reward_video = new File(System.getProperty("user.dir")+"/rewardvideos/"+parent_frame.getDataHandler().getVideoName());
					if (!saved_reward_video.exists()) {
						//if video has been deleted, set to default
						parent_frame.getDataHandler().setVideoName("ffmpeg_reward_video.avi");
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
	 * Checks the existence of necessary files
	 * 
	 * if resources folder can't be found, abort program now instead of get exceptions thrown everywhere
	 * no barrier against if a png was deleted for example 
	 * (could test for all contents and abort program, but that's not the purpose of this project)
	 * 
	 * Default spelling list and reward video. Application won't run if they're deleted 
	 * 
	 * Creates folder if doesn't already exist (ie new user)
	 */
	private static void checkFileExistence() {		 
		File resources_folder = new File(parent_frame.getResourceFileLocation());
		if (!resources_folder.exists()) {
			JOptionPane.showMessageDialog(null, "Fatal Error\nThe necessary resources folder has been removed\nAborting", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		File default_spelling_list = new File(System.getProperty("user.dir")+"/spellinglists/"+parent_frame.getDataHandler().getSpellingListName());
		if (!default_spelling_list.exists()) {
			JOptionPane.showMessageDialog(null, "Fatal Error\nThe necessary NZCER-spelling-lists.txt has been removed\n(should be in spellinglists folder)\nPlease put it back and restart Voxspell", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		File default_reward_video = new File(System.getProperty("user.dir")+"/rewardvideos/"+parent_frame.getDataHandler().getVideoName());
		if (!default_reward_video.exists()) {
			JOptionPane.showMessageDialog(null, "Fatal Error\nThe necessary ffmpeg_reward_video.avi has been removed\n(should be in rewardvideos folder)\nPlease put it back and restart Voxspell", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		File user_folder = new File(parent_frame.getResourceFileLocation()+parent_frame.getDataHandler().getUser()+"/");
		if (!user_folder.exists()) {
			user_folder.mkdir();
		}
	}

	/**
	 * Method that reads contents of files and stores them into data structures
	 */
	public static void readListSpecificFiles(){
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
	 * files specific to this list
	 */
	private static void setupListSpecificFiles() {
		parent_frame.getDataHandler().setSpellingList(System.getProperty("user.dir")+"/spellinglists/"+parent_frame.getDataHandler().getSpellingListName());
		parent_frame.getDataHandler().setFestivalScheme(parent_frame.getResourceFileLocation()+"festival.scm");
		parent_frame.getDataHandler().setReviewlist(parent_frame.getResourceFileLocation()+parent_frame.getDataHandler().getUser()+"/"+parent_frame.getDataHandler().getUser()+"_"+parent_frame.getDataHandler().getSpellingListName()+"_reviewlist");
		parent_frame.getDataHandler().setStatsfile(parent_frame.getResourceFileLocation()+parent_frame.getDataHandler().getUser()+"/"+parent_frame.getDataHandler().getUser()+"_"+parent_frame.getDataHandler().getSpellingListName()+"_statsfile");
		parent_frame.getDataHandler().setListSettings(parent_frame.getResourceFileLocation()+parent_frame.getDataHandler().getUser()+"/"+parent_frame.getDataHandler().getUser()+"_"+parent_frame.getDataHandler().getSpellingListName()+"_settings");

		//creates files that can be empty at this point (because them not existing is an issue that won't crash the program)
		try {	
			File festival_scheme_file = new File(parent_frame.getDataHandler().getFestivalScheme());
			if (!festival_scheme_file.exists()) {
				festival_scheme_file.createNewFile();				
			}

			File review_list = new File(parent_frame.getDataHandler().getReviewlist());
			if (!review_list.exists()) {
				review_list.createNewFile();				
			}

			File stats_file = new File(parent_frame.getDataHandler().getStatsfile());
			if (!stats_file.exists()) {
				stats_file.createNewFile();				
			}

			File list_settings_file = new File(parent_frame.getDataHandler().getListSettings());
			if (!list_settings_file.exists()) {
				list_settings_file.createNewFile();				
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fatal error concerning data files\nAborting", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * Sets up data structure infrastructure
	 */
	private static void declareDataStructures() {
		parent_frame.getDataHandler().setLevelNames(new ArrayList<String>());
		parent_frame.getDataHandler().setWordlistWords(new ArrayList<ArrayList<String>>());
		parent_frame.getDataHandler().setReviewlistWords(new ArrayList<ArrayList<String>>());

		parent_frame.getDataHandler().setPersistentAllwords(new ArrayList<ArrayList<String>>());
		parent_frame.getDataHandler().setPersistentMasterCount(new ArrayList<ArrayList<Integer>>());
		parent_frame.getDataHandler().setPersistentFaultedCount(new ArrayList<ArrayList<Integer>>());
		parent_frame.getDataHandler().setPersistentFailedCount(new ArrayList<ArrayList<Integer>>());

		parent_frame.getDataHandler().setSessionWords(new ArrayList<ArrayList<String>>());
		parent_frame.getDataHandler().setSessionMasterCount(new ArrayList<ArrayList<Integer>>());
		parent_frame.getDataHandler().setSessionFaultedCount(new ArrayList<ArrayList<Integer>>());
		parent_frame.getDataHandler().setSessionFailedCount(new ArrayList<ArrayList<Integer>>());
	}

	/**
	 * wordlist into wordlist arraylist of arraylists by level
	 */
	private static void readInWordlist() {
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(parent_frame.getDataHandler().getSpellingList()));
			String string_input;
			ArrayList<String> temp_string_array = new ArrayList<String>();
			parent_frame.getDataHandler().getLevelNames().add(" ");

			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					if (string_input.charAt(0)=='%'){ //new level, offload previous level
						parent_frame.getDataHandler().getLevelNames().add(string_input.substring(1));
						parent_frame.getDataHandler().getWordlistWords().add(temp_string_array);
						temp_string_array= new ArrayList<String>();	
					} else { //add to list for current level
						temp_string_array.add(string_input.trim());
					}
				}
			}
			parent_frame.getDataHandler().getWordlistWords().add(temp_string_array); //offload final level

			current_BR.close();

			//Check if list comes with sample sentences @author Abby S
			File sample_sentences_file = new File(System.getProperty("user.dir")+"/samplesentences/"+parent_frame.getDataHandler().getSpellingListName());
			if (sample_sentences_file.exists()) {
				parent_frame.getDataHandler().setHasSampleSentences(true);
				readInSampleSentences();
			} else {
				parent_frame.getDataHandler().setHasSampleSentences(false);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves sample sentences into arraylist of arraylists by level
	 * @author Abby S
	 */
	private static void readInSampleSentences() {
		parent_frame.getDataHandler().setSampleSentences(new ArrayList<ArrayList<String>>());
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/samplesentences/"+parent_frame.getDataHandler().getSpellingListName()));
			String string_input;
			ArrayList<String> temp_string_array = new ArrayList<String>();

			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					if (string_input.charAt(0)=='%'){ //new level, offload previous level
						parent_frame.getDataHandler().getSampleSentences().add(temp_string_array);
						temp_string_array= new ArrayList<String>();	
					} else { //add to list for current level
						temp_string_array.add(string_input);
					}
				}
			}
			parent_frame.getDataHandler().getSampleSentences().add(temp_string_array); //offload final level

			current_BR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * initialise ArrayLists for data structure based on how many levels in wordlist
	 */
	private static void initialiseDataStructures() {
		for(int i=0; i<parent_frame.getDataHandler().getWordlistWords().size(); i++){
			parent_frame.getDataHandler().getPersistentAllwords().add(new ArrayList<String>());
			parent_frame.getDataHandler().getPersistentMasterCount().add(new ArrayList<Integer>());
			parent_frame.getDataHandler().getPersistentFaultedCount().add(new ArrayList<Integer>());
			parent_frame.getDataHandler().getPersistentFailedCount().add(new ArrayList<Integer>());
			parent_frame.getDataHandler().getSessionWords().add(new ArrayList<String>());
			parent_frame.getDataHandler().getSessionMasterCount().add(new ArrayList<Integer>());
			parent_frame.getDataHandler().getSessionFaultedCount().add(new ArrayList<Integer>());
			parent_frame.getDataHandler().getSessionFailedCount().add(new ArrayList<Integer>());
			parent_frame.getDataHandler().getReviewlistWords().add(new ArrayList<String>());
		}
	}

	/**
	 * statsfile contains all words in and their stats (will be 0s if not attempted)
	 */
	private static void readInStatsFile() {
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(parent_frame.getDataHandler().getStatsfile()));

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
					mastered_count = Integer.valueOf(split_line[1]);
					faulted_count = Integer.valueOf(split_line[2]);
					failed_count = Integer.valueOf(split_line[3]);

					//for words that include spaces
					for(int i=4; i<split_line.length; i++){
						word+=split_line[i];
						word+=" ";
					}
					word=word.trim();//trim trailing white space

					//add word and it's stats to the data structures
					parent_frame.getDataHandler().getPersistentAllwords().get(word_level).add(word);
					parent_frame.getDataHandler().getPersistentMasterCount().get(word_level).add(mastered_count);
					parent_frame.getDataHandler().getPersistentFaultedCount().get(word_level).add(faulted_count);
					parent_frame.getDataHandler().getPersistentFailedCount().get(word_level).add(failed_count);
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
	 * adds words from wordlist that's not in statsfile into persistent structures
	 * if wordlist hasn't been used before (and therefore there statsfile is empty)
	 * or if wordlist has had additional words added to it since last loaded
	 */
	private static void addWordsNotInStats() {
		for(int word_level = 0; word_level<parent_frame.getDataHandler().getWordlistWords().size(); word_level++){
			for(String w: parent_frame.getDataHandler().getWordlistWords().get(word_level)){
				if(!parent_frame.getDataHandler().getPersistentAllwords().get(word_level).contains(w)){
					parent_frame.getDataHandler().getPersistentAllwords().get(word_level).add(w);
					parent_frame.getDataHandler().getPersistentMasterCount().get(word_level).add(0);
					parent_frame.getDataHandler().getPersistentFaultedCount().get(word_level).add(0);
					parent_frame.getDataHandler().getPersistentFailedCount().get(word_level).add(0);
				}
			}
		}
	}

	/**
	 * add words to review from file into data structure based on level
	 */
	private static void readInReviewList() {
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(parent_frame.getDataHandler().getReviewlist()));

			int level = 0;
			String string_input;
			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					//go to next level in data structure
					if (string_input.charAt(0)=='%'){
						level++;
					} else {
						parent_frame.getDataHandler().getReviewlistWords().get(level).add(string_input);
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
	 * Gets contents of settings file for this list
	 */
	private static void readInListSettings() {
		//sets default
		parent_frame.getDataHandler().setCurrentLevel(0);
		try {
			BufferedReader current_BR = new BufferedReader(new FileReader(parent_frame.getDataHandler().getListSettings()));

			String string_input;
			while ((string_input = current_BR.readLine()) != null) {
				String[] split_line = string_input.split(" ");
				parent_frame.getDataHandler().setCurrentLevel(Integer.parseInt(split_line[0]));

				//not first time launching, so will have saved festival settings
				if(parent_frame.getDataHandler().getCurrentLevel()<parent_frame.getDataHandler().getNumberOfLevels() && parent_frame.getDataHandler().getCurrentLevel()>0){
					//other list specific stuff
				} else {
					parent_frame.getDataHandler().setCurrentLevel(0);
				}
			}
			current_BR.close();

			//first launch or cleared data
			if (parent_frame.getDataHandler().getCurrentLevel()==0){
				parent_frame.getDataHandler().chooseLevel("", false);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a file to see if it conforms to spelling list format
	 * @author Abby S
	 */
	public static boolean errorCheckSelectedFile(File selected) {
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
}