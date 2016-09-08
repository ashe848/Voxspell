package voxspell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import voxspell.StatsChooser.StatsType;

public class FileIO {
	private static Voxspell parent_frame;
	private static FileIO instance=null;

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

	private static int current_level=0;

	private FileIO(Voxspell parent){
		parent_frame=parent;

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

		readFiles();
	}

	public static FileIO getInstance(Voxspell parent){
		if (instance==null){
			instance=new FileIO(parent);
		}
		return instance;
	}

	private static void readFiles(){
		BufferedReader current_BR;
		try {
			//wordlist into wordlist array
			current_BR = new BufferedReader(new FileReader(parent_frame.getResourceFileLocation()+"NZCER-spelling-lists.txt"));
			String string_input;
			ArrayList<String> temp_string_array = new ArrayList<String>();

			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					if (string_input.charAt(0)=='%'){
						wordlist_words.add(temp_string_array);
						temp_string_array= new ArrayList<String>();	
					} else {
						temp_string_array.add(string_input.trim());
					}
				}
			}
			wordlist_words.add(temp_string_array);


			current_BR.close();

			//initialise ArrayLists for data structure
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

			//statsfile into allwords
			current_BR = new BufferedReader(new FileReader(parent_frame.getResourceFileLocation()+"statsfile"));

			int word_level;
			int mastered_count, faulted_count, failed_count;

			String word;
			String[] split_line;

			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					word = "";
					split_line = string_input.split(" ");
					word_level = Integer.valueOf(split_line[0]);
					mastered_count  = Integer.valueOf(split_line[1]);
					faulted_count = Integer.valueOf(split_line[2]);
					failed_count = Integer.valueOf(split_line[3]);

					for(int i=4; i<split_line.length; i++){
						word+=split_line[i];
						word+=" ";
					}
					word=word.trim();

					persistent_allwords.get(word_level).add(word);
					persistent_master_count.get(word_level).add(mastered_count);
					persistent_faulted_count.get(word_level).add(faulted_count);
					persistent_failed_count.get(word_level).add(failed_count);
				}
			}

			current_BR.close(); //close statsfile

			ArrayList<Integer> temp_mastered_array = new ArrayList<Integer>();
			ArrayList<Integer> temp_faulted_array = new ArrayList<Integer>();
			ArrayList<Integer> temp_failed_array = new ArrayList<Integer>();
			temp_string_array = new ArrayList<String>();

			//adds words from wordlist that not in statsfile into persistent structures
			for(int i = 0; i<wordlist_words.size(); i++){
				//i=level
				for(String w: wordlist_words.get(i)){
					if(!persistent_allwords.get(i).contains(w)){
						persistent_allwords.get(i).add(w);
						persistent_master_count.get(i).add(0);
						persistent_faulted_count.get(i).add(0);
						persistent_failed_count.get(i).add(0);
						System.out.println(w);
					}
				}
			}

			//To see them in debug mode
			ArrayList<ArrayList<String>> localwl = wordlist_words;
			ArrayList<ArrayList<String>> localaw = persistent_allwords;
			ArrayList<ArrayList<Integer>> localmc = persistent_master_count;
			ArrayList<ArrayList<Integer>> localfauc = persistent_faulted_count;
			ArrayList<ArrayList<Integer>> localfailc = persistent_failed_count;

			//			WE DO REVIEWLIST HERE

			current_BR = new BufferedReader(new FileReader(parent_frame.getResourceFileLocation()+"reviewlist"));
			temp_string_array = new ArrayList<String>();

			while ((string_input = current_BR.readLine()) != null) {
				if (!string_input.isEmpty()){
					if (string_input.charAt(0)=='%'){
						reviewlist_words.add(temp_string_array);
						temp_string_array= new ArrayList<String>();	
					} else {
						temp_string_array.add(string_input);
					}
				}

			}
			reviewlist_words.add(temp_string_array);

			ArrayList<ArrayList<String>> localrl = reviewlist_words;
			current_BR.close();


			//READ IN SETTINGS FILE FOR LEVEL

			current_BR = new BufferedReader(new FileReader(parent_frame.getResourceFileLocation()+"settings"));
			while ((string_input = current_BR.readLine()) != null) {
				current_level=Integer.parseInt(string_input);
				if(!(current_level<getNumberOfLevels() && current_level>0)){
					current_level=0;
				}
			}
			current_BR.close();

			if (current_level==0){
				//open level chooser
				Object[] choices = {"1","2","3","4","5","6","7","8","9","10","11"}; //options in drop down
				String choice = (String)JOptionPane.showInputDialog(parent_frame.getContentPane(), "Welcome! This is your first time! Please select a level to start at", "Which level?", JOptionPane.QUESTION_MESSAGE, null, choices, null);
				if (choice==null){
					System.exit(0);
				} else {
					current_level=Integer.parseInt(choice);
					writeToSettings();
				}
			}

			//DONEZO
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i=0; i<wordlist_words.size();i++){
			
		}

		ArrayList<ArrayList<String>> localsw=session_words;
		ArrayList<ArrayList<Integer>> localsmc=session_master_count;
		ArrayList<ArrayList<Integer>> localsfaulc=session_faulted_count;
		ArrayList<ArrayList<Integer>> localsfailc=session_failed_count;
		System.out.print("");
	}

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

	public static int getNumberOfLevels(){
		return persistent_allwords.size();
	}

	public int getCurrentLevel(){
		return current_level;
	}

	public void writeStats(){
		try {
			FileWriter fw = new FileWriter(new File(parent_frame.getResourceFileLocation()+"statsfile"), false);
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

	public void writeToReview(){
		try {
			FileWriter fw = new FileWriter(new File(parent_frame.getResourceFileLocation()+"reviewlist"), false);
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

	public static void writeToSettings(){
		try {
			FileWriter fw = new FileWriter(new File(parent_frame.getResourceFileLocation()+"settings"), false);
			fw.write(""+current_level);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void processQuizResults(ArrayList<String> mastered_words, ArrayList<String> faulted_words, ArrayList<String> failed_words, String quiz_type){
		enterResultsIntoDataStructure(mastered_words, WordResult.Mastered);
		enterResultsIntoDataStructure(faulted_words, WordResult.Faulted);
		enterResultsIntoDataStructure(failed_words, WordResult.Failed);
		
		if (quiz_type.equals("Review")){
			removeFromReviewList(mastered_words);
			removeFromReviewList(faulted_words);
		} else {
			addToReviewList(failed_words);
		}
	}
	
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
	
	public enum WordResult {
		Mastered, Faulted, Failed;
	}
}