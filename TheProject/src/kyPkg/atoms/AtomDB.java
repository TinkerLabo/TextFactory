package kyPkg.atoms;

import static kyPkg.sql.ISAM.CHARACTER_SET_OEM;
import static kyPkg.sql.ISAM.COL_NAME_HEADER_FALSE;
import static kyPkg.sql.ISAM.MAX_SCAN_ROWS_0;
import static kyPkg.sql.ISAM.SCHEMA_INI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import globals.ResControl;
import kyPkg.filter.EzWriter;
import kyPkg.sql.JDBC_Sqlite;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;
import kyPkg.uFile.YamlControl;
import kyPkg.util.InputDialog;

public class AtomDB extends JDBC_Sqlite implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LIMIT_PREFIX = "�Z���N�g���j�^�[��";
	public static final String LIM = ".lim";
	public static final String SQL = "sql�F";
	public static final String COMMENT1 = "comment1�F";
	public static final String COMMENT2 = "comment2�F";
	private static final String MON_LIMIT_COUNT = "MonLimitCount�F";
	private Atomics atomics = null;
	private static String CURRENT = globals.ResControl.getCurrentPath();
	private static String dbName = "QPR";
	private static String dbDir = FileUtil.getParent(CURRENT, true);

	//	private static String dbDir = kyPkg.uFile.FileUtil_.getParent(kyPkg.uFile.ResControl.getCurrentPath(), true);
	//	dbDir => "C:/@qpr/home/Personal/MonSets/"
	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public AtomDB() {
		super(dbName, dbDir);
		this.atomics = new Atomics(CURRENT);
	}

	public AtomDB(Atomics atomics) {
		super(dbName, atomics.getDirPath());
		this.atomics = atomics;
	}

	//���j�^�[���o�����������ɃR�[������Ă���
	public int query2file(String outPath, String sql) {
		String delimiter = "\t";
		writeSchema();
		int count = query2File(outPath, sql, delimiter);
		//		System.out.println("������query2Filex #debug20150811#");
		//		System.out.println("��  sql:" + sql);
		//		System.out.println("��  outPath:" + outPath);
		//		System.out.println("��  count:" + count);
		return count;
	}

	// TODO	�}���`�A���T�[�̏�����ǉ�����";
	public String genLimit(String saveAs, String[] array1, String[] array2) {
		System.out.println("Comment1:" + array1[0]);
		System.out.println("Comment2:" + array2[0]);
		System.out.println("formula1:" + array1[1]);
		System.out.println("formula2:" + array2[1]);
		String sql = "";
		if (!array1[1].equals("")) {
			sql += "(" + array1[1] + ")";// formula1
		}
		if (!array2[1].equals("")) {
			if (!sql.equals("")) {
				sql += " and ";
			}
			sql += "(" + array2[1] + ")";// formula2
		}
		if (!sql.equals("")) {
			sql = "select key_0 from current where " + sql;
		}
		List<String> infoList = new ArrayList();
		infoList.add(AtomDB.COMMENT1 + "\t" + array1[0]);
		infoList.add(AtomDB.COMMENT2 + "\t" + array2[0]);
		HashMap<String, String> infoMap = new HashMap();
		infoMap.put(AtomDB.COMMENT1, array1[0]);
		infoMap.put(AtomDB.COMMENT2, array2[0]);
		//20160613
		String message = saveLimFile(saveAs, sql, infoMap);
		// String message = saveLimFile(saveAs, sql, infoList, infoMap);
		return message;
	}

	//TODO	����𓮂�����lim�t�@�C�����č쐬�ł���񂾂Ǝv�����E�E�E�ǂ����낤�H�H
	//	public String saveLimFile(String saveAs, String sql, List<String> infoList,
	//			HashMap<String, String> infoMap) {
	public String saveLimFile(String saveAs, String sql,
			HashMap<String, String> infoMap) {
		String message = "";
		String outPath = ResControl.getMonSetsDir() + saveAs + AtomDB.LIM;
		System.out.println("##<AtomDB.saveLimFile>#######################");
		System.out.println("# 	sql:" + sql);
		System.out.println("# 	out Path:" + outPath);
		// �e�L�X�gDB��������Ɉ�v�����f�[�^���t�@�C���o�͂���iID�̂݁j
		int count = query2file(outPath, sql);
		if (count > 0) {
			message = "���Y�����ɊY������A" + count + "���̃f�[�^���o�͂��܂���";
			//			infoList.add(AtomDB.MON_LIMIT_COUNT + "\t" + count);
			//			infoList.add(AtomDB.SQL + "\t" + sql);
			infoMap.put(AtomDB.MON_LIMIT_COUNT, String.valueOf(count));
			infoMap.put(AtomDB.SQL, sql);
			//			new YamlControl(infoList).saveAs(outPath);
			new YamlControl(infoMap).saveAs(outPath);
		} else {
			message = "�y�Y������f�[�^�����݂��Ȃ��̂ŁA�����𒆒f���܂����B�z";
		}
		System.out.println("# 	message:" + message);
		System.out.println("###############################################");
		return message;
	}

	//-------------------------------------------------------------------------
	//	MonitorID�݂̂�ۑ�����ꍇ
	//-------------------------------------------------------------------------
	public static void saveAsMonitorSelecter(List<String> dstList) {
		String timeStamp = kyPkg.uDateTime.DateCalc.getTimeStamp();
		String title = "���j�^�[��������Ƃ��ĕۑ�";
		String msg = "��������ɖ��O��t���Ă�������";
		String name = "����@" + timeStamp;
		InputDialog dialog = new InputDialog(null, title, true, msg, name);
		dialog.setVisible(true);
		name = dialog.getValue();
		String outPath = ResControl.getMonSetsDir() + name.trim() + AtomDB.LIM;
		// XXX list2File�Ƀe�L�X�g�t�B���^�[���I�v�V�����Ŏw��ł���悤�ɂ�����
		ListArrayUtil.list2File(outPath, dstList);

		dialog.setVisible(false);
		dialog.dispose();
		dialog = null;
		//		super.closeDialog();
		JOptionPane.showMessageDialog(null, new JLabel(outPath + "�ɕۑ����܂����B"),
				"Information...", JOptionPane.INFORMATION_MESSAGE);
	}

	public static String genComment(String[] array1, String[] array2) {
		String comment1 = array1[0].trim();
		String comment2 = array2[0].trim();
		String comment = comment1 + comment2;
		if (!comment1.equals("") && !comment2.equals(""))
			comment = comment1 + "�A����" + comment2;
		comment = comment.replaceAll("�B", "");
		comment = comment.replaceAll(" ", "");
		if (comment.equals("")) {
			String timeStamp = kyPkg.uDateTime.DateCalc.getTimeStamp();
			comment = getDefaultSaveAsxxx();
		}
		return comment;
	}

	public static String getDefaultSaveAsxxx() {
		String timeStamp = kyPkg.uDateTime.DateCalc.getTimeStamp();
		return LIMIT_PREFIX + timeStamp;
	}

	// ------------------------------------------------------------------------
	// �f/�s��Ԃ��i�W�v�ꐔ�Ƃ��Ďg�p����j�@
	// ------------------------------------------------------------------------
	public static Integer[] getGT(String field, String srcPath, int range,
			int occ) {
		HashMap<String, Integer> hMap = AtomDB.getGTmap(field, srcPath, occ);
		// �����ӁI�I�o���������ڂ̂݁A�܂�\�����O�͂łĂ��Ȃ��I�I
		// HashMap���烊�X�g�ɕϊ�����
		List list = new ArrayList();
		for (int i = 1; i <= range; i++) {
			String key = String.valueOf(i);
			Integer value = hMap.get(key);
			// XXX NA����ъO��l(���̑�)���l������K�v����E�E�ENA�͋�l�ƂȂ��Ă���͂�
			// System.out.println(" key> " + key + " value>" + value);
			list.add(value);
		}
		// ���X�g��z��ɕϊ�
		Integer[] array = (Integer[]) list.toArray(new Integer[list.size()]);
		// �����ł��ׂ�null���Ԃ��Ă��Ă��܂��Ă���H�Ȃ����ˁH
		return array;
	}

	// -------------------------------------------------------------------------
	// �f/�s��Ԃ��i�W�v�ꐔ�Ƃ��Ďg�p����j
	// �w�肳�ꂽ���ڂ��Ƃ̃J�E���g���n�b�V���}�b�v�ŕԂ�
	// -------------------------------------------------------------------------
	private static HashMap<String, Integer> getGTmap(String field,
			String srcPath, int occ) {
		String sql = "";
		if (occ == 1) {
			// �s�V���O���A���T�[�̏ꍇ�t
			sql = singleSqlGen(field);
		} else {
			// �s�}���`�A���T�[�̏ꍇ�t���m�`����
			sql = multiSqlGen(field, occ);
		}
		// --------------------------------------------------------------------
		// SELECT MAP_24,COUNT(*) FROM current GROUP BY MAP_24 ORDER BY MAP_24
		// srcPath:C:/@qpr/home/238881000301/tran/current.txt
		// --------------------------------------------------------------------
		System.out.println("####�@�f/�s��Ԃ��i�W�v�ꐔ�Ƃ��Ďg�p����j####�@");
		//		System.out.println("##�@srcPath:" + srcPath);
		//		System.out.println("##�@sql:" + sql);
		List<List> matrix = AtomDB.executeQueryMatrix(srcPath, sql);
		HashMap<String, Integer> hmap = new HashMap();
		String key = "";
		int count = 0;
		for (List<Object> list : matrix) {
			key = (String) list.get(0);// �ŏ��̃J�����̓}�b�v����
			count = (Integer) list.get(1);// 2�Ԗڂ̓J�����̃J�E���g
			// System.out.println("GT key> " + key + " value>" + count);
			hmap.put(key, count);
		}
		return hmap;
	}

	// -------------------------------------------------------------------------
	// �s�}���`�A���T�[�̏ꍇ�t���m�`����
	// �g�p�၄�@String sql = sqlGen("MAP_4", 22);
	// -------------------------------------------------------------------------
	public static String multiSqlGen(String field, int occ) {
		StringBuffer buff = new StringBuffer();
		buff.append(
				"SELECT 'NA',count(*) FROM current where " + field + " =''");
		for (int i = 1; i <= occ; i++) {
			buff.append(" union ");
			buff.append("SELECT '" + i + "',count(*) FROM current where substr("
					+ field + "," + i + ",1)='1'");
		}
		return buff.toString();
	}

	// -------------------------------------------------------------------------
	// �s�V���O���A���T�[�̏ꍇ�t
	// -------------------------------------------------------------------------
	public static String singleSqlGen(String field) {
		String sql = "SELECT " + field + ",COUNT(*) FROM current GROUP BY "
				+ field + " ORDER BY " + field + "";
		return sql;
	}

	//list���Ɠ����ɃX�L�[�}�������o���Ă���
	//���j�^�[�����f�[�^�𐶐�����Ƃ��ɃR�[������Ă���H
	public static List<List> executeQueryMatrix(String srcPath, String sql) {
		//		System.out.println("������executeQueryMatrix");
		//		System.out.println("��  srcPath:" + srcPath);
		//		System.out.println("��  sql:" + sql);
		AtomDB ins = new AtomDB(new Atomics(srcPath));
		ins.writeSchema();
		return ins.query2Matrix(sql);
	}

	public void export(String path, List headList) {
		EzWriter writer = new EzWriter(path);
		writer.open();
		writer.write("Hello!!");
		writer.close();
	}

	// ---------------------------------------------------------------
	// Schema.ini for ISAM
	// ---------------------------------------------------------------
	private void writeSchema() {
		String format = atomics.getFormat();
		//		System.out.println("������ Atom_DB.writeSchema()������  format:" + format);
		boolean debug = false;
		String dirPath = atomics.getDirPath();
		String name = atomics.getName();
		String Path = dirPath + SCHEMA_INI;
		if (debug) {
			System.out.println("# @writeSchema # dir:" + dirPath);
			System.out.println("# @writeSchema # name:" + name);
			System.out.println("# @writeSchema # Path:" + Path);
		}
		List list = new ArrayList();
		list.add("[" + name + ".txt]                   ");
		//		list.add("ColNameHeader=False        ");
		list.add(COL_NAME_HEADER_FALSE);

		// 20150821 ������ǂݍ��߂Ȃ��@�������烂�j�^�[���o���𔲂��o���Ƃ��̓^�u��؂��
		//		����ȊO�̎��̓J���}��؂�ɂȂ��Ă���悤���E�E�E�ǂ�����Γ���ł��邩�l����E�E�E
		String currentTxt = ResControl.getCurrentPath();
		list.add("Format=" + format + "        ");
		//		list.add("Format=CSVDelimited        ");  //20150820�@�T���v�������E���Ȃ������̂ŏC��
		//				list.add("Format=TABDelimited        ");//20150821 �߂��Ă݂�E�E�E
		//		list.add("MaxScanRows=0              ");
		//		list.add("CharacterSet=OEM           ");
		list.add(MAX_SCAN_ROWS_0);
		list.add(CHARACTER_SET_OEM);
		HashMap<String, String> hMap = new HashMap();
		int[] keyElements = atomics.getKeyElement();
		if (keyElements != null) {
			for (int i = 0; i < keyElements.length; i++) {
				int seq = keyElements[i];
				hMap.put(String.valueOf(seq), "Col" + (seq + 1) + "=\"KEY_" + i
						+ "\"   Char Width 8  ");
			}
		}
		int[] mapElements = atomics.getMapElement();
		if (mapElements != null) {
			for (int i = 0; i < mapElements.length; i++) {
				int seq = mapElements[i];
				hMap.put(String.valueOf(seq), "Col" + (seq + 1) + "=\"MAP_" + i
						+ "\"   Char Width 3  ");
			}
		}
		int[] numElements = atomics.getNumElement();
		if (numElements != null) {
			for (int i = 0; i < numElements.length; i++) {
				int seq = numElements[i];
				hMap.put(String.valueOf(seq), "Col" + (seq + 1) + "=\"NUM_" + i
						+ "\"   Integer Width 3 ");
			}
		}
		for (int i = 0; i < hMap.size(); i++) {
			String seq = String.valueOf(i);
			String val = hMap.get(seq);
			if (debug)
				System.out.println("# @writeSchema # val=>" + val);
			list.add(val);
		}
		ListArrayUtil.list2File(Path, list);
		// // �@�X�L�[�}��`�Ɠ����e�[�u��create���邓�������𐶐�����(sqlite�p)
		// new Schema2Sql(dirPath, "create.sql", "import.sql").execute();
		//---------------------------------------------------------------------
		// ISAM����C���|�[�g
		//---------------------------------------------------------------------
		importFromIsam();//ATOM_DB
	}

}
