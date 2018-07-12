package kyPkg.uFile;

import static kyPkg.uFile.YamlControl.YML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

import globals.ResControl;
import kyPkg.atoms.AtomDB;

public class MondSetUtil {
	// ------------------------------------------------------------------------
	// ���q�d�o�̃��j�^�[���o�������A�V�������o�������C���|�[�g����
	// ���o�����̃R�����g�̓��^�f�[�^�Ƃ��ĕʃt�@�C���ɏo�͂��Ă���
	// ------------------------------------------------------------------------
	public static void openMonSetDir() {
		String monSetsDir = ResControl.getMonSetsDir();
		DosEmu.openWithExplorer(" /n,/e,/root," + monSetsDir);
	}

	// ------------------------------------------------------------------------
	// �����������Z���N�g�������limit���\�[�X�𐶐�����
	// XXX ���ƂŃC���^�t�F�[�X����������
	// ------------------------------------------------------------------------
	public static int importFromMonBank(String bankName) {
		if (bankName == null || bankName.equals("�w�肵�Ȃ�"))
			return -1;
		// TODO ����ID�ȊO�̂��̂��w�肳��Ă���P�[�X������̂Œ���
		// �����Ƃ��΁@���C�n�����j�^�[�Q�@�Ȃǂ�id�ł͂Ȃ��z�X�g�̃o�b�`�X�e�b�v���L�q����Ă���

		// XXX 110811 �n���������Q�̓o�b�`�������ċ��p���Ă��悢��������Ȃ�
		// XXX 110811 ���̃A���P�[�g������ʓI�Ȃ��̂͂܂Ƃ߂ăo�b�`�������ċ��p����̂��悢���낤

		String userDir = ResControl.getQPRHome();
		String limitPath = userDir + "Limit" + AtomDB.LIM;
		String ymlPath = userDir + "Limit." + YML;

		String monSetsDir = ResControl.getMonSetsDir();
		//	String bankPath =ResControl.getMonBankDir(bankName);
		String path = monSetsDir + bankName;
		System.out.println("monBank:" + path);
		System.out.println("limitPath:" + limitPath);

		// ------------------------------------------------------------------------
		// �R�����Ă��limit�������ɕϊ�����Ⴂ���̂�
		// �ȈՓǂݍ��݂��s���擪�Ƃ����܂��̂���Ȃ��Ƃ���������؂�΂������ȁH
		// ------------------------------------------------------------------------
		// //MONSCPY.COMMENT DD *
		// �m�p�P�g�ҁ@�@�@�@�@�@�@�@�@�@
		// //MONSCPY.SYSIN DD *
		// /*
		// ------------------------------------------------------------------------
		String comment = "";
		StringBuffer buff = new StringBuffer();
		String key = "//MONSCPY.COMMENT DD * ";
		String begin = "//MONSCPY.SYSIN DD * ";
		String end = "/* ";
		String LE = "\n";
		int cnt = 0;
		try {
			boolean flag1 = false;
			boolean flag2 = false;
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter bw = new BufferedWriter(new FileWriter(limitPath));
			while (br.ready()) {
				String rec = br.readLine();
				if (rec.startsWith(begin)) {
					flag1 = false;
					flag2 = true;
				} else if (rec.startsWith(end)) {
					flag1 = false;
					flag2 = false;
				} else if (rec.startsWith(key)) {
					flag1 = true;
				} else {
					// ���o�����R�����g
					if (flag1) {
						buff.append(rec.trim());
					}
					// Monitor ID
					if (flag2) {
						cnt++;
						bw.write(rec);
						bw.write(LE);
					}
				}
			}
			bw.close();
			br.close();
		} catch (Exception e) {
		}
		comment = buff.toString();
		System.out.println("comment:" + comment);
		// comment => ������yml�ɕۑ��E�E�E����
		HashMap<String, String> infoMap = new HashMap();
		infoMap.put(AtomDB.COMMENT1, comment);
		new YamlControl(infoMap).saveAs(ymlPath);
		return cnt;
	}
}
