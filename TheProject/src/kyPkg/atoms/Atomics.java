package kyPkg.atoms;

import static kyPkg.sql.ISAM.CSV_DELIMITED;
import static kyPkg.sql.ISAM.TAB_DELIMITED;
import static kyPkg.util.KUtil.ModArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.converter.CnvMap;
import kyPkg.filter.CommonClojure;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;
import kyPkg.util.*;

public class Atomics {
	private static final String KEY = "KEY";
	private static final String MAP = "MAP";
	private static final String NUM = "NUM";
	private static final String MAP_NAMES = "MAP_NAMES";
	private static final String NUM_NAMES = "NUM_NAMES";
	private static final String UNIT = "UNIT";
	private static final String MAP_OCC = "MAP_OCC";
	private static final String TAB = "\t";
	private static final String ATM = "atm";
	private static final String TAG_VER1 = "T_";
	private static final String TAG_VER0 = "TAG_";
	private static final String wLF = System.getProperty("line.separator");
	private static final String naName = "NA"; // NA�ɂ��閼��

	protected static String getEXT() {
		return ATM;
	}

	private String otherName = ""; // other�ɂ��閼��
	private HashMap<String, String> hashMap = null;

	// �V���A���C�Y�ł��Ȃ��E�E�E�v���p�e�C �^�O�͂ǂ����悤�H ���V���O���A���T�[�����Ή����Ă��Ȃ��I�I
	// -------------------------------------------------------------------------
	// �ȉ��̃v���p�e�B�͑Ή�����Element��length�Ƀt�F�[�Y�����킹��
	// -------------------------------------------------------------------------
	private String[] version = null; // version
	private int[] keyElement = null; // �j�����G�������g
	private int[] mapElement = null; // �l�����G�������g
	private int[] numElement = null; // �m�����G�������g
	private int[] mapOcc = null; // �e�l�����G�������g�̃I�J�����X�i�P�Ȃ�V���O���A���T�[�j
	private String[] mapTitle = null; // �e�l�����G�������g�̖��O
	private String[] numTitle = null; // �e�m�����G�������g�̖��O
	private String[] numUnit = null; // �e�m�����G�������g�̒P��
	private ArrayList<String[]> tagName = null; // �^�O�����X�g�i�e�l�����G�������g�ɑΉ��j
	private CnvMap[] mapConv = null; // �}�b�v�̒l��ϊ����鑕�u
	private String name = null;
	private String dirPath = null;
	private String format = CSV_DELIMITED;//default

	protected String getDirPath() {
		return dirPath;
	}

	// --------------------------------------------------------------
	// �R���X�g���N�^
	// --------------------------------------------------------------
	public Atomics() {
		this.hashMap = new HashMap();
		this.tagName = new ArrayList();
	}

	// --------------------------------------------------------------
	// ���j�^�[�������i�A���P�[�g���Ɋ܂܂��v�f�j�����[�J���ɐ�������
	// --------------------------------------------------------------
	public Atomics(String dataPath) {
		this();
		// dataPath�����ݒ�Ȃ�J�����g���w�肷�� =>C:/@qpr/home/Personal/MonSets/current.txt
		if (dataPath == null || dataPath.trim().equals(""))
			dataPath = ResControl.getCurrentPath();
		incore(dataPath);
	}

	private void incore(String path) {
		System.out.println("#################################################");
		System.out.println("### Atomics �R���X�g���N�^  Path:" + path);
		System.out.println("#################################################");
		String delimiter = new File49ers(path).getDelimiter();
		if (delimiter.equals(TAB)) {
			format = TAB_DELIMITED;
		} else {
			format = CSV_DELIMITED;
		}
		if (FileUtil.isExists(path)) {
			HashMap<String, String> pHmap = incoreMeta(path);
			mapIncore(pHmap);
		} else {
			System.out.println("@Atomics file not exists:" + path);
		}
	}

