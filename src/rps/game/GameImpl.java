package rps.game;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
	public boolean done1;
	public boolean done2;

	public Player winner;
	public FigureKind newType;
	
	public FigureKind choice1 = null;
	public FigureKind choice2 = null;
	
	public boolean surrendered = false;
	
	public int nextPlayer = 0;
	public int lastPlayer = 0;
	
	public GameImpl(GameListener listener1, GameListener listener2) {
		this.listener1 = listener1;
		this.listener2 = listener2;
		this.field = new Figure[42];
	}
	
	private boolean hasAnythingMoveable(Player p) {
		ArrayList<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < 42; i++) {
			if (field[i] != null && (field[i].belongsTo(p))) {
				if (i%7 > 0) {
					if(field[i - 1] == null || 
							(field[i - 1] != null &&!field[i - 1].belongsTo(p)))
						moves.add(new Move(i, i-1, field));
				}				
				if (i%7 < 6) {
					if(field[i + 1] == null || 
							(field[i + 1] != null &&!field[i + 1].belongsTo(p)))
						moves.add(new Move(i, i+1, field));
				}
				
				if (i < 35) {
					if(field[i + 7] == null || 
							(field[i + 7] != null &&!field[i + 7].belongsTo(p)))
						moves.add(new Move(i, i+7, field));
				}
				
				if (i >= 7) {
					if(field[i - 7] == null || 
							(field[i - 7] != null &&!field[i - 7].belongsTo(p)))
						moves.add(new Move(i, i-7, field));
				}
			}
		}
		
		return (moves.size() > 0);
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
		if (this.listener1.getPlayer().equals(p) && done1 || 
			this.listener2.getPlayer().equals(p) && done2) {
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
			done1 = true;
		} else if (this.listener2.getPlayer().equals(p)) {
			done2 = true;
		}
		
		if(done1 && done2) {
			done1 = false;
			done2 = false;
			listener1.figureMoved();
			listener2.figureMoved();
			listener1.provideInitialChoice();
			listener2.provideInitialChoice();
		}
	}

	@Override
	public void setInitialChoice(Player p, FigureKind kind) throws RemoteException {
		
		//Wait for players
		if (listener1.getPlayer().equals(p) && !done1) {
			choice1 = kind;
			done1 = true;
		} else if (listener2.getPlayer().equals(p) && !done2) {
			done2 = true;
			choice2 = kind;
		}
		
		if(done1 && done2) {
			if (choice1 != null && choice2 != null &&
				(choice1.attack(choice2) == AttackResult.WIN || 
				choice1.attack(choice2) == AttackResult.LOOSE)) {
				//Process the result
				if(choice1.attack(choice2) == AttackResult.WIN) {
					sendMessage(listener1.getPlayer(), listener1.getPlayer().getNick() + " gewinnt.");
					sendMessage(listener2.getPlayer(), listener1.getPlayer().getNick() + " gewinnt.");
					nextPlayer = 1;
				} else {
					sendMessage(listener1.getPlayer(), listener2.getPlayer().getNick() + " gewinnt.");
					sendMessage(listener2.getPlayer(), listener2.getPlayer().getNick() + " gewinnt.");
					nextPlayer = 2;
				}
				//Start the game
				if (nextPlayer == 1) {
					done1 = false;
					done2 = false;
					listener1.startGame();
					listener2.startGame();
					listener1.provideNextMove();
				} else if (nextPlayer == 2) {
					done1 = false;
					done2 = false;
					listener1.startGame();
					listener2.startGame();
					listener2.provideNextMove();
				}
			} else {
				sendMessage(listener1.getPlayer(), "Unentschieden");
				sendMessage(listener2.getPlayer(), "Unentschieden");
				done1 = false;
				done2 = false;
				listener1.provideInitialChoice();
				listener2.provideInitialChoice();
			}
		}
	}
	
	public void uncoverAll() {
		for(int i = 0; i < 42; i++) {
			if(field[i] != null) {
				field[i].setDiscovered();
			}
		}
	}

	
	public void move(Player movingPlayer, int fromIndex, int toIndex) throws RemoteException, IllegalStateException {
		//Is game drawn?
		if((!hasAnythingMoveable(listener1.getPlayer())) && (!hasAnythingMoveable(listener2.getPlayer()))) {
			listener1.gameIsDrawn();
			listener2.gameIsDrawn();
		}
		//Is movement legal?
		Figure[] tmpBoard = field.clone();
		boolean legalMovement = true;
		if(lastPlayer == 2 && movingPlayer.equals(listener1.getPlayer()))
			lastPlayer = 1;
		else if(lastPlayer == 1 && movingPlayer.equals(listener2.getPlayer()))
			lastPlayer = 2;
		else {
			if(movingPlayer.equals(listener1.getPlayer()))
				lastPlayer = 1;
			else
				lastPlayer = 2;
		}
		
		//if movement is legal, start moving
		if(legalMovement) {
			//Standard movement
			if(field[toIndex] == null) {
				field[toIndex] = field[fromIndex].clone();
				field[fromIndex] = null;
				listener1.figureMoved();
				listener2.figureMoved();
			}
			//Attacking
			if(field[toIndex].belongsTo(getOpponent(movingPlayer))) {
				listener1.figureAttacked();
				listener2.figureAttacked();
				
				AttackResult r = field[fromIndex].attack(field[toIndex]);
				tmpBoard[toIndex].setDiscovered();
				//Reacting to the result of the battle
				if(r == AttackResult.WIN) {
					field[toIndex] = field[fromIndex].clone();
					field[toIndex].setDiscovered();
					field[fromIndex] = null;
				} else if(r == AttackResult.LOOSE) {
					tmpBoard[toIndex].setDiscovered();
					field[toIndex].setDiscovered();
					field[fromIndex] = null;
				} else if(r == AttackResult.LOOSE_AGAINST_TRAP) {
					field[toIndex] = null;
					field[fromIndex] = null;
				} else if(r == AttackResult.WIN_AGAINST_FLAG) {
					uncoverAll();
					if(movingPlayer.equals(listener1.getPlayer())) {
						listener1.gameIsWon();
						listener2.gameIsLost();
					} else {
						listener2.gameIsWon();
						listener1.gameIsLost();
					}
				} else if(r == AttackResult.DRAW) {
					JOptionPane.showMessageDialog(null, "Auf Grund von sehr verwirrenden Fehlern " +
							"konnten wir leider keine Entscheidungsschlacht implementieren.", "Fehler!", JOptionPane.ERROR_MESSAGE);
					/*boolean success = false;
					
					FigureKind tmpLastType = null;
					
					while(!success) {					
						done1 = false;
						done2 = false;
						listener1.provideChoiceAfterFightIsDrawn();
						listener2.provideChoiceAfterFightIsDrawn();
						
						while(!(done1 && done2));
						
						r = choice1.attack(choice2);
						listener1.figureAttacked();
						listener2.figureAttacked();	
						if(r == AttackResult.WIN) {
							field[toIndex] = new Figure(choice1, listener1.getPlayer());
							field[fromIndex] = null;
							success = true;
						}
						else if(r == AttackResult.LOOSE) {
							field[toIndex] = new Figure(choice2, listener2.getPlayer());
							field[fromIndex] = null;
							success = true;
						} else if(tmpLastType == newType) {
							success = true;
							field[toIndex] = new Figure(newType, movingPlayer);
							field[fromIndex] = new Figure(newType, getOpponent(movingPlayer));
						}
					}*/
				}
			}
			
			//Update LastMove
			this.lastMove = new Move(fromIndex, toIndex, tmpBoard.clone());
			
			//Update moving player
			if(lastPlayer == 1) {
				if(hasAnythingMoveable(listener2.getPlayer()))
					listener2.provideNextMove();
				else
					listener1.provideNextMove();
			} else if(lastPlayer == 2) {
				if(hasAnythingMoveable(listener1.getPlayer()))
					listener1.provideNextMove();
				else
					listener2.provideNextMove();
			}
		}
	}

	@Override
	public void setUpdatedKindAfterDraw(Player p, FigureKind kind) throws RemoteException {

		if(p.equals(listener1.getPlayer()) && !done1) {
			done1 = true;
			choice1 = kind;
		} else if(p.equals(listener2.getPlayer()) && !done2) {
			done2 = true;
			choice2 = kind;
		}
	}

	@Override
	public void surrender(Player p) throws RemoteException, IllegalStateException {
		// check if both players surrendered at once
		listener1.chatMessage(p, "I surrender");
		listener2.chatMessage(p, "I surrender");
		
		if(!surrendered) {
			surrendered = true;
			if (listener1.getPlayer().equals(p)) {
				listener1.gameIsLost();
				listener2.gameIsWon();
			} else if (listener2.getPlayer().equals(p)) {
				listener1.gameIsWon();
				listener2.gameIsLost();
			}
		} else throw new IllegalStateException();
		
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