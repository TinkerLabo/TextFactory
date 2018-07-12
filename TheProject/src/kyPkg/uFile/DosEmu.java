package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.regex.*;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.atoms.AtomDB;
import kyPkg.util.Shell;

//-------------------------------------------------------------------------
// �����̃v���O�����́ADigger.java�����FileUtil.java�Ɉˑ����Ă��܂��B
// �t�@�C���V�X�e���̊K�w�����ǂ�̂�Digger.java���g���Ă��܂��B
// ���ۂ̃R�}���h��FileUtil.java�Ŏ������Ă�B
//-------------------------------------------------------------------------
// �����̂n�r�ł�dos�R�}���h���ɑ���ł���
// ��UNIX�R�}���h��win�Ŏg�������ꍇ�̃G�~�����[�^���ق����C������i�I�v�V��������ς����j
//-------------------------------------------------------------------------
//	COPY=>XCOPY (�f�C���N�g�����쐬���������E�E�E)
//bool wFlg = kyPkg.uFile.copyIt(wSrc,wDst);  // copy
//	bool wFlg =kyPkg.uFile.moveIt(wSrc,wDst);  // move
//	bool wFlg = kyPkg.uFile.delIt(wSrc);        // del
//	MOVE=>RENAME
//-------------------------------------------------------------------------
/*******************************************************************************
 * �s DosEmu �t 2007-05-11 <BR>
 * ���������R�}���h�̑�֋@�\�E�E�኱�d�l���Â��̂Ŏg�p���ɒ��� ���G���[�����͍l������Ă��Ȃ��E�E�E
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 * 
 ******************************************************************************/
public class DosEmu {

	private static final String REN = "ren";
	private static final String XDEL = "xdel";
	private static final String XMOVE = "xmove";
	private static final String XCOPY = "xcopy";
	private static final String DEL = "del";
	private static final String MOVE = "move";
	private static final String COPY = "copy";
	private static final String XDIR = "xdir";
	private static final String DIR = "dir";