	// --------------------------------------------------------------
	// �t�@�C������ǂݍ���
	// --------------------------------------------------------------
	private HashMap incoreMeta(String path) {
		String encode = FileUtil.windowsDecoding;
		name = FileUtil.getFirstName2(path);
		dirPath = FileUtil.getParent2(path, true);
		String metaPath = FileUtil.changeExt(path, ATM);
		//		System.out.println("## Atomics incoreMeta  metaPath:" + metaPath);
		return file2HashMap(metaPath, encode);
	}

	public String getFormat() {
		return format;
	}

	protected String getName() {
		return name;
	}

	protected int[] getKeyElement() {
		return keyElement;
	}

	protected int[] getMapElement() {
		return mapElement;
	}

	protected int[] getNumElement() {
		return numElement;
	}

	// --------------------------------------------------------------
	// ��ԍŌ��map�J����
	// --------------------------------------------------------------
	protected int getLastmapCol() {
		int lastmapCol = -1;
		for (int i = 0; i < mapElement.length; i++) {
			if (lastmapCol < mapElement[i]) {
				lastmapCol = mapElement[i];
			}
		}
		return lastmapCol;
	}

	private void mapIncore(HashMap<String, String> pHmap) {
		if (pHmap != null) {
			this.hashMap = pHmap;
			parseIt();
		}
	}

	// --------------------------------------------------------------
	// �t�@�C������ǂݎ�����n�b�V��������ϐ��ɏ�������
	// --------------------------------------------------------------
	private void parseIt() {
		keyElement = KUtil.split2Int(hashMap.get(KEY));
		mapElement = KUtil.split2Int(hashMap.get(MAP));
		numElement = KUtil.split2Int(hashMap.get(NUM));
		// System.out.println("@parse mapElement.length:"+mapElement.length);
		if (mapElement != null) {
			mapConv = new CnvMap[mapElement.length];
			//
			mapOcc = KUtil.split2Int_Lim(hashMap.get(MAP_OCC),
					mapElement.length, 1);
			mapTitle = KUtil.split2Str_Lim(hashMap.get(MAP_NAMES),
					mapElement.length);
			// ----------------------------------------------------------------
			// ���O���g���~���O
			// ----------------------------------------------------------------
			mapTitle = spaceTrim(mapTitle);
			// // ���O���g���~���O�E�E�E2009/10/20
			// for (int i = 0; i < mapTitle.length; i++) {
			// mapTitle[i] = mapTitle[i].replaceAll("�@", " ");// �S�pSPACE���p��
			// mapTitle[i] = mapTitle[i].trim();
			// mapTitle[i] = mapTitle[i].replaceAll(" ", "�@");// ���pSPACE�S�p��
			// // System.out.println("@parse mapTitle[i]:"+mapTitle[i]);
			// }
		}
		if (numElement != null) {
			numTitle = KUtil.split2Str_Lim(hashMap.get(NUM_NAMES),
					numElement.length);
			numUnit = KUtil.split2Str_Lim(hashMap.get(UNIT), numElement.length,
					"000");
		}
		if (hashMap.containsKey("VER")) {
			version = (hashMap.get("VER")).split(",");
		} else {
			version = null;
		}
		tagName = new ArrayList();
		if (version == null) {
			for (int i = 0; i < mapElement.length; i++) {
				String[] array = KUtil.splitStr(hashMap.get(TAG_VER0 + i));
				tagName.add(array);
			}
		} else {
			for (int i = 0; i < mapElement.length; i++) {
				int col = mapElement[i];
				String[] array = KUtil.splitStr(hashMap.get(TAG_VER1 + col));
				tagName.add(array);
			}
		}
	}

