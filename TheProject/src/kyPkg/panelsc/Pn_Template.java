package kyPkg.panelsc;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

public class Pn_Template extends Pn_Scaffold {
	public static void main(String[] argv) {
		standAlone(new Pn_Template(), "Title");
	}
	private static final long serialVersionUID = -3066425890995680687L;
	private JButton btnKick01;
	private CommonTabPane commonRes;

	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public Pn_Template() {
		super(640, 480);
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
		btnKick01 = new JButton("kick01");
		btnKick01.setBounds(200, 200, 150, 20);
		pnlSouth.pnlS.add(btnKick01);
		// ---------------------------------------------------------------------
		// Button
		// ---------------------------------------------------------------------
		btnKick01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickOut01();
			}
		});
		commonRes.setTextIn(" c:\\aaa\\test.data\ncall func(\"abc\",'def');");
	}

	// -------------------------------------------------------------------------
	// kickOut01 例＞エスケープ文字に変換　
	// -------------------------------------------------------------------------
	private void kickOut01() {
		String tex = commonRes.getTextIn();
		String[] array = tex.split("\n");
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			String str = array[i];
			str = str.replaceAll("\\\\", "\\\\\\\\");
			str = str.replaceAll("\"", "\\\\\"");
			buf.append("\"" + str + "\"\n");	
		}
		commonRes.setTextOut(buf.toString());
	}
}