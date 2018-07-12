package kyPkg.uRegex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ListUtil {

	public ListUtil() {
		// TODO Auto-generated constructor stub
	}

	// -------------------------------------------------------------------------
	//　まぁ、やっつけ仕事（もっときれいに書ければよいのだが・・・） 20160819
	// -------------------------------------------------------------------------
	/**************************************************************************
	 * limitter		
	 * @author	ken yuasa
	 * @version	1.0
	 * <ul>
	 * <ll>【概要】要素をデリミタで分割し、当該カラムが正規表現に一致する行について、指定したカラムをフォーマットしたものをリストで返す
	 * </ul>
	 * @param regCol	正規表現で評価する対象とするカラム		 
	 * @param regex		正規表現検査文字列	 
	 * @param format	出力に使用するフォーマット、出力カラムと同数の%sを指定する		 
	 * @param cols		出力対象カラム	 
	 * @param delimiter	リスト中の文字列を分割する		 
	 * @param list		処理対象とする文字列リスト	 
	 **************************************************************************/
	public static List<String> limitter(int[] regCol, String[] regex,
			String format, int[] cols, String delimiter, List<String> list) {
		List<String> resList = new ArrayList();
		Pattern[] pattern = getPatterns(regex, false);
		for (String element : list) {
			String[] array = element.split(delimiter, -1);
			int chkCount = check(regCol, pattern, array);
			if (chkCount == regCol.length) {
				String res = "";
				switch (cols.length) {
				case 1:
					res = String.format(format, array[cols[0]]);
					break;
				case 2:
					res = String.format(format, array[cols[0]], array[cols[1]]);
					break;
				case 3:
					res = String.format(format, array[cols[0]], array[cols[1]],
							array[cols[2]]);
					break;
				case 4:
					res = String.format(format, array[cols[0]], array[cols[1]],
							array[cols[2]], array[cols[3]]);
					break;
				case 5:
					res = String.format(format, array[cols[0]], array[cols[1]],
							array[cols[2]], array[cols[3]], array[cols[4]]);
					break;
				default:
					break;
				}
				resList.add(res);
//				System.out.println("limitter test=>" + res);
			}
		}
		return resList;
	}

	//-------------------------------------------------------------------------
	//	リストの各要素をデリミタで区切り指定された位置の要素をユニークなリストで返す 20160822
	// -------------------------------------------------------------------------
	//	110098000002_東京急行電鉄／０９０７ふって飲むゼリー飲料　　　　　　　　　　　　　　　　　　　,E89093,110098000002,2015/02/03 12:21:51
	//	110098000008_東京急行電鉄／ランキン用栄養ドリンク　　　　　　　　　　　　　　　　　　　　　　,COMMON,110098000008,2015/02/03 12:21:51
	//	110098000009_東京急行電鉄／ランキン用スポーツドリンク　　　　　　　　　　　　　　　　　　　　,COMMON,110098000009,2015/02/03 12:21:51
	//	110098000130_東京急行電鉄／ランキン‐ミネラルウォーター　　　　　　　　　　　　　　　　　　　,COMMON,110098000130,2015/04/27 12:07:53
	//	123067000001_フジパン／バターロール１５０７　　　　　　　　　　　　　　　　　　　　　　　　　,COMMON,123067000001,2016/02/25 09:27:00
	//	123067000002_フジパン／薄皮シリーズ１５０２　　　　　　　　　　　　　　　　　　　　　　　　　,COMMON,123067000002,2016/01/04 12:35:35
	//	123091000001_ピジョン／ハンドソープ　　　　　　　　　　　　　　　　　　　　　　　　　　　　　,COMMON,123091000001,2015/02/03 12:03:26
	//	123620000001_永谷園／惣菜の素１４０４　　　　　　　　　　　　　　　　　　　　　　　　　　　　,COMMON,123620000001,2015/02/02 17:21:45
	//-------------------------------------------------------------------------
	public static List<String> list2uqList(String delimiter, List<String> list,int col) {
		Set<String> set = new HashSet();
		for (String element : list) {
			String[] array = element.split(delimiter, -1);
			if (array.length > col) {
				set.add(array[col]);
			}
		}
		List<String> resList = new ArrayList(set);
		Collections.sort(resList);
		return resList;
	}

	// -------------------------------------------------------------------------
	//パターンに一致した数を返す（いくつのパターンに一致するか）
	//TODO　パフォーマンス改善の余地あり・・・・
	//TODO	OR　　一個でも一致すればよい場合　=>　一個目の一致でリターンさせたい
	//TODO	AND　すべてに一致しなければならない場合　=>　一個でも一致しなければ０を返す
	// -------------------------------------------------------------------------
	private static Pattern[] getPatterns(String[] regex, boolean ignoreCase) {
		Pattern[] pattern = new Pattern[regex.length];
		for (int col = 0; col < regex.length; col++) {
			pattern[col] = Regex.getPatternEx(regex[col], ignoreCase);
		}
		return pattern;
	}

	// -------------------------------------------------------------------------
	//TODO regColとpatternの数があっていないといけない 
	// -------------------------------------------------------------------------
	public static int check(int[] regCol, Pattern[] pattern, String[] array) {
		int chkCount = 0;
		for (int seq = 0; seq < regCol.length; seq++) {
			int col = regCol[seq];
			if (array.length > col && pattern.length > seq) {
				if (pattern[seq].matcher(array[col]).matches()) {
					chkCount++;
				}
			}
		}
		return chkCount;
	}


	// -------------------------------------------------------------------------
	//	指定品目をピックアップするロジックに使用する予定　20160819
	// -------------------------------------------------------------------------
	public static void testLimitter01() {
		List<String> list = new ArrayList();
		list.add(
				"110098000002_東京急行電鉄／０９０７ふって飲むゼリー飲料　　　　　　　　　　　　　　　　　　　,E89093,110098000002,2015/02/03 12:21:51");
		list.add(
				"110098000008_東京急行電鉄／ランキン用栄養ドリンク　　　　　　　　　　　　　　　　　　　　　　,COMMON,110098000008,2015/02/03 12:21:51");
		list.add(
				"110098000009_東京急行電鉄／ランキン用スポーツドリンク　　　　　　　　　　　　　　　　　　　　,COMMON,110098000009,2015/02/03 12:21:51");
		list.add(
				"110098000130_東京急行電鉄／ランキン‐ミネラルウォーター　　　　　　　　　　　　　　　　　　　,COMMON,110098000130,2015/04/27 12:07:53");
		list.add(
				"123067000001_フジパン／バターロール１５０７　　　　　　　　　　　　　　　　　　　　　　　　　,COMMON,123067000001,2016/02/25 09:27:00");
		list.add(
				"123067000002_フジパン／薄皮シリーズ１５０２　　　　　　　　　　　　　　　　　　　　　　　　　,COMMON,123067000002,2016/01/04 12:35:35");
		list.add(
				"123091000001_ピジョン／ハンドソープ　　　　　　　　　　　　　　　　　　　　　　　　　　　　　,COMMON,123091000001,2015/02/03 12:03:26");
		list.add(
				"123620000001_永谷園／惣菜の素１４０４　　　　　　　　　　　　　　　　　　　　　　　　　　　　,COMMON,123620000001,2015/02/02 17:21:45");
		int[] regCol = new int[] { 1, 0 };
		String[] regex = new String[] { "COMMON", "ランキン" };
		String format = "%s 最終更新日付：%s";
		int[] col = new int[] { 0, 3 };
		String delimiter = ",";
		List<String> resList = ListUtil.limitter(regCol, regex, format, col,
				delimiter, list);
	}

	// -------------------------------------------------------------------------
	// it2ファイル群からｉｓａｍ（指定品目名フォルダ）を生成するバッチ
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		//---------------------------------------------------------------------
		testLimitter01();
		//---------------------------------------------------------------------
		elapse.stop();
	}

}
