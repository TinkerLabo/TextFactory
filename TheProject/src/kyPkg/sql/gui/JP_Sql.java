package kyPkg.sql.gui;

import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import kyPkg.panel.JP_Ancestor;
import java.awt.*;
import javax.swing.*;
public class JP_Sql extends JP_Ancestor {
	private DB_Control pnlControl;
	private DB_Grid pnlGrid;
	private JSplitPane splitP;

	// -Xmx512M
	// ------------------------------------------------------------------------
	// main (for stand alone Version)
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
		String title="Da+a lab-o-ra-to-ry";
		standAlone(new JP_Sql(), title);
	}

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public JP_Sql() {
		super();
		setSize(940, 520);
		this.setLayout(null);
		initMe();
	}

	// -------------------------------------------------------------------------
	// init
	// -------------------------------------------------------------------------
	public void initMe() {
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(144, 210, 255));
		pnlControl = new DB_Control();
		pnlGrid = new DB_Grid(pnlControl);
		splitP = new JSplitPane(VERTICAL_SPLIT, pnlControl, pnlGrid);
		splitP.setDividerLocation((200));
		this.add("Center", splitP);
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// // 描画
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// public void paint(Graphics g) {
	// super.paint(g);// Super経由でpaintCompをよぶ
	// }
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// // 描画
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// public void paintComponent(Graphics g) {
	// Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
	// Color.white);
	// g.setColor(Color.blue);
	// }

}
// ---------------------------------------------------------------------
// javax.swing.JRootPane クラスにある
// JRootPane root = getRootPane();
// if(root != null) root.setDefaultButton(null);
// ◎デフォルトボタンを無視する場合
// getRootPane().setDefaultButton(jBtnQ2Tbl);
// ---------------------------------------------------------------------
