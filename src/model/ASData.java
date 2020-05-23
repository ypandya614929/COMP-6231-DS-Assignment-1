package model;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author ypandya
 *
 */
public class ASData {
	/**
	 * 
	 */
	HashMap<String, HashMap<String, Administrator>> adminserverData;
	HashMap<String, HashMap<String, Player>> playerserverData;
	String asiaIp;
	private static Logger logger;

	/**
	 * @return
	 */
	public HashMap<String, HashMap<String, Administrator>> getAdminserverData() {
		return adminserverData;
	}

	/**
	 * @param serverData
	 */
	public void setPlayerserverData(HashMap<String, HashMap<String, Player>> serverData) {
		this.playerserverData = serverData;
	}
	
	/**
	 * @return
	 */
	public HashMap<String, HashMap<String, Player>> getPlayerserverData() {
		return playerserverData;
	}

	/**
	 * @param serverData
	 */
	public void setAdminserverData(HashMap<String, HashMap<String, Administrator>> serverData) {
		this.adminserverData = serverData;
	}

	/**
	 * @return
	 */
	public String getasiaIp() {
		return asiaIp;
	}

	/**
	 * @param asiaIp
	 */
	public void setasiaIp(String asiaIp) {
		this.asiaIp = asiaIp;
	}

	public ASData() {
		adminserverData = new HashMap<>();
		playerserverData = new HashMap<>();
		addLog("logs/AS.txt", "AS");
	}

	/**
	 * @param username
	 * @param password
	 * @param ip
	 * @return
	 */
	synchronized public String getPlayerStatus(String username, String password, String ip) {
		logger.info("IP : " + ip + ", username : " + username + ", start getPlayerStatus() operation.");
		Administrator adminObj;
		String key = Character.toString(username.charAt(0));
		int onlineCount = 0;
		int offlineCount = 0;
		if (adminserverData.containsKey(key)) {
			HashMap<String,Administrator> temp = adminserverData.get(key);
			if (temp.containsKey(username)) {
				adminObj = temp.get(username);
			} else {
				adminObj = new Administrator(username, password, ip);
			}
		} else {
			adminObj = new Administrator(username, password, ip);
			HashMap<String,Administrator> temp1 = new HashMap<>();
			temp1.put(username, adminObj);
			adminserverData.put(key, temp1);
		}
		adminObj.setPassword(password);
		if (adminObj.userName.equals("admin") && adminObj.password.equals("admin")) {
			for (Entry <String, HashMap<String, Player>> outerHashmap : playerserverData.entrySet()) {
				for (Entry <String, Player> innerHashmap : outerHashmap.getValue().entrySet()) {
					if (innerHashmap.getValue() != null) {
						Player playerObj = innerHashmap.getValue();
						if (playerObj.isStatus()) {
							onlineCount++;
						} else {
							offlineCount++;
						}
					}
				}
			}
			logger.info("IP : " + ip + ", username : " + username + ", Result getPlayerStatus() : " + "Asian : "+ onlineCount + " online , " + offlineCount + " offline. ");
			return "Asian : "+ onlineCount + " online , " + offlineCount + " offline. ";
		}
		logger.info("IP : " + ip + ", username : " + username + ", Result getPlayerStatus() : invalid username or password");
		return "Invalid username or password";
	}
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param userName
	 * @param password
	 * @param ipAddress
	 * @return
	 */
	synchronized public String createPlayerAccount(String firstName, String lastName, String age, String userName, String password, String ipAddress) {
		logger.info("IP : " + ipAddress + ", username : " + userName + ", start createPlayerAccount() operation.");
		Player playerObj;
		String key = Character.toString(userName.charAt(0));
		if (playerserverData.containsKey(key)) {
			HashMap<String,Player> temp = playerserverData.get(key);
			if (temp.containsKey(userName)) {
				playerObj = temp.get(userName);
				logger.info("IP : " + ipAddress + ", username : " + userName + ", Result createPlayerAccount() : Player already exists");
				return "Player already exists";
			} else {
				playerObj = new Player(firstName, lastName, age, userName, password, ipAddress);
				temp.put(userName, playerObj);
				logger.info("IP : " + ipAddress + ", username : " + userName + ", Result createPlayerAccount() : Player created successfully");
				return "Player created successfully";
			}
		} else {
			playerObj = new Player(firstName, lastName, age, userName, password, ipAddress);
			HashMap<String,Player> temp1 = new HashMap<>();
			temp1.put(userName, playerObj);
			playerserverData.put(key, temp1);
			logger.info("IP : " + ipAddress + ", username : " + userName + ", Result createPlayerAccount() : Player created successfully");
			return "Player created successfully";
		}
	}
	
