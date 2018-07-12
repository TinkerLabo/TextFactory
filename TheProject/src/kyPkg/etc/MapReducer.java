package kyPkg.etc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import globals.ResControl;
import kyPkg.converter.DateConverter;
import kyPkg.filter.CommonClojure;
import kyPkg.filter.EzReader;
import kyPkg.filter.Inf_BaseClojure;

//import kyPkg.util.File49ers;
//XXX	�W���ŃJ�E���^�[���Ō㕔�ɂ��Ă���̂̓I�v�V�����Ƃ�����
//XXX	������g�����āE�E�E�Ekey�P�ʂŁA�X�ʂ̏W�v�Ȃǂ������Ȃ�����
public class MapReducer implements Inf_BaseClojure {
	private static final String LF = System.getProperty("line.separator");

	private int[] condIndex = null;

	private String[] condRegix = null;

	private boolean countOption = false;

	private StringBuffer buff = null;

	private StringBuffer wkBuff = null;

	private boolean sumFlag = true; // false:���������Ȃ��A�㏑������i�P�ɏW�񂷂�ꍇ�Ɏg�p�j

	private boolean modFlag = false; // true:���łɑ��݂��镶����ɘA������,false:��ɏ㏑��

	private String modDelimiter = "\t";

	private String outPath = null; // �o��
	private int[] keyIndex = null; // key�Ƃ��鍀�ڂ��w���C���f�b�N�X
	private int[] mapIndex = null; // �v�f�Ƃ��鍀�ڂ��w���C���f�b�N�X
	private int[] sumIndex = null; // �������ލ��ڂ��w���C���f�b�N�X
	private String delimiter = "\t";

	private HashMap<String, MappedObject> hashMap;

	private ArrayList<String> keyList;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public MapReducer(String outPath, String keyCol, String sumCol,
			String delimiter) {
		this(outPath, cnv2array(keyCol), null, cnv2array(sumCol), delimiter);
	}

	public MapReducer(String outPath, String keyCol, String mapCol,
			String sumCol, String delimiter) {
		this(outPath, cnv2array(keyCol), cnv2array(mapCol), cnv2array(sumCol),
				delimiter);
	}

	private MapReducer(String outPath, int[] keyIndex, int[] mapIndex,
			int[] sumIndex, String delimiter) {
		this.outPath = outPath;
		this.keyIndex = keyIndex;
		this.mapIndex = mapIndex;
		this.sumIndex = sumIndex;
		this.delimiter = delimiter;
		// for (int i = 0; i < keyIndex.length; i++) {
		// System.out.println("################################# keyIndex["+i+"]:"+keyIndex[i]);
		// }
	}

	@Override
	public void init() {
		hashMap = new HashMap();
		buff = new StringBuffer();
		wkBuff = new StringBuffer();
		keyList = new ArrayList();
	}

	public String getSumCol() {
		int mapCnt = keyIndex.length;
		int sumCnt = sumIndex.length;
		if (countOption) {
			sumCnt += 1;
		}
		StringBuffer buff = new StringBuffer();
		buff.append("" + (mapCnt));
		for (int i = (mapCnt + 1); i < (mapCnt + sumCnt); i++) {
			buff.append("," + i);
		}
		return buff.toString();
	}

	public List getKeyList() {
		return keyList;
	}

	// ���������������ꍇ�A�Ώۈʒu�i�O���j�A���K�\���̂悤�Ɏw�肷��
	public void setCondition(int condIndex, String condRegix) {
		setCondition(new int[] { condIndex }, new String[] { condRegix });
	}

	public void setCondition(int[] condIndex, String[] condRegix) {
		if (condIndex.length != condRegix.length) {
			System.out.println("@MapReducer �����ݒ�G���[");
			return;
		}
		this.condIndex = condIndex;
		this.condRegix = condRegix;
	}

	private class MappedObject {
		private long counter = 0;

		private String[] strElement = null;

		private Integer[] numElement = null;

