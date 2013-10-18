package nyu.hezzze.weiqi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GoServiceAsync {

	void updateState(String id1, String id2, String state, AsyncCallback<Void> callback);
	void openChannel(String id, AsyncCallback<String> callback);
	void joinGame(String id, AsyncCallback<String> callback);
}
