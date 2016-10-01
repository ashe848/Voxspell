package voxspell;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class WindowBuilderInputError extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public WindowBuilderInputError(InputError error_type) {
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(800, 300, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String allowed="";
		switch (error_type) {
		case Alpha:
			allowed="alphabetical";
			break;
		case Alnum:
			allowed="alphabetical and numerical";
			break;
		default:
			break;
		}
		
		
		JLabel lblOnlyAlphabeticalAnd = new JLabel("Only "+allowed+" characters allowed!");
		lblOnlyAlphabeticalAnd.setBounds(49, 112, 340, 15);
		contentPane.add(lblOnlyAlphabeticalAnd);
	}
	
	public enum InputError {
		Alpha, Numeric, Alnum, AlnumSpace, AtLeastOneAlpha
	}

}
