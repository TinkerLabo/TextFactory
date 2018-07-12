package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import kyPkg.converter.Inf_ArrayCnv;

public class File2Matrix {
	private String encoding = "";
	private Inf_ArrayCnv cnv;
	private int skip = 0;
	private long limit = -1;
	private int checkCount = 100;
	private HashMap<String, List<Object>> analyzed;
	//	private Vector<Vector> matrix;
	private Vector matrix;
	private String path;
	private String delimiter = "";

	// -------------------------------------------------------------------------
	// アクセッサ
	// -------------------------------------------------------------------------
	public String getDelimiter() {
		return delimiter;
	}

	public String getEncoding() {
		return encoding;
	}

	public Vector<Vector> getMatrix() {
		return matrix;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setCnv(Inf_ArrayCnv cnv) {
		this.cnv = cnv;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public File2Matrix(String path) {
		super();
		this.path = path;
	}

	public File2Matrix(String path, Inf_ArrayCnv cnv) {
		this(path);
		this.cnv = cnv;
	}

	// -------------------------------------------------------------------------
	// ※delimiterに nullが指定された場合分割しない
	// ※delimiterに ""が指定された場合??自動判定
	// 20130221 ここでバグ・・・ファイルをロックしてしまうようだ・・・（この部分だけでの単体テストは問題なさそう・・・）
	// XXX skipパラメータを追加したい
	// -------------------------------------------------------------------------
	public void execute() {
		// System.out.println("### file2Vector ###");
		// ファイルが存在しないばあい等は空のマトリックスを返す
		// System.out.println("(1)debug1017 path:" + path);
		// FileUtil_.isLocked(path, "<201>");
		matrix = new Vector(0);
		if (path.trim().equals("") || new File(path).isFile() == false)
			return;
		analyzed = TextAnalyzer.analyzeIt(path, checkCount, cnv);
		// ---------------------------------------------------------------------
		// FileUtil_.isLocked(path, "after analyzeIt<203>");
		// ---------------------------------------------------------------------
		List<Object> types = analyzed.get(TextAnalyzer.TYPE);
		List<Object> maxWidths = analyzed.get(TextAnalyzer.MAX_WIDTH);
		List<Object> minWidths = analyzed.get(TextAnalyzer.MIN_WIDTH);
		List<Object> samples = analyzed.get(TextAnalyzer.SAMPLE);
		// ---------------------------------------------------------------------
		// for (int index = 0; index < typeList.size(); index++) {
		// System.out.println(" type:" + typeList.get(index) + " width:"
		// + (Integer) widthList.get(index) + " sample:"
		// + (String) sampleList.get(index));
		// }
		// FileUtil_.isLocked(path, "<777>");
		// ---------------------------------------------------------------------
		if (encoding.trim().equals(""))
			encoding = (String) analyzed.get(TextAnalyzer.ENCODE).get(0);
		if (delimiter != null && delimiter.equals(""))
			delimiter = (String) analyzed.get(TextAnalyzer.DELIM).get(0);
		// ---------------------------------------------------------------------
		// System.out.println("delimiter>>>" + delimiter);
		// ---------------------------------------------------------------------
		if (encoding.trim().equals(""))
			encoding = FileUtil.getDefaultEncoding();//20161222
		//			encoding = System.getProperty("file.encoding");
		if (limit < 0)
			limit = Long.MAX_VALUE;
		long iCnt = 0;
		long oCnt = 0;
		Vector cols = null;
		String val = "";
		String type = "";
		try {
			String rec;
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), encoding));
			while (br.ready()) {
				rec = br.readLine();
				iCnt++;
				if (iCnt > skip && rec != null) {
					if (delimiter == null) {
						// System.out.println("add rec>>>" + rec);
						matrix.add(rec);
					} else {
						// ※ split の引数はRegix
						String[] array = rec.split(delimiter);
						// カラム辞書が存在するなら変換する(2012-01-16)
						if (cnv != null)
							array = cnv.convert(array, 0);
						cols = new Vector(0);
						for (int col = 0; col < array.length; col++) {
							try {
								val = array[col].trim();
								if (val.endsWith("."))
									val = val + "0";
								type = (String) types.get(col);
								if (type.equals(TextAnalyzer.INTEGER)) {
									if (val.equals(""))
										val = "0";
									cols.add(
											new Integer(Integer.parseInt(val)));
								} else if (type.equals(TextAnalyzer.DOUBLE)) {
									if (val.equals(""))
										val = "0";
									cols.add(Double.parseDouble(val));
								} else {
									cols.add(array[col]);
								}

							} catch (Exception e) {
								System.out.println(e.toString());
								System.out.println(
										"@vectorUtil parsexxx error col:" + col
												+ " array[col]:" + array[col]);
								cols.add("type error:" + type);
							}
						}
						// System.out.println("add cols>>>" + cols);
						matrix.add(cols);
					}
					oCnt++;
					if (oCnt >= limit)
						break;
				}
			}
			// System.out.println("<<<br.close()>>>");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return;
	}

