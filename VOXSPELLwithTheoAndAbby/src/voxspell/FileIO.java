package voxspell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class FileIO {
	private static Voxspell parent_frame;
	private static FileIO instance=null;
	
	private static ArrayList<ArrayList<String>> persistent_words;
	private ArrayList<ArrayList<Integer>> persistent_master_count;
	private ArrayList<ArrayList<Integer>> persistent_faulted_count;
	private ArrayList<ArrayList<Integer>> persistent_failed_count;
	
	private ArrayList<ArrayList<String>> session_words;
	private ArrayList<ArrayList<Integer>> session_master_count;
	private ArrayList<ArrayList<Integer>> session_faulted_count;
	private ArrayList<ArrayList<Integer>> session_failed_count;
	
	private FileIO(Voxspell parent){
		parent_frame=parent;
		
		persistent_words = new ArrayList<ArrayList<String>>();
		persistent_master_count = new ArrayList<ArrayList<Integer>>();
		persistent_faulted_count = new ArrayList<ArrayList<Integer>>();
		persistent_failed_count = new ArrayList<ArrayList<Integer>>();
		
		session_words = new ArrayList<ArrayList<String>>();
		session_master_count = new ArrayList<ArrayList<Integer>>();
		session_faulted_count = new ArrayList<ArrayList<Integer>>();
		session_failed_count = new ArrayList<ArrayList<Integer>>();
		
		readWordlist();
	}
	
	public static FileIO getInstance(Voxspell parent){
		if (instance==null){
			instance=new FileIO(parent);
		}
		return instance;
	}
	 
	private static void readWordlist(){
		BufferedReader wordListBR;
		try {
			wordListBR = new BufferedReader(new FileReader(parent_frame.getResourceFileLocation()+"NZCER-spelling-lists.txt"));
			String wordInList;
			int level=0;
			ArrayList<String> temp = new ArrayList<String>();
			while ((wordInList = wordListBR.readLine()) != null) {
				//System.out.println(wordInList);
				if (wordInList.charAt(0)=='%'){
					level++;
					persistent_words.add(temp);
					temp= new ArrayList<String>();		
				} else {
					temp.add(wordInList);
				}
			}
			persistent_words.add(temp);
			ArrayList<ArrayList<String>> local = persistent_words;
			temp= new ArrayList<String>();	
			wordListBR.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
