package nyu.hezzze.weiqi.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface GoImages extends ClientBundle {

	/**
	 * Image Source: <br>
	 * http://en.wikipedia.org/wiki/File:Realistic_White_Go_Stone.svg
	 * @return
	 */
	@Source("white_stone.png")
	ImageResource whiteStone();
	
	/**
	 * Image Source: <br>
	 * http://en.wikipedia.org/wiki/File:Realistic_Go_Stone.svg
	 */
	@Source("black_stone.png")
	ImageResource blackStone();
	
	@Source("blank.png")
	ImageResource blank();
	
}
