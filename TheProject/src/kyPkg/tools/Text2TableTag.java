package kyPkg.tools;

/**
 * テキスト文字列からテーブルタグを作り出します
 * クラスに対してのコメントを記述します。
 * ここでは以下の内容を記述します。
 * <BR><UL>
 * <BR><LI>クラスがどういうことをするのかの説明
 * <BR><LI>インスタンスが取り得る状態についての情報。
 * <BR>    例） ファイルがオープンされている状態とクローズされた状態での振る舞い。
 * <BR><LI>OSやハードウェアへの依存性。例）java.io.Fileクラス
 * <BR><LI>クラスの不変条件や一般契約。例）java.lang.Comparableインタフェース
 * <BR><LI>インスタンスのスレッド安全性レベル（注2）。
 * <BR>    例）java.lang.Appendableインタフェース
 * <BR><LI>セキュリティ制約。例）java.lang.RuntimePermissionクラス
 * <BR><LI>直列化の形式
 * <BR><LI>インタフェースを実装する場合またはクラスを継承する場合の注意点。
 * <BR>    例）java.util.AbstractListクラス
 * <BR><LI>他のクラスとの関連性。
 * <BR><LI>外部仕様への参照。例）java.net.URLクラス
 * @author ken yuasa
 * @version 1.0
 * @see http://gihyo.jp/dev/serial/01/skillful_method/0001?page=3
 */
public class Text2TableTag extends Tool {
	// ------------------------------------------------------------------------
	/**
	 * getDelimiter1	区切り文字を決定する			
	 * @param str		検査対象文字列	 
	 */
	// ------------------------------------------------------------------------

	private static String getDelimiter1(String str) {
		String delimiter = "\n";
		if (str.indexOf("\n") >= 0) {
			delimiter = "\n";
		} else if (str.indexOf(";") >= 0) {
			delimiter = ";";
		} else if (str.indexOf(".") >= 0) {
			delimiter = ".";
		}
		return delimiter;
	}

	private static String getDelimiter2(String str) {
		//先頭が\tだと＞0のところで誤動作する！！
		String delimiter = "\t";
		if (str.indexOf("\t") >= 0) {
			delimiter = "\t";
		} else if (str.indexOf(",") >= 0) {
			delimiter = ",";
		} else if (str.indexOf(":") >= 0) {
			delimiter = ":";
		} else if (str.indexOf(" ") >= 0) {
			delimiter = " ";
		}
		return delimiter;
	}

	public static String execute(String str) {
		StringBuffer buf = new StringBuffer();
		String dlm1 = getDelimiter1(str);
		String[] arr1 = str.split(dlm1, -1);
		boolean firstTime = true;

		buf.append("<br>\n");
		buf.append("<br><hr>入出力ファイル　【サンプル】　\n");
		buf.append("<br>\n");
		buf.append("<table border='1'>\n");
		for (String line : arr1) {
			if (!line.equals("")) {
//				line = line.replaceFirst("\t", "");//コメントを外す・・
				line = line.replaceFirst("^\\s*//\\s*", "");//コメントを外す・・
				String dlm2 = getDelimiter2(line);
				String[] arr2 = line.split(dlm2, -1);
				if (firstTime) {
					buf.append("<tr bgcolor='DeepSkyBlue'>");
					for (int col = 0; col < arr2.length; col++) {
						buf.append("<td>#" + col + "</td>");
					}
					buf.append("</tr>\n");
					firstTime = false;
				}
				buf.append("<tr>");
				for (String element : arr2) {
					buf.append("<td>" + element + "</td>");
				}
				buf.append("</tr>\n");
			}
		}
		buf.append("</table>");
		return buf.toString();
	}

	public static void main(String[] args) {
		StringBuffer buf= new StringBuffer();		
		buf.append("	// 0_Id,1_Price,2_Count,3_Break,4_Shop1,5_AcceptDate,6_Flg3,7_Flg1,8_Flg2,9_Shop2,10_ym,11_hh,12_Idx,13_Week,14_Capa\n");
		buf.append("	//	73302423	359	1	00002: 生ラーメン	1V	20141018	 	 	 	1	1410	15	30	6	336000\n");
		buf.append("	//	71205401	321	1	00002: 生ラーメン	71	20140924	 	 	 	7	1409	18	37	3	336000\n");
		buf.append("	//	74136616	420	1	00004: 生うどん	10	20141224	 	 	 	1	1412	13	27	3	424000\n");
		buf.append("	//	74136616	418	1	00004: 生うどん	10	20141001	 	 	 	1	1410	13	27	3	424000\n");
		buf.append("	//	71596376	409	2	00004: 生うどん	10	20141219	 	 	 	1	1412	16	33	5	424000\n");
		String str = buf.toString();
		String result = Text2TableTag.execute(str);
		System.out.println("result=>" + result);
	}
}
