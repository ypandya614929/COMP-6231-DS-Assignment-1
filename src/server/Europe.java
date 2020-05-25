package server;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.nio.file.Files;
import java.nio.file.Paths;
import controller.Controller;
/**
 *
 * @author ypandya
 */
public class Europe {

	/**
	 * This is Europe server class
	 */
	Controller controllerObj = null;
	private static Logger logger;
	
	/**
	 * Constructor to set controller object
	 * @param aThis controller object
	 */
	public Europe(Controller aThis) {
		
		this.controllerObj = aThis;
	}

	/**
	 * This method is used to connect with port specific server to retrive player status from
	 * that server
	 * @param port port of the server that is running on
	 */
	public void serverConnection(int port) {
		addLog("logs/EU.txt", "EU");
		logger.info("European Server Started");
		DatagramSocket ds = null;

		while (true) {
			try {
				ds = new DatagramSocket(port);
				byte[] receive = new byte[65535];
				DatagramPacket dp = new DatagramPacket(receive, receive.length);
				ds.receive(dp);
				byte[] data = dp.getData();
				String[] data1 = new String(data).split(",");
				String username = data1[0];
				String password = data1[1];
				String ip = data1[2];
				String temp = "";
				if (username.equals("Admin") && password.equals("Admin")) {
					temp = controllerObj.euData.getPlayerStatus(username, password, ip);
				}
				DatagramPacket dp1 = new DatagramPacket(temp.getBytes(), temp.length(),
						dp.getAddress(), dp.getPort());
				ds.send(dp1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ds.close();
			}

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