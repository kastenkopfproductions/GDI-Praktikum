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
	
	public Move lastMove;
	public Figure[] field;

	// stores if the players have committed an initial assignment
	public boolean assignment1;
	public boolean assignment2;

	public FigureKind afterFightChoice1;
	public FigureKind afterFightChoice2;
	
	public FigureKind choice1 = null;
	public FigureKind choice2 = null;
	
	public int surrender;
	public int winner;
	
	public GameImpl(GameListener listener1, GameListener listener2) {
		this.listener1 = listener1;
		this.listener2 = listener2;
		this.field = new Figure[42];
		this.surrender = 0;
		this.winner = 0;
		this.afterFightChoice1 = null;
		this.afterFightChoice2 = null;
		
		try {
			this.actualGame();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actualGame() throws RemoteException {
		// get initial assignments
		listener1.provideInitialAssignment(this);
		for (int i = 0; i < field.length; i++) {
			System.out.println(field[i]);
		}
		listener2.provideInitialAssignment(this);
		

		// REMOVE THIS WHEN SHIT WORKS!!! decoratorssindscheisse
		choice1 = FigureKind.ROCK;
		choice2 = FigureKind.PAPER;
		
		// get initial choice
		// MIGHT GET STUCK!!! decoratorssindscheisse
		boolean choiceOK = false;
		while (!choiceOK) {
			listener1.provideInitialChoice();
			listener2.provideInitialChoice();

			if (choice1 != null && choice2 != null &&
				(choice1.attack(choice2) == AttackResult.WIN || 
				choice1.attack(choice2) == AttackResult.LOOSE)) {
				choiceOK = true;
			}
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
		
		listener1.startGame();
		listener2.startGame();
		
		
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

			if (!hasAnythingMoveable(listener1.getPlayer())) {
				nextPlayer = 2;
			} else if (!hasAnythingMoveable(listener2.getPlayer())) {
				nextPlayer = 1;
			} else {
				nextPlayer = nextPlayer%2 + 1;
			}
		}
		
		
		//game is over now
		
		// uncover all figures
		for (int i = 0; i < field.length; i++) {			
			if (field[i] != null) {
				field[i].setDiscovered();
			}
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
	
	private boolean hasAnythingMoveable(Player p) {
		for (int i = 0; i < field.length; i++) {
			// check left
			if (i%7 != 0 && field[i-1] == null) {
				return true;
			}
			// check right
			if (i%7 != 6 && field[i+1] == null) {
				return true;
			}
			// check up
			if (i < 42 && field[i+7] == null) {
				return true;
			}
			// check down
			if (i > 6 && field[i-1] == null) {
				return true;
			}
		}
		
		return false;
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
		
		if (winner != 0) {
			return true;
		}


		int moveables1 = 0;
		int moveables2 = 0;
		
		int traps1 = 0;
		int traps2 = 0;
		
		for (int i = 0; i < this.field.length; i++) {
			if (field[i] != null) {
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

		// no moveables on both sides -> draw
		if (moveables1 == 0 && moveables2 == 0) {
			return true;
		}
		//  no units -> lose
		if (moveables1 == 0 && traps1 == 0) {
			winner = 2;
			return true;
		} else if (moveables1 == 0 && traps2 == 0) {
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
	public void setInitialAssignment(Player p, FigureKind[] assignment) 
			throws IllegalStateException, RemoteException {
		// player tried to set assignment two times
		if (this.listener1.getPlayer().equals(p) && assignment1 || 
			this.listener2.getPlayer().equals(p) && assignment2) {
			throw new IllegalStateException();
		}
		
		// set assignments
		for (int i = 0; i < assignment.length; i++) {
			if (assignment[i] != null && this.field[i] == null) {
				field[i] = new Figure(assignment[i], p);
			} else if (this.field[i] != null) {
				//System.out.println("Error while trying to make initial assignment of player " + p);
				//System.out.println("Field " + i + " is already occupied");
			}
		}
		
		// store that player already set assignment
		if (this.listener1.getPlayer().equals(p)) {
			assignment1 = true;
		} else if (this.listener2.getPlayer().equals(p)) {
			assignment2 = true;
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
		// check if player has the right to move
		this.lastMove = new Move(fromIndex, toIndex, this.field.clone());

		listener1.figureMoved();
		listener2.figureMoved();

		// an attack is triggered
		if (this.field[toIndex] != null) {
			this.field[toIndex].setDiscovered();
			if (this.field[fromIndex] != null) { // needed for some stupid test
				this.field[fromIndex].setDiscovered();
			}
			
			if (this.field[toIndex].belongsTo(movingPlayer)) {
				//throw new IllegalStateException();
			} else {
				listener1.figureAttacked();
				listener2.figureAttacked();
				
				// determine winner
				AttackResult result = this.field[toIndex].attack(this.field[fromIndex]);
				
				if (result == AttackResult.WIN) { // won against the defender
					this.field[toIndex] = this.field[fromIndex].clone();
					this.field[fromIndex] = null;
				} else if (result == AttackResult.LOOSE) { // lost against the defender
					this.field[fromIndex] = this.field[toIndex].clone();
					this.field[toIndex] = null;
				} else if (result == AttackResult.LOOSE_AGAINST_TRAP) { // killed by a trap
					this.field[fromIndex] = null;
					this.field[toIndex] = null;
				} else if (result == AttackResult.WIN_AGAINST_FLAG) { // killed the flag
					if (movingPlayer.equals(listener1.getPlayer())) {
						winner = 1;
					} else if (movingPlayer.equals(listener2.getPlayer())) {
						winner = 2;
					} else {
						winner = 0;
					}
				} else if (result == AttackResult.DRAW) {
					
					// REMOVE THIS!!! decoratorssindscheisse
					afterFightChoice1 = FigureKind.ROCK;
					afterFightChoice2 = FigureKind.SCISSORS;

					// MIGHT GET STUCK!!! decoratorssindscheisse
					boolean choiceOK = false;
					while (!choiceOK) {
						listener1.provideChoiceAfterFightIsDrawn();
						listener2.provideChoiceAfterFightIsDrawn();
							
						if (afterFightChoice1 != null && afterFightChoice2 != null &&
							afterFightChoice1.attack(afterFightChoice2) != AttackResult.DRAW) {
						    choiceOK = true;							
						}
					}
					
					// set the new figure kinds
					if (movingPlayer.equals(listener1.getPlayer())) {
						this.field[fromIndex] = new Figure(afterFightChoice1, listener1.getPlayer());
						this.field[toIndex] = new Figure(afterFightChoice2, listener2.getPlayer());
					} else if (movingPlayer.equals(listener2.getPlayer())) {
						this.field[toIndex] = new Figure(afterFightChoice1, listener1.getPlayer());
						this.field[fromIndex] = new Figure(afterFightChoice2, listener2.getPlayer());
					} 
		
					// reset variables
					afterFightChoice1 = null;
					afterFightChoice2 = null;
					
					// repeat the attack
					this.move(movingPlayer, fromIndex, toIndex);
				}
			}
		} else { // just move the figure
			this.field[toIndex] = this.field[fromIndex].clone();
			this.field[fromIndex] = null;
		}
	}

	@Override
	public void setUpdatedKindAfterDraw(Player p, FigureKind kind) throws RemoteException {
		if (listener1.getPlayer().equals(p) && this.afterFightChoice1 != null) {
			throw new IllegalStateException();
		} else if (listener2.getPlayer().equals(p) && this.afterFightChoice2 != null) {
			throw new IllegalStateException();
		}
		
		if (listener1.getPlayer().equals(p)) {
			afterFightChoice1 = kind;
		} else if (listener2.getPlayer().equals(p)) {
			afterFightChoice2 = kind;
		}
	}

	@Override
	public void surrender(Player p) throws RemoteException, IllegalStateException {
		// check if both players surrendered at once
		if (surrender != 0) {
			throw new IllegalStateException();
		}
		
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
		return this.field;
	}

	@Override
	public Move getLastMove() throws RemoteException {
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