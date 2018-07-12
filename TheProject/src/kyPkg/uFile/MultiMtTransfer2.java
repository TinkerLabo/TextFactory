package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globals.ResControl;

//仮想ＭＴが128マルチまでしか持てない為
// マルチボリュームテープの各ラベルデータをＦＴＰ転送（移行作業）　2010-07-22 yuasa
// ボリュームの前のほうのデータなら2分、後方（1200番台）だと10分かかる
//　ラベルのシークに時間がかかっている雰囲気・・・リワンドさせない方法はないか？
public class MultiMtTransfer2 {
	public static void main(String[] args) {
		// 実行方法　ftp -s:ftp2host.txt>>ftplog.txt
		List<String> tmpList = incoreTmp("c:/tmpJcl.txt");
		List prmList = paramAnalyze(
				ResControl.DSKTOP + "MTLIST0723.TXT");
		List outList = new ArrayList();
		String outPath = "c:/jcl/ftpJcl.txt";
		outList.add(outPath);
		int fromLine = 20;
		int toLine = 23;
		writeIt(outPath, tmpList, prmList, fromLine, toLine);
		writeFtpScript("c:/jcl/ftp2host.txt", outList);
		List batch = new ArrayList();
		batch.add("ftp -s:ftp2host.txt>>ftplog.txt");
		writeFromList("c:/jcl/run.bat", batch);
	}

	public static List paramAnalyze(String path) {
		List outList = new ArrayList();
		try {
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] array = line.split("\\!");
				if (array.length > 5) {
					System.out.println(array[2] + ":" + array[7]);
					String param = array[2].trim();
					if (param.equals("DATASET NAME")) {
					} else if (array[7].trim().equals("0")) {
						param = "";// 位置をカウントするのにダミーが必要！！
						outList.add(param);
					} else {
						outList.add(param);
					}
				} else {
					if (line.contains("VSN")) {
						String vsn = (array[1].substring(6)).trim();
						System.out.println(vsn);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			System.err.format("Unable to open file named '%s': %s", path,
					e.getMessage());
			e.printStackTrace();
		}
		return outList;
	}

	public static void writeIt(String outPath, List<String> tmpList,
			List prmList, int fromLine, int toLine) {
		String LF = System.getProperty("line.separator");
		fromLine = fromLine - 1;// 微調整！！
		// 2007-07-23
		// int startAt = 1219;// for debug
		// int endAt = 1263;// for debug
		// 2007-07-26
		// int startAt = 1100;// for debug
		// int endAt = 1200;// for debug
		// 2007-07-27
		// int startAt = 1000;// for debug
		// int endAt = 1199;// for debug
		// int startAt = 1001;// for debug
		// int endAt = 1100;// for debug
		// 2007-07-28
		// int startAt = 901;// for debug
		// int endAt = 1000;// for debug
		// 2007-07-29
		// int startAt = 801;// for debug
		// int endAt = 900;// for debug
		// 2007-07-30
		// int startAt = 601;// for debug
		// int endAt = 800;// for debug
		// 2007-08-02
		// int startAt = 401;// for debug
		// int endAt = 600;// for debug
		// 2007-08-03
		int startAt = 000;// for debug
		int endAt = 400;// for debug
		try {
			File file = new File(outPath);
			FileWriter writer = new FileWriter(file);
			// header
			for (int i = 0; i < fromLine; i++) {
				String line = tmpList.get(i);
				writer.write(line);
				writer.write(LF);
			}
			// body
			int seq = 0;
			for (Iterator iterator = prmList.iterator(); iterator.hasNext();) {
				String param = (String) iterator.next();
				seq++;
				if (!param.equals("")) {
					for (int i = fromLine; i < toLine; i++) {
						String line = tmpList.get(i);
						if (startAt <= seq && seq <= endAt) {
							writer.write(String.format(line, param, seq));
							writer.write(LF);
						}
					}

				}
			}
			// footer
			for (int i = toLine; i < tmpList.size(); i++) {
				String line = tmpList.get(i);
				writer.write(line);
				writer.write(LF);

			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeFtpScript(String outPath, List<String> outList) {
		try {
			File file = new File(outPath);
			FileWriter writer = new FileWriter(file);
			writer.write("open 10.2.255.10\n");
			writer.write("EJFTP\n");
			writer.write("QPRFTP\n");
			writer.write("ascii \n");
			writer.write("quote site setcode s  \n");
			for (Iterator iterator = outList.iterator(); iterator.hasNext();) {
				String path = (String) iterator.next();
				writer.write("put " + path + " INTRDR \n");
			}
			writer.write("bye                   \n");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeFromList(String outPath, List<String> outList) {
		try {
			File file = new File(outPath);
			FileWriter writer = new FileWriter(file);
			for (Iterator iterator = outList.iterator(); iterator.hasNext();) {
				String line = (String) iterator.next();
				writer.write(line);
				writer.write("\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> incoreTmp(String path) {
		List tmpList = new ArrayList();
		try {
			File file = new File(path);
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				tmpList.add(line);
			}
			br.close();
		} catch (Exception e) {
			System.err.format("Unable to open file named '%s': %s", path,
					e.getMessage());
		}
		return tmpList;
	}
}
