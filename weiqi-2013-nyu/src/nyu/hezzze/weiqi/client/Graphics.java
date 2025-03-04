package nyu.hezzze.weiqi.client;

import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;

import java.util.List;

import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.Go;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.gwtfb.sdk.FBXfbml;

/**
 * The view class of the game <br>
 * I need give much credits to the previous students of this course, their code
 * helped me a lot
 * 
 * @author hezzze
 * 
 */
public class Graphics extends Composite implements Presenter.View {

	/**
	 * The size of the stone image as in pixels
	 */
	static final int STONE_SIZE = 30;

	/**
	 * The duration of the stone animation as in milliseconds
	 */
	static final int SET_STONE_ANIMATION_DURATION = 300;

	/**
	 * Fancy stuff for setting up the UiBinder
	 * 
	 * @author hezzze
	 * 
	 */
	interface GoUiBinder extends UiBinder<Widget, Graphics> {
	};

	/**
	 * More fancy stuff to programatically change some CSS style
	 * 
	 * @author hezzze
	 * 
	 */
	interface MyStyle extends CssResource {
		String cellHover();
	}

	private static GoUiBinder uiBinder = GWT.create(GoUiBinder.class);

	/**
	 * The Css style class for programatically changing the style of widgets
	 */
	@UiField
	MyStyle style;

	/**
	 * This is for the animation to locate a absolute position on the board
	 */
	@UiField
	AbsolutePanel boardPanel;

	/**
	 * The HTML table containing the game board
	 */
	@UiField
	Grid goBoard;

	/**
	 * The button for pass or restart the game
	 */
	@UiField
	Button passOrRestartBtn;

	/**
	 * The image indicating who's the current player
	 */
	@UiField
	Image whoseTurnImage;

	/**
	 * The logo of the game I drew if my self Just for Fun :)
	 */
	@UiField
	Image gameLogo;

	/**
	 * For communicating with the presenter
	 */
	private Presenter presenter = null;

	/**
	 * For retrieving images
	 */
	final GoResources goResources;

	/**
	 * For telling the button what to do, if the game is over, then the button
	 * will be used as a way to restart the game, otherwise it's used to pass
	 * the current turn
	 */
	boolean isGameOver;

	/**
	 * The sound of putting down a stone
	 */
	Audio stoneSound;

	/**
	 * The button for loading the list of available games
	 */
	@UiField
	Button friendListBtn;

	/**
	 * The widget is responsible for giving text feedback of the game status
	 */
	@UiField
	TextArea gameLogArea;

	/**
	 * The log to be shown in the text
	 */
	StringBuilder gameLog = new StringBuilder();

	/**
	 * The dialogBox for loading a game
	 */
	FriendListPanel friendListPanel;

	/**
	 * Indicating if it's the client's turn to do something
	 */
	boolean isMyTurn;

	/**
	 * The title of the game
	 */
	@UiField
	Label title;


	/**
	 * For getting the text to be displayed according to the locale
	 */
	final GoMessages goMessages;

	/**
	 * The text displayed when the cursor hover on the current player image as
	 * tooltip for drag and drop
	 */
	@UiField
	Label dragTooltip;

	/**
	 * The label for displaying the user's email address
	 */
	@UiField
	Label nameLabel;

	/**
	 * The label for displaying the user's rank range
	 */
	@UiField
	InlineLabel rankLabel;
	
	@UiField
	InlineLabel rank;

	/**
	 * The button for triggering a single game
	 */
	@UiField
	Button singleGameBtn;
	
	@UiField
	SimplePanel fbLoginPanel;
	
	@UiField
	Image myPic;

