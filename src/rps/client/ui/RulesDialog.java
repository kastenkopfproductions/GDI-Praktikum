package rps.client.ui;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.*;

public class RulesDialog extends JDialog implements ActionListener{
	
	public static final long serialVersionUID = 42L;
	
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
	
	ImageIcon image = new ImageIcon(System.getProperty("user.dir") + "/data/spielregeln.png");
	
	JLabel rules = new JLabel();
	JButton exit = new JButton();
	
	public RulesDialog(JFrame parent) {
		
		super(parent, "Spielregeln", true);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		rules.setAlignmentX(Component.CENTER_ALIGNMENT);
		rules.setIcon(image);
		rules.setDisabledIcon(image);
		
		getContentPane().add(rules);
		
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