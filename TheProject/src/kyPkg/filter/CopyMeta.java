package kyPkg.filter;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.YamlControl;

// 2010-04-28 yuasa
public class CopyMeta extends Abs_BaseTask {
	private String inPath;
	private String outPath;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// ※ファイルの拡張子を除いた部分にメタデータの拡張子をつけたものが処理対象となる
	// -------------------------------------------------------------------------
	public CopyMeta(String outPath, String inPath) {
		this.inPath = inPath;
		this.outPath = outPath;
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");
		// HashMap<String, String> infoMap = new
		// YamlControl(inPath).getInfoMap();
		// new YamlControl(infoMap).saveAs(outPath);

		YamlControl ymlCtrl = new YamlControl(inPath);
		ymlCtrl.saveAs(outPath);

	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String userDir = globals.ResControl.getQPRHome();

		String inPath = userDir +"306308000010/tran/20100101_20100331.dmy";
		String outPath = userDir +"20100101_20100331.dmy";
		new CopyMeta(outPath, inPath).execute();
	}
}
