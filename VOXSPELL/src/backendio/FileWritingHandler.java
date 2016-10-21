package backendio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import audio.Festival.FestivalSpeed;
import audio.Festival.FestivalVoice;
import visiblegui.StatsChooser.StatsType;
import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access" })

/**
 * Singleton class responsible for writing to files
 * @author Abby S
 *
 */
public class FileWritingHandler {
	private static Voxspell parent_frame;

	private static FileWritingHandler instance=null;

	/**
	 * Private constructor for single instance, reference parent frame
	 */
	private FileWritingHandler(Voxspell parent){
		parent_frame=parent;
	}

	/**
	 * Gets single instance of FileReadingHandler
	 * @param parent 	parent frame
	 * @return instance of itself (or creates one if first time called)
	 */
	public static FileWritingHandler getInstance(Voxspell parent){
		if (instance==null){
			instance=new FileWritingHandler(parent);
		}
		return instance;
	}

	/**
	 * writes to stats file in the format
	 * <word level> <mastered count> <faulted count> <failed count> <the word>
	 */
	void writeStats(){
		try {
			FileWriter fw = new FileWriter(new File(parent_frame.getDataHandler().getStatsfile()), false);
			for (int i=0; i<parent_frame.getDataHandler().getNumberOfLevels(); i++){
				for (Object[] o:parent_frame.getDataHandler().returnWordDataForLevel(i, StatsType.Persistent)){
					fw.write(o[0]+" "+ o[3]+" "+o[4]+" "+o[5]+" "+o[2]+"\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes to reviewlist with %LevelName between the levels
	 */
	void writeToReview(){
		try {
			FileWriter fw = new FileWriter(new File(parent_frame.getDataHandler().getReviewlist()), false);
			for (int i=1; i<parent_frame.getDataHandler().getNumberOfLevels(); i++){
				fw.write("%"+parent_frame.getDataHandler().getLevelNames().get(i)+"\n");
				for (String w:parent_frame.getDataHandler().getReviewlistWords().get(i)){
					fw.write(w+"\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes to settings files for the user including list-specific and general settings
	 */
	public static void writeToSettingsFiles(){
		try {
			FileWriter fw_list_settings = new FileWriter(new File(parent_frame.getDataHandler().getListSettings()), false);
			fw_list_settings.write(""+parent_frame.getDataHandler().getCurrentLevel());
			fw_list_settings.close();

			FileWriter fw_user_sesttings = new FileWriter(new File(parent_frame.getDataHandler().getUserSettings()), false);
			fw_user_sesttings.write(parent_frame.getDataHandler().getSpellingListName()+" "+parent_frame.getFestival().getFestivalSpeed().getSpeedValue()+" "+parent_frame.getFestival().getFestivalVoice().getVoiceValue()+" "+parent_frame.getDataHandler().getNumWordsInQuiz()+" "+parent_frame.getDataHandler().getPersonalBest()+" "+parent_frame.getDataHandler().getVideoName());
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
			FileWriter fw = new FileWriter(new File(parent_frame.getDataHandler().getFestivalScheme()), false);
			fw.write("(voice_" + voice.getVoiceValue() +")\n");
			fw.write("(Parameter.set 'Duration_Stretch " + speed.getSpeedValue() +")\n");
			fw.write("(SayText \""+speech+"\")");			
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes list of users and program-wide data 
	 * @author Abby S
	 */
	public static void writeToProgramFiles(){
		try {
			//users list
			FileWriter fw = new FileWriter(new File(parent_frame.getDataHandler().getUsersList()), false);
			for (String u:parent_frame.getDataHandler().getUsers()){
				fw.write(u+"\n");
			}
			fw.close();

			//program-wide stats
			fw = new FileWriter(new File(parent_frame.getDataHandler().getProgramStats()), false);
			fw.write(parent_frame.getDataHandler().getGlobalTop());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes to user-built list (and sample sentences if exists) with %Level number between the levels (same format as inputted lists)
	 * @author Abby S
	 */
	public void writeCustomList(String list_name, ArrayList<String> words_to_add, ArrayList<String> sentences_to_add){
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
	 * Resets stats for current user current list
	 * @author Abby S
	 */
	public void resetListStats() {
		try {
			FileWriter fw = new FileWriter(new File(parent_frame.getDataHandler().getReviewlist()), false);
			fw.close();
			fw = new FileWriter(new File(parent_frame.getDataHandler().getStatsfile()), false);
			fw.close();
			fw = new FileWriter(new File(parent_frame.getDataHandler().getListSettings()), false);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		parent_frame.getDataHandler().setCurrentLevel(0);
		parent_frame.getFileReadingHandler().readListSpecificFiles();

		parent_frame.changePanel(PanelID.MainMenu);
	}

	/**
	 * wipe files and calls read files to read in Visitor's data into the data structures
	 * will be as if this was the user never existed
	 * 
	 * based on http://www.adam-bien.com/roller/abien/entry/java_7_deleting_recursively_a
	 * 
	 * @author Abby S
	 */
	public void resetUser() {
		parent_frame.getDataHandler().getUsers().remove(parent_frame.getDataHandler().getUser());
		writeToProgramFiles();

		//walks down tree from that user's stats directory deleting all it's contents
		Path user_folder_path=Paths.get(parent_frame.getResourceFileLocation()+parent_frame.getDataHandler().getUser()+"/", "");
		try {
			Files.walkFileTree(user_folder_path, new SimpleFileVisitor<Path>(){
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		//delete the now-empty folder for this user
		File user_folder = new File(parent_frame.getResourceFileLocation()+parent_frame.getDataHandler().getUser()+"/");
		user_folder.delete();

		//Logs back in to Visitor
		parent_frame.getDataHandler().setUser("Visitor");
		parent_frame.getFileReadingHandler().readUserFiles();
		parent_frame.getFileReadingHandler().readListSpecificFiles();
		parent_frame.changePanel(PanelID.MainMenu);
	}

	/**
	 * Resets non-list-specific settings back to application defaults
	 * @author Abby S
	 */
	public void resetToDefaults() {
		File user_settings_file = new File(parent_frame.getDataHandler().getUserSettings());
		user_settings_file.delete();
		parent_frame.getFileReadingHandler().readUserFiles();
		parent_frame.getFileReadingHandler().readListSpecificFiles();

		parent_frame.changePanel(PanelID.MainMenu);
	}
}