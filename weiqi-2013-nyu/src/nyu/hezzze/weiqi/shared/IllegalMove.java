package nyu.hezzze.weiqi.shared;


/**
 * Exception class for invalid 
 * moves by the players
 * 
 * @author hezzze
 *
 */
public class IllegalMove extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IllegalMove(String msg) {
		super(msg);
	}

}
