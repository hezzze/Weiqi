package nyu.hezzze.weiqi.server;

import java.util.LinkedList;

import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.Go;
import nyu.hezzze.weiqi.shared.State;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * The entity class containing the inforamtion of a game
 * 
 * @author hezzze
 * 
 */
@Entity
public class Game {

	public static final int BLACK_PLAYER = 0;
	public static final int WHITE_PLAYER = 1;

	@Id
	String id;
	String stateStr;
	String winner;
	String[] playerEmails = new String[2];
	LinkedList<String> connectionIds = new LinkedList<String>();
	

	@SuppressWarnings("unused")
	private Game() {
	}

	Game(String email1, String email2, String stateStr) {
		id = email1.split("@")[0] + Go.GAME_ID_FILLER + email2.split("@")[0];
		playerEmails[0] = email1;
		playerEmails[1] = email2;
		this.stateStr = stateStr;
	}

	String getGameId() {
		return id;
	}

	String getPlayerEmail(int color) {
		return playerEmails[color];
	}

	String getWhoseTurnEmail() {
		String whoseTurnEmail;
		State state = State.deserialize(stateStr);
		if (state.whoseTurn() == Gamer.BLACK) {
			whoseTurnEmail = getPlayerEmail(BLACK_PLAYER);
		} else {
			whoseTurnEmail = getPlayerEmail(WHITE_PLAYER);
		}
		return whoseTurnEmail;
	}

	String getState() {
		return stateStr;
	}

	public String[] getPlayerEmails() {
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

}
