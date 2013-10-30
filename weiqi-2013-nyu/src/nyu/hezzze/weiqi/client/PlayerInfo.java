package nyu.hezzze.weiqi.client;

import java.io.Serializable;
import java.util.List;

public class PlayerInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<GameInfo> gameInfos;
	private String rankStr;

	public List<GameInfo> getGameInfos() {
		return gameInfos;
	}

	public void setGameInfos(List<GameInfo> gameInfos) {
		this.gameInfos = gameInfos;
	}

	public String getRankStr() {
		return rankStr;
	}

	public void setRankStr(String rankStr) {
		this.rankStr = rankStr;
	}

}
