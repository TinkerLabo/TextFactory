package kyPkg.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import globals.ResControl;
import kyPkg.uFile.DosEmu;
import kyPkg.uFile.FileUtil;

// -----------------------------------------------------------------------------
// �w�肳�ꂽ�t�@�C�����c�����ɘA���o�͂���
// -----------------------------------------------------------------------------
public class TexMerge_V {
	private kyPkg.filter.EzWriter writer;
	private String outPath;
	private List<String> pathList;
	boolean separator = false;
	boolean trim = true;
	boolean sort = true;
	private String rec;
	// -----------------------------------------------------------------------------
	// �R���X�g���N�^
	// -----------------------------------------------------------------------------
	public TexMerge_V(String outPath, String inDir, String regex) {
		this(outPath, DosEmu.pathList2List(inDir, regex));
	}

	public TexMerge_V(String outPath, List<String> pathList) {
		this.outPath = outPath;
		this.pathList = pathList;
	}
	// -----------------------------------------------------------------------------
	// �t�@�C��������؂�Ƃ��đ}������H���ǂ���
	// -----------------------------------------------------------------------------
	public void setSeparator(boolean separator) {
		this.separator = separator;
	}

	// -----------------------------------------------------------------------------
	// �t�@�C�������Ƀ\�[�g������Ō�������H���ǂ���
	// -----------------------------------------------------------------------------
	public void setSort(boolean sort) {
		this.sort = sort;
	}

	// -----------------------------------------------------------------------------
	// ��s���폜����H���ǂ���
	// -----------------------------------------------------------------------------
	public void setTrim(boolean trim) {
		this.trim = trim;
	}



	public int execute() {
		writer = new kyPkg.filter.EzWriter(outPath);
		writer.open();
		if (sort == true)
			Collections.sort(pathList);
		for (String path : pathList) {
			System.out.println("=>" + path);
			if (separator == true)
				writer.write("<<<<<" + path + ">>>>>");
			try {
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(path));
				rec = br.readLine();
				while (rec != null) {
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
		TexMerge_V ins = new TexMerge_V(outPath, inDir, regex);
		ins.execute();
	}

}
