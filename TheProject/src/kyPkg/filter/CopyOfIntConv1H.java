package kyPkg.filter;
public class CopyOfIntConv1H implements Inf_IntConverter {
	//-------------------------------------------------------------------------
	// �R�O�����݃e�[�u���̕��т�ԑg�\�^�������тɕύX����
	//-------------------------------------------------------------------------
	@Override
	public int convertInt(int in){
		if (in < 10){
			in += 38;
		}else{
			in -= 10;
		}
		return in;
	}
}
