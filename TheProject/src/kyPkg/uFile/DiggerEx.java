package kyPkg.uFile;

import java.io.*;
import java.util.regex.*;

import kyPkg.uDateTime.DateCalc;
import kyPkg.uDateTime.DateUtil;
import kyPkg.uRegex.Regex;

//20150323 検索対象が大きい時に対応できるようなバージョンを作りたいと考えた
//クローじゃのような内部処理クラスをインターフェースにもたせ、スタティックなリストを持たせないようにしたい

/*******************************************************************************
 * 《 Digger_neo 》 2007-05-11 <BR>
 * カレント・ディレクトリの下を再帰的に探索して 指定された名前のファイルを見つけて表示するクラス
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 ******************************************************************************/
public class DiggerEx {
	private static final String FS = System.getProperty("file.separator");
	private static String regex = null;// 正規表現文字パターン
	private static Pattern pattern = null; // コンパイル済みパターン
	private static boolean recursive = false; // サブフォルダを検索するかどうか
	// private boolean ignoreCase = false;// 大文字小文字の区別をするかどうか
	private static boolean interrupt = false;// 中断させる

	public static void interrupt() {
		DiggerEx.interrupt = true;
	}

	private static Inf_OnMatch matcher = null;// 各クラスでオブジェクトを持つと重くなるのでstatic

	private String rootPath; // ディレクトリのパス名
	private File[] fileArray; // ディレクトリ内のファイルオブジェクト

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	// dir 対象とするパス
	// regex 処理対象とするファイルの正規表現パターン
	// recursive サブディレクトリ以下も対象とするかどうか
	// -------------------------------------------------------------------------
	public DiggerEx(String dir, String regFileName, boolean ignoreCase,
			boolean recursive, Inf_OnMatch matcher) {
		interrupt = false;
		DiggerEx.matcher = matcher;
		matcher.init();

		if (dir == null)
			return;
		if (isDirExists(dir) == false)
			return;
		DiggerEx.recursive = recursive;
		File fileObj = new File(dir);
		if (!fileObj.exists()) {
			System.out.println("Error@Digger Path is not exists:" + dir);
			return;
		}
		setCur(fileObj);
		setRegFileName(regFileName, ignoreCase);
	}

	private DiggerEx(File fileObj) {
		setCur(fileObj);
	}

	// -----------------------------------------------------------------
	// 正規表現文字とかぶるものについて変更しなければならない
	// 文字列を表現するregixに簡易変換する
	// 括弧など、問題あるだろうね・・・・
	// 元の文字列はregixでは無いという前提で変換している・・
	// -----------------------------------------------------------------
	private void setRegFileName(String regFileName, boolean ignoreCase) {
		if (regFileName == null || regFileName.equals(""))
			regFileName = ""; // パターン指定が空だった場合
		// regFileName = escapeIt(regFileName);
		// System.out.println("#debug# 20150402 regFileName:"+regFileName);
		DiggerEx.regex = regFileName;
		DiggerEx.pattern = Regex.getPatternEx(regFileName, ignoreCase); // 正規表現パターンをコンパイル
	}

	private void setCur(File fileObj) {
		try {
			if (fileObj.isDirectory()) {
				// rootPath = fileObj.getCanonicalPath();
				rootPath = fileObj.getAbsolutePath();
				if (!rootPath.endsWith(FS))
					rootPath = rootPath + FS;
				fileArray = fileObj.listFiles();// カレントデイレクトリ上のファイル一覧
			} else {
				// -----------------------------------------------------------------
				// 存在しないディレクトリを指定された場合のエラー処理の取り回しのしかた
				// 指定したパスが存在するディレクトリなのかどうか・・・・
				// -----------------------------------------------------------------
				System.out.println("Error　not Directory:" + fileObj.toString());
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// サブディレクトリを探索する (再帰的に行う場合recursive==trueとする)
	// -------------------------------------------------------------------------
	private void searchSubDirs() throws Exception {
		if (fileArray == null)
			return;
		for (int i = 0; i < fileArray.length; i++) {
			String name = fileArray[i].getName();
			String absPath = fileArray[i].getAbsolutePath();
			if (fileArray[i].isDirectory()) {
				if (regex.equals(".*")
						|| DiggerEx.pattern.matcher(name).matches()) {
					// System.out.println("match:" + absPath);
					matcher.onMatch(fileArray[i]);
					// matcher.forDebug(absPath);
				}
				if (recursive) {
					try {
						DiggerEx subIns = new DiggerEx(fileArray[i]);
						subIns.searchSubDirs();
					} catch (Exception e) {
						continue;
					}
				}
			} else {
				if (regex.equals(".*")
						|| DiggerEx.pattern.matcher(name).matches()) {
					// System.out.println("match:" + absPath);
					matcher.onMatch(fileArray[i]);
					// matcher.forDebug(absPath);
				}
			}
			if (interrupt) // 中断フラグを参照する
				break;
		}
	}

	// -------------------------------------------------------------------------
	// 検索
	// -------------------------------------------------------------------------
	public void search() {
		try {
			this.searchSubDirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// ルートパスを返す
	// -------------------------------------------------------------------------
	public String getRootPath() {
		return rootPath;
	}

	public int getRootPathLength() {
		return rootPath.length();
	}

	private static boolean isDirExists(String dir) {
		File wFile = new File(dir);
		if (!wFile.exists()) {
			System.out.println(
					"Digger ### Error@isDirExists  not exists=>" + dir);
			return false;
		}
		return true;
	}

	public String escapeIt(String regex) {
		regex = regex.replaceAll("\\\\", "\\\\");
		regex = regex.replaceAll("\"", "\\\\\"");
		regex = regex.replaceAll("\'", "\\\\\'");
		regex = regex.replaceAll("\t", "\\\\t");
		regex = regex.replaceAll("\\.", "\\\\.");
		regex = regex.replaceAll("\\s", "\\\\s");
		regex = regex.replaceAll("\\*", ".*");
		return regex;
	}

	// -------------------------------------------------------------------------
	// #main
	// -------------------------------------------------------------------------
	public static void main(String argv[]) {
		System.out.println("#start#");
		testDigger20150323();
		System.out.println("#end#");
	}

	// -------------------------------------------------------------------------
	// 単体テスト
	// -------------------------------------------------------------------------
	public static void testDigger20150323() {
		String inDir = "c:/@qpr/";
		boolean recursive = true;// サブデイレクトリも探す
		String regFileName = "*.txt";// ファイル名称のパターン
		regFileName = "";
		String keyword = "2015";// コンテンツ内を検索するキーワード
		boolean ignoreCase = false;// 大文字小文字を区別する場合true
		long limitMin = 0;// 1k以下をdefault
		long limitMax = Long.MAX_VALUE;// 1k以下をdefault

		String today = DateCalc.getToday();
		String theDay = DateUtil.edate1(null, DateUtil.WEEK, -1);
		int bYmd = Integer.valueOf(theDay);
		int aYmd = Integer.valueOf(today);
		bYmd = -1;
		aYmd = -1;
		Inf_OnMatch onMatch = new MatcherEx(keyword, ignoreCase, limitMin,
				limitMax, bYmd, aYmd);
		DiggerEx insDig = new DiggerEx(inDir, regFileName, ignoreCase,
				recursive, onMatch);
		insDig.search();
		onMatch.fin();

	}
}
