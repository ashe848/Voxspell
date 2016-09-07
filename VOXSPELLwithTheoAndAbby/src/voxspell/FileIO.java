package voxspell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

	private int current_level = 0;

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
						temp_string_array.add(string_input);
					}
				}
			}
			wordlist_words.add(temp_string_array);
			current_BR.close();

			for(int i=0; i<wordlist_words.size();i++){
				persistent_allwords.add(new ArrayList<String>());
				persistent_master_count.add(new ArrayList<Integer>());
				persistent_faulted_count.add(new ArrayList<Integer>());
				persistent_failed_count.add(new ArrayList<Integer>());
			}

			//statsfile into allwords
			current_BR = new BufferedReader(new FileReader(parent_frame.getResourceFileLocation()+"statsfile"));
			temp_string_array = new ArrayList<String>();
			ArrayList<Integer> temp_mastered_array = new ArrayList<Integer>();
			ArrayList<Integer> temp_faulted_array = new ArrayList<Integer>();
			ArrayList<Integer> temp_failed_array = new ArrayList<Integer>();

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

			persistent_allwords.add(temp_string_array);
			persistent_master_count.add(temp_mastered_array);
			persistent_faulted_count.add(temp_faulted_array);
			persistent_failed_count.add(temp_failed_array);

			temp_string_array = new ArrayList<String>();
			temp_mastered_array = new ArrayList<Integer>();
			temp_faulted_array = new ArrayList<Integer>();
			temp_failed_array = new ArrayList<Integer>();			

			//adds words from wordlist that not in statsfile into persistent structures
			for(int i = 0; i<wordlist_words.size(); i++){
				//i=level
				for(String w: wordlist_words.get(i)){
					if(!persistent_allwords.get(i).contains(w)){
						persistent_allwords.get(i).add(w);
						persistent_master_count.get(i).add(0);
						persistent_faulted_count.get(i).add(0);
						persistent_failed_count.get(i).add(0);
					}
				}
			}

			//To see them in debug mode
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

			//DONEZO
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Object[]> returnWordDataForLevel(int level){
		ArrayList<Object[]> to_return = new ArrayList<Object[]>();
		for (int i=0; i<persistent_allwords.get(level).size(); i++){
			to_return.add(new Object[]{Integer.valueOf(level), persistent_allwords.get(level).get(i),
					persistent_master_count.get(level).get(i), 
					persistent_faulted_count.get(level).get(i), 
					persistent_failed_count.get(level).get(i)});
		}
		return to_return;
	}

	public int getNumberOfLevels(){
		return persistent_allwords.size();
	}

	public int getCurrentLevel(){
		return current_level;
	}

	public void writeStats(){
		try {
			FileWriter fw = new FileWriter(new File(parent_frame.getResourceFileLocation()+"statsfile"), false);
			for (int i=0; i<getNumberOfLevels(); i++){
				for (Object[] o:returnWordDataForLevel(i)){
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
			for (int i=1; i<getNumberOfLevels()-1; i++){
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
}