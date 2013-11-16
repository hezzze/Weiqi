package nyu.hezzze.weiqi.server;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	Map<Gamer, String> playerFbIds = new HashMap<Gamer, String>();
	Set<String> connectionIds = new HashSet<String>();
	Date startDate;
	boolean isSingleGame;

	@SuppressWarnings("unused")
	private Game() {
	}

	Game(String id1, String id2, String stateStr) {

		id = Integer.parseInt(id1) < Integer.parseInt(id2) ? id1 + Go.GAME_ID_FILLER + id2 : id2
				+ Go.GAME_ID_FILLER + id1;
		playerFbIds.put(Gamer.BLACK, id1);
		playerFbIds.put(Gamer.WHITE, id2);
		this.stateStr = stateStr;
		startDate = new Date();
		isSingleGame = false;
	}

	String getGameId() {
		return id;
	}

	String getPlayerFbId(Gamer gamer) {
		return playerFbIds.get(gamer);
	}

	String getWhoseTurnFbId() {
		State state = State.deserialize(stateStr);
		return getPlayerFbId(state.whoseTurn());
	}

	String getStateStr() {
		return stateStr;
	}

	Date getStartDate() {
		return startDate;
	}

	public void setState(String stateStr) {
		this.stateStr = stateStr;
	}

	Set<String> getConnectionIds() {
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

	public String getWinnerFbId() {
		Gamer gamer = getWinner();
		return gamer == null ? null : playerFbIds.get(getWinner());
	}

	public String getOpponentId(String id) {
		String whiteId = getPlayerFbId(Gamer.WHITE);
		if (whiteId.equals(id)) {
			return getPlayerFbId(Gamer.WHITE.getOpponent());
		} else {
			return whiteId;
		}
	}

}
