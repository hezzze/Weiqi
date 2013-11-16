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

	@Id
	String fbId;
	Set<String> gameIds = new HashSet<String>();
	Date dateOfLastGame;
	private Rank rank;

	@SuppressWarnings("unused")
	private Player() {
	}

	Player(String fbId) {
		this.fbId = fbId;
		rank = new Rank();
	}

	String getFbId() {
		return fbId;
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

	void updateRankRating(Player opponent, String winnerUserName) {
		rank.updateAfterGame(opponent.getRank().getRD(), opponent.getRank()
				.getRating(), winnerUserName.equals(fbId) ? 1 : 0);
	}

	void setDateOfLastGame(Date dateOfLastGame) {
		this.dateOfLastGame = dateOfLastGame;
	}

}
