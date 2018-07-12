package kyPkg.filter;

import kyPkg.task.Abs_BaseTask;

// 2016-06-01 �d������L�[���o�͂���
//�X�^���h�A���[�������Ă����΁@�o�b�`�Ŏ��݂Ɏg����E�E�E
public class Flt_DupKeys extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// ���̓N���[�W��
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private int keyCol = 0;
	private String preKey = null;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Flt_DupKeys(String outPath, String inPath, int keyCol) {
		this.reader = new EzReader(inPath);
		this.writer = new EzWriter(outPath);
		this.keyCol = keyCol;
		this.preKey = null;
	}

	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long wCnt = 0;
		reader.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (splited.length > keyCol) {
				String key = splited[keyCol];
				if (preKey != null && key.equals(preKey)) {
					System.out.println("duplicate:" + preKey);
					writer.write(preKey);
					wCnt++;
				}
				preKey = key;
			}
			splited = reader.readSplited();
		}
		reader.close();
		writer.close();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test01();
		test02();
	}

	public static void test01() {
		//---------------------------------------------------------------------
		//	�d�����Ă���L�[�i�i�`�m�R�[�h�Ȃǁj���t�@�C���ɏ����o��
		//---------------------------------------------------------------------
		String userDir = globals.ResControl.getQPRHome();
		String inPath = "c:/Users/EJQP7/hoge.txt";
		String outPath = "c:/Users/EJQP7/dupKeys.txt";
		int keyCol = 0;
		new Flt_DupKeys(outPath, inPath, keyCol).execute();
	}

	public static void test02() {
		//---------------------------------------------------------------------
		//	�L�[���i�d�����Ă���i�`�m�R�[�h�j�Ɉ�v������̂Ƃ��Ȃ����́i���j�[�N�ȃf�[�^�j�ɐU�蕪���o�͂��s��
		//---------------------------------------------------------------------
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		String path_M = "c:/Users/EJQP7/dupKeys.txt";
		String path_T = "c:/Users/EJQP7/hoge.txt";
		String path_O = "c:/Users/EJQP7/notDuplicated.txt";
		String path_D = "c:/Users/EJQP7/Duplicated.txt";
		Flt_Venn venn = new Flt_Venn(path_O, path_M, path_T,
				Flt_Venn.RIGHT_ONLY);//�d���͊܂܂Ȃ�
		venn.setOutPath_I(path_D);
		//���ꂼ����o�������̂����E�E�E�C���i�[�ƃg�����I�����[�̂��ꂼ���ʁX�Ɏ��o������
		//		venn.setMasterKeyCol(0);
		//		venn.setTranKeyCol(0);
		//		venn.setDelimiter(",");
		venn.execute();
		elapse.stop();
	//---------------------------------------------------------------------

	
	}

}
