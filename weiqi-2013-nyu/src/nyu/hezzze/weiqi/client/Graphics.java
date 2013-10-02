package nyu.hezzze.weiqi.client;

import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.GoBoard;
import nyu.hezzze.weiqi.shared.State;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The view class of the game <br>
 * I need give much credits to the previous students of this course, their code
 * helped me a lot
 * 
 * @author hezzze
 * 
 */
public class Graphics extends Composite implements Presenter.View {

	interface GoUiBinder extends UiBinder<Widget, Graphics> {
	};

	private static GoUiBinder uiBinder = GWT.create(GoUiBinder.class);

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
	 * The label responsible for outputting error messages
	 */
	@UiField
	Label messageLabel;

	/**
	 * The html widget showing the status of the game
	 */
	@UiField
	HTML statusHTML;

	/**
	 * The logo of the game I drew if my self Just for Fun :)
	 */
	@UiField
	Image gameLogo;

	/**
	 * For communicating with the presenter
	 */
	private final Presenter presenter;

	/**
	 * For retrieving images
	 */
	private final GoImages goImages;

	/**
	 * For telling the button what to do, if the game is over, then the button
	 * will be used as a way to restart the game, otherwise it's used to pass
	 * the current turn
	 */
	boolean isGameOver;

	/**
	 * Constructor of the view. It will create a presenter for the game passing
	 * it self as a parameter, display the initial state of the game, and
	 * initialize History functionality
	 */
	public Graphics() {
		presenter = new Presenter(this);
		this.goImages = GWT.create(GoImages.class);
		initWidget(uiBinder.createAndBindUi(this));

		gameLogo.setResource(goImages.gameLogo());

		// Setting the style of the board
		// to make the cells collapse together
		goBoard.resize(GoBoard.ROWS, GoBoard.COLS);
		goBoard.setWidth("570px");
		goBoard.setHeight("570px");
		goBoard.setCellPadding(0);
		goBoard.setCellSpacing(0);
		goBoard.setBorderWidth(0);

		isGameOver = false;

		passOrRestartBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!isGameOver()) {
					presenter.pass();
					History.newItem("state="
							+ State.serialize(presenter.currentState));

				} else {
					presenter.restartGame();
					History.newItem("state="
							+ State.serialize(presenter.currentState));
				}

			}

		});

		whoseTurnImage.setResource(goImages.blackPlayer());

		for (int i = 0; i < GoBoard.ROWS; i++) {
			for (int j = 0; j < GoBoard.COLS; j++) {
				Image blank = new Image();
				final int row = i;
				final int col = j;
				blank.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						presenter.makeMove(row, col);
						History.newItem("state="
								+ State.serialize(presenter.currentState));
					}

				});
				blank.setResource(getBlank(row, col));
				blank.setWidth("30px");
				blank.setHeight("30px");
				goBoard.setWidget(i, j, blank);
			}
		}

		History.newItem("state=" + State.serialize(presenter.currentState));

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				try {
					if (historyToken.substring(0, 6).equals("state=")) {
						String str = historyToken.substring(6);
						State state = State.deserialize(str);
						presenter.setState(state);

					}
				} catch (IndexOutOfBoundsException e) {
					setMessage("State does not exist!");
					presenter.setState(new State());
				}

			}

		});

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
			img.setResource(goImages.blackStone());
		} else if (gamer == WHITE) {
			img.setResource(goImages.whiteStone());
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
				return goImages.blankNorthWestCorner();
			} else if (col == GoBoard.MAX_COL_INDEX) {
				return goImages.blankNorthEastCorner();
			} else {
				return goImages.blankNorthEdge();
			}
		} else if (row == GoBoard.MAX_ROW_INDEX) {
			if (col == 0) {
				return goImages.blankSouthWestCorner();
			} else if (col == GoBoard.MAX_COL_INDEX) {
				return goImages.blankSouthEastCorner();
			} else {
				return goImages.blankSouthEdge();
			}
		} else if (col == 0) {
			return goImages.blankWestEdge();
		} else if (col == GoBoard.MAX_COL_INDEX) {
			return goImages.blankEastEdge();
		} else if (GoBoard.isPositionWithBlackDot(row, col)) {
			return goImages.blankDotted();
		} else {
			return goImages.blank();
		}
	}

	/**
	 * Setting the image under the current player to indicate whose turn is this
	 */
	@Override
	public void setWhoseTurn(Gamer gamer) {
		if (gamer == WHITE) {
			whoseTurnImage.setResource(goImages.whitePlayer());
		} else {
			whoseTurnImage.setResource(goImages.blackPlayer());
		}
	}

	/**
	 * Setting the message to give feedback to the player
	 */
	@Override
	public void setMessage(String msg) {
		messageLabel.setText(msg);
	}

	/**
	 * Displaying the game status
	 */
	@Override
	public void showStatus(String html) {
		statusHTML.setHTML(html);
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
}
