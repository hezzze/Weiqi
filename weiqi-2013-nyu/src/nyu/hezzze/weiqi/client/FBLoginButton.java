package nyu.hezzze.weiqi.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class FBLoginButton extends Widget {
	public FBLoginButton () {
		Element element = DOM.createDiv();
		element.setClassName("fb-login-button");	
		element.setAttribute("autologoutlink", "true");
		setElement(element);
	}
}
