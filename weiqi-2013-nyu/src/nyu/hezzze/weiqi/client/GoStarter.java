package nyu.hezzze.weiqi.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class GoStarter implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		Graphics graphics = new Graphics();
		RootLayoutPanel.get().add(graphics);
	}
	
}
