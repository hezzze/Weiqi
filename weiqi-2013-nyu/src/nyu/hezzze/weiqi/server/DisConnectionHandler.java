package nyu.hezzze.weiqi.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

/**
 * The HttpServlet for tracking disconnections of clients
 * 
 * @author hezzze
 * 
 */
public class DisConnectionHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ChannelService channelService = ChannelServiceFactory.getChannelService();

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		ChannelPresence channelPresence = channelService.parsePresence(req);
		String connectionId = channelPresence.clientId();
		Connection connection = ofy().load().type(Connection.class)
				.id(connectionId).now();
		if (connection.gameRef != null) {
			Game game = connection.getGame();
			if (game != null) {
				game.removeConnectionId(connectionId);
				ofy().save().entity(game).now();
			}
		}

		ofy().delete().entity(connection).now();

	}

}
