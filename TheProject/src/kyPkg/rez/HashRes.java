package kyPkg.rez;

import java.io.*;
import java.util.*;

import kyPkg.uFile.FileUtil;

public class HashRes {
	protected String wLF = System.getProperty("line.separator");

	private String delimiter = ",";

	private HashMap hashMap;

	// -------------------------------------------------------------------------
	// 値取得
	// -------------------------------------------------------------------------
	public String getValue(String key) {
		if (hashMap == null)
			return "NoName";
		return (String) hashMap.get(key);
	}

	// -------------------------------------------------------------------------
	// getSize
	// -------------------------------------------------------------------------
	public int getSize() {
		if (hashMap == null)
			return -1;
		return hashMap.size();
	}

	public void debug() {
		Set collectionView = hashMap.entrySet();
		Iterator iter = collectionView.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			java.util.Map.Entry ent = (java.util.Map.Entry) obj;
			String key = (String) ent.getKey();
			String val = (String) ent.getValue();
			System.out.println("DEBUG key:" + key + " val:" + val);
		}
	}

	// -------------------------------------------------------------------------
	// saveAs
	// -------------------------------------------------------------------------
	public int saveAs(String outPath) {
		int rtn = -1;
		// File file = new File(parmPath);
		// if(!file.canWrite()){
		// System.out.println("File can't open:"+parmPath);
		// return rtn;
		// }
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			rtn++;
			Set collectionView = hashMap.entrySet();
			Iterator iter = collectionView.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				java.util.Map.Entry ent = (java.util.Map.Entry) obj;
				String key = (String) ent.getKey();
				String val = (String) ent.getValue();
				bw.write(key);
				bw.write(delimiter);
				bw.write(val);
				bw.write(wLF);
				rtn++;
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return rtn;
	}

	// -------------------------------------------------------------------------
	// Incore
	// -------------------------------------------------------------------------
	public int incore(String parmPath, String delimiter) {
		setDelimiter(delimiter);
		return incore(parmPath);
	}

	public int incore(String path) {
		int rtn = -1;
		String wRec;
		File fl = new File(path);
		if (fl.exists() == false) {
			System.out.println("HashRes@incore FileNotFound:" + path);
			return rtn;
		}
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null && (!wRec.equals(""))) {
					String[] splited = wRec.split(delimiter);
					if (splited.length >= 2) {
						hashMap.put(splited[0], splited[1]);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return rtn;
		}
		rtn = hashMap.size();
		return rtn;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public HashRes() {
		super();
		hashMap = new HashMap();
	}

	public HashRes(String parmPath) {
		this();
		incore(parmPath);
	}

	public HashRes(String parmPath, String delimiter) {
		this();
		setDelimiter(delimiter);
		incore(parmPath);
	}



	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public int size() {
		if (hashMap == null)
			return -1;
		return hashMap.size();
	}

	public static void main(String[] args) {
		test01();
	}
	// -------------------------------------------------------------------------
	// for TestDrive
	// -------------------------------------------------------------------------
	public static void test00() {
		String aliasPath = "Z:/s2/rx/Idxc/Enquete/属性・ライフスタイル編/2008/alias.txt";
		kyPkg.rez.HashRes hRes = new kyPkg.rez.HashRes(aliasPath, "\t");
		System.out.println("connect=>" + hRes.getValue("connect"));
		System.out.println("table=>" + hRes.getValue("table"));
		System.out.println("field=>" + hRes.getValue("field"));
		System.out.println("key=>" + hRes.getValue("key"));
		System.out.println("Cond=>" + hRes.getValue("cond"));
	}

	private static void test01() {
		String curDir = globals.ResControl.getCurDir();
		String parmPath = curDir + "QPRRES/QBRCat08.txt"; // カテゴリーコード＆名称対応表
		kyPkg.rez.HashRes hRes = new kyPkg.rez.HashRes(parmPath, "\t");
		// hRes.incore(parmPath);
		System.out.println("2010=>" + hRes.getValue("2010x"));
		// for (int i = 0; i < hRes.getSize(); i++) {
		// System.out.println("#" + i + "=>" + hRes.getValue("" + i));
		// }
		// hRes.saveAs("c:/backdrop.txt");
	}

}
