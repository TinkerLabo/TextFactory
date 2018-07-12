package kyPkg.filter;

import kyPkg.converter.DCnvDefault;

public class EzWriterDual extends EzWriter {
	private Inf_DualConverter dualConverter = null; // 入力が2系統あるコンバータ

	public void setDualConverter(Inf_DualConverter dualConverter) {
		this.dualConverter = dualConverter;
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public EzWriterDual(String path) {
		super(path);
	}

	public EzWriterDual(String path, Inf_DualConverter converterD) {
		super(path);
		this.dualConverter = converterD;
	}

	// ------------------------------------------------------------------------
	// open
	// ------------------------------------------------------------------------
	@Override
	public boolean open(boolean append) {
		if (dualConverter != null)
			dualConverter.init();
		return super.open(append);
	}

	// ------------------------------------------------------------------------
	// close
	// ------------------------------------------------------------------------
	@Override
	public synchronized void close() {
		if (dualConverter != null)
			dualConverter.fin();
		super.close();
	}

	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	@Override
	public boolean write(Inf_iClosure reader) {
		return write(reader.readSplited(), (int) writeCount);
	}

	// ------------------------------------------------------------------------
	// 入力が2系統の場合
	// ------------------------------------------------------------------------
	public boolean write(String[] splited_L, String[] splited_R, int stat) {
		if (dualConverter == null)
			dualConverter = new DCnvDefault(delimiter);
		return super.write(dualConverter.convert(splited_L, splited_R, stat));
	}

}
