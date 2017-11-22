package tw.org.ttc.iot.model.script;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

}
