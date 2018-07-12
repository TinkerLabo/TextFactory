package kyPkg.uFile;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/*******************************************************************************
 * 《 Digger_neo 》 2007-05-11 <BR>
 * カレント・ディレクトリの下を再帰的に探索して 指定された名前のファイルを見つけて表示するクラス ※検索深度が深い場合・・・検索対象が大きい場合には要注意
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 ******************************************************************************/
// XXX BUGる可能性あり、要修正！！dirListなど共有してしまうと危険なのでシングルトン化する。
// XXX もしくはセッションidを持たせてリストをハッシュで管理する！！
public class Digger {
	private static int defaultSize = 1024;

	private static final String FS = System.getProperty("file.separator");
	private static String regex;
	private static Pattern pattern = null; // コンパイル済みパターン
	private static boolean recursive; // サブディレクトリ検索するかどうか

	private static List<String> fileList; // ファイル一覧表

	private static List<String> dirList; // ディレクトリ一覧表

	private String rootPath; // ディレクトリのパス名

	private File[] fileArray; // ディレクトリ内のファイルオブジェクト

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Digger(String dir, String regex) {
		this(dir, regex, false);
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	// dir 対象とするパス
	// regex 処理対象とするファイルの正規表現パターン
	// recursive サブディレクトリ以下も対象とするかどうか
	// -------------------------------------------------------------------------
	public Digger(String dir, String regex, boolean recursive) {
		if (dir == null)
			return;
		if (isDirExists(dir) == false)
			return;
		// XXX 以下がオリジナルだが・・・どうかと思ったので 2008/01/28（問題があったら戻す）
		// if (wDir == null)
		// wDir = ".";
		Digger.recursive = recursive;
		if (regex == null)
			regex = "*";
		File fileObj = new File(dir);
		if (!fileObj.exists()) {
			System.out.println("Error@Digger Path is not exists:" + dir);
			return;
		}
		setRegex(regex);
		setCur(fileObj);
	}

	private Digger(File fileObj) {
		setCur(fileObj);
	}

	// -----------------------------------------------------------------
	// 正規表現文字とかぶるものについて変更しなければならない
	// 文字列を表現するregixに簡易変換する
	// 括弧など、問題あるだろうね・・・・
	// 元の文字列はregixでは無いという前提で変換している・・
	// -----------------------------------------------------------------
	private void setRegex(String regex) {
//		System.out.println("regex:"+regex);
		if (regex.equals(""))
			regex = "*"; // パターン指定が空だった場合
		if (regex.equals("*.*"))
			regex = "*"; // パターン指定が空だった場合
		regex = escapeIt(regex);
		Digger.regex = regex;
		Digger.pattern = patternIgnoreCase(regex); // 正規表現パターンをコンパイル
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
				// 20130502getCanonicalPath()が滅茶苦茶遅いのでgetAbsolutePath()に差し替えた
				if (regex.equals(".*") || pattern.matcher(name).matches()) {
					dirList.add(fileArray[i].getAbsolutePath());
					// dirList.add(fileArray[i].getCanonicalPath());
					//					System.out.println("match:"+absPath);
				}
				if (recursive) {
					try {
						Digger subIns = new Digger(fileArray[i]);
						subIns.searchSubDirs();
					} catch (Exception e) {
						continue;
					}
				}
			} else {
//				System.out.println("name:"+name);
				if (regex.equals(".*") || pattern.matcher(name).matches()) {
					fileList.add(fileArray[i].getAbsolutePath());
					// fileList.add(fileArray[i].getCanonicalPath());
					//					System.out.println("match:" + absPath);
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// 検索
	// -------------------------------------------------------------------------
	public List<String> search() {
		fileList = new ArrayList(defaultSize);
		dirList = new ArrayList(defaultSize);
		try {
			this.searchSubDirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileList;
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

	// -------------------------------------------------------------------------
	// 条件に合ったファイルの一覧を返す
	// -------------------------------------------------------------------------
	public List<String> getFileList() {
		return fileList;
	}

	// -------------------------------------------------------------------------
	// 条件に合ったヂレクトリの一覧を返す
	// -------------------------------------------------------------------------
	public static List<String> getDirList() {
		return dirList;
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

	public String[] getFileArray() {
		return (String[]) fileList.toArray(new String[fileList.size()]);
	}

	// -------------------------------------------------------------------------
	// このリスト内の要素を適切な順序で繰り返し処理する反復子を返します。
	// -------------------------------------------------------------------------
	public Iterator iterator() {
		return fileList.iterator();
	}

	// -------------------------------------------------------------------------
	// リストを文字列として返す
	// -------------------------------------------------------------------------
	@Override
	public String toString() {
		StringBuffer sBuf = new StringBuffer();
		Iterator it = this.iterator();
		while (it.hasNext()) {
			sBuf.append((String) it.next());
			sBuf.append("\n");
		}
		return sBuf.toString();
	}

	private String escapeIt(String regex) {
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
	// 検査パターンをコンパイル
	// -------------------------------------------------------------------------
	public static Pattern patternIgnoreCase(String pRegex) {
		return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	}

	// -------------------------------------------------------------------------
	// #main
	// -------------------------------------------------------------------------
	public static void main(String argv[]) {
		// testDigger();
		testDigger20150323();
	}

	// -------------------------------------------------------------------------
	// testDriver
	// -------------------------------------------------------------------------
	public static void testDigger() {
		Digger insDig = new Digger("./", "*fa*.java", true);
		// Digger insDig = new Digger("./", "*fa*", true);
		// Digger insDig = new Digger("./", "*");
		insDig.search();
		String[] lst = insDig.getFileArray();
		for (int i = 0; i < lst.length; i++) {
			System.out.println(lst[i]);
		}
	}

	public static void testDigger20150323() {
		boolean recursive = true;
		Digger insDig = new Digger("c:/sample/", "*.txt", recursive);
		insDig.search();
		String[] list = insDig.getFileArray();
		for (int i = 0; i < list.length; i++) {
			System.out.println(list[i]);
		}
	}

}
