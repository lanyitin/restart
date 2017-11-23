package tw.org.ttc.iot.model.script;

import java.util.List;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import io.swagger.models.parameters.Parameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tw.org.ttc.iot.model.EndPointRequest;
import tw.org.ttc.iot.utils.tasks.RESTfulRequestTaskCfg;

public class RESTfulRequestAST implements AST {

	private final EndPointRequest request;
	private final ObservableList<AST> childNodes;

	public RESTfulRequestAST(EndPointRequest endPoint) {
		this.request = endPoint;
		childNodes = FXCollections.emptyObservableList();
	}

	@Override
	public ObservableList<AST> getChildNodes() {
		return childNodes;
	}

	@Override
	public String getTextForTreeCell() {
		List<Parameter> params = this.request.getEndPoint().getOperation().getParameters();
		return String.format("%s %s", this.getRequest().getEndPoint().getMethod(), renderUrl(this.request.getEndPoint().getUrl(), params.stream(), this.request.getArguments()));
	}

	@Override
	public boolean couldHaveChild() {
		return false;
	}

	public EndPointRequest getRequest() {
		return this.request;
	}

	@Override
	public RESTfulRequestTaskCfg toTestScript() {
		RESTfulRequestTaskCfg cfg = new RESTfulRequestTaskCfg();
		cfg.method = this.request.getEndPoint().getMethod();

		List<Parameter> params = this.request.getEndPoint().getOperation().getParameters();
		cfg.url = renderUrl(this.request.getEndPoint().getUrl(), params.stream(), this.request.getArguments());
		
		JsonObject body = new JsonObject();
		filterParametersByInAndArguments("body", params.stream(), this.request.getArguments())
			.forEach(p -> body.addProperty(p.getName(), this.request.getArguments().get(p.getName()).getAsString()));
		cfg.body = body.toString();
		return cfg;
	}
	
	String renderUrl(String url, Stream<Parameter> param, JsonObject arguments) {
		return filterParametersByInAndArguments("path", param, arguments)
			.reduce(url,
					(String u, Parameter p) -> {
						return u.replace("{" + p.getName() +"}", arguments.get(p.getName()).getAsString());
					}, (x, y) -> x);
	}

	private Stream<Parameter> filterParametersByInAndArguments(String in, Stream<Parameter> param, JsonObject arguments) {
		return param.filter(x -> x.getIn().equals(in))
			.filter(x -> arguments.has(x.getName()));
	}
}
