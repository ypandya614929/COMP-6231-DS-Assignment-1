package interfaceClass;

import java.io.IOException;
import java.rmi.Remote;
/**
 *
 * @author ypandya
 */
public interface PlayerInterface extends Remote {

	/**
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param userName
	 * @param password
	 * @param ipAddress
	 * @return
	 * @throws IOException
	 */
	public String createPlayerAccount(String firstName, String lastName, String age, String userName, String password, String ipAddress) throws IOException;

	/**
	 * @param userName
	 * @param password
	 * @param ipAddress
	 * @return
	 * @throws IOException
	 */
	public String playerSignIn(String userName, String password, String ipAddress) throws IOException;

	/**
	 * @param userName
	 * @param ipAddress
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String playerSignOut(String userName, String ipAddress) throws IOException, InterruptedException;

}
