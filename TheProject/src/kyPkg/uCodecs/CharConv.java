package kyPkg.uCodecs;

import java.util.*;

import kyPkg.uFile.FileUtil;

import java.io.*;

public class CharConv {
	private static final String TEST_STR1 = "ﾞγひらがなカタカナ漢字壺壷藪薮･ｰｧｨｩｪｫｬｭｮｯｱﾞｲﾟｳﾞ$#123ﾊﾟﾋﾟﾌﾟｶﾞｷﾞｸﾞ<>*#$%abc:;\\ABC★△■フジ　ユニー　E-プラスふんわり食感　６枚";
	private static final String TEST_STR2 = "ﾞ･ｰｧｨｩｪｫｬｭｮｯｱﾞｲﾟｳﾞ$#123ﾊﾟﾋﾟﾌﾟｶﾞｷﾞｸﾞ<>*#$%abc:;\\ABC★△■フジ　ユニー　E-プラスふんわり食感　６枚";

	private static final char SPACE = '　';
	private static CharConv CnvIns;
	private HashMap n2w;
	private HashMap w2n;
	private HashMap ngf;
	private HashMap ngx;
	private HashMap k2n;
	private HashMap ckj;
	private HashMap opt;
	private static CharConv insCnv = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	private CharConv() {
	}

	// -------------------------------------------------------------------------
	// singleton
	// -------------------------------------------------------------------------
	public static CharConv getInstance() {
		if (CnvIns == null)
			CnvIns = new CharConv();
		return CnvIns;
	}

	// -------------------------------------------------------------------------
	// 固定長にする(バイト長ではないので注意！)
	// pStr 対象文字列
	// pLen 希望する長さ
	// pFiller 空白文字を指定する
	// -------------------------------------------------------------------------
	public static String cnvFixLength(int iVal, int pLen) {
		return cnvFixLength(String.valueOf(iVal), pLen, ' ');
	}

	public static String cnvFixLength(String pStr, int pLen) {
		return cnvFixLength(pStr, pLen, ' ');
	}

	public static String cnvFixLength(String pStr, int pLen, char pFiller) {
		StringBuffer wBuff = new StringBuffer(pLen);
		if (pStr.length() > pLen) {
			wBuff.append(pStr.substring(0, pLen));
		} else {
			wBuff.append(pStr);
			for (int i = pStr.length(); i < pLen; i++) {
				wBuff.append(pFiller);
			}
		}
		String wRtn = wBuff.toString();
		return wRtn;
	}

	// -----------------------------------------------------------------
	// 全角化する
	// -----------------------------------------------------------------
	public static String[] cnvWide(String[] array, int len) {
		if (insCnv == null)
			insCnv = CharConv.getInstance();
		for (int i = 0; i < array.length; i++) {
			array[i] = insCnv.cnvWideStr(array[i], len);
		}
		return array;
	}

	// CharConv.cnvWide(String str, int len)
	// public static String cnvWide(String str) {
	// return "";
	// }

	public static String cnvWide(String str, int len) {
		if (insCnv == null)
			insCnv = CharConv.getInstance();
		str = insCnv.cnvWideStr(str, len);
		return str;
	}

	// -------------------------------------------------------------------------
	// 半角→全角
	// syllableの問題
	// 濁音、撥音をどうするか・・・ｶﾞ → カ゛→ ガ と２段階変換
	// 濁音（ガ･ザ･ダ･バの4行）、半濁音は未対応・・語の長さが変わるので注意
	// -------------------------------------------------------------------------
	public String cnvWide(String pStr, int pLen, char pFiller) {
		return cnvFixLength(cnvWide(pStr), pLen, pFiller);
	}

	private String cnvWideStr(String pStr, int pLen) {
		return cnvFixLength(cnvWide(pStr), pLen, SPACE);
	}

