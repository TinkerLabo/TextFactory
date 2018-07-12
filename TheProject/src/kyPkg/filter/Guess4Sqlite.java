package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import globals.ResControl;
import kyPkg.sql.ServerConnecter;
import kyPkg.task.Inf_ProgressTask;

public class Guess4Sqlite extends Guess {
	// ---------------------------------------------------------------------
	// �ŕp�A�ϓ��A�W���΍��̂�������v�Z����̂Ɏg�p�������̂ŁE�E�E���̕ӂ��l����
	// sqlite�ɂ͕W���΍����v�Z����֐����Ȃ��̂ŁE�E�E
	// �P.�S�̂̕��ς����߂�
	// sqlite> select avg(val1) from LOY1_HEAD;
	// 191.248341232228
	// �Q�D�S�̂̕��ςƂ̍��̂Q��̕��������Ƃ�
	// select key,(val1-191.248341232228) from LOY1_HEAD group by key;
	// �E�E�E��͂��ς����Ȃ̂ŁA�Ƃ肠�����h�r�`�l�őΉ����Ă������E�E
	// ---------------------------------------------------------------------
	private static final String Mysterious = "Mysterious";
	private static final String HALF_STRING = "�ݶ�";
	private static final String WIDE_STRING = "�S�p";
	private static final String NUMERIC = "����";
	private static final String DOTNUMERIC = "�����_�l";

	// ------------------------------------------------------------------------
	// �R���X�g���N�^�[
	// ------------------------------------------------------------------------
	public Guess4Sqlite() {
		super();
	}

	// ---------------------------------------------------------------------
	// analyzeIt
	// ---------------------------------------------------------------------
	@Override
	public void analyzeIt(String path, boolean headOpt) {
		Vector<Vector> matrix = super.getMatrix(path, 0, 100);
		if (matrix == null || matrix.size() == 0) {
			System.out.println("ERROR?! Empty Data?:" + path);
			return;
		}
		if (headOpt)
			headOpt = isHeaderExist(matrix);
		// ---------------------------------------------------------------------
		// ���ꂼ��̗�ɂ��Č^������s��
		// ---------------------------------------------------------------------
		autoDetect(matrix, headOpt);

		// ---------------------------------------------------------------------
		// ���`�r�p�k������
		// ---------------------------------------------------------------------
		create_Table_sql = create_Table_sql(names, typeFigures, tableName);
		create_Index_sql = create_Index_sql(names, typeFigures, tableName);
		drop_Table_sql = drop_Table_sql(names, typeFigures, tableName);
		drop_Index_sql = drop_Index_sql(names, typeFigures, tableName);
		truncate_Table_sql = truncate_Table_sql(names, typeFigures, tableName);

		i_List = insert_sql(names, tableName);
		r_List = select_sql(names, tableName);
		u_List = update_sql(names, tableName);
		d_List = delete_sql(names, tableName);

		// ---------------------------------------------------------------------
		// bcp�p�����[�^������
		// ---------------------------------------------------------------------
		bcp_param = bcpParam(names, types, figures, delimiter);
		// ---------------------------------------------------------------------
		// bcp�o�b�`������
		// ---------------------------------------------------------------------
		String dbName = "QPRDB1";
		String user = "qpr";
		String pass = "";
		bcp_bat = bcp_batch(ServerConnecter.CURRENT_SERVER, dbName, ResControl.D_QPR, user, pass, tableName,
				delimiter);

		// ---------------------------------------------------------------------
		// bcp�p�����[�^������
		// ---------------------------------------------------------------------
		sampleCodeList = sampleCode(getR_Sql());// Select �����E���Ă���
		// ---------------------------------------------------------------------
		// (SQLITE�Ǝ�)
		// ---------------------------------------------------------------------
		String dirName = parentDir + "/" + tableName + "/";
		// ---------------------------------------------------------------------
		// import�p�r�p�k���o�b�`������ăt�@�C���o�͂���
		// ---------------------------------------------------------------------
		String importPath = dirName + "/" + tableName + "_Import.sql";
		EzWriter.list2File(importPath, create_Import(create_Table_sql));
		String batPath = dirName + "/" + tableName + "_Import.bat";
		EzWriter.list2File(batPath, create_batch(importPath));
		// ---------------------------------------------------------------------
		// �ȈՍX�V�����̃o�b�`�V�X�e����������������
		// ---------------------------------------------------------------------
		String trnPath = dirName + "/" + tableName + "_Transcation.sql";
		EzWriter.list2File(trnPath, create_Transaction(tableName));
		String trnBatPath = dirName + "/" + tableName + "_Transcation.bat";
		EzWriter.list2File(trnBatPath, create_batch(trnPath));
		// ---------------------------------------------------------------------
		// �ȈՍX�V�����̃o�b�`�V�X�e����������������(�X�V�̃��J�o�[)
		// ---------------------------------------------------------------------
		String rcvPath = dirName + "/" + tableName + "_Recover.sql";
		EzWriter.list2File(rcvPath, create_Recover(tableName));
		String rcvBatPath = dirName + "/" + tableName + "_Recover.bat";
		EzWriter.list2File(rcvBatPath, create_batch(rcvPath));
		// ---------------------------------------------------------------------
		// �m�F�p�ɏo�͂��Ă��邪�s�v�����E�E
		// ---------------------------------------------------------------------
		String fileName = tableName + ".sql";
		EzWriter.list2File(parentDir + "/SQL/CREATE/TABLE/" + fileName,
				create_Table_sql);
		EzWriter.list2File(parentDir + "/SQL/CREATE/INDEX/" + fileName,
				create_Index_sql);
		EzWriter.list2File(parentDir + "/SQL/DROP/TABLE/" + fileName,
				drop_Table_sql);
		EzWriter.list2File(parentDir + "/SQL/DROP/INDEX/" + fileName,
				drop_Index_sql);
		EzWriter.list2File(parentDir + "/SQL/TRUNCATE/TABLE/" + fileName,
				truncate_Table_sql);

		EzWriter.list2File(parentDir + "/" + tableName + "_bcp.bat", bcp_bat);
		EzWriter.list2File(parentDir + "/FMT/" + tableName + ".fmt", bcp_param);

		EzWriter.list2File(dirName + "/insert/" + fileName, i_List);
		EzWriter.list2File(dirName + "/select/" + fileName, r_List);
		EzWriter.list2File(dirName + "/update/" + fileName, u_List);
		EzWriter.list2File(dirName + "/delete/" + fileName, d_List);
	}

