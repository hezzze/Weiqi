package nyu.hezzze.weiqi.shared;

import static com.google.common.base.Preconditions.checkArgument;


import java.util.ArrayList;
import java.util.HashSet;

import com.google.common.base.Objects;

/**
 * This class represent a group of stones
 * in a Go game.
 * 
 * @author hezzze
 *
 */
public class Group {
	ArrayList<Stone> members;
	
	/**
	 * The owner of a group
	 */
	Gamer gamer;
	
	/**
	 * This variable will be set by a Judge
	 * Object during the scoring period
	 * to indicate if the group is alive or
	 * dead
	 */
	boolean isAlive;

	public Group(Gamer gamer) {
		this.gamer = gamer;
		members = new ArrayList<Stone>();
		isAlive = false;
	}

	public Group(Group group) {
		members = new ArrayList<Stone>(group.members);
		for (Stone stone : members) {
			stone.group = this;
		}
		gamer = group.gamer;
		isAlive = false;
	}

	/**
	 * This methods combines two stone groups 
	 * whenever they are connected on the board.
	 * It changes set  all the group members to
	 * be a member of the newly formed group 
	 * 
	 * @param other another group to be unioned together
	 * @return the union of the two group
	 */
	public Group union(Group other) {
		checkArgument(this.gamer == other.gamer);
		Group bigger = this;
		Group smaller = other;
		if (other.members.size() > members.size()) {
			bigger = other;
			smaller = this;
		}
		for (Stone stone : smaller.members) {
			stone.group = bigger;
		}
		bigger.members.addAll(smaller.members);
		return new Group(bigger);

	}

	/**
	 * This methods will return the perimeter
	 * positions of a group, it's useful determine
	 * the owner of territories during the scoring
	 * phase
	 * 
	 * @return a hashset of perimeter positions
	 */
	public HashSet<Position> getGroupPerimeterPositions () {
		ArrayList<Position> memberPositions = new ArrayList<> ();
		HashSet<Position> periPositions = new HashSet<> ();
		
		for (Stone stone : members) {
			memberPositions.add(stone.pos);
		}
		
		for (Position pos : memberPositions) {
			if (!memberPositions.contains(pos.north())) {
				periPositions.add(pos.north());
			}
			if (!memberPositions.contains(pos.south())) {
				periPositions.add(pos.south());
			}
			if (!memberPositions.contains(pos.east())) {
				periPositions.add(pos.east());
			}
			if (!memberPositions.contains(pos.west())) {
				periPositions.add(pos.west());
			}
		}
		
		return periPositions;
		
			
	}
	

	
	@Override
	public String toString() {
		String str = "";
		for (Stone stone : members) {
			str += ("(" + stone.pos.row + " " + stone.pos.col + ")");
		}
		return str;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode( members, gamer);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Group)) {
			return false;
		}
		Group other = (Group) obj;
		return Objects.equal(gamer, other.gamer)
				&& Objects.equal(members, other.members)
				&& isAlive == other.isAlive;
	}

}
