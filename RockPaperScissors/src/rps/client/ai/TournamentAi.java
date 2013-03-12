package rps.client.ai;

import java.rmi.RemoteException;

import rps.client.GameListener;
import rps.game.Game;
import rps.game.data.Player;

/**
 * This class contains an advanced AI, that should participate in the
 * tournament.
 */
public class TournamentAi implements GameListener {

	// player nick must end with group number, e.g. "Tournament AI (#123)"
	private final Player player = new Player("Tournament AI (#<Group Number>)");

	private final int maxDurationForMoveInMilliSeconds;
	private final int maxDurationForAllMovesInMilliSeconds;

	public TournamentAi(int maxDurationForMoveInMilliSeconds, int maxDurationForAllMovesInMilliSeconds) {
		this.maxDurationForMoveInMilliSeconds = maxDurationForMoveInMilliSeconds;
		this.maxDurationForAllMovesInMilliSeconds = maxDurationForAllMovesInMilliSeconds;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public void chatMessage(Player sender, String message) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void provideInitialAssignment(Game game) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void provideInitialChoice() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startGame() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void provideNextMove() throws RemoteException {
		long moveCalculationStartedAt = System.nanoTime();
		// TODO Auto-generated method stub
	}

	@Override
	public void figureMoved() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void figureAttacked() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void provideChoiceAfterFightIsDrawn() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameIsLost() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameIsWon() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameIsDrawn() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return player.getNick();
	}
}