package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays; //import java.util.Collections;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import globals.ResControl;
import kyPkg.tools.Onbiki;
import kyPkg.uRegex.Regex;
import static kyPkg.uFile.FileUtil.*;

public class ListArrayUtil {

	/***************************************************************************
	 * List���t�@�C���ɏ����o��<br>
	 * 
	 * @param pPath
	 *            �o�͐�t�@�C��
	 * @param pSrc
	 *            �ǂݍ��݌�Vector
	 * @param delimiter
	 *            �e�t�B�[���h����؂�f���~�^���� <br>
	 * @param isAppend
	 *            ���ǉ��������� �s�g�p��t<br>
	 *            ListArrayUtil.list2File("./janMatrix.txt",matrix,"\t");
	 **************************************************************************/
	public static String array2String(Object[] objArray) {
		return array2String(objArray, null);
	}

	public static String array2String(Object[] objArray, String delimiter) {
		if (objArray == null)
			return null;
		StringBuffer buf = new StringBuffer(1024);
		for (int i = 0; i < objArray.length; i++) {
			buf.append(objArray[i].toString());
			if (delimiter != null)
				buf.append(delimiter);
		}
		return buf.toString();
	}

	public static List<String> eachChar2List(String pString) {
		char[] array = pString.toCharArray();
		return charArray2List(array);
	}

	private static List<String> charArray2List(char[] array) {
		if (array.length == 0)
			return null;
		List<String> list = new ArrayList(array.length);
		for (int i = 0; i < array.length; i++) {
			list.add("" + array[i]);
		}
		return list;
	}

	// ����(�g���q�͕t���Ȃ�)
	public static List dir2List_asc(String dirPath, String regex) {
		return dir2List(dirPath, regex, 0, false, true);
	}

	// ����
	public static List dir2List_asc(String dirPath, String regex,
			boolean optExt) {
		return dir2List(dirPath, regex, 0, false, optExt);
	}

	// �~��
	public static List dir2List_dsc(String dirPath, String regex,
			boolean optExt) {
		return dir2List(dirPath, regex, -1, false, optExt);
	}

	// �~��(�g���q�͕t���Ȃ�)
	public static List dir2List_dsc(String dirPath, String regex) {
		return dir2List(dirPath, regex, -1, false, true);
	}

	// �f�B���N�g�������K�v
	public static List<String> dir2ListWithDir(String dirPath, String regex) {
		return dir2List(dirPath, regex, -1, true, false);
	}

	// optDir �f�B���N�g������ǉ�
	// optExt �g���q���s�v�Ȃ�true
	private static List<String> dir2List(String dirPath, String regex,
			int order, boolean optDir, boolean optExt) {
		dirPath = FileUtil.normarizeIt(dirPath);
		if (!dirPath.endsWith("/"))
			dirPath = dirPath + "/";
		if (regex.equals("*") || regex.equals(""))
			regex = "\\S*";
		Pattern pattern = Regex.getPattern(regex, true);
		// System.out.println("@dir2List inPath: " + dirPath + " regex:" +
		// regex);

		List<String> list = new ArrayList();
		File file = new File(dirPath);
		// System.out.println("debug dirPath:"+dirPath);
		if (!file.isDirectory()) {
			System.out.println("#Error@dir2List not a Directory :" + dirPath);
			return null;
		}
		String[] array = file.list();
		if (array == null) {
			System.out
					.println("#Error@dir2List File not exist path:" + dirPath);
			return null;
		}
		// System.out.println("array.length :" + array.length);
		for (int i = 0; i < array.length; i++) {
			String filePath = dirPath + array[i];
			File wFile = new File(filePath);
			if (wFile.isFile() || wFile.isDirectory()) {
				String fileName = wFile.getName();
				// System.out.println("dir2List fileName:" + fileName);

				if (pattern.matcher(fileName).find()) {
					String lastMod = DateFormat.getDateInstance()
							.format(new Date(wFile.lastModified()));
					if (optDir) {
						list.add(lastMod + "\t" + filePath);
					} else {
						if (optExt) {
							String firstname = FileUtil.getFirstName2(array[i]);
							list.add(lastMod + "\t" + firstname);
						} else {
							list.add(lastMod + "\t" + array[i]);
						}

					}
				}
			}
		}
		if (list != null && list.size() > 0) {
			if (order != 0)
				kyPkg.Sorts.CommonSort.sortIt(list, order);// -1:�~���\�[�g,1�����\�[�g
			list = getColElement(list, 1);// �t�@�C�����̃J�����������o���ĕԂ�
		} else {
			list = null;
		}
		return list;
	}

