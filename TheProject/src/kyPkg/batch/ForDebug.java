package kyPkg.batch;

import java.util.HashMap;

public class ForDebug {
	public static void test() {
		HashMap hash = new HashMap();
		String[] array = kyPkg.uFile.DosEmu.dirList2Array("N:/PowerBX/2008/", "T*");
		for (int i = 0; i < array.length; i++) {
			System.out.println("D=>" + array[i]);
			String[] splited = array[i].split("_");
			if (splited.length > 1) {
				hash.put(splited[0], array[i]);
				System.out.println("  0=>" + splited[0]);
				System.out.println("  i=>" + array[i]);
			}
		}
	}

	public static void main(String[] argv) {
		test();
	}
}
