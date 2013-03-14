package rps.client.ui;

import rps.client.UIController;

import static javax.swing.BoxLayout.Y_AXIS;

import java.awt.Color;
import java.awt.Container;
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
	
	//The Input-Handler for the board-buttons
	@Override
	public void actionPerformed(ActionEvent e) {
		//Call the UIController-Method that handles the gameInput
		if(e.getSource() instanceof GameSquare) {
			controller.handleGameInput((GameSquare)e.getSource());
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
		status.add(statusNews);
		status.setSize(20, 100);
		gamePane.add(status);
		
		chat.setLineWrap(true);
		chat.setEditable(false);
		
		gamePane.setVisible(false);
		
		parent.add(gamePane);
		
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
			fields[i].setType(new Figure(null, null));
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
	public void setInitialAssignment(Figure[] figures) {
		for(int i = 0; i < 14; i++) {
			fields[i + 28].setType(figures[i]);
		}
	}
	
	public void setInitialChoice() {
		
	}
	
	public void setNextMove() {
		
	}
	
	public void setMove() {
		
	}
	
	public void setAttack() {
		
	}
	
	public void setChoiceAfterFightIsDrawn() {
		
	}

	public void lost() {
		JOptionPane.showMessageDialog(null, "Boah bist du schlecht! Du hast VERLOREN!!!", "Verloren!", JOptionPane.INFORMATION_MESSAGE);
		controller.switchBackToStartup();
	}
	
	public void won() {
		JOptionPane.showMessageDialog(null, "Du hast Gewonnen!! WUUUUHUUUUU...", "Gewonnen!", JOptionPane.INFORMATION_MESSAGE);
		controller.switchBackToStartup();
	}
	
	public void drawn() {
		JOptionPane.showMessageDialog(null, "Ganz schwache Leistung... Unentschieden!!", "Unentschieden", JOptionPane.INFORMATION_MESSAGE);
		controller.switchBackToStartup();
	}
	
	/**
	 * writes the actual event into the status line
	 */
	public void setStatusUpdate() {
		
	}
}