package backendio;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import visiblegui.StatsChooser;
import visiblegui.StatsChooser.StatsType;
import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access" })

/**
 * Singleton data handling class containing data structures and their access control
 * Maximising reuse of methods and data structures, so fairly long class 
 * lowest visibility possible: mostly only backend I/O classes get access
 * 
 * JavaDoc comments almost take up more lines than actual code.
 */
public class DataHandler {
	private static Voxspell parent_frame;

	private static DataHandler instance=null; 

	private static String user;
	private static String spelling_list_name;
	private static String video_name;
	private static double personal_best;
	private static int words_in_quiz;
	private static ArrayList<String> users;
	private static String global_top;

	//filenames
	private static String program_stats; //program-wide stats (e.g. global top score)
	private static String users_list; //list of registered users
	private static String user_settings; //settings for this user
	private static String festival_scheme; //name of scheme file used to say words
	private static String spelling_list; //NZ official spelling list
	private static String statsfile; //File holding stats for words attempted
	private static String reviewlist; //Holds words that have been failed and not mastered/faulted after
	private static String list_settings; //settings for this user specific to the list

	private static ArrayList<ArrayList<String>> wordlist_words; //words from wordlist file
	private static ArrayList<ArrayList<String>> reviewlist_words; //words from reviewlist file
	private static boolean has_sample_sentences; //whether this list has sample sentences
	private static ArrayList<ArrayList<String>> sample_sentences;//sample sentences from file
	private static ArrayList<String> level_names; //names of the levels (uses integers under the hood)
	private static int current_level; //current level

	//Statistics data structures (Uses master/faulted/failed under the hood)
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

	private static int latest_quiz_length; //length of latest quiz. May not be same as preferred words in quiz as there might not be that many words
	private static boolean levelled_up=false; //flag for whether user had decided to level up
	private static boolean returning_to_quiz_complete=false; //flag for whether user had just watched video not did another quiz

	/**
	 * Constructor for single instance, reference parent frame and starts reading files
	 */
	private DataHandler(Voxspell parent){
		parent_frame=parent;

		//default to visitor
		setUser("Visitor");

		parent_frame.getFileReadingHandler().readInFiles();
	}

	/**
	 * Gets single instance of DataHandler
	 */
	public static DataHandler getInstance(Voxspell parent){
		if (instance==null){
			instance=new DataHandler(parent);
		}
		return instance;
	}

