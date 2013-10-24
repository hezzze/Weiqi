package nyu.hezzze.weiqi.server;


import static com.googlecode.objectify.ObjectifyService.ofy;
import nyu.hezzze.weiqi.client.LoginInfo;
import nyu.hezzze.weiqi.client.LoginService;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

/**
 * The server side implementation of the login service, it will send back an
 * object containing the login status and relevant information of the user
 * 
 * @author hezzze
 * 
 */
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static {
		ObjectifyService.register(Player.class);
		ObjectifyService.register(Game.class);
		ObjectifyService.register(Connection.class);
	}
	
	ChannelService channelService  = ChannelServiceFactory.getChannelService();

	@Override
	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		

		if (user != null) {
			String email = user.getEmail().toLowerCase();
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(email);
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			
			if (ofy().load().type(Player.class).id(email).now() == null) {
				Player newPlayer = new Player(email);
				ofy().save().entity(newPlayer).now();
			} 

		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;

	}

}
