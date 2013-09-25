package nyu.hezzze.weiqi.shared;

/**
 * Class representing the two players of 
 * the game. A Go player uses either white
 * or black stones
 * 
 * @author hezzze
 *
 */
public enum Gamer {
	W, B;

	public static final Gamer EMPTY = null;
	public static final Gamer _ = EMPTY;
	public static final Gamer WHITE = W;
	public static final Gamer BLACK = B;
	public static final Gamer O = WHITE;
	public static final Gamer X = BLACK;

	public boolean isWhite() {
		return this == WHITE;
	}

	public boolean isBlack() {
		return this == BLACK;
	}

	public Gamer getOpponent() {
		return this == WHITE ? BLACK : WHITE;
	}

}
