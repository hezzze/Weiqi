package nyu.hezzze.weiqi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The interface for the login service which will be 
 * implemented by the server
 * @author hezzze
 *
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
	public LoginInfo login(String requestUrl);

}
