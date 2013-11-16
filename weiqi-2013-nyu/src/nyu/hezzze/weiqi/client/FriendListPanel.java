package nyu.hezzze.weiqi.client;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

/**
 * A panel showing all the available games of the player stored in the online
 * datastore
 * 
 * @author hezzze
 * 
 */
public class FriendListPanel extends DialogBox {

	/**
	 * The list of available games
	 */
	final CellList<FbUserInfo> friendList;

	final ListDataProvider<FbUserInfo> dataProvider = new ListDataProvider<FbUserInfo>();

	/**
	 * An injected presenter object for communication between the panel and the
	 * presenter
	 */
	final Presenter presenter;
	GoMessages goMessages;

	public FriendListPanel(final Presenter presenter, final Graphics graphics) {

		goMessages = presenter.goMessages;

		setText(goMessages.friendList());

		setAnimationEnabled(true);

		setGlassEnabled(true);

		this.presenter = presenter;

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(4);
		vp.setWidth("250px");

		Label promptLabel = new Label(goMessages.promptForInvite());

		friendList = new CellList<FbUserInfo>(new UserCell(presenter), FbUserInfo.KEY_PROVIDER);
		friendList.setPageSize(20);

		dataProvider.addDataDisplay(friendList);

		ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();
		pagerPanel.setDisplay(friendList);
		pagerPanel.setPixelSize(500, 500);

		HorizontalPanel btnPanel = new HorizontalPanel();
		Button cancelBtn = new Button(goMessages.cancel());



		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}

		});

		btnPanel.add(cancelBtn);

		vp.add(promptLabel);
		vp.add(pagerPanel);
		vp.setCellHorizontalAlignment(pagerPanel, HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(btnPanel);

		setWidget(vp);

		//test();

	}


//	private void test() {
//		UserInfo userInfo = new UserInfo();
//		userInfo.setName("haha");
//		userInfo.setId("100001043090875");
//		userInfo.setPictureUrl("https://graph.facebook.com/" + userInfo.getId() + "/picture");
//		dataProvider.getList().add(userInfo);
//	}

	void setList(List<FbUserInfo> friendList) {
		dataProvider.getList().clear();
		Collections.sort(friendList);
		dataProvider.getList().addAll(friendList);
		dataProvider.refresh();
	}
	
	

}
