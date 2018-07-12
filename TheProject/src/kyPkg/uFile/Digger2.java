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
public class Digger2 {
	private static boolean killerSw = true;
	private static int defaultSize = 1024;

	private static final String FS = System.getProperty("file.separator");

	private static Pattern pattern = null; // コンパイル済みパターン

	private static boolean recursive; // サブディレクトリ検索するかどうか

	private static List<String> fileList; // ファイル一覧表

	private static List<String> dirList; // ディレクトリ一覧表

	private String rootPath; // ディレクトリのパス名

	private File[] gFileArray; // ディレクトリ内のファイルオブジェクト

	public static List<String> getDirList() {
		return dirList;
	}

	// -------------------------------------------------------------------------
	// サブディレクトリを探索する (再帰的に行う場合recursive==trueとする)
	// -------------------------------------------------------------------------
	private void searchSubDirs() throws Exception {
		for (int i = 0; i < gFileArray.length; i++) {
			String wStr = gFileArray[i].getName();
			if (gFileArray[i].isDirectory()) {
				if (pattern.matcher(wStr).matches()) {
					// 20130507 念のため・・
					dirList.add(gFileArray[i].getAbsolutePath());
					// dirList.add(gFileArray[i].getCanonicalPath());
				}
				if (recursive) {
					try {
						Digger2 subIns = new Digger2(gFileArray[i]);
						subIns.searchSubDirs();
					} catch (Exception e) {
						continue;
					}
				}
			} else {
				if (pattern.matcher(wStr).matches()) {
					// 20130507 念のため・・
					fileList.add(gFileArray[i].getAbsolutePath());
					// fileList.add(gFileArray[i].getCanonicalPath());
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// 検索
	// -------------------------------------------------------------------------
	public void search(int defaultSize) {
		setDefaultSize(defaultSize);
		search();
	}

	public void search() {
		fileList = new ArrayList(defaultSize);
		dirList = new ArrayList(defaultSize);
		try {
			this.searchSubDirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// 公開するコンストラクタ
	// wPath 対象とするパス
	// wRegex 処理対象とするファイルの正規表現パターン
	// pOption サブディレクトリ以下も対象とするかどうか
	// -------------------------------------------------------------------------
	private static boolean isDirExists(String dir) {
		File wFile = new File(dir);
		if (!wFile.exists()) {
			System.out.println("Digger2 ### Error@isDirExists  not exists=>"
					+ dir);
			return false;
		}
		return true;
	}

	public Digger2(String dir, String regex, boolean pRecursive,
			boolean killerflag) {
		killerSw = killerflag;
		if (dir == null)
			return;
		if (isDirExists(dir) == false)
			return;
		// XXX 以下がオリジナルだが・・・どうかと思ったので 2008/01/28（問題があったら戻す）
		// if (wDir == null)
		// wDir = ".";
		Digger2.recursive = pRecursive;
		if (regex == null)
			regex = "*";
		File wFile = new File(dir);
		if (!wFile.exists()) {
			System.out.println("Error@Digger Path is not exists:" + dir);
			return;
		}
		setCur(wFile);
		setRegex(regex);
	}

	private Digger2(File dir) {
		setCur(dir);
	}

	// ファイルを消すことによって、当該ディレクトリが空になった場合再帰的に削除していく
	public static int chainReaction(String path) {
		return chainReaction(new File(path));
	}

	private static int chainReaction(File file) {
		System.out.println("chainReaction:" + file.getAbsolutePath());
		File parent = file.getParentFile();
		file.delete();
		return removeInvs(parent) + 1;
	}

	private static int removeInvs(File parent) {
		int kilCount = 0;
		File[] fArray = parent.listFiles();
		if (fArray.length == 1) {
			for (int i = 0; i < fArray.length; i++) {
				File file = fArray[i];
				String name = file.getName();
				if (name.equals(".cvsignore")) {
					file.delete();
					// kilCount++;
				} else if (name.equals(".DS_Store")) {
					file.delete();
					// kilCount++;
				} else if (name.equals(".svn")) {
					file.delete();
					// kilCount++;
				}
			}
			fArray = parent.listFiles();
		}
		if (fArray.length == 0) {
			kilCount += chainReaction(parent);
		}
		return kilCount;
	}

	private void setCur(File file) {
		try {
			if (file.isDirectory()) {
				// 20130507 念のため・・
				rootPath = file.getAbsolutePath();
				// rootPath = file.getCanonicalPath();
				if (!rootPath.endsWith(FS))
					rootPath = rootPath + FS;
				if (killerSw) {
					removeInvs(file);
				}
				gFileArray = file.listFiles();
			} else {
				// -----------------------------------------------------------------
				// 存在しないディレクトリを指定された場合のエラー処理の取り回しのしかた
				// 指定したパスが存在するディレクトリなのかどうか・・・・
				// -----------------------------------------------------------------
				System.out.println("Error　not Directory:" + file.toString());
				throw new Exception();
			}
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

	// -------------------------------------------------------------------------
	// リスト内のすべての要素が正しい順序で格納されている配列を返します。
	// -------------------------------------------------------------------------
	public List getList() {
		return fileList;
	}

	public List getListD() {
		return dirList;
	}

	public String[] getFileArray() {
		return (String[]) fileList.toArray(new String[fileList.size()]);
	}

	public String[] getDirArray() {
		return (String[]) dirList.toArray(new String[dirList.size()]);
	}

	// -------------------------------------------------------------------------
	// このリスト内の要素を適切な順序で繰り返し処理する反復子を返します。
	// -------------------------------------------------------------------------
	public Iterator iterator() {
		return fileList.iterator();
	}

	public Iterator iteratorD() {
		return dirList.iterator();
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

	// -----------------------------------------------------------------
	// 正規表現文字とかぶるものについて変更しなければならない
	// 文字列を表現するregixに簡易変換する
	// 括弧など、問題あるだろうね・・・・
	// 元の文字列はregixでは無いという前提で変換している・・
	// -----------------------------------------------------------------
	private void setRegex(String regex) {
		if (regex.equals(""))
			regex = "*"; // パターン指定が空だった場合
		regex = escapeIt(regex);
		// System.out.println(">>pRegex :" + regex);
		pattern = patternIgnoreCase(regex); // 正規表現パターンをコンパイル
	}

	// -------------------------------------------------------------------------
	// 検査パターンをコンパイル
	// -------------------------------------------------------------------------
	public static Pattern patternIgnoreCase(String pRegex) {
		return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	}

	// -------------------------------------------------------------------------
	// listの初期サイズ
	// -------------------------------------------------------------------------
	public static void setDefaultSize(int defaultSize) {
		Digger2.defaultSize = defaultSize;
	}

	// -------------------------------------------------------------------------
	// testDriver
	// -------------------------------------------------------------------------
	public static void main(String argv[]) {
		Digger2 insDig = new Digger2("./", "*fa*.java", true, true);
		// Digger insDig = new Digger("./", "*fa*", true);
		// Digger insDig = new Digger("./", "*");
		insDig.search();
		String[] lst = insDig.getFileArray();
		for (int i = 0; i < lst.length; i++) {
			System.out.println(lst[i]);
		}
		// ---------------------------------------------------------------------
		// 反復子で受け取る場合（ディレクトリ版）
		// ---------------------------------------------------------------------
		Iterator it = insDig.iteratorD();
		while (it.hasNext()) {
			System.out.println("dir=>" + (String) it.next());
		}
	}

}
