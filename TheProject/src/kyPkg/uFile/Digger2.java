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
public class Digger2 {
	private static boolean killerSw = true;
	private static int defaultSize = 1024;

	private static final String FS = System.getProperty("file.separator");

	private static Pattern pattern = null; // �R���p�C���ς݃p�^�[��

	private static boolean recursive; // �T�u�f�B���N�g���������邩�ǂ���

	private static List<String> fileList; // �t�@�C���ꗗ�\

	private static List<String> dirList; // �f�B���N�g���ꗗ�\

	private String rootPath; // �f�B���N�g���̃p�X��

	private File[] gFileArray; // �f�B���N�g�����̃t�@�C���I�u�W�F�N�g

	public static List<String> getDirList() {
		return dirList;
	}

	// -------------------------------------------------------------------------
	// �T�u�f�B���N�g����T������ (�ċA�I�ɍs���ꍇrecursive==true�Ƃ���)
	// -------------------------------------------------------------------------
	private void searchSubDirs() throws Exception {
		for (int i = 0; i < gFileArray.length; i++) {
			String wStr = gFileArray[i].getName();
			if (gFileArray[i].isDirectory()) {
				if (pattern.matcher(wStr).matches()) {
					// 20130507 �O�̂��߁E�E
					dirList.add(gFileArray[i].getAbsolutePath());
					// dirList.add(gFileArray[i].getCanonicalPath());
				}
				if (recursive) {
					try {
						Digger2 subIns = new Digger2(gFileArray[i]);
						subIns.searchSubDirs();
					} catch (Exception e) {
						continue;
					}
				}
			} else {
				if (pattern.matcher(wStr).matches()) {
					// 20130507 �O�̂��߁E�E
					fileList.add(gFileArray[i].getAbsolutePath());
					// fileList.add(gFileArray[i].getCanonicalPath());
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

	public void search() {
		fileList = new ArrayList(defaultSize);
		dirList = new ArrayList(defaultSize);
		try {
			this.searchSubDirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// ���J����R���X�g���N�^
	// wPath �ΏۂƂ���p�X
	// wRegex �����ΏۂƂ���t�@�C���̐��K�\���p�^�[��
	// pOption �T�u�f�B���N�g���ȉ����ΏۂƂ��邩�ǂ���
	// -------------------------------------------------------------------------
	private static boolean isDirExists(String dir) {
		File wFile = new File(dir);
		if (!wFile.exists()) {
			System.out.println("Digger2 ### Error@isDirExists  not exists=>"
					+ dir);
			return false;
		}
		return true;
	}

	public Digger2(String dir, String regex, boolean pRecursive,
			boolean killerflag) {
		killerSw = killerflag;
		if (dir == null)
			return;
		if (isDirExists(dir) == false)
			return;
		// XXX �ȉ����I���W�i�������E�E�E�ǂ����Ǝv�����̂� 2008/01/28�i��肪��������߂��j
		// if (wDir == null)
		// wDir = ".";
		Digger2.recursive = pRecursive;
		if (regex == null)
			regex = "*";
		File wFile = new File(dir);
		if (!wFile.exists()) {
			System.out.println("Error@Digger Path is not exists:" + dir);
			return;
		}
		setCur(wFile);
		setRegex(regex);
	}

	private Digger2(File dir) {
		setCur(dir);
	}

	// �t�@�C�����������Ƃɂ���āA���Y�f�B���N�g������ɂȂ����ꍇ�ċA�I�ɍ폜���Ă���
	public static int chainReaction(String path) {
		return chainReaction(new File(path));
	}

	private static int chainReaction(File file) {
		System.out.println("chainReaction:" + file.getAbsolutePath());
		File parent = file.getParentFile();
		file.delete();
		return removeInvs(parent) + 1;
	}

	private static int removeInvs(File parent) {
		int kilCount = 0;
		File[] fArray = parent.listFiles();
		if (fArray.length == 1) {
			for (int i = 0; i < fArray.length; i++) {
				File file = fArray[i];
				String name = file.getName();
				if (name.equals(".cvsignore")) {
					file.delete();
					// kilCount++;
				} else if (name.equals(".DS_Store")) {
					file.delete();
					// kilCount++;
				} else if (name.equals(".svn")) {
					file.delete();
					// kilCount++;
				}
			}
			fArray = parent.listFiles();
		}
		if (fArray.length == 0) {
			kilCount += chainReaction(parent);
		}
		return kilCount;
	}

	private void setCur(File file) {
		try {
			if (file.isDirectory()) {
				// 20130507 �O�̂��߁E�E
				rootPath = file.getAbsolutePath();
				// rootPath = file.getCanonicalPath();
				if (!rootPath.endsWith(FS))
					rootPath = rootPath + FS;
				if (killerSw) {
					removeInvs(file);
				}
				gFileArray = file.listFiles();
			} else {
				// -----------------------------------------------------------------
				// ���݂��Ȃ��f�B���N�g�����w�肳�ꂽ�ꍇ�̃G���[�����̎��񂵂̂�����
				// �w�肵���p�X�����݂���f�B���N�g���Ȃ̂��ǂ����E�E�E�E
				// -----------------------------------------------------------------
				System.out.println("Error�@not Directory:" + file.toString());
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public List getList() {
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
		// System.out.println(">>pRegex :" + regex);
		pattern = patternIgnoreCase(regex); // ���K�\���p�^�[�����R���p�C��
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
		Digger2.defaultSize = defaultSize;
	}

	// -------------------------------------------------------------------------
	// testDriver
	// -------------------------------------------------------------------------
	public static void main(String argv[]) {
		Digger2 insDig = new Digger2("./", "*fa*.java", true, true);
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

}
