package nyu.hezzze.weiqi.shared;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;
import static nyu.hezzze.weiqi.shared.GameResult.WHITE_WIN;
import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;
import static nyu.hezzze.weiqi.shared.Gamer.EMPTY;
import static nyu.hezzze.weiqi.shared.GoBoard.COLS;
import static nyu.hezzze.weiqi.shared.GoBoard.INIT_BOARD;
import static nyu.hezzze.weiqi.shared.GoBoard.ROWS;
import static nyu.hezzze.weiqi.shared.GoBoard.inBoard;

import java.util.Arrays;

import com.google.common.base.Objects;

/**
 * Representing a particular moment or state during a Go game
 * 
 * @author hezzze
 * 
 */
public class State {

	private final Gamer[][] board;
	private final Gamer whoseTurn;
	private GameOver gameOver;

	/**
	 * Indicates if the a player has passed last turn. In a Go game, when both
	 * player pass consecutively, the game ends. <br>
	 * http://en.wikipedia.org/wiki/Go_(board_game)#Passing
	 */
	private boolean passedLastTurn;

	public State() {
		board = INIT_BOARD;
		whoseTurn = BLACK;
		passedLastTurn = false;
		gameOver = null;

	}

	public State(Gamer[][] board, Gamer whoseTurn, boolean passedLastTurn,
			GameOver gameOver) {
		this.board = board;
		this.whoseTurn = whoseTurn;
		this.passedLastTurn = passedLastTurn;
		this.gameOver = gameOver;
	}

	/**
	 * Representing a move from a player. The method will not mutate the
	 * receiver
	 * 
	 * @param pos
	 *            The position where the player puts the next stone
	 * @return The next state of the game
	 * @throws IllegalMove
	 *             an exception is thrown when the move is invalid
	 * @throws GameOverException
	 *             an exception is thrown when game is over
	 */
	public State makeMove(Position pos) throws IllegalMove, GameOverException {
		if (gameOver != null) {
			throw new GameOverException("Game is over!!!");
		}
		if (!inBoard(pos)) {
			throw new IllegalMove("Out of bounds");
		}
		Judge judge = new Judge(this);

		if (!judge.validMove(pos)) {
			throw new IllegalMove("Invalid Move!!");
		}

		Gamer[][] nextBoard = judge.removeGroupsWithoutLiberty();

		return new State(nextBoard, whoseTurn.getOpponent(), false, gameOver);
	}

	/**
	 * In each turn of a Go game, a player may choose to pass if no meaningful
	 * moves is present, the game ends when players pass consecutively
	 * 
	 * @return The next state of the game
	 * @throws GameOverException
	 *             Throws an exception if the game is over
	 */
	public State pass() throws GameOverException {
		if (gameOver != null) {
			throw new GameOverException("Game is over!!!");
		}

		Judge judge = new Judge(this);

		if (passedLastTurn) {
			judge.finalizeGame();
			if (judge.isBlackWin()) {
				gameOver = new GameOver(BLACK_WIN, judge.getPointsOfBlack(),
						judge.getPointsOfWhite());
			} else {
				gameOver = new GameOver(WHITE_WIN, judge.getPointsOfBlack(),
						judge.getPointsOfWhite());
			}
		}

		return new State(board, whoseTurn.getOpponent(), true, gameOver);
	}

	public Gamer[][] getBoard() {
		Gamer[][] newBoard = new Gamer[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}

	public Gamer whoseTurn() {
		return this.whoseTurn;
	}

	public GameOver getGameOver() {
		return gameOver;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof State)) {
			return false;
		}
		State other = (State) obj;
		return Arrays.deepEquals(board, other.board)
				&& whoseTurn.equals(other.whoseTurn)
				&& Objects.equal(gameOver, other.gameOver)
				&& passedLastTurn == other.passedLastTurn;

	}
	
	@Override
	public String toString() {
		Gamer[][] board = getBoard();
		String str = whoseTurn.toString() + "-";
		for (Gamer[] row : board) {
			for (Gamer gamer : row) {
				if (gamer != null) {
					str += gamer;
				} else {
					str += "E";
				}
			}
		}
		return str;
	}

	public static String serialize(State state) {
		return state.toString();

	}

	public static State deserialize(String str) {
		String[] data = str.split("-");
		Gamer whoseTurn = (data[0].equals("B")) ? BLACK : WHITE;
		String boardStr = data[1];
		Gamer[][] board = new Gamer[ROWS][COLS];
		for (int i = 0; i < boardStr.length(); i++) {
			char cell = boardStr.charAt(i);
			if (cell == 'W') {
				board[i / ROWS][i % COLS] = WHITE;
			} else if (cell == 'B') {
				board[i / ROWS][i % COLS] = BLACK;
			} else {
				board[i / ROWS][i % COLS] = EMPTY;
			}
		}

		return new State(board, whoseTurn, false, null);

	}
}
