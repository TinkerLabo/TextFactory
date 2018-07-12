package kyPkg.sql;

import java.util.ArrayList;
import java.util.List;

public class Sanitize {

	public Sanitize() {
		// TODO Auto-generated constructor stub
	}

	/**
	* 文字列の置換を行う
	*
	* @param input 処理の対象の文字列
	* @param pattern 置換前の文字列
	* @oaram replacement 置換後の文字列
	* @return 置換処理後の文字列
	*/
	static public String substitute(String input, String pattern,
			String replacement) {
		// 置換対象文字列が存在する場所を取得
		int index = input.indexOf(pattern);
		// 置換対象文字列が存在しなければ終了
		if (index == -1) {
			return input;
		}
		// 処理を行うための StringBuffer
		StringBuffer buffer = new StringBuffer();
		buffer.append(input.substring(0, index) + replacement);
		if (index + pattern.length() < input.length()) {
			// 残りの文字列を再帰的に置換
			String rest = input.substring(index + pattern.length(),
					input.length());
			buffer.append(substitute(rest, pattern, replacement));
		}
		return buffer.toString();
	}

	/**
	* HTML 出力用に次の置換を行う
	* & -> &
	* < -> <
	* > -> >
	* " -> "
	*
	* @param input 置換対象の文字列
	* @return 置換処理後の文字列
	*/
	static public String htmlEscape(String input) {
		input = substitute(input, "&", "&");
		input = substitute(input, "<", "<");
		input = substitute(input, ">", ">");
		//  input = substitute(input, "\"", """);
		return input;
	}

	/**
	* SQL文出力用に次の置換を行う
	* ' -> ''
	* \ -> \\
	*
	* @param list 置換対象の文字列
	* @return 置換処理後の文字列
	*/

	public static String sqlEscape(String input) {
		input = substitute(input, "'", "''");
		input = substitute(input, "\\", "\\\\");
		return input;
	}

	public static List<Object> sqlEscape(List<Object> list) {
		List<Object> res = new ArrayList();
		for (Object element : list) {
			if (element instanceof String) {
				res.add(sqlEscape((String)element));
			} else {
				res.add(element);
			}
		}
		return res;
	}

}
