package kyPkg.task;

import java.util.List;

public class TaskController {
	protected Inf_ProgressTask task;
	protected List<Inf_ProgressTask> taskList;

	public Inf_ProgressTask getTask() {
		return task;
	}

	public void reset() {
		taskList = null;
	}

	public void stop() {
		if (task != null)
			task.stop();
	}

	public void interrupt() {
		stop();
		reset();
	}
}