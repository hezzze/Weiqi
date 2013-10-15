package nyu.hezzze.weiqi.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A panel showing all the saved games in 
 * the local storage. User can load a specific 
 * game or remove a record.
 * @author hezzze
 *
 */
public class LoadGamePanel extends DialogBox {

	/**
	 * The list of saved games
	 */
	final ListBox savedGameListBox;
	
	/**
	 * An injected presenter object 
	 * for managing the local storage
	 */
	final Presenter presenter;
	Button loadBtn;
	Button removeBtn;

	public LoadGamePanel(final Presenter presenter) {
		setText("Load");

		setAnimationEnabled(true);

		setGlassEnabled(true);

		this.presenter = presenter;

		VerticalPanel vp = new VerticalPanel();

		Label lb = new Label("Choose a game to load:   ");

		savedGameListBox = new ListBox();
		savedGameListBox.setWidth("11em");
		savedGameListBox.setVisibleItemCount(10);
		

		
		HorizontalPanel hp = new HorizontalPanel();
		loadBtn = new Button("Load");
		removeBtn = new Button("Remove");
		Button cancelBtn = new Button("Cancel");
		

		loadBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LoadGamePanel.this.hide();
				presenter.loadGame(savedGameListBox
						.getItemText(savedGameListBox.getSelectedIndex()));
			}

		});

		removeBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String key = savedGameListBox
						.getItemText(savedGameListBox.getSelectedIndex());
				presenter.storage.removeItem(key);
				savedGameListBox.removeItem(savedGameListBox.getSelectedIndex());
				updateButton();
			}

		});

		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LoadGamePanel.this.hide();
			}

		});

		hp.add(loadBtn);
		hp.add(removeBtn);
		hp.add(cancelBtn);

		vp.add(lb);
		vp.add(savedGameListBox);
		vp.add(hp);

		setWidget(vp);
		
		reset();
		
		

	}

	private void updateButton() {
		boolean hasItem = savedGameListBox.getItemCount() > 0;
		loadBtn.setEnabled(hasItem);
		removeBtn.setEnabled(hasItem);
	}

	void reset() {
		savedGameListBox.clear();
		String[] savedGames = presenter.getSavedGameNames();
		for (int i = 0; i < savedGames.length; i++) {
			savedGameListBox.addItem(savedGames[i]);
		}
		updateButton();
	}
	
	
	
	
}
