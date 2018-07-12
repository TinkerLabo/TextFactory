package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// ----------------------------------------------------------------------------
//　ソートパラメータのカラム指定は0よりはじめる（1ではない！！）
// ----------------------------------------------------------------------------
public class SortUtil {
	public static String[] arraySkip(String[] data, int skip) {
		String[] outData = new String[data.length - skip];
		int j = 0;
		for (int i = skip; i < data.length; i++) {
			outData[j++] = data[i];
		}
		return outData;
	}

	/**************************************************************************
	 * array2CnvMap			横ソート用マップ生成（非購入列退避＆先頭へ挿入処理版）	
	 <br>※ （最後の要素が非購入であることが解っていて、この要素の表示位置を先頭に移動したい為にarray2CnvMapを改造してある）
	 * @param sortParam		ソートパラメータ（位置、型、A:昇順 D:降順）　"0,String,A";	 
	 * @param data			ソート対象とする値の配列	{40641: ビール,40681: リキュール類,40691: 発泡酒,D0400: ビール,D0900: 発泡酒,D0950: 新ジャンル,D9999: ノンアルコールビール,非購入};	 
		<hr>
		<br>出力イメージ
		<br>ソート後の位置と、ソート前の位置との対応を表すマップを作る　（※注意ソート対象とする値はマップに含めない）
		<br>		ソート後の位置:0 ソート前の位置:8 ソート対象とする値:非購入  
		<br>		ソート後の位置:0 ソート前の位置:0 ソート対象とする値:40641: ビール
		<br>		ソート後の位置:1 ソート前の位置:1 ソート対象とする値:40681: リキュール類
		<br>		ソート後の位置:2 ソート前の位置:2 ソート対象とする値:40691: 発泡酒
		<br>		ソート後の位置:3 ソート前の位置:3 ソート対象とする値:D0400: ビール
		<br>		ソート後の位置:4 ソート前の位置:4 ソート対象とする値:D0900: 発泡酒
		<br>		ソート後の位置:5 ソート前の位置:5 ソート対象とする値:D0950: 新ジャンル
		<br>		ソート後の位置:6 ソート前の位置:6 ソート対象とする値:D9999: ノンアルコールビール
		<hr>
	 **************************************************************************/
	public static HashMap<Integer, Integer> array2CnvMap_plus(String sortParam,
			String[] data, boolean debug) {
		//---------------------------------------------------------------------
		//｛ソート対象とする値、元の位置｝を要素にもつリストを作ってソートを行う
		//---------------------------------------------------------------------
		List<String[]> arrayList = new ArrayList();
		for (int i = 0; i < data.length - 1; i++) {//非購入（一番最後の値）をソート対象から外す
			arrayList.add(new String[] { data[i], String.valueOf(i) });
		}
		List<String[]> result = sortArrayList(sortParam, arrayList);
		HashMap<Integer, Integer> map = new HashMap();
		map.put(0, data.length - 1);//非購入を先頭に挿入
		//		System.out.println(
		//				"ソート後の位置:" + 0 + " ソート前の位置:" + data.length + " ソート対象とする値:" + data[data.length-1]);
		for (int i = 0; i < result.size(); i++) {
			String[] array = result.get(i);
			map.put(i + 1, Integer.valueOf(array[1]));//非購入を先頭に挿入したぶんずらす
			//			System.out.println(
			//					"ソート後の位置:" + i + " ソート前の位置:" + array[1] + " ソート対象とする値:" + array[0]);
		}
		return map;
	}