	// 　全角化処理　カタカナ変換処理をする際の語長さ調整あり　＆　キャラクタ配列使用版
	public String cnvWide(String pStr) {
		if (pStr.equals(""))
			return "";
		String filler = "　";
		if (n2w == null)
			incoreN2W();
		char[] cDes = new char[pStr.length()];
		int j = 0;
		char wCnv;
		for (int i = 0; i < pStr.length(); i++) {
			String wDic = (String) n2w.get(String.valueOf(pStr.charAt(i)));
			if (wDic != null) {
				wCnv = wDic.charAt(0);
			} else {
				wCnv = pStr.charAt(i);
			}
			char wChr = 0;
			if ((j > 0) && (cDes[j - 1] > 0)) {
				switch (wCnv) {
				case '゛':
					switch (cDes[j - 1]) {
					case 'カ':
						wChr = 'ガ';
						break;
					case 'キ':
						wChr = 'ギ';
						break;
					case 'ク':
						wChr = 'グ';
						break;
					case 'ケ':
						wChr = 'ゲ';
						break;
					case 'コ':
						wChr = 'ゴ';
						break;
					case 'サ':
						wChr = 'ザ';
						break;
					case 'シ':
						wChr = 'ジ';
						break;
					case 'ス':
						wChr = 'ズ';
						break;
					case 'セ':
						wChr = 'ゼ';
						break;
					case 'ソ':
						wChr = 'ゾ';
						break;
					case 'タ':
						wChr = 'ダ';
						break;
					case 'チ':
						wChr = 'ジ';
						break;
					case 'ツ':
						wChr = 'ヅ';
						break;
					case 'テ':
						wChr = 'デ';
						break;
					case 'ト':
						wChr = 'ド';
						break;
					case 'ハ':
						wChr = 'バ';
						break;
					case 'ヒ':
						wChr = 'ビ';
						break;
					case 'フ':
						wChr = 'ブ';
						break;
					case 'ヘ':
						wChr = 'ベ';
						break;
					case 'ホ':
						wChr = 'ボ';
						break;
					case 'ウ':
						wChr = 'ヴ';
						break;
					}
					if (wChr > 0) {
						cDes[j - 1] = 0;
						wCnv = wChr;
					}
					break;
				case '゜':
					switch (cDes[j - 1]) {
					case 'ハ':
						wChr = 'パ';
						break;
					case 'ヒ':
						wChr = 'ピ';
						break;
					case 'フ':
						wChr = 'プ';
						break;
					case 'ヘ':
						wChr = 'ペ';
						break;
					case 'ホ':
						wChr = 'ポ';
						break;
					}
					if (wChr > 0) {
						cDes[j - 1] = 0;
						wCnv = wChr;
					}
					break;
				default:
					break;
				}
			}
			cDes[j++] = wCnv;
		}
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < cDes.length; i++) {
			if (cDes[i] > 0)
				wBuff.append(cDes[i]);
		}
		// filler処理
		for (int i = wBuff.length(); i < cDes.length; i++) {
			wBuff.append(filler);
		}
		return wBuff.toString();
	}

	public String cnvWide_classic(String pStr) {
		if (n2w == null)
			incoreN2W();
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) n2w.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			// System.out.println("in:"+pStr.charAt(i)+" => out:"+wCnv);
			wBuff.append(wCnv);
		}
		String wRtn = wBuff.toString();
		if (wRtn.indexOf('゛') >= 0) {
			wRtn = wRtn.replaceAll("カ゛", "ガ");
			wRtn = wRtn.replaceAll("キ゛", "ギ");
			wRtn = wRtn.replaceAll("ク゛", "グ");
			wRtn = wRtn.replaceAll("ケ゛", "ゲ");
			wRtn = wRtn.replaceAll("コ゛", "ゴ");
			wRtn = wRtn.replaceAll("サ゛", "ザ");
			wRtn = wRtn.replaceAll("シ゛", "ジ");
			wRtn = wRtn.replaceAll("ス゛", "ズ");
			wRtn = wRtn.replaceAll("セ゛", "ゼ");
			wRtn = wRtn.replaceAll("ソ゛", "ゾ");
			wRtn = wRtn.replaceAll("タ゛", "ダ");
			wRtn = wRtn.replaceAll("チ゛", "ジ");
			wRtn = wRtn.replaceAll("ツ゛", "ヅ");
			wRtn = wRtn.replaceAll("テ゛", "デ");
			wRtn = wRtn.replaceAll("ト゛", "ド");
			wRtn = wRtn.replaceAll("ハ゛", "バ");
			wRtn = wRtn.replaceAll("ヒ゛", "ビ");
			wRtn = wRtn.replaceAll("フ゛", "ブ");
			wRtn = wRtn.replaceAll("ヘ゛", "ベ");
			wRtn = wRtn.replaceAll("ホ゛", "ボ");
			wRtn = wRtn.replaceAll("ウ゛", "ヴ");
		}
		if (wRtn.indexOf('゜') >= 0) {
			wRtn = wRtn.replaceAll("ハ゜", "パ");
			wRtn = wRtn.replaceAll("ヒ゜", "ピ");
			wRtn = wRtn.replaceAll("フ゜", "プ");
			wRtn = wRtn.replaceAll("ヘ゜", "ペ");
			wRtn = wRtn.replaceAll("ホ゜", "ポ");
		}
		return wRtn;
	}

	// ------------------------------------------------------------------------------
	// 選択された値を固定の長さで返す(fixStr)
	// pStr 入力文字列
	// pLen 出力長（※バイト長さではなく、文字数）
	// 《使用例》
	// wAns = EnqChk.fixStr("あbcd",10);
	// System.out.println("■fixStr =>"+wAns);
	// ------------------------------------------------------------------------------
	public static String[] fixStrArray(String[] array, int len) {
		for (int i = 0; i < array.length; i++) {
			array[i] = fixStr(array[i].trim(), len);
		}
		return array;
	}

	public static String fixStr(String src, int len) {
		String ans = "";
		if (len > 0) {
			if (src == null)
				src = "";
			// DecimalFormat df = new DecimalFormat("00000");を使うように後で変更したい
			char[] cSrc = src.toCharArray(); // キャラクター配列にする
			char[] cDes = new char[len]; // 出力バッファ
			for (int i = 0; i < cDes.length; i++)
				cDes[i] = ' ';
			for (int i = 0; (i < cDes.length && i < cSrc.length); i++) {
				cDes[i] = cSrc[i];
			}
			ans = new String(cDes);
		}
		return ans;
	}

	// ------------------------------------------------------------------------------
	// 文字列を右寄せ固定長処理
	// ------------------------------------------------------------------------------
	public static String fixRight(String src, int len, char stuff) {
		String ans = "";
		if (len > 0) {
			char[] cSrc = src.toCharArray(); // キャラクター配列にする
			char[] cDes = new char[len]; // 出力バッファ
			for (int i = 0; i < cDes.length; i++)
				cDes[i] = stuff; // 埋め文字
			for (int i = cDes.length - 1, j = cSrc.length - 1; (i >= 0
					&& j >= 0); i--, j--) {
				cDes[i] = cSrc[j];
			}
			ans = new String(cDes);
		}
		return ans;
	}

	// ------------------------------------------------------------------------------
	// 文字列を左寄せ固定長処理
	// ------------------------------------------------------------------------------
	public static String fixLeft(String src, int len, char stuff) {
		String ans = "";
		if (len > 0) {
			char[] cSrc = src.toCharArray(); // キャラクター配列にする
			char[] cDes = new char[len]; // 出力バッファ
			for (int i = 0; i < cDes.length; i++)
				cDes[i] = stuff; // 埋め文字
			for (int i = 0; (i < cDes.length && i < cSrc.length); i++) {
				cDes[i] = cSrc[i];
			}
			ans = new String(cDes);
		}
		return ans;
	}

	// -------------------------------------------------------------------------
	// 固定長全角文字を返す
	// -------------------------------------------------------------------------
	public static String cnvFixWide(String str, int len) {
		CharConv charConv = CharConv.getInstance();
		str = fixStr(str, len);
		return charConv.cnvWide(str);
	}

	// -------------------------------------------------------------------------
	// 固定長全角Plus(ホスト漢字処理)
	// -------------------------------------------------------------------------
	public String cnvFixWide2(String cel, int len) {
		cel = fixStr(cel, len);
		cel = cnvWide(cel);
		return cnvCKJ(cel);// debug　ホスト漢字処理・・・とほほ
	}

	// -------------------------------------------------------------------------
	// 固定長半角
	// -------------------------------------------------------------------------
	public String cnvFixHalf(String cel, int len) {
		cel = cnvNarrow(cel);
		return fixStr(cel, len);
	}

	// -------------------------------------------------------------------------
	// 全角→半角変換
	// pAlter は該当する文字が存在しない場合の代替文字
	// -------------------------------------------------------------------------
	public String cnvNarrow(String pStr) {
		return cnvNarrow(pStr, '_');
	}

	// 間違っている・・・ショック　charの範囲で判定したほうがいいだろう
	public String cnvNarrow(String pStr, char pAlter) {
		if (w2n == null)
			incoreW2N();
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			// String wStr = String.valueOf(pStr.substring(i,i+1));
			// String wStr = String.valueOf(pStr.charAt(i));
			char wChr = pStr.charAt(i);
			String wStr = String.valueOf(wChr);
			// if (wStr.matches("[A-Za-z0-9\\s]")){
			// ･
			// 記号がもれていたので範囲判定に変更した・・・とほほ2009/10/21
			if ((wChr >= 32) && (wChr <= 126)) {
				wBuff.append(wStr);
			} else {
				String wCnv = (String) w2n.get(wStr);
				if (wCnv == null)
					wCnv = String.valueOf(pAlter);
				// System.out.println("in:"+pStr.charAt(i)+" => out:"+wCnv);
				wBuff.append(wCnv);
			}

		}
		String wRtn = wBuff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// ｶﾅ文字をノーマライズ for Host(kana => normal)
	// -------------------------------------------------------------------------
	public String cnvK2N(String pStr) {
		if (k2n == null)
			incoreK2N();
		StringBuffer buff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) k2n.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			buff.append(wCnv);
		}
		String wRtn = buff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// 漢字をＨｏｓｔ用にノーマライズ for Host(漢字 => normal)
	// -------------------------------------------------------------------------
	public String cnvCKJ(String pStr) {
		if (pStr.equals(""))
			return "";
		if (ckj == null)
			incoreCKJ();
		StringBuffer buff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) ckj.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			buff.append(wCnv);
		}
		String wRtn = buff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// ファイル名に使えない文字を変換
	// -------------------------------------------------------------------------
	public String cnvNGFnm(String pStr) {
		if (ngf == null)
			incoreNGF();
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) ngf.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			wBuff.append(wCnv);
		}
		String wRtn = wBuff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// エクセルのシート名などで使えない文字を変換
	// plus３１文字以内 空白も駄目らしい
	// -------------------------------------------------------------------------
	public String cnvNGExcel(String pStr) {
		if (ngx == null)
			incoreNGX();
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) ngx.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			wBuff.append(wCnv);
		}
		String wRtn = wBuff.toString();
		wRtn = wRtn.trim();
		if (wRtn.length() > 31)
			wRtn = wRtn.substring(0, 31);
		if (wRtn == "")
			wRtn = "NG";
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// 変換マップをファイルから取り込む場合
	// ファイル形式：一つの文字＋タブ＋変換する文字列
	// -------------------------------------------------------------------------
	public void incoreOPT(String pPath) {
		opt = CharConv.file2HashMap(pPath);
	}

	// -------------------------------------------------------------------------
	// 外部マップを使った変換
	// wCnv.incoreOPT("Caesar"); // Caesar暗号・・半角のみ
	// String Ans = wCnv.optConvert(wStr);
	// System.out.println("#Option     =>"+Ans );
	// -------------------------------------------------------------------------
	public String optConvert(String pStr) {
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) opt.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			wBuff.append(wCnv);
		}
		String wRtn = wBuff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// 変換マップＩｎｃｏｒｅ
	// -------------------------------------------------------------------------
	public void incoreN2W() {
		n2w = new HashMap(); // 半角→全角
		// ---------------------------------------------------------------------
		n2w.put(" ", "　");
		n2w.put("!", "！");
		n2w.put("\"", "”");
		n2w.put("#", "＃");
		n2w.put("$", "＄");
		n2w.put("%", "％");
		n2w.put("&", "＆");
		n2w.put("\'", "’");
		n2w.put("(", "（");
		n2w.put(")", "）");
		n2w.put("*", "＊");
		n2w.put("+", "＋");
		n2w.put(",", "，");
		// wN2W.put("-","－");　//うまく変換できないようだ・・・2009/07/31　16進表記を後で拾っておこう
		n2w.put(".", "．");
		n2w.put("/", "／");
		n2w.put("0", "０");
		n2w.put("1", "１");
		n2w.put("2", "２");
		n2w.put("3", "３");
		n2w.put("4", "４");
		n2w.put("5", "５");
		n2w.put("6", "６");
		n2w.put("7", "７");
		n2w.put("8", "８");
		n2w.put("9", "９");
		n2w.put(":", "：");
		n2w.put(";", "；");
		n2w.put("<", "＜");
		n2w.put("=", "＝");
		n2w.put(">", "＞");
		n2w.put("?", "？");
		n2w.put("@", "＠");
		n2w.put("A", "Ａ");
		n2w.put("B", "Ｂ");
		n2w.put("C", "Ｃ");
		n2w.put("D", "Ｄ");
		n2w.put("E", "Ｅ");
		n2w.put("F", "Ｆ");
		n2w.put("G", "Ｇ");
		n2w.put("H", "Ｈ");
		n2w.put("I", "Ｉ");
		n2w.put("J", "Ｊ");
		n2w.put("K", "Ｋ");
		n2w.put("L", "Ｌ");
		n2w.put("M", "Ｍ");
		n2w.put("N", "Ｎ");
		n2w.put("O", "Ｏ");
		n2w.put("P", "Ｐ");
		n2w.put("Q", "Ｑ");
		n2w.put("R", "Ｒ");
		n2w.put("S", "Ｓ");
		n2w.put("T", "Ｔ");
		n2w.put("U", "Ｕ");
		n2w.put("V", "Ｖ");
		n2w.put("W", "Ｗ");
		n2w.put("X", "Ｘ");
		n2w.put("Y", "Ｙ");
		n2w.put("Z", "Ｚ");
		n2w.put("[", "［");
		n2w.put("\\", "￥");
		n2w.put("]", "］");
		n2w.put("^", "＾");
		n2w.put("_", "＿");
		n2w.put("`", "｀");
		n2w.put("a", "ａ");
		n2w.put("b", "ｂ");
		n2w.put("c", "ｃ");
		n2w.put("d", "ｄ");
		n2w.put("e", "ｅ");
		n2w.put("f", "ｆ");
		n2w.put("g", "ｇ");
		n2w.put("h", "ｈ");
		n2w.put("i", "ｉ");
		n2w.put("j", "ｊ");
		n2w.put("k", "ｋ");
		n2w.put("l", "ｌ");
		n2w.put("m", "ｍ");
		n2w.put("n", "ｎ");
		n2w.put("o", "ｏ");
		n2w.put("p", "ｐ");
		n2w.put("q", "ｑ");
		n2w.put("r", "ｒ");
		n2w.put("s", "ｓ");
		n2w.put("t", "ｔ");
		n2w.put("u", "ｕ");
		n2w.put("v", "ｖ");
		n2w.put("w", "ｗ");
		n2w.put("x", "ｘ");
		n2w.put("y", "ｙ");
		n2w.put("z", "ｚ");
		n2w.put("{", "｛");
		n2w.put("|", "｜");
		n2w.put("}", "｝");
		n2w.put("~", "￣");
		n2w.put("｡", "。");
		n2w.put("｢", "「");
		n2w.put("｣", "」");
		n2w.put("､", "、");
		n2w.put("･", "・");
		n2w.put("ｦ", "ヲ");
		n2w.put("ｧ", "ァ");
		n2w.put("ｨ", "ィ");
		n2w.put("ｩ", "ゥ");
		n2w.put("ｪ", "ェ");
		n2w.put("ｫ", "ォ");
		n2w.put("ｬ", "ャ");
		n2w.put("ｭ", "ュ");
		n2w.put("ｮ", "ョ");
		n2w.put("ｯ", "ッ");
		n2w.put("-", "－");
		n2w.put("ｱ", "ア");
		n2w.put("ｲ", "イ");
		n2w.put("ｳ", "ウ");
		n2w.put("ｴ", "エ");
		n2w.put("ｵ", "オ");
		n2w.put("ｶ", "カ");
		n2w.put("ｷ", "キ");
		n2w.put("ｸ", "ク");
		n2w.put("ｹ", "ケ");
		n2w.put("ｺ", "コ");
		n2w.put("ｻ", "サ");
		n2w.put("ｼ", "シ");
		n2w.put("ｽ", "ス");
		n2w.put("ｾ", "セ");
		n2w.put("ｿ", "ソ");
		n2w.put("ﾀ", "タ");
		n2w.put("ﾁ", "チ");
		n2w.put("ﾂ", "ツ");
		n2w.put("ﾃ", "テ");
		n2w.put("ﾄ", "ト");
		n2w.put("ﾅ", "ナ");
		n2w.put("ﾆ", "ニ");
		n2w.put("ﾇ", "ヌ");
		n2w.put("ﾈ", "ネ");
		n2w.put("ﾉ", "ノ");
		n2w.put("ﾊ", "ハ");
		n2w.put("ﾋ", "ヒ");
		n2w.put("ﾌ", "フ");
		n2w.put("ﾍ", "ヘ");
		n2w.put("ﾎ", "ホ");
		n2w.put("ﾏ", "マ");
		n2w.put("ﾐ", "ミ");
		n2w.put("ﾑ", "ム");
		n2w.put("ﾒ", "メ");
		n2w.put("ﾓ", "モ");
		n2w.put("ﾔ", "ヤ");
		n2w.put("ﾕ", "ユ");
		n2w.put("ﾖ", "ヨ");
		n2w.put("ﾗ", "ラ");
		n2w.put("ﾘ", "リ");
		n2w.put("ﾙ", "ル");
		n2w.put("ﾚ", "レ");
		n2w.put("ﾛ", "ロ");
		n2w.put("ﾜ", "ワ");
		n2w.put("ﾝ", "ン");
		n2w.put("ﾞ", "゛");
		n2w.put("ﾟ", "゜");
		n2w.put("ﾟ", "゜");
		n2w.put("-", "‐");
		n2w.put("-", "ー");//20150312
		n2w.put("ｰ", "ー");

	}

	// ---------------------------------------------------------------------
	// 全角→半角
	// ---------------------------------------------------------------------
	public void incoreW2N() {
		w2n = new HashMap();
		// .
		w2n.put("･", "･"); // 2010/08/18 追加・・・うーん
		w2n.put("　", " ");
		w2n.put("！", "!");
		w2n.put("”", "\"");
		w2n.put("＃", "#");
		w2n.put("＄", "$");
		w2n.put("％", "%");
		w2n.put("＆", "&");
		w2n.put("’", "\'");
		w2n.put("（", "(");
		w2n.put("）", ")");
		w2n.put("＊", "*");
		w2n.put("＋", "+");
		w2n.put("，", ",");
		w2n.put("ー", "-");
		w2n.put("．", ".");
		w2n.put("／", "/");
		w2n.put("０", "0");
		w2n.put("１", "1");
		w2n.put("２", "2");
		w2n.put("３", "3");
		w2n.put("４", "4");
		w2n.put("５", "5");
		w2n.put("６", "6");
		w2n.put("７", "7");
		w2n.put("８", "8");
		w2n.put("９", "9");
		w2n.put("：", ":");
		w2n.put("；", ";");
		w2n.put("＜", "<");
		w2n.put("＝", "=");
		w2n.put("＞", ">");
		w2n.put("？", "?");
		w2n.put("＠", "@");
		w2n.put("Ａ", "A");
		w2n.put("Ｂ", "B");
		w2n.put("Ｃ", "C");
		w2n.put("Ｄ", "D");
		w2n.put("Ｅ", "E");
		w2n.put("Ｆ", "F");
		w2n.put("Ｇ", "G");
		w2n.put("Ｈ", "H");
		w2n.put("Ｉ", "I");
		w2n.put("Ｊ", "J");
		w2n.put("Ｋ", "K");
		w2n.put("Ｌ", "L");
		w2n.put("Ｍ", "M");
		w2n.put("Ｎ", "N");
		w2n.put("Ｏ", "O");
		w2n.put("Ｐ", "P");
		w2n.put("Ｑ", "Q");
		w2n.put("Ｒ", "R");
		w2n.put("Ｓ", "S");
		w2n.put("Ｔ", "T");
		w2n.put("Ｕ", "U");
		w2n.put("Ｖ", "V");
		w2n.put("Ｗ", "W");
		w2n.put("Ｘ", "X");
		w2n.put("Ｙ", "Y");
		w2n.put("Ｚ", "Z");
		w2n.put("［", "[");
		w2n.put("￥", "\\");
		w2n.put("］", "]");
		w2n.put("＾", "^");
		w2n.put("＿", "_");
		w2n.put("｀", "`");
		w2n.put("ａ", "a");
		w2n.put("ｂ", "b");
		w2n.put("ｃ", "c");
		w2n.put("ｄ", "d");
		w2n.put("ｅ", "e");
		w2n.put("ｆ", "f");
		w2n.put("ｇ", "g");
		w2n.put("ｈ", "h");
		w2n.put("ｉ", "i");
		w2n.put("ｊ", "j");
		w2n.put("ｋ", "k");
		w2n.put("ｌ", "l");
		w2n.put("ｍ", "m");
		w2n.put("ｎ", "n");
		w2n.put("ｏ", "o");
		w2n.put("ｐ", "p");
		w2n.put("ｑ", "q");
		w2n.put("ｒ", "r");
		w2n.put("ｓ", "s");
		w2n.put("ｔ", "t");
		w2n.put("ｕ", "u");
		w2n.put("ｖ", "v");
		w2n.put("ｗ", "w");
		w2n.put("ｘ", "x");
		w2n.put("ｙ", "y");
		w2n.put("ｚ", "z");
		w2n.put("｛", "{");
		w2n.put("｜", "|");
		w2n.put("｝", "}");
		w2n.put("￣", "~");
		w2n.put("。", "｡");
		w2n.put("「", "｢");
		w2n.put("」", "｣");
		w2n.put("、", "､");
		w2n.put("・", "･");
		w2n.put("ヲ", "ｦ");
		w2n.put("ァ", "ｧ");
		w2n.put("ィ", "ｨ");
		w2n.put("ゥ", "ｩ");
		w2n.put("ェ", "ｪ");
		w2n.put("ォ", "ｫ");
		w2n.put("ャ", "ｬ");
		w2n.put("ュ", "ｭ");
		w2n.put("ョ", "ｮ");
		w2n.put("ッ", "ｯ");
		w2n.put("－", "-");
		w2n.put("ア", "ｱ");
		w2n.put("イ", "ｲ");
		w2n.put("ウ", "ｳ");
		w2n.put("エ", "ｴ");
		w2n.put("オ", "ｵ");
		w2n.put("カ", "ｶ");
		w2n.put("キ", "ｷ");
		w2n.put("ク", "ｸ");
		w2n.put("ケ", "ｹ");
		w2n.put("コ", "ｺ");
		w2n.put("サ", "ｻ");
		w2n.put("シ", "ｼ");
		w2n.put("ス", "ｽ");
		w2n.put("セ", "ｾ");
		w2n.put("ソ", "ｿ");
		w2n.put("タ", "ﾀ");
		w2n.put("チ", "ﾁ");
		w2n.put("ツ", "ﾂ");
		w2n.put("テ", "ﾃ");
		w2n.put("ト", "ﾄ");
		w2n.put("ナ", "ﾅ");
		w2n.put("ニ", "ﾆ");
		w2n.put("ヌ", "ﾇ");
		w2n.put("ネ", "ﾈ");
		w2n.put("ノ", "ﾉ");
		w2n.put("ハ", "ﾊ");
		w2n.put("ヒ", "ﾋ");
		w2n.put("フ", "ﾌ");
		w2n.put("ヘ", "ﾍ");
		w2n.put("ホ", "ﾎ");
		w2n.put("マ", "ﾏ");
		w2n.put("ミ", "ﾐ");
		w2n.put("ム", "ﾑ");
		w2n.put("メ", "ﾒ");
		w2n.put("モ", "ﾓ");
		w2n.put("ヤ", "ﾔ");
		w2n.put("ユ", "ﾕ");
		w2n.put("ヨ", "ﾖ");
		w2n.put("ラ", "ﾗ");
		w2n.put("リ", "ﾘ");
		w2n.put("ル", "ﾙ");
		w2n.put("レ", "ﾚ");
		w2n.put("ロ", "ﾛ");
		w2n.put("ワ", "ﾜ");
		w2n.put("ン", "ﾝ");
		w2n.put("あ", "ｱ");
		w2n.put("い", "ｲ");
		w2n.put("う", "ｳ");
		w2n.put("え", "ｴ");
		w2n.put("お", "ｵ");
		w2n.put("か", "ｶ");
		w2n.put("き", "ｷ");
		w2n.put("く", "ｸ");
		w2n.put("け", "ｹ");
		w2n.put("こ", "ｺ");
		w2n.put("さ", "ｻ");
		w2n.put("し", "ｼ");
		w2n.put("す", "ｽ");
		w2n.put("せ", "ｾ");
		w2n.put("そ", "ｿ");
		w2n.put("た", "ﾀ");
		w2n.put("ち", "ﾁ");
		w2n.put("つ", "ﾂ");
		w2n.put("て", "ﾃ");
		w2n.put("と", "ﾄ");
		w2n.put("な", "ﾅ");
		w2n.put("に", "ﾆ");
		w2n.put("ぬ", "ﾇ");
		w2n.put("ね", "ﾈ");
		w2n.put("の", "ﾉ");
		w2n.put("は", "ﾊ");
		w2n.put("ひ", "ﾋ");
		w2n.put("ふ", "ﾌ");
		w2n.put("へ", "ﾍ");
		w2n.put("ほ", "ﾎ");
		w2n.put("ま", "ﾏ");
		w2n.put("み", "ﾐ");
		w2n.put("む", "ﾑ");
		w2n.put("め", "ﾒ");
		w2n.put("も", "ﾓ");
		w2n.put("や", "ﾔ");
		w2n.put("ゆ", "ﾕ");
		w2n.put("よ", "ﾖ");
		w2n.put("ら", "ﾗ");
		w2n.put("り", "ﾘ");
		w2n.put("る", "ﾙ");
		w2n.put("れ", "ﾚ");
		w2n.put("ろ", "ﾛ");
		w2n.put("わ", "ﾜ");
		w2n.put("ん", "ﾝ");
		w2n.put("‐", "-");
		w2n.put("゛", "ﾞ");
		w2n.put("゜", "ﾟ");
		// ２syllableは２文字に変換される・・・
		w2n.put("ヴ", "ｳﾞ");
		w2n.put("ガ", "ｶﾞ");
		w2n.put("ギ", "ｷﾞ");
		w2n.put("グ", "ｸﾞ");
		w2n.put("ゲ", "ｹﾞ");
		w2n.put("ゴ", "ｺﾞ");
		w2n.put("ザ", "ｻﾞ");
		w2n.put("ジ", "ｼﾞ");
		w2n.put("ズ", "ｽﾞ");
		w2n.put("ゼ", "ｾﾞ");
		w2n.put("ゾ", "ｿﾞ");
		w2n.put("ダ", "ﾀﾞ");
		w2n.put("ジ", "ｼﾞ");
		w2n.put("ヅ", "ﾂﾞ");
		w2n.put("デ", "ﾃﾞ");
		w2n.put("ド", "ﾄﾞ");
		w2n.put("バ", "ﾊﾞ");
		w2n.put("ビ", "ﾋﾞ");
		w2n.put("ブ", "ﾌﾞ");
		w2n.put("ベ", "ﾍﾞ");
		w2n.put("ボ", "ﾎﾞ");
		w2n.put("パ", "ﾊﾟ");
		w2n.put("ピ", "ﾋﾟ");
		w2n.put("プ", "ﾌﾟ");
		w2n.put("ペ", "ﾍﾟ");
		w2n.put("ポ", "ﾎﾟ");
		w2n.put("が", "ｶﾞ");
		w2n.put("ぎ", "ｷﾞ");
		w2n.put("ぐ", "ｸﾞ");
		w2n.put("げ", "ｹﾞ");
		w2n.put("ご", "ｺﾞ");
		w2n.put("ざ", "ｻﾞ");
		w2n.put("じ", "ｼﾞ");
		w2n.put("ず", "ｽﾞ");
		w2n.put("ぜ", "ｾﾞ");
		w2n.put("ぞ", "ｿﾞ");
		w2n.put("だ", "ﾀﾞ");
		w2n.put("じ", "ｼﾞ");
		w2n.put("づ", "ﾂﾞ");
		w2n.put("で", "ﾃﾞ");
		w2n.put("ど", "ﾄﾞ");
		w2n.put("ば", "ﾊﾞ");
		w2n.put("び", "ﾋﾞ");
		w2n.put("ぶ", "ﾌﾞ");
		w2n.put("べ", "ﾍﾞ");
		w2n.put("ぼ", "ﾎﾞ");
		w2n.put("ぱ", "ﾊﾟ");
		w2n.put("ぴ", "ﾋﾟ");
		w2n.put("ぷ", "ﾌﾟ");
		w2n.put("ぺ", "ﾍﾟ");
		w2n.put("ぽ", "ﾎﾟ");
		// ---------------------------------------------------------------------
		// TODO 暫定的にこうしたが後で修正しておく！！ﾊﾝｶｸｶﾀｶﾅのレンジはどうなっているの？
		w2n.put("ｦ", "ｦ");
		w2n.put("ｧ", "ｧ");
		w2n.put("ｨ", "ｨ");
		w2n.put("ｩ", "ｩ");
		w2n.put("ｪ", "ｪ");
		w2n.put("ｫ", "ｫ");
		w2n.put("ｬ", "ｬ");
		w2n.put("ｭ", "ｭ");
		w2n.put("ｮ", "ｮ");
		w2n.put("ｯ", "ｯ");
		w2n.put("-", "-");
		w2n.put("ｱ", "ｱ");
		w2n.put("ｲ", "ｲ");
		w2n.put("ｳ", "ｳ");
		w2n.put("ｴ", "ｴ");
		w2n.put("ｵ", "ｵ");
		w2n.put("ｶ", "ｶ");
		w2n.put("ｷ", "ｷ");
		w2n.put("ｸ", "ｸ");
		w2n.put("ｹ", "ｹ");
		w2n.put("ｺ", "ｺ");
		w2n.put("ｻ", "ｻ");
		w2n.put("ｼ", "ｼ");
		w2n.put("ｽ", "ｽ");
		w2n.put("ｾ", "ｾ");
		w2n.put("ｿ", "ｿ");
		w2n.put("ﾀ", "ﾀ");
		w2n.put("ﾁ", "ﾁ");
		w2n.put("ﾂ", "ﾂ");
		w2n.put("ﾃ", "ﾃ");
		w2n.put("ﾄ", "ﾄ");
		w2n.put("ﾅ", "ﾅ");
		w2n.put("ﾆ", "ﾆ");
		w2n.put("ﾇ", "ﾇ");
		w2n.put("ﾈ", "ﾈ");
		w2n.put("ﾉ", "ﾉ");
		w2n.put("ﾊ", "ﾊ");
		w2n.put("ﾋ", "ﾋ");
		w2n.put("ﾌ", "ﾌ");
		w2n.put("ﾍ", "ﾍ");
		w2n.put("ﾎ", "ﾎ");
		w2n.put("ﾏ", "ﾏ");
		w2n.put("ﾐ", "ﾐ");
		w2n.put("ﾑ", "ﾑ");
		w2n.put("ﾒ", "ﾒ");
		w2n.put("ﾓ", "ﾓ");
		w2n.put("ﾔ", "ﾔ");
		w2n.put("ﾕ", "ﾕ");
		w2n.put("ﾖ", "ﾖ");
		w2n.put("ﾗ", "ﾗ");
		w2n.put("ﾘ", "ﾘ");
		w2n.put("ﾙ", "ﾙ");
		w2n.put("ﾚ", "ﾚ");
		w2n.put("ﾛ", "ﾛ");
		w2n.put("ﾜ", "ﾜ");
		w2n.put("ﾝ", "ﾝ");
		w2n.put("ﾞ", "ﾞ");
		w2n.put("ﾟ", "ﾟ");
		w2n.put("ｰ", "ｰ");
		// ---------------------------------------------------------------------
		// ここからオマケ
		w2n.put("一", "1");
		w2n.put("二", "2");
		w2n.put("三", "3");
		w2n.put("四", "4");
		w2n.put("五", "5");
		w2n.put("六", "6");
		w2n.put("七", "7");
		w2n.put("八", "8");
		w2n.put("九", "9");
		w2n.put("十", "0");
		w2n.put("ヰ", "ｲ");
		w2n.put("漢", "ｶﾝ");
		w2n.put("字", "ｼﾞ");
		w2n.put("個", "ｺ");
		w2n.put("枚", "ﾏｲ");
		w2n.put("★", "*");
		w2n.put("△", "^");
		w2n.put("■", "#");
		w2n.put("『", "{");
		w2n.put("』", "}");
		w2n.put("【", "[");
		w2n.put("】", "]");
		w2n.put("《", "<");
		w2n.put("》", ">");
		// ↑ ここまでオマケ
		// 拗音　2010-07-27追加
		w2n.put("ぁ", "ｧ");
		w2n.put("ぃ", "ｨ");
		w2n.put("ぅ", "ｩ");
		w2n.put("ぇ", "ｪ");
		w2n.put("ぉ", "ｫ");
		w2n.put("ゃ", "ｬ");
		w2n.put("ゅ", "ｭ");
		w2n.put("ょ", "ｮ");
		w2n.put("っ", "ｯ");
		w2n.put("ゎ", "ﾜ");
	}

	// ---------------------------------------------------------------------
	// ファイル名禁則文字(いまのところWindows)
	// これはＯＳによって違ってくる
	// あとで見直す、あれれＥｘｃｅｌとオンナジだ？！
	// ---------------------------------------------------------------------
	public void incoreNGF() {
		ngf = new HashMap(); // ファイル名に使えない文字
		ngf.put(" ", "");
		ngf.put("\\", "_");
		ngf.put("/", "_");
		ngf.put(":", "_");
		ngf.put("*", "_");
		ngf.put("?", "_");
		ngf.put("\"", "_");
		ngf.put("<", "_");
		ngf.put(">", "_");
		ngf.put("|", "_");
	}

	// ---------------------------------------------------------------------
	// Excel禁則文字
	// ---------------------------------------------------------------------
	public void incoreNGX() {
		ngx = new HashMap(); // エクセルシート名に使えない文字など
		ngx.put("\\", "_");
		ngx.put("/", "_");
		ngx.put("*", "_");
		ngx.put("[", "_");
		ngx.put("]", "_");
	}

	// ---------------------------------------------------------------------
	// 半角カタカナnormalize(jicfsｶﾅﾒｲｼｮｳをHostに送る為)2010/08/19
	// ---------------------------------------------------------------------
	public void incoreK2N() {
		k2n = new HashMap();
		k2n.put("!", " ");
		k2n.put("･", "･");
		k2n.put("ｰ", "-");
		k2n.put("ｧ", "ｱ");
		k2n.put("ｨ", "ｲ");
		k2n.put("ｩ", "ｳ");
		k2n.put("ｪ", "ｴ");
		k2n.put("ｫ", "ｵ");
		k2n.put("ｬ", "ﾔ");
		k2n.put("ｭ", "ﾕ");
		k2n.put("ｮ", "ﾖ");
		k2n.put("ｯ", "ﾂ");
		k2n.put("~", "_");
	}

	// ---------------------------------------------------------------------
	// 漢字コードを変換　HOST　2010/08/19 つぼ
	// ---------------------------------------------------------------------
	public void incoreCKJ() {
		ckj = new HashMap();
		// （変換元、変換先）
		//	ckj.put("－", "ー");//20161102=>20170628

		ckj.put("藪", "薮");
		ckj.put("鶯", "鴬");
		ckj.put("麩", "麩");
		ckj.put("琉", "琉");
		ckj.put("潅", "灌");
		ckj.put("鉾", "鉾");
		ckj.put("靭", "靱");

		ckj.put("諫", "諌");
		ckj.put("諌", "諫");

		// クロスコンバートされている？？ようだ
		ckj.put("鯵", "鰺");
		ckj.put("鰺", "鯵");

		ckj.put("蠣", "蛎");
		ckj.put("蛎", "蠣");

		ckj.put("頚", "頸");
		ckj.put("頸", "頚");

		ckj.put("籠", "篭");
		ckj.put("篭", "籠");

		ckj.put("砺", "礪");
		ckj.put("礪", "砺");

		ckj.put("壷", "壺");
		ckj.put("壺", "壷");

	}

	// -------------------------------------------------------------------------
	// file2HashMap ファイルをハッシュテーブルに読み込む
	// -------------------------------------------------------------------------
	// file2HashMap wHtbl = FileUtil.file2HashMap("hosts","\t");
	// System.out.println("size:"+wHtbl.size());
	// System.out.println("---------------------------------------------------");
	// for (Enumeration enum = wHtbl.elements(); enum.hasMoreElements() ;) {
	// System.out.println("enum:"+enum.nextElement());
	// }
	// -------------------------------------------------------------------------
	public static HashMap file2HashMap(String path) {
		String pDlm = "\t";
		// System.out.println("■FileUtil.file2HashMap:"+pPath);
		File fl = new File(path);
		if (fl.exists() == false)
			return null;
		HashMap wHtbl = new HashMap();
		String wRec;
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] wArray = wRec.split(pDlm);
					if (wArray.length > 1) {
						wHtbl.put(wArray[0], wArray[1]);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return wHtbl;
	}

	// -------------------------------------------------------------------------
	// filterT フィルタープログラム 藤居氏パラメータ加工 使用例と・・
	// 例 boolean swt = filterT(wPath_I);
	// -------------------------------------------------------------------------
	public static boolean filterT(String path) {
		String wLs = System.getProperty("line.separator");
		String wDlm = ",";
		long l = System.currentTimeMillis();
		String sCel0 = "";
		String sCel1 = "";
		String sCel2 = "";
		String sCel3 = "";
		File file = new File(path);
		if (!file.exists())
			return false;

		CharConv wCnv = CharConv.getInstance();
		// CharConv wCnv = new CharConv();

		String wRec = "";
		int wSize = 1024;
		StringBuffer sBuf = new StringBuffer(wSize);
		try {
			FileWriter pFr = new FileWriter("catbrd.CSV");
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((wRec = br.readLine()) != null) {
				String[] wCel = wRec.split("\t");
				// for(int i = 0;i< wCel.length ; i++){
				// System.out.println(""+i+">"+wCel[i]);
				// }
				if (wCel.length >= 2) {
					if (!wCel[0].equals("")) {
						sCel0 = wCel[0].trim();
						if (!wCel[1].equals("")) {
							sCel1 = wCel[1].trim();
							sCel1 = wCnv.cnvWide(sCel1, 25, '　');
						}
					}
				}

				if (wCel.length > 3) {

					if (!wCel[2].equals(""))
						sCel2 = wCel[2].trim();
					if (!wCel[3].equals("")) {
						sCel3 = wCel[3].trim();
						sCel3 = wCnv.cnvWide(sCel3, 25, '　');
					}

					sBuf.delete(0, sBuf.length());
					sBuf.append(sCel0);
					sBuf.append(wDlm);
					sBuf.append(sCel1);
					sBuf.append(wDlm);
					sBuf.append(sCel2);
					sBuf.append(wDlm);
					sBuf.append(sCel3);
					sBuf.append(wLs);

					// System.out.println("r>"+sBuf.toString());
					pFr.write(sBuf.toString());
				}

			}
			br.close();
			pFr.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		l -= System.currentTimeMillis();
		System.out.println("Elapse:" + (-l));
		return true;
	}

	// -------------------------------------------------------------------------
	// for test
	// -------------------------------------------------------------------------
	public static void test02() {
		CharConv.filterT("Fujii.txt");
	}

	public static void test01() {

		// CharConv conv = new CharConv();
		kyPkg.uCodecs.CharConv conv = kyPkg.uCodecs.CharConv.getInstance();

		String wWide = conv.cnvWide(TEST_STR1);
		String wWide2 = conv.cnvWide_classic(TEST_STR1);
		String wFix = CharConv.cnvFixLength(TEST_STR1, 50, '★');
		String wNarrow = conv.cnvNarrow(wWide, '?');
		String wNgf = conv.cnvNGFnm(wNarrow);
		String wNgx = conv.cnvNGExcel(wNarrow);
		String wk2n = conv.cnvK2N(TEST_STR1);
		String wckj = conv.cnvCKJ(TEST_STR1);// ホスト用漢字にコンバートするロジック

		System.out.println("#Original   =>" + TEST_STR1);
		System.out.println("#Wide2      =>" + wWide2);
		System.out.println("#Wide       =>" + wWide);
		System.out.println("#Narrow     =>" + wNarrow);
		System.out.println("#Fix        =>" + wFix);
		System.out.println("#cnvNGFnm   =>" + wNgf);
		System.out.println("#cnvNGExcel =>" + wNgx);
		System.out.println("#ｶﾅforHost =>" + wk2n);
		System.out.println("#漢字forHost =>" + wckj);
		conv = null;
	}

	//固定長全角文字列に変換する
	public static void testcnvWide20150309() {
		String wWide = CharConv.cnvWide(TEST_STR2, 50);
		System.out.println("#Original   =>" + TEST_STR2);
		System.out.println("#Wide       =>" + wWide);
	}

	public static void main(String[] argv) {
		// test01();
		testcnvWide20150309();
	}

}
