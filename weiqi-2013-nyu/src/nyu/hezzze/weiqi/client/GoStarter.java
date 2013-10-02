package nyu.hezzze.weiqi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * The Entry class of the game, the game will start immediately once the page is
 * fully loaded
 * 
 * @author hezzze
 * 
 */
public class GoStarter implements EntryPoint {

	@Override
	public void onModuleLoad() {

		Graphics graphics = new Graphics();
		RootLayoutPanel.get().add(graphics);
	}

}
