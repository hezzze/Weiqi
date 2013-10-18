package nyu.hezzze.weiqi.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * The Dialogbox widget showing at the start of the 
 * game to prompt the user to login as google user
 * @author hezzze
 *
 */
public class LoginPanel extends DialogBox {
	public LoginPanel(final String loginUri) {
		setText("Welcome!!");
		
		setAnimationEnabled(true);
		
		setGlassEnabled(true);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(8);
		Label l = new Label("Sign in to play the game pls!");
		
		HorizontalPanel hp = new HorizontalPanel();
		Button signinBtn = new Button("Sign-in");
		signinBtn.addClickHandler( new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(loginUri);
				LoginPanel.this.hide();
				
			}
			
		});
		// Button cancelBtn = new Button("No, thanks");
		// cancelBtn.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// LoginPanel.this.hide();
		// }
		//
		// });
		
		hp.add(signinBtn);
		//hp.add(cancelBtn);
		
		
		vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
		
		vp.add(l);
		vp.add(hp);
		
		setWidget(vp);
	}

}
