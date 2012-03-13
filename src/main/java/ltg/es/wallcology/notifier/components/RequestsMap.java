/*
 * Created Mar 13, 2012
 */
package ltg.es.wallcology.notifier.components;

import java.util.HashMap;
import java.util.Map;

import ltg.es.wallcology.notifier.requests.RequestData;


/**
 * TODO Description
 *
 * @author Gugo
 */
public class RequestsMap {
	
	// The map that holds all the requests
	private Map<String, RequestData> map = null;
	
	
	
	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private RequestsMap() {
		map = new HashMap<String, RequestData>();
	}
	
	/**
	 * SingletonHolder is loaded on the first execution of <code>Singleton.getInstance()</code> 
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder { 
		private static final RequestsMap INSTANCE = new RequestsMap();
	}

	/**
	 * Get instance method for the singleton
	 *
	 * @return the only instance of network controller
	 */
	public static RequestsMap getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	
	
	public void addRequest(RequestData req) {
		map.put(req.getId(), req);
	}
	
	
	
	public RequestData removeRequest(String id) {
		return map.remove(id);
	}

}
