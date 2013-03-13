package rps.game;

import java.rmi.RemoteException;

import rps.game.data.Figure;
import rps.game.data.Move;
import rps.game.data.Player;

/**
 * This decorator is used to remove all information from get* methods that are
 * not visible for the corresponding player. Most importantly this is the
 * FigureKind of all Figure on the field that are undiscovered yet and do belong
 * to the other player.
 */
public class FigureHidingGame implements Game {

	private final Game game;
	private final Player player;
	
	public FigureHidingGame(Game game, Player player) throws RemoteException {
		this.game = game;
		this.player = player;
	}


	@Override
	public Figure[] getField() throws RemoteException {
		Figure[] retField = this.game.getField().clone();
		for(int i = 0; i < retField.length; i++) {
			if (retField[i].belongsTo(this.player)) {
				retField[i] = retField[i].cloneWithHiddenKind();
			}
		}
		
		return retField;
	}

	@Override
	public void move(Player p, int from, int to) throws RemoteException {
		game.move(p, from, to);
	}

	@Override
	public Move getLastMove() throws RemoteException {
		return game.getLastMove();
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
}