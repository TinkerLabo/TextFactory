package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapUtil {
	// ------------------------------------------------------------------------
	// ファイルからハッシュマップにデータを格納（連結）する
	// ------------------------------------------------------------------------
	public static HashMap<String, List<String>> file2Map(String path) {
		File49ers f49 = new File49ers(path);
		String delimiter = f49.getDelimiter();
		int maxCol = f49.getMaxColCount();
		HashMap<String, List<String>> map = mapit(path, delimiter, null, null);
		return map;
	}
	
	public static HashMap<String, List<String>> file2Dict(String path) {
		return file2Dict(path,null,  null);
	}
	public static HashMap<String, List<String>> file2Dict(String path,int[] keyCols, int[] valCols) {
		File49ers f49 = new File49ers(path);
		String delimiter = f49.getDelimiter();
		int maxCol = f49.getMaxColCount();
		HashMap<String, List<String>> map = mapit(path, delimiter, keyCols,
				valCols);
		return map;
	}

	private static HashMap<String, List<String>> mapit(String path,
			String delimiter, int[] keyCols, int[] valCols) {
		HashMap<String, List<String>> map = new HashMap();
		String[] array = null;
		if (keyCols == null)
			keyCols = new int[] { 0 };
		try {
			StringBuffer keyBuf = new StringBuffer();
			BufferedReader br = FileUtil.getBufferedReader(path);
			String rec = br.readLine();
			while (rec != null) {
				array = rec.split(delimiter, -1);
				//-------------------------------------------------------------
				// key Generate
				//-------------------------------------------------------------
				keyBuf.delete(0, keyBuf.length());
				for (Integer keyCol : keyCols) {
					//	System.out.println("array.length:" + array.length + " > " + keyCol);
					if (array.length > keyCol)
						keyBuf.append(array[keyCol]);
				}
				List<String> list = array2List(array, valCols);
				map.put(keyBuf.toString(), list);
				rec = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}


	private static List<String> array2List(String[] array, int[] valCols) {
		List<String> list = new ArrayList();
		if (valCols == null) {
			for (int col = 1; col < array.length; col++) {
				list.add(array[col]);
			}
		} else {
			for (Integer col : valCols) {
				if (array.length > col)
					list.add(array[col]);
			}
		}
		return list;
	}

	//-------------------------------------------------------------------------
	// これと・・・別のファイルを連結させるロジックを作る・・・・バッチで動作するようにする20170613
	//-------------------------------------------------------------------------
	public static void main(String[] argv) {
		String path = "c:/gabbagabbahey.txt";
		int[] keyCols = null;
		int[] valCols = new int[] { 1, 2, 3, 4, 5, 6 };
		HashMap<String, List<String>> map = file2Dict(path, keyCols, valCols);
		List<String> keyList = new ArrayList(map.keySet());
		for (int i = 1; i < 10; i++) {
			System.out.println("key:" + i + " =>" + map.get(String.valueOf(i)));
		}
	}

}
