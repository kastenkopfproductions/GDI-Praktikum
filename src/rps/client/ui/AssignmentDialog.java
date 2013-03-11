package rps.client.ui;

import static javax.swing.JOptionPane.showMessageDialog;
import rps.game.data.FigureKind;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.GridLayout;

public class AssignmentDialog extends JDialog implements ActionListener {
	
	static final long serialVersionUID = 1337L;
	
	JPanel boardPanel = new JPanel();
	JButton randomButton = new JButton();
	JButton doneButton = new JButton();
	
	GameSquare fields[][] = new GameSquare[6][7];
	
	FigureKind[] figures = new FigureKind[14];
	
	public AssignmentDialog(JFrame parentFrame) {
		super(parentFrame, "Startaufstellungsassistent", true);
		boardPanel.setLayout(new GridLayout(2, 7));
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 7; j++) {
				fields[i][j] = new GameSquare(i, j, 0, this);
				boardPanel.add(fields[i][j]);
			}
		}
		
		randomButton.setText("Zufällige Startaufstellung generieren!");
		randomButton.setName("random");
		randomButton.addActionListener(this);
		randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		doneButton.setText("Fertig");
		doneButton.setName("done");
		doneButton.addActionListener(this);
		doneButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		getContentPane().add(boardPanel);
		getContentPane().add(randomButton);
		getContentPane().add(doneButton);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof GameSquare) {
			GameSquare actSquare = (GameSquare)e.getSource();
			actSquare.setType((actSquare.getType() + 1) % 6);
		} else if (e.getSource() instanceof JButton) {
			JButton actButton = (JButton)e.getSource();
			
			if(actButton.getName() == "random")
				randomAssignment();
			else if(actButton.getName() == "done") {
				if(fieldsValid()) {
					for(int i = 0; i < 2; i++) {
						for(int j = 0; j < 7; j++) {
							switch(fields[i][j].getType()) {
							case 1:
								figures[j + 7*i] = FigureKind.ROCK;
								break;
							case 2:
								figures[j + 7*i] = FigureKind.PAPER;
								break;
							case 3:
								figures[j + 7*i] = FigureKind.SCISSORS;
								break;
							case 4:
								figures[j + 7*i] = FigureKind.TRAP;
								break;
							case 5:
								figures[j + 7*i] = FigureKind.FLAG;
								break;
							}
						}
					}
					setVisible(false);
					dispose();
				} else {
					showMessageDialog(null, "Fehlerhafte Auswahl!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	private boolean fieldsValid() {
		int numRock = 0;
		int numPaper = 0;
		int numScissors = 0;
		int numFlag = 0;
		int numTrap = 0;
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 7; j++) {
				switch(fields[i][j].getType()) {
				case 1:
					numRock++;
					break;
				case 2:
					numPaper++;
					break;
				case 3:
					numScissors++;
					break;
				case 4:
					numTrap++;
					break;
				case 5:
					numFlag++;
					break;
				}
			}
		}
		
		if(numRock == 4 && numPaper == 4 && numScissors == 4 && numTrap == 1 && numFlag == 1)
			return true;
		return false;
	}
	
	public void randomAssignment() {
		
		int numRock = 4;
		int numPaper = 4;
		int numScissors = 4;
		int numFlag = 1;
		int numTrap = 1;
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 7; j++) {
				if(fields[i][j].getType() == 4)
					numTrap--; 
				else if(fields[i][j].getType() == 5)
					numFlag--;
			}
		}
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 7; j++) {
				boolean valid = false;
				if(fields[i][j].getType() == 4 || fields[i][j].getType() == 5)
					valid = true;
				while(!valid) {
					int type = (int)(Math.random() * 5 + 1);
					switch(type) {
					case 1:
						if(numRock > 0) {
							numRock--;
							fields[i][j].setType(type);
							valid = true;
						}
						break;
					case 2:
						if(numPaper > 0) {
							numPaper--;
							fields[i][j].setType(type);
							valid = true;
						}
						break;
					case 3:
						if(numScissors > 0) {
							numScissors--;
							fields[i][j].setType(type);
							valid = true;
						}
						break;
					case 4:
						if(numTrap > 0) {
							numTrap--;
							fields[i][j].setType(type);
							valid = true;
						}
						break;
					case 5:
						if(numFlag > 0) {
							numFlag--;
							fields[i][j].setType(type);
							valid = true;
						}
						break;
					}
				}
			}
		}
	}
	
	public FigureKind[] getResult() {
		return figures;
	}
}