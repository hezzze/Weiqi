package nyu.hezzze.weiqi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The main remote service of the game, responsible for send updates of the game
 * states to all the clients through channel API
 * 
 * @author hezzze
 * 
 */
@RemoteServiceRelativePath("go")
public interface GoService extends RemoteService {
	void updateGame(String gameId, String state);

	String openChannel(String email, String connectionId);

	String joinGame(String connectionId);

	void connectToGame(String connectionId, String oldGameId, String newGameId);

	void startGame(String connectionId, String oldGameId, String email,
			String otherEmail) throws Exception;

	List<GameInfo> getGameList(String email);
}
