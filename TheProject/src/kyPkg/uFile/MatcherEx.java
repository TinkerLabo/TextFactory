package kyPkg.uFile;

import java.io.File;

import kyPkg.filter.EzWriter;
import kyPkg.filter.FileChecker;
import kyPkg.filter.Inf_oClosure;

public class MatcherEx extends Matcher {
	private Inf_oClosure writer;
	private String outPath = "./findResult.txt";// 標準で出力される結果ファイル

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public MatcherEx(String keyword, boolean ignoreCase, long limitMin, long limitMax, int bYmd,
			int aYmd) {
		super(keyword, ignoreCase,limitMin, limitMax, bYmd, aYmd);
	}

	@Override
	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	public void init() {
		writer = new EzWriter(outPath);
		writer.open();
	}

	@Override
	// ------------------------------------------------------------------------
	// onMatch
	// ------------------------------------------------------------------------
	public void onMatch(File fileObj) {
		if (fileObj.isFile()) {
			fileSize = getFileSize(fileObj);// ファイルのサイズをチェック
			if (fileSize < limitMax) {
				lastUpdate = getLastModified(fileObj); // 更新日付をチェック
				if ((bYmd < 0 && aYmd < 0) || (bYmd < 0 && lastUpdate <= aYmd)
						|| (bYmd <= lastUpdate && aYmd < 0)) {
					String absPath = fileObj.getAbsolutePath();

					// コンテンツ内にキーワードが存在するかどうか調べる
					// TODO そもそもパターンが何も指定されていなければ、newしたくない、無条件に通過させたい
					FileChecker checker = new FileChecker(absPath, pattern);
					checker.execute();
					if (checker.isPatternFound()) {
						System.out.println(absPath);
						if (writer != null)
							writer.write(absPath);

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
