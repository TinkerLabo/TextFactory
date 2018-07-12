package kyPkg.cross;

import java.awt.LayoutManager;

import kyPkg.mySwing.MyPanel;

public class TmpPanel extends MyPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TmpPanel() {
		super();
	}

	public TmpPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public TmpPanel(int x, int y, int width, int height, LayoutManager layout) {
		super(x, y, width, height, layout);
	}

	public TmpPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public TmpPanel(LayoutManager layout) {
		super(layout);
	}

}
