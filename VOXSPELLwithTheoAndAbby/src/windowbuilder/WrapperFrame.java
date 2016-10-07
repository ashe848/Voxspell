package windowbuilder;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class WrapperFrame extends JFrame {

	JFrame frame = this;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WrapperFrame instance = new WrapperFrame();
				instance.setVisible(true);
			}
		});
	}

	public WrapperFrame() {
		setSize(1366, 787);
		setVisible(true);
		MainMenuPanel mainMenu=new MainMenuPanel();
		setContentPane(mainMenu);	
	}
}
