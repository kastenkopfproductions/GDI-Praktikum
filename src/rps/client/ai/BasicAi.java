package rps.client.ai;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rps.client.GameListener;
import rps.game.data.FigureKind;
import rps.game.Game;
import rps.game.data.Figure;
import rps.game.data.Move;
import rps.game.data.Player;

import static rps.game.data.FigureKind.FLAG;
import static rps.game.data.FigureKind.PAPER;
import static rps.game.data.FigureKind.ROCK;
import static rps.game.data.FigureKind.SCISSORS;
import static rps.game.data.FigureKind.TRAP;

/**
 * This class contains a very basic AI, that allows to play a game against it.
 * The main benefit is to be able to test the UI.
 */
public class BasicAi implements GameListener {

	private Player player = new Player("Basic AI");
	private Game game;

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public void chatMessage(Player sender, String message) throws RemoteException {
		// TODO Auto-generated method stub
		if (!player.equals(sender)) {
			game.sendMessage(player, "you said: " + message);
		}
	}

	@Override
	public void provideInitialAssignment(Game game) throws RemoteException {
		this.game = game;
		FigureKind[] assignment = {
				null, null, null, null, null, null, null,
				null, null, null, null, null, null, null,
				null, null, null, null, null, null, null,
				null, null, null, null, null, null, null,
				ROCK, FLAG, ROCK, ROCK, ROCK, PAPER, PAPER,
				PAPER, PAPER, SCISSORS, SCISSORS, SCISSORS, SCISSORS, TRAP
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
		// TODO Auto-generated method stub
		this.chatMessage(this.player, "GL HF");
	}

	@Override
	public void provideNextMove() throws RemoteException {
		// get all possible moves
		Figure[] figures = this.game.getField();
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for (int i = 0; i < 42; i++) {
			if (figures[i] != null && (figures[i].belongsTo(this.player)) && 
					!(figures[i].getKind() == TRAP || figures[i].getKind() == FLAG)) {
				if (i%7 > 0) {
					if(figures[i - 1] == null || 
							(figures[i - 1] != null &&!figures[i - 1].belongsTo(player)))
						moves.add(new Move(i, i-1, figures));
				}				
				if (i%7 < 6) {
					if(figures[i + 1] == null || 
							(figures[i + 1] != null &&!figures[i + 1].belongsTo(player)))
						moves.add(new Move(i, i+1, figures));
				}
				
				if (i < 35) {
					if(figures[i + 7] == null || 
							(figures[i + 7] != null &&!figures[i + 7].belongsTo(player)))
						moves.add(new Move(i, i+7, figures));
				}
				
				if (i >= 7) {
					if(figures[i - 7] == null || 
							(figures[i - 7] != null &&!figures[i - 7].belongsTo(player)))
						moves.add(new Move(i, i-7, figures));
				}
			}
		}
		
		// get the "best" possible move
		Move[] moveArray = new Move[moves.size()];
		for(int i = 0; i < moves.size(); i++) {
			moveArray[i] = (Move)moves.get(i);
		}
		Move moveToDo = moveArray[(int)(Math.random()*moveArray.length)];
		
		// send move to game
		game.move(this.player, moveToDo.getFrom(), moveToDo.getTo());		
		
	}

	@Override
	public void figureMoved() throws RemoteException {
		// TODO Auto-generated method stub
		this.chatMessage(this.player, "figureMoved()");
	}

	@Override
	public void figureAttacked() throws RemoteException {
		// TODO Auto-generated method stub
		this.chatMessage(this.player, "figureAttacked()");
	}

	@Override
	public void provideChoiceAfterFightIsDrawn() throws RemoteException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		this.chatMessage(this.player, "asdfasdfasdf!!!!!");
	}

	@Override
	public void gameIsWon() throws RemoteException {
		// TODO Auto-generated method stub
		this.chatMessage(this.player, "YEAAAAAAAAH!!!!!");
	}

	@Override
	public void gameIsDrawn() throws RemoteException {
		// TODO Auto-generated method stub
		this.chatMessage(this.player, "FFFFFUUUUU!!!!!");
	}

	@Override
	public String toString() {
		return player.getNick();
	}
}