package kyPkg.filter;
public class Inf_IntConv1H implements Inf_IntConverter {
	//-------------------------------------------------------------------------
	// �ꎞ�ԍ��݃e�[�u���̕��т�ԑg�\�^�������тɕύX����
	//-------------------------------------------------------------------------
	@Override
	public int convertInt(int in){
		if (in < 5){
			in += 19;
		}else{
			in -= 5;
		}
		return in;
	}
}