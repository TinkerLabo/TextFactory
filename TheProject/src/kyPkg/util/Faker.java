package kyPkg.util;

public class Faker {
	// java�ɂ�join���Ė����񂾂����H�H���ɍ���Ă������E�E�E
	//XXX ����Ȃ̂�����̂Ō�œ������Ă���=>kyPkg.util.KUtil.join(dim_L)
	public static String getDummy(int occ, String delim) {
		if (occ >0){
			return join(new String[occ],delim); 
		}else{
			return null; 
		}
	}
	public static String join(String[] array, String delim) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if (i > 0)
				buf.append(delim);
			if (array[i] != null)
				buf.append(array[i]);
		}
		return buf.toString();
	}
	public static void main(String[] argv){
		String magicChar="@";
		String delimiter=",";
		int max_L = 5;		//	�v�f��
		int magicIndex =1;	//	�ʒu�C���f�b�N�X�i�[������n�܂�j
		String DUMMY_L = kyPkg.util.Faker.joinPlus(new String[max_L], delimiter,magicIndex,magicChar);
		System.out.println(DUMMY_L);
	}
	public static String joinPlus(String[] array, String delim,int index,String magicChar) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if (i > 0)
				buf.append(delim);
			if (index >= 0 && index == i)
				array[i] = magicChar;
			if (array[i] != null)
				buf.append(array[i]);
		}
		return buf.toString();
	}
}
