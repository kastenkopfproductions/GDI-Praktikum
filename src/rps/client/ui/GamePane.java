package rps.client.ui;

import rps.client.UIController;

import static javax.swing.BoxLayout.Y_AXIS;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionListener;

import rps.game.data.FigureKind;
import rps.game.data.Figure;
import rps.game.Game;
import rps.game.data.Player;
import rps.client.ui.GameSquare;

public class GamePane implements ActionListener{

	private final JPanel gamePane = new JPanel();
	private final JTextField chatInput = new JTextField();
	private final JTextArea chat = new JTextArea(4, 30);
	private final JScrollPane scrollPane = new JScrollPane(chat);
	private final JPanel status = new JPanel();
	private final JLabel statusNews = new JLabel();
	private final JPanel boardPanel = new JPanel(new GridLayout(6, 7));
	private final GameSquare fields[] = new GameSquare[42];
	private final UIController controller;
	
	private Game game;
	private Player player;
	
	boolean locked = false;
	GameSquare actSquare = null;
	GameSquare lockField = null; 
	
	boolean movable;
	
	//The Input-Handler for the board-buttons
	@Override
	public void actionPerformed(ActionEvent e) {
		//Call the UIController-Method that handles the gameInput
		try {
			if(e.getSource() instanceof GameSquare) {
				actSquare = (GameSquare)e.getSource();
				if(movable) {
					if(!locked) {
						if(!(game.getField()[actSquare.getPosition()] == null
								|| actSquare.getType().getKind() == FigureKind.FLAG
								|| actSquare.getType().getKind() == FigureKind.TRAP)
								&& actSquare.getType().belongsTo(player)) {
							lockField = actSquare;
							locked = true;
						}
					} else {
						
						int x1 = actSquare.getPosition() % 7;
						int y1 = (actSquare.getPosition() - x1) / 6;
						
						int x2 = lockField.getPosition() % 7;
						int y2 = (lockField.getPosition() - x2) / 6;
						
						if(actSquare != lockField && 
								(actSquare.getType().getKind() == null 
								|| actSquare.getType().belongsTo(game.getOpponent(player))) 
								&& (((x1 == x2 + 1 || x1 == x2 - 1) && (y1 == y2)) ||
								((y1 == y2 + 1 || y1 == y2 - 1) && (x1 == x2)))) {
	
							try {
								game.move(this.player, lockField.getPosition(), actSquare.getPosition());
							} catch (RemoteException re) {
								JOptionPane.showMessageDialog(null, "Die Verbindung zum Gegner ist weg.", "Fehler!", JOptionPane.ERROR_MESSAGE);
							}
						}
						locked = false;
						movable = false;
					}
				}
			}
		}
		catch(RemoteException re) {
			System.exit(1337);
		}
	}

	/**
	 * initializes the panel for the board the status line and chat
	 * @param parent container for the panel gamePane
	 * @param controller UIController 
	 */
	public GamePane(Container parent, UIController controller) {
		
		this.controller = controller;
		gamePane.setLayout(new BoxLayout(gamePane, Y_AXIS));
		
		//Init the gameboard and add it to the gamePane
		initBoardPanel();
		gamePane.add(boardPanel);
		gamePane.add(scrollPane);
		gamePane.add(chatInput);
		status.setLayout(new FlowLayout(FlowLayout.LEFT));
		status.add(statusNews);
		gamePane.add(status);
		
		chat.setLineWrap(true);
		chat.setEditable(false);
		
		gamePane.setVisible(false);
		
		parent.add(gamePane);
		
		movable = false;
		
		bindButtons();
	}
	
