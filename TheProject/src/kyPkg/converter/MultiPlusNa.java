package kyPkg.converter;

public class MultiPlusNa implements kyPkg.converter.Inf_StrConverter {
	private StringBuffer buff = null;
	private int size = 0;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
		buff = new StringBuffer(size + 1);
	}
	public MultiPlusNa() {
		setSize(0);
	}
	public MultiPlusNa(int size) {
		setSize(size);
	}
	// -------------------------------------------------------------------------
	// 列の先頭にＮＡを作成する
	// XXX サイズ調整させたほうが良いか？（パフォーマンス次第かな）
	// -------------------------------------------------------------------------
	@Override
	public String convert(String val) {
		buff.delete(0, buff.length());
		if (val.trim().equals("")) {
			 buff.append("1"); // こっちが正しい！
//			buff.append("@"); // for Debug
		} else {
			 buff.append(" "); // こっちが正しい！
//			buff.append("@"); // for Debug
		}
		buff.append(val);
		return buff.toString();
	}

}
