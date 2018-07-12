package kyPkg.panel;

import javax.swing.JPanel;

public abstract class RezPanel extends JPanel  {
	private static final long serialVersionUID = 1L;
	public RezPanel() {
		super();
	}
	public abstract void init();
	public abstract void update();
}