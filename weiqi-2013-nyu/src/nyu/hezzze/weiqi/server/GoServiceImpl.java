package nyu.hezzze.weiqi.server;

import nyu.hezzze.weiqi.client.GoService;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GoServiceImpl extends RemoteServiceServlet implements GoService {

	static String waitingClientId = null;
	private ChannelService channelService = ChannelServiceFactory.getChannelService();;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void updateState(String id1, String id2, String state) {
		channelService.sendMessage(new ChannelMessage(id1, state));
		channelService.sendMessage(new ChannelMessage(id2, state));

	}

	@Override
	public String openChannel(String id) {
		return channelService.createChannel(id);
	}

	@Override
	public String joinGame(String id) {
		String matchingId = "";
		if (waitingClientId == null) {
			waitingClientId = id;
		} else if (!waitingClientId.equals(id)) {
			matchingId = waitingClientId;
			channelService.sendMessage(new ChannelMessage(waitingClientId, id
					+ ",B"));
			channelService.sendMessage(new ChannelMessage(id, waitingClientId
					+ ",W"));
			waitingClientId = null;
		}
		
		return matchingId;
		
	}

}
