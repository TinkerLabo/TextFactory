package kyPkg.filter;

import static kyPkg.util.KUtil.array2String;
import static kyPkg.util.KUtil.list2String;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kyPkg.sql.JDBC_Sqlite;
import kyPkg.task.Abs_ProgressTask;

public class HML2Sqlite extends Abs_ProgressTask {
	public static final String TABLE_NAME = "WORK";
	public static final String DB_NAME = "monDB";
	public static final String MONSEL_DIR = "monsel";
	//-------------------------------------------------------------------------
	private static final String regexNum = "^[-]?[0-9]*[.]?[0-9]+$";//���K�\���A���l�����񂩂ǂ���
	private static final String delimiter = "\t";
	private static final String SEQ = "������";
	private static final String ASTA = "��";
	private String dbDir = "c:/temp/";
	private String tableName = TABLE_NAME;
	private Inf_iClosure inClosure = null; // 	���̓N���[�W��
	private Inf_oClosure outClosure = null; // 	�o�̓N���[�W��
	private List<String> headerList; //	���[�̃w�b�_�[�����i�Q�l�\�����j
	private List<String> nameList;

	public List<String> getNameList() {
		return nameList;
	}

	public List<String> getHeaderList() {
		return headerList;
	}

	public String getHeaderString() {
		StringBuffer buf = new StringBuffer();
		for (String element : headerList) {
			buf.append(element);
			buf.append("\n");
		}
		return buf.toString();
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^                                                                                                                            2015-06-03 yuasa
	// -------------------------------------------------------------------------
	//TODO sqlite3�Ƀp�X���ʂ��Ă��Ȃ��ƃX�N���v�g�����s�ł��Ȃ��̂ł͂Ȃ����H�t���p�X�Ŏw�肷��K�v�����邩���m��Ȃ�
	// -------------------------------------------------------------------------
	public HML2Sqlite(String dbDir, String tableName, String inPath) {
		//	public HML2Sqlite(String tableName, String inPath) {
		super();
		this.dbDir = dbDir;
		this.tableName = tableName;
		this.inClosure = new EzReader(inPath);
		String outPath = dbDir + tableName + ".txt";
		this.outClosure = new EzWriter(outPath);
	}

	public long getWriteCount() {
		return outClosure.getWriteCount();
	}

	public void setDelimiter(String delimiter) {
		this.outClosure.setDelimiter(delimiter);
	}

	@Override
	public void execute() {
		super.start("Flt_Base", 2048);
		loop();// loop
		super.stop();// ����I��
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop() {
		long lCount = -1;
		inClosure.open();
		inClosure.setDelimiter(delimiter);
		String[] cells = null;
		char[] ch;
		HashMap<Integer, String> colNameMap = new HashMap();
		headerList = new ArrayList();

		while ((cells = inClosure.readSplited()) != null) {
			lCount++;
			ch = cells[0].toCharArray();
			switch (ch[0]) {
			case '@':
				System.out.println("@@" + cells[0]);
				colNameMap = new HashMap();
				headerList = new ArrayList();
				outClosure.close();
				outClosure.open();
				break;
			case 'A':
				String head = array2String(cells, delimiter);
				headerList.add(head);
				//tab��؂��String�̃��X�g�ŗǂ��Ǝv���E�E�E
				break;
			case 'B':
				for (int col = 1; col < cells.length; col++) {
					String name = colNameMap.get(col);
					if (name == null)
						name = "";
					//�@������ϊ��E�E�E
					name = name.replaceAll("ճ�������", "�L���l��");
					name = name.replaceAll("�ݶ޸_100������", "�S�l����w�����z");
					name = name.replaceAll("ֳخ�_100������", "�S�l����w���e��");
					name = name.replaceAll("��خ�_100������", "�S�l����w������");
					name = name.replaceAll("��ƭ�������", "�w�����@");
					name = name.replaceAll("�ݶ޸", "�w�����z");
					name = name.replaceAll("ֳخ�", "�w���e��");
					name = name.replaceAll("��خ�", "�w������");
					name = name.replaceAll("��ƭ������", "�w���l��");
					name = name.replaceAll("����", "�w����");
					name = name.replaceAll("SEQ", SEQ);
					name = (name + cells[col]).trim();
					if (!name.equals("")) {
						colNameMap.put(col, name);
					}
				}
				//map
				break;
			case 'C':
				boolean writeFlag = true;
				List<String> recList = new ArrayList();
				for (int col = 1; col < cells.length; col++) {
					String colName = colNameMap.get(col);
					String cell = cells[col].trim();
					if (colName == null) {
						colName = "";
					}
					if (!colName.equals("")) {
						if (colName.startsWith(ASTA)) {
							if (colName.equals(SEQ)) {
								//								System.out.println("SEQ =>" + cell);
								if (cell.trim().equals("TOTAL")) {
									System.out
											.println("reject TOTAL =>" + cell);
									writeFlag = false; //�l��TOTAL�Ȃ炱�̍s���o�͂��Ȃ�
								}
							}
							if (!cell.matches(regexNum)) {
								cell = "0";//���l�łȂ���΂O�Ƃ���
							}
						} else {
							//	������s
						}
						recList.add(cell);
					}
				}
				if (writeFlag) {
					String rec = list2String(recList, delimiter);
					outClosure.write(rec);
				}
				break;
			default:
				break;
			}
		}
		inClosure.close();
		outClosure.close();
		List<Integer> keys = new ArrayList(colNameMap.keySet());
		nameList = new ArrayList();//��������
		List<String> createSqlList = new ArrayList();//
		List<String> importSqlList = new ArrayList();//
		//---------------------------------------------------------------------
		Collections.sort(keys);
		String comma = "";
		String dataPath = dbDir + tableName + ".txt";
		//---------------------------------------------------------------------
		importSqlList.add(".separator \"\t\"");
		importSqlList.add(".import \"" + dataPath + "\" " + tableName + "");
		//---------------------------------------------------------------------
		createSqlList.add("drop table if exists " + tableName + ";");
		createSqlList.add("create table " + tableName + " (");
		for (int seq = 0; seq < keys.size(); seq++) {
			Integer key = keys.get(seq);
			String colName = colNameMap.get(key);
			nameList.add(colName);
			String fld = "    fld" + String.valueOf(key);
			if ((seq + 1) == keys.size()) {
				comma = "";
			} else {
				comma = ",";
			}
			if (colName.startsWith(ASTA)) {
				createSqlList.add(fld + " Integer " + comma);
			} else {
				createSqlList.add(fld + " text not null" + comma);
			}
		}
		createSqlList.add(");");
		//---------------------------------------------------------------------
		//		for (String head : headerList) {
		//			System.out.println("head:" + head);
		//		}
		//---------------------------------------------------------------------
		//create.sql���o�͂���
		//---------------------------------------------------------------------
		EzWriter.list2File(dbDir + JDBC_Sqlite.CREATE_SQL, createSqlList);
		//---------------------------------------------------------------------
		//import.sql���o�͂���
		//---------------------------------------------------------------------
		EzWriter.list2File(dbDir + JDBC_Sqlite.IMPORT_SQL, importSqlList);
		//---------------------------------------------------------------------
		//�t�B�[���h����֏��
		//---------------------------------------------------------------------
		EzWriter.list2File(dbDir + "name.txt", nameList);
		//		for (String element : nameList) {
		//			System.out.println("##" + element);
		//		}
		return lCount;
	}

	// ------------------------------------------------------------------------
	// �g�p�၄
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		String inPath = "C:/temp/QUOHML.PRN";
		genHML2Sqlite(inPath);
	}

	// ------------------------------------------------------------------------
	// �A���P�[�g�f�[�^�����ԓ��L�����j�^�[�Ɍ��肷��
	// ------------------------------------------------------------------------
	public static JDBC_Sqlite genHML2Sqlite(String inPath) {
		// --------------------------------------------------------------------
		// �w���ʑw�̏o�̓f�[�^���p�[�X����DB�̐����ƃC���|�[�g�p�̃X�N���v�g�𐶐�����
		// --------------------------------------------------------------------
		System.out.println("�w���ʑw�̏o�̓f�[�^���p�[�X����DB�̐����ƃC���|�[�g�p�̃X�N���v�g�𐶐�����");
		String dbDir = globals.ResControl.getQPRHome(MONSEL_DIR);//=>C:/@qpr/home/monsel/
		//		System.out.println("userDir:" + dbDir);
		String dbName = DB_NAME;
		String tableName = TABLE_NAME;
		HML2Sqlite ins = new HML2Sqlite(dbDir, tableName, inPath);
		ins.execute();
		List<String> headerList = ins.getHeaderList();
		// --------------------------------------------------------------------
		//�@���������X�N���v�g����sqLite�̃��[�J��DB�𐶐�����
		// --------------------------------------------------------------------
		System.out.println("���������X�N���v�g����sqLite�̃��[�J��DB�𐶐�����");
		JDBC_Sqlite dbObj = new JDBC_Sqlite(dbName, dbDir);
		dbObj.exeCreateAndImport();
		return dbObj;
	}

}