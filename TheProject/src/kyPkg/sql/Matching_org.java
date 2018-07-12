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
public class Matching_org extends Abs_BaseTask {
	private int keyCol = 0;
	private String key_L = null;
	private String key_R = null;
	private Inf_iClosure reader_L = null; // ���̓N���[�W��
	private Inf_iClosure reader_R = null; // ���̓N���[�W��
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private String[] splited_L;
	private String[] splited_R;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Matching_org(String outPath, String inPath_L, String inPath_R) {
		reader_L = new EzReader(inPath_L);
		reader_R = new EzReader(inPath_R);
		writer = new EzWriter(outPath);
	}

	// -------------------------------------------------------------------------
	// read_L
	// -------------------------------------------------------------------------
	private String read_L() {
		splited_L = reader_L.readSplited();
		if (splited_L != null && splited_L.length > keyCol) {
			return splited_L[keyCol].trim();
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// read_R
	// -------------------------------------------------------------------------
	private String read_R() {
		splited_R = reader_R.readSplited();
		if (splited_R != null && splited_R.length > keyCol) {
			return splited_R[keyCol].trim();
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
		reader_L.open();
		reader_R.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader_L.getDelimiter();
		key_L = read_L();
		key_R = read_R();
		int cmp = 0;
		while (key_L != null && key_R != null) {
			cmp = key_L.compareTo(key_R);
			// System.out.println("=> L:" + key_L + " R:" + key_R);
			if (cmp < 0) {
				writer.write(join(splited_L, delimiter));
				cnt_L++;//L only
				key_L = read_L();
			} else if (cmp > 0) {
				writer.write(join(splited_R, delimiter));
				cnt_R++;//R only
				key_R = read_R();
			} else {
				//	System.out.println("=> L:" + key_L + " R:" + key_R);
//				if (key_L.equals("4515445811310")) {
//					System.out.println("Helllo");
//				}

				for (int idx = 0; idx < splited_L.length; idx++) {
					if (idx != keyCol) { //�L�[�̃J�����͏����ΏۊO 
						String val_L = splited_L[idx];//new
						String val_R = splited_R[idx];//master
//						System.out.println("val_L:" + val_L);
//						System.out.println("val_R:" + val_R);
						//R������̃}�X�^�̓��e�A�EL�͐V�K�X�V�f�[�^�C���[�W
						//�󔒂��邢�͋�l�ȊO�̎���L�̒l�ŏ㏑������
						if (val_L.equals("")) {
						} else if (kyPkg.converter.ValueChecker.isSPC(val_L)) {//�A������󔒕���
						} else if (kyPkg.converter.ValueChecker.isZERO(val_L)) {//�A������0
						} else if (kyPkg.converter.ValueChecker.isKJSPC(val_L)) {//�A�����銿���󔒕���
						} else if (val_R.equals(val_L)) {//�ύX�����邩�ǂ����E�E�E
						} else {
							//�@splited_R[idx] = "��" + val_L;
//							splited_R[idx] = val_L;//�ǂ�ɂ����Ă͂܂�Ȃ��ꍇ�̂ݏ㏑��������
							splited_L[idx] = val_R;//�ǂ�ɂ����Ă͂܂�Ȃ��ꍇ�̂ݏ㏑��������
						}
					}
				}
				writer.write(join(splited_R, delimiter));
				cnt_EQ++;
				key_L = read_L();
				key_R = read_R();
			}
		}
		reader_L.close();
		reader_R.close();
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
		new Matching_org(outPath, inPath_L, inPath_R).execute();
	}
}
