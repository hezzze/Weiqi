package nyu.hezzze.weiqi.shared;

import com.google.common.base.Objects;

/**
 * The class representing a stone
 * in a Go game
 * @author hezzze
 *
 */
public class Stone {

	/**
	 * The group the stone
	 * belongs to
	 */
	Group group;
	
	/**
	 * The owner or commander or 
	 * what have you of the stone
	 */
	Gamer gamer;
	
	/**
	 * The position of the stone
	 */
	Position pos;
	
	public Stone(Gamer gamer, Position pos) {
		this.gamer = gamer;
		this.group = new Group(gamer);
		group.members.add(this);
		this.pos = pos;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Stone)) {
			return false;
		}
		Stone other = (Stone) obj;
		return group == other.group &&
				Objects.equal(gamer, other.gamer) &&
				Objects.equal(pos, other.pos);
				
	}

}
