package usabilitymanagement;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

/**
 * Means the file chooser is restricted to the directory I specify
 * So user only allowed to choose lists for the spellinglists folder for example
 * For ease of use, as users don't want to accidently click something 
 * and end up changing things in the system file hierarchy
 * Especially considering this application is intended for use on a healthcare robot
 * 
 * Slightly modified from:	
 * https://tips4java.wordpress.com/2009/01/28/single-root-file-chooser/
 * http://www.camick.com/java/source/SingleRootFileSystemView.java
 * 
 * A FileSystemView class that limits the file selections to a single root.
 * When used with the JFileChooser component the user will only be able to
 * traverse the directories contained within the specified root fill.
 * The "Look In" combo box will only display the specified root
 * The "Up One Level" button will be disabled
 */
public class SingleRootFileSystemView extends FileSystemView{
	File root;
	File[] roots = new File[1];

	public SingleRootFileSystemView(File path){
		super();

		try{
			root = path.getCanonicalFile();
			roots[0] = root;
		}catch(IOException e){
			throw new IllegalArgumentException( e );
		}

		if ( !root.isDirectory() ) {
			String message = root + " is not a directory";
			throw new IllegalArgumentException( message );
		}
	}

	//Disable creating new folders. The icon will do nothing (as with the home icon)
	public File createNewFolder(File containingDir){
		return null;
	}

	public File getDefaultDirectory(){
		return root;
	}

	public File getHomeDirectory(){
		return root;
	}

	public File[] getRoots(){
		return roots;
	}
}