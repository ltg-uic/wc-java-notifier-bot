/*
 * Created Mar 12, 2012
 */
package ltg.es.wallcology.notifier;

import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jsonj.JsonElement;
import com.github.jsonj.exceptions.JsonParseException;
import com.github.jsonj.tools.JsonParser;

/**
 * TODO Description
 *
 * @author Gugo
 */
public class JSONPacketProcessor implements IPacketProcessor {
	// Logger
	private Logger log = LoggerFactory.getLogger(this.getClass());
	// JSON parser
	private final JsonParser jsonParser = new JsonParser();


	
	/* (non-Javadoc)
	 * @see ltg.es.wallcology.notifier.PacketProcessorI#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	@Override
	public void processPacket(Message m) {
		JsonElement json = null; 
		try {
			json = jsonParser.parse(m.getBody());
			log.info(json.toString());
		} catch (JsonParseException e) {
			// Not JSON... skip
		}
	}

}
