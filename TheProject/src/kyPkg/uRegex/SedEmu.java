package kyPkg.uRegex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kyPkg.converter.Inf_StrConverter;

public class SedEmu implements Inf_StrConverter {
	private Pattern pattern;
	private String replacement = "$2_$1";
	private boolean replaceAll = false;
	private String result = "";

	// sedのように、正規表現を条件に設定して・・・マッチした部分を変換などできると良い・・・これがファイルから指定できるとなお良いのではないか？
	// sed の s/xxxxx/yyyyy/g と同じ処理
	public SedEmu(String regex, String replacement, boolean replaceAll) {
		super();
		this.replacement = replacement;
		this.replaceAll = replaceAll;
		pattern = Pattern.compile(regex);
	}

	// ----------------------------------------------------------------------
	// Pattern.compile パラメータ:
	// regex - コンパイルされる表現
	// flags - マッチフラグ。CASE_INSENSITIVE、MULTILINE、DOTALL、UNICODE_CASE、CANON_EQ、
	// UNIX_LINES、LITERAL、および COMMENTS を含めることのできるビットマスク
	// 例外:
	// IllegalArgumentException - 定義済みマッチフラグに対応するビット値以外の値が flags に設
	// 定されている場合
	// PatternSyntaxException - 表現の構文が無効である場合
	//
	// 2番目の引数に修飾子を指定します。指定可能な値は次の通りです。
	//
	// ----------------------------------------------------------------------
	// Pattern.CASE_INSENSITIVE 大文字と小文字を区別しないマッチングを有効にする
	// Pattern.MULTILINE 複数行モードを有効にする
	// Pattern.DOTALL DOTALL モードを有効にする
	// Pattern.UNICODE_CASE Unicode に準拠した大文字と小文字を区別しないマッチングを有効にする
	// Pattern.CANON_EQ 正規等価を有効にする
	// Pattern.UNIX_LINES Unix ラインモードを有効にする
	// Pattern.LITERAL パターンのリテラル構文解析を有効にする
	// Pattern.COMMENTS パターン内で空白とコメントを使用できるようにする
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
		// s/Orange/オレンジ/g のように部分置換したい場合は　SedEmu("(Orange)", "オレンジ", true);
		String str = "Orange is 100yen, Banana is 180yen,PureOrange is 100yen, .";
		System.out.println("res:"
				+ new SedEmu("Orange", "オレンジ", true).convert(str));
		System.out.println("res:"
				+ new SedEmu("(\\d.+?)(yen)", "($2)$1", true).convert(str));
	}

	public static void main(String[] args) {
		test120409();
	}

}
