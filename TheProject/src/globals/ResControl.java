package globals;

import java.io.File;
import java.util.List;

import kyPkg.atoms.AtomDB;
import kyPkg.uFile.DosEmu;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;

public class ResControl {
	public static final String ENQ_DIR = "Z:/S2/rx/enquetes/";

	public static final String ITP_LISTBY_USER_TXT = "itpListbyUser.txt";

	public static final String GIFTS = "10_�����̂�.lim";
	public static final String IT2 = ".IT2";
	public static final String ITP_BANKS = "itpBANKs/";
	public static final String TXT = ".txt";
	public static final String D_DRV = "T:/";//TODO	����Ő����̍X�V�����Ȃǂ𓮂����Ă݂悤���Ǝv���@2016-05-18
	public static final String D_RES = D_DRV + "resources/";//TODO	����Ő����̍X�V�����Ȃǂ𓮂����Ă݂悤���Ǝv���@2016-05-18
	public static final String D_QPR = D_DRV + "QPR/";
	public static final String D_DAT = D_QPR + "dat/";
	public static final String DSKTOP = D_DRV + "qbr/Archives/DeskTop/";
	public static final String NSEI_DIR = D_DRV
			+ "qbr/Archives/DeskTop/�����X�V�f�[�^/";

	public static final String PERSONAL = "Personal/";
	private static final String USERS_ENQ = PERSONAL + "enquetes/";
	private static final String USERS_ITP = PERSONAL + "@itp/";
	public static final String T_RES = D_RES + "User/";

	public static final String ENQNQ_DIR = ENQ_DIR + "NQ/";
	public static final String LEGACY_ENQ_DIR = ENQ_DIR + "LEGACY/";
	public static final String REPOSITORY_TXT = "REPOSITORY.TXT";
	public static final String REPOSITORY = ENQ_DIR + REPOSITORY_TXT;
	public static final String SVREPOSITORY = ENQ_DIR + "svREPOSITORY.TXT";
	public static final String ENQ_DEFAULT00 = "�p�o�q�A���P�[�g";
	public static final String ENQ_DEFAULT02 = "02_�����E���C�t�X�^�C����";
	public static final String ENQ_DEFAULT03 = "03_�����E���N���";

	public static final String PDE_DRIVE = "P:/";
	public static final String WINDOWS_ROOT = "C:/";
	public static final String LINUX_ROOT = "~/";
	public static final String CURRENT_ATM = "current.atm";
	public static final String CURRENT_TXT = "current.txt";
	public static final String CURRENTP_TXT = "currentP.txt";
	public static final String CURRENT = "current";
	private static final String QPR_ROOT = "@qpr/";
	private static final String QPR_HOME = "home/";
	private static final String CUR_DIR = "./";
	private static final String ATOM = "Atom/";
	private static final String MYDATA = "MYDATA/";
	private static final String MON_SETS = "MonSets/";
	private static final String QPR = "qpr/";
	private static final String RES = "res/";
	private static final String CNL_SETS = "CnlSets/";
	private static String rootDir = null; // �A�v���P�[�V�����̂�������
	// ------------------------------------------------------------------------
	// ResControl.getQtbDir()
	// ------------------------------------------------------------------------
	public static String getLocalItpBanks() {
		// C:/@qpr/home/Personal/itpBANKs/
		String dir = getQPRHome() + PERSONAL + ITP_BANKS;
		FileUtil.makeParents(dir);
		FileUtil.makedir(dir);
		return dir;
	}

	public static String getUSERS_ITPBANKS_MERGE() {
		String mergeDir = getLocalItpBanks() + "merge/";
		FileUtil.mkdir(mergeDir);
		return mergeDir;
	}

	public static String getUSERS_ITPBANKS_MERGE_forTest() {
		return getUSERS_ITPBANKS_MERGE();
	}

