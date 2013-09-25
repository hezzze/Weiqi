package nyu.hezzze.weiqi.shared;

/**
 * This exception class will be thrown if the 
 * user intended to make a move or pass after 
 * game is over
 * 
 * @author hezzze
 *
 */
public class GameOverException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 287151534705875075L;

	public GameOverException(String msg) {
		super(msg);
	}
}