	// ---------------------------------------------------------------------
	// ������̌㕔�ɘA������A���X�y�[�X����苎��
	// ---------------------------------------------------------------------
	public static String[] spaceTrim(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = spaceTrim(array[i]);
		}
		return array;
	}

	public static String spaceTrim(String inStr) {
		String outStr = inStr.replaceAll("�@", " ");// �S�p�X�y�[�X�𔼊p��
		outStr = outStr.trim();
		return outStr.replaceAll(" ", "�@");// ���p�X�y�[�X��S�p�l
	}

	// --------------------------------------------------------------
	// �ۑ�����
	// --------------------------------------------------------------
	// KEY 0
	// MAP 2,3
	// NUM 4,5,6
	// MAP_NAMES �敪�P,�敪�Q
	// NUM_NAMES ����,���z��T���v����
	// UNIT 000,001,000
	// TAG_0 ���̂P,���̂Q,���̂R,���̂S,���̂T,���̂U,���̂V,���̂W,���̂X,���̂P�O
	// TAG_1 ���̂P,���̂Q,���̂R,���̂S,���̂T,���̂U,���̂V,���̂W,���̂X,���̂P�O
	// --------------------------------------------------------------

	// XXX�@�T�[�o�[��Ɉ�ʓI�ȑ����f�[�^���o�b�`�Ő������Ă����E�E�E����𖈓��R�s�[����H�I
	// ## Atomics saveAs Path=>C:/Documents and
	// Settings/Administrator.AGC/QPR/Personal/MonSets/current.txt
	// ## Atomics saveAs metaPath=>C:/Documents and
	// Settings/Administrator.AGC/QPR/Personal/MonSets/current.atm
	public int saveAsATM(String path) {
		String metaPath = kyPkg.uFile.FileUtil.changeExt(path, ATM);
		// System.out.println("#saveAsATM# Atomics saveAs      Path=>" + path);
		System.out.println("#saveAsATM# ���^�f�[�^���o�͂���  metaPath=>" + metaPath);
		if (mapElement == null) {
			System.out.println(
					"#saveAsATM# Atomics saveAs      mapElement==null");
			return -1;
		}
		// String metaPath = destPathxx + EXT;
		// XXX VERSION���������������E�E�E�ǂ����悤���H�H
		// �������ڂ��n�b�V���}�b�v�ɏ����߂�
		// �]�v�ȃX�y�[�X����蕥��
		mapTitle = spaceTrim(mapTitle);
		hashMap.put(KEY, KUtil.join(keyElement));
		hashMap.put(MAP, KUtil.join(mapElement));
		hashMap.put(NUM, KUtil.join(numElement));
		hashMap.put(MAP_NAMES, KUtil.join(mapTitle));
		hashMap.put(NUM_NAMES, KUtil.join(numTitle));
		hashMap.put(UNIT, KUtil.join(numUnit));
		hashMap.put(MAP_OCC, KUtil.join(mapOcc));

		// �}�b�v�R���o�[�^�̓V���A���C�Y�ł��Ȃ��E�E�E�A�h�z�b�N��
		if (version == null) {
			// version = new String[]{"1.0"};
			// hashMap.put("VER", KUtil.joinStr(version));
			for (int i = 0; i < mapElement.length; i++) {
				String[] array = tagName.get(i);

				if (array != null) {
					// �]�v�ȃX�y�[�X����蕥��
					array = spaceTrim(array);

					// for (int j = 0; j < array.length; j++) {
					// array[j] = (array[j].replaceAll("�@", " ")).trim();
					// }
					String tag = TAG_VER0 + i;
					//					System.out.println("tag:"+tag +" =>"+KUtil.join(array));
					hashMap.put(tag, KUtil.join(array));
				}
			}
		} else {
			hashMap.put("VER", KUtil.join(version));
			for (int i = 0; i < mapElement.length; i++) {
				String[] array = tagName.get(i);
				if (array != null) {
					// �]�v�ȃX�y�[�X����蕥��
					array = spaceTrim(array);

					// for (int j = 0; j < array.length; j++) {
					// array[j] = (array[j].replaceAll("�@", " ")).trim();
					// }
					String tag = TAG_VER1 + mapElement[i];
					hashMap.put(tag, KUtil.join(array));
				}
			}
		}
		System.out.println(
				"#####################################################");
		return hashMap2File(metaPath, hashMap);
	}

	// --------------------------------------------------------------
	// 2��̃^�u��؂�e�L�X�g��ǂݍ��݁A�O��ڂ��L�[��1��ڂ�l�Ƃ��ăn�b�V���}�b�v��put���Ă����Ԃ�
	// --------------------------------------------------------------
	// Sample
	// --------------------------------------------------------------
	// KEY 0
	// MAP 1,2
	// MAP_NAMES ���ѓ����c��w�N��,���ю�N��
	// MAP_OCC 1,1
	// NUM
	// NUM_NAMES
	// TAG_0 ��Y��,�`�Q�X��,�`�R�X��,�`�S�X��,�`�T�X��,�U�O�ˁ`
	// TAG_1 ��Y��,�`�Q�X��,�`�R�X��,�`�S�X��,�`�T�X��,�U�O�ˁ`
	// UNIT
	// --------------------------------------------------------------
	private HashMap file2HashMap(String path, String encode) {
		HashMap<String, String> hashMap = new HashMap();
		if (new File(path).exists() == false) {
			System.out.println("# ERROR # text2HashMap FileNotFound:" + path);
			return null;
			// return hashMap;
		}
		try {
			String rec;
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), encode));
			while (br.ready()) {
				rec = br.readLine();
				if (rec != null && (!rec.equals(""))) {
					String[] splited = rec.split(TAB);
					if (splited.length >= 2) {
						// System.out.println("0:"+splited[0].trim()+" 1:"+splited[1]);
						hashMap.put(splited[0].trim(), splited[1]);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return hashMap;
	}

	// --------------------------------------------------------------------
	// �n�b�V���}�b�v���t�@�C���ɏ����o��
	// --------------------------------------------------------------------
	int hashMap2File(String metaPath, HashMap<String, String> hashMap) {
		int count = 0;
		try {
			kyPkg.uFile.FileUtil.makeParents(metaPath);
			BufferedWriter bw = new BufferedWriter(new FileWriter(metaPath));
			List<String> keyList = new ArrayList(hashMap.keySet());
			Collections.sort(keyList);
			for (String key : keyList) {
				String val = hashMap.get(key);
				bw.write(key);
				bw.write(TAB);
				bw.write(val);
				bw.write(wLF);
				//				System.out.println("#hashMap2File key:"+key+" val:"+val);
				count++;
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			// return -1;
			System.exit(0);
		}
		return count;
	}

	protected void addTagName(int col, String title, List list) {
		// System.out.println("##addTagName##");
		if (col <= 0)
			return;
		if (title == null)
			return;
		if (list == null)
			return;
		mapElement = ModArray(mapElement, col);
		mapTitle = ModArray(mapTitle, title);
		String[] tagArray = (String[]) list.toArray(new String[list.size()]);
		tagName.add(tagArray);
	}

	public void modTagName(String value, String delimiter) {
		// System.out.println("##modTagName##");
		if (value == null)
			return;
		this.tagName.add(value.split(delimiter));
	}

	protected void setTagName(ArrayList<String[]> tagName) {
		// System.out.println("##setTagName##");
		if (tagName == null)
			return;
		this.tagName = tagName;
	}

	public String[] getTag(int seq) {
		String[] array = null;
		CnvMap mapConv = getMapConv(seq);
		if (mapConv != null) {
			array = mapConv.getTagArray();// XXX ��֖��E�E�E���v���H�H@@@
		} else {
			if (tagName.size() >= seq) {
				array = tagName.get(seq);
			} else {
				System.out.println("#ERROR @getTag seq:" + seq);
				System.out.println(
						"#ERROR @getTag tagName.size():" + tagName.size());
				array = null;
			}
		}
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				array[i] = (array[i].replaceAll("�@", " ")).trim();
			}
		}
		return array;
	}

	public List getTagList(int seq) {
		String[] array = getTag(seq);
		if (array == null)
			return null;
		List list = Arrays.asList(array);
		return list;
	}

	// -------------------------------------------------------------------------
	// ���̑���t��������ꍇ���l���E�E�E�E
	// XXX ���̑���NA�����̓N���X�x�[�X�ł͂Ȃ��ق����悳�����Ȃ̂ŗv�C�����ȁE�E�E
	// -------------------------------------------------------------------------
	public String[] getTagNa(int seq) {
		String[] array = getTag(seq);
		if (otherName.equals("")) {
			return array;
		} else {
			String[] rtnArray = new String[array.length + 1];
			if (mapOcc[seq] == 1) {
				rtnArray[0] = otherName;
			} else {
				rtnArray[0] = naName;
			}
			for (int i = 0; i < array.length; i++) {
				rtnArray[i + 1] = array[i];
			}
			return rtnArray;
		}
	}

	// -------------------------------------------------------------------------
	// �Y���l�`�o�̃^�O�̃T�C�Y��Ԃ�(���̑���t��������ꍇ���l������Ă���E�E�E�E)
	// -------------------------------------------------------------------------
	public int getSize(int seq) {
		String[] array = getTagNa(seq);
		if (array == null)
			return 0;// XXX ����ł����̂��ǂ����^�� �A-1�ł͌듮�삵���Ƃ���₱�����̂�
		return array.length;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	// -------------------------------------------------------------------------
	// private String dataPath=""; // �Ή�����f�[�^�̃p�X
	// ���Ƃłɒǉ����Ă��������p�����[�^
	// VER �o�[�W�����i���o�[
	// NAME �f�[�^�̖��O�i�ȗ��j
	// TARGET �Ώۃf�[�^�̃p�X�i�ȗ��j
	// OWNER �I�[�i�[
	// CATEGORY �Ώەi��
	// TERM �Ώۊ���
	// SAMPLE �T���v����
	// COMMENT ���o�����ȂǁE�E�E�E
	// -------------------------------------------------------------------------
	// �ʒu���i�[������n�܂�j�R���e�i�Ȃ̂ł���Ȃ�̃A�N�Z�b�T���p�ӂ���
	// -------------------------------------------------------------------------
	protected void setMapConv(CnvMap[] mapConv) {
		this.mapConv = mapConv;
	}

	protected void setMapConv(int seq, CnvMap pMapConv) {
		if (mapConv == null)
			mapConv = new CnvMap[mapElement.length]; // TODO �������I
		this.mapConv[seq] = pMapConv;
	}

	protected CnvMap getMapConv(int seq) {
		if (mapConv == null)
			return null;
		// System.out.println("debug@getMapConv mapConv.length:"+mapConv.length);
		// System.out.println("debug@getMapConv seq:"+seq);
		// String bug20100826;
		if (mapConv.length <= seq)
			return null;
		return mapConv[seq];
	}

	// --------------------------------------------------------------
	// �񖼂̓��j�[�N�łȂ���΂Ȃ�Ȃ��E�E�E���Ƃɂ��悤�I�I�����
	// --------------------------------------------------------------
	protected int getColSeq(String title) {
		for (int i = 0; i < mapTitle.length; i++) {
			if (title.equals(mapTitle[i])) {
				return mapElement[i];
			}
		}
		return -1;
	}

	// -------------------------------------------------------------------------
	// �Y���l�`�o�̃^�O����Ԃ� <Original Version>
	// -------------------------------------------------------------------------
	public String[] getTagsByName(String title) {
		for (int i = 0; i < mapTitle.length; i++) {
			if (title.equals(mapTitle[i])) {
				return tagName.get(i);
				// String[] array = KUtil.splitStr(hashMap.get(TAG_VER0 + i));
				// return array;
			}
		}
		return null;
	}

	public void setKeyElement(List keyList) {
		this.keyElement = KUtil.list2intArray(keyList);
	}

	public void setMapElement(List mapList) {
		this.mapElement = KUtil.list2intArray(mapList);
	}

	public void setNumElement(List numList) {
		this.numElement = KUtil.list2intArray(numList);
	}

	public void setKeyElement(int[] keyElement) {
		this.keyElement = keyElement;
	}

	public void setMapElement(int[] mapElement) {
		this.mapElement = mapElement;
	}

	public void setMapOcc(int[] mapOcc) {
		this.mapOcc = mapOcc;
	}

	public void setMapTitle(String[] mapTitle) {
		this.mapTitle = mapTitle;
	}

	public void setNumElement(int[] numElement) {
		this.numElement = numElement;
	}

	public void setNumTitle(String[] numTitle) {
		this.numTitle = numTitle;
	}

	public void setNumUnit(String[] numUnit) {
		this.numUnit = numUnit;
	}

	// --------------------------------------------------------------
	// �I�J�����X���P�Ȃ�V���O�����ځA����ȏ�Ȃ�}���`�A���T�[�Ƃ���i���\�����E�E�E�ȒP�ׁ̈j
	// --------------------------------------------------------------
	public int getMapOcc(int seq) {
		if (mapOcc.length <= seq)
			return -1; // error ...1�ł��ǂ�����
		return mapOcc[seq];
	}

	public int getKeyElement(int seq) {
		if (keyElement.length <= seq)
			return -1;
		return keyElement[seq];
	}

	public int getMapElement(int seq) {
		if (mapElement.length <= seq)
			return -1;
		return mapElement[seq];
	}

	public int getNumElement(int seq) {
		if (numElement.length <= seq)
			return -1;
		return numElement[seq];
	}

	public String getMapTitle(int seq) {
		if (mapTitle.length <= seq)
			return null;
		return mapTitle[seq];
	}

	public String[] getParmArray() {
		return mapTitle;
	}

	public String getNumTitle(int seq) {
		if (numTitle.length <= seq)
			return null;
		return numTitle[seq];
	}

	public String[] getNumTitle() {
		return numTitle;
	}

	public String getNumUnit(int seq) {
		if (numUnit.length <= seq)
			return null;
		return numUnit[seq];
	}

	public List<String> getChileList(int mapCol, String delim) {
		List<String> list = new ArrayList();
		String mapName = getMapTitle(mapCol);
		String[] tagNames = getTag(mapCol);
		int mapOcc = getMapOcc(mapCol);
		for (int i = 0; i < tagNames.length; i++) {
			// �@�^�O����=�񓚑I�����A�񓚒l�A�}�b�v�����ݖ�A�}�b�v�V�[�P���X���}�b�v�J�����A�I�J�����X
			list.add(tagNames[i] + delim + (i + 1) + delim + mapName + delim
					+ mapCol + delim + mapOcc);
		}
		return list;
	}

	public void mapTitles2Combo(JComboBox comboBox) {
		copyArray2Combo(comboBox, getParmArray());
		return;
	}

	public void copyArray2Combo(JComboBox comboBox, String[] array) {
		// �����ɈӖ���������������̂Ń\�[�g�������Ă͂����Ȃ�
		if (array == null)
			return;
		comboBox.removeAllItems();
		List<String> list = Arrays.asList(array);
		if (list == null)
			return;
		for (String element : list) {
			comboBox.addItem(element);
		}
		comboBox.setSelectedIndex(0);
	}

	// ------------------------------------------------------------------------
	// XXX �}���`�A���T�[���ڂȂ̂��A�V���O���A���T�[���ڂȂ̂��H���ꂼ��ǂ��g���̂��H
	// XXX ���l���ڂ����ꍇ�A�����Ȃ��ꍇ�E�E�E�g���q�͕ς���H����Ƃ����ׂĐ��l���ڂ��������邩�H
	// XXX �w���f�[�^�Ȃ̂��ǂ����A���肳����K�v�͂���̂����m��Ȃ��A���ɂ������̂�����E�E�Eatx�Ƃ���H
	// ����������ꍇ�E�E�EMETA�f�[�^�̍������s��Ȃ���΂Ȃ�Ȃ����A�^�C�g���̓��j�[�N�ɂ��Ȃ���΂Ȃ�Ȃ�
	// ���R�F�@�B�I�ɍ��ڂ��������́i����j�ł��邩���肷��̂Ɏg������ �၄�Ί��Ԕ�r�Ȃǂ��s���ꍇ�Ȃ�
	// �������ڂ��n�b�V���}�b�v�ɏ����߂�
	// --------------------------------------------------------------
	// �l��������������
	// --------------------------------------------------------------
	public void remove(int seq) {
	};

	// --------------------------------------------------------------
	// �l���������� �i��-�P�j�{��??�ɂ���č�������i���[�������ɍ�������j
	// --------------------------------------------------------------
	public void composeIt(int seq1, int seq2) {
	};

	// --------------------------------------------------------------
	// �G���e�B�e�B�Ԃ̘A��������
	// �P�����̂��߂m�����������Ă���G���e�B�e�B�͍��Ӂi���j�ɂ̂ݎw��Ƃ���
	// �m�������ڂ�A������Ɓ����z�P�A���z�Q�ȂǂƖ���������b����₱�����Ȃ�
	// ���l�Ԃ̔�r���w�肳����ꍇ�Ȃǂ��S�e�S�e���ă_�T�C���Ȃ�\��������
	// �i�������A�m�����������Ă���G���e�B�e�B���ǂ����ǂ�����Ĕ��肷��́H�j
	// �e�Ɋp�A���R�x��������Ȃ��悤�ɒ��ӂ��悤�E�E�E
	// --------------------------------------------------------------
	public void synthesize(String path1, String path2, int[] element1,
			int[] element2) {
	};

	// --------------------------------------------------------------
	// �l�����̍\����ύX�i�J�e�S���[�ϊ��j3���Ԃ����ɕϊ��Ƃ��E�E����������
	// --------------------------------------------------------------
	public void filtering(int seq, CnvMap cnvMap) {
	};

	public static void main(String[] args) {
		//		test00();
	}

	// ------------------------------------------------------------------------
	//	20150811 for debug
	// ------------------------------------------------------------------------

	public static void test00() {
		int[] cols = { 1, 2 };// �~�����J�����̏��
		// String zappa = "20091105";
		String userDir = globals.ResControl.getQPRHome();
		String enqPath = userDir + "current";
		System.out.println("20121004debug @test00 dstPath:" + enqPath);
		System.out.println("# checkpoint 002 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(enqPath);
		// ------------------------------------------------------
		// ���̃J�����̃f�[�^�l�ɑΉ�����^�O���̂��ق����E�E�E
		// ------------------------------------------------------
		HashMap<String, String>[] mapArray = new HashMap[cols.length];
		for (int i = 0; i < mapArray.length; i++) {
			mapArray[i] = new HashMap<String, String>();
			String[] tags = insAtomics.getTag(i);
			for (int j = 0; j < tags.length; j++) {
				String tag = tags[j];
				String key = String.valueOf(j);
				System.out.println("key>>" + key + " tag>>" + tag);
				mapArray[i].put(key, tag);
			}
		}
		int i = 1;
		System.out.println("tag 1:" + mapArray[i].get("1"));
		System.out.println("tag 2:" + mapArray[i].get("2"));
		System.out.println("tag 3:" + mapArray[i].get("3"));
		System.out.println("tag 4:" + mapArray[i].get("4"));
		System.out.println("tag 5:" + mapArray[i].get("5"));
	}

	public static void test01() {
		String atmPath = ResControlWeb
				.getD_Resources_PUBLIC("commonstock/elements/freakoutAtm.atx");
		System.out.println("20121004debug @test00 dstPath:" + atmPath);
		System.out.println("# checkpoint 003 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(atmPath);
		int[] dim1 = { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
		int[] dim2 = { 1, 0, 1, 0 };
		int[] val = { 1 };
		Map2Matrix closure = new Map2Matrix(insAtomics, dim1, dim2, val);
		closure.setOutPath(ResControlWeb.getD_Resources_PUBLIC(
				"commonstock/elements/freakoutAtm.json"));
		String srcFile = ResControlWeb
				.getD_Resources_PUBLIC("commonstock/elements/freakoutAtm.txt");
		new CommonClojure().incore(closure, srcFile, true);
	}
}