	/**************************************************************************
	 * array2CnvMap			横ソート用マップ生成	
	 * @param sortParam		ソートパラメータ（位置、型、A:昇順 D:降順）　"0,String,A";	 
	 * @param data			ソート対象とする値の配列	{40641: ビール,40681: リキュール類,40691: 発泡酒,D0400: ビール,D0900: 発泡酒,D0950: 新ジャンル,D9999: ノンアルコールビール,非購入};	 
	 **************************************************************************/
	public static HashMap<Integer, Integer> array2CnvMap(String sortParam,
			String[] data) {
		//---------------------------------------------------------------------
		//｛ソート対象とする値、元の位置｝を要素にもつリストを作ってソートを行う
		//---------------------------------------------------------------------
		List<String[]> arrayList = new ArrayList();
		for (int i = 0; i < data.length; i++) {
			arrayList.add(new String[] { data[i], String.valueOf(i) });
		}
		List<String[]> result = sortArrayList(sortParam, arrayList);
		//---------------------------------------------------------------------
		//ソート後の位置と、ソート前の位置との対応を表すマップを作る　（ソート対象とする値はマップに含めない）
		//		ソート後の位置:0 ソート前の位置:0 ソート対象とする値:40641: ビール
		//		ソート後の位置:1 ソート前の位置:1 ソート対象とする値:40681: リキュール類
		//		ソート後の位置:2 ソート前の位置:2 ソート対象とする値:40691: 発泡酒
		//		ソート後の位置:3 ソート前の位置:3 ソート対象とする値:D0400: ビール
		//		ソート後の位置:4 ソート前の位置:4 ソート対象とする値:D0900: 発泡酒
		//		ソート後の位置:5 ソート前の位置:5 ソート対象とする値:D0950: 新ジャンル
		//		ソート後の位置:6 ソート前の位置:6 ソート対象とする値:D9999: ノンアルコールビール
		//		ソート後の位置:7 ソート前の位置:7 ソート対象とする値:非購入
		//---------------------------------------------------------------------
		HashMap<Integer, Integer> map = new HashMap();
		for (int i = 0; i < result.size(); i++) {
			String[] array = result.get(i);
			map.put(i, Integer.valueOf(array[1]));
			//			System.out.println(
			//					"ソート後の位置:" + i + " ソート前の位置:" + array[1] + " ソート対象とする値:" + array[0]);
		}
		return map;
	}

	public static List<String[]> sortArrayList(String sortParam,
			List<String[]> arrayList) {
		ArrayComparator multiComparator = new ArrayComparator(sortParam);
		// ---------------------------------------------------------------------
		// sort
		// ---------------------------------------------------------------------
		Collections.sort(arrayList, multiComparator);
		List<String[]> outArrayList = new ArrayList();
		if (arrayList.size() > 0) {
			for (String[] array : arrayList) {
				outArrayList.add(array);
			}
		}
		return outArrayList;
	}

	// -------------------------------------------------------------------------
	// test
	// -------------------------------------------------------------------------
	public static void test_Array2CnvMap_01() {
		String[] data = { "", "合計", "4901002045876: Ｓ＆Ｂ 生風味スパソースツナしょうゆ風味８１．４ｇ",
				"4901002125745: Ｓ＆Ｂ まぜスパペペロンチーノ ４４．６ｇ",
				"4901002125752: Ｓ＆Ｂ まぜスパバジル ４８ｇ",
				"4901002125769: Ｓ＆Ｂ まぜスパ海老トマトクリーム １３０ｇ",
				"4901002132323: Ｓ＆Ｂ まぜスパ 生風味たらこバター ５３．４ｇ",
				"4901577020650: キユーピー あえるパスタソース たらこ ２３ｇ×２",
				"4901577020667: キューピーあえるパスタソースからし明太子２３ｇ×２",
				"4901577020698: キユーピー あえるパスタソースツナマヨ ４０ｇ×２",
				"4901577050282: あえるパスタソース薫るあおさ＆じゃこ ５６．８ｇ",
				"4901577055218: あえるミートソースフォン・ド・ヴォー仕立８０ｇ×２",
				"4901577055232: あえるカルボナーラ 濃厚チーズ仕立て ７０ｇ×２",
				"4901577055256: あえるカニのトマトＣマスカルポーネ仕立７０ｇ×２",
				"4901577055263: あえる和風きのこバター醤油仕立て５５ｇ×２",
				"4901577055287: あえるボンゴレビアンコ 白ワイン仕立て ６０ｇ×２",
				"4902110318609: ママー パスタキッチン ミートソース １４０ｇ",
				"4902110318616: ママー パスタキッチン ナポリタン １４０ｇ",
				"4902110318661: ママー リッチセレクト ミートソース ２６０ｇ",
				"4902110318685: ママー リッチセレクト ナポリタン ２４０ｇ",
				"4902110318708: ママー リッチセレクト カルボナーラ ２６０ｇ",
				"4902110319545: 青の洞窟Ｐ牛肉と赤ワインマスカルポーネ １３４ｇ",
				"4902521110236: 大人むけのパスタ イセエビのトマトクリーム１３０ｇ",
				"4902521110328: 大人むけのパスタ 粒たらこの十勝産生クリーム仕立て",
				"4902521110335: 大人むけのパスタ紅ずわい蟹のトマトクリーム１８０ｇ",
				"4902521110359: オマール海老のトマトソーススープ仕立て １８０ｇ",
				"4902521110366: ハインツ牛肉とイベリコ豚の粗挽きボロネーゼ１３０ｇ",
				"4902521110373: ハインツ 厚切りベーコンの贅沢ナポリタン １３０ｇ", "非購入" };
		int skip = 2;
		String sortParam = "0,String,A";
		HashMap<Integer, Integer> cnvMap = SortUtil.array2CnvMap(sortParam,
				arraySkip(data, skip));

		debug(cnvMap, data, skip);
	}

