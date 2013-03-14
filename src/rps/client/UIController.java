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
		AssignmentDialog ad = new AssignmentDialog(null, gamePane.getPlayer());
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
			if(!(actSquare.getType().getKind() == null
					|| actSquare.getType().getKind() == FigureKind.FLAG
					|| actSquare.getType().getKind() == FigureKind.TRAP)) {
				lockField = actSquare;
				locked = true;
			}
		} else {
			
			int x1 = actSquare.getPosition() % 7;
			int y1 = (actSquare.getPosition() - x1) / 6;
			
			int x2 = lockField.getPosition() % 7;
			int y2 = (lockField.getPosition() - x2) / 6;
			
			if(actSquare != lockField && actSquare.getType().getKind() == null && (
					((x1 == x2 + 1 || x1 == x2 - 1) && (y1 == y2)) ||
					((y1 == y2 + 1 || y1 == y2 - 1) && (x1 == x2)))) {

				actSquare.setType(lockField.getType());
				lockField.setType(new Figure(null, null));
			}
			locked = false;
		}
	}
}