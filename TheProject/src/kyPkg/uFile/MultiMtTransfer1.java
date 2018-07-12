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
public class MultiMtTransfer1 {
	public static void main(String[] args) {
		// 実行方法　ftp -s:ftp2host.txt>>ftplog.txt
		List<String> tmpList = incoreTmp(args[0]);
		List prmList = paramAnalyze(ResControl.DSKTOP + "MTLIST.TXT");
		List outList = new ArrayList();
		for (Iterator iterator = prmList.iterator(); iterator.hasNext();) {
			String param = (String) iterator.next();
			String prefix = param.replace('.', '_');
			String outPath = "c:/jcl/" + prefix + ".txt";
			outList.add(outPath);
			writeIt(outPath, tmpList, param);
		}
		writeFtpScript("c:/jcl/ftp2host.txt", outList);
		List batch = new ArrayList();
		batch.add("ftp -s:ftp2host.txt>>ftplog.txt");
		writeFromList("c:/jcl/run.bat", batch);
	}

	public static List paramAnalyze(String filename) {
		List outList = new ArrayList();
		try {
			File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] array = line.split("\\!");
				if (array.length > 5) {
					System.out.println(array[2] + ":" + array[7]);
					String param = array[2].trim();
					if (!array[7].trim().equals("0")
							&& !param.equals("DATASET NAME")) {
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
			System.err.format("Unable to open file named '%s': %s", filename,
					e.getMessage());
			e.printStackTrace();
		}
		return outList;
	}

	public static void writeIt(String outPath, List<String> tmpList,
			String param) {
		try {

			File file = new File(outPath);
			FileWriter writer = new FileWriter(file);
			for (Iterator iterator = tmpList.iterator(); iterator.hasNext();) {
				String line = (String) iterator.next();
				writer.write(String.format(line + "%n", param));
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

	public static List<String> incoreTmp(String filename) {
		List tmpList = new ArrayList();
		try {
			File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				tmpList.add(line);
			}
			br.close();
		} catch (Exception e) {
			System.err.format("Unable to open file named '%s': %s", filename,
					e.getMessage());
		}
		return tmpList;
	}
}
