package tw.org.ttc.iot.utils.tasks;

import tw.pllab.probelib.TaskCfg;

public class LoopTaskCfg extends TaskCfg {
	public int iterationCount;
	public Object bodyTask;
	
	@Override
	public String toString() {
		return String.format("Repeat for %s times %s", iterationCount, bodyTask);
	}
}
