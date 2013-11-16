package nyu.hezzze.weiqi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client interface to make RPC calls to go service servlet
 * 
 * @author hezzze
 * 
 */
public interface GoServiceAsync {

	void updateGame(String gameId, String state, AsyncCallback<Void> callback);

	void openChannel(String id, String connectionId,
			AsyncCallback<String> callback);

	void joinGame(String connectionId, String oldGameId,
			AsyncCallback<String> callback);

	void connectToGame(String connectionId, String oldGameId, String newGameId,
			AsyncCallback<Void> callback);

	void getMyInfo(String id, AsyncCallback<MyInfo> callback);

	void startGame(String connectionId, String oldGameId, String fbId,
			String otherFbId, AsyncCallback<Void> callback);

	void startSingleGame(String connectionId, String oldGameId, String myId,
			AsyncCallback<Void> callback);

	void connect(String myId, String connectionId,
			AsyncCallback<Void> asyncCallback);
}
