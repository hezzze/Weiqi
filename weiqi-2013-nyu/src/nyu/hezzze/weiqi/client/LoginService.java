package nyu.hezzze.weiqi.client;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * The interface for the login service which will be 
 * implemented by the server
 * @author hezzze
 *
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends XsrfProtectedService {
	
	public LoginInfo login(String requestUrl);

}