		// ---------------------------------------------------------------------
		// �R���X�g���N�^
		// ---------------------------------------------------------------------
		MappedObject() {
			this.counter = 0;
			if (mapIndex != null) { // ������������
				strElement = new String[mapIndex.length];
				for (int i = 0; i < strElement.length; i++) {
					strElement[i] = "";
				}
			}
			if (sumIndex != null) { // ������������
				numElement = new Integer[sumIndex.length];
				for (int i = 0; i < numElement.length; i++) {
					numElement[i] = new Integer(0);
				}
			}
		}

		// ---------------------------------------------------------------------
		// countOption �ŏI�Z���ɑ��݌������ڂ���Ȃ�true
		// ---------------------------------------------------------------------
		private String getResult(String delimiter) {
			StringBuffer buff = new StringBuffer();
			if (mapIndex != null) {
				for (int i = 0; i < strElement.length; i++) {
					if (buff.length() != 0)
						buff.append(delimiter);
					if (strElement[i] == null || strElement[i].equals("")) {
						buff.append(" "); // �f���~�^�[�����ފ֌W�ŁA�������Ă���
					} else {
						buff.append("�y");
						buff.append(strElement[i]);
						buff.append("�z");
					}
				}
			}
			if (sumIndex != null) {
				for (int i = 0; i < numElement.length; i++) {
					if (buff.length() != 0)
						buff.append(delimiter);
					if (numElement[i] == null) {
						buff.append(0);
					} else {
						buff.append(numElement[i].toString());
					}
				}
			}
			if (countOption == true) {
				if (buff.length() != 0)
					buff.append(delimiter);
				buff.append(Long.toString(counter)); // ���݌���
			}
			// System.out.println("=>" + buff.toString());
			return buff.toString();
		}

