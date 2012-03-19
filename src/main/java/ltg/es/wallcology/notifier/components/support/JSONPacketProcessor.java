/*
 * Created Mar 12, 2012
 */
package ltg.es.wallcology.notifier.components.support;


import ltg.es.wallcology.notifier.json_handlers.CountHandler;

import org.jivesoftware.smack.packet.Message;

import com.github.jsonj.JsonElement;
import com.github.jsonj.JsonObject;
import com.github.jsonj.exceptions.JsonParseException;
import com.github.jsonj.tools.JsonParser;

/**
 * TODO Description
 *
 * @author Gugo
 */
public class JSONPacketProcessor implements IPacketProcessor {
	
	// JSON parser
	private final JsonParser jsonParser = new JsonParser();


	
	/* (non-Javadoc)
	 * @see ltg.es.wallcology.notifier.PacketProcessorI#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	@Override
	public void processPacket(Message m) {
		JsonElement jsone = null;
		JsonObject json = null;
		try {
			jsone = jsonParser.parse(m.getBody());
			if (!jsone.isObject()) {
				// The element is not an object... bad...
				return;
			}
			json = jsone.asObject();
			// Handle other eventual JSON messages that don't follow the SAIL syntax
			// CODE HERE...
			// Pick the right JSON handler based on the event type
			if (json.getString("eventType")!= null && 
					json.getString("eventType").equals("new_observation")) {
				if(json.getString("payload", "type")!= null &&
						json.getString("payload", "type").equals("count"))
					new CountHandler(json).handle();
				//else if (json.getString("payload", "type").equals("somethingElse"))
			} //else if (json.getString("eventType".equals("somethingElse")) {
				// Do something else
			//}
		} catch (JsonParseException e) {
			// Not JSON... skip
		}
	}

}
