package kyPkg.panelsc;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import kyPkg.uFile.FileUtil;

// ============================================================================
public class Pn_DupWalker extends Pn_Scaffold  {
	private static final long serialVersionUID = -3066425890995680687L;
	// -------------------------------------------------------------------------
	// Local変数
	// -------------------------------------------------------------------------
	private JButton jBt01;
	private JCheckBox chkFlag;
	private CommonTabPane commonRes;
	// -------------------------------------------------------------------------
	// 《ＧＵＩ関連》
	// -------------------------------------------------------------------------
	void createGUI() {
//		super.createGUI();
		jBt01 = new JButton("実行");			jBt01.setBounds(200, 200, 150, 20);

		chkFlag = new JCheckBox("killer",false);
		chkFlag.setBounds(780,90,120,20);
		//chkFlag.setEnabled(false);
		chkFlag.setOpaque(false);

		pnlSouth.pnlS.add(jBt01);
		pnlSouth.pnlS.add(chkFlag);
		
		// ---------------------------------------------------------------------
		// Test
		// ---------------------------------------------------------------------
		jBt01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wPath = commonRes.getInPath(0);
				System.out.println("wPath:"+wPath);
				boolean flag = chkFlag.isSelected();
				//flag=false;
				kyPkg.uFile.DupWalker.dupWalker(wPath.trim()+"/",flag);
				commonRes.setTextOut(FileUtil.file2String("./dupWalker.log"));
			}
		});
	}
	public static void main(String[] argv){
		standAlone(new Pn_DupWalker(),"DupWalker");
	}

	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public Pn_DupWalker() {
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