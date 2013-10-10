package nyu.hezzze.weiqi.shared;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;
import static nyu.hezzze.weiqi.shared.GameResult.WHITE_WIN;

/**
 * 
 * This is the Class encapsulating the end game information. When the game ends,
 * the field in the State class will be set to a instance of this class.
 * 
 * @author hezzze
 */
public class GameOver {

	private int pointsOfBlack;
	private int pointsOfWhite;
	private GameResult winner;

	/**
	 * Construct a GameOver objects containing the end game info
	 * 
	 * @param winner
	 * @param pointsOfBlack
	 * @param pointsOfWhite
	 */
	public GameOver(GameResult winner, int pointsOfBlack, int pointsOfWhite) {
		this.pointsOfBlack = pointsOfBlack;
		this.pointsOfWhite = pointsOfWhite;
		this.winner = winner;
	}

	public int getBlackPoints() {
		return pointsOfBlack;
	}

	public int getWhitePoints() {
		return pointsOfWhite;
	}

	public GameResult getGameResult() {
		return winner;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof GameOver)) {
			return false;
		}
		GameOver other = (GameOver) obj;
		return pointsOfBlack == other.pointsOfBlack
				&& pointsOfWhite == other.pointsOfWhite
				&& winner == other.winner;
	}

	public static String serialize(GameOver gameOver) {
		return gameOver.toString();
	}

	public static GameOver deserialize(String str) {
		String[] data = str.split("_");
		GameResult winner;
		if(data[0].equals("x")) {
			winner = BLACK_WIN;
		} else {
			winner = WHITE_WIN;
		}
		int pointsOfBlack = Integer.parseInt(data[1]);
		int pointsOfWhite = Integer.parseInt(data[2]);
		
		return new GameOver(winner, pointsOfBlack, pointsOfWhite);
	}

	@Override
	public String toString() {
		String winnerStr;
		if (winner == BLACK_WIN) {
			winnerStr = "x";
		} else {
			winnerStr = "o";
		}

		return winnerStr + "_" + pointsOfBlack + "_" + pointsOfWhite;
	}
}
