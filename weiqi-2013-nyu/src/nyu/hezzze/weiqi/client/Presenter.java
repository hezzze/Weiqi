package nyu.hezzze.weiqi.client;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;
import nyu.hezzze.weiqi.shared.GameOver;
import nyu.hezzze.weiqi.shared.GameOverException;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.GoBoard;
import nyu.hezzze.weiqi.shared.IllegalMove;
import nyu.hezzze.weiqi.shared.Position;
import nyu.hezzze.weiqi.shared.State;

import com.google.gwt.storage.client.Storage;

/**
 * The presenter of the game, following the MVP design pattern, it has a public
 * interface for the view to implement and need a view as a parameter for
 * construction
 * 
 * @author hezzze
 * 
 */
public class Presenter {

	/**
	 * The view of the game
	 */
	final View graphics;
	State currentState;
	Gamer[][] board;

	/**
	 * The local storage for saving games
	 */
	Storage storage;

	/**
	 * specify a bunch of methods for the presenter to talk to the view
	 * 
	 * @author hezzze
	 * 
	 */
	public interface View {
		void setCell(int row, int col, Gamer gamer);

		void setWhoseTurn(Gamer gamer);

		void setMessage(String msg);

		void showStatus(String html);

		void setButton(String str);

		void setGameOver(boolean isGameOver);

		void animateSetStone(int row, int col, Gamer gamer);
	}

	/**
	 * Taking a view as a parameter, the presenter initialize itself with a new
	 * state
	 * 
	 * @param graphics
	 *            the view of the game
	 */
	public Presenter(final View graphics) {
		this.graphics = graphics;
		currentState = new State();
		board = currentState.getBoard();

		storage = Storage.getLocalStorageIfSupported();

	}

	/**
	 * Try to make a move for the current player, if the move is valid, then
	 * update the current state and the view, otherwise tell the view to display
	 * error message to the user
	 * 
	 * @param row
	 *            the row index of the move
	 * @param col
	 *            the column index of the move
	 */
	void makeMove(int row, int col) {
		Position pos = new Position(row, col);

		try {
			currentState = currentState.makeMove(pos);

		} catch (IllegalMove e) {
			graphics.setMessage(e.getMessage());
		} catch (GameOverException e) {
			graphics.setMessage(e.getMessage());
		}

	}

	void makeAnimatedMove(int row, int col) {
		Position pos = new Position(row, col);
		Gamer gamer = currentState.whoseTurn();

		try {
			currentState = currentState.makeMove(pos);

			graphics.animateSetStone(row, col, gamer);

		} catch (IllegalMove e) {
			graphics.setMessage(e.getMessage());
		} catch (GameOverException e) {
			graphics.setMessage(e.getMessage());
		}
	}

	/**
	 * Pass for the current player, display an error message if the game is over
	 */
	public void pass() {
		try {
			currentState = currentState.pass();

		} catch (GameOverException e) {
			graphics.setMessage(e.getMessage());
		}

	}

	/**
	 * Updating the board by finding the different cell between the old and new
	 * board
	 * 
	 * @param newBoard
	 *            the board to be updated to
	 */
	void updateBoard(Gamer[][] newBoard) {
		for (int i = 0; i < GoBoard.ROWS; i++) {
			for (int j = 0; j < GoBoard.COLS; j++) {
				if (board[i][j] != newBoard[i][j]) {
					graphics.setCell(i, j, newBoard[i][j]);
				}

			}
		}
		board = newBoard;
	}

	/**
	 * Set the state of the game, along with the history API enabling the user
	 * to use the back button on the browser to go forward and back to a
	 * specific state
	 * 
	 * @param state
	 */
	void setState(State state) {
		currentState = state;
		Gamer[][] newBoard = currentState.getBoard();

		updateInfo();
		updateBoard(newBoard);
	}

	/**
	 * Update the information of the game, including refreshing the error label,
	 * update the current player and display game result
	 */
	void updateInfo() {
		graphics.setMessage("");
		graphics.setWhoseTurn(currentState.whoseTurn());
		if (currentState.getGameOver() != null) {
			showEndGameInfo();
		} else {
			graphics.showStatus("Still on...");
			graphics.setButton("PASS");
			graphics.setGameOver(false);
		}
	}

	/**
	 * Show the end game info using html, current it shows the winner, and the
	 * points gained by both player, more elements is to add to this methods
	 * like remaining stones or the time of the game etc.
	 */
	void showEndGameInfo() {
		String html = "";
		GameOver gameOver = currentState.getGameOver();

		if (gameOver.getGameResult() == BLACK_WIN) {
			html += "Black Wins!!! <br>";
		} else {
			html += "White Wins!!! <br>";
		}

		html += "Black Points: " + gameOver.getBlackPoints()
				+ "<br>White Points: " + gameOver.getWhitePoints();

		graphics.showStatus(html);
		graphics.setButton("RESTART");
		graphics.setGameOver(true);
	}

	void restartGame() {
		currentState = new State();

	}

	void saveGame(String key) {
		
		if (storage != null) {
			storage.setItem(key, State.serialize(currentState));
		}
	}

	String[] getSavedGameNames() {
		String[] names = new String[storage.getLength()];
		for (int i = 0; i < names.length; i++) {
			names[i] = storage.key(i);
		}
		return names;

	}

	void loadGame(String key) {
		if (storage != null) {
			setState(State.deserialize(storage.getItem(key)));
		}

	}

}
