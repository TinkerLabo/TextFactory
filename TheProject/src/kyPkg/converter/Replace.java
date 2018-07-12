package kyPkg.converter;

//-----------------------------------------------------------------------------
// ���镶���Q��Ή����镶���Q�ɕϊ�����i�}�b�`������̂������ꍇ�̓f�t�H���g���K�p�����j
//-----------------------------------------------------------------------------
public class Replace implements Inf_Converter {
	private static String delimiter = ":";
	private String regex = "";
	private String replacement = "";

	//-------------------------------------------------------------------------
	// �R���X�g���N�^
	//-------------------------------------------------------------------------
	public Replace(String regex, String replacement) {
		this(regex + delimiter + replacement);
	}

	//-------------------------------------------------------------------------
	// �R���X�g���N�^	String�ЂƂ������ɂƂ�i�hbefore:after�h�j 
	//	\ * + . ? { } ( ) [ ] ^ $ - |
	//-------------------------------------------------------------------------
	public Replace(String param) {
		int index = param.indexOf(delimiter, 1);//��؂蕶����2�����ڈڍs�ɏo������F�Ȃ̂ŁF���̂��̂�ϊ����邱�Ƃ��\
		if (index < 0) {
			System.out.println("#ERROR usage...");
			return;
		}
		regex = param.substring(0, index);
		replacement = param.substring(index + 1);
		if (replacement.equalsIgnoreCase("\\t"))
			replacement = "\t";//	\t	�����^�u
		if (replacement.equalsIgnoreCase("\\n"))
			replacement = "\n";//	\n	���s
		if (replacement.equalsIgnoreCase("\\r"))
			replacement = "\r";//	\r	���A
		if (replacement.equalsIgnoreCase("\\f"))
			replacement = "\f";//	\f	���y�[�W
		if (replacement.equalsIgnoreCase("\\b"))
			replacement = "\b";//	\b	�o�b�N�X�y�[�X
		if (replacement.equalsIgnoreCase("\\'"))
			replacement = "\'";//	\'	�V���O���N�I�[�e�[�V����
		//		System.out.println("bef:" + regex);
		//		System.out.println("aft:" + replacement);
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.util.Inf_CellConverter#convert(java.lang.String)
	 */
	@Override
	public String convert(String cell, String[] cells) {
		return cell.replaceAll(regex, replacement);
	}

	public static void main(String[] argv) {
		test00();
	}

	public static void test00() {
		Inf_Converter translate = new Replace("\\$", ".");
		System.out.println(
				"# test=>" + translate.convert("yuasa@tokyu-agc$co$jp", null));
	}

}
