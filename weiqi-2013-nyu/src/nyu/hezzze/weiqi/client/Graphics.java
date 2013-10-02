package nyu.hezzze.weiqi.client;

import static nyu.hezzze.weiqi.shared.GameResult.BLACK_WIN;
import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;
import nyu.hezzze.weiqi.shared.GameOver;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.GoBoard;
import nyu.hezzze.weiqi.shared.State;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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

public class Graphics extends Composite implements Presenter.View {

	interface GoUiBinder extends UiBinder<Widget, Graphics> {
	};

	private static GoUiBinder uiBinder = GWT.create(GoUiBinder.class);

	@UiField
	Grid goBoard;
	@UiField
	Button passBtn;
	@UiField
	Image whoseTurnImage;
	@UiField
	Label messageLabel;
	@UiField
	HTML statusHTML;

	private final Presenter presenter;
	private final GoImages goImages;

	public Graphics() {
		presenter = new Presenter(this);
		this.goImages = GWT.create(GoImages.class);
		initWidget(uiBinder.createAndBindUi(this));

		goBoard.resize(GoBoard.ROWS, GoBoard.COLS);
		goBoard.setWidth("570px");
		goBoard.setHeight("570px");
		goBoard.setCellPadding(0);
		goBoard.setCellSpacing(0);
		goBoard.setBorderWidth(0);
		// gameGrid.setStyleName("board");

		passBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.pass();
				History.newItem("state="
						+ State.serialize(presenter.currentState));

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
				blank.setResource(goImages.blank());
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

	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public void setCell(int row, int col, Gamer gamer) {
		Image img = (Image) goBoard.getWidget(row, col);

		if (gamer == BLACK) {
			img.setResource(goImages.blackStone());
		} else if (gamer == WHITE) {
			img.setResource(goImages.whiteStone());
		} else {
			img.setResource(goImages.blank());
		}

	}

	@Override
	public void setWhoseTurn(Gamer gamer) {
		if (gamer == WHITE) {
			whoseTurnImage.setResource(goImages.whitePlayer());
		} else {
			whoseTurnImage.setResource(goImages.blackPlayer());
		}
	}

	@Override
	public void setMessage(String msg) {
		messageLabel.setText(msg);
	}

	@Override
	public void showGameResult(GameOver gameOver) {
		String html = "";
		
		if (gameOver.getGameResult() == BLACK_WIN) {
			html += "Black Wins!!! <br>";
		} else {
			html += "White Wins!!! <br>";
		}
		
		html += "Black Points: " + gameOver.getBlackPoints() + "<br>White Points: " + gameOver.getWhitePoints();
		
		statusHTML.setHTML(html);
	}
	
	

}
