package nyu.hezzze.weiqi.client;

import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.O;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;
import static nyu.hezzze.weiqi.shared.Gamer.X;
import static nyu.hezzze.weiqi.shared.Gamer._;
import static nyu.hezzze.weiqi.shared.GameResult.WHITE_WIN;
import nyu.hezzze.weiqi.shared.GameOver;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.State;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Some unit tests for the presenter, more test cases are to be added
 * 
 * @author hezzze
 * 
 */
public class PresenterTest {

	Presenter.View view;
	Presenter presenter;

	@Before
	public void setup() {
		view = Mockito.mock(Presenter.View.class);
		presenter = new Presenter(view);
	}

	/**
	 * The makeMove method should: <br>
	 * setting the appropriate cell to show the right image if the move is valid
	 * update the error message label and the image for the current player
	 */
	@Test
	public void testMakeMove() {

		Gamer whoseTurn = presenter.currentState.whoseTurn();
		presenter.makeMove(3, 4);
		Gamer nextTurn = presenter.currentState.whoseTurn();
		presenter.setState(presenter.currentState);
		
		Mockito.verify(view).setCell(3, 4, whoseTurn);
		Mockito.verify(view).setMessage("");
		Mockito.verify(view).setWhoseTurn(nextTurn);
	}

	@Test
	public void testSetState() {
		Gamer[][] board = {
				// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
				{ _, X, X, _, X, _, _, _, _, _, _, _, _, _, X, O, O, _, _ }, // 0
				{ _, O, X, X, O, X, _, X, X, O, _, O, _, X, _, X, O, _, _ }, // 1
				{ X, O, X, O, O, X, X, O, O, O, _, O, X, _, X, _, O, _, _ }, // 2
				{ O, _, O, O, _, X, O, O, _, _, O, X, _, X, _, X, O, _, _ }, // 3
				{ O, _, _, O, _, X, X, O, X, X, O, _, X, X, _, X, O, O, _ }, // 4
				{ _, O, O, O, _, _, _, X, O, _, O, _, _, _, O, _, O, X, _ }, // 5
				{ _, X, _, O, X, X, _, X, O, _, O, X, X, O, _, _, O, X, _ }, // 6
				{ _, O, X, X, O, X, _, _, X, O, O, O, X, O, _, _, X, O, _ }, // 7
				{ O, _, O, O, O, X, _, _, X, _, _, X, _, O, _, X, X, X, _ }, // 8
				{ _, O, O, O, X, _, X, _, _, _, X, _, O, O, _, X, _, _, _ }, // 9
				{ _, _, _, O, O, X, _, X, _, _, _, X, X, O, _, O, X, _, _ }, // 10
				{ _, X, O, _, X, O, X, X, _, _, _, _, X, O, _, O, X, X, X }, // 11
				{ X, _, X, X, O, O, O, O, X, X, _, _, _, X, X, O, O, X, _ }, // 12
				{ _, X, O, O, O, _, _, _, O, O, X, X, _, X, O, X, O, O, X }, // 13
				{ _, X, X, _, _, _, _, _, _, _, _, _, _, _, O, X, X, X, _ }, // 14
				{ X, _, X, _, X, X, X, _, _, X, O, X, X, O, O, O, X, _, _ }, // 15
				{ _, X, O, O, O, O, _, X, X, _, X, O, O, _, O, X, X, X, _ }, // 16
				{ X, O, _, _, _, _, O, O, O, X, X, X, O, _, O, X, X, O, O }, // 17
				{ _, O, _, _, _, _, _, _, _, _, X, _, _, _, _, O, O, O, _ } // 18
		};

		State state = new State(board, BLACK, false, null);
		presenter.setState(state);

		// Just a random test to see if the
		// cell is set appropriately
		Mockito.verify(view).setCell(13, 8, WHITE);
		Mockito.verify(view).setMessage("");
		Mockito.verify(view).setWhoseTurn(BLACK);

	}

	@Test
	public void testPass() {
		presenter.pass();
		Gamer nextTurn = presenter.currentState.whoseTurn();
		presenter.setState(presenter.currentState);
		
		Mockito.verify(view).setMessage("");
		Mockito.verify(view).setWhoseTurn(nextTurn);

	}

	/**
	 * The view should call methods to show the right status when the game is
	 * over
	 */
	@Test
	public void testGameOver() {
		Gamer[][] finalBoard = {
				// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
				{ _, _, _, O, O, X, _, _, _, _, X, O, O, O, O, _, _, _, _ }, // 0
				{ _, _, _, O, X, X, X, _, _, _, _, X, X, X, O, _, O, _, O }, // 1
				{ _, _, _, O, X, X, X, X, X, _, X, _, X, O, _, _, O, O, X }, // 2
				{ _, _, O, _, O, X, O, O, X, _, X, _, X, O, O, O, X, X, X }, // 3
				{ _, _, O, O, O, O, O, O, O, X, X, X, O, O, O, X, _, X, _ }, // 4
				{ _, O, X, X, X, O, O, X, X, O, X, X, X, O, O, X, X, _, _ }, // 5
				{ _, _, O, X, X, X, X, X, O, O, O, X, X, O, O, X, _, _, _ }, // 6
				{ _, _, O, O, X, _, _, X, O, O, X, O, O, O, X, X, O, X, _ }, // 7
				{ _, O, X, X, _, X, X, X, O, O, X, X, X, X, X, O, X, X, _ }, // 8
				{ _, O, X, X, X, X, O, O, X, O, X, O, X, O, X, O, O, _, _ }, // 9
				{ _, O, X, O, O, O, _, _, X, O, X, O, O, O, X, _, _, _, _ }, // 10
				{ O, O, O, _, _, O, O, O, O, O, X, O, _, O, O, X, _, _, _ }, // 11
				{ X, X, O, O, O, X, X, X, O, X, O, O, O, X, X, X, _, _, _ }, // 12
				{ X, _, X, O, X, X, _, X, O, _, X, _, O, X, _, _, _, _, _ }, // 13
				{ _, X, X, O, X, _, X, O, O, _, _, _, _, O, X, X, X, _, _ }, // 14
				{ _, O, X, X, _, X, O, O, O, _, _, _, _, O, O, O, X, X, X }, // 15
				{ _, X, _, _, _, X, X, O, _, _, _, _, _, _, _, _, O, O, X }, // 16
				{ _, _, _, O, X, _, X, X, O, O, _, _, _, _, _, _, O, _, O }, // 17
				{ _, _, _, _, _, _, _, X, X, O, _, _, _, _, _, _, _, _, _ } // 18
		};

		presenter.setState(new State(finalBoard, WHITE, true, new GameOver(
				WHITE_WIN, 182, 179)));

		Mockito.verify(view).showStatus(
				"White Wins!!! <br>Black Points: 182<br>White Points: 179");

	}

}
