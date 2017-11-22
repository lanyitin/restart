package tw.org.ttc.iot.model;

import com.google.gson.JsonObject;

public class EndPointRequest {
	private final EndPoint endPoint;
	private JsonObject parameters;

	public EndPointRequest (EndPoint endPoint) {
		this.endPoint = endPoint;
	}
	
	public EndPoint getEndPoint() {
		return this.endPoint;
	}
	
	public void setParameters(JsonObject params) {
		this.parameters = params;
	}
	
	public JsonObject getParameters() {
		if (this.parameters == null) {
			return new JsonObject();
		}
		return this.parameters;
	}
}
