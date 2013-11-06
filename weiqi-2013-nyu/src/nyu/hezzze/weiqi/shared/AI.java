package nyu.hezzze.weiqi.shared;

import java.util.Random;

/**
 * This class represents the AI of the game, currently the AI player will just play randomly, 
 * the goal of the making a decent computer player of go is beyond my capability right now 
 * because of inherent complexity of the game.
 * 
 * @author hezzze
 *
 */
public class AI {

	Random rgen = new Random();

	public State autoplay(State state, int trials) {
		if (trials == 0 || state.passLastTurn()) {
			return state.pass();
		}
		int row = rgen.nextInt(Go.ROWS);
		int col = rgen.nextInt(Go.COLS);
		State newState = null;
		try {
			newState = state.makeMove(new Position(row, col));
			
		} catch (IllegalMove e) {
			return autoplay(state, trials - 1);
		} catch (GameOverException e) {
			return state;
		}
		return newState;
	}

}
