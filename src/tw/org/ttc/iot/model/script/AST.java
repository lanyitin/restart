package tw.org.ttc.iot.model.script;

import javafx.collections.ObservableList;

public interface AST {
	public ObservableList<AST> getChildNodes();

	public String getTextForTreeCell();
	
	public boolean couldHaveChild();
	
	public Object toTestScript();
}
