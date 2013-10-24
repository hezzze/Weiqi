package nyu.hezzze.weiqi.shared;

import static nyu.hezzze.weiqi.shared.Gamer._;

/**
 * A collection of constants of the game
 * 
 * @author hezzze
 * 
 */
public class Go {

	public static final int ROWS = 19;
	public static final int COLS = 19;
	public static final int MAX_ROW_INDEX = ROWS - 1;
	public static final int MAX_COL_INDEX = COLS - 1;

	public static final Gamer[][] INIT_BOARD = {
			// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
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
			{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ } // 18
	};

	static boolean inBoard(Position pos) {
		return pos.row <= MAX_ROW_INDEX && pos.row >= 0
				&& pos.col <= MAX_COL_INDEX && pos.col >= 0;
	}

	static boolean atBoardFringe(Position pos) {
		int row = pos.row;
		int col = pos.col;
		return (row == -1 && col >= 0 && col <= MAX_COL_INDEX)
				|| (row == 19 && col >= 0 && col <= MAX_COL_INDEX)
				|| (col == -1 && row >= 0 && row <= MAX_ROW_INDEX)
				|| (col == 19 && row >= 0 && row <= MAX_ROW_INDEX);
	}

	public static boolean isPositionWithBlackDot(int row, int col) {
		return (row == 3 || row == 9 || row == 15)
				&& (col == 3 || col == 9 || col == 15);
	}
	
	public static String MSG_HEADER = "!>?<!";
	public static String GAME_ID_FILLER = "-vs-";

}
