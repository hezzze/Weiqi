package nyu.hezzze.weiqi.shared;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;
import static nyu.hezzze.weiqi.shared.GameResult.WHITE_WIN;
import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.EMPTY;
import static nyu.hezzze.weiqi.shared.GoBoard.COLS;
import static nyu.hezzze.weiqi.shared.GoBoard.INIT_BOARD;
import static nyu.hezzze.weiqi.shared.GoBoard.ROWS;
import static nyu.hezzze.weiqi.shared.GoBoard.inBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.google.common.base.Objects;

/**
 * Representing a particular moment or 
 * state during a Go game
 * 
 * @author hezzze
 *
 */
public class State {

	/**
	 * Contains all the connected stone
	 * groups, be it a empty group or
	 * a groups owned by either player
	 */
	final HashSet<Group> groups;
	
	private final Gamer[][] board;
	
	/**
	 * A parallel 2D array of stones
	 * corresponding to the board
	 */
	private final Stone[][] stones;
	private final Gamer whoseTurn;
	private GameOver gameOver;
	
	/**
	 * Indicates if the a player has passed
	 * last turn. In a Go game, when both player 
	 * pass consecutively, the game ends. <br>
	 * http://en.wikipedia.org/wiki/Go_(board_game)#Passing
	 */
	private boolean passedLastTurn;
	
	public State() {
		board = INIT_BOARD;
		whoseTurn = BLACK;
		stones = new Stone[ROWS][COLS];
		groups = new HashSet<Group>();
		passedLastTurn = false;
		gameOver = null;
		findConnectedGroups();
	}

	public State(Gamer[][] board, Gamer whoseTurn, boolean passedLastTurn, GameOver gameOver) {
		this.board = board;
		this.whoseTurn = whoseTurn;
		this.passedLastTurn = passedLastTurn;
		this.gameOver = gameOver;
		stones = new Stone[ROWS][COLS];
		groups = new HashSet<Group>();
		findConnectedGroups();
	}

	/**
	 * Based on the board, create the 
	 * Stone objects and forming connected 
	 * groups for validating possible moves
	 * from the player
	 */
	private void findConnectedGroups() {

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				setStone(board[i][j], new Position(i, j));
			}
		}
	}

	void setStone(Gamer gamer, Position pos) {
		Stone newStone = new Stone(gamer, pos);
		stones[pos.row][pos.col] = newStone;
		groups.add(newStone.group);

		connect(pos, pos.north());
		connect(pos, pos.south());
		connect(pos, pos.west());
		connect(pos, pos.east());

	}

	private void connect(Position pos1, Position pos2) {
		if (!(inBoard(pos1) && inBoard(pos2))) {
			return;
		}
		Stone stone1 = getStone(pos1);
		Stone stone2 = getStone(pos2);
		if (stone1 == null || stone2 == null || stone1.gamer != stone2.gamer) {
			return;
		}
		if (stone1.group != stone2.group) {
			groups.remove(stone1.group);
			groups.remove(stone2.group);
			Group newGroup = stone1.group.union(stone2.group);
			groups.add(newGroup);
		}

	}

	/**
	 * Representing a move from a player.
	 * The method will not mutate the receiver
	 * @param pos The position where the player puts 
	 * the next stone
	 * @return The next state of the game
	 * @throws IllegalMove an exception is thrown
	 * when the move is invalid
	 * @throws GameOverException an exception is thrown 
	 * when game is over
	 */
	public State makeMove(Position pos) throws IllegalMove,GameOverException {
		if (gameOver!= null) {
			throw new GameOverException("Game is over!!!");
		}
		
		Gamer[][] newBoard = board.clone();
		if (!inBoard(pos)) {
			throw new IllegalMove("Out of bounds");
		}

		newBoard[pos.row][pos.col] = whoseTurn;
		if (!goodPos(pos)) {
			throw new IllegalMove("No liberty!");
		}
		setStone(whoseTurn, pos);
		removeGroupsWithoutLiberty();

		return new State(newBoard, whoseTurn.getOpponent(), false, gameOver);
	}
	
	/**
	 * Determine if the position can be placed a stone according to the Go rule. <br>
	 * A player may not place a stone such that it or its group immediately has
	 * no liberties, unless doing so immediately deprives an enemy group of its
	 * final liberty. <br>
	 * http://en.wikipedia.org/wiki/Go_(board_game)#Playing_stones_with_no_liberties
	 * 
	 * @param pos The position to place a stone
	 * @return true if it's a valid position to put the next stone
	 */
	private boolean goodPos(Position pos) {
		Gamer[][] wouldBeBoard = board.clone();
		wouldBeBoard[pos.row][pos.col] = whoseTurn;
		return libertyAvailableAt(wouldBeBoard, pos.north())
				|| libertyAvailableAt(wouldBeBoard, pos.south())
				|| libertyAvailableAt(wouldBeBoard, pos.west())
				|| libertyAvailableAt(wouldBeBoard, pos.east());
	}

	private boolean libertyAvailableAt(Gamer[][] board, Position pos) {

		if (!inBoard(pos)) {
			return false;
		} else if (isEmpty(board, pos)) {
			return true;
		} else {
			Stone stone = getStone(pos);
			return (stone.gamer == whoseTurn && groupHasLiberty(board, stone.group))
					|| (stone.gamer == whoseTurn.getOpponent() && !groupHasLiberty(
							board, stone.group));
		}
	}

	/**
	 * At the end of each turn, remove
	 * the group without liberty, which is considered dead according to
	 * the rule of Go
	 */
	private void removeGroupsWithoutLiberty() {
		ArrayList<Group> groupsToBeRemoved = new ArrayList<>();
		ArrayList<Position> stonesToBeRemoved = new ArrayList<>();

		for (Group group : groups) {
			if (group.gamer != EMPTY && !groupHasLiberty(board, group)
					&& group.gamer == whoseTurn.getOpponent()) {
				for (Stone stone : group.members) {
					stonesToBeRemoved.add(stone.pos);
				}
				groupsToBeRemoved.add(group);
			}
		}

		for (Position pos : stonesToBeRemoved) {
			setGamer(EMPTY, pos);
			// stones[pos.row][pos.col] = null;
		}

		groups.removeAll(groupsToBeRemoved);

	}

	private boolean groupHasLiberty(Gamer[][] board, Group group) {
		for (Stone stone : group.members) {
			if (stoneOwnLiberty(board, stone.pos)) {
				return true;
			}
		}
		return false;
	}

	private boolean stoneOwnLiberty(Gamer[][] board, Position pos) {
		return isEmpty(board, pos.north()) || isEmpty(board, pos.south())
				|| isEmpty(board, pos.west()) || isEmpty(board, pos.east());
	}

	private boolean isEmpty(Gamer[][] board, Position pos) {
		return inBoard(pos) && getGamer(pos) == EMPTY;
	}
	
	/**
	 * In each turn of a Go game, a player may choose 
	 * to pass if no meaningful moves is present, the game 
	 * ends when players pass consecutively
	 * @return The next state of the game
	 * @throws GameOverException Throws an exception if the game
	 * is over
	 */
	public State pass() throws GameOverException {
		if (gameOver != null) {
			throw new GameOverException("Game is over!!!");
		}

		if (passedLastTurn) {
			Judge judge = new Judge(this);
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
		return board.clone();
	}

	public Gamer whoseTurn() {
		return this.whoseTurn;
	}

	public GameOver getGameOver() {
		return gameOver;
	}

	public HashSet<Group> getGroups() {
		return new HashSet<Group>(groups);
	}
	
	Stone getStone(Position pos) {
		return stones[pos.row][pos.col];
	}

	void setGamer(Gamer gamer, Position pos) {
		board[pos.row][pos.col] = gamer;
	}

	Gamer getGamer(Position pos) {
		return board[pos.row][pos.col];
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

	// Temporary methods for testing
	public HashSet<Group> getEmptyGroups() {
		HashSet<Group> emptyGroups = new HashSet<>();
		for (Group group : groups) {
			if (group.gamer == EMPTY) {
				emptyGroups.add(group);
			}
		}
		return emptyGroups;
	}

}
