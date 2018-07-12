package kyPkg.util;

//　kyPkg.util.BufferCtrl bCtr = new kyPkg.util.BufferCtrl(delimiter);
public class BufferCtrl {
	private String LF = System.getProperty("line.separator");
	private String delimiter = "\t";
	private StringBuffer buf = null;
	// -------------------------------------------------------------------------
	// StringBufferを使った汎用ロジック
	// -------------------------------------------------------------------------
	// 《使用例》
	// List<String> keyList = kyPkg.util.KUtil.cnvHmap2KeyList(keyMap);
	// String delimiter = "\t";
	// kyPkg.util.BufferCtrl bCtr = new kyPkg.util.BufferCtrl(delimiter);
	// for (String key : keyList)
	// bCtr.append(key);
	// bCtr.flush(writer);
	// -------------------------------------------------------------------------
	public BufferCtrl(String delimiter) {
		super();
		this.delimiter = delimiter;
		buf = new StringBuffer();
	}
	// XXX　毎回buf.length()をチェックするオーバーヘッドを避けたい気もする
	public void append(String val) {
		if (buf.length() > 0)
			buf.append(delimiter);
		buf.append(val);
	}

	public void append() {
		buf.append(delimiter);
	}

	public void flush(java.io.Writer writer, String val) throws Exception {
		append(val);
		flush(writer);
	}

	public void flush(java.io.Writer writer) throws Exception {
		writer.write(buf.toString());
		writer.write(LF);
		buf.delete(0, buf.length());
	}
}
