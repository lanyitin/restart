package tw.org.ttc.iot.model;

import io.swagger.models.properties.Property;

public class Response {

	private final String code;
	private final String description;
	private final Property schema;

	public Response(String key, io.swagger.models.Response value) {
		this.code = key;
		this.description = value.getDescription();
		this.schema = value.getSchema();
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getDescription() {
		return this.description;
	}
}
