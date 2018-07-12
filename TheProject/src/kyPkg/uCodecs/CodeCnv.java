package kyPkg.uCodecs;
public class CodeCnv{
	//---+---------+---------+---------+---------+---------+---------+---------+
	// 文字列を固定長にする処理・・・全角にするとか半角にするとかはどうしようか？
	// 使用例  CodeCnv.fixStr("あいうえお",3); とすると"あいう"が返る・・予定だが
	//---+---------+---------+---------+---------+---------+---------+---------+
	public static String fixStr(String pStr,int pLen){
		String wRtn = "";
		if (pStr.length() < pLen){
			StringBuffer wSbuf = new StringBuffer();
			wSbuf.append(pStr);
			for(int i = pStr.length();i < pLen;i++){
				wSbuf.append(" ");
			}
			wRtn = wSbuf.toString();
		} else {
			wRtn = pStr.substring(0,pLen);
		}
		return wRtn;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	// 文字列変換などなど
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** 半角数字文字列かどうか
	*  @param	pStr	検査文字列
	*  @return			半角数字のみの文字列ならtrue
	*/
	//CodeCnv.isNumeric(String pStr)
	public static boolean isNumeric(String pStr){
//		System.out.println("isNumeric in :" + pStr);
		boolean wRtn = false;
		if (pStr.matches("[+-]*\\d+[.]*\\d*")) {
			//System.out.println("チェック結果：半角数字のみの文字列です。");
			wRtn = true;
		} else {
			//System.out.println("チェック結果：半角数字以外の文字が含まれます。");
			wRtn = false;
		}
//		System.out.println("isNumeric Ans:" + wRtn);
		return wRtn;
	}

	//---+---------+---------+---------+---------+---------+---------+---------+
	/** 半角英数字文字列かどうか
	*  @param	pStr	検査文字列
	*  @return			半角数字のみの文字列ならtrue
	*/
	public static boolean isAlphaNum(String pStr){
		System.out.println("isNarrow in :" + pStr);
		boolean wRtn = false;
		if (pStr.matches("[0-9a-zA-Z]+")) {
			System.out.println("チェック結果：半角英数のみの文字列です。");
			wRtn = true;
		} else {
			System.out.println("チェック結果：半角英数以外の文字が含まれます。");
			wRtn = false;
		}
		System.out.println("isNarrow Ans:" + wRtn);
		return wRtn;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** ストリング中の半角文字を全角に変換
	*  @param	pChar	入力文字（半角 文字列）
	*  @return			出力文字（全角 文字列）
	*  《使用例》
	*  	String wCnv = cnvWide("ｱｲｳ$#123ﾊﾗﾎﾛﾋﾘﾊﾚabcdefgABC");
	*  	System.out.println("After=>" + wCnv );
	*/
	public static String cnvWide(String pStr){
		char[] cArray = pStr.toCharArray();
		for(int i = 0;i<pStr.length();i++){
			char wWide = cnvWide(cArray[i]);
			System.out.println("in:"+cArray[i]+" => out:"+wWide);
			cArray[i] = wWide;
		}
		return new String(cArray);
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** ストリング中の全角文字を半角に変換
	*  @param	pChar	入力文字（全角 文字列）
	*  @return			出力文字（半角 文字列）
	*/
	public static String cnvNarrow(String pStr){
		char[] cArray = pStr.toCharArray();
		for(int i = 0;i<pStr.length();i++){
			char wWide = cnvNarrow(cArray[i]);
			System.out.println("in:"+cArray[i]+" => out:"+wWide);
			cArray[i] = wWide;
		}
		return new String(cArray);
	}

	//---+---------+---------+---------+---------+---------+---------+---------+
	/** ファイル名に使えない文字を変換する
	*  ￥／：＊？”＜＞｜全角にすりゃなんの問題もないぞ！！（拡張子は別）
	*  @param	pChar	入力文字
	*  @return			出力文字
	*/
	public static char cnvNGfnm(char pChar){
		char wNG =  ' ';
		switch(pChar){
			case'\\' : wNG = '_' ;break;
			case'/'  : wNG = '_' ;break;
			case':'  : wNG = '_' ;break;
			case'*'  : wNG = '_' ;break;
			case'?'  : wNG = '_' ;break;
			case'\"' : wNG = '_' ;break;
			case'<'  : wNG = '_' ;break;
			case'>'  : wNG = '_' ;break;
			case'|'  : wNG = '_' ;break;
		}
		return wNG;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** エクセルのシート名などで使えない文字を変換する
	*  @param	pChar	入力文字
	*  @return			出力文字
	*/
	public static char cnvNGfExcel(char pChar){
		char wNG =  ' ';
		switch(pChar){
			case'\\' : wNG = '_' ;break;
			case'/'  : wNG = '_' ;break;
			case':'  : wNG = '_' ;break;
			case'*'  : wNG = '_' ;break;
			case'?'  : wNG = '_' ;break;
			case'\"' : wNG = '_' ;break;
			case'<'  : wNG = '_' ;break;
			case'>'  : wNG = '_' ;break;
			case'|'  : wNG = '_' ;break;
		}
		return wNG;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	public static void main(String[] argv){
		 new CodeCnv();
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** キャラクタ半角文字を全角に変換
	*  @param	pChar	入力文字（半角 キャラクタ）
	*  @return			出力文字（全角 キャラクタ）
	*/
	public static char cnvWide(char pChar){
		char wWide = '■';
		switch(pChar){
			case ' '  : wWide = '　';break;
			case '!'  : wWide = '！';break;
			case '"'  : wWide = '”';break;
			case '#'  : wWide = '＃';break;
			case '$'  : wWide = '＄';break;
			case '%'  : wWide = '％';break;
			case '&'  : wWide = '＆';break;
			case '\'' : wWide = '’';break;
			case '('  : wWide = '（';break;
			case ')'  : wWide = '）';break;
			case '*'  : wWide = '＊';break;
			case '+'  : wWide = '＋';break;
			case ','  : wWide = '，';break;
//			case '-'  : wWide = '－';break;
			case '.'  : wWide = '．';break;
			case '/'  : wWide = '／';break;
			case '0'  : wWide = '０';break;
			case '1'  : wWide = '１';break;
			case '2'  : wWide = '２';break;
			case '3'  : wWide = '３';break;
			case '4'  : wWide = '４';break;
			case '5'  : wWide = '５';break;
			case '6'  : wWide = '６';break;
			case '7'  : wWide = '７';break;
			case '8'  : wWide = '８';break;
			case '9'  : wWide = '９';break;
			case ':'  : wWide = '：';break;
			case ';'  : wWide = '；';break;
			case '<'  : wWide = '＜';break;
			case '='  : wWide = '＝';break;
			case '>'  : wWide = '＞';break;
			case '?'  : wWide = '？';break;
			case '@'  : wWide = '＠';break;
			case 'A'  : wWide = 'Ａ';break;
			case 'B'  : wWide = 'Ｂ';break;
			case 'C'  : wWide = 'Ｃ';break;
			case 'D'  : wWide = 'Ｄ';break;
			case 'E'  : wWide = 'Ｅ';break;
			case 'F'  : wWide = 'Ｆ';break;
			case 'G'  : wWide = 'Ｇ';break;
			case 'H'  : wWide = 'Ｈ';break;
			case 'I'  : wWide = 'Ｉ';break;
			case 'J'  : wWide = 'Ｊ';break;
			case 'K'  : wWide = 'Ｋ';break;
			case 'L'  : wWide = 'Ｌ';break;
			case 'M'  : wWide = 'Ｍ';break;
			case 'N'  : wWide = 'Ｎ';break;
			case 'O'  : wWide = 'Ｏ';break;
			case 'P'  : wWide = 'Ｐ';break;
			case 'Q'  : wWide = 'Ｑ';break;
			case 'R'  : wWide = 'Ｒ';break;
			case 'S'  : wWide = 'Ｓ';break;
			case 'T'  : wWide = 'Ｔ';break;
			case 'U'  : wWide = 'Ｕ';break;
			case 'V'  : wWide = 'Ｖ';break;
			case 'W'  : wWide = 'Ｗ';break;
			case 'X'  : wWide = 'Ｘ';break;
			case 'Y'  : wWide = 'Ｙ';break;
			case 'Z'  : wWide = 'Ｚ';break;
			case '['  : wWide = '［';break;
			case '\\' : wWide = '￥';break;
			case ']'  : wWide = '］';break;
			case '^'  : wWide = '＾';break;
			case '_'  : wWide = '＿';break;
			case '`'  : wWide = '｀';break;
			case 'a'  : wWide = 'ａ';break;
			case 'b'  : wWide = 'ｂ';break;
			case 'c'  : wWide = 'ｃ';break;
			case 'd'  : wWide = 'ｄ';break;
			case 'e'  : wWide = 'ｅ';break;
			case 'f'  : wWide = 'ｆ';break;
			case 'g'  : wWide = 'ｇ';break;
			case 'h'  : wWide = 'ｈ';break;
			case 'i'  : wWide = 'ｉ';break;
			case 'j'  : wWide = 'ｊ';break;
			case 'k'  : wWide = 'ｋ';break;
			case 'l'  : wWide = 'ｌ';break;
			case 'm'  : wWide = 'ｍ';break;
			case 'n'  : wWide = 'ｎ';break;
			case 'o'  : wWide = 'ｏ';break;
			case 'p'  : wWide = 'ｐ';break;
			case 'q'  : wWide = 'ｑ';break;
			case 'r'  : wWide = 'ｒ';break;
			case 's'  : wWide = 'ｓ';break;
			case 't'  : wWide = 'ｔ';break;
			case 'u'  : wWide = 'ｕ';break;
			case 'v'  : wWide = 'ｖ';break;
			case 'w'  : wWide = 'ｗ';break;
			case 'x'  : wWide = 'ｘ';break;
			case 'y'  : wWide = 'ｙ';break;
			case 'z'  : wWide = 'ｚ';break;
			case '{'  : wWide = '｛';break;
			case '|'  : wWide = '｜';break;
			case '}'  : wWide = '｝';break;
			case '~'  : wWide = '￣';break;
			case '｡'  : wWide = '。';break;
			case '｢'  : wWide = '「';break;
			case '｣'  : wWide = '」';break;
			case '､'  : wWide = '、';break;
			case '･'  : wWide = '・';break;
			case 'ｦ'  : wWide = 'ヲ';break;
			case 'ｧ'  : wWide = 'ァ';break;
			case 'ｨ'  : wWide = 'ィ';break;
			case 'ｩ'  : wWide = 'ゥ';break;
			case 'ｪ'  : wWide = 'ェ';break;
			case 'ｫ'  : wWide = 'ォ';break;
			case 'ｬ'  : wWide = 'ャ';break;
			case 'ｭ'  : wWide = 'ュ';break;
			case 'ｮ'  : wWide = 'ョ';break;
			case 'ｯ'  : wWide = 'ッ';break;
			case '-'  : wWide = '－';break;
			case 'ｱ'  : wWide = 'ア';break;
			case 'ｲ'  : wWide = 'イ';break;
			case 'ｳ'  : wWide = 'ウ';break;
			case 'ｴ'  : wWide = 'エ';break;
			case 'ｵ'  : wWide = 'オ';break;
			case 'ｶ'  : wWide = 'カ';break;
			case 'ｷ'  : wWide = 'キ';break;
			case 'ｸ'  : wWide = 'ク';break;
			case 'ｹ'  : wWide = 'ケ';break;
			case 'ｺ'  : wWide = 'コ';break;
			case 'ｻ'  : wWide = 'サ';break;
			case 'ｼ'  : wWide = 'シ';break;
			case 'ｽ'  : wWide = 'ス';break;
			case 'ｾ'  : wWide = 'セ';break;
			case 'ｿ'  : wWide = 'ソ';break;
			case 'ﾀ'  : wWide = 'タ';break;
			case 'ﾁ'  : wWide = 'チ';break;
			case 'ﾂ'  : wWide = 'ツ';break;
			case 'ﾃ'  : wWide = 'テ';break;
			case 'ﾄ'  : wWide = 'ト';break;
			case 'ﾅ'  : wWide = 'ナ';break;
			case 'ﾆ'  : wWide = 'ニ';break;
			case 'ﾇ'  : wWide = 'ヌ';break;
			case 'ﾈ'  : wWide = 'ネ';break;
			case 'ﾉ'  : wWide = 'ノ';break;
			case 'ﾊ'  : wWide = 'ハ';break;
			case 'ﾋ'  : wWide = 'ヒ';break;
			case 'ﾌ'  : wWide = 'フ';break;
			case 'ﾍ'  : wWide = 'ヘ';break;
			case 'ﾎ'  : wWide = 'ホ';break;
			case 'ﾏ'  : wWide = 'マ';break;
			case 'ﾐ'  : wWide = 'ミ';break;
			case 'ﾑ'  : wWide = 'ム';break;
			case 'ﾒ'  : wWide = 'メ';break;
			case 'ﾓ'  : wWide = 'モ';break;
			case 'ﾔ'  : wWide = 'ヤ';break;
			case 'ﾕ'  : wWide = 'ユ';break;
			case 'ﾖ'  : wWide = 'ヨ';break;
			case 'ﾗ'  : wWide = 'ラ';break;
			case 'ﾘ'  : wWide = 'リ';break;
			case 'ﾙ'  : wWide = 'ル';break;
			case 'ﾚ'  : wWide = 'レ';break;
			case 'ﾛ'  : wWide = 'ロ';break;
			case 'ﾜ'  : wWide = 'ワ';break;
			case 'ﾝ'  : wWide = 'ン';break;
			case 'ﾞ'  : wWide = '゛';break;
			case 'ﾟ'  : wWide = '゜';break;
//		  default   : wWide = '■';
		}
		return wWide;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** キャラクタ全角文字を半角に変換
	*  @param	pChar	入力文字（全角 キャラクタ）
	*  @return			出力文字（半角 キャラクタ）
	*/
	public static char cnvNarrow(char pChar){
		//濁音（ガ･ザ･ダ･バの4行）、半濁音は未対応・・語の長さが変わるので
		char wNarrow =  '?';
		switch(pChar){
			case'壱' : wNarrow = '1' ;break;  //オマケ
			case'弐' : wNarrow = '2' ;break;  //オマケ
			case'参' : wNarrow = '3' ;break;  //オマケ
			case'一' : wNarrow = '1' ;break;  //オマケ
			case'二' : wNarrow = '2' ;break;  //オマケ
			case'三' : wNarrow = '3' ;break;  //オマケ
			case'四' : wNarrow = '4' ;break;  //オマケ
			case'五' : wNarrow = '5' ;break;  //オマケ
			case'六' : wNarrow = '6' ;break;  //オマケ
			case'七' : wNarrow = '7' ;break;  //オマケ
			case'八' : wNarrow = '8' ;break;  //オマケ
			case'九' : wNarrow = '9' ;break;  //オマケ
			case'十' : wNarrow = '0' ;break;  //オマケ
			case'　' : wNarrow = ' ' ;break;
			case'！' : wNarrow = '!' ;break;
			case'”' : wNarrow = '"' ;break;
			case'＃' : wNarrow = '#' ;break;
			case'＄' : wNarrow = '$' ;break;
			case'％' : wNarrow = '%' ;break;
			case'＆' : wNarrow = '&' ;break;
			case'’' : wNarrow = '\'';break;
			case'（' : wNarrow = '(' ;break;
			case'）' : wNarrow = ')' ;break;
			case'＊' : wNarrow = '*' ;break;
			case'＋' : wNarrow = '+' ;break;
			case'，' : wNarrow = ',' ;break;
			case'ー' : wNarrow = '-' ;break;
			case'．' : wNarrow = '.' ;break;
			case'／' : wNarrow = '/' ;break;
			case'０' : wNarrow = '0' ;break;
			case'１' : wNarrow = '1' ;break;
			case'２' : wNarrow = '2' ;break;
			case'３' : wNarrow = '3' ;break;
			case'４' : wNarrow = '4' ;break;
			case'５' : wNarrow = '5' ;break;
			case'６' : wNarrow = '6' ;break;
			case'７' : wNarrow = '7' ;break;
			case'８' : wNarrow = '8' ;break;
			case'９' : wNarrow = '9' ;break;
			case'：' : wNarrow = ':' ;break;
			case'；' : wNarrow = ';' ;break;
			case'＜' : wNarrow = '<' ;break;
			case'＝' : wNarrow = '=' ;break;
			case'＞' : wNarrow = '>' ;break;
			case'？' : wNarrow = '?' ;break;
			case'＠' : wNarrow = '@' ;break;
			case'Ａ' : wNarrow = 'A' ;break;
			case'Ｂ' : wNarrow = 'B' ;break;
			case'Ｃ' : wNarrow = 'C' ;break;
			case'Ｄ' : wNarrow = 'D' ;break;
			case'Ｅ' : wNarrow = 'E' ;break;
			case'Ｆ' : wNarrow = 'F' ;break;
			case'Ｇ' : wNarrow = 'G' ;break;
			case'Ｈ' : wNarrow = 'H' ;break;
			case'Ｉ' : wNarrow = 'I' ;break;
			case'Ｊ' : wNarrow = 'J' ;break;
			case'Ｋ' : wNarrow = 'K' ;break;
			case'Ｌ' : wNarrow = 'L' ;break;
			case'Ｍ' : wNarrow = 'M' ;break;
			case'Ｎ' : wNarrow = 'N' ;break;
			case'Ｏ' : wNarrow = 'O' ;break;
			case'Ｐ' : wNarrow = 'P' ;break;
			case'Ｑ' : wNarrow = 'Q' ;break;
			case'Ｒ' : wNarrow = 'R' ;break;
			case'Ｓ' : wNarrow = 'S' ;break;
			case'Ｔ' : wNarrow = 'T' ;break;
			case'Ｕ' : wNarrow = 'U' ;break;
			case'Ｖ' : wNarrow = 'V' ;break;
			case'Ｗ' : wNarrow = 'W' ;break;
			case'Ｘ' : wNarrow = 'X' ;break;
			case'Ｙ' : wNarrow = 'Y' ;break;
			case'Ｚ' : wNarrow = 'Z' ;break;
			case'［' : wNarrow = '[' ;break;
			case'￥' : wNarrow = '\\';break;
			case'］' : wNarrow = ']' ;break;
			case'＾' : wNarrow = '^' ;break;
			case'＿' : wNarrow = '_' ;break;
			case'｀' : wNarrow = '`' ;break;
			case'ａ' : wNarrow = 'a' ;break;
			case'ｂ' : wNarrow = 'b' ;break;
			case'ｃ' : wNarrow = 'c' ;break;
			case'ｄ' : wNarrow = 'd' ;break;
			case'ｅ' : wNarrow = 'e' ;break;
			case'ｆ' : wNarrow = 'f' ;break;
			case'ｇ' : wNarrow = 'g' ;break;
			case'ｈ' : wNarrow = 'h' ;break;
			case'ｉ' : wNarrow = 'i' ;break;
			case'ｊ' : wNarrow = 'j' ;break;
			case'ｋ' : wNarrow = 'k' ;break;
			case'ｌ' : wNarrow = 'l' ;break;
			case'ｍ' : wNarrow = 'm' ;break;
			case'ｎ' : wNarrow = 'n' ;break;
			case'ｏ' : wNarrow = 'o' ;break;
			case'ｐ' : wNarrow = 'p' ;break;
			case'ｑ' : wNarrow = 'q' ;break;
			case'ｒ' : wNarrow = 'r' ;break;
			case'ｓ' : wNarrow = 's' ;break;
			case'ｔ' : wNarrow = 't' ;break;
			case'ｕ' : wNarrow = 'u' ;break;
			case'ｖ' : wNarrow = 'v' ;break;
			case'ｗ' : wNarrow = 'w' ;break;
			case'ｘ' : wNarrow = 'x' ;break;
			case'ｙ' : wNarrow = 'y' ;break;
			case'ｚ' : wNarrow = 'z' ;break;
			case'｛' : wNarrow = '{' ;break;
			case'｜' : wNarrow = '|' ;break;
			case'｝' : wNarrow = '}' ;break;
			case'￣' : wNarrow = '~' ;break;
			case'。' : wNarrow = '｡' ;break;
			case'「' : wNarrow = '｢' ;break;
			case'」' : wNarrow = '｣' ;break;
			case'、' : wNarrow = '､' ;break;
			case'・' : wNarrow = '･' ;break;
			case'ヲ' : wNarrow = 'ｦ' ;break;
			case'ァ' : wNarrow = 'ｧ' ;break;
			case'ィ' : wNarrow = 'ｨ' ;break;
			case'ゥ' : wNarrow = 'ｩ' ;break;
			case'ェ' : wNarrow = 'ｪ' ;break;
			case'ォ' : wNarrow = 'ｫ' ;break;
			case'ャ' : wNarrow = 'ｬ' ;break;
			case'ュ' : wNarrow = 'ｭ' ;break;
			case'ョ' : wNarrow = 'ｮ' ;break;
			case'ッ' : wNarrow = 'ｯ' ;break;
			case'－' : wNarrow = '-' ;break;
			case'ア' : wNarrow = 'ｱ' ;break;
			case'イ' : wNarrow = 'ｲ' ;break;
			case'ウ' : wNarrow = 'ｳ' ;break;
			case'エ' : wNarrow = 'ｴ' ;break;
			case'オ' : wNarrow = 'ｵ' ;break;
			case'カ' : wNarrow = 'ｶ' ;break;
			case'キ' : wNarrow = 'ｷ' ;break;
			case'ク' : wNarrow = 'ｸ' ;break;
			case'ケ' : wNarrow = 'ｹ' ;break;
			case'コ' : wNarrow = 'ｺ' ;break;
			case'サ' : wNarrow = 'ｻ' ;break;
			case'シ' : wNarrow = 'ｼ' ;break;
			case'ス' : wNarrow = 'ｽ' ;break;
			case'セ' : wNarrow = 'ｾ' ;break;
			case'ソ' : wNarrow = 'ｿ' ;break;
			case'タ' : wNarrow = 'ﾀ' ;break;
			case'チ' : wNarrow = 'ﾁ' ;break;
			case'ツ' : wNarrow = 'ﾂ' ;break;
			case'テ' : wNarrow = 'ﾃ' ;break;
			case'ト' : wNarrow = 'ﾄ' ;break;
			case'ナ' : wNarrow = 'ﾅ' ;break;
			case'ニ' : wNarrow = 'ﾆ' ;break;
			case'ヌ' : wNarrow = 'ﾇ' ;break;
			case'ネ' : wNarrow = 'ﾈ' ;break;
			case'ノ' : wNarrow = 'ﾉ' ;break;
			case'ハ' : wNarrow = 'ﾊ' ;break;
			case'ヒ' : wNarrow = 'ﾋ' ;break;
			case'フ' : wNarrow = 'ﾌ' ;break;
			case'ヘ' : wNarrow = 'ﾍ' ;break;
			case'ホ' : wNarrow = 'ﾎ' ;break;
			case'マ' : wNarrow = 'ﾏ' ;break;
			case'ミ' : wNarrow = 'ﾐ' ;break;
			case'ム' : wNarrow = 'ﾑ' ;break;
			case'メ' : wNarrow = 'ﾒ' ;break;
			case'モ' : wNarrow = 'ﾓ' ;break;
			case'ヤ' : wNarrow = 'ﾔ' ;break;
			case'ユ' : wNarrow = 'ﾕ' ;break;
			case'ヨ' : wNarrow = 'ﾖ' ;break;
			case'ラ' : wNarrow = 'ﾗ' ;break;
			case'リ' : wNarrow = 'ﾘ' ;break;
			case'ル' : wNarrow = 'ﾙ' ;break;
			case'レ' : wNarrow = 'ﾚ' ;break;
			case'ロ' : wNarrow = 'ﾛ' ;break;
			case'ワ' : wNarrow = 'ﾜ' ;break;
			case'ン' : wNarrow = 'ﾝ' ;break;
			case'゛' : wNarrow = 'ﾞ' ;break;
			case'゜' : wNarrow = 'ﾟ' ;break;
//		  default  : wNarrow = '';
		}
		return wNarrow;
	}



/*
	以下コメントアウトしてあります
	//---+---------+---------+---------+---------+---------+---------+---------+
	// switch文の代わりにハッシュマップを使う方法も考えようとしたが
	// （大量に変換する必要がある場合はその方がいいと思った）
	// charはオブジェクトじゃないのでHashTableで使用するためには
	// 一旦ラッパークラスで作らなければならない・・・重そうなので
	// ので、String配列としようとしたが、これも型変換が頻発しそうなので
	// このままペンデイングしてある。
	// ただ”が”→"ｶﾞ"の様な変換はStringで処理しなければならないだろう・・
	// ※マッピングする文字をインコアする元はテキストファイルだといいかも
	//---+---------+---------+---------+---------+---------+---------+---------+
	Object[][] map001 = {
		{"\'","’"},{"\\","￥"},{"\"","”"},
	    {" ","　"},{"!","！"},{"#","＃"},{"$","＄"},{"%","％"},
		{"&","＆"},{"(","（"},{")","）"},{"*","＊"},{"+","＋"},
		{",","，"},{".","．"},{"/","／"},{"?","？"},{"@","＠"},
		{"0","０"},{"1","１"},{"2","２"},{"3","３"},{"4","４"},
		{"5","５"},{"6","６"},{"7","７"},{"8","８"},{"9","９"},
		{":","："},{";","；"},{"<","＜"},{"=","＝"},{">","＞"},
		{"A","Ａ"},{"B","Ｂ"},{"C","Ｃ"},{"D","Ｄ"},{"E","Ｅ"},
		{"F","Ｆ"},{"G","Ｇ"},{"H","Ｈ"},{"I","Ｉ"},{"J","Ｊ"},
		{"K","Ｋ"},{"L","Ｌ"},{"M","Ｍ"},{"N","Ｎ"},{"O","Ｏ"},
		{"P","Ｐ"},{"Q","Ｑ"},{"R","Ｒ"},{"S","Ｓ"},{"T","Ｔ"},
		{"U","Ｕ"},{"V","Ｖ"},{"W","Ｗ"},{"X","Ｘ"},{"Y","Ｙ"},
		{"Z","Ｚ"},{"[","［"},{"]","］"},{"^","＾"},{"_","＿"},
		{"`","｀"},{"a","ａ"},{"b","ｂ"},{"c","ｃ"},{"d","ｄ"},
		{"e","ｅ"},{"f","ｆ"},{"g","ｇ"},{"h","ｈ"},{"i","ｉ"},
		{"j","ｊ"},{"k","ｋ"},{"l","ｌ"},{"m","ｍ"},{"n","ｎ"},
		{"o","ｏ"},{"p","ｐ"},{"q","ｑ"},{"r","ｒ"},{"s","ｓ"},
		{"t","ｔ"},{"u","ｕ"},{"v","ｖ"},{"w","ｗ"},{"x","ｘ"},
		{"y","ｙ"},{"z","ｚ"},{"{","｛"},{"|","｜"},{"}","｝"},
		{"~","￣"},{"｡","。"},{"｢","「"},{"｣","」"},{"､","、"},
		{"･","・"},{"ｦ","ヲ"},{"ｧ","ァ"},{"ｨ","ィ"},{"ｩ","ゥ"},
		{"ｪ","ェ"},{"ｫ","ォ"},{"ｬ","ャ"},{"ｭ","ュ"},{"ｮ","ョ"},
		{"ｯ","ッ"},{"-","－"},{"ｱ","ア"},{"ｲ","イ"},{"ｳ","ウ"},
		{"ｴ","エ"},{"ｵ","オ"},{"ｶ","カ"},{"ｷ","キ"},{"ｸ","ク"},
		{"ｹ","ケ"},{"ｺ","コ"},{"ｻ","サ"},{"ｼ","シ"},{"ｽ","ス"},
		{"ｾ","セ"},{"ｿ","ソ"},{"ﾀ","タ"},{"ﾁ","チ"},{"ﾂ","ツ"},
		{"ﾃ","テ"},{"ﾄ","ト"},{"ﾅ","ナ"},{"ﾆ","ニ"},{"ﾇ","ヌ"},
		{"ﾈ","ネ"},{"ﾉ","ノ"},{"ﾊ","ハ"},{"ﾋ","ヒ"},{"ﾌ","フ"},
		{"ﾍ","ヘ"},{"ﾎ","ホ"},{"ﾏ","マ"},{"ﾐ","ミ"},{"ﾑ","ム"},
		{"ﾒ","メ"},{"ﾓ","モ"},{"ﾔ","ヤ"},{"ﾕ","ユ"},{"ﾖ","ヨ"},
		{"ﾗ","ラ"},{"ﾘ","リ"},{"ﾙ","ル"},{"ﾚ","レ"},{"ﾛ","ロ"},
		{"ﾜ","ワ"},{"ﾝ","ン"},{"ﾞ","゛"},{"ﾟ","゜"}  
	};
	Object[][] map002 = {
		{"’","\'"},{"￥","\\"},{"”","\""},
		{"　"," "},{"！","!"},{"＃","#"},{"＄","$"},{"％","%"},
		{"＆","&"},{"（","("},{"）",")"},{"＊","*"},{"＋","+"},
		{"，",","},{"ー","-"},{"．","."},{"／","/"},{"０","0"},
		{"１","1"},{"２","2"},{"３","3"},{"４","4"},{"５","5"},
		{"６","6"},{"７","7"},{"８","8"},{"９","9"},{"：",":"},
		{"；",";"},{"＜","<"},{"＝","="},{"＞",">"},{"？","?"},
		{"＠","@"},{"Ａ","A"},{"Ｂ","B"},{"Ｃ","C"},{"Ｄ","D"},
		{"Ｅ","E"},{"Ｆ","F"},{"Ｇ","G"},{"Ｈ","H"},{"Ｉ","I"},
		{"Ｊ","J"},{"Ｋ","K"},{"Ｌ","L"},{"Ｍ","M"},{"Ｎ","N"},
		{"Ｏ","O"},{"Ｐ","P"},{"Ｑ","Q"},{"Ｒ","R"},{"Ｓ","S"},
		{"Ｔ","T"},{"Ｕ","U"},{"Ｖ","V"},{"Ｗ","W"},{"Ｘ","X"},
		{"Ｙ","Y"},{"Ｚ","Z"},{"［","["},{"］","]"},{"＾","^"},
		{"＿","_"},{"｀","`"},{"ａ","a"},{"ｂ","b"},{"ｃ","c"},
		{"ｄ","d"},{"ｅ","e"},{"ｆ","f"},{"ｇ","g"},{"ｈ","h"},
		{"ｉ","i"},{"ｊ","j"},{"ｋ","k"},{"ｌ","l"},{"ｍ","m"},
		{"ｎ","n"},{"ｏ","o"},{"ｐ","p"},{"ｑ","q"},{"ｒ","r"},
		{"ｓ","s"},{"ｔ","t"},{"ｕ","u"},{"ｖ","v"},{"ｗ","w"},
		{"ｘ","x"},{"ｙ","y"},{"ｚ","z"},{"｛","{"},{"｜","|"},
		{"｝","}"},{"￣","~"},{"。","｡"},{"「","｢"},{"」","｣"},
		{"、","､"},{"・","･"},{"ヲ","ｦ"},{"ァ","ｧ"},{"ィ","ｨ"},
		{"ゥ","ｩ"},{"ェ","ｪ"},{"ォ","ｫ"},{"ャ","ｬ"},{"ュ","ｭ"},
		{"ョ","ｮ"},{"ッ","ｯ"},{"－","-"},{"ア","ｱ"},{"イ","ｲ"},
		{"ウ","ｳ"},{"エ","ｴ"},{"オ","ｵ"},{"カ","ｶ"},{"キ","ｷ"},
		{"ク","ｸ"},{"ケ","ｹ"},{"コ","ｺ"},{"サ","ｻ"},{"シ","ｼ"},
		{"ス","ｽ"},{"セ","ｾ"},{"ソ","ｿ"},{"タ","ﾀ"},{"チ","ﾁ"},
		{"ツ","ﾂ"},{"テ","ﾃ"},{"ト","ﾄ"},{"ナ","ﾅ"},{"ニ","ﾆ"},
		{"ヌ","ﾇ"},{"ネ","ﾈ"},{"ノ","ﾉ"},{"ハ","ﾊ"},{"ヒ","ﾋ"},
		{"フ","ﾌ"},{"ヘ","ﾍ"},{"ホ","ﾎ"},{"マ","ﾏ"},{"ミ","ﾐ"},
		{"ム","ﾑ"},{"メ","ﾒ"},{"モ","ﾓ"},{"ヤ","ﾔ"},{"ユ","ﾕ"},
		{"ヨ","ﾖ"},{"ラ","ﾗ"},{"リ","ﾘ"},{"ル","ﾙ"},{"レ","ﾚ"},
		{"ロ","ﾛ"},{"ワ","ﾜ"},{"ン","ﾝ"},{"゛","ﾞ"},{"゜","ﾟ"},
		{"一","1"},{"二","2"},{"三","3"},{"四","4"},
		{"五","5"},{"六","6"},{"七","7"},{"八","8"},{"九","9"},
		{"十","0"} //オマケ
	};
	HashMap wHash001 = new java.util.HashMap();
	//----------------------------------------------------------------
	public CodeCnv(){
		incore001();
	}
	//----------------------------------------------------------------
	// incore map001をハッシュテーブル化
	//----------------------------------------------------------------
	public void incore001(){
		for(int i = 0;i<map001.length;i++){
			if(map001[i].length==2){
				wHash001.put(map001[i][0],map001[i][1]);
			}
		}
	}
	//----------------------------------------------------------------
	// 変換コードをハッシュテーブルから取り出す
	//----------------------------------------------------------------
	public String getNam001(String pKey){
		System.out.println("■getNam001■in【"+pKey+"】");
		String wRtn = pKey;
		if ( ! pKey.trim().equals("")){
			Object wObj = wHash001.get(pKey);
			if(wObj!=null){
				if (wObj instanceof String){
					wRtn = wObj.toString();
				}else{
					wRtn = "null?";
				}
			}
		}else{
			wRtn = "?";
		}
		System.out.println("■getNam001■Out【"+wRtn+"】");
		return wRtn;
	}
*/

}
