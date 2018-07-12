package kyPkg.cross;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import kyPkg.mySwing.ListPanel;
import kyPkg.mySwing.MyCheckBox;
import kyPkg.mySwing.MyLabel;
import kyPkg.mySwing.MyPanel;
public class CondPanel3 extends MyPanel {
	private static final long serialVersionUID = 1L;
	private static Color darkRed = new Color(128,0,0);
	public MyLabel LabInfo_ca;
	public MyLabel LabInfo_cb;
	public ListPanel listQtb3;
	public CondPanel3(int x,int y,int width ,int height,LayoutManager layout) {
		super(x, y, width, height,layout);
		setGrid(true);
		MyCheckBox OptionRel1 = new MyCheckBox(10, 20, 100, 20, "Ç`ÇmÇc èåè");
		MyCheckBox OptionRel2 = new MyCheckBox(10, 60, 100, 20, "ÇnÇq   èåè");
		this.add(OptionRel1);
		this.add(OptionRel2);

		listQtb3 = new ListPanel(new Rectangle(400, 0, 200, 100), false, false);
		this.add(listQtb3);
		
		this.add(new MyLabel(150, 00, 100, 20, "èåèÇ`"));
		LabInfo_ca = new   MyLabel(150, 20, 450, 20, "");
		LabInfo_ca.setColor(Color.YELLOW,darkRed);
		
		this.add(new MyLabel(150, 40, 100, 20, "èåèÇa"));
		LabInfo_cb = new   MyLabel(150, 60, 450, 20, "");
		LabInfo_cb.setColor(Color.YELLOW,darkRed);

		this.add(LabInfo_ca);
		this.add(LabInfo_cb);
	}
}