	// ------------------------------------------------------------------------
	// autoDetect:�}�g���b�N�X�𕪐͂��āAsqlite�̃t�@�C����`�����import���������
	// ------------------------------------------------------------------------
	private void autoDetect(Vector<Vector> matrix, boolean headerOpt) {
		if (matrix == null || matrix.size() <= 0)
			return;
		// Header enumerate
		List rows = matrix.get(0);
		int maxCol = rows.size();
		String heads[] = new String[maxCol];
		String guess[] = new String[maxCol];
		Integer maxVal[] = new Integer[maxCol];
		Integer maxLen[] = new Integer[maxCol];
		Integer minLen[] = new Integer[maxCol];
		// --------------------------------------------------------------------
		// �w�b�_�[���̂̏���
		// --------------------------------------------------------------------
		if (headerOpt) {
			// �擪�s���w�b�_�[�Ƃ���ꍇ
			for (int col = 0; col < maxCol; col++) {
				heads[col] = rows.get(col).toString();
			}
		} else {
			// �f�t�H���g�̃w�b�_�[���𐶐�
			for (int col = 0; col < maxCol; col++) {
				heads[col] = PREFIX + String.valueOf(col + 1);
			}
		}
		// --------------------------------------------------------------------
		int skip = 1;
		Object obj;
		for (int col = 0; col < maxCol; col++) {
			guess[col] = Mysterious;
			maxVal[col] = -1;
			maxLen[col] = -1;
			minLen[col] = Integer.MAX_VALUE;
			int line = 0;
			for (List rowObj : matrix) {
				line++;
				if (line > skip && rowObj != null) {
					if (rowObj.size() > col) {
					}
					obj = rowObj.get(col);
					if (obj != null) {
						// String val = obj.toString().trim();// �X�y�[�X���܂܂Ȃ������ƂȂ�
						String val = obj.toString();
						int iVal = getMaxVal(val);
						if (maxVal[col] < iVal)
							maxVal[col] = iVal;
						int curLen = val.length();
						// if(col==1){
						// System.out.println("val:>"+val+"< curLen:"+curLen);
						// }
						if (maxLen[col] < curLen)
							maxLen[col] = curLen;
						if (minLen[col] > curLen)
							minLen[col] = curLen;
						// ���p�����񂩁H
						if (kyPkg.uRegex.Regex.isHalfWidthString(val)) {
							if (kyPkg.uRegex.Regex.isNumeric(val)) {
								guess[col] = NUMERIC;// ���l
							} else if (kyPkg.uRegex.Regex.isDotNumeric(val)) {
								guess[col] = DOTNUMERIC;// �����_�l
							} else {
								guess[col] = HALF_STRING;// ���p������
							}
						} else {
							if (kyPkg.uRegex.Regex.isFullWidthString(val)) {
								guess[col] = WIDE_STRING;// ����������
							} else {
								guess[col] = Mysterious;// �S�p���p����
							}
						}
					}
				}
			}
			String type = "";// �t�B�[���h�̌^
			if (guess[col].equals(WIDE_STRING)) {
				type = VARCHAR;
				// figure = String.valueOf(maxLen[col]);
			} else {
				if (maxLen[col] == minLen[col]) {
					if (maxLen[col] == 0) {
						// ���ׂċ���ۂȂ̂��Ǝv���i�J�n�ʒu�A�������w�肵�Ă��Ȃ��j
						type = VARCHAR;
					} else {
						type = CHAR;
						// figure = String.valueOf(maxLen[col]);
					}
				} else {
					if (guess[col].equals(NUMERIC)) {
						type = INT;
					} else if (guess[col].equals(DOTNUMERIC)) {
						type = FLOAT;
					} else {
						// ���p�S�p���݂̏ꍇ�Ȃ�
						type = VARCHAR;
						// figure = String.valueOf(maxLen[col]);
					}
				}
			}
			String figure = String.valueOf(maxLen[col]);// ���� 20140723 for Bcp
			String name = heads[col];
			String typeFigure = "";
			names.add(name);
			types.add(type);
			if (nSet.contains(type)) {
				if (type.equals(VARCHAR)) {
					Integer iFigure = Integer.parseInt(figure);
					figure = String.valueOf((iFigure * 2));
				}
				typeFigure = type + "(" + figure + ")";
			} else {
				typeFigure = type;
			}
			figures.add(figure);
			typeFigures.add(typeFigure);
		}

		// VARCHAR���w�肳��Ă����猅��{�ɂ���
		for (int i = 0; i < types.size(); i++) {
			String type = types.get(i);
		}
	}

