package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kyPkg.uFile.DosEmu;
import kyPkg.uRegex.Regex;

public class FileChecker {
	private Inf_iClosure reader;
	//	private long line = 0;
	private boolean findAll = false;// 全件見つける場合　true
	private List<String> findElements;
	private Pattern pattern;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public FileChecker(String inPath, String regex, boolean ignoreCase) {
		this(inPath, Regex.getPatternEx(regex, ignoreCase));
	}

	public FileChecker(String inPath, Pattern pattern) {
		this.reader = new EzReader(inPath);
		this.pattern = pattern;
	}

	// ------------------------------------------------------------------------
	// パターンマッチするすべての部分をピックアップするかどうか
	// ------------------------------------------------------------------------
	public void setFindAll(boolean findAll) {
		this.findAll = findAll;
	}

	// ------------------------------------------------------------------------
	// 検索条件に一致した部分
	// ------------------------------------------------------------------------
	public List<String> getFindElements() {
		return findElements;
	}

	// ------------------------------------------------------------------------
	// 検索条件に一致したかどうか
	// ------------------------------------------------------------------------
	public boolean isPatternFound() {
		if (findElements.size() > 0)
			return true;
		return false;
	}

	// ------------------------------------------------------------------------
	// 検索条件に一致したかどうか
	// ------------------------------------------------------------------------
	public boolean isExists() {
		if (findElements.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// ------------------------------------------------------------------------
	// 実行部分
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
				// 見つかった部分と、その行数をStringで返したい
				// 最初の条件一致でbreakしたい
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
		boolean ignoreCase = false;// 大文字小文字を区別する場合true
		Pattern pattern = Regex.getPatternEx(regex, ignoreCase);
		List<String> pathList = DosEmu.xdir("c:/sample/*.txt");
		for (String path : pathList) {
			FileChecker ins = new FileChecker(path, pattern);
			// ins.setFindAll(true);
			ins.execute();
			List<String> findElements = ins.getFindElements();
			if (findElements.size() > 0) {
				System.out.println("■inPath:" + path);
			}
		}
		System.out.println("end");
	}

	// ------------------------------------------------------------------------
	public static void test20150320() {
		System.out.println("Start");
		String regex = "ウメ";
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
