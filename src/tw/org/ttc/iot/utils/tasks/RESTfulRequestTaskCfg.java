package tw.org.ttc.iot.utils.tasks;

import com.google.gson.JsonObject;

public class RESTfulRequestTaskCfg {
	public String url;
	public String method;
	public JsonObject header;
	public JsonObject body;

	@Override
	public String toString() {
		return String.format("%s %s %s", method, url, body.toString());
	}
}
