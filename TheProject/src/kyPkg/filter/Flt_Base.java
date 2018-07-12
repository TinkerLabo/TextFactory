package kyPkg.filter;

import kyPkg.task.Abs_ProgressTask;

public class Flt_Base extends Abs_ProgressTask {
	protected Inf_iClosure inClosure = null; // ���̓N���[�W��
	protected Inf_oClosure outClosure = null; // �o�̓N���[�W��

	// -------------------------------------------------------------------------
	// 2011-10-05 yuasa
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Flt_Base(String outPath, String inPath) {
		super();
		this.inClosure = new EzReader(inPath);
		this.outClosure = new EzWriter(outPath);
	}

	public long getWriteCount() {
		return outClosure.getWriteCount();
	}

	public void setDelimiter(String delimiter) {
		this.outClosure.setDelimiter(delimiter);
	}

	@Override
	public void execute() {
		super.start("Flt_Base",2048);
		outClosure.open();
		loop();// loop
		outClosure.close();
		super.stop();// ����I��
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop() {
		long lCount = -1;
		inClosure.open();
		String[] cells = null;
		while ((cells = inClosure.readSplited()) != null) {
			lCount++;
			outClosure.write("outrec" + cells[0]);
		}
		inClosure.close();
		return lCount;
	}

	// -------------------------------------------------------------------------
	// �g�p�၄
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		// tester();
	}

	// test
	// public static void tester() {
	// // �A���P�[�g�f�[�^�����ԓ��L�����j�^�[�Ɍ��肷��
	// String userDir = kyPkg.uFile.ResControl.getQPRHome();
	// String inPath = ResControl.getCurrentPath();
	// String outPath = userDir+"828111000507/calc/current.txt";
	// Flt_Base venn = new Flt_Base(outPath, inPath);
	// venn.execute();
	// }

}