	/**
	 * Constructor of the view. It takes a presenter of the game as a parameter
	 * and initialize the user interface
	 * 
	 * @param presenter
	 */
	public Graphics(final Presenter presenter) {
		this.presenter = presenter;
		goResources = GWT.create(GoResources.class);

		goMessages = presenter.goMessages;

		initWidget(uiBinder.createAndBindUi(this));
		gameLogo.setResource(goResources.gameLogo());

		title.setText(goMessages.title());
		friendListBtn.setText(goMessages.friendList());
		passOrRestartBtn.setText(goMessages.pass());
		dragTooltip.setText(goMessages.dragTooltip());
		nameLabel.setText(goMessages.guest());
		rankLabel.setText(goMessages.rank());
		singleGameBtn.setText(goMessages.singleGame());
		myPic.setResource(goResources.guestImg());

		// Setting the style of the board
		// to make the cells collapse together
		goBoard.resize(Go.ROWS, Go.COLS);
		goBoard.setWidth("570px");
		goBoard.setHeight("570px");
		goBoard.setCellPadding(0);
		goBoard.setCellSpacing(0);
		goBoard.setBorderWidth(0);

		isGameOver = false;

		isMyTurn = true;

		gameLogArea.setPixelSize(250, (int) (Go.ROWS * STONE_SIZE * 0.75));
		gameLogArea.setCharacterWidth(25);
		gameLogArea.setReadOnly(true);

		boardPanel.setPixelSize(Go.ROWS * STONE_SIZE, Go.COLS * STONE_SIZE);

		passOrRestartBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (isMyTurn) {
					if (!isGameOver()) {
						presenter.pass();

					} else {
						presenter.restartGame();

					}
				}

			}

		});

		whoseTurnImage.setResource(goResources.blackPlayer());
		whoseTurnImage.getElement().setDraggable(Element.DRAGGABLE_TRUE);
		whoseTurnImage.addDragStartHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {
				if (isMyTurn) {
					event.setData("text", "Dragging!");
					event.getDataTransfer().setDragImage(
							whoseTurnImage.getElement(), STONE_SIZE / 2,
							STONE_SIZE / 2);
				}
			}

		});

		for (int i = 0; i < Go.ROWS; i++) {
			for (int j = 0; j < Go.COLS; j++) {
				final Image cell = new Image();
				final int row = i;
				final int col = j;
				cell.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						if (isMyTurn) {
							presenter.makeAnimatedMove(row, col);
						}
					}

				});

				cell.setResource(getBlank(row, col));
				cell.setPixelSize(STONE_SIZE, STONE_SIZE);
				goBoard.setWidget(i, j, cell);

				cell.addDragOverHandler(new DragOverHandler() {

					@Override
					public void onDragOver(DragOverEvent event) {

					}

				});
				cell.addDragEnterHandler(new DragEnterHandler() {

					@Override
					public void onDragEnter(DragEnterEvent event) {
						if (isMyTurn) {
							cell.getElement().addClassName(style.cellHover());
						}
					}

				});
				cell.addDragLeaveHandler(new DragLeaveHandler() {

					@Override
					public void onDragLeave(DragLeaveEvent event) {
						if (isMyTurn) {
							cell.getElement()
									.removeClassName(style.cellHover());
						}

					}

				});

				cell.addDropHandler(new DropHandler() {

					@Override
					public void onDrop(DropEvent event) {
						cell.getElement().removeClassName(style.cellHover());
						event.preventDefault();

						if (isMyTurn) {
							presenter.makeMove(row, col);
						}

					}

				});
			}
		}

		if (Audio.isSupported()) {
			stoneSound = Audio.createIfSupported();
			stoneSound.addSource(
					goResources.stoneMp3().getSafeUri().asString(),
					AudioElement.TYPE_MP3);
			stoneSound.addSource(
					goResources.stoneWav().getSafeUri().asString(),
					AudioElement.TYPE_WAV);
		}

		friendListPanel = new FriendListPanel(presenter, this);

	}

	/**
	 * @return presenter of the game
	 */
	public Presenter getPresenter() {
		return presenter;
	}

	/**
	 * Setting a specific cell to show the corresponding image in the table
	 * containing the game board
	 */
	@Override
	public void setCell(int row, int col, Gamer gamer) {
		Image img = (Image) goBoard.getWidget(row, col);

		if (gamer == BLACK) {
			img.setResource(goResources.blackStone());
		} else if (gamer == WHITE) {
			img.setResource(goResources.whiteStone());
		} else {
			img.setResource(getBlank(row, col));
		}

	}

	/**
	 * Blank spaces on the board have different images corresponding to it, thus
	 * calling this method to determine which images should be retrieved
	 * 
	 * @param row
	 *            the row index of the empty spot
	 * @param col
	 *            the column index of the empty spot
	 * @return the appropriate image to display
	 */
	private ImageResource getBlank(int row, int col) {
		if (row == 0) {
			if (col == 0) {
				return goResources.blankNorthWestCorner();
			} else if (col == Go.MAX_COL_INDEX) {
				return goResources.blankNorthEastCorner();
			} else {
				return goResources.blankNorthEdge();
			}
		} else if (row == Go.MAX_ROW_INDEX) {
			if (col == 0) {
				return goResources.blankSouthWestCorner();
			} else if (col == Go.MAX_COL_INDEX) {
				return goResources.blankSouthEastCorner();
			} else {
				return goResources.blankSouthEdge();
			}
		} else if (col == 0) {
			return goResources.blankWestEdge();
		} else if (col == Go.MAX_COL_INDEX) {
			return goResources.blankEastEdge();
		} else if (Go.isPositionWithBlackDot(row, col)) {
			return goResources.blankDotted();
		} else {
			return goResources.blank();
		}
	}

	/**
	 * Setting the image under the current player to indicate whose turn is this
	 */
	@Override
	public void setWhoseTurnImage(Gamer gamer) {
		if (gamer == WHITE) {
			whoseTurnImage.setResource(goResources.whitePlayer());
		} else {
			whoseTurnImage.setResource(goResources.blackPlayer());
		}

	}

	/**
	 * Setting the message to give feedback to the player
	 */
	@Override
	public void log(String msg) {
		gameLog.append(msg + "\n");
		gameLogArea.setText(gameLog.toString());
		//gameLogArea.setCursorPos(gameLogArea.getText().length());
		scrollToBottom(gameLogArea.getElement());
	}
	

	private void scrollToBottom(com.google.gwt.user.client.Element element) {
		element.setScrollTop(element.getScrollHeight() - element.getClientHeight());
	}

	/**
	 * Setting the isGameOver instance variable
	 */
	@Override
	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	/**
	 * @return <code>true</code> if the game is over
	 */
	boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * set the text on the button
	 */
	@Override
	public void setButton(String str) {
		passOrRestartBtn.setText(str);

	}

	/**
	 * Creates an rustic little animation when the player put a stone
	 */
	@Override
	public void animateSetStone(int row, int col, Gamer gamer) {
		ImageResource stoneRes;
		if (gamer == WHITE) {
			stoneRes = goResources.whitePlayer();
		} else {
			stoneRes = goResources.blackPlayer();
		}
		SetStoneAnimation animation = new SetStoneAnimation(
				(Image) goBoard.getWidget(row, col), stoneRes, presenter,
				stoneSound);
		animation.run(SET_STONE_ANIMATION_DURATION);

	}

	@UiHandler("friendListBtn")
	void handleFriendListClick(ClickEvent e) {
		presenter.updateInfo();
		friendListPanel.center();
		friendListPanel.show();
	}


	@UiHandler("singleGameBtn")
	void handleSingleGameClick(ClickEvent e) {
		presenter.startSingleGame();
	}

	/**
	 * Play the sound of putting a stone
	 */
	@Override
	public void playStoneSound() {
		if (stoneSound != null) {
			stoneSound.play();
		}

	}

	/**
	 * Is this my turn?
	 */
	@Override
	public void setIsMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;

	}

	/**
	 * Set the available games list
	 */
	@Override
	public void setFriendInfos(List<FbUserInfo> friendList) {
		friendListPanel.setList(friendList);
	}

	@Override
	public void showNameAndPic(String name, String picUrl) {
		nameLabel.setText(name);
		myPic.setUrl(picUrl);

	}

	@Override
	public void setRank(String rankStr) {
		rank.setText(rankStr);
	}

	@Override
	public void setBtnsEnabled(boolean enabled) {
		friendListBtn.setEnabled(enabled);
		singleGameBtn.setEnabled(enabled);
	}

	public void createFBLoginButton() {
		fbLoginPanel.setWidget(new FBLoginButton());
		FBXfbml.parse();
	}
	
	public void hideFriendList() {
		friendListPanel.hide();
	}


}
