package kyPkg.util;

public class Faker {
	// javaにはjoinって無いんだっけ？？仮に作っておいた・・・
	//XXX こんなのがあるので後で統合しておく=>kyPkg.util.KUtil.join(dim_L)
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
		int max_L = 5;		//	要素数
		int magicIndex =1;	//	位置インデックス（ゼロから始まる）
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
