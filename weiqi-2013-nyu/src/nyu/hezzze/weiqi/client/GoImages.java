package nyu.hezzze.weiqi.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * The image resource bundle for the game <br>
 * The first three images are from wikipedia based which the rest of the images
 * are derived
 * 
 * @author hezzze
 * 
 */
public interface GoImages extends ClientBundle {

	/**
	 * Image Source: <br>
	 * http://en.wikipedia.org/wiki/File:Realistic_White_Go_Stone.svg
	 * 
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

	/**
	 * Image Source: <br>
	 * http://en.wikipedia.org/wiki/File:Blank_Go_board.svg
	 * 
	 * @return
	 */
	@Source("blank.png")
	ImageResource blank();

	@Source("blank_dot.png")
	ImageResource blankDotted();

	@Source("blank_N.png")
	ImageResource blankNorthEdge();

	@Source("blank_S.png")
	ImageResource blankSouthEdge();

	@Source("blank_W.png")
	ImageResource blankWestEdge();

	@Source("blank_E.png")
	ImageResource blankEastEdge();

	@Source("blank_NW.png")
	ImageResource blankNorthWestCorner();

	@Source("blank_NE.png")
	ImageResource blankNorthEastCorner();

	@Source("blank_SW.png")
	ImageResource blankSouthWestCorner();

	@Source("blank_SE.png")
	ImageResource blankSouthEastCorner();

	@Source("black.png")
	ImageResource blackPlayer();

	@Source("white.png")
	ImageResource whitePlayer();

	@Source("logo.png")
	ImageResource gameLogo();

}
