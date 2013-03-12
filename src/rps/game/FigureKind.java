package rps.game;

import rps.game.data.AttackResult;

public enum FigureKind {
	ROCK, PAPER, SCISSORS, LIZARD, SPOCK, FLAG, TRAP, HIDDEN;

	private FigureKind weakness[] = new FigureKind[2];

	static {
		ROCK.setWeakness(PAPER, SPOCK);
		PAPER.setWeakness(SCISSORS, LIZARD);
		SCISSORS.setWeakness(ROCK, SPOCK);
	}

	private void setWeakness(FigureKind weakness1, FigureKind weakness2) {
		this.weakness[0] = weakness1;
		this.weakness[1] = weakness2;
	}

	public AttackResult attack(FigureKind kind) {
		if (equals(kind)) {
			return AttackResult.DRAW;
		} else if (kind == weakness[0] || kind == weakness[1]) {
			return AttackResult.LOOSE;
		} else if (kind == FigureKind.FLAG) {
			return AttackResult.WIN_AGAINST_FLAG;
		} else if (kind == FigureKind.TRAP) {
			return AttackResult.LOOSE_AGAINST_TRAP;
		} else {
			return AttackResult.WIN;
		}
	}

	public boolean isMovable() {
		return ordinal() <= 4;
	}
}