	/**
	 * @param userName
	 * @param password
	 * @param ipAddress
	 * @return
	 */
	synchronized public String playerSignIn(String userName, String password, String ipAddress) {
		logger.info("IP : " + ipAddress + ", username : " + userName + ", start playerSignIn() operation.");
		Player playerObj;
		String key = Character.toString(userName.charAt(0));
		if (playerserverData.containsKey(key)) {
			HashMap<String,Player> temp = playerserverData.get(key);
			if (temp.containsKey(userName)) {
				playerObj = temp.get(userName);
				if (playerObj.userName.equals(userName) && playerObj.password.equals(password) && playerObj.ipAddress.equals(ipAddress)) {
					if (playerObj.isStatus()) {
						logger.info("IP : " + ipAddress + ", username : " + userName + ", Result playerSignIn() : Player already signed in");
						return "Player already signed in";
					} 
					else {
						playerObj.setStatus(true);
						logger.info("IP : " + ipAddress + ", username : " + userName + ", Result playerSignIn() : Player sign in successfully");
						return "Player sign in successfully";
					}
				}
				playerObj.setStatus(false);
				logger.info("IP : " + ipAddress + ", username : " + userName + ", Result playerSignIn() : Invalid password or IP address");
				return "Invalid password or IP address";
			}
		}
		logger.info("IP : " + ipAddress + ", username : " + userName + ", Result playerSignIn() : Player doesn't exists");
		return "Player doesn't exists";
	}
	
	/**
	 * @param userName
	 * @param ipAddress
	 * @return
	 */
	synchronized public String playerSignOut(String userName, String ipAddress) {
		logger.info("IP : " + ipAddress + ", username : " + userName + ", start playerSignOut() operation.");
		Player playerObj;
		String key = Character.toString(userName.charAt(0));
		if (playerserverData.containsKey(key)) {
			HashMap<String,Player> temp = playerserverData.get(key);
			if (temp.containsKey(userName)) {
				playerObj = temp.get(userName);
				if (playerObj.userName.equals(userName) && playerObj.ipAddress.equals(ipAddress)) {
					if (!playerObj.isStatus()) {
						logger.info("IP : " + ipAddress + ", username : " + userName + ", Result playerSignOut() : Player is not signed in");
						return "Player is not signed in";
					} else {
						playerObj.setStatus(false);
						logger.info("IP : " + ipAddress + ", username : " + userName + ", Result playerSignOut() : Player sign out successfully");
						return "Player sign out successfully";
					}
				}
				logger.info("IP : " + ipAddress + ", username : " + userName + ", Result playerSignOut() : Invalid IP address");
				return "Invalid IP address";
			}
		}
		logger.info("IP : " + ipAddress + ", username : " + userName + ", Result playerSignOut() : Player doesn't exists");
		return "Player doesn't exists";
	}
	
	/**
	 * @param path
	 * @param key
	 */
	static void addLog(String path, String key) {
		try {
			File f = new File(path);
			String data = "";
			logger = Logger.getLogger(key);
			if(f.exists() && !f.isDirectory()) { 
				data = new String(Files.readAllBytes(Paths.get(path)));
			}
			if (logger.getHandlers().length < 1)
			{	
				try {
					f.delete();
				} catch (Exception e) {}
				logger = Logger.getLogger(key);
				FileHandler fh = new FileHandler(path, true);
				SimpleFormatter ft = new SimpleFormatter();
				fh.setFormatter(ft);
				logger.addHandler(fh);
				logger.setUseParentHandlers(false);
				logger.info(data);
				logger.setUseParentHandlers(true);
				
			}
		} catch (Exception err) {
			logger.info("Unable to create file, please check file permission.");
		}
	}

}
