package kyPkg.filter;

import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;

public class DummyWriter extends OutClosureBase {
	// -------------------------------------------------------------------------
	// 2014-04-30
	// �g�p�၄ DummyWriter ezWriter = new DummyWriter(arrayConv);
	// -------------------------------------------------------------------------
	private Inf_ArrayCnv arrayConv;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public DummyWriter(Inf_ArrayCnv arrayConv) {
		this.arrayConv = arrayConv;
	}

	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	@Override
	public boolean write(String[] splited, int stat) {
		arrayConv.convert(splited, 0);
		return true;
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
		test01();
	}

	// ------------------------------------------------------------------------
	// �l�X�ȕ����R�[�h�Ńt�@�C���������o���Ă݂�e�X�g
	// ------------------------------------------------------------------------
	public static void test01() {
	}

	@Override
	public boolean open() {
		return true;
	}

	@Override
	public boolean open(boolean append) {
		return true;
	}

	@Override
	public void setDelimiter(String delimiter) {
	}

	@Override
	public void setLF(String lf) {
	}

	@Override
	public long getWriteCount() {
		return 0;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public void close() {
	}

	@Override
	public void setHeader(String header) {

	}

	@Override
	public void setHeader(List<String> headList, String delimiter) {

	}

	@Override
	public void setHeader(String[] headArray, String delimiter) {

	}

}
