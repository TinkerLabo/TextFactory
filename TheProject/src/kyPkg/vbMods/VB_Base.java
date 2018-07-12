package kyPkg.vbMods;

import java.io.File; //import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
import java.util.List;
import kyPkg.sql.JDBC;
import kyPkg.uFile.FileUtil;

public class VB_Base {
	public static final String DQ = "\"";
	public static final String vbTab = "\t";
	public static final String vbCrLf = "\n";
	public static final String LF = System.getProperty("line.separator");

	// public static void main(String[] argv) {
	// testCnvChr();
	//
	// }

	public void xComand(String command) {

	}

	public void Multi2Single(List xMsq, String pPath, Object outlet) {

	}

	public static void RevertVector(List gHed2) {

	}

	// 仮作成・・あとでちゃんと作る
	public static int ODBC_SQL2VECTOR(String xCnn, String xSql,
			List<List> xVector) {
		return -999;
	}

	// CrossMod.ODBC_SQL2TEX(xCnn, xSql, gPath_Tmp + "\\AX.TXT", false,
	// false,false, ",");
	public static int ODBC_SQL2TEX(String xCnn, String xSql, String outPath,
			boolean pEnclose, boolean trimFlag, boolean px1, String delimiter) {
		JDBC ins = new JDBC(xCnn);
		//System.out.println("delimiter::::::"+delimiter);
		return ins.query2File(outPath, xSql, ",");
	}

	// target:
	// regex:\\$
	// replacement:

	public static void testCnvChr() {
		String target = "SELECT BASETABLE.CH_ID as ID,SUBSTRING($,338,1) AS q14 FROM TBL_NQMED2009 AS BASETABLE";
		System.out.println("=>" + CnvChr(target, "$", "CH_DAT"));
		System.out.println("$  testCnvChr():"
				+ CnvChr("Select * from $ ", "$", "TableName"));
		System.out.println("^  testCnvChr():"
				+ CnvChr("Select * from ^ ", "^", "TableName"));
		System.out.println("?  testCnvChr():"
				+ CnvChr("Select * from ? ", "?", "TableName"));
		System.out.println(".  testCnvChr():"
				+ CnvChr("Select * from . ", ".", "TableName"));
	}

	public static final int vbOKOnly = 1; // 適当（使用箇所は要修正）
	public static final int vbCritical = 4; // 適当（使用箇所は要修正）

	// StripChar(xVal, Chr(39)

	static String StripChar(String target, String regex) {
		return target.replaceAll(regex, "");
	}

	static String CnvChr(String target, String pattern, String replacement) {
		// もし置換元が特殊文字だったら
		// . ^ $ [ ] * + ? | ( )
		String regex = pattern;
		regex = regex.replaceAll("\\$", "\\\\\\$");
		regex = regex.replaceAll("\\^", "\\\\\\^");
		regex = regex.replaceAll("\\?", "\\\\\\?");
		regex = regex.replaceAll("\\.", "\\\\\\.");
		// System.out.println("target:" + target);
		// System.out.println("regex:" + regex);
		// System.out.println("replacement:" + replacement);
		return target.replaceAll(regex, replacement);
	}

	static int UBound(int[] array) {
		return array.length;
	}

	static boolean IsNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static int UBound(String[] array) {
		return array.length - 1;
	}

	// public int InStr_R(String str,String src){
	// return str.indexOf(src);
	// }
	//	
	// public String Mid(String str,int len){
	// return str.substring(len);
	// }
	// public String Left(String str,int len){
	// //int length = str.length();
	// //if (length > len)
	// return str.substring(0,len);
	// }
	// public String Right(String str,int len){
	// int length = str.length();
	// length = length -len;
	// return str.substring(length);
	// }

	static String UCase(String val) {
		return val.toUpperCase();
	}

	static String GetShortName(String inPath) {
		return inPath; // TODO 要修正
	}

	static String Trim(String val) {
		return val.trim();
	}

	static void MsgBox(String message) {
		new kyPkg.util.Msgbox(null).info(message);
	}

	static int MsgBox(String message, int style, String title) {
		MsgBox(message);
		return -1;
	}

	static boolean FileCopy(String pPath_I, String pPath_O) {
		return FileUtil.fileCopy(pPath_O, pPath_I);
	}

