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
		MyButton comClr_a = new MyButton(500, 0, 150, 20, "条件をクリア");
		MyButton ComJamin_a = new MyButton(500, 20, 150, 20, "件数確認");

		MyCheckBox OptionRel1a = new MyCheckBox(500, 40, 100, 20, "ＡＮＤ 条件");
		MyCheckBox OptionRel2a = new MyCheckBox(500, 60, 100, 20, "ＯＲ   条件");

		LabInfo_b = new   MyLabel(500, 80, 100, 20, "条件設定後検索ボタンを押してください!!");
		LabInfo_b.setColor(Color.YELLOW,darkRed);
		this.add(LabInfo_b);

		this.add(listQtb3b);
		this.add(comClr_a);
		this.add(ComJamin_a);
		this.add(OptionRel1a);
		this.add(OptionRel2a);
	}


}
