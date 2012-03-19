/*
 * Created Mar 12, 2012
 */
package ltg.es.wallcology.notifier.components.support;

import ltg.es.wallcology.notifier.xml_handlers.GetCountHandler;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.smack.packet.Message;

/**
 * TODO Description
 *
 * @author Gugo
 */
public class XMLPacketProcessor implements IPacketProcessor {	
	
	/* (non-Javadoc)
	 * @see ltg.es.wallcology.notifier.IPacketProcessor#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	@Override
	public void processPacket(Message pack) {
		Element root = null;
		try {
			root = DocumentHelper.parseText(pack.getBody()).getRootElement();
			if (root==null)
				return;
			// Pick the right XML processor based on the root element name
			if (root.getName().equals("getCount"))
				new GetCountHandler(root).handle();
			//else if (root.getName().equals("COMMAND_NAME"))
			//	new GetCountHandler(root).handle();
		} catch (DocumentException e) {
			// Not XML... skip
		}
	}

}
