package rps.game;

import java.rmi.RemoteException;

import rps.game.data.Figure;
import rps.game.data.Move;
import rps.game.data.Player;



public class ValidatingGame implements Game {

	private final Game game;

	public ValidatingGame(Game game, Player player) throws RemoteException {
		this.game = game;
	}

	@Override
	public void move(Player p, int from, int to) throws RemoteException {
		game.move(p, from, to);
	}

	@Override
	public void sendMessage(Player p, String message) throws RemoteException {
		game.sendMessage(p, message);
	}

	@Override
	public void surrender(Player p) throws RemoteException {
		game.surrender(p);
	}

	@Override
	public Player getOpponent(Player p) throws RemoteException {
		return game.getOpponent(p);
	}

	@Override
	public void setInitialAssignment(Player p,
			rps.game.data.FigureKind[] assignment) throws RemoteException {
		// TODO Auto-generated method stub
		game.setInitialAssignment(p, assignment);
		
	}

	@Override
	public void setInitialChoice(Player p, rps.game.data.FigureKind kind)
			throws RemoteException {
		// TODO Auto-generated method stub
		game.setInitialChoice(p, kind);
	}

	@Override
	public void setUpdatedKindAfterDraw(Player p, rps.game.data.FigureKind kind)
			throws RemoteException {
		// TODO Auto-generated method stub
		game.setUpdatedKindAfterDraw(p, kind);
		
	}

	@Override
	public Figure[] getField() throws RemoteException {
		// TODO Auto-generated method stub
		return game.getField();
	}

	@Override
	public Move getLastMove() throws RemoteException {
		return game.getLastMove();
	}
}