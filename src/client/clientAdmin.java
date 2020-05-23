package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.FileHandler;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import interfaceClass.AdminInterface;

/**
 *
 * @author ypandya
 */
public class clientAdmin {
	
	/**
	 * 
	 */
	static AdminInterface adminObj;
	static Registry registry;
	static BufferedReader br;
	private static Logger logger;
	
	/**
	 * @param args
	 * @throws NotBoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws NotBoundException, IOException, InterruptedException {

		registry = LocateRegistry.getRegistry(8080);
		br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			adminObj = null;
			System.out.println("Distributed Player Status System");
			System.out.println("================================");
			System.out.println("Admin Options : \n");
			System.out.println("1 : Get Player status");
			System.out.println("2 : Exit\n");
			System.out.print("Select : ");
			String choice = br.readLine().trim();
			if (choice.equals("1")){
				getPlayerStatus();
			}
			else if (choice.equals("2")){
				break;
			}
			else {
				System.out.println("\nPlease select valid option\n");
				continue;
			}
		}
	}
		
	/**
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }
	
	/**
	 * @return
	 * @throws IOException
	 */
	public static boolean exitCheck() throws IOException {
		System.out.println("\n----- Invalid Input -----\n");
		System.out.println("Please select below options : ");
		System.out.println("1 : Do you want to re-enter");
		System.out.println("2 : exit");
		String str = br.readLine().trim();
		if (str.equals("exit") || str.equals("2")) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param ip
	 * @return
	 */
	public static boolean ipCheck(String ip) {
		if (ip.startsWith("132") || ip.startsWith("182")) {
			if (ip.length()==15) {
				if ((ip.substring(3,4).equals(".")) && (ip.substring(7,8).equals(".")) && (ip.substring(11, 12).equals("."))) {
					return true;
				}
			}
		}
		else if (ip.startsWith("93")) {
			if (ip.length()==14) {
				if ((ip.substring(2,3).equals(".")) && (ip.substring(6,7).equals(".")) && (ip.substring(10, 11).equals("."))) { 
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NotBoundException
	 */
	public static void getPlayerStatus() throws IOException, InterruptedException, NotBoundException{
		boolean is_info_collected = false;
		String username = "";
		String password = "";
		String ip = "";
		boolean returnMenu = false;
		while (!returnMenu) {
			if (!returnMenu) {
				while (isNullOrEmpty(username)) {
					System.out.print("Enter username : ");
					username = br.readLine().trim();
					if (isNullOrEmpty(username) && exitCheck()) {
						returnMenu = true;
						break;
					}
				}
			}
			if (!returnMenu) {
				while (isNullOrEmpty(password)) {
					System.out.print("Enter password : ");
					password = br.readLine().trim();
					if (isNullOrEmpty(password) && exitCheck()) {
						returnMenu = true;
						break;
					}
				}
			}
			if (!returnMenu) {
				while (!ipCheck(ip) && !returnMenu) {
					System.out.print("Enter ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
					ip = br.readLine().trim();
					if (ipCheck(ip)) {
						returnMenu = true;
						break;
					}
					else {
						if (exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!isNullOrEmpty(username) && !isNullOrEmpty(password) && ipCheck(ip)) {
				is_info_collected = true;
			}
		}
		if (is_info_collected && ip!=null) {
			addLog("logs/" + username + ".txt", username);
			createAdminObject(ip);
			logger.info("IP : " + ip + ", username : " + username + ", start getPlayerStatus() operation.");
			String response = adminObj.getPlayerStatus(username, password, ip);
			logger.info("IP : " + ip + ", username : " + username + ", Result getPlayerStatus() : " + response);
			System.out.println(response);
		}
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

	
	/**
	 * @param ip
	 * @throws AccessException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static void createAdminObject(String ip)
			throws AccessException, RemoteException, NotBoundException {
		if (ip.startsWith("132")) {
			adminObj = (AdminInterface) registry.lookup("North America");
		} 
		else if (ip.startsWith("93")) {
			adminObj = (AdminInterface) registry.lookup("Europe");
		} 
		else if (ip.startsWith("182")) {
			adminObj = (AdminInterface) registry.lookup("Asia");
		}
	}
}
	
