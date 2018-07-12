package kyPkg.panelsc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Pn_ProgTool extends Pn_Scaffold {
	private static final long serialVersionUID = -3066425890995680687L;
	private CommonTabPane commonRes;
	private JButton btnKick01;
	private JButton btnKick02;
	private JButton btnKick03;
	private JButton btnKick04;
	private JButton btnKick05;
	private JButton btnKick06;
	private JButton btnKick07;
	private JButton btnKick08;
	private JButton btnKick09;

	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public Pn_ProgTool() {
		super(1000, 480);
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
		pnlSouth.pnlS.setLayout(new FlowLayout());

		btnKick01 = new JButton("エスケープに変換");
		btnKick01.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick01);

		btnKick02 = new JButton("テスター作成");
		btnKick02.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick02);

		btnKick03 = new JButton("Assert作成");
		btnKick03.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick03);

		btnKick04 = new JButton("定数定義");
		btnKick04.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick04);

		btnKick05 = new JButton("リスト化");
		btnKick05.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick05);

		btnKick06 = new JButton("配列化");
		btnKick06.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick06);

		btnKick07 = new JButton("JavaDoc@作成");
		btnKick07.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick07);

		btnKick08 = new JButton("<Table>作成");
		btnKick08.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick08);

		btnKick09 = new JButton("<Table>除去");
		btnKick09.setBounds(200, 200, 200, 20);
		pnlSouth.pnlS.add(btnKick09);

		// ---------------------------------------------------------------------
		// Button01
		// ---------------------------------------------------------------------
		btnKick01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickCnvEsc();
			}
		});
		// ---------------------------------------------------------------------
		// Button02
		// ---------------------------------------------------------------------
		btnKick02.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickCreateTester();
			}
		});
		// ---------------------------------------------------------------------
		// Button03
		// ---------------------------------------------------------------------
		btnKick03.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickAssertMaker();
			}
		});
		// ---------------------------------------------------------------------
		// Button04
		// ---------------------------------------------------------------------
		btnKick04.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickCnvStatics();
			}
		});
		// ---------------------------------------------------------------------
		// Button05
		// ---------------------------------------------------------------------
		btnKick05.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickCnvListSource();
			}
		});
		// ---------------------------------------------------------------------
		// Button06
		// ---------------------------------------------------------------------
		btnKick06.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickCnvArraySource();
			}
		});

		// ---------------------------------------------------------------------
		// Button07
		// ---------------------------------------------------------------------
		btnKick07.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickCreateJavaDocTag();
			}
		});

		// ---------------------------------------------------------------------
		// Button08
		// ---------------------------------------------------------------------
		btnKick08.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickText2TableTag();
			}
		});

		// ---------------------------------------------------------------------
		// Button08
		// ---------------------------------------------------------------------
		btnKick09.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickTagRemover();
			}
		});

		
		// commonRes
		// .setTextIn("kyPkg.util.DateCalc.formatYMD(\"yyyy/MM/dd\",splited[0])");
		// commonRes
		// .modTextIn("public CalcWLev(String outPath, int monCount, List<ResDict> resDicts,int whichBase, int calcType, boolean debug, String outPath2) {");
		// commonRes.modTextIn(" c:\\aaa\\test.data\ncall func(\"abc\",'def');");

		String[] testArray = {
				"kyPkg.util.DateCalc.formatYMD(\"yyyy/MM/dd\",splited[0])",
				"public CalcWLev(String outPath, int monCount, List<ResDict> resDicts,int whichBase, int calcType, boolean debug, String outPath2) {",
				" c:\\aaa\\test.data\ncall func(\"abc\",'def');",
				"メーカー　　　　　　　　　　　:MKR", "区分１　　　　　　　　　　　　:KB1",
				"区分２　　　　　　　　　　　　:KB2", "区分３　　　　　　　　　　　　:KB3",
				"区分４　　　　　　　　　　　　:KB4", "区分５　　　　　　　　　　　　:KB5",
				"区分６　　　　　　　　　　　　:KB6", "アイテム　　　　　　　　　　　:ITM",
				"品目名  　　　　　　　　　　　:HN4",
				"<< 期間１・期間２ >>	00001: こんにゃく麺	00002: 生ラーメン	00003: 生そば	00004: 生うどん	非購入" };
		for (String element : testArray) {
			commonRes.modTextIn(element);
		}

		// str =
		// "	public CalcWLev(String outPath, int monCount, List<ResDict> resDicts,int whichBase, int calcType, boolean debug, String outPath2) {";

	}

	// -------------------------------------------------------------------------
	// kickOut01 例＞エスケープ文字に変換　
	// -------------------------------------------------------------------------
	private void kickCnvEsc() {
		StringBuffer buf = new StringBuffer();
		for (String expression : commonRes.getList()) {
			String ans = kyPkg.tools.AssertMaker.cnvEsc(expression);
			buf.append("\"" + ans + "\"\n");
		}
		commonRes.setTextOut(buf.toString());
	}

	// -------------------------------------------------------------------------
	// テスター作成　
	// -------------------------------------------------------------------------
	private void kickCreateTester() {
		StringBuffer buf = new StringBuffer();
		for (String expression : commonRes.getList()) {
			buf.append(expression + "\n");
		}
		String ans = kyPkg.tools.CreateTester.execute(buf.toString());
		commonRes.setTextOut(ans);
	}

	// -------------------------------------------------------------------------
	// JavaDoc用@paramアノーテーション作成　
	// -------------------------------------------------------------------------
	private void kickCreateJavaDocTag() {
		StringBuffer buf = new StringBuffer();
		for (String expression : commonRes.getList()) {
			buf.append(expression + "\n");
		}
		String ans = kyPkg.tools.CreateJavaDocTag.execute(buf.toString());
		commonRes.setTextOut(ans);
	}

	// -------------------------------------------------------------------------
	// テキスト→テーブルタグ変換
	// -------------------------------------------------------------------------
	private void kickText2TableTag() {
		StringBuffer buf = new StringBuffer();
		for (String expression : commonRes.getList()) {
			buf.append(expression + "\n");
		}
		String ans = kyPkg.tools.Text2TableTag.execute(buf.toString());
		commonRes.setTextOut(ans);
	}

	// -------------------------------------------------------------------------
	// テーブルタグからタグを取り除きます
	// -------------------------------------------------------------------------
	private void kickTagRemover() {
		StringBuffer buf = new StringBuffer();
		for (String expression : commonRes.getList()) {
			buf.append(expression + "\n");
		}
		String ans = kyPkg.tools.TagRemover.execute(buf.toString());
		commonRes.setTextOut(ans);
	}

	// -------------------------------------------------------------------------
	// Assert作成　
	// -------------------------------------------------------------------------
	private void kickAssertMaker() {
		StringBuffer buf = new StringBuffer();
		for (String expression : commonRes.getList()) {
			String ans = kyPkg.tools.AssertMaker.execute(expression);
			buf.append(ans + "\n");
		}
		commonRes.setTextOut(buf.toString());
	}

	// -------------------------------------------------------------------------
	// 定数定義　
	// -------------------------------------------------------------------------
	private void kickCnvStatics() {
		List<String> dataList = new ArrayList();
		for (String expression : commonRes.getList()) {
			if (expression.indexOf(":") > 0) {
				dataList.add(expression);
			}
		}
		List<String> listRes = kyPkg.tools.CnvStatics.execute(dataList);
		StringBuffer buf = new StringBuffer();
		for (String element : listRes) {
			buf.append(element);
			buf.append("\n");
			System.out.println("element>" + element);
		}
		commonRes.setTextOut(buf.toString());
	}

	// -------------------------------------------------------------------------
	// 定数定義　
	// -------------------------------------------------------------------------
	private void kickCnvListSource() {
		List<String> listRes = kyPkg.tools.CnvListSource
				.execute(commonRes.getList());
		StringBuffer buf = new StringBuffer();
		for (String element : listRes) {
			buf.append(element);
			buf.append("\n");
			System.out.println("element>" + element);
		}
		commonRes.setTextOut(buf.toString());
	}

	// -------------------------------------------------------------------------
	// 定数定義　
	// -------------------------------------------------------------------------
	private void kickCnvArraySource() {
		List<String> listRes = kyPkg.tools.CnvArraySource
				.execute(commonRes.getList());
		StringBuffer buf = new StringBuffer();
		for (String element : listRes) {
			buf.append(element);
			buf.append("\n");
			System.out.println("element>" + element);
		}
		commonRes.setTextOut(buf.toString());
	}

	public static void main(String[] argv) {
		standAlone(new Pn_ProgTool(), "ProgTool??");
	}

}