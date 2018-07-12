package kyPkg.converter;

//-----------------------------------------------------------------------------
// ある文字群を対応する文字群に変換する（マッチするものが無い場合はデフォルトが適用される）
//-----------------------------------------------------------------------------
public class Replace implements Inf_Converter {
	private static String delimiter = ":";
	private String regex = "";
	private String replacement = "";

	//-------------------------------------------------------------------------
	// コンストラクタ
	//-------------------------------------------------------------------------
	public Replace(String regex, String replacement) {
		this(regex + delimiter + replacement);
	}

	//-------------------------------------------------------------------------
	// コンストラクタ	Stringひとつを引数にとる（”before:after”） 
	//	\ * + . ? { } ( ) [ ] ^ $ - |
	//-------------------------------------------------------------------------
	public Replace(String param) {
		int index = param.indexOf(delimiter, 1);//区切り文字は2文字目移行に出現する：なので：そのものを変換することも可能
		if (index < 0) {
			System.out.println("#ERROR usage...");
			return;
		}
		regex = param.substring(0, index);
		replacement = param.substring(index + 1);
		if (replacement.equalsIgnoreCase("\\t"))
			replacement = "\t";//	\t	水平タブ
		if (replacement.equalsIgnoreCase("\\n"))
			replacement = "\n";//	\n	改行
		if (replacement.equalsIgnoreCase("\\r"))
			replacement = "\r";//	\r	復帰
		if (replacement.equalsIgnoreCase("\\f"))
			replacement = "\f";//	\f	改ページ
		if (replacement.equalsIgnoreCase("\\b"))
			replacement = "\b";//	\b	バックスペース
		if (replacement.equalsIgnoreCase("\\'"))
			replacement = "\'";//	\'	シングルクオーテーション
		//		System.out.println("bef:" + regex);
		//		System.out.println("aft:" + replacement);
	}

	/*
	 * (非 Javadoc)
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
