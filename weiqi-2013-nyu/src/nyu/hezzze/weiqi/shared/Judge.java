package nyu.hezzze.weiqi.shared;

import static com.google.common.base.Preconditions.checkNotNull;
import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;
import static nyu.hezzze.weiqi.shared.Gamer.EMPTY;
import static nyu.hezzze.weiqi.shared.GoBoard.*;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class is used to decide 
 * who's the winner when the Go game ends
 * It's based on the area scoring rule. <br>
 * http://en.wikipedia.org/wiki/Rules_of_Go#Area
 * 
 * @author hezzze
 *
 */
public class Judge {
	
	private final State state;
	private int pointsOfBlack;
	private int pointsOfWhite;
	/**
	 * Rules of Go
	 * http://en.wikipedia.org/wiki/Rules_of_Go#Area <br>
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
	 * The only constructor of Judge
	 * object, it takes a State Object
	 * and do some pre-processing to it
	 * for deciding the winner
	 * @param state the final state of the game
	 */
	public Judge (State state) {
		this.state = new State(state.getBoard(), state.whoseTurn(), true, null);
		pointsOfBlack = 0;
		finalizeGame();
	}
	
	public void finalizeGame() {
		removeDeadGroups();
		countingAreaOfBlack();
	}

	/**
	 * First determine life or death for the 
	 * non-single group, then make extra 
	 * considerations about the dead non-single group
	 * in the first round and the singles.
	 */
	private void removeDeadGroups() {
		ArrayList<Group> groupsToBeRemoved = new ArrayList<>();
		ArrayList<Position> stonesToBeRemoved = new ArrayList<>();

		determineLifeOrDeathForNonSingleGroups();
		furtherDetermineLifeOrDeathForGroups();

		for (Group group : state.groups) {
			if (group.gamer != EMPTY && !group.isAlive) {
				for (Stone stone : group.members) {
					stonesToBeRemoved.add(stone.pos);
				}
				groupsToBeRemoved.add(group);
			}
		}

		for (Position pos : stonesToBeRemoved) {
			
			state.setGamer(EMPTY, pos);
			state.setStone(EMPTY, pos);
		}

		state.groups.removeAll(groupsToBeRemoved);

	}
	
	/**
	 * A group is alive in the first round under the following rule: <br>
	 * There are at least two members of the group that
	 * can form an eye of their own
	 */
	private void determineLifeOrDeathForNonSingleGroups() {
		for (Group group : state.groups) {
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
			return state.getGamer(pos) == EMPTY || state.getGamer(pos) == gamer;
		} else {
			return atBoardFringe(pos);
		}
	}

	/**
	 * A group is alive for the second round under the following rule:
	 * It has a chance two connect to a group that is determined
	 * alive at the first round
	 */
	private void furtherDetermineLifeOrDeathForGroups() {
		for (Group group : state.groups) {
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
				|| hasHopeAt(gamer, pos.west()) || hasHopeAt(gamer, pos.east())) {
				return true;
			}
		}
		return false;
	}

	private boolean hasHopeAt(Gamer gamer, Position pos) {
		if (inBoard(pos) && state.getGamer(pos) == EMPTY) {
			boolean hopeAtNorth = false,
					hopeAtSouth = false,
					hopeAtWest = false,
					hopeAtEast = false;
			if (inBoard(pos.north())) {
				Stone northStone = state.getStone(pos.north());
				hopeAtNorth = northStone.gamer == gamer && northStone.group.isAlive;
			
			}
			if (inBoard(pos.south())) {
				Stone southStone = state.getStone(pos.south());
				hopeAtSouth = southStone.gamer == gamer && southStone.group.isAlive;
				
			}
			if (inBoard(pos.west())) {
				Stone westStone = state.getStone(pos.west());
				hopeAtWest = westStone.gamer == gamer && westStone.group.isAlive;
			}
			if (inBoard(pos.east())) {
				Stone eastStone = state.getStone(pos.east());
				hopeAtEast = eastStone.gamer == gamer && eastStone.group.isAlive;
			}
			return hopeAtNorth||hopeAtSouth||hopeAtWest||hopeAtEast;
		}
		return false;
	}

	
	
	private void countingAreaOfBlack() {
		
		for (Group group : state.groups) {
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
		HashSet<Position> perimeterPositions = group.getGroupPerimeterPositions();
		boolean isBlack = true;
		boolean isWhite = true;
		for (Position pos : perimeterPositions) {
			if (inBoard(pos) && state.getGamer(pos) == WHITE) {
				isBlack = false;
			}
			if (inBoard(pos) && state.getGamer(pos) == BLACK) {
				isWhite = false;
			}
		}
		if (isBlack) {
			return BLACK;
		} else if(isWhite) {
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

	Gamer[][] getFinalBoard() {
		return state.getBoard();
	}
}
