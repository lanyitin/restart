package tw.org.ttc.iot.utils.tasks;

//import tw.pllab.probelib.TaskCfg;

//public class ForLoopTaskCfg extends TaskCfg {
public class LoopTaskCfg {
	public int iterationCount;
	public Object bodyTask;
	
	@Override
	public String toString() {
		return String.format("Repeat for %s times %s", iterationCount, bodyTask);
	}
}
