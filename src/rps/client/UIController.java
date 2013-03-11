package rps.client;

import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.JOptionPane;

import rps.client.ui.AssignmentDialog;
import rps.client.ui.ChoiceDialog;
import rps.game.data.FigureKind;

import rps.client.ui.GamePane;
import rps.client.ui.GameSquare;
import rps.client.ui.Menu;
import rps.client.ui.StartupPane;
import rps.client.ui.WaitingPane;

public class UIController {

	static final int SET_INITIAL_ASSIGNMENT = 0;
	static final int SET_INITIAL_CHOICE = 1;
	static final int PLAY = 2;
	
	FigureKind[] figures;
	
	private GameSquare lockField;
	private boolean locked = false;
	
	private StartupPane startupPane;
	private WaitingPane waitingPane;
	private GamePane gamePane;
	private GameController gameController;
	private Menu menu;
	
	int gameState;

	public void setComponents(Menu menu, StartupPane startupPane, WaitingPane waitingPane, GamePane gamePane,
			GameController gameController) {
		this.menu = menu;
		this.startupPane = startupPane;
		this.waitingPane = waitingPane;
		this.gamePane = gamePane;
		this.gameController = gameController;
	}

	public void handleSurrender() {
		gameController.surrender();
		menu.gameEnded();
	}

	public void handleExit() {
		gameController.exit();
		System.exit(0);
	}

	public void handleNewGame() {
		gameController.resetForNewGame();
		menu.reset();
		startupPane.show();
		waitingPane.hide();
		gamePane.hide();
	}

	public void switchToWaitingForOpponentPane() {
		startupPane.hide();
		waitingPane.show();
	}

	public void stopWaitingAndSwitchBackToStartup() {
		gameController.unregister();
		switchBackToStartup();
	}

	public void switchToGamePane() {
		AssignmentDialog ad = new AssignmentDialog(null);
		figures = ad.getResult();
		ChoiceDialog cd = new ChoiceDialog(null);
		FigureKind initialChoice = cd.getResult();
		String sResult = "";
		switch(initialChoice) {
		case ROCK:
			sResult = "Stein";
			break;
		case PAPER:
			sResult = "Papier";
			break;
		case SCISSORS:
			sResult = "Schere";
			break;
		default:
			sResult = "Nichts";
		}
		JOptionPane.showMessageDialog(null, "Sie haben " + sResult + " gewählt!", "Juhu.", JOptionPane.INFORMATION_MESSAGE);
		gamePane.reset();
		gamePane.setInitialAssignment(figures);
		menu.gameStarted();
		waitingPane.hide();
	}

	public void switchBackToStartup() {
		waitingPane.hide();
		startupPane.show();
	}
	
	//Handles input during a game
	public void handleGameInput(GameSquare actSquare) {
		if(!locked) {
			if(!(actSquare.getType() <= 0 || actSquare.getType() >= 4)) {
				lockField = actSquare;
				locked = true;
			}
		} else {
			if(actSquare != lockField && actSquare.getType() == 0) {
				actSquare.setType(lockField.getType());
				lockField.setType(0);
			}
			locked = false;
		}
	}
}