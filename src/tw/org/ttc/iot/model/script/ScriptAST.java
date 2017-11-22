package tw.org.ttc.iot.model.script;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScriptAST implements AST {
	private final ObservableList<AST> childNodes;

	public ScriptAST() {
		this.childNodes = FXCollections.observableArrayList();
	}

	@Override
	public ObservableList<AST> getChildNodes() {
		return childNodes;
	}

	public void addChildNode(AST childNode) {
		this.childNodes.add(childNode);
	}

	@Override
	public String getTextForTreeCell() {
		return "Test Script";
	}
}
