package kyPkg.task;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class DummyTask extends Abs_ProgressTask {
	private int sleep;
	private List<Component> components;

	public void setComps(java.awt.Component component) {
		if (components == null)
			components = new ArrayList();
		components.add(component);
	}

	public void setComps(List<java.awt.Component> comps) {
		this.components = comps;
	}

	public DummyTask(String message, int sleep) {
		super();
		setMessage(message);
		this.sleep = sleep;
	}

	@Override
	public void execute() {
		super.start("DummyTask", 2048);
		try {
			setMessage("Dummy Start");
			Thread.sleep(sleep);
			stop();
			setMessage("Dummy End");
		} catch (Exception e) {
		}
		super.stop();// ê≥èÌèIóπ

		if (components != null){
			for (Component component : components) {
				component.setEnabled(true);
			}
		}
	}
}
