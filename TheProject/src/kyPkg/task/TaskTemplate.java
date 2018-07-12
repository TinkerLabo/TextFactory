package kyPkg.task;

import java.io.*;

import kyPkg.uFile.FileObj;
import kyPkg.uFile.FileUtil;

public class TaskTemplate extends Abs_ProgressTask {
	private static final String LF = System.getProperty("line.separator");

	private static final String FS = System.getProperty("file.separator");

	private long limit = Long.MAX_VALUE;

	private long cut = 20;

	private String path_I;

	private String path_O;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public TaskTemplate(String path_O, String path_I, long limit) {
		super();
		this.limit = limit;
		this.path_I = path_I;
		this.path_O = path_O;
	}

	// ------------------------------------------------------------------------
	// 外部からコールされるトリガー
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TaskTemplate", 2048);
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

			filter01(path_O, path_I);

			stop();// 正常終了

		}

		// -------------------------------------------------------------------------
		// filter
		// -------------------------------------------------------------------------
		public void filter01(String oDir, String path) {
			int seq = 1;
			int lineCount = -1;
			String wRec;

			// ---------------------------------------------------------------------
			// oDirのmkdirもやったほうが良い
			// ---------------------------------------------------------------------
			FileObj fObj = new FileObj(path);

			long fileSize = fObj.getLength() / 80; // レコード長を８０バイトと仮定

			// setLengthOfTask((int) fileSize); // プログレスバーの全体の長さ

			// ---------------------------------------------------------------------
			// Loop !!
			// ---------------------------------------------------------------------
			try {
				StringBuffer buff = new StringBuffer(256);
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(path));
				String oPath;
				FileWriter writer = null;
				long wCut = 20;

				while ((wRec = br.readLine()) != null && limit < lineCount) {
					wCut++;
					if (wCut > cut) {
						if (seq > 1)
							writer.close();
						seq++;
						oPath = oDir + FS + fObj.getName() + seq
								+ fObj.getExt(); // .tmpが無いという前提
						System.out.println("taskTemplaete 処理→" + oPath);
						writer = new java.io.FileWriter(oPath);
						wCut = 0;
					}

					setCurrent(lineCount++); // プログレスバーの位置
					buff.delete(0, buff.length());
					buff.append(wRec);
					buff.append(LF);
					writer.write(buff.toString());

				}
				br.close();
				writer.close();

			} catch (IOException ie) {
				ie.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
