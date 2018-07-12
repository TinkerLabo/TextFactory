package kyPkg.tools;

/**
 * �e�L�X�g�����񂩂�e�[�u���^�O�����o���܂�
 * �N���X�ɑ΂��ẴR�����g���L�q���܂��B
 * �����ł͈ȉ��̓��e���L�q���܂��B
 * <BR><UL>
 * <BR><LI>�N���X���ǂ��������Ƃ�����̂��̐���
 * <BR><LI>�C���X�^���X����蓾���Ԃɂ��Ă̏��B
 * <BR>    ��j �t�@�C�����I�[�v������Ă����ԂƃN���[�Y���ꂽ��Ԃł̐U�镑���B
 * <BR><LI>OS��n�[�h�E�F�A�ւ̈ˑ����B��jjava.io.File�N���X
 * <BR><LI>�N���X�̕s�Ϗ������ʌ_��B��jjava.lang.Comparable�C���^�t�F�[�X
 * <BR><LI>�C���X�^���X�̃X���b�h���S�����x���i��2�j�B
 * <BR>    ��jjava.lang.Appendable�C���^�t�F�[�X
 * <BR><LI>�Z�L�����e�B����B��jjava.lang.RuntimePermission�N���X
 * <BR><LI>���񉻂̌`��
 * <BR><LI>�C���^�t�F�[�X����������ꍇ�܂��̓N���X���p������ꍇ�̒��ӓ_�B
 * <BR>    ��jjava.util.AbstractList�N���X
 * <BR><LI>���̃N���X�Ƃ̊֘A���B
 * <BR><LI>�O���d�l�ւ̎Q�ƁB��jjava.net.URL�N���X
 * @author ken yuasa
 * @version 1.0
 * @see http://gihyo.jp/dev/serial/01/skillful_method/0001?page=3
 */
public class Text2TableTag extends Tool {
	// ------------------------------------------------------------------------
	/**
	 * getDelimiter1	��؂蕶�������肷��			
	 * @param str		�����Ώە�����	 
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
		//�擪��\t���Ɓ�0�̂Ƃ���Ō듮�삷��I�I
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
		buf.append("<br><hr>���o�̓t�@�C���@�y�T���v���z�@\n");
		buf.append("<br>\n");
		buf.append("<table border='1'>\n");
		for (String line : arr1) {
			if (!line.equals("")) {
//				line = line.replaceFirst("\t", "");//�R�����g���O���E�E
				line = line.replaceFirst("^\\s*//\\s*", "");//�R�����g���O���E�E
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
		buf.append("	//	73302423	359	1	00002: �����[����	1V	20141018	 	 	 	1	1410	15	30	6	336000\n");
		buf.append("	//	71205401	321	1	00002: �����[����	71	20140924	 	 	 	7	1409	18	37	3	336000\n");
		buf.append("	//	74136616	420	1	00004: �����ǂ�	10	20141224	 	 	 	1	1412	13	27	3	424000\n");
		buf.append("	//	74136616	418	1	00004: �����ǂ�	10	20141001	 	 	 	1	1410	13	27	3	424000\n");
		buf.append("	//	71596376	409	2	00004: �����ǂ�	10	20141219	 	 	 	1	1412	16	33	5	424000\n");
		String str = buf.toString();
		String result = Text2TableTag.execute(str);
		System.out.println("result=>" + result);
	}
}
