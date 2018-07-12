package kyPkg.filter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uDateTime.DateCalc;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;

// 2016-10-24 yuasa
public class MyDataGen extends Abs_BaseTask {
	private static final String K00 = "K00";
	private static final String MYDATA = "C:/MYDATA/";
	private String dim1 = "抽出条件";
	private String dim2 = "タイトル【日付】";
	private String title = "タイトル";
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private int occ = 0;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public MyDataGen(String inPath) {
		inPath = inPath.trim();
		if (!inPath.equals("")) {
			reader = new EzReader(inPath);
			title = FileUtil.getFirstName(inPath);
			String today = DateCalc.getTodayKJ();
			dim2 = title + "【" + today + "】";
		} else {
			System.out.println("#error @MyDataGen ファイル名が指定されていない");
		}
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		if (reader == null) {
			return;
		}
		List<String> groupList = null;
		HashMap<String, boolean[]> idMap = new HashMap();
		super.setMessage("<<START>>");
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (groupList == null) {
				groupList = incore(splited);
			} else {
				mapIt(idMap, occ, splited);
			}
			splited = reader.readSplited();
		}
		reader.close();
		String outDir = MYDATA + dim1 + "/" + dim2 + "/";
		writeAsm(outDir + "ASM.txt", idMap);
		ListArrayUtil.list2File(outDir + "QTB1.txt", getQTB1(groupList));
		ListArrayUtil.list2File(outDir + "Schema.ini", getSchema(occ));
		ListArrayUtil.list2File(outDir + "alias.txt", getAlias());
	}

	//-------------------------------------------------------------------------
	//データ部出力
	//-------------------------------------------------------------------------
	private void writeAsm(String outPath, HashMap<String, boolean[]> idMap) {
		long wCnt = 0;
		writer = new EzWriter(outPath);
		if (idMap != null) {
			writer.open();
			List<String> idList = new ArrayList(idMap.keySet());
			for (String id : idList) {
				String rec = id + "\t\"" + bools2Str(idMap.get(id)) + "\"";
				//				System.out.println(rec);
				writer.write(rec);
				wCnt++;
			}
			writer.close();
		}
	}

	//-------------------------------------------------------------------------
	//パラメータ部出力
	//	0000,ROOT,XQQQ,,アンタイトルド,6,6,MULTI,1,6,K00
	//	0001,XQQQ,XQQQ_1,1,ブランドＡ購入,1,1,MULTI,1,1,K00
	//	0002,XQQQ,XQQQ_2,1,ブランドＢ購入,1,1,MULTI,2,1,K00
	//	0003,XQQQ,XQQQ_3,1,ブランドＣ購入,1,1,MULTI,3,1,K00
	//	0004,XQQQ,XQQQ_4,1,ブランドＤ購入,1,1,MULTI,4,1,K00
	//	0005,XQQQ,XQQQ_5,1,ブランドＥ購入,1,1,MULTI,5,1,K00
	//	0006,XQQQ,XQQQ_6,1,ブランドＦ購入,1,1,MULTI,6,1,K00
	//-------------------------------------------------------------------------
	private List<String> getQTB1(List<String> groupList) {
		DecimalFormat df4 = new DecimalFormat("0000");
		DecimalFormat df3 = new DecimalFormat("000");
		if (groupList == null)
			return null;
		int seq = 0;

		List<String> listQTB1 = new ArrayList();
		listQTB1.add(df4.format(seq) + ",ROOT,XQQQ,," + title + "," + occ + ","
				+ occ + ",MULTI,1," + occ + "," + K00);
		seq++;
		for (String groupName : groupList) {
			listQTB1.add(df4.format(seq) + ",XQQQ,XQQQ_" + df3.format(seq)
					+ ",1," + groupName + ",1,1,MULTI," + seq + ",1," + K00);
			seq++;
		}
		return listQTB1;
	}

	//-------------------------------------------------------------------------
	//データベースコネクト文等出力
	//-------------------------------------------------------------------------
	private List<String> getAlias() {
		List<String> alias = new ArrayList();
		alias.add(
				"connect\tDRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir=$;DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;");
		alias.add("table\tASM#TXT");
		alias.add("field\t");
		alias.add("key\tID");
		alias.add("Cond\t");
		return alias;
	}

	//-------------------------------------------------------------------------
	//ISAM定義出力
	//-------------------------------------------------------------------------
	private List<String> getSchema(int width) {
		List<String> schema = new ArrayList();
		schema.add("[QTB1.TXT] ");
		schema.add("ColNameHeader=FALSE ");
		schema.add("Format=CSVDelimited ");
		schema.add("MaxScanRows=0 ");
		schema.add("CharacterSet=OEM ");
		schema.add("Col1=\"Srt\" Char Width 60 ");
		schema.add("Col2=\"Mot\" Char Width 60 ");
		schema.add("Col3=\"Key\" Char Width 60 ");
		schema.add("Col4=\"Val\" Char Width 60 ");
		schema.add("Col5=\"Nam\" Char Width 60 ");
		schema.add("Col6=\"Max\" Integer ");
		schema.add("Col7=\"Occ\" Integer ");
		schema.add("Col8=\"Typ\" Char Width 10 ");
		schema.add("Col9=\"Col\" Integer ");
		schema.add("Col10=\"Len\" Integer ");
		schema.add("Col11=\"Opt\" Char Width 128 ");
		schema.add("[ASM.TXT] ");
		schema.add("ColNameHeader=FALSE ");
		schema.add("Format=TABDelimited ");
		schema.add("MaxScanRows=0 ");
		schema.add("CharacterSet=OEM ");
		schema.add("Col1=\"ID\" Char Width 9 ");
		schema.add("Col2=\"" + K00 + "\" Char Width " + width + " ");

		return schema;
	}

	//-------------------------------------------------------------------------
	//ブール配列を文字列に変換
	//-------------------------------------------------------------------------
	private String bools2Str(boolean[] array) {
		StringBuffer buf = new StringBuffer();
		for (boolean b : array) {
			if (b) {
				buf.append("1");
			} else {
				buf.append(" ");
			}
		}
		return buf.toString();
	}

	//-------------------------------------------------------------------------
	//データをマッピングする
	//-------------------------------------------------------------------------
	void mapIt(HashMap<String, boolean[]> idMap, int occ, String[] splited) {
		for (int seq = 0; seq < splited.length; seq++) {
			String id = splited[seq];
			if (id != null && !id.trim().equals("")) {
				boolean[] array = idMap.get(id);
				if (array == null)
					array = new boolean[occ];
				array[seq] = true;
				idMap.put(id, array);
			}
		}
	}

	// -------------------------------------------------------------------------
	//1行目はグループを表すことにする
	// -------------------------------------------------------------------------
	private List<String> incore(String[] splited) {
		List<String> groupList = new ArrayList();
		for (String element : splited) {
			if (element != null && !element.trim().equals(""))
				groupList.add(element);
		}
		occ = groupList.size();
		return groupList;
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String inPath = "C:/samples/MyDataGen/アンタイトルド.txt";
		new MyDataGen(inPath).execute();
	}

}
