package kyPkg.filter;

import java.util.List;

import kyPkg.converter.Inf_StatConverterM;

public class EzWriterStatConv extends EzWriterBase   {
	private Inf_StatConverterM converterM = null; // 入力が1系統のコンバータ
	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public EzWriterStatConv(String path, Inf_StatConverterM converterM) {
		super(path);
		this.converterM = converterM;
	}
	// ------------------------------------------------------------------------
	// open
	// ------------------------------------------------------------------------
	@Override
	public boolean open(boolean append) {
		 if (converterM != null)
			 converterM.init();
		return super.open(append);
	}
	// ------------------------------------------------------------------------
	// close
	// ------------------------------------------------------------------------
	@Override
	public synchronized void close() {
		if (converterM != null)
			converterM.fin();
	}
	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	@Override
	public boolean write(Inf_iClosure reader) {
		return write(reader.readSplited(), (int) writeCount);
	}
	// ------------------------------------------------------------------------
	// 入力が１系統の場合
	// ------------------------------------------------------------------------
	@Override
	public boolean write(String[] splited, int stat) {
		if (splited == null)
			return false;
		if (converterM != null) {
			List<String> list = converterM.convert(splited, stat);
			for (String rec : list) {
				return super.write(rec);
			}
			return true;
		} else {
			return super.write(splited);
		}
	}

	public static void main(String[] argv) {
	}
	@Override
	public boolean write(List splited, int stat) {
		return false;
	}
}
