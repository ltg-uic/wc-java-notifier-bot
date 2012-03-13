/*
 * Created Mar 12, 2012
 */
package ltg.es.wallcology.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ltg.es.wallcology.notifier.components.ConfFile;
import ltg.es.wallcology.notifier.components.NetworkController;


/**
 * Notifier agent for WallCology
 *
 * @author Gugo
 */
public class Notifier {
	
	// Components
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private ConfFile config = null; 
	private NetworkController net = null;
	
	
	public static void main(String[] args) {
		String configFile = null;
		// Get the first (and only) CL argument, that is the confFile path 
		if (args.length == 1)
			 configFile = args[0];
		new Notifier().start(configFile);
	}
	
	
	private void start(String configFile) {
		// Parse the configuration file
		config = new ConfFile(configFile);
		if(!config.parse())
			System.exit(1);
		// Initialize persistency
//		db = new PSPersistence(this.getClass().getSimpleName());
//		db.restore();
		// Connect to the XMPP server
		net = NetworkController.getInstance();
		if (!net.connect()) 
			Thread.currentThread().interrupt();
		// Initialize requests map
		
		// Join chat room and begin listening for incoming messages
		if(!Thread.currentThread().isInterrupted()) {
			log.info("Notifier agent STARTED");
			if (!net.joinRoom(ConfFile.getProperty("XMPP_ROOM_NAME")))
					Thread.currentThread().interrupt();
			log.info("Joined chat room " + ConfFile.getProperty("XMPP_ROOM_NAME"));
			net.listen();
		}
	}	

}
