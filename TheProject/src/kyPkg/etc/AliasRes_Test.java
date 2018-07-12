package kyPkg.etc;

import globals.ResControl;
import globals.ResControlWeb;

public class AliasRes_Test {

	public AliasRes_Test() {
		// TODO Auto-generated constructor stub
	}
	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
//		test_CreateCurrentData();
	}

	// -------------------------------------------------------------------------
	// test
	// -------------------------------------------------------------------------
//	public static void test00_batchConvert() {
//		String aliasDir = "";
//		aliasDir = "G:/s2/rx/Enquetes/NQ/���𒲍��i�l�j/2009";
//		System.out.println("outDir:" + batchConvert(aliasDir));
//		aliasDir = "G:/s2/rx/Enquetes/NQ/���𒲍��i���сj/2009";
//		System.out.println("outDir:" + batchConvert(aliasDir));
//	}

	public static void test01_saveAsAtomics() {
		String aliasDir = ResControl.ENQ_DIR + "NQ/�����E���N���/2007";
		String[] fields = { "A23", "A10", "A22" }; // ���ڃL�[�̖��O
		String outDir = ResControlWeb.getD_Resources_ATOM(""); // �o�̓f�B���N�g���p�X
		String outName = "������1201_1264";
		// --------------------------------------------------------------
		System.out.println("#20130402#checkpoint 011");

		AliasRes aliasRes = new AliasRes(aliasDir);
		aliasRes.saveAsAtomics(outDir + outName, fields);
	}

	public static void test02_saveAsAtomics() {
		// XXX ���^�f�[�^�����ɂ͂ǂ������炢�����l����
		String aliasDir = "Z:/s2/rx/Idxc/Enquete/�����E���C�t�X�^�C����/2007";
		String[] fields = { "q1", "q2", "q3", "q4", "q5", "q6", "q7", "q47",
				"q48", "q49", "q50", "cluster" }; // ���ڃL�[�̖��O
		String outDir = globals.ResControlWeb.getD_Resources_ATOM(""); // �o�̓f�B���N�g���p�X
		String outName = "bonvoyage";
		System.out.println("#20130402#checkpoint 012");

		AliasRes aliasRes = new AliasRes(aliasDir);
		aliasRes.saveAsAtomics(outDir + outName, fields);
	}

//	public static void test1019_saveAsAtomicsWrap() {
//		String outname1 = AliasRes.saveAsAtomicsWrap("./atom/",
//				"NQ/�����E���N���/2007/", "A23,A10,A22");
//		System.out.println("outname1:" + outname1);
//	}
//
//	public static void testSaveAsAtomicsWrap() {
//		String atomDir = ResControl.getAtomDir();
//		String levs = "�p�o�q�A���P�[�g/�����E���N���/2009/";
//		String field = "A01,A02";
//		String outname1 = AliasRes.saveAsAtomicsWrap(atomDir, levs, field);
//		System.out.println("outname1:" + outname1);
//	}

	// ------------------------------------------------------------------------
	// �W�v���Ԃɓ��������邽�߂ɏW�v�Ώۊ��Ԃ̏I���N���x�[�X��
	// �wcurrentP.txt�x�ˁwcurrentP2014.txt�x�ւƓ��e������������������
	// ------------------------------------------------------------------------
//	public static void test_CreateCurrentData() {
//	String pPath = "C:/@qpr/home/Personal/MonSets/currentP2009.txt";
//		createCurrentData(pPath);
//	}
	public static void testxxx() {
		// G:/s2/rx/Enquetes/NQ/���𒲍��i���сj/2008
		// G:/s2/rx/Enquetes/NQ/���𒲍��i�l�j/2008

		String aliasDir = "";
		String outDir = "";// �o�̓f�B���N�g���p�X

		aliasDir = "G:/s2/rx/Enquetes/NQ/���𒲍��i���сj/2008";
		outDir = ResControl.getUsersEnqDir() + "���𒲍��i���сj/2008/";

		// aliasDir = "G:/s2/rx/Enquetes/NQ/���𒲍��i�l�j/2008";
		// outDir = ResControl.getUsersEnqDir() + "���𒲍��i�l�j/2008/";

		// aliasDir = "G:/s2/rx/Enquetes/common/�u�����h���[�U�[�i�r�[���n���j/2008(10-12)";
		// outDir = ResControl.getUsersEnqDir() +
		// "�u�����h���[�U�[�i�r�[���n���j/2008(10-12)/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/common/�u�����h���[�U�[�i�r�[���n���j/2008(07-09)";
		// outDir = ResControl.getUsersEnqDir() +
		// "�u�����h���[�U�[�i�r�[���n���j/2008(07-09)/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/common/�u�����h���[�U�[�i�r�[���n���j/2008(04-06)";
		// outDir = ResControl.getUsersEnqDir() +
		// "�u�����h���[�U�[�i�r�[���n���j/2008(04-06)/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/common/�u�����h���[�U�[�i�r�[���n���j/2008(01-03)";
		// outDir = ResControl.getUsersEnqDir() +
		// "�u�����h���[�U�[�i�r�[���n���j/2008(01-03)/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/NQ/���������P/2008";
		// outDir = ResControl.getUsersEnqDir() + "���������P/2008/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/NQ/���������Q/2008";
		// outDir = ResControl.getUsersEnqDir() + "���������Q/2008/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/NQ/���������R/2008";
		// outDir = ResControl.getUsersEnqDir() + "���������R/2008/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/NQ/���������S/2008";
		// outDir = ResControl.getUsersEnqDir() + "���������S/2008/";
		System.out.println("#20130402#checkpoint 013");

		AliasRes aliasRes = new AliasRes(aliasDir);
		aliasRes.saveAsStandAlone(outDir);
	}
	
}
