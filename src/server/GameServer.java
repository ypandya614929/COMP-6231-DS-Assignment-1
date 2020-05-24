package server;
import java.io.File;
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
		
		Registry registry = LocateRegistry.createRegistry(8080);

		registry.bind("North America", northamerica);
		registry.bind("Asia", asia);
		registry.bind("Europe", europe);
				
		System.out.println("Server(s) are Started");
		
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