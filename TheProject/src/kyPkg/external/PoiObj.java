package kyPkg.external;

import static kyPkg.util.KUtil.enumHashMap;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kyPkg.Sorts.IncoreSort;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.TextFactory;
import kyPkg.filter.Inf_oClosure;
import kyPkg.panelsc.Inf_Context;
import kyPkg.uFile.FileUtil;
import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import globals.ResControl;

//-----------------------------------------------------------------------------
//XXX 神戸生協のデータを読み込む処理を作成する、規定の分類に従いカテゴリーコードを付与する
//-----------------------------------------------------------------------------
public class PoiObj {
	public static final String LF = System.getProperty("line.separator");
	private List<String> sheets = null;
	private HashMap<String, Integer> sheetMap = null;
	// private HSSFWorkbook wkBook;
	private Workbook wkBook;

	private static String delimiter = "\t";
	private String bookPath;

	public String getBookPath() {
		return bookPath;
	}

	public static void main(String[] args) {
		test00();
		// test01();
		// test02();
		// testEx();
	}

	// -------------------------------------------------------------------------
	// 　例＞　Excel のシートを読み出してｃｓｖテキストに出力する
	// -------------------------------------------------------------------------
	public static void test00() {
		String bookPath1 = "";
		bookPath1 = "C:/result.xlsx";
		PoiObj poiObj1 = new PoiObj(bookPath1);
		String rootDir = globals.ResControl.getQprRootDir();
		System.out.println("rootDir:" + rootDir);
		poiObj1.saveAsCsv(rootDir + "まとめ.txt", "result"); // シート名がスペース指定なら一番先頭のシートをあてがう
	}

	// -------------------------------------------------------------------------
	// 　例＞　Excel のシート上の指定列のデータをリストに変換する
	// -------------------------------------------------------------------------
	public static void test01() {
		String bookPath1 = "";
		bookPath1 = ResControl.NSEI_DIR
				+ "100616湯浅さんへコープこうべＪＩＣＦＳコード/PB100619.xls";
		PoiObj poiObj1 = new PoiObj(bookPath1);
		List chklist = poiObj1.getList("", 4, 0);
		for (Iterator iterator = chklist.iterator(); iterator.hasNext();) {
			String str = (String) iterator.next();
			System.out.println("str:" + str);
		}
	}

	// -------------------------------------------------------------------------
	// 　例＞　Excel のシート上の指定列のデータをハッシュマップに変換する
	// -------------------------------------------------------------------------
	public static void test02() {
		System.out.println("Start!");
		String bookPath2 = "";
		bookPath2 = ResControl.NSEI_DIR
				+ "100616湯浅さんへコープこうべＪＩＣＦＳコード/生協ライン体系.xls";
		PoiObj poiObj2 = new PoiObj(bookPath2);
		HashMap<String, String> dic = poiObj2.getHmap("まとめ", 2, 4, 1);
		poiObj2 = null;
		enumHashMap(dic);
		System.out.println("map.size():" + dic.size());
		System.out.println("Fin!");
	}

