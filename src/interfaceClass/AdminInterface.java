package interfaceClass;

import java.io.IOException;
import java.rmi.Remote;
/**
 *
 * @author ypandya
 */
public interface AdminInterface extends Remote {

	/**
	 * @param userName
	 * @param password
	 * @param ipAddress
	 * @return
	 * @throws IOException
	 */
	public String getPlayerStatus(String userName, String password, String ipAddress) throws IOException;

}
