package kyPkg.uFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

import globals.ResControl;
import kyPkg.tools.Onbiki;
import kyPkg.uDateTime.DateCalc;

/*******************************************************************************
 * 《 FileUtil 》 <BR>
 * ファイル関連、良く使うものなど
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 ******************************************************************************/
//-----------------------------------------------------------------------------
// kyPkg.uFile.FileUtil
//-----------------------------------------------------------------------------
// fileCheck
//-----------------------------------------------------------------------------
//'./'は、ここ→ 'T:\workspace\QPRweb'
//-----------------------------------------------------------------------------
public class FileUtil {
	public static final String FS = System.getProperty("file.separator");
	public static final String LF = System.getProperty("line.separator");
	public static final String JIS0212 = "JIS0212";// JIS X 0212、日本語
	public static final String JIS0208 = "JIS0208";// JIS X 0208、日本語
	public static final String JIS0201 = "JIS0201";// JIS X 0201、日本語
	public static final String JIS_AUTO_DETECT = "JISAutoDetect";// Shift-JIS、EUC-JP、ISO 2022 JP の検出および変換
	public static final String US_ASCII = "US-ASCII";// American Standard Code for Information
	public static final String ISO_8859_1 = "ISO-8859-1";// ISO 8859-1、ラテンアルファベット No. 1
	public static final String ISO8859_1 = "ISO8859_1";// ISO 8859-1、ラテンアルファベット No. 1
	public static final String ASCII = "ASCII";// American Standard Code for Information Interchange
	//  BOMによって指定されたバイト順
	public static final String UTF_16LE = "UTF-16LE";// 16 ビット UCS Transformation Format、リトルエンディアンバイト順
	public static final String UTF_16BE = "UTF-16BE";// 16 ビット UCS Transformation Format、ビッグエンディアンバイト順
	//  リトルエンディアンバイト順
	public static final String UTF_16 = "UTF-16";// 16 ビット Unicode Transformation
	//  リトルエンディアンバイト順、バイト順マーク付き
	public static final String UNICODE_LITTLE_UNMARKED = "UnicodeLittleUnmarked";// 16 ビット Unicode Transformation
	//  ビッグエンディアンバイト順
	public static final String UNICODE_LITTLE = "UnicodeLittle";// 16 ビット Unicode Transformation
	public static final String UNICODE_BIG_UNMARKED = "UnicodeBigUnmarked";// 16 ビット Unicode Transformation
	public static final String UNICODE_BIG = "UnicodeBig"; // 16 ビット Unicode Transformation
	public static final String CP943 = "Cp943";// IBM OS/2 日本語、Cp932 および Shift-JIS のスーパーセット
	public static final String CP942 = "Cp942";// IBM OS/2 日本語、Cp932 のスーパーセット
	public static final String CP939 = "Cp939";// UDC 4370 文字を含む日本語ラテン文字漢字、5035 のスーパーセット
	public static final String CP932 = "CP932";// IBM OS/2 日本語 。MS932 とほぼ同様だが若干異なる。
	public static final String CP930 = "Cp930";// UDC 4370 文字を含む日本語カタカナ漢字、5026 のスーパーセット
	public static final String CP33722 = "Cp33722";// IBM-eucJP - 日本語 (5050 のスーパーセット)
	public static final String ISO2022JP = "ISO2022JP";// JIS X 0201、ISO 2022 形式の 0208、日本語
	public static final String ISO_2022_JP = "ISO-2022-JP";// 同上
	public static final String EUC_JP = "EUC_JP";// JIS X 0201、0208、0212、EUC エンコーディング、日本語
	public static final String UNICODE = "Unicode";// unicodeとだけ指定するとどうなる？
	public static final String UTF8 = "UTF8";// 8 ビット Unicode Transformation Format
	public static final String SJIS = "SJIS";// Shift-JIS、日本語
	public static final String SHIFT_JIS = "Shift_JIS";// JDK1.1 までは SJIS と同義。JDK1.2 からは MS932 と同義。
	public static final String UTF_8 = "UTF-8";// 8 ビット UCS Transformation Format 
	public static final String MS932 = "MS932";// Windows 日本語シフトJISとほぼ同様だが若干異なる。
	//	public static final String DEFAULT_ENCODE = MS932;
	//	static String defaultEncoding = MS932;// windows=>MS932
	//	public static String defaultEncoding2 = MS932;// windows=>MS932
	public static final String DEFAULT_ENCODE = MS932;//SHIFT_JIS;
	public static String defaultEncoding = MS932;//SHIFT_JIS;// windows=>MS932
	public static String defaultEncoding2 = MS932;;//SHIFT_JIS;// windows=>MS932
	public static final String WINDOWS_31J = "windows-31j";// windows-31j,shift-jis,ISO-2022-JP
	public static final String windowsDecoding = MS932;;//SHIFT_JIS;// windows-31j,shift-jis,ISO-2022-JP
	//	public static final String windowsDecoding = WINDOWS_31J;// windows-31j,shift-jis,ISO-2022-JP
	//	public static final String DEFAULT_ENCODE = MS932;//20170324 　これでいいんじゃないか？試す・・・問題がある場合は　rec = Onbiki.cnv2Similar(rec, SHIFT_JIS);などでなおす
	//	static String defaultEncoding = System.getProperty("file.encoding");// windows=>MS932
	// -------------------------------------------------------------------------------
	// キャラクタセット（エンコード）の一覧をリストで返す
	// List charsetNames = kyPkg.uFile.FileUtil_.getCharsetNames();
	// -------------------------------------------------------------------------------
	// サポートされているエンコーディング
	// http://java.sun.com/j2se/1.4/ja/docs/ja/guide/intl/encoding.doc.html
	// 参考ページ
	// http://www.tohoho-web.com/java/file.htm
	// 　utf-8についてはBOMがあっても無くても”UTF-8”でok
	// -------------------------------------------------------------------------------

	public static List getCharsetNames() {
		List list = new ArrayList();
		list.add(MS932); // Windows 日本語シフトJISとほぼ同様だが若干異なる。
		list.add(SHIFT_JIS); // JDK1.1 までは SJIS と同義。JDK1.2 からは MS932 と同義。
		list.add(SJIS); // Shift-JIS、日本語
		list.add(UTF_8); // 8 ビット UCS Transformation Format
		list.add(UTF8); // 8 ビット Unicode Transformation Format
		list.add(UNICODE); // unicodeとだけ指定するとどうなる？
		list.add(EUC_JP); // JIS X 0201、0208、0212、EUC エンコーディング、日本語
		list.add(ISO2022JP); // JIS X 0201、ISO 2022 形式の 0208、日本語
		list.add(ISO_2022_JP); // 同上
		list.add(CP33722); // IBM-eucJP - 日本語 (5050 のスーパーセット)
		list.add(CP930); // UDC 4370 文字を含む日本語カタカナ漢字、5026 のスーパーセット
		//		 list.add(CP932); // IBM OS/2 日本語 。MS932 とほぼ同様だが若干異なる。
		list.add(CP939); // UDC 4370 文字を含む日本語ラテン文字漢字、5035 のスーパーセット
		list.add(CP942); // IBM OS/2 日本語、Cp932 のスーパーセット
		list.add(CP943); // IBM OS/2 日本語、Cp932 および Shift-JIS のスーパーセット
		list.add(UNICODE_BIG); // 16 ビット Unicode Transformation
		// Format、ビッグエンディアンバイト順、バイト順マーク付き
		list.add(UNICODE_BIG_UNMARKED); // 16 ビット Unicode Transformation
		// Format、ビッグエンディアンバイト順
		list.add(UNICODE_LITTLE); // 16 ビット Unicode Transformation
		// Format、リトルエンディアンバイト順、バイト順マーク付き
		list.add(UNICODE_LITTLE_UNMARKED); // 16 ビット Unicode Transformation
		// Format、リトルエンディアンバイト順
		list.add(UTF_16); // 16 ビット Unicode Transformation
		// Format、必須の初期バイト順マークによって指定されたバイト順
		list.add(UTF_16BE); // 16 ビット UCS Transformation Format、ビッグエンディアンバイト順
		list.add(UTF_16LE); // 16 ビット UCS Transformation Format、リトルエンディアンバイト順
		list.add(ASCII); // American Standard Code for Information Interchange
		list.add(ISO8859_1); // ISO 8859-1、ラテンアルファベット No. 1
		list.add(ISO_8859_1); // ISO 8859-1、ラテンアルファベット No. 1
		list.add(US_ASCII); // American Standard Code for Information
		list.add(JIS_AUTO_DETECT); // Shift-JIS、EUC-JP、ISO 2022 JP の検出および変換
		// (Unicode への変換のみ)
		list.add(JIS0201); // JIS X 0201、日本語
		list.add(JIS0208); // JIS X 0208、日本語
		list.add(JIS0212); // JIS X 0212、日本語
		// Interchange
		return list;
	}

	//---------------------------------------------------------------------------
	public static String getDefaultEncoding() {
		return defaultEncoding;
	}

	public static void setDefaultEncoding(String defaultEncoding) {
		FileUtil.defaultEncoding = defaultEncoding;
	}

	// -------------------------------------------------------------------------------
	// 拡張子を書き換える
	// sample: kyPkg.uFile.FileUtil_.cnvExt(path, "dif")
	// -------------------------------------------------------------------------------
	public static String cnvExt(String path, String ext) {
		String preExt = FileUtil.getPreExt(path);
		return preExt + "." + ext.trim();
	}

	// -------------------------------------------------------------------------------
	// 親パスを書き換える
	// sample: kyPkg.uFile.FileUtil_.cnvParent("z:/", path)
	// ※新しい親パスの終端が"/"では無い場合切り捨てるので注意
	// -------------------------------------------------------------------------------
	public static String cnvParent(String newParent, String path) {
		String name = FileUtil.getName(path);
		String parent = FileUtil.getParent2(newParent, true);
		return parent + name;
	}

