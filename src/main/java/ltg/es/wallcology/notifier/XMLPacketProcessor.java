/*
 * Created Mar 12, 2012
 */
package ltg.es.wallcology.notifier;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Description
 *
 * @author Gugo
 */
public class XMLPacketProcessor implements IPacketProcessor {

	// Logger
	private Logger log = LoggerFactory.getLogger(this.getClass());

	
	
	/* (non-Javadoc)
	 * @see ltg.es.wallcology.notifier.IPacketProcessor#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	@Override
	public void processPacket(Message pack) {
		Document xml = null;
		try {
			xml = DocumentHelper.parseText(pack.getBody());
			log.info(xml.asXML());
		} catch (DocumentException e) {
			// Not XML... skip
		}
	}

}
