package kyPkg.filter;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.converter.Corpus;

public class TextFactory_Test {
	// -------------------------------------------------------------------------
	// �o�b�`�N������ꍇ�̗�
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		// testerY();
		// test0717();
		jicfsConvert();
	}

	// -------------------------------------------------------------------------
	// �p�����[�^�t�@�C���𗘗p�����
	// -------------------------------------------------------------------------
	public static void testerZ() {
		String sys100 = ResControlWeb
				.getD_Resources_QPR("qpr_monitor_info_2008XXXX.txt");
		String sys200 = ResControlWeb.getD_Resources_Templates("MONNew.txt");
		String sys110 = ResControlWeb.getD_Resources_QPR("Monp.csv");
		kyPkg.converter.SubstrCnv converter = new kyPkg.converter.SubstrCnv(sys110);
		TextFactory ins = new TextFactory(sys200, sys100, converter);
		ins.execute();
	}

	// -------------------------------------------------------------------------
	// csv���Œ蒷�t�@�C���ɕϊ������i�p�����[�^�͕����z��j
	// -------------------------------------------------------------------------
	public static void testerX() {
		String sys300 = ResControlWeb.getD_Resources_Templates("clusterMIX.csv");
		String sys400 = ResControlWeb.getD_Resources_Templates("clusterFIX.TXT");

		String[] paramArray = { "1,1,8," + Corpus.FIX_HALF + ",\t���j�^�[�h�c",
				"2,1,1," + Corpus.FIX_HALF + ",\t�Q�O�O�V�N�x���C�t�X�^�C���N���X�^",
				"3,1,1," + Corpus.FIX_HALF + ",\t�Q�O�O�W�N�x���C�t�X�^�C���N���X�^",
				"4,1,1," + Corpus.FIX_HALF + ",\t",
				"5,1,1," + Corpus.FIX_HALF + ",\t",
				"6,1,1," + Corpus.FIX_HALF + ",\t",
				"7,1,1," + Corpus.FIX_HALF + ",\t",
				"8,1,1," + Corpus.FIX_HALF + ",\t",
				"9,1,1," + Corpus.FIX_HALF + ",\t",
				"10,1,1," + Corpus.FIX_HALF + ",\t",
				"11,1,1," + Corpus.FIX_HALF + ",\t",
				"12,1,1," + Corpus.FIX_HALF + ",\t",
				"13,1,1," + Corpus.FIX_HALF + ",\t",
				"15,1,60," + Corpus.FIX_HALF + ",\t�c�t�l�l�x", };
		kyPkg.converter.SubstrCnv converter = new kyPkg.converter.SubstrCnv(paramArray);

		TextFactory substr = new TextFactory(sys400, sys300, converter);
		substr.setRedersDelimiter(",");
		substr.execute();
	}

	// -------------------------------------------------------------------------
	// �Œ蒷����^�u��؂�e�L�X�g�ɕϊ������i�p�����[�^�͕����z��j
	// -------------------------------------------------------------------------
	public static void testerY() {
		String sys100 = ResControlWeb.getD_Resources_QPR("MONNew.txt");
		String sys201 = ResControlWeb.getD_Resources_Templates("testerY.txt");

		String[] paramArray = { "1,1,8,�Œ�O�[��,\t���j�^ID", "D,�J���}\t",
				"1,1,1," + Corpus.FIX_HALF + ",\t���C�t�X�^�C���N���X�^1", "D,�^�u\t",
				"1,2,2," + Corpus.FIX_HALF + ",\t���C�t�X�^�C���N���X�^2", "D,�J���}\t",
				"1,3,3," + Corpus.FIX_HALF + ",\t���C�t�X�^�C���N���X�^3", "D,�^�u\t",
				"1,4,4," + Corpus.FIX_HALF + ",\t���C�t�X�^�C���N���X�^4", "D,�J���}\t",
				"1,5,5," + Corpus.FIX_HALF + ",\t���C�t�X�^�C���N���X�^5", "D,�^�u\t",
				"1,6,6," + Corpus.FIX_HALF + ",\t���C�t�X�^�C���N���X�^6", "D,�J���}\t",
				"1,7,7," + Corpus.FIX_HALF + ",\t���C�t�X�^�C���N���X�^7", "D,�^�u\t",
				"1,8,8," + Corpus.FIX_HALF + ",\t���C�t�X�^�C���N���X�^8", };
		kyPkg.converter.SubstrCnv converter = new kyPkg.converter.SubstrCnv(paramArray);

		TextFactory substr = new TextFactory(sys201, sys100, converter);
		// 1�Z���߂́A0�����߂���A1�����̒������A2�Ŏn�܂�Ȃ�Ƀ}�b�`������A�X�e�[�^�X�ɂP�U��������
		// RegFilter filter = new RegFilter(1, 0, 1, 16, "^2", true);
		// 0�Z���߂́A0�����߂���A1�����̒������A7�Ŏn�܂�Ȃ�Ƀ}�b�`������A�X�e�[�^�X�ɂP�U��������
		RegChecker filter = new RegChecker(0, 0, 1, "^7", 16);
		substr.setFilter(filter, 16); // �X�e�[�^�X�l�̍��v���P�U�ȏ�Ȃ�o�͂���
		substr.execute();

		// <<����e�X�g>> �ȑO�̏������ʂƓ������ǂ����̊m�F
		String sys202 = ResControlWeb.getD_Resources_Templates("testerY.org");
		String msg = new kyPkg.tools.Compare(sys201, sys202, 10, false)
				.compareAndGetStatRez();
		if (!msg.equals(""))
			System.out.println(msg);
	}

	public static void test0717() {
		String enqData = "N:/datas/Lotte09Spring/8001.csv";
		String parmPath = "N:/PowerBX/Lotte09Spring/T8001_���b�e�K��/subParm.csv";
		String outPath = "N:/PowerBX/Lotte09Spring/T8001_���b�e�K��/ASM.TXT";
		System.out.println(" enqData:" + enqData);
		System.out.println(" parmPath:" + parmPath);
		System.out.println(" outPath:" + outPath);
		// �p�����[�^�t�@�C���̍쐬�����܂��ł������H�ȂǂȂǊm�F��E�E���X�̏����������ɏ��� 
		TextFactory insSub = new TextFactory(outPath, enqData,
				new kyPkg.converter.SubstrCnv(parmPath));
		// insSub.setRedersDelimiter(COMMA);
		insSub.headerOption(true);
		insSub.execute();
	}

	// 2010/08/31 JICFS_New_Format_Data convert to
	// Jicfs_Classic_Format_Data(HOST)
	public static void jicfsConvert() {
		String userDir = globals.ResControl.getQPRHome();
		String iPathJ = userDir + "�f�X�N�g�b�v/JMAK�ϊ�����/JAN/EA1.txt";
		String pPathJ = userDir + "�f�X�N�g�b�v/JMAK�ϊ�����/JAN/JanCnvP.txt";
		String oPathJ = ResControl.D_DAT + "JICFS_JAN.DAT";
		System.out.println(" iPath:" + iPathJ);
		System.out.println(" pPath:" + pPathJ);
		System.out.println(" oPath:" + oPathJ);
		TextFactory insSubJ = new TextFactory(oPathJ, iPathJ,
				new kyPkg.converter.SubstrCnv(pPathJ));
		insSubJ.execute();

		String iPathS = userDir + "�f�X�N�g�b�v/JMAK�ϊ�����/�Z�k/ED12.txt";
		String pPathS = userDir + "�f�X�N�g�b�v/JMAK�ϊ�����/�Z�k/makCnvP_Short.txt";
		String oPathS = ResControl.D_DAT + "JICFS_MAK_SHORT.DAT";
		System.out.println(" iPath:" + iPathS);
		System.out.println(" pPath:" + pPathS);
		System.out.println(" oPath:" + oPathS);
		TextFactory insSubS = new TextFactory(oPathS, iPathS,
				new kyPkg.converter.SubstrCnv(pPathS));
		insSubS.execute();
		String iPathN = userDir + "�f�X�N�g�b�v/JMAK�ϊ�����/�W��/ED11.txt";
		String pPathN = userDir + "�f�X�N�g�b�v/JMAK�ϊ�����/�W��/makCnvP_Stdrd.txt";
		String oPathN = ResControl.D_DAT + "JICFS_MAK_STDRD.DAT";
		System.out.println(" iPath:" + iPathN);
		System.out.println(" pPath:" + pPathN);
		System.out.println(" oPath:" + oPathN);
		TextFactory insSubN = new TextFactory(oPathN, iPathN,
				new kyPkg.converter.SubstrCnv(pPathN));
		insSubN.execute();
	}
}
