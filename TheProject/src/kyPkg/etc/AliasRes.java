package kyPkg.etc;

import static kyPkg.sql.ISAM.CHARACTER_SET_OEM;
import static kyPkg.sql.ISAM.COL_NAME_HEADER_FALSE;
import static kyPkg.sql.ISAM.CSV_DELIMITED;
import static kyPkg.sql.ISAM.FORMAT_CSV_DELIMITED;
import static kyPkg.sql.ISAM.MAX_SCAN_ROWS_0;
import static kyPkg.sql.ISAM.SCHEMA_INI;
import static kyPkg.sql.ISAM.TAB_DELIMITED;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap; //import java.util.Iterator;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.atoms.Atomics;
import kyPkg.atoms.MetaMixer;
import kyPkg.batch.RegFilterBat;
import kyPkg.converter.CelConverter;
import kyPkg.converter.CnvMap;
import kyPkg.converter.Inf_StrConverter;
import kyPkg.filter.CommonClojure;
import kyPkg.filter.MultiMatch;
import kyPkg.pmodel.JsonU;
import kyPkg.sql.CommonConnector;
import kyPkg.sql.Connector;
import kyPkg.task.TaskSQL2File;
import kyPkg.uFile.DosEmu;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;
import kyPkg.util.Joint;
import kyPkg.util.KUtil;

public class AliasRes {
	protected static final String VTAB = "" + '\u000b'; // �����^�u

	private static final String ROOT = "ROOT";
	private static final String SINGLE = "SINGLE";
	private static final String MULTI = "MULTI";

	private static final String CONNECT = "connect";
	private static final String COND = "Cond";
	private static final String KEY = "key";
	private static final String FIELD = "field";
	private static final String TABLE = "table";

	private static final String TXT = "txt";
	private static final String TAB = "\t";
	private String optionField = "";
	private CelConverter celConverter = null;
	private String delimiter = ",";
	protected String wLF = System.getProperty("line.separator");
	private String jURL = "";
	private String user = ""; // ���[�U��
	private String pass = ""; // �p�X���[�h
	private String aliasDir = "";
	private String sql = "";
	private kyPkg.rez.HashRes hRes;
	private HashMap<String, String> map;

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public AliasRes(String pAliasDir) {
		this.aliasDir = genAliasDir(pAliasDir);
		String aliasPath = this.aliasDir + "alias.txt";
		File alias = new File(aliasPath);
		System.out.println("## AliasRes ##�@aliasPath:" + aliasPath);
		if (alias.exists() != true) {
			System.out.println("#ERROR @AliasRes not exists:" + aliasPath);
		} else {
			// alias.txt�̓��e����R�l�N�g�����쐬����
			hRes = new kyPkg.rez.HashRes(aliasPath, TAB);
			String connect = hRes.getValue(CONNECT);
			String[] array = connect.split(";");
			map = new HashMap<String, String>();
			for (int i = 0; i < array.length; i++) {
				String[] chunk = array[i].split("=");
				if (chunk.length == 1) {
					map.put(chunk[0], "");
				} else if (chunk.length == 2) {
					map.put(chunk[0], chunk[1]);
				}
			}
			user = map.get("UID");
			pass = map.get("PWD");
			jURL = connect.replaceFirst("DRIVER", "JDBC:ODBC:DRIVER");
			//			System.out.println("#######################################");
			//			System.out.println("user:" + user);
			//			System.out.println("pass:" + pass);
			//			System.out.println("jURL:" + jURL);
			//			System.out.println("#######################################");
		}
	}

	public void setOptionField(String optionField) {
		this.optionField = optionField;
	}

	private String getResTable() {
		if (hRes == null)
			return null;
		return hRes.getValue(TABLE);
	}

	private String getResField() {
		if (hRes == null)
			return null;
		return hRes.getValue(FIELD);
	}

	private String getResKey() {
		if (hRes == null)
			return null;
		return hRes.getValue(KEY);
	}

	private String getResCond() {
		if (hRes == null)
			return null;
		String wStr = hRes.getValue(COND);
		wStr = wStr.toUpperCase();
		int pos = wStr.indexOf("WHERE");
		if (pos >= 0) {
			wStr = wStr.substring(pos + 5);
		}
		System.out.println("@@@@@@@@@@@@@@@@@@@@@" + wStr);
		return wStr;
	}

	// key:DRIVER val:{SQL Server}
	// key:SERVER val:KS1S003
	// key:UID val:sa
	// key:PWD val:
	// key:DATABASE val:qprdb
	// key:Trusted_Connection val:true

