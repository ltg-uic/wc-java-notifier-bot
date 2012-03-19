/*
 * Created Mar 13, 2012
 */
package ltg.es.wallcology.notifier.xml_handlers;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Description
 *
 * @author Gugo
 */
public abstract class XMLHandler {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected Element xml = null;

	
	public XMLHandler(Element e) {
		this.xml = e;
	}

	
	public abstract void handle();

}
