package kyPkg.panelsc;

import static kyPkg.util.Joint.join;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import globals.ResControl;
import kyPkg.etc.AliasRes;
import kyPkg.uFile.FileUtil;

// ============================================================================
public class Pn_Cnv2StdAlone extends Pn_Scaffold {
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
		// Z:\s2\rx\Enquetes\NQ\属性・性年代編\2008

		
		commonRes.setDefaultInPath("G:/s2/rx/Enquetes/NQ/");
		jBt01 = new JButton("test");
		jBt01.setBounds(200, 200, 150, 20);
		// ---------------------------------------------------------------------
		pnlSouth.pnlS.add(jBt01);
		// ---------------------------------------------------------------------
		// Test
		// ---------------------------------------------------------------------
		jBt01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String aMessage = "";
				String path = commonRes.getInPath(0);
				String outDir = "";
				String aliasDir = "";
				path = FileUtil.normarizeIt(path);
				System.out.println("path2:" + path);

				String[] splited = path.split("/");
				if (splited.length > 3) {
					// XXX joinの拡張について未検証のため、明日（２・４）テストを行う！！
					aliasDir = join(splited, "/");
				}
				if (splited.length > 3) {
					outDir = ResControl.getUsersEnqDir()  + splited[splited.length - 3]
							+ "/" + splited[splited.length - 2] + "/";
				}
				System.out.println("aliasDir:" + aliasDir);
				System.out.println("outDir:" + outDir);
				aMessage = aMessage + "aliasDir:" + aliasDir + "\n";
				aMessage = aMessage + "outDir:" + outDir;
				commonRes.setTextOut(aMessage);
				System.out.println("#20130402#checkpoint 001");
				AliasRes aliasRes = new AliasRes(aliasDir);
				aliasRes.saveAsStandAlone(outDir);
			}
		});
	}
	public static void main(String[] argv){
		standAlone(new Pn_Cnv2StdAlone(),"Csv2");
	}
	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public Pn_Cnv2StdAlone() {
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