	// -------------------------------------------------------------------------
	// 神戸生協データフォーマット＆カテゴリー設定 2010-07-27 yuasa
	// 【Ｆｌｏｗ】
	// 神戸生協更新データ（Ｅｘｃｅｌ）ファイル
	// ↓
	// 神戸生協の分類コード一覧（生協ライン体系.xls）より辞書をカテゴリーコード変換用作成する→HashMap dic
	// ※シート名は「まとめ」
	// ↓
	// 神戸生協の更新データ（PB100619.xls）を先ほど作成した辞書を使って編集しつつテキストファイルに保存する
	// ↓
	// レイアウト変換（パラメータファイルは「c:/kobeSParam.txt」）
	// 5,1,13," + FIX_HALF + ", jan
	// D,タブ
	// 4,1,6," + FIX_HALF + ", category
	// D,タブ
	// 6,1,25," + FIX_HALF + ", ｶﾅﾒｲｼｮｳ
	// D,タブ
	// 6,1,20,固定長全角, 漢字名称
	// ↓
	// 結果をソート出力
	// -------------------------------------------------------------------------
	public static void testEx() {
		String masterPath = "";
		String tranPath = "";

		masterPath = ResControl.NSEI_DIR
				+ "100616湯浅さんへコープこうべＪＩＣＦＳコード/生協ライン体系.xls";
		PoiObj poiMst = new PoiObj(masterPath);
		HashMap<String, String> dic = poiMst.getHmap("まとめ", 4, 6, 1);// 対象シート、キー位置、値位置、読み込み開始行
		poiMst = null;
		// KUtil.enumHashMap(dic); // for Debug
		// System.out.println("dictionary.size():" + dic.size());

		tranPath = ResControl.NSEI_DIR
				+ "100616湯浅さんへコープこうべＪＩＣＦＳコード/PB100619.xls";
		String rootDir = globals.ResControl.getQprRootDir();
		String wkPath = rootDir + "wkKobe.txt";
		PoiObj poiTrn = new PoiObj(tranPath);
		poiTrn.saveAsCsvMod(wkPath, "", 2, 6, dic, 3, null); // シート名がスペース指定なら一番先頭のシートをあてがう

		// XXX substrフィルターで整形する・・・従来のものもフォーマットしたほうがいいだろう
		// XXX ソートをどうするか
		// XXX マクロミルに渡す際、他の生協と併せたいが　どうすればいいだろう
		// XXX 流し込む前に目検の必要はないか？！

		// ---------------------------------------------------------------------
		// Format
		// ---------------------------------------------------------------------
		String outPath2 = rootDir + "pre神戸.txt";
		String outPath3 = rootDir + "srt神戸.txt";
		String param2 = rootDir + "kobeSParam.txt";
		// log.info("	---------------------------------------------------");
		// log.info("	Flt_SUBSTR2によりフォーマット処理する");
		TextFactory substr2 = new TextFactory(outPath2, wkPath,
				new kyPkg.converter.SubstrCnv(param2));
		// substr2.setRedersDelimiter("\t");
		substr2.execute();
		new IncoreSort("0,String,asc", new EzWriter(outPath3),
				new EzReader(outPath2)).execute();

		System.out.println("Finish!");

		// 5,1,13," + FIX_HALF + ", jan
		// D,タブ
		// 4,1,6," + FIX_HALF + ", category
		// D,タブ
		// 6,1,25," + FIX_HALF + ", ｶﾅﾒｲｼｮｳ
		// D,タブ
		// 6,1,20,固定長全角, 漢字名称

	}

	// --------------------------------------------------------------------------------------------------------
	// 2013-07-17
	// poi2.5　→　poi3.9に変更　excel　2008以降のファイルはＸＦＦＳ形式で（それ以前はHSSF形式）で取回さわなければならないようだ
	// 緩衝用にWorkbookFactory.create　というファクトリメソッドがあり、インターフェースで受けて操作する形に書き換えればよさそう
	// とりあえず引っかかったものをインタフェースに変えてみた・・・どこで引っかかるかわからない。
	// ライブラリの入れ替えにも注意が必要（サブフォルダにおまけのjarが入っていたりする）
	// 参考にした→ http://www.javadrive.jp/poi/install/
	// --------------------------------------------------------------------------------------------------------
	// あと「エンコーディング設定(ENCODING_UTF_16)は不要になった」らしい？！のでエラーが出る該当箇所をコメントアウトしてみた
	// org.apache.poi.hssf.usermodel -> HSSFCell -> setEncoding(short encoding)
	// 参考にした→ http://d.hatena.ne.jp/papa33/20071001
	// --------------------------------------------------------------------------------------------------------
	// なぜか今まで以上にリージョンを食うようになった気がする
	// JVMのコマンドラインパラメタ
	// -server -Xms256M -Xmx256M
	// --------------------------------------------------------------------------------------------------------
	// Office Open XML形式に対応したPOIの概要と環境構築
	// http://codezine.jp/article/detail/4783?p=2
	// POIFS for OLE 2 Documents
	// HSSF for Excel Documents
	// HWPF for Word Documents
	// HSLF for PowerPoint Documents
	// HDGF for Visio Documents
	// HPSF for Document Properties
	// --------------------------------------------------------------------------------------------------------

	public PoiObj(String bookPath) {
		super();
		this.bookPath = bookPath;
		// Excelのワークブックを読み込みます。
		System.out.println("bookPath:" + bookPath);
		try {
			// POIFSFileSystem filein = new POIFSFileSystem(new
			// FileInputStream(bookPath));
			// wkBook = new HSSFWorkbook(filein);
			wkBook = WorkbookFactory.create(new FileInputStream(bookPath));
			enumSheets();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("処理が失敗しました");
		}
	}

