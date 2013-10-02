package nyu.hezzze.weiqi.client;

import nyu.hezzze.weiqi.shared.GameOver;
import nyu.hezzze.weiqi.shared.GameOverException;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.GoBoard;
import nyu.hezzze.weiqi.shared.IllegalMove;
import nyu.hezzze.weiqi.shared.Position;
import nyu.hezzze.weiqi.shared.State;

public class Presenter {

	final View graphics;
	State currentState;
	Gamer[][] board;

	public interface View {
		void setCell(int row, int col, Gamer gamer);

		void setWhoseTurn(Gamer gamer);

		void setMessage(String msg);
		void showGameResult(GameOver gameOver);
	}

	public Presenter(View graphics) {
		this.graphics = graphics;
		currentState = new State();
		board = currentState.getBoard();

	}

	void makeMove(int row, int col) {
		Position pos = new Position(row, col);

		try {
			currentState = currentState.makeMove(pos);
			Gamer[][] newBoard = currentState.getBoard();

			updateLabels();
			updateBoard(newBoard);

		} catch (IllegalMove e) {
			graphics.setMessage(e.getMessage());
		} catch (GameOverException e) {
			graphics.setMessage(e.getMessage());
		}

	}

	private void updateBoard(Gamer[][] newBoard) {
		for (int i = 0; i < GoBoard.ROWS; i++) {
			for (int j = 0; j < GoBoard.COLS; j++) {
				if (board[i][j] != newBoard[i][j]) {
					graphics.setCell(i, j, newBoard[i][j]);
				}

			}
		}
		board = newBoard;
	}

	void setState(State state) {
		currentState = state;
		Gamer[][] newBoard = currentState.getBoard();

		updateLabels();
		updateBoard(newBoard);
	}

	private void updateLabels() {
		graphics.setMessage("");
		graphics.setWhoseTurn(currentState.whoseTurn());
	}

	public void pass() {
		try {
			currentState = currentState.pass();
			updateLabels();
			if (currentState.getGameOver()!=null) {
				graphics.showGameResult(currentState.getGameOver());
			}
		} catch (GameOverException e) {
			graphics.setMessage(e.getMessage());
		}

	}

	

}
