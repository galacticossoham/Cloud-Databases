package app_kvEcs;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SSHPublicKeyAuthentication{
	
			
	public static void sshConnection(String hostname, int port, String strategy, int cachesize){
	
			String ip = hostname;
			int port1 = port;
			String strategy1 = strategy;
			int cachesize1 = cachesize;

			File keyfile = new File("/.ssh/id_rsa"); // or "~/.ssh/id_dsa"
			String keyfilePass = "joespass";// will be ignored if not needed

			try
			{
				/* Create a connection instance */

				Connection conn = new Connection(ip);

				/* Now connect */

				conn.connect();

				/* Authenticate */

				boolean isAuthenticated = conn.authenticateWithPublicKey("ECSAdmin", keyfile, keyfilePass);

				if (isAuthenticated == false)
					throw new IOException("Authentication failed.");

				/* Create a session */

				Session sess = conn.openSession();

				sess.execCommand("nohup java -jar <path>/ms3-server.jar "+ port + " ERROR " );

				InputStream stdout = new StreamGobbler(sess.getStdout());

				BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

				System.out.println("Here is some information about the remote host:");

				while (true)
				{
					String line = br.readLine();
					if (line == null)
						break;
					System.out.println(line);
				}

				/* Close this session */
				
				sess.close();

				/* Close the connection */

				conn.close();

			}
			catch (IOException e)
			{
				e.printStackTrace(System.err);
				System.exit(2);
			}
		}
	}



