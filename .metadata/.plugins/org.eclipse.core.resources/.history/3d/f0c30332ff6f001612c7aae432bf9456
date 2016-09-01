package voxspell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TestClass extends JFrame{
	JPanel contentPane=new JPanel();
	
	public TestClass(){
		//Below setup code for frame from Theo's code
		super();
		setTitle("Test Level Chooser");
		setSize(800,600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		revalidate();
		
		setContentPane(contentPane);
		JButton levelChooser = new JButton("Level Chooser");
		levelChooser.setAlignmentX(Component.CENTER_ALIGNMENT);
		levelChooser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Object[] choices = {"1","2","3","4","5","6","7","8","9","10","11"}; //options in drop down
				String choice = (String)JOptionPane.showInputDialog(contentPane, "Which level?", "Please Select", JOptionPane.QUESTION_MESSAGE, null, choices, null);
			}
		});
		add(levelChooser);  
	}
}
