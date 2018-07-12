package kyPkg.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kyPkg.rez.HashList;
import kyPkg.uFile.HashMapUtil;

// ----------------------------------------------------------------------------
//�@������������ϊ����邽�߂̃}�b�v���i�e�L�X�g�t�@�C�����j��������
// ----------------------------------------------------------------------------
public class CnvDictionary implements Inf_StrConverter {
	private static final String COMMA = ",";
	private int grade = 0;
	private HashList hashList; // �������̂���n�b�V���}�b�v�H�ł͂Ȃ�
	private Inf_StrConverter converter;
	private HashSet<String> keyNotFound;

	public HashSet<String> getKeyNotFound() {
		return keyNotFound;
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	protected CnvDictionary() {
		super();
		hashList = new HashList();
		keyNotFound = new HashSet();
	}

	public CnvDictionary(String path, int keyCol, int valCol) {
		this();
		hashList = HashMapUtil.file2HashList(path, keyCol, valCol);
	}

	protected CnvDictionary(String path) {
		this(path, 0, 1); // default�A�ł̓[���Ԗڂ��L�[�J�����ƂȂ�A�P�Ԗڂ��l�̃J�����ƂȂ�
	}

	protected CnvDictionary(Set<String> set) {
		this();
		List<String> list = new ArrayList(set);
		Collections.sort(list);
		hashList = incoreList(list, COMMA);
	}

	protected CnvDictionary(List list) {
		this();
		hashList = incoreList(list, COMMA);
	}

	// ------------------------------------------------------------------------
	// �A�N�Z�b�T
	// ------------------------------------------------------------------------
	public void setConverter(Inf_StrConverter converter) {
		this.converter = converter;
	}

	// ------------------------------------------------------------------------
	// �L�[�ꗗ�����X�g�ŕԂ�
	// ------------------------------------------------------------------------
	public List<String> getKeyList() {
		return hashList.getKeyList();
	}

	// ------------------------------------------------------------------------
	// �l�ꗗ��z��ŕԂ�
	// ------------------------------------------------------------------------
	public String[] getValArray() {
		return hashList.getValStrArray();
	}

	// ------------------------------------------------------------------------
	// �l�擾
	// ------------------------------------------------------------------------
	public String getValue(String key) {
		if (hashList == null)
			return "null";
		String ans = (String) hashList.get(key);
		if (ans != null) {
			return ans;
		} else {
			if (converter != null) {
				String val = converter.convert(key);
				keyNotFound.add(val);
				return val;
			} else {
				return null;
			}
		}
	}

	// ------------------------------------------------------------------------
	// �o�͐�Z���ʒu�擾   �ǂ̃Z���ɏo�͂��邩
	//�@�����ҏW�����ꍇ�A�o�͐���ύX���Ȃ���΂Ȃ�Ȃ�
	// ------------------------------------------------------------------------
	public int getDestPos(String key) {
		if (hashList == null)
			return -1;
		return hashList.getIndex(key);
	}

	// ------------------------------------------------------------------------
	// getSize
	// ------------------------------------------------------------------------
	public int getSize() {
		if (hashList == null)
			return -1;
		return hashList.size();
	}

	public void remove(String key) {
		hashList.remove(key);
	}

	// ------------------------------------------------------------------------
	//�@���ڌ��菈���i�K�v�ȍ��ڂ݂̂Ɍ��肷��j
	//TODO	��������̕ύX���s���������E�E�E�ǂ����낤
	//�@����ł͎w�肳��Ă��Ȃ����ڂ��������铮���ɂȂ��Ă��邪�����ł͂Ȃ��w�肳�ꂽ���ڂ��������̏����ŏE��������`�ɏ��������Ă݂�E�E�E�ǂ����낤
	// ------------------------------------------------------------------------
	public List<Integer> limit(String[] limitKeys) {
		//20150730 test
		//			resDict.remove("0");//�s�v�ȍ���
		//			resDict.remove("2");
		//			resDict.remove("4");
		//			resDict.remove("0");
		//			resDict.limit(new String[] { "0", "1", "2", "3", "4" });//�K�v�ȍ��ڂ݂̂Ɍ���
		if (limitKeys == null)
			return null;
		return hashList.limitByArray(limitKeys);
	}

	public void put(String key, String val) {
		hashList.put(key, val);
	}

	private HashList incoreList(List<String> list, String delimiter) {
		HashList hashList = new HashList();
		// Collections.sort(list);
		int counter = -1;
		counter = 0;
		for (String element : list) {
			if (element != null && (!element.equals(""))) {
				counter++;
				String[] splited = element.split(delimiter);
				if (splited.length >= 2) {
					hashList.put(splited[0], splited[1]);
				} else if (splited.length == 1) {
					//�P�Z�����������Ȃ��ꍇ�A���̈ʒu���L�[�Ƃ��āA�[���Ԗڂ��l�ƂȂ�
					//�ʒu��ZERO����n�܂�H�H
					//System.out.println("#TAG# key:" + String.valueOf(counter)	+ " val:" + splited[0]);
					hashList.put(String.valueOf(counter), splited[0]);
				}
			}
		}
		return hashList;
	}

	@Override
	public String convert(String key) {
		int seq = getDestPos(key);
		return String.valueOf(seq + grade);// XXX grade�̏��͌v�Z���ɂ��������֗����Ǝv��
	};

	protected void eumKeys() {
		List<String> keyList = hashList.getKeyList();
		for (String key : keyList) {
			System.out.println("eumKeys key:" + key);
		}
	}

	public void debug() {
		System.out.println("-------------------------------------------------");
		System.out.println("Dictionary dic = new Dictionary();");
		String[] keys = hashList.getKeyArray();
		String[] Vals = hashList.getValStrArray();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				System.out.println("dic.put(\"" + keys[i] + "\",\"" + Vals[i]
						+ "\");");
			}
		}
		System.out.println("-------------------------------------------------");
	}

	public static void main(String[] args) {
	}
}
