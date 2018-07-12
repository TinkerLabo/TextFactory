package kyPkg.uFile;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kyPkg.sql.ServerConnecter;
import kyPkg.util.CnvArray;

abstract public class SourceObj implements INF_FieldSource {
	protected static final String TABLE = "table";
	protected static final String FIELD = "field";
	protected static final String KEY = "key";
	// ------------------------------------------------------------------------
	// DEFAULT_FIELDS=>C:/@qpr/home/qpr/res/defaultFields.txt
	// ------------------------------------------------------------------------
	// ���[�U�[�ŗL�̑����ݒ胊�X�g���A�ȉ��̂悤�Ȍ`���Ŋi�[����Ă���
	// selectedFields<TAB>���j�^�[�h�c,�w�����t,�w������,JanCode,�w�����z�w����,�w�����z,�w������,�w����i�Ƒԁj,�w����i�ڍׁj,�A�C�e������
	// sourceDir<TAB>Z:/S2/rx/enquetes/NQ/03_�����E���N���/2014/
	// ------------------------------------------------------------------------
	// �N�̗v�f���ǂ��ɂ������������E�E�Etarget�F2014�̂悤�ɂ���΂������낤���E�E�E���ݖ��̂Ŏ����Ă���̂Ŗ��̂��ς���Ă��܂��ƃC�P�i�C
	// ------------------------------------------------------------------------
	// XXX 20140922 ���ꂼ��̗v�f���킩��ɂ����̂ŗᎦ����K�v����
	// ------------------------------------------------------------------------
	private String sourceDir = "";
	private String[] splitteds;
	private String key = "";// "XA1";

	protected String table = "";// �Ώ�SQL�e�[�u������
	protected List<String> selectedFields = null; // �t�B�[���h�̖��́i���x���j( �၄"���j�^�[�h�c","�w�����t",)
	private List<String> allFields; // �t�B�[���h��(�၄"���j�^�[�h�c","�w�����t","�w�����z","�w����i�ڍׁj")
	private HashMap<String, String> fieldMap;// �t�B�[���h���Ǝ��ʃt�B�[���h�̑Ή��\(�၄put(MONITORID,"TRN.Monitor");)
	private HashMap<String, HashMap<String, String>> dictionary;

	protected static final String CONNECT = "connect";
	private HashMap<String, String> connectMap;

	// ------------------------------------------------------------------------
	// QTB�֘A
	// ------------------------------------------------------------------------
	// �t�B�[���h�l�̒l�Ƃ��ꂪ�w�����́i���e�j��ϊ�����}�b�v
	// �၄�P�F���j���A�Q�F�Ηj���E�E�E�E
	// ------------------------------------------------------------------------
	private static final String QTB1_TXT = "Qtb1.TXT";
	private static final String ALIAS_TXT = "alias.txt";
	private HashMap<String, String> typMap = new HashMap();
	private HashMap<String, String> key2nam = new HashMap();
	//	private String alterName = "T1";//"MON";//
	private String alterName = "mon";//20150831  �����������̃G���[�Ή��@���ꂪ�������̂��ǂ����킩��Ȃ����E�E�E

	public String getTableAlterName() {
		return alterName;
	}

	private String KEY_NAME = "KEY";

