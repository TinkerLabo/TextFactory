package kyPkg.rez;

import java.io.*;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import kyPkg.uFile.FileUtil;

public class IntRes {
	private String wLF = System.getProperty("line.separator");
	private int[] converter = new int[0];
	// -------------------------------------------------------------------------
	// 当該値に対応する値を返す
	// -------------------------------------------------------------------------
	public int[] getConverter() {
		return converter;
	}

	public void setConverter(int[] converter) {
		if (converter==null){
			converter = new int[0];
		}else{
			this.converter = converter;
		}
	}
	public int getConverter(int idx) {
		if (idx > converter.length)
			return -1;
		return converter[idx];
	}

	// -------------------------------------------------------------------------
	// getSize
	// -------------------------------------------------------------------------
	public int getSize() {
		return converter.length;
	}

	// -------------------------------------------------------------------------
	// saveAs
	// -------------------------------------------------------------------------
	public int saveAs(String outPath) {
		int rtn = -1;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			rtn++;
			for (int i = 0; i < converter.length; i++) {
				bw.write(String.valueOf(converter[i]+1));
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
	public void incore(String path) {
		ArrayList list = new ArrayList();
		String wRec;
		File fl = new File(path);
		if (fl.exists() == false) {
			System.out.println("@incore FileNotFound:" + path);
			JOptionPane.showMessageDialog(null,new JLabel("IntRes@incore FileNotFound:" + path)
			,"Warning...",JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec == null)
					wRec = "-1";
				list.add(Integer.valueOf(wRec));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		converter = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof Integer) {
				converter[i] = ((Integer) obj).intValue()-1;
			} else {
				converter[i] = -1;
			}
		}

	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public IntRes() {
		super();
		this.converter = new int[0];
	}

	public IntRes(String parmPath) {
		this();
		incore(parmPath);
	}

	// -------------------------------------------------------------------------
	// for Test
	//		IntRes intRes = new IntRes("C:/nameRes/30分刻み_30分刻みx.cnv");
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		String parmPath = "C:/nameRes/30分刻み_30分刻みx.cnv";
		IntRes intRes = new IntRes(parmPath);
		for (int i = 0; i < intRes.getSize(); i++) {
			System.out.println("#" + i + "=>" + intRes.getConverter(i));
		}
		intRes.saveAs("c:/backdrop.txt");
	}


}
