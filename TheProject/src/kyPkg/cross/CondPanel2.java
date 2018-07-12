package kyPkg.cross;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import kyPkg.mySwing.ListPanel;
import kyPkg.mySwing.MyButton;
import kyPkg.mySwing.MyCheckBox;
import kyPkg.mySwing.MyLabel;
import kyPkg.mySwing.MyPanel;

public class CondPanel2 extends MyPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Color darkRed = new Color(128,0,0);
	private ListPanel listQtb3b;
	private MyLabel LabInfo_b;
	public void comClr_a_Click() {
		LabInfo_b.setText("");
//		LabInfo_ca.setText("");
		listQtb3b.clear();
	}

	public CondPanel2(int x, int y, int width, int height, LayoutManager layout) {
		super(x, y, width, height, layout);
		this.setGrid(true);
		listQtb3b = new ListPanel(new Rectangle(0, 0, 500, 100), false, false);
		MyButton comClr_a = new MyButton(500, 0, 150, 20, "ğŒ‚ğƒNƒŠƒA");
		MyButton ComJamin_a = new MyButton(500, 20, 150, 20, "Œ”Šm”F");

		MyCheckBox OptionRel1a = new MyCheckBox(500, 40, 100, 20, "‚`‚m‚c ğŒ");
		MyCheckBox OptionRel2a = new MyCheckBox(500, 60, 100, 20, "‚n‚q   ğŒ");

		LabInfo_b = new   MyLabel(500, 80, 100, 20, "ğŒİ’èŒãŒŸõƒ{ƒ^ƒ“‚ğ‰Ÿ‚µ‚Ä‚­‚¾‚³‚¢!!");
		LabInfo_b.setColor(Color.YELLOW,darkRed);
		this.add(LabInfo_b);

		this.add(listQtb3b);
		this.add(comClr_a);
		this.add(ComJamin_a);
		this.add(OptionRel1a);
		this.add(OptionRel2a);
	}


}
