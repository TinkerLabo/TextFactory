package kyPkg.filter;

import static kyPkg.sql.ISAM.FORMAT_CSV_DELIMITED;
import static kyPkg.sql.ISAM.FORMAT_TAB_DELIMITED;
import static kyPkg.sql.ISAM.SCHEMA_INI;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Guess4Isam extends Guess {

	// 同じフィールド名が存在してしまう可能性があるので気を付ける
	private static final String TEXT = "Text";
	private static final String INT = "Integer";
	private static final String FLOAT = "Float";
	private static final String CHAR = "char";
	private static final String QUESTION = "Question";
	private static final String HALF_STRING = "ﾊﾝｶｸ";
	private static final String WIDE_STRING = "全角";
	private static final String NUMERIC = "数字";
	private static final String DOTNUMERIC = "小数点値";
	private static List<String> schemaList;
	private static List<String> fieldDefs;

	// ------------------------------------------------------------------------
	// マトリックスを分析して、ISAMのSchema.iniを生成するプログラム
	// ------------------------------------------------------------------------
	private void autoDetect(Vector<Vector> matrix, boolean headerOpt) {
		fieldDefs = null;
		if (matrix == null || matrix.size() <= 0)
			return;
		fieldDefs = new ArrayList();
		//		fieldNames = new ArrayList();
		//		typeFigure = new ArrayList();
		// Header enumerate
		List rows = matrix.get(0);
		int maxCol = rows.size();
		String heads[] = new String[maxCol];
		String guess[] = new String[maxCol];
		Integer maxVal[] = new Integer[maxCol];
		Integer maxLen[] = new Integer[maxCol];
		Integer minLen[] = new Integer[maxCol];
		// --------------------------------------------------------------------
		// ヘッダー名称の処理
		// --------------------------------------------------------------------
		if (headerOpt) {
			// 先頭行をヘッダーとする場合
			for (int col = 0; col < maxCol; col++) {
				heads[col] = rows.get(col).toString();
			}
		} else {
			// デフォルトのヘッダー名を生成
			for (int col = 0; col < maxCol; col++) {
				heads[col] = PREFIX + String.valueOf(col + 1);
			}
		}
		// --------------------------------------------------------------------
		int skip = 1;
		Object obj;
		for (int col = 0; col < maxCol; col++) {
			guess[col] = QUESTION;
			maxVal[col] = -1;
			maxLen[col] = -1;
			minLen[col] = Integer.MAX_VALUE;
			int line = 0;
			for (List rowObj : matrix) {
				line++;
				if (line > skip && rowObj != null) {
					obj = rowObj.get(col);
					if (obj != null) {
						String val = obj.toString().trim();// スペースを含まない長さとなる
						int iVal = getMaxVal(val);
						if (maxVal[col] < iVal)
							maxVal[col] = iVal;
						int curLen = val.length();
						if (maxLen[col] < curLen)
							maxLen[col] = curLen;
						if (minLen[col] > curLen)
							minLen[col] = curLen;
						// 半角文字列か？
						if (kyPkg.uRegex.Regex.isHalfWidthString(val)) {
							if (kyPkg.uRegex.Regex.isNumeric(val)) {
								guess[col] = NUMERIC;// 数値
							} else if (kyPkg.uRegex.Regex.isDotNumeric(val)) {
								guess[col] = DOTNUMERIC;// 小数点値
							} else {
								guess[col] = HALF_STRING;// 半角文字列
							}
						} else {
							if (kyPkg.uRegex.Regex.isFullWidthString(val)) {
								guess[col] = WIDE_STRING;// 漢字文字列
							} else {
								guess[col] = QUESTION;// 全角半角混在
							}
						}
					}
				}
			}
			// ----------------------------------------------------------------
			// Col1="kCode" Char Width 13
			// Col10="kAMT" Integer Width 7
			// Type. データ型。許可値は Char、Text、
			// Float、Double、Integer、Short、LongChar、Memo、および Date です。
			// ----------------------------------------------------------------
			String field = "";
			String type = "";
			String lengs = " Width " + maxLen[col] + " ";
			if (guess[col].equals(WIDE_STRING)) {
				type = CHAR + lengs;
			} else {
				if (guess[col].equals(NUMERIC)) {
					type = INT;
					if (maxLen[col] == minLen[col] && maxLen[col] >= 3) {
						type = CHAR + lengs; // 3文字以上の数字列で長さがそろっていたらcharとする
					}
				} else if (guess[col].equals(DOTNUMERIC)) {
					type = FLOAT;
				} else {
					//					type = CHAR + lengs; // 半角全角混在の場合など
					//					if (maxLen[col] == minLen[col] && maxLen[col] == 0) {
					type = TEXT;// すべて空（開始位置、長さを指定していない）
					//					}
				}
			}
			field = heads[col];
			names.add(field);
			typeFigures.add(type);

			String fieldDef = "Col" + String.valueOf((col + 1)) + "=" + "\""
					+ field + "\" " + type;
			fieldDefs.add(fieldDef);
		}
		debug("check0>>>>>>>>>>>>>>>>>");

	}

	private void debug(String comment) {
		boolean debug = false;
		if (debug == false)
			return;
		System.out.println(comment);
		for (String element : names) {
			System.out.println("element>" + element);
		}

	}

	@Override
	public void analyzeIt(String inPath, boolean headOpt) {
		// ---------------------------------------------------------------------
		// data pickup
		// ---------------------------------------------------------------------
		int limit = -1;
		int skip = 0;
		Vector<Vector> matrix = super.getMatrix(inPath, skip, limit);
		// File2Matrix.debugTheMatrix(matrix);// for Debug
		if (matrix == null || matrix.size() == 0) {
			System.out.println("ERROR?! Empty Data?:" + inPath);
			return;
		}
		if (headOpt)
			headOpt = isHeaderExist(matrix);

		// ---------------------------------------------------------------------
		// autoDetect ( field & type )
		// ---------------------------------------------------------------------
		// Guess4Isam isamGuess = new Guess4Isam();
		autoDetect(matrix, headOpt);
		debug("check1>>>>>>>>>>>>>>>>>");

		// ---------------------------------------------------------------------
		// create schema
		// ---------------------------------------------------------------------
		String name = kyPkg.uFile.FileUtil.getName(inPath);
		name = name.toUpperCase();
		schemaList = new ArrayList();
		schemaList.add("[" + name + "]");

		// XXX ColNameHeader 先頭行をヘッダーとみなすかどうか
		if (headOpt) {
			schemaList.add("ColNameHeader=True        ");
		} else {
			schemaList.add("ColNameHeader=False        ");
		}
		if (delimiter.equals("\t")) {
			schemaList.add(FORMAT_TAB_DELIMITED);
		} else {
			schemaList.add(FORMAT_CSV_DELIMITED);
		}
		schemaList.add("MaxScanRows=0              ");
		schemaList.add("CharacterSet=OEM");
		for (int i = 0; i < fieldDefs.size(); i++) {
			schemaList.add(fieldDefs.get(i));
		}
		// ---------------------------------------------------------------------
		// 最頻、変動、標準偏差のあたりを計算するのに使用したいので・・・その辺を考える
		// SQL ServerやOracleで標準偏差（Standard Deviation）を求めるにはSTDDEVもしくはSTDEVを使います。
		// 例>SELECT id,avg(val1) as avg,stdev(val1)as stdev FROM LOY1_HEAD.TXT
		// group by id
		// 同じ階層に他のファイル（何かは不明）があるとうまく認識されない様子(schema.iniと当該ファイルのみならうまく動いてくれるﾎﾟｲ・・・)
		// ---------------------------------------------------------------------
		String outPath = parentDir + SCHEMA_INI;
		EzWriter.list2File(outPath, schemaList);

		// ---------------------------------------------------------------------
		// 雛形ＳＱＬをつくる
		// ---------------------------------------------------------------------
		debug("check2>>>>>>>>>>>>>>>>>");

		i_List = insert_sql(names, tableName);
		create_Table_sql = create_Table_sql(names, typeFigures, tableName);
		r_List = select_sql(names, tableName);
		u_List = update_sql(names, tableName);
		d_List = delete_sql(names, tableName);
		sampleCodeList = sampleCode(getR_Sql());// Select 文を拾ってくる

		// ---------------------------------------------------------------------
		// 確認用に出力しているが不要かも・・
		// ---------------------------------------------------------------------
		// EzWriter.list2File(preExt + "_C.txt", c_List);
		// EzWriter.list2File(preExt + "_I.txt", i_List);
		// EzWriter.list2File(preExt + "_R.txt", r_List);
		// EzWriter.list2File(preExt + "_U.txt", u_List);
		// EzWriter.list2File(preExt + "_D.txt", d_List);

	}

	public static void main(String[] argv) {
		String inPath = "C:/loy1_Head.txt";
		Guess4Isam ins = new Guess4Isam();
		ins.analyzeIt(inPath, true);
	}
}
