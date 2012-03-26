/*
 * Created Mar 13, 2012
 */
package ltg.es.wallcology.notifier.json_handlers;

import ltg.es.wallcology.notifier.components.NetworkController;
import ltg.es.wallcology.notifier.components.RequestsMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jsonj.JsonObject;

/**
 * TODO Description
 *
 * @author Gugo
 */
public abstract class JsonHandler {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected NetworkController net = NetworkController.getInstance();
	protected RequestsMap rm = RequestsMap.getInstance();
	protected JsonObject json = null;

	
	public JsonHandler(JsonObject json) {
		this.json = json;
	}

	
	public abstract void handle();

}