	public String getJURL() {
		return jURL;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	private String getSql(int[] col, int[] len, String[] AsName) {
		return getSql(col, len, AsName, "");
	}

	private String getSql(int[] col, int[] len, String[] AsName,
			String option) {
		String fields = "";
		String table = hRes.getValue(TABLE);
		String field = hRes.getValue(FIELD);
		String key = hRes.getValue(KEY);
		String cond = hRes.getValue(COND);
		if (cond == null)
			cond = "";
		cond = cond.toUpperCase();
		cond = cond.replaceAll("WHERE", "");
		cond = cond.trim();
		if (key == null || key.trim().equals(""))
			key = "ID";
		if (field == null || field.trim().equals("")) {
			fields = "Field?";
		} else {
			fields = ""; // XXX jet�ɂ�SUBSTRING�����������悤�ȋC������E�E�E
			if ((col != null) && (len != null) && (AsName != null)
					&& (col.length == len.length)
					&& (col.length == AsName.length)) {
				for (int i = 0; i < col.length; i++) {
					if (!fields.equals(""))
						fields = fields + ",";
					fields = fields + "SUBSTRING(" + field + "," + col[i] + ","
							+ len[i] + ") as " + AsName[i];
				}
			} else {
				fields = field;
			}
			if (option != null && !option.equals("")) {
				fields = fields + option;
			}
		}
		if (table == null || table.trim().equals(""))
			table = "Table?";
		sql = "select " + key + "," + fields + " from " + table;
		if (!cond.equals("")) {
			sql = sql + " where " + cond;
		}
		return sql;
	}

	// ------------------------------------------------------------------------
	// �f�[�^�x�[�X�ɑ��݂���L�[���ڂ݂̂ɕϊ�����
	// ���N�x�ύX�����ꍇ�ɓ������̂��قȂ�ꍇ�Ȃǂ�z�肵�Ă���
	// ------------------------------------------------------------------------
	private String[] limit2ExistedField(List keyList, String[] mapFields) {
		List<String> existed = new ArrayList();
		for (String key : mapFields) {
			if (keyList.contains(key)) {
				existed.add(key);
			} else {
				System.out.println("not found on qtb:" + key);
			}
		}
		return (String[]) existed.toArray(new String[existed.size()]);
	}

	// ------------------------------------------------------------------------
	// XXX �t�@�C�����Ȃǂ̃L�����N�^�Z�b�g�𒲐����Ă���
	// 2011-09-01 �R�R�ŗ����Ă���炵���@�t�@�C�����̖�肩�ȁE�E�E
	// ------------------------------------------------------------------------
	/**************************************************************************
	 * saveAsAtomics				
	 * @param metaPath		"C:/@qpr/home/Personal/MonSets/atom/�p�o�q�A���P�[�g/03_�����E���N���/2015/_";	 
	 * @param mapFields		{A01,A02,A03,A04,A05,A0601,A0602,A0603,A0604,A0605,A0606,A0608,A07,A08,A09,A10,A11,A12,A13,A14,A15,A19,A20,A21,A22,A23,A24,B25,B26,B28};	 
	 **************************************************************************/
	public void saveAsAtomics(String metaPath, String[] mapFields) {
		System.out.println("## saveAsAtomics ##");
		//		debug   //�����͂�������g���[�X����\��
		// --------------------------------------------------------------------
		// �p�s�a�P���I�u�W�F�N�g������
		// [QTB1.TXT]
		// ColNameHeader=False
		// Format=CSVDelimited
		// MaxScanRows=0
		// CharacterSet=OEM
		// Col1="Srt" Char Width 60
		// Col2="Mot" Char Width 60
		// Col3="Key" Char Width 60
		// Col4="Val" Char Width 60
		// Col5="Nam" Char Width 60
		// Col6="Max" Integer
		// Col7="Occ" Integer
		// Col8="Typ" Char Width 10
		// Col9="Col" Integer
		// Col10="Len" Integer
		// Col11="Opt" Char Width 128
		// --------------------------------------------------------------------
		String pathQTB1 = aliasDir + "QTB1.txt";
		String pathQTB2 = aliasDir + "QTB2.txt";
		System.out.println("@saveAsAtomics pathQTB1:" + pathQTB1);
		System.out.println("@saveAsAtomics pathQTB2:" + pathQTB2);
		if (!new File(pathQTB1).exists()) {
			System.out
					.println("ERROR@saveAsAtomics File Not Found:" + pathQTB1);
			return;
		}
		if (!new File(pathQTB2).exists()) {
			pathQTB2 = pathQTB1; // QTB2�����݂��Ȃ��ꍇ
		}
		// ---------------------------------------------------------------------
		// QTB1 incore (key,strElement,numElement)
		// MapReducer�𗘗p���ăL�[���X�g���E���A���ڂ��W�񂵂��肷��
		// ---------------------------------------------------------------------
		String delim1 = new File49ers(pathQTB1).getDelimiter();
		String delim2 = new File49ers(pathQTB2).getDelimiter();
		// ---------------------------------------------------------------------
		String keyCol1 = "2";
		String mapCol1 = "4,7";
		String sumCol1 = "8,9,6";
		MapReducer closure1 = new MapReducer("", keyCol1, mapCol1, sumCol1,
				delim1);
		// closure1.setOutPath(outPath1);
		closure1.setCondition(1, ROOT); // 1��ڂ�ROOT�Ƀ}�b�`������̈ӊO�͏��O
		closure1.setSumFlag(false); // ���l���ڂ����Z���Ȃ�
		closure1.setCountOption(false); // �퐔�͗v��Ȃ�
		new CommonClojure().incore(closure1, pathQTB1, true);
		// ---------------------------------------------------------------------
		// QTB2 incore
		// MapReducer�𗘗p���ăL�[���X�g���E���A���ڂ��W�񂵂��肷��
		// ---------------------------------------------------------------------
		String keyCol2 = "1";
		String mapCol2 = "3,4";
		String sumCol2 = null;
		MapReducer closure2 = new MapReducer("", keyCol2, mapCol2, sumCol2,
				delim2);
		// closure2.setOutPath(outPath2);
		closure2.setSumFlag(false); // ���l�͉��Z���Ȃ�
		closure2.setModFlag(true); // �L�[���������ڂ͒ǋL����
		closure2.setCountOption(false); // �퐔�͗v��Ȃ�
		new CommonClojure().incore(closure2, pathQTB2, true);
		// ---------------------------------------------------------------------
		// �f�[�^�x�[�X�ɑ��݂��鍀�ڂ݂̂ɏ���������i2014/09/25�j
		// 20140924 (�w�肵���p�����[�^�t�B�[���h���������ɑ��݂��Ȃ��ꍇnull�ƂȂ�)
		// ����𖳎�����
		// ---------------------------------------------------------------------
		List keyList = closure1.getKeyList();
		mapFields = limit2ExistedField(keyList, mapFields);
		// ---------------------------------------------------------------------
		// �t�B�[���h�w�肪�Ȃ���Ύ�����������
		// ---------------------------------------------------------------------
		if (mapFields == null) {
			mapFields = (String[]) keyList.toArray(new String[keyList.size()]);
		}
		int mLen = mapFields.length;

		Inf_StrConverter[] cnvMap = new Inf_StrConverter[mLen];
		String[] mapTtl = new String[mLen];
		String[] mapTyp = new String[mLen];
		int[] target = new int[mLen];
		int[] col = new int[mLen];
		int[] len = new int[mLen];
		int[] occ = new int[mLen];
		Atomics atomics = new Atomics();
		// ---------------------------------------------------------------------
		// �p�s�a���fields�̈ʒu���𒲂ׂ�(���Y�p�����[�^���擾�����)
		// ---------------------------------------------------------------------
		ArrayList<String> mapList = new ArrayList();
		for (int i = 0; i < mapFields.length; i++) {
			String key = mapFields[i];
			String title = closure1.getStrElement(key, 0);
			//			System.out.println("xx [" + i + "]:" + key + "  title:" + title);
			// ----------------------------------------------------------------
			// QTB1
			// ----------------------------------------------------------------
			mapList.add("" + (i + 1));
			target[i] = i + 1;
			col[i] = closure1.getNumElement(key, 0);
			len[i] = closure1.getNumElement(key, 1);
			occ[i] = closure1.getNumElement(key, 2);
			mapTtl[i] = closure1.getStrElement(key, 0);
			mapTyp[i] = closure1.getStrElement(key, 1);
			// �C��@ 2009/01/21
			if (mapTyp[i].equals(MULTI)) {
				len[i] = len[i] * occ[i];
			}
			// XXX �}���`��occ���P�Ȃ�p�����[�^�G���[�Ȃ̂�len���������Ƃ���E�E�E
			// System.out.println("field[" + i + "]:" + mapFields[i] +
			// " type:"
			// + mapType[i] + " title:" + mapTitle[i]);
			// System.out.println("col:" + col[i] + " len:" + len[i] +
			// " occ:"
			// + occ[i]);
			// ----------------------------------------------------------------
			// QTB2
			// ----------------------------------------------------------------
			String valStr = closure2.getStrElement(key, 0);
			String tagStr = closure2.getStrElement(key, 1);
			if (mapTyp[i].equals(SINGLE)) {
				//				System.out.println("#pre CnvMap# tagStr:"+tagStr);
				//				System.out.println("#pre CnvMap# valStr:"+valStr);
				cnvMap[i] = new CnvMap(tagStr, valStr, null, TAB);
			} else {
				if (occ[i] == 1)
					occ[i] = len[i];
				cnvMap[i] = null; // XXX or �}���`�p�t�B���^
				// cnvMap[i] = new MultiPlusNa(occ[i]); //�����ł͂��Ȃ��ق����x�^�[����
			}
			// System.out.println("mapTitle[" + i + "]:" + mapTitle[i]);
			// for (int j = 0; j < tagNames.length; j++) {
			// System.out.println("names[" + j + "]:" + tagNames[j]);
			// }
			// atomics.setTagName(mapTitle[i],tagStr,"\t");
			atomics.modTagName(tagStr, TAB); // "\t"
		}
		atomics.setMapOcc(occ);
		atomics.setKeyElement(new int[] { 0 });
		atomics.setMapElement(mapList);
		atomics.setMapTitle(mapTtl);
		// ---------------------------------------------------------------------
		// ���^�f�[�^���o�͂���
		// �၄metaPath=>C:/@qpr/home/Personal/MonSets/atom/�p�o�q�A���P�[�g/01_�����E���f�B�A��/2013/_.atm
		// ---------------------------------------------------------------------
		atomics.saveAsATM(metaPath);
		// ---------------------------------------------------------------------
		// csv�������Ƀf�[�^�l�𖼏̂ɕϊ�������t�B���^
		// ---------------------------------------------------------------------
		celConverter = new CelConverter(target, cnvMap);
		// ---------------------------------------------------------------------
		// ���[�J���ɑ�������ۑ�����i�����������s����j
		// �၄outPath:C:/@qpr/home/Personal/MonSets/atom/�p�o�q�A���P�[�g/01_�����E���f�B�A��/2013/_.txt
		// ---------------------------------------------------------------------
		Connector connector = new CommonConnector(this);
		String sql = this.getSql(col, len, mapFields, optionField);
		//		System.out.println("sql:" + sql);
		String outPath = metaPath + ".txt";
		System.out.println("�����f�[�^�����[�J���ɕۑ�����@�@�@�@�@�@outPath:" + outPath);
		TaskSQL2File task = new TaskSQL2File(outPath, connector, sql);
		if (celConverter != null)
			task.setCelConverter(celConverter);// map�ϊ����s�� 2008/10/31�ǉ�
		task.setDelimiter(delimiter);// "\t"
		task.execute();
	}

	// XXX �t�@�C�����Ȃǂ̃L�����N�^�Z�b�g�𒲐����Ă���
	public void saveAsStandAlone(String outDir) {
		// XXX ���̃t�@�C���o�͕͂K�v�Ȃ̂��H�H
		String outPath1 = ResControlWeb.getD_Resources_Templates("cnvQTB1.txt");
		String outPath2 = ResControlWeb.getD_Resources_Templates("cnvQTB2.txt");
		System.out.println("@saveAsStandAlone outDir:" + outDir);
		String outPath = outDir + "ASM.txt";
		// ---------------------------------------------------------------------
		// �p�s�a�P���I�u�W�F�N�g������
		// [QTB1.TXT]
		// ColNameHeader=False
		// Format=CSVDelimited
		// MaxScanRows=0
		// CharacterSet=OEM
		// Col1="Srt" Char Width 60
		// Col2="Mot" Char Width 60
		// Col3="Key" Char Width 60
		// Col4="Val" Char Width 60
		// Col5="Nam" Char Width 60
		// Col6="Max" Integer
		// Col7="Occ" Integer
		// Col8="Typ" Char Width 10
		// Col9="Col" Integer
		// Col10="Len" Integer
		// Col11="Opt" Char Width 128
		// ---------------------------------------------------------------------
		String pathQTB1 = aliasDir + "QTB1.txt";
		String pathQTB2 = aliasDir + "QTB2.txt";
		String outQtb1 = outDir + "QTB1.txt";
		String outQtb2 = outDir + "QTB2.txt";
		if (!new File(pathQTB1).exists()) {
			System.out.println("ERROR File Not Found:" + pathQTB1);
			return;
		}
		if (!new File(pathQTB2).exists()) {
			pathQTB2 = pathQTB1; // QTB2�����݂��Ȃ��ꍇ
		}
		// System.out.println(" inQTB1:" + inQTB1);
		// System.out.println(" inQTB2:" + inQTB2);
		// System.out.println(" outQTB1:" + outQTB1);
		// System.out.println(" outQTB2:" + outQTB2);
		String delim1 = new File49ers(pathQTB1).getDelimiter();
		String delim2 = new File49ers(pathQTB2).getDelimiter();
		// ---------------------------------------------------------------------
		// QTB1 incore (key,strElement,numElement)
		// ---------------------------------------------------------------------
		String keyCol = "2";
		String mapCol = "4,7";
		String sumCol = "8,9,6";
		MapReducer closure1 = new MapReducer(outPath1, keyCol, mapCol, sumCol,
				delim1);
		// closure1.setOutPath(outPath1);
		closure1.setCondition(1, ROOT); // 1��ڂ�ROOT�Ƀ}�b�`������̈ӊO�͏��O
		closure1.setSumFlag(false); // ���l���ڂ����Z���Ȃ�
		closure1.setCountOption(false); // �퐔�͗v��Ȃ�
		// elmQTB1.setOutPath(outPath5);
		new CommonClojure().incore(closure1, pathQTB1, true);
		// ---------------------------------------------------------------------
		// QTB2 incore
		// ---------------------------------------------------------------------
		String keyCol2 = "1";
		String mapCol2 = "3,4";
		String sumCol2 = null;
		MapReducer closure2 = new MapReducer(outPath2, keyCol2, mapCol2,
				sumCol2, delim2);
		// closure2.setOutPath(outPath2);
		closure2.setSumFlag(false); // ���l�͉��Z���Ȃ�
		closure2.setModFlag(true); // �L�[���������ڂ͒ǋL����
		closure2.setCountOption(false); // �퐔�͗v��Ȃ�
		// XXX ���̃t�@�C���o�͕͂K�v�Ȃ̂��H�H
		// elmQTB2.setOutPath(outPath6);
		new CommonClojure().incore(closure2, pathQTB2, true);
		// ---------------------------------------------------------------------
		// ���ׂẴ}�b�v�t�B�[���h���E���E�E�E
		// ---------------------------------------------------------------------
		List keyList = closure1.getKeyList();
		// for (int i = 0; i < keyList.size(); i++) {
		// String wkFld = (String)keyList.get(i);
		// System.out.println("Fld<"+i+">:"+wkFld);
		// }
		String[] mapFields = (String[]) keyList
				.toArray(new String[keyList.size()]);
		// Inf_StrConverter[] cnvMap = new Inf_StrConverter[mapFields.length];
		int[] col = new int[mapFields.length];
		int[] len = new int[mapFields.length];
		int[] occ = new int[mapFields.length];
		String[] mapTitle = new String[mapFields.length];
		String[] mapType = new String[mapFields.length];
		// ---------------------------------------------------------------------
		// �p�s�a���fields�̈ʒu���𒲂ׂ�(���Y�p�����[�^���擾�����)
		// ---------------------------------------------------------------------
		// [ASM.TXT]
		// ColNameHeader=False
		// Format=CSVDelimited
		// MaxScanRows=0
		// CharacterSet=OEM
		// Col1="ID" Char Width 15
		// Col2="EXB" Char Width ??
		int[] target = new int[mapFields.length];
		// ---------------------------------------------------------------------
		// alias.txt
		// ---------------------------------------------------------------------
		ArrayList<String> aliasList = new ArrayList();
		aliasList.add(
				"connect\tDRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir=$;DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;");
		aliasList.add("table\tASM#TXT");
		aliasList.add("field\tXXX");
		aliasList.add("key\tID");
		aliasList.add("Cond\t");
		String outAlias = outDir + "alias.txt";
		ListArrayUtil.list2File(outAlias, aliasList);
		// ---------------------------------------------------------------------
		// schema
		// ---------------------------------------------------------------------
		ArrayList<String> schemaList = new ArrayList();
		schemaList.add("[ASM.TXT]");
		schemaList.add(COL_NAME_HEADER_FALSE);
		schemaList.add(FORMAT_CSV_DELIMITED);
		schemaList.add(MAX_SCAN_ROWS_0);
		schemaList.add(CHARACTER_SET_OEM);
		schemaList.add("Col1=\"ID\" Char Width 15");
		ArrayList<String> mapList = new ArrayList();
		for (int i = 0; i < mapFields.length; i++) {
			mapList.add("" + (i + 1));
			target[i] = i + 1;
			// QTB1
			col[i] = closure1.getNumElement(mapFields[i], 0);
			len[i] = closure1.getNumElement(mapFields[i], 1);
			occ[i] = closure1.getNumElement(mapFields[i], 2);
			mapTitle[i] = closure1.getStrElement(mapFields[i], 0);
			mapType[i] = closure1.getStrElement(mapFields[i], 1);
			if (mapType[i].equals(MULTI)) {
				len[i] = len[i] * occ[i];
			}
			// System.out.println("field[" + i + "]:" + mapFields[i] + " type:"+
			// mapType[i] + " title:" + mapTitle[i]);
			// System.out.println("col:" + col[i] + " len:" + len[i] + " occ:"+
			// occ[i]);
			// Col2="EXB" Char Width ??
			String wStr = "";
			if (mapType[i].equals(SINGLE)) {
				wStr = "Col" + (i + 2) + "=X" + mapFields[i] + " Char Width "
						+ len[i];
			} else if (mapType[i].equals(MULTI)) {
				wStr = "Col" + (i + 2) + "=X" + mapFields[i] + " Char Width "
						+ len[i];
			}
			// System.out.println(wStr);
			schemaList.add(wStr);
			// XXX �}���`��occ���P�Ȃ�p�����[�^�G���[�Ȃ̂�len���������Ƃ���E�E�E
			// QTB2
			// String valStr = elmQTB2.getStrElement(mapFields[i], 0);
			// String tagStr = elmQTB2.getStrElement(mapFields[i], 1);
			// if (mapType[i].equals("SINGLE")) {
			// cnvMap[i] = new ConvertMap(tagStr, valStr, null, "\t");
			// } else {
			// if (occ[i] == 1)
			// occ[i] = len[i];
			// cnvMap[i] = null; // XXX or �}���`�p�t�B���^
			// }
		}
		// QTB1 & 2
		// DosEmu.copy(aliasDir + "/QTB*.txt", outDir);
		new RegFilterBat().qtb_Convert(outQtb1, pathQTB1);
		if (!pathQTB1.equals(pathQTB2)) {
			new RegFilterBat().qtb_Convert(outQtb2, pathQTB2);
		}
		String inSchema = aliasDir + SCHEMA_INI;
		String outSchema = outDir + SCHEMA_INI;
		// Schema.INI
		List corelist = ListArrayUtil.file2List(inSchema);
		List list = KUtil.listModz(corelist, schemaList);

		list = KUtil.listReplaceAll(list, TAB_DELIMITED, CSV_DELIMITED);
		// for (Iterator iter = list.iterator(); iter.hasNext();) {
		// String rec = (String) iter.next();
		// // System.out.println("rec:" + rec);
		// }
		ListArrayUtil.list2File(outSchema, list);
		// csv�������ɒl��ϊ�������t�B���^
		String sql = this.getSql(col, len, mapFields, optionField);
		// ---------------------------------------------------------------------
		// �R�l�N�^�[�����
		// ---------------------------------------------------------------------
		Connector connector = new CommonConnector(this);
		// ---------------------------------------------------------------------
		// �����������s����
		// ---------------------------------------------------------------------
		TaskSQL2File task = new TaskSQL2File(outPath, connector, sql);
		// if (celConverter != null)
		// task.setCelConverter(celConverter);// map�ϊ����s�� 2008/10/31�ǉ�
		task.setLiteral("\"");
		task.setKeyEnc(false);
		task.setDelimiter(delimiter);// "\t"
		task.execute();
	}

	private String genAliasDir(String aliasDir) {
		File file = new File(aliasDir);
		if (file.exists() == false)
			return null;
		aliasDir = file.getAbsolutePath();
		String PS = System.getProperty("file.separator");
		if (PS.equals("\\"))
			PS = "\\\\";
		aliasDir = aliasDir.replaceAll(PS, "/");
		return aliasDir + "/";
	}

	private static String getAliasDir(String levs) {
		// levs:�p�o�q�A���P�[�g/03.�����E���N���/2012/
		String[] keys = levs.split("/");
		HashMap<String, String> reposMap = JsonU
				.file2hash(ResControl.REPOSITORY);
		return reposMap.get(keys[0]) + "/" + keys[1] + "/" + keys[2] + "/";
	}

	// ------------------------------------------------------------------------
	// aliasDir:Z:\s2\rx\Enquetes\NQ/01_�����E���f�B�A��/2013/
	// ex C:\@QPR\home\personal\Atom\�p�o�q�A���P�[�g\03_�����E���N���\2013
	// ------------------------------------------------------------------------
	/**************************************************************************
	 * saveAsAtomicsWrap	���̃f�[�^�\�[�X����w�肳�ꂽ�t�B�[���h�l�݂̂𒊏o�������̂�Atomics�I�u�W�F�N�g�Ƃ��ă��[�J���ɕۑ�����
	 * @param outDir	�o�͐� :C:/@qpr/home/Personal/MonSets/atom/			 
	 * @param key		���͌��p�X�p�����[�^:�p�o�q�A���P�[�g/03_�����E���N���/2015/		 
	 * @param field		�Ώ̃t�B�[���h:A01,A02,A03,A04,A05,A0601,A0602,A0603,A0604,A0605,A0606,A0608,A07,A08,A09,A10,A11,A12,A13,A14,A15,A19,A20,A21,A22,A23,A24,B25,B26,B28	 
	 **************************************************************************/
	private static String saveAsAtomicsWrap(String outDir, String key,
			String field) {
		//���͌��p�X�p�����[�^�̐擪�����i�w�p�o�q�A���P�[�g�x�̕����j�����|�W�g���[�̃p�X�ƒu���������p�X����Ԃ�
		String aliasDir = getAliasDir(key); // =>�@Z:/s2/rx/Enquetes/NQ/03_�����E���N���/2015/
		// --------------------------------------------------------------------
		// ���̃t�@�C���̒������n�r�̋K�肩��O��Ă��܂��G���[�ƂȂ��Ă���E�E�E�Ȃ�ł���Ȗ��O�ɂȂ��Ă���񂾂����H
		// ���񓯂����O�ł͖�肪����̂��낤���H���ɁQ���g���Ă݂悤�Ǝv��
		// �A�����������X�g�����O����n�b�V���R�[�h�𐶐����Ă���𗘗p����������
		// --------------------------------------------------------------------
		// String outPath = outDir + key + field; //original
		String outPath = outDir + key + "_";
		// outPath�@=>�@C:/@qpr/home/Personal/MonSets/atom/�p�o�q�A���P�[�g/03_�����E���N���/2015/_
		boolean debug = true;
		if (debug) {
			System.out.println("##<<saveAsAtomicsWrap xx >>##");
			System.out.println("## key     :" + key);
			System.out.println("## field   :" + field);
			System.out.println("## outDir  :" + outDir);
			System.out.println("## aliasDir:" + aliasDir);
			System.out.println("## outPath :" + outPath);
			System.out.println("##<<saveAsAtomicsWrap xxx>>##");
		}
		new AliasRes(aliasDir).saveAsAtomics(outPath, field.split(","));
		return outPath;
	}

	public static List<String> listCnv(List<String> srcList, String regex,
			String replacement) {
		List<String> dstList = new ArrayList();
		for (String element : srcList) {
			dstList.add(element.replaceAll(regex, replacement));
		}
		return dstList;
	}

	//	private static HashMap<String, List> listX2HashMap(List<String> parmList,boolean debug) {
	//		HashMap<String, List> hmap = new HashMap();
	//		for (String element : parmList) {
	//			String[] splited = element.split(VTAB);
	//			if (splited.length >= 3) {
	//				String comment = splited[0];
	//				String key = splited[1];
	//				String val = splited[2];
	//				System.out.println("��key:" + key + "val:" + val);
	//				List list = hmap.get(key);
	//				if (list == null) {
	//					list = new ArrayList();
	//				}
	//				list.add(val);
	//				hmap.put(key, list);
	//			}
	//		}
	//		hmap.remove(null);
	//		return hmap;
	//	}

	public static HashMap<String, List> cnvParam2Map(List<String> parmList) {
		HashMap<String, List> hmap = new HashMap();
		for (String element : parmList) {
			element = element.replaceAll(VTAB, TAB);
			String[] splited = element.split(TAB, -1);
			if (splited.length >= 3) {
				String comment = splited[0];
				String key = splited[1];
				String val = splited[2];
				//				System.out.println("��key:" + key + "val:" + val);
				List<String> list = hmap.get(key);
				if (list == null)
					list = new ArrayList();
				if (!list.contains(val))
					list.add(val);//�t�B�[���h�͏d�������Ȃ�
				hmap.put(key, list);
				//				Set<String> set = hmap.get(key);
				//				if (set == null)
				//					set = new HashSet();
				//				set.add(val);
				//				hmap.put(key, set);
			}
		}
		hmap.remove(null);
		return hmap;
	}

	// --------------------------------------------------------------------
	// �o�b�`�ŗ��p�ł���悤�ɍl���A�t�@�C���ɏo�͂���
	//	rec�၄�p�o�q�A���P�[�g/03_�����E���N���/2015/	A01,A02,A03,A04,A05,A0601,A0602,A0603,A0604,A0605,A0606,A0608,A07,A08,A09,A10,A11,A12,A13,A14,A15,A19,A20,A21,A22,A23,A24,B25,B26,B28
	//	��������p�X�ɏ����o��
	//	prmPath=>C:/@qpr/home/Personal/MonSets/currentP.txt
	// --------------------------------------------------------------------
	//	private static void list2paramFile(String prmPath,
	//			HashMap<String, List> hmap,boolean debug) {
	//		List<String> keyList = new ArrayList(hmap.keySet());
	//		List<String> recList = new ArrayList();
	//		for (String key : keyList) {
	//			String rec = key + "\t" + Joint.join(hmap.get(key), ",");
	//			System.out.println("rec:" + rec);
	//			recList.add(rec);
	//		}
	//		ListArrayUtil.list2File(prmPath, recList);
	//	}

	//	public static void saveCurrentMap(String prmPath,
	//			HashMap<String, List> hmap) {
	//		List<String> recList = new ArrayList();
	//		List<String> keyList = new ArrayList(hmap.keySet());
	//		for (String key : keyList) {
	//			String rec = key + "\t" + Joint.join(hmap.get(key), ",");
	//			recList.add(rec);
	//		}
	//		ListArrayUtil.list2File(prmPath, recList);
	//	}

	//TODO ���̕ӂ𗍂߂��������l����
	// ------------------------------------------------------------------------
	// �I���W�i���̃p�����[�^����Ώ۔N�����炵�Ă���ꍇ�ɃR�[�������
	// ------------------------------------------------------------------------
	// �f�[�^�����ƂɃf�[�^���o���s�����[�J���ɃR�s�[����(�I���W�i���̃p�����[�^����Ώ۔N�����炵�Ă���ꍇ�̑Ή�)
	// --------------------------------------------------------------------

	//---------------------------------------------------------------------
	//��p�����[�^�t�@�C���̓��e��
	//�p�o�q�A���P�[�g/02_�����E���C�t�X�^�C����/2015/	cluster2,cluster,cluster,cluster2
	//�p�o�q�A���P�[�g/03_�����E���N���/2016/	A01,A02,A03,A04,A05,A0601,A0602,A0603,A0604,A0605,A0606,A0608,A07,A08,A09,A10,A11,A12,A13,A14,A15,A19,A20,A21,A22,A23,A24,B25,B26,B28,A01,A02,A03,A04,A05,A0601,A0602,A0603,A0604,A0605,A0606,A0608,A07,A08,A09,A10,A11,A12,A13,A14,A15,A19,A20,A21,A22,A23,A24,B25,B26,B28
	//---------------------------------------------------------------------
	public static HashMap<String, List> loadCurrentMap() {
		String prmPath = ResControl.getCurrentP_TXT();
		return paramFile2HashMap(prmPath);
	}

	public static HashMap<String, List> paramFile2HashMap(String prmPath) {
		List<String> pList = ListArrayUtil.file2List(prmPath);
		if (pList == null) {
			System.out.println("@list2HashMap (pList == null) ?!");
			return null;
		}
		List<String> keys = new ArrayList();
		HashMap<String, List> fieldMap = new HashMap();
		for (String rec : pList) {
			System.out.println("## createCurrentData param=>" + rec);
			String[] array = rec.split(TAB, -1);
			if (array.length >= 2) {
				String key = array[0];//Path
				String fields = array[1];//Fields
				System.out.println("key:" + key);
				System.out.println("field:" + fields);
				keys.add(key);
				String[] arrayx = fields.split(",");
				List list = new ArrayList();
				Set set = new HashSet();
				for (String field : arrayx) {
					if (!set.contains(field))
						list.add(field);
					set.add(field);
				}
				fieldMap.put(key, list);
			}
		}
		return fieldMap;
	}
	//	public static HashMap<String, String> list2HashMap(List<String> pList) {
	//		if (pList == null) {
	//			System.out.println("@list2HashMap (pList == null) ?!");
	//			return null;
	//		}
	//		List<String> keys = new ArrayList();
	//		HashMap<String, String> fieldMap = new HashMap<String, String>();
	//		for (String rec : pList) {
	//			System.out.println("## createCurrentData param=>" + rec);
	//			String[] array = rec.split(TAB, -1);
	//			if (array.length >= 2) {
	//				String key = array[0];//Path
	//				String field = array[1];//Fields
	//				System.out.println("key:" + key);
	//				System.out.println("field:" + field);
	//				keys.add(key);
	//				fieldMap.put(key, field);
	//			}
	//		}
	//		return fieldMap;
	//	}

	public static void createCurrentData(HashMap<String, List> fieldMap,
			String thisYear) {
		//		String thisYear = String.valueOf(DateCalc.getThisYear());
		//		�p�o�q�A���P�[�g/03_�����E���N���/%s/	A0608,A09,A0606,A08,A07,A0605,A0604,B26,A0603,B25,A0602,A0601,A02,A01,B28,A24,A23,A05,A04,A22,A03,A21,A20,A19,A11,A10,A13,A12,A15,A14
		String prefix = "�p�o�q�A���P�[�g/03_�����E���N���/";
		String template1 = prefix + "%s/";
		//		String def_val1 = "A0608,A09,A0606,A08,A07,A0605,A0604,B26,A0603,B25,A0602,A0601,A02,A01,B28,A24,A23,A05,A04,A22,A03,A21,A20,A19,A11,A10,A13,A12,A15,A14,cluster1,cluster2";
		String def_val1 = "A01,A02,A03,A04,A05,A0601,A0602,A0603,A0604,A0605,A0606,A0608,A07,A08,A09,A10,A11,A12,A13,A14,A15,A19,A20,A21,A22,A23,A24,B25,B26,B28,cluster1,cluster2";
		String def_key1 = String.format(template1, thisYear);
		System.out.println("<AliasRes.createCurrentData() begin>----------");
		System.out.println("<<�T�[�o�[���烍�[�J���ɑ������f�[�^���W�߂�>>--------");
		String outDir = ResControl.getMonSetsDir() + "atom/";
		System.out.println("################################ outDir:" + outDir);
		// ----------------------------------------------------------------
		// �p�o�q�A���P�[�g/03_�����E���N���/2014/ A01,A02,A03,A04
		// XXX 20140929 �����E���N��҂Ɋւ��鍀�ڗ񂪂Ȃ���Βǉ��A����ΌŒ荀�ڂ�ǉ�����悤�ɏ���������
		// ----------------------------------------------------------------
		// ���S�ɔC�ӂ̑��������擾����悤�ȃC���[�W�ɂȂ��Ă��邪�E�E�E
		// �w���f�[�^�̓���̍��ځi�J�n�I������ѐ��ѓ������j�͌Œ�őI�������悤�ɂ�����
		// ----------------------------------------------------------------
		List<String> outList = new ArrayList();
		// ----------------------------------------------------------------
		//�f�t�H���g�ݒ肳���e�[�u���̎w���擪�ɒu��
		// ----------------------------------------------------------------
		outList.add(saveAsAtomicsWrap(outDir, def_key1, def_val1));
		// ----------------------------------------------------------------
		//�f�t�H���g�ݒ肳���e�[�u���̎w�����菜��
		// ----------------------------------------------------------------
		//20161227 �ꂵ����E�E
		List<String> keyListX = new ArrayList(fieldMap.keySet());
		for (String key : keyListX) {
			if (key.startsWith(prefix)) {
				System.out.println("������ remove key:" + key);
				fieldMap.remove(key);
			}
		}

		//		fieldMap.remove(def_key1);
		for (String key : fieldMap.keySet()) {
			String field = Joint.join(fieldMap.get(key), ",");
			System.out.println("������ key:" + key + " # field:" + field);
			outList.add(saveAsAtomicsWrap(outDir, key, field));
		}

		//		AliasRes.saveCurrentMap(ResControl.getCurrentP_TXT(), fieldMap);//TODO!!
		//*********************************************************************
		//	saveCurrentMap 2016-12-12
		//*********************************************************************
		List<String> recList = new ArrayList();
		recList.add(def_key1 + TAB + def_val1);

		List<String> keyList = new ArrayList(fieldMap.keySet());
		for (String key : keyList) {
			String rec = key + "\t" + Joint.join(fieldMap.get(key), ",");
			System.out.println("rec:" + rec);
			recList.add(rec);
		}
		ListArrayUtil.list2File(ResControl.getCurrentP_TXT(), recList);
		//*********************************************************************

		System.out.println("###############################################");
		System.out.println("## ���[�J���ɏW�߂�����������������");
		System.out.println("## =>C:/@qpr/home/Personal/MonSets/current.txt");
		System.out.println("###############################################");
		String dstPath = FileUtil.changeExt(
				ResControl.getMonSetsDir() + ResControl.CURRENT, TXT);
		String outPath = outList.get(0);
		String modPath = FileUtil.changeExt(outPath, TXT);
		System.out.println("������##-> modPath:" + modPath);
		System.out.println("������##-> dstPath:" + dstPath);
		outList.remove(0);
		//		String delimiter =",";
		String delimiter = "\t";//20150824
		new Atomics(modPath).saveAsATM(dstPath); // ���^�f�[�^����
		FileUtil.fileCopy(dstPath, modPath); // �f�[�^������
		for (String element : outList) {
			modPath = FileUtil.changeExt(element, TXT);
			//			System.out.println("������##-> modPath:" + modPath);
			// ���^�f�[�^����
			new MetaMixer(dstPath, modPath).saveAsATM(dstPath);
			// �f�[�^������
			new MultiMatch(dstPath, "full", modPath, delimiter).execute();
		}
		System.out.println("<createCurrentData end>------------------------");
	}

	//	public static void createCurrentData(HashMap<String, String> fieldMap) {
	//		System.out.println("<AliasRes.createCurrentData() begin>----------");
	//		System.out.println("<<�T�[�o�[���烍�[�J���ɑ������f�[�^���W�߂�>>--------");
	//		String outDir = ResControl.getMonSetsDir() + "atom/";
	//		System.out.println("################################ outDir:" + outDir);
	//		// ----------------------------------------------------------------
	//		// �p�o�q�A���P�[�g/03_�����E���N���/2014/ A01,A02,A03,A04
	//		// XXX 20140929 �����E���N��҂Ɋւ��鍀�ڗ񂪂Ȃ���Βǉ��A����ΌŒ荀�ڂ�ǉ�����悤�ɏ���������
	//		// ----------------------------------------------------------------
	//		// ���S�ɔC�ӂ̑��������擾����悤�ȃC���[�W�ɂȂ��Ă��邪�E�E�E
	//		// �w���f�[�^�̓���̍��ځi�J�n�I������ѐ��ѓ������j�͌Œ�őI�������悤�ɂ�����
	//		// ----------------------------------------------------------------
	//		List<String> outList = new ArrayList();
	//		for (String key : fieldMap.keySet()) {
	//			//			Joint.join(fieldMap.get(key)
	//			String field = fieldMap.get(key);
	//			String outname = saveAsAtomicsWrap(outDir, key, field);//20161130 check!!
	//			outList.add(outname);
	//		}
	//		String outPath = outList.get(0);
	//		System.out.println("###############################################");
	//		System.out.println(
	//				"## ���[�J���ɏW�߂�����������������=>C:/@qpr/home/Personal/MonSets/current.txt");
	//		System.out.println("###############################################");
	//		String dstPath = FileUtil_.changeExt(
	//				ResControl.getMonSetsDir() + ResControl.CURRENT, TXT);
	//		String modPath = FileUtil_.changeExt(outPath, TXT);
	//		//				System.out.println("������##-> modPath:" + modPath);
	//		//				System.out.println("������##-> dstPath:" + dstPath);
	//		outList.remove(0);
	//		//		String delimiter =",";
	//		String delimiter = "\t";//20150824
	//		new Atomics(modPath).saveAsATM(dstPath); // ���^�f�[�^����
	//		FileUtil.copyIt(dstPath, modPath); // �f�[�^������
	//		for (String element : outList) {
	//			modPath = FileUtil_.changeExt(element, TXT);
	//			//			System.out.println("������##-> modPath:" + modPath);
	//			// ���^�f�[�^����
	//			new MetaMixer(dstPath, modPath).saveAsATM(dstPath);
	//			// �f�[�^������
	//			new MultiMatch(dstPath, "full", modPath, delimiter).execute();
	//		}
	//		System.out.println("<createCurrentData end>------------------------");
	//	}
	@SuppressWarnings("unused")
	private String getSql() {
		return getSql(null, null, null);
	}

	@SuppressWarnings("unused")
	private String getSql(int col, int len, String AsName) {
		return getSql(new int[] { col }, new int[] { len },
				new String[] { AsName });
	}

	@SuppressWarnings("unused")
	private String getResConnect() {
		if (hRes == null)
			return null;
		return hRes.getValue(CONNECT);
	}

	@SuppressWarnings("unused")
	private String getValue(String key) {
		return hRes.getValue(key);
	}

	@SuppressWarnings("unused")
	private String getUrlValue(String key) {
		return map.get(key);
	}

	@SuppressWarnings("unused")
	private void saveAsTex(String outDir) {
		String outName = "ASM.txt";
		String key = getResKey();
		String field = getResField();
		String table = getResTable();
		String cond = getResCond();
		String sql = "select " + key + "," + field + " from " + table;
		if (cond != null) {
			sql = sql + " where (" + cond + ")";
		}
		// System.out.println("saveAsTex sql:" + sql);
		System.out.println("@saveAsCsv pass:" + outDir);
		// XXX �t�@�C�����Ȃǂ̃L�����N�^�Z�b�g�𒲐����Ă���
		String outPath = outDir + outName;
		// System.out.println(" aliasDir:" + aliasDir);
		// System.out.println(" outPath:" + outPath);
		DosEmu.copy(aliasDir + "QTB*.txt", outDir);
		// Schema.ini
		DosEmu.copy(aliasDir + SCHEMA_INI, outDir);
		// System.out.println("getSql2 >" + sql);
		// ---------------------------------------------------------------------
		// �����������s����
		// ---------------------------------------------------------------------
		Connector connector = new CommonConnector(this);
		kyPkg.task.TaskSQL2File task = new kyPkg.task.TaskSQL2File(outPath,
				connector, sql);
		task.setLiteral("\"");
		task.setDelimiter(",");// "\t"
		task.execute();
	}

	// -------------------------------------------------------------------------
	// Standalone�p�ɕϊ�����o�b�` 2009/07/10
	// �g�p�၄
	// String aliasDir = "G:/s2/rx/Enquetes/NQ/���𒲍��i�l�j/2009";
	// System.out.println("outDir:" + batchConvert(aliasDir));
	// -------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private static String batchConvert(String aliasDir) {
		aliasDir = FileUtil.normarizeIt(aliasDir);
		String[] splited = aliasDir.split("/");
		// String rootDir = kyPkg.uFile.ResControl.getRootDir();
		String outDir = ResControl.getUsersEnqDir()
				+ splited[splited.length - 2] + "/"
				+ splited[splited.length - 1] + "/";
		System.out.println("<<batchConvert>>-----------------Dir:" + outDir);
		System.out.println("#20130402#checkpoint 009");

		new AliasRes(aliasDir).saveAsStandAlone(outDir);
		return outDir;
	}

	// -------------------------------------------------------------------------
	// alias.txt�͈ȉ��̂悤�ȓ��e�E�E�E�E
	// connect DRIVER={SQL
	// Server};SERVER=KS1S003;UID=sa;PWD=;DATABASE=qprdb;Trusted_Connection=true
	// table TBL_NQMON2008
	// field CH_DAT
	// key CH_ID
	// Cond
	// -------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private void currentPcnv(int year, String path) {
		String[] array = ListArrayUtil.file2Array(path);
		for (String element : array) {
			String[] elements = element.split(TAB);
			System.out.println("elements[0]:" + elements[0]);
			// ���������Ɂh/�h��split���Ė��[���N�������Ă��邩�ǂ������肷��
		}
	}

}
