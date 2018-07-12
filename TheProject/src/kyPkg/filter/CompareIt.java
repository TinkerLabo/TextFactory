package kyPkg.filter;

import globals.ResControl;
import kyPkg.uFile.FileUtil;

public class CompareIt implements Inf_FileConverter {
	private String chkDir;

	private int sameCount = 0;

	private int diffCount = 0;

	private int nof1Count = 0;

	private int nof2Count = 0;

	private int emptyCount = 0;

	public CompareIt(String chkDir) {
		chkDir = FileUtil.mkdir(chkDir);
		this.chkDir = chkDir;
	}

	public void info() {
		System.out.println("sameCount :" + sameCount);
		System.out.println("diffCount :" + diffCount);
		System.out.println("nof1Count :" + nof1Count);
		System.out.println("nof2Count :" + nof2Count);
		System.out.println("emptyCount:" + emptyCount);
	}

	@Override
	public void fileConvert(String outDir, String inPath) {
		String fileName = FileUtil.getFileName(inPath);
		String chkPath = chkDir + fileName;
		int result = new kyPkg.tools.Compare(chkPath, inPath, 10, false).compare();
		if (result > 0) {
			System.out.println("違っている！！:" + inPath);
			diffCount++;
		}
		if (result == 0) {
			System.out.println("同じ！！:" + inPath);
			sameCount++;
		}
		if (result == -1) {
			nof1Count++;
		}
		if (result == -2) {
			nof2Count++;
		}
		if (result == -3) {
			emptyCount++;
		}
	}

	// -------------------------------------------------------------------------
	// 小さい集団をdir1に指定する
	// dir1と同じファイルをdir2に探しに行き、内容を比較する・・・dir3は今のところ未使用
	// -------------------------------------------------------------------------
	public static void dirIter() {
		String dir1 = "J:/ejqp7/qpr_buying_data";
		String dir2 = ResControl.D_DRV + "eclipse/workspace/kyProject/●重要●/all";
		String dir3 = ResControl.D_DRV + "dst";
		String regex = "*.txt";
		CompareIt ins = new CompareIt(dir2);
		kyPkg.filter.DirIterate insConverter = new kyPkg.filter.DirIterate(ins,
				dir3, dir1, regex);
		insConverter.execute();
		System.out.println("** finish! **");
		ins.info();
	}

	public static void main(String[] args) {
		dirIter();
	}
}
