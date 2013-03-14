package rps.game;

import java.rmi.RemoteException;

import rps.client.GameListener;
import rps.game.data.AttackResult;
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
	
	private int surrender;
	private int winner;
	
	public GameImpl(GameListener listener1, GameListener listener2) {
		this.listener1 = listener1;
		this.listener2 = listener2;
		this.field = new Figure[49];
		this.surrender = 0;
		this.winner = 0;
	}
	
	public void actualGame() throws RemoteException {
		// get initial assignments
		listener1.provideInitialAssignment(this);
		listener2.provideInitialAssignment(this);
		
		
		
		while(choice1 != null && choice2 != null && choice1.attack(choice2) == AttackResult.DRAW) {
			listener1.provideInitialAssignment(this);
			listener2.provideInitialAssignment(this);
		}

		int nextPlayer = 0;
		
		// determine first player
		if(choice1.attack(choice2) == AttackResult.WIN) {
			nextPlayer = 1;
		} else if (choice2.attack(choice1) == AttackResult.WIN) {
			nextPlayer = 2;
		} else {
			System.out.println("WTF???? choice 1 did neither lose nor win against choice 2");
			System.exit(1337);
		}
		
		// main game loop
		while (!gameIsOver()) {
			if (nextPlayer == 1) {
				listener1.provideNextMove();
			} else if (nextPlayer == 2) {
				listener2.provideNextMove();
			} else {
				System.out.println("WTF??? player number 0 is the next player");
				System.exit(42);
			}
			
			nextPlayer = nextPlayer%2 + 1;
		}
		
		// game over stuff
		if (surrender == 0) { // not surrendered
			if (winner == 0) {
				listener1.gameIsDrawn();
				listener2.gameIsDrawn();
			} else if (winner == 1) {
				listener1.gameIsWon();
				listener2.gameIsLost();
			} else if (winner == 2) {
				listener1.gameIsLost();
				listener2.gameIsWon();
			}
		}
	}

	private boolean gameIsOver() throws RemoteException {
		// a player surrendered
		if (surrender == 1) {
			winner = 2;
			return true;
		} else if (surrender == 2) {
			winner = 1;
			return true;
		}
		
		// check if both players still have a flag
		int flags1 = 0;
		int flags2 = 0;
		
		int moveables1 = 0;
		int moveables2 = 0;
		
		int traps1 = 0;
		int traps2 = 0;
		
		for (int i = 0; i < this.field.length; i++) {
			if (field[i] != null) {
				// count flags
				if (field[i].getKind() == FigureKind.FLAG) {
					if (this.field[i].belongsTo(listener1.getPlayer())) {
						flags1++;
					} else if (this.field[i].belongsTo(listener1.getPlayer())) {
						flags2++;
					}
				}
				// count moveables
				if (this.field[i].getKind().isMovable()) {
					if (this.field[i].belongsTo(listener1.getPlayer())) {
						moveables1++;
					} else if (this.field[i].belongsTo(listener1.getPlayer())) {
						moveables2++;
					}
				}
				// count traps
				if (this.field[i].getKind() == FigureKind.TRAP) {
					if (this.field[i].belongsTo(listener1.getPlayer())) {
						traps1++;
					} else if (this.field[i].belongsTo(listener1.getPlayer())) {
						traps2++;
					}
				}
			}
		}

		// no moveables -> draw
		if (flags1 != 0 && flags2 != 0 && moveables1 == 0 && moveables2 == 0) {
			return true;
		}
		//  no flag || no units -> lose
		if (flags1 == 0 || (moveables1 == 0 && traps1 == 0)) {
			winner = 2;
			return true;
		} else if (flags2 == 0 || (moveables1 == 0 && traps2 == 0)) {
			winner = 1;
			return true;
		}
		
		
		return false;		
	}
	
	
	@Override
	public void sendMessage(Player p, String message) throws RemoteException {
		listener1.chatMessage(p, message);
		listener2.chatMessage(p, message);
	}

	@Override
	public void setInitialAssignment(Player p, FigureKind[] assignment) throws RemoteException {
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
		if (listener1.getPlayer().equals(p)) {
			choice1 = kind;
		} else if (listener2.getPlayer().equals(p)) {
			choice2 = kind;
		} else {
			System.out.println("unknown player tried to set initial choice: " + p);
		}
	}

	
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
			surrender = 1;
			listener1.gameIsLost();
			listener2.gameIsWon();
		} else if (listener2.getPlayer().equals(p)) {
			surrender = 2;
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