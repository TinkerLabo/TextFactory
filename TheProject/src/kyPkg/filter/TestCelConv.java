package kyPkg.filter;

import java.util.HashMap;

public class TestCelConv {
	//コンストラクタは引数なし・・・メソッドにシグニチャはString,String[]とする・・・か
	public TestCelConv() {
		System.out.println("コンストラクタ");
	}
	public String convert(HashMap<String, String> infoMap) {
		String val = "Hello";
		System.out.println("@Convert HashMap<String, String>"+val);
		return val;
	}
	public String convert( String[] parms) {
		System.out.println("@Convert String[] ");
		return "";
	}
	public String convert(String cel, String[] parms) {
		String val = cel.toUpperCase();
		System.out.println("セルの値とパラメータが引数であるメソッドが呼ばれました");
		return val;
	}
}