	//-------------------------------------------------------------------------
	// �T�[�o�[��̎w��i�ڃt�@�C�����w���t�@�C���p�X���`�����ĕԂ�
	//-------------------------------------------------------------------------
	public static String getItpPath_Local(String itpCode) {
		String dir = getLocalItpBanks();
		return getItpPath(dir, itpCode, true);
	}

	public static String getItpPath(String dir, String itpCode, boolean debug) {
		if (itpCode == null || itpCode.equals(""))
			return null;
		itpCode = itpCode.trim().replaceAll("#", "");
		itpCode = itpCode.trim().replaceAll("-", "");
		itpCode = itpCode.trim().replaceAll("/", "");
		return String.format(dir + "#%s" + IT2, itpCode);
	}

	// ------------------------------------------------------------------------
	// ���ۂɂ́@c:/@qpr/Home/Users/@itp/�@���Ԃ�͂��E�E
	// �g�p��> String dbDir = ResControl.getUsersItpDir();
	// ------------------------------------------------------------------------
	public static String getUsersItpDir() {
		return getQPRHome() + USERS_ITP;
	}

	public static String getItpListByUsers() {
		return getUsersItpDir() + ResControl.ITP_LISTBY_USER_TXT;
	}

	public static String getUsersEnqDir() {
		return getQPRHome() + USERS_ENQ;
	}

	public static String getQtbDir(int year) {
		String pattern = "";
		//---------------------------------------------------------------------
		//		if (year <= 2006) {
		//			System.out.println("### year:" + year);
		//			pattern = ResControl.LEGACY_ENQ_DIR + ResControl.ENQ_DEFAULTEX
		//					+ "/%1$04d/";
		//		}else{
		//---------------------------------------------------------------------
		pattern = ResControl.ENQNQ_DIR + ResControl.ENQ_DEFAULT03 + "/%1$04d/";
		//		}
		String qtbDir = String.format(pattern, year);
		if (!new File(qtbDir).isDirectory()) {
			qtbDir = String.format(pattern, (year - 1));//������Έ�N�O�̑���
		}
		return qtbDir;
	}

	public static String getQtbDir_org(int year) {
		String lev3 = String.valueOf(year);
		String qtbDir = ResControl.ENQNQ_DIR + ResControl.ENQ_DEFAULT03 + "/"
				+ lev3 + "/";
		if (!new File(qtbDir).isDirectory()) {
			lev3 = String.valueOf(year - 1);//���݂��Ȃ���ΑO�N�Ƃ���
			qtbDir = ResControl.ENQNQ_DIR + ResControl.ENQ_DEFAULT03 + "/"
					+ lev3 + "/";
		}
		return qtbDir;
	}

	// -----------------------------------------------------------------
	// ex. String resDir = kyPkg.uFile.ResControl.getlocoQprSrc("");
	// -----------------------------------------------------------------
	public static String getQPRHomeQprRes(String path) {
		String qprResDir = getQPRHome() + QPR + RES + path;
		FileUtil.makeParents(qprResDir);
		return qprResDir;
	}

	// -------------------------------------------------------------------------
	// ���[�J���ɕۑ����ꂽ���j�^�[�̑������ւ̃p�X
	// -------------------------------------------------------------------------
	public static String getCurrentPath() {
		return getPersonalDir(MON_SETS) + ResControl.CURRENT_TXT;
	}

	// -------------------------------------------------------------------------
	// ���[�J���ɕۑ����ꂽ���j�^�[�̑������i���^�f�[�^�j�ւ̃p�X
	// -------------------------------------------------------------------------
	public static String getAtmPath(boolean debug) {
		return getPersonalDir(MON_SETS) + ResControl.CURRENT_ATM;
	}

	// -------------------------------------------------------------------------
	// ��@String rootDir = kyPkg.uFile.ResControl.getRootDir();
	// -------------------------------------------------------------------------
	public static String getRootDir() {
		String wOsName = System.getProperty("os.name");
		if (wOsName.startsWith("Windows"))
			return WINDOWS_ROOT; //	"C:/"
		return LINUX_ROOT; //	"~/"
	}

