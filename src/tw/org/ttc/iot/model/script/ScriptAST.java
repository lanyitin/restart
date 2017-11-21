package tw.org.ttc.iot.model.script;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;

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
	@Override
	public ContextMenu getContextMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void removeLinkFrom(AST value) {
		this.childNodes.remove(value);
	}
	@Override
	public void addLinkTo(AST value) {
		this.childNodes.add(value);
	}
	@Override
	public boolean isDraggableTo(AST value) {
		if (value != this) {
			return true;
		} else {
			return false;
		}
	}

}
