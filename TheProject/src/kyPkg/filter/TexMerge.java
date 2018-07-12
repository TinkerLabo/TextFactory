package kyPkg.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import globals.ResControl;
import kyPkg.uFile.DosEmu;
import kyPkg.uFile.FileUtil;

public class TexMerge {
	private kyPkg.filter.EzWriter writer;

	private String outPath;

	private List pathList;

	boolean separator = false;

	boolean trim = true;

	boolean sort = true;

	// -----------------------------------------------------------------------------
	// ファイル名を区切りとして挿入する？かどうか
	// -----------------------------------------------------------------------------
	public void setSeparator(boolean separator) {
		this.separator = separator;
	}

	// -----------------------------------------------------------------------------
	// ファイル名順にソートした上で結合する？かどうか
	// -----------------------------------------------------------------------------
	public void setSort(boolean sort) {
		this.sort = sort;
	}

	// -----------------------------------------------------------------------------
	// 空行を削除する？かどうか
	// -----------------------------------------------------------------------------
	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	// -----------------------------------------------------------------------------
	// 指定したディレクトリ以下のファイルをひとつのファイルにまとめる
	// -----------------------------------------------------------------------------
	public TexMerge(String outPath, String inDir, String regex) {
		this(outPath, DosEmu.pathList2List(inDir, regex));
	}

	public TexMerge(String outPath, List pathList) {
		this.outPath = outPath;
		this.pathList = pathList;
	}

	public int execute() {
		writer = new kyPkg.filter.EzWriter(outPath);
		writer.open();
		if (sort == true)
			Collections.sort(pathList);
		for (Iterator iter = pathList.iterator(); iter.hasNext();) {
			String path = (String) iter.next();
			System.out.println("=>" + path);
			if (separator == true)
				writer.write("<<<<<" + path + ">>>>>");
			try {
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(path));
				String rec = "";
				rec = br.readLine();
				while (rec != null) {
					// System.out.println("=>" + rec);
					if (trim == true) {
						rec = rec.trim();
						if (!rec.equals(""))
							writer.write(rec);
					} else {
						writer.write(rec);
					}
					rec = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		writer.close();
		return 0;
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test00();
	}

	public static void test00() {
		String outPath = ResControl.D_DRV + "preMix/test.mix";
		String inDir = ResControl.D_DRV + "preMix/";
		String regex = "*.txt";
		TexMerge ins = new TexMerge(outPath, inDir, regex);
		ins.execute();
	}

}
