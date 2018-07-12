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
	// ��̐擪�ɂm�`���쐬����
	// XXX �T�C�Y�����������ق����ǂ����H�i�p�t�H�[�}���X���悩�ȁj
	// -------------------------------------------------------------------------
	@Override
	public String convert(String val) {
		buff.delete(0, buff.length());
		if (val.trim().equals("")) {
			 buff.append("1"); // ���������������I
//			buff.append("@"); // for Debug
		} else {
			 buff.append(" "); // ���������������I
//			buff.append("@"); // for Debug
		}
		buff.append(val);
		return buff.toString();
	}

}
