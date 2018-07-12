package kyPkg.rez;

import java.io.*;
import java.util.*;

import kyPkg.uFile.FileUtil;

public class TextRes {
	private static final String wLF = System.getProperty("line.separator");
	private String[] array = new String[0];

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public TextRes(String[] array) {
		this("");
		setArray(array);
	}

	public TextRes() {
		this("");
	}

	public TextRes(String parmPath) {
		super();
		array = new String[0];
		if (!parmPath.trim().equals("")) {
			incore(parmPath);
		}
	}

	// -------------------------------------------------------------------------
	// Incore
	// -------------------------------------------------------------------------
	public void incore(String path) {
		ArrayList list = new ArrayList();
		File file = new File(path);
		if (file.exists() == false) {
			System.out.println("TextRes@incore FileNotFound:" + path);
			// JOptionPane.showMessageDialog(null, new JLabel(
			// "@incore FileNotFound:" + parmPath), "Warning...",
			// JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			String wRec;
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null && (!wRec.equals(""))) {
					list.add(wRec);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		if (list.size() > 0) {
			array = (String[]) list.toArray(new String[list.size()]);
		} else {
			array = new String[0];
		}
	}

	// -------------------------------------------------------------------------
	// 値の追加 (めんどくさい・・)
	// -------------------------------------------------------------------------
	public void append(String str) {
		List orgList = Arrays.asList(array);
		ArrayList list = new ArrayList(orgList);
		list.add(str);
		array = (String[]) list.toArray(new String[list.size()]);
	}

	// -------------------------------------------------------------------------
	// 名称取得
	// -------------------------------------------------------------------------
	public String getName(int idx) {
		if (idx > array.length)
			return "error";
		return array[idx];
	}

	// -------------------------------------------------------------------------
	// getSize
	// -------------------------------------------------------------------------
	public int getSize() {
		return array.length;
	}

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		if (array == null) {
			this.array = new String[0];
		} else {
			this.array = array;

		}
	}

	// -------------------------------------------------------------------------
	// saveAs
	// -------------------------------------------------------------------------
	public int saveAs(String outPath) {
		int rtn = -1;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			rtn++;
			for (int i = 0; i < array.length; i++) {
				bw.write(array[i]);
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
	// for Test
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		String parmPath = "C:/nameRes/30分刻みx.txt";
		kyPkg.rez.TextRes strRes = new kyPkg.rez.TextRes(parmPath);
		// ins.incore(parmPath);
		strRes.append("The OtherOne");
		for (int i = 0; i < strRes.getSize(); i++) {
			System.out.println("#" + i + "=>" + strRes.getName(i));
		}
		strRes.saveAs("c:/backdrop.txt");
	}

}
