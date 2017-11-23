package tw.org.ttc.iot.model.script;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tw.org.ttc.iot.utils.tasks.SleepTaskCfg;

public class SleepAST implements AST {

	private final LongProperty duration;

	public SleepAST() {
		this.duration = new SimpleLongProperty(0);
	}

	public void setDuration(long d) {
		this.duration.set(d);
		;
	}

	public long getDuration() {
		return this.duration.get();
	}

	@Override
	public ObservableList<AST> getChildNodes() {
		return FXCollections.emptyObservableList();
	}

	@Override
	public String getTextForTreeCell() {
		return String.format("Sleep for %s ms", this.duration.get());
	}
	
	@Override
	public boolean couldHaveChild() {
		return false;
	}

	@Override
	public SleepTaskCfg toTestScript() {
		SleepTaskCfg task = new SleepTaskCfg();
		task.durationMs = this.getDuration();
		return task;
	}

}
