package nyu.hezzze.weiqi.shared;

import static nyu.hezzze.weiqi.shared.Gamer.O;
import static nyu.hezzze.weiqi.shared.Gamer.X;
import static nyu.hezzze.weiqi.shared.Gamer._;
import static nyu.hezzze.weiqi.shared.Gamer.BLACK;

public class State {
	
	public static final Gamer[][] INIT_BOARD = {
		//0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 0
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 1
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 2
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 3
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 4
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 5
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 6
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 7
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 8
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 9
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 10
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 11
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 12
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 13
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 14 
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 15
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 16
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 17
		{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }  // 18
	};
	
	private final Gamer[][] board;

	private Gamer whoseTurn;
	
	public State() {
		board = INIT_BOARD;
		whoseTurn = BLACK;
	}
	
	
	public State(Gamer[][] board, Gamer whoseTurn) {
		// TODO Auto-generated constructor stub
		this.board = board;
		this.whoseTurn = whoseTurn;
	}


	public State makeMove(Position pos) {
		//TODO implementation for making a move in the game
		return new State(board.clone(), whoseTurn.getNext());
	}


	public Gamer[][] getBoard() {
		// TODO Auto-generated method stub
		return null;
	}


	public Gamer whoseTurn() {
		// TODO Auto-generated method stub
		return this.whoseTurn;
	}


	public GameOver getGameOver() {
		// TODO Auto-generated method stub
		return null;
	}

}
