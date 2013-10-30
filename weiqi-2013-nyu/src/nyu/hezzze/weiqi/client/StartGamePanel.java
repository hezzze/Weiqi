package nyu.hezzze.weiqi.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The panel for the player to start a new game by entering an opponent's email
 * address
 * 
 * @author hezzze
 * 
 */
public class StartGamePanel extends DialogBox {

	final TextBox opponentEmailBox;
	GoMessages goMessages;

	public StartGamePanel(final Presenter presenter, final Graphics graphics) {

		goMessages = presenter.goMessages;

		setText(goMessages.start());

		setAnimationEnabled(true);

		setGlassEnabled(true);

		VerticalPanel vp = new VerticalPanel();

		Label lb = new Label(goMessages.promptForEmailOfOpponent());

		opponentEmailBox = new TextBox();

		HorizontalPanel hp = new HorizontalPanel();
		Button startBtn = new Button(goMessages.start());
		Button cancelBtn = new Button(goMessages.cancel());

		startBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				StartGamePanel.this.hide();
				presenter.startGame(opponentEmailBox.getText());
				graphics.joinBtn.setEnabled(false);
			}

		});

		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				StartGamePanel.this.hide();
			}

		});

		hp.add(startBtn);
		hp.add(cancelBtn);

		vp.add(lb);
		vp.add(opponentEmailBox);
		vp.add(hp);

		vp.setCellHorizontalAlignment(lb, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);

		setWidget(vp);

	}

	void focus() {
		opponentEmailBox.setFocus(true);
	}

	void reset() {
		opponentEmailBox.setText("");
	}
}
