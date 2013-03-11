package rps.client.ui;

import rps.game.data.FigureKind;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionListener;

import rps.game.Game;
import rps.game.data.Player;
import rps.client.ui.GameSquare;

public class GamePane implements ActionListener{

	private final JPanel gamePane = new JPanel();
	private final JTextField chatInput = new JTextField();
	private final JTextArea chat = new JTextArea(4, 30);
	private final JScrollPane scrollPane = new JScrollPane(chat);
	private final JPanel boardPanel = new JPanel(new GridLayout(6, 7));
	private final GameSquare fields[][] = new GameSquare[6][7];
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

	public GamePane(Container parent, UIController controller) {

		this.controller = controller;
		gamePane.setLayout(new BoxLayout(gamePane, Y_AXIS));
		
		//Init the gameboard and add it to the gamePane
		initBoardPanel();
		gamePane.add(boardPanel);
		
		gamePane.add(chatInput);
		gamePane.add(scrollPane);

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

	public void hide() {
		gamePane.setVisible(false);
	}
	
	public void show() {
		gamePane.setVisible(true);
	}

	public void startGame(Player player, Game game) {
		this.player = player;
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
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				fields[i][j].setType(0);
			}
		}
	}
	
	//Initialize the buttons on the board
	private void initBoardPanel() {		
		
		boardPanel.setBackground(Color.BLACK);
		boardPanel.setBorder(BorderFactory.createEmptyBorder());
		
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				fields[i][j] = new GameSquare(i, j, 0, this);
				boardPanel.add(fields[i][j]);
			}
		}
	}
	
	public void setInitialAssignment(FigureKind[] figures) {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 7; j++) {
				switch(figures[j + i*7]) {
				case ROCK:
					fields[4+i][j].setType(1);
					break;
				case PAPER:
					fields[4+i][j].setType(2);
					break;
				case SCISSORS:
					fields[4+i][j].setType(3);
					break;
				case TRAP:
					fields[4+i][j].setType(4);
					break;
				case FLAG:
					fields[4+i][j].setType(5);
					break;
				}
			}
		}
	}
}