	/**
	 * @return number of words attempted in current level
	 */
	public int getAttemptedCount() {
		int attempted_count=0;
		for (Object[] o:returnWordDataForLevel(getCurrentLevel(), StatsType.Persistent)){
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
		setCurrentLevel(getCurrentLevel() + 1);
	}

	/**
	 * @return number of levels (including the empty level 0)
	 */
	public static int getNumberOfLevels(){
		return getPersistentAllwords().size();
	}

	/**
	 * Returns the levels as an Integer array
	 */
	public static String[] getLevelArray() {
		String[] levels = new String[getNumberOfLevels()-1];//subtract 1 to exclude level 0
		for(int level=1; level<getNumberOfLevels(); level++){
			levels[level-1]=getLevelNames().get(level);
		}
		return levels;
	}

	/**
	 * level chooser pop up asking user to select a level to quiz at
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
			setCurrentLevel(getLevelNames().indexOf(choice));
			parent_frame.getFileWritingHandler().writeToSettingsFiles();
			return true;
		}
	}

	/**
	 * selects type and level for the word data asked for
	 */
	public ArrayList<Object[]> returnWordDataForLevel(int level, StatsChooser.StatsType type){
		ArrayList<ArrayList<String>> words;
		ArrayList<ArrayList<Integer>> master_count, faulted_count, failed_count;
		switch (type) {
		case Persistent:
			words=getPersistentAllwords();
			master_count=getPersistentMasterCount();
			faulted_count=getPersistentFaultedCount();
			failed_count=getPersistentFailedCount();
			break;
		default: //Session
			words=getSessionWords();
			master_count=getSessionMasterCount();
			faulted_count=getSessionFaultedCount();
			failed_count=getSessionFailedCount();
			break;
		}
		return getWordDataForLevel(level, words, master_count, faulted_count, failed_count);
	}

	/**
	 * returns an object array containing <word level> <level name> <the word> <mastered count> <faulted count> <failed count>
	 */
	private ArrayList<Object[]> getWordDataForLevel(int level, ArrayList<ArrayList<String>> words, ArrayList<ArrayList<Integer>> master_count, ArrayList<ArrayList<Integer>> faulted_count, ArrayList<ArrayList<Integer>> failed_count){
		ArrayList<Object[]> to_return = new ArrayList<Object[]>();
		for (int i=0; i<words.get(level).size(); i++){
			to_return.add(new Object[]{Integer.valueOf(level), getLevelNames().get(level), words.get(level).get(i),
					master_count.get(level).get(i), 
					faulted_count.get(level).get(i), 
					failed_count.get(level).get(i)});
		}
		return to_return;
	}

	/**
	 * gets accuracy rates message for current level to be displayed to the user at all relevant times
	 */
	public String getAccuracyRates(){
		String to_return=getUser();
		to_return+=" [Level: "+getLevelNames().get(getCurrentLevel());
		to_return+="] [Attempted "+getAttemptedCount()+"/"+getPersistentAllwords().get(getCurrentLevel()).size();
		to_return+=" words] [Didn't get "+getReviewlistWords().get(getCurrentLevel()).size()+"]";
		return to_return;
	}

	/**
	 * @return the user @author Abby S
	 */
	public static String getUser() {
		return user;
	}

	/**
	 * set the user @author Abby S
	 */
	public static void setUser(String user) {
		DataHandler.user = user;
	}

	/**
	 * @return the spelling list name @author Abby S
	 */
	public static String getSpellingListName() {
		return spelling_list_name;
	}

	/**
	 * set the spelling list name @author Abby S
	 */
	public static void setSpellingListName(String spelling_list_name) {
		DataHandler.spelling_list_name = spelling_list_name;
	}

	/**
	 * @return the video name @author Abby S
	 */
	public static String getVideoName() {
		return video_name;
	}

	/**
	 * set the reward video name @author Abby S
	 */
	public static void setVideoName(String video_name) {
		DataHandler.video_name = video_name;
	}

	/**
	 * @return the personal best score of this user @author Abby S
	 */
	public static double getPersonalBest() {
		return personal_best;
	}

	/**
	 * set the personal best score of this user @author Abby S
	 */
	public static void setPersonalBest(double personal_best) {
		DataHandler.personal_best = personal_best;
	}

	/**
	 * @return the preferred number of words in a quiz @author Abby S
	 */
	public static int getNumWordsInQuiz() {
		return words_in_quiz;
	}

	/**
	 * set the preferred number of words in a quiz @author Abby S
	 */
	public static void setNumWordsInQuiz(int words_in_quiz) {
		DataHandler.words_in_quiz = words_in_quiz;
	}

	/**
	 * @return the users @author Abby S
	 */
	public static ArrayList<String> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set @author Abby S
	 */
	static void setUsers(ArrayList<String> users) {
		DataHandler.users = users;
	}

	/**
	 * @return the global top information @author Abby S
	 */
	public static String getGlobalTop() {
		return global_top;
	}

	/**
	 * set new global top record @author Abby S
	 */
	public static void setGlobalTop(String global_top) {
		DataHandler.global_top = global_top;
	}

	/**
	 * @return the program_stats @author Abby S
	 */
	static String getProgramStats() {
		return program_stats;
	}

	/**
	 * @param program_stats the program_stats to set @author Abby S
	 */
	static void setProgramStats(String program_stats) {
		DataHandler.program_stats = program_stats;
	}

	/**
	 * @return the users_list @author Abby S
	 */
	static String getUsersList() {
		return users_list;
	}

	/**
	 * @param users_list the users_list to set @author Abby S
	 */
	static void setUsersList(String users_list) {
		DataHandler.users_list = users_list;
	}

	/**
	 * @return the user_settings @author Abby S
	 */
	static String getUserSettings() {
		return user_settings;
	}

	/**
	 * @param user_settings the user_settings to set @author Abby S
	 */
	static void setUserSettings(String user_settings) {
		DataHandler.user_settings = user_settings;
	}

	/**
	 * @return the festival_scheme @author Abby S
	 */
	static String getFestivalScheme() {
		return festival_scheme;
	}

	/**
	 * @param festival_scheme the festival_scheme to set @author Abby S
	 */
	static void setFestivalScheme(String festival_scheme) {
		DataHandler.festival_scheme = festival_scheme;
	}

	/**
	 * @return the spelling_list @author Abby S
	 */
	static String getSpellingList() {
		return spelling_list;
	}

	/**
	 * @param spelling_list the spelling_list to set @author Abby S
	 */
	static void setSpellingList(String spelling_list) {
		DataHandler.spelling_list = spelling_list;
	}

	/**
	 * @return the statsfile @author Abby S
	 */
	static String getStatsfile() {
		return statsfile;
	}

	/**
	 * @param statsfile the statsfile to set @author Abby S
	 */
	static void setStatsfile(String statsfile) {
		DataHandler.statsfile = statsfile;
	}

	/**
	 * @return the reviewlist @author Abby S
	 */
	static String getReviewlist() {
		return reviewlist;
	}

	/**
	 * @param reviewlist the reviewlist to set @author Abby S
	 */
	static void setReviewlist(String reviewlist) {
		DataHandler.reviewlist = reviewlist;
	}

	/**
	 * @return the list_settings @author Abby S
	 */
	static String getListSettings() {
		return list_settings;
	}

	/**
	 * @param list_settings the list_settings to set @author Abby S
	 */
	static void setListSettings(String list_settings) {
		DataHandler.list_settings = list_settings;
	}

	/**
	 * @return the words in wordlist @author Abby S
	 */
	public static ArrayList<ArrayList<String>> getWordlistWords() {
		return wordlist_words;
	}

	/**
	 * @param wordlist_words the wordlist_words to set @author Abby S
	 */
	static void setWordlistWords(ArrayList<ArrayList<String>> wordlist_words) {
		DataHandler.wordlist_words = wordlist_words;
	}

	/**
	 * @return the words in reviewlist @author Abby S
	 */
	public static ArrayList<ArrayList<String>> getReviewlistWords() {
		return reviewlist_words;
	}

	/**
	 * @param reviewlist_words the reviewlist_words to set @author Abby S
	 */
	static void setReviewlistWords(ArrayList<ArrayList<String>> reviewlist_words) {
		DataHandler.reviewlist_words = reviewlist_words;
	}

	/**
	 * @return whether this list has sample sentences @author Abby S
	 */
	public static boolean hasSampleSentences() {
		return has_sample_sentences;
	}

	/**
	 * @param has_sample_sentences the has_sample_sentences to set @author Abby S
	 */
	static void setHasSampleSentences(boolean has_sample_sentences) {
		DataHandler.has_sample_sentences = has_sample_sentences;
	}

	/**
	 * @return the sample sentences @author Abby S
	 */
	public static ArrayList<ArrayList<String>> getSampleSentences() {
		return sample_sentences;
	}

	/**
	 * @param sample_sentences the sample_sentences to set @author Abby S
	 */
	static void setSampleSentences(ArrayList<ArrayList<String>> sample_sentences) {
		DataHandler.sample_sentences = sample_sentences;
	}

	/**
	 * @return the level names @author Abby S
	 */
	public static ArrayList<String> getLevelNames() {
		return level_names;
	}

	/**
	 * @param level_names the level_names to set @author Abby S
	 */
	static void setLevelNames(ArrayList<String> level_names) {
		DataHandler.level_names = level_names;
	}

	/**
	 * @return the current_level
	 */
	public static int getCurrentLevel() {
		return current_level;
	}

	/**
	 * @param current_level   the current level to set @author Abby S
	 */
	public static void setCurrentLevel(int current_level) {
		DataHandler.current_level = current_level;
	}

	/**
	 * @return the persistent_allwords @author Abby S
	 */
	static ArrayList<ArrayList<String>> getPersistentAllwords() {
		return persistent_allwords;
	}

	/**
	 * @param persistent_allwords the persistent_allwords to set @author Abby S
	 */
	static void setPersistentAllwords(ArrayList<ArrayList<String>> persistent_allwords) {
		DataHandler.persistent_allwords = persistent_allwords;
	}

	/**
	 * @return the persistent_master_count @author Abby S
	 */
	static ArrayList<ArrayList<Integer>> getPersistentMasterCount() {
		return persistent_master_count;
	}

	/**
	 * @param persistent_master_count the persistent_master_count to set @author Abby S
	 */
	static void setPersistentMasterCount(ArrayList<ArrayList<Integer>> persistent_master_count) {
		DataHandler.persistent_master_count = persistent_master_count;
	}

	/**
	 * @return the persistent_faulted_count @author Abby S
	 */
	static ArrayList<ArrayList<Integer>> getPersistentFaultedCount() {
		return persistent_faulted_count;
	}

	/**
	 * @param persistent_faulted_count the persistent_faulted_count to set @author Abby S
	 */
	static void setPersistentFaultedCount(ArrayList<ArrayList<Integer>> persistent_faulted_count) {
		DataHandler.persistent_faulted_count = persistent_faulted_count;
	}

	/**
	 * @return the persistent_failed_count @author Abby S
	 */
	static ArrayList<ArrayList<Integer>> getPersistentFailedCount() {
		return persistent_failed_count;
	}

	/**
	 * @param persistent_failed_count the persistent_failed_count to set @author Abby S
	 */
	static void setPersistentFailedCount(ArrayList<ArrayList<Integer>> persistent_failed_count) {
		DataHandler.persistent_failed_count = persistent_failed_count;
	}

	/**
	 * @return the session_words @author Abby S
	 */
	static ArrayList<ArrayList<String>> getSessionWords() {
		return session_words;
	}

	/**
	 * @param session_words the session_words to set @author Abby S
	 */
	static void setSessionWords(ArrayList<ArrayList<String>> session_words) {
		DataHandler.session_words = session_words;
	}

	/**
	 * @return the session_master_count @author Abby S
	 */
	static ArrayList<ArrayList<Integer>> getSessionMasterCount() {
		return session_master_count;
	}

	/**
	 * @param session_master_count the session_master_count to set @author Abby S
	 */
	static void setSessionMasterCount(ArrayList<ArrayList<Integer>> session_master_count) {
		DataHandler.session_master_count = session_master_count;
	}

	/**
	 * @return the session_faulted_count @author Abby S
	 */
	static ArrayList<ArrayList<Integer>> getSessionFaultedCount() {
		return session_faulted_count;
	}

	/**
	 * @param session_faulted_count the session_faulted_count to set @author Abby S
	 */
	static void setSessionFaultedCount(ArrayList<ArrayList<Integer>> session_faulted_count) {
		DataHandler.session_faulted_count = session_faulted_count;
	}

	/**
	 * @return the session_failed_count @author Abby S
	 */
	static ArrayList<ArrayList<Integer>> getSessionFailedCount() {
		return session_failed_count;
	}

	/**
	 * @param session_failed_count the session_failed_count to set @author Abby S
	 */
	static void setSessionFailedCount(ArrayList<ArrayList<Integer>> session_failed_count) {
		DataHandler.session_failed_count = session_failed_count;
	}

	/**
	 * @return the latest_mastered_words @author Abby S
	 */
	static ArrayList<String> getLatestMasteredWords() {
		return latest_mastered_words;
	}

	/**
	 * @param latest_mastered_words the latest_mastered_words to set @author Abby S
	 */
	static void setLatestMasteredWords(ArrayList<String> latest_mastered_words) {
		DataHandler.latest_mastered_words = latest_mastered_words;
	}

	/**
	 * @return the latest_faulted_words @author Abby S
	 */
	static ArrayList<String> getLatestFaultedWords() {
		return latest_faulted_words;
	}

	/**
	 * @param latest_faulted_words the latest_faulted_words to set @author Abby S
	 */
	static void setLatestFaultedWords(ArrayList<String> latest_faulted_words) {
		DataHandler.latest_faulted_words = latest_faulted_words;
	}

	/**
	 * @return the latest_failed_words @author Abby S
	 */
	static ArrayList<String> getLatestFailedWords() {
		return latest_failed_words;
	}

	/**
	 * @param latest_failed_words the latest_failed_words to set @author Abby S
	 */
	static void setLatestFailedWords(ArrayList<String> latest_failed_words) {
		DataHandler.latest_failed_words = latest_failed_words;
	}

	/**
	 * @return the length of the latest quiz @author Abby S
	 */
	public static int getLatestQuizLength() {
		return latest_quiz_length;
	}

	/**
	 * @param latest_quiz_length the latest_quiz_length to set @author Abby S
	 */
	static void setLatestQuizLength(int latest_quiz_length) {
		DataHandler.latest_quiz_length = latest_quiz_length;
	}

	/**
	 * @return whether user decided to level up @author Abby S
	 */
	public boolean getLevelledUp(){
		return levelled_up;
	}

	/**
	 * sets on user deciding to level up @author Abby S
	 */
	public void setLevelledUp(boolean flag){
		levelled_up=flag;
	}

	/**
	 * @return whether user is returning to quiz complete after video @author Abby S
	 */
	public boolean isReturningToQuizComplete(){
		return returning_to_quiz_complete;
	}

	/**
	 * sets whether user is returning to quiz complete after video @author Abby S
	 */
	public void setIsReturningToQuizComplete(boolean flag){
		returning_to_quiz_complete=flag;
	}
}