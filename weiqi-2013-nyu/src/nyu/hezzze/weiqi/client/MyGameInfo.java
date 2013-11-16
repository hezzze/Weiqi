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
public class MyGameInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final ProvidesKey<MyGameInfo> KEY_PROVIDER = new ProvidesKey<MyGameInfo>() {

		@Override
		public Object getKey(MyGameInfo item) {
			return item == null ? null : item.getId();
		};

	};

	private String gameId;
	private String winnerName;
	private boolean isMyTurn;
	private Gamer iAmPlaying;
	private Date startDate;
	private String opponentId;

	public MyGameInfo() {

	}

	public MyGameInfo(String gameId, String opponentId, String winnerName, boolean whoseTurn,
			Gamer iAmPlaying, Date startDate) {
		this.gameId = gameId;
		this.opponentId = opponentId;
		this.winnerName = winnerName;
		this.isMyTurn= whoseTurn;
		this.iAmPlaying = iAmPlaying;
		this.startDate = startDate;
	}

	public String getId() {
		return gameId;
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

	/**
	 * @return the opponentId
	 */
	public String getOpponentId() {
		return opponentId;
	}
	
}
