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
		// Store data in the map
		String reqId = rm.addRequest(new CountRequestData(
				Integer.parseInt(json.getString("payload", "chosen_habitat")),   							//wall
				Integer.parseInt(json.getString("payload", "light_level")),									//light
				Integer.parseInt(json.getString("payload", "temperature")),									//temp
				Integer.parseInt(json.getString("payload", "humidity")),									//humid
				Integer.parseInt(json.getString("payload", "organism_counts", "scum", "count1")),			//s1
				Integer.parseInt(json.getString("payload", "organism_counts", "mold", "count1" )),			//f1
				Integer.parseInt(json.getString("payload", "organism_counts", "blue_bug", "count1")),		//bb1
				Integer.parseInt(json.getString("payload", "organism_counts", "green_bug", "count1")),		//gb1
				Integer.parseInt(json.getString("payload", "organism_counts", "predator", "count1")),		//pr1
				Integer.parseInt(json.getString("payload", "organism_counts", "blue_bug", "count2")),		//bb2
				Integer.parseInt(json.getString("payload", "organism_counts", "green_bug", "count2")),		//gb2
				Integer.parseInt(json.getString("payload", "organism_counts", "predator", "count2")),		//pr2
				Integer.parseInt(json.getString("payload", "organism_counts", "blue_bug", "count3")),		//bb3
				Integer.parseInt(json.getString("payload", "organism_counts", "green_bug", "count3")),		//gb3
				Integer.parseInt(json.getString("payload", "organism_counts", "predator", "count3")),		//pr3
				Integer.parseInt(json.getString("payload", "organism_counts", "blue_bug", "average")),		//bb_avg
				Integer.parseInt(json.getString("payload", "organism_counts", "green_bug", "average")),		//gb_avg
				Integer.parseInt(json.getString("payload", "organism_counts", "predator", "average")),		//pred_avg
				Integer.parseInt(json.getString("payload", "organism_counts", "scum", "final_count")),		//s_f
				Integer.parseInt(json.getString("payload", "organism_counts", "mold", "final_count")),		//f_f
				Integer.parseInt(json.getString("payload", "organism_counts", "blue_bug", "final_count")),	//bb_f
				Integer.parseInt(json.getString("payload", "organism_counts", "green_bug", "final_count")),	//gb_f
				Integer.parseInt(json.getString("payload", "organism_counts", "predator", "final_count"))	//pr_f
				));
		// Send request to WallCology server
		String message = "<getCount reqId=\""+ reqId +"\" wall=\""+ json.getString("payload", "chosen_habitat") +"\" />";
		net.sendTo(ConfFile.getProperty("WALLCOLOGY_USERNAME"), message);
	}

}
