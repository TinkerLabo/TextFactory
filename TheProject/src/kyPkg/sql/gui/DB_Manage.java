package kyPkg.sql.gui;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

//-------+---------+---------+---------+---------+---------+---------+---------+
/**
 * SQL_Login
 * 
 * @(#)SQL_Login.java 1.11 04/09/15 Copyright 2004 Ken Yuasa. All rights
 *                    reserved. �ˑ��N���X SQL_GVDlg
 * @author Ken Yuasa
 * @version 1.00 04/09/15
 * @since 1.3
 */
// -------+---------+---------+---------+---------+---------+---------+---------+
// XXX 2012/06/20 GUI�Ƃ��̑��̋@�\�𕪗�����

// ---------------------------------------------------------------------------
// SQL_Login �N���X
// User,Pass,JURL���悹���p�l���̐��`
// ---------------------------------------------------------------------------
// XXX�@�e��h���C�o�̃_�E�����[�h����񋓂��Ƃ��ׂ����낤�E�E�E
// http://dev.mysql.com/downloads/connector/j/3.1.html
public abstract class DB_Manage extends JTabbedPane {
	public static final String EQUAL = "=";
	public static final String NOT_EQUAL = "<>";
	public static final String GT = ">";
	public static final String LT = "<";
	public static final String GE = ">=";
	public static final String LE = "<=";
	public static final String LIKE = "Like";
	public static final String LIKE_XXXQ = "Like XXX?";
	public static final String LIKE_QXXX = "Like ?XXX";
	public static final String IN = "IN";
	public static final String NOT_IN = "NOT IN";
	public static final String BETWEEN = "Between";
	// -------------------------------------------------------------------------
	public static final String SUM = "���v";
	public static final String MIN = "�ŏ�";
	public static final String MAX = "�ő�";
	public static final String AVG = "����";
	public static final String STDEV = "�W���΍�";
	public static final String VARP = "���U";
	public static final String COUNT = "�J�E���g";

