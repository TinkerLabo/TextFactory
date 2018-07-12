package kyPkg.filter;

// -------------------------------------------------------------------------
// インナークラス ParmsObj substr用のパラメータ
// -------------------------------------------------------------------------
public class ParmsObj {
	private static final String BAR = "|";
	private static final String RETURN = "改行";
	private static final String SPACE = "スペース";
	private static final String TAB = "タブ";
	private static final String COMMA = "カンマ";
	private int col = -1;
	private int start = -1;
	private int len = -1;
	private int[] parmi;

	private String comment = "";//コメントはヘッダーに流用される場合もある
	private String filterName = " ";
	private String param = "";//フィルターに与えるパラメータ
	private String[] paramArray;
	private String signature = " ";//フィルターを一意に判別する為（同じフィルターでもパラメータによって挙動が変わる為）

	public String getSignature() {
		return signature;
	}

	public String getParam() {
		return param;
	}

	// ---------------------------------------------------------------------
	// アクセッサ
	// ---------------------------------------------------------------------
	public String getFilterName(boolean debug) {
		return filterName;
	}

	public int getCol() {
		return col;
	}

	public int getStart() {
		return start;
	}

	public int getLen() {
		return len;
	}

	public String getParm(int n) {
		if (0 <= n && n < paramArray.length)
			return paramArray[n];
		return null;
	}

	public int getParmi(int n) {
		if (0 <= n && n < parmi.length)
			return parmi[n] - 1;
		return -1;
	}

	public String getComment(boolean debug) {
		return comment;
	}

	// ---------------------------------------------------------------------
	// コンストラクタ
	// ---------------------------------------------------------------------
	// parm は→パラメータ + タブ + コメント
	// jackUpは、パラメータに履かせている下駄 インデックス指定だと見た目わかりづらい
	// new ParmsObj("1,,2"); ←この場合救済されるべきだろう・・・
	// ---------------------------------------------------------------------
	public ParmsObj(Object parm) {
		parse(parm.toString());
	}

	private void parse(String parm) {
		col = -1;
		start = -1;
		len = -1;
		filterName = "";
		comment = "";
		param = "";
		paramArray = new String[] { "" };

		String[] splitedByTAB = parm.split("\t"); // タブ以降はコメント扱いとする
		if (splitedByTAB.length >= 2) {
			comment = splitedByTAB[1];
		}
		splitedByTAB[0] = splitedByTAB[0] + ",,, ";// パラメータが何も指定されていないときを想定
		String[] splitedByComma = splitedByTAB[0].split(","); // パラメータはカンマ区切り
		if (splitedByComma[0].equals("@")) {
			filterName = "@";// 固定文字列
			if (splitedByComma.length > 1) {
				param = splitedByComma[1];
			}
		} else {
			if (splitedByComma[0].equals("D")) {
				filterName = "D";// 区切り文字
				if (splitedByComma[1].equals("")) {
					param = "\t";
				} else if (splitedByComma[1].equals(COMMA)) {
					param = ",";
				} else if (splitedByComma[1].equals(TAB)) {
					param = "\t";
				} else if (splitedByComma[1].equals(SPACE)) {
					param = " ";
				} else if (splitedByComma[1].equals(RETURN)) {
					param = "\n";
				} else if (splitedByComma[1].equals(BAR)) {
					param = ",";
				}
			} else {
				if (splitedByComma[0].matches("\\d+")) {
					filterName = "";
					col = Integer.parseInt(splitedByComma[0]) - 1;//処理対象とするカラム
					if (splitedByComma.length > 1) {
						if (!splitedByComma[1].equals("*")) {
							if (splitedByComma.length > 1
									&& splitedByComma[1].matches("\\d+")) {
								start = Integer.parseInt(splitedByComma[1]) - 1;
							}
							if (splitedByComma.length > 2
									&& splitedByComma[2].matches("\\d+")) {
								int endPos = Integer
										.parseInt(splitedByComma[2]);
								len = (endPos - start);
							}
							// if (startPos < 0 && endPos >= 0)
							if (start < 0 && len >= 0)
								start = 0;
							if (splitedByComma.length > 3)
								filterName = splitedByComma[3];//フィルター名称
							if (splitedByComma.length > 4) {
								if (!splitedByComma[4].trim().equals("")) {
									param = splitedByComma[4];//フィルター用パラメータ
									paramArray = param.split(":");
									if (paramArray.length > 0) {
										// 毎度毎度パースするオーバーヘッドを回避・・int配列を用意
										parmi = new int[paramArray.length];
										for (int i = 0; i < paramArray.length; i++) {
											try {
												parmi[i] = Integer
														.parseInt(getParm(i));
											} catch (NumberFormatException e) {
												parmi[i] = -1; // 数値じゃないものは-1
											}
										}
									} else {
										parmi = null;
									}
								}
							}
						}
					}
				} else {
					System.out.println("#Error@ParmsObject  Parm:" + parm);
				}
			}
		}
		signature = filterName + param;

	}

} // end of class ParmsObj
