package kyPkg.filter;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.tools.Onbiki;
import kyPkg.uFile.FileUtil;

public class Onbiki_Cnv implements Inf_ArrayCnv {
	private String encoding;

	//
	public Onbiki_Cnv(String encoding) {
		super();
		this.encoding = encoding;
	}

	@Override
	public void init() {
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] rec, int stat) {
		for (int i = 0; i < rec.length; i++) {
			rec[i] = Onbiki.cnv2Similar(rec[i], encoding);
		}
		return rec;
	}

	@Override
	public void fin() {
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test();
	}

	// -------------------------------------------------------------------------
	// 単体テスト　
	// -------------------------------------------------------------------------
	public static void test() {
		System.out.println("#start#");
		String inPath = "C:/@QPR/enc_UTF8.txt";
		String outPath = "C:/@QPR/result.txt";
		
		Inf_ArrayCnv converter = new Onbiki_Cnv(FileUtil.MS932);
		
		EzReader reader = new EzReader(inPath, FileUtil.UTF8);
		EzWriter writer = new EzWriter(outPath, FileUtil.MS932);
//		writer.setConverter(converter);
		new Common_IO(writer, reader).execute();
		System.out.println("#fin#");
	}

}