	// -------------------------------------------------------------------------
	// �O���[�v�W�v���r�p�k���쐬
	// String pStr �Ώۃp�����[�^ "fld:���v"�̂悤�Ȍ`
	// char pdLm ��؂蕶�� ��L�̂悤�ȏꍇ��':'
	// �g�p�� pStr = grpCalc(pStr,'@');
	// ��FORMAT�ł��Ȃ����H
	// -------------------------------------------------------------------------
	public static String grpCalc(String pStr, char pdLm) {
		String wRtn = "";
		String wPre;
		String wSuf;
		int pos = pStr.indexOf(pdLm); // 0�Ԗڂ��J�n�����
		wPre = pStr.substring(0, pos).trim();
		wSuf = pStr.substring(pos + 1).trim();
		if (wSuf.equals("")) {
		} else if (wSuf.equals(SUM)) {// "���v"
			wRtn = "Sum(" + wPre + ") AS SumOf" + wPre;
		} else if (wSuf.equals(MIN)) {// "�ŏ�"
			wRtn = "Min(" + wPre + ") AS MinOf" + wPre;
		} else if (wSuf.equals(MAX)) {// "�ő�"
			wRtn = "Max(" + wPre + ") AS MaxOf" + wPre;
		} else if (wSuf.equals(AVG)) {// "����"
			wRtn = "Avg(" + wPre + ") AS AvgOf" + wPre;
		} else if (wSuf.equals(STDEV)) {
			wRtn = "StDevP(" + wPre + ") AS StDevpOf" + wPre;
		} else if (wSuf.equals(VARP)) {// "���U"
			wRtn = "VarP(" + wPre + ") AS VarpOf" + wPre;
		} else if (wSuf.equals(COUNT)) {// "�J�E���g"
			wRtn = "Count(" + wPre + ") AS CountOf" + wPre;
		}
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// �p�����[�^���������o�r�p�k���쐬����
	// -------------------------------------------------------------------------
	public static String createCond(String field, String expr, String val1,
			String val2, int sqlType) {
		String condStr;
		String message = "";
		if (val1.trim().equals("")) {
			val1 = "NULL";
			message = "\'���̒l���ݒ肳��Ă������̂Ńk���l��ݒ肵�܂���";
			JOptionPane.showMessageDialog(null, new JLabel(message),
					"Warning...", JOptionPane.WARNING_MESSAGE);
		}
		if (isNumType(sqlType)) { // ���l�̏ꍇ
			if (val1.trim().equals(""))
				val1 = "0";
			if (val2.trim().equals(""))
				val2 = "0";
			if ((isNumeric(val1) == false) || (isNumeric(val2) == false)) {
				message = "��r����l�����l�ł͂���܂���I\n���l���w�肵�ĉ������B";
				JOptionPane.showMessageDialog(null, new JLabel(message),
						"Warning...", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			if (expr.equals(LIKE) || expr.equals(LIKE_XXXQ)
					|| expr.equals(LIKE_QXXX)) {
				message = "��r����l�����l�Ȃ̂�Like���Z�q�͎g���܂���I\n���̉��Z�q���w�肵�ĉ������B";
				JOptionPane.showMessageDialog(null, new JLabel(message),
						"Warning...", JOptionPane.WARNING_MESSAGE);
				return null;
			}
		} else { // �����̏ꍇ
			val1 = StripChar(val1, "\'"); // �S�~����� �������I�I���֐�
			val2 = StripChar(val2, "\'");
			if (expr.equals(LIKE)) {
				val1 = "\'%" + val1 + "%\'";
			} else if (expr.equals(LIKE_XXXQ)) {
				val1 = "\'" + val1 + "%\'";
			} else if (expr.equals(LIKE_QXXX)) {
				val1 = "\'%" + val1 + "\'";
			} else {
				if (val1.toUpperCase().equals("NULL")) {
					val1 = "NULL";
				} else {
					val1 = "\'" + val1 + "\'";
				}
				if (val2.toUpperCase().equals("NULL")) {
					val2 = "NULL";
				} else {
					val2 = "\'" + val2 + "\'";
				}
			}
		}
		if (expr.equals(BETWEEN)) {
			condStr = " " + field + " Between " + val1 + " and " + val2 + " ";
		} else if (expr.equals(LIKE)) {
			condStr = " " + field + " Like " + val1 + " ";
		} else if (expr.equals(LIKE_XXXQ)) {
			condStr = " " + field + " Like " + val1 + " ";
		} else if (expr.equals(LIKE_QXXX)) {
			condStr = " " + field + " Like " + val1 + " ";
		} else if (expr.equals(IN)) {
			condStr = " " + field + " IN (" + val1 + ") ";
		} else if (expr.equals(NOT_IN)) {
			condStr = " " + field + " NOT IN (" + val1 + ") ";
		} else {
			condStr = " " + field + " " + expr + " " + val1 + " ";
		}
		return condStr;
	}

	// -------------------------------------------------------------------------
	// �s�v�ȕ��������苎��
	// -------------------------------------------------------------------------
	public static String StripChar(String rsObjName, String rsChar) {
		// trim(); ??
		String wStr = rsObjName.replaceAll(rsChar, "");
		return wStr;
	}

	// -------------------------------------------------------------------------
	// ���l�ł��邩�ǂ������肷��
	// -------------------------------------------------------------------------
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	// #########################################################################
	// ## addUnique2Model �e�[�u�����f���Ɂi���łɁj�܂܂�Ă��Ȃ��l�݂̂�ǉ�����
	// #########################################################################
	public static void addUq2Model(DefaultListModel model, String val) {
		boolean xSw = false;
		int sz = model.getSize();
		ck: for (int i = 0; i < sz; i++) {
			String wElement = ((String) model.getElementAt(i));
			if (wElement.equals(val)) {
				xSw = true;
				break ck;
			}
		}
		if (xSw == false) {
			model.addElement(val);
		}
	}

	// #########################################################################
	// ## �t�B�[���h�̒l�����ł��邩���肷��
	// #########################################################################
	public static boolean isNumType(int sqlType) {
		switch (sqlType) {
		case java.sql.Types.NUMERIC:
		case java.sql.Types.DECIMAL:
		case java.sql.Types.INTEGER:
		case java.sql.Types.SMALLINT:
		case java.sql.Types.FLOAT:
		case java.sql.Types.REAL:
		case java.sql.Types.DOUBLE:
			return true;
		case java.sql.Types.CHAR:
		case java.sql.Types.VARCHAR:
			break;
		default:
		}
		return false;
	}

	private static String concat(List<String> list, String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (String element : list) {
			if (buf.length() > 0)
				buf.append(delimiter);
			buf.append(element);
		}
		return buf.toString();
	}

	// -------------------------------------------------------------------------
	// ���`�r�p�k��Ԃ�
	// db�̎�ނɂ���Ă������̏��������Ⴄ�E�E�E
	// -------------------------------------------------------------------------
	public static String genSql01(String template, String table,
			List<String> fields, List<String> orders, String where1,
			String where2, List<String> grpCalcs, boolean countOption,
			boolean groupOption) {
		String tmpSql = "";
		if (grpCalcs.size() > 0)
			groupOption = true;
		// ---------------------------------------------------------------------
		// Order
		// ---------------------------------------------------------------------
		String sOrder = "";
		for (String element : orders) {
			if (sOrder.length() > 0)
				sOrder += ",";
			sOrder += element;
			// ��܊֌W�𒲐�����
			if (!fields.contains(element))
				fields.add(element);
		}
		if (sOrder.equals("") == false)
			sOrder = " ORDER BY " + sOrder;
		// ---------------------------------------------------------------------
		// Group
		// ---------------------------------------------------------------------
		String sGroup = "";
		String sCalcs = "";
		String wField = concat(fields, ",");
		if (countOption) {
			fields.add("count(*)");
		} else {
			sCalcs = concat(grpCalcs, ",");
			if (!sCalcs.equals(""))
				sCalcs = "," + sCalcs;
		}
		if (groupOption || countOption) {
			if (fields.size() > 0)
				sGroup = " GROUP BY " + wField;
		}
		// ---------------------------------------------------------------------
		// Field
		// ---------------------------------------------------------------------
		String sField = concat(fields, ",");
		if (sField.equals("")) {
			sField = "*";
		} else {
			sField += sCalcs;
		}

		// ---------------------------------------------------------------------
		String sWhere = "";
		if (where1.equals("") == false) {
			if (where2.equals("") == false) {
				sWhere = " where((" + where1 + ") and (" + where1 + "))";
			} else {
				sWhere = " where(" + where1 + ")";
			}
		} else {
			if (where2.equals("") == false) {
				sWhere = " where(" + where2 + ")";
			}
		}
		// ---------------------------------------------------------------------
		if (template.equals("SELECT *")) {
			tmpSql = "SELECT " + sField + " FROM " + table + sWhere + sGroup
					+ sOrder;
		} else if (template.equals("SELECT count(*)")) {
			tmpSql = "SELECT count(*) as cnt FROM " + table + sWhere + sGroup
					+ sOrder;
		} else if (template.equals("SELECT 100")) {
			tmpSql = "SELECT * from(SELECT " + sField + " FROM " + table
					+ sWhere + sGroup + sOrder + ") where rownum < 100";
		} else if (template.equals("SELECT")) {
			tmpSql = "SELECT [LIMIT n m] [DISTINCT] \n"
					+ "{ selectExpression | table.* | * } [, ... ] \n"
					+ "[INTO [CACHED|TEMP|TEXT] newTable] \n" + "FROM " + table
					+ " \n" + "[WHERE Expression]  \n"
					+ "[ORDER BY selectExpression [{ASC | DESC}] [, ...] ]  \n"
					+ "[GROUP BY Expression [, ...] ]  \n"
					+ "[UNION [ALL] selectStatement] ";
		} else if (template.equals("INSERT")) {
			// tmpSql = "INSERT INTO " + wTbl + " [ (column [,...] ) ] \n" +
			// "{ VALUES(Expression [,...]) | SelectStatement } ";
			tmpSql = "INSERT INTO " + table + " SELECT * FROM TMP_TABLE;\n"
					+ "INSERT INTO " + table
					+ " VALUES(1,'ENGLISH ','Hello World');\n" + "INSERT INTO "
					+ table + " VALUES(2,'JAPANESE','����ɂ���' );  ";
		} else if (template.equals("UPDATE")) {
			tmpSql = "UPDATE " + table + " SET column = Expression [, ...]  \n"
					+ "[WHERE Expression] ";
		} else if (template.equals("DELETE")) {
			tmpSql = "DELETE FROM " + table + " [WHERE Expression] ";
		} else if (template.equals("CREATE TABLE")) {
			// tmpSql = "CREATE [TEMP] [CACHED|MEMORY|TEXT] TABLE " + wTbl + "
			// \n" +
			// "( columnDefinition [, ...] ) ";
			tmpSql = "CREATE TABLE " + table + " (       \n"
					+ "  SEQ      INTEGER NOT NULL, \n"
					+ "  LANGUAGE VARCHAR(50),      \n"
					+ "  MESSAGE  VARCHAR(100),     \n"
					+ "  PRIMARY  KEY(SEQ)     );     ";
		} else if (template.equals("DROP TABLE")) {
			tmpSql = "DROP TABLE " + table + " ";
		} else if (template.equals("CREATE INDEX")) {
			tmpSql = "CREATE [UNIQUE] INDEX index ON  \n" + table
					+ " (column [, ...]) ";
		} else if (template.equals("DROP INDEX")) {
			tmpSql = "DROP INDEX table.index";
		} else if (template.equals("SET")) {
			tmpSql = "AUTOCOMMIT { TRUE | FALSE } \n"
					+ "IGNORECASE { TRUE | FALSE } \n" + "LOGSIZE size \n"
					+ "MAXROWS maxrows \n" + "PASSWORD password \n"
					+ "READONLY { TRUE | FALSE } \n"
					+ "REFERENTIAL_INTEGRITY { TRUE | FALSE } \n"
					+ "TABLE table READONLY { TRUE | FALSE } \n"
					+ "TABLE table SOURCE \"file\" [DESC] \n"
					+ "WRITE_DELAY { TRUE | FALSE } ";
		} else if (template.equals("@TEXT_T Create")) {
			tmpSql = "CREATE TEXT TABLE TOKAI(B VARCHAR,XA1 VARCHAR,XB1 VARCHAR,XC1 VARCHAR)";
		} else if (template.equals("@TEXT_T Set")) {
			tmpSql = "SET TABLE TOKAI SOURCE \"toukai.csv;ignore_fist=true\"";
		} else if (template.equals("@TEXT_T drop")) {
			tmpSql = "DROP TABLE TOKAI";
		} else if (template.equals("@TEXT_T toText")) {
			tmpSql = "SELECT * INTO TEXT sql2tex FROM TOKAI";
		} else if (template.equals("SHUTDOWN")) {
			tmpSql = "SHUTDOWN [COMPACT|IMMEDIATELY] ";
		} else {
			// System.out.println("????"+jcSql);
		}
		return tmpSql;
	}

	public static String sql_Mup(String table_L, String table_R,
			String relation, String key_L, String key_R, List<String> fields,
			List<String> orders, String where1, String where2,
			List<String> grpCalcs, boolean countOption, boolean groupOption) {
		String tmpSql = "";
		// ---------------------------------------------------------------------
		// Order
		// ---------------------------------------------------------------------
		String sOrder = "";
		for (String element : orders) {
			if (sOrder.length() > 0)
				sOrder += ",";
			sOrder += element;
			// ��܊֌W�𒲐�����
			if (!fields.contains(element))
				fields.add(element);
		}
		if (sOrder.equals("") == false)
			sOrder = " ORDER BY " + sOrder;
		// ---------------------------------------------------------------------
		// Group
		// ---------------------------------------------------------------------
		String sGroup = "";
		String sCalcs = "";
		String wField = concat(fields, ",");
		if (groupOption || countOption) {
			if (fields.size() > 0)
				sGroup = " GROUP BY " + wField;
		}
		if (countOption) {
			fields.add("count(*)");
			System.out.println("##############>>> count(*)");
		} else {
			sCalcs = concat(grpCalcs, ",");
		}
		// ---------------------------------------------------------------------
		// Field
		// ---------------------------------------------------------------------
		String sField = concat(fields, ",");
		sField = sField.replaceAll("L\\|", "L.");
		sField = sField.replaceAll("R\\|", "R.");
		if (sField.equals("")) {
			sField = "*";
		} else {
			sField += sCalcs;
		}
		// ---------------------------------------------------------------------
		String sWhere = "";
		if (where1.equals("") == false) {
			if (where2.equals("") == false) {
				sWhere = " where((" + where1 + ") and (" + where1 + "))";
			} else {
				sWhere = " where(" + where1 + ")";
			}
		} else {
			if (where2.equals("") == false) {
				sWhere = " where(" + where2 + ")";
			}
		}
		if (relation.contains("LEFT")) {
			sWhere = " where(R." + key_R + " = NULL)";
		} else if (relation.contains("RIGHT")) {
			sWhere = " where(L." + key_L + " = NULL)";
		}
		// ---------------------------------------------------------------------
		tmpSql = "SELECT " + sField + " FROM " + table_L + " as L INNER JOIN "
				+ table_R + " as R ON L." + key_L + " = R." + key_R + sWhere
				+ sGroup + sOrder;
		return tmpSql;
	}

	public static String sql_Crs(String table_L, String key_L, String key_R) {
		String tmpSql = "";
		// ---------------------------------------------------------------------
		// Group
		// ---------------------------------------------------------------------
		String sCalcs = "";
		// ---------------------------------------------------------------------
		// Field
		// ---------------------------------------------------------------------
		String sField = "";
		sField = sField.replaceAll("L\\|", "L.");
		sField = sField.replaceAll("R\\|", "R.");
		if (sField.equals("")) {
			sField = "*";
		} else {
			sField += sCalcs;
		}
		// ---------------------------------------------------------------------
		tmpSql = "TRANSFORM count(*) select " + key_L + " FROM " + table_L
				+ " GROUP BY " + key_L + " PIVOT " + key_R;
		return tmpSql;
	}

	// public static integer MsgBox(String pMsg,Integer Style,String pTitle){
	// JOptionPane.showMessageDialog(null,pMsg,pTitle,JOptionPane.INFORMATION_MESSAGE);
	// }
}