	public static void test_Array2CnvMap_02() {
		String[] data = { "0.15", "0.33", "0.13", "0.04", "0.11", "0.33",
				"0.08", "0.02", "0.09", "0.12", "0.10", "0.05", "0.06", "0.02",
				"0.01", "0.01", "0.03", "0.02", "0.03", "0.01", "0.81", "0.50",
				"0.30", "0.08", "0.16", "0.12", "0.07", "0.08", "0.17", "0.09",
				"0.10", "0.06", "0.26", "0.20", "0.07", "0.06", "0.13", "0.22",
				"0.08", "0.08", "0.03", "0.04", "0.03", "0.02", "0.16", "0.01",
				"0.01", "0.07", "0.02", "0.07", "0.04", "0.02", "0.04", "0.09",
				"0.09", "0.05", "0.03", "0.03", "0.05", "0.09", "0.18", "0.42",
				"0.27", "0.44", "0.08", "0.14", "0.06", "0.07", "0.10", "0.19",
				"0.25", "0.07", "0.04", "0.15", "0.07", "0.21", "0.14", "0.08",
				"0.15", "0.11", "0.04", "0.07", "0.03", "0.09", "0.08", "0.20",
				"0.13", "0.10", "0.02", "93.58" };

		String sortParam = "0,Double,D";
		HashMap<Integer, Integer> cnvMap = SortUtil.array2CnvMap(sortParam,
				data);

		debug(cnvMap, data, 0);
	}

	private static void debug(HashMap<Integer, Integer> map, String[] testData,
			int skip) {
		for (int seq = 0; seq < map.size(); seq++) {
			System.out.println("debug key:" + seq + " map:" + map.get(seq)
					+ " val:" + testData[map.get(seq) + skip]);
		}

	}

	//返値を受け取らなければ　副作用が発生しないなぜか？？　20170529
	public static String[] arrayCnvByMap_orgx(HashMap<Integer, Integer> cnvMap,
			String[] rec, int skip) {
		String[] outRec = new String[rec.length];
		if (cnvMap == null) {
			for (int i = 0; i < rec.length; i++) {
				outRec[i] = rec[i];
				if (outRec[i].equals(""))
					outRec[i] = "0";//20151124
			}
		} else {
			int col = 0;
			for (int i = 0; i < skip; i++) {
				outRec[col] = rec[i];
				if (outRec[col].equals(""))
					outRec[col] = "0";//20151124
				col++;
			}
			//			System.out.println(">>>#20170529# col:" + col + " skip:" + skip);
			for (int seq = 0; seq < cnvMap.size(); seq++) {
				Integer xSeq = cnvMap.get(seq);
				if (xSeq != null) {
					outRec[col] = rec[xSeq + skip];
					if (outRec[col].equals(""))
						outRec[col] = "0";//20151124
					col++;
				} else {
					System.err.println("xSeq==null seq:" + seq);
				}
			}
		}
		//		for (String element : outRec) {
		//			System.out.println("outRec #20170529# :" + element);
		//		}
		return outRec;
	}

