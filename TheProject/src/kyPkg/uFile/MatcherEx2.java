package kyPkg.uFile;

import java.io.File;

import kyPkg.filter.EzWriter;
import kyPkg.filter.FileChecker;
import kyPkg.filter.Inf_oClosure;
import kyPkg.mySwing.ListPanel;

public class MatcherEx2 extends Matcher {
	private Inf_oClosure writer;
	// private String outPath = "./findResult.txt";// �W���ŏo�͂���錋�ʃt�@�C��
	private String outPath = null;
	private ListPanel listPanel;
	private boolean hidden = false;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
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
			fileSize = getFileSize(fileObj);// �t�@�C���̃T�C�Y���`�F�b�N
			if (limitMin <= fileSize && fileSize <= limitMax) {
				lastUpdate = getLastModified(fileObj); // �X�V���t���`�F�b�N
				// System.out.println("#debug# 20150402 bYmd:" + bYmd + " aYmd:"
				// + aYmd + " lastUpdate:" + lastUpdate + " path:"
				// + fileObj.getAbsolutePath());
				if ((bYmd < 0 && aYmd < 0) || (bYmd < 0 && lastUpdate <= aYmd)
						|| (bYmd <= lastUpdate && aYmd < 0)
						|| (bYmd <= lastUpdate && lastUpdate <= aYmd)) {
					String absPath = fileObj.getAbsolutePath();
					// System.out.println("#debug# 20150402 absPath:" +
					// absPath);

					// �R���e���c���ɃL�[���[�h�����݂��邩�ǂ������ׂ�
					// TODO ���������p�^�[���������w�肳��Ă��Ȃ���΁Anew�������Ȃ��A�������ɒʉ߂�������

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
