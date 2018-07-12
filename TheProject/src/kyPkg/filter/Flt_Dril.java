package kyPkg.filter;
import java.io.*;
public class Flt_Dril extends Abs_FileFilter {
	/*
	 * --------------------------------------------------------------------
	 * 《概要》反復練習問題をｃｓｖ形式に変換する
	 * 
	 * 《使用例 その壱》 import kyPkg.filter.*; FltDril ft = new FltDril();
	 * ft.setReader(jTa1.getText()); ft.convert(); String rtn = ft.getString();
	 * jTa2.setText(rtn);
	 * 
	 * 《使用例 其の弐》 import kyPkg.filter.*; String wPath1 = jTf1.getText(); if(new
	 * File(wPath1).exists() == false ) { String wErr = "入力データが存在していません";
	 * JOptionPane.showMessageDialog((Component)null,wErr
	 * ,"Message...",JOptionPane.ERROR_MESSAGE ); return; } FltDril ft = new
	 * FltDril(); ft.openIt(wPath1); String wEndMsg = ft.convert();
	 * ft.closeIt(); JOptionPane.showMessageDialog((Component)null,wEndMsg
	 * ,"Message...",JOptionPane.INFORMATION_MESSAGE); ft = null;
	 * --------------------------------------------------------------------
	 */
 	public Flt_Dril() {
		super(true);
	}
	// -------------------------------------------------------------------------
	// 文字実態に変換する
	// -------------------------------------------------------------------------
	String convEsc(String pStr) {
		String[][] map001 = { { " ", "&nbsp;" },
				{ "\t", "&nbsp;&nbsp;&nbsp;&nbsp;" } };
		for (int i = 0; i < map001.length; i++) {
			if (map001[i].length == 2) {
				pStr = pStr.replaceAll(map001[i][0], map001[i][1]);
			}
		}
		return pStr;
	}
	// -------------------------------------------------------------------------
	// テキストをストリームとして扱うには・・・
	// StringReader in = new StringReader(jTa1.getText());
	// BufferedReader br = new BufferedReader(in);
	// -------------------------------------------------------------------------
	// filterT フィルタープログラム
	// 指定されたストリームを読み込んで、ストリームへ書き出す
	// 例 boolean swt = filterT( new StringReader(jTa1.getText()) );
	// -------------------------------------------------------------------------
	@Override
	public boolean execute() {
		if (getReader() == null) {
			System.out.println("最初にsetReader処理が必要です");
			super.setStat("ERROR");
			return false;
		}
		if (getWriter() == null) {

		}
		try {
			BufferedReader br = new BufferedReader(getReader());
			StringBuffer wBuf = new StringBuffer();
			StringBuffer wQes = new StringBuffer();
			StringBuffer wPgm = new StringBuffer();
			StringBuffer wSel = new StringBuffer();
			// ---------------------------------------------------------------------
			// header 等幅・・・ Teletyped Text => TT
			// ---------------------------------------------------------------------
			// out.write("<TT>");
			// ---------------------------------------------------------------------
			// body スペースのみのセルをどうスル？
			// ---------------------------------------------------------------------
			String wRec = "";
			char wChar = 'A' - 1; // = @ 選択肢に付ける回答記号
			short wSeq = 0; // ソースコード用行番号
			String wQSeq = ""; // 設問番号
			String wAnswer = ""; // 解答
			while ((wRec = br.readLine()) != null) {
				if (!wRec.equals("")) {
					if (wRec.startsWith("//")) {
					} else if (wRec.startsWith("@Q")) { // 設問番号部分
						// -----------------------------------------------------
						// Break!!
						if (wQSeq != "") {
							wBuf.append(wQSeq + "<br>");
							wBuf.append("<input type=Hidden name=qsq value='"
									+ wQSeq + "'>");
							wBuf.append("\t");
							wBuf.append("<input type=Hidden name=ans value='"
									+ wAnswer + "'>");
							wBuf.append("\t");
							wBuf.append(wQes);
							wBuf.append("\t");
							wBuf.append(wPgm);
							wBuf.append("\t");
							wBuf.append(wSel);
							wBuf.append("$$");
							wBuf.append(System.getProperty("line.separator"));
							getWriter().write(wBuf.toString());
							// Clear
							wQes.delete(0, wQes.length());
							wPgm.delete(0, wPgm.length());
							wSel.delete(0, wSel.length());
							wBuf.delete(0, wBuf.length());
						}
						// -----------------------------------------------------
						wChar = 'A' - 1; // = @ 選択肢に付ける回答記号
						wSeq = 0; // ソースコード用行番号
						wQSeq = wRec.trim().substring(3);
					} else if (wRec.startsWith("@A")) { // 解答部分
						// 解答はいくつあるか？ によって カンマの数をかぞえる？
						// ＝＞正しいものをＡ～？から？つ選びなさい。のコメントを作成
						wAnswer = wRec.trim().substring(5);
						String[] wAnsArray = wAnswer.split(",");
						wQes.append("(" + wAnsArray.length + "個選択)");
						wQes.append("<BR>");

					} else {
						if (wRec.startsWith("\t")) { // ソースコード部分
							wRec = convEsc(wRec);
							wSeq++;
							if (wSeq < 10) {
								wPgm.append("0" + wSeq);
							} else {
								wPgm.append("" + wSeq);
							}
							wPgm.append(":");
							wPgm.append(wRec);
							wPgm.append("<BR>");
						} else if (wRec.startsWith(":")) { // 選択肢部分
							wRec = convEsc(wRec);
							wChar++;
							wSel
									.append("<input type='checkbox' name='objP1' value='"
											+ wChar + "'>");
							wSel.append(wChar);
							wSel.append(wRec);
							wSel.append("<BR>");
						} else {
							wRec = convEsc(wRec);
							wQes.append(wRec);
							wQes.append("<BR>");
						}
					}
				}
			}
			// -----------------------------------------------------
			// Break!!
			if (wQSeq != "") {
				wBuf.append(wQSeq + "<br>");
				wBuf.append("<input type=Hidden name=qsq value='" + wQSeq+ "'>");
				wBuf.append("\t");
				wBuf.append("<input type=Hidden name=ans value='" + wAnswer	+ "'>");
				wBuf.append("\t");
				wBuf.append(wQes);
				wBuf.append("\t");
				wBuf.append(wPgm);
				wBuf.append("\t");
				wBuf.append(wSel);
				wBuf.append("$$");
				wBuf.append(System.getProperty("line.separator"));
				getWriter().write(wBuf.toString());
				// Clear
				wQes.delete(0, wQes.length());
				wPgm.delete(0, wPgm.length());
				wSel.delete(0, wSel.length());
				wBuf.delete(0, wBuf.length());
			}
			// -----------------------------------------------------
			// footer
			// -----------------------------------------------------
			// out.write("</TT>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// ---------------------------------------------------------------------
		super.setStat("変換終了" + "\n 出力ファイル： " + this.getOutFile());
		// ---------------------------------------------------------------------

		return true;
	}
	public static void tester() {
		Flt_Dril ft = new Flt_Dril();
		String rootDir = globals.ResControl.getQprRootDir();
		ft.open_I(rootDir+"●勉強/模擬試験１.txt");
		ft.open_O(rootDir+"result.txt",false);
		ft.execute();
		System.out.println("result=>" + ft.getOutFile());
	}
	public static void main(String[] argv) {
		tester();
	}

}
