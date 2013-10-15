package nyu.hezzze.weiqi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * An asynchrous version of the login service
 * @author hezzze
 *
 */
public interface LoginServiceAsync {

	public void login(String requestUri, AsyncCallback<LoginInfo> async);

}
