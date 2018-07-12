package kyPkg.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kyPkg.task.Abs_ProgressTask;

public class Flt_ConcatH extends Abs_ProgressTask {
	private Inf_iClosure inClosure = null; // ���̓N���[�W��
	private Inf_oClosure outClosure = null; // �o�̓N���[�W��
	private List<String> inlist;
	private String delimiter = "\t";
	private List<Queue<String>> matrix = null;
	private String regex = "\\w";// �X�y�[�X�Ȃǂ͏��������

	// regex="[a-z]+";

	// REGEX��z��Ŏ������ق����ǂ������m��Ȃ��E�E�E
	// �s�ʂɃt�B���^�����O�Ƃ����Ӗ��E�E�E
	public void setRegex(String regex) {
		this.regex = regex;
	}

	// -------------------------------------------------------------------------
	// 2011-10-05 yuasa
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Flt_ConcatH(String outPath, List<String> inlist) {
		super();
		this.inlist = inlist;
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
		super.start("Flt_ConcatH",2048);
		outClosure.open();
		for (String path : inlist) {
			loop(path);// loop
		}
		output();
		 
		outClosure.close();
		super.stop();// ����I��

	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private int loop(String inPath) {
		int line = -1;
		this.inClosure = new EzReader(inPath);
		inClosure.open();
		String[] cells = null;
		while ((cells = inClosure.readSplited()) != null) {
			line++;
			// �擪�s���̃��X�g��p�ӂ���i2�s�ڈȍ~�̃Z�����������ꍇ�̂Ă���j
			Queue<String> que = null;
			if (matrix == null) {
				matrix = new ArrayList();
			}
			if (line < matrix.size()) {
				que = matrix.get(line);
			} else {
				que = null;
			}
			if (que == null) {
				que = new LinkedList();
				matrix.add(que);
			}
			for (int i = 0; i < cells.length; i++) {
				if (cells[i].matches(regex))
					que.add(cells[i]);
			}
		}
		inClosure.close();
		return line;
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
		String rootDir = globals.ResControl.getQprRootDir();
		String abcPath = rootDir+"cba.txt";
		String cbaPath = rootDir+"abc.txt";
		String outPath = rootDir+"concat.txt";
		List inList = new ArrayList();
		inList.add(abcPath);
		inList.add(abcPath);
		inList.add(cbaPath);
		Flt_ConcatH venn = new Flt_ConcatH(outPath, inList);
		venn.execute();
	}

}
