package kyPkg.uFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import kyPkg.uRegex.Regex;

//############################################################################
//�t�@�C�������}�b�`�������ɏ���������N���X�̃C���^�t�F�[�X
//############################################################################
public abstract  class Matcher implements Inf_OnMatch {
	private SimpleDateFormat sdFmt = null;
	protected int bYmd = -1;// before ymd
	protected int aYmd = -1;// after ymd
	protected long limitMin = 0;// 1k�ȉ���default
	protected long limitMax = 1024;// 1k�ȉ���default
	// ------------------------------------------------------------------------
	protected Pattern pattern;
	protected long fileSize;
	protected int lastUpdate;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public Matcher(String keyword, boolean ignoreCase, long limitMin, long limitMax, int bYmd,
			int aYmd) {
		super();
		this.limitMin = limitMin;
		this.limitMax = limitMax;
		this.bYmd = bYmd;
		this.aYmd = aYmd;
		System.out.println("From:" + bYmd + " to:" + aYmd);

		// �R���e���c������������L�[���[�h,�啶������������ʂ���ꍇtrue
		pattern = Regex.getPatternEx(keyword, ignoreCase);

	}

	// ------------------------------------------------------------------------
	// �A�N�Z�b�T
	// ------------------------------------------------------------------------
	public void setLimit(long limit) {
		this.limitMax = limit;
	}

	/**
	 * �t�@�C�����:�t�@�C���T�C�Y���擾
	 */
	public long getFileSize(File fileObj) {
		return fileObj.length();
	}



	/**
	 * �t�@�C�����:�ŏI�X�V�������擾
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