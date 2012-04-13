/*
 * Created Mar 19, 2012
 */
package ltg.es.wallcology.notifier.xml_handlers;

import java.util.ArrayList;
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
	private boolean doNotSendNotification = true;
	private List<BasicDBObject> alerts = null;


	/**
	 * @param e
	 */
	public GetCountHandler(Element e) {
		super(e);
		db = DBController.getInstance();
		alerts = new ArrayList<BasicDBObject>();
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

		// Compare values of...
		checkValues();

		// Compose and store the notification using the generated alerts
		if (!doNotSendNotification) {
			BasicDBObject n = new BasicDBObject();
			n.put("title", cd.getOrigin() + " need help!");
			n.put("status", "new");
			n.put("history", new BasicDBObject("new", System.currentTimeMillis()));
			n.put("alerts", alerts);
			db.saveNotification(n);
			log.debug(n.toString());
		}
	}


	// Checks all the values for each count
	private void checkValues() {
		// Get real values...
		int real_fuzz = Integer.parseInt(xml.element("fluffyMold").elementTextTrim("amount"));
		int real_scum = Integer.parseInt(xml.element("greenScum").elementTextTrim("amount"));
		int real_blue = Integer.parseInt(xml.element("blueBug").elementTextTrim("amount"));
		int real_green = Integer.parseInt(xml.element("greenBug").elementTextTrim("amount"));
		int real_p = Integer.parseInt(xml.element("fuzzPredator").elementTextTrim("amount"));
		
		// Set flags
		boolean noSCountingErrors = true;
		boolean noFCountingErrors = true;
		boolean noBBCountingErrors = true;
		boolean noGBCountingErrors = true;
		boolean noPRCountingErrors = true;
		boolean noBBAvgErrors = true;
		boolean noGBAvgErrors = true;
		boolean noPRAvgErrors = true;
		
		// Environmental conditions (3 IFs)
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

		// Entered values of creatures with only ONE count only (2 IFs)
		if(!compare(real_fuzz, 5, cd.getF())) {
			generateAlert("Kids entered the wrong GREEN VEGETATION value <br />Entered value = " + cd.getF() + " <br />Real value = " + real_fuzz);
			doNotSendNotification = false;
			noFCountingErrors = false;
		}
		if(!compare(real_scum, 5, cd.getS())) {
			generateAlert("Kids entered the wrong ORANGE VEGETATION value <br />Entered value = " + cd.getS() + " <br />Real value = " + real_scum);
			doNotSendNotification = false;
			noSCountingErrors = false;
		}

		// Both entered values for creatures with more than one count (6 IFs)
		if(!compare(real_blue, 5, cd.getBb1())) {
			generateAlert("Kids entered the wrong BLUE BUGS value in their first count. <br />Entered value = " + cd.getBb1() + " <br />Real value = " + real_blue);
			doNotSendNotification = false;
			noBBCountingErrors = false;
		}
		if(!compare(real_blue, 5, cd.getBb2())) {
			generateAlert("Kids entered the wrong BLUE BUGS value in their second count. <br />Entered value = " + cd.getBb2() + " <br />Real value = " + real_blue);
			doNotSendNotification = false;
			noBBCountingErrors = false;
		}
		if(!compare(real_green, 5, cd.getGb1())) {
			generateAlert("Kids entered the wrong GREEN BUGS value in their first count. <br />Entered value = " + cd.getGb1() + " <br />Real value = " + real_green);
			doNotSendNotification = false;
			noGBCountingErrors = false;
		}
		if(!compare(real_green, 5, cd.getGb2())) {
			generateAlert("Kids entered the wrong GREEN BUGS value in their second count. <br />Entered value = " + cd.getGb2() + " <br />Real value = " + real_green);
			doNotSendNotification = false;
			noGBCountingErrors = false;
		}
		if (real_p != 0) { 
			if(!compare(real_p, 5, cd.getPr1())) {
				generateAlert("Kids entered the wrong PREDATOR value in their first count. <br />Entered value = " + cd.getPr1() + " <br />Real value = " + real_p);
				doNotSendNotification = false;
				noPRCountingErrors = false;
			}
			if(!compare(real_p, 5, cd.getPr2())) {
				generateAlert("Kids entered the wrong PREDATOR value in their second count. <br />Entered value = " + cd.getPr2() + " <br />Real value = " + real_p);
				doNotSendNotification = false;
				noPRCountingErrors = false;
			}
		}

		// The averaging math (3 IFs)
		if(noBBCountingErrors && !compare(avg(cd.getBb1(),cd.getBb2()), 5, cd.getBb_avg())) {
			generateAlert("Kids entered the wrong BLUE BUGS average. <br />Entered value = " + cd.getBb_avg() + " <br />Real value = " + avg(cd.getBb1(),cd.getBb2()));
			doNotSendNotification = false;
			noBBAvgErrors = false;
		}
		if(noGBCountingErrors && !compare(avg(cd.getGb1(),cd.getGb2()), 5, cd.getGb_avg())) {
			generateAlert("Kids entered the wrong GREEN BUGS average. <br />Entered value = " + cd.getGb_avg() + " <br />Real value = " + avg(cd.getGb1(),cd.getGb2()));
			doNotSendNotification = false;
			noGBAvgErrors = false;
		}
		if (real_p != 0) { 
			if(noPRCountingErrors && !compare(avg(cd.getPr1(),cd.getPr2()), 5, cd.getPr_avg())) {
				generateAlert("Kids entered the wrong BLUE BUGS average. <br />Entered value = " + cd.getPr_avg() + " <br />Real value = " + avg(cd.getPr1(),cd.getPr2()));
				doNotSendNotification = false;
				noPRAvgErrors = false;
			}
		}

		// The final estimate math (5 IFs)
		if(noFCountingErrors && !compare(realFinal(cd.getF(), cd.getF_mult()), 5, cd.getF_f())) {
			generateAlert("Kids entered the wrong FINAL VALUE for the GREEN VEGETATION. <br />Entered value = " + cd.getF_f() + " <br />Real value = " + realFinal(cd.getF(), cd.getF_mult()));
			doNotSendNotification = false;
		}
		if(noSCountingErrors && !compare(realFinal(cd.getS(), cd.getS_mult()), 5, cd.getS_f())) {
			generateAlert("Kids entered the wrong FINAL VALUE for the ORANGE VEGETATION. <br />Entered value = " + cd.getS_f() + " <br />Real value = " + realFinal(cd.getS(), cd.getS_mult()));
			doNotSendNotification = false;
		}
		if(noBBCountingErrors && noBBAvgErrors && !compare(realFinal(cd.getBb_avg(), cd.getBb_mult()), 5, cd.getBb_f())) {
			generateAlert("Kids entered the wrong BLUE BUGS FINAL VALUE. <br />Entered value = " + cd.getBb_mult() + " <br />Real value = " + realFinal(cd.getBb_avg(), cd.getBb_mult()));
			doNotSendNotification = false;
		}
		if(noGBCountingErrors && noGBAvgErrors && !compare(realFinal(cd.getGb_avg(), cd.getGb_mult()), 5, cd.getGb_f())) {
			generateAlert("Kids entered the wrong GREEN BUGS FINAL VALUE. <br />Entered value = " + cd.getGb_mult() + " <br />Real value = " + realFinal(cd.getGb_avg(), cd.getGb_mult()));
			doNotSendNotification = false;
		}
		if (real_p != 0) { 
			if(noPRCountingErrors && noPRAvgErrors && !compare(realFinal(cd.getPr_avg(), cd.getPr_mult()), 5, cd.getPr_f())) {
				generateAlert("Kids entered the wrong PREDATOR FINAL VALUE. <br />Entered value = " + cd.getPr_mult() + " <br />Real value = " + realFinal(cd.getPr_avg(), cd.getPr_mult()));
				doNotSendNotification = false;
			}
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
	
	
	// Average... java doesn't have it :(
	private int avg(int sim, int kids) {
		return (sim+kids)/2;
	}
	
	
	// Real final value
	private int realFinal(int real_value, int multiplier) {
		return real_value * multiplier;
	}


	private void generateAlert(String text) {
		BasicDBObject alert = new BasicDBObject("text", text);
		alerts.add(alert);
	}

}
