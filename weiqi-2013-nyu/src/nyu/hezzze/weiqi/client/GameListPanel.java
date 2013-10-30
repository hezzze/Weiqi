package nyu.hezzze.weiqi.client;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * A panel showing all the available games of the player stored in the online
 * datastore
 * 
 * @author hezzze
 * 
 */
public class GameListPanel extends DialogBox {

	/**
	 * The list of available games
	 */
	final CellList<GameInfo> gameIdList;

	final ListDataProvider<GameInfo> dataProvider = new ListDataProvider<GameInfo>();

	String selectedGameId = null;

	/**
	 * An injected presenter object for communication between the panel and the
	 * presenter
	 */
	final Presenter presenter;
	Button selectBtn;
	GoMessages goMessages;

	public GameListPanel(final Presenter presenter, final Graphics graphics) {

		goMessages = presenter.goMessages;

		setText(goMessages.gameList());

		setAnimationEnabled(true);

		setGlassEnabled(true);

		this.presenter = presenter;

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(4);
		vp.setWidth("250px");

		Label lb = new Label(goMessages.promptForSelectGame());

		gameIdList = new CellList<GameInfo>(new GameCell(graphics));
		gameIdList.setPageSize(5);
		final SingleSelectionModel<GameInfo> selectionModel = new SingleSelectionModel<GameInfo>(
				GameInfo.KEY_PROVIDER);
		gameIdList.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						selectedGameId = selectionModel.getSelectedObject()
								.getId();
						selectBtn.setEnabled(selectedGameId != null);

					}

				});

		dataProvider.addDataDisplay(gameIdList);

		ScrollPanel wp = new ScrollPanel(gameIdList);
		wp.setPixelSize(250, 300);

		HorizontalPanel hp = new HorizontalPanel();
		selectBtn = new Button(goMessages.select());
		Button cancelBtn = new Button(goMessages.cancel());

		selectBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				GameListPanel.this.hide();
				graphics.joinBtn.setEnabled(false);
				presenter.loadGame(selectedGameId);

			}

		});

		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				GameListPanel.this.hide();
			}

		});

		hp.add(selectBtn);
		hp.add(cancelBtn);

		vp.add(lb);
		vp.add(wp);
		vp.setCellHorizontalAlignment(wp, HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(hp);

		setWidget(vp);

		//test();

	}

	void focus() {
		selectBtn.setFocus(true);
	}

//	private void test() {
//		dataProvider.getList().add(
//				new GameInfo("test", "test", true, Gamer.BLACK, new Date()));
//	}

	void setList(List<GameInfo> gameList) {
		dataProvider.getList().clear();
		dataProvider.getList().addAll(gameList);
		selectBtn.setEnabled(selectedGameId != null);
	}

}
