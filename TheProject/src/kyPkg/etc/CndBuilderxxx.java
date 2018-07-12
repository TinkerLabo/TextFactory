package kyPkg.etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//20170719  �敪���̂ŃA�C�e������ł���悤�ɂ���ׂ̃��@�[�W����
public class CndBuilderxxx {
	// ------------------------------------------------------------------------
	public static final String CONTAINS = "���܂܂��";
	private static final String BEGINWITH = "�Ŏn�܂�";
	private static final String ENDWITH = "�ŏI���";
	public static final String EQUALS = "�ƈ�v����";
	public static final String NOT_CONTAINS = "���܂܂Ȃ�";
	public static final String NOT_EQUALS = "�ƈ�v���Ȃ�";
	public static final String[] CONDITIONS = new String[] { CONTAINS,
			BEGINWITH, EQUALS, NOT_CONTAINS, NOT_EQUALS };

	public static String getPattern() {
		return "(" + CndBuilderxxx.CONTAINS + "|" + CndBuilderxxx.ENDWITH + "|"
				+ CndBuilderxxx.BEGINWITH + "|" + CndBuilderxxx.EQUALS + ")";
	}

	// ------------------------------------------------------------------------
	public static final String AND = "1";
	public static final String OR = "0";
	// ------------------------------------------------------------------------
	private String prefix = "";
	private HashMap<String, String> dictionary = null;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public CndBuilderxxx(String debug, String prefix,
			HashMap<String, String> dictionary) {
		super();
		//		System.out.println("#DEBUG#CndBuilder�F" + debug);
		prefix = prefix.trim();
		if (!prefix.equals("")) {
			prefix = prefix + ".";
		}
		this.prefix = prefix;
		this.dictionary = dictionary;
	}

	public static HashMap<String, String> array2hashMapU(String[] array,
			String delimiter) {
		// key�����͑啶���ɕϊ����܂��B
		HashMap<String, String> dictionary = new HashMap();
		for (int i = 0; i < array.length; i++) {
			String[] splited = array[i].split(delimiter);
			if (splited.length > 1) {
				dictionary.put(splited[0].toUpperCase(), splited[1]);
			}
		}
		return dictionary;
	}

	public String getCondJan(List<String> condList1, List<String> condList2,
			String rel1, String rel2, String rel3, boolean debug) {
		if (condList1 == null)
			condList1 = new ArrayList(); //20170718
		if (condList2 == null)
			condList2 = new ArrayList(); //20170718
		if (condList1.size() == 0 && condList2.size() == 0)
			return "";
		return getCond(condList1, condList2, rel1, rel2, rel3);
	}

	public String getCond(List<String> condList1, List<String> condList2,
			String rel1, String rel2, String rel3) {
		// dictionary => �t�B�[���h��DB��̃t�B�[���h���̑�
		// �����Ŏ����ɓ������̂����������̶ӂ���Ȃ�
		String xWhere = "";
		String xWhere1 = parseIt(rel1, condList1);
		String xWhere2 = parseIt(rel2, condList2);
		if ((!xWhere1.equals("")) && (!xWhere2.equals(""))) {
			if (rel3.equals(AND)) {
				xWhere = "(" + xWhere1 + ") AND (" + xWhere2 + ")";
			} else {
				xWhere = "(" + xWhere1 + ") OR (" + xWhere2 + ")";
			}
		} else {
			xWhere = xWhere1 + xWhere2;
		}
		return xWhere;
	}

