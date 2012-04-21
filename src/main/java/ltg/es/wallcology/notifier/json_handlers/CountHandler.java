/*
 * Created Mar 13, 2012
 */
package ltg.es.wallcology.notifier.json_handlers;

import ltg.es.wallcology.notifier.components.ConfFile;
import ltg.es.wallcology.notifier.requests.CountRequestData;

import com.github.jsonj.JsonObject;

/**
 * TODO Description
 *
 * @author Gugo
 */
public class CountHandler extends JsonHandler {

	public CountHandler(JsonObject json) {
		super(json);
	}


	/* (non-Javadoc)
	 * @see ltg.es.wallcology.notifier.handlers.JsonHandler#handle()
	 */
	@Override
	public void handle() {
		// Process data to check if this is a predator count or not
		String pr1 = 	json.getString("payload", "organism_counts", "predator", "count1");
		if (pr1.equals("")) pr1="0";
		String pr2 = 	json.getString("payload", "organism_counts", "predator", "count2");
		if (pr2.equals("")) pr2="0";
		String pr_avg = json.getString("payload", "organism_counts", "predator", "average");
		if (pr_avg.equals("")) pr_avg="0";
		String p_f = 	json.getString("payload", "organism_counts", "predator", "final_count");
		if (p_f.equals("")) p_f="0";
		// Store data in the map
		try {
			String reqId = rm.addRequest(new CountRequestData(
					json.getString("origin"),
					Integer.parseInt(json.getString("payload", "chosen_habitat")),   							//wall
					Integer.parseInt(json.getString("payload", "light_level")),									//light
					Integer.parseInt(json.getString("payload", "temperature")),									//temp
					Integer.parseInt(json.getString("payload", "humidity")),									//humid
					Integer.parseInt(json.getString("payload", "organism_counts", "scum", "count1")),			//s1
					Integer.parseInt(json.getString("payload", "organism_counts", "mold", "count1" )),			//f1
					Integer.parseInt(json.getString("payload", "organism_counts", "blue_bug", "count1")),		//bb1
					Integer.parseInt(json.getString("payload", "organism_counts", "green_bug", "count1")),		//gb1
					Integer.parseInt(pr1),																		//pr1
					Integer.parseInt(json.getString("payload", "organism_counts", "blue_bug", "count2")),		//bb2
					Integer.parseInt(json.getString("payload", "organism_counts", "green_bug", "count2")),		//gb2
					Integer.parseInt(pr2),																		//pr2
					Double.parseDouble(json.getString("payload", "organism_counts", "blue_bug", "average")),		//bb_avg
					Double.parseDouble(json.getString("payload", "organism_counts", "green_bug", "average")),		//gb_avg
					Double.parseDouble(pr_avg),																	//pred_avg
					Integer.parseInt(json.getString("payload", "organism_counts", "scum", "final_count")),		//s_f
					Integer.parseInt(json.getString("payload", "organism_counts", "mold", "final_count")),		//f_f
					Double.parseDouble(json.getString("payload", "organism_counts", "blue_bug", "final_count")),	//bb_f
					Double.parseDouble(json.getString("payload", "organism_counts", "green_bug", "final_count")),	//gb_f
					Double.parseDouble(p_f),																		//pr_f
					Integer.parseInt(json.getString("payload", "organism_counts", "scum", "multiplier")),		//s_mult
					Integer.parseInt(json.getString("payload", "organism_counts", "mold", "multiplier" )),		//f_mult
					Integer.parseInt(json.getString("payload", "organism_counts", "blue_bug", "multiplier")),	//bb_mult
					Integer.parseInt(json.getString("payload", "organism_counts", "green_bug", "multiplier")),	//gb_mult
					Integer.parseInt(json.getString("payload", "organism_counts", "predator", "multiplier"))	//pr_mult
					));
			// Send request to WallCology server
			String message = "<getCount reqId=\""+ reqId +"\" wall=\""+ json.getString("payload", "chosen_habitat") +"\" />";
			net.sendTo(ConfFile.getProperty("WALLCOLOGY_USERNAME"), message);
		} catch (NumberFormatException e) {
			for(StackTraceElement el : e.getStackTrace())
				if(el.getFileName().contains((this.getClass().getSimpleName())))
						log.error("Error parsing count values entered by kids. Line: " + el.getLineNumber());
		}
	}

}
