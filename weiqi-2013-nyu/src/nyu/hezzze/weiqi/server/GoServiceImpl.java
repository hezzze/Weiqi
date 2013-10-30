package nyu.hezzze.weiqi.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nyu.hezzze.weiqi.client.GameInfo;
import nyu.hezzze.weiqi.client.GoService;
import nyu.hezzze.weiqi.client.PlayerInfo;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.Go;
import nyu.hezzze.weiqi.shared.State;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;
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
	private ChannelService channelService = ChannelServiceFactory
			.getChannelService();
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
		if (game.getGameOver() != null) {
			endGame(game);
		}
		ofy().save().entity(game).now();
		sendUpdatesOfGameToConnections(game);

	}

	private void endGame(Game game) {
		Player player1 = ofy().load().type(Player.class)
				.id(game.getPlayerEmail(Gamer.BLACK)).now();
		Player player2 = ofy().load().type(Player.class)
				.id(game.getPlayerEmail(Gamer.WHITE)).now();

		Date date = new Date();

		player1.setDateOfLastGame(date);
		player2.setDateOfLastGame(date);

		player1.updateRankRating(player2, game.getWinnerEmail());
		player2.updateRankRating(player1, game.getWinnerEmail());

		ofy().save().entity(player1).now();
		ofy().save().entity(player2).now();
	}

	/**
	 * 
	 * 
	 */
	@Override
	public String openChannel(final String email, final String connectionId) {

		String channelToken = channelService.createChannel(connectionId);

		ofy().transact(new Work<Void>() {

			@Override
			public Void run() {
				Player player = ofy().load().type(Player.class).id(email).now();
				Connection connection = new Connection(connectionId, player);
				ofy().save().entity(connection);
				return null;
			}

		});

		return channelToken;
	}

	@Override
	public String joinGame(String connectionId) {
		String matchingId = "";

		if (waitingConnectionId == null
				|| waitingConnectionId.equals(connectionId)
				|| ofy().load().type(Connection.class).id(waitingConnectionId)
						.now() == null) {
			waitingConnectionId = connectionId;
			matchingId = connectionId;

		} else {

			startAutoMatchedGame(waitingConnectionId, connectionId);

			matchingId = waitingConnectionId;

			waitingConnectionId = null;

		}

		return matchingId;

	}

	private void startAutoMatchedGame(String waitingConnectionId,
			String connectionId) {

		final Connection c1 = ofy().load().type(Connection.class)
				.id(waitingConnectionId).now();
		final Connection c2 = ofy().load().type(Connection.class)
				.id(connectionId).now();
		final Player p1 = c1.getPlayer();
		final Player p2 = c2.getPlayer();

		String email1 = p1.getEmail();
		String email2 = p2.getEmail();

		final Game newGame = new Game(email1, email2,
				State.serialize(new State()));
		String newGameId = newGame.getGameId();

		newGame.addConnectionId(waitingConnectionId);
		newGame.addConnectionId(connectionId);

		p1.addGameId(newGameId);
		p2.addGameId(newGameId);

		c1.setGame(newGame);
		c2.setGame(newGame);

		sendMessageToConnection(waitingConnectionId, Go.MSG_HEADER + email2);
		sendMessageToConnection(connectionId, Go.MSG_HEADER + email1);

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
		LinkedList<String> connectionIds = game.getConnectionIds();
		for (String id : connectionIds) {
			Connection connection = ofy().load().type(Connection.class).id(id)
					.now();
			Player player = connection.getPlayer();
			if (player.getEmail().equals(game.getPlayerEmail(Gamer.BLACK))) {
				channelService.sendMessage(new ChannelMessage(id, game
						.getState() + ",B," + game.getGameId()));
			} else {
				channelService.sendMessage(new ChannelMessage(id, game
						.getState() + ",W," + game.getGameId()));
			}
		}
	}

	@Override
	public PlayerInfo getPlayerInfo(String email) {
		Player player = ofy().load().type(Player.class).id(email).now();
		player.updateRankRD();
		Set<String> gameIds = player.getGameIds();
		List<GameInfo> gameInfos = new ArrayList<GameInfo>();
		for (String gid : gameIds) {
			Game game = ofy().load().type(Game.class).id(gid).safe();
			GameInfo gameInfo = new GameInfo(
					game.getGameId(),
					game.getWinnerEmail() == null ? "" : Player
							.emailToName(game.getWinnerEmail()),
					(game.getWhoseTurnEmail().equals(email) ? true : false),
					(game.getPlayerEmail(Gamer.BLACK).equals(email) ? Gamer.BLACK
							: Gamer.WHITE), game.getStartDate());
			gameInfos.add(gameInfo);
		}
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setGameInfos(gameInfos);
		playerInfo.setRankStr(player.getRank().toString());

		ofy().save().entity(player).now();

		return playerInfo;

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
			if (game.getConnectionIds().contains(connectionId)) {
				game.removeConnectionId(connectionId);
			}
			ofy().save().entity(game);
		}
	}

	@Override
	public void startGame(String connectionId, String oldGameId, String email,
			String otherEmail) throws Exception {

		final Player player = ofy().load().type(Player.class).id(email).now();
		final Player otherPlayer = ofy().load().type(Player.class)
				.id(otherEmail).now();
		if (otherPlayer == null) {
			throw new Exception("Player not found!!!");
		} else {
			final Game newGame = new Game(player.getEmail(),
					otherPlayer.getEmail(), State.serialize(new State()));
			player.addGameId(newGame.getGameId());
			otherPlayer.addGameId(newGame.getGameId());
			ofy().save().entity(newGame).now();

			connectToGame(connectionId, oldGameId, newGame.getGameId());

			ofy().transact(new Work<Void>() {

				@Override
				public Void run() {
					ofy().save().entity(player);
					ofy().save().entity(otherPlayer);
					return null;
				}

			});

		}

	}

}
