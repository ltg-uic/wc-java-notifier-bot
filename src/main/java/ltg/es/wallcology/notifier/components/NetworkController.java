/*
 * Created Jun 15, 2010
 */
package ltg.es.wallcology.notifier.components;



import ltg.es.wallcology.notifier.components.support.IPacketProcessor;
import ltg.es.wallcology.notifier.components.support.JSONPacketProcessor;
import ltg.es.wallcology.notifier.components.support.XMLPacketProcessor;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages all the XMPP connections, incoming and outgoing messages. 
 * This class is a singleton meaning there is only one instance per phenomenaPod.
 *
 * @author Gugo
 */
public class NetworkController {
	
	// Logger
	private Logger log = LoggerFactory.getLogger(NetworkController.class);
	// Connection
	protected XMPPConnection connection = null;
	private Roster roster = null;
	private MultiUserChat muc = null;
	// Credentials
	private String myId = null;
	private String password = null;
	// Packet collectors and processors
	private PacketCollector pc = null;
	private IPacketProcessor json_pp = null;
	private IPacketProcessor xml_pp = null;
	
	
	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private NetworkController() {
		this.myId = ConfFile.getProperty("XMPP_USERNAME");
		this.password = ConfFile.getProperty("XMPP_PASSWORD");
	}
	
	
	/**
	 * SingletonHolder is loaded on the first execution of <code>Singleton.getInstance()</code> 
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder { 
		private static final NetworkController INSTANCE = new NetworkController();
	}

	
	/**
	 * Get instance method for the singleton
	 *
	 * @return the only instance of network controller
	 */
	public static NetworkController getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	

	/**
	 * Creates a new XMPP connection associated to a particular phenomena.
	 * The default resource identifier is used.
	 *
	 * @param username It's the unique identifier of the phenomena
	 * @param password 
	 */
	public synchronized boolean connect() {
		if (connection!=null) {
			log.warn("Pod " + myId + " is already online");
			return false;
		}
		ConnectionConfiguration config = 
			new ConnectionConfiguration(ConfFile.getProperty("XMPP_SERVER_URL")
					, Integer.parseInt(ConfFile.getProperty("XMPP_SERVER_PORT"))
					, ConfFile.getProperty("XMPP_SERVER_DOMAIN"));
		connection = new XMPPConnection(config);
		try {
			// Connect
			connection.connect();
			connection.login(myId, password, ConfFile.getProperty("DEFAULT_RESOURCE_ID"));
		} catch (XMPPException e) {
			log.error("Impossible to connect to XMPP server");
			connection = null;
			return false;
		}
		roster = connection.getRoster();
		return true;
	}
	
	
	
	public boolean joinRoom(String chatRoomName) {
		muc = new MultiUserChat(connection, chatRoomName);
		try {
			muc.join(this.myId);
		} catch (XMPPException e) {
			log.error("Impossible to join room " + chatRoomName);
			return false;
		}
		return true;
	}


	/**
	 * Disconnects a particular phenomena instance.
	 * 
	 * @param myId Unique identifier of the phenomena.
	 */
	public synchronized boolean disconnect() {
		if (connection==null) {
			log.warn("Pod " + myId + " is already offline");
			return false;
		}
		connection.disconnect();
		connection=null;
		return true;
	}
	
	
	/**
	 * Start to wait for commands. As a command comes it is parsed and then executed
	 */
	public synchronized void listen() {
		log.info("Listening for JSON and XML messages...");
		pc = connection.createPacketCollector(new PacketTypeFilter(Message.class));
		json_pp = new JSONPacketProcessor();
		xml_pp = new XMLPacketProcessor();
		Message m = null; 
		while(!Thread.currentThread().isInterrupted()) {
			m = (Message) pc.nextResult();
			if(m.getBody()!=null)
				json_pp.processPacket(m);
			if(m.getBody()!=null)
			xml_pp.processPacket(m);
		}
		this.disconnect();
	}


	/**
	 * Used to send a message to a particular client.
	 *
	 * @param dest JID of the client that needs to receive the message 
	 * @param message Message to be sent
	 */
	public synchronized void sendTo(String dest, String message) {
		if (connection==null || !isConnected()){
			log.error("Impossible to send message: pod " +myId+ " isn't connected anymore!");
			return;
		}
		Message m = new Message();
		//m.setFrom(myId);
		m.setTo(dest);
		m.setBody(message);
		connection.sendPacket(m);
	}
	
	
	/**
	 * Used to broadcast a message to all contacts in the roaster
	 *
	 * @param message
	 */
	public synchronized void broadcast(String message) {
		if (connection==null || !isConnected()){
			log.error("Impossible to send message: pod " +myId+ " isn't connected anymore!");
			return;
		}
		roster = connection.getRoster();
		if (roster == null) {
			log.error("Impossible to broadcast message: roaster is null!");
			return;
		}
		Message m = new Message();
		m.setFrom(myId);
		m.setBody(message);
		for (RosterEntry re : roster.getEntries()) {
			m.setTo(re.getUser());
			connection.sendPacket(m);
		}
	}
	
	
	public synchronized boolean isConnected() {
		return connection.isConnected() && connection.isAuthenticated();
	}
	
	
	public String getPass() {
		return password;
	}



	

}
