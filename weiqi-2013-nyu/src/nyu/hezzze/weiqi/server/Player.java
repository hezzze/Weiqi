package nyu.hezzze.weiqi.server;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * The entity class containing useful information of a player
 * 
 * @author hezzze
 * 
 */
@Entity
public class Player {

	@Id
	String email;
	String name;
	Set<String> gameIds = new HashSet<String>();

	@SuppressWarnings("unused")
	private Player() {
	}

	Player(String email) {
		this.email = email;
		this.name = email.split("@")[0];
	}

	String getEmail() {
		return email;
	}

	public Set<String> getGameIds() {
		return gameIds;
	}

	void addGameId(String newGameId) {
		gameIds.add(newGameId);
	}
}
