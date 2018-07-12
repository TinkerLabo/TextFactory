package kyPkg.filter;

import java.util.List;
import java.util.Vector;

public class Guess4CreateTable extends Guess {
	public static final String CREATE = "CREATE";
	private static final String GO = "GO";
	private boolean flg = false;

	// ------------------------------------------------------------------------
	// SQLのCreate Tableより型とフィールド名を抜き出す　20140717
	// ※本当は一度すべて連結したうえでカンマで区切って処理をしなければならないのかも知れない
	// ------------------------------------------------------------------------
	public void autoDetect(Vector<Vector> matrix) {
		if (matrix == null || matrix.size() <= 0)
			return;
		flg = false;
		for (List<String> rowObj : matrix) {
			String element = rowObj.get(0);
			element = element.replace("(", " ");
			element = element.replace(")", " ");
			element = element.trim();
			element = element.toUpperCase();
			if (element.indexOf(GO) >= 0)
				flg = false;
			if (flg) {
				String[] array = element.split(" ");
				if (array.length >= 2) {
					String name = array[0];
					String type = array[1].toLowerCase();
					String n = "";
					if (nSet.contains(type)) {
						if (kyPkg.uRegex.Regex.isNumeric(array[2])) {
							int iVal = Integer.valueOf(array[2]);
							n = String.valueOf(iVal);
						}
					}
					names.add(name);
					types.add(type);
					figures.add(n);
					// System.out.println("@Guess4CreateTable field>" + array[0]
					// + " type>" + array[1] + " n:" + n);
				}
			}
			if (element.indexOf(CREATE) >= 0)
				flg = true;
		}

	}

	public Guess4CreateTable() {
		super();
	}

	@Override
	public void analyzeIt(String inPath, boolean headOpt) {
		int limit = -1;
		int skip = 0;
		Vector<Vector> matrix = super.getMatrix(inPath, skip, limit);
		if (matrix == null || matrix.size() == 0) {
			System.out.println("ERROR?! Empty Data?:" + inPath);
			return;
		}
		autoDetect(matrix);
	}

	public static void main(String[] argv) {
		System.out.println("20140724 test>>");
		String path = "C:/ITEM_SUB.SQL";
		Guess4CreateTable guess = new Guess4CreateTable();
		guess.analyzeIt(path, true);

		List<String> wNames = guess.getNames();
		System.out.println("20140724 test check1>>");
		for (String element : wNames) {
			System.out.println("wNames>>" + element);
		}
		System.out.println("20140724 test check2>>");
		List<String> wTypes = guess.getTypes();
		for (String element : wTypes) {
			System.out.println("wTypes>>" + element);
		}
		System.out.println("20140724 test check3>>");
		List<String> wFigure = guess.getFigures();
		for (String element : wFigure) {
			System.out.println("wFigure>>" + element);
		}
	}
}
