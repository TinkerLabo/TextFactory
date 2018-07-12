package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.regex.Pattern;

import kyPkg.task.Abs_BaseTask;

// 2016-06-16 yuasa
//指定したカラムが正規表現にマッチしたデータのみを出力する
public class RegFilter_org extends Abs_BaseTask {
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private Pattern pattern;
	private int col = 0;
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public RegFilter_org(String outPath, String inPath, int col, String regex) {
		this.col = col;
		pattern = Pattern.compile(regex);
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath);
	}
	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long wCnt = 0;
		reader.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (splited.length > col) {
				String target = splited[col];
				if (pattern.matcher(target).matches()) {
					writer.write(join(splited, delimiter));
					wCnt++;
				}
			}
			splited = reader.readSplited();
		}
		reader.close();
		writer.close();
	}
	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testRegFilter();
	}
	public static void testRegFilter() {
		String wkDir = "C:/samples/生協テスト/";
		String inPath = wkDir + "SYOMSDT_1605.TXT";
		String targetPath = wkDir + "45or49.txt";
		String regex = "^49.*|^45.*";//49または45で始まるものに限定
		int col = 0;
		new RegFilter_org(targetPath, inPath, col, regex).execute();
	}
}
