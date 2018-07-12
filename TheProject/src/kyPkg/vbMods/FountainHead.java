package kyPkg.vbMods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FountainHead extends VB_Base {
	public List<String> keyList;
	private HashMap<String, Integer> hashIdx;
	private HashMap<String, List> hashVal;

	// コンストラクター
	public FountainHead() {
		super();
		System.out.println("コンストラクター");
		keyList = new ArrayList();
		hashIdx = new HashMap();
		hashVal = new HashMap();
	}

	private void forceRegist(String key, List list) {
		hashVal.put(key, list);
	}

	public int regist(String key, String value) {
		Integer hIdx = hashIdx.get(key);
		if (hIdx != null && hIdx > 0) {
			// キーが存在する;
			List<String> list = hashVal.get(key);
			// 同じフィールドが存在しない場合のみ追加する;
			for (String wVal : list) {
				if (wVal.equals(value))
					return hIdx;
			}
			list.add(value);
			hashVal.put(key, list);
		} else {
			// キーが存在しなかった;
			keyList.add(key);
			hashIdx.put(key, keyList.size());
			List<String> list = new ArrayList();
			list.add(value);
			hashVal.put(key, list);
			hIdx = hashIdx.get(key);
		}
		return (int) hIdx;
	}

	public String getKey(int index) {
		return keyList.get(index);
	}

	public List getCollection_old(int index) {
		String key = getKey(index);
		return hashVal.get(key);
	}

	public List getCollection(String key) {
		return hashVal.get(key);
	}

	public void enumurateIt() {
		System.out.println("#<enumurateIt># start");
		for (String wKey : keyList) {
			System.out.println("table:" + wKey);
			List<String> list = hashVal.get(wKey);
			for (String wVal : list) {
				System.out.println("    field:" + wVal);
			}
		}
	}

	// 意味が分からない・・・本当は配列をかいしたいんじゃないのか？？？
	public List getArray(String key) {
		List<String> list = hashVal.get(key);
		List xArray = new ArrayList(); // ReDim xArray(list.Count);
		for (String v1 : list) {
			xArray.add(v1);
		}
		return xArray;
	} // <function>;

	public void buildTester() {
		System.out.println("テスト用データ生成");
		String key = "Z:\\s2\\rx\\Enquetes\\Common\\ブランドユーザー（その他茶ドリンク）\\201007～09\\ALIAS.txt";
		regist(key, "");
		regist(key, "'1'" + vbTab + "FIXX" + vbTab + "Char     Width 1");
		regist(key, "SUBSTRING($,1,1) " + vbTab + "q1x1_01" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,2,1) " + vbTab + "q1x1_02" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,3,1) " + vbTab + "q1x1_03" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,4,1) " + vbTab + "q1x1_04" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,5,1) " + vbTab + "q1x1_05" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,6,1) " + vbTab + "q1x1_06" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,7,1) " + vbTab + "q1x1_07" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,8,1) " + vbTab + "q1x1_08" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,9,1) " + vbTab + "q1x1_09" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,11,1)" + vbTab + "q1x1_11" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,12,1)" + vbTab + "q1x1_12" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,13,1)" + vbTab + "q1x1_13" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,14,1)" + vbTab + "q1x1_14" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,15,1)" + vbTab + "q1x1_15" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,16,1)" + vbTab + "q1x1_16" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,17,1)" + vbTab + "q1x1_17" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,18,1)" + vbTab + "q1x1_18" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,19,1)" + vbTab + "q1x1_19" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,20,1)" + vbTab + "q1x1_20" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,21,1)" + vbTab + "q1x1_21" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,22,1)" + vbTab + "q1x1_22" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,23,1)" + vbTab + "q1x1_23" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,24,1)" + vbTab + "q1x1_24" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,25,1)" + vbTab + "q1x1_25" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,26,1)" + vbTab + "q1x1_26" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,27,1)" + vbTab + "q1x1_27" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,28,1)" + vbTab + "q1x1_28" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,29,1)" + vbTab + "q1x1_29" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,30,1)" + vbTab + "q1x1_30" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,31,1)" + vbTab + "q1x1_31" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,32,1)" + vbTab + "q1x1_32" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,33,1)" + vbTab + "q1x1_33" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,34,1)" + vbTab + "q1x1_34" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,35,1)" + vbTab + "q1x1_35" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,36,1)" + vbTab + "q1x1_36" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,37,1)" + vbTab + "q1x1_37" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,38,1)" + vbTab + "q1x1_38" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,39,1)" + vbTab + "q1x1_39" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,40,1)" + vbTab + "q1x1_40" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,41,1)" + vbTab + "q1x1_41" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,42,1)" + vbTab + "q1x1_42" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,43,1)" + vbTab + "q1x1_43" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,44,1)" + vbTab + "q1x1_44" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,45,1)" + vbTab + "q1x1_45" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,46,1)" + vbTab + "q1x1_46" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,47,1)" + vbTab + "q1x1_47" + vbTab
				+ "Char     Width 1");
		regist(key, "SUBSTRING($,10,1)" + vbTab + "q1x1_10" + vbTab
				+ "Char     Width 1");
		regist(key, "" + vbTab + "" + vbTab + "");
	}

	public String concatenate(List<String> mappedList, String key) {
		// パラメータを再構築(Substringをひとまとめにする)
		int svLen = 0;
		String preSub = "";
		String svSub = "";
		int prePos = 0;
		int preLen = 0;
		for (String wStr : mappedList) {
			wStr = wStr.replaceAll("\\)", ",");
			String[] chunk2 = wStr.split(",");
			if (UBound(chunk2) >= 2) {
				String wSub = chunk2[0];
				int wPos = Integer.parseInt(chunk2[1]);
				int wLen = Integer.parseInt(chunk2[2]);
				if ((prePos + preLen) == wPos) {
					preLen = preLen + wLen;
				} else {
					if (prePos > 0) {
						if (!svSub.equals(""))
							svSub = svSub + " + ";
						svSub = svSub + preSub + "," + prePos + "," + preLen
								+ ")";
						svLen = svLen + preLen;
						// System.out.println("#>" + svSub + " Char     Width "
						// + svLen)
					}
					preSub = wSub;
					prePos = wPos;
					preLen = wLen;
				}
				// System.out.println(" " + prePos + " " + preLen)
			}
		}
		if (prePos > 0) {
			if (!svSub.equals(""))
				svSub = svSub + " + ";
			svSub = svSub + preSub + "," + prePos + "," + preLen + ")";
			svLen = svLen + preLen;
			// System.out.println("#>" + svSub + " Char Width " + svLen)
		} // end_if;
		// 2011_02_01 スペースがサプレスされるのを防ぐために区切り文字を追加した
		// isamにインポートする際、先行するスペースがすべてサプレスされてしまうのでマルチアンサー項目は破壊されてしまう
		// これを回避するためには項目を”で括る
		// この場合全スペースはis　nullで引かないとピックアップできない（=' 'などではピックアップできない）
		// sql上に区切り文字を乗っけられるかな？
		// SELECT ID,FIXX,'"'+q7+'"' FROM T01.TXT
		// 結論→イケル！！
		String dQ = "";
		String sQ = "";
		String quote = "";
		dQ = Chr(34);
		sQ = Chr(39);
		quote = sQ + dQ + sQ;
		return quote + " + " + svSub + " + " + quote + vbTab + key + vbTab
				+ "Char Width " + svLen;
	} // <function>;

	private List MapWithMother(List<String> fieldList) {
		// パラメータ中のを親フィールドごとにsubstrパラメータを合成する;
		List ansList = new ArrayList();
		FountainHead objMini = new FountainHead();
		for (String wVar : fieldList) {
			if (!wVar.equals("")) {
				String[] chunk = wVar.split(vbTab);
				if (UBound(chunk) >= 2) {
					String qx_Sub = chunk[0];
					String qx_Key = chunk[1];
					String qx_Wid = chunk[2];
					String wMother = Left(qx_Key,
							(InStr(qx_Key + "_", "_")) - 1);
					System.out.println("sub:" + qx_Sub + " key:" + qx_Key
							+ " Wid:" + qx_Wid + " wMother:" + wMother);
					objMini.regist(wMother, qx_Sub);
				}
			}
		}
		// Substrを合成する;
		for (String wKey : objMini.keyList) {
			List mappedList = objMini.getCollection(wKey);
			if (!wKey.equals("")) {
				String ans = objMini.concatenate(mappedList, wKey);
				System.out.println("Ans >" + ans);
				ansList.add(ans);
			}
		}
		return ansList;
	}

	public void fieldConvert() {
		for (String wTable : keyList) {
			// テーブル１が持っているフィールドコレクション;
			List fieldList = getCollection(wTable);
			// フィールドを集約する;
			List ansList = MapWithMother(fieldList);
			// 各キー,構\成要素を＋でコンカチしたものでレジストしていく・・・;
			forceRegist(wTable, ansList);
		}
	}

	public static void main(String[] argv) {
		FountainHead ins = new FountainHead();
		ins.testFountainHeads();
	}

	public void testFountainHeads() {
		// ------------------------------------------------------------------------------
		// <使用例>
		// Dim objFH As FountainHead
		// objFH = new FountainHead();
		// objFH.testFountainHeads()
		// :
		// regist etc ...
		// :
		// objFH.fieldConvert()
		// ------------------------------------------------------------------------------
		System.out.println("テストデータ生成");
		buildTester();
		System.out.println("パラメータを親フィールドごとに集約する");
		fieldConvert();
		System.out.println("結果確認");
		enumurateIt();
	}
}
// Function getKeyArrayxxx() As Variant
// Dim xArray() As Variant
// xArray = new ArrayList(); // ReDim xArray(keyList.Count)
// Dim wKey As Variant
// Dim i As Integer
// i = 0
// For Each wKey In keyList
// i = i + 1
// xArray[i] = wKey
// System.out.println("table:" + wKey)
// } // Next
// getKeyArrayxxx = xArray
// return rtnVal;
// } // <function>
