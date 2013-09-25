package nyu.hezzze.weiqi.shared;

/**
 * 
 * This is the Class encapsulating the end game information.
 * When the game ends, the field in the State class will be set
 * to a instance of this class.
 * 
 * @author hezzze
 */
public class GameOver {

	private int pointsOfBlack;
	private int pointsOfWhite;
	private GameResult winner;

	/**
	 * Construct a GameOver objects containing the 
	 * end game info
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
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GameOver)) {
			return false;
		}
		GameOver other = (GameOver) obj;
		return pointsOfBlack == other.pointsOfBlack
				&& pointsOfWhite == other.pointsOfWhite
				&& winner == other.winner;
	}
}
