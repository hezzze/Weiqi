package nyu.hezzze.weiqi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client interface to make RPC calls to go service servlet
 * 
 * @author hezzze
 * 
 */
public interface GoServiceAsync {

	void updateGame(String gameId, String state, AsyncCallback<Void> callback);

	void openChannel(String email, String connectionId,
			AsyncCallback<String> callback);

	void joinGame(String connectionId, AsyncCallback<String> callback);

	void connectToGame(String connectionId, String oldGameId, String newGameId,
			AsyncCallback<Void> callback);

	void getGameList(String email, AsyncCallback<List<GameInfo>> callback);

	void startGame(String connectionId, String oldGameId, String email,
			String otherEmail, AsyncCallback<Void> callback);
}
