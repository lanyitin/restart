package tw.org.ttc.iot.model.script;

import javafx.collections.ObservableList;
import tw.pllab.probelib.TaskCfg;

public interface AST {
	public ObservableList<AST> getChildNodes();

	public String getTextForTreeCell();
	
	public boolean couldHaveChild();
	
	public TaskCfg toTestScript();
}