	public static String getQprRootDir() {
		if (rootDir != null)
			return rootDir;
		rootDir = getRootDir() + QPR_ROOT; //	"C:/@qpr/"
		FileUtil.makedir(rootDir);
		return rootDir;
	}

	// -------------------------------------------------------------------------
	// kyPkg.uFile.ResControl.getUserDir()
	//	=>C:/@qpr/home/
	// -------------------------------------------------------------------------
	public static String getQPRHome() {
		String qprHomeDir = getQprRootDir() + QPR_HOME; //	"C:/@qpr/home/"
		FileUtil.makedir(qprHomeDir);
		return normalize(qprHomeDir);
	}

	// -------------------------------------------------------------------------
	// kyPkg.uFile.ResControl.getUserHomeQPR()
	// -------------------------------------------------------------------------
	public static String getQPRHome(String value) {
		if (value.startsWith("/"))
			value = value.substring(1);
		String userDir = ResControl.getQPRHome() + value;
		return FileUtil.makedir(userDir);
	}

	// -------------------------------------------------------------------------
	// ���j�^�[������ (��rep��p)
	// -------------------------------------------------------------------------
	public static String getMonBankDir() {
		String monbankDir = FileUtil.mkdir("c:/@qpr/monBank/");
		return monbankDir;
	}

