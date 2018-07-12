package kyPkg.uRegex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kyPkg.converter.Inf_StrConverter;

public class SedEmu implements Inf_StrConverter {
	private Pattern pattern;
	private String replacement = "$2_$1";
	private boolean replaceAll = false;
	private String result = "";

	// sed�̂悤�ɁA���K�\���������ɐݒ肵�āE�E�E�}�b�`����������ϊ��Ȃǂł���Ɨǂ��E�E�E���ꂪ�t�@�C������w��ł���ƂȂ��ǂ��̂ł͂Ȃ����H
	// sed �� s/xxxxx/yyyyy/g �Ɠ�������
	public SedEmu(String regex, String replacement, boolean replaceAll) {
		super();
		this.replacement = replacement;
		this.replaceAll = replaceAll;
		pattern = Pattern.compile(regex);
	}

	// ----------------------------------------------------------------------
	// Pattern.compile �p�����[�^:
	// regex - �R���p�C�������\��
	// flags - �}�b�`�t���O�BCASE_INSENSITIVE�AMULTILINE�ADOTALL�AUNICODE_CASE�ACANON_EQ�A
	// UNIX_LINES�ALITERAL�A����� COMMENTS ���܂߂邱�Ƃ̂ł���r�b�g�}�X�N
	// ��O:
	// IllegalArgumentException - ��`�ς݃}�b�`�t���O�ɑΉ�����r�b�g�l�ȊO�̒l�� flags �ɐ�
	// �肳��Ă���ꍇ
	// PatternSyntaxException - �\���̍\���������ł���ꍇ
	//
	// 2�Ԗڂ̈����ɏC���q���w�肵�܂��B�w��\�Ȓl�͎��̒ʂ�ł��B
	//
	// ----------------------------------------------------------------------
	// Pattern.CASE_INSENSITIVE �啶���Ə���������ʂ��Ȃ��}�b�`���O��L���ɂ���
	// Pattern.MULTILINE �����s���[�h��L���ɂ���
	// Pattern.DOTALL DOTALL ���[�h��L���ɂ���
	// Pattern.UNICODE_CASE Unicode �ɏ��������啶���Ə���������ʂ��Ȃ��}�b�`���O��L���ɂ���
	// Pattern.CANON_EQ ���K������L���ɂ���
	// Pattern.UNIX_LINES Unix ���C�����[�h��L���ɂ���
	// Pattern.LITERAL �p�^�[���̃��e�����\����͂�L���ɂ���
	// Pattern.COMMENTS �p�^�[�����ŋ󔒂ƃR�����g���g�p�ł���悤�ɂ���
	// ----------------------------------------------------------------------

	@Override
	public String convert(String str) {
		Matcher m = pattern.matcher(str);
		if (replaceAll) {
			result = m.replaceAll(replacement);
		} else {
			result = m.replaceFirst(replacement);
		}
		return result;

	}

	public static void test120409() {
		// s/Orange/�I�����W/g �̂悤�ɕ����u���������ꍇ�́@SedEmu("(Orange)", "�I�����W", true);
		String str = "Orange is 100yen, Banana is 180yen,PureOrange is 100yen, .";
		System.out.println("res:"
				+ new SedEmu("Orange", "�I�����W", true).convert(str));
		System.out.println("res:"
				+ new SedEmu("(\\d.+?)(yen)", "($2)$1", true).convert(str));
	}

	public static void main(String[] args) {
		test120409();
	}

}
