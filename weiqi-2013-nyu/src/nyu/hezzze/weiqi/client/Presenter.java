package nyu.hezzze.weiqi.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

import nyu.hezzze.weiqi.shared.GameOverException;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.GoBoard;
import nyu.hezzze.weiqi.shared.IllegalMove;
import nyu.hezzze.weiqi.shared.Position;
import nyu.hezzze.weiqi.shared.State;

public class Presenter {

	final View graphics;
	private State currentState;
	private Gamer[][] board;

	public interface View {
		void setCell(int row, int col, Gamer gamer);
	}

	public Presenter(View graphics) {
		this.graphics = graphics;
		currentState = new State();
		board = currentState.getBoard();
		History.newItem("state=" + State.serialize(currentState));

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				try {
					if (historyToken.substring(0, 6).equals("state=")) {
						String str = historyToken.substring(6);
						State state = State.deserialize(str);
						setState(state);
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println("State does not exist!");
				}

			}

		});
	}

	void makeMove(int row, int col) {
		Position pos = new Position(row, col);

		try {
			currentState = currentState.makeMove(pos);
			Gamer[][] newBoard = currentState.getBoard();
			updateBoard(newBoard);
			History.newItem("state=" + State.serialize(currentState));

		} catch (IllegalMove e) {
			System.out.println(e.getMessage());
		} catch (GameOverException e) {
			System.out.println(e.getMessage());
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

	private void setState(State state) {
		currentState = state;
		Gamer [][] newBoard = currentState.getBoard();
		updateBoard(newBoard);
	}

}
