package nyu.hezzze.weiqi.shared;

import static com.google.common.base.Preconditions.checkNotNull;
import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;
import static nyu.hezzze.weiqi.shared.Gamer.EMPTY;
import static nyu.hezzze.weiqi.shared.GoBoard.*;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class is used to decide who's the winner when the Go game ends It's
 * based on the area scoring rule. <br>
 * http://en.wikipedia.org/wiki/Rules_of_Go#Area
 * 
 * @author hezzze
 * 
 */
public class Judge {

	final Gamer[][] board;

	/**
	 * A parallel 2D array of stones corresponding to the board
	 */
	final Stone[][] stones;

	/**
	 * Contains all the connected stone groups, be it a empty group or a groups
	 * owned by either player
	 */
	final HashSet<Group> groups;
	final Gamer whoseTurn;

	private int pointsOfBlack;
	private int pointsOfWhite;
	/**
	 * Rules of Go http://en.wikipedia.org/wiki/Rules_of_Go#Area <br>
	 * 
	 * This is the other major set of rules in widespread use, also known as
	 * "area" rules. At the end, one player (usually Black) fills in all of
	 * his/her captured territory, and the other (White) stones are removed from
	 * the board. Prisoners do not count. Black stones are then arranged in
	 * groups of ten—eighteen such groups, plus half the komi, plus at least one
	 * additional stone = victory for Black. So for example with a komidashi of
	 * 7.5 points, under Chinese rules Black needs at least 185 stones on the
	 * board at the end to win. Komidashi is usually 7.5 points.
	 */
	public static final int POINTS_TO_WIN_FOR_BLACK = 185;

	/**
	 * The only constructor of Judge object, it takes a Go State and do some
	 * pre-processing to it for deciding the winner
	 * 
	 * @param state
	 *            the final state of the game
	 */
	public Judge(State state) {
		board = state.getBoard();
		whoseTurn = state.whoseTurn();
		stones = new Stone[ROWS][COLS];
		groups = new HashSet<Group>();
		findConnectedGroups();

		pointsOfBlack = 0;
		pointsOfWhite = 0;
	}

	/**
	 * Based on the board, create the Stone objects and forming connected groups
	 * for validating possible moves from the player
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
	 * Try to place a stone at the position specified <br>
	 * A player may not place a stone such that it or its group immediately has
	 * no liberties, unless doing so immediately deprives an enemy group of its
	 * final liberty. <br>
	 * http://en.wikipedia.org/wiki/Go_(board_game)#
	 * Playing_stones_with_no_liberties
	 * 
	 * @param pos
	 *            The position to place a stone
	 * @return true if the move is valid
	 */
	public boolean validMove(Position pos) {

		if (getGamer(pos) != EMPTY) {
			return false;
		}

		setGamer(whoseTurn, pos);
		setStone(whoseTurn, pos);

		return libertyAvailableAt(pos.north())
				|| libertyAvailableAt(pos.south())
				|| libertyAvailableAt(pos.west())
				|| libertyAvailableAt(pos.east());
	}

	private boolean libertyAvailableAt(Position pos) {

		if (!inBoard(pos)) {
			return false;
		} else if (isEmpty(pos)) {
			return true;
		} else {
			Stone stone = getStone(pos);

			return (stone.gamer == whoseTurn && groupHasLiberty(board,
					stone.group))
					|| (stone.gamer == whoseTurn.getOpponent() && !groupHasLiberty(
							board, stone.group));
		}
	}

	/**
	 * At the end of each turn, remove the group without liberty, which is
	 * considered dead according to the rule of Go
	 */
	Gamer[][] removeGroupsWithoutLiberty() {
		ArrayList<Group> groupsToBeRemoved = new ArrayList<Group>();
		ArrayList<Position> stonesToBeRemoved = new ArrayList<Position>();

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

		return board;

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
		return isEmpty(pos.north()) || isEmpty(pos.south())
				|| isEmpty(pos.west()) || isEmpty(pos.east());
	}

	private boolean isEmpty(Position pos) {
		return inBoard(pos) && getGamer(pos) == EMPTY;
	}

	private Stone getStone(Position pos) {
		return stones[pos.row][pos.col];
	}

	private void setGamer(Gamer gamer, Position pos) {
		board[pos.row][pos.col] = gamer;
	}

	private Gamer getGamer(Position pos) {
		return board[pos.row][pos.col];
	}

	public void finalizeGame() {
		removeDeadGroups();
		countingAreaOfBlack();
	}

	/**
	 * First determine life or death for the non-single group, then make extra
	 * considerations about the dead non-single group in the first round and the
	 * singles.
	 */
	private void removeDeadGroups() {
		ArrayList<Group> groupsToBeRemoved = new ArrayList<>();
		ArrayList<Position> stonesToBeRemoved = new ArrayList<>();

		determineLifeOrDeathForNonSingleGroups();
		furtherDetermineLifeOrDeathForGroups();

		for (Group group : groups) {
			if (group.gamer != EMPTY && !group.isAlive) {
				for (Stone stone : group.members) {
					stonesToBeRemoved.add(stone.pos);
				}
				groupsToBeRemoved.add(group);
			}
		}

		for (Position pos : stonesToBeRemoved) {

			setGamer(EMPTY, pos);
			setStone(EMPTY, pos);
		}

		groups.removeAll(groupsToBeRemoved);

	}

