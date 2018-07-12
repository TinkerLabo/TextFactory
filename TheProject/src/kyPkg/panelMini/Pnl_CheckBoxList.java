package kyPkg.panelMini;
import java.awt.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
//---------------------------------------------------------------------
// 使用例
//---------------------------------------------------------------------
//String[] weekname1 = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
//String[] weekname2 = {"日","月","火","水","金","土"};
//String[] weekname3 = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
//Pnl_CheckBoxList weekTable = new Pnl_CheckBoxList(weekname1);
//weekTable.setBounds(400, 0, 100, 150);
//---------------------------------------------------------------------
public class Pnl_CheckBoxList extends JPanel{
 	private static final long serialVersionUID = -3679681341587082210L;
	private List listData;
	private JCheckBox cbox[];
	//-----------------------------------------------------------------------
	//　コンストラクタ   
	//-----------------------------------------------------------------------
	public Pnl_CheckBoxList(String[] array){
		this(Arrays.asList(array));
	}
	public Pnl_CheckBoxList(List listData){
		super();
		this.listData = listData;
		initGUI();
	}
	private void initGUI(){
		this.setLayout(new BorderLayout());
		JPanel innerPanel = new JPanel(null) {
			private static final long serialVersionUID = -52113885765424933L;
		};
		int n = 0;
		cbox = new JCheckBox[listData.size()];
		for (Iterator iter = listData.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			cbox[n] = new JCheckBox(element);
			cbox[n].setBounds(0, (n * 20 + 5), 200, 20);
			cbox[n].setOpaque(false);
			innerPanel.add(cbox[n]);
			n++;
		}
		innerPanel.setBackground(Color.WHITE); //パネルの背景色
		innerPanel.setPreferredSize(new Dimension(80, listData.size()*20+10 ));
		this.add(new JScrollPane(innerPanel),BorderLayout.CENTER);
	}
	public boolean isSelected(int idx){
		return cbox[idx].isSelected();
	}
	public void checkIt(int idx){
		if (listData.size()>idx){
			cbox[idx].setSelected(true);
		}
	}
}