	static boolean Kill(String parm) {
		return new File(parm).delete();
	}

	static String MakeDir(String dir, boolean option) {
		String val = kyPkg.uFile.FileUtil.makedir(dir);
		if (option) {
			if (val.equals("")) {
				// XXX　ディレクトリが作れなかったらその旨ダイアログ表示する
			}
		}
		return val;
	}

	static String MakeDir(String dir) {
		return kyPkg.uFile.FileUtil.makedir(dir);
	}

	static int InStr_R(String pParm, String val) {
		return pParm.lastIndexOf(val) + 1;
	}

	public static void testLeft() {
		System.out.println("0=>" + Left("12345", 0) + "<=");
		System.out.println("1=>" + Left("12345", 1) + "<=");
		System.out.println("2=>" + Left("12345", 2) + "<=");
		System.out.println("3=>" + Left("12345", 3) + "<=");
		System.out.println("4=>" + Left("12345", 4) + "<=");
		System.out.println("5=>" + Left("12345", 5) + "<=");
		System.out.println("6=>" + Left("12345", 6) + "<=");
	}

	public static void testRight() {
		System.out.println("0=>" + Right("12345", 0) + "<=");
		System.out.println("1=>" + Right("12345", 1) + "<=");
		System.out.println("2=>" + Right("12345", 2) + "<=");
		System.out.println("3=>" + Right("12345", 3) + "<=");
		System.out.println("4=>" + Right("12345", 4) + "<=");
		System.out.println("5=>" + Right("12345", 5) + "<=");
		System.out.println("6=>" + Right("12345", 6) + "<=");
	}

	public static void testMid() {
		System.out.println("0=>" + Mid("12345", 0) + "<=");
		System.out.println("1=>" + Mid("12345", 1) + "<=");
		System.out.println("2=>" + Mid("12345", 2) + "<=");
		System.out.println("3=>" + Mid("12345", 3) + "<=");
		System.out.println("4=>" + Mid("12345", 4) + "<=");
		System.out.println("5=>" + Mid("12345", 5) + "<=");
		System.out.println("6=>" + Mid("12345", 6) + "<=");
	}

	// 要確認
	static String Right(String pParm, int length) {
		int endIndex = pParm.length();
		int startIndex = endIndex - length;
		if (startIndex < 0)
			startIndex = 0;
		if (endIndex > pParm.length())
			endIndex = pParm.length();
		return pParm.substring(startIndex, endIndex);
	}

	static String Left(String pParm, int endIndex) {
		if (endIndex > pParm.length())
			endIndex = pParm.length();
		return pParm.substring(0, endIndex);
	}

	static String Mid(String pParm, int startIndex) {
		startIndex--;
		if (startIndex > pParm.length())
			startIndex = pParm.length();
		return pParm.substring(startIndex);
	}

	static boolean isDir(String path) {
		File ins = new File(path);
		return ins.isDirectory();
	}

	static boolean isExists(String path) {
		File ins = new File(path);
		return ins.isFile();
	}

	static void kill(String path) {
		File ins = new File(path);
		ins.delete();
	}

	static void rename(String after, String before) {
		File ins = new File(before);
		ins.renameTo(new File(after));
	}

	public class OPENFILENAME extends Object {
		public String lpstrFile;

		OPENFILENAME() {
			super();
		}
	}

	public static final String vbNullChar = null;

	// XXX すべてテストを作成しておく（必須）
	//
	public String Chr(int x) {
		char c = (char) x;
		String wk = "" + c;
		return wk;
	}

	public String CurDir() {
		return "Current Directory??";
	}

	public static int Len(String str) {
		return str.length();
	}

	public String[] Split(String var, String delim) {
		return var.split(delim);
	}

	public boolean IsMissing(Object obj) {
		if (obj == null) {
			return true;
		} else {
			return false;
		}
	}

	public String Dir(String path) {
		File file = new File(path);
		return file.getAbsolutePath();
	}

	public static int InStr(String str, String src) {
		return str.indexOf(src);
	}

	public void SetHourglass(Boolean flag) {

	}

	public static void main(String[] argv) {
		testCnvChr();
	}

}
