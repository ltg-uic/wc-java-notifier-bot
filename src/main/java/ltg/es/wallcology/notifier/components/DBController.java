/*
 * Created Mar 13, 2012
 */
package ltg.es.wallcology.notifier.components;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


/**
 * TODO Description
 *
 * @author Gugo
 */
public class DBController {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	// The notifications collection of Wallcology DB
	private DBCollection notifications = null;
	

	
	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private DBController() {
		Mongo m = null;
		DB db = null;
		try {
			m = new Mongo("carbon.evl.uic.edu");
			db = m.getDB("wallcology");
			notifications = db.getCollection("notifications");
		} catch (UnknownHostException e) {
			log.error("Unable to resolve DB host");
			Thread.currentThread().interrupt();
		} catch (MongoException e) {
			log.error("Unable to connect to DB");
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * SingletonHolder is loaded on the first execution of <code>Singleton.getInstance()</code> 
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder { 
		private static final DBController INSTANCE = new DBController();
	}

	/**
	 * Get instance method for the singleton
	 *
	 * @return the only instance of network controller
	 */
	public static DBController getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	
	/**
	 * Adds a new Notification to the notifications database
	 */
	public void saveNotification(BasicDBObject obj) {
		notifications.insert(obj);
	}

}