	private void bindButtons() {
		chatInput.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				boolean isEnter = e.getKeyCode() == KeyEvent.VK_ENTER;
				if (isEnter) {
					addToChat();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	private void addToChat() {
		String message = chatInput.getText().trim();
		if (message.length() > 0) {
			try {
				game.sendMessage(player, message);
				chatInput.setText("");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void hide() {
		gamePane.setVisible(false);
	}
	
	public void show() {
		gamePane.setVisible(true);
	}

	public void startGame(Game game) {
		this.game = game;
		gamePane.setVisible(true);
	}

	public void receivedMessage(Player sender, String message) {

		if (chat.getText().length() != 0) {
			chat.append("\n");
		}
		String formatted = sender.getNick() + ": " + message;
		chat.append(formatted);
		chat.setCaretPosition(chat.getDocument().getLength());
	}

	public void reset() {
		chat.setText(null);
		for(int i = 0; i < 42; i++) {
			fields[i].setType(new Figure(null, null), false, false);
		}
	}
	
	/**
	 * Initialize the buttons on the board
	 */
	private void initBoardPanel() {		
		
		boardPanel.setBackground(Color.BLACK);
		boardPanel.setBorder(BorderFactory.createEmptyBorder());
		
		for(int i = 0; i < 42; i++) {
			fields[i] = new GameSquare(i, new Figure(null, null), this);
			boardPanel.add(fields[i]);
		}
	}
	
	/**
	 * takes over the starting grid formation
	 * @param figures figures of the starting grid formation
	 */
	public void setInitialAssignment() {
		setStatusUpdate("Legen Sie Ihre Startaufstellung fest...");
		AssignmentDialog ad = new AssignmentDialog(null, player);
		try {
			Figure[] result = ad.getResult();
			FigureKind[] assignment = new FigureKind[42];
			for(int i = 28; i < 42; i++) {
				assignment[i] = result[i-28].getKind();
			}
			for(int i = 0; i < 28; i++) {
				assignment[i] = null;
			}
			game.setInitialAssignment(player, assignment);
			setStatusUpdate("     ");
		} catch (RemoteException re) {
			JOptionPane.showMessageDialog(null, "Die Verbindung zum Gegner ist weg.", "Fehler!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setInitialChoice() {
		setStatusUpdate("Wählen Sie eine Figur, um festzustellen wer beginnen darf...");
		ChoiceDialog cd = new ChoiceDialog(null);
		try {
			game.setInitialChoice(player, cd.getResult());
		} catch (RemoteException re) {
			JOptionPane.showMessageDialog(null, "Die Verbindung zum Gegner ist weg.", "Fehler!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setNextMove() {
		setStatusUpdate("Machen Sie Ihren Zug...");
		movable = true;
	}
	
	public void setMove() {
		try {
			Figure[] gameFields = game.getField();
			for(int i = 0; i < 42; i++) {
				fields[i].setType(gameFields[i], false, fields[i].wasAttacked);
			}
		setStatusUpdate("   ");	
		} catch (RemoteException re) {
			JOptionPane.showMessageDialog(null, "Die Verbindung zum Gegner ist weg.", "Fehler!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setAttack() {
		chatInput.setText("");
	}
	
	public void showLastMove(int newIndex) throws RemoteException {
		Figure[] gameFields = game.getField();
		fields[newIndex].setType(gameFields[newIndex], true, fields[newIndex].wasAttacked);
	}
	
	public void setChoiceAfterFightIsDrawn() {
		setStatusUpdate("Wählen Sie eine neue Figur...");
		ChoiceDialog cd = new ChoiceDialog(null);
		try {
			game.setInitialChoice(player, cd.getResult());
		} catch (RemoteException re) {
			JOptionPane.showMessageDialog(null, "Die Verbindung zum Gegner ist weg.", "Fehler!", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void lost() {
		JOptionPane.showMessageDialog(null, "Boah bist du schlecht! Du hast VERLOREN!!!", "Verloren!", JOptionPane.INFORMATION_MESSAGE);
		this.hide();
		controller.switchBackToStartup();
	}
	
	public void won() {
		JOptionPane.showMessageDialog(null, "Du hast Gewonnen!! WUUUUHUUUUU...", "Gewonnen!", JOptionPane.INFORMATION_MESSAGE);
		this.hide();
		controller.switchBackToStartup();
	}
	
	public void drawn() {
		JOptionPane.showMessageDialog(null, "Ganz schwache Leistung... Unentschieden!!", "Unentschieden", JOptionPane.INFORMATION_MESSAGE);
		this.hide();
		controller.switchBackToStartup();
	}
	
	/**
	 * writes the actual event into the status line
	 */
	public void setStatusUpdate(String text) {
		statusNews.setText(text);
	}
}