package tw.org.ttc.iot.model.script;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tw.org.ttc.iot.model.EndPoint;

public class RESTfulRequestAST implements AST {

	private final EndPoint endPoint;
	private final ObservableList<AST> childNodes;

	public RESTfulRequestAST(EndPoint endPoint) {
		this.endPoint = endPoint;
		childNodes = FXCollections.emptyObservableList();
	}

	@Override
	public ObservableList<AST> getChildNodes() {
		return childNodes;
	}

	@Override
	public String getTextForTreeCell() {
		return String.format("%s %s", endPoint.getMethod(), endPoint.getUrl());
	}
}
