/*
 * Created Mar 12, 2012
 */
package ltg.es.wallcology.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ltg.es.wallcology.notifier.lib.ConfFile;
import ltg.es.wallcology.notifier.lib.NetworkController;


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
		if (args.length == 1)
			 configFile = args[0];
		Notifier.getInstance().start(configFile);
	}
	
	
	private void start(String configFile) {
		config = new ConfFile(configFile);
		if(!config.parse())
			System.exit(1);
//		db = new PSPersistence(this.getClass().getSimpleName());
//		db.restore();
		net = new NetworkController(ConfFile.getProperty("XMPP_USERNAME"),
				ConfFile.getProperty("XMPP_PASSWORD"));
		if (!net.connect()) 
			Thread.currentThread().interrupt();
		if(!Thread.currentThread().isInterrupted()) {
			log.info("Notifier agent STARTED");
			if (!net.joinRoom(ConfFile.getProperty("XMPP_ROOM_NAME")))
					Thread.currentThread().interrupt();
			log.info("Joined chat room " + ConfFile.getProperty("XMPP_ROOM_NAME"));
			
			net.listen();
		}
	}
	
	
	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private Notifier() {
		// Don't write ANY instructions here! 
		// (unless they DON'T use the configuration file for something) 
	}
	
	/**
	 * SingletonHolder is loaded on the first execution of <code>Singleton.getInstance()</code> 
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder { 
		private static final Notifier INSTANCE = new Notifier();
	}


	public static Notifier getInstance() {
		return SingletonHolder.INSTANCE;
	}
	

}
