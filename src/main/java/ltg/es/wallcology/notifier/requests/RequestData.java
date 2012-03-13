/*
 * Created Mar 13, 2012
 */
package ltg.es.wallcology.notifier.requests;


/**
 * TODO Description
 *
 * @author Gugo
 */
public abstract class RequestData {
	
	protected String requestId = null;
	
	
	public RequestData() {
		this.requestId = Integer.toString(this.hashCode());
	}
	
	
	public String getId() {
		return requestId;
	}
}
