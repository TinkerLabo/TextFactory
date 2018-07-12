package kyPkg.filter;
import static kyPkg.uFile.ListArrayUtil.*;

import java.util.Iterator;
import java.util.List;

import kyPkg.task.Abs_BaseTask;


// 2010-11-10 �}�[�W yuasa
public class Flt_Template4 extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// ���̓N���[�W��
	// ------------------------------------------------------------------------
	private List pathList = null;
	private Inf_oClosure writer = null;
	private String doneExt = "";
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	//	String outPath �o�̓t�@�C���̃p�X
	//	String dataDir�@�����Ώۃf�B���N�g���̃p�X
	//	String regex�@�Ώے��o�p���K�\��
	//	String doneExt�@�����ϓ��̓t�@�C���̊g���q�i�ύX��j
	// -------------------------------------------------------------------------
	public Flt_Template4(String outPath, String dataDir, String regex,String doneExt) {
		this(outPath, dir2ListWithDir(dataDir, regex),doneExt);
	}
	public Flt_Template4(String outPath, List<String> list, String doneExt) {
		this.pathList = list;
		this.writer = new EzWriter(outPath);
		this.doneExt = doneExt.trim();
	}
	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("## Start ##");
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();

		writer.open();
		Inf_iClosure reader = null;
		// ���X�g�Ɏw�肳�ꂽ�����̃t�@�C����ǂݍ��݁A�o�̓t�@�C���Ɍp������
		if (pathList == null) {
			System.out.println("�Y������t�@�C���͑��݂��Ȃ��׏����𒆒f���܂���");
			return;
		}

		for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
			String inPath = (String) iterator.next();
			System.out.println("inPath:" + inPath);
			reader = new EzReader(inPath);
			reader.open();
			while (writer.write(reader))
				;
			reader.close();
		}
		writer.close();
		if (!doneExt.equals("")) {
			for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
				String inPath = (String) iterator.next();
				// �g���q��ύX���ă}�[�W�ς݂ł��邱�Ƃ������B
				kyPkg.uFile.FileUtil.renExt(inPath, doneExt);
			}
		}
		elapse.stop();

	}
	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}
	public static void test1() {
		String inDir = "c:/mixed/";
		String outPath = "c:/mixed/result.dat";
		String regex = "\\S*\\.txt";
		new Flt_Template4(outPath, inDir, regex, "").execute();
	}
}
