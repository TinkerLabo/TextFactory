package kyPkg.panelsc;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

// ============================================================================
public class PnlDataCutter  extends Pn_Scaffold  {
	public static void main(String[] argv){
		standAlone(new PnlDataCutter(),"Cutter");
	}
	private static final long serialVersionUID = -3066425890995680687L;
	// -------------------------------------------------------------------------
	// Local変数
	// -------------------------------------------------------------------------
	private JButton jBt01;
	private CommonTabPane commonRes;
	// -------------------------------------------------------------------------
	// 《ＧＵＩ関連》
	// -------------------------------------------------------------------------
	void createGUI() {
//		super.createGUI();
		jBt01 = new JButton("test");			jBt01.setBounds(200, 200, 150, 20);
		// ---------------------------------------------------------------------
		pnlSouth.pnlS.add(jBt01);
		// ---------------------------------------------------------------------
		// Test
		// ---------------------------------------------------------------------
		jBt01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String parm = commonRes.getTextOut();
				String ans = kyPkg.converter.ValueChecker.classifyOld3(parm,"1","@");
				commonRes.setTextOut(ans);
			}
		});
	}
	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public PnlDataCutter() {
		super(640, 480);
		int option1 = -1;
		int option2 = -1;
		if (option1 != 0 & option2 != 0) {
			commonRes = new CommonTabPane("input", "outout", option1, option2);
			add(commonRes, BorderLayout.CENTER);
		}
		createGUI(); // GUI部作成
	}
}