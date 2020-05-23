/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;
import interfaceClass.PlayerInterface;
import interfaceClass.AdminInterface;
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
	 * 
	 */
	public String IP;
	public String returnData;
	private static Logger logger;
	private static final long serialVersionUID = 1L;
	public NAData naData;
	public ASData asData;
	public EUData euData;
	
	/**
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
			Runnable eu = () -> {europe.serverConnection(9990);};
			Thread t1 = new Thread(eu);
			t1.start();
		} 
		else if (IP.equals("AS")) 
		{
			Asia asia = new Asia(this);
			Runnable as = () -> {asia.serverConnection(9991);};
			Thread t2 = new Thread(as);
			t2.start();
		} 
		else if (IP.equals("NA")) 
		{
			NorthAmerica northamerica = new NorthAmerica(this);
			Runnable na = () -> {northamerica.serverConnection(9992);};
			Thread t3 = new Thread(na);
			t3.start();
		} 
		else {
			System.out.println("Something went wrong while starting the server.");
		}
	}

	/**
	 *
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
	 *
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
	 *
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
	 *
	 */
	@Override
	public String getPlayerStatus(String userName, String password, String ipAddress) throws IOException {
		String output = "";
		if (ipAddress.startsWith("132")) {
			output += naData.getPlayerStatus(userName, password, ipAddress);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 9990);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 9991);
		}
		else if (ipAddress.startsWith("93")) {
			output += euData.getPlayerStatus(userName, password, ipAddress);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 9991);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 9992);
		}
		else if (ipAddress.startsWith("182")) {
			output += asData.getPlayerStatus(userName, password, ipAddress);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 9990);
			output += " ";
			output += DatafromOtherIP(userName, password, ipAddress, 9992);
		}
		return output;
	}
	
	/**
	 * @param username
	 * @param password
	 * @param ip
	 * @param port
	 * @return
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
