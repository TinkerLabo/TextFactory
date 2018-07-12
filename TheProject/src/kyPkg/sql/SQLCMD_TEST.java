package kyPkg.sql;

public class SQLCMD_TEST {
	// ------------------------------------------------------------------------
	// �e�X�g (���ۂ͉��L�̂悤��yml�t�@�C���������Ɏg���悤�ȃC���[�W)�E�E�E�������͏o�͐�
	// ------------------------------------------------------------------------
	// java -jar T:\QPR\SQLCMD.jar "T:\QPR\ITEM_MM_Export.yml" 
	// "T:\QPR\dat\MM_JAN.VAR"
	// �@��L�Ńp�����[�^�Ɏg�p���Ă���@"T:\QPR\ITEM_MM_Export.yml"�@�̓��e�͈ȉ��̒ʂ�
	// -<��������>----------------------------------------------
	// sql SELECT 'D2','01',' ','1',' ',' ',' ',XA1,XA2,XA3,XC3,'
	// ',XC4,XC2,XD2,XF1,XH1,XH2,XF2,XF3,XF4,XF5,XJ1,XJ2,XM1,XM2,XM3,XM4,'0','0','
	// ','0000'
	// FROM ITEM_MM;
	// -<�����܂�>----------------------------------------------
	// Eclipse�ォ�瓯������������ꍇ�͉��L�̂Ƃ���i�����e��yml�ASQLCMD_TEST.yml��p�ӂ��Ď��s����j
	// ------------------------------------------------------------------------
	public static void test20150313() {
		String ymlPath = "c:/SQLCMD_TEST.yml";
		String outPath = "c:/SQLCMD_RESULT.txt";
		SQLCMD ins = new SQLCMD(new String[] { ymlPath, outPath });
		ins.execute();
	}
	public static void test20160610() {
		String ymlPath = "C:/samples/murakamiSelmon/�u���E�N��i�P�O�΋敪�j�v���u�j�P�O��v.YML";
		String outPath = "c:/SQLCMD_RESULT.txt";
		SQLCMD ins = new SQLCMD(new String[] { ymlPath, outPath });
		ins.execute();
	}

	public static void main(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SQLCMD Elapse");
		elapse.start();

//		test20150313();
		test20160610();

		String comment = "";
		elapse.setComment(comment);
		elapse.stop();
	}
}
