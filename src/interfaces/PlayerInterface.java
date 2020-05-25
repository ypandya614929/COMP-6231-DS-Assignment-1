package interfaces;

import java.io.IOException;
import java.rmi.Remote;
/**
 *
 * @author ypandya
 */
public interface PlayerInterface extends Remote {

	/**
	 * This interface method is used to create the player account
	 * @param firstName firstname of the player
	 * @param lastName lastname of the player
	 * @param age age of the player
	 * @param userName username of the player
	 * @param password password of the player
	 * @param ipAddress ip of the player
	 * @return String containing success or error message
	 * @throws IOException
	 */
	public String createPlayerAccount(String firstName, String lastName, String age, String userName, String password, String ipAddress) throws IOException;

	/**
	 * This interface method is used to sign in into the player account
	 * @param userName username of the player
	 * @param password password of the player
	 * @param ipAddress ip of the player
	 * @return String containing success or error message
	 * @throws IOException
	 */
	public String playerSignIn(String userName, String password, String ipAddress) throws IOException;

	/**
	 * This interface method is used to sign out from the player account
	 * @param userName username of the player
	 * @param ipAddress ip of the player
	 * @return String containing success or error message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String playerSignOut(String userName, String ipAddress) throws IOException, InterruptedException;

}
