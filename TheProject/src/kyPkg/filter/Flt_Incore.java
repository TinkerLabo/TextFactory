package kyPkg.filter;

import kyPkg.task.Abs_BaseTask;

// 2016-02-29 yuasa
public class Flt_Incore extends Abs_BaseTask {
	private Inf_iClosure reader = null;
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Incore(Inf_iClosure reader) {
		this.reader = reader;
	}
	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("## Start ##");
		reader.open();
		while (reader.readSplited() != null)
			;
		reader.close();
	}
	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {

	}

}
