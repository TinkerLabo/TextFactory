package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.HashMap;

import kyPkg.task.Abs_BaseTask;

// 2015-09-16 yuasa�@�t�@�C����ǂݍ��ސ��`
@SuppressWarnings("unused")
public class Flt_Template_Read extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// ���̓N���[�W��
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private String delimiter = null;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Flt_Template_Read(String inPath) {
		reader = new EzReader(inPath);
	}

	// -------------------------------------------------------------------------
	// execute
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("## Start ##");
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();

		long wCnt = 0;
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = null;
		// ---------------------------------------------------------------------
		// incore
		// ---------------------------------------------------------------------
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				splited = reader.getSplited();
				System.out.println("Flt_Template_Read�@debug=>" + splited[0]);
				for (int i = 0; i < splited.length; i++) {
				}

				wCnt++;
			}
		}
		reader.close();

		elapse.stop();

	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String inPath = "C:/@qpr/home/123620000058/calc/#006_sorted.txt";
		new Flt_Template_Read(inPath).execute();
	}

}
