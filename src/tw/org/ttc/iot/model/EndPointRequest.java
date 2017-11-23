package tw.org.ttc.iot.model;

import com.google.gson.JsonObject;

public class EndPointRequest {
	private final EndPoint endPoint;
	private JsonObject arguments;

	public EndPointRequest (EndPoint endPoint) {
		this.endPoint = endPoint;
	}
	
	public EndPoint getEndPoint() {
		return this.endPoint;
	}
	
	public void setArguments(JsonObject params) {
		this.arguments = params;
	}
	
	public JsonObject getArguments() {
		if (this.arguments == null) {
			this.arguments = new JsonObject();
		}
		return this.arguments;
	}
}
