package kyPkg.filter;

import java.util.ArrayList;

import kyPkg.task.Abs_BaseTask;

// 2009-08-06 yuasa CSVファイルのXY軸を取り替える（256セル以上あるものが見たいという案件＞藤居）
public class Flt_XYConv extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private int maxCel =0;
	private Inf_iClosure reader = null;

	private Inf_oClosure writer = null;

	private String delimiter = null;

	private ArrayList<String[]> list;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_XYConv(String outPath, String inPath) {
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath);
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");

		
		list = new ArrayList();
		long wCnt = 0;
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = null;
		// ---------------------------------------------------------------------
		// incore
		// ---------------------------------------------------------------------
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				splited = reader.getSplited();
				if(maxCel<splited.length)maxCel=splited.length;
//				System.out.println("maxCel>"+maxCel);
				list.add(splited);
				wCnt++;
			}
		}
		reader.close();
		System.out.println("maxCel>"+maxCel);
		// ---------------------------------------------------------------------
		// output
		// ---------------------------------------------------------------------
		writer.open();
		StringBuffer buf = new StringBuffer();
		if (wCnt > 0) {
			for (int index = 0; index < maxCel; index++) {
				buf.delete(0, buf.length());
				for (String[] data : list) {
//					System.out.println("index>"+index);
//					System.out.println("data.length>"+data.length);

					if (data.length>index){
						buf.append(data[index]);
					}
					buf.append(delimiter);
				}
				writer.write(buf.toString());
			}
		}
		writer.close();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}
	public static void test1() {
		String rootDir = globals.ResControl.getQprRootDir();
		String inPath = rootDir+"SBSPC86A.PRN";
		String outPath = rootDir+"SBSPC86A.txt";
		new Flt_XYConv(outPath, inPath).execute();
	}

}
