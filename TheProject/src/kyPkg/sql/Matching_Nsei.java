package kyPkg.sql;

import static kyPkg.util.Joint.join;

import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.task.Abs_BaseTask;

// 20160615 yuasa
//�@�����f�[�^���}�X�^�[�f�[�^�ƃ}�b�`���O���āE�E�E�]�X�@
//TODO	�}�N���~���ɓ]������f�[�^�������ō���Ă��܂�
public class Matching_Nsei extends Abs_BaseTask {
	private int keyCol = 0;
	private String key_L = null;
	private String key_R = null;
	private Inf_iClosure reader_T = null; // ���̓N���[�W��
	private Inf_iClosure reader_M = null; // ���̓N���[�W��
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private String[] splited_T;
	private String[] splited_M;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Matching_Nsei(String outPath, String inPath_Tran, String inPath_Master) {
		reader_T = new EzReader(inPath_Tran);
		reader_M = new EzReader(inPath_Master);
		writer = new EzWriter(outPath);
	}

	// -------------------------------------------------------------------------
	// read_L
	// -------------------------------------------------------------------------
	private String read_T() {
		splited_T = reader_T.readSplited();
		if (splited_T != null && splited_T.length > keyCol) {
			return splited_T[keyCol].trim();
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// read_R
	// -------------------------------------------------------------------------
	private String read_M() {
		splited_M = reader_M.readSplited();
		if (splited_M != null && splited_M.length > keyCol) {
			return splited_M[keyCol].trim();
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long cnt_EQ = 0;
		long cnt_L = 0;
		long cnt_R = 0;
		reader_T.open();
		reader_M.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader_T.getDelimiter();
		key_L = read_T();
		key_R = read_M();
		int cmp = 0;
		while (key_L != null && key_R != null) {
			cmp = key_L.compareTo(key_R);
			// System.out.println("=> L:" + key_L + " R:" + key_R);
			if (cmp < 0) {
				writer.write(join(splited_T, delimiter));
				cnt_L++;//L only
				key_L = read_T();
			} else if (cmp > 0) {
				writer.write(join(splited_M, delimiter));
				cnt_R++;//R only
				key_R = read_M();
			} else {
				//	System.out.println("=> L:" + key_L + " R:" + key_R);
//				if (key_L.equals("4515445811310")) {
//					System.out.println("Helllo");
//				}

				for (int idx = 0; idx < splited_T.length; idx++) {
					if (idx != keyCol) { //�L�[�̃J�����͏����ΏۊO 
						String val_T = splited_T[idx];//new
						String val_M = splited_M[idx];//master
						//R������̃}�X�^�̓��e�A�EL�͐V�K�X�V�f�[�^�C���[�W
						//�󔒂��邢�͋�l�ȊO�̎���L�̒l�ŏ㏑������
						if (val_T.trim().equals("")) {
						} else if (kyPkg.converter.ValueChecker.isSPC(val_T)) {//�A������󔒕���
						} else if (kyPkg.converter.ValueChecker.isZERO(val_T)) {//�A������0
						} else if (kyPkg.converter.ValueChecker.isKJSPC(val_T)) {//�A�����銿���󔒕���
						} else if (val_M.equals(val_T)) {//�ύX�����邩�ǂ����E�E�E
						} else {
//							splited_T[idx] = val_M;//�ǂ�ɂ����Ă͂܂�Ȃ��ꍇ�̂ݏ㏑��������
							splited_M[idx] = val_T;//�ǂ�ɂ����Ă͂܂�Ȃ��ꍇ�̂ݏ㏑��������
						}
					}
				}
				writer.write(join(splited_M, delimiter));
//				writer.write(join(splited_T, delimiter));//20170720 �����Ⴄ�񂶂�Ȃ����H�H
				cnt_EQ++;
				key_L = read_T();
				key_R = read_M();
			}
		}
		reader_T.close();
		reader_M.close();
		writer.close();
		System.out.println("L only:" + cnt_L);
		System.out.println("R only:" + cnt_R);
		System.out.println("EQ    :" + cnt_EQ);
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testMatching();
	}

	public static void testMatching() {
		String wkDir = "C:/samples/�����e�X�g/";
		String inPath_L = wkDir + "formated.txt";
		String inPath_R = wkDir + "masterImage.txt";
		String outPath = wkDir + "xxxxx.OUT";
		new Matching_Nsei(outPath, inPath_L, inPath_R).execute();
	}
}
