package model;

/**
 * @author ypandya
 *
 */
public class Administrator {
	
	/**
	 * 
	 */
	public String userName;
	public String password;
	public String ipAddress;
	
	/**
	 * @param username
	 * @param password
	 * @param ipAddress
	 */
	Administrator(String username, String password, String ipAddress){
		this.userName = username;
		this.password = password;
		this.ipAddress = ipAddress;
	}

	/**
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
}