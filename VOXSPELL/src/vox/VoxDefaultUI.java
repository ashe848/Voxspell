package vox;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;

/**
 * Singleton class responsible for making the default UI effects
 * @author Abby S
 */
public class VoxDefaultUI {
	
	private static VoxDefaultUI instance=null; //since singleton class
	
	/**
	 * Private constructor
	 * @author Abby S
	 */
	private VoxDefaultUI(){
		setGraphicalDefaults();
	}
	
	/**
	 * Method to return the single instance responsible for making the default UI effects
	 * @author Abby S
	 */
	public static VoxDefaultUI getInstance(){
		if (instance==null){
			instance=new VoxDefaultUI();
		}
		return instance;
	}
	
	/**
	 * Sets defaults to fit the theme of the application
	 * @author Abby S
	 */
	private void setGraphicalDefaults() {
		UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 25));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 25));
		UIManager.put("OptionPane.background", new Color(235, 235, 235));
		UIManager.put("ComboBox.selectionBackground", new Color(254, 157, 79)); //background of hovered over or selected item
		UIManager.put("ComboBox.background", Color.WHITE);
		UIManager.put("ScrollBar.background", new Color(254, 157, 79));
		UIManager.put("ScrollPane.background", new Color(254, 157, 79));
		UIManager.put("TableHeader.font", new Font("Arial", Font.PLAIN, 23));
		UIManager.put("TableHeader.foreground", Color.WHITE);
		UIManager.put("TableHeader.background", new Color(254, 157, 79));
		UIManager.put("ProgressBar.selectionBackground", Color.BLACK); //colour of text overlay not covered by bar
		UIManager.put("ProgressBar.selectionForeground", Color.BLACK); //colour of text overlay covered by bar
		UIManager.put("ProgressBar.font", new Font("Arial", Font.PLAIN, 20));
		UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 18)); //enhances default java buttons (e.g. pop up dialog and file chooser) 
		UIManager.put("Button.background",new Color(254, 157, 79));
		UIManager.put("Button.foreground",Color.WHITE);
	}
}