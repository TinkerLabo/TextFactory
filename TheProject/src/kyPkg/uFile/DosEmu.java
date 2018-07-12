package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.regex.*;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.atoms.AtomDB;
import kyPkg.util.Shell;

//-------------------------------------------------------------------------
// ※このプログラムは、Digger.javaおよびFileUtil.javaに依存しています。
// ファイルシステムの階層をたどるのにDigger.javaを使っています。
// 実際のコマンドはFileUtil.javaで実装してる。
//-------------------------------------------------------------------------
// ※他のＯＳでもdosコマンド風に操作できる
// ※UNIXコマンドをwinで使いたい場合のエミュレータもほしい気がする（オプションが大変そう）
//-------------------------------------------------------------------------
//	COPY=>XCOPY (デイレクトリも作成したいが・・・)
//bool wFlg = kyPkg.uFile.copyIt(wSrc,wDst);  // copy
//	bool wFlg =kyPkg.uFile.moveIt(wSrc,wDst);  // move
//	bool wFlg = kyPkg.uFile.delIt(wSrc);        // del
//	MOVE=>RENAME
//-------------------------------------------------------------------------
/*******************************************************************************
 * 《 DosEmu 》 2007-05-11 <BR>
 * ※ｄｏｓコマンドの代替機能・・若干仕様が甘いので使用時に注意 ※エラー処理は考慮されていない・・・
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 * 
 ******************************************************************************/
public class DosEmu {

	private static final String REN = "ren";
	private static final String XDEL = "xdel";
	private static final String XMOVE = "xmove";
	private static final String XCOPY = "xcopy";
	private static final String DEL = "del";
	private static final String MOVE = "move";
	private static final String COPY = "copy";
	private static final String XDIR = "xdir";
	private static final String DIR = "dir";

