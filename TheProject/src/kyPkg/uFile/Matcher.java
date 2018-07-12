package kyPkg.uFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import kyPkg.uRegex.Regex;

//############################################################################
//ファイル名がマッチした時に処理をするクラスのインタフェース
//############################################################################
public abstract  class Matcher implements Inf_OnMatch {
	private SimpleDateFormat sdFmt = null;
	protected int bYmd = -1;// before ymd
	protected int aYmd = -1;// after ymd
	protected long limitMin = 0;// 1k以下をdefault
	protected long limitMax = 1024;// 1k以下をdefault
	// ------------------------------------------------------------------------
	protected Pattern pattern;
	protected long fileSize;
	protected int lastUpdate;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public Matcher(String keyword, boolean ignoreCase, long limitMin, long limitMax, int bYmd,
			int aYmd) {
		super();
		this.limitMin = limitMin;
		this.limitMax = limitMax;
		this.bYmd = bYmd;
		this.aYmd = aYmd;
		System.out.println("From:" + bYmd + " to:" + aYmd);

		// コンテンツ内を検索するキーワード,大文字小文字を区別する場合true
		pattern = Regex.getPatternEx(keyword, ignoreCase);

	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public void setLimit(long limit) {
		this.limitMax = limit;
	}

	/**
	 * ファイル情報:ファイルサイズを取得
	 */
	public long getFileSize(File fileObj) {
		return fileObj.length();
	}



	/**
	 * ファイル情報:最終更新日時を取得
	 */
	public int getLastModified(File fileObj) {
		if (sdFmt == null)
			sdFmt = new SimpleDateFormat("yyyyMMdd");
		return Integer.valueOf(sdFmt.format(new Date(fileObj.lastModified())));
	}


	@Override
	public void forDebug(String val) {
		System.out.println("#forDebug #=>" + val);
	};
}