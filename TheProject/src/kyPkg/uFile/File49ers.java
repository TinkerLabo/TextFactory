package kyPkg.uFile;

import java.io.*;
import java.util.*;

import kyPkg.sql.TContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import globals.ResControlWeb;

import static kyPkg.uFile.FileUtil.*;

//XXX schemaを返せないか？？2012-05-02　Happy Birthday Anthony!!
/*******************************************************************************
 * 《 File49ers 》 2007-05-11 LastUpdate 2008-06-04 Yuasa<BR>
 * 注意！！タブがあると優先されます 他は、区切り文字の多い順となります
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 ******************************************************************************/

// BOM（バイト順マーク）の使用について
// UTF-8で符号されたテキストデータはエンディアンに関わらず同じ内容になるので、バイト順マーク (BOM) は必要ない。
// しかし、テキストデータがUTF-8で符号化されていることの標識として、
// データの先頭にEF BB BF（16進。UCSでのバイト順マークU+FEFFのUTF-8での表現）を付加することがある。
// なお、このシーケンスがある方をUTF-8、ない方をUTF-8Nと呼ぶこともあるが、
// このような呼び分けは日本以外ではほとんど知られておらず、また公的規格などによる裏付けもない。
// このため、UTF-8という呼び名を使っていれば情報交換の相手が文書先頭にこのシーケンスがあると見なすと期待すべきではないし、
// いっぽう、UTF-8Nという呼び名は情報交換の際に用いるべきではない。

// LGPLの juniversalchardetを使用するか？？その場合ライブラリの設置方法を調べてみる
// http://java.akjava.com/library/juniversalchardet
// kyPkg.uFile.File49ers
public class File49ers {
	public static final String APPENDA_CLASS = "kyPkg.sql";
	public static Log log = LogFactory.getLog(TContainer.APPENDA_CLASS);
	static final boolean DEBUG = false;
	//	private String encoding = System.getProperty("file.encoding");
	private String encoding = FileUtil.getDefaultEncoding();//20161222
	private String delimiter = "";
	private int maxColCount = 0;
	private boolean exists = true;

	public boolean isExists() {
		return exists;
	}

	private boolean file = true;

	public boolean isFile() {
		return file;
	}

	private boolean canRead = true;

	public boolean isCanRead() {
		return canRead;
	}

