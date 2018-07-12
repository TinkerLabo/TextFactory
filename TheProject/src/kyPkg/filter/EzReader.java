package kyPkg.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException; //import java.util.List;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import globals.ResControl;
import kyPkg.converter.ConverterList;
import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.converter.Inf_StrConverter;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

public class EzReader implements Inf_iClosure {
	//	private String charsetName = System.getProperty("file.encoding");
	private String charsetName = FileUtil.getDefaultEncoding();//20161222

	private String path;
	private String delimiter = null;
	private BufferedReader br;
	private String currentRec;
	private int stat;
	// converter etc
	private Inf_StringCnv stringCnv;
	private ConverterList preConverter = null;
	private Inf_ArrayCnv arrayConv = null;
	private Inf_BaseClojure clojure;

	public void setArrayConv(Inf_ArrayCnv arrayConv) {
		this.arrayConv = arrayConv;
	}

	public void setClojure(Inf_BaseClojure clojure) {
		this.clojure = clojure;
	}

	// 正規表現による結果statがこの数値より大きい場合のみ出力される
	// 正規表現による結果statは複数指定することができ、なおかつ加算されたものが最終的なstatとなります
	private RegChecker regChecker = null;
	private int checkLevel = 0;// デフォルトは0なのですべて出力対象となる
	private long readCount = 0;

	public long getReadCount() {
		return readCount;
	}

	public void addConverter(int index, Inf_StrConverter converter) {
		if (this.preConverter == null)
			this.preConverter = new ConverterList();
		preConverter.addConverter(index, converter);
	}

	private void setCharsetName(String charsetName) {
		charsetName = charsetName.trim();
		if (charsetName.equals("")) {
			//charsetName = System.getProperty("file.encoding");
			charsetName = FileUtil.getDefaultEncoding();//20161222
		}
		this.charsetName = charsetName;
	}

	public void setStringCnv(Inf_StringCnv stringCnv) {
		this.stringCnv = stringCnv;
	}

	public String getCurrentRec() {
		return currentRec;
	}

	public void setLevel(int level) {
		this.checkLevel = level;
	}

	// private boolean reject = false;// 条件にマッチしないstat０のデータを除去するかどうか
	// // trueなら、stat=０のデータを除去する（デフォルトはfalseなので除去されない）
	// public void setReject(boolean reject) {
	// this.reject = reject;
	// }
	// -------------------------------------------------------------------------
	// list→カラム、開始位置（0より始める）、長さ、ブール値、設定ステータス値、レジックスパターン
	// -------------------------------------------------------------------------
	// 当該カラムの、開始位置から長さ分（省略時は0よりそのセル全部）が
	// レジックスパターンに（ブール値がTrueなら）一致するなら設定ステータス値が加算される
	// -------------------------------------------------------------------------
	// 最初のセルが７で始まるなら 16
	// list.add("0, 0,1,True,16,^7.*");
	// 最初のセル（区切りなし固定長ならそのレコードの）54バイトめから1文字が3なら1
	// list.add("1,54,1,True,1,3");
	// -------------------------------------------------------------------------
	// FilterParams paramObj
	// public void setFilter(List<FilterParam> list,int level) {

