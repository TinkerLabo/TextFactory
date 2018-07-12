package kyPkg.uFile;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/*******************************************************************************
 * �s Digger_neo �t 2007-05-11 <BR>
 * �J�����g�E�f�B���N�g���̉����ċA�I�ɒT������ �w�肳�ꂽ���O�̃t�@�C���������ĕ\������N���X �������[�x���[���ꍇ�E�E�E�����Ώۂ��傫���ꍇ�ɂ͗v����
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 ******************************************************************************/
// XXX BUG��\������A�v�C���I�IdirList�Ȃǋ��L���Ă��܂��Ɗ댯�Ȃ̂ŃV���O���g��������B
// XXX �������̓Z�b�V����id���������ă��X�g���n�b�V���ŊǗ�����I�I
public class Digger {
	private static int defaultSize = 1024;

	private static final String FS = System.getProperty("file.separator");
	private static String regex;
	private static Pattern pattern = null; // �R���p�C���ς݃p�^�[��
	private static boolean recursive; // �T�u�f�B���N�g���������邩�ǂ���

	private static List<String> fileList; // �t�@�C���ꗗ�\

	private static List<String> dirList; // �f�B���N�g���ꗗ�\

	private String rootPath; // �f�B���N�g���̃p�X��

	private File[] fileArray; // �f�B���N�g�����̃t�@�C���I�u�W�F�N�g

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Digger(String dir, String regex) {
		this(dir, regex, false);
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	// dir �ΏۂƂ���p�X
	// regex �����ΏۂƂ���t�@�C���̐��K�\���p�^�[��
	// recursive �T�u�f�B���N�g���ȉ����ΏۂƂ��邩�ǂ���
	// -------------------------------------------------------------------------
	public Digger(String dir, String regex, boolean recursive) {
		if (dir == null)
			return;
		if (isDirExists(dir) == false)
			return;
		// XXX �ȉ����I���W�i�������E�E�E�ǂ����Ǝv�����̂� 2008/01/28�i��肪��������߂��j
		// if (wDir == null)
		// wDir = ".";
		Digger.recursive = recursive;
		if (regex == null)
			regex = "*";
		File fileObj = new File(dir);
		if (!fileObj.exists()) {
			System.out.println("Error@Digger Path is not exists:" + dir);
			return;
		}
		setRegex(regex);
		setCur(fileObj);
	}

	private Digger(File fileObj) {
		setCur(fileObj);
	}

	// -----------------------------------------------------------------
	// ���K�\�������Ƃ��Ԃ���̂ɂ��ĕύX���Ȃ���΂Ȃ�Ȃ�
	// �������\������regix�ɊȈՕϊ�����
	// ���ʂȂǁA��肠�邾�낤�ˁE�E�E�E
	// ���̕������regix�ł͖����Ƃ����O��ŕϊ����Ă���E�E
	// -----------------------------------------------------------------
	private void setRegex(String regex) {
//		System.out.println("regex:"+regex);
		if (regex.equals(""))
			regex = "*"; // �p�^�[���w�肪�󂾂����ꍇ
		if (regex.equals("*.*"))
			regex = "*"; // �p�^�[���w�肪�󂾂����ꍇ
		regex = escapeIt(regex);
		Digger.regex = regex;
		Digger.pattern = patternIgnoreCase(regex); // ���K�\���p�^�[�����R���p�C��
	}

	private void setCur(File fileObj) {
		try {
			if (fileObj.isDirectory()) {
				// rootPath = fileObj.getCanonicalPath();
				rootPath = fileObj.getAbsolutePath();
				if (!rootPath.endsWith(FS))
					rootPath = rootPath + FS;
				fileArray = fileObj.listFiles();// �J�����g�f�C���N�g����̃t�@�C���ꗗ
			} else {
				// -----------------------------------------------------------------
				// ���݂��Ȃ��f�B���N�g�����w�肳�ꂽ�ꍇ�̃G���[�����̎��񂵂̂�����
				// �w�肵���p�X�����݂���f�B���N�g���Ȃ̂��ǂ����E�E�E�E
				// -----------------------------------------------------------------
				System.out.println("Error�@not Directory:" + fileObj.toString());
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// �T�u�f�B���N�g����T������ (�ċA�I�ɍs���ꍇrecursive==true�Ƃ���)
	// -------------------------------------------------------------------------
	private void searchSubDirs() throws Exception {
		if (fileArray == null)
			return;
		for (int i = 0; i < fileArray.length; i++) {
			String name = fileArray[i].getName();
			String absPath = fileArray[i].getAbsolutePath();
			if (fileArray[i].isDirectory()) {
				// 20130502getCanonicalPath()���Œ��ꒃ�x���̂�getAbsolutePath()�ɍ����ւ���
				if (regex.equals(".*") || pattern.matcher(name).matches()) {
					dirList.add(fileArray[i].getAbsolutePath());
					// dirList.add(fileArray[i].getCanonicalPath());
					//					System.out.println("match:"+absPath);
				}
				if (recursive) {
					try {
						Digger subIns = new Digger(fileArray[i]);
						subIns.searchSubDirs();
					} catch (Exception e) {
						continue;
					}
				}
			} else {
//				System.out.println("name:"+name);
				if (regex.equals(".*") || pattern.matcher(name).matches()) {
					fileList.add(fileArray[i].getAbsolutePath());
					// fileList.add(fileArray[i].getCanonicalPath());
					//					System.out.println("match:" + absPath);
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// ����
	// -------------------------------------------------------------------------
	public List<String> search() {
		fileList = new ArrayList(defaultSize);
		dirList = new ArrayList(defaultSize);
		try {
			this.searchSubDirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileList;
	}

	// -------------------------------------------------------------------------
	// ���[�g�p�X��Ԃ�
	// -------------------------------------------------------------------------
	public String getRootPath() {
		return rootPath;
	}

	public int getRootPathLength() {
		return rootPath.length();
	}

	// -------------------------------------------------------------------------
	// �����ɍ������t�@�C���̈ꗗ��Ԃ�
	// -------------------------------------------------------------------------
	public List<String> getFileList() {
		return fileList;
	}

	// -------------------------------------------------------------------------
	// �����ɍ������a���N�g���̈ꗗ��Ԃ�
	// -------------------------------------------------------------------------
	public static List<String> getDirList() {
		return dirList;
	}

	private static boolean isDirExists(String dir) {
		File wFile = new File(dir);
		if (!wFile.exists()) {
			System.out.println(
					"Digger ### Error@isDirExists  not exists=>" + dir);
			return false;
		}
		return true;
	}

	public String[] getFileArray() {
		return (String[]) fileList.toArray(new String[fileList.size()]);
	}

	// -------------------------------------------------------------------------
	// ���̃��X�g���̗v�f��K�؂ȏ����ŌJ��Ԃ��������锽���q��Ԃ��܂��B
	// -------------------------------------------------------------------------
	public Iterator iterator() {
		return fileList.iterator();
	}

	// -------------------------------------------------------------------------
	// ���X�g�𕶎���Ƃ��ĕԂ�
	// -------------------------------------------------------------------------
	@Override
	public String toString() {
		StringBuffer sBuf = new StringBuffer();
		Iterator it = this.iterator();
		while (it.hasNext()) {
			sBuf.append((String) it.next());
			sBuf.append("\n");
		}
		return sBuf.toString();
	}

	private String escapeIt(String regex) {
		regex = regex.replaceAll("\\\\", "\\\\");
		regex = regex.replaceAll("\"", "\\\\\"");
		regex = regex.replaceAll("\'", "\\\\\'");
		regex = regex.replaceAll("\t", "\\\\t");
		regex = regex.replaceAll("\\.", "\\\\.");
		regex = regex.replaceAll("\\s", "\\\\s");
		regex = regex.replaceAll("\\*", ".*");
		return regex;
	}

	// -------------------------------------------------------------------------
	// �����p�^�[�����R���p�C��
	// -------------------------------------------------------------------------
	public static Pattern patternIgnoreCase(String pRegex) {
		return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	}

	// -------------------------------------------------------------------------
	// #main
	// -------------------------------------------------------------------------
	public static void main(String argv[]) {
		// testDigger();
		testDigger20150323();
	}

	// -------------------------------------------------------------------------
	// testDriver
	// -------------------------------------------------------------------------
	public static void testDigger() {
		Digger insDig = new Digger("./", "*fa*.java", true);
		// Digger insDig = new Digger("./", "*fa*", true);
		// Digger insDig = new Digger("./", "*");
		insDig.search();
		String[] lst = insDig.getFileArray();
		for (int i = 0; i < lst.length; i++) {
			System.out.println(lst[i]);
		}
	}

	public static void testDigger20150323() {
		boolean recursive = true;
		Digger insDig = new Digger("c:/sample/", "*.txt", recursive);
		insDig.search();
		String[] list = insDig.getFileArray();
		for (int i = 0; i < list.length; i++) {
			System.out.println(list[i]);
		}
	}

}