	//返値を受け取らなければ　副作用が発生しないなぜか？？　20170529
	//副作用は勘違いであった
	public static String[] arrayCnvByMap(HashMap<Integer, Integer> cnvMap,
			String[] rec, int skip) {
		String[] outRec = new String[rec.length];
		for (int i = 0; i < rec.length; i++) {
			outRec[i] = rec[i];
			if (outRec[i].equals(""))
				outRec[i] = "0";//20151124
		}
		if (cnvMap != null) {
			int srcCol = 0;
			int dstCol = 0;
			List<Integer> keyList = new ArrayList(cnvMap.keySet());
			for (Integer key : keyList) {
				Integer val = cnvMap.get(key);
				if (val != null) {
					dstCol = key + skip;
					srcCol = val + skip;
					if (rec.length > srcCol && outRec.length > dstCol) {
						outRec[dstCol] = rec[srcCol];
						if (outRec[dstCol].equals(""))
							outRec[dstCol] = "0";//20151124
					}
				} else {
					System.err.println("xSeq==null seq:" + key);
				}
			}
		}
		return outRec;
	}

	public static String[] arrayCnvByMap_org(HashMap<Integer, Integer> cnvMap,
			String[] rec, int skip) {
		String[] outRec = new String[rec.length];
		if (cnvMap == null) {
			for (int i = 0; i < rec.length; i++) {
				outRec[i] = rec[i];
			}
		} else {
			int col = 0;
			for (int i = 0; i < skip; i++) {
				outRec[col++] = rec[i];
			}
			for (int seq = 0; seq < cnvMap.size(); seq++) {
				outRec[col++] = rec[cnvMap.get(seq) + skip];
			}
		}
		return outRec;
	}

	/**************************************************************************
	 * getCnvMap 横SORT用コンバータ作成				
	 * @param total_Low		合計行	{合計,100,0.45,2.27,0,23.65,11.01,26.38,9.5,56.45};	 
	 * @param headArray		ヘッダー	{期間１/期間２,合計,40641: ビール,40681: リキュール類,40691: 発泡酒,D0400: ビール,D0900: 発泡酒,D0950: 新ジャンル,D9999: ノンアルコールビール,非購入};	 
	 * @param skip			スキップ	skip = 2; 
	 * @param sortPlan		sortPlan = true;	 
	 **************************************************************************/
	public static HashMap<Integer, Integer> getCnvMap(String[] total_Low,
			String[] headArray, int skip, boolean sortPlan) {
		//#createTester--------------------------------------------------
//		System.out.println("public static void testgetCnvMap() {");
//		System.out.println("    String[] total_Low = {"
//				+ kyPkg.util.KUtil.join(total_Low, ",", "\"") + "};");
//		System.out.println("    String[] headArray = {"
//				+ kyPkg.util.KUtil.join(headArray, ",", "\"") + "};");
//		System.out.println("    int skip = " + skip + ";");
//		System.out.println("    boolean sortPlan = " + sortPlan + ";");
//		System.out.println(
//				"    getCnvMap ins = new getCnvMap(total_Low,headArray,skip,sortPlan);");
//		System.out.println("}");
		//--------------------------------------------------
		HashMap<Integer, Integer> cnvMap = null;
		//		int skip = 2;//先頭の2セルを処理対象外としたいので・・・とばす
		if (sortPlan) {
			//			for (int i = 0; i < total_Low.length; i++) {
			//				System.out.println("###debug20150721### total_Low[" + i + "]"
			//						+ total_Low[i]);
			//			}
			String sortParam = "0,Double,D";
			//TODO この段階でソートの対象から非購入を外しておく必要があり・・・処理後に変換マップの先頭に非購入を挿入する必要がある・・か
			String[] data = SortUtil.arraySkip(total_Low, skip);
			//			System.out.println("    String[] data = {" + kyPkg.util.KUtil.join(data) + "};");
			cnvMap = SortUtil.array2CnvMap_plus(sortParam, data, true);//非購入を先頭に移動する処理も行っている
		} else {
			//			for (int i = 0; i < headArray.length; i++) {
			//				System.out.println("###debug20150721### headArray[" + i + "]"
			//						+ headArray[i]);
			//			}
			String sortParam = "0,String,A";
			String[] data = SortUtil.arraySkip(headArray, skip);
			cnvMap = SortUtil.array2CnvMap_plus(sortParam, data, true);//非購入を先頭に移動する処理も行っている
		}
		return cnvMap;
	}

