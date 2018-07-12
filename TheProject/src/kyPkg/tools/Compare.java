package kyPkg.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import globals.ResControl;
import kyPkg.uFile.FileUtil;

public class Compare {

	/**
	 * *************************************************************************
	 * **
	 */
	// < Encode >--------------------------------------------------------------
	// 正準名 説明
	// ISO2022JP JIS X 0201、ISO 2022 形式の 0208、日本語
	// SJIS Shift-JIS、日本語
	// JISAutoDetect Shift-JIS、EUC-JP、ISO 2022 JP の検出および変換
	// ASCII American Standard Code for Information Interchange
	// MS932 Windows 日本語
	// UTF8 08 ビット Unicode Transformation Format
	// UTF-16 16 ビット Unicode Transformation Format、
	// 必須の初期バイト順マークによって指定されたバイト順
	// UnicodeLittle 16 ビット Unicode Transformation Format
	// リトルエンディアンバイト順、バイト順マーク付き
	// UnicodeLittleUnmarked 16 ビット Unicode Transformation Format
	// リトルエンディアンバイト順
	// UnicodeBig 16 ビット Unicode Transformation Format
	// ビッグエンディアンバイト順、バイト順マーク付き
	// UnicodeBigUnmarked 16 ビット Unicode Transformation Format
	// ビッグエンディアンバイト順
	// Cp1252 Windows ラテン文字-1
	// ISO8859_1 ISO 8859-1、ラテンアルファベット No. 1
	// ※intel系はリトルエンディアン
	// ------------------------------------------------------------------------
	/***************************************************************************
	 * ファイルを比較する<br>
	 * 
	 * @param sBuf
	 *            比較結果を表示するJTextArea
	 * @param pFile1
	 *            読み込むファイルのパス
	 * @param pFile2
	 *            読み込むファイルのパス
	 **************************************************************************/
	private List<String> duffList;

	public List<String> getDuffList() {
		return duffList;
	}

	private String status = "";

	private String path1;

	private String path2;

	private int limit = Integer.MAX_VALUE;
	private boolean trim;

	private long lineCount;// 比較した行数

	private int diffCount;// 違った件数

	private String name1;

	private String name2;

	// Statにファイル名も載せたくなったので追加した
	private String about() {
		String about = name1 + "　と　" + name2 + "　について,　";
		if (name1.equals(name2)) {
			about = name1 + "　について,　";
		}
		return about;
	}

	// -------------------------------------------------------------------------
	// アンマッチ件数
	// -------------------------------------------------------------------------
	public int getDiffCount() {
		return diffCount;
	}

	// -------------------------------------------------------------------------
	// マッチ件数
	// -------------------------------------------------------------------------
	public long getMatchCount() {
		return lineCount;
	}

	// -------------------------------------------------------------------------
	// 比較した結果
	// -------------------------------------------------------------------------
	public String getStatus() {
		return about() + status;
	}

