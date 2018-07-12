package kyPkg.filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import kyPkg.uFile.FileUtil;

public class DictionaryControl {
	// XXX �n�b�V���ł͂Ȃ��A�o�͗p�z�u�C���f�b�N�X�ɒu�������郍�W�b�N�͓�����H�H
	private static final String LF = System.getProperty("line.separator");
	public static final int ROW_AS_VALUE = -1;
	public static final int ROW_AS_KEY = -2;

	public static void debugHmap(HashMap<String, String> hmap) {
		System.out.println("<For Debug>-------------------------------------");
		java.util.Set set = hmap.entrySet(); // ����iterator���ĂׂȂ��̂ň�USET���擾����
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			String val = (String) ent.getValue();
			System.out.println(
					"DictionaryControl@debugHmap�@key:" + key + "  val:" + val);
		}
		System.out.println("<For Debug>-------------------------------------");
	}

	public static HashMap<String, String> readDictionary(String inPath) {
		return readDictionary(ROW_AS_KEY, inPath);
	}

	public static HashMap<String, String> readDictionary(int keyCol,
			String path) {
		int ValCol = 1;
		String delimiter = "\t";
		if (keyCol < 0)
			delimiter = null;
		HashMap<String, String> hmap = new HashMap();
		int count = 0;
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			if (delimiter != null) {
				while ((line = br.readLine()) != null) {
					String[] splited = line.split(delimiter);
					if (splited.length >= 2) {
						count++;
						hmap.put(splited[keyCol], splited[ValCol]);
					}
				}
			} else {
				// ����ȃP�[�X�E�E�E
				if (keyCol == ROW_AS_VALUE) {
					//�f�[�^���L�[�ɒl�̑��݂���s�ԍ���l�Ƃ��ė��p����ꍇ
					while ((line = br.readLine()) != null) {
						hmap.put(line, String.valueOf(count));
						count++;
					}
				} else if (keyCol == ROW_AS_KEY) {
					//�l�̑��݂���s�ԍ����L�[�Ƃ��ė��p����ꍇ
					while ((line = br.readLine()) != null) {
						hmap.put(String.valueOf(count), line);
						count++;
					}
				} else {
				}

			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hmap;
	}

	// -------------------------------------------------------------------------
	// �����t�@�C���������o��
	// -------------------------------------------------------------------------
	public static void writeDictionary(String outPath, HashMap hmap,
			String delimiter) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			java.util.Set set = hmap.entrySet();
			java.util.Iterator it = set.iterator();
			while (it.hasNext()) {
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				String key = (String) ent.getKey();
				String val = (String) ent.getValue();
				// System.out.println("key:" + key + "  val:" + val);
				bw.write(key);
				bw.write(delimiter);
				bw.write(val);
				bw.write(LF);
			}
			bw.close();

		} catch (Exception e) {
			System.out.println("#ERROR @writeDictionary:" + e.toString());
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// �h�n�b�V���F���́h������薼�̂��\�[�g�������̏��ʂ��C���f�b�N�Ƃ������������A
	// �܂��A���̃C���f�b�N�X�ɑΉ����閼�̎������쐬����
	// -------------------------------------------------------------------------
	public static int writeDictionary2(String outPath1, String outpath2,
			HashMap hmap, String delimiter) {
		int counter = 0;
		List<String> listx = new ArrayList();
		try {
			java.util.Set set = hmap.entrySet();
			java.util.Iterator it = set.iterator();
			while (it.hasNext()) {
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				String dimName = (String) ent.getValue();
				listx.add(dimName);
			}
			Collections.sort(listx);
			System.out.println("debug0330 dictH2I:" + outPath1);
			System.out.println("debug0330 dictI2N:" + outpath2);

			// ------------------------------------------------
			// hashCode to Index converter (�}�g���b�N�X�쐬�p)
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(outPath1));
			// ------------------------------------------------
			// Index to Name converter�@�@�@�@�i�}�g���b�N�X�����̕����p�j
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(outpath2));
			for (int index = 0; index < listx.size(); index++) {
				String name = listx.get(index);
				// System.out.println("debug0330:"+name);
				int iHash = name.hashCode();
				String sIndex = String.valueOf(index);
				String sHash = String.valueOf(iHash);
				// ------------------------------------------------
				bw1.write(sHash);
				bw1.write(delimiter);
				bw1.write(sIndex);
				bw1.write(LF);
				// ------------------------------------------------
				bw2.write(sIndex);
				bw2.write(delimiter);
				bw2.write(name);
				bw2.write(LF);
				counter++;
			}
			bw1.close();
			bw2.close();

		} catch (Exception e) {
			System.out.println("#ERROR @writeDictionary:" + e.toString());
			e.printStackTrace();
			return -1;
		}
		return counter;
	}

	/**************************************************************************
	 * writeDictionary3 �C���f�b�N�X�ʒu�i�s�j�ɖ��̂�����Ă��鎫���t�@�C�������				
	 * @param outPath	�o�̓t�@�C��		 
	 * @param keySet		�L�[���ڂ�Set	 
	 * @param delimiter		��؂蕶��	 
	 **************************************************************************/
	public static int writeSortedDict2File(String outPath, Set<String> keySet,
			String[] prefix) {
		int counter = 0;
		List<String> listx = new ArrayList();
		try {
			for (String dimName : keySet) {
				listx.add(dimName);
			}
			//-----------------------------------------------------------------
			//sort
			//-----------------------------------------------------------------
			Collections.sort(listx);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			if (prefix != null) {
				for (String pref : prefix) {
					bw.write(pref);//�h��w���h�Ȃǂ�}������ꍇ
					bw.write(LF);
					counter++;
				}
			}

			for (int index = 0; index < listx.size(); index++) {
				String name = listx.get(index);
				bw.write(name);
				bw.write(LF);
				counter++;
			}
			bw.close();

		} catch (Exception e) {
			System.out.println("#ERROR @writeDictionary3:" + e.toString());
			e.printStackTrace();
			return -1;
		}
		return counter;
	}
	// public static void debugHmap(HashMap<String, String[]> hmap) {
	// System.out.println("<For Debug>-------------------------------------");
	// java.util.Set set = hmap.entrySet(); // ����iterator���ĂׂȂ��̂ň�USET���擾����
	// java.util.Iterator it = set.iterator();
	// while (it.hasNext()) {
	// java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
	// String key = (String) ent.getKey();
	// String[] val = (String[]) ent.getValue();
	//
	// System.out.println("key:" + key + "  val:" + val[1]);
	// }
	// System.out.println("<For Debug>-------------------------------------");
	// }

}
