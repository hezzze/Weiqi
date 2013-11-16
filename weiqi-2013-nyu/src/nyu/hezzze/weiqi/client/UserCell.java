package nyu.hezzze.weiqi.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtfb.client.JSOModel;

/**
 * The cell object that is responsible for rendering the information of a game
 * info object in a cell containing widget
 * 
 * @author hezzze
 * 
 */
public class UserCell extends AbstractCell<FbUserInfo> {

	private static final String GREEN = "#87C67C";
	private static final String BLUE = "#3661A0";

	private Presenter presenter;

	public UserCell(final Presenter presenter) {
		super("click");
		this.presenter = presenter;

	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			final FbUserInfo userInfo, SafeHtmlBuilder sb) {
		if (userInfo == null) {
			return;
		}

		SafeHtml rendered = cell(userInfo.getPictureUrl(), userInfo.getName(),
				userInfo.hasGameWithMe ? "Go!" : "Invite!",
				userInfo.hasGameWithMe ? GREEN
						: BLUE);
		sb.append(rendered);

	}

	@Override
	public void onBrowserEvent(Context context, Element parent,
			FbUserInfo userInfo, NativeEvent event,
			ValueUpdater<FbUserInfo> valueUpdater) {
		super.onBrowserEvent(context, parent, userInfo, event, valueUpdater);

		if ("click".equals(event.getType())) {
			EventTarget eventTarget = event.getEventTarget();
			if (!Element.is(eventTarget)) {
				return;
			}

			if (parent.getElementsByTagName("button").getItem(0)
					.isOrHasChild(Element.as(eventTarget))) {
				if (userInfo.hasGameWithMe) {
					presenter.loadGame(presenter.myInfo.getGameIdWith(userInfo
							.getId()));	
				} else {
					makeFbRequest(userInfo);
				}
				presenter.getGraphics().hideFriendList();
				
			}
		}

	}

	private void makeFbRequest(final FbUserInfo userInfo) {
		JSOModel params = JSOModel.create();
		params.set("method", "apprequests");
		params.set("message", "Let's Go!");
		params.set("to", userInfo.getId());
		presenter.fbCore.ui(params, new AsyncCallback<JavaScriptObject>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(JavaScriptObject result) {
				presenter.startGame(userInfo.getId());
			}

		});
	}

	private SafeHtml cell(String imageHtml, String name, String buttonText,
			String labelColor) {
		SafeHtmlBuilder bd = new SafeHtmlBuilder();
		String html = getHtmlTemplate();
		
		html = html.replaceAll("\\{0\\}", imageHtml);
		html = html.replaceAll("\\{1\\}", name);
		html = html.replaceAll("\\{2\\}", buttonText);
		html = html.replaceAll("\\{3\\}", labelColor);

		bd.appendHtmlConstant(html);
		return bd.toSafeHtml();

	}

	private native String getHtmlTemplate() /*-{
		var html = "<div> \
			<span style='border-collapse: collapse; background-color: #E0EAF1; border: 1px solid #c5c5c5; \
      						border-radius: 5px; display: inline-block' > \
				<table> \
					<tbody> \
						<tr> \
							<td style='padding: 0px 5px; border-right: 1px solid #e5e5e5'> \
								<image src='{0}'></image> \
							</td> \
	      					<td width='230'><div style='color: #FFFFF5; border: 1px solid #cccccc; border-radius: 10px; \
	      					   padding: 10px; font: 15px arial, sans-serif; background-color: {3}; \
	      					   text-align:center'>{1}</div> \
	      					</td> \
	      					<td><button class='goBtn'>{2}</button></td> \
	      				</tr> \
	      			</tbody> \
	      		</table> \
	      	</span> \
    	</div>";
		return html;
	}-*/;

}
