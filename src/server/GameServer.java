package server;
//References:
//https://systembash.com/a-simple-java-udp-server-and-udp-client/
//https://www.tutorialspoint.com/java_rmi/java_rmi_introduction.htm
//https://www.javatpoint.com/RMI
//https://www.geeksforgeeks.org/multithreading-in-java/
//https://www.geeksforgeeks.org/synchronized-in-java/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import controller.Controller;
/**
 *
 * @author ypandya
 */
public class GameServer {
	
	/**
	 * This is the GameServer class
	 */
	
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
		
		Registry registry1 = LocateRegistry.createRegistry(9990);
		registry1.bind("North America", northamerica);
		
		Registry registry2 = LocateRegistry.createRegistry(9991);
		registry2.bind("Europe", europe);
		
		Registry registry3 = LocateRegistry.createRegistry(9992);
		registry3.bind("Asia", asia);
				
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
			while (line != null) {
				String[] listParts = line.split(",");
				String firstName = listParts[0];
				String lastName = listParts[1];
				String age = listParts[2];
				String userName = listParts[3];
				String password = listParts[4];
				String ipAddress = listParts[5];
				
				if (ipAddress.startsWith("132")) {
					northamerica.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
				} else if (ipAddress.startsWith("93")) {
					europe.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
				} else if (ipAddress.startsWith("182")) {
					asia.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
				}
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
	
}