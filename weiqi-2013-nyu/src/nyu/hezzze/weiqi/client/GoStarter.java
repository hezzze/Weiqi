package nyu.hezzze.weiqi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * The Entry class of the game, the game will start immediately once the page is
 * fully loaded
 * 
 * @author hezzze
 * 
 */
public class GoStarter implements EntryPoint {
	
	LoginInfo loginInfo = null;

	@Override
	public void onModuleLoad() {
		loadGame();
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo> () {

			@Override
			public void onFailure(Throwable error) {
				handleError(error);
				
			}

			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn()) {
					
				} else {
					LoginPanel lg = new LoginPanel(loginInfo.getLoginUrl());
					lg.center();
					lg.show();
				}
				
			}
			
		});

	}

	protected void handleError(Throwable error) {
		Window.alert(error.getMessage());
	if(error instanceof NotLoggedInException) {
		Window.Location.replace(loginInfo.getLogoutUrl());
	}
		
	}

	private void loadGame() {
		Graphics graphics = new Graphics();
		RootLayoutPanel.get().add(graphics);
	}

}
