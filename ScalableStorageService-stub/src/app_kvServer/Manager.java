package app_kvServer;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Manager {
	
	/*public static boolean isServeractivecheck() {
		return serveractivecheck;
	}
	public static void setServeractivecheck(boolean serveractivecheck) {
		Manager.serveractivecheck = serveractivecheck;
	}*/

	public static BigInteger hash(String node) throws NoSuchAlgorithmException,UnsupportedEncodingException
    {
    	MessageDigest md5 = MessageDigest.getInstance("MD5");
    	byte[] checksum = md5.digest(node.getBytes("UTF-8"));
    	return new BigInteger(1, checksum);
    }
}
