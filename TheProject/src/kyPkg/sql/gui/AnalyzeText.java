package kyPkg.sql.gui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import kyPkg.uFile.FileUtil;

public class AnalyzeText {
	public static final String preFix = "fld";
	public static final String TYPE = "type";
	public static final String NAME = "name";

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// テキストファイルをテーブルにインポートする
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public static HashMap<String, List<Object>> analyzeIt(String path,
			String encode, String delimiter, int checkCount,
			boolean headOption) {
		if (encode.equals("")){
//			encode = System.getProperty("file.encoding");
			encode = FileUtil.getDefaultEncoding();//20161222
		}

		int cnt = 0;
		String[] arrays;
		int[] eachMaxLen = null;
		String token = "";

		HashMap<String, List<Object>> map = new HashMap<String, List<Object>>();
		map.put(NAME, new ArrayList());
		map.put(TYPE, new ArrayList());
		String wRec = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), encode));
			while ((wRec = br.readLine()) != null) {
				cnt++;
				if (cnt > checkCount)
					break;
				arrays = wRec.split(delimiter);
				if (cnt == 1) {
					// 各行の長さがまちまちだと困るのだが・・・・
					eachMaxLen = new int[arrays.length];
					// 1行目にヘッダー情報がある場合
					if (headOption == true) {
						StringTokenizer tok = new StringTokenizer(wRec,
								delimiter);
						while (tok.hasMoreTokens()) {
							token = tok.nextToken();
							String[] elm = token.split(" ");
							if (elm[0].startsWith("*")) { // *で始まる項目は、数値とみなす
								map.get(NAME).add(elm[0].substring(1).trim()); // *より後ろの文字を取り出す
								map.get(TYPE).add("INTEGER");
							} else {
								map.get(NAME).add(elm[0].trim());
								if (elm.length > 1) {
									String wType = elm[1].trim().toUpperCase();
									if (wType.equals("INTEGER")) {
										map.get(TYPE).add(wType);
									} else if (wType.startsWith("VARCHAR")) {
										map.get(TYPE).add(wType);
									}
								} else {
									map.get(TYPE).add("VARCHAR");
								}
							}
						}
						// このオプションが指定された場合、ファイルの内容は調査しない
						break;
					}
				}
				// ファイルの中身を調べる
				for (int i = 0; i < arrays.length; i++) {
					// 一番長い文字列の長さを調べる(スペースも長さの内に入れる)
					if (i < eachMaxLen.length) {
						String wk = arrays[i];
						if (isNumeric(wk.trim()) == false) {
							if (eachMaxLen[i] < wk.length())
								eachMaxLen[i] = wk.length();
						}
					}
				}
			}
			for (int seq = 0; seq < eachMaxLen.length; seq++) {
				map.get(NAME).add(preFix + seq);
				if (eachMaxLen[seq] == 0) {
					map.get(TYPE).add("INTEGER");
				} else {
					map.get(TYPE).add("VARCHAR(" + eachMaxLen[seq] + ")");
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return map;
	}

	/**
	 * 半角数字文字列かどうか
	 * 
	 * @param pStr
	 *            検査文字列
	 * @return 半角数字のみの文字列ならtrue
	 */
	private static boolean isNumeric(String pStr) {
		// System.out.println("isNumeric in :" + pStr);
		boolean wRtn = false;
		if (pStr.matches("[+-]*\\d+[.]*\\d*")) {
			// System.out.println("チェック結果：半角数字のみの文字列です。");
			wRtn = true;
		} else {
			// System.out.println("チェック結果：半角数字以外の文字が含まれます。");
			wRtn = false;
		}
		// System.out.println("isNumeric Ans:" + wRtn);
		return wRtn;
	}

}
