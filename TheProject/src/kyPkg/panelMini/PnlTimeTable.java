package kyPkg.panelMini;
import java.awt.*;
import javax.swing.*;

import kyPkg.util.Ruler;
public class PnlTimeTable extends JPanel{
 	private static final long serialVersionUID = -3679681341587082210L;
	private JCheckBox jCb_xxx[];
	//-----------------------------------------------------------------------
	//　コンストラクタ   
	//-----------------------------------------------------------------------
	public PnlTimeTable( ){
		super();
		this.setLayout(new BorderLayout());
		// ---------------------------------------------------------------------
		// チェックボックスパネル _xxx
		// ---------------------------------------------------------------------
		JPanel wPnl_xxx = new JPanel() {
			private static final long serialVersionUID = -52113885765424933L;
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
						new Color(255, 255, 128));
			}
		};
		// wPnl_xxx.setBackground(Color.WHITE); //パネルの背景色
		wPnl_xxx.setPreferredSize(new Dimension(80, 480));
		wPnl_xxx.setLayout(null);
		// ---------------------------------------------------------------------
		// Point wPoint = jViewp.getViewPosition();
		// System.out.println("wPoint:"+wPoint );
		// ---------------------------------------------------------------------

		jCb_xxx = new JCheckBox[24];
		jCb_xxx[0] = new JCheckBox(" 0:00");
		jCb_xxx[1] = new JCheckBox(" 1:00");
		jCb_xxx[2] = new JCheckBox(" 2:00");
		jCb_xxx[3] = new JCheckBox(" 3:00");
		jCb_xxx[4] = new JCheckBox(" 4:00");
		jCb_xxx[5] = new JCheckBox(" 5:00");
		jCb_xxx[6] = new JCheckBox(" 6:00");
		jCb_xxx[7] = new JCheckBox(" 7:00");
		jCb_xxx[8] = new JCheckBox(" 8:00");
		jCb_xxx[9] = new JCheckBox(" 9:00");		jCb_xxx[9].setSelected(true);
		jCb_xxx[10] = new JCheckBox("10:00");		jCb_xxx[10].setSelected(true);
		jCb_xxx[11] = new JCheckBox("11:00");		jCb_xxx[11].setSelected(true);
		jCb_xxx[12] = new JCheckBox("12:00");		jCb_xxx[12].setSelected(true);
		jCb_xxx[13] = new JCheckBox("13:00");		jCb_xxx[13].setSelected(true);
		jCb_xxx[14] = new JCheckBox("14:00");		jCb_xxx[14].setSelected(true);
		jCb_xxx[15] = new JCheckBox("15:00");		jCb_xxx[15].setSelected(true);
		jCb_xxx[16] = new JCheckBox("16:00");		jCb_xxx[16].setSelected(true);
		jCb_xxx[17] = new JCheckBox("17:00");		jCb_xxx[17].setSelected(true);
		jCb_xxx[18] = new JCheckBox("18:00");		jCb_xxx[18].setSelected(true);
		jCb_xxx[19] = new JCheckBox("19:00");		jCb_xxx[19].setSelected(true);
		jCb_xxx[20] = new JCheckBox("20:00");		jCb_xxx[20].setSelected(true);
		jCb_xxx[21] = new JCheckBox("21:00");		jCb_xxx[21].setSelected(true);
		jCb_xxx[22] = new JCheckBox("22:00");
		jCb_xxx[23] = new JCheckBox("23:00");
		for (int n = 0; n < 24; n++) {
			jCb_xxx[n].setBounds(0, (n * 20 + 5), 200, 20);
			jCb_xxx[n].setOpaque(false);
			wPnl_xxx.add(jCb_xxx[n]);
		}
		JScrollPane jSp0 = new JScrollPane(wPnl_xxx); 
		JViewport jViewp = jSp0.getViewport();
		jViewp.setViewPosition(new Point(0, 180)); // 左上隅座標を指定
		this.add(jSp0,BorderLayout.CENTER);
	}
	public boolean getHoge(int hour){
		return jCb_xxx[hour].isSelected();
	}
}