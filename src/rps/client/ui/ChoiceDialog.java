package rps.client.ui;

import rps.game.data.FigureKind;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class ChoiceDialog extends JDialog implements ActionListener{
	
	static final long serialVersionUID = 14L;
	
	private FigureKind result;
	
	//Datapath
	private static final String projPath = System.getProperty("user.dir");
	private static final String data = projPath + "/data";
	
	//Imagepaths
	private static final String rockPath = data + "/stein_start.png";
	private static final String paperPath = data + "/papier_start.png";
	private static final String scissorsPath = data + "/schere_start.png";
	
	//Images
	private static final ImageIcon iRock = new ImageIcon(rockPath);
	private static final ImageIcon iPaper = new ImageIcon(paperPath);
	private static final ImageIcon iScissors = new ImageIcon(scissorsPath);
	
	JButton rock = new JButton();
	JButton paper = new JButton();
	JButton scissors = new JButton();
	
	public ChoiceDialog(JFrame parent) {
		super(parent, "Auswahl", true);
		
		rock.setName("rock");
		rock.setIcon(iRock);
		rock.setDisabledIcon(iRock);
		rock.addActionListener(this);
		
		paper.setName("paper");
		paper.setIcon(iPaper);
		paper.setDisabledIcon(iPaper);
		paper.addActionListener(this);
		
		scissors.setName("scissors");
		scissors.setIcon(iScissors);
		scissors.setDisabledIcon(iScissors);
		scissors.addActionListener(this);
		
		getContentPane().setLayout(new GridLayout(1, 3));
		getContentPane().add(scissors);
		getContentPane().add(rock);
		getContentPane().add(paper);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	public FigureKind getResult() {
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)  {
		if(e.getSource() instanceof JButton) {
			JButton actButton = ((JButton)e.getSource());
			if(actButton.getName().equals("rock")) {
				result = FigureKind.ROCK;
				setVisible(false);
				dispose();
			} else if(actButton.getName().equals("paper")) {
				result = FigureKind.PAPER;
				setVisible(false);
				dispose();
			} else if(actButton.getName().equals("scissors")) {
				result = FigureKind.SCISSORS;
				setVisible(false);
				dispose();
			}
		}
	}
}
