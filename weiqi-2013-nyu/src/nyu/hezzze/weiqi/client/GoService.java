package nyu.hezzze.weiqi.client;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * The main remote service of the game, responsible for send updates of the game
 * states to all the clients through channel API
 * 
 * @author hezzze
 * 
 */
@RemoteServiceRelativePath("go")
public interface GoService extends XsrfProtectedService {
	void updateGame(String gameId, String state);

	String openChannel(String fbId, String connectionId);

	String joinGame(String connectionId, String oldGameId);

	void connectToGame(String connectionId, String oldGameId, String newGameId);

	void startGame(String connectionId, String oldGameId, String fbId,
			String otherFbId) throws Exception;

	MyInfo getMyInfo(String fbId);
	
	void startSingleGame(String connectionId, String oldGameId, String myEmail);
	
	void connect(String myId, String connectionId);
}
