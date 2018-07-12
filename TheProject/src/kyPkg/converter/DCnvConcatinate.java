package kyPkg.converter;

import static kyPkg.util.Joint.join;

import kyPkg.filter.Inf_DualConverter;

public class DCnvConcatinate implements Inf_DualConverter {
	protected String delimiter = "\t";

	public DCnvConcatinate(String delimiter) {
		super();
		this.delimiter = delimiter;
	}

	// ------------------------------------------------------------------------
	// �t�B���^�A�^�C�v�P
	// arrayConverter = new DefaultArrayConverter();
	// ------------------------------------------------------------------------
	@Override
	public String convert(String[] lefts, String[] rights, int stat) {
		//XXX 20170725 �b��e�X�g�@�@�@ 
		if (lefts == null && rights == null) {
			return "";
		}
		if (lefts == null) {
			lefts = new String[rights.length];
			lefts[0]=rights[0];
		}
		if (rights == null) {
			rights = new String[lefts.length];
			rights[0]=lefts[0];
		}
		return lefts[0]+ delimiter +  join(lefts, delimiter,1) + delimiter +  join(rights, delimiter,1);
//		TODO	��������ꍇ��ID�݂̂��o�͂���ꍇ���l���Ă݂�E�E�E�@�N���X�^�[�����Ɏg���̂����E�E�E
//		TODO	��������̏����E�E�E�i�ڃ}�X�^�}�b�`���O
//		�܂�A�R���o�[�^�[�𕡐��I���\�ɂ���E�E�E

	}

	@Override
	public void init() {
	};

	@Override
	public void fin() {
	};

}
