package nyu.hezzze.weiqi.client;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;

/**
 * This class is for the client to get relevant status updates from the server
 * about a particular on-going game
 * 
 * @author hezzze
 * 
 */
public class GameInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final ProvidesKey<GameInfo> KEY_PROVIDER = new ProvidesKey<GameInfo>() {

		@Override
		public Object getKey(GameInfo item) {
			return item == null ? null : item.getId();
		};

	};

	private String id;
	private String winner;
	private String whoseTurn;
	private String iAmPlaying;

	public GameInfo() {

	}

	public GameInfo(String id, String winner, String whoseTurn,
			String iAmPlaying) {
		this.id = id;
		this.winner = winner;
		this.whoseTurn = whoseTurn;
		this.iAmPlaying = iAmPlaying;
	}

	public String getId() {
		return id;
	}

	public String getWinner() {
		return winner;
	}

	public String getWhoseTurn() {
		return whoseTurn;
	}

	public String getIAmPlaying() {
		return iAmPlaying;
	}

}
