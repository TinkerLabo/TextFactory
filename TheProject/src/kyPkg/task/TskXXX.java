package kyPkg.task;

import kyPkg.uFile.Digger;
import kyPkg.uFile.FileUtil;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*; // ←オプションダイアログを使用したので・・
import java.util.regex.*;

//-----------------------------------------------------------------------------
// ファイル編集雛形                                        K.Yuasa 2006.2.13            update 2007.5.16
//-----------------------------------------------------------------------------
public class TskXXX extends Abs_ProgressTask {
	private static final String LF = System.getProperty("line.separator");
	private static final String FS = System.getProperty("file.separator");
	private String[] fileArray;
	private String gParent;
	private String gBef[];
	private String gAft[];
	private String gRgx;
	private ArrayList gArray; // 処理ファイル一覧

	// -------------------------------------------------------------------------
	public TskXXX(String[] args) {
		this(args[0], new String[] { args[1] }, null, args[2], false);
	}

	public TskXXX(String pPath, String pBef[], String pAft[], String pRgx,
			boolean pFlg) {
		super();
		gBef = pBef;
		gAft = pAft;
		gRgx = pRgx; // ex. ".*\\.sql"
		// ---------------------------------------------------------------------
		// 指定したディレクトリ以下のファイルリストを検索するサブルーチン
		// ---------------------------------------------------------------------
		Digger digger = new Digger(pPath, gRgx, pFlg);
		digger.search();
		fileArray = digger.getFileArray();

		gArray = new ArrayList(fileArray.length); // 予め適当な大きめに作成
		gParent = FileUtil.getParent(new File(pPath).getAbsolutePath());
		gParent = gParent + FS + "conv";
	}

	// ------------------------------------------------------------------------
	// 外部からコールされるトリガー
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TskXXX", 2048);
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

	};

	// -------------------------------------------------------------------------
	class ActualTask {
		ActualTask() {
			boolean wDirty;
			System.out.println("## ActualTask ## on TskXXX");
			gArray.clear();
			// -----------------------------------------------------------------
			for (int i = 0; i < fileArray.length; i++) {
				// -------------------------------------------------------------
				// プログレスバーを見せる為の時間稼ぎ ・・・
				// -------------------------------------------------------------
				try {
					Thread.sleep(1); // sleep for a second
				} catch (InterruptedException e) {
					System.out.println("ActualTask interrupted");
				}
				String wPath_I = (String) fileArray[i];
				setCurrent(i); // make some progress
				setMessage(" (" + getCurrent() + "/" + getTotalLen() + ")"
						+ wPath_I);
				// -------------------------------------------------------------
				// 実処理 　
				// -------------------------------------------------------------
				wDirty = filterT(wPath_I, gBef, gAft); // 【処理】
				if (wDirty) {
					gArray.add(wPath_I);
				}
				// ※ 処理対象ファイルをArraylistに格納していくほうが良いと思う（同時にカウントも取れる）
				// ※ 処理が終わった時点で結果として提示する
				// ※ 変換しないで検索だけする場合も考える
			}
			stop();
			setCurrent(getTotalLen());
			String wMsg = gArray.size() + "件処理しました";
			JOptionPane.showMessageDialog((Component) null, wMsg, "Message...",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	/**
	 * filterT フィルタープログラム 指定されたファイルを読み込んで、ライターへ書き出す
	 * 
	 * @param path
	 *            入力パス
	 * @param pBef
	 *            変換前、文字列
	 * @param pAft
	 *            変換後、文字列
	 * @return 変換した場合はtrue ※予めパターンをコンパイルして、配列化しておけば・・パフォーマンスがあがる！
	 * 
	 */
	private static boolean filterT(String path, String[] pBef, String[] pAft) {
		StringBuffer buff = new StringBuffer(256);
		boolean wDirty = false;
		String rec = "";
		String preRec = "";
		String gBef[] = pBef;
		String gAft[] = pAft;
		File file = new File(path);
		if (file.isFile()) {
			try {
				String oPath = path + ".tmp"; // 同じ名前のファイルが既に存在していたらどうなる？
				FileWriter fw1 = new FileWriter(oPath);
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((rec = br.readLine()) != null) {
					preRec = rec;
					buff.delete(0, buff.length());
					for (int k = 0; k < gBef.length; k++) {
						if (!gBef[k].equals("")) {
							String wRegx1 = gBef[k];
							String wRegx2 = gAft[k];
							if (!wRegx2.equals("")) {
								Pattern ptn = Digger.patternIgnoreCase(wRegx1); // パターン作成
								rec = ptn.matcher(rec).replaceAll(wRegx2);
							}
							// wRec = wRec.replaceAll(gBef[k],gAft[k]);
						}
					}
					if (!rec.equals(preRec))
						wDirty = true;
					buff.append(rec);
					buff.append(LF);
					fw1.write(buff.toString());
				}
				br.close();
				fw1.close();
				File wFile_O = new File(oPath);
				if (wDirty) {
					// オリジナルを消してtmpをオリジナルの名称に変更
					file.delete();
					// wFile_I.renameTo(new File(pPath_I+".bak")); // バックアップを残す？
					wFile_O.renameTo(new File(path));
					System.out.println("Tsjxx 処理→" + path);
				} else {
					// tmpを消す（そもそもtmpを作らなければ良いと思うけど・・処理が怪しくなりそう）
					wFile_O.delete();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return wDirty;
	}

}
