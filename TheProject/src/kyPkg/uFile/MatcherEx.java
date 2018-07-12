package kyPkg.uFile;

import java.io.File;

import kyPkg.filter.EzWriter;
import kyPkg.filter.FileChecker;
import kyPkg.filter.Inf_oClosure;

public class MatcherEx extends Matcher {
	private Inf_oClosure writer;
	private String outPath = "./findResult.txt";// �W���ŏo�͂���錋�ʃt�@�C��

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
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
			fileSize = getFileSize(fileObj);// �t�@�C���̃T�C�Y���`�F�b�N
			if (fileSize < limitMax) {
				lastUpdate = getLastModified(fileObj); // �X�V���t���`�F�b�N
				if ((bYmd < 0 && aYmd < 0) || (bYmd < 0 && lastUpdate <= aYmd)
						|| (bYmd <= lastUpdate && aYmd < 0)) {
					String absPath = fileObj.getAbsolutePath();

					// �R���e���c���ɃL�[���[�h�����݂��邩�ǂ������ׂ�
					// TODO ���������p�^�[���������w�肳��Ă��Ȃ���΁Anew�������Ȃ��A�������ɒʉ߂�������
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
