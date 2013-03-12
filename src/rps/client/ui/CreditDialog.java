package rps.client.ui;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.*;

/**
 * creates a dialog to show the credits of the game
 * @author Sarah Lettmann
 *
 */
public class CreditDialog extends JDialog implements ActionListener{

	static final long serialVersionUID = 11L;
	
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
	
	ImageIcon image = new ImageIcon(System.getProperty("user.dir") + "/data/credits.png");
	
	JLabel credits = new JLabel();
	JButton exit = new JButton();
	
	public CreditDialog(JFrame parent) {
		
		super(parent, "Credits", true);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		credits.setAlignmentX(Component.CENTER_ALIGNMENT);
		credits.setIcon(image);
		credits.setDisabledIcon(image);
		
		getContentPane().add(credits);
		
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
