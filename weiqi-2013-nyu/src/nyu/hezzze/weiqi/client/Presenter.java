package nyu.hezzze.weiqi.client;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;
import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;

import java.util.List;

import nyu.hezzze.weiqi.shared.GameOver;
import nyu.hezzze.weiqi.shared.GameOverException;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.Go;
import nyu.hezzze.weiqi.shared.IllegalMove;
import nyu.hezzze.weiqi.shared.Position;
import nyu.hezzze.weiqi.shared.State;

import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.ChannelFactoryImpl;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.XsrfToken;

/**
 * The presenter of the game, following the MVP design pattern, it has a public
 * interface for the view to implement and need a view as a parameter for
 * construction
 * 
 * @author hezzze
 * 
 */
public class Presenter {

	/**
	 * The view of the game
	 */
	View graphics;
	State currentState;
	Gamer[][] board;

	/**
	 * The socket to recieve important message from the server
	 */
	Socket socket;

	/**
	 * Who am I ?! Am i black ?! Or White ?!
	 */
	Gamer whoAmI;

	/**
	 * The email address of the client
	 */
	String myEmail;

	/**
	 * The game Id of the current game for retrieving information from the
	 * server
	 */
	String gameId;

	/**
	 * The id of the current connection to the server, it is essentially the
	 * client ID
	 */
	String connectionId;

	/**
	 * The service of the game to make RPC calls to
	 */
	GoServiceAsync goService;

	/**
	 * Called after the game state is updated
	 */
	AsyncCallback<Void> gameUpdateCallback;

	/**
	 * +- * For getting the text to be displayed according to the locale
	 */
	final GoMessages goMessages;

	/**
	 * specify a bunch of methods for the presenter to talk to the view
	 * 
	 * @author hezzze
	 * 
	 */
	public interface View {
		void setCell(int row, int col, Gamer gamer);

		void setWhoseTurnImage(Gamer gamer);

		void log(String msg);

		void setButton(String str);

		void setGameOver(boolean isGameOver);

		void animateSetStone(int row, int col, Gamer gamer);

		void playStoneSound();

		void setIsMyTurn(boolean isMyTurn);

		void setGameList(List<GameInfo> gameList);

		void showUserEmail(String emailAddress);

		void setRank(String rank);

	}