	// -------------------------------------------------------------------------
	// 以下、static で定義してある、簡単に使えるユーティリティ関数
	// -------------------------------------------------------------------------
	// 拡張子からデリミターを決定
	// 古いのはココを使っている
	// -------------------------------------------------------------------------
	public static Vector extract(String path) {
		String ext = FileUtil.getExt(path);
		String delimiter = FileUtil.getDefaultDelimiter(ext);
		File2Matrix ins = new File2Matrix(path);
		ins.setDelimiter(delimiter);
		ins.execute();
		return ins.getMatrix();
	}

	// -------------------------------------------------------------------------
	// 新しいのはココを使っている
	// -------------------------------------------------------------------------
	public static Vector extract(String path, Inf_ArrayCnv cnv) {
		File2Matrix ins = new File2Matrix(path, cnv);
		ins.execute();
		return ins.getMatrix();
	}

	// -------------------------------------------------------------------------
	public static Vector extract(String path, String delimiter) {
		int limit = -1;
		return extract(path, delimiter, limit);
	}

	// -------------------------------------------------------------------------
	public static Vector extract(String path, String delimiter, int limit) {
		File2Matrix ins = new File2Matrix(path);
		ins.setDelimiter(delimiter);
		ins.setLimit(limit);
		ins.execute();
		return ins.getMatrix();
	}

	public static Vector extract(String path, String delimiter, int limit,
			String encoding) {
		File2Matrix ins = new File2Matrix(path);
		ins.setDelimiter(delimiter);
		ins.setLimit(limit);
		ins.setEncoding(encoding);
		ins.execute();
		return ins.getMatrix();
	}

	public static Vector<Vector> extract(String path, int skip, int limit) {
		File2Matrix insF2M = new File2Matrix(path);
		insF2M.setLimit(limit);
		insF2M.setSkip(skip);
		insF2M.execute();
		return insF2M.getMatrix();
	}

	public static void debugTheMatrix(Vector<Vector> matrix) {
		if (matrix == null) {
			System.out.println("ERROR @debugTheMatrix matrix is null");
			return;
		}
		if (matrix.size() == 0) {
			System.out.println("ERROR @debugTheMatrix matrix.size()==0");
			return;
		}
		for (Vector vector : matrix) {
			for (Object element : vector) {
				System.out.print("■" + element.toString());
			}
			System.out.println("■");
		}
	}

	public static void main(String[] args) {
		testFile2Vector1();
	}

	// -------------------------------------------------------------------------
	// 単体で動かしても問題ない20130222
	// -------------------------------------------------------------------------
	public static void testFile2Vector0() {
		String path = "C:/loy1_Head.txt";
		String delimiter = "";

		Vector<Vector> matrix = extract(path, delimiter);
		for (Vector vector : matrix) {
			for (Object element : vector) {
				System.out.print("■" + element.toString());
			}
			System.out.println("■");
		}
	}

	public static void testFile2Vector1() {
		String path = "C:/loy1_Head.txt";
		int limit = 20;
		int skip = 1;
		Vector<Vector> matrix = extract(path, skip, limit);
		for (Vector vector : matrix) {
			for (Object element : vector) {
				System.out.print("■" + element.toString());
			}
			System.out.println("■");
		}
	}
}