	// -------------------------------------------------------------------------
	// シート名の一覧をリスト＆ハッシュマップに落としこむ
	// -------------------------------------------------------------------------
	private void enumSheets() {
		sheets = new ArrayList();
		sheetMap = new HashMap();
		int sheetCount = wkBook.getNumberOfSheets();
		// System.out.println("sheetCount:" + sheetCount);
		for (int i = 0; i < sheetCount; i++) {
			String sheetName = wkBook.getSheetName(i);
			// System.out.println("enum name:" + sheetName);
			sheetMap.put(sheetName, i);
			sheets.add(sheetName);
		}
	}

	private Sheet openTheSheet(String sheetName) {
		if (sheetName.equals("")) {
			sheetName = sheets.get(0);
		} else {
			Integer i = sheetMap.get(sheetName);
			if (i == null) {
				System.out.println("#ERROR シート'" + sheetName + "'は存在しません！！ ");
				// XXX 当該シートが存在しなかったら？？
				return null;
			}
		}
		// シートを読み込みます。

		Sheet sheet = wkBook.getSheet(sheetName);
		return sheet;
	}

	// private HSSFSheet openTheSheet(String sheetName) {
	// if (sheetName.equals("")) {
	// sheetName = sheets.get(0);
	// } else {
	// Integer i = sheetMap.get(sheetName);
	// if (i == null) {
	// System.out.println("#ERROR シート'" + sheetName + "'は存在しません！！ ");
	// // XXX 当該シートが存在しなかったら？？
	// return null;
	// }
	// }
	// // シートを読み込みます。
	// HSSFSheet sheet = wkBook.getSheet(sheetName);
	// return sheet;
	// }

	// -------------------------------------------------------------------------
	// 指定したシートの内容をｃｓｖ出力する
	// -------------------------------------------------------------------------
	public void saveAsCsv(String outPath, String sheetName) {
		try {
			// HSSFSheet sheet = openTheSheet(sheetName);
			Sheet sheet = openTheSheet(sheetName);
			if (sheet == null)
				return;
			int lastRow = sheet.getLastRowNum();
			System.out.println("saveAsCsv@poiObj最終行:" + lastRow);
			StringBuffer buf = new StringBuffer();
			outPath = FileUtil.normarizeIt(outPath);
			Inf_oClosure writer = new EzWriter(outPath);
			writer.setLF("");
			writer.open();
			for (int index = 0; index <= lastRow; index++) {
				Row row = sheet.getRow(index);
				// HSSFRow row = sheet.getRow(index);
				if (row != null) {
					int lastCell = row.getLastCellNum();
					// System.out.println("lastCell:"+lastCell);
					for (int j = 0; j < lastCell; j++) {
						// HSSFCell cell = row.getCell((short) j);
						Cell cell = row.getCell((short) j);
						String val = getCellValue(cell);
						if (val != null)
							buf.append(val);
						buf.append(delimiter);
					}
					writer.write(buf.toString());
					writer.write(LF);
					// System.out.println("rec:" + buf.toString());
					buf.delete(0, buf.length());
				} else {
					// 空行の場合nullになるようだ・・・
					// System.out.println("row == null??? @index:" + index);
				}
			}
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("処理が失敗しました");
		}
	}

