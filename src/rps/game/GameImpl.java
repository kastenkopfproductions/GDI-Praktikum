package rps.game;

import java.rmi.RemoteException;

import rps.client.GameListener;
import rps.game.data.Figure;
import rps.game.data.FigureKind;
import rps.game.data.Move;
import rps.game.data.Player;

/**
 * The {@code GameImpl} is an implementation for the {@code Game} interface. It
 * contains the necessary logic to play a game.
 */
public class GameImpl implements Game {

	private GameListener listener1;
	private GameListener listener2;
	
	private Move lastMove;
	private Figure[] field;
	
	private FigureKind choice1 = null;
	private FigureKind choice2 = null;
	
	
	public GameImpl(GameListener listener1, GameListener listener2) {
		this.listener1 = listener1;
		this.listener2 = listener2;
		this.field = new Figure[49];
	}

	@Override
	public void sendMessage(Player p, String message) throws RemoteException {
		listener1.chatMessage(p, message);
		listener2.chatMessage(p, message);
		// TODO Auto-generated method stub
	}

	@Override
	public void setInitialAssignment(Player p, FigureKind[] assignment) throws RemoteException {
		// TODO Auto-generated method stub
		for (int i = 0; i < assignment.length; i++) {
			if (assignment[i] != null && this.field[i] == null) {
				field[i] = new Figure(assignment[i], p);
			} else if (this.field[i] != null) {
				System.out.println("Error while trying to make initial assignment of player " + p);
				System.out.println("Field " + i + " is already occupied");
			}
		}
	}

	@Override
	public void setInitialChoice(Player p, FigureKind kind) throws RemoteException {
		// TODO Auto-generated method stub
		if (listener1.getPlayer().equals(p)) {
			choice1 = kind;
		} else if (listener2.getPlayer().equals(p)) {
			choice2 = kind;
		} else {
			System.out.println("unknown player tried to set initial choice: " + p);
		}
	}

	@Override
	public void move(Player movingPlayer, int fromIndex, int toIndex) throws RemoteException {
		// TODO Auto-generated method stub
		this.lastMove = new Move(fromIndex, toIndex, this.field.clone());

		this.field[toIndex] = this.field[fromIndex];
		this.field[fromIndex] = null;
	}

	@Override
	public void setUpdatedKindAfterDraw(Player p, FigureKind kind) throws RemoteException {
		listener1.chatMessage(p, "mein stein is jetz ne ente");
		listener2.chatMessage(p, "mein stein is jetz ne ente");
		// TDO Auto-generated method stub
	}

	@Override
	public void surrender(Player p) throws RemoteException {
		listener1.chatMessage(p, "I surrender");
		listener2.chatMessage(p, "I surrender");
		
		if (listener1.getPlayer().equals(p)) {
			listener1.gameIsLost();
			listener2.gameIsWon();
		} else if (listener2.getPlayer().equals(p)) {
			listener1.gameIsWon();
			listener2.gameIsLost();
		}
	}

	@Override
	public Figure[] getField() throws RemoteException {
		// TODO Auto-generated method stub
		return this.field;
	}

	@Override
	public Move getLastMove() throws RemoteException {
		// TODO Auto-generated method stub
		return this.lastMove;
	}

	@Override
	public Player getOpponent(Player p) throws RemoteException {
		if (listener1.getPlayer().equals(p)) {
			return listener2.getPlayer();
		} else if (listener2.getPlayer().equals(p)) {
			return listener1.getPlayer();
		} else {
			return null;
		}
	}
}