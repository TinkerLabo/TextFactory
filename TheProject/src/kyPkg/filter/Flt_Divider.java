package kyPkg.filter;

import kyPkg.task.Abs_BaseTask;

//import kyPkg.rez.ITP_Model;

// 2009-06-16 yuasa
public class Flt_Divider extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private String outDir = "";

	private String outFile = "";

	private String userId = "";

	private Inf_iClosure reader = null;

	private Inf_oClosure writer = null;

	private Inf_FileConverter converter = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Divider(Inf_FileConverter converter, String outDir, String inPath) {
		this.converter = converter;
		this.outDir = outDir;
		this.reader = new EzReader(inPath);
	}

	public Flt_Divider(String outDir, String inPath) {
		this(null, outDir, inPath);
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");

		String outPath = "";
		long wCnt = 0;
		reader.open();
		// ---------------------------------------------------------------------
		// incore
		// ---------------------------------------------------------------------
		while (reader.readLine() != null) {
			String wRec = reader.getCurrent();
			if (wRec.startsWith("@@")) {
				closenConvert(outPath);
				String[] wSplited = wRec.substring(2).split("\t");
				if (wSplited.length >= 2) {
					outFile = wSplited[0].trim();
					userId = wSplited[1].trim() + "/";
				} else {
					outFile = wSplited[0].trim();
					userId = "";
				}
				outPath = outDir + userId + outFile;
				writer = new EzWriter(outPath);
				writer.open();
			} else {
				wCnt++;
				writer.write(wRec);
			}
		}
		reader.close();
		closenConvert(outPath);
		// if (writer != null)
		// writer.close();
	}

	private void closenConvert(String it2Path) {
		if (!it2Path.equals("")) {
			if (writer != null)
				writer.close();
			if (converter != null)
				converter.fileConvert(outDir + userId, it2Path);
		}
	}

 
}