	// -----------------------------------------------------------------------------
	// saveAsCsvMod　（ｃｓｖとして保存する・・・拡張版）
	// ※各カラム位置colは0から始まる
	// outPath 出力先パス
	// sheetName 対象シート名
	// srcCol 辞書変換対象カラム
	// srcLen 長さ
	// dic 辞書
	// dstCol 辞書で変換したものを書き込むカラム
	// -----------------------------------------------------------------------------
	public void saveAsCsvMod(String outPath, String sheetName, int srcCol,
			int srcLen, HashMap<String, String> dict, int dstCol,
			Inf_Context context) {
		int lcnt =0;
		Set<String> errorSet = new HashSet();// 重複するエラー”Code Not Found”を集約する
		String dstValue = "";
		String dstCode = "";
		try {
			Sheet sheet = openTheSheet(sheetName);
			// HSSFSheet sheet = openTheSheet(sheetName);
			if (sheet == null)
				return;
			int lastRow = sheet.getLastRowNum();
			System.out.println("saveAsCsvMod@poiObj最終行:" + lastRow);
			StringBuffer buf = new StringBuffer();
			outPath = FileUtil.normarizeIt(outPath);
			Inf_oClosure writer = new EzWriter(outPath);
			writer.setLF("");
			writer.open();
			for (int index = 0; index <= lastRow; index++) {
				// HSSFRow row = sheet.getRow(index);
				Row row = sheet.getRow(index);
				if (row != null) {
					lcnt++;
					int lastCell = row.getLastCellNum();
					System.out.println("lcnt:"+lcnt+" lastCell:"+lastCell);
//					 System.out.println("lastCell:"+lastCell);
					for (int iCol = 0; iCol < lastCell; iCol++) {
						// HSSFCell cell = row.getCell((short) iCol);
						Cell cell = row.getCell((short) iCol);
						String val = getCellValue(cell);
						if(val!=null){
							if (iCol == srcCol) {
								dstValue = "";
								if (val.length() > srcLen) {
									dstCode = val.substring(0, srcLen);
									dstValue = dict.get(dstCode);
									if (dstValue == null) {
										//									context.append("Code Not Found :" + dstCode);
										errorSet.add("Code Not Found :" + dstCode);
										dstValue = "";
									}
								}
							}
						}
						if (iCol == dstCol) {
							val = dstValue;
						}
						if (val != null)
							buf.append(val);
						buf.append(delimiter);
					}
					writer.write(buf.toString());
					writer.write(LF);
					// System.out.println("rec:" + buf.toString());
					buf.delete(0, buf.length());
				} else {
					// 空行の場合nullになるようだ・・・
					// System.out.println("row == null??? @index:" + index);
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("処理が失敗しました");
		}
		List<String> errorList = new ArrayList(errorSet);
		Collections.sort(errorList);
		for (String element : errorList) {
			context.append(">>" + element);

		}

	}

	// -------------------------------------------------------------------------
	// 指定したシートの指定カラムの内容をリストにして返す
	// カラム位置colは0から始まる
	// -------------------------------------------------------------------------
	public List getList(String sheetName, int col, int offSet) {
		List<String> list = new ArrayList();
		try {
			// HSSFSheet sheet = openTheSheet(sheetName);
			Sheet sheet = openTheSheet(sheetName);
			if (sheet == null)
				return null;
			int lastRow = sheet.getLastRowNum();
			System.out.println("getList@poiObj最終行:" + lastRow);
			for (int index = offSet; index <= lastRow; index++) {
				// HSSFRow row = sheet.getRow(index);
				Row row = sheet.getRow(index);
				if (row != null) {
					int lastCell = row.getLastCellNum();
					// System.out.println("lastCell:"+lastCell);
					if (col < lastCell) {
						// HSSFCell cell = row.getCell((short) col);
						Cell cell = row.getCell((short) col);
						String val = getCellValue(cell);
						if (val != null)
							list.add(val);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("処理が失敗しました");
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// 指定したシートの指定カラムの内容をハッシュマップにして返す
	// ※各カラム位置colは0から始まる
	// sheetName 処理の対象とするシート
	// keyCol 　キーとするカラム位置
	// valCol　　　値とするカラムの位置
	// offSet　　　読み込み開始行（何行目からを処理対象とするか、を指定する）
	// -------------------------------------------------------------------------
	public HashMap<String, String> getHmap(String sheetName, int keyCol,
			int valCol, int offSet) {
		HashMap<String, String> hmap = new HashMap();
		try {
			// HSSFSheet sheet = openTheSheet(sheetName);
			Sheet sheet = openTheSheet(sheetName);
			if (sheet == null)
				return null;
			int lastRow = sheet.getLastRowNum();
			System.out.println("getHmap@poiObj最終行:" + lastRow);
			for (int index = offSet; index <= lastRow; index++) {
				// HSSFRow row = sheet.getRow(index);
				Row row = sheet.getRow(index);
				if (row != null) {
					int lastCell = row.getLastCellNum();
					if (keyCol <= lastCell && valCol <= lastCell) {
						// HSSFCell keyCell = row.getCell((short) keyCol);
						// HSSFCell valCell = row.getCell((short) valCol);
						Cell keyCell = row.getCell((short) keyCol);
						Cell valCell = row.getCell((short) valCol);
						String keyVal = getCellValue(keyCell);
						String valVal = getCellValue(valCell);
						if (keyVal != null && valVal != null) {
							hmap.put(keyVal, valVal);
							System.out.println(
									" keyVal:" + keyVal + " valVal:" + valVal);
						}
					} else {
						System.out.println("lastCell:" + lastCell);
						System.out.println("keyCol:" + keyCol);
						System.out.println("valCol:" + valCol);
						System.out.println("パラメータのレンジがセルの幅とマッチしないので処理を中断しました");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("処理が失敗しました");
		}
		return hmap;
	}

	// -------------------------------------------------------------------------
	// セルの型判定をして、文字列として返す
	// -------------------------------------------------------------------------
	private String getCellValue(Cell cell) {
		String rtn = null;
		if (cell != null) {
			int type = cell.getCellType();
			switch (type) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				// 小数点以下が存在する場合を考慮しているつもり
				double dNum = cell.getNumericCellValue();
				int iNum = (int) dNum;
				if ((dNum - iNum) > 0) {
					rtn = String.valueOf(dNum);
				} else {
					rtn = String.valueOf(iNum);
				}
				break;
			case HSSFCell.CELL_TYPE_STRING:
				String str = cell.getStringCellValue();
				rtn = str;
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				boolean bool = cell.getBooleanCellValue();
				rtn = String.valueOf(bool);
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				break;
			default:
				System.out.println("type??:" + type);
				break;
			}
		}
		return rtn;
	}

	// public static void testRead(String path) {
	// try {
	// // Excelのワークブックを読み込みます。
	// POIFSFileSystem filein = new POIFSFileSystem(new FileInputStream(
	// path));
	// HSSFWorkbook wkBook = new HSSFWorkbook(filein);
	//
	// int sheetCount = wkBook.getNumberOfSheets();
	// System.out.println("sheetCount:" + sheetCount);
	// String sheetName = "";
	// for (int i = 0; i < sheetCount; i++) {
	// sheetName = wkBook.getSheetName(i);
	// System.out.println("enum name:" + sheetName);
	// }
	//
	// // シートを読み込みます。
	// HSSFSheet sheet = wkBook.getSheet(sheetName);
	// int lastRow = sheet.getLastRowNum();
	// System.out.println("lastRow:" + lastRow);
	// String delimiter = "@";
	// StringBuffer buf = new StringBuffer();
	// for (int index = 0; index < lastRow; index++) {
	// HSSFRow row = sheet.getRow(index + 1);
	// int lastCell = row.getLastCellNum();
	// // System.out.println("lastCell:"+lastCell);
	// for (int j = 0; j < lastCell; j++) {
	// HSSFCell cell = row.getCell((short) j);
	// if (cell != null) {
	// int type = cell.getCellType();
	// // System.out.println("type:"+type);
	// if (type == 1) {
	// String str = cell.getStringCellValue();
	// buf.append(str);
	// } else {
	// // 小数点以下が存在する場合はどうするか？！
	// int num = cell.getCellNum();
	// buf.append(String.valueOf(num));
	// }
	// } else {
	// // XXX　どんなときNULLなんだろう？
	// buf.append("NULL");
	// }
	// buf.append(delimiter);
	// }
	// System.out.println("rec:" + buf.toString());
	// buf.delete(0, buf.length());
	// }
	//
	// // //3行目の値を読み込みます。
	// // HSSFRow row = sheet.getRow(2);
	// // //getStringCellValueにて文字列を読み込みます。
	// // HSSFCell cell = row.getCell((short) 1);
	// // String shopName = cell.getStringCellValue();
	// // //getDateCellValueにて日付を読み込みます。
	// // cell = row.getCell((short) 2);
	// // Date inputDate = cell.getDateCellValue();
	// // //6行目から17行目の値を読み込みます。
	// // int sum = 0; //合計金額を保存する変数
	// // for (int i = 5; i <= 16; i++) {
	// // //getNumericCellValueにて数字を読み込みます。
	// // row = sheet.getRow(i);
	// // cell = row.getCell((short) 2);
	// // sum = sum + (int) cell.getNumericCellValue();
	// // }
	// // //Excelから読み込んだ結果を出力します。
	// // System.out.println(shopName + "の年間売上は"
	// // + sum + "円です。");
	// // System.out.println(new SimpleDateFormat
	// // ("yyyy/MM/dd").format(inputDate) + "入力" );
	// } catch (Exception e) {
	// e.printStackTrace();
	// System.out.println("処理が失敗しました");
	// }
	// }

}