	public void setKeyName(String key_name) {
		KEY_NAME = key_name;
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public SourceObj() {
		super();
		selectedFields = null;
		allFields = new ArrayList();
		typMap = new HashMap();
		key2nam = new HashMap();
		fieldMap = new HashMap();
		dictionary = new HashMap();
		connectMap = new HashMap();
	}

	//	public String getAlterName() {
	//		return alterName;
	//	}
	//
	//	public void setAlterName(String alterName) {
	//		this.alterName = alterName;
	//	}

	// ------------------------------------------------------------------------
	// getConnectVal("SERVER");
	// getConnectVal("DATABASE");
	// ------------------------------------------------------------------------
	public String getConnectVal(String key) {
		String rtnVal = "";
		if (connectMap == null)
			return rtnVal;
		rtnVal = connectMap.get(key);
		if (rtnVal == null)
			rtnVal = "";
		return rtnVal;
	}

	public String getServer() {
		return getConnectVal("SERVER").toUpperCase();
	}

	public String getDataBase() {
		return getConnectVal("DATABASE");
	}

	// ------------------------------------------------------------------------
	//
	// ------------------------------------------------------------------------
	public void setConnect(String connect) {
		// System.out.println("setConnect:" + connect);
		connectMap = parseConnect(connect);
	}

	// ------------------------------------------------------------------------
	// connect DRIVER={SQL
	// Server};SERVER=ks1s003;UID=sa;PWD=;DATABASE=qprdb;Trusted_Connection=true
	// ------------------------------------------------------------------------
	private HashMap<String, String> parseConnect(String connect) {
		HashMap<String, String> connectMap = new HashMap<String, String>();
		String[] ar1 = connect.split(";");
		for (int i = 0; i < ar1.length; i++) {
			String[] ar2 = ar1[i].split("=");
			if (ar2.length >= 2) {
				connectMap.put(ar2[0].toUpperCase(), ar2[1]);
			}
		}
		return connectMap;
	}

	public void setDictionary(
			HashMap<String, HashMap<String, String>> dictionary) {
		this.dictionary = dictionary;
	}

	// ------------------------------------------------------------------------
	// �t�B�[���h������т���ɑΉ������ϊ������̑g
	// ------------------------------------------------------------------------
	public void dictionary_put(String key, HashMap<String, String> cnv) {
		// XXX key��null�Ȃ�ǉ����Ȃ��I�I
		dictionary.put(key, cnv);
	}

	public HashMap<String, String> dictionary_get(String nam) {
		HashMap<String, String> cnv = dictionary.get(nam);
		if (cnv == null)
			cnv = new HashMap();
		return cnv;
	}

	public HashMap<String, HashMap<String, String>> getDictionary() {
		return dictionary;
	}

	// ------------------------------------------------------------------------
	// ���Y�e�[�u���̎�L�[
	// �၄ setKey:XA1
	// ------------------------------------------------------------------------
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// ------------------------------------------------------------------------
	// �A�N�Z�b�T
	// ------------------------------------------------------------------------
	public void setSourceDir(String sourceDir) {
		splitteds = sourceDir.split("/");
		// �W�v�Ώۊ��Ԃɍ��킹�Ė��[��Ώ۔N�x�ɕύX������
		// ���[�̐��K�\����1984�ȍ~2099�N�ȉ��̐��K�\���ɓ��Ă͂܂邩�ǂ���
		// �i[12]���Ƃ�3�P�^�̐����j�Ŕ��肩��
		// �E�E�E�E�W�v���W�b�N�ł��̃I�u�W�F�N�g���ǂ�Ȃӂ��ɎQ�Ƃ��Ă��邩�m�F����@�Q�Ǝ��ɓ��Y�N�x���p�����[�^�œn���H
		String joined = kyPkg.util.KUtil.join(splitteds, "/") + "/";
		this.sourceDir = joined;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setFieldList(List<String> fieldList) {
		this.allFields = fieldList;
	}

	// ------------------------------------------------------------------------
	// key�Ƒ��݂��邷�ׂẴt�B�[���h��I����Ԃɂ���
	// ------------------------------------------------------------------------
	public void selectAllFields() {
		selectedFields = new ArrayList();
		selectedFields.clear();
		selectedFields.add(KEY_NAME);
		selectedFields.addAll(allFields);
	}

	// ------------------------------------------------------------------------
	// Debug
	// ------------------------------------------------------------------------
	public static void enumIt(String message, List<String> fieldList) {
		System.out.println("#debug# " + message);
		for (String element : fieldList) {
			System.out.println("@sOURCEoBJ    element:" + element);
		}
	}

	public void setFieldMap(HashMap<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	// ------------------------------------------------------------------------
	// �Ώۃe�[�u���������Ă���A�S�t�B�[���h�����X�g
	// ------------------------------------------------------------------------
	public void addAllFields(String nam) {
		allFields.add(nam);
	}

	// ------------------------------------------------------------------------
	// �t�B�[���h�́i���x�����ˎ��ʎq�j�̑g
	// ------------------------------------------------------------------------
	public void putFieldMap(String key, String val) {
		// XXX key��null�Ȃ�ǉ����Ȃ��I�I
		// System.out.println("fieldMap_put >" + key + " >" + val);
		fieldMap.put(key, val);
	}

	// ------------------------------------------------------------------------
	// ���x�����ˎ��ʎq�i���������j
	// ------------------------------------------------------------------------
	public String getField(String nam) {
		String fld = fieldMap.get(nam);
		if (fld == null) {
			return null;// �X�y�[�X�̕����悢���낤���H
		} else {
			return fld;
		}
	}

	public HashMap<String, String> getFieldMap() {
		return fieldMap;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setSelectedFields(List<String> selectedFields) {
		//TODO null��������ǂ�����H
		this.selectedFields = selectedFields;
	}

	public void setSelectedFields(String[] array) {
		this.selectedFields = java.util.Arrays.asList(array);
	}

	@Override
	public List<String> getFieldList() {
		return allFields;
	}

	@Override
	public String getFields() {
		return concatFields(fieldMap, getSelectedFields());
	}

	@Override
	public List<String> getSelectedFields() {
		return selectedFields;
	}

	//-------------------------------------------------------------------------
	// �g�p�၄saveSelectedFields("c:/selectedFields.txt")
	//-------------------------------------------------------------------------
	public void saveSelectedFields(String path) {
		kyPkg.uFile.ListArrayUtil.list2File(path, selectedFields);
	}

	//-------------------------------------------------------------------------
	// �g�p�၄loadSelectedFields("c:/selectedFields.txt")
	//-------------------------------------------------------------------------
	public void loadSelectedFields(String path) {
		List list = kyPkg.uFile.ListArrayUtil.file2List(path);
		if (list == null)
			return;//file is not exists
		setSelectedFields(list);
	}

	//�I�����ꂽ���ږ��ꗗ����c�a�̃t�B�[���h�����R���J�`�������̂�Ԃ�
	private String concatFields(HashMap<String, String> fldMap,
			List<String> keyList) {
		if (keyList == null || keyList.size() == 0) {
			// System.out
			// .println("@concatFields........keyList == null || keyList.size() == 0");
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (String key : keyList) {
			String val = fldMap.get(key);
			// System.out.println("#20140917#  key:" + key + " ===> val:" +
			// val);
			//���݂��Ȃ��t�B�[���h���w�肳��Ă��G���[�Ƃ��Ȃ��i���b�Z�[�W�͏o�͂��悤���H�j
			if (val != null) {
				val = val.trim();
				if (!val.equals("")) {
					if (buf.length() > 0)
						buf.append(",");
					buf.append(val);
				}
			} else {
				//���j�^�[�h�c
				System.out.println(
						"#ERROR SourceObj@concatFields �w�肳�ꂽ�t�B�[���h�͌�����܂���ł�����"
								+ key);
			}
		}
		return buf.toString();
	}

	// ------------------------------------------------------------------------
	// for Debug
	// ------------------------------------------------------------------------
	public void enumurate() {
		for (String nam : getFieldList()) {
			System.out.println("nam:" + nam);
			String fld = getField(nam);
			String typ = typMap.get(nam);
			System.out.println("    fld:" + fld);
			System.out.println("    typ:" + typ);
			HashMap<String, String> cnv = dictionary_get(nam);
			List<String> keyList = new ArrayList(cnv.keySet());
			for (String element : keyList) {
				System.out.println("       element :" + element + " =>"
						+ cnv.get(element));
			}
		}
	}

	// ------------------------------------------------------------------------
	// QTB�t�@�C����ǂݍ���ŁE�E�E�t�B�[���h���ƑΉ��t�B�[���h�̕ϊ������𐶐�����
	// path:Z:\S2\rx\enquetes\NQ\03_�����E���N���\2014\Qtb1.TXT
	// monolith:
	// �����ӁFfield�͈�̃t�B�[���h�i�ꖇ��j��z�肵�Ă���A�����A���̏ꍇ���W�b�N���C�����Ȃ���΂Ȃ�Ȃ�
	// ���Ώۃe�[�u�����i�L�[�A���@�����[�j��2�̃t�B�[���h�łł��Ă��邱�Ƃ�z�肵�Ă���
	// ------------------------------------------------------------------------
	public boolean qtb2HashMap(String path, String monolith) {
		// System.out.println("qtbPath�F" + pPath);
		// System.out.println("monolith�F" + monolith);
		int motCol = 1;
		int keyCol = 2;
		int valCol = 3;
		int namCol = 4;
		int maxCol = 5;
		int occCol = 6;
		int typCol = 7;
		int colCol = 8;
		int lenCol = 9;
		String delimiter = "\t";
		File49ers insF49 = new File49ers(path);
		if (!insF49.isExists()) {
			System.out
					.println("#err @qtb2HashMap file is not existed:" + path);
			return false;
		}
		// System.out.println("delimiter:" + insF49.getDelimiter());
		// System.out.println("maxColumn:" + insF49.getMaxColCount());
		delimiter = insF49.getDelimiter();
		String wRec;
		typMap.clear();
		key2nam.clear();

		// �L�[�̃��x���������炩���ߓo�^���Ă����E�E�E
		putFieldMap(KEY_NAME, key);
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] array = wRec.split(delimiter);
					if (array.length > motCol) {
						String mot = array[motCol].trim();
						if (mot.equals("ROOT")) {
							// �e�L�[��\���s�̏ꍇ
							String key = array[keyCol];// qtb�̃L�[�R�[�h
							String val = array[valCol];// �f�[�^�l
							String nam = array[namCol];// �f�[�^�l���w�����́i�ݖ▼�́j
							String typ = array[typCol];// �A���T�[�i�����~�j�^�C�v�@SINGLE/MULTI
							String col = array[colCol];// ���J�����ڂ��炩
							String len = array[lenCol];// ����

							String fld = "SUBSTRING(" + alterName + "."
									+ monolith + "," + col + "," + len + ")";

							// fld�Fsql�ŕ\������񓚂̑Ώۈ�
							if (typ.toUpperCase().equals("SINGLE")) {
								nam = nam.replaceAll("�@", "");// �S�p�X�y�[�X����蕥��
								addAllFields(nam);// �Ώۃt�B�[���h�̊�������

								// System.out.println("key:" + key + " val:" +
								// val
								// + " nam:" + nam + " typ:" + typ
								// + " fld:" + fld);

								putFieldMap(nam, fld);// �������́F�Ή�����Asql�t�B�[���h�̑�
								key2nam.put(key, nam);// �L�[���疼�̂̋t�����p
								typMap.put(nam, typ);// Multi or Single
							}
						} else {
							// �I������\���s�̏ꍇ
							String val = array[valCol];// �f�[�^�l
							String nam = array[namCol];// �f�[�^�l���w�����́i�I�������́j
							String max = array[maxCol];// �ő�
							String occ = array[occCol];// �J��Ԃ�
							String typ = array[typCol];// �^�C�v
							String col = array[colCol];// ���J�����ڂ��炩
							String len = array[lenCol];// ����
							String motherName = key2nam.get(mot);
							HashMap<String, String> cnv = dictionary_get(
									motherName);
							nam = nam.replaceAll("�@", "");// �S�p�X�y�[�X����蕥��
							// �l�ϊ��p����
							cnv.put(val, nam);
							if (motherName != null)
								dictionary_put(motherName, cnv);
						}
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return true;
	}

	// ------------------------------------------------------------------------
	// �R���o�[�^�[�̑Ή��J�����ɕϊ��������d����ŕԂ�
	// ------------------------------------------------------------------------
	@Override
	public CnvArray getConverter() {
		CnvArray cnv = new CnvArray("");// �ϊ�����
		List<String> selectedFields = getSelectedFields();
		if (selectedFields != null) {
			HashMap<String, HashMap<String, String>> dicts = getDictionary();
			for (int col = 0; col < selectedFields.size(); col++) {
				HashMap<String, String> map = dicts
						.get(selectedFields.get(col));
				if (map != null)
					cnv.setDict(col, map);
			}
		}
		return cnv;
	}

	// @Override
	// abstract public void incore(String sourceDir, boolean initOpt);

	// ------------------------------------------------------------------------
	// 20141015
	// ------------------------------------------------------------------------
	public void incore(String sourceDir) {
		String jUrl = ServerConnecter.getDEF_JURL();
		String user = ServerConnecter.getDEF_USER();
		String pswd = ServerConnecter.getDEF_PASS();
		// --------------------------------------------------------------------
		// ALIAS_TXT��ǂݍ��ݗv�f���n�b�V���}�b�v�Ɋi�[����
		// �၄ aliasPath�F�@Z:/S2/rx/enquetes/NQ/03_�����E���N���/2014/alias.txt
		// ���t�@�C���̓��e�͈ȉ��̂悤�ɂȂ��Ă���
		// --------------------------------------------------------------------
		// connect<TAB>DRIVER={SQLServer};SERVER=xxxx;UID=xxx;PWD=xxx;DATABASE=xxx;Trusted_Connection=true
		// table<TAB>TABLE_NAME
		// field<TAB>XB1
		// key<TAB>XA1
		// Cond<TAB>
		// --------------------------------------------------------------------
		String aliasPath = sourceDir + ALIAS_TXT;
		// System.out.println("�@�@�@�@#debug20141015# aliasPath�F" + aliasPath);
		HashMap<String, String> hMap = HashMapUtil.file2HashMapX(aliasPath);
		if (hMap != null) {
			// 20140922 �����Ń��[�U�[�ݒ���㏑�����Ă��邪�E�E�E�悢�̂��H�^�₾
			// ���[�U�[�ݒ�ł̓R�l�N�g�͎����Ă��Ȃ��Ǝv���E�E�E���[�U�[�ݒ莝�����������悢�̂ł͂Ȃ����H�l����i�}����ʃ\�[�X�ɕύX���ꂽ�ꍇ�͌���̕����ǂ��j
			//			System.out.println("TABLE:" + hMap.get(TABLE));
			//			System.out.println("KEY:" + hMap.get(KEY));
			//			System.out.println("CONNECT:" + hMap.get(CONNECT));
			//			System.out.println("FIELD:" + hMap.get(FIELD));
			setTable(hMap.get(TABLE));
			setKey(hMap.get(KEY));
			setConnect(hMap.get(CONNECT));
			String qtbPath = sourceDir + QTB1_TXT;
			// QTB�t�@�C����ǂݍ���ŁE�E�E�t�B�[���h���ƑΉ��t�B�[���h�̕ϊ������𐶐�
			qtb2HashMap(qtbPath, hMap.get(FIELD));
		}
		//		System.out.println("#QTB_Source.incore end # ");
	}

	// @Override
	// public String getTable() {
	// return table;
	// }
	@Override
	public String getTable() {
		String dataBase = getDataBase();
		String server = getServer();

		//		if (!server.equals(ServerConnecter.QPRSERVER)) {
		if (!server.equalsIgnoreCase(ServerConnecter.CURRENT_SERVER)) {
			// ���T�[�o�[�ȊO�Ȃ�@�����N�T�[�o�[�̃e�[�u���Ƃ݂Ȃ�
			// �����N�T�[�o�[�͂��炩���ߒ�`���Ă����Ȃ���΂Ȃ�Ȃ�
			return server + "." + dataBase + ".dbo." + table;
		}
		return table;
	}

}