package kyPkg.tools;

import java.io.*;
import java.util.*;

import kyPkg.uFile.FileUtil;

public class UrlMixer {
	final static String TAB = "\t";
	final static String REF1 = "<BR><A HREF=\"";
	final static String REF2 = "\">";
	final static String REF3 = "</A>";
	final static String LS = System.getProperty("line.separator");

	// -------------------------------------------------------------------------
	// 同階層にある*.urlファイルをすべて読み込んで・・
	// fab.htmlに まとめる・・それだけ
	// -------------------------------------------------------------------------
	// 指定されたファイルを読み込んで、正規表現にマッチするパターンを検査する
	// 使用例 boolean swt = checkIt(wPath_I,".*static\\s*void\\s*main.*");
	// -------------------------------------------------------------------------
	public static String checkURL(String path, FileWriter fw) {
		//System.out.println("checkIt >>" + iPath);
		File iFile = new File(path);
		String wName = iFile.getName();
		String splited[] = wName.split("\\.");
		wName = splited[0]; // ファイル名の頭
		String wRec;
		String wUrl = "";
		String wInfo = "";
		boolean wFlg = false;
		// --------------------------------------------------------------------
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			wUrl = "";
			while ((wRec = br.readLine()) != null) {
				if (wRec.startsWith("[DEFAULT]")) {
					wFlg = true;
				}
				if (wRec.startsWith("[InternetShortcut]")) {
					wFlg = true;
				}
				if (wFlg == true) {
					if (wRec.matches("BASEURL=*.*")) {
						wUrl = wRec.substring(8, wRec.length());
						wInfo = REF1 + TAB + wUrl + TAB + REF2 + TAB + wName
								+ TAB + REF3 + LS;
						break;
					}
					if (wRec.matches("URL=*.*")) {
						wUrl = wRec.substring(4, wRec.length());
//						System.out.println("  wName >>" + wName);
//						System.out.println("  BASEURL >>" + wRec);
						wInfo = REF1 + TAB + wUrl + TAB + REF2 + TAB + wName
								+ TAB + REF3 + LS;
						break;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wInfo;
	}

	// ----------------------------------------------------------------
	// Get月日
	// ----------------------------------------------------------------
	public static String getYY_MM() {
		String wRtn;
		Calendar wCal = Calendar.getInstance();
		int sMM = wCal.get(Calendar.MONTH) + 1;
		int sDD = wCal.get(Calendar.DATE);
		wRtn = "" + sMM + "_" + sDD;
		return wRtn;
	}

	// ----------------------------------------------------------------
	// #xxx ペアレントパスを取得(File.getParent()が使えない時・・)
	// 《使用例》
	// String mamapath = makeParents("c:\ga\bba\gabba\hey.txt");
	// ----------------------------------------------------------------
	public static String getPpath(String pPath) {
		if (pPath.indexOf(".") > 0) { // ファイル名が含まれているか
			int pos = pPath.lastIndexOf(System.getProperty("file.separator"));
			if (pos < 0) {
				System.out.println("#Error Not Directory Path!\n" + "       =>"
						+ pPath);
				return "";
			}
			pPath = pPath.substring(0, pos); // ParentPathを設定し直す
		}
		return pPath;
	}

	// -----------------------------------------------------------
	// ボタンが押された
	// btnFetch.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent arg0) {
	// btnFetch.setEnabled(false);
	// JFileChooser fc = new JFileChooser(gPath);
	// // fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	// // fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	// fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	// if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	// setDefaultPath(fc.getSelectedFile().toString());
	// if (actionListener != null) {
	// actionListener.actionPerformed(arg0);
	// }
	// }
	// btnFetch.setEnabled(true);
	// }
	// });

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		String iPath;
		File f = new File(".");
		try {
			String wkDir = getPpath(f.getAbsolutePath());
			String oPath = wkDir + System.getProperty("file.separator")
					+ getYY_MM() + ".html";
			System.out.println("oPath >>" + oPath);
			FileWriter writer = new FileWriter(new File(oPath));
			// -----------------------------------------------------------------
			// カレントパス上にあるファイルを配列に流し込み
			// -----------------------------------------------------------------
			String[] array = f.list();
			for (int i = 0; i < array.length; i++) {
				iPath = array[i];
				// 拡張子を判定して実処理
				if ((iPath.toLowerCase()).endsWith(".url")) {
					String wInfo = checkURL(iPath, writer);
					if (writer != null && !wInfo.equals("")) {
						writer.write(wInfo);
					}

				}
			}
			// -----------------------------------------------------------------
			writer.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
