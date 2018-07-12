package kyPkg.util;

import java.io.IOException;

public class MontyPython {
	public static void test() {
		MontyPython.call("python ./hello.py > abc.txt");
	}
	public static boolean call(String command){
		// XXX WINDOWSNTならこれでOKでした・・・他のOSへの対応も書いておくべき
		// Shell経由でないと動いてくれない感じ・・・
		try {
			Runtime.getRuntime()
					.exec("cmd /C " + command );
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static void main(String[] argv){
		MontyPython.call("python ./hello.py > abc.txt");
	}
}