	// �w�肵���f�B���N�g�����폜����i�f�B���N�g���ȉ��̃t�@�C���E�f�B���N�g�����ċA�I�ɍ폜����j
	// ��P��DosEmu.rmDir(rmvDataDir);
	// ��Q��List<String> rmList = DosEmu.rmDir(rmvDataDir);//�폜�����p�X���m�F�������ꍇ�E�E
	public static List<String> rmDir(String path) {
		List<String> rmList = new ArrayList();// �폜�����t�@�C���̃p�X������Ă����i�m�F�p�j
		File file = new File(path);
		if (file.exists())
			try {
				delRecursive(rmList, file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return rmList;
	}

	private static void delRecursive(List<String> rmList, File file)
			throws java.io.IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				delRecursive(rmList, files[i]);
			}
		}
		if (!(file.delete())) {
			throw new java.io.IOException("cannot delete:" + file.getPath());
		} else {
			rmList.add(file.getPath());
		}
	}

	public static String getOsName() {
		String osName = System.getProperty("os.name");
		System.out.println("os=>" + osName);
		return osName;
	}

	public static String getWinDir() {
		String osName = DosEmu.getOsName();
		Process process = null;
		Runtime runtime = Runtime.getRuntime();
		try {
			if (osName.startsWith("Windows 9")) {
				process = runtime.exec("command.com /c echo %windir%");
			} else {
				process = runtime.exec("cmd.exe /c echo %windir%");
			}
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void xComand(String command) {
		nativeExe(command, true);
	}

	public static void openWithExplorer(String dir) {
		if (!dir.equals("")) {
			new Shell().execute("explorer " + dir, true);
		}
	}

	private static void nativeExe(String command, boolean waitFor) {
		try {
			Process proc = Runtime.getRuntime().exec(command);
			if (waitFor)
				proc.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// �啶����������B���Ƀ}�b�`���O�������ꍇ���Q
	// �����p�^�[����������g���ꍇ�͂����炪�x�^�[
	// �s�g�p��t
	// Pattern ptn = Regexx.patternIgnoreCase(pRegex);
	// wFlg = ptn.matcher(pCharSeq).find();
	// -------------------------------------------------------------------------
	// �啶�����������B���ȃp�^�[�����쐬
	// -------------------------------------------------------------------------
	private static Pattern patternIgnoreCase(String pRegex) {
		return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	}

	// -------------------------------------------------------------------------
	// �f�C���N�g���ȉ��̃t�@�C�����X�g��Ԃ�
	// �� �f�B���N�g���̂݌��������ꍇ�������ˁE�E�E���X�g����E�����H
	// �� �t�@�C���̂ݗ~�����ꍇ������E�E�E�E
	// System.out.println(DosEmu.dirString("."));
	// System.out.println(DosEmu.dirString("c:/"));
	// System.out.println(DosEmu.dirString("c:/*.*"));
	// System.out.println(DosEmu.dirString("c:/*.java"));
	// -------------------------------------------------------------------------
	// private static String dirString(String pSrc) {
	// Digger ZZZ = new Digger(pSrc, false);
	// ZZZ.search(1024);
	// String wStr = ZZZ.toString();
	// return wStr;
	// }

	// -------------------------------------------------------------------------
	// << Wrapper >>�t�@�C���ꗗ
	// -------------------------------------------------------------------------
	public static List dir(String pSrc, boolean recursive) {
		if (recursive) {
			return xdir(pSrc);
		}
		return dir(pSrc);
	}

	// �w�肳�ꂽ�f�C���N�g���ȉ��ɂ���A�t�@�C�����̃��X�g�i�g���q�͊܂܂Ȃ��j
	public static String[] fileNameList2Array(String dir, String regex) {
		List list = fileNameList(dir, regex);
		String[] array = null;
		if (list != null) {
			array = (String[]) list.toArray(new String[list.size()]);
		}
		return array;
	}

	// �w�肳�ꂽ�f�C���N�g���ȉ��ɂ���A�w�t�@�C�����i�g���q�͊܂܂Ȃ��j�x�̃��X�g
	public static List fileNameList(String dir, String regex) {
		if (new File(dir).isDirectory() == false)
			return (List) null;
		Digger insDig = new Digger(dir, regex);
		int dirLen = insDig.getRootPathLength();
		List<String> fileList = insDig.search();
		List list = new ArrayList();
		for (String path : fileList) {
			path = path.substring(dirLen);
			int poz = path.indexOf('.');
			if (poz > 0) {
				list.add(path.substring(0, poz));
			} else {
				list.add(path);
			}
		}
		return list;
	}

	// public static List fileNameList(String dir, String regex) {
	// Digger insDig = new Digger(dir, regex);
	// int preLen = insDig.getRootPathLength();
	// insDig.search();
	// String[] fileArray = insDig.getFileArray();
	// List<String> list = new ArrayList();
	// for (int i = 0; i < fileArray.length; i++) {
	// String wStr = fileArray[i];
	// wStr = wStr.substring(preLen);
	// list.add(wStr.substring(0, wStr.indexOf('.')));
	// }
	// return list;
	// }

	// ------------------------------------------------------------------------
	// �w�肳�ꂽ�f�C���N�g���ȉ��ɂ���A�t�@�C���i�t���p�X�j�̃��X�g
	// ------------------------------------------------------------------------
	// �g�p�၄ String[] array = DosEmu.getFileArray(inDir, pattern);
	// ------------------------------------------------------------------------
	public static String[] getFileArray(String dir, String regex) {
		Digger insDig = new Digger(dir, regex);
		insDig.search();
		return insDig.getFileArray();
	}

	// �w�肳�ꂽ�f�C���N�g���ȉ��ɂ���A�t�@�C���i�t���p�X�j�̃��X�g
	// �����ӁApath�̏I�[��"/"�ł͖����ꍇ�A���O��"/"�܂ŃJ�b�g�����
	public static List getFileList(String path, String regex) {
		String dir = FileUtil.getParent2(path, true);
		Digger insDig = new Digger(dir, regex);
		insDig.search();
		return insDig.getFileList();
	}

	// �w�肳�ꂽ�f�C���N�g���ȉ��ɂ���A�t�@�C���i�t���p�X�j�̃��X�g
	public static List<String> pathList2List(String iPath) {
		String wPath = FileUtil.normarizeIt(iPath);
		int poz = wPath.lastIndexOf("/");
		String dir = wPath.substring(0, poz);
		String regex = wPath.substring(poz + 1);
		// System.out.println("debug@getFileArray dir:" + dir);
		// System.out.println("debug@getFileArray regex:" + regex);
		return pathList2List(dir, regex);
	}

	public static List<String> pathList2List(String dir, String regex) {
		Digger insDig = new Digger(dir, regex);
		insDig.search();
		return insDig.getFileList();
	}

	public static String[] dirList2Array(String dir, String regex) {
		return ListArrayUtil.list2Array(dirList(dir, regex));
	}

	// TODO ���̃��b�\�b�h�̃p�t�H�[�}���X���グ��E�E�E�v�e�X�g
	public static List<String> dirList(String dir, String regex) {
		dir = FileUtil.mkdir(dir);
		Digger insDig = new Digger(dir, regex);
		int preLength = insDig.getRootPathLength();
		// System.out.println("pre search()");
		insDig.search();
		// System.out.println("pre getDirList()");
		List<String> list = Digger.getDirList();
		if (list == null)
			return null;
		// System.out.println("pre sort");
		// XXX BUG��\������A�v�C���I�I
		Collections.sort(list);
		List result = new ArrayList();
		for (String element : list) {
			result.add(element.substring(preLength));
		}
		return result;
	}

	public static List dir(String pSrc) {
		return command(DIR, pSrc, "");
	}

	public static List xdir(String pSrc) {
		return command(XDIR, pSrc, "");
	}

	// DosEmu.copy(aliasDir + "QTB*.txt", outDir);
	public static List copy(String pSrc, String pDst) {
		return command(COPY, pSrc, pDst);
	}


	// �R�s�[��̃f�[�^�����݂����ꍇ�A�����I�ɏ㏑�������
	public static List move(String pSrc, String pDst) {
		//		//#createTester--------------------------------------------------
		//		System.out.println("public static void testmove() {");
		//		System.out.println("    String pSrc = \"" + pSrc + "\";");
		//		System.out.println("    String pDst = \"" + pDst + "\";");
		//		System.out.println("    move ins = new move(pSrc,pDst);");
		//		System.out.println("}");
		//		//--------------------------------------------------

		return command(MOVE, pSrc, pDst);
	}

	// DosEmu.cleanUp(resDir + "*.txt");
	public static List cleanUp(String pSrc) {
		// System.out.println("��20140428��@DosEmu.cleanUp:"+pSrc);
		return del(pSrc);
	}

	// DosEmu.del(resDir + "*.trn");
	public static List del(String pSrc) {
		return command(DEL, pSrc, "");
	}

	public static List xcopy(String pSrc, String pDst) {
		return command(XCOPY, pSrc, pDst);
	}

	public static List xmove(String pSrc, String pDst) {
		return command(XMOVE, pSrc, pDst);
	}

	public static List xdel(String pSrc) {
		return command(XDEL, pSrc, "");
	}

	public static List ren(String pSrc, String pDst) {
		return command(REN, pSrc, pDst);
	}

	// -------------------------------------------------------------------------
	// �R�}���h�����s����
	// �����s�O�ɑΏۂ��m�F������@���~�����E�E�E
	// �����ʂ��o�b�t�@�[�ɒ��߂邩�ǂ��� �o�[�{�E�Y�I�v�V�����H�I
	// ���Ώۂ����b�N����Ă���A�A�N�Z�X�����Ȃǂ̃G���[�����E�E�E�Ȃǂ̖��
	// -------------------------------------------------------------------------
	private static List command(String pFunk, String pSrc, String pDst) {
		// System.out.println("(_) pFunk:" + pFunk+" pSrc:" + pSrc+" pDst:" +
		// pDst);
		// pFunk:del pSrc:C:\DOCUME~1\ADMINI~1.AGC\LOCALS~1\Temp\/*.txt
		// pDst:
		String wFunk = "";
		boolean rSw = false; // �ċA�E�E�E�T�uDir���������邩�ǂ���
		if (pFunk.equals(DIR)) {
			wFunk = DIR;
		} else if (pFunk.equals(COPY)) {
			wFunk = COPY;
		} else if (pFunk.equals(MOVE)) {
			wFunk = MOVE;
		} else if (pFunk.equals(DEL)) {
			wFunk = DEL;
		} else if (pFunk.equals(XDIR)) {
			wFunk = DIR;
			rSw = true;
		} else if (pFunk.equals(XCOPY)) {
			wFunk = COPY;
			rSw = true;
		} else if (pFunk.equals(XMOVE)) {
			wFunk = MOVE;
			rSw = true;
		} else if (pFunk.equals(XDEL)) {
			wFunk = DEL;
			rSw = true;
		} else if (pFunk.equals(REN)) {
			wFunk = REN;
		}
		String wRegx1 = "";
		String wRegx2 = "";
		String wPath1 = "";
		String wPath2 = "";
		String wSrc = "";
		String wDst = "";
		int xIdx = -1;
		// -----------------------------------------------------------------
		if (pSrc == null)
			return null;
		pSrc = pSrc.replaceAll("\\\\", "/");
		pDst = pDst.replaceAll("\\\\", "/");
		// System.out.println(" pFunk:" + pFunk + " pSrc:" + pSrc + " pDst:" +
		// pDst);
		if (pSrc.lastIndexOf("/") == -1)
			pSrc = pSrc + "/";
		xIdx = pSrc.lastIndexOf("/");
		wPath1 = pSrc.substring(0, xIdx + 1);
		wRegx1 = pSrc.substring(xIdx + 1).trim();
		// -----------------------------------------------------------------
		if (pDst.lastIndexOf("/") == -1)
			pDst = pDst + "/";
		xIdx = pDst.lastIndexOf("/");
		wPath2 = pDst.substring(0, xIdx + 1);
		wRegx2 = pDst.substring(xIdx + 1).trim();
		// -----------------------------------------------------------------
		// ���p�����[�^�� c:\xxxx �̂悤�Ɏw�肳�ꂽ�ꍇ
		// xxxx���f�B���N�g���Ƃ��Ĉ�������
		// -----------------------------------------------------------------
		if (!wRegx2.trim().equals("")) {
			if (wRegx2.lastIndexOf(".") == -1) {
				pDst = pDst + "/";
				xIdx = pDst.lastIndexOf("/");
				wPath2 = pDst.substring(0, xIdx + 1);
				wRegx2 = pDst.substring(xIdx + 1).trim();
			}
		}
		new java.io.File(wPath2).mkdirs();
		// Digger insDig = new Digger(pSrc, rSw);
		// System.out.println("wPath1:" + wPath1);
		// System.out.println("wRegx1:" + wRegx1);
		Digger insDig = new Digger(wPath1, wRegx1, rSw);
		insDig.search();
		String wPath = insDig.getRootPath();// ?!�Ӗ����킩���
		// System.out.println("20130502 <debug xxx> wPath :" + wPath);
		// wPath = wPath1;�p�b�`����ꂽ���߂��@���ɋ���߽����������
		// System.out.println("20130502 <debug xxx> wPath1:" + wPath1);
		if (wPath == null)
			return null; // ???20100316 ���ɁE�E�E��������
		wPath = wPath.replaceAll("\\\\", "/"); // 2010/06/23 yuasa
		// System.out.println("20130502 <debug> wPath :" + wPath);

		int wPos = wPath.length();
		// System.out.println("wPath:" + wPath);
		// System.out.println("length:" + wPos);
		wRegx1 = wRegx1.replaceAll("\\*", "");
		wRegx2 = wRegx2.replaceAll("\\*", "");
		wRegx1 = wRegx1.replaceAll("\\.", "\\\\.");
		if (wRegx1.equals("\\.") | wRegx1.equals(""))
			wRegx1 = "\\..*";
		// -----------------------------------------------------------------
		// System.out.println("��wPath1 :" + wPath1);
		// System.out.println("��wRegx1 :" + wRegx1);
		// System.out.println("��wPath2 :" + wPath2);
		// System.out.println("��wRegx2 :" + wRegx2);
		Pattern ptn = patternIgnoreCase(wRegx1); // �p�^�[���쐬
		// -----------------------------------------------------------------

		Iterator it = insDig.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			// wSrc = (String)it.next();
			wSrc = it.next().toString();
			wSrc = wSrc.replaceAll("\\\\", "/"); // 2010/06/23 yuasa
			// System.out.println("2013<debug> wPath2:" + wPath2);
			// System.out.println("2013<debug> wSrc:" + wSrc);
			// System.out.println("2013<debug> wPos:" + wPos);
			wDst = wPath2 + wSrc.substring(wPos);
			// System.out.println("wSrc=>"+wSrc);
			// System.out.println("wDst=>"+wDst);
			// wDst = wDst.replaceAll(wRegx1,wRegx2);
			if (!wRegx2.equals("")) {
				wDst = ptn.matcher(wDst).replaceAll(wRegx2);
			}
			// System.out.println("conv=>"+wDst);
			// -----------------------------------------------------------------
			// ���p�ł��郍�W�b�N�Ȃ̂ŁA
			// ���ƂŊe�X�̃��\�b�h���ƂɃ��b�p�[���\�b�h������Ă����ƕ֗�
			// -----------------------------------------------------------------
			boolean wFlg = false;
			if (wFunk.equals(DIR)) {
				list.add(wSrc);
				wFlg = true;
			} else if (wFunk.equals(COPY)) {
				wFlg = FileUtil.fileCopy(wDst, wSrc);
				list.add("copy " + wSrc + " to " + wDst);
			} else if (wFunk.equals(MOVE)) {
				wFlg = FileUtil.moveIt(wSrc, wDst);
				list.add("move " + wSrc + " to " + wDst);
			} else if (wFunk.equals(DEL)) {
				wFlg = FileUtil.delIt(wSrc);
				list.add("del " + wSrc);
			} else if (wFunk.equals(REN)) {
				// wFlg = FileUtil.moveIt(wSrc, wDst);
				// list.add("move " + wSrc + " to " + wDst);
				// ����ɂ��Ă͑�Q�p�����[�^�̌`���Ⴄ �E�E�EMOVE�ő�p�\���낤
			}
			if (wFlg == false) {
				System.out.println("Error @xCommand :" + wFunk + "�@=>" + wSrc);
			}
			// wVec.add("\n");
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// main �g�p�� �J�����g�p�X��������ɍ����p�^�[���̃t�@�C�����X�g�����
	// -------------------------------------------------------------------------
	public static void test01() {
		// System.out.println(DosEmu.dirString("."));
		// System.out.println(DosEmu.dirString("c:/"));
		// System.out.println(DosEmu.dirString("c:/*.*"));
		// System.out.println(DosEmu.dirString("c:/*.java"));
		// new java.io.File("c:/backups").mkdirs();
		// Vector wVec = DosEmu.move("c:/*.txt", "c:/backups");
		// for (int i = 0; i < wVec.size(); i++) {
		// System.out.println("=>" + wVec.get(i));
		// }
		String[] array = DosEmu.fileNameList2Array("c:/", "*.txt");
		for (int i = 0; i < array.length; i++) {
			System.out.println("==>" + array[i]);
		}
		// String[] arrayD = DosEmu.dirList2StrArray("c:/", "e*");
		String[] arrayD = kyPkg.uFile.DosEmu.dirList2Array("N:/PowerBX/2008/",
				"T*");
		for (int i = 0; i < arrayD.length; i++) {
			System.out.println("D=>" + arrayD[i]);
		}
		DosEmu.copy("c:/*.csv", "c:/freaks/");
		// DosEmu.ren(ResControl.QPR_DAT + "NQFACE.DAT", "NQFACE.OLD");
	}

	public static void test02() {
		String targetPath = ResControlWeb.getD_Resources_ATOM("");
		String pattern = "*.*";
		String[] arrayD = DosEmu.getFileArray(targetPath, pattern);
		for (int i = 0; i < arrayD.length; i++) {
			System.out.println("D=>" + arrayD[i]);
		}
	}

	public static void test03() {
		String userDir = globals.ResControl.getQPRHome();
		String[] array = DosEmu.fileNameList2Array(userDir + "110098000002/",
				"*.trn");
		for (int i = 0; i < array.length; i++) {
			System.out.println("==>" + array[i]);
		}
	}

	public static void test0601() {
		// String targetPath = ResControl.getWebDir(target);
		String targetPath = ResControl.D_DRV + "resources/User/@itp";
		String[] array = DosEmu.dirList2Array(targetPath, "e95*");
		for (int i = 0; i < array.length; i++) {
			System.out.println("==>" + array[i]);
		}
	}

	public static void testGetFileList() {
		System.out.println("<start testGetFileList>");
		String wPath1 = "c:/test.txt";
		String wPath2 = "z:/test.txt";
		String regex = "*.txt";
		List<String> list = DosEmu.getFileList(wPath1, regex);
		for (String str : list) {
			// �e�p�X������������
			String tPath = FileUtil.cnvParent(wPath2, str);
			// �g���q������������
			String difPath = FileUtil.cnvExt(str, "dif");
			System.out.println(
					">>" + str + " parent>" + tPath + " dif>" + difPath);

		}
		System.out.println("<end   testGetFileList>");
	}

	// private static final String FS = System.getProperty("file.separator");
	public static void testRmDir() {
		List<String> rmList = DosEmu.rmDir("c:/encodeTest/");
		for (String path : rmList) {
			System.out.println("removed => " + path);
		}
	}

	// 20130502
	public static void testDirList() {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("DividerVer1");
		elapse.start();
		String itpDir = ResControl.T_RES + "@itp/E92019";
		String regex = "8*";
		itpDir = globals.ResControlWeb.getD_Resources_USER_ITP_COMMON();
		regex = "*";
		List<String> list = DosEmu.dirList(itpDir, regex);
		ListArrayUtil.enumList(list);
		elapse.stop();
	}

	// ���Y�f�B���N�g���ȉ��̃p�^�[���Ƀ}�b�`����t�@�C���ꗗ
	public static void testDir() {
		// ���Y�f�B���N�g���ȉ��̃p�^�[���Ƀ}�b�`����t�@�C���ꗗ
		String path = "C:/@qpr/home/238881000301/calc/#004_sorted_AMT_*.txt";
		List<String> list = DosEmu.dir(path, false);
		for (String element : list) {
			System.out.println("@dOSeMU element:" + element);
		}
	}

	public static void testCopy() {
		String serverPath = "//Ag6s012/PRG/s2/rx/qpr/tran/#340018212101/20130301_20130331.*";
		String locoPath = "C:/@qpr/home/340018212101/tran/20130301_20130331.*";
		DosEmu.copy(serverPath, locoPath);
	}

	public static void testCopy20160114() {
		String srcPath = ResControl.getCnlSets_P() + "*"+AtomDB.LIM; //C:/@qpr/home/Personal/CnlSets/
		String dstPath = ResControl.getCnlSets(); //C:/@qpr/home/qpr/res/CnlSets/
		System.out.println("srcPath:" + srcPath);
		System.out.println("dstPath:" + dstPath);
		DosEmu.copy(srcPath, dstPath);
	}

	//�ċN�I�ɏ���
	public static void testDel() {
		DosEmu.xdel("C:/@qpr/outitpBANK/E97009/*.*");
	}

	//����
	public static void test20161004() {
		String janSetDir = "C:/@qpr/home/304667003001/JanSets/*.*";
		DosEmu.del(janSetDir);
	}

	public static void main(String argv[]) {
		// test02();
		// test03();
		// test0601();
		// testGetFileList();
		// testFileNameList();
		// testRmDir();
		// testCopy();
		// testFileNameList();
		// testDirList();
		// testDir();
		// testCopy20160114();
		//		testDel();
		test20161004();
	}
	//	public static String userName() {
	//		return kyPkg.util.RuntimeEnv.getUserID();
	//	}
}