	/***************************************************************************
	 * OutputStreamWriterを返す<br>
	 * 
	 * @param resultPath
	 *            読み込むファイル
	 * @return 失敗したらnullが返る <br>
	 *         例 <br>
	 *         OutputStreamWriter oSw = new
	 *         FileUtil().getStreamWriter("c:\zappa.txt"); <br>
	 *         if ( oSw ^= null ){ <br>
	 *         String wRec = sBuf.toString(); <br>
	 *         oSw.write(wRec,0,wRec.length()); <br>
	 *         oSw.close(); <br>
	 **************************************************************************/
	// public OutputStreamWriter getStreamWriter(String outPath) {
	// kyPkg.uFile.FileUtil.makeParents(outPath); // 親パスが無ければ作る
	// OutputStreamWriter writer = null;
	// if (!FileUtil.oFileChk(outPath).equals("")) {
	// return null;
	// }
	// File oFile = new File(outPath);
	// try {
	// writer = new OutputStreamWriter(new FileOutputStream(oFile));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return writer;
	// }
	public static File fileCheck(String path) {
		if (path == null || path.trim().equals("")) {
			System.out.println("#ERROR @fileCheck =>path is :" + path);
			return null;
		}
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("#ERROR @fileCheck FileNotFound:" + path);
			return null;
		}
		if (!file.isFile()) {
			System.out.println("#ERROR @fileCheck Not a File:" + path);
			return null;
		}
		if (file.length() == 0) {
			System.out.println("#ERROR @fileCheck length is 0:" + path);
			return null;
		}
		return file;
	}

	/***************************************************************************
	 * 入力ファイルチェック<br>
	 * 
	 * @param pPath
	 *            ファイルパス
	 * @return ファイルクラス <br>
	 *         《使用例1》<br>
	 *         if ( FileUtil.iFileChk(iPath).equals("") ){ //OK File wFile = new
	 *         File(path); }
	 **************************************************************************/
	public static File iFileChk(String path) {
		String message = "";
		File file = new File(path);
		try {
			if (!file.exists()) {
				message = "ファイルが存在しません：" + path;
				message = "通常ファイルではありません：" + path;
			} else if (!file.isFile()) {
			} else if (!file.canRead()) {
				message = "読み込み可能ではありません：" + path;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			message = "セキュリテイ例外です";
		}
		if (message.equals("")) {
			return file;
		} else {
			System.out.println(message);
			return null;
		}
	}

	/***************************************************************************
	 * 出力ファイルチェック<br>
	 * 
	 * @param pPath
	 *            ファイルパス
	 * @return ファイルクラス if ( FileUtil.oFileChk(iPath).equals("") ){ //OK File
	 *         wFile = new File(path); }
	 **************************************************************************/
	public static String oFileChk(String path) {
		String wEmsg = "";
		File wFile = new File(path);
		try {
			if (!wFile.exists()) {
			} else if (!wFile.isFile()) {
				wEmsg = "通常ファイルではありません：" + path;
			} else if (!wFile.canWrite()) {
				wEmsg = "書き出し可能ではありません：" + path;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			wEmsg = "セキュリテイ例外です";
		}
		if (!wEmsg.equals("")) {
			System.out.println("#ERROR @oFileChk " + wEmsg);
		}
		return wEmsg;
	}

	public static void copyItTest(String[] args) {
		if (args.length != 2) {
			System.out.println("使い方: java FileUtil 入力File 出力OutFile");
			System.exit(1);
		}
		fileCopy(args[1], args[0]);
	}

	// ----------------------------------------------------------------------
	// ファイルの存在チェック
	// ----------------------------------------------------------------------
	public static boolean isExists(String path) {
		if (new File(path).exists()) {
			return true;
		} else {
			return false;
		}
	}

	// ファイルがロックされた場所をデバッグするためにつくった
	// kyPkg.uFile.FileUtil_.isLocked(ResControl.D_DRV + "workspace/QPRweb/~/janresult.txt","#check01#");
	public static boolean isLocked(String path, String comment) {
		File orgFile = new java.io.File(path);
		String wkFile = "./" + kyPkg.uDateTime.DateCalc.getTimeStamp();
		File tstFile = new java.io.File(wkFile);
		if (orgFile.renameTo(tstFile)) {
			tstFile.renameTo(new File(path)); // もとに戻す
			System.out.println("-------------------------------------------");
			System.out.println("-- File is unLocked  " + comment + "  ");
			System.out.println("-------------------------------------------");
			return true;
		} else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("XX File is Locked " + comment + "---> " + path);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			return false;
		}
	}
	// 使用例＞ String mmDir = FileUtil.getMMDir("");
	// public static String getMMDir() {
	// String mmDir = ResControl.D_DRV + "eclipse/workspace/kyProject/";
	// return mmDir;
	// }

	// -------------------------------------------------------------------------
	// 使用例＞ String xxx = FileUtil.charsetConv(xxx);
	// -------------------------------------------------------------------------
	public static String charsetConv(String src) {
		String charsetName = java.nio.charset.Charset.defaultCharset().name();
		// System.out.println("charsetName:"+charsetName);
		String dest = src + "<UnsupportedEncoding>";
		try {
			dest = new String(src.getBytes(), charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dest;
	}

	// String resDir = kyPkg.uFile.ResControl.getResDir(path);
	// public static String getResDir(String path) {
	// return "../../../resources/" + path;
	// }

	// FileUtil. renameIt( path_out, path_tmp, path_in);
	public static boolean renameIt(String path_out, String path_tmp,
			String path_in) {
		return renameIt(new File(path_out), new File(path_tmp),
				new File(path_in));
	}

	public static boolean renameIt(File file_out, File file_temp,
			File file_in) {
		// もし入力パスと出力パスが同じ場合には．．．
		if (file_in.getAbsolutePath().equals(file_out.getAbsolutePath())) {
			file_in.renameTo(new File(file_in.getAbsolutePath() + ".bak")); // バックアップを残す
		}
		if (file_out.exists())
			file_out.delete(); // ちゃんと消せるのかどうか、上書きｏｋか要確認かも
		file_temp.renameTo(file_out);
		System.out.println("#処理→" + file_out.getAbsolutePath());
		return true;
	}

	// -------------------------------------------------------------------------------
	// ファイル名のみを返す
	// String fileName = FileUtil.getFileName("c:/zappa.txt"); // => zappa.txt
	// -------------------------------------------------------------------------------
	public static String getFileName(String pSrc) {
		return new File(pSrc).getName();
	}

	// -------------------------------------------------------------------------------
	// String tempDir = FileUtil.getTempDir();
	// String UserDir = FileUtil.getUserDir();
	// String CurrentDir = FileUtil.getCurrentDir();
	// System.out.print("tempDir :"+tempDir);
	// System.out.print("UserDir :"+UserDir);
	// System.out.print("CurrentDir:"+CurrentDir);
	// -------------------------------------------------------------------------------
	// "ユーザのホームディレクトリ "を返す
	// String userDir = kyPkg.uFile.FileUtil.getUserDir( );
	// -------------------------------------------------------------------------------
	// ディレクトリを作る、作れなかったらカレントパスを返す 良くないので没！
	// String wPath = FileUtil.mkDirPlus( "./macDownloads");
	// -------------------------------------------------------------------------------
	// public static String mkDirPlus(String wPath){
	// File wDir = new File(wPath);
	// if( !wDir.exists() ) {
	// if (wDir.mkdirs()==false) wPath = ".";
	// }
	// return wPath;
	// }
	// -------------------------------------------------------------------------------
	// ユーザの現在の作業ディレクトリを返す(処理系によって終わりに￥がつくのか不明)
	// String currentDir = FileUtil.getCurrentDir( );
	// -------------------------------------------------------------------------------
	public static String getCurrentDir() {
		return getCurrentDir(false);
	}

	public static String getCurrentDir(boolean sw) {
		String currentDir = System.getProperty("user.dir").trim();
		if (sw && !currentDir.endsWith(FS)) {
			return currentDir + FS;
		}
		return currentDir;
	}

	/***************************************************************************
	 * リソースを読み込む javax.swing.ImageIcon<br>
	 * 
	 * @param pPath
	 *            読み込み元となるパス
	 * @return イメージアイコン <br>
	 *         《使用例》<br>
	 *         Icon icon = FileUtil.getImageIcon("images/DEAD.GIF"); JLabel
	 *         wLabel = new JLabel(icon);
	 **************************************************************************/
	public static ImageIcon getImageIcon(String pPath) {
		if (pPath == null || pPath.equals("")) {
			return null;
		}
		// ClassLoader loader = this.getClass().getClassLoader();
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		java.net.URL iURL = loader.getResource(pPath);
		return new ImageIcon(iURL); // System.out.println(iURL);
	}

	/***************************************************************************
	 * ファイルの最初の部分（拡張子を含まない）を取り出す<br>
	 * 
	 * @param wPath_I
	 *            ファイルのパス
	 * @return ファイル名 <br>
	 *         《使用例》<br>
	 *         <br>
	 *         String wFirstName = FileUtil.getFirstName("c:\test.txt"); <br>
	 *         System.out.println("FirstName:"+wFirstName); ※ / および 相対パスは駄目
	 **************************************************************************/
	public static String getFirstName(String pPath) {
		String wRtn = "";
		String wPath = pPath;
		// ファイルセパレータが存在しない場合p1は０となる
		int p1 = wPath.lastIndexOf(FS);
		if (p1 < 0)
			p1 = wPath.lastIndexOf("/");
		p1++;
		int p2 = wPath.lastIndexOf('.');
		if (p2 == -1)
			p2 = pPath.length(); // 拡張子がない場合
		if (p2 <= p1)
			p2 = pPath.length(); // ./aaa のような場合
		if (p2 > p1 && p2 > 0)
			wRtn = wPath.substring(p1, p2);
		// System.out.print (" p1:"+p1);
		// System.out.println(" p2:"+p2);
		return wRtn;
	}

	/***************************************************************************
	 * ファイルの最初の部分（拡張子を含まない）を取り出す<br>
	 * 
	 * @param wPath_I
	 *            ファイルのパス
	 * @return ファイル名 <br>
	 *         《使用例》<br>
	 * <br>
	 *         String wFirstName = FileUtil_.getFirstName("c:\test.txt"); <br>
	 *         System.out.println("FirstName:"+wFirstName); ※ / および 相対パスは駄目
	 **************************************************************************/
	public static String getFirstName2(String pPath) {
		String wRtn = "";
		String wPath = normarizeIt(pPath);
		int p1 = wPath.lastIndexOf("/");
		if (p1 > 0) {
			p1++;
		} else {
			p1 = 0;
		}
		int p2 = wPath.lastIndexOf('.');
		if (p2 == -1)
			p2 = pPath.length(); // 拡張子がない場合
		if (p2 <= p1)
			p2 = pPath.length(); // ./aaa のような場合
		// System.out.println("p1:"+p1);
		// System.out.println("p2:"+p2);
		if (p2 > p1 && p2 > 0)
			wRtn = wPath.substring(p1, p2);
		return wRtn;
	}

	public static String getAbsPath(String targetPath) {
		File wFile = new File(targetPath);
		// 20130507 念のため・・
		if (wFile.exists() && (wFile.isFile() || wFile.isDirectory())) {
			return wFile.getAbsolutePath();
			// try {
			// return wFile.getCanonicalPath();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		return "";
	}

	/***************************************************************************
	 * ファイルの最初の部分（拡張子を含まない）を取り出す<br>
	 * 
	 * @param wPath_I
	 *            ファイルのパス
	 * @return ファイル名 <br>
	 *         《使用例》<br>
	 *         <br>
	 *         String wFirstName = FileUtil.getAbsFirst("c:\dirx\test.txt");
	 *         <br>
	 *         wFirstName => c:\dirx\test ※ / および 相対パスは駄目
	 **************************************************************************/
	public static String getAbsFirst(String pPath) {
		String wRtn = "";
		String wPath = new File(pPath).getAbsolutePath();
		int wPos = wPath.lastIndexOf('.');
		if (wPos == -1)
			wPos = pPath.length(); // 拡張子がない場合
		if (wPos <= 0)
			wPos = pPath.length(); // ./aaa のような場合
		if (wPos > 0)
			wRtn = wPath.substring(0, wPos);
		return wRtn;
	}

	/***************************************************************************
	 * 親のパスを取得 (File.getParent()は使いものにならないので・・)<br>
	 * ※注意お尻に￥を付けないと駄目！<br>
	 * 
	 * @param pPath
	 *            ファイルパス
	 * @return 成功なら,パス文字列 <br>
	 *         《使用例》<br>
	 *         <br>
	 *         String mamapath = FileUtil.getPpath("c:\ga\bba\gabba\hey.txt");
	 *         <br>
	 *         = > c:\ga\bba\gabbaが返る
	 **************************************************************************/
	// public static String getParent(String pPath) {
	// return FileUtil_.getParent(pPath, false);
	// }
	// FileUtil.getPpath("c:\ga\bba\gabba\hey.txt");
	// public static String getParent(String pPath, boolean sw) {
	// if (pPath.indexOf(".") > 0) { // ファイル名が含まれているか
	// int pos = pPath.lastIndexOf(FS);
	// if (pos < 0)
	// pos = pPath.lastIndexOf("/");
	// if (pos < 0) {
	// System.out.println("#Error Not Directory Path!\n\t=>" + pPath);
	// return "";
	// }
	// pPath = pPath.substring(0, pos); // ParentPathを設定し直す
	// }
	// if (sw && !pPath.endsWith(FS)) {
	// return pPath.trim() + FS;
	// }
	// return pPath.trim();
	// }

	/***************************************************************************
	 * ファイル名を分割する・・失敗時はnullを返す
	 * 
	 * @param pPath
	 *            読み込み元となるパス
	 * @return 文字配列 <br>
	 *         《使用例》<br>
	 *         String[] wAns = FileUtil.fileNameAna(wPath_I); if (wAns!=null){
	 *         System.out.println(" 親パス :"+wAns[0]); // ex "c:\aaa\bbb"
	 *         System.out.println(" ファイル名:"+wAns[1]); // ex "name"
	 *         System.out.println(" 拡張子 :"+wAns[2]); // ex ".txt" }
	 **************************************************************************/
	public static String[] fileNameAna(String pPath) {
		pPath = new File(pPath).getAbsolutePath();
		int p1 = pPath.lastIndexOf(FS);
		int p2 = pPath.lastIndexOf('.');
		// System.out.print (" p1:"+p1);
		// System.out.println(" p2:"+p2);
		String[] wAns = null;
		if (p2 > p1 & p1 > 0 & p2 > 0) {
			wAns = new String[3];
			wAns[0] = pPath.substring(0, p1);
			wAns[1] = pPath.substring(p1 + 1, p2);
			wAns[2] = pPath.substring(p2).toLowerCase();
		}
		return wAns;
	}

	/***************************************************************************
	 * バックアップ用ファイルのパス文字列作成<br>
	 * 
	 * @param wPath_I
	 *            元のパス
	 * @param wExt
	 *            拡張子・・・ "" を指定するとオリジナルと同じ拡張子
	 * @param pOpt
	 *            true ならファイル名と同じ名前のフォルダを作成
	 * @return バックアップ用ファイルのパス文字列 <br>
	 *         例》 String oPath =
	 *         FileUtil.makeBkupPath("c:/test.frm","txt",true); <br>
	 *         = > c:￥test￥test.txt となる <br>
	 *         String oPath = makeBkupPath("c:/test.frm","",false); <br>
	 *         = > c:￥test.txt（ ※この場合、元パス＆結果は同じになる）
	 **************************************************************************/
	public static String makeBkupPath(String wPath_I) {
		return makeBkupPath(wPath_I, "bak", false);
	}

	public static String makeBkupPath(String wPath_I, String wEx) {
		return makeBkupPath(wPath_I, wEx, true);
	}

	// -------------------------------------------------------------------------
	public static String makeBkupPath(String wPath_I, String wExt,
			boolean pOpt) {
		String[] val = wPath_I.split("\\."); // ※ split の引数は Regix
		String wPath_O;
		String sDir = val[0]; // ピリオドの直前まで
		if (wExt.trim().equals(""))
			wExt = val[1]; // ピリオドの直後～（拡張子）
		if (pOpt) {
			File wDir = new File(sDir);
			if (wDir.isDirectory() == false) {
				wDir.mkdir();
			}
			String wSep = FS;
			int pos = sDir.lastIndexOf(wSep);
			if (pos < 0)
				pos = 0;
			String wName = sDir.substring(pos, sDir.length());
			wPath_O = sDir + wSep + wName + "." + wExt;
		} else {
			wPath_O = sDir + "." + wExt;
		}
		return wPath_O;
	}

	/***************************************************************************
	 * 再帰的にディレクトリを手繰ってパスの一覧表を作成する
	 * 
	 * @param pPath
	 *            読み込み元となるパス <br>
	 *            《使用例》<br>
	 *            <br>
	 *            例?@》カレントディレクトリのパス一覧をRC.logファイルに出力する <br>
	 *            try{ <br>
	 *            FileOutputStream fo = new FileOutputStream("./RC.log"); <br>
	 *            FileUtil.recurList(new File("."),fo); <br>
	 *            fo.close(); <br>
	 *            catch(IOException e){ e.printStackTrace(); } <br>
	 *            例?A》 <br>
	 *            FileUtil.recurList(new File("c:/"),System.out);
	 **************************************************************************/
	public static void recurList(File pFile, OutputStream pOst) {
		String[] dLIst = new File(pFile, ".").list();
		for (int i = 0; i < dLIst.length; i++) {
			File wFile = new File(pFile, dLIst[i]); // ※第一パラメータ必須！
			if (wFile.isDirectory()) {
				recurList(wFile, pOst);
			} else {
				String wRec = wFile.getAbsolutePath() + LF;
				try {
					pOst.write(wRec.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/***************************************************************************
	 * 読み込み可能か 書き出し可能か？
	 * 
	 * @param pPath
	 *            検査するファイルのパス
	 * @return 読み書き可ならTrueが返る <br>
	 *         《使用例》<br>
	 *         System.out.println("読み書き可？："+FileUtil.canRW("./RC.log"));
	 **************************************************************************/
	public static boolean canRW(String pPath) {
		boolean bStat = false;
		File fl = new File(pPath);
		// &&の場合、片方がfalseだったらもう一方のチェックをしない
		if (fl.canRead() == true && fl.canWrite() == true)
			bStat = true;
		// System.out.println("読み込み可能か " + fl.canRead());
		// System.out.println("書きだし可能か " + fl.canWrite());
		return bStat;
	}

	/***************************************************************************
	 * ファイルサイズを調べる
	 * 
	 * @param pPath
	 *            検査するファイルのパス
	 * @return ファイルサイズが返る <br>
	 *         《使用例》<br>
	 *         System.out.print(FileUtil.fileSize("./RC.log")+" Byte");
	 **************************************************************************/
	public static long fileSize(String pPath) {
		File wCur = new File(pPath);
		return wCur.length();
	}

	public static int fileSizeK(String pPath) {
		long byteSize = fileSize(pPath);
		return (int) (byteSize / 1024);
	}

	public static int fileSizeM(String pPath) {
		long byteSize = fileSize(pPath);
		return (int) (byteSize / (1024 * 1024));
	}


//	public static String fileUpdate(String pPath,String fmt) {
//		return DateCalc.getLastModDate(pPath, "yyyy/MM/dd HH:mm:ss");
//	}

	/***************************************************************************
	 * テキストファイルをコピーする <br>
	 * （バイト単位なのでバイナリー可）<br>
	 * 
	 * @param dstPath
	 *            出力ファイルパス
	 * @param srcPath
	 *            入力ファイルパス
	 * 
	 * @return 成功ならTrue <br>
	 *         《使用例》<br>
	 *         bool b = FileUtil.copyIt(pPath_I,pPath_O); if (b == null) return;
	 **************************************************************************/
	public static boolean fileCopy(String dstPath, String srcPath) {
		if (srcPath.equals(dstPath)) {
			System.out.println("入出力のファイルが同一です！:" + srcPath);
			return false;
		}
		if (iFileChk(srcPath) == null)
			return false;
		File iFile = new File(srcPath);
		if (!oFileChk(dstPath).equals(""))
			return false;
		// 入力パスがディレクトリなら・・新しいディレクトリを作るだけにしよう！！
		if (iFile.isDirectory() == true)
			return true;
		File oFile = new File(dstPath);
		if (oFile.exists())
			oFile.delete();
		if (iFile != null && oFile != null) {
			FileInputStream iSt = create_I_Stream(iFile);
			FileOutputStream oSt = create_O_Stream(oFile);
			if (iSt != null && oSt != null) {
				int rByte = 0;
				byte[] buff = new byte[8192];
				try {
					while ((rByte = iSt.read(buff)) != -1) {
						oSt.write(buff, 0, rByte);
					}
					oSt.close();
					iSt.close();
				} catch (IOException ie) {
					System.out.println("IOException on copyIt");
					System.exit(1);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		return true;
	} // end of copyIt

	/***************************************************************************
	 * ファイルを移動する<br>
	 * （カタログ上）<br>
	 * 
	 * @param pPath_I
	 *            旧ファイルパス
	 * @param pPath_O
	 *            新ファイルパス
	 * @return 成功ならTrue
	 **************************************************************************/
	public static boolean moveIt(String pOld, String pNew) {
		if (pOld.equals(pNew)) {
			System.out.println("入出力のファイルが同一です！:" + pOld);
			return false;
		}
		boolean bStat = false;
		File wFile_O = new File(pOld);
		File wFile_N = new File(pNew);
		if (wFile_O.exists() == false) {
			System.out.println("Error!元ファイルが存在しません・・:" + pOld);
		} else {
			if (wFile_N.exists()) {
				if (wFile_N.canWrite()) {
					wFile_N.delete(); // 存在したら消す
				} else { // ※消せなかったらどうする？
					System.out.println("Error!元ファイルが存在し且つ消せません:" + pNew);
					return false;
				}
			}
			makeParents(pNew); // 親パスを作る
			bStat = wFile_O.renameTo(wFile_N);
		}
		return bStat;
	} // End of moveIt

	// -------------------------------------------------------------------------
	// 拡張子を変更する　
	// ex> kyPkg.uFile.FileUtil.renExt(inPath, "old");
	// -------------------------------------------------------------------------
	public static boolean renExt(String path, String ext) {
		String newPath = "";
		int endIndex = path.indexOf(".");
		if (endIndex > 0) {
			newPath = path.substring(0, endIndex + 1) + ext;
		} else {
			newPath = path.trim() + "." + ext;
		}
		return moveIt(path, newPath);
	}

	/***************************************************************************
	 * rename ファイル名の変更・・・《ファイルの移動も可能》<br>
	 * 
	 * @param oldName
	 *            旧ファイルパス
	 * @param newName
	 *            新ファイルパス
	 * @return 成功ならTrue <br>
	 *         《使用例》<br>
	 *         <br>
	 *         boolean boo = FileUtil.rename("tubby.txt","DubWise/gabba.txt");
	 *         <br>
	 *         boolean boo = FileUtil.moveIt("tubby.txt","DubWise/gabba.txt");
	 **************************************************************************/
	public static boolean rename(String oldName, String newName) {
		return moveIt(oldName, newName);
	}

	/***************************************************************************
	 * ファイルを削除する
	 * 
	 * @param pPath
	 *            削除するファイルのパス
	 * @return 成功ならTrueが返る <br>
	 *         《使用例》<br>
	 *         FileUtil.killIt("./RC.log"); FileUtil.delIt("./RC.log");
	 **************************************************************************/
	public static boolean killIt(String pPath) {
		// System.out.println("killIt===>"+pPath);
		return delIt(pPath);
	}

	public static boolean delIt(String pPath) {
		boolean bStat = false;
		File fl = new File(pPath);
		if (fl.exists() == true) {
			bStat = fl.delete();
		} else {
			// System.out.println("■on delIt FileNotExist:"+pPath);
		}
		return bStat;
	}

	// -------------------------------------------------------------------------
	// ディレクトリ中のファイル一覧
	// 引数 String pDir 対象ディレクトリのパス ex) "."
	// -------------------------------------------------------------------------
	// 例?@》
	// String[] wList = FileUtil.fileList(".");
	// for(int i = 0;i<wList.length;i++){
	// System.out.println(">>" + wList[i]);
	// }
	// -------------------------------------------------------------------------
	public static String[] fileList(String pDir) {
		File fl = new File(pDir);
		return fl.list();
	}

	/***************************************************************************
	 * filteredList 指定した拡張子のファイルの一覧 《無名クラスインナークラス版》 引数 String pDir 対象ディレクトリのパス
	 * ex) "." String pExt 終端文字 ex) ".txt"
	 * -------------------------------------------------------------------------
	 * 例?@》 String[] wList = FileUtil.filteredList(".",".bat"); for(int i =
	 * 0;i<wList.length;i++){ System.out.println("filteredList>" + wList[i]); }
	 **************************************************************************/
	public static String[] filteredList(String pDir, final String pExt) {
		//#createTester--------------------------------------------------
		//		System.out.println("public static void testfilteredList() {");
		//		System.out.println("    String pDir = \"" + pDir + "\";");
		//		System.out.println("    final pExt = " + pExt + ";");
		//		System.out.println("    String[] ar = filteredList(pDir,String);");
		//		System.out.println("}");
		//--------------------------------------------------
		File fl = new File(pDir);
		String[] array = fl.list(new FilenameFilter() {
			@Override
			public boolean accept(File pDir, String pName) {
				return pName.toLowerCase().endsWith(pExt);
			}
		});
		//nullが返ってしまうので修正　20170327
		if (array == null)
			array = new String[] {};
		return array;
	}

	// FileUtil.getDirList
	// デイレクトリ以下にある、ディレクトリ名の一覧をリストで返す（親パスは付かない）
	public static List<String> getDirList(String path) {
		return getFileNameList(path, "Dir");
	}

	// デイレクトリ以下にある、ファイル名の一覧をリストで返す（親パスは付かない）
	public static List<String> getFileList(String path) {
		return getFileNameList(path, "FILE");
	}

	public static List<String> getFileNameList(String path, String option) {
		option = option.toUpperCase();
		List<String> dirNames = new ArrayList();
		List<String> fileNames = new ArrayList();
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				dirNames.add(files[i].getName());
			}
			if (files[i].isFile()) {
				fileNames.add(files[i].getName());
			}
		}
		if (option.equals("FILE")) {
			return fileNames;
		}
		return dirNames;
	}

	// -------------------------------------------------------------------------
	// 《無名クラスインナークラス版》に一本化 （他のコードも実行可能）
	// -------------------------------------------------------------------------
	// filteredList2 拡張子"java"のファイルの一覧 ＜要MyFilterクラス＞
	// class MyFileFilter implements FilenameFilter{
	// public boolean accept(File pDir,String pName){
	// return pName.toLowerCase().endsWith(".java");
	// }
	// }
	// -------------------------------------------------------------------------
	// 例?@》
	// String[] wList = FileUtil.filteredList3(".");
	// for(int i = 0;i<wList.length;i++){
	// System.out.println("java>" + wList[i]);
	// }
	// -------------------------------------------------------------------------
	// public static String[] filteredList3(String pDir){
	// File fl = new File(pDir);
	// return fl.list(new MyFileFilter());
	// }
	// -------------------------------------------------------------------------
	// filteredList2 指定した拡張子のファイルの一覧 《インナークラス版》
	// 無名クラスにできないか？？
	// -------------------------------------------------------------------------
	// 例?@》
	// String[] wList = FileUtil.filteredList2(".",".bat");
	// for(int i = 0;i<wList.length;i++){
	// System.out.println("xxx>" + wList[i]);
	// }
	// -------------------------------------------------------------------------
	// public static String[] filteredList2(String pDir,String pExt){
	// class INMyFileFilter implements FilenameFilter{
	// String wExt;
	// INMyFileFilter(String qExt){ wExt = qExt; } // Constructor
	// public boolean accept(File pDir,String pName){
	// return pName.toLowerCase().endsWith(wExt);
	// }
	// }
	// File fl = new File(pDir);
	// return fl.list(new INMyFileFilter(pExt));
	// }
	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------
	// 拡張子によってファイルを削除する 例 FileUtil.delByExt(".",".txt")
	// -------------------------------------------------------------------------
	public static boolean delByExt(String pDir, String pExt) {
		boolean bStat = true;
		String[] wList = filteredList(pDir, pExt);
		for (int i = 0; i < wList.length; i++) {
			System.out.println("dwlByExt>" + wList[i]);
			File fl = new File(pDir + FS + wList[i]);
			if (fl.exists() == true) {
				bStat = fl.delete();
				System.out.println("delete ok?");
			} else {
				bStat = false;
				System.out.println("Not Exist");
			}
		}
		return bStat;
	}

	/***************************************************************************
	 * 正規表現にマッチするファイルの一覧 <br>
	 * 《無名クラスインナークラス版》 <br>
	 * 
	 * @param pDir
	 *            対象ディレクトリのパス ex) "."
	 * @param pExt
	 *            正規表現 ex) "sm.*txt"
	 * @return ファイルの一覧 <br>
	 *         《使用例》<br>
	 *         <br>
	 *         String[] wList = FileUtil.RegfilteredList(".","SM.*txt"); <br>
	 *         for(int i = 0;i<wList.length;i++){ <br>
	 *         System.out.println("xxx>" + wList[i]); <br>
	 **************************************************************************/
	// import java.util.regex.*;
	public static String[] regixFilteredList(String pDir, final String pRegex) {
		if (!pDir.endsWith("/")) {
			pDir = pDir.trim() + "/";//20170327　誤動作を防止する
		}
		File fl = new File(pDir);
		return fl.list(new FilenameFilter() {
			@Override
			public boolean accept(File pDir, String pName) {
				//				 System.out.println("◆on accept pName:"+pName);
				return pName.toLowerCase().matches(pRegex.toLowerCase());
			}
		});
	}

	/***************************************************************************
	 * ディレクトリを作成する（パスに含まれるものスベテ！！）<br>
	 * 作れた場合は親パスを返す、失敗したら""を返す<br>
	 * 
	 * @param dir
	 *            ファイルパス
	 * @return 成功なら,パス文字列 <br>
	 *         《使用例》<br>
	 *         <br>
	 *         String mamaPath =
	 *         FileUtil.makeParents("c:\ga\bba\gabba\hey.txt");
	 **************************************************************************/
	// -------------------------------------------------------------------------------
	// 当該ディレクトリが存在しなければこれを作成して、後ろにファイル区切り文字を添えて返す
	// String localDir = FileUtil.mkdir("c:/suzy/cream/cheeze/");
	// String localDir = FileUtil.mkdir("c:/suzy/cream/cheeze");
	// localDir=> c:\suzy\cream\cheeze\
	// ※パラメータの終わりがパス区切り文字じゃなくてもＯＫ
	// -------------------------------------------------------------------------------
	// public static String mkdir(String pPath) {
	// pPath = charsetConv(pPath);
	// File file = new File(pPath);
	// if (!file.exists())
	// file.mkdirs();
	// return file.getPath() + "/";
	// // return file.getPath() + FS;
	// }
	// Directoryを作りそのパスを返す（パスのお尻にFSがつく！）
	// kyPkg.uFile.FileUtil.mkdir("c:/hello/")
	public static String mkdir(String dir) {
		return makedir(dir);
	}

	// kyPkg.uFile.FileUtil.makedir(dir);
	public static String makedir(String pPath) {
		// getAbsolutePath("c:/frank/zappa/")
		// ファイルの区切り文字はすべてFSに変換された上で一番最後のFSがはずされるので
		// 返りはこうなる＝＞c:\frank\zappa のでFSをコンカチする・・・
		// System.out.println("DEBUG pPath:"+pPath);
		pPath = charsetConv(pPath);
		String abs = new File(pPath).getAbsolutePath() + FS;
		File file = new File(abs);
		if (file.exists() == false) {
			if (file.isDirectory() == false) {
				if (file.mkdirs() == false) {
					System.out.println("@makedir Error :" + abs);
					abs = "";
				}
			}

		}
		abs = abs.replaceAll("\\\\", "/");
		return abs.trim();
	}

	// 例 kyPkg.uFile.FileUtil.makeParents("./test/Some.txt");
	public static String makeParents(String path) {
		String wPath = "";
		if (!path.trim().equals("")) {
			File file = new File(path);
			wPath = file.getAbsolutePath();
			//	入力が”ｃ:\samples\result.txt”の時に・・・
			//			System.out.println("#1 getPath:" + file.getPath());
			// if (wPath.indexOf(".") < 0) wPath = wPath +
			// System.getProperty("file.separator");
			int pos = wPath.lastIndexOf(FS);
			if (pos > 0)
				wPath = wPath.substring(0, pos);
			// System.out.println("makeParents oya?!:"+wPath);
			makedir(wPath);
			// wFile = new File(wPath);
			// if (wFile.exists() == true) {
			// // System.out.println("# File Path already
			// // existed!\n\t=>"+wPath);
			// wPath = "";
			// } else {
			// if (wFile.mkdirs() == false)
			// wPath = ""; // ここが実体
			// }
		}
		return wPath;
	}

	/***************************************************************************
	 * ディレクトリごとコピーする <br>
	 * 内部クラスなので使用方法に注意！<br>
	 * 
	 * @param pOld
	 *            旧ファイルパス
	 * @param pNew
	 *            新ファイルパス
	 * @return 成功ならTrue <br>
	 *         《使用例》<br>
	 *         <br>
	 *         new FileUtil.CopyDir(".","c:/Target");
	 **************************************************************************/
	public class CopyDir {
		String gTarget;

		int gLen;

		boolean gMove;

		// -------------------------------------------------------------------------
		// Constructor
		// -------------------------------------------------------------------------
		public CopyDir(String pSource, String pTarget) {
			this(pSource, pTarget, false); // copy !!
		}

		public CopyDir(String pSource, String pTarget, boolean pMove) {
			if (pSource.equals(pTarget)) {
				System.out.println("#Error 入出力のファイルが同じ：" + pSource);
			} else {
				File wFile = new File(pSource);
				if (wFile.exists() == true && wFile.isDirectory() == true) {
					gTarget = pTarget;
					gMove = pMove;
					gLen = pSource.length();
					recurCopy(wFile);
				} else {
					System.out.println("#Error 入力パス異常：" + pSource);
				}
			}
		}

		/***********************************************************************
		 * 再帰的にコピーする<BR>
		 * 
		 * @param pParent
		 *            親ファイルクラス
		 **********************************************************************/
		public void recurCopy(String path) {
			File pParent = new File(path);
			recurCopy(pParent);
		}

		public void recurCopy(File pParent) {
			String[] dLIst = new File(pParent, ".").list();
			for (int i = 0; i < dLIst.length; i++) {
				File wFile = new File(pParent, dLIst[i]); // ※第一パラメータ必須！
				if (wFile.isDirectory()) {
					recurCopy(wFile);
				} else {
					String wSorce = wFile.getPath();
					String wTarget = gTarget + wSorce.substring(gLen);
					System.out.println("  ■" + wSorce + "  □" + wTarget);
					// mkdir & Copy 処理なのだ・・・
					makeParents(wTarget);
					if (gMove == true) {
						moveIt(wSorce, wTarget);
					} else {
						fileCopy(wTarget, wSorce);
					}
				}
			}
		}
	} // End of class CopyDir

	/***************************************************************************
	 * 入力ストリーム作成<br>
	 * 
	 * @param pPath
	 *            ファイルパス
	 * @return 入力ストリーム
	 **************************************************************************/
	private static FileInputStream create_I_Stream(File pFile) {
		try {
			FileInputStream wstr = new FileInputStream(pFile);
			return wstr;
		} catch (FileNotFoundException fe) {
			System.out.println("ファイルが見つかりません：" + pFile.toString());
		} catch (SecurityException se) {
			System.out.println("セキュリティ例外：" + pFile.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	/***************************************************************************
	 * 出力ストリーム作成<br>
	 * 
	 * @param pPath
	 *            ファイルパス
	 * @return 出力ストリーム
	 **************************************************************************/
	private static FileOutputStream create_O_Stream(File pFile) {
		try {
			FileOutputStream wstr = new FileOutputStream(pFile);
			return wstr;
		} catch (FileNotFoundException fe) {
			System.out.println(
					"#ERROR@create_O_Stream ファイルを開けません：" + pFile.toString());
		} catch (SecurityException se) {
			System.out.println(
					"#ERROR@create_O_Stream セキュリティ例外：" + pFile.toString());
		} catch (Exception e) {
			System.out.println("#ERROR@create_O_Stream ??：" + pFile.toString());
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	// /***************************************************************************
	// * 出力ファイルチェック<br>
	// * @param pPath
	// * ファイルパス
	// * @return ファイルクラス if ( FileUtil_.oFileChk(iPath).equals("") ){ //OK File
	// * wFile = new File(path); }
	// **************************************************************************/
	public static void string2file_(String outPath, String strData,
			String encode) {
		ListArrayUtil.array2File(outPath, new String[] { strData }, encode);
	}

	public static void string2file_(String outPath, String strData) {
		ListArrayUtil.array2file(outPath, new String[] { strData });
	}

	// -------------------------------------------------------------------------
	// outFile の最終行に文字列を付け加える
	// -------------------------------------------------------------------------
	public static boolean str2FileMod(String outPath, String wRec) {
		if (!new File(outPath).exists()) {
			System.out.println("出力パスが存在しません:" + outPath);
			return false;
		}
		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(outPath, true));
			bw.write(wRec);
			bw.write(LF);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------
	// outFile に modFileを追書きする
	// -------------------------------------------------------------------------
	public static int fileMod(String outFile, String modFile) {
		return fileMod(outFile, modFile, true);
	}

	// -------------------------------------------------------------------------
	// outFile に modFileを書き込む（appendがtrueなら追書き、falseなら上書き）
	// -------------------------------------------------------------------------
	public static int fileMod(String outPath, String path, boolean append) {
		int cnt = 0;
		makeParents(outPath); // 親パスが無ければ作る
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(outPath, append));
			while (br.ready()) {
				cnt++;
				String wRec = br.readLine();
				bw.write(wRec);
				bw.write(LF);
			}
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnt;
	}

	/***************************************************************************
	 * ReadHex ヘキサダンプを表示する<br>
	 * 
	 * @param pFile
	 *            読み込むファイル
	 * @param wSkip
	 *            スキップする行数（１６ビット単位）
	 * @param wMaxl
	 *            読み込む行数（０を指定すると最後まで読み込む） <br>
	 *            使用例 wStr = ReadHex(argv[0],wSkip,wMaxl); <br>
	 *            返値をＶｅｃｔｏｒ化できないだろうか？？？挑戦してみよう！ <br>
	 *            課題：多バイト文字を表示できないだろうか？
	 **************************************************************************/
	public static String readAsHex(String pFName, long pSkip, long pMax) {
		int poz = 0;
		long pCnt = 0;
		byte[] buff = new byte[1024];
		boolean cont = true;
		FileInputStream infile = null;
		StringBuffer sBuf1 = new StringBuffer("");
		StringBuffer sBuf2 = new StringBuffer("");
		StringBuffer sBuf3 = new StringBuffer("");

		if (iFileChk(pFName) == null) {
			return "Error";
		}
		File wFile = new File(pFName);

		// System.out.println("File.length:" + wFile.length());
		if (pMax < 1)
			pMax = (wFile.length() / 16) + 1 - pSkip;
		pSkip = pSkip * 16;
		if (wFile.length() < pSkip) {
			System.out.println("skipパラメータがファイルサイズを超えました");
			return "";
		}
		// --------------------------------------------------
		try {
			infile = new FileInputStream(pFName);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		// --------------------------------------------------
		try {
			long wSkip = infile.skip(pSkip);
			System.out.println("Skipped:" + wSkip);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// --------------------------------------------------
		while (cont) {
			try {
				int n = infile.read(buff, 0, 16);
				poz += n;
				if (n > 0) {
					pCnt++;
					if (pCnt == pMax)
						cont = false;
					sBuf1.delete(0, sBuf1.length());
					sBuf2.delete(0, sBuf2.length());
					for (int i = 0; i < n; i++) {
						// ----------------------------------
						char hex1, hex2;
						hex1 = (char) (buff[i] & 0xF0);
						hex1 >>= 4;
						hex2 = (char) (buff[i] & 0x0F);
						// ----------------------------------
						if (hex1 >= 10) {
							hex1 = (char) (hex1 + 'A' - 10);
						} else {
							hex1 = (char) (hex1 + '0');
						}
						// ----------------------------------
						if (hex2 >= 10) {
							hex2 = (char) (hex2 + 'A' - 10);
						} else {
							hex2 = (char) (hex2 + '0');
						}
						// ----------------------------------
						// System.out.print( " "+hex1 + hex2);
						sBuf2.append(" " + hex1 + hex2);
						if (buff[i] < 0x20 || 0x7e < buff[i]) {
							sBuf1.append("."); // 表示可能文字以外
						} else {
							sBuf1.append((char) (buff[i]));
						}
					}
					for (int i = 0; i < (16 - n); i++) {
						sBuf2.append("   "); // padding!
					}
					// System.out.println(sBuf2.toString() + " |" +
					// sBuf1.toString());
					sBuf3.append(
							sBuf2.toString() + " |" + sBuf1.toString() + "\n");
					// System.out.write(buff,0,n);
				} else
					cont = false;
			} catch (EOFException e) {
				// e.printStackTrace();
				System.out.println("<<EOF???>>");
				cont = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			infile.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		// System.out.println(sBuf3.toString());
		return (sBuf3.toString());
	}

	static public byte[] readByte(File objFile, int iStartPos, int iLength) {
		FileInputStream objFIS = null;
		byte byteBuff[] = null;
		if (objFile.length() < iStartPos + iLength) {
			return null;
		}
		try {
			objFIS = new FileInputStream(objFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		try {
			byteBuff = new byte[iLength];
			objFIS.skip(iStartPos);
			objFIS.read(byteBuff, 0, iLength);
			objFIS.close();
		} catch (IOException e) {
			System.out.print("MP3Info::readByte() " + objFile.getName() + "\n");
			e.printStackTrace();
			return null;
		}
		return byteBuff;
	}

	static public boolean readHeader(String path, String key) {
		File objFile = new File(path);
		byte[] byteArray = FileUtil.readByte(objFile, 0, key.length());
		if (byteArray != null
				&& (new String(byteArray, 0, key.length())).matches(key))
			return true;
		return false;
	}

	/***************************************************************************
	 * JTextAreaにデータを読み込む<br>
	 * 
	 * @param pTex
	 *            読み込み先となるJTextArea
	 * @param pFile
	 *            読み込むファイル String ans = kyPkg.util.FileUtil.file2String(path);
	 **************************************************************************/
	public static String file2Str(String pPath) {
		int charsRead = 0;
		File file = new File(pPath);
		long lFileSize = file.length();
		if (lFileSize > Integer.MAX_VALUE) {
			System.out.println(
					"#ERROR @file2Str Integer.MAX_VALUE < " + lFileSize);
			return null;
		}
		int fileSize = (int) lFileSize;
		char charArray[] = new char[fileSize];
		try {
			FileReader fr = new FileReader(pPath);
			charsRead = fr.read(charArray);
			System.out.println(
					"charsRead: " + (charsRead / (1024 * 1024)) + "MByte");
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		String rStr = "overFlow";
		System.out.println(
				"x  charsRead: " + (charsRead / (1024 * 1024)) + "MByte");
		try {
			System.out.println("debug1: ");
			rStr = new String(charArray);
			System.out.println("debug2: ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(
				"xx charsRead: " + (charsRead / (1024 * 1024)) + "MByte");
		return rStr.trim();
	}

	public static String file2String(String path) {
		if (path == null)
			return null;
		if (path.equals(""))
			return null;
		return file2String(path, 1, Integer.MAX_VALUE);
	}

	public static void file2Stream(java.io.PrintWriter writer, String path) {
		if (iFileChk(path) == null)
			return;
		File pFile = new File(path);
		long lFileSize = pFile.length();
		System.out.println("xxfile2Stream path: " + path);
		System.out.println("xxfile2Stream canRead: " + pFile.canRead());

		if (lFileSize > Integer.MAX_VALUE) {
			System.out.println(
					"#ERROR @file2Str Integer.MAX_VALUE < " + lFileSize);
			return;
		}
		int fileSize = (int) lFileSize;
		System.out.println(
				"xxfile2Stream fileSize: " + (fileSize / (1024)) + "KByte");
		// System.out.println("xxfile2Stream fileSize:
		// "+(fileSize/(1024*1024))+"MByte");

		InputStreamReader isr = null;
		// ---------------------------------------------------------------------
		// 仮読み込みを行い、エンコードを判定したいが・・・
		// ---------------------------------------------------------------------
		// unicodeと仮定して読んでみる、エラーならJISAutoDetectとする
		// しかし・・UTFの場合Exceptionとならずに文字化けをおこす（T.T）
		// ---------------------------------------------------------------------
		// String wCharSet = "UnicodeLittleUnmarked";
		// wCharSet = "JISAutoDetect";

		// ---------------------------------------------------------------------
		try {
			int integer;
			// isr = new InputStreamReader(new FileInputStream(pFile),
			// wCharSet);
			isr = new InputStreamReader(new FileInputStream(pFile));
			while ((integer = isr.read()) != -1) {
				// System.out.print(integer);
				writer.write(integer);
			}
			isr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * JTextAreaにデータを読み込む<br>
	 * 
	 * @param pTex
	 *            読み込み先となるJTextArea
	 * @param pFile
	 *            読み込むファイル
	 * @param pFrom
	 *            読み込み開始行
	 * @param pLimit
	 *            読み込み終了行 <br>
	 *            ※unicode対策をどうするか？？ <br>
	 *            読み込む範囲 何行目から、何行目までの指定を追加する
	 **************************************************************************/
	public static String file2String(String path, int pFrom, int pLimit) {
		File pFile = iFileChk(path);
		if (pFile == null)
			return null;
		long lFileSize = pFile.length();
		if (lFileSize > Integer.MAX_VALUE) {
			System.out.println(
					"#ERROR @file2Str Integer.MAX_VALUE < " + lFileSize);
			return null;
		}
		int fileSize = (int) lFileSize;

		StringBuffer sBuf = new StringBuffer(fileSize);
		FileInputStream fs = null;
		InputStreamReader isr = null;
		// ---------------------------------------------------------------------
		// 仮読み込みを行い、エンコードを判定したいが・・・
		// ---------------------------------------------------------------------
		// unicodeと仮定して読んでみる、エラーならJISAutoDetectとする
		// しかし・・UTFの場合Exceptionとならずに文字化けをおこす（T.T）
		// ---------------------------------------------------------------------
		String wCharSet = "UnicodeLittleUnmarked";
		wCharSet = "JISAutoDetect";
		// try {
		// fs = new FileInputStream(pFile);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// try {
		// isr = new InputStreamReader(fs, wCharSet);
		// // System.out.println("InputStreamReader.getEncoding():" +
		// // isr.getEncoding());
		// BufferedReader br = new BufferedReader(isr);
		// if (br.ready()) {
		// br.readLine();
		// // 以下の文でエラーを拾えないか？
		// // wRec = br.readLine();
		// // wRec = new String(s.getBytes("JISAutoDetect"), "SJIS")
		// }
		// br.close();
		// // } catch (sun.io.MalformedInputException me){
		// // wCharSet ="JISAutoDetect";
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// ---------------------------------------------------------------------
		try {
			try {
				fs = new FileInputStream(pFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			isr = new InputStreamReader(fs, wCharSet);
			// -----------------------------------------------------------------
			// 疑問：filereaderだとキャラクターセットは指定できないのか？
			// -----------------------------------------------------------------
			BufferedReader br = new BufferedReader(isr);
			int i = 0;
			int iLct = 0;
			String wRec;
			// -----------------------------------------------------------------
			// Skip!!
			// -----------------------------------------------------------------
			for (int j = 0; j < (pFrom - 1); j++) {
				if (br.ready()) {
					iLct++;
					br.readLine();
				}
			}
			// -----------------------------------------------------------------
			// Loop!!
			// -----------------------------------------------------------------
			while (br.ready()) {
				wRec = br.readLine();
				// wRec = new String(s.getBytes("JISAutoDetect"), "SJIS")
				i++;
				iLct++;
				if (wRec != null) {
					// System.out.println(wRec);
					sBuf.append(wRec);
					sBuf.append("\n");
				}
				if (i >= pLimit)
					break;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("@@@" );
		return sBuf.toString();
	}

	// ------------------------------------------------------------------------
	// 《 Ufile2String 》使用箇所→JP_IOtest 279行目
	// なぜわざわざキャラクター配列に読み込んでいるのか？？
	// 意図がわかんない
	// ------------------------------------------------------------------------
	public String readUnicode(String path) {
		if (iFileChk(path) == null)
			return "";
		File pFile = new File(path);
		StringBuffer buff = new StringBuffer();
		try {
			DataInputStream dataInput = new DataInputStream(
					new FileInputStream(pFile));
			int nDataLen = dataInput.available();
			for (int nLoop = 0; nLoop < nDataLen; nLoop++) {
				char ch = dataInput.readChar(); // 入力ストリームの次の 2 バイトを表す Unicode文字
				System.out.print(">>" + ch);
				buff.append(ch);
			}
			dataInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	// ------------------------------------------------------------------------
	// DataOutputStream
	// データ出力ストリームを使うと、プリミティブ型の Java データを
	// 移植性のある形で出力ストリームに書き込むことができます。
	// メソッド
	// void flush()
	// int size()
	// void write(byte[] b, int off, int len)
	// void write(int b)
	// void writeBoolean(boolean v)
	// void writeByte(int v)
	// void writeBytes(String s)
	// void writeChar(int v)
	// void writeChars(String s)
	// void writeDouble(double v)
	// void writeFloat(float v)
	// void writeInt(int v)
	// void writeLong(long v)
	// void writeShort(int v)
	// void writeUTF(String str)
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// 文字列を指定されたエンコードに変換する
	// <<charsetName 文字セット>>
	// US-ASCII 7 ビット ASCII (ISO646-US/Unicode 文字セットの Basic Latin ブロック)
	// ISO-8859-1 ISO Latin Alphabet No. 1 (ISO-LATIN-1)
	// UTF-8 8 ビット UCS 変換形式
	// UTF-16BE 16 ビット UCS 変換形式、ビッグエンディアンバイト順
	// UTF-16BE 16 ビット UCS 変換形式、リトルエンディアンバイト順
	// UTF-16 16 ビット UCS 変換形式、オプションのバイト順マークで識別されるバイト順
	// ------------------------------------------------------------------------
	public static String encode(String str, String charsetName) {
		// String をバイトシーケンスに符号化し、結果を新規バイト配列に格納。
		try {
			byte[] byteArray = str.getBytes(charsetName);
			return new String(byteArray, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	// -------------------------------------------------------------------------
	// デフォルトの文字セットを返します。(ex windows-31j)
	// System.out.println(kyPkg.uFile.FileUtil.getDefaultCharset());
	// -------------------------------------------------------------------------
	public static String getDefaultCharset() {
		return (java.nio.charset.Charset.defaultCharset()).name();
	}

	/***************************************************************************
	 * 指定されたエントリーを取り除く（Schema.iniファイルなど）<br>
	 * 
	 * @param path
	 *            読み込むファイルへのパス
	 * @param outPath
	 *            書き出すファイルへのパス
	 * @param pEntry
	 *            除去するエントリー <br>
	 *            《使用例》<br>
	 *            new FileUtil().rmvEnt(ISAM.SCHEMA_INI,"Copy.txt","[K2.TXT]");
	 *            このあとリネームしとく！
	 **************************************************************************/
	public void rmvEnt(String path, String outPath, String pEntry) {
		boolean wFlg = true;
		boolean append = true;
		System.out.println("■rmvEnt pI_Path:" + path);
		System.out.println("         pO_Path:" + outPath);
		System.out.println("         pEntry:" + pEntry);
		try {
			int i = 0;
			String wRec;
			if (new File(path).exists() == false) {
				return; // 入力ファイルが無かったらさよならする
			}
			// if (new File(pO_Path).exists()==false) pAppend = false;
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(outPath, append));
			// -----------------------------------------------------------------
			while (br.ready()) {
				i++;
				wRec = br.readLine();
				System.out.println("wRec>" + wRec);
				if (wRec.startsWith("[")) {
					System.out.println("[wRec>" + wRec);
					if (wRec.startsWith(pEntry)) {
						wFlg = false;
					} else {
						wFlg = true;
					}
				}
				if (wFlg == false) {
					System.out.println("rejected>" + wRec);
				} else {
					bw.write(wRec, 0, wRec.length());
					bw.write(LF, 0, LF.length()); // 改行コード
				}
			}
			bw.close();
			br.close();
			// -----------------------------------------------------------------
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	} // end of rmvEnt

	/***************************************************************************
	 * テーブルモデル中のデータをファイルに書き出す
	 * 
	 * @param resultPath
	 *            ファイルの出力先パス
	 * @param tModel
	 *            入力データ（テーブルモデル）
	 * @param headOption
	 *            要ヘッダーかどうか
	 * @param delimiter
	 *            区切り文字
	 * @return 書き出した行数
	 **************************************************************************/
	public static String getTModelHeader(TableModel tModel, String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < tModel.getColumnCount(); j++) {
			if (j > 0)
				buf.append(delimiter);
			buf.append(tModel.getColumnName(j));
		}
		buf.append(LF); // 改行コード
		return buf.toString();
	}

	/***************************************************************************
	 * テーブルモデル中のデータをファイルに書き出す
	 * 
	 * @param outPath
	 *            ファイルの出力先パス
	 * @param tModel
	 *            入力データ（テーブルモデル）
	 * @param headOption
	 *            要ヘッダーかどうか
	 **************************************************************************/
	public int tmdl2file(String outPath, TableModel tModel,
			boolean headOption) {
		// 拡張子による、区切り文字の判定を入れよう・・
		if (outPath.indexOf(".") == -1)
			outPath = outPath + ".CSV";
		String[] val = outPath.split("\\."); // ※ split の引数は Regix
		String wExt = val[1].toUpperCase(); // ピリオドの直後～（拡張子）
		String delimiter = "";
		System.out.println("拡張子：" + wExt + "によって区切り文字を判定している");
		if (wExt.equals("TXT")) {
			delimiter = "\t";
		} else if (wExt.equals("CSV")) {
			delimiter = ",";
		} else if (wExt.equals("PRN")) {
			delimiter = "\t";
		} else if (wExt.equals("TSV")) {
			delimiter = "\t";
		} else if (wExt.equals("SSV")) {
			delimiter = " ";
		} else {
			delimiter = "";
		}
		return tmdl2file(outPath, tModel, headOption, delimiter);
	}

	public int tmdl2file(String outPath, TableModel tModel, boolean headOption,
			String delimiter) {

		int wCnt = 0;

		//String encode = SHIFT_JIS;
		String encode = MS932;//20161222

		//		String newVal = "ー";
		//		String regex = "～";

		System.out.println("tmdl2file => outPath:" + outPath);
		try {
			OutputStreamWriter writer = FileUtil.getWriter(outPath, encode);
			String wRec = "";
			StringBuffer buf = new StringBuffer();
			// -----------------------------------------------------------------
			// Ｈｅａｄ
			// -----------------------------------------------------------------
			if (headOption == true) {
				wRec = getTModelHeader(tModel, delimiter);
				//				wRec = StringEncoder.utf8ToSjis(wRec);
				wRec = Onbiki.cnv2Similar(wRec, FileUtil.defaultEncoding);
				writer.write(wRec, 0, wRec.length());
				wCnt++;
			}
			// -----------------------------------------------------------------
			// Ｂｏｄｙ
			// -----------------------------------------------------------------
			// int wRow = tModel.getRowCount(); // 行数
			for (int i = 0; i < tModel.getRowCount(); i++) {
				buf.delete(0, buf.length()); // バッファをクリア
				for (int j = 0; j < tModel.getColumnCount(); j++) {
					if (j > 0)
						buf.append(delimiter);
					Object wObj = tModel.getValueAt(i, j);
					if (wObj != null) {
						buf.append(wObj.toString());
					} else {
						buf.append("");
					}
				}
				wCnt++;
				buf.append(LF); // 改行コード
				wRec = buf.toString();
				//				wRec=wRec.replaceAll(regex, newVal);
				//				wRec = StringEncoder.utf8ToSjis(wRec);
				wRec = Onbiki.cnv2Similar(wRec, FileUtil.defaultEncoding);

				//				wRec = new String(wRec.getBytes(encode), encode);
				// System.out.println("wRec>" + wRec);
				// oBw.write(wRec, 0, wRec.length());
				writer.write(wRec, 0, wRec.length());
			}
			// oBw.close();
			writer.close();
			// -----------------------------------------------------------------
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return wCnt;
	} // tmdl2file

	// ------------------------------------------------------------------------
	// ファイルの情報をストリングで返す
	// pPath ファイルのパス
	// 例》 String wFinfo = fileInfo("System.ini");
	// ------------------------------------------------------------------------
	public static String fileInfo(String pPath) {
		pPath = new File(pPath).getAbsolutePath();
		String wMsg;
		StringBuffer sBuf = new StringBuffer();
		sBuf.delete(0, sBuf.length());
		File wFile = new File(pPath);
		sBuf.append("ファイル名    : " + wFile.getName());
		sBuf.append((wFile.isFile() ? " ファイルです\n" : " 名前パイプか、ディレクトリです\n"));
		// sBuf.append("ファイルサイズ: " + (wFile.length())+"\n");
		sBuf.append("ファイルサイズ: " + (wFile.length()) + " Byte \n");
		sBuf.append("ファイルサイズ: " + (wFile.length() / 1024) + " KByte \n");
		sBuf.append("ファイルのパス: " + wFile.getPath() + "\n");
		sBuf.append("絶対パス        : " + wFile.getAbsolutePath() + "\n");
		// ※↓要import java.text.*;
//		String wDate = DateFormat.getDateInstance().format(new Date(wFile));
		String wDate = DateCalc.getLastModDate(pPath, "yyyyMMddHHmmss");

		sBuf.append("最終修正      : " + wDate + "\n");
		sBuf.append((wFile.exists() ? "存在します" : "存在しません") + "\n");
		sBuf.append((wFile.canRead() ? "読み込み可能" : "読み込み不能") + "\n");
		// sBuf.append((wFile.canWrite() ? "書き込み可能" : "書き込み不能")+"\n");
		sBuf.append((wFile.isDirectory() ? "ディレクトリです" : "ディレクトリではない") + "\n");
		if (wFile.isDirectory()) {
			wMsg = "#<<Directory>>########################################";
			sBuf.append(wMsg + "\n");
			File[] flist = wFile.listFiles();
			for (int i = 0; i < flist.length; i++) {
				sBuf.append(">" + flist[i].getName() + "\n");
			}
		}
		return sBuf.toString();
	}

	/***************************************************************************
	 * BufferedReaderを返す<br>
	 * 
	 * @param path
	 *            読み込むファイル
	 * @return 失敗したらnullが返る <br>
	 *         例 <br>
	 *         try{ <br>
	 *         BufferedReader br = new
	 *         FileUtil().getISReader("C:\#340018000310.IT2"); <br>
	 *         if (br.ready()) { <br>
	 *         String wRec = br.readLine(); <br>
	 *         <br>
	 *         br.close(); <br>
	 *         catch (Exception e) { <br>
	 *         e.printStackTrace(); <br>
	 **************************************************************************/
	public static BufferedReader getBufferedReader(String path) {
		if (iFileChk(path) == null)
			return null;
		try {
			return new BufferedReader(new FileReader(new File(path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//	public static BufferedReader getBufferedReader(String path) {
	//		try {
	//			return new BufferedReader(new InputStreamReader(
	//					new FileInputStream(path), DEFAULT_ENCODE));
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}

	/***************************************************************************
	 * 拡張子からデリミターを決定<br>
	 * 
	 * @param ext
	 *            拡張子
	 * @return 区切り文字 <br>
	 **************************************************************************/
	public static String getDefaultDelimiter(String ext) {
		ext = ext.toUpperCase().trim();
		String delimiter = "";
		if (ext.equals("")) {
			delimiter = " ";
		} else if (ext.equals("BAK")) {
			delimiter = ",";
		} else if (ext.equals("TMP")) {
			delimiter = ",";
		} else if (ext.equals("TXT")) {
			delimiter = ",";
		} else if (ext.equals("CSV")) {
			delimiter = ",";
		} else if (ext.equals("PRN")) {
			delimiter = "\t";
		} else if (ext.equals("TSV")) {
			delimiter = "\t";
		} else if (ext.equals("DAT")) {
			delimiter = "\t";
		} else if (ext.equals("OLD")) {
			delimiter = "\t";
		} else {
			delimiter = "\t";
		}
		return delimiter;
	}

	// -------------------------------------------------------------------------------
	// パス区切り文字を”/”に変換する
	// String path ="c:\\suzy\\cream\\cheeze\\zappa.txt";
	// path = kyPkg.uFile.FileUtil_.normarizeIt(path);
	// System.out.println("path:"+path);
	// System.out.println("normarizeIt:"+FileUtil_.normarizeIt("c:\\suzy\\cream\\cheeze\\zappa.txt"));
	// -------------------------------------------------------------------------------
	public static String normarizeIt(String path) {
		String PS = System.getProperty("file.separator");
		if (PS.equals("\\"))
			PS = "\\\\";
		path = path.replaceAll(PS, "/");
		return path;
	}

	public static String cnv2localStyle(String path) {
		String PS = System.getProperty("file.separator");
		if (PS.equals("\\"))
			PS = "\\\\";
		path = path.replaceAll("/", PS);
		return path;
	}

	/***************************************************************************
	 * ファイルの拡張子を取り出す<br>
	 * 
	 * @param pPath
	 *            ファイルのパス
	 * @return ピリオドの直後～（拡張子） <br>
	 *         《使用例》<br>
	 * <br>
	 *         String wExt = FileUtil_.getExt("c:\test.ext"); <br>
	 *         => EXT
	 **************************************************************************/
	public static String getExt(String pPath) {
		pPath = normarizeIt(pPath);
		String[] splited = pPath.split("/");
		String[] val = splited[splited.length - 1].split("\\.");
		if (val.length > 1) {
			return val[val.length - 1].toUpperCase();
		}
		return "";
	}

	/***************************************************************************
	 * ファイルの拡張子より前の部分を返す<br>
	 * 
	 * @param inPath
	 *            ファイルのパス
	 * @return ファイルの拡張子より前の部分 <br>
	 *         《使用例》<br>
	 * <br>
	 *         String wExt = FileUtil_.getPreExt("c:\\test.ext"); <br>
	 *         => c:\test
	 **************************************************************************/
	public static String getPreExt(String inPath) {
		String wExt = getExt(inPath);// 拡張子を取り出す
		if (wExt.equals("")) {
			return inPath;
		} else {
			return inPath.substring(0, inPath.length() - (wExt.length() + 1));
		}
	}

	/***************************************************************************
	 * 親のパスを取得 (File.getParent()は使いものにならないので・・)<br>
	 * ※注意お尻に￥を付けないと駄目！<br>
	 * 
	 * @param pPath
	 *            ファイルパス
	 * @return 成功なら,パス文字列 <br>
	 *         《使用例》<br>
	 * <br>
	 *         String mamapath = FileUtil_.getParent("c:\ga\bba\gabba\hey.txt"); <br>=
	 *         > c:\ga\bba\gabbaが返る
	 **************************************************************************/
	public static String getParent(String pPath) {
		// ※終端”/”が含まれないので注意！！（含めたい場合オプション付きを使う）
		return getParent(pPath, false);
	}

	// optionがtureなら終端”/”を追加する
	public static String getParent2(String pPath, boolean option) {
		pPath = FileUtil.normarizeIt(pPath);
		if (pPath.indexOf(".") > 0) { // ファイル名が含まれているか
			// int pos = pPath.lastIndexOf(FS);
			// if (pos < 0)
			int pos = pPath.lastIndexOf("/");
			if (pos < 0) {
				System.out.println("#Error Not Directory Path!\n\t=>" + pPath);
				return "";
			}
			pPath = pPath.substring(0, pos); // ParentPathを設定し直す

		}
		if (option && !pPath.endsWith("/")) {
			return pPath.trim() + "/";
		}
		return pPath.trim();
	}

	//-------------------------------------------------------------------------
	// optionがtureなら終端”/”を追加する
	//-------------------------------------------------------------------------
	public static String getParent(String pPath, boolean option) {
		pPath = normarizeIt(pPath);
		if (pPath.indexOf(".") > 0) { // ファイル名が含まれているか
			// int pos = pPath.lastIndexOf(FS);
			// if (pos < 0)
			int pos = pPath.lastIndexOf("/");
			if (pos < 0) {
				System.out.println("#Error Not Directory Path!\n\t=>" + pPath);
				return "";
			}
			pPath = pPath.substring(0, pos); // ParentPathを設定し直す

		}
		if (option && !pPath.endsWith("/")) {
			return pPath.trim() + "/";
		}
		return pPath.trim();
	}

	// 親パスを除いた部分を返す
	// String name = kyPkg.uFile.FileUtil_.getName(path);
	public static String getName(String pPath) {
		String wPath = normarizeIt(pPath);
		int p1 = wPath.lastIndexOf("/");
		if (p1 > 0) {
			p1++;
		} else {
			p1 = 0;
		}
		return wPath.substring(p1);
	}

	// ----------------------------------------------------------------
	// 拡張子を変更したパスを返す（拡張子が存在しなければそのまま拡張子をつける）
	// 使用例＞　
	// String path="c:/suzy/cream/cheeze/zappa.txt";
	// System.out.println("extConvert>"+kyPkg.uFile.FileUtil_.getExtConvPath(path,"xls"));
	// ----------------------------------------------------------------
	public static String changeExt(String path, String newExt) {
		return getPreExt(path) + "." + newExt;
	}

	public static String getAbsolutePath(String pPath) {
		return new File(pPath).getAbsolutePath();
	}

	/***************************************************************************
	 * パスを指定してストリームライターオブジェクトを取得する<br>
	 * 
	 * @param pFile
	 *            書き出すファイル
	 * @param strData
	 *            読み込み元となるString FileUtil.string2file("c:/dummy.txt","Hello");
	 **************************************************************************/
	public static OutputStreamWriter getWriterEx(String outPath) {
		return getWriter(outPath, "");
	}

	public static OutputStreamWriter getWriter(String outPath) {
		//		return getWriter(outPath, SHIFT_JIS);
		return getWriter(outPath, MS932);//20161222
	}

	public static OutputStreamWriter getWriter(String outPath, String encode) {
		return getStreamWriter(outPath, encode, true);
	}

	//-------------------------------------------------------------------------
	// String encode = "UTF-8"; utf-8が出力される
	// String encode = "MS932";文字化けする
	// String encode = "ISO-2022-JP"; 文字化けする
	// String encode = "Shift_JIS";
	//-------------------------------------------------------------------------
	public static OutputStreamWriter getStreamWriter(String outPath,
			String encode, boolean append) {
		OutputStreamWriter writer = null;
		FileUtil.makeParents(outPath); // 親パスが無ければ作る
		String message = FileUtil.oFileChk(outPath);
		if (!message.equals("")) {
			System.out.println("@getStreamWriter#error:" + message);
			return null;
		}
		try {
			if (encode == null || encode.trim().equals("")) {
				encode = (java.nio.charset.Charset.defaultCharset()).name();
			}
			FileOutputStream fo = new FileOutputStream(new File(outPath));
			// FileOutputStream fo = new FileOutputStream(new File(outPath),
			// append);
			writer = new OutputStreamWriter(fo, encode);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer;
	}

	//	private static void iterator2file(String outPath, Iterator iterator,
	//			String encode) {
	//		try {
	//			OutputStreamWriter writer = FileUtil.getWriter(outPath, encode);
	//			if (writer != null) {
	//				for (Iterator it = iterator; it.hasNext();) {
	//					String line = (String) it.next();
	//					writer.write(line, 0, line.length());
	//					writer.write(LF, 0, LF.length()); // 改行コード
	//				}
	//				writer.close();
	//			}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	// -------------------------------------------------------------------------------
	// tester
	// -------------------------------------------------------------------------------
	//	public static void test01() {
	//		String iPath = Transfer.F_MMTRANSFER
	//				+ "dataBase/data/qpr_monitor_out.txt";
	//		if (kyPkg.uFile.FileUtil.isExists(iPath) == false) {
	//			System.out.println(iPath + "が見つからないのでモニターファイルの処理を行いませんでした。");
	//		} else {
	//			System.out.println(iPath + "は存在します");
	//		}
	//	}

	public static void test03() {
		// 当該ディレクトリで最新のものを拾い上げる
		String dirPath = ResControl.D_DRV + "workspace/QPRweb/神戸生協/data/";
		// List<String> list =
		// kyPkg.uFile.ListArrayUtil.dir2ListWithDir(dirPath,"\\S*\\.xls");
		List<String> list = kyPkg.uFile.ListArrayUtil.dir2ListWithDir(dirPath,
				"\\.xls$");
		// *.txt
		if (list != null)
			System.out.println("answer:" + list.get(0));
	}

	public static void testMakedir() {

	}

	//20161118
	public static void testfilteredList() {
		String pDir = "C:/@QPR/home/123620000001/tran/";
		String pExt = ".txt";
		String[] ar = filteredList(pDir, pExt);
		for (String element : ar) {
			System.out.println(">>" + element);
		}
	}

	// -------------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------------
	public static void main(String[] args) {
		testfilteredList();
		//		testMakedir();
		//		test03();
	}
}

/*
 * ● save as の書き方・・・ String wPath = ""; JFileChooser fc = new JFileChooser(".");
 * if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { wPath =
 * fc.getSelectedFile().toString(); }else{ return; }
 * 
 * 
 * jBtXXX.addActionListener(new ActionListener(){ public void
 * actionPerformed(ActionEvent arg0) { jBtFch.setEnabled(false); JFileChooser fc
 * = new JFileChooser("./"); //
 * fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
 * fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //
 * fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); if
 * (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
 * jTfPath.setText(fc.getSelectedFile().toString()); } jBtFch.setEnabled(true);
 * } });
 */
