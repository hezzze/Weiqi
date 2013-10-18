package nyu.hezzze.weiqi.client;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;
import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;

import java.util.Iterator;

import nyu.hezzze.weiqi.shared.GameOver;
import nyu.hezzze.weiqi.shared.GameOverException;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.GoBoard;
import nyu.hezzze.weiqi.shared.IllegalMove;
import nyu.hezzze.weiqi.shared.Position;
import nyu.hezzze.weiqi.shared.State;

import com.google.common.base.Splitter;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.ChannelFactoryImpl;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
	 * The local storage for saving games
	 */
	Storage storage;

	Socket socket;

	boolean isStarted;

	Gamer whoAmI;

	String myId;

	String opponentId;

	GoServiceAsync goService;

	AsyncCallback<Void> updateCallback;

	/**
	 * specify a bunch of methods for the presenter to talk to the view
	 * 
	 * @author hezzze
	 * 
	 */
	public interface View {
		void setCell(int row, int col, Gamer gamer);

		void setWhoseTurnImage(Gamer gamer);

		void setMessage(String msg);

		void setButton(String str);

		void setGameOver(boolean isGameOver);

		void animateSetStone(int row, int col, Gamer gamer);

		void playStoneSound();

		void setIsMyTurn(boolean isMyTurn);

	}

	/**
	 * The presenter initialize itself with a new state
	 * 
	 */
	public Presenter() {

		currentState = null;
		board = GoBoard.INIT_BOARD;
		storage = Storage.getLocalStorageIfSupported();

		setGraphics(new Graphics(this));

		updateCallback = new AsyncCallback<Void>() {

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
			goService.updateState(myId, opponentId,
					State.serialize(currentState), updateCallback);

		} catch (IllegalMove e) {
			graphics.setMessage(e.getMessage());
		} catch (GameOverException e) {
			graphics.setMessage(e.getMessage());
		}

	}

	void makeAnimatedMove(int row, int col) {
		Position pos = new Position(row, col);
		Gamer gamer = currentState.whoseTurn();

		try {
			currentState = currentState.makeMove(pos);
			graphics.animateSetStone(row, col, gamer);
			graphics.playStoneSound();
			goService.updateState(myId, opponentId,
					State.serialize(currentState), updateCallback);

		} catch (IllegalMove e) {
			graphics.setMessage(e.getMessage());
		} catch (GameOverException e) {
			graphics.setMessage(e.getMessage());
		}
	}

	/**
	 * Pass for the current player, display an error message if the game is over
	 */
	public void pass() {
		try {
			currentState = currentState.pass();
			setState(currentState);
			goService.updateState(myId, opponentId,
					State.serialize(currentState), updateCallback);

		} catch (GameOverException e) {
			graphics.setMessage(e.getMessage());
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
			graphics.setMessage("It's your turn!");
		} else {
			graphics.setMessage("Waiting for your opponent to make a move...");
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
			graphics.setButton("PASS");
			graphics.setGameOver(false);
		}
	}

	/**
	 * Updating the board by finding the different cell between the old and new
	 * board
	 * 
	 * @param newBoard
	 *            the board to be updated to
	 */
	void updateBoard(Gamer[][] newBoard) {
		for (int i = 0; i < GoBoard.ROWS; i++) {
			for (int j = 0; j < GoBoard.COLS; j++) {
				if (board[i][j] != newBoard[i][j]) {
					graphics.setCell(i, j, newBoard[i][j]);
				}

			}
		}
		board = newBoard;
	}

	/**
	 * Show the end game info using html, current it shows the winner, and the
	 * points gained by both player, more elements is to add to this methods
	 * like remaining stones or the time of the game etc.
	 */
	void showEndGameInfo() {
		String msg = "";
		GameOver gameOver = currentState.getGameOver();

		if (gameOver.getGameResult() == BLACK_WIN) {
			msg += "Black Wins!!! \n";
		} else {
			msg += "White Wins!!! \n";
		}

		msg += "Black Points: " + gameOver.getBlackPoints()
				+ "\nWhite Points: " + gameOver.getWhitePoints();

		graphics.setMessage(msg);
		graphics.setButton("RESTART");
		graphics.setGameOver(true);
	}

	void restartGame() {
		currentState = new State();
		setState(currentState);
		goService.updateState(myId, opponentId, State.serialize(currentState),
				updateCallback);

	}

	void saveGame(String key) {

		if (storage != null) {
			storage.setItem(key, State.serialize(currentState));
		}
	}

	String[] getSavedGameNames() {
		String[] names = new String[storage.getLength()];
		for (int i = 0; i < names.length; i++) {
			names[i] = storage.key(i);
		}
		return names;

	}

	void loadGame(String key) {
		if (storage != null) {
			setState(State.deserialize(storage.getItem(key)));
		}

	}

	public Graphics getGraphics() {
		return (Graphics) graphics;
	}

	public void initializeMuiltiplayerGame() {

		goService = GWT.create(GoService.class);
		goService.openChannel(myId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());

			}

			@Override
			public void onSuccess(final String channelToken) {
				openSocket(channelToken);

			}

		});

	}

	private void openSocket(final String channelToken) {
		socket = new ChannelFactoryImpl().createChannel(channelToken).open(
				new SocketListener() {

					@Override
					public void onOpen() {
						graphics.setMessage("Server connected !!!");
					}

					@Override
					public void onMessage(String msg) {

						if (!isStarted) {
							Iterable<String> info = Splitter.on(',').split(msg);
							Iterator<String> it = info.iterator();
							opponentId = it.next();
							String me = it.next();
							whoAmI = (me.equals("B") ? BLACK : WHITE);

							isStarted = true;

							graphics.setMessage("You got an opponent >"
									+ opponentId + "<\n" + "You are playing "
									+ (whoAmI == BLACK ? ">Black<" : ">White<"));

							setState(new State());
						} else {
							if (!msg.equals(State.serialize(currentState))) {
								setState(State.deserialize(msg));
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

		graphics.setMessage("Welcome !!! \n" + myId);

	}

	void joinNewGame() {
		goService.joinGame(myId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				graphics.setMessage(caught.getMessage());

			}

			@Override
			public void onSuccess(String result) {
				
			}

		});
		graphics.setMessage("Game Joined !!!");
		graphics.setMessage("Waiting for an opponent to connect...");
	}

	public void setGraphics(View graphics) {
		this.graphics = graphics;

	}

	public void setMyId(String emailAddress) {
		myId = emailAddress;

	}

}
