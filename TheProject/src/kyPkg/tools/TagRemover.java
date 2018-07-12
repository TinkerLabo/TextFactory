package kyPkg.tools;

/**
 * �e�[�u���^�O����^�O����菜���܂�
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
		String str = "<table border='1'><tr><td>���[�J�[�@�@�@�@�@�@�@�@�@�@�@</td><td>MKR</td></tr><tr><td>�敪�P�@�@�@�@�@�@�@�@�@�@�@�@</td><td>KB1</td></tr><tr><td>�敪�Q�@�@�@�@�@�@�@�@�@�@�@�@</td><td>KB2</td></tr><tr><td>�敪�R�@�@�@�@�@�@�@�@�@�@�@�@</td><td>KB3</td></tr><tr><td>�敪�S�@�@�@�@�@�@�@�@�@�@�@�@</td><td>KB4</td></tr><tr><td>�敪�T�@�@�@�@�@�@�@�@�@�@�@�@</td><td>KB5</td></tr><tr><td>�敪�U�@�@�@�@�@�@�@�@�@�@�@�@</td><td>KB6</td></tr><tr><td>�A�C�e���@�@�@�@�@�@�@�@�@�@�@</td><td>ITM</td></tr><tr><td>�i�ږ�  �@�@�@�@�@�@�@�@�@�@�@</td><td>HN4</td></tr></table>";
		String result = TagRemover.execute(str);
		System.out.println("result=>" + result);
	}
}
