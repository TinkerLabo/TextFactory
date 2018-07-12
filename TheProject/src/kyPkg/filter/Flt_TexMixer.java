package kyPkg.filter;

import static kyPkg.uFile.ListArrayUtil.*;

import java.util.Iterator;
import java.util.List;

import globals.ResControl;
import kyPkg.task.Abs_BaseTask;

// 2010-11-10 �}�[�W yuasa(Template4���x�[�X�ɂ��Ă���)
public class Flt_TexMixer extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// ���̓N���[�W��
	// ------------------------------------------------------------------------
	private List<String> pathList = null;
	private Inf_oClosure writer = null;
	private static final String doneExt = "old";
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// String outPath �o�̓t�@�C���̃p�X
	// String dataDir�@�����Ώۃf�B���N�g���̃p�X
	// String regex�@�Ώے��o�p���K�\��
	// String doneExt�@�����ϓ��̓t�@�C���̊g���q�i�ύX��j
	// -------------------------------------------------------------------------
	public Flt_TexMixer(String outPath, String dataDir, String regex) {
		this(outPath, dir2ListWithDir(dataDir, regex));
	}
	public Flt_TexMixer(String outPath, List<String> list) {
		this.pathList = list;
		this.writer = new EzWriter(outPath);
	}
	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("�w�w�J�n");
		System.out.println("Flt_TexMixer: start");
		writer.open();
		Inf_iClosure reader = null;
		// ���X�g�Ɏw�肳�ꂽ�����̃t�@�C����ǂݍ��݁A�o�̓t�@�C���Ɍp������
		if (pathList == null) {
			System.out.println("�Y������t�@�C���͑��݂��Ȃ��׏����𒆒f���܂���");
			return;
		}

		for (String inPath : pathList) {
			System.out.println("inPath:" + inPath);
			reader = new EzReader(inPath);
			reader.open();
			while (writer.write(reader))
				;
			reader.close();

		}
		// for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
		// String inPath = (String) iterator.next();
		// }
		writer.close();
		if (!doneExt.equals("")) {
			for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
				String inPath = (String) iterator.next();
				// �g���q��ύX���ă}�[�W�ς݂ł��邱�Ƃ������B
				kyPkg.uFile.FileUtil.renExt(inPath, doneExt);
			}
		}
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String inDir = ResControl.D_DRV + "k14x/Ftp/�����X�V�f�[�^/data/";
		// "SYOMSDT_"�܂���"kobe"�Ŏn�܂�"������"������ɑ���".txt"�ŏI���p�^�[��
		String regex = "SYOMSDT_|kobe\\S*\\.txt";
		String outPath = inDir + "result.dat";
		new Flt_TexMixer(outPath, inDir, regex).execute();
	}
}
