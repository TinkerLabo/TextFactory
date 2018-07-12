package kyPkg.tools;

/**
 * テーブルタグからタグを取り除きます
 * @author ken yuasa
 * @version 1.0
 * @see http://gihyo.jp/dev/serial/01/skillful_method/0001?page=3
 */
public class TagRemover extends Tool {
	public static String execute(String str) {
		str = str.replaceAll("\n", "");
		str = str.replaceAll("<table border='1'>", "");
		str = str.replaceAll("</table>", "");
		str = str.replaceAll("<tr>", "");
		str = str.replaceAll("</tr>", "\n");
		str = str.replaceAll("<td>", "");
		str = str.replaceAll("</td>", "\t");
		return str;
	}

	public static void main(String[] args) {
		String str = "<table border='1'><tr><td>メーカー　　　　　　　　　　　</td><td>MKR</td></tr><tr><td>区分１　　　　　　　　　　　　</td><td>KB1</td></tr><tr><td>区分２　　　　　　　　　　　　</td><td>KB2</td></tr><tr><td>区分３　　　　　　　　　　　　</td><td>KB3</td></tr><tr><td>区分４　　　　　　　　　　　　</td><td>KB4</td></tr><tr><td>区分５　　　　　　　　　　　　</td><td>KB5</td></tr><tr><td>区分６　　　　　　　　　　　　</td><td>KB6</td></tr><tr><td>アイテム　　　　　　　　　　　</td><td>ITM</td></tr><tr><td>品目名  　　　　　　　　　　　</td><td>HN4</td></tr></table>";
		String result = TagRemover.execute(str);
		System.out.println("result=>" + result);
	}
}
