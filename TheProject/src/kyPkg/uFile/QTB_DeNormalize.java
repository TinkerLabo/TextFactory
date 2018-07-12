package kyPkg.uFile;

import java.util.List;

import kyPkg.task.TaskSQL2File;
import kyPkg.uDateTime.DateCalc;
import kyPkg.util.CnvArray;

//アンケート用の『alias.txt』およびパラメータファイル『qtb1.txt』を使って、
//値を辞書変換したテキストファイルを書き出す

public class QTB_DeNormalize extends SourceObj {
	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	// QTBファイルをベースにフィールド定義、値⇒ラベル変換辞書の生成を行う
	// ------------------------------------------------------------------------
	public QTB_DeNormalize(String sourceDir) {
		super();
		System.out.println("# @QTB_DeNormalize # sourceDir:" + sourceDir);
		String keyName = "モニターＩＤ";
		super.setSourceDir(sourceDir);
		super.setKeyName(keyName);
		super.incore(getSourceDir());
	}

	public String createCommmonSQL() {
		return "";
	}

	public String qtb2Text(String outPath, String befYmd, String aftYmd, String delimiter ) {
		if (selectedFields == null)
			selectAllFields();//ここですべてのフィールドを設定しているの（これ以降任意のフィールドを指定できれば良さそう）
		//存在しないフィールドが指定された場合にも変な動きをしないようにする工夫が必要！！
		String alter = getTableAlterName();
		String table = getTable();
		String key = getKey();
		String fields = getFields();
		CnvArray cnv = getConverter();
		List<String> selectedFields = getSelectedFields();
		String sql = "SELECT " + fields + " FROM " + table + " as " + alter;
		// --------------------------------------------------------------------
		// 期間内有効モニターに限定するかどうか
		// --------------------------------------------------------------------
		boolean isValidMon = true;
		String validMonCnd = "";
		if (isValidMon) {
			validMonCnd = " WHERE(" + alter + "." + "XK2" + " <= " + befYmd
					+ " ) AND (" + alter + "." + "XK3" + " >= " + aftYmd
					+ " ) AND (" + alter + "." + "XK2" + " <> 0) ";
			sql = sql + validMonCnd;
		}
//		System.out.println("############### qtb2Text sql:" + sql);
		TaskSQL2File task = new TaskSQL2File(outPath, sql);
		task.setConverter(cnv); 				// 変換辞書設定
		task.setHeader(selectedFields, delimiter); 	// ヘッダーファイルを追加
		task.setDelimiter(delimiter);
		task.execute();
//		System.out.println("qtb2Text end");
		return sql;
	}

	//TODO　サンプルデータをマトメテ保管＆管理するべし
	// ------------------------------------------------------------------------
	// 例＞モニター属性ファイルを出力、項目を非正規化して出力する　　（forIizuka20141016）
	// ------------------------------------------------------------------------
	public static void denormalizeIt() {
		String befYmd = "20170109";
		String aftYmd = "20170319";
		int nendo = DateCalc.getNendo(aftYmd);// 集計対象期間より　『年度』　を決定する
//		String qtbDir = ResControl.getQtbDir(nendo);//"c:/samples/QTB_SAMPLE";// ｑｔｂファイルが入っているデイレクトリのパス
		String qtbDir = "Z:/S2/rx/enquetes/NQ/03_属性・性年代編/2016/";
		String outPath = "c:/samples/QTB_SAMPLE/Attr.txt";
		QTB_DeNormalize ins = new QTB_DeNormalize(qtbDir);
		ins.loadSelectedFields("c:/selectedFields.txt"); //出力対象項目を読み込む
		ins.qtb2Text(outPath, befYmd, aftYmd, "\t");
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		denormalizeIt();
	}
}
