package nyu.hezzze.weiqi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;
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
	Presenter presenter;
	GoMessages goMessages;
	Graphics graphics;

	@Override
	public void onModuleLoad() {
		goMessages = GWT.create(GoMessages.class);
		presenter = new Presenter(goMessages);
		graphics = presenter.getGraphics();
		RootLayoutPanel.get().add(graphics );

		Cookies.setCookie("JSESSIONID", "JSESSIONID", null, null, "/", false);
		
		XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync) GWT
				.create(XsrfTokenService.class);
		((ServiceDefTarget) xsrf).setServiceEntryPoint(GWT.getModuleBaseURL()
				+ "xsrf");

		xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {

			public void onSuccess(XsrfToken token) {
				login(token);
			}

			public void onFailure(Throwable caught) {
				try {
					throw caught;
				} catch (RpcTokenException e) {
					// Can be thrown for several reasons:
					// - duplicate session cookie, which may be a sign of a
					// cookie
					// overwrite attack
					// - XSRF token cannot be generated because session cookie
					// isn't
					// present
				} catch (Throwable e) {
					// unexpected
				}
			}
		});

	}

	protected void login(final XsrfToken xsrfToken) {
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		((HasRpcToken) loginService).setRpcToken(xsrfToken);

		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {

					@Override
					public void onFailure(Throwable error) {
						handleError(error);

					}

					@Override
					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							presenter.setMyEmail(loginInfo.getEmailAddress());
							presenter.initializeOnlineGame(xsrfToken);
							graphics.setSignInLink(goMessages.signOut(), loginInfo.getLogoutUrl());
						} else {
							LoginPanel lg = new LoginPanel(loginInfo
									.getLoginUrl(), goMessages, graphics);
							graphics.setBtnsEnabled(false);
							graphics.setSignInLink(goMessages.signIn().asString(), loginInfo
									.getLoginUrl());
							lg.center();
							lg.show();
						}

					}

				});

	}

	protected void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}

	}

}
