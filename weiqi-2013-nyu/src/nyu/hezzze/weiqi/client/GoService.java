package nyu.hezzze.weiqi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("update")
public interface GoService extends RemoteService {
	void updateState(String id1, String id2, String state);
	String openChannel(String id);
	String joinGame(String id);
}
