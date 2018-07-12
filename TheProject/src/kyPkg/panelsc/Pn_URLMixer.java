package kyPkg.panelsc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class Pn_URLMixer extends Pn_Scaffold {
	public static void main(String[] argv) {
		standAlone(new Pn_URLMixer(), "URL Mix");
	}

	private static final long serialVersionUID = -3066425890995680687L;
	private JButton btnMixIt;
	private CommonTabPane commonRes;

	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public Pn_URLMixer() {
		super(640, 480);
		this.setSize(new Dimension(640,480));
		this.setPreferredSize(new Dimension(640,480));

		int option1 = -1;
		int option2 = -1;
		if (option1 != 0 & option2 != 0) {
			commonRes = new CommonTabPane("input", "outout", option1, option2);
			add(commonRes, BorderLayout.CENTER);
		}
		createGUI(); // GUI部作成
	}

	// -------------------------------------------------------------------------
	// create GUI
	// -------------------------------------------------------------------------
	void createGUI() {
		btnMixIt = new JButton("MixIt");
		btnMixIt.setBounds(200, 200, 150, 20);
		pnlSouth.pnlS.add(btnMixIt);
		// ---------------------------------------------------------------------
		// Button
		// ---------------------------------------------------------------------
		btnMixIt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickOut01();
			}
		});
	}

	// -------------------------------------------------------------------------
	// kickOut01
	// -------------------------------------------------------------------------
	private void kickOut01() {
		String path = commonRes.getInPath(0);
		File file = new File(path);
		System.out.println("Path:" + path);
		String wkDir = file.getParent() + System.getProperty("file.separator");
		System.out.println("wkDir:" + wkDir);
		String oPath = wkDir + kyPkg.tools.UrlMixer.getYY_MM() + ".html";
		System.out.println("oPath >>" + oPath);
		File f = new File(wkDir);
		String iPath;
		try {
			FileWriter writer = new FileWriter(new File(oPath));
			// -----------------------------------------------------------------
			// XXX いったんリストにパスを格納して、ファイルの更新日付などでソートできるようにできると便利
			// 　出力のｃｓｖ化は可能か？？
			// -----------------------------------------------------------------
			String[] array = f.list();
			for (int i = 0; i < array.length; i++) {
				iPath = array[i];
				// 拡張子を判定して実処理
				if ((iPath.toLowerCase()).endsWith(".url")) {
					commonRes.modTextIn(iPath);
					String wInfo = kyPkg.tools.UrlMixer.checkURL(wkDir + iPath,
							writer);
					if (writer != null && !wInfo.equals("")) {
						writer.write(wInfo);
					} else {
						System.out.println("not printed iPath:" + iPath);
					}
				}
			}
			// -----------------------------------------------------------------
			writer.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		commonRes.setTextOut("Finish!");
	}
}