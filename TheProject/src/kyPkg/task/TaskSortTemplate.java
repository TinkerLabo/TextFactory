package kyPkg.task;

import java.io.*;
//-----------------------------------------------------------------------------
//　汎用I/O　　　                                      K.Yuasa 2008.2.25
//-----------------------------------------------------------------------------
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kyPkg.Sorts.Comparator4Delim;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;

public class TaskSortTemplate extends Abs_ProgressTask {
	private String path_I;

	private String path_O;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public TaskSortTemplate(String path_O, String path_I) {
		super();
		this.path_I = path_I;
		this.path_O = path_O;
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// I/O
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public static void filter01(String path_O, String path) {
		String wRec = "";
		StringBuffer buff = new StringBuffer(256);
		File file = new File(path);
		if (file.isFile()) {
			try {
				String wPath_O = path + ".tmp"; // .tmpが無いという前提
				FileWriter writer = new FileWriter(wPath_O);
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((wRec = br.readLine()) != null) {
					buff.delete(0, buff.length());

					// --------------------------------------------------------------------
					// ここでフィルター処理・・・
					// --------------------------------------------------------------------

					buff.append(wRec);
					buff.append(System.getProperty("line.separator"));
					writer.write(buff.toString());
				}
				br.close();
				writer.close();
				File wFile_O = new File(wPath_O);
				file.delete();
				wFile_O.renameTo(new File(path_O));
				System.out.println("TaskSortTemp 処理→" + path_O);
			} catch (IOException ie) {
				ie.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ------------------------------------------------------------------------
	// 外部からコールされるトリガー
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TaskSortTemplate", 2048);
		if (isStarted()) {
			final SwingWrk worker = new SwingWrk() {
				@Override
				public Object construct() {
					return new ActualTask(); // 実際の処理
				}
			};
			worker.start();
		}
		super.stop();// 正常終了
	}

	// ------------------------------------------------------------------------
	// 《実際の処理》
	// ------------------------------------------------------------------------
	class ActualTask {
		ActualTask() {
			// setLengthOfTask(256); // これを処理単位のサイズ

			// I/O etc...
			// setLengthOfTask(256); //これを処理単位のサイズ
			// filter01(path_O, path_I);
			// setCurrent(256); //これをフィルタ内での位置をセットすればよい

			sortAndEtc(path_O, path_I);

			stop();// 正常終了

		}

		// -------------------------------------------------------------------------
		// sortAndEtc
		// -------------------------------------------------------------------------
		public void sortAndEtc(String path_O, String path_I) {
			String saveID = "";
			String wPath_O = path_I + ".tmp"; // .tmpが無いという前提
			int wCnt = -1;
			// ---------------------------------------------------------------------
			// Sort !!
			// ---------------------------------------------------------------------
			List list = ListArrayUtil.file2List(path_I);
			Collections.sort(list,
					new Comparator4Delim(",", new int[] { 1, 2 }));

			// setLengthOfTask(list.size()); // プログレスバーの全体の長さ

			// ---------------------------------------------------------------------
			// Loop !!
			// ---------------------------------------------------------------------
			try {
				FileWriter writer = new java.io.FileWriter(wPath_O);
				StringBuffer buff = new StringBuffer(256);

				for (Iterator iter = list.iterator(); iter.hasNext();) {

					setCurrent(wCnt++); // プログレスバーの位置

					String element = (String) iter.next();
					// System.out.println(element);
					String[] array = element.split("\t");
					if (array.length > 2) {
						if (saveID.equals(array[0])) {
							try {

							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {

							// System.out.println(saveID + "_" +
							// map.toString()+"=>"+insMap.cnv2String(size48));
							// map.clear();
							buff.delete(0, buff.length());

							buff.append(saveID + "_");
							buff.append(System.getProperty("line.separator"));
							writer.write(buff.toString());

						}
						saveID = array[0];
					}
				}
				writer.close();
				File wFile_O = new File(wPath_O);
				wFile_O.renameTo(new File(path_O));
				System.out.println("TaskSortTemp 処理→" + path_O);
			} catch (IOException ie) {
				ie.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