		// ---------------------------------------------------------------------
		// �������󔒂̂Ƃ��͏㏑�����Ȃ����Ƃɂ����E�E�E�l�̑��݂��鍀�ڂ̂ݗL��
		// ---------------------------------------------------------------------
		private void mapp(String[] array) {
			counter++;
			if (mapIndex != null) {
				for (int i = 0; i < mapIndex.length; i++) {
					if (array.length > mapIndex[i]) {
						String str = array[mapIndex[i]].trim();
						if (modFlag) {
							wkBuff.delete(0, wkBuff.length());
							if (strElement[i] != null)
								wkBuff.append(strElement[i]);
							if (wkBuff.length() > 0)
								wkBuff.append(modDelimiter); // �����^�u
							wkBuff.append(array[mapIndex[i]]);
							strElement[i] = wkBuff.toString();
							// System.out.println("?>"+strElement[i]);
						} else {
							if (!str.equals(""))
								strElement[i] = array[mapIndex[i]];
							// System.out.println("strs[i]:"+strs[i]);
						}
					}
				}
			}
			if (sumIndex != null) {
				for (int i = 0; i < sumIndex.length; i++) {
					if (array.length > sumIndex[i]) {
						try {
							if (sumFlag) {
								numElement[i] = numElement[i]
										+ Integer.parseInt(array[sumIndex[i]]);
							} else {
								numElement[i] = Integer
										.parseInt(array[sumIndex[i]]);
							}
							// System.out.println("ints[i]:"+ints[i]);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		private String getStrElement(int index) {
			if (strElement == null || strElement.length < index)
				return null;// ����ł����̂��H
			return strElement[index];
		}

		private int getNumElement(int index) {
			if (numElement == null || numElement.length < index)
				return -1;// Integer.MIN_VALUE;// ����ł����̂��H
			return numElement[index];
		}

		// private String getElement(int index) {
		// if (strElement == null || strElement.length < index)
		// return null;
		// return strElement[index];
		// }
		//
		// private Integer[] getSumElements() {
		// return numElement;
		// }
		//
		// private String[] getElements() {
		// return strElement;
		// }
	}

	public void setModFlag(boolean modFlag) {
		this.modFlag = modFlag;
	}

	// �����܂��ɂ��̃L�[�̏o�������o�͂���itrue�j
	public void setCountOption(boolean optCounter) {
		this.countOption = optCounter;
	}

	public void setSumFlag(boolean sumFlag) {
		this.sumFlag = sumFlag;
	}

	private static int[] cnv2array(String str) {
		// System.out.println("cnv >>"+str);
		if (str == null)
			return null;
		str = str.trim();
		if (str.equals(""))
			return null;
		// �p�����[�^��M�����ă`�F�b�N���Ă��Ȃ��A�������炸�I!
		String[] array = str.split(",");
		int[] intArray = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			try {
				intArray[i] = Integer.parseInt(array[i].trim());
				// System.out.println(">>"+intArray[i]);

			} catch (java.lang.NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return intArray;
	}

	// -------------------------------------------------------------------------
	// �������ʂ𕶎��z��ŕԂ��i�p�t�H�[�}���X�������E�E�E�j
	// -------------------------------------------------------------------------
	public String[] getStringArray() {
		String[] array = null;
		List list = this.getList();
		if (list != null)
			array = (String[]) list.toArray(new String[list.size()]);
		return array;
	}

	// -------------------------------------------------------------------------
	// �������ʂ�List�ŕԂ�
	// key + �v�f[] + summery[] + ����
	// -------------------------------------------------------------------------
	public List getList() {
		List list = new ArrayList();
		Set collectionView = hashMap.entrySet();
		Iterator iter = collectionView.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			java.util.Map.Entry ent = (java.util.Map.Entry) obj;
			String key = (String) ent.getKey();
			MappedObject val = hashMap.get(key);
			String rec = key + delimiter + val.getResult(delimiter);
			list.add(rec);
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// fin
	// -------------------------------------------------------------------------
	@Override
	public void write() {
		// System.out.println("DEBUG fin()@MapReducer");
		// �o�̓p�X���w�肳��Ă���ꍇ�̓t�@�C���o�͂����B
		saveAs(outPath);
	}

	// -------------------------------------------------------------------------
	// saveAs
	// -------------------------------------------------------------------------
	public int saveAs(String outPath) {
		return saveAs(outPath, delimiter);
	}

	public int saveAs(String outPath, String delimiter) {
		// System.out.println("DEBUG saveAs()@MapReducer:" + outPath);
		int counter = -1;
		if (outPath == null)
			return counter;
		if (outPath.equals(""))
			return counter;
		File file = new File(outPath);
		if (file.exists() && file.isFile() && !file.canWrite()) {
			System.out.println("#Error MapReducer@saveAs File can't Write:"
					+ outPath);
			return counter;
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			Set collectionView = hashMap.entrySet();
			Iterator iter = collectionView.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				java.util.Map.Entry ent = (java.util.Map.Entry) obj;
				String key = (String) ent.getKey();
				MappedObject val = hashMap.get(key);
				// String rec = key + delimiter + val.toSVStr(delimiter);
				// String rec = val.getResult(delimiter, optionFlag);
				String rec = key + delimiter + val.getResult(delimiter);
				bw.write(rec);
				bw.write(LF);
				counter++;
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return counter;
	}

	// -------------------------------------------------------------------------
	// SumIndex�Ɏw�肵�����ڂ𑫂�����
	// -------------------------------------------------------------------------
	@Override
	public void execute(String[] array) {
		buff.delete(0, buff.length());
		// �����Ƀ}�b�`���Ȃ���΁E�E�E�������Ȃ�
		if (condIndex != null) {
			for (int i = 0; i < condIndex.length; i++) {
				if (array.length > condIndex[i]) {
					if (!array[condIndex[i]].matches(condRegix[i]))
						return;
				}
			}
		}
		if (keyIndex != null) {
			buff.append(array[keyIndex[0]]);
			for (int i = 1; i < keyIndex.length; i++) {
				if (array.length > keyIndex[i]) {
					buff.append(delimiter);
					buff.append(array[keyIndex[i]]);
				}
			}
		}
		String key = buff.toString();
		buff.delete(0, buff.length());
		MappedObject obj = hashMap.get(key);
		// System.out.println("DEBUG @@key:" + key);
		if (obj == null) {
			obj = new MappedObject();
			if (hashMap.get(key) == null)
				keyList.add(key);
			hashMap.put(key, obj);
		}
		obj.mapp(array);

	}

	@Override
	public void execute(String rec) {
		String[] array = rec.split(delimiter);
		execute(array);
	}

	public String getStrElement(String key, int index) {
		String val = null;
		MappedObject obj = hashMap.get(key);
		if (obj != null) {
			val = obj.getStrElement(index);
		}
		return val;
	}

	public int getNumElement(String key, int index) {
		int val = -1;
		MappedObject obj = hashMap.get(key);
		if (obj != null) {
			val = obj.getNumElement(index);
		}
		return val;
	}

	public static void tester() {
		String userDir = ResControl.getQPRHome();
		String modItpPath = userDir + "modItp.txt";
		String mapRed1 = userDir + "MapReduce1.txt";

		String keyColumns = "0,3,5,"; // monitor,jan�i�Œ�j�A���t
		String numColumns = "1,2"; // ���������鍀��
		MapReducer closure = new MapReducer(mapRed1, keyColumns, numColumns,
				"\t");
		closure.setCountOption(true);

		String befYmd = "20090801";
		String aftYmd = "20091031";
		String sType = "Date";
		int cut = 14;

		DateConverter converter = new DateConverter(befYmd, aftYmd, sType, cut);
		EzReader ezReader = new EzReader(modItpPath);
		ezReader.addConverter(5, converter);
		new CommonClojure().incore(closure, ezReader);

	}

	public static void tester_1() {
		String userDir = ResControl.getQPRHome();
		String mapPath0 = userDir + "MapReduce0.txt";
		String mapPath2 = userDir + "MapReduce2.txt";
		String keyColumns = "1,2";
		String sumColumns = "3,4,5";
		String delimiter = "\t";
		MapReducer closure2 = new MapReducer(mapPath2, keyColumns, sumColumns,
				delimiter);
		// closure2.setOutPath(mapPath2);
		closure2.setCountOption(true);
		new CommonClojure().incore(closure2, mapPath0, true);
	}

	public static void test20100203() {
		// �w���f�[�^�̓��������f�[�^��Z�ߏグ��
		String userDir = globals.ResControl.getQPRHome();
		String mapPath0 = userDir + "148247000005/20090101_20091231.trn";
		String mapPath2 = userDir + "148247000005/convertedxxx.txt";
		String keyColumns = "0,5,3,4";
		String sumColumns = "1,2";
		String delimiter = "\t";
		MapReducer closure2 = new MapReducer(mapPath2, keyColumns, sumColumns,
				delimiter);
		closure2.setCountOption(true);
		new CommonClojure().incore(closure2, mapPath0, true);

	}

	public static void main(String[] argv) {
		test20100203();
	}

}
// �ȉ��g���p�߂��E�E�E�V���O���l���}���`�ɕϊ�����΂����̃��W�b�N�A�o���񐔂��E�� ���̃N���X��elements�Ƃ���
// class StrElements {
// HashMap<String, Integer> map;
// StrElements(){
// map=new HashMap();
// }
// public void mapp(String key){
// Integer wInt = map.get(key);
// if (wInt==null){
// map.put(key, wInt++);
// }else{
// map.put(key, new Integer(1));
// }
// }
// public List getList() {
// List list = new ArrayList();
// Set collectionView = map.entrySet();
// Iterator iter = collectionView.iterator();
// while (iter.hasNext()) {
// Object obj = iter.next();
// java.util.Map.Entry ent = (java.util.Map.Entry) obj;
// String key = (String) ent.getKey();
// Integer val = map.get(key);
// String rec = key + delimiter + val ;
// list.add(rec);
// }
// return list;
// }
// }
