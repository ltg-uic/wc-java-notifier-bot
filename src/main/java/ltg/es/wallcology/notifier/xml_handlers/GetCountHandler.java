/*
 * Created Mar 19, 2012
 */
package ltg.es.wallcology.notifier.xml_handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ltg.es.wallcology.notifier.components.DBController;
import ltg.es.wallcology.notifier.requests.CountRequestData;

import org.dom4j.Element;

import com.mongodb.BasicDBObject;

/**
 * TODO Description
 *
 * @author Gugo
 */
public class GetCountHandler extends XMLHandler {
	
	private DBController db = null;
	
	private CountRequestData cd = null;
	private List<BasicDBObject> alerts = null;
	

	/**
	 * @param e
	 */
	public GetCountHandler(Element e) {
		super(e);
		db = DBController.getInstance();
	}

	/* (non-Javadoc)
	 * @see ltg.es.wallcology.notifier.xml_handlers.XMLHandler#handle()
	 */
	@Override
	public void handle() {
		// Get the stored kids-entered data 
		cd = (CountRequestData) rm.removeRequest(xml.attributeValue("reqId"));
		if (cd==null)
			return;
		
		// Compare values...
		boolean doNotSendNotification = true;
		alerts = new ArrayList<BasicDBObject>();
		
		// ... for environment
		if (!compare(Double.parseDouble(xml.elementTextTrim("temperature")), 2.0, cd.getTemp())) {
			generateAlert("Kids entered the wrong temperature value <br />Entered value = " + cd.getTemp() + " <br />Real value = " + xml.elementTextTrim("temperature"));
			doNotSendNotification = false;
		}
		if (!compare(Double.parseDouble(xml.elementTextTrim("humidity")), 3.0, cd.getHumid())) {
			generateAlert("Kids entered the wrong humidity value <br />Entered value = " + cd.getHumid() + " <br />Real value = " + xml.elementTextTrim("humidity"));
			doNotSendNotification = false;
		}
		if (!compare(Double.parseDouble(xml.elementTextTrim("light")), 3.0, cd.getLight())) {
			generateAlert("Kids entered the wrong light value <br />Entered value = " + cd.getLight() + " <br />Real value = " + xml.elementTextTrim("light"));
			doNotSendNotification = false;
		}
		// ... for creatures with one count only
		
		// ... and the math
		
		
		// ... and finally compose and store the notification in the DB
		if (!doNotSendNotification) {
			BasicDBObject n = new BasicDBObject();
			n.put("id", cd.getId());
			n.put("title", cd.getOrigin() + " need help!");
			n.put("status", "new");
			n.put("history", new BasicDBObject("new", System.currentTimeMillis()));
			n.put("alerts", alerts);
			db.saveNotification(n);
		}
	}
	
	
	// Returns true if the kids' value is correct and false if it is not
	private boolean compare(int sim, int tolerance, int kids) {
		if (Math.abs(sim-kids) < tolerance)
			return true;
		return false;
	}
	
	
	// Returns true if the kids' value is correct and false if it is not
	private boolean compare(double sim, double tolerance, double kids) {
		if (Math.abs(sim-kids) < tolerance)
			return true;
		return false;
	}
	
	
	private void generateAlert(String text) {
		BasicDBObject alert = new BasicDBObject("text", text);
		alerts.add(alert);
	}

}
