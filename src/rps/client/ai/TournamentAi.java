package rps.client.ai;

import static rps.game.data.FigureKind.FLAG;
import static rps.game.data.FigureKind.PAPER;
import static rps.game.data.FigureKind.ROCK;
import static rps.game.data.FigureKind.SCISSORS;
import static rps.game.data.FigureKind.TRAP;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rps.client.GameListener;
import rps.game.Game;
import rps.game.data.Figure;
import rps.game.data.FigureKind;
import rps.game.data.Move;
import rps.game.data.Player;

/**
 * This class contains an advanced AI, that should participate in the
 * tournament.
 */
public class TournamentAi implements GameListener {

	Player player = new Player("Tournament AI");
	Game game;
	
	private long moveCalculationStartedAt;
	private final int maxDurationForMoveInMilliSeconds;
	private final int maxDurationForAllMovesInMilliSeconds;

	public Player getPlayer() {
		return player;
	}
	
	public TournamentAi(int maxDurationForMoveInMilliSeconds, int maxDurationForAllMovesInMilliSeconds) {
		this.maxDurationForMoveInMilliSeconds = maxDurationForMoveInMilliSeconds;
		this.maxDurationForAllMovesInMilliSeconds = maxDurationForAllMovesInMilliSeconds;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void chatMessage(Player sender, String message) throws RemoteException {
		if(player.equals(sender))
			game.sendMessage(sender, message);
	}

	@Override
	public void provideInitialAssignment(Game game) throws RemoteException {
		this.game = game;
		FigureKind[] assignment = {
				PAPER, ROCK, SCISSORS, TRAP, SCISSORS, ROCK, PAPER, 
				ROCK, SCISSORS, PAPER, FLAG, PAPER, SCISSORS, ROCK
		};
		this.game.setInitialAssignment(this.player, assignment);
	}

	@Override
	public void provideInitialChoice() throws RemoteException {
		int rnd = (int) (Math.random()*3);
		if (rnd == 0) {
			this.game.setInitialChoice(this.player, rps.game.data.FigureKind.ROCK);
		} else if (rnd == 1) {
			this.game.setInitialChoice(this.player, rps.game.data.FigureKind.PAPER);
		} else if (rnd == 2) {
			this.game.setInitialChoice(this.player, rps.game.data.FigureKind.SCISSORS);
		} else {
			System.out.println("WTF??? Randomed a value >= 3 for the initial choice!!!");
		}
	}

	@Override
	public void startGame() throws RemoteException {
		chatMessage(player, "Good Luck and have fun!");
	}

	@Override
	public void provideNextMove() throws RemoteException {
		
		moveCalculationStartedAt = System.nanoTime();

		//Get all possible Moves
		Figure[] figures = this.game.getField();
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for (int i = 0; i < 42; i++) {
			if (figures[i] != null && !(figures[i].belongsTo(this.player))) {
				if (i%7 != 0) {
					if(figures[i - 1] == null || !figures[i - 1].belongsTo(player))
						moves.add(new Move(i, i-1, figures));
				}
				
				if ((i+1)%7 != 0) {
					if(figures[i + 1] == null || !figures[i + 1].belongsTo(player))
						moves.add(new Move(i, i+1, figures));
				}
				
				if (i <= 35) {
					if(figures[i + 7] == null || !figures[i + 7].belongsTo(player))
						moves.add(new Move(i, i+7, figures));
				}
				
				if (i%7 >= 7) {
					if(figures[i - 7] == null || !figures[i - 7].belongsTo(player))
						moves.add(new Move(i, i-7, figures));
				}
			}
		}
		
		// get the "best" possible move
		Move[] moveArray = (Move[]) moves.toArray();
		
		ArrayList<Move> maxMoves = new ArrayList<Move>();
		
		int actMax = -100;
		
		for(Move move : moveArray) {
			Figure[] updatedBoard = getUpdatedBoard(figures, move, player);
			int tmp = minValue(updatedBoard, -200, 200, 5);
			
			if(tmp > actMax) {
				maxMoves.clear();
				maxMoves.add(move);
				actMax = tmp;
			} else if(tmp == actMax) {
				maxMoves.add(move);
			}
		}
		
	}
	
	public int maxValue(Figure[] state, int alpha, int beta, int k) throws RemoteException {
		
		//Get all possible Moves
		Figure[] figures = this.game.getField();
		ArrayList<Move> moves = new ArrayList<Move>();
				
		for (int i = 0; i < 42; i++) {
			if (figures[i] != null && !(figures[i].belongsTo(this.player))) {
				if (i%7 != 0) {
					if(figures[i - 1] == null || !figures[i - 1].belongsTo(player))
						moves.add(new Move(i, i-1, figures));
				}
				
				if ((i+1)%7 != 0) {
					if(figures[i + 1] == null || !figures[i + 1].belongsTo(player))
						moves.add(new Move(i, i+1, figures));
				}
				
				if (i <= 35) {
					if(figures[i + 7] == null || !figures[i + 7].belongsTo(player))
						moves.add(new Move(i, i+7, figures));
				}
				
				if (i%7 >= 7) {
					if(figures[i - 7] == null || !figures[i - 7].belongsTo(player))
						moves.add(new Move(i, i-7, figures));
				}
			}
		}
		
		// get the "best" possible move
		Move[] moveArray = (Move[]) moves.toArray();
		
		int actMax = -200;
		
		for(Move move : moveArray) {
			
			Figure[] updatedBoard = getUpdatedBoard(figures, move, player);
			
			int tmp;
			
			if(k > 1)
				tmp = minValue(updatedBoard, alpha, beta, k - 1);
			else
				tmp = doHeuristics(updatedBoard);
			
			actMax = Math.max(actMax, tmp);
			
			if(actMax >= beta) {
				return actMax;
			}
			
			alpha = Math.max(alpha, actMax);
			
			if((System.nanoTime() - moveCalculationStartedAt) / 1000000 > maxDurationForMoveInMilliSeconds - 100) {
				return Math.max(actMax, doHeuristics(updatedBoard));
			}
		}
		
		return actMax;
	}
	
	public int minValue(Figure[] state, int alpha, int beta, int k) throws RemoteException {
		
		//Get all possible Moves
		Figure[] figures = this.game.getField();
		ArrayList<Move> moves = new ArrayList<Move>();
				
		for (int i = 0; i < 42; i++) {
			// check if it is your figure
			if (figures[i] != null && !(figures[i].belongsTo(game.getOpponent(player)))) {
				if (i%7 != 0) {
					if(figures[i - 1] == null || figures[i - 1].belongsTo(player))
						moves.add(new Move(i, i-1, figures));
				}
				
				if ((i+1)%7 != 0) {
					if(figures[i + 1] == null || figures[i + 1].belongsTo(player))
						moves.add(new Move(i, i+1, figures));
				}
				
				if (i <= 35) {
					if(figures[i + 7] == null || figures[i + 7].belongsTo(player))
						moves.add(new Move(i, i+7, figures));
				}
				
				if (i%7 >= 7) {
					if(figures[i - 7] == null || figures[i - 7].belongsTo(player))
						moves.add(new Move(i, i-7, figures));
				}	
			}
		}
		
		// get the "best" possible move
		Move[] moveArray = (Move[]) moves.toArray();
		
		int actMin = 200;
		
		for(Move move : moveArray) {
			Figure[] updatedBoard = getUpdatedBoard(figures, move, player);
			int tmp;
			
			if(k > 1)
				tmp = maxValue(updatedBoard, alpha, beta, k - 1);
			else
				tmp = doHeuristics(updatedBoard);
			
			actMin = Math.min(actMin, tmp);
			
			if(actMin <= alpha) {
				return actMin;
			}
			
			alpha = Math.min(beta, actMin);
			
			if((System.nanoTime() - moveCalculationStartedAt) / 1000000 > maxDurationForMoveInMilliSeconds - 100) {
				return Math.min(actMin, doHeuristics(updatedBoard));
			}
		}
		
		return actMin;
	}
	
	private int doHeuristics(Figure[] board) {
		
		int numPointsPlayer = 0;
		int numPointsOpponent = 0;
		
		int ownFigures = 0;
		int opposingFigures = 0;
		
		//Iterate through the board and counts points for each player
		//		1 Point is given for every living figure of each player
		//		2 Points are given for every figure of the same player, that
		//			is reachable from the actual position
		//The number of the figures is also counted
		for(int i = 0; i < 49; i++) {
			if(board[i] != null) {
				if(board[i].belongsTo(player)) {
					numPointsPlayer++;
					ownFigures++;
					int x = (i - (i % 7)) / 6;
					int y = i % 7;
					if(x > 0) {
						if(board[i - 1] != null && board[i - 1].belongsTo(player))
							numPointsPlayer += 2;
					}
					if(x < 5) {
						if(board[i + 1] != null && board[i + 1].belongsTo(player))
							numPointsPlayer += 2;
					}
					if(y > 0) {
						if(board[i - 7] != null && board[i - 7].belongsTo(player))
							numPointsPlayer += 2;
					}
					if( i < 6) {
						if(board[i + 7] != null && board[i + 7].belongsTo(player))
							numPointsPlayer += 2;
					}
				}
				else {
					opposingFigures++;
					numPointsOpponent++;
					int x = (i - (i % 7)) / 6;
					int y = i % 7;
					if(x > 0) {
						if(board[i - 1] != null && !board[i - 1].belongsTo(player))
							numPointsOpponent += 2;
					}
					if(x < 5) {
						if(board[i + 1] != null && !board[i + 1].belongsTo(player))
							numPointsOpponent += 2;
					}
					if(y > 0) {
						if(board[i - 7] != null && !board[i - 7].belongsTo(player))
							numPointsOpponent += 2;
					}
					if( i < 6) {
						if(board[i + 7] != null && !board[i + 7].belongsTo(player))
							numPointsOpponent += 2;
					}
				}
			}
		}
		
		//Opponent only has left his flag and player has more than 2 figures left
		if(opposingFigures == 1 && ownFigures > 2)
			return 150;
		//Same situation, but turned around
		if(ownFigures == 1 && opposingFigures > 2)
			return -150;
		//During the game...
		return numPointsPlayer - numPointsOpponent;
	}

	private Figure[] getUpdatedBoard(Figure[] board, Move move, Player player) {
		Figure[] updatedBoard = new Figure[board.length];
		//Create Temporary Board for movement
		for(int i = 0; i < board.length; i++) {
			updatedBoard[i] = new Figure(board[i].getKind(), player);
		}
		updatedBoard[move.getTo()] = updatedBoard[move.getFrom()];
		updatedBoard[move.getFrom()] = null;
		return board;
	}
	
	@Override
	public void figureMoved() throws RemoteException {
		chatMessage(player, "Good Luck and have fun!");
	}

	@Override
	public void figureAttacked() throws RemoteException {
		chatMessage(player, "Some Figure has moved.");
	}

	@Override
	public void provideChoiceAfterFightIsDrawn() throws RemoteException {
		int rnd = (int) (Math.random()*3);
		if (rnd == 0) {
			this.game.setInitialChoice(this.player, FigureKind.ROCK);
		} else if (rnd == 1) {
			this.game.setInitialChoice(this.player, FigureKind.PAPER);
		} else if (rnd == 2) {
			this.game.setInitialChoice(this.player, FigureKind.SCISSORS);
		} else {
			System.out.println("WTF??? Randomed a value >= 3 for the after fight choice!!!");
		}

	}

	@Override
	public void gameIsLost() throws RemoteException {
		chatMessage(player, "Good Game...");
	}

	@Override
	public void gameIsWon() throws RemoteException {
		chatMessage(player, "Dann ziehe ich mich jetzt mal als ungeschlagener Champion zurück...");
	}

	@Override
	public void gameIsDrawn() throws RemoteException {
		chatMessage(player, "Wenn ich gewollt hätte...");
	}

	@Override
	public String toString() {
		return "Tournament AI";
	}
}