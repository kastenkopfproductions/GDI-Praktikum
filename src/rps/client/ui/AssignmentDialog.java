package rps.client.ui;

import static javax.swing.JOptionPane.showMessageDialog;
import rps.game.data.Figure;
import rps.game.data.FigureKind;
import rps.game.data.Player;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.GridLayout;

/**
 * a dialog to get the starting grid formation
 * @author Felix Klose
 *
 */
public class AssignmentDialog extends JDialog implements ActionListener {
	
	static final long serialVersionUID = 1337L;
	
	private boolean firstKlick = true;
	private boolean thirdKlick = false;
	//squares of the first and second klick
	private GameSquare firstField;
	private GameSquare secondField;
	
	private Player player;
	
	JPanel boardPanel = new JPanel();
	JButton randomButton = new JButton();
	JButton doneButton = new JButton();
	
	GameSquare fields[] = new GameSquare[14];
	
	Figure[] figures = new Figure[14];
	
	public AssignmentDialog(JFrame parentFrame, Player player) {
		super(parentFrame, "Startaufstellungsassistent", true);
		boardPanel.setLayout(new GridLayout(2, 7));
		
		this.player = player;
		
		for(int i = 0; i < 14; i++) {
			fields[i] = new GameSquare(i, new Figure(null, null), this);
			boardPanel.add(fields[i]);
		}
		
		//initializes the buttons
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
	
	/**
	 * checks which figures shall be set and the events for the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof GameSquare) {
			GameSquare actSquare = (GameSquare)e.getSource();
			//interchanges the figures
			if(firstKlick && !thirdKlick) {
				actSquare.setType(new Figure(FigureKind.FLAG, player), false, false);
				firstKlick = false;
			} else if(!firstKlick && !thirdKlick) {
				actSquare.setType(new Figure(FigureKind.TRAP, player), false, false);
				randomAssignment();
				thirdKlick = true;
			} else if(thirdKlick && !firstKlick) {
				firstField = actSquare;
				firstKlick = true;
			} else if(thirdKlick && firstKlick) {
				secondField = actSquare;
				Figure x = firstField.getType();
				Figure y = secondField.getType();
					
				actSquare.setType(x, false, false);
				firstField.setType(y, false, false);
				
				firstKlick = false;
			}
		
		} else if (e.getSource() instanceof JButton) {
			JButton actButton = (JButton)e.getSource();
			//random starting grid formation
			if(actButton.getName() == "random")
				randomAssignment();
			//sets the typ for the figures
			else if(actButton.getName() == "done") {
				if(fieldsValid()) {
					for(int i = 0; i < 14; i++) {
						figures[i] = fields[i].getType();
					}
					setVisible(false);
					dispose();
				} else {
					showMessageDialog(null, "Fehlerhafte Auswahl!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	/**
	 * checks whether there is a valid or a not valid starting grid formation
	 * @return a boolean whether the fields are valid or not
	 */
	private boolean fieldsValid() {
		int numRock = 0;
		int numPaper = 0;
		int numScissors = 0;
		int numFlag = 0;
		int numTrap = 0;
		
		for(int i = 0; i < 14; i++) {
			switch(fields[i].getType().getKind()) {
			case ROCK:
				numRock++;
				break;
			case PAPER:
				numPaper++;
				break;
			case SCISSORS:
				numScissors++;
				break;
			case TRAP:
				numTrap++;
				break;
			case FLAG:
				numFlag++;
				break;
			}
		}
		
		if(numRock == 4 && numPaper == 4 && numScissors == 4 && numTrap == 1 && numFlag == 1)
			return true;
		return false;
	}
	
	/**
	 * a random starting grid formation
	 */
	public void randomAssignment() {
		
		thirdKlick = true;
		firstKlick = false;
		
		int numRock = 4;
		int numPaper = 4;
		int numScissors = 4;
		int numFlag = 1;
		int numTrap = 1;
		
		for(int i = 0; i < 14; i++) {
			if(fields[i].getType().getKind() == FigureKind.TRAP)
				numTrap--; 
			else if(fields[i].getType().getKind() == FigureKind.FLAG)
				numFlag--;
		}
		
		for(int i = 0; i < 14; i++) {
			boolean valid = false;
			if(fields[i].getType().getKind() == FigureKind.TRAP ||
					fields[i].getType().getKind() == FigureKind.FLAG)
				valid = true;
			while(!valid) {
				int type = (int)(Math.random() * 5 + 1);
				switch(type) {
				case 1:
					if(numRock > 0) {
						numRock--;
						fields[i].setType(new Figure(FigureKind.ROCK, player), false, false);
						valid = true;
					}
					break;
				case 2:
					if(numPaper > 0) {
						numPaper--;
						fields[i].setType(new Figure(FigureKind.PAPER, player), false, false);
						valid = true;
					}
					break;
				case 3:
					if(numScissors > 0) {
						numScissors--;
						fields[i].setType(new Figure(FigureKind.SCISSORS, player), false, false);
						valid = true;
					}
					break;
				case 4:
					if(numTrap > 0) {
						numTrap--;
						fields[i].setType(new Figure(FigureKind.TRAP, player), false, false);
						valid = true;
					}
					break;
				case 5:
					if(numFlag > 0) {
						numFlag--;
						fields[i].setType(new Figure(FigureKind.FLAG, player), false, false);
						valid = true;
					}
					break;
				}
			}
		}
	}
	
	/**
	 * getting the figures
	 * @return the figures
	 */
	public Figure[] getResult() {
		
		showMessageDialog(null, "Ich gehöre " + player.getNick() + "!", "Error", JOptionPane.ERROR_MESSAGE);
		
		return figures;
	}
}