	// ---------------------------------------------------------------------
	// create_batch �iSQLITE�j
	// ---------------------------------------------------------------------
	private List<String> create_batch(String sqlPath) {
		List<String> list = new ArrayList();
		String dbName = "test.Db";
		list.add("sqlite3 " + dbName + " \".read " + sqlPath + "\"");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// create_Import�@�iSQLITE�j
	// ---------------------------------------------------------------------
	private List<String> create_Import(List<String> c_List) {
		List<String> list = new ArrayList(c_List);
		// ---------------------------------------------------------------------
		// 20141212 �g�����U�N�V�����p�̃e�[�u���������������Ă݂��E�E�E
		// ---------------------------------------------------------------------
		List<String> create_Table_trn_sql;// �X�V���ǉ��p�f�[�^�ꎞ�i�[�p�e�[�u��
		List<String> create_Table_pre_sql;// �X�V�O���i�[�p�e�[�u��
		create_Table_trn_sql = create_Table_sql(names, typeFigures, tableName
				+ "_trn");
		create_Table_pre_sql = create_Table_sql(names, typeFigures, tableName
				+ "_pre");
		list.addAll(create_Table_trn_sql);
		list.addAll(create_Table_pre_sql);
		// ---------------------------------------------------------------------
		// import�������
		// ---------------------------------------------------------------------
		list.add(".separator \"" + delimiter + "\"");
		list.add(".import " + path + " " + tableName);
		return list;
	}

	private List<String> create_Transaction(String name) {
		List<String> list = new ArrayList();
		// import����
		list.add(".separator \"" + delimiter + "\"");
		list.add(".import " + path + " " + tableName + "_trn");
		// <1>���O����ɂ���
		list.add("delete from " + name + "_pre;");
		// <2>���O�ɓf���o��
		list.add("insert into " + name + "_pre select * from " + name
				+ " where " + name + ".fld_1 in (select Fld_1 from " + name
				+ "_trn);");
		// <3>����
		list.add("delete from " + name + " where " + name
				+ ".fld_1 in (select Fld_1 from " + name + "_trn);");
		// <4>�ǉ�����
		list.add("insert into " + name + " select * from " + name + "_trn;");
		// <5>���ʂ��o�͂���
		list.add(".output result.txt");
		list.add("select * from " + name + ";");
		return list;
	}
	private List<String> create_Recover(String name) {
		List<String> list = new ArrayList();
		//<1>���O�Ɠ������e�̂��̂�����
		list.add("delete from " + name + " where " + name + ".fld_1 in (select Fld_1 from " + name + "_pre);");
		//<2>���O����߂�
		list.add("insert into " + name + " select * from " + name + "_pre;");
		// <5>���ʂ��o�͂���
		list.add(".output result.txt");
		list.add("select * from " + name + ";");
		return list;
	}

	// ---------------------------------------------------------------------
	// Main
	// ---------------------------------------------------------------------
	public static void main(String[] argv) {
		test01();
	}

	public void template() {
		String dbDir = "C:/";
		String sql = list2String(r_List, "\n");
		String resultPath = dbDir + "result.txt";
		Inf_ProgressTask task = kyPkg.etc.CommonMethods.queryIsam2File(resultPath,
				sql, dbDir);
		new kyPkg.task.TaskWatcherNoGUI(task).execute();
	}

	public static void test01() {
		String inPath = "C:/loy1_Head.txt";
		inPath = "C:/Documents and Settings/EJQP7/�f�X�N�g�b�v/zapp/K6.txt";
		inPath = "C:/Documents and Settings/EJQP7/�f�X�N�g�b�v/zapp/loy1_Head.txt";
		boolean headerOption = false;
		new Guess4Sqlite().analyzeIt(inPath, headerOption);
	}
}
