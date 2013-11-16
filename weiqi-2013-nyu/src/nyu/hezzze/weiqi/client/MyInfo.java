package nyu.hezzze.weiqi.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<MyGameInfo> gameInfos;
	private String rankStr;

	public List<MyGameInfo> getGameInfos() {
		return gameInfos;
	}
	
	public String getGameIdWith(String opponentId) {
		for (MyGameInfo gameInfo : gameInfos) {
			if (gameInfo.getOpponentId().equals(opponentId)) {
				return gameInfo.getId();
			}
		}
		return null;
	}

	public List<String> getOpponentIds() {
		ArrayList<String> ids = new ArrayList<String>();
		for (MyGameInfo info : gameInfos) {
			ids.add(info.getOpponentId());
		}
		return ids;
	}

	public String getRankStr() {
		return rankStr;
	}

	public void setRankStr(String rankStr) {
		this.rankStr = rankStr;
	}

	public void setGameInfos(List<MyGameInfo> gameInfos) {
		this.gameInfos = gameInfos;
	}
}
