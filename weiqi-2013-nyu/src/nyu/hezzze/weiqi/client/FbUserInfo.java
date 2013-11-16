package nyu.hezzze.weiqi.client;

import com.google.gwt.view.client.ProvidesKey;


public class FbUserInfo implements Comparable<FbUserInfo> {
	
	public static final ProvidesKey<FbUserInfo> KEY_PROVIDER = new ProvidesKey<FbUserInfo>() {

		@Override
		public Object getKey(FbUserInfo item) {
			return item == null ? null : item.getId();
		};

	};

	private String id;
	private String name;
	private String userName;
	boolean hasGameWithMe;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pictureUrl
	 */
	public String getPictureUrl() {
		return "https://graph.facebook.com/" + id + "/picture";
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int compareTo(FbUserInfo other) {
		int myKey = hasGameWithMe ? 1 : 0;
		int otherKey = other.hasGameWithMe ? 1 : 0;
		if (myKey > otherKey) {
			return -1;
		} else if (myKey < otherKey) {
			return 1;
		}
		return 0;	
	}

}
