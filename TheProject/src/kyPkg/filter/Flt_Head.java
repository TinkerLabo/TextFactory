package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.ListArrayUtil;

// 2016-01-05 �t�@�C���̐擪��萔�s�����o���ďo�͂���
public class Flt_Head extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// ���̓N���[�W��
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private long limit = 100;
	private List<String> result;

	public List<String> getResult() {
		return result;
	}

	public String getResultStr() {
		StringBuffer buf = new StringBuffer();
		for (String element : result) {
			buf.append(element);
			buf.append("\n");
		}
		return buf.toString();
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Flt_Head(String inPath, int limit) {
		this.reader = new EzReader(inPath);
		this.limit = limit;
	}

	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		result = new ArrayList();
		long lcnt = 0;
		super.setMessage("## Start ##");
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		reader.open();
		String line = reader.readLine();
		while (line != null) {
			lcnt++;
			if (lcnt <= limit) {
				//				System.out.println("debug:"+line);
				result.add(line);
				line = reader.readLine();
			} else {
				line = null;
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
		//�t�@�C���̐擪��256�s���o���ďo�͂���
		String inPath = "P:/20151225_yuasa/stdType2Head.csv";
		int limit = 256;
		Flt_Head ins = new Flt_Head(inPath, limit);
		ins.execute();
		List<String> result = ins.getResult();
		String outPath = "P:/20151225_yuasa/stdType2Headtop256.txt";
		ListArrayUtil.list2File(outPath, result);
	}

}
