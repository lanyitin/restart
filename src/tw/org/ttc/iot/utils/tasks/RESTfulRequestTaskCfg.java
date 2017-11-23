package tw.org.ttc.iot.utils.tasks;

import tw.pllab.probelib.TaskCfg;

public class RESTfulRequestTaskCfg extends TaskCfg {
	public String url;
	public String method;
	public String header;
	public String body;

	@Override
	public String toString() {
		return String.format("%s %s\n %s\n", method, url, body);
	}
}