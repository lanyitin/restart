package tw.org.ttc.iot.model.script;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import io.swagger.models.parameters.Parameter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
		StringProperty url = new SimpleStringProperty(this.request.getEndPoint().getUrl());
		List<Parameter> params = this.request.getEndPoint().getOperation().getParameters();
		Stream<Parameter> filtered = params.stream()
				.filter(x -> x.getIn().equals("path")
						&& this.request.getParameters().keySet().contains(x.getName()));
		filtered.forEach((x) -> {
			System.out.println(x.getName());
			url.set(url.get().replaceAll(String.format("\\{%s\\}", x.getName()),
					this.request.getParameters().get(x.getName()).getAsString()));
		});
		return String.format("%s %s", this.getRequest().getEndPoint().getMethod(), url.get());
	}

	@Override
	public boolean couldHaveChild() {
		return false;
	}

	public EndPointRequest getRequest() {
		return this.request;
	}

	@Override
	public Object toTestScript() {
		RESTfulRequestTaskCfg cfg = new RESTfulRequestTaskCfg();
		cfg.method = this.request.getEndPoint().getMethod();
		StringProperty url = new SimpleStringProperty(this.request.getEndPoint().getUrl());
		List<Parameter> params = this.request.getEndPoint().getOperation().getParameters();
		Stream<Parameter> filtered = params.stream()
				.filter(x -> x.getIn().equals("path") && this.request.getParameters().keySet().contains(x.getName()));
		filtered.forEach((x) -> {
			url.set(url.get().replaceAll(String.format("\\{%s\\}", x.getName()),
					this.request.getParameters().get(x.getName()).getAsString()));
		});
		cfg.url = url.get();
		JsonObject body = new JsonObject();
		this.request.getEndPoint().getOperation().getParameters().stream().filter(x -> x.getIn().equals("body"))
				.forEach((x) -> {
					body.addProperty(x.getName(), this.request.getParameters().get(x.getName()).getAsString());
				});
		cfg.body = body;
		return cfg;
	}

}
