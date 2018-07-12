package kyPkg.filter;

import java.io.File;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.DosEmu;

public class DirIterate extends Abs_BaseTask {
	private Inf_FileConverter converter = null;
	private String outDir = "";
	private String inDir = "";
	private String pattern = "";

	// ------------------------------------------------------------------------
	// ファイル名の正規表現
	// ------------------------------------------------------------------------
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	// ------------------------------------------------------------------------
	// 入力対象ファイルのパターン（拡張子など）を指定する
	// ------------------------------------------------------------------------
	public void setExt(String pattern) {
		this.pattern = "*." + pattern;
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public DirIterate(Inf_FileConverter converter, String outDir, String inDir) {
		this.converter = converter;
		this.outDir = outDir;
		this.inDir = inDir;
		new File(outDir).mkdir();
	}

	public DirIterate(Inf_FileConverter converterIns, String outDir,
			String inDir, String pattern) {
		this(converterIns, outDir, inDir);
		this.pattern = pattern;
	}

	@Override
	public void execute() {
		super.setMessage("DirIterate開始");
		String[] array = DosEmu.getFileArray(inDir, pattern);
		for (int i = 0; i < array.length; i++) {
			converter.fileConvert(outDir, array[i]);
		}
	}

	public static void main(String[] args) {
//		Itp_Parse.itpMassConvertBatch();
	}

}
