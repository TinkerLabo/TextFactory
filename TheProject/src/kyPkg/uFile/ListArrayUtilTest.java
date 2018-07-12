package kyPkg.uFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;

public class ListArrayUtilTest {
	public static void file2Map_N_List() {
		String path =ResControl.SVREPOSITORY;
		List<String> list = kyPkg.uFile.ListArrayUtil.file2List(path);
		List<String> keylist = new ArrayList();
		HashMap<String, String> hmap = new HashMap();
		for (String element : list) {
			String[] array = element.split("\t");
			if (array.length >= 2) {
				keylist.add(array[0]);
				hmap.put(array[0], array[1]);
			}
		}
		for (String key : keylist) {
			System.out.println("key:" + key + "  val:" + hmap.get(key));
		}
	}

	public static void testFile2HashMap() {
		String path =  ResControl.SVREPOSITORY;
		HashMap<String, String> hMap = HashMapUtil.file2HashMapX(path);
		List keyList = new ArrayList(hMap.keySet());
	}

	public static void main(String[] argv) {
		file2Map_N_List();
	}
}
