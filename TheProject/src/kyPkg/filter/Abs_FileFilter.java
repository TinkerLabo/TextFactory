package kyPkg.filter;

import java.io.*;
import java.util.Arrays;

interface If_FileFilter {
	public boolean execute();
}

public abstract class Abs_FileFilter implements If_FileFilter {
	private Common_OCmp oComp;
	private Common_ICmp iComp;
	protected static final String LS = System.getProperty("line.separator");

	public Abs_FileFilter(boolean debug) {
	}

	// -------------------------------------------------------------------------
	// 入力ストリームをリーダーとする場合
	// Reader in = new FileReader(inFile);
	// xxx.setReader(in);
	// -------------------------------------------------------------------------
	// Stringをストリーム扱いにしてラインリードする場合
	// StringReader in = new StringReader(jTa1.getText());
	// -------------------------------------------------------------------------
	public void open_I(String inPath) {
		iComp = new Common_ICmp(inPath, true);
	}
	public void open_I(String inPath, boolean option) {
		iComp = new Common_ICmp(inPath, option);
	}
	public void open_O() {
		oComp = new Common_OCmp();
	}
	public void open_O(String outPath, boolean append) {
		String inPath = "";
		if (iComp != null)
			inPath = iComp.getInPath();
		oComp = new Common_OCmp(Arrays.asList(new String[] { outPath }),
				inPath, append);
	}
	public void open_O(String[] array, boolean append) {
		String inPath = "";
		if (iComp != null)
			inPath = iComp.getInPath();
		oComp = new Common_OCmp(Arrays.asList(array), inPath, append);
	}
	// -------------------------------------------------------------------------
	// ファイルを閉じる
	// -------------------------------------------------------------------------
	public void close() {
		close_I();
		close_O();
	}
	public void close_I() {
		iComp.close();
	}
	public void close_O() {
		oComp.close();
	}
	
	protected Writer getWriter() {
		return oComp.getWriter(0);
	}
	public Writer getWriter(int index) {
		return oComp.getWriter(index);
	}

//	protected String getOutDir(int index) {
//		return oComp.getOutDir(index);
//	}

	// -------------------------------------------------------------------------
	// 出力先を追加する（ユニークなインデックスが返る）
	// -------------------------------------------------------------------------
	public int addOutFile(String outPath, boolean append) {
		return oComp.addOutFile(outPath, append);
	}


	protected String getOutFile() {
		return oComp.getOutPath(0);
	}

	public String getString() {
		return oComp.getString();
	}

	protected Reader getReader() {
		return iComp.getReader();
	}

	protected void setDelimiter(String delimiter) {
		iComp.setDelimiter(delimiter);
	}

	protected String getDelimiter() {
		return iComp.getDelimiter();
	}

	protected void setStat(String stat) {
		oComp.setStat(stat);
	}

	public String getStat() {
		return oComp.getStat();
	}

}