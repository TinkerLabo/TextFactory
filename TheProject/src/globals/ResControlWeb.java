package globals;

import kyPkg.uFile.FileUtil;

public class ResControlWeb {
	//	private static final String SERVER = "ks1s014";
	//	@SuppressWarnings("unused")
	//	private static final String NEWSERVER = "ag6s022";

	public static final String ITPBANKS_SERVER =  "\\\\ks1s014/itpBANKs/";
	private static String D_Resources =  "\\\\ks1s014/ks1s014D/resources/";

	private static final String QPR_DST = "qpr/dst/";

	private static final String USER_ITP_COMMON = "User/@itp/COMMON/";
	private static final String TEMP = "temp/";
	private static final String TEMPLATES = "templates/";

	private static final String QPR = "qpr/";
	private static final String RES = "res/";
	private static final String ATOM = "Atom/";
	private static final String PUBLIC = "Public/";

	public static String getItpPath_remote(String itpCode) {
		String dir = ResControlWeb.ITPBANKS_SERVER;
		return ResControl.getItpPath(dir, itpCode, true);
	}

	public static String getLogDir() {
		String remotePath = ITPBANKS_SERVER + "userLog/";
		FileUtil.makedir(remotePath);
		return remotePath;
	}

	// ------------------------------------------------------------------------
	// D:/resources/User/@itp/
	// ------------------------------------------------------------------------
	public static String getD_Resources_ItpDir() {
		String dir = getD_Resources() + "User/@itp/";
		FileUtil.makeParents(dir);
		return dir;
	}

	// ------------------------------------------------------------------------
	//	String srcPath = "D:/resources/User/@itp/itpListbyUser.txt";
	// ------------------------------------------------------------------------
	public static String getItpListByUser() {
		return ResControlWeb.ITPBANKS_SERVER + ResControl.ITP_LISTBY_USER_TXT; //20170328
	}

	// ------------------------------------------------------------------------
	// 使用例>String commonDir = kyPkg.uFile.ResControlWeb.getWebCommonItpDir();
	// =>D:/resources/User/@itp/COMMON/
	// ------------------------------------------------------------------------
	public static String getD_Resources_USER_ITP_COMMON() {
		String itpDir = getD_Resources() + USER_ITP_COMMON;
		FileUtil.makeParents(itpDir);
		return itpDir;
	}

	private static String getD_Resources() {
		if (D_Resources != null)
			return D_Resources;
		String wOsName = System.getProperty("os.name");
		System.out.println("getWebDir os=>" + wOsName);
		if (wOsName.startsWith("Windows 2003")) {
			D_Resources = "D:/resources/";
		} else if (wOsName.startsWith("Windows")) {
			D_Resources = "C:/resources/";
		} else {
			D_Resources = "./resources/";
		}
		System.out.println("webDir=>" + D_Resources);
		FileUtil.makeParents(D_Resources);
		return D_Resources;
	}

	public static String getD_Resources(String path) {
		String dir = getD_Resources() + path;
		return dir;
	}

	// XXX ｗｅｂサーバー用（ローカルでは使わないようにする）
	public static String getD_Resources_QPR_RES() {
		String wkDir = getD_Resources() + QPR + RES;
		FileUtil.makeParents(wkDir);
		return wkDir;
	}

	public static String getD_Resources_QPR(String path) {
		String wkDir = getD_Resources() + QPR + path;
		FileUtil.makeParents(wkDir);
		return wkDir;
	}

	public static String getD_Resources_PUBLIC(String path) {
		String wkDir = getD_Resources() + PUBLIC + path;
		FileUtil.makeParents(wkDir);
		return wkDir;
	}

	public static String getD_Resources_ATOM(String path) {
		String wkDir = getD_Resources() + ATOM + path;
		FileUtil.makeParents(wkDir);
		return wkDir;
	}

	public static String getD_Resources_Templates(String path) {
		String wkDir = getD_Resources() + TEMPLATES + path;
		FileUtil.makeParents(wkDir);
		return wkDir;
	}

	// getWebQprDst()
	// ResControl.D_DRV + ”resources/qpr/dst/”が返る
	public static String getD_Resources_QprDst() {
		String wkDir = getD_Resources() + QPR_DST;
		FileUtil.makeParents(wkDir);
		return wkDir;
	}

	// web系の場合のみ・・・
	public static String getD_Resources_TEMP(String userName) {
		userName = userName.trim();
		if (userName.equals(""))
			userName = "Anonymous";
		String userResPath = getD_Resources() + TEMP + userName;
		kyPkg.uFile.FileUtil.makeParents(userResPath);
		return userResPath;
	}


	public static void main(String[] args) {
	}
}
