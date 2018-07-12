package kyPkg.etc;

import static kyPkg.uFile.FileUtil.getExt;
import static kyPkg.uFile.FileUtil.getPreExt;
import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Guess;
import kyPkg.filter.Guess4CreateTable;
import kyPkg.filter.Guess4Isam;
import kyPkg.filter.Guess4Sqlite;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.task.Abs_BaseTask;
import kyPkg.util.Msgbox;

// 2014-05-09 yuasa データファイルから簡易レイアウトを作る（便利）
//2014-07-17	本格的な使用に堪える？改良
public class Ez_Layout extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public Ez_Layout() {
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
	}

	private static String[] getSampleRec(String samplePath,
			boolean headerOption, int sampleRow) {
		String[] sampleRec = null;
		long count = 0;
		Inf_iClosure reader = new EzReader(samplePath);
		reader.open();
		String delimiter = "\t";
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		// if (headerOption)
		// header = splited;
		while (splited != null) {
			count++;
			if (count == sampleRow) {
				sampleRec = splited;
			}
			splited = reader.readSplited();
		}
		reader.close();
		return sampleRec;
	}

	// ------------------------------------------------------------------------
	// XXX 型の自動判定
	// XXX ｓｑｌの定義文も出力する
	// XXX GUIで操作したい
	// ------------------------------------------------------------------------
	private static void writeIt(String outPath, List<String> fieldNames,
			List<String> fieldTypes, List<String> figures, String[] sampleRec) {
		String delimiter = "\t";
		String[] array = new String[10];
		Inf_oClosure writer = new EzWriter(outPath);
		writer.open();
		array = new String[10];
		writer.write("");
		// --------------------------------------------------------------------
		array = new String[10];
		array[1] = "title:";
		array[2] = "タイトル";
		writer.write(join(array, delimiter));
		// --------------------------------------------------------------------
		array = new String[10];
		array[1] = "file:";
		array[2] = "samplePath";
		writer.write(join(array, delimiter));
		// --------------------------------------------------------------------
		writer.write("");
		// --------------------------------------------------------------------
		array = new String[10];
		array[1] = "Seq";
		array[2] = "Field_Name";
		array[3] = "Type";
		array[4] = "Figure";
		array[5] = "Comment";
		array[6] = "Sample";
		writer.write(join(array, delimiter));
		// --------------------------------------------------------------------
		array = new String[10];
		array[0] = "    ";
		array[1] = "----";
		array[2] = "----------";
		array[3] = "----------";
		array[4] = "----------";
		array[5] = "--------------------";
		array[6] = "--------------------------------------------------";
		writer.write(join(array, delimiter));
		// --------------------------------------------------------------------
		String fieldName = "";
		String fieldType = "";
		String figure = "";
		for (int i = 0; i < sampleRec.length; i++) {
			String element = sampleRec[i];
			if (element == null) {
				element = "";
			} else {
				element = "'" + element + "'";
			}
			String sSeq = String.valueOf(i + 1);
			array = new String[10];
			array[1] = sSeq;
			// if (header != null && header.length > i)
			// fieldName = header[i];
			if (fieldName == null || fieldName.equals(""))
				fieldName = "Field_" + sSeq;
			// if (fieldList.size() > i) {
			// String val = fieldList.get(i);
			// String[] tuple = val.split("\t");
			// if (tuple.length >= 2) {
			// fieldName = tuple[0];
			// type = tuple[1];
			// }
			// }
			fieldName = "__whocare";// フィールド名称
			fieldType = Guess.VARCHAR;// フィールド型
			figure = "";// 桁数
			if (fieldNames.size() > i)
				fieldName = fieldNames.get(i);
			if (fieldTypes.size() > i)
				fieldType = fieldTypes.get(i);
			if (figures.size() > i)
				figure = figures.get(i);
			array[2] = fieldName;
			array[3] = fieldType;
			array[4] = figure;
			array[5] = "";// comment
			array[6] = element;// sample
			writer.write(join(array, delimiter));
		}
		// --------------------------------------------------------------------
		array = new String[10];
		array[0] = "    ";
		array[1] = "----";
		array[2] = "----------";
		array[3] = "----------";
		array[4] = "----------";
		array[5] = "--------------------";
		array[6] = "--------------------------------------------------";
		writer.write(join(array, delimiter));
		writer.write("");
		// --------------------------------------------------------------------
		writer.close();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		boolean headerOption = false;
		goAhead("C:/loy1_head.txt", headerOption);
	}

	public static String goAhead(String inPath) {
		return goAhead(inPath, true);
	}

	public static String goAhead(String inPath, boolean headerOption) {
		String preExtName = getPreExt(inPath);
		String dataPath = preExtName + "_Layout.txt";
		String ext = getExt(inPath);
		String[] sampleRecs = null;
		// --------------------------------------------------------------------
		// ファイルの拡張子がsqlならcreateTable文 、それ以外はローデータと判断
		// --------------------------------------------------------------------
		Guess guess = null;
		if (ext.equals("SQL")) {
			guess = new Guess4CreateTable();
			//sql文の雛形を自動生成する
			guess.analyzeIt(inPath, true);
			// すでにテーブルが存在するものとしてサンプルデータを生成するか？？

		} else {
			//sql文の雛形を自動生成する
			guess = new Guess4Sqlite();
			guess.analyzeIt(inPath, headerOption);
			// 凡例用サンプルデータを配列として用意する
			int sampleRow = 2;
			sampleRecs = getSampleRec(inPath, headerOption, sampleRow);
		}
		List<String> fldNames = guess.getNames();
		List<String> fldTypes = guess.getTypes();
		List<String> fldFigure = guess.getFigures();
		if (sampleRecs == null) {
			// 空のarrayを作る
			sampleRecs = new String[fldNames.size()];
		}
		// --------------------------------------------------------------------
		// 空のファイルの場合fieldListがnullになる
		// --------------------------------------------------------------------
		if (fldNames == null || fldTypes == null) {
			new Msgbox(null).info("ファイルが空です");
			return "";
		}
		// --------------------------------------------------------------------
		// 簡易レイアウト作成
		// --------------------------------------------------------------------
		writeIt(dataPath, fldNames, fldTypes, fldFigure, sampleRecs);
		// new Ez_Layout(bodyPath, fldNames, fldTypes, fldFigure, sampleRecs)
		// .execute();
		// -------------------------------------------------------------------------
		// Guess4Isam schema出力
		// -------------------------------------------------------------------------
		Guess4Isam guess4I = new Guess4Isam();
		guess4I.analyzeIt(inPath, headerOption);
		// -------------------------------------------------------------------------
		// XXX bcp crudのsql&Batchを作成する？
		// template();
		// -------------------------------------------------------------------------
		// sample();
		// XXX　すでに同名のファイルがそんざいしていたらどうなるか検証しておく
		String template = "c:/LayoutTmp.xlsm";// 雛形
		String outPath = preExtName + "ファイル定義.xlsm";// 出力ファイル
		String[] headers = null;
		HashMap<String, String> meta = new HashMap();
		SheetObject sheetObj = new SheetObject(dataPath, meta, headers);
		sheetObj.setSheetName("text");// 出力先シート名
		// -------------------------------------------------------------------------
		List<SheetObject> sheetObjs=new ArrayList();
		sheetObjs.add(sheetObj);
		// -------------------------------------------------------------------------
		Poi4MashUps.send2excel(outPath,  template,  sheetObjs);
		return guess.getSqmpleCode();
	}

	public static void sample() {
		String dbDir = "C:/test/";
		String sql = "SELECT Fld_1,Fld_2,Fld_3,Fld_4,Fld_5,Fld_6,Fld_7,Fld_8,Fld_9,Fld_10 FROM LOY1_HEAD#TXT;";
		String resultPath = dbDir + "result.txt";
		kyPkg.task.Inf_ProgressTask task = kyPkg.etc.CommonMethods
				.queryIsam2File(resultPath, sql, dbDir);
		new kyPkg.task.TaskWatcherNoGUI(task).execute();
	}

}
