package kyPkg.util;

import java.io.IOException;

public class MontyPython {
	public static void test() {
		MontyPython.call("python ./hello.py > abc.txt");
	}
	public static boolean call(String command){
		// XXX WINDOWSNT�Ȃ炱���OK�ł����E�E�E����OS�ւ̑Ή��������Ă����ׂ�
		// Shell�o�R�łȂ��Ɠ����Ă���Ȃ������E�E�E
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
