package rps.client.ui;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.*;

/**
 * creates the About-Dialog for the menu
 * @author Sarah Lettmann
 *
 */
public class AboutDialog extends JDialog implements ActionListener{

	static final long serialVersionUID = 23L;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton act = (JButton)e.getSource();
			if(act.getName().equals("return")) {
				setVisible(false);
				dispose();
			}
		}
	}
	
	//picture of the About-Dialog
	ImageIcon image = new ImageIcon(System.getProperty("user.dir") + "/data/about_dialog.png");
	
	JLabel about = new JLabel();
	JButton exit = new JButton();
	
	public AboutDialog(JFrame parent) {

		super(parent, "Über Stein, Papier, Schere", true);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		about.setAlignmentX(Component.CENTER_ALIGNMENT);
		about.setIcon(image);
		about.setDisabledIcon(image);
		
		getContentPane().add(about);
		
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		exit.setText("Zurück");
		exit.setName("return");
		exit.addActionListener(this);
		
		getContentPane().add(exit);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		pack();
		
		setResizable(false);
		setVisible(true);
	}
	
}
