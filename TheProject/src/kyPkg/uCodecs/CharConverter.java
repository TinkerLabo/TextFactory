package kyPkg.uCodecs;

import kyPkg.converter.Inf_StrConverter;

public class CharConverter implements Inf_StrConverter {
	public static final String HOST_KANJI = "cnvCKJ";
	public static final String KANA2NORMAL = "cnvK2N";
	public static final String NG_EXCEL = "cnvNGExcel";
	public static final String NG_FNM = "cnvNGFnm";
	public static final String NARROW = "cnvNarrow";
	public static final String FIX_LENGTH = "cnvFixLength";
	public static final String WIDE_CLASSIC = "cnvWide_classic";
	public static final String WIDE = "cnvWide";
	private CharConv conv;
	private String op1 = " ";
	private int key = -1;

	public CharConverter(String type) {
		this(type, "");
	}

	public CharConverter(String type, String op1) {
		conv = CharConv.getInstance();
		this.op1 = op1;
		if (type.equals(WIDE))
			key = 1;
		if (type.equals(WIDE_CLASSIC))
			key = 2;
		if (type.equals(FIX_LENGTH))
			key = 3;
		if (type.equals(NARROW))
			key = 4;
		if (type.equals(NG_FNM))
			key = 5;
		if (type.equals(NG_EXCEL))
			key = 6;
		if (type.equals(KANA2NORMAL))
			key = 7;
		if (type.equals(HOST_KANJI))
			key = 8;
	}

	@Override
	public String convert(String val) {
		switch (key) {
		case 1:
			// �@�S�p�������@�J�^�J�i�ϊ�����������ۂ̌꒷����������@���@�L�����N�^�z��g�p��
			return conv.cnvWide(val);
		case 2:
			return conv.cnvWide_classic(val);
		case 3:
			// �Œ蒷�ɂ���(�o�C�g���ł͂Ȃ��̂Œ��ӁI)
			return CharConv.cnvFixLength(val, Integer.valueOf(op1), ' ');
		case 4:
			// �S�p�����p
			return conv.cnvNarrow(val, '?');
		case 5:
			// �t�@�C�����Ɏg���Ȃ�������ϊ�
			return conv.cnvNGFnm(val);
		case 6:
			// �G�N�Z���̃V�[�g���ȂǂŎg���Ȃ�������ϊ�
			return conv.cnvNGExcel(val);
		case 7:
			// �ŕ������m�[�}���C�Y for Host(kana => normal)
			return conv.cnvK2N(val);// �ŕ������m�[�}���C�Y
		case 8:
			// �������g�������p�Ƀm�[�}���C�Y for Host(���� => normal)
			return conv.cnvCKJ(val);// �z�X�g�p�����ɃR���o�[�g���郍�W�b�N
		default:
			return null;
		}
	}

}
