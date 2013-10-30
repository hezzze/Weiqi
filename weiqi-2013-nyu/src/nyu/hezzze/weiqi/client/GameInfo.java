package nyu.hezzze.weiqi.client;

import java.io.Serializable;
import java.util.Date;

import nyu.hezzze.weiqi.shared.Gamer;

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
	private String winnerName;
	private boolean isMyTurn;
	private Gamer iAmPlaying;
	private Date startDate;

	public GameInfo() {

	}

	public GameInfo(String id, String winnerName, boolean whoseTurn,
			Gamer iAmPlaying, Date startDate) {
		this.id = id;
		this.winnerName = winnerName;
		this.isMyTurn= whoseTurn;
		this.iAmPlaying = iAmPlaying;
		this.startDate = startDate;
	}

	public String getId() {
		return id;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public boolean isMyTurn() {
		return isMyTurn;
	}

	public Gamer getIAmPlaying() {
		return iAmPlaying;
	}

	public Date getStartDate() {
		return startDate;
	}

}