	private static List<String> getColElement(List<String> list, int column) {
		String delimiter = "\t";
		List<String> dstList = new ArrayList();
		for (String val : list) {
			String[] array = val.split(delimiter);
			if (array.length > column) {
				dstList.add(array[column]);
			}
		}
		return dstList;
	}

	public static int string2File(String pPath, String str) {
		List<String> list = new ArrayList();
		list.add(str);
		return list2File(pPath, list);
	}

	public static void array2File(String outPath, int[] iArray) {
		String[] array = new String[iArray.length];
		for (int i = 0; i < iArray.length; i++) {
			array[i] = String.valueOf(iArray[i]);
		}
		array2File(outPath, array, null);
	}

	public static void array2file(String outPath, String[] array) {
		array2File(outPath, array, null);
	}

	public static void array2File(String outPath, String[] array,
			String encode) {
		try {
			OutputStreamWriter writer = FileUtil.getWriter(outPath, encode);
			if (writer != null) {
				for (int i = 0; i < array.length; i++) {
					String line = array[i];
					writer.write(line, 0, line.length());
					writer.write(LF, 0, LF.length()); // ���s�R�[�h
				}
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// kyPkg.uFile.ListArrayUtil.list2FileMod(String pPath, List<String> list)
	// -------------------------------------------------------------------------
	public static int list2FileMod(String pPath, List<String> list) {
		return list2File(pPath, list, true, DEFAULT_ENCODE);
	}

	public static List<String> cnv2StrList(List<Object> list) {
		List<String> res = new ArrayList();
		for (Object element : list) {
			res.add(element.toString());
		}
		return res;
	}

	// -------------------------------------------------------------------------
	// kyPkg.uFile.ListArrayUtil.list2File(String path, List<String> list)
	// -------------------------------------------------------------------------
	public static int list2File(String path, List<String> list) {
		return list2File(path, list, false, DEFAULT_ENCODE);
	}

	public static int list2File(String path, List<String> list, String encode) {
		return list2File(path, list, false, encode);
	}

	private static int list2File(String path, List<String> list, boolean append,
			String encode) {
		if (list == null)
			return -1;
		int count = 0;
		FileUtil.makeParents(path); // �e�p�X��������΍��
		try {
			BufferedWriter bw = new BufferedWriter(
					FileUtil.getStreamWriter(path, encode, append));
			for (String element : list) {
				element = Onbiki.cnv2Similar(element, FileUtil.defaultEncoding);//20170425
				bw.write(element, 0, element.length());
				bw.write(FileUtil.LF, 0, FileUtil.LF.length());
				count++;
			}
			bw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return count;
	}

	// -------------------------------------------------------------------------
	// ListArrayUtil.set2File(String path, List<String> set)
	// -------------------------------------------------------------------------
	public static int set2File(String path, Set<String> set) {
		return set2File(path, set, false);
	}

	public static int set2File(String path, Set<String> set, boolean append) {
		if (set == null)
			return -1;
		int count = 0;
		FileUtil.makeParents(path); // �e�p�X��������΍��
		try {
			BufferedWriter bw = new BufferedWriter(
					FileUtil.getStreamWriter(path, DEFAULT_ENCODE, append));
			for (String str : set) {
				bw.write(str, 0, str.length());
				bw.write(FileUtil.LF, 0, FileUtil.LF.length());
				count++;
			}
			bw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return count;
	}

	// �d���̖������X�g��Ԃ�
	private static List uniqueList(List<String> partList) {
		HashSet<String> hashSet = listToHashSet(partList);
		return hashSetToList(hashSet);
	}

	// ���X�g�����j�[�N�ɂ������iHashSet��Ԃ��j
	private static HashSet<String> listToHashSet(List<String> listIn) {
		HashSet<String> set = new HashSet();
		for (String element : listIn) {
			set.add(element);
		}
		return set;
	}

	// ���X�g����C���f�b�N�X��Ԃ��n�b�V���}�b�v�i�����j�ɕϊ�����
	public static HashMap<String, Integer> listToHashMap(List<String> listIn) {
		HashMap<String, Integer> hmap = new HashMap();
		int seq = 0;
		for (String key : listIn) {
			Integer value = hmap.get(key);
			if (value == null) {
				value = new Integer(seq++);
				hmap.put(key, value);
			}
		}
		return hmap;
	}

	// HashMap�̃L�[���������X�g�Ƃ��ĕԂ�
	public static List<String> getKeylist(HashMap<String, Integer> hmap) {
		Set<String> set = hmap.keySet();
		List<String> listOut = new ArrayList();
		for (String element : set) {
			listOut.add(element);
		}
		return listOut;
	}

	public static void enumHashMap(HashMap<String, Integer> hmap) {
		System.out.println(
				"< enumHashMap start >----------------------------------------------------");
		List<String> keyList = getKeylist(hmap);
		for (String key : keyList) {
			System.out.println(key + " => " + hmap.get(key));
		}
		System.out.println("# keyList.size():" + keyList.size());
		System.out.println(
				"< enumHashMap end   >----------------------------------------------------");
	}

	public static void enumList(List<String> list) {
		if (list == null) {
			System.out.println("#ERROR@enumList list==null");
			return;
		}
		System.out.println(
				"< enumList start >-------------------------------------------------------");
		for (String element : list) {
			System.out.println(">>" + element);
		}
		System.out.println("# list.size():" + list.size());
		System.out.println(
				"< enumList end   >-------------------------------------------------------");
	}

	// -------------------------------------------------------------------------
	// HashSet�����X�g�ɂ��� �iXXX�@���̕��@���������m�F����j
	// -------------------------------------------------------------------------
	private static List hashSetToList(HashSet<String> set) {
		List<String> listOut = new ArrayList();
		for (String element : set) {
			listOut.add(element);
		}
		return listOut;
	}

	// -------------------------------------------------------------------------
	// file2List
	// -------------------------------------------------------------------------
	/***************************************************************************
	 * �t�@�C����List�ɓǂݍ���<br>
	 * 
	 * @param path
	 *            �ǂݍ��݌��ƂȂ�t�@�C��
	 * @return List <br>
	 * 
	 **************************************************************************/
	// �s�g�p��t
	// List<String> list = ListArrayUtil.file2List("c:/category2.txt");
	// Collections.sort(list, new Comparator4Delim(",", new int[] { 1, 2 }));
	// for (String element : list) {
	// System.out.println(element);
	// }

	/***************************************************************************
	 * �t�@�C���𕶎��z��ɕϊ�����<br>
	 * 
	 * @param pPath
	 *            �ǂݍ��݌��ƂȂ�t�@�C��
	 * @return �����z�� <br>
	 *         �s�g�p��t<br>
	 *         String[] xArray = �@ListArrayUtil.file2Array("Some.txt");
	 **************************************************************************/
	public static String[] file2Array(String path) {
		return list2Array(file2List(path, -1));
	}

	public static String[] list2Array(List<String> list) {
		if (list != null) {
			return (String[]) list.toArray(new String[list.size()]);
		}
		return new String[] {};
	}

	// public static String[] file2array(List list) {
	// if (list != null) {
	// return (String[]) list.toArray(new String[list.size()]);
	// }
	// return new String[] { "Notfound" };
	// }

	public static String[] file2Array_org(String path) {
		List list = new ArrayList();
		try {
			String rec;
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				rec = br.readLine();
				if (rec != null)
					list.add(rec);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			list = null;
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	// -------------------------------------------------------------------------
	// �t�@�C���̈�s�߂��A��؂蕶���ł΂炵�����̂�z��Ƃ��ĕԂ�
	// -------------------------------------------------------------------------
	public static List<String> file2List(String path, String delimiter) {
		List<String> list = kyPkg.uFile.ListArrayUtil.file2List(path);
		if (list != null && list.size() > 0) {
			String val = list.get(0);
			String[] array = val.split(delimiter);
			return java.util.Arrays.asList(array);
		} else {
			return null;
		}
	}

	// -------------------------------------------------------------------------
	// �t�@�C�������X�g������(��s���A��̗v�f�ɂȂ�悤�Ԃ����)
	// ex> List list = kyPkg.uFile.ListArrayUtil.file2List("c:\test");
	// -------------------------------------------------------------------------
	public static List<String> file2List(String path) {
		return file2List(path, -1);
	}

	// -------------------------------------------------------------------------
	// �t�@�C���̓���̃J���������X�g������
	// -------------------------------------------------------------------------
	// // unique:true => �t�@�C�������j�[�N�ȃ��X�g������
	// public static List<String> file2ListUQ(String path, String delimiter,
	// int col) {
	// return file2List(path, delimiter, col, true, null, col);
	// }
	public static List<String> file2List(String path, String delimiter,
			int col) {
		return file2List(path, delimiter, col, null, col);
	}

	// -------------------------------------------------------------------------
	// �{��
	// XXX �����J�������o�͂������Ȃ����낤���H
	// -------------------------------------------------------------------------
	private static List<String> file2List(String path, String delimiter,
			int col, HashMap<String, Pattern> regMap, int regCol) {
		List<String> listIn = file2List(path, -1);
		List<String> partList;
		if (regMap == null) {
			partList = partOf(listIn, delimiter, col);
		} else {
			// regCol�ɂ�����Z���̒l�����K�\���Ƀ}�b�`���邩�ǂ�������
			partList = partOfPlus(listIn, delimiter, col, regMap, regCol);
		}
		return partList;
	}

	// ------------------------------------------------------------------------
	// ���X�g���̎w��J���������X�g�Ƃ��ĕԂ��idelimiter�ŋ�؂����J�����ʒu�̂���)
	// ------------------------------------------------------------------------
	private static List<String> partOf(List<String> listIn, String delimiter,
			int col) {
		List<String> listOut = new ArrayList();
		for (String element : listIn) {
			String[] array = element.split(delimiter);
			if (array.length > col) {
				listOut.add(array[col]);
			}
		}
		return listOut;
	}

	// ------------------------------------------------------------------------
	// ���X�g���̎w��J������<<���K�\���Ƀ}�b�`�������̂̂�>>���X�g�Ƃ��ĕԂ�
	// // <<���K�\��>>�����쐬
	// List<String> regList = new ArrayList();
	// regList.add(".*Quick$");
	// regList.add("^Quick$");
	// regList.add("^Quick.*");
	// HashMap<String, Pattern> regMap = Regex.regCompile(regList);
	// ------------------------------------------------------------------------
	public static List<String> partOfPlus(List<String> listIn, String delimiter,
			int col, HashMap<String, Pattern> regMap, int regCol) {
		List<String> listOut = new ArrayList();
		for (String element : listIn) {
			String[] array = element.split(delimiter);
			if (array.length > col) {
				// <<���K�\��>>�}�b�`���O
				if (Regex.regMatch(regMap, array[regCol])) {
					listOut.add(array[col]);
				}
			}
		}
		return listOut;
	}

	public static List<String> file2List(String path, long limit) {
		if (limit < 0)
			limit = Long.MAX_VALUE;
		long counter = 0;
		if (new File(path).isFile() == false) {
			System.out.println("#error @file2List  data not Found:" + path);
			return null;
		}
		List list = new ArrayList();
		try {
			String rec;
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				rec = br.readLine();
				if (rec != null) {
					list.add(rec);
					counter++;
					if (counter >= limit)
						break;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
			list = null;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			list = null;
		}
		return list;
	}

	//
	public static void testIncore() {
		String path = ResControl.D_DAT + "CATEGORY.DAT";
		String delimiter = "\t";
		int col = 1;
		boolean sortOpt = true;
		// �w��J�����݂̂����X�g�ɓǂ݂���
		List<String> partList = ListArrayUtil.file2List(path, delimiter, col);
		if (sortOpt)
			Collections.sort(partList);
		ListArrayUtil.enumList(partList);
	}

	public static void testIncoreUQ() {
		String path = ResControl.D_DAT + "CATEGORY.DAT";
		String delimiter = "\t";
		int col = 1;
		boolean sortOpt = true;
		// �w��J�����݂̂����X�g�ɓǂ݂���
		List<String> listOut = ListArrayUtil.file2List(path, delimiter, col);
		listOut = ListArrayUtil.uniqueList(listOut);

		if (sortOpt)
			Collections.sort(listOut);
		ListArrayUtil.enumList(listOut);
	}

	public static void testIncoreUQ_Plus() {
		String path = ResControl.D_DAT + "CATEGORY.DAT";
		String delimiter = "\t";
		int col = 0;
		String regex = "^112[0-9][0-9][0-9]";

		List<String> listout = ListArrayUtil.getFileListSpecial(path, delimiter,
				col, regex);

		ListArrayUtil.enumList(listout);
	}

	public static List<String> getFileListSpecial(String path, String delimiter,
			int col, String regex) {
		// <<���K�\��>>�����쐬
		List<String> regList = new ArrayList();
		// regList.add("^[0-9][0-9][0-9][0-9][0-9][0-9]");
		// regList.add("^112[0-9][0-9][0-9]");
		regList.add(regex);
		HashMap<String, Pattern> regMap = Regex.regCompile(regList);
		int regCol = 0;
		regCol = col;
		boolean sortOpt = true;
		// �w��J�����݂̂����X�g�ɓǂ݂���
		List<String> listOut = ListArrayUtil.file2List(path, delimiter, col,
				regMap, regCol);
		listOut = ListArrayUtil.uniqueList(listOut); // �d���̖������X�g��Ԃ�
		if (sortOpt)
			Collections.sort(listOut);
		return listOut;
	}

	public static Set<String> file2Set(String path) {
		if (new File(path).isFile() == false) {
			System.out.println("#error @file2List  data not Found:" + path);
			return null;
		}
		Set<String> set = new HashSet();
		try {
			String rec;
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				rec = br.readLine();
				if (rec != null) {
					set.add(rec);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
			set = null;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			set = null;
		}
		return set;
	}

	//-------------------------------------------------------------------------
	//��USet�ɕۑ��������̂��\�[�g���Ă���o�͂��Ă���
	//-------------------------------------------------------------------------
	public static List<String> file2UqList(String path, int col,
			String delimiter) {
		Set<String> set = file2Set(path, col, delimiter);
		List<String> list = new ArrayList(set);
		Collections.sort(list);
		return list;
	}

	public static Set<String> file2Set(String path, int col, String delimiter) {
		if (new File(path).isFile() == false) {
			System.out.println("#error @file2List  data not Found:" + path);
			return null;
		}
		Set<String> set = new HashSet();
		try {
			String rec;
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				rec = br.readLine();
				if (rec != null) {
					String[] array = rec.split(delimiter, -1);
					if (array.length > col)
						set.add(array[col]);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
			set = null;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			set = null;
		}
		return set;
	}

	//20140819 ����Ă݂������Ńe�X�g���Ă��Ȃ��E�E�E�E�v�e�X�g
	public static HashSet<String> file2Set(String path, int keyCol,
			String delimiter, boolean ucaseOpt) {
		HashSet<String> hashSet = new HashSet();
		String wRec;
		File fl = new File(path);
		if (fl.exists() == false)
			return null;
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] wArray = wRec.split(delimiter);
					if (wArray.length > keyCol) {
						String wKey = wArray[keyCol].trim();
						wKey = wKey.replace("�@", "");// �b��I�ȃt�B���^����
						// �L�[�͂��ׂđ啶���ɕϊ�����i�I�v�V�����j
						if (ucaseOpt)
							wKey = wKey.toUpperCase();
						if (!wKey.equals(""))
							hashSet.add(wKey);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return hashSet;
	}

	//�@#�������������̂ŋ}�����������E�E�E
	public static Set<String> removeStr(Set<String> src, String remove) {
		Set<String> res = new HashSet();
		for (String element : src) {
			element = element.replaceAll(remove, "");
			res.add(element);
		}
		return res;
	}

	public static void test_20161102() {
		List<String> list = ListArrayUtil.file2List("c:/result.txt", 5);
		for (String element : list) {
			System.out.println("element:" + element);
		}
	}

	public static void testFile2Set() {
		String path = "C:/samples/dupval.txt";
		Set<String> set = file2Set(path);
		for (String element : set) {
			System.out.println(">>" + element);
		}
	}

	public static void testFile2Set2() {
		String path = "C:/samples/dupval2.txt";
		String delimiter = ",";
		int col = 1;
		Set<String> set = file2Set(path, col, delimiter);
		//		for (String element : set) {
		//			System.out.println(">>" + element);
		//		}
		List<String> list = new ArrayList(set);
		Collections.sort(list);
		for (String element : list) {
			System.out.println(">>" + element);
		}
	}

	public static void testFile2UqList() {
		String path = "C:/samples/dupval3.txt";
		String delimiter = "\t";
		int col = 1;
		List<String> list = file2UqList(path, col, delimiter);
		for (String element : list) {
			System.out.println(">>" + element);
		}
	}

	public static void testFile2Set_20160823() {
		String tempDir = globals.ResControl.getTempDir();
		String path = tempDir + "srcJan.txt";
		String delimiter = "\t";
		int col = 0;
		Set<String> set = ListArrayUtil.file2Set(path, col, delimiter);
		set = removeStr(set, "#");
		for (String element : set) {
			System.out.println(">>" + element);
		}
	}

	public static void testIncoreHash() {
		String path = ResControl.D_DAT + "CATEGORY.DAT";
		String delimiter = "\t";
		int col = 1;
		boolean sortOpt = true;
		// �w��J�����݂̂����X�g�ɓǂ݂���
		List<String> partList = ListArrayUtil.file2List(path, delimiter, col);
		partList = ListArrayUtil.uniqueList(partList); // �d���̖������X�g��Ԃ�
		HashMap<String, Integer> hmap = ListArrayUtil.listToHashMap(partList);
		List<String> keyList = ListArrayUtil.getKeylist(hmap);
		if (sortOpt)
			Collections.sort(keyList);
		ListArrayUtil.enumList(keyList);
		ListArrayUtil.enumHashMap(hmap);
	}

	public static void testFile2Array() {
		String[] array = ListArrayUtil.file2Array("c:/HOSTS");
		for (int i = 0; i < array.length; i++) {
			System.out.println("test>>" + array[i]);
		}
		int cnt = list2File("c:/zapp.txt", Arrays.asList(array));
		System.out.println("cnt>>" + cnt);

	}

	// 20130336
	public static void testFile2List() {
		String path = "C:/@QPR/Personal/MonSets/";
		path = "c:/HOSTS";
		List<String> list = ListArrayUtil.file2List(path, -1);
		ListArrayUtil.enumList(list);
		int cnt = list2File("c:/list2file.txt", list);
		System.out.println("cnt>>" + cnt);
	}



	// ######################################################################################
	// ## M a i n
	// ######################################################################################
	public static void main(String[] argv) {
		//		testFile2Set_20160823();
		//		test_20161102();
//		testTran2JanSet();
	}
}
