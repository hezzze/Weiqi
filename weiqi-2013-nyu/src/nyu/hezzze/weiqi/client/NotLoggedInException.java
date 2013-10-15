package nyu.hezzze.weiqi.client;

import java.io.Serializable;

/**
 * The exception is thrown when logging in is 
 * needed for performing certains actions
 * @author hezzze
 *
 */
public class NotLoggedInException extends Exception implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotLoggedInException() {
		super();
	}
	
	public NotLoggedInException(String message) {
		super(message);
	}

}
