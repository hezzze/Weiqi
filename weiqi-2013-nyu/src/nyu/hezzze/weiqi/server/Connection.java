package nyu.hezzze.weiqi.server;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

/**
 * The entity class representing a connection between a client and the server
 * 
 * @author hezzze
 * 
 */
@Entity
public class Connection {
	@Id
	String id;
	@Load
	Ref<Player> playerRef;
	@Load
	Ref<Game> gameRef = null;

	@SuppressWarnings("unused")
	private Connection() {
	}

	Connection(String id, Player player) {
		this.id = id;
		this.playerRef = Ref.create(player);
	}

	public Player getPlayer() {
		return playerRef.get();
	}

	String getId() {
		return id;
	}

	/**
	 * @return the game entity corresponding to this connection
	 */
	public Game getGame() {
		return gameRef.get();
	}

	/**
	 * Set the game entity this connection involves
	 * 
	 * @param game
	 */
	public void setGame(Game game) {
		this.gameRef = Ref.create(game);
	}
}
