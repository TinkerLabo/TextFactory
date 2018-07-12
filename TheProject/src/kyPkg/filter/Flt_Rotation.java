package kyPkg.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kyPkg.task.Abs_ProgressTask;

public class Flt_Rotation extends Abs_ProgressTask {
	private String dummyVal = "";
	private Inf_iClosure inClosure = null; // ���̓N���[�W��
	private Inf_oClosure outClosure = null; // �o�̓N���[�W��
	private String delimiter = "\t";
	private List<Queue<String>> matrix = null;

	// -------------------------------------------------------------------------
	// 2011-10-05 yuasa
	// CSV�f�[�^�̂w�C�x������������
	// �b�`�b�a�b�b�b�c�b
	// �b���b���b���b���b
	// �Ƃ������т̃f�[�^��
	// �b�`��
	// �b�a��
	// �b�b��
	// �b�c��
	// �Ƃ������т̂Ł[���ɕϊ�����
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Flt_Rotation(String outPath, String inPath) {
		super();
		this.inClosure = new EzReader(inPath); // R:Tran
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
		super.start("Flt_Rotation", 2048);
		outClosure.open();
		loop();// loop
		output();
		outClosure.close();
		super.stop();// ����I��
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop() {
		long iCount = -1;
		inClosure.open();
		String[] cells = null;
		while ((cells = inClosure.readSplited()) != null) {
			iCount++;
			// �擪�s���̃��X�g��p�ӂ���i2�s�ڈȍ~�̃Z�����������ꍇ�̂Ă���j
			if (matrix == null) {
				matrix = new ArrayList();
				for (int i = 0; i < cells.length; i++) {
					matrix.add(new LinkedList());
				}
			}
			for (int i = 0; i < matrix.size(); i++) {
				Queue que = matrix.get(i);
				if (cells.length > i) {
					que.add(cells[i]);
				} else {
					que.add(dummyVal);// DummyCell
				}
			}
		}
		inClosure.close();
		return iCount;
	}

	private void output() {
		long oCount = -1;
		StringBuffer buf = new StringBuffer();
		for (Queue<String> que : matrix) {
			buf.delete(0, buf.length());
			buf.append(que.remove());
			while (!que.isEmpty()) {
				buf.append(delimiter);
				buf.append(que.remove());
			}
			oCount++;
			outClosure.write(buf.toString());
		}
	}

	// -------------------------------------------------------------------------
	// �g�p�၄
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		tester();
	}

	// test
	public static void tester() {
		// abc.txt�@=�@qw�o
		// a b c d e f g h i j k l m n o
		// 1 2 3 4 5 6 7 8 9 0
		// a b c d e f g h i j
		// 1 2 3 4 5
		// }
		String rootDir = globals.ResControl.getQprRootDir();
		String abcPath = rootDir + "abc.txt";
		String cbaPath = rootDir + "cba.txt";
		new Flt_Rotation(cbaPath, abcPath).execute();
		// ���ϊ������t�@�C������͂ɂ��āA�ĕϊ�����ƌ��̕��тɖ߂�
		String cbaRPath = rootDir + "cbaR.txt";// �A���A�_�~�[�Z�����ǉ������
		new Flt_Rotation(cbaRPath, cbaPath).execute();
	}

	public static void tester2() {
		String rootDir = globals.ResControl.getQprRootDir();
		String abcPath = rootDir + "relMeta.txt";
		String cbaPath = rootDir + "relMetaR.txt";
		new Flt_Rotation(cbaPath, abcPath).execute();
		// ���ϊ������t�@�C������͂ɂ��āA�ĕϊ�����ƌ��̕��тɖ߂�
		String cbaRPath = rootDir + "relMetaRR.txt";// �A���A�_�~�[�Z�����ǉ������
		new Flt_Rotation(cbaRPath, cbaPath).execute();
	}

}
