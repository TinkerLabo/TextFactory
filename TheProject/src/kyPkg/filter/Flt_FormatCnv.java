package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.HashMap;
import java.util.List;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.ListArrayUtil;

// 2016-06-02 yuasa   
public class Flt_FormatCnv extends Abs_BaseTask {
	private static final String DEFAULT = "default\t";
	private static final String O_LAYOUT = "oLayout\t";
	private static final String I_LAYOUT = "iLayout\t";
	private static final String P_DELIMITER = "pDelimiter\t"; //パラメータ中の区切り文字
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private static HashMap<String, Integer> inColMap;
	private static String[] iLayouts;
	private static String[] oLayout;
	private static String[] oDefault;
	private static String[] wRecs;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public Flt_FormatCnv(String oPath, String iPath, String pPath) {
		reader = new EzReader(iPath);
		writer = new EzWriter(oPath);
		init(pPath);

	}

	//#########################################################################
	// init
	//#########################################################################
	//ex"item,maker,category,JanName(kanji),kanaJanName(kana),kanaJanName(ryaku)"; 
	private static void init(String pPath) {
		String pDelimiter = ",";
		List<String> paramlist = ListArrayUtil.file2List(pPath);
		for (String element : paramlist) {
			if (element.startsWith("#")) {
			} else if (element.startsWith(P_DELIMITER)) {//パラメータが何で区切られているか
				element = element.substring(P_DELIMITER.length());
				pDelimiter = element;
			} else if (element.startsWith(I_LAYOUT)) {//入力フィールドの並び
				element = element.substring(I_LAYOUT.length());
				iLayouts = element.split(pDelimiter, -1);
			} else if (element.startsWith(O_LAYOUT)) {//出力フィールドの並び
				element = element.substring(O_LAYOUT.length());
				oLayout = element.split(pDelimiter, -1);
			} else if (element.startsWith(DEFAULT)) {//デフォルト値
				element = element.substring(DEFAULT.length());
				oDefault = element.split(pDelimiter, -1);
			}
		}
		//必須パラメータの指定の有無を確認する
		if (oLayout == null) {
			System.out.println("出力パラメータが指定されていない為処理を中断します");
			System.exit(999);
		}
		wRecs = new String[oLayout.length];
		parseHead(iLayouts);

	}

	// ------------------------------------------------------------------------
	// 実行
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long wCnt = 0;
		reader.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (wCnt == 0) {

			} else {

				//もし実行時にiLayoutが空なら一行目を使う・・・この時の区切り文字云々が問題だろう　パラメータ内で統一させればいいんじゃないか？？
				//#############################################################
				// loop
				//#############################################################
				for (int i = 0; i < oLayout.length; i++) {
					Integer col = inColMap.get(oLayout[i]);
					if (col != null && splited.length > col) {
						wRecs[i] = splited[col];
					} else {
						wRecs[i] = oDefault[i];
					}
				}
				//				String delimiter = "☆";
				//				System.out.println(">>" + join(wRecs, delimiter));
				writer.write(join(wRecs, delimiter));
			}

			wCnt++;
			splited = reader.readSplited();
		}
		reader.close();
		writer.close();
	}

	//入力レコードのフィールドの並びをマップに格納する
	private static void parseHead(String[] aHeads) {
		inColMap = new HashMap();
		for (int i = 0; i < aHeads.length; i++) {
			inColMap.put(aHeads[i], new Integer(i));
		}
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String wkDir = "C:/samples/生協テスト/";
		String inPath = wkDir + "SYOMSDT_1605.TXT";
		String metaPath = wkDir + "meta.txt";
		String formatedPath = wkDir + "formated.txt";
		//---------------------------------------------------------------------
		//　ソート　　(0カラム目を辞書順)
		//---------------------------------------------------------------------
		new kyPkg.Sorts.IncoreSort(inPath).execute();
		//---------------------------------------------------------------------
		//　フォーマット変換
		//---------------------------------------------------------------------
		new kyPkg.filter.Flt_FormatCnv(formatedPath, inPath, metaPath).execute();
	}
}
