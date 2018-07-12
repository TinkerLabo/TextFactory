package kyPkg.uFile;

import java.io.File;
import java.util.Arrays;
import kyPkg.uDateTime.DateCalc;

public class BackupUtil {

	private static final String BAK = "bak";

	public BackupUtil() {
	}

	// ------------------------------------------------------------------------
	// �g���q��ύX����
	// ------------------------------------------------------------------------
	public static String changeExt(String path, String ext) {
		int pos = path.lastIndexOf(".");
		return path.substring(0, pos) + ext;
	}

	// ------------------------------------------------------------------------
	// �o�b�N�A�b�v���R�s�[�����
	//20170327 �^�C���X�^���v�ł͂Ȃ��@�Ώۃt�@�C���̍ŏI�C�����t���^�C���X�^���v�Ƃ���
	// ------------------------------------------------------------------------
	private static String createBackup(String path, String optStr) {
		if (path.trim().equals("") || new File(path).isFile() == false)
			return null;
		String timeStmp = DateCalc.getLastModDate(path, "yyyyMMddHHmmss");
		//String timeStmp = getTimeStamp();
		String bkup = changeExt(path, "_" + timeStmp + optStr + "." + BAK);
		DosEmu.copy(path, bkup);//		DosEmu.move(orgPath, bkup);
		return bkup;
	}

	// ------------------------------------------------------------------------
	// �g���q��ύX����
	// ------------------------------------------------------------------------
	//�o�̓t�@�C�����Ɠ������̂Ŏn�܂���_�������t�@�C���Ő���Ǘ����s��
	//	BackupUtil.createBackup(String path, int maxGen)
	// ------------------------------------------------------------------------
	public static String createBackup(String path, int maxGen) {
		//���̓t�@�C���̖��̂Ƀ^�C���X�^���v��t���������̂̃o�b�N�A�b�v���쐬����
		//���s�҂̃��[�U�[�h�c���t�����邩�ǂ�����������
		String uerId = kyPkg.util.RuntimeEnv.getUserID();
		String bkPath = createBackup(path, uerId);
		if (bkPath == null)
			return null;
		String dir = FileUtil.getParent(path);
		String firstName = FileUtil.getFirstName2(path);
		System.out.println("Dir:" + dir + " FirstName:" + firstName);
		String[] array = FileUtil.regixFilteredList(dir,
				firstName + "_.*" + BAK);
		Arrays.sort(array); //SORT ���t�̐V�������͎̂c��
		int j = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			j++;
			if (j > maxGen) {
				String wPath = dir + "/" + array[i];
				System.err.println("kil=>" + wPath);
				if (new File(wPath).exists())
					new File(wPath).delete();
			} else {
				System.out.println("kpt=>" + array[i]);
			}

		}
		return bkPath;
	}

	public static void main(String[] argv) {
		int maxGen = 5;
		String dstPath = "c:/test.txt";
		createBackup(dstPath, maxGen);//���������ꍇ�ǂ��Ȃ邩���E�E�E
	}

}
