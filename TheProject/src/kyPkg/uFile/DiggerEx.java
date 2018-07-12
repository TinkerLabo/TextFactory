package kyPkg.uFile;

import java.io.*;
import java.util.regex.*;

import kyPkg.uDateTime.DateCalc;
import kyPkg.uDateTime.DateUtil;
import kyPkg.uRegex.Regex;

//20150323 �����Ώۂ��傫�����ɑΉ��ł���悤�ȃo�[�W��������肽���ƍl����
//�N���[����̂悤�ȓ��������N���X���C���^�[�t�F�[�X�ɂ������A�X�^�e�B�b�N�ȃ��X�g���������Ȃ��悤�ɂ�����

/*******************************************************************************
 * �s Digger_neo �t 2007-05-11 <BR>
 * �J�����g�E�f�B���N�g���̉����ċA�I�ɒT������ �w�肳�ꂽ���O�̃t�@�C���������ĕ\������N���X
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 ******************************************************************************/
public class DiggerEx {
	private static final String FS = System.getProperty("file.separator");
	private static String regex = null;// ���K�\�������p�^�[��
	private static Pattern pattern = null; // �R���p�C���ς݃p�^�[��
	private static boolean recursive = false; // �T�u�t�H���_���������邩�ǂ���
	// private boolean ignoreCase = false;// �啶���������̋�ʂ����邩�ǂ���
	private static boolean interrupt = false;// ���f������

	public static void interrupt() {
		DiggerEx.interrupt = true;
	}

	private static Inf_OnMatch matcher = null;// �e�N���X�ŃI�u�W�F�N�g�����Əd���Ȃ�̂�static

	private String rootPath; // �f�B���N�g���̃p�X��
	private File[] fileArray; // �f�B���N�g�����̃t�@�C���I�u�W�F�N�g

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	// dir �ΏۂƂ���p�X
	// regex �����ΏۂƂ���t�@�C���̐��K�\���p�^�[��
	// recursive �T�u�f�B���N�g���ȉ����ΏۂƂ��邩�ǂ���
	// -------------------------------------------------------------------------
	public DiggerEx(String dir, String regFileName, boolean ignoreCase,
			boolean recursive, Inf_OnMatch matcher) {
		interrupt = false;
		DiggerEx.matcher = matcher;
		matcher.init();

		if (dir == null)
			return;
		if (isDirExists(dir) == false)
			return;
		DiggerEx.recursive = recursive;
		File fileObj = new File(dir);
		if (!fileObj.exists()) {
			System.out.println("Error@Digger Path is not exists:" + dir);
			return;
		}
		setCur(fileObj);
		setRegFileName(regFileName, ignoreCase);
	}

	private DiggerEx(File fileObj) {
		setCur(fileObj);
	}

	// -----------------------------------------------------------------
	// ���K�\�������Ƃ��Ԃ���̂ɂ��ĕύX���Ȃ���΂Ȃ�Ȃ�
	// �������\������regix�ɊȈՕϊ�����
	// ���ʂȂǁA��肠�邾�낤�ˁE�E�E�E
	// ���̕������regix�ł͖����Ƃ����O��ŕϊ����Ă���E�E
	// -----------------------------------------------------------------
	private void setRegFileName(String regFileName, boolean ignoreCase) {
		if (regFileName == null || regFileName.equals(""))
			regFileName = ""; // �p�^�[���w�肪�󂾂����ꍇ
		// regFileName = escapeIt(regFileName);
		// System.out.println("#debug# 20150402 regFileName:"+regFileName);
		DiggerEx.regex = regFileName;
		DiggerEx.pattern = Regex.getPatternEx(regFileName, ignoreCase); // ���K�\���p�^�[�����R���p�C��
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
				if (regex.equals(".*")
						|| DiggerEx.pattern.matcher(name).matches()) {
					// System.out.println("match:" + absPath);
					matcher.onMatch(fileArray[i]);
					// matcher.forDebug(absPath);
				}
				if (recursive) {
					try {
						DiggerEx subIns = new DiggerEx(fileArray[i]);
						subIns.searchSubDirs();
					} catch (Exception e) {
						continue;
					}
				}
			} else {
				if (regex.equals(".*")
						|| DiggerEx.pattern.matcher(name).matches()) {
					// System.out.println("match:" + absPath);
					matcher.onMatch(fileArray[i]);
					// matcher.forDebug(absPath);
				}
			}
			if (interrupt) // ���f�t���O���Q�Ƃ���
				break;
		}
	}

	// -------------------------------------------------------------------------
	// ����
	// -------------------------------------------------------------------------
	public void search() {
		try {
			this.searchSubDirs();
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

	private static boolean isDirExists(String dir) {
		File wFile = new File(dir);
		if (!wFile.exists()) {
			System.out.println(
					"Digger ### Error@isDirExists  not exists=>" + dir);
			return false;
		}
		return true;
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
	// #main
	// -------------------------------------------------------------------------
	public static void main(String argv[]) {
		System.out.println("#start#");
		testDigger20150323();
		System.out.println("#end#");
	}

	// -------------------------------------------------------------------------
	// �P�̃e�X�g
	// -------------------------------------------------------------------------
	public static void testDigger20150323() {
		String inDir = "c:/@qpr/";
		boolean recursive = true;// �T�u�f�C���N�g�����T��
		String regFileName = "*.txt";// �t�@�C�����̂̃p�^�[��
		regFileName = "";
		String keyword = "2015";// �R���e���c������������L�[���[�h
		boolean ignoreCase = false;// �啶������������ʂ���ꍇtrue
		long limitMin = 0;// 1k�ȉ���default
		long limitMax = Long.MAX_VALUE;// 1k�ȉ���default

		String today = DateCalc.getToday();
		String theDay = DateUtil.edate1(null, DateUtil.WEEK, -1);
		int bYmd = Integer.valueOf(theDay);
		int aYmd = Integer.valueOf(today);
		bYmd = -1;
		aYmd = -1;
		Inf_OnMatch onMatch = new MatcherEx(keyword, ignoreCase, limitMin,
				limitMax, bYmd, aYmd);
		DiggerEx insDig = new DiggerEx(inDir, regFileName, ignoreCase,
				recursive, onMatch);
		insDig.search();
		onMatch.fin();

	}
}
