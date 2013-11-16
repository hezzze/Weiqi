package nyu.hezzze.weiqi.client;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * This interface is used for internationalization. Each method will return text
 * according to the locale for the user interface or feedbacks of the game etc.
 * 
 * @author hezzze
 * 
 */

public interface GoMessages extends Messages {

	@DefaultMessage("Welcome !!!")
	String welcome();

	@DefaultMessage("Sign in using your facebook account to play the game pls !")
	String promptForSignIn();
	
	@DefaultMessage("Sign-in")
	SafeHtml signIn();
	
	@DefaultMessage("Sign-out")
	String signOut();

	@DefaultMessage("Go")
	String title();

	@DefaultMessage("Friend List")
	String friendList();

	@DefaultMessage("Start")
	String start();

	@DefaultMessage("Join")
	String join();

	@DefaultMessage("Current Player")
	String currentPlayer();

	@DefaultMessage("PASS")
	String pass();

	@DefaultMessage("RESTART")
	String restart();

	@DefaultMessage("Game Info")
	String gameInfo();

	@DefaultMessage("Selecting a game from the list:   ")
	String promptForSelectGame();
	
	@DefaultMessage("Invite or start a game with a friend: ")
	String promptForInvite();

	@DefaultMessage("Select")
	String select();

	@DefaultMessage("Cancel")
	String cancel();

	@DefaultMessage("Enter the email of your opponent:   ")
	String promptForEmailOfOpponent();

	@DefaultMessage("It''s your turn!")
	String yourTurn();

	@DefaultMessage("Waiting for your opponent to make a move...")
	String waitingForOpponentToMove();

	@DefaultMessage("Black wins !!! ")
	String blackWin();

	@DefaultMessage("White wins !!!")
	String whiteWin();

	@DefaultMessage("Black Points: {0}")
	String blackPoints(Integer blackPoints);

	@DefaultMessage("White Points: {0}")
	String whitePoints(Integer whitePoints);

	@DefaultMessage("Server connected!!!")
	String serverConnected();

	@DefaultMessage("Channel Opened!!!")
	String channelOpened();

	@DefaultMessage("Game list loaded succesfully...")
	String gameListLoaded();

	@DefaultMessage("Game Joined !!!")
	String gameJoined();

	@DefaultMessage("Waiting for an opponent to connect...")
	String waitingForOpponentToConnect();

	@DefaultMessage("Loading a game, the current game will be saved on the server, "
			+ "you can always load it back if you want...")
	String loadingGame();

	@DefaultMessage("Game loaded successfully!!")
	String gameLoaded();
	
	@DefaultMessage("Starting a new game, the current game will be saved on the server,"
			+ " you can always load it back if you want")
	String startingGame();
	
	@DefaultMessage("Game started  with {0} !!!")
	String gameStarted(String email);
	
	@DefaultMessage("Invalid Move !!!")
	String invalidMoveException();
	
	@DefaultMessage("Game is over !!!")
	String gameOverException();

	@DefaultMessage("You''ve got an opponent: {0}!!!")
	String youGotOpponent(String email);

	@DefaultMessage("Whose Turn")
	String whoseTurn();

	@DefaultMessage("Winner")
	String winner();
	
	@DefaultMessage("Starting at: {0,date,short}")
	String startDate(Date timeStamp);

	@DefaultMessage("Drag and drop me to make a move!")
	String dragTooltip();

	@DefaultMessage("Hello!")
	String hello();

	@DefaultMessage("Me")
	String myTurn();
	
	@DefaultMessage("Opponent")
	String othersTurn();

	@DefaultMessage("Rank")
	String rank();

	@DefaultMessage("Guest")
	String guest();

	@DefaultMessage("Player not found!!!")
	String playerNotFound();

	@DefaultMessage("Single game starting error!")
	String singleGameStartFailed();

	@DefaultMessage("Single Game")
	String singleGame();

	@DefaultMessage("No thanks")
	SafeHtml noThanks();

	@DefaultMessage("Reopenning the channel...")
	String reopenChannel();



	
	
}
