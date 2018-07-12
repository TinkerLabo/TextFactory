package kyPkg.filter;

import java.util.Iterator;
import java.util.List;

import globals.ResControlWeb;
import kyPkg.converter.CnvMap;
import kyPkg.etc.MapReducer;

public class Test_MapReducer {
	// #########################################################################
	public static void main(String[] argv) {
		test3();
	}

	// -------------------------------------------------------------------------
	// テストの結果は以下のとおりとなる
	// keyb,b,this is B,20,40,60,2
	// keyc,c,this is C,10,20,30,1
	// keya,a,this is A,30,60,90,3
	// -------------------------------------------------------------------------
	public static void testX() {
		String[] rec = { "key,a,         ,10,20,30",
				"key,a,this is A,10,20,30", "key,a,         ,10,20,30",
				"key,b,this is B,10,20,30", "key,b,         ,10,20,30",
				"key,c,this is C,10,20,30" };
		// MapReducer ins = new MapReducer(
		// new int[] { 0, 1 },
		// new int[] { 1, 2 },
		// new int[] { 3, 4, 5 }, ",");
		String keyColumns= "0 ,1 " ; 
		String mapColumns= " 1, 2 " ;
		String sumColumns= " 3, 4, 5 ";
		MapReducer mapRedObj = new MapReducer("",keyColumns,mapColumns,sumColumns ,",");
		mapRedObj.init();
		for (int i = 0; i < rec.length; i++) {
			mapRedObj.execute(rec[i]);
		}
		List list = mapRedObj.getList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			System.out.println("//　" + element);
		}
		mapRedObj.write();
	}

	public static void test1() {
		String srcFile = ResControlWeb.getD_Resources_Templates("temp01.txt");
		String outPath = ResControlWeb.getD_Resources_Templates("MapReducer.txt");
		String keyColumns = "2 ,3 "; // マッピングに使用する項目
		String sumColumns = " 4, 5, 6 "; // 足しあげる項目
		MapReducer closure = new MapReducer(outPath,keyColumns, sumColumns,"\t");
//		closure.setOutPath(outPath);
		new CommonClojure().incore(closure, srcFile, true);
	}

	public static void test2() {
		// 注意 インデックスは零から始めること！！
		// 出力順は保障されません（目茶目茶です）
		String srcFile = "Z:/s2/rx/Idxc/Enquete/属性・ライフスタイル編/2008/QTB1.txt";
		String outPath = ResControlWeb.getD_Resources_Templates("MapReducer.txt");
		String keyColumns="2"; 
		String mapColumns="4";
		String sumColumns="8,9";
		MapReducer closure = new MapReducer(outPath, keyColumns,mapColumns,sumColumns,",");
//		closure.setOutPath(outPath);
		closure.setSumFlag(false); // 数値は加算しない
		closure.setCountOption(false); // 種数は要らない
		new CommonClojure().incore(closure, srcFile, true);
		// cluster

		String element = closure.getStrElement("cluster", 0);
		int from = closure.getNumElement("cluster", 0);
		int to = closure.getNumElement("cluster", 1);
		System.out.println("cluster " + element + "from:" + from + " to:" + to);
	}

	public static void test3() {
		// 注意 インデックスは零から始めること！！
		// 出力順は保障されません（目茶目茶です）
		String aliasDir = "Z:/s2/rx/Idxc/Enquete/属性・ライフスタイル編/2007/";
		String pathQTB1 = aliasDir + "QTB3.txt";
		String pathQTB2 = aliasDir + "QTB3.txt";
		String[] fields = { "q1", "q2", "q47", "cluster" };

		// 以下AliasResの部分テスト・・・

		// ---------------------------------------------------------------------
		// QTB1 incore
		// ---------------------------------------------------------------------
		String outPath = ResControlWeb.getD_Resources_Templates("cnvQTB1.txt");
		String keyColumns="2"; 
		String mapColumns="4,7";
		String sumColumns="8,9";
		MapReducer elmQTB1 = new MapReducer(outPath,keyColumns,mapColumns,sumColumns,",");// key
//		elmQTB1.setOutPath(outPath);

		// strElem
		// ,numElm
		elmQTB1.setCondition(1, "ROOT"); // 1列目がROOTにマッチするもの意外は除外
		elmQTB1.setSumFlag(false); // 数値項目を加算しない
		elmQTB1.setCountOption(false); // 種数は要らない
		new CommonClojure().incore(elmQTB1, pathQTB1, true);
		// ---------------------------------------------------------------------
		// QTB2 incore
		// ---------------------------------------------------------------------
		String outPath2 = ResControlWeb.getD_Resources_Templates("cnvQTB2.txt");
		String keyColumns2="1"; 
		String mapColumns2="3,4";
		String sumColumns2=null;
		MapReducer elmQTB2 = new MapReducer(outPath2,keyColumns2,mapColumns2,sumColumns2,",");
//		elmQTB2.setOutPath(outPath2);

		elmQTB2.setSumFlag(false); // 数値は加算しない
		elmQTB2.setModFlag(true); // キーが同じ項目は追記する
		elmQTB2.setCountOption(false); // 種数は要らない
		new CommonClojure().incore(elmQTB2, pathQTB2, true);
		// ---------------------------------------------------------------------
		CnvMap[] cnvMap = new CnvMap[fields.length];
		int[] from = new int[fields.length];
		int[] to = new int[fields.length];
		String[] title = new String[fields.length];
		String[] type = new String[fields.length];
		// ---------------------------------------------------------------------
		// ＱＴＢよりfieldsの位置情報を調べる(当該パラメータを取得する為)
		// ---------------------------------------------------------------------
		for (int i = 0; i < fields.length; i++) {
			// QTB1
			from[i] = elmQTB1.getNumElement(fields[i], 0);
			to[i] = elmQTB1.getNumElement(fields[i], 1);
			title[i] = elmQTB1.getStrElement(fields[i], 0);
			type[i] = elmQTB1.getStrElement(fields[i], 1);
			System.out.println("field[" + i + "]:" + fields[i] + " type:"
					+ type[i] + " title:" + title[i]);
			System.out.println("from:" + from[i] + " to:" + to[i]);
			// QTB2
			String valArray = elmQTB2.getStrElement(fields[i], 0);
			String tagArray = elmQTB2.getStrElement(fields[i], 1);
			// System.out.println("valArray:"+valArray);
			// System.out.println("tagArray:"+tagArray);
			cnvMap[i] = new CnvMap(tagArray, valArray, null, "\t");
			String[] names = cnvMap[i].getTagArray();
			for (int j = 0; j < names.length; j++) {
				System.out.println("names[" + j + "]:" + names[j]);
			}
		}
	}
}
