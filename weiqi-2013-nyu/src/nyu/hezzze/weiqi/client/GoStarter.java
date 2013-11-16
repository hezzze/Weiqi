package nyu.hezzze.weiqi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.gwtfb.client.JSOModel;
import com.gwtfb.sdk.FBCore;
import com.gwtfb.sdk.FBEvent;
import com.gwtfb.sdk.FBXfbml;

/**
 * The Entry class of the game, the game will start immediately once the page is
 * fully loaded
 * 
 * @author hezzze
 * 
 */
public class GoStarter implements EntryPoint {

	public static final String APPID = "390807777719509";
	public static final String CHANNEL_URL = "//8.weiqi-hezzze.appspot.com/channel.html";
	public static final boolean STATUS = true;
	public static final boolean XFBML = true;

	Presenter presenter;
	GoMessages goMessages;
	Graphics graphics;
	FBCore fbCore;
	FBEvent fbEvent;
	FBXfbml fbXfbml;
	FbUserInfo myFbInfo;

	@Override
	public void onModuleLoad() {
		fbCore = GWT.create(FBCore.class);
		fbEvent = GWT.create(FBEvent.class);
		goMessages = GWT.create(GoMessages.class);
		presenter = new Presenter(goMessages, fbCore);
		graphics = presenter.getGraphics();

		RootLayoutPanel.get().add(graphics);

		graphics.log(goMessages.gameStarted("AI"));
		//graphics.setBtnsEnabled(false);

		Cookies.setCookie("JSESSIONID", "JSESSIONID", null, null, "/", false);

		XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync) GWT
				.create(XsrfTokenService.class);
		((ServiceDefTarget) xsrf).setServiceEntryPoint(GWT.getModuleBaseURL()
				+ "xsrf");

		xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {

			public void onSuccess(final XsrfToken token) {
				Scheduler.get().scheduleDeferred(
						new Scheduler.ScheduledCommand() {

							@Override
							public void execute() {
								fbCore.init(APPID, CHANNEL_URL, STATUS, XFBML);
								login(token);
							}
						});

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

		fbCore.getLoginStatus(new AsyncCallback<JavaScriptObject>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(JavaScriptObject result) {
				JSOModel jsm = result.cast();
				String status = jsm.get("status");
				if (status.equals("not_authorized")
						|| !status.equals("connected")) {
					LoginPanel lg = new LoginPanel(fbCore, goMessages, graphics);
					lg.center();
					lg.show();
				} else {
					graphics.createFBLoginButton();
				}
			}

		});

		fbEvent.subscribe("auth.authResponseChange",
				new AsyncCallback<JavaScriptObject>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(JavaScriptObject result) {
						JSOModel jsm = result.cast();
						String status = jsm.get("status");
						if (status.equals("connected")) {

							initialize(xsrfToken);

						}
					}

				});

		// LoginServiceAsync loginService = GWT.create(LoginService.class);
		// ((HasRpcToken) loginService).setRpcToken(xsrfToken);

		// fbCore.login(new AsyncCallback<JavaScriptObject> () {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// //
		//
		// }
		//
		// @Override
		// public void onSuccess(JavaScriptObject result) {
		// //
		//
		// }
		//
		// });

		// loginService.login(GWT.getHostPageBaseURL(),
		// new AsyncCallback<LoginInfo>() {
		//
		// @Override
		// public void onFailure(Throwable error) {
		// handleError(error);
		//
		// }
		//
		// @Override
		// public void onSuccess(LoginInfo result) {
		// loginInfo = result;
		// if (loginInfo.isLoggedIn()) {
		// presenter.setMyEmail(loginInfo.getEmailAddress());
		// presenter.initializeOnlineGame(xsrfToken);
		// graphics.setSignInLink(goMessages.signOut(),
		// loginInfo.getLogoutUrl());
		// } else {
		// LoginPanel lg = new LoginPanel(loginInfo
		// .getLoginUrl(), goMessages, graphics);
		// graphics.setBtnsEnabled(false);
		// graphics.setSignInLink(goMessages.signIn()
		// .asString(), loginInfo.getLoginUrl());
		// lg.center();
		// lg.show();
		// }
		//
		// }
		//
		// });

	}

	protected void initialize(final XsrfToken xsrfToken) {
		fbCore.api("/me", new AsyncCallback<JavaScriptObject>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(JavaScriptObject result) {
				JSOModel jsm = result.cast();
				myFbInfo = new FbUserInfo();
				myFbInfo.setId(jsm.get("id"));
				myFbInfo.setName(jsm.get("name"));
				myFbInfo.setUserName(jsm.get("username"));
				presenter.setMyNameAndPic(myFbInfo.getName(), myFbInfo.getPictureUrl());
				presenter.setMyId(myFbInfo.getId());
				presenter.initializeOnlineGame(xsrfToken);
			}

		});
	}

}
