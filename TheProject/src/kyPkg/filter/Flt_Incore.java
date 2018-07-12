package kyPkg.filter;

import kyPkg.task.Abs_BaseTask;

// 2016-02-29 yuasa
public class Flt_Incore extends Abs_BaseTask {
	private Inf_iClosure reader = null;
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Flt_Incore(Inf_iClosure reader) {
		this.reader = reader;
	}
	// -------------------------------------------------------------------------
	// ���s
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