	// -------------------------------------------------------------------------
	// 比較した結果およびその内容
	// -------------------------------------------------------------------------
	public String getResultAsString() {
		return kyPkg.util.Joint.join(duffList, "\n");
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Compare(String path1, String path2) {
		this.path1 = path1;
		this.path2 = path2;
		name1 = FileUtil.getName(path1);
		name2 = FileUtil.getName(path2);
	}

	public Compare(String path1, String path2, int limit, boolean trim) {
		this(path1, path2);
		this.limit = limit;
		this.trim = trim;
	}

	// -------------------------------------------------------------------------
	// 比較をしてその結果を返す
	// -------------------------------------------------------------------------
	public String compareAndGetStatRez() {
		compare();
		if (duffList.size() == 0) {
			return getStatus();
		} else {
			return getStatus() + "\n" + getResultAsString();
		}
	}

	public String compareAndGetStat() {
		compare();
		return getStatus();
	}

	// -------------------------------------------------------------------------
	// 0:アンマッチが０件、且つ空ではない(つまり、すべて一致した)
	// -1:path1ファイルが存在しなかった
	// -2:path2ファイルが存在しなかった
	// -3:どちらかのレコードが空
	// 1以上・・アンマッチ（違っている件数）
	// -------------------------------------------------------------------------
	public int compare() {
		this.lineCount = 0;
		this.diffCount = 0;
		this.status = "";
		// this.result = "";
		if (FileUtil.iFileChk(path1) == null) {
			status = "ファイル1が存在しません（処理を中断しました）->" + path1;
			this.diffCount = -1;
			return this.diffCount;
		}
		if (FileUtil.iFileChk(path2) == null) {
			status = "ファイル2が存在しません（処理を中断しました）->" + path2;
			this.diffCount = -2;
			return this.diffCount;
		}
		// StringBuffer sBuf = new StringBuffer(2048);
		duffList = new ArrayList();
		File file1 = new File(path1);
		File file2 = new File(path2);
		try {
			String pRec1;
			String pRec2;
			String wRec1;
			String wRec2;
			BufferedReader br1 = FileUtil.getBufferedReader(path1);
			BufferedReader br2 = FileUtil.getBufferedReader(path2);
//			BufferedReader br1 = new BufferedReader(new FileReader(file1));
//			BufferedReader br2 = new BufferedReader(new FileReader(file2));
			// -----------------------------------------------------------------
			while ((br1.ready() == true) && (br2.ready() == true)
					&& (diffCount < limit)) {
				lineCount++;
				pRec1 = br1.readLine();
				pRec2 = br2.readLine();
				wRec1 = new String(pRec1.getBytes("SJIS"), "SJIS");
				wRec2 = new String(pRec2.getBytes("SJIS"), "SJIS");
				if (trim) {
					wRec1 = wRec1.trim();
					wRec2 = wRec2.trim();
				}
				if (wRec1.equals(wRec2) == false) {
					diffCount++;
					int pos = kyPkg.tools.Compare.diffPos(wRec1, wRec2) + 1;
					if (pos < wRec1.length()) {
						duffList.add("Line:" + lineCount + " Col:" + pos + " <"
								+ wRec1.substring(pos - 1, pos) + ">");
					} else {
						duffList.add("Line:" + lineCount + " Col:" + pos + "");
					}
					duffList.add("Rec1:" + wRec1);
					duffList.add("Rec2:" + wRec2);
				}
			}
			br1.close();
			br2.close();
			// -----------------------------------------------------------------
			// ※すべてマッチした場合どうする？？？？
			// -----------------------------------------------------------------
			if (lineCount == 0) {
				this.diffCount = -3;
				status = "どちらかのレコードが空なので比較を行いませんでした。";
			} else {
				if (diffCount == 0) {
					status = lineCount + "レコードの比較を行い、全レコードが一致いたしました。";
				} else {
					if (diffCount >= limit) {
						status = "不一致の上限" + limit
								+ "件に達したので比較処理を中断しました（一致しませんでした）";
					} else {
						status = lineCount + "レコードの比較を行い" + diffCount
								+ "件、一致しませんでした";
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return this.diffCount;
	}

	// -------------------------------------------------------------------------
	// 文字列を比較して異なる文字が最初に出現した位置(ゼロから始まる)を返す
	// -------------------------------------------------------------------------
	public static int diffPos(String str1, String str2) {
		char[] array1 = str1.toCharArray();
		char[] array2 = str2.toCharArray();
		int len = 0;
		if (array1.length > array2.length) {
			len = array2.length;
		} else {
			len = array1.length;
		}
		for (int i = 0; i < len; i++) {
			// System.out.println("array1[i]:"+array1[i]);
			// System.out.println("array2[i]:"+array2[i]);
			if (array1[i] != array2[i])
				return i;
		}
		if (array1.length != array2.length) {
			return len;
		}
		return -1;
	}

	public static void main(String[] argv) {
		test2();
	}

	public static void test2() {
		System.out.println("test0:" + kyPkg.tools.Compare.diffPos("a", "a"));
		System.out.println(
				"test1:" + kyPkg.tools.Compare.diffPos("test", "test"));
		System.out.println("test2:" + kyPkg.tools.Compare.diffPos("a", "b"));
		System.out.println("test3:" + kyPkg.tools.Compare.diffPos("", "b"));
		System.out.println(
				"test4:" + kyPkg.tools.Compare.diffPos("testA", "testB"));
	}

	public static void test() {
		String path1 = ResControl.D_DAT + "NQFACE1.DAT";
		String path2 = ResControl.D_DAT + "NQFACE2.DAT";
		String result = new kyPkg.tools.Compare(path2, path1)
				.compareAndGetStatRez();
		System.out.println(result);
	}

}
