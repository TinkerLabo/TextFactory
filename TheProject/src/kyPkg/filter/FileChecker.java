package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kyPkg.uFile.DosEmu;
import kyPkg.uRegex.Regex;

public class FileChecker {
	private Inf_iClosure reader;
	//	private long line = 0;
	private boolean findAll = false;// �S��������ꍇ�@true
	private List<String> findElements;
	private Pattern pattern;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public FileChecker(String inPath, String regex, boolean ignoreCase) {
		this(inPath, Regex.getPatternEx(regex, ignoreCase));
	}

	public FileChecker(String inPath, Pattern pattern) {
		this.reader = new EzReader(inPath);
		this.pattern = pattern;
	}

	// ------------------------------------------------------------------------
	// �p�^�[���}�b�`���邷�ׂĂ̕������s�b�N�A�b�v���邩�ǂ���
	// ------------------------------------------------------------------------
	public void setFindAll(boolean findAll) {
		this.findAll = findAll;
	}

	// ------------------------------------------------------------------------
	// ���������Ɉ�v��������
	// ------------------------------------------------------------------------
	public List<String> getFindElements() {
		return findElements;
	}

	// ------------------------------------------------------------------------
	// ���������Ɉ�v�������ǂ���
	// ------------------------------------------------------------------------
	public boolean isPatternFound() {
		if (findElements.size() > 0)
			return true;
		return false;
	}

	// ------------------------------------------------------------------------
	// ���������Ɉ�v�������ǂ���
	// ------------------------------------------------------------------------
	public boolean isExists() {
		if (findElements.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// ------------------------------------------------------------------------
	// ���s����
	// ------------------------------------------------------------------------
	public void execute() {
		//		line = 0;
		findElements = new ArrayList();
		reader.open();
		String rec = reader.readLine();
		while (rec != null) {
			//			line++;
			if (pattern.matcher(rec).matches()) {
				//				System.out.println("At line:" + line + " =>" + rec);
				// �������������ƁA���̍s����String�ŕԂ�����
				// �ŏ��̏�����v��break������
				findElements.add(rec);
				if (findAll == false)
					break;
			}
			rec = reader.readLine();
		}
		;
		reader.close();
	}

	public static void main(String[] argv) {
		test20150323();
	}

	// ------------------------------------------------------------------------
	public static void test20150323() {
		System.out.println("Start");
		String regex = "vol";
		boolean ignoreCase = false;// �啶������������ʂ���ꍇtrue
		Pattern pattern = Regex.getPatternEx(regex, ignoreCase);
		List<String> pathList = DosEmu.xdir("c:/sample/*.txt");
		for (String path : pathList) {
			FileChecker ins = new FileChecker(path, pattern);
			// ins.setFindAll(true);
			ins.execute();
			List<String> findElements = ins.getFindElements();
			if (findElements.size() > 0) {
				System.out.println("��inPath:" + path);
			}
		}
		System.out.println("end");
	}

	// ------------------------------------------------------------------------
	public static void test20150320() {
		System.out.println("Start");
		String regex = "�E��";
		String inPath = "c:/jicfsKCNV_sql.txt";
		boolean ignoreCase = false;
		Pattern pattern = Regex.getPatternEx(regex, ignoreCase);

		// List<String> dirList = DosEmu.dir("C:/encode/*.txt");
		// for (String path : dirList) {
		// System.out
		// .println(path + "  >>  " + EncodeDetector.getEncode(path));
		// }

		kyPkg.filter.FileChecker ins = new kyPkg.filter.FileChecker(inPath,
				pattern);
		ins.setFindAll(true);
		ins.execute();
		if (ins.isPatternFound())
			System.out.println("Found!!");

		List<String> findElements = ins.getFindElements();
		if (findElements.size() > 0) {
			System.out.println("inPath:" + inPath);
		}
		System.out.println("end");
	}
}