	@Override
	public void setFilter(RegChecker filter, int level) {
		if (filter != null) {
			this.regChecker = filter;
			this.checkLevel = level;
		}
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	// リストに指定したレジックスパターンによる設定値の合計がlevelより大きい場合は出力される
	// それ以外は出力されない？！
	// ※readLineしか使わない場合意味を成さない！
	// public EasyReader(String path, List<FilterParam> list, int level) {
	// -------------------------------------------------------------------------
	public EzReader(String path) {
		this.path = path;
	}

	public EzReader(String path, String charsetName) {
		this(path);
		setCharsetName(charsetName);
	}

	public EzReader(String path, Inf_BaseClojure clojure) {
		this(path);
		setClojure(clojure);
	}

	public EzReader(String path, Inf_ArrayCnv converter) {
		this(path);
		setArrayConv(converter);
	}

	public EzReader(String path, Inf_StringCnv converter) {
		this(path);
		setStringCnv(converter);
	}

	public EzReader(String path, RegChecker filter, int checkLevel) {
		this(path);
		setFilter(filter, checkLevel);
	}

	@Override
	public void open() {
		readCount = 0;
		if (clojure != null)
			clojure.init();
		if (arrayConv != null)
			arrayConv.init();
		if (stringCnv != null)
			stringCnv.init();
		File wkFile = new File(path);
		if (!wkFile.isFile()) {
			System.out.println("#error @EasyReader not a File=>" + path);
			//			new Msgbox(null)
			//					.info("#20150623##error @EasyReader not a File=>" + path);
			return;
		}
		if (!wkFile.canRead()) {
			System.out.println("#error File can not read =>" + path);
			return;
		}
		File49ers f49_L = new File49ers(path, 20, charsetName, null);
		if (this.delimiter == null)
			this.delimiter = f49_L.getDelimiter();
		if (br != null)
			return;// 同じインスタンスを重複して登録する場合を考慮している
		try {
			if (!new File(path).exists()) {
				System.out.println("File is Not Existed:" + path);
			} else {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(path), charsetName));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public String readLine() {
		currentRec = null;
		if (br != null) {
			try {
				currentRec = br.readLine();
				readCount++;
				if (stringCnv != null)
					currentRec = stringCnv.convert(currentRec);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return currentRec;
	}

	@Override
	public int getStat() {
		getSplited();
		return stat;
	}

	@Override
	public String[] readSplited() {
		readLine();
		return getSplited();
	}

	@Override
	public String[] getSplited() {
		return getSplited(getCurrentRec());
	}

	private String[] getSplited(String rec) {

		stat = 999;
		if (rec == null)
			return null;

		// splitに引数を与えることによってサプレスを避けることができる2011/07/29
		String[] array = rec.split(delimiter, Integer.MAX_VALUE);

		if (array == null)
			return null;

		stat = 0;
		if (preConverter != null)
			array = preConverter.convert(array);

		if (array != null && regChecker != null) {
			stat = regChecker.checkIt(array);
			// 文字列パターンに適合したstatの合計が、指定した値以下なら空を返す
			if (stat < checkLevel)
				array = new String[] {};
		}
		if (array != null && clojure != null) {
			clojure.execute(array);
		}

		if (array != null && arrayConv != null) {
			array = arrayConv.convert(array, 0);
		}

		if (array != null) {
			return array.clone();
		} else {
			return null;
		}
	}

	public boolean isEOF() {
		if (currentRec == null)
			return true;
		return false;
	}

	@Override
	public boolean notEOF() {
		if (currentRec == null)
			return false;
		return true;
	}

	@Override
	public void close() {
		if (br == null)
			return;// 同じインスタンスを重複して登録する場合を考慮している
		try {
			br.close();
			br = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (stringCnv != null)
			stringCnv.fin();
		if (arrayConv != null)
			arrayConv.fin();
		if (clojure != null) {
			clojure.write();
		}
	}

	@Override
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public String getCurrent() {
		return currentRec;
	}

	public static void main(String[] argv) {
		test01();
	}

	public static void test01() {
		List charsetNames = FileUtil.getCharsetNames();
		String path;
		String dir = ResControl.D_DRV + "resources/templates/codingTest/zapp/";
		for (Iterator iterator1 = charsetNames.iterator(); iterator1
				.hasNext();) {
			String charsetName = (String) iterator1.next();
			if (!charsetName.equals("JISAutoDetect")) {
				path = dir + charsetName + ".txt";
				System.out.println(
						"------------------------------------------------------------------");
				System.out.println("read path:" + path);
				EzReader reader = new EzReader(path, charsetName);
				reader.open();
				String[] splited = reader.readSplited();
				while (splited != null) {
					if (splited.length > 0) {
						for (int i = 0; i < splited.length; i++) {
							System.out.print("ttt>" + splited[i]);
						}
						System.out.println("<ttt");
						splited = reader.readSplited();
					}
				}
				reader.close();

			}
		}
	}

}
