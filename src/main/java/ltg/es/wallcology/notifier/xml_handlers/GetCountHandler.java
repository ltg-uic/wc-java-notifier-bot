/*
 * Created Mar 19, 2012
 */
package ltg.es.wallcology.notifier.xml_handlers;

import org.dom4j.Element;

/**
 * TODO Description
 *
 * @author Gugo
 */
public class GetCountHandler extends XMLHandler {

	/**
	 * @param e
	 */
	public GetCountHandler(Element e) {
		super(e);
	}

	/* (non-Javadoc)
	 * @see ltg.es.wallcology.notifier.xml_handlers.XMLHandler#handle()
	 */
	@Override
	public void handle() {
		log.debug("Temp: " + xml.elementTextTrim("temperature") );
		log.debug("Humi: " + xml.elementTextTrim("humidity") );
		log.debug("Ligh: " + xml.elementTextTrim("light") );
	}

}
