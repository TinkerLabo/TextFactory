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
public class Digger_org {
	private static int defaultSize = 1024;

	private static final String FS = System.getProperty("file.separator");
	private String regex;
	private static Pattern pattern = null; // �R���p�C���ς݃p�^�[��

	private static boolean recursive; // �T�u�f�B���N�g���������邩�ǂ���

	private static List<String> fileList; // �t�@�C���ꗗ�\

	private static List<String> dirList; // �f�B���N�g���ꗗ�\

	private String rootPath; // �f�B���N�g���̃p�X��

	private File[] fileArray; // �f�B���N�g�����̃t�@�C���I�u�W�F�N�g

	public static List<String> getDirList() {
		return dirList;
	}

	private static boolean isDirExists(String dir) {
		File wFile = new File(dir);
		if (!wFile.exists()) {
			System.out.println("Digger ### Error@isDirExists  not exists=>"
					+ dir);
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Digger_org(String dir, String regex) {
		this(dir, regex, false);
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	// dir �ΏۂƂ���p�X
	// regex �����ΏۂƂ���t�@�C���̐��K�\���p�^�[��
	// recursive �T�u�f�B���N�g���ȉ����ΏۂƂ��邩�ǂ���
	// -------------------------------------------------------------------------
	public Digger_org(String dir, String regex, boolean recursive) {
		if (dir == null)
			return;
		if (isDirExists(dir) == false)
			return;
		// XXX �ȉ����I���W�i�������E�E�E�ǂ����Ǝv�����̂� 2008/01/28�i��肪��������߂��j
		// if (wDir == null)
		// wDir = ".";
		Digger_org.recursive = recursive;
		if (regex == null)
			regex = "*";
		File fileObj = new File(dir);
		if (!fileObj.exists()) {
			System.out.println("Error@Digger Path is not exists:" + dir);
			return;
		}
		setCur(fileObj);
		setRegex(regex);
	}

	private Digger_org(File fileObj) {
		setCur(fileObj);
	}

	// -----------------------------------------------------------------
	// ���K�\�������Ƃ��Ԃ���̂ɂ��ĕύX���Ȃ���΂Ȃ�Ȃ�
	// �������\������regix�ɊȈՕϊ�����
	// ���ʂȂǁA��肠�邾�낤�ˁE�E�E�E
	// ���̕������regix�ł͖����Ƃ����O��ŕϊ����Ă���E�E
	// -----------------------------------------------------------------
	private void setRegex(String regex) {
		if (regex.equals(""))
			regex = "*"; // �p�^�[���w�肪�󂾂����ꍇ
		regex = escapeIt(regex);
		this.regex = regex;
		System.out.println("��debug20150323 regex:" + regex);
		pattern = patternIgnoreCase(regex); // ���K�\���p�^�[�����R���p�C��
	}

	private void setCur(File fileObj) {
		System.out.println("����theOtherDigger:"+fileObj.getAbsolutePath());
		try {
			if (fileObj.isDirectory()) {
				// rootPath = fileObj.getCanonicalPath();
				rootPath = fileObj.getAbsolutePath();
				if (!rootPath.endsWith(FS))
					rootPath = rootPath + FS;
				fileArray = fileObj.listFiles();// �J�����g�f�C���N�g����̃t�@�C���ꗗ
				for (int q = 0; q < fileArray.length; q++) {
					System.out.println("�@�@��debug20150323 currents>>"
							+ fileArray[q]);
				}

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
			if (fileArray[i].isDirectory()) {
				// 20130502getCanonicalPath()���Œ��ꒃ�x���̂�getAbsolutePath()�ɍ����ւ���
				if (regex.equals(".*")
						|| pattern.matcher(fileArray[i].getName()).matches()) {
					dirList.add(fileArray[i].getAbsolutePath());
					// dirList.add(fileArray[i].getCanonicalPath());
				}
				if (recursive) {
					try {
						Digger_org subIns = new Digger_org(fileArray[i]);
						subIns.searchSubDirs();
					} catch (Exception e) {
						continue;
					}
				}
			} else {
				if (regex.equals(".*")
						|| pattern.matcher(fileArray[i].getName()).matches()) {
					fileList.add(fileArray[i].getAbsolutePath());
					// fileList.add(fileArray[i].getCanonicalPath());
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// ����
	// -------------------------------------------------------------------------
	public void search(int defaultSize) {
		setDefaultSize(defaultSize);
		search();
	}

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
	// ���X�g���̂��ׂĂ̗v�f�������������Ŋi�[����Ă���z���Ԃ��܂��B
	// -------------------------------------------------------------------------
	public List<String> getFileList() {
		return fileList;
	}

	public List getListD() {
		return dirList;
	}

	public String[] getFileArray() {
		return (String[]) fileList.toArray(new String[fileList.size()]);
	}

	public String[] getDirArray() {
		return (String[]) dirList.toArray(new String[dirList.size()]);
	}

	// -------------------------------------------------------------------------
	// ���̃��X�g���̗v�f��K�؂ȏ����ŌJ��Ԃ��������锽���q��Ԃ��܂��B
	// -------------------------------------------------------------------------
	public Iterator iterator() {
		return fileList.iterator();
	}

	public Iterator iteratorD() {
		return dirList.iterator();
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

	public String escapeIt(String regex) {
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
	// list�̏����T�C�Y
	// -------------------------------------------------------------------------
	public static void setDefaultSize(int defaultSize) {
		Digger_org.defaultSize = defaultSize;
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
		Digger_org insDig = new Digger_org("./", "*fa*.java", true);
		// Digger insDig = new Digger("./", "*fa*", true);
		// Digger insDig = new Digger("./", "*");
		insDig.search();
		String[] lst = insDig.getFileArray();
		for (int i = 0; i < lst.length; i++) {
			System.out.println(lst[i]);
		}
		// ---------------------------------------------------------------------
		// �����q�Ŏ󂯎��ꍇ�i�f�B���N�g���Łj
		// ---------------------------------------------------------------------
		Iterator it = insDig.iteratorD();
		while (it.hasNext()) {
			System.out.println("dir=>" + (String) it.next());
		}
	}

	public static void testDigger20150323() {
		String inDir = "c:/@qpr/";
		boolean recursive = true;
		Digger_org insDig = new Digger_org(inDir, "*.txt", recursive);
		insDig.search();
		String[] lst = insDig.getFileArray();
		for (int i = 0; i < lst.length; i++) {
			System.out.println(lst[i]);
		}
		// ---------------------------------------------------------------------
		// // �����q�Ŏ󂯎��ꍇ�i�f�B���N�g���Łj
		// ---------------------------------------------------------------------
		// Iterator it = insDig.iteratorD();
		// while (it.hasNext()) {
		// System.out.println("dir=>" + (String) it.next());
		// }
	}

}
