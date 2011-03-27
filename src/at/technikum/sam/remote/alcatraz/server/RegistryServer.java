/**
 * FH Technikum-Wien,
 * BICSS - Sommersemester 2011
 *
 * Softwarearchitekturen und Middlewaretechnologien
 * Alcatraz - Remote - Projekt
 * Gruppe B2
 *
 *
 * @author Christian Fossati
 * @author Stefan Schramek
 * @author Michael Strobl
 * @author Sebastian Vogel
 * @author Juergen Zornig
 *
 *
 * @date 2011/03/10
 *
 **/
package at.technikum.sam.remote.alcatraz.server;

import at.technikum.sam.remote.alcatraz.commons.Constants;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;


/**
 *
 * TODO: comment
 */
public class RegistryServer implements Constants {

  private static String spreadHost = "localhost";
  private SpreadConnection connection;
  private SpreadGroup group;

  public static void main(String[] args) {
    new RegistryServer();
  }

  public RegistryServer() {
    try {
      System.out.println("Starting up Registry Server instance...");
      RegistryServerImplementation r = new RegistryServerImplementation();

      System.out.println("Joining Spread Server Group...");
      try {
        /* Connect to local spread daemon and get spread group */
        connection = new SpreadConnection();
        connection.connect(InetAddress.getByName(spreadHost),
                0,
                SPREAD_SERVER_GROUP_NAME,
                true,
                true);

        /* Add AdvancedMessageListener Implementation to this Spread connection */
        connection.add(r);
      } catch (SpreadException e) {
        System.err.println("There was an error connecting to the daemon.");
        e.printStackTrace();
        System.exit(1);
      } catch (UnknownHostException e) {
        System.err.println("Can't find the spread daemon on" + spreadHost);
        System.exit(1);
      }

      System.out.println("Creating RMI Registry...");
      java.rmi.registry.LocateRegistry.createRegistry(1099);
      Naming.rebind("rmi://localhost:1099/".concat(RMI_SERVER_SERVICE), r);
      System.out.println("Registry Server up and running. Ready to receive requests...");
    } catch (Exception e) {
      System.out.println("Something went wrong while bringing up server.");
      e.printStackTrace();
    }
    byte buffer[] = new byte[80];
    do {
      try {
        System.in.read(buffer);
        if (buffer[0] == 'j') {
          group = new SpreadGroup();
          group.join(connection, SPREAD_SERVER_GROUP_NAME);
        } else if (buffer[0] == 'l') {
          group.leave();
        }
      } catch (Exception e) {
        System.out.println("Something went wrong while bringing up server.");
        e.printStackTrace();
      }
    } while (buffer[0] != 'q');
  }

}
