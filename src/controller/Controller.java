/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
//References:
//https://systembash.com/a-simple-java-udp-server-and-udp-client/
//https://www.tutorialspoint.com/java_rmi/java_rmi_introduction.htm
//https://www.javatpoint.com/RMI
//https://www.geeksforgeeks.org/multithreading-in-java/
//https://www.geeksforgeeks.org/synchronized-in-java/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import interfaces.AdminInterface;
import interfaces.PlayerInterface;
import server.Asia;
import server.NorthAmerica;
import server.Europe;
import model.NAData;
import model.ASData;
import model.EUData;
/**
 *
 * @author ypandya
 */
public class Controller extends UnicastRemoteObject implements PlayerInterface, AdminInterface {

	/**
	 * This is the Controller class that handles data manipulation
	 * between client and server/ server models
	 */
	public String IP;
	public String returnData;
	private static Logger logger;
	private static final long serialVersionUID = 1L;
	public NAData naData;
	public ASData asData;
	public EUData euData;
	
	/**
	 * Controller constructor
	 * @param IP
	 * @throws RemoteException
	 */
	public Controller(String IP) throws RemoteException {
		super();
		naData = new NAData();
		asData = new ASData();
		euData = new EUData();
		this.IP = IP;

		if (IP.equals("EU")) 
		{
			Europe europe = new Europe(this);
			Runnable eu = () -> {europe.serverConnection(8880);};
			Thread t1 = new Thread(eu);
			t1.start();
		} 
		else if (IP.equals("AS")) 
		{
			Asia asia = new Asia(this);
			Runnable as = () -> {asia.serverConnection(8881);};
			Thread t2 = new Thread(as);
			t2.start();
		} 
		else if (IP.equals("NA")) 
		{
			NorthAmerica northamerica = new NorthAmerica(this);
			Runnable na = () -> {northamerica.serverConnection(8882);};
			Thread t3 = new Thread(na);
			t3.start();
		} 
		else {
			System.out.println("Something went wrong while starting the server.");
		}
	}

	/**
	 * This class overriden method is used to create the player account
	 * @param firstName firstname of the player
	 * @param lastName lastname of the player
	 * @param age age of the player
	 * @param userName username of the player
	 * @param password password of the player
	 * @param ipAddress ip of the player
	 * @return String containing success or error message
	 * @throws IOException
	 */
	@Override
	public String createPlayerAccount(String firstName, String lastName, String age, String userName, String password, String ipAddress) throws IOException {
		String output = "";
		if (ipAddress.startsWith("132")) {
			output = naData.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
		}
		else if (ipAddress.startsWith("93")) {
			output = euData.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
		}
		else if (ipAddress.startsWith("182")) {
			output = asData.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
		}
		return output;	
	}
	
	/**
	 * This class overriden method is used to sign in into the player account
	 * @param userName username of the player
	 * @param password password of the player
	 * @param ipAddress ip of the player
	 * @return String containing success or error message
	 * @throws IOException
	 */
	@Override
	public String playerSignIn(String userName, String password, String ipAddress) throws IOException {
		String output = "";
		if (ipAddress.startsWith("132")) {
			output = naData.playerSignIn(userName, password, ipAddress);
		}
		else if (ipAddress.startsWith("93")) {
			output = euData.playerSignIn(userName, password, ipAddress);
		}
		else if (ipAddress.startsWith("182")) {
			output = asData.playerSignIn(userName, password, ipAddress);
		}
		return output;
	}
	
	/**
	 * This class overriden method is used to sign out from the player account
	 * @param userName username of the player
	 * @param ipAddress ip of the player
	 * @return String containing success or error message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public String playerSignOut(String userName, String ipAddress) throws IOException, InterruptedException {
		String output = "";
		if (ipAddress.startsWith("132")) {
			output = naData.playerSignOut(userName, ipAddress);
		}
		else if (ipAddress.startsWith("93")) {
			output = euData.playerSignOut(userName, ipAddress);
		}
		else if (ipAddress.startsWith("182")) {
			output = asData.playerSignOut(userName, ipAddress);
		}
		return output;
	}
	
	/**
	 * This class overriden method is used to get the status of all the players
	 * @param userName username of the admin
	 * @param password password of the admin
	 * @param ipAddress ip of the admin
	 * @return String containing number of online and offline players
	 * @throws IOException
	 */
	@Override
	public String getPlayerStatus(String userName, String password, String ipAddress) throws IOException {
		String output = "";
		if (ipAddress.startsWith("132")) {
			output += naData.getPlayerStatus(userName, password, ipAddress);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 8880);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 8881);
		}
		else if (ipAddress.startsWith("93")) {
			output += euData.getPlayerStatus(userName, password, ipAddress);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 8881);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 8882);
		}
		else if (ipAddress.startsWith("182")) {
			output += asData.getPlayerStatus(userName, password, ipAddress);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 8880);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 8882);
		}
		return output;
	}
	
	/**
	 * This method is used to communicate with the other servers to get the player status
	 * counts for admin user
	 * @param username username of the admin
	 * @param password password of the admin
	 * @param ip ip of the admin
	 * @param port port of the other servers that are running on
	 * @return String containing number of online and offline players from port specific server
	 */
	public String DatafromOtherIP(String username, String password, String ip, int port) {

		DatagramSocket ds = null;
		byte[] b = new byte[65535];
		try {
			String request = username + "," + password + "," + ip;
			ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(
				request.getBytes(), request.getBytes().length,
				InetAddress.getByName("localhost"), port
			);
			ds.send(dp);
			DatagramPacket dp1 = new DatagramPacket(b, b.length);
			ds.receive(dp1);
			String returnData = new String(dp1.getData());
			return returnData.trim();
		} catch (UnknownHostException e) {
			logger.info(e.getMessage());
		} catch (SocketException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		} finally {
			ds.close();
		}
		return "";
	}
	
}
