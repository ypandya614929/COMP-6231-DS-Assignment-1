package model;

/**
 * @author ypandya
 *
 */
public class Player {
	
	/**
	 * 
	 */
	public String firstName;
	public String lastName;
	public String age;
	public String userName;
	public String password;
	public String ipAddress;
	public boolean status;
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param userName
	 * @param password
	 * @param ipAddress
	 */
	Player(String firstName, String lastName, String age, String userName, String password, String ipAddress){
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.userName = userName;
		this.password = password;
		this.ipAddress = ipAddress;
		this.status = false;	
	}
	
	/**
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	/**
	 * @return
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}