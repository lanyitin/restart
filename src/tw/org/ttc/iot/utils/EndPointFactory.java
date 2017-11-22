package tw.org.ttc.iot.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import tw.org.ttc.iot.model.EndPoint;

public class EndPointFactory {
	private EndPointFactory() {
	}

	public static List<EndPoint> getEndPoints(String path) {
		Swagger swagger = new SwaggerParser().read(path);
		return find_endpoints(swagger);
	}

	private static List<EndPoint> find_endpoints(Swagger swagger) {
		List<EndPoint> result = new ArrayList<EndPoint>();
		Iterator<Entry<String, Path>> it = swagger.getPaths().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Path> item = it.next();
			result.addAll(find_endpoints(item.getKey(), item.getValue()));
		}
		return result;
	}

	private static Collection<? extends EndPoint> find_endpoints(String key, Path value) {
		// TODO Auto-generated method stub
		List<EndPoint> result = new ArrayList<EndPoint>();
		Iterator<Entry<HttpMethod, Operation>> it = value.getOperationMap().entrySet().iterator();
		while (it.hasNext()) {
			Entry<HttpMethod, Operation> item = it.next();
			result.add(new EndPoint(item.getKey().name(), key, item.getValue()));
		}
		return result;
	}
}
