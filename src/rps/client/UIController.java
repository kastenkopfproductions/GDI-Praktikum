package rps.client;

import javax.swing.JOptionPane;

import rps.client.ui.AssignmentDialog;
import rps.client.ui.ChoiceDialog;
import rps.game.data.Figure;
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
	
	Figure[] figures;
	
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
		gamePane.reset();
		menu.gameStarted();
		waitingPane.hide();
	}

	public void switchBackToStartup() {
		waitingPane.hide();
		startupPane.show();
	}
}