package rps.client.ai;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rps.client.GameListener;
import rps.game.FigureKind;
import rps.game.Game;
import rps.game.data.Figure;
import rps.game.data.Move;
import rps.game.data.Player;

import static rps.game.FigureKind.FLAG;
import static rps.game.FigureKind.PAPER;
import static rps.game.FigureKind.ROCK;
import static rps.game.FigureKind.SCISSORS;
import static rps.game.FigureKind.TRAP;

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
				PAPER, PAPER, SCISSORS, SCISSORS, SCISSORS, SCISSORS
		};
		this.game.setInitialAssignment(this.player, assignment);
	}

	@Override
	public void provideInitialChoice() throws RemoteException {
		int rnd = (int) (Math.random()*3);
		if (rnd == 0) {
			this.game.setInitialChoice(this.player, FigureKind.ROCK);
		} else if (rnd == 1) {
			this.game.setInitialChoice(this.player, FigureKind.PAPER);
		} else if (rnd == 2) {
			this.game.setInitialChoice(this.player, FigureKind.SCISSORS);
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
		
		for (int i = 0; i < 7; i++) {
			// check if it is your figure
			if (figures[i].belongsTo(this.player)) {
				if (i%7 != 0) {
					System.out.println("Can move Figure @" + i + "left");
					moves.add(new Move(i, i-1, figures));
				}
				
				if ((i+1)%7 != 0) {
					System.out.println("Can move Figure @" + i + "right");
					moves.add(new Move(i, i+1, figures));
				}
				
				if (i <= 41) {
					System.out.println("Can move Figure @" + i + "up");
					moves.add(new Move(i, i+7, figures));
				}
				
				if (i%7 >= 7) {
					System.out.println("Can move Figure @" + i + "down");
					moves.add(new Move(i, i-7, figures));
				}				
			}
		}
		
		// get the "best" possible move
		Move[] moveArray = (Move[]) moves.toArray();
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