	private String parseIt(String relType, List<String> condList) {
		if (condList == null || condList.size() == 0)
			return "";
		String xWhere = "";
		String relation = " OR ";
		if (relType.equals(AND))
			relation = " AND ";
		for (String element : condList) {
			//TODO	��肽��������
			//kMK IN (SELECT MK.kCode FROM MK where( kName Like '%�A�C%' )) 

			String[] array = element.split("\t");
			if (array != null && array.length >= 3 && !array[1].equals("")) {
				String fieldTyp = array[0];
				String value = array[1];
				String ope = array[2];
//				System.out.println("fieldTyp:" + fieldTyp);
//				System.out.println("value:" + value);
//				System.out.println("ope:" + ope);

				//�������C��
				//				String field = dictionary.get(fieldTyp);
				//				fieldTyp�@�A�C�e�����A�A�C�e���A���[�J�[�A���[�J�[��
				if (fieldTyp.startsWith("�A�C�e��")) {
					prefix = "JAN.";
				}
				if (fieldTyp.startsWith("���[�J�[")) {
					prefix = "MK.";
				}
				if (fieldTyp.startsWith("�敪�P")) {
					prefix = "K1.";
				}
				if (fieldTyp.startsWith("�敪�Q")) {
					prefix = "K2.";
				}
				if (fieldTyp.startsWith("�敪�R")) {
					prefix = "K3.";
				}
				if (fieldTyp.startsWith("�敪�S")) {
					prefix = "K4.";
				}
				if (fieldTyp.startsWith("�敪�T")) {
					prefix = "K5.";
				}
				if (fieldTyp.startsWith("�敪�U")) {
					prefix = "K6.";
				}

				//				fieldTyp�@�A�C�e�����A�A�C�e���A���[�J�[�A���[�J�[��
				String field = "kCode";
				if (fieldTyp.endsWith("��")) {
					field = "kName";
				}

				if (field == null) {
					// �����Ŏ��������ł��Ă��Ȃ��E�E�Ȃ����H
					System.out.println(
							"#ERROR# fields Not Found=>" + fieldTyp + "<=");
					System.out.println("#=> �ȉ��̑I�����Ɋ܂܂�Ă��Ȃ���΂Ȃ�Ȃ�");
					List<String> keyList = new ArrayList(dictionary.keySet());
					for (String key : keyList) {
						System.out.println("# field:" + key);
					}
				}
				field = prefix + field;
				String condition = parseCond(field, value, ope);

				if (fieldTyp.startsWith("�A�C�e��")) {
					condition = "JAN.kCode IN (SELECT JAN.kCode FROM JAN where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("���[�J�[")) {
					condition = "JAN.kMK   IN (SELECT MK.kCode  FROM MK  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("�敪�P")) {
					condition = "JAN.kK1   IN (SELECT K1.kCode  FROM K1  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("�敪�Q")) {
					condition = "JAN.kK2   IN (SELECT K2.kCode  FROM K2  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("�敪�R")) {
					condition = "JAN.kK3   IN (SELECT K3.kCode  FROM K3  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("�敪�S")) {
					condition = "JAN.kK4   IN (SELECT K4.kCode  FROM K4  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("�敪�T")) {
					condition = "JAN.kK5   IN (SELECT K5.kCode  FROM K5  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("�敪�U")) {
					condition = "JAN.kK6   IN (SELECT K6.kCode  FROM K6  where( "
							+ condition + " )) ";
				}

//				System.out.println("condition:" + condition);

				if (!condition.equals("")) {
					if (!xWhere.equals(""))
						xWhere += relation;
					xWhere += "(" + condition + ")";
				}
			}
		}
		//		System.out.println("xWhere:" + xWhere);
		return xWhere;
	}

	// -------------------------------------------------------------------------
	// ���X�g�ɂ́hNAME_�A�T�q_1�h�̂悤�Ȍ`���̒l���͂���
	// => t1.kName like '%�A�T�q%'
	// -------------------------------------------------------------------------
	// TODO 20150515 ope��GUI���ڂƂ����ł̔��肪��v���Ȃ��\��������̂ŏC������
	private String parseCond(String field, String value, String ope) {
		//	System.out.println("��parseCond���@field>>" + field + " value>>" + value
		//	+ " ope>>" + ope);
		value = kyPkg.rez.Normalizer.removeInjection(value);
		String condition = "";
		if (field != null && !field.equals("")) {
			if (ope.equals("")) {
			} else if (ope.equals("1")) {
				condition = field + " like '%" + value + "%'";// ���܂܂��
			} else if (ope.equals("2")) {
				condition = field + " like '" + value + "%'";// �Ŏn�܂�
			} else if (ope.equals("3")) {
				condition = field + " = '" + value + "'";// �ƈ�v����
			} else if (ope.equals("4")) {
				condition = field + " not like '%" + value + "%'";// ���܂܂�Ȃ�
			} else if (ope.equals("5")) {
				condition = field + " <> '" + value + "'";// �ƈ�v���Ȃ�
			} else if (ope.equals(CONTAINS)) {
				condition = field + " like '%" + value + "%'";// ���܂܂��
			} else if (ope.equals(BEGINWITH)) {
				condition = field + " like '" + value + "%'";// �Ŏn�܂�
			} else if (ope.equals(EQUALS)) {
				condition = field + " = '" + value + "'";// �ƈ�v����
			} else if (ope.equals(NOT_CONTAINS)) {
				condition = field + " not like '%" + value + "%'";// ���܂܂�Ȃ�
			} else if (ope.equals(NOT_EQUALS)) {
				condition = field + " <> '" + value + "'";// �ƈ�v���Ȃ�
			}
		}
		//		System.out.println("���@parseCond�@ope:" + ope + ">>:" + condition);
		return condition;
	}
}
