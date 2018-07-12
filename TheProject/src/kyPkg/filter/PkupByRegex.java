package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kyPkg.task.Abs_BaseTask;

// 2016-11-01 yuasa
public class PkupByRegex extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Pattern pattern;
	private List<String> list;

	public List<String> getList() {
		return list;
	}

	private int from = 0;
	private int to = 0;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public PkupByRegex(String inPath, String regex, int from, int to) {
		this.pattern = Pattern.compile(regex);
		this.reader = new EzReader(inPath);
		this.list = new ArrayList();
		this.from = from;
		this.to = to;
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long wCnt = 0;
		reader.open();
		String rec = "";
		while ((rec = reader.readLine()) != null) {
			if (pattern.matcher(rec).matches()) {
				list.add(rec.substring(from, to));
			}
		}
		reader.close();
	}

}
