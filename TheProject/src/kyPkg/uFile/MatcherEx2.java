package kyPkg.uFile;

import java.io.File;

import kyPkg.filter.EzWriter;
import kyPkg.filter.FileChecker;
import kyPkg.filter.Inf_oClosure;
import kyPkg.mySwing.ListPanel;

public class MatcherEx2 extends Matcher {
	private Inf_oClosure writer;
	// private String outPath = "./findResult.txt";// 標準で出力される結果ファイル
	private String outPath = null;
	private ListPanel listPanel;
	private boolean hidden = false;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public MatcherEx2(String outPath, String keyword, boolean ignoreCase,
			long limitMin, long limitMax, int bYmd, int aYmd,
			ListPanel listPanel) {
		super(keyword, ignoreCase, limitMin, limitMax, bYmd, aYmd);
		this.outPath = outPath;
		this.listPanel = listPanel;
	}

	@Override
	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	public void init() {
		if (listPanel != null)
			listPanel.clear();

		if (outPath != null && !outPath.equals("")) {
			writer = new EzWriter(outPath);
			writer.open();
		}
	}

	@Override
	// ------------------------------------------------------------------------
	// onMatch
	// ------------------------------------------------------------------------
	public void onMatch(File fileObj) {
		if (fileObj.isFile()) {
			if (fileObj.isHidden()) {
				if (hidden == false)
					return;
			}
			fileSize = getFileSize(fileObj);// ファイルのサイズをチェック
			if (limitMin <= fileSize && fileSize <= limitMax) {
				lastUpdate = getLastModified(fileObj); // 更新日付をチェック
				// System.out.println("#debug# 20150402 bYmd:" + bYmd + " aYmd:"
				// + aYmd + " lastUpdate:" + lastUpdate + " path:"
				// + fileObj.getAbsolutePath());
				if ((bYmd < 0 && aYmd < 0) || (bYmd < 0 && lastUpdate <= aYmd)
						|| (bYmd <= lastUpdate && aYmd < 0)
						|| (bYmd <= lastUpdate && lastUpdate <= aYmd)) {
					String absPath = fileObj.getAbsolutePath();
					// System.out.println("#debug# 20150402 absPath:" +
					// absPath);

					// コンテンツ内にキーワードが存在するかどうか調べる
					// TODO そもそもパターンが何も指定されていなければ、newしたくない、無条件に通過させたい

					FileChecker checker = new FileChecker(absPath, pattern);
					checker.execute();
					if (checker.isPatternFound()) {
						if (writer != null)
							writer.write(absPath);
						if (listPanel != null) {
							listPanel.setVisible(false);
							listPanel.addElement(absPath);
							listPanel.setVisible(true);
						}

					}

				}
			}
		}
	};

	@Override
	public void fin() {
		writer.close();
	}
}
