package nyu.hezzze.weiqi.server;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import nyu.hezzze.weiqi.shared.GameOver;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.Go;
import nyu.hezzze.weiqi.shared.State;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

/**
 * The entity class containing the inforamtion of a game
 * 
 * @author hezzze
 * 
 */
@Entity
public class Game {

	@Id
	String id;
	String stateStr;
	@Serialize
	Map<Gamer, String> playerEmails = new HashMap<Gamer, String>();
	LinkedList<String> connectionIds = new LinkedList<String>();
	Date startDate;
	boolean isSingleGame;

	@SuppressWarnings("unused")
	private Game() {
	}

	Game(String email1, String email2, String stateStr) {
		id = Player.emailToName(email1) + Go.GAME_ID_FILLER
				+ Player.emailToName(email2);
		playerEmails.put(Gamer.BLACK, email1);
		playerEmails.put(Gamer.WHITE, email2);
		this.stateStr = stateStr;
		startDate = new Date();
		isSingleGame = false;
	}
	
	String getGameId() {
		return id;
	}

	String getPlayerEmail(Gamer gamer) {
		return playerEmails.get(gamer);
	}

	String getWhoseTurnEmail() {
		State state = State.deserialize(stateStr);
		return getPlayerEmail(state.whoseTurn());
	}

	String getState() {
		return stateStr;
	}

	Date getStartDate() {
		return startDate;
	}

	public Map<Gamer, String> getPlayerEmails() {
		return playerEmails;
	}

	public void setState(String stateStr) {
		this.stateStr = stateStr;
	}

	LinkedList<String> getConnectionIds() {
		return connectionIds;
	}

	void removeConnectionId(String connectionId) {
		connectionIds.remove(connectionId);
	}

	void addConnectionId(String connectionId) {
		connectionIds.add(connectionId);
	}

	public GameOver getGameOver() {
		State state = State.deserialize(stateStr);
		return state.getGameOver();
	}

	private Gamer getWinner() {
		GameOver gameOver = getGameOver();
		return gameOver == null ? null
				: (gameOver.getGameResult() == BLACK_WIN ? Gamer.BLACK
						: Gamer.WHITE);

	}

	public String getWinnerEmail() {
		Gamer gamer = getWinner();
		return gamer == null ? null : playerEmails.get(getWinner());
	}

}
