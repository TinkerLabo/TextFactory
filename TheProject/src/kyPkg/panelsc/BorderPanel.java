package kyPkg.panelsc;

import java.awt.*;

import javax.swing.*;

import kyPkg.panel.JP_Ancestor;

public class BorderPanel extends JP_Ancestor {
	private static final long serialVersionUID = -2499195222831432091L;
	public JPanel pnlN; // North
	public JPanel pnlS; // South
	public JPanel pnlC; // Center
	public JPanel pnlE; // East
	public JPanel pnlW; // West
	public BorderPanel() {
		this("");
	}
	public BorderPanel(String title) {
		super(title);
		this.setLayout(new BorderLayout());
		// --------------------------------------------------------------------
		pnlN = new JPanel();
		pnlS = new JPanel();
		pnlC = new JPanel();
		pnlE = new JPanel();
		pnlW = new JPanel();
		pnlN.setLayout(new BoxLayout(pnlN, BoxLayout.Y_AXIS));
		pnlS.setLayout(new BoxLayout(pnlS, BoxLayout.Y_AXIS));
		pnlC.setLayout(new BorderLayout());
		pnlE.setLayout(new FlowLayout());
		pnlW.setLayout(new FlowLayout());
		this.add(pnlN, BorderLayout.NORTH);
		this.add(pnlS, BorderLayout.SOUTH);
		this.add(pnlC, BorderLayout.CENTER);
		this.add(pnlE, BorderLayout.EAST);
		this.add(pnlW, BorderLayout.WEST);
		this.setOpaque(true);
		pnlN.setOpaque(true);
		pnlS.setOpaque(true);
		pnlC.setOpaque(true);
		pnlE.setOpaque(true);
		pnlW.setOpaque(true);

	}

	public void addCenter(Component comp) {
		pnlC.add(comp);
	}
	// -------------------------------------------------------------------------
	// initGui《 InfChilePanel 》
	// -------------------------------------------------------------------------
	@Override
	public void initGui(int pWidth, int pHeight, Object pParent,
			LayoutManager layout) {
	}

	// public Component add(Component comp) {
	// pnlC.add(comp);
	// return comp;
	// }

	// //-------------------------------------------------------------------------
	// // 描画
	// //-------------------------------------------------------------------------
	// public void paint(Graphics g){
	// super.paint(g); // Super経由でpaintCompをよぶ
	// }
	// //-------------------------------------------------------------------------
	// // 描画
	// //-------------------------------------------------------------------------
	// public void paintComponent(Graphics g){
	// // new Color(120,244,120)); //明るいグリーン
	// //kyPkg.util.Ruler.drawRuler(g,this.getSize().width,this.getSize().height,new
	// Color(128,255,128,250));
	// kyPkg.util.Ruler.drawRuler(g,this.getSize().width,this.getSize().height,
	// new Color(155,187,244,200));
	// }
}