	// 指定したディレクトリを削除する（ディレクトリ以下のファイル・ディレクトリも再帰的に削除する）
	// 例１＞DosEmu.rmDir(rmvDataDir);
	// 例２＞List<String> rmList = DosEmu.rmDir(rmvDataDir);//削除したパスを確認したい場合・・
	public static List<String> rmDir(String path) {
		List<String> rmList = new ArrayList();// 削除したファイルのパスを取っておく（確認用）
		File file = new File(path);
		if (file.exists())
			try {
				delRecursive(rmList, file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return rmList;
	}

	private static void delRecursive(List<String> rmList, File file)
			throws java.io.IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				delRecursive(rmList, files[i]);
			}
		}
		if (!(file.delete())) {
			throw new java.io.IOException("cannot delete:" + file.getPath());
		} else {
			rmList.add(file.getPath());
		}
	}

	public static String getOsName() {
		String osName = System.getProperty("os.name");
		System.out.println("os=>" + osName);
		return osName;
	}

	public static String getWinDir() {
		String osName = DosEmu.getOsName();
		Process process = null;
		Runtime runtime = Runtime.getRuntime();
		try {
			if (osName.startsWith("Windows 9")) {
				process = runtime.exec("command.com /c echo %windir%");
			} else {
				process = runtime.exec("cmd.exe /c echo %windir%");
			}
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void xComand(String command) {
		nativeExe(command, true);
	}

	public static void openWithExplorer(String dir) {
		if (!dir.equals("")) {
			new Shell().execute("explorer " + dir, true);
		}
	}

	private static void nativeExe(String command, boolean waitFor) {
		try {
			Process proc = Runtime.getRuntime().exec(command);
			if (waitFor)
				proc.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// 大文字小文字を曖昧にマッチングしたい場合＃２
	// 同じパターンを何回も使う場合はこちらがベター
	// 《使用例》
	// Pattern ptn = Regexx.patternIgnoreCase(pRegex);
	// wFlg = ptn.matcher(pCharSeq).find();
	// -------------------------------------------------------------------------
	// 大文字小文字が曖昧なパターンを作成
	// -------------------------------------------------------------------------
	private static Pattern patternIgnoreCase(String pRegex) {
		return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	}

	// -------------------------------------------------------------------------
	// デイレクトリ以下のファイルリストを返す
	// ※ ディレクトリのみ見つけたい場合もあるよね・・・リストから拾うか？
	// ※ ファイルのみ欲しい場合も或る・・・・
	// System.out.println(DosEmu.dirString("."));
	// System.out.println(DosEmu.dirString("c:/"));
	// System.out.println(DosEmu.dirString("c:/*.*"));
	// System.out.println(DosEmu.dirString("c:/*.java"));
	// -------------------------------------------------------------------------
	// private static String dirString(String pSrc) {
	// Digger ZZZ = new Digger(pSrc, false);
	// ZZZ.search(1024);
	// String wStr = ZZZ.toString();
	// return wStr;
	// }

	// -------------------------------------------------------------------------
	// << Wrapper >>ファイル一覧
	// -------------------------------------------------------------------------
	public static List dir(String pSrc, boolean recursive) {
		if (recursive) {
			return xdir(pSrc);
		}
		return dir(pSrc);
	}

	// 指定されたデイレクトリ以下にある、ファイル名のリスト（拡張子は含まない）
	public static String[] fileNameList2Array(String dir, String regex) {
		List list = fileNameList(dir, regex);
		String[] array = null;
		if (list != null) {
			array = (String[]) list.toArray(new String[list.size()]);
		}
		return array;
	}

	// 指定されたデイレクトリ以下にある、『ファイル名（拡張子は含まない）』のリスト
	public static List fileNameList(String dir, String regex) {
		if (new File(dir).isDirectory() == false)
			return (List) null;
		Digger insDig = new Digger(dir, regex);
		int dirLen = insDig.getRootPathLength();
		List<String> fileList = insDig.search();
		List list = new ArrayList();
		for (String path : fileList) {
			path = path.substring(dirLen);
			int poz = path.indexOf('.');
			if (poz > 0) {
				list.add(path.substring(0, poz));
			} else {
				list.add(path);
			}
		}
		return list;
	}

	// public static List fileNameList(String dir, String regex) {
	// Digger insDig = new Digger(dir, regex);
	// int preLen = insDig.getRootPathLength();
	// insDig.search();
	// String[] fileArray = insDig.getFileArray();
	// List<String> list = new ArrayList();
	// for (int i = 0; i < fileArray.length; i++) {
	// String wStr = fileArray[i];
	// wStr = wStr.substring(preLen);
	// list.add(wStr.substring(0, wStr.indexOf('.')));
	// }
	// return list;
	// }

	// ------------------------------------------------------------------------
	// 指定されたデイレクトリ以下にある、ファイル（フルパス）のリスト
	// ------------------------------------------------------------------------
	// 使用例＞ String[] array = DosEmu.getFileArray(inDir, pattern);
	// ------------------------------------------------------------------------
	public static String[] getFileArray(String dir, String regex) {
		Digger insDig = new Digger(dir, regex);
		insDig.search();
		return insDig.getFileArray();
	}

	// 指定されたデイレクトリ以下にある、ファイル（フルパス）のリスト
	// ※注意、pathの終端が"/"では無い場合、直前の"/"までカットされる
	public static List getFileList(String path, String regex) {
		String dir = FileUtil.getParent2(path, true);
		Digger insDig = new Digger(dir, regex);
		insDig.search();
		return insDig.getFileList();
	}

	// 指定されたデイレクトリ以下にある、ファイル（フルパス）のリスト
	public static List<String> pathList2List(String iPath) {
		String wPath = FileUtil.normarizeIt(iPath);
		int poz = wPath.lastIndexOf("/");
		String dir = wPath.substring(0, poz);
		String regex = wPath.substring(poz + 1);
		// System.out.println("debug@getFileArray dir:" + dir);
		// System.out.println("debug@getFileArray regex:" + regex);
		return pathList2List(dir, regex);
	}

	public static List<String> pathList2List(String dir, String regex) {
		Digger insDig = new Digger(dir, regex);
		insDig.search();
		return insDig.getFileList();
	}

	public static String[] dirList2Array(String dir, String regex) {
		return ListArrayUtil.list2Array(dirList(dir, regex));
	}

	// TODO このメッソッドのパフォーマンスを上げる・・・要テスト
	public static List<String> dirList(String dir, String regex) {
		dir = FileUtil.mkdir(dir);
		Digger insDig = new Digger(dir, regex);
		int preLength = insDig.getRootPathLength();
		// System.out.println("pre search()");
		insDig.search();
		// System.out.println("pre getDirList()");
		List<String> list = Digger.getDirList();
		if (list == null)
			return null;
		// System.out.println("pre sort");
		// XXX BUGる可能性あり、要修正！！
		Collections.sort(list);
		List result = new ArrayList();
		for (String element : list) {
			result.add(element.substring(preLength));
		}
		return result;
	}

	public static List dir(String pSrc) {
		return command(DIR, pSrc, "");
	}

	public static List xdir(String pSrc) {
		return command(XDIR, pSrc, "");
	}

	// DosEmu.copy(aliasDir + "QTB*.txt", outDir);
	public static List copy(String pSrc, String pDst) {
		return command(COPY, pSrc, pDst);
	}


	// コピー先のデータが存在した場合、強制的に上書きされる
	public static List move(String pSrc, String pDst) {
		//		//#createTester--------------------------------------------------
		//		System.out.println("public static void testmove() {");
		//		System.out.println("    String pSrc = \"" + pSrc + "\";");
		//		System.out.println("    String pDst = \"" + pDst + "\";");
		//		System.out.println("    move ins = new move(pSrc,pDst);");
		//		System.out.println("}");
		//		//--------------------------------------------------

		return command(MOVE, pSrc, pDst);
	}

	// DosEmu.cleanUp(resDir + "*.txt");
	public static List cleanUp(String pSrc) {
		// System.out.println("■20140428■@DosEmu.cleanUp:"+pSrc);
		return del(pSrc);
	}

	// DosEmu.del(resDir + "*.trn");
	public static List del(String pSrc) {
		return command(DEL, pSrc, "");
	}

	public static List xcopy(String pSrc, String pDst) {
		return command(XCOPY, pSrc, pDst);
	}

	public static List xmove(String pSrc, String pDst) {
		return command(XMOVE, pSrc, pDst);
	}

	public static List xdel(String pSrc) {
		return command(XDEL, pSrc, "");
	}

	public static List ren(String pSrc, String pDst) {
		return command(REN, pSrc, pDst);
	}

	// -------------------------------------------------------------------------
	// コマンドを実行する
	// ※実行前に対象を確認する方法が欲しい・・・
	// ※結果をバッファーに貯めるかどうか バーボウズオプション？！
	// ※対象がロックされている、アクセス制限などのエラー処理・・・などの問題
	// -------------------------------------------------------------------------
	private static List command(String pFunk, String pSrc, String pDst) {
		// System.out.println("(_) pFunk:" + pFunk+" pSrc:" + pSrc+" pDst:" +
		// pDst);
		// pFunk:del pSrc:C:\DOCUME~1\ADMINI~1.AGC\LOCALS~1\Temp\/*.txt
		// pDst:
		String wFunk = "";
		boolean rSw = false; // 再帰・・・サブDirを検査するかどうか
		if (pFunk.equals(DIR)) {
			wFunk = DIR;
		} else if (pFunk.equals(COPY)) {
			wFunk = COPY;
		} else if (pFunk.equals(MOVE)) {
			wFunk = MOVE;
		} else if (pFunk.equals(DEL)) {
			wFunk = DEL;
		} else if (pFunk.equals(XDIR)) {
			wFunk = DIR;
			rSw = true;
		} else if (pFunk.equals(XCOPY)) {
			wFunk = COPY;
			rSw = true;
		} else if (pFunk.equals(XMOVE)) {
			wFunk = MOVE;
			rSw = true;
		} else if (pFunk.equals(XDEL)) {
			wFunk = DEL;
			rSw = true;
		} else if (pFunk.equals(REN)) {
			wFunk = REN;
		}
		String wRegx1 = "";
		String wRegx2 = "";
		String wPath1 = "";
		String wPath2 = "";
		String wSrc = "";
		String wDst = "";
		int xIdx = -1;
		// -----------------------------------------------------------------
		if (pSrc == null)
			return null;
		pSrc = pSrc.replaceAll("\\\\", "/");
		pDst = pDst.replaceAll("\\\\", "/");
		// System.out.println(" pFunk:" + pFunk + " pSrc:" + pSrc + " pDst:" +
		// pDst);
		if (pSrc.lastIndexOf("/") == -1)
			pSrc = pSrc + "/";
		xIdx = pSrc.lastIndexOf("/");
		wPath1 = pSrc.substring(0, xIdx + 1);
		wRegx1 = pSrc.substring(xIdx + 1).trim();
		// -----------------------------------------------------------------
		if (pDst.lastIndexOf("/") == -1)
			pDst = pDst + "/";
		xIdx = pDst.lastIndexOf("/");
		wPath2 = pDst.substring(0, xIdx + 1);
		wRegx2 = pDst.substring(xIdx + 1).trim();
		// -----------------------------------------------------------------
		// 第二パラメータを c:\xxxx のように指定された場合
		// xxxxをディレクトリとして扱いたい
		// -----------------------------------------------------------------
		if (!wRegx2.trim().equals("")) {
			if (wRegx2.lastIndexOf(".") == -1) {
				pDst = pDst + "/";
				xIdx = pDst.lastIndexOf("/");
				wPath2 = pDst.substring(0, xIdx + 1);
				wRegx2 = pDst.substring(xIdx + 1).trim();
			}
		}
		new java.io.File(wPath2).mkdirs();
		// Digger insDig = new Digger(pSrc, rSw);
		// System.out.println("wPath1:" + wPath1);
		// System.out.println("wRegx1:" + wRegx1);
		Digger insDig = new Digger(wPath1, wRegx1, rSw);
		insDig.search();
		String wPath = insDig.getRootPath();// ?!意味がわからん
		// System.out.println("20130502 <debug xxx> wPath :" + wPath);
		// wPath = wPath1;パッチを入れたが戻す　狩野に駆るﾊﾟｽが原因かな
		// System.out.println("20130502 <debug xxx> wPath1:" + wPath1);
		if (wPath == null)
			return null; // ???20100316 仮に・・・こうした
		wPath = wPath.replaceAll("\\\\", "/"); // 2010/06/23 yuasa
		// System.out.println("20130502 <debug> wPath :" + wPath);

		int wPos = wPath.length();
		// System.out.println("wPath:" + wPath);
		// System.out.println("length:" + wPos);
		wRegx1 = wRegx1.replaceAll("\\*", "");
		wRegx2 = wRegx2.replaceAll("\\*", "");
		wRegx1 = wRegx1.replaceAll("\\.", "\\\\.");
		if (wRegx1.equals("\\.") | wRegx1.equals(""))
			wRegx1 = "\\..*";
		// -----------------------------------------------------------------
		// System.out.println("■wPath1 :" + wPath1);
		// System.out.println("■wRegx1 :" + wRegx1);
		// System.out.println("■wPath2 :" + wPath2);
		// System.out.println("■wRegx2 :" + wRegx2);
		Pattern ptn = patternIgnoreCase(wRegx1); // パターン作成
		// -----------------------------------------------------------------

		Iterator it = insDig.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			// wSrc = (String)it.next();
			wSrc = it.next().toString();
			wSrc = wSrc.replaceAll("\\\\", "/"); // 2010/06/23 yuasa
			// System.out.println("2013<debug> wPath2:" + wPath2);
			// System.out.println("2013<debug> wSrc:" + wSrc);
			// System.out.println("2013<debug> wPos:" + wPos);
			wDst = wPath2 + wSrc.substring(wPos);
			// System.out.println("wSrc=>"+wSrc);
			// System.out.println("wDst=>"+wDst);
			// wDst = wDst.replaceAll(wRegx1,wRegx2);
			if (!wRegx2.equals("")) {
				wDst = ptn.matcher(wDst).replaceAll(wRegx2);
			}
			// System.out.println("conv=>"+wDst);
			// -----------------------------------------------------------------
			// 共用できるロジックなので、
			// あとで各々のメソッドごとにラッパーメソッドを作っておくと便利
			// -----------------------------------------------------------------
			boolean wFlg = false;
			if (wFunk.equals(DIR)) {
				list.add(wSrc);
				wFlg = true;
			} else if (wFunk.equals(COPY)) {
				wFlg = FileUtil.fileCopy(wDst, wSrc);
				list.add("copy " + wSrc + " to " + wDst);
			} else if (wFunk.equals(MOVE)) {
				wFlg = FileUtil.moveIt(wSrc, wDst);
				list.add("move " + wSrc + " to " + wDst);
			} else if (wFunk.equals(DEL)) {
				wFlg = FileUtil.delIt(wSrc);
				list.add("del " + wSrc);
			} else if (wFunk.equals(REN)) {
				// wFlg = FileUtil.moveIt(wSrc, wDst);
				// list.add("move " + wSrc + " to " + wDst);
				// これについては第２パラメータの形が違う ・・・MOVEで代用可能だろう
			}
			if (wFlg == false) {
				System.out.println("Error @xCommand :" + wFunk + "　=>" + wSrc);
			}
			// wVec.add("\n");
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// main 使用例 カレントパス上より引数に合うパターンのファイルリストを作る
	// -------------------------------------------------------------------------
	public static void test01() {
		// System.out.println(DosEmu.dirString("."));
		// System.out.println(DosEmu.dirString("c:/"));
		// System.out.println(DosEmu.dirString("c:/*.*"));
		// System.out.println(DosEmu.dirString("c:/*.java"));
		// new java.io.File("c:/backups").mkdirs();
		// Vector wVec = DosEmu.move("c:/*.txt", "c:/backups");
		// for (int i = 0; i < wVec.size(); i++) {
		// System.out.println("=>" + wVec.get(i));
		// }
		String[] array = DosEmu.fileNameList2Array("c:/", "*.txt");
		for (int i = 0; i < array.length; i++) {
			System.out.println("==>" + array[i]);
		}
		// String[] arrayD = DosEmu.dirList2StrArray("c:/", "e*");
		String[] arrayD = kyPkg.uFile.DosEmu.dirList2Array("N:/PowerBX/2008/",
				"T*");
		for (int i = 0; i < arrayD.length; i++) {
			System.out.println("D=>" + arrayD[i]);
		}
		DosEmu.copy("c:/*.csv", "c:/freaks/");
		// DosEmu.ren(ResControl.QPR_DAT + "NQFACE.DAT", "NQFACE.OLD");
	}

	public static void test02() {
		String targetPath = ResControlWeb.getD_Resources_ATOM("");
		String pattern = "*.*";
		String[] arrayD = DosEmu.getFileArray(targetPath, pattern);
		for (int i = 0; i < arrayD.length; i++) {
			System.out.println("D=>" + arrayD[i]);
		}
	}

	public static void test03() {
		String userDir = globals.ResControl.getQPRHome();
		String[] array = DosEmu.fileNameList2Array(userDir + "110098000002/",
				"*.trn");
		for (int i = 0; i < array.length; i++) {
			System.out.println("==>" + array[i]);
		}
	}

	public static void test0601() {
		// String targetPath = ResControl.getWebDir(target);
		String targetPath = ResControl.D_DRV + "resources/User/@itp";
		String[] array = DosEmu.dirList2Array(targetPath, "e95*");
		for (int i = 0; i < array.length; i++) {
			System.out.println("==>" + array[i]);
		}
	}

	public static void testGetFileList() {
		System.out.println("<start testGetFileList>");
		String wPath1 = "c:/test.txt";
		String wPath2 = "z:/test.txt";
		String regex = "*.txt";
		List<String> list = DosEmu.getFileList(wPath1, regex);
		for (String str : list) {
			// 親パスを書き換える
			String tPath = FileUtil.cnvParent(wPath2, str);
			// 拡張子を書き換える
			String difPath = FileUtil.cnvExt(str, "dif");
			System.out.println(
					">>" + str + " parent>" + tPath + " dif>" + difPath);

		}
		System.out.println("<end   testGetFileList>");
	}

	// private static final String FS = System.getProperty("file.separator");
	public static void testRmDir() {
		List<String> rmList = DosEmu.rmDir("c:/encodeTest/");
		for (String path : rmList) {
			System.out.println("removed => " + path);
		}
	}

	// 20130502
	public static void testDirList() {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("DividerVer1");
		elapse.start();
		String itpDir = ResControl.T_RES + "@itp/E92019";
		String regex = "8*";
		itpDir = globals.ResControlWeb.getD_Resources_USER_ITP_COMMON();
		regex = "*";
		List<String> list = DosEmu.dirList(itpDir, regex);
		ListArrayUtil.enumList(list);
		elapse.stop();
	}

	// 当該ディレクトリ以下のパターンにマッチするファイル一覧
	public static void testDir() {
		// 当該ディレクトリ以下のパターンにマッチするファイル一覧
		String path = "C:/@qpr/home/238881000301/calc/#004_sorted_AMT_*.txt";
		List<String> list = DosEmu.dir(path, false);
		for (String element : list) {
			System.out.println("@dOSeMU element:" + element);
		}
	}

	public static void testCopy() {
		String serverPath = "//Ag6s012/PRG/s2/rx/qpr/tran/#340018212101/20130301_20130331.*";
		String locoPath = "C:/@qpr/home/340018212101/tran/20130301_20130331.*";
		DosEmu.copy(serverPath, locoPath);
	}

	public static void testCopy20160114() {
		String srcPath = ResControl.getCnlSets_P() + "*"+AtomDB.LIM; //C:/@qpr/home/Personal/CnlSets/
		String dstPath = ResControl.getCnlSets(); //C:/@qpr/home/qpr/res/CnlSets/
		System.out.println("srcPath:" + srcPath);
		System.out.println("dstPath:" + dstPath);
		DosEmu.copy(srcPath, dstPath);
	}

	//再起的に消す
	public static void testDel() {
		DosEmu.xdel("C:/@qpr/outitpBANK/E97009/*.*");
	}

	//消す
	public static void test20161004() {
		String janSetDir = "C:/@qpr/home/304667003001/JanSets/*.*";
		DosEmu.del(janSetDir);
	}

	public static void main(String argv[]) {
		// test02();
		// test03();
		// test0601();
		// testGetFileList();
		// testFileNameList();
		// testRmDir();
		// testCopy();
		// testFileNameList();
		// testDirList();
		// testDir();
		// testCopy20160114();
		//		testDel();
		test20161004();
	}
	//	public static String userName() {
	//		return kyPkg.util.RuntimeEnv.getUserID();
	//	}
}