	// -------------------------------------------------------------------------
	// XXX �n�r���ȂɂȂ̂��ɂ���āA�x�[�X�ɂȂ�f�B���N�g����ύX����ׂ��I�I
	// -------------------------------------------------------------------------
	// ���[�g�h���C�u����Ԃ��@=>�@c:/
	// �����g�p�၄��String ROOT = RuntimeEnv.getRoot();
	// private static String getRoot() {
	// String ROOT = new java.io.File("./").getAbsolutePath();
	// if (ROOT.endsWith("."))
	// ROOT = ROOT.substring(0, ROOT.length() - 1);
	// return ROOT;
	// }
	// -------------------------------------------------------------------------------
	// �f�t�H���g�ꎞ�t�@�C���̃p�X��Ԃ�
	// String tempDir = FileUtil.getTempDir( );
	// -------------------------------------------------------------------------------
	// �Ⴑ��ȃp�X => C:\Users\EJQP7\AppData\Local\Temp
	// -------------------------------------------------------------------------------
	public static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}

	// -------------------------------------------------------------------------------
	// �e���|�����t�@�C�����𐶐� �i �������O�̃e���|��������������悤�ł���΁E�E�E����������j
	// tmpPath = ResControl.getTempFile();
	// -------------------------------------------------------------------------------
	public static String getTempFile() {
		String timeStamp = kyPkg.uDateTime.DateCalc.getTimeStamp(0, 7, "_");
		String tmpPath = ResControl.getTempDir() + "~Tmp" + timeStamp + ".tmp";
		// System.out.println("@getTempFile :" + tmpPath);
		return tmpPath;
	}

	private static File isFileReady(String path) {
		File f = new File(path);
		if (f.isFile() && f.canWrite()) {
			return f;
		} else {
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// �t�@�C�����̓���ւ�
	// ResControl.xchange("a","b");
	// -------------------------------------------------------------------------------
	public static boolean xchange(String path1, String path2) {
		String tmpPath = getTempFile();
		File f1 = isFileReady(path1);
		File f2 = isFileReady(path2);
		if (f1 != null && f2 != null) {
			try {
				DosEmu.move(path1, tmpPath);
				DosEmu.move(path2, path1);
				DosEmu.move(tmpPath, path2);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// �g�p�၄
	// String rootDir = kyPkg.uFile.ResControl.getCurDir();
	// ------------------------------------------------------------------------
	// �J�����g�f�B���N�g���ɂ��ā@
	// eclipse�ォ����s�����ꍇ�i�v���W�F�N�g�t�H���_�j�@./ => C:\workspace\QPRweb\
	// jar�ォ����s�����ꍇ�@�@./ => Jar�t�@�C���Ɠ��K�w
	// ------------------------------------------------------------------------
	public static String getCurDir() {
		// eclipse�ォ��Ŏ��s�����ꍇ
		// './'�́A������ ResControl.D_DRV + 'workspace/QPRweb/'
		String curDir = CUR_DIR;
		return curDir;
	}

	public static String getMydataDir() {
		String myDataDir = "";
		myDataDir = getRootDir() + MYDATA;
		FileUtil.makedir(myDataDir);
		return myDataDir;
	}

	// -------------------------------------------------------------------------
	// XXX �P�̃e�X�g�p�f�[�^���U�킵�Ă��܂��A���؂ł��Ȃ��Ȃ邱�Ƃ������̂ŁE�E�ݏ��𓝈ꂵ�Ă�������
	// testName�ɃN���X���ł��w�肷��΂悢�����m��Ȃ��E�E�E�E
	// windows�၄ T:\workspace\QPRweb\Res\TestData\CalcBasic
	// �၄ String testDir = ResControl.getTestDir();
	// -------------------------------------------------------------------------
	public static String getTestDir(String testName) {
		String testDir = CUR_DIR + "Res/TestData/" + testName + "/";
		FileUtil.makedir(testDir);
		return testDir;
	}

	// -------------------------------------------------------------------------
	// �၄String deskTopDir = kyPkg.uFile.ResControl.getDeskTop();
	// -------------------------------------------------------------------------
	public static String getDeskTop() {
		// C:\Documents and Settings\Administrator.AGC\�f�X�N�g�b�v\��������.pdf
		// XXX �n�r�Ɉˑ����Ȃ��f�X�N�g�b�v�c�����ɕύX������
		// windows=>"�f�X�N�g�b�v"�E�E�E���Ԃ���{��ł݂̂��낤�A���̃o�[�W�������Ƃǂ��Ȃ̂��낤�H�H
		// linux& macOs => "Desktop"
		String deskTop = "Desktop";
		if (System.getProperty("os.name").startsWith("Windows")) {
			deskTop = "�f�X�N�g�b�v";
		}
		File dtpFile = new File(System.getProperty("user.home"), deskTop);
		return dtpFile.getAbsolutePath();
	}

	public static String getQPRHomeQpr(String path) {
		String wPath = getQPRHome() + QPR + path;
		FileUtil.makeParents(wPath);
		return wPath;
	}

	// -------------------------------------------------------------------------
	// kyPkg.uFile.ResControl.getPersonalDir()
	// c;/DocumentAndSettings/user/qpr/Personal/MonSets/
	// -------------------------------------------------------------------------
	public static String getPersonalDir() {
		return getPersonalDir("");
	}

	public static String getPersonalDir(String path) {
		String qprPersonalDir = ResControl.getQPRHome(PERSONAL) + path;
		FileUtil.makedir(qprPersonalDir);
		return qprPersonalDir;
	}

	// ------------------------------------------------------------------------
	// ex String dbDir = ResControl.getItpDir(cliCode, catCode);
	// ------------------------------------------------------------------------
	public static String getItpDir(String clientCode, String categoryCode) {
		return getUsersItpDir() + "#" + clientCode + categoryCode;
	}

	private static String getUserRootxxx() {
		String userHome = normalize(System.getProperty("user.home"));
		String[] splited = userHome.split("/");
		String userRoot = splited[0] + "/";
		FileUtil.makeParents(userRoot);// ?!�s�v���낤�E�E�E
		return userRoot;
	}

	public static String getAtomDir() {
		return getPersonalDir(ATOM);
	}

	//	public static String getAtomDir() {
	//		return getPersonalDir(ResControl.MON_SETS) + "atom/";
	//	}
	// -------------------------------------------------------------------------
	// ���[�J���ɕۑ����ꂽ���j�^�[������
	// -------------------------------------------------------------------------
	public static String getMonSetsDir() {
		return getPersonalDir(ResControl.MON_SETS);
	}

	// ----------------------------------------------------------------------
	// �Ԓl "C:/@qpr/home/Personal/MonSets/currentP.txt"
	// ----------------------------------------------------------------------
	public static String getCurrentP_TXT() {
		return ResControl.getMonSetsDir() + ResControl.CURRENTP_TXT;
	}

	// -------------------------------------------------------------------------
	// ���[�J���ɕۑ����ꂽ�w���������
	// -------------------------------------------------------------------------
	public static String getCnlSets() {
		return getQPRHomeQprRes(CNL_SETS);
	}

	public static String getCnlSets_P() {
		return getPersonalDir(CNL_SETS);
	}

	// ------------------------------------------------------------------------
	//�@�l�p�`���l���Z���N�g�������R�s�[����
	//�@	�R�s�[���@C:/@qpr/home/Personal/CnlSets/*.lim
	//�@	�R�s�[��@C:/@qpr/home/qpr/res/CnlSets/
	//	���g�p�၄	ResControl.copyPersonalCnlSets()
	// ------------------------------------------------------------------------
	public static void copyPersonalCnlSets() {
		String srcPath = ResControl.getCnlSets_P() + "*" + AtomDB.LIM;
		String dstPath = ResControl.getCnlSets();
		System.out.println("srcPath:" + srcPath);
		System.out.println("dstPath:" + dstPath);
		DosEmu.copy(srcPath, dstPath);
	}

	// ------------------------------------------------------------------------
	// XXX ���݃`�F�b�N���邩�H�H������΍�邩�H�H
	// ------------------------------------------------------------------------
	public static String getPdeDir(String userID) {
		String pdeDir = PDE_DRIVE + userID + "/";
		return pdeDir;
	}

	public static void testEtc() {
		System.out.println("getUserRoot():" + getUserRootxxx());
		System.out.println("getUserDir():" + getQPRHome());
		System.out.println("getTempDir():" + ResControl.getTempDir());
		// �����s���ʁ�
		// getUserRoot():C:/
		// getUserDir():C:/@qpr/home/
		// getTempDir():C:\DOCUME~1\ADMINI~1.AGC\LOCALS~1\Temp\
	}

	private static String normalize(String path) {
		String fs = System.getProperty("file.separator");
		if (fs.equals("\\"))
			fs = "\\\\";
		return path.replaceAll(fs, "/");
	}

	// ------------------------------------------------------------------------
	// �s�b�N�A�b�v�����f�[�^�Ɋւ��郁�^�����쐬���ăt�@�C���ɗ��Ƃ�
	// ------------------------------------------------------------------------
	public static void createYYMMRes(String path, String befYmd,
			String aftYmd) {
		List<String> monthList = kyPkg.uDateTime.DateUtil.dateMapYYMM(befYmd,
				aftYmd, "yyMM", "yy�NMM��");
		//		System.out.println("createYYMMRes path:" + path);
		//		for (String element : monthList) {
		//			System.out.println("createYYMMRes:" + element);
		//		}
		ListArrayUtil.list2File(path, monthList);
	}

	// ------------------------------------------------------------------------
	// m a i n
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		testDirList();
		//	testGetQtbDir();
		//	testEtc();
		//	testXchange();
	}

	public static void testDirList() {
		String dir = ResControl.ENQNQ_DIR + ResControl.ENQ_DEFAULT03;
		List<String> dirList = ListArrayUtil.dir2List_dsc(dir, "*", false);
		for (String element : dirList) {
			System.out.println("element:" + element);
		}

	}

	public static void testGetQtbDir() {
		System.out.println("testGetQtbDir=>" + getQtbDir(2017));
		System.out.println("testGetQtbDir=>" + getQtbDir(2016));
	}

	// ------------------------------------------------------------------------
	// �t�@�C���������ւ���
	// ------------------------------------------------------------------------
	public static void testXchange() {
		String path1 = "c:/right.txt";
		String path2 = "c:/left.txt";
		xchange(path1, path2);
	}

}
