package server;
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
public class Server {
	
	/**
	 * @param args
	 * @throws AlreadyBoundException
	 * @throws RemoteException
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
	 * @param europe
	 * @param northamerica
	 * @param asia
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
	 * @param path
	 */
	public static void buildLogDirectory(String path) {
		File outputDir = new File(path);
		if (!outputDir.exists()) {
			outputDir.mkdir();
		}
	}
	
}