	//-------------------------------------------------------------------------
	//　こういうもののテストを描くのはむづかしい
	//-------------------------------------------------------------------------
	public static void testArrayCnvByMap() {
		String[] total_Low = { "合計", "100", "54.09", "20.53", "23.74", "27.37",
				"3.13", "3.42", "35.53", "41.88", "62.49", "25.27", "34.59",
				"41.78", "48.67", "70.6", "36.24", "18.03", "63.1", "27.68",
				"4.64", "3.01" };
		String[] headArray = { "期間１/期間２", "合計", "A0000: 日本茶飲料", "B0000: 麦茶飲料",
				"C0000: 中国茶飲料", "D0000: ブレンド茶他", "D1000: 健康茶", "E0000: 抹茶ラテ",
				"F0000: ミネラルウォーター", "G0000: 紅茶飲料", "H0000: コーヒー飲料",
				"I0000: 野菜１００％飲料", "I1000: 野菜果汁混合飲料", "J0000: 果汁１００％",
				"J1000: 果汁飲料", "L0000: 炭酸飲料", "M0000: スポーツ飲料",
				"N0000: 機能性飲料（栄養ドリンク含む）", "O0000: 乳性・乳酸菌", "Q0000: 豆乳飲料",
				"R0000: 酢飲料", "非購入" };
		String[] answer = { "期間１/期間２", "合計", "非購入", "L0000: 炭酸飲料",
				"O0000: 乳性・乳酸菌", "H0000: コーヒー飲料", "A0000: 日本茶飲料", "J1000: 果汁飲料",
				"G0000: 紅茶飲料", "J0000: 果汁１００％", "M0000: スポーツ飲料",
				"F0000: ミネラルウォーター", "I1000: 野菜果汁混合飲料", "Q0000: 豆乳飲料",
				"D0000: ブレンド茶他", "I0000: 野菜１００％飲料", "C0000: 中国茶飲料",
				"B0000: 麦茶飲料", "N0000: 機能性飲料（栄養ドリンク含む）", "R0000: 酢飲料",
				"E0000: 抹茶ラテ", "D1000: 健康茶", };
		int skip = 2;
		boolean sortPlan = true;
		HashMap<Integer, Integer> cnvMap = getCnvMap(total_Low, headArray, skip,sortPlan);
		//		List<Integer> keyList = new ArrayList(cnvMap.keySet());
		//		for (Integer key : keyList) {
		//			System.out.println("key:" + key + " val:" + cnvMap.get(key));
		//		}
		//		String[] result = arrayCnvByMap(cnvMap, headArray, 2);
		String[] result = arrayCnvByMap(cnvMap, headArray, 2);
		//		for (int i = 0; i < result.length; i++) {
		//			System.out.println("\"" + result[i] + "\",");
		//		}
		boolean isEQ = Arrays.equals(answer, result);
		System.out.println("isEQ:" + isEQ);
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		//		testgetCnvMap();
		testArrayCnvByMap();
	}
}
