package tw.org.ttc.iot.model.script;

import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tw.org.ttc.iot.utils.tasks.LoopTaskCfg;

public class LoopAST implements AST {
	private final ObservableList<AST> childNodes;
	private final IntegerProperty count;

	public LoopAST() {
		this.childNodes = FXCollections.observableArrayList();
		this.count = new SimpleIntegerProperty(0);
	}

	public LoopAST(AST body, int count) {
		this();
		this.childNodes.add(body);
		this.setCount(count);
	}

	public void setCount(int count) {
		this.count.set(count);
	}

	public int getCount() {
		return this.count.get();
	}

	@Override
	public ObservableList<AST> getChildNodes() {
		return this.childNodes;
	}

	@Override
	public String getTextForTreeCell() {
		return String.format("Repeat for %s times", getCount());
	}

	@Override
	public boolean couldHaveChild() {
		return true;
	}

	@Override
	public LoopTaskCfg toTestScript() {
		LoopTaskCfg task = new LoopTaskCfg();
		task.iterationCount = this.getCount();
		task.bodyTask = this.getChildNodes().stream().map(AST::toTestScript).collect(Collectors.toList());
		return task;
	}
}
