package kyPkg.task;

import java.io.*;

import kyPkg.uFile.FileObj;

public class TaskFileCutter extends Abs_ProgressTask {
	private static final String LF = System.getProperty("line.separator");
	private static final String FS = System.getProperty("file.separator");
	private long limit = Long.MAX_VALUE;
	private long cut = Long.MAX_VALUE;
	private String head = "";
	private String foot = "";
	private String iPath;
	private String oDir;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public TaskFileCutter(String oDir, String iPath, int cut) {
		this(oDir, iPath, (long) cut);
	}

	public TaskFileCutter(String oDir, String iPath, long cut) {
		super();
		this.oDir = oDir;
		this.iPath = iPath;
		this.cut = cut;
	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public void setFoot(String foot) {
		this.foot = foot;
	}

	public void setHead(String head) {
		this.head = head;
	}

	// ------------------------------------------------------------------------
	// 外部からコールされるトリガー
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TaskFileCutter",2048);
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
	// for 単体テスト
	// T:\eclipse\workspace\kyProject\kkk\qpr_buying_data_20080316.txt
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
//		Inf_ProgressTask ins = new TaskFileCutter("c:/divIt", ResControl.D_DAT + Mrg_Tran.NQDATA_DAT,
//				500000);
//		((TaskFileCutter) ins).setFoot("20080317091554	20080317091554	0500000");
//		ins.execute();
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

			filter01(oDir, iPath);

			stop();// 正常終了

		}

		// -------------------------------------------------------------------------
		// filter
		// -------------------------------------------------------------------------
		public void filter01(String oDir, String iPath) {

			int seq = 0;
			int lineCount = -1;
			String wRec;

			// ---------------------------------------------------------------------
			// oDirのmkdirもやったほうが良い
			// ---------------------------------------------------------------------
			FileObj fObj = new FileObj(iPath);

			long fileSize = fObj.getLength() / 80; // レコード長を８０バイトと仮定

			// setLengthOfTask((int) fileSize); // プログレスバーの全体の長さ

			// ---------------------------------------------------------------------
			// Loop !!
			// ---------------------------------------------------------------------
			try {
				BufferedReader br = new BufferedReader(new FileReader(iPath));
				String oPath;
				FileWriter writer = null;
				long wCut = cut;

				while ((wRec = br.readLine()) != null && limit > lineCount) {
					wCut++;
					if (wCut > cut) {
						if (writer != null) {
							if (!foot.equals("")) {
								writer.write(foot);
								writer.write(LF);
							}
							writer.close();
						}
						seq++;
						oPath = oDir + FS + fObj.getName() + "_" + seq
								+ fObj.getExt();
						System.out.println("TaskFileCutter 処理→" + oPath);
						writer = new java.io.FileWriter(oPath);
						if (!head.equals("")) {
							writer.write(head);
							writer.write(LF);
						}
						wCut = 1;
					}
					setCurrent(lineCount++); // プログレスバーの位置
					writer.write(wRec);
					writer.write(LF);
				}
				br.close();
				if (writer != null) {
					if (!foot.equals("")) {
						writer.write(foot);
						writer.write(LF);
					}
					writer.close();
				}
				System.out.println("完了");
			} catch (IOException ie) {
				ie.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
