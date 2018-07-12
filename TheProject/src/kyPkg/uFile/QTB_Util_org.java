package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;

public class QTB_Util_org {

	private HashMap<String, HashMap<String, String>> dictionary;
	private HashMap<String, String> typMap;
	private HashMap<String, String> fldMap;
	private HashMap<String, String> nam2key;
	private List<String> namList;

	private String connect;
	private String table;
	private String key;
	private String field;
	private String cond;

	public QTB_Util_org(String path) {
		super();
		dictionary = new HashMap();
		typMap = new HashMap();
		fldMap = new HashMap();
		nam2key = new HashMap();
		namList = new ArrayList();

		// ex "Z:/S2/rx/enquetes/NQ/01_属性・メディア編/2012/alias.txt"
		String path1 = path + "alias.txt";
		HashMap<String, String> hMap = HashMapUtil.file2HashMapX(path1);
		if (hMap != null) {
			connect = hMap.get("connect");
			key = hMap.get("key");
			table = hMap.get("table");
			field = hMap.get("field");
			cond = hMap.get("cond");
		}
		System.out.println("connect:" + connect);
		System.out.println("key:" + key);
		System.out.println("table:" + table);
		System.out.println("field:" + field);
		System.out.println("cond:" + cond);

		// key:Cond Val:
		// key:connect Val:DRIVER={SQL
		// Server};SERVER=ks1s004;UID=sa;PWD=;DATABASE=qprdb;Trusted_Connection=true
		// key:key Val:CH_ID
		// key:table Val:TBL_NQMED2012
		// key:field Val:CH_DAT

		String path2 = path + "Qtb1.TXT";
		qtb2HashMap(path2,field);
		enumurate();

	}

	public boolean qtb2HashMap(String path, String field) {
		int motCol = 1;
		int keyCol = 2;
		int valCol = 3;
		int namCol = 4;
		int maxCol = 5;
		int occCol = 6;
		int typCol = 7;
		int colCol = 8;
		int lenCol = 9;
		String delimiter = "\t";

		String wRec;
		File fl = new File(path);
		if (fl.exists() == false)
			return false;

		dictionary.clear();
		typMap.clear();
		fldMap.clear();
		nam2key.clear();
		namList.clear();
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] wArray = wRec.split(delimiter);
					if (wArray.length > motCol) {
						String mot = wArray[motCol].trim();
						if (mot.equals("ROOT")) {
							String key = wArray[keyCol];
							String val = wArray[valCol];
							String nam = wArray[namCol];
							String typ = wArray[typCol];
							String col = wArray[colCol];
							String len = wArray[lenCol];
							String fld = "SUBSTRING(" + field + "," + col + ","
									+ len + ")";
							namList.add(nam);
							nam2key.put(nam, key);
							fldMap.put(key, fld);
							typMap.put(key, typ);// Multi or Single
						} else {
							HashMap<String, String> cnv = dictionary.get(mot);
							if (cnv == null)
								cnv = new HashMap();
							String val = wArray[valCol];
							String nam = wArray[namCol];
							String max = wArray[maxCol];
							String occ = wArray[occCol];
							String typ = wArray[typCol];
							String col = wArray[colCol];
							String len = wArray[lenCol];
							cnv.put(val, nam);
							dictionary.put(mot, cnv);
						}
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return true;
	}

	public void enumurate() {
		for (String nam : namList) {
			String key = nam2key.get(nam);
			System.out.println("key:" + key + " nam:" + nam);
			String fld = fldMap.get(key);
			String typ = typMap.get(key);
			System.out.println("    fld:" + fld);
			System.out.println("    typ:" + typ);
			HashMap<String, String> cnv = dictionary.get(key);
			if (cnv != null) {
				List<String> keyList = new ArrayList(cnv.keySet());
				for (String element : keyList) {
					System.out.println("       element :" + element + " =>"
							+ cnv.get(element));
				}
			}
		}
	}

	// #######################################################################
	public static void test_file2HashMap() {
	}

	public static void test_qtb2HashMap() {
		String path = ResControl.ENQ_DIR + "NQ/03_属性・性年代編/2012/";
		QTB_Util_org ins = new QTB_Util_org(path);
	}

	public static void main(String[] args) {
		HashMapUtil.testFile2HashMapX();
		QTB_Util_org.test_qtb2HashMap();
	}

}
