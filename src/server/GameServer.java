package server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import controller.Controller;
/**
 *
 * @author ypandya
 */
public class GameServer {
	
	/**
	 * This is the GameServer class
	 */
	private static Logger logger;
	
	/**
	 * main method to run all the servers
	 * @param args args for main function
	 * @throws AlreadyBoundException exception of already bound with server 
	 * @throws RemoteException remote exception
	 */
	public static void main(String args[]) throws AlreadyBoundException, RemoteException {
		
		buildLogDirectory("./logs");
		
		Controller europe = new Controller("EU");
		Controller northamerica = new Controller("NA");
		Controller asia = new Controller("AS");
		
		Registry registry = LocateRegistry.createRegistry(8080);

		registry.bind("North America", northamerica);
		registry.bind("Asia", asia);
		registry.bind("Europe", europe);
				
		System.out.println("Server(s) are Started");
		
		loadData(europe, northamerica, asia);
		
		System.out.println("Initial data loaded into server");
	}
	
	/**
	 * This method is used to load the initial player data
	 * @param europe europe controller object 
	 * @param northamerica northamerica controller object 
	 * @param asia asia controller object 
	 */
	static void loadData(Controller europe, Controller northamerica, Controller asia) {
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("src/data.txt"));
			String line = reader.readLine();
			String response = "";
			while (line != null) {
				String[] listParts = line.split(",");
				String firstName = listParts[0];
				String lastName = listParts[1];
				String age = listParts[2];
				String userName = listParts[3];
				String password = listParts[4];
				String ipAddress = listParts[5];
				
				addLog("logs/" + userName  + ".txt", userName);
				logger.info("IP : " + ipAddress + ", username : " + userName + ", start createPlayerAccount() operation.");
				if (ipAddress.startsWith("132")) {
					response = northamerica.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
				} else if (ipAddress.startsWith("93")) {
					response = europe.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
				} else if (ipAddress.startsWith("182")) {
					response = asia.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
				}
				logger.info("IP : " + ipAddress + ", username : " + userName + ", Result createPlayerAccount() : " + response);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to create logs directory to store the logs
	 * @param path location of the logs folder
	 */
	public static void buildLogDirectory(String path) {
		File outputDir = new File(path);
		if (!outputDir.exists()) {
			outputDir.mkdir();
		}
	}
	
	/**
	 * This method is used to set/update logger
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