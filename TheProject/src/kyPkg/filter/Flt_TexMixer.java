package kyPkg.filter;

import static kyPkg.uFile.ListArrayUtil.*;

import java.util.Iterator;
import java.util.List;

import globals.ResControl;
import kyPkg.task.Abs_BaseTask;

// 2010-11-10 マージ yuasa(Template4をベースにしてある)
public class Flt_TexMixer extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private List<String> pathList = null;
	private Inf_oClosure writer = null;
	private static final String doneExt = "old";
	// -------------------------------------------------------------------------
	// コンストラクタ
	// String outPath 出力ファイルのパス
	// String dataDir　検索対象ディレクトリのパス
	// String regex　対象抽出用正規表現
	// String doneExt　処理済入力ファイルの拡張子（変更先）
	// -------------------------------------------------------------------------
	public Flt_TexMixer(String outPath, String dataDir, String regex) {
		this(outPath, dir2ListWithDir(dataDir, regex));
	}
	public Flt_TexMixer(String outPath, List<String> list) {
		this.pathList = list;
		this.writer = new EzWriter(outPath);
	}
	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");
		System.out.println("Flt_TexMixer: start");
		writer.open();
		Inf_iClosure reader = null;
		// リストに指定された複数のファイルを読み込み、出力ファイルに継ぎ足す
		if (pathList == null) {
			System.out.println("該当するファイルは存在しない為処理を中断しました");
			return;
		}

		for (String inPath : pathList) {
			System.out.println("inPath:" + inPath);
			reader = new EzReader(inPath);
			reader.open();
			while (writer.write(reader))
				;
			reader.close();

		}
		// for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
		// String inPath = (String) iterator.next();
		// }
		writer.close();
		if (!doneExt.equals("")) {
			for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
				String inPath = (String) iterator.next();
				// 拡張子を変更してマージ済みであることを示す。
				kyPkg.uFile.FileUtil.renExt(inPath, doneExt);
			}
		}
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String inDir = ResControl.D_DRV + "k14x/Ftp/生協更新データ/data/";
		// "SYOMSDT_"または"kobe"で始まり"文字列"がそれに続き".txt"で終わるパターン
		String regex = "SYOMSDT_|kobe\\S*\\.txt";
		String outPath = inDir + "result.dat";
		new Flt_TexMixer(outPath, inDir, regex).execute();
	}
}