	public String getEncoding() {
		return encoding;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public int getMaxColCount() {
		return maxColCount;
	}

	// 区切り文字はなにか？優先順位をつけるのがいいだろう スペースが多くなりそうなきがするが
	// 何からむ存在しそうか？（探索レベルで最大のものを返す）
	// エンコーディングはなにか判定できるといいなー
	class LittleObj {
		private String delim = "";
		private int mxCol = 0;

		public LittleObj(String delim) {
			this.delim = delim;
		}

		public String getDelim() {
			return delim;
		}

		public void checkIt(String wRec) {
			String[] array = wRec.split(delim, -1);
			// XXX すべて区切り文字のデータではsplitが上手くいかない　１件になってしまうバグあり
			// System.out.println("checkIt=============> array.length:"+array.length);
			// 区切り文字が存在しなくても分割されるので必ず１以上となる
			if (this.mxCol < array.length)
				this.mxCol = array.length;
		}

		public int getMaxColm() {
			return mxCol;
		}
	}

	// 使用例
	// -------------------------------------------------------------------------
	// File49ers f49 = new File49ers(pnlFile_J.getPath(),20);
	// System.out.println("getDelim:"+f49.getDelim());
	// System.out.println("getMaxColm:"+f49.getMaxColm());
	// -------------------------------------------------------------------------
	// 1 行のテキストを読み込みます。1 行の終端は、改行 (「\n」) か、復帰 (「\r」)、または復行とそれに続く改行のどれかで認識されます。
	public File49ers(String path) {
		this(path, 20, "", null);
	}

	public File49ers(String path, int limit, String pCharsetName,
			String defDelimiter) {
		int priority1 = -1;
		int priority2 = -1;
		File wFile = new File(path);
		if (!wFile.exists()) {
			exists = false;
			System.out.println("#error @File49ers  Not exists:" + path);
			log.info("#error @File49ers  Not exists:" + path);
			return;
		}
		if (!wFile.isFile()) {
			file = false;
			System.out.println("#error @File49ers  Not File:" + path);
			log.info("#error @File49ers  Not File:" + path);
			return;
		}
		if (!wFile.canRead()) {
			canRead = false;
			System.out.println("#error @File49ers  Can't Read:" + path);
			return;
		}

		//		if (!wFile.isHidden()) {
		//			canRead = false;
		//			System.out.println("#error @File49ers  Can't Read:" + path);
		//			return;
		//		}
		encoding = pCharsetName.trim();

		// kyPkg.tools.FileUtil_.isLocked(path, "<500 ok>");

		if (encoding.equals("")) {
			// JISAutoDetectにすると、バイト配列がEUCかJISかShiftJISかを自動判別してくれる。
			// charsetName = "JISAutoDetect";
			// charsetName = System.getProperty("file.encoding");//
			// windows=>MS932
			encoding = EncodeDetector.getEncode(path);

			if (DEBUG)
				System.out.println("charsetName:" + encoding);
			if (encoding.equals("WINDOWS-1252")) {
				//				encoding = SHIFT_JIS;
				encoding = defaultEncoding2;//20161222
				// TODO 購買データが化けてしまうのでこうしたが・・・本当にこれでいいか検討する
			}

		}
		// kyPkg.tools.FileUtil_.isLocked(path, "<501 ng>");

		int wCnt = 0;
		String wRec = "";
		List list = new ArrayList();
		if (defDelimiter == null) {
			list.add(new LittleObj(","));
			list.add(new LittleObj("\t"));
			list.add(new LittleObj(":"));
			list.add(new LittleObj(";"));
			// list.add( new LittleObj(">"));
			// list.add( new LittleObj("@"));
			//			list.add(new LittleObj(" "));//20161028
			list.add(new LittleObj("\n"));//20161028
		} else {
			list.add(new LittleObj(defDelimiter));
		}
		// kyPkg.tools.FileUtil_.isLocked(path, "<502>");

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), encoding));
			while (br.ready() && wCnt < limit) {
				wRec = br.readLine();
				// System.out.println("wRec=>" + wRec);
				// System.out.println("wRec.indexOf =>" + wRec.indexOf("?"));
				if (wRec != null) {
					wCnt++;
					for (int i = 0; i < list.size(); i++) {
						LittleObj lObj = (LittleObj) list.get(i);
						lObj.checkIt(wRec);
						if (lObj.getDelim().equals("\t")
								&& lObj.getMaxColm() > 1) {
							priority1 = i; // タブが存在する場合他より優先させる・・・
						}
						if (lObj.getDelim().equals(",")
								&& lObj.getMaxColm() > 1) {
							priority2 = i; // その次にカンマを優先させる・・・
						}
					}
				}
			}
			br.close();
		} catch (java.io.FileNotFoundException e) {
			canRead = false;
			System.out.println("#error @File49ers  Can't Read:" + path + " "
					+ e.toString());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		//		if (!wFile.canRead()) {
		//		canRead = false;
		//		System.out.println("#error @File49ers  Can't Read:" + path);
		//		return;
		//		}

		// kyPkg.tools.FileUtil_.isLocked(path, "<503>");
		// System.out.println("--------------------------------------------------");
		// for (int i = 0; i < list.size(); i++) {
		// LittleObj wkObj = (LittleObj) list.get(i);
		// delim = wkObj.getDelim();
		// int maxColm = wkObj.getMaxColm();
		// System.out.println(">> delim=>" + delim + "<= getMaxColm:"+ maxColm);
		// }

		LittleObj maxObj = null;
		if (priority1 >= 0) {
			maxObj = (LittleObj) list.get(priority1);
		} else {
			if (priority2 >= 0) {
				maxObj = (LittleObj) list.get(priority2);
			} else {
				maxObj = (LittleObj) Collections.max(list,
						new lObjComparator());
			}
		}
		delimiter = maxObj.getDelim();
		maxColCount = maxObj.getMaxColm();
		// System.out.println("pPath:" + path);
		// System.out.println("delim:" + delim);
		// System.out.println("@File49 maxColm:" + maxColCount);
	}

	// JISAutoDetectを利用してエンコードを判定
	public static String getEncodeType(String rStr) {
		ArrayList<String> encList = new ArrayList();
		encList.add(UTF_8);
		encList.add(UTF_16);
		encList.add(MS932);
		encList.add(EUC_JP);
		encList.add(SJIS);
		encList.add(ISO2022JP);
		encList.add(JIS0201);
		encList.add(JIS0208);
		encList.add(JIS0212);
		encList.add(CP930);
		encList.add(CP939);
		encList.add(CP942);
		encList.add(CP943);
		encList.add(CP33722);
		String autoEnc = "";
		try {
			autoEnc = new String(rStr.getBytes("iso-8859-1"), "JISAutoDetect");
			for (String encode : encList) {
				String specific = new String(rStr.getBytes("iso-8859-1"),
						encode);
				if (specific.equals(autoEnc))
					return encode;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private class lObjComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			LittleObj obj1 = (LittleObj) o1;
			LittleObj obj2 = (LittleObj) o2;
			return obj1.getMaxColm() - obj2.getMaxColm();
		}
	}

	// 使用例、
	public static void test01() {
		// 2つのインスタンスが干渉しないかどうか確認
		String path_L = ResControlWeb
				.getD_Resources_Templates("cluster2007.txt");
		String path_R = ResControlWeb
				.getD_Resources_Templates("cluster2008.txt");

		File49ers f49_L = new File49ers(path_L);
		String delim_L = f49_L.getDelimiter();
		int max_L = f49_L.getMaxColCount();
		System.out.println("delim:" + delim_L);
		System.out.println("max:" + max_L);

		File49ers f49_R = new File49ers(path_R);
		String delim_R = f49_R.getDelimiter();
		int max_R = f49_R.getMaxColCount();
		System.out.println("delim:" + delim_R);
		System.out.println("max:" + max_R);
	}

	public static void test02() {
		String path = ResControlWeb.getD_Resources_Templates("cluster2007.txt");
		File49ers insF49 = new File49ers(path);
		// String encoding = insF49.getEncoding();
		String delimiter = insF49.getDelimiter();
		int maxCol = insF49.getMaxColCount();
		System.out.println("delim:" + delimiter);
		System.out.println("max:" + maxCol);
	}

	public static void test03() {
		String path = "C:/encode/ShiftJisCRLF.txt";
		File49ers insF49 = new kyPkg.uFile.File49ers(path);
		System.out.println("delimiter:" + insF49.getDelimiter());
		System.out.println("maxColumn:" + insF49.getMaxColCount());
	}

	public static void test00() {
		checkCRLF("C:/encode/ShiftJisCRLF.txt");
		checkCRLF("C:/encode/ShiftJisCR.txt");
		checkCRLF("C:/encode/ShiftJisLF.txt");
	}

	public static void testcheckBOM() {
		checkBOM("C:/encode/ShiftJisLF.txt");
		checkBOM("C:/encode/UTF8CR.txt");
		checkBOM("C:/encode/UTF8NCR.txt");
		checkBOM("C:/encode/UTF8CRLF.txt");
		checkBOM("C:/encode/UTF8NCRLF.txt");
		checkBOM("C:/encode/UTF8LF.txt");
		checkBOM("C:/encode/UTF8NLF.txt");
	}

	// BOMで始まっているかどうか調べる EF(239) BB(187) BF(191)　CSでのバイト順マークU+FEFFのUTF-8での表現
	public static boolean checkBOM(String path) {
		System.out.println("checkBOM path=>" + path);
		final int EF = 239;
		final int BB = 187;
		final int BF = 191;
		List<Integer> list = new ArrayList();
		try {
			FileInputStream in = new FileInputStream(path);
			int iVal;
			while (((iVal = in.read()) != -1) && list.size() < 3) {
				list.add(iVal);
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		if (list.get(0) == EF && list.get(1) == BB && list.get(2) == BF) {
			System.out.println("BOM exist");
			return true;
		} else {
			return false;
		}
	}

	// 改行コードとＯＳの関係
	// unix LF \n 0x0A 8進で 012 10進で10
	// Mac CR \r 0x0D 8進で 015 10進で13
	// Windows CR+LF \r\n
	// XXX 特に改行の判定をしなくてもラインリーダが判定してくれるようだ・・・では何故macで上手くいかなかったのか？？
	public static void checkCRLF(String path) {
		System.out.println("path=>" + path);
		final int CR = 13;
		final int LF = 10;
		int xLF = -1;
		int xCR = -1;
		int xCRLF = -1;
		int pre = -1;
		try {
			FileInputStream in = new FileInputStream(path);
			int iVal;
			while ((iVal = in.read()) != -1) {
				switch (iVal) {
				case CR:// CR
					xCR++;
					break;
				case LF: // LF
					if (pre == CR) {
						xCR--;
						xCRLF++;
					} else {
						xLF++;
					}
					break;
				default:
					break;
				}
				// System.out.print( "=>"+iVal+"<=");
				// System.out.print(Integer.toHexString(iVal) + " ");
				pre = iVal;
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("xLF  :" + xLF);
		System.out.println("xCR  :" + xCR);
		System.out.println("xCRLF:" + xCRLF);
	}

	public static void main(String[] argv) {
		// test01();
		test00();
		// test03();
	}
	// XXX　改行文字が何か判定したい
	// XXX　文字エンコードが何か判定したい
	// XXX　BOMがある場合判定する？？

	// http://code.google.com/p/juniversalchardet/

}
