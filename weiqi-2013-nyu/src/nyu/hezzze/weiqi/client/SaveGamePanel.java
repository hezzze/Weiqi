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

public class SaveGamePanel extends DialogBox {
	
	final TextBox gameNameBox;
	
	public SaveGamePanel(final Presenter presenter) {
		setText("Save");
		
		setAnimationEnabled(true);
		
		setGlassEnabled(true);
		
		VerticalPanel vp = new VerticalPanel();
		
		Label lb = new Label("Enter a name for the game (Example: gg):   ");
		
		gameNameBox = new TextBox();
		
		HorizontalPanel hp = new HorizontalPanel();
		Button saveBtn = new Button("Save");
		Button cancelBtn = new Button("Cancel");
		
		saveBtn.addClickHandler( new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SaveGamePanel.this.hide();
				presenter.saveGame(gameNameBox.getText());
			}
			
		});
		
		cancelBtn.addClickHandler( new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				SaveGamePanel.this.hide();
			}
			
		});
		
		
		
		hp.add(saveBtn);
		hp.add(cancelBtn);
		
		vp.add(lb);
		vp.add(gameNameBox);
		vp.add(hp);
		
		vp.setCellHorizontalAlignment(lb, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
		
		setWidget(vp);
		
	}
	
	void reset() {
		gameNameBox.setText("");
	}
}
