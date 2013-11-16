package nyu.hezzze.weiqi.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nyu.hezzze.weiqi.client.GoService;
import nyu.hezzze.weiqi.client.MyGameInfo;
import nyu.hezzze.weiqi.client.MyInfo;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.Go;
import nyu.hezzze.weiqi.shared.State;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;

/**
 * This is the implementation of the go service. This servlet is responsible for
 * auto matching players of the game, updating the game states in the datastore
 * and send those updates to the connected players through Channel API
 * 
 * @author hezzze
 * 
 */
public class GoServiceImpl extends XsrfProtectedServiceServlet implements
		GoService {

	/**
	 * A static variable used to represent the player who's waiting for an
	 * opponent
	 */
	static String waitingConnectionId = null;
	static String waitingOldGameId = null;
	private ChannelService channelService = ChannelServiceFactory
			.getChannelService();

	static {
		ObjectifyService.register(Player.class);
		ObjectifyService.register(Game.class);
		ObjectifyService.register(Connection.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * update the game state stored on the server and send updates to the
	 * clients
	 * 
	 */
	@Override
	public void updateGame(String gameId, String stateStr) {
		Game game = ofy().load().type(Game.class).id(gameId).now();
		game.setState(stateStr);
		if (game.getGameOver() != null && !game.isSingleGame) {
			endGame(game);
		}
		ofy().save().entity(game).now();
		sendUpdatesOfGameToConnections(game);

	}

	private void endGame(Game game) {
		Player player1 = ofy().load().type(Player.class)
				.id(game.getPlayerFbId(Gamer.BLACK)).now();
		Player player2 = ofy().load().type(Player.class)
				.id(game.getPlayerFbId(Gamer.WHITE)).now();

		Date date = new Date();

		player1.setDateOfLastGame(date);
		player2.setDateOfLastGame(date);

		player1.updateRankRating(player2, game.getWinnerFbId());
		player2.updateRankRating(player1, game.getWinnerFbId());

		ofy().save().entity(player1).now();
		ofy().save().entity(player2).now();
	}

	/**
	 * 
	 * 
	 */
	@Override
	public String openChannel(final String fbId, final String connectionId) {

		String channelToken = channelService.createChannel(connectionId);
		connect(fbId, connectionId);
		return channelToken;
	}

	@Override
	public void connect(final String fbId, final String connectionId) {
		ofy().transact(new Work<Void>() {

			@Override
			public Void run() {
				Player player = ofy().load().type(Player.class).id(fbId).now();
				if (player == null) {
					player = new Player(fbId);
					ofy().save().entity(player).now();
				}
				Connection connection = new Connection(connectionId, player);
				ofy().save().entity(connection).now();
				return null;
			}

		});
	}

	@Override
	public String joinGame(String connectionId, String oldGameId) {
		String matchingId = "";

		if (waitingConnectionId == null
				|| waitingConnectionId.equals(connectionId)
				|| ofy().load().type(Connection.class).id(waitingConnectionId)
						.now() == null) {
			waitingConnectionId = connectionId;
			waitingOldGameId = oldGameId;
			matchingId = connectionId;

		} else {

			startAutoMatchedGame(waitingConnectionId, waitingOldGameId,
					connectionId, oldGameId);

			matchingId = waitingConnectionId;

			waitingConnectionId = null;
			waitingOldGameId = null;

		}

		return matchingId;

	}

	private void startAutoMatchedGame(String waitingConnectionId,
			String waitingOldGameId, String connectionId, String oldGameId) {

		final Connection c1 = ofy().load().type(Connection.class)
				.id(waitingConnectionId).now();
		final Connection c2 = ofy().load().type(Connection.class)
				.id(connectionId).now();
		final Player p1 = c1.getPlayer();
		final Player p2 = c2.getPlayer();

		String id1 = p1.getFbId();
		String id2 = p2.getFbId();

		final Game newGame = new Game(id1, id2, State.serialize(new State()));
		String newGameId = newGame.getGameId();

		removeConnetionIdFromGame(waitingConnectionId, waitingOldGameId);
		removeConnetionIdFromGame(connectionId, oldGameId);

		newGame.addConnectionId(waitingConnectionId);
		newGame.addConnectionId(connectionId);

		p1.addGameId(newGameId);
		p2.addGameId(newGameId);

		c1.setGame(newGame);
		c2.setGame(newGame);

		sendMessageToConnection(waitingConnectionId, Go.MSG_HEADER + id2);
		sendMessageToConnection(connectionId, Go.MSG_HEADER + id1);

		ofy().transact(new Work<Void>() {

			@Override
			public Void run() {
				ofy().save().entity(newGame);
				ofy().save().entity(p1);
				ofy().save().entity(p2);
				ofy().save().entity(c1);
				ofy().save().entity(c2);
				return null;
			}

		});

		sendUpdatesOfGameToConnections(newGame);
	}

	private void sendMessageToConnection(String connectionId, String msg) {
		channelService.sendMessage(new ChannelMessage(connectionId, msg));
	}

	private void sendUpdatesOfGameToConnections(Game game) {
		Set<String> connectionIds = game.getConnectionIds();
		for (String id : connectionIds) {
			Connection connection = ofy().load().type(Connection.class).id(id)
					.now();
			Player player = connection.getPlayer();
			if (player.getFbId().equals(game.getPlayerFbId(Gamer.BLACK))) {
				channelService.sendMessage(new ChannelMessage(id, game
						.getStateStr()
						+ ",B,"
						+ game.getGameId()
						+ ","
						+ (game.isSingleGame ? "1" : "2")));
			} else {
				channelService.sendMessage(new ChannelMessage(id, game
						.getStateStr()
						+ ",W,"
						+ game.getGameId()
						+ ","
						+ (game.isSingleGame ? "1" : "2")));
			}
		}
	}

	@Override
	public MyInfo getMyInfo(String fbId) {
		Player player = ofy().load().type(Player.class).id(fbId).now();
		player.updateRankRD();
		Set<String> gameIds = player.getGameIds();
		List<MyGameInfo> myGameInfos = new ArrayList<MyGameInfo>();
		for (String gid : gameIds) {
			Game game = ofy().load().type(Game.class).id(gid).safe();
			MyGameInfo myGameInfo = new MyGameInfo(game.getGameId(),
					game.getOpponentId(fbId), game.getWinnerFbId() == null ? ""
							: game.getWinnerFbId(), (game.getWhoseTurnFbId()
							.equals(fbId) ? true : false), (game.getPlayerFbId(
							Gamer.BLACK).equals(fbId) ? Gamer.BLACK
							: Gamer.WHITE), game.getStartDate());
			myGameInfos.add(myGameInfo);
		}
		MyInfo myInfo = new MyInfo();
		myInfo.setGameInfos(myGameInfos);
		myInfo.setRankStr(player.getRank().toString());

		ofy().save().entity(player).now();

		return myInfo;

	}

	@Override
	public void connectToGame(String connectionId, String oldGameId,
			String otherGameId) {
		removeConnetionIdFromGame(connectionId, oldGameId);

		Connection connection = ofy().load().type(Connection.class)
				.id(connectionId).now();
		Game newGame = ofy().load().type(Game.class).id(otherGameId).now();

		connection.setGame(newGame);
		newGame.addConnectionId(connectionId);

		ofy().save().entity(newGame).now();
		ofy().save().entity(connection).now();

		sendUpdatesOfGameToConnections(newGame);

	}

	private void removeConnetionIdFromGame(String connectionId, String gameId) {
		if (gameId != null) {
			Game game = ofy().load().type(Game.class).id(gameId).now();
			game.removeConnectionId(connectionId);
			ofy().save().entity(game).now();
		}
	}

	@Override
	public void startGame(String connectionId, String oldGameId, String fbId,
			String otherFbId) throws Exception {

		Player player = ofy().load().type(Player.class).id(fbId).now();
		Player otherPlayer = ofy().load().type(Player.class).id(otherFbId)
				.now();
		if (otherPlayer == null) {
			otherPlayer = new Player(otherFbId);
			ofy().save().entity(otherPlayer).now();
		}
		final Game newGame = new Game(player.getFbId(), otherPlayer.getFbId(),
				State.serialize(new State()));
		player.addGameId(newGame.getGameId());
		otherPlayer.addGameId(newGame.getGameId());
		ofy().save().entity(newGame).now();

		connectToGame(connectionId, oldGameId, newGame.getGameId());

		ofy().save().entity(player).now();
		ofy().save().entity(otherPlayer).now();

	}

	@Override
	public void startSingleGame(String connectionId, String oldGameId,
			String fbId) {
		final Player player = ofy().load().type(Player.class).id(fbId).now();

		final Game newGame = new Game(player.getFbId(), getAIEmail(),
				State.serialize(new State()));
		newGame.isSingleGame = true;
		player.addGameId(newGame.getGameId());
		ofy().save().entity(newGame).now();

		connectToGame(connectionId, oldGameId, newGame.getGameId());

		ofy().save().entity(player).now();

	}

	private String getAIEmail() {
		return "AI@ok.com";
	}

}
