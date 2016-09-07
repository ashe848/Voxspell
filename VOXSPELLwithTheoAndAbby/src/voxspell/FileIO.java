package voxspell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.crypto.Mac;

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
				if (string_input.charAt(0)=='%'){
					wordlist_words.add(temp_string_array);
					temp_string_array= new ArrayList<String>();	
				} else {
					temp_string_array.add(string_input);
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
			
			int current_level = 0;
			
			String word;
			String[] split_line;
			
			while ((string_input = current_BR.readLine()) != null) {
				
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
				if (string_input.charAt(0)=='%'){
					reviewlist_words.add(temp_string_array);
					temp_string_array= new ArrayList<String>();	
				} else {
					temp_string_array.add(string_input);
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
	
}