	/**
	 * A group is alive in the first round under the following rule: <br>
	 * There are at least two members of the group that can form an eye of their
	 * own
	 */
	private void determineLifeOrDeathForNonSingleGroups() {
		for (Group group : groups) {
			if (group.gamer != EMPTY && group.members.size() > 1) {
				if (isNonSingleAlive(group)) {
					group.isAlive = true;
				}
			}
		}
	}

	private boolean isNonSingleAlive(Group group) {
		Gamer gamer = checkNotNull(group.gamer);
		int numOfPossibleEyes = 0;
		for (Stone stone : group.members) {
			if (canMakeEye(gamer, stone)) {
				++numOfPossibleEyes;
				if (numOfPossibleEyes > 1)
					return true;
			}
		}
		return false;

	}

	private boolean canMakeEye(Gamer gamer, Stone stone) {
		Position pos = stone.pos;
		return canMakeEye(gamer, pos, "north")
				|| canMakeEye(gamer, pos, "south")
				|| canMakeEye(gamer, pos, "west")
				|| canMakeEye(gamer, pos, "east");
	}

	private boolean canMakeEye(Gamer gamer, Position pos, String direction) {
		switch (direction) {
		case "north":
			return canBeEyePart(gamer, pos.northwest())
					&& canBeEyePart(gamer, pos.northeast())
					&& canBeEyePart(gamer, pos.north().north());
		case "south":
			return canBeEyePart(gamer, pos.southwest())
					&& canBeEyePart(gamer, pos.southeast())
					&& canBeEyePart(gamer, pos.south().south());
		case "west":
			return canBeEyePart(gamer, pos.northwest())
					&& canBeEyePart(gamer, pos.southwest())
					&& canBeEyePart(gamer, pos.west().west());
		case "east":
			return canBeEyePart(gamer, pos.northeast())
					&& canBeEyePart(gamer, pos.southeast())
					&& canBeEyePart(gamer, pos.east().east());
		default:
			return false;
		}
	}

	private boolean canBeEyePart(Gamer gamer, Position pos) {
		if (inBoard(pos)) {
			return getGamer(pos) == EMPTY || getGamer(pos) == gamer;
		} else {
			return atBoardFringe(pos);
		}
	}

	/**
	 * A group is alive for the second round under the following rule: It has a
	 * chance two connect to a group that is determined alive at the first round
	 */
	private void furtherDetermineLifeOrDeathForGroups() {
		for (Group group : groups) {
			if (group.gamer != EMPTY && !group.isAlive) {
				if (groupHasHope(group)) {
					group.isAlive = true;
				}
			}
		}

	}

	private boolean groupHasHope(Group group) {
		Gamer gamer = group.gamer;
		for (Stone stone : group.members) {
			Position pos = stone.pos;
			if (hasHopeAt(gamer, pos.north()) || hasHopeAt(gamer, pos.south())
					|| hasHopeAt(gamer, pos.west())
					|| hasHopeAt(gamer, pos.east())) {
				return true;
			}
		}
		return false;
	}

	private boolean hasHopeAt(Gamer gamer, Position pos) {
		if (inBoard(pos) && getGamer(pos) == EMPTY) {
			boolean hopeAtNorth = false, hopeAtSouth = false, hopeAtWest = false, hopeAtEast = false;
			if (inBoard(pos.north())) {
				Stone northStone = getStone(pos.north());
				hopeAtNorth = northStone.gamer == gamer
						&& northStone.group.isAlive;

			}
			if (inBoard(pos.south())) {
				Stone southStone = getStone(pos.south());
				hopeAtSouth = southStone.gamer == gamer
						&& southStone.group.isAlive;

			}
			if (inBoard(pos.west())) {
				Stone westStone = getStone(pos.west());
				hopeAtWest = westStone.gamer == gamer
						&& westStone.group.isAlive;
			}
			if (inBoard(pos.east())) {
				Stone eastStone = getStone(pos.east());
				hopeAtEast = eastStone.gamer == gamer
						&& eastStone.group.isAlive;
			}
			return hopeAtNorth || hopeAtSouth || hopeAtWest || hopeAtEast;
		}
		return false;
	}

	private void countingAreaOfBlack() {

		for (Group group : groups) {
			if (group.gamer == EMPTY) {
				if (territoryBelongsTo(group) == BLACK) {
					pointsOfBlack += group.members.size();
				} else if (territoryBelongsTo(group) == WHITE) {
					pointsOfWhite += group.members.size();
				}
			} else if (group.gamer == BLACK) {
				pointsOfBlack += group.members.size();
			} else if (group.gamer == WHITE) {
				pointsOfWhite += group.members.size();
			}
		}

	}

	private Gamer territoryBelongsTo(Group group) {
		HashSet<Position> perimeterPositions = group
				.getGroupPerimeterPositions();
		boolean isBlack = true;
		boolean isWhite = true;
		for (Position pos : perimeterPositions) {
			if (inBoard(pos) && getGamer(pos) == WHITE) {
				isBlack = false;
			}
			if (inBoard(pos) && getGamer(pos) == BLACK) {
				isWhite = false;
			}
		}
		if (isBlack) {
			return BLACK;
		} else if (isWhite) {
			return WHITE;
		} else {
			return null;
		}
	}

	boolean isBlackWin() {
		return pointsOfBlack >= POINTS_TO_WIN_FOR_BLACK;
	}

	boolean isWhiteWin() {
		return !isBlackWin();
	}

	int getPointsOfBlack() {
		return pointsOfBlack;
	}

	int getPointsOfWhite() {
		return pointsOfWhite;
	}

}
