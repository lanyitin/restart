package tw.org.ttc.iot.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import io.swagger.models.Operation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class EndPoint {
	final private StringProperty method;
	final private StringProperty url;
	final private ObjectProperty<Operation> operation;

	public EndPoint(String method, String url, Operation operation) {
		this.method = new SimpleStringProperty(method);
		this.url = new SimpleStringProperty(url);
		this.operation = new SimpleObjectProperty<Operation>(operation);
	}
	
	public String getMethod() {
		return method.get();
	}
	
	public String getUrl() {
		return url.get();
	}
	
	public Operation getOperation() {
		return operation.get();
	}
}
