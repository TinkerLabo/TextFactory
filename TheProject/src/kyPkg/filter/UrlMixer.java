package kyPkg.filter;

import java.io.*;
import java.util.*;

import globals.ResControl;

public class UrlMixer {
	private HashMap hmap = null;
	private static final String LF = System.getProperty("line.separator");
	private static final String SEP = System.getProperty("file.separator");
	private static final String REF1 = "<BR><A HREF=\t\"";
	private static final String REF2 = "\"\t";
	private static final String REF3 = ">\t";
	private static final String REF4 = "\t</A>";
	//	private String parent;
	private String current;
	private String defaultPath;

	public UrlMixer(String dir) {
		System.out.println("dir:" + dir);
		hmap = new HashMap();
		File f = new File(dir);
		current = f.getAbsolutePath() + SEP;
		//		parent = getPpath(f.getAbsolutePath())+ SEP;
		//		System.out.println("parent:" + parent);
		//		defaultPath = parent + getYY_MM() + ".html";
		defaultPath = current + getYY_MM() + ".html";
		String[] lst = f.list();
		for (int i = 0; i < lst.length; i++) {
			String path = lst[i].trim();
			// 拡張子を判定して実処理へ
			if ((path.toLowerCase()).endsWith(".url")) {
				checkIt(path);
			}
		}
		//enumHmap();
		write2File();
	}

	public void write2File() {
		write2File(defaultPath);
	}

	public void write2File(String oPath) {
		if (hmap.size() == 0)
			return;
		try {
			//System.out.println("oPath:" + oPath);
			FileWriter fw = new FileWriter(new File(oPath));
			mapWrite(fw);
			fw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enumHmap() {
		ArrayList<String> list = new ArrayList();
		Set set = hmap.entrySet();
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			Map.Entry<String, String> element = (Map.Entry) iter.next();
			String key = element.getKey();
			list.add(key);
			String value = element.getValue();
			System.out.println("UrlMixer key:" + key + " value:" + value);
		}
	}

	public void mapWrite(FileWriter fw) throws IOException {
		ArrayList<String> list = new ArrayList();
		Set set = hmap.entrySet();
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			Map.Entry<String, String> element = (Map.Entry) iter.next();
			String key = element.getKey();
			list.add(key);
		}
		Collections.sort(list);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String value = (String) hmap.get(key);
			//System.out.println("key:" + key + " value:" + value);
			String wRec = REF1 + key + REF2;
			wRec = wRec + REF3 + value + REF4 + LF;
			//System.out.println("=>"+wRec);
			fw.write(wRec);
		}
	}

	public void mapWrite_old(FileWriter fw) throws IOException {
		Set set = hmap.entrySet();
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			Map.Entry<String, String> element = (Map.Entry) iter.next();
			String key = element.getKey();
			String value = element.getValue();
			//System.out.println("key:" + key + " value:" + value);
			String wRec = REF1 + key + REF2;
			wRec = wRec + REF3 + value + REF4 + LF;
			//System.out.println("=>"+wRec);
			fw.write(wRec);
		}
	}

	// -------------------------------------------------------------------------
	// k.yuasa 2005.4.13
	// 同階層にある*.urlファイルをすべて読み込んでリンクをMAPに集約する
	// -------------------------------------------------------------------------
	// [DEFAULT]
	// BASEURL=http://health.yahoo.co.jp/column/influenza/
	//
	// [InternetShortcut]
	// URL=http://health.yahoo.co.jp/column/influenza/
	// Modified=20DA4E4D7424CA01F8
	// -------------------------------------------------------------------------
	private void checkIt(String path) {
		path = current + path;
		System.out.println("iPath:" + path);
		File iFile = new File(path);
		//		String wVar[] = (iFile.getName()).split("\\.");
		String wName = iFile.getName();
		wName = (wName).substring(0, (wName.length() - 4)); // ファイル名の頭
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			boolean swDef = false;
			boolean swIns = false;
			String wUrl = "";
			while ((line = br.readLine()) != null) {
				// []はﾚｼﾞｯｸｽで別の意味を持つのでNG line.matches("[DEFAULT]*.*")
				if (line.startsWith("[DEFAULT]"))
					swDef = true;
				if (line.startsWith("[InternetShortcut]"))
					swIns = true;
				if (swDef == true) {
					if (line.matches("BASEURL=*.*")) {
						wUrl = line.substring(8, line.length());
						hmap.put(wUrl, wName);
						swDef = false;
					}
				}
				if (swIns == true) {
					if (line.matches("URL=*.*")) {
						wUrl = line.substring(4, line.length());
						hmap.put(wUrl, wName);
						swIns = false;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------
	// Get月日
	// ----------------------------------------------------------------
	private String getYY_MM() {
		String wRtn;
		Calendar wCal = Calendar.getInstance();
		int sMM = wCal.get(Calendar.MONTH) + 1;
		int sDD = wCal.get(Calendar.DATE);
		wRtn = "" + sMM + "_" + sDD;
		return wRtn;
	}

	//	// ----------------------------------------------------------------
	//	// #xxx ペアレントパスを取得(File.getParent()が使えない時・・)
	//	// 《使用例》
	//	// String mamapath = makeParents("c:\ga\bba\gabba\hey.txt");
	//	// ----------------------------------------------------------------
	//	private String getPpath(String pPath) {
	//		if (pPath.indexOf(".") > 0) { // ファイル名が含まれているか
	//			int pos = pPath.lastIndexOf(SEP);
	//			if (pos < 0) {
	//				System.out.println("#Error Not Directory:" + pPath);
	//				return "";
	//			}
	//			pPath = pPath.substring(0, pos); // ParentPathを設定し直す
	//		}
	//		return pPath;
	//	}
	// -------------------------------------------------------------------------
	// 指定ディレクトリ上にあるurlファイルをまとめる
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		new UrlMixer(ResControl.DSKTOP + "URL");
		//		new UrlMixer(".");
	}

}
