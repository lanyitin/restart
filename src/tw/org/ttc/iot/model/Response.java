package tw.org.ttc.iot.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Response {

	private final StringProperty code;
	private final StringProperty description;

	public Response(String key, io.swagger.models.Response value) {
		this.code = new SimpleStringProperty(key);
		this.description = new SimpleStringProperty(value.getDescription());
	}

	public String getCode() {
		return this.code.get();
	}

	public String getDescription() {
		return this.description.get();
	}
}