	/**
	 * The presenter initialize itself with a new state
	 * 
	 */
	public Presenter(final GoMessages goMessages) {

		gameId = null;
		currentState = null;
		board = Go.INIT_BOARD;
		this.goMessages = goMessages;

		setGraphics(new Graphics(this));

		gameUpdateCallback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Void result) {
			}

		};

	}

	/**
	 * Try to make a move for the current player, if the move is valid, then
	 * update the current state and the view, otherwise tell the view to display
	 * error message to the user
	 * 
	 * @param row
	 *            the row index of the move
	 * @param col
	 *            the column index of the move
	 */
	void makeMove(int row, int col) {
		Position pos = new Position(row, col);

		try {
			currentState = currentState.makeMove(pos);
			setState(currentState);
			graphics.playStoneSound();
			goService.updateGame(gameId, State.serialize(currentState),
					gameUpdateCallback);

		} catch (IllegalMove e) {
			graphics.log(e.getMessage());
		} catch (GameOverException e) {
			graphics.log(e.getMessage());
		}

	}

	void makeAnimatedMove(int row, int col) {
		Position pos = new Position(row, col);
		Gamer gamer = currentState.whoseTurn();

		try {
			currentState = currentState.makeMove(pos);
			graphics.animateSetStone(row, col, gamer);
			graphics.playStoneSound();
			goService.updateGame(gameId, State.serialize(currentState),
					gameUpdateCallback);

		} catch (IllegalMove e) {
			graphics.log(goMessages.invalidMoveException());
		} catch (GameOverException e) {
			graphics.log(goMessages.gameOverException());
		}
	}

	/**
	 * Pass for the current player, display an error message if the game is over
	 */
	public void pass() {
		try {
			currentState = currentState.pass();
			setState(currentState);
			goService.updateGame(gameId, State.serialize(currentState),
					gameUpdateCallback);

		} catch (GameOverException e) {
			graphics.log(goMessages.gameOverException());
		}

	}

	/**
	 * Set the state of the game,
	 * 
	 * @param state
	 */
	void setState(State state) {
		currentState = state;
		Gamer[][] newBoard = currentState.getBoard();

		boolean isMyTurn = whoAmI == currentState.whoseTurn();
		if (isMyTurn) {
			graphics.log(goMessages.yourTurn());
		} else {
			graphics.log(goMessages.waitingForOpponentToMove());
		}
		graphics.setIsMyTurn(isMyTurn);
		updateInfo(state);
		updateBoard(newBoard);
	}

	/**
	 * Update the information of the game, including refreshing the error label,
	 * update the current player and display game result
	 * 
	 * @param state
	 */
	void updateInfo(State state) {
		graphics.setWhoseTurnImage(state.whoseTurn());
		if (state.getGameOver() != null) {
			showEndGameInfo();
		} else {
			graphics.setButton(goMessages.pass());
			graphics.setGameOver(false);
		}
	}

	/**
	 * Updating the board by finding the different cells between the old and new
	 * board
	 * 
	 * @param newBoard
	 *            the board to be updated to
	 */
	void updateBoard(Gamer[][] newBoard) {
		for (int i = 0; i < Go.ROWS; i++) {
			for (int j = 0; j < Go.COLS; j++) {
				if (board[i][j] != newBoard[i][j]) {
					graphics.setCell(i, j, newBoard[i][j]);
				}

			}
		}
		board = newBoard;
	}

	/**
	 * Show the end game info, currently it shows the winner, and the points
	 * gained by both player, more elements are to be added to this methods like
	 * remaining stones or the time of the game etc.
	 */
	void showEndGameInfo() {
		String msg = "";
		GameOver gameOver = currentState.getGameOver();

		if (gameOver.getGameResult() == BLACK_WIN) {
			msg += goMessages.blackWin() + "\n";
		} else {
			msg += goMessages.whiteWin() + "\n";
		}

		msg += goMessages.blackPoints(gameOver.getBlackPoints()) + "\n"
				+ goMessages.whitePoints(gameOver.getWhitePoints()) + "\n";

		graphics.log(msg);
		graphics.setButton(goMessages.restart());
		graphics.setGameOver(true);
	}

	void restartGame() {
		currentState = new State();
		setState(currentState);
		goService.updateGame(gameId, State.serialize(currentState),
				gameUpdateCallback);

	}

	public Graphics getGraphics() {
		return (Graphics) graphics;
	}

	/**
	 * This method will do some initializations for connecting to the server
	 * like opening up the channel and loading the available game list
	 * @param xsrfToken 
	 */
	public void initializeOnlineGame(XsrfToken xsrfToken) {

		connectionId = generateUUID();

		goService = GWT.create(GoService.class);
		((HasRpcToken) goService).setRpcToken(xsrfToken);
		graphics.log(goMessages.serverConnected());
		goService.openChannel(myEmail, connectionId,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());

					}

					@Override
					public void onSuccess(final String channelToken) {

						openSocket(channelToken);

					}

				});

		updatePlayerInfo();

	}

	/**
	 * With the channel token sent back by the AppEngine, we can create a
	 * channel for further communication with the server
	 * 
	 * @param channelToken
	 *            the token for creating a channel on the client side
	 */
	private void openSocket(final String channelToken) {
		socket = new ChannelFactoryImpl().createChannel(channelToken).open(
				new SocketListener() {

					@Override
					public void onOpen() {
						graphics.log(goMessages.channelOpened());
					}

					@Override
					public void onMessage(String msg) {

						int len = Go.MSG_HEADER.length();
						if (msg.substring(0, len).equals(Go.MSG_HEADER)) {
							String email = msg.substring(len).trim();
							graphics.log(goMessages.youGotOpponent(email));
						} else {
							String[] strs = msg.split(",");
							String stateStr = strs[0];
							whoAmI = (strs[1].equals("B") ? BLACK : WHITE);
							gameId = strs[2];
							if (!stateStr.equals(State.serialize(currentState))) {
								setState(State.deserialize(stateStr));
							}
						}

					}

					@Override
					public void onError(ChannelError error) {

					}

					@Override
					public void onClose() {
						// Window.alert("Channel Closed!!");

					}

				});

		graphics.log(goMessages.welcome() + " \n" + myEmail);

	}

	/**
	 * Load the game list using an RPC call to the server
	 */
	void updatePlayerInfo() {
		goService.getPlayerInfo(myEmail, new AsyncCallback<PlayerInfo>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(PlayerInfo playerInfo) {
				graphics.setRank(playerInfo.getRankStr());
				graphics.setGameList(playerInfo.getGameInfos());
				graphics.log(goMessages.gameListLoaded());
			}

		});
	}

	/**
	 * This method is called when the player click the join button, which
	 * enables auto-matching for to online players
	 */
	void joinNewGame() {
		goService.joinGame(connectionId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				graphics.log(caught.getMessage());

			}

			@Override
			public void onSuccess(String result) {
				// graphics.log("matching id " + result);
			}

		});
		graphics.log(goMessages.gameJoined());
		graphics.log(goMessages.waitingForOpponentToConnect());
	}

	/**
	 * The user can load an available game on the server
	 * 
	 * @param newGameId
	 *            the id of the on-going game
	 */
	public void loadGame(String newGameId) {
		graphics.log(goMessages.loadingGame());
		String oldGameId = gameId;
		goService.connectToGame(connectionId, oldGameId, newGameId,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Void result) {
						graphics.log(goMessages.gameLoaded());

					}

				});

	}

	/**
	 * A player can start a new game by typing an user's email address, and if
	 * that user is found, a new game will be established between the two
	 * players
	 * 
	 * @param otherEmail
	 *            the email address of the opponent
	 */
	public void startGame(final String otherEmail) {
		String oldGameId = gameId;
		graphics.log(goMessages.startingGame());
		goService.startGame(connectionId, oldGameId, myEmail, otherEmail,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable error) {
						graphics.log(goMessages.playerNotFound());

					}

					@Override
					public void onSuccess(Void result) {

						graphics.log(goMessages.gameStarted(otherEmail));
					}

				});

	}

	public void setGraphics(View graphics) {
		this.graphics = graphics;

	}

	public void setMyEmail(String emailAddress) {
		myEmail = emailAddress;
		graphics.showUserEmail(emailAddress);

	}

	/**
	 * http://stackoverflow.com/questions/105034/how-to-create-a-guid-uuid-in-
	 * javascript
	 * 
	 * @return a an rfc4122 version 4 compliant UUID
	 */
	private native String generateUUID() /*-{
		var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx';
		uuid = uuid.replace(/[xy]/g, function(c) {
			var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		});
		return uuid;
	}-*/;

}
