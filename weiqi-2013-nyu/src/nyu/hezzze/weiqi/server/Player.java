package nyu.hezzze.weiqi.server;

import java.util.Date;
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

	public static String emailToName(String email) {
		return email.split("@")[0];
	}

	@Id
	String email;
	String name;
	Set<String> gameIds = new HashSet<String>();
	Date dateOfLastGame;
	private Rank rank;

	@SuppressWarnings("unused")
	private Player() {
	}

	Player(String email) {
		this.email = email;
		this.name = emailToName(email);
		rank = new Rank();
	}

	String getEmail() {
		return email;
	}

	Rank getRank() {
		return rank;
	}

	public Set<String> getGameIds() {
		return gameIds;
	}

	void addGameId(String newGameId) {
		gameIds.add(newGameId);
	}

	void updateRankRD() {
		if (dateOfLastGame != null) {
			rank.updateRD(dateOfLastGame);
		}
	}

	void updateRankRating(Player opponent, String winnerEmail) {
		rank.updateAfterGame(opponent.getRank().getRD(), opponent.getRank()
				.getRating(), winnerEmail.equals(email) ? 1 : 0);
	}

	void setDateOfLastGame(Date dateOfLastGame) {
		this.dateOfLastGame = dateOfLastGame;
	}

}
