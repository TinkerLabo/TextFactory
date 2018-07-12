package kyPkg.filter;
import static kyPkg.uFile.ListArrayUtil.*;

import java.util.Iterator;
import java.util.List;

import kyPkg.task.Abs_BaseTask;


// 2010-11-10 マージ yuasa
public class Flt_Template4 extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private List pathList = null;
	private Inf_oClosure writer = null;
	private String doneExt = "";
	// -------------------------------------------------------------------------
	// コンストラクタ
	//	String outPath 出力ファイルのパス
	//	String dataDir　検索対象ディレクトリのパス
	//	String regex　対象抽出用正規表現
	//	String doneExt　処理済入力ファイルの拡張子（変更先）
	// -------------------------------------------------------------------------
	public Flt_Template4(String outPath, String dataDir, String regex,String doneExt) {
		this(outPath, dir2ListWithDir(dataDir, regex),doneExt);
	}
	public Flt_Template4(String outPath, List<String> list, String doneExt) {
		this.pathList = list;
		this.writer = new EzWriter(outPath);
		this.doneExt = doneExt.trim();
	}
	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("## Start ##");
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();

		writer.open();
		Inf_iClosure reader = null;
		// リストに指定された複数のファイルを読み込み、出力ファイルに継ぎ足す
		if (pathList == null) {
			System.out.println("該当するファイルは存在しない為処理を中断しました");
			return;
		}

		for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
			String inPath = (String) iterator.next();
			System.out.println("inPath:" + inPath);
			reader = new EzReader(inPath);
			reader.open();
			while (writer.write(reader))
				;
			reader.close();
		}
		writer.close();
		if (!doneExt.equals("")) {
			for (Iterator iterator = pathList.iterator(); iterator.hasNext();) {
				String inPath = (String) iterator.next();
				// 拡張子を変更してマージ済みであることを示す。
				kyPkg.uFile.FileUtil.renExt(inPath, doneExt);
			}
		}
		elapse.stop();

	}
	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}
	public static void test1() {
		String inDir = "c:/mixed/";
		String outPath = "c:/mixed/result.dat";
		String regex = "\\S*\\.txt";
		new Flt_Template4(outPath, inDir, regex, "").execute();
	}
}
