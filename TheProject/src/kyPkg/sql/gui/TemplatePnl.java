package kyPkg.sql.gui;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public class TemplatePnl extends JPanel {
	public InfDBHandler dbHandler;
	public TemplatePnl(InfDBHandler pDbHandler, LayoutManager layout) {
		super(layout);
	}
}
