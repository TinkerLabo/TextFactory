package kyPkg.sql;

import java.util.ArrayList;
import java.util.List;

public class Sanitize {

	public Sanitize() {
		// TODO Auto-generated constructor stub
	}

	/**
	* ������̒u�����s��
	*
	* @param input �����̑Ώۂ̕�����
	* @param pattern �u���O�̕�����
	* @oaram replacement �u����̕�����
	* @return �u��������̕�����
	*/
	static public String substitute(String input, String pattern,
			String replacement) {
		// �u���Ώە����񂪑��݂���ꏊ���擾
		int index = input.indexOf(pattern);
		// �u���Ώە����񂪑��݂��Ȃ���ΏI��
		if (index == -1) {
			return input;
		}
		// �������s�����߂� StringBuffer
		StringBuffer buffer = new StringBuffer();
		buffer.append(input.substring(0, index) + replacement);
		if (index + pattern.length() < input.length()) {
			// �c��̕�������ċA�I�ɒu��
			String rest = input.substring(index + pattern.length(),
					input.length());
			buffer.append(substitute(rest, pattern, replacement));
		}
		return buffer.toString();
	}

	/**
	* HTML �o�͗p�Ɏ��̒u�����s��
	* & -> &
	* < -> <
	* > -> >
	* " -> "
	*
	* @param input �u���Ώۂ̕�����
	* @return �u��������̕�����
	*/
	static public String htmlEscape(String input) {
		input = substitute(input, "&", "&");
		input = substitute(input, "<", "<");
		input = substitute(input, ">", ">");
		//  input = substitute(input, "\"", """);
		return input;
	}

	/**
	* SQL���o�͗p�Ɏ��̒u�����s��
	* ' -> ''
	* \ -> \\
	*
	* @param list �u���Ώۂ̕�����
	* @return �u��������̕�����
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
