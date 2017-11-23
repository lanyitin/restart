package tw.org.ttc.iot.model.script;

import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tw.pllab.probelib.task.SequentialCfg;

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
	public boolean couldHaveChild() {
		return true;
	}

	@Override
	public SequentialCfg toTestScript() {
		SequentialCfg cfg = new SequentialCfg();
		cfg.setCfgList(this.getChildNodes().stream().map(AST::toTestScript).collect(Collectors.toList()));
		return cfg;
	}

}
