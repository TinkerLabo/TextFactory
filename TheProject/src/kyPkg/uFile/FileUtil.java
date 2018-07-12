package kyPkg.uFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

import globals.ResControl;
import kyPkg.tools.Onbiki;
import kyPkg.uDateTime.DateCalc;

/*******************************************************************************
 * �s FileUtil �t <BR>
 * �t�@�C���֘A�A�ǂ��g�����̂Ȃ�
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 ******************************************************************************/
//-----------------------------------------------------------------------------
// kyPkg.uFile.FileUtil
//-----------------------------------------------------------------------------
// fileCheck
//-----------------------------------------------------------------------------
//'./'�́A������ 'T:\workspace\QPRweb'
//-----------------------------------------------------------------------------
public class FileUtil {
	public static final String FS = System.getProperty("file.separator");
	public static final String LF = System.getProperty("line.separator");
	public static final String JIS0212 = "JIS0212";// JIS X 0212�A���{��
	public static final String JIS0208 = "JIS0208";// JIS X 0208�A���{��
	public static final String JIS0201 = "JIS0201";// JIS X 0201�A���{��
	public static final String JIS_AUTO_DETECT = "JISAutoDetect";// Shift-JIS�AEUC-JP�AISO 2022 JP �̌��o����ѕϊ�
	public static final String US_ASCII = "US-ASCII";// American Standard Code for Information
	public static final String ISO_8859_1 = "ISO-8859-1";// ISO 8859-1�A���e���A���t�@�x�b�g No. 1
	public static final String ISO8859_1 = "ISO8859_1";// ISO 8859-1�A���e���A���t�@�x�b�g No. 1
	public static final String ASCII = "ASCII";// American Standard Code for Information Interchange
	//  BOM�ɂ���Ďw�肳�ꂽ�o�C�g��
	public static final String UTF_16LE = "UTF-16LE";// 16 �r�b�g UCS Transformation Format�A���g���G���f�B�A���o�C�g��
	public static final String UTF_16BE = "UTF-16BE";// 16 �r�b�g UCS Transformation Format�A�r�b�O�G���f�B�A���o�C�g��
	//  ���g���G���f�B�A���o�C�g��
	public static final String UTF_16 = "UTF-16";// 16 �r�b�g Unicode Transformation
	//  ���g���G���f�B�A���o�C�g���A�o�C�g���}�[�N�t��
	public static final String UNICODE_LITTLE_UNMARKED = "UnicodeLittleUnmarked";// 16 �r�b�g Unicode Transformation
	//  �r�b�O�G���f�B�A���o�C�g��
	public static final String UNICODE_LITTLE = "UnicodeLittle";// 16 �r�b�g Unicode Transformation
	public static final String UNICODE_BIG_UNMARKED = "UnicodeBigUnmarked";// 16 �r�b�g Unicode Transformation
	public static final String UNICODE_BIG = "UnicodeBig"; // 16 �r�b�g Unicode Transformation
	public static final String CP943 = "Cp943";// IBM OS/2 ���{��ACp932 ����� Shift-JIS �̃X�[�p�[�Z�b�g
	public static final String CP942 = "Cp942";// IBM OS/2 ���{��ACp932 �̃X�[�p�[�Z�b�g
	public static final String CP939 = "Cp939";// UDC 4370 �������܂ޓ��{�ꃉ�e�����������A5035 �̃X�[�p�[�Z�b�g
	public static final String CP932 = "CP932";// IBM OS/2 ���{�� �BMS932 �Ƃقړ��l�����኱�قȂ�B
	public static final String CP930 = "Cp930";// UDC 4370 �������܂ޓ��{��J�^�J�i�����A5026 �̃X�[�p�[�Z�b�g
	public static final String CP33722 = "Cp33722";// IBM-eucJP - ���{�� (5050 �̃X�[�p�[�Z�b�g)
	public static final String ISO2022JP = "ISO2022JP";// JIS X 0201�AISO 2022 �`���� 0208�A���{��
	public static final String ISO_2022_JP = "ISO-2022-JP";// ����
	public static final String EUC_JP = "EUC_JP";// JIS X 0201�A0208�A0212�AEUC �G���R�[�f�B���O�A���{��
	public static final String UNICODE = "Unicode";// unicode�Ƃ����w�肷��Ƃǂ��Ȃ�H
	public static final String UTF8 = "UTF8";// 8 �r�b�g Unicode Transformation Format
	public static final String SJIS = "SJIS";// Shift-JIS�A���{��
	public static final String SHIFT_JIS = "Shift_JIS";// JDK1.1 �܂ł� SJIS �Ɠ��`�BJDK1.2 ����� MS932 �Ɠ��`�B
	public static final String UTF_8 = "UTF-8";// 8 �r�b�g UCS Transformation Format 
	public static final String MS932 = "MS932";// Windows ���{��V�t�gJIS�Ƃقړ��l�����኱�قȂ�B
	//	public static final String DEFAULT_ENCODE = MS932;
	//	static String defaultEncoding = MS932;// windows=>MS932
	//	public static String defaultEncoding2 = MS932;// windows=>MS932
	public static final String DEFAULT_ENCODE = MS932;//SHIFT_JIS;
	public static String defaultEncoding = MS932;//SHIFT_JIS;// windows=>MS932
	public static String defaultEncoding2 = MS932;;//SHIFT_JIS;// windows=>MS932
	public static final String WINDOWS_31J = "windows-31j";// windows-31j,shift-jis,ISO-2022-JP
	public static final String windowsDecoding = MS932;;//SHIFT_JIS;// windows-31j,shift-jis,ISO-2022-JP
	//	public static final String windowsDecoding = WINDOWS_31J;// windows-31j,shift-jis,ISO-2022-JP
	//	public static final String DEFAULT_ENCODE = MS932;//20170324 �@����ł����񂶂�Ȃ����H�����E�E�E��肪����ꍇ�́@rec = Onbiki.cnv2Similar(rec, SHIFT_JIS);�ȂǂłȂ���
	//	static String defaultEncoding = System.getProperty("file.encoding");// windows=>MS932
	// -------------------------------------------------------------------------------
	// �L�����N�^�Z�b�g�i�G���R�[�h�j�̈ꗗ�����X�g�ŕԂ�
	// List charsetNames = kyPkg.uFile.FileUtil_.getCharsetNames();
	// -------------------------------------------------------------------------------
	// �T�|�[�g����Ă���G���R�[�f�B���O
	// http://java.sun.com/j2se/1.4/ja/docs/ja/guide/intl/encoding.doc.html
	// �Q�l�y�[�W
	// http://www.tohoho-web.com/java/file.htm
	// �@utf-8�ɂ��Ă�BOM�������Ă������Ă��hUTF-8�h��ok
	// -------------------------------------------------------------------------------

	public static List getCharsetNames() {
		List list = new ArrayList();
		list.add(MS932); // Windows ���{��V�t�gJIS�Ƃقړ��l�����኱�قȂ�B
		list.add(SHIFT_JIS); // JDK1.1 �܂ł� SJIS �Ɠ��`�BJDK1.2 ����� MS932 �Ɠ��`�B
		list.add(SJIS); // Shift-JIS�A���{��
		list.add(UTF_8); // 8 �r�b�g UCS Transformation Format
		list.add(UTF8); // 8 �r�b�g Unicode Transformation Format
		list.add(UNICODE); // unicode�Ƃ����w�肷��Ƃǂ��Ȃ�H
		list.add(EUC_JP); // JIS X 0201�A0208�A0212�AEUC �G���R�[�f�B���O�A���{��
		list.add(ISO2022JP); // JIS X 0201�AISO 2022 �`���� 0208�A���{��
		list.add(ISO_2022_JP); // ����
		list.add(CP33722); // IBM-eucJP - ���{�� (5050 �̃X�[�p�[�Z�b�g)
		list.add(CP930); // UDC 4370 �������܂ޓ��{��J�^�J�i�����A5026 �̃X�[�p�[�Z�b�g
		//		 list.add(CP932); // IBM OS/2 ���{�� �BMS932 �Ƃقړ��l�����኱�قȂ�B
		list.add(CP939); // UDC 4370 �������܂ޓ��{�ꃉ�e�����������A5035 �̃X�[�p�[�Z�b�g
		list.add(CP942); // IBM OS/2 ���{��ACp932 �̃X�[�p�[�Z�b�g
		list.add(CP943); // IBM OS/2 ���{��ACp932 ����� Shift-JIS �̃X�[�p�[�Z�b�g
		list.add(UNICODE_BIG); // 16 �r�b�g Unicode Transformation
		// Format�A�r�b�O�G���f�B�A���o�C�g���A�o�C�g���}�[�N�t��
		list.add(UNICODE_BIG_UNMARKED); // 16 �r�b�g Unicode Transformation
		// Format�A�r�b�O�G���f�B�A���o�C�g��
		list.add(UNICODE_LITTLE); // 16 �r�b�g Unicode Transformation
		// Format�A���g���G���f�B�A���o�C�g���A�o�C�g���}�[�N�t��
		list.add(UNICODE_LITTLE_UNMARKED); // 16 �r�b�g Unicode Transformation
		// Format�A���g���G���f�B�A���o�C�g��
		list.add(UTF_16); // 16 �r�b�g Unicode Transformation
		// Format�A�K�{�̏����o�C�g���}�[�N�ɂ���Ďw�肳�ꂽ�o�C�g��
		list.add(UTF_16BE); // 16 �r�b�g UCS Transformation Format�A�r�b�O�G���f�B�A���o�C�g��
		list.add(UTF_16LE); // 16 �r�b�g UCS Transformation Format�A���g���G���f�B�A���o�C�g��
		list.add(ASCII); // American Standard Code for Information Interchange
		list.add(ISO8859_1); // ISO 8859-1�A���e���A���t�@�x�b�g No. 1
		list.add(ISO_8859_1); // ISO 8859-1�A���e���A���t�@�x�b�g No. 1
		list.add(US_ASCII); // American Standard Code for Information
		list.add(JIS_AUTO_DETECT); // Shift-JIS�AEUC-JP�AISO 2022 JP �̌��o����ѕϊ�
		// (Unicode �ւ̕ϊ��̂�)
		list.add(JIS0201); // JIS X 0201�A���{��
		list.add(JIS0208); // JIS X 0208�A���{��
		list.add(JIS0212); // JIS X 0212�A���{��
		// Interchange
		return list;
	}

	//---------------------------------------------------------------------------
	public static String getDefaultEncoding() {
		return defaultEncoding;
	}

	public static void setDefaultEncoding(String defaultEncoding) {
		FileUtil.defaultEncoding = defaultEncoding;
	}

	// -------------------------------------------------------------------------------
	// �g���q������������
	// sample: kyPkg.uFile.FileUtil_.cnvExt(path, "dif")
	// -------------------------------------------------------------------------------
	public static String cnvExt(String path, String ext) {
		String preExt = FileUtil.getPreExt(path);
		return preExt + "." + ext.trim();
	}

	// -------------------------------------------------------------------------------
	// �e�p�X������������
	// sample: kyPkg.uFile.FileUtil_.cnvParent("z:/", path)
	// ���V�����e�p�X�̏I�[��"/"�ł͖����ꍇ�؂�̂Ă�̂Œ���
	// -------------------------------------------------------------------------------
	public static String cnvParent(String newParent, String path) {
		String name = FileUtil.getName(path);
		String parent = FileUtil.getParent2(newParent, true);
		return parent + name;
	}

	/***************************************************************************
	 * OutputStreamWriter��Ԃ�<br>
	 * 
	 * @param resultPath
	 *            �ǂݍ��ރt�@�C��
	 * @return ���s������null���Ԃ� <br>
	 *         �� <br>
	 *         OutputStreamWriter oSw = new
	 *         FileUtil().getStreamWriter("c:\zappa.txt"); <br>
	 *         if ( oSw ^= null ){ <br>
	 *         String wRec = sBuf.toString(); <br>
	 *         oSw.write(wRec,0,wRec.length()); <br>
	 *         oSw.close(); <br>
	 **************************************************************************/
	// public OutputStreamWriter getStreamWriter(String outPath) {
	// kyPkg.uFile.FileUtil.makeParents(outPath); // �e�p�X��������΍��
	// OutputStreamWriter writer = null;
	// if (!FileUtil.oFileChk(outPath).equals("")) {
	// return null;
	// }
	// File oFile = new File(outPath);
	// try {
	// writer = new OutputStreamWriter(new FileOutputStream(oFile));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return writer;
	// }
	public static File fileCheck(String path) {
		if (path == null || path.trim().equals("")) {
			System.out.println("#ERROR @fileCheck =>path is :" + path);
			return null;
		}
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("#ERROR @fileCheck FileNotFound:" + path);
			return null;
		}
		if (!file.isFile()) {
			System.out.println("#ERROR @fileCheck Not a File:" + path);
			return null;
		}
		if (file.length() == 0) {
			System.out.println("#ERROR @fileCheck length is 0:" + path);
			return null;
		}
		return file;
	}

	/***************************************************************************
	 * ���̓t�@�C���`�F�b�N<br>
	 * 
	 * @param pPath
	 *            �t�@�C���p�X
	 * @return �t�@�C���N���X <br>
	 *         �s�g�p��1�t<br>
	 *         if ( FileUtil.iFileChk(iPath).equals("") ){ //OK File wFile = new
	 *         File(path); }
	 **************************************************************************/
	public static File iFileChk(String path) {
		String message = "";
		File file = new File(path);
		try {
			if (!file.exists()) {
				message = "�t�@�C�������݂��܂���F" + path;
				message = "�ʏ�t�@�C���ł͂���܂���F" + path;
			} else if (!file.isFile()) {
			} else if (!file.canRead()) {
				message = "�ǂݍ��݉\�ł͂���܂���F" + path;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			message = "�Z�L�����e�C��O�ł�";
		}
		if (message.equals("")) {
			return file;
		} else {
			System.out.println(message);
			return null;
		}
	}

	/***************************************************************************
	 * �o�̓t�@�C���`�F�b�N<br>
	 * 
	 * @param pPath
	 *            �t�@�C���p�X
	 * @return �t�@�C���N���X if ( FileUtil.oFileChk(iPath).equals("") ){ //OK File
	 *         wFile = new File(path); }
	 **************************************************************************/
	public static String oFileChk(String path) {
		String wEmsg = "";
		File wFile = new File(path);
		try {
			if (!wFile.exists()) {
			} else if (!wFile.isFile()) {
				wEmsg = "�ʏ�t�@�C���ł͂���܂���F" + path;
			} else if (!wFile.canWrite()) {
				wEmsg = "�����o���\�ł͂���܂���F" + path;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			wEmsg = "�Z�L�����e�C��O�ł�";
		}
		if (!wEmsg.equals("")) {
			System.out.println("#ERROR @oFileChk " + wEmsg);
		}
		return wEmsg;
	}

	public static void copyItTest(String[] args) {
		if (args.length != 2) {
			System.out.println("�g����: java FileUtil ����File �o��OutFile");
			System.exit(1);
		}
		fileCopy(args[1], args[0]);
	}

	// ----------------------------------------------------------------------
	// �t�@�C���̑��݃`�F�b�N
	// ----------------------------------------------------------------------
	public static boolean isExists(String path) {
		if (new File(path).exists()) {
			return true;
		} else {
			return false;
		}
	}

	// �t�@�C�������b�N���ꂽ�ꏊ���f�o�b�O���邽�߂ɂ�����
	// kyPkg.uFile.FileUtil_.isLocked(ResControl.D_DRV + "workspace/QPRweb/~/janresult.txt","#check01#");
	public static boolean isLocked(String path, String comment) {
		File orgFile = new java.io.File(path);
		String wkFile = "./" + kyPkg.uDateTime.DateCalc.getTimeStamp();
		File tstFile = new java.io.File(wkFile);
		if (orgFile.renameTo(tstFile)) {
			tstFile.renameTo(new File(path)); // ���Ƃɖ߂�
			System.out.println("-------------------------------------------");
			System.out.println("-- File is unLocked  " + comment + "  ");
			System.out.println("-------------------------------------------");
			return true;
		} else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("XX File is Locked " + comment + "---> " + path);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			return false;
		}
	}
	// �g�p�၄ String mmDir = FileUtil.getMMDir("");
	// public static String getMMDir() {
	// String mmDir = ResControl.D_DRV + "eclipse/workspace/kyProject/";
	// return mmDir;
	// }

	// -------------------------------------------------------------------------
	// �g�p�၄ String xxx = FileUtil.charsetConv(xxx);
	// -------------------------------------------------------------------------
	public static String charsetConv(String src) {
		String charsetName = java.nio.charset.Charset.defaultCharset().name();
		// System.out.println("charsetName:"+charsetName);
		String dest = src + "<UnsupportedEncoding>";
		try {
			dest = new String(src.getBytes(), charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dest;
	}

	// String resDir = kyPkg.uFile.ResControl.getResDir(path);
	// public static String getResDir(String path) {
	// return "../../../resources/" + path;
	// }

	// FileUtil. renameIt( path_out, path_tmp, path_in);
	public static boolean renameIt(String path_out, String path_tmp,
			String path_in) {
		return renameIt(new File(path_out), new File(path_tmp),
				new File(path_in));
	}

	public static boolean renameIt(File file_out, File file_temp,
			File file_in) {
		// �������̓p�X�Əo�̓p�X�������ꍇ�ɂ́D�D�D
		if (file_in.getAbsolutePath().equals(file_out.getAbsolutePath())) {
			file_in.renameTo(new File(file_in.getAbsolutePath() + ".bak")); // �o�b�N�A�b�v���c��
		}
		if (file_out.exists())
			file_out.delete(); // �����Ə�����̂��ǂ����A�㏑���������v�m�F����
		file_temp.renameTo(file_out);
		System.out.println("#������" + file_out.getAbsolutePath());
		return true;
	}

	// -------------------------------------------------------------------------------
	// �t�@�C�����݂̂�Ԃ�
	// String fileName = FileUtil.getFileName("c:/zappa.txt"); // => zappa.txt
	// -------------------------------------------------------------------------------
	public static String getFileName(String pSrc) {
		return new File(pSrc).getName();
	}

	// -------------------------------------------------------------------------------
	// String tempDir = FileUtil.getTempDir();
	// String UserDir = FileUtil.getUserDir();
	// String CurrentDir = FileUtil.getCurrentDir();
	// System.out.print("tempDir :"+tempDir);
	// System.out.print("UserDir :"+UserDir);
	// System.out.print("CurrentDir:"+CurrentDir);
	// -------------------------------------------------------------------------------
	// "���[�U�̃z�[���f�B���N�g�� "��Ԃ�
	// String userDir = kyPkg.uFile.FileUtil.getUserDir( );
	// -------------------------------------------------------------------------------
	// �f�B���N�g�������A���Ȃ�������J�����g�p�X��Ԃ� �ǂ��Ȃ��̂Ŗv�I
	// String wPath = FileUtil.mkDirPlus( "./macDownloads");
	// -------------------------------------------------------------------------------
	// public static String mkDirPlus(String wPath){
	// File wDir = new File(wPath);
	// if( !wDir.exists() ) {
	// if (wDir.mkdirs()==false) wPath = ".";
	// }
	// return wPath;
	// }
	// -------------------------------------------------------------------------------
	// ���[�U�̌��݂̍�ƃf�B���N�g����Ԃ�(�����n�ɂ���ďI���Ɂ������̂��s��)
	// String currentDir = FileUtil.getCurrentDir( );
	// -------------------------------------------------------------------------------
	public static String getCurrentDir() {
		return getCurrentDir(false);
	}

	public static String getCurrentDir(boolean sw) {
		String currentDir = System.getProperty("user.dir").trim();
		if (sw && !currentDir.endsWith(FS)) {
			return currentDir + FS;
		}
		return currentDir;
	}

	/***************************************************************************
	 * ���\�[�X��ǂݍ��� javax.swing.ImageIcon<br>
	 * 
	 * @param pPath
	 *            �ǂݍ��݌��ƂȂ�p�X
	 * @return �C���[�W�A�C�R�� <br>
	 *         �s�g�p��t<br>
	 *         Icon icon = FileUtil.getImageIcon("images/DEAD.GIF"); JLabel
	 *         wLabel = new JLabel(icon);
	 **************************************************************************/
	public static ImageIcon getImageIcon(String pPath) {
		if (pPath == null || pPath.equals("")) {
			return null;
		}
		// ClassLoader loader = this.getClass().getClassLoader();
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		java.net.URL iURL = loader.getResource(pPath);
		return new ImageIcon(iURL); // System.out.println(iURL);
	}

	/***************************************************************************
	 * �t�@�C���̍ŏ��̕����i�g���q���܂܂Ȃ��j�����o��<br>
	 * 
	 * @param wPath_I
	 *            �t�@�C���̃p�X
	 * @return �t�@�C���� <br>
	 *         �s�g�p��t<br>
	 *         <br>
	 *         String wFirstName = FileUtil.getFirstName("c:\test.txt"); <br>
	 *         System.out.println("FirstName:"+wFirstName); �� / ����� ���΃p�X�͑ʖ�
	 **************************************************************************/
	public static String getFirstName(String pPath) {
		String wRtn = "";
		String wPath = pPath;
		// �t�@�C���Z�p���[�^�����݂��Ȃ��ꍇp1�͂O�ƂȂ�
		int p1 = wPath.lastIndexOf(FS);
		if (p1 < 0)
			p1 = wPath.lastIndexOf("/");
		p1++;
		int p2 = wPath.lastIndexOf('.');
		if (p2 == -1)
			p2 = pPath.length(); // �g���q���Ȃ��ꍇ
		if (p2 <= p1)
			p2 = pPath.length(); // ./aaa �̂悤�ȏꍇ
		if (p2 > p1 && p2 > 0)
			wRtn = wPath.substring(p1, p2);
		// System.out.print (" p1:"+p1);
		// System.out.println(" p2:"+p2);
		return wRtn;
	}

	/***************************************************************************
	 * �t�@�C���̍ŏ��̕����i�g���q���܂܂Ȃ��j�����o��<br>
	 * 
	 * @param wPath_I
	 *            �t�@�C���̃p�X
	 * @return �t�@�C���� <br>
	 *         �s�g�p��t<br>
	 * <br>
	 *         String wFirstName = FileUtil_.getFirstName("c:\test.txt"); <br>
	 *         System.out.println("FirstName:"+wFirstName); �� / ����� ���΃p�X�͑ʖ�
	 **************************************************************************/
	public static String getFirstName2(String pPath) {
		String wRtn = "";
		String wPath = normarizeIt(pPath);
		int p1 = wPath.lastIndexOf("/");
		if (p1 > 0) {
			p1++;
		} else {
			p1 = 0;
		}
		int p2 = wPath.lastIndexOf('.');
		if (p2 == -1)
			p2 = pPath.length(); // �g���q���Ȃ��ꍇ
		if (p2 <= p1)
			p2 = pPath.length(); // ./aaa �̂悤�ȏꍇ
		// System.out.println("p1:"+p1);
		// System.out.println("p2:"+p2);
		if (p2 > p1 && p2 > 0)
			wRtn = wPath.substring(p1, p2);
		return wRtn;
	}

	public static String getAbsPath(String targetPath) {
		File wFile = new File(targetPath);
		// 20130507 �O�̂��߁E�E
		if (wFile.exists() && (wFile.isFile() || wFile.isDirectory())) {
			return wFile.getAbsolutePath();
			// try {
			// return wFile.getCanonicalPath();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		return "";
	}

	/***************************************************************************
	 * �t�@�C���̍ŏ��̕����i�g���q���܂܂Ȃ��j�����o��<br>
	 * 
	 * @param wPath_I
	 *            �t�@�C���̃p�X
	 * @return �t�@�C���� <br>
	 *         �s�g�p��t<br>
	 *         <br>
	 *         String wFirstName = FileUtil.getAbsFirst("c:\dirx\test.txt");
	 *         <br>
	 *         wFirstName => c:\dirx\test �� / ����� ���΃p�X�͑ʖ�
	 **************************************************************************/
	public static String getAbsFirst(String pPath) {
		String wRtn = "";
		String wPath = new File(pPath).getAbsolutePath();
		int wPos = wPath.lastIndexOf('.');
		if (wPos == -1)
			wPos = pPath.length(); // �g���q���Ȃ��ꍇ
		if (wPos <= 0)
			wPos = pPath.length(); // ./aaa �̂悤�ȏꍇ
		if (wPos > 0)
			wRtn = wPath.substring(0, wPos);
		return wRtn;
	}

	/***************************************************************************
	 * �e�̃p�X���擾 (File.getParent()�͎g�����̂ɂȂ�Ȃ��̂ŁE�E)<br>
	 * �����ӂ��K�Ɂ���t���Ȃ��ƑʖځI<br>
	 * 
	 * @param pPath
	 *            �t�@�C���p�X
	 * @return �����Ȃ�,�p�X������ <br>
	 *         �s�g�p��t<br>
	 *         <br>
	 *         String mamapath = FileUtil.getPpath("c:\ga\bba\gabba\hey.txt");
	 *         <br>
	 *         = > c:\ga\bba\gabba���Ԃ�
	 **************************************************************************/
	// public static String getParent(String pPath) {
	// return FileUtil_.getParent(pPath, false);
	// }
	// FileUtil.getPpath("c:\ga\bba\gabba\hey.txt");
	// public static String getParent(String pPath, boolean sw) {
	// if (pPath.indexOf(".") > 0) { // �t�@�C�������܂܂�Ă��邩
	// int pos = pPath.lastIndexOf(FS);
	// if (pos < 0)
	// pos = pPath.lastIndexOf("/");
	// if (pos < 0) {
	// System.out.println("#Error Not Directory Path!\n\t=>" + pPath);
	// return "";
	// }
	// pPath = pPath.substring(0, pos); // ParentPath��ݒ肵����
	// }
	// if (sw && !pPath.endsWith(FS)) {
	// return pPath.trim() + FS;
	// }
	// return pPath.trim();
	// }

	/***************************************************************************
	 * �t�@�C�����𕪊�����E�E���s����null��Ԃ�
	 * 
	 * @param pPath
	 *            �ǂݍ��݌��ƂȂ�p�X
	 * @return �����z�� <br>
	 *         �s�g�p��t<br>
	 *         String[] wAns = FileUtil.fileNameAna(wPath_I); if (wAns!=null){
	 *         System.out.println(" �e�p�X :"+wAns[0]); // ex "c:\aaa\bbb"
	 *         System.out.println(" �t�@�C����:"+wAns[1]); // ex "name"
	 *         System.out.println(" �g���q :"+wAns[2]); // ex ".txt" }
	 **************************************************************************/
	public static String[] fileNameAna(String pPath) {
		pPath = new File(pPath).getAbsolutePath();
		int p1 = pPath.lastIndexOf(FS);
		int p2 = pPath.lastIndexOf('.');
		// System.out.print (" p1:"+p1);
		// System.out.println(" p2:"+p2);
		String[] wAns = null;
		if (p2 > p1 & p1 > 0 & p2 > 0) {
			wAns = new String[3];
			wAns[0] = pPath.substring(0, p1);
			wAns[1] = pPath.substring(p1 + 1, p2);
			wAns[2] = pPath.substring(p2).toLowerCase();
		}
		return wAns;
	}

	/***************************************************************************
	 * �o�b�N�A�b�v�p�t�@�C���̃p�X������쐬<br>
	 * 
	 * @param wPath_I
	 *            ���̃p�X
	 * @param wExt
	 *            �g���q�E�E�E "" ���w�肷��ƃI���W�i���Ɠ����g���q
	 * @param pOpt
	 *            true �Ȃ�t�@�C�����Ɠ������O�̃t�H���_���쐬
	 * @return �o�b�N�A�b�v�p�t�@�C���̃p�X������ <br>
	 *         ��t String oPath =
	 *         FileUtil.makeBkupPath("c:/test.frm","txt",true); <br>
	 *         = > c:��test��test.txt �ƂȂ� <br>
	 *         String oPath = makeBkupPath("c:/test.frm","",false); <br>
	 *         = > c:��test.txt�i �����̏ꍇ�A���p�X�����ʂ͓����ɂȂ�j
	 **************************************************************************/
	public static String makeBkupPath(String wPath_I) {
		return makeBkupPath(wPath_I, "bak", false);
	}

	public static String makeBkupPath(String wPath_I, String wEx) {
		return makeBkupPath(wPath_I, wEx, true);
	}

	// -------------------------------------------------------------------------
	public static String makeBkupPath(String wPath_I, String wExt,
			boolean pOpt) {
		String[] val = wPath_I.split("\\."); // �� split �̈����� Regix
		String wPath_O;
		String sDir = val[0]; // �s���I�h�̒��O�܂�
		if (wExt.trim().equals(""))
			wExt = val[1]; // �s���I�h�̒���`�i�g���q�j
		if (pOpt) {
			File wDir = new File(sDir);
			if (wDir.isDirectory() == false) {
				wDir.mkdir();
			}
			String wSep = FS;
			int pos = sDir.lastIndexOf(wSep);
			if (pos < 0)
				pos = 0;
			String wName = sDir.substring(pos, sDir.length());
			wPath_O = sDir + wSep + wName + "." + wExt;
		} else {
			wPath_O = sDir + "." + wExt;
		}
		return wPath_O;
	}

	/***************************************************************************
	 * �ċA�I�Ƀf�B���N�g������J���ăp�X�̈ꗗ�\���쐬����
	 * 
	 * @param pPath
	 *            �ǂݍ��݌��ƂȂ�p�X <br>
	 *            �s�g�p��t<br>
	 *            <br>
	 *            ��?@�t�J�����g�f�B���N�g���̃p�X�ꗗ��RC.log�t�@�C���ɏo�͂��� <br>
	 *            try{ <br>
	 *            FileOutputStream fo = new FileOutputStream("./RC.log"); <br>
	 *            FileUtil.recurList(new File("."),fo); <br>
	 *            fo.close(); <br>
	 *            catch(IOException e){ e.printStackTrace(); } <br>
	 *            ��?A�t <br>
	 *            FileUtil.recurList(new File("c:/"),System.out);
	 **************************************************************************/
	public static void recurList(File pFile, OutputStream pOst) {
		String[] dLIst = new File(pFile, ".").list();
		for (int i = 0; i < dLIst.length; i++) {
			File wFile = new File(pFile, dLIst[i]); // �����p�����[�^�K�{�I
			if (wFile.isDirectory()) {
				recurList(wFile, pOst);
			} else {
				String wRec = wFile.getAbsolutePath() + LF;
				try {
					pOst.write(wRec.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/***************************************************************************
	 * �ǂݍ��݉\�� �����o���\���H
	 * 
	 * @param pPath
	 *            ��������t�@�C���̃p�X
	 * @return �ǂݏ����Ȃ�True���Ԃ� <br>
	 *         �s�g�p��t<br>
	 *         System.out.println("�ǂݏ����H�F"+FileUtil.canRW("./RC.log"));
	 **************************************************************************/
	public static boolean canRW(String pPath) {
		boolean bStat = false;
		File fl = new File(pPath);
		// &&�̏ꍇ�A�Е���false���������������̃`�F�b�N�����Ȃ�
		if (fl.canRead() == true && fl.canWrite() == true)
			bStat = true;
		// System.out.println("�ǂݍ��݉\�� " + fl.canRead());
		// System.out.println("���������\�� " + fl.canWrite());
		return bStat;
	}

	/***************************************************************************
	 * �t�@�C���T�C�Y�𒲂ׂ�
	 * 
	 * @param pPath
	 *            ��������t�@�C���̃p�X
	 * @return �t�@�C���T�C�Y���Ԃ� <br>
	 *         �s�g�p��t<br>
	 *         System.out.print(FileUtil.fileSize("./RC.log")+" Byte");
	 **************************************************************************/
	public static long fileSize(String pPath) {
		File wCur = new File(pPath);
		return wCur.length();
	}

	public static int fileSizeK(String pPath) {
		long byteSize = fileSize(pPath);
		return (int) (byteSize / 1024);
	}

	public static int fileSizeM(String pPath) {
		long byteSize = fileSize(pPath);
		return (int) (byteSize / (1024 * 1024));
	}


//	public static String fileUpdate(String pPath,String fmt) {
//		return DateCalc.getLastModDate(pPath, "yyyy/MM/dd HH:mm:ss");
//	}

	/***************************************************************************
	 * �e�L�X�g�t�@�C�����R�s�[���� <br>
	 * �i�o�C�g�P�ʂȂ̂Ńo�C�i���[�j<br>
	 * 
	 * @param dstPath
	 *            �o�̓t�@�C���p�X
	 * @param srcPath
	 *            ���̓t�@�C���p�X
	 * 
	 * @return �����Ȃ�True <br>
	 *         �s�g�p��t<br>
	 *         bool b = FileUtil.copyIt(pPath_I,pPath_O); if (b == null) return;
	 **************************************************************************/
	public static boolean fileCopy(String dstPath, String srcPath) {
		if (srcPath.equals(dstPath)) {
			System.out.println("���o�͂̃t�@�C��������ł��I:" + srcPath);
			return false;
		}
		if (iFileChk(srcPath) == null)
			return false;
		File iFile = new File(srcPath);
		if (!oFileChk(dstPath).equals(""))
			return false;
		// ���̓p�X���f�B���N�g���Ȃ�E�E�V�����f�B���N�g������邾���ɂ��悤�I�I
		if (iFile.isDirectory() == true)
			return true;
		File oFile = new File(dstPath);
		if (oFile.exists())
			oFile.delete();
		if (iFile != null && oFile != null) {
			FileInputStream iSt = create_I_Stream(iFile);
			FileOutputStream oSt = create_O_Stream(oFile);
			if (iSt != null && oSt != null) {
				int rByte = 0;
				byte[] buff = new byte[8192];
				try {
					while ((rByte = iSt.read(buff)) != -1) {
						oSt.write(buff, 0, rByte);
					}
					oSt.close();
					iSt.close();
				} catch (IOException ie) {
					System.out.println("IOException on copyIt");
					System.exit(1);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		return true;
	} // end of copyIt

	/***************************************************************************
	 * �t�@�C�����ړ�����<br>
	 * �i�J�^���O��j<br>
	 * 
	 * @param pPath_I
	 *            ���t�@�C���p�X
	 * @param pPath_O
	 *            �V�t�@�C���p�X
	 * @return �����Ȃ�True
	 **************************************************************************/
	public static boolean moveIt(String pOld, String pNew) {
		if (pOld.equals(pNew)) {
			System.out.println("���o�͂̃t�@�C��������ł��I:" + pOld);
			return false;
		}
		boolean bStat = false;
		File wFile_O = new File(pOld);
		File wFile_N = new File(pNew);
		if (wFile_O.exists() == false) {
			System.out.println("Error!���t�@�C�������݂��܂���E�E:" + pOld);
		} else {
			if (wFile_N.exists()) {
				if (wFile_N.canWrite()) {
					wFile_N.delete(); // ���݂��������
				} else { // �������Ȃ�������ǂ�����H
					System.out.println("Error!���t�@�C�������݂��������܂���:" + pNew);
					return false;
				}
			}
			makeParents(pNew); // �e�p�X�����
			bStat = wFile_O.renameTo(wFile_N);
		}
		return bStat;
	} // End of moveIt

	// -------------------------------------------------------------------------
	// �g���q��ύX����@
	// ex> kyPkg.uFile.FileUtil.renExt(inPath, "old");
	// -------------------------------------------------------------------------
	public static boolean renExt(String path, String ext) {
		String newPath = "";
		int endIndex = path.indexOf(".");
		if (endIndex > 0) {
			newPath = path.substring(0, endIndex + 1) + ext;
		} else {
			newPath = path.trim() + "." + ext;
		}
		return moveIt(path, newPath);
	}

	/***************************************************************************
	 * rename �t�@�C�����̕ύX�E�E�E�s�t�@�C���̈ړ����\�t<br>
	 * 
	 * @param oldName
	 *            ���t�@�C���p�X
	 * @param newName
	 *            �V�t�@�C���p�X
	 * @return �����Ȃ�True <br>
	 *         �s�g�p��t<br>
	 *         <br>
	 *         boolean boo = FileUtil.rename("tubby.txt","DubWise/gabba.txt");
	 *         <br>
	 *         boolean boo = FileUtil.moveIt("tubby.txt","DubWise/gabba.txt");
	 **************************************************************************/
	public static boolean rename(String oldName, String newName) {
		return moveIt(oldName, newName);
	}

	/***************************************************************************
	 * �t�@�C�����폜����
	 * 
	 * @param pPath
	 *            �폜����t�@�C���̃p�X
	 * @return �����Ȃ�True���Ԃ� <br>
	 *         �s�g�p��t<br>
	 *         FileUtil.killIt("./RC.log"); FileUtil.delIt("./RC.log");
	 **************************************************************************/
	public static boolean killIt(String pPath) {
		// System.out.println("killIt===>"+pPath);
		return delIt(pPath);
	}

	public static boolean delIt(String pPath) {
		boolean bStat = false;
		File fl = new File(pPath);
		if (fl.exists() == true) {
			bStat = fl.delete();
		} else {
			// System.out.println("��on delIt FileNotExist:"+pPath);
		}
		return bStat;
	}

	// -------------------------------------------------------------------------
	// �f�B���N�g�����̃t�@�C���ꗗ
	// ���� String pDir �Ώۃf�B���N�g���̃p�X ex) "."
	// -------------------------------------------------------------------------
	// ��?@�t
	// String[] wList = FileUtil.fileList(".");
	// for(int i = 0;i<wList.length;i++){
	// System.out.println(">>" + wList[i]);
	// }
	// -------------------------------------------------------------------------
	public static String[] fileList(String pDir) {
		File fl = new File(pDir);
		return fl.list();
	}

	/***************************************************************************
	 * filteredList �w�肵���g���q�̃t�@�C���̈ꗗ �s�����N���X�C���i�[�N���X�Łt ���� String pDir �Ώۃf�B���N�g���̃p�X
	 * ex) "." String pExt �I�[���� ex) ".txt"
	 * -------------------------------------------------------------------------
	 * ��?@�t String[] wList = FileUtil.filteredList(".",".bat"); for(int i =
	 * 0;i<wList.length;i++){ System.out.println("filteredList>" + wList[i]); }
	 **************************************************************************/
	public static String[] filteredList(String pDir, final String pExt) {
		//#createTester--------------------------------------------------
		//		System.out.println("public static void testfilteredList() {");
		//		System.out.println("    String pDir = \"" + pDir + "\";");
		//		System.out.println("    final pExt = " + pExt + ";");
		//		System.out.println("    String[] ar = filteredList(pDir,String);");
		//		System.out.println("}");
		//--------------------------------------------------
		File fl = new File(pDir);
		String[] array = fl.list(new FilenameFilter() {
			@Override
			public boolean accept(File pDir, String pName) {
				return pName.toLowerCase().endsWith(pExt);
			}
		});
		//null���Ԃ��Ă��܂��̂ŏC���@20170327
		if (array == null)
			array = new String[] {};
		return array;
	}

	// FileUtil.getDirList
	// �f�C���N�g���ȉ��ɂ���A�f�B���N�g�����̈ꗗ�����X�g�ŕԂ��i�e�p�X�͕t���Ȃ��j
	public static List<String> getDirList(String path) {
		return getFileNameList(path, "Dir");
	}

	// �f�C���N�g���ȉ��ɂ���A�t�@�C�����̈ꗗ�����X�g�ŕԂ��i�e�p�X�͕t���Ȃ��j
	public static List<String> getFileList(String path) {
		return getFileNameList(path, "FILE");
	}

	public static List<String> getFileNameList(String path, String option) {
		option = option.toUpperCase();
		List<String> dirNames = new ArrayList();
		List<String> fileNames = new ArrayList();
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				dirNames.add(files[i].getName());
			}
			if (files[i].isFile()) {
				fileNames.add(files[i].getName());
			}
		}
		if (option.equals("FILE")) {
			return fileNames;
		}
		return dirNames;
	}

	// -------------------------------------------------------------------------
	// �s�����N���X�C���i�[�N���X�Łt�Ɉ�{�� �i���̃R�[�h�����s�\�j
	// -------------------------------------------------------------------------
	// filteredList2 �g���q"java"�̃t�@�C���̈ꗗ ���vMyFilter�N���X��
	// class MyFileFilter implements FilenameFilter{
	// public boolean accept(File pDir,String pName){
	// return pName.toLowerCase().endsWith(".java");
	// }
	// }
	// -------------------------------------------------------------------------
	// ��?@�t
	// String[] wList = FileUtil.filteredList3(".");
	// for(int i = 0;i<wList.length;i++){
	// System.out.println("java>" + wList[i]);
	// }
	// -------------------------------------------------------------------------
	// public static String[] filteredList3(String pDir){
	// File fl = new File(pDir);
	// return fl.list(new MyFileFilter());
	// }
	// -------------------------------------------------------------------------
	// filteredList2 �w�肵���g���q�̃t�@�C���̈ꗗ �s�C���i�[�N���X�Łt
	// �����N���X�ɂł��Ȃ����H�H
	// -------------------------------------------------------------------------
	// ��?@�t
	// String[] wList = FileUtil.filteredList2(".",".bat");
	// for(int i = 0;i<wList.length;i++){
	// System.out.println("xxx>" + wList[i]);
	// }
	// -------------------------------------------------------------------------
	// public static String[] filteredList2(String pDir,String pExt){
	// class INMyFileFilter implements FilenameFilter{
	// String wExt;
	// INMyFileFilter(String qExt){ wExt = qExt; } // Constructor
	// public boolean accept(File pDir,String pName){
	// return pName.toLowerCase().endsWith(wExt);
	// }
	// }
	// File fl = new File(pDir);
	// return fl.list(new INMyFileFilter(pExt));
	// }
	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------
	// �g���q�ɂ���ăt�@�C�����폜���� �� FileUtil.delByExt(".",".txt")
	// -------------------------------------------------------------------------
	public static boolean delByExt(String pDir, String pExt) {
		boolean bStat = true;
		String[] wList = filteredList(pDir, pExt);
		for (int i = 0; i < wList.length; i++) {
			System.out.println("dwlByExt>" + wList[i]);
			File fl = new File(pDir + FS + wList[i]);
			if (fl.exists() == true) {
				bStat = fl.delete();
				System.out.println("delete ok?");
			} else {
				bStat = false;
				System.out.println("Not Exist");
			}
		}
		return bStat;
	}

	/***************************************************************************
	 * ���K�\���Ƀ}�b�`����t�@�C���̈ꗗ <br>
	 * �s�����N���X�C���i�[�N���X�Łt <br>
	 * 
	 * @param pDir
	 *            �Ώۃf�B���N�g���̃p�X ex) "."
	 * @param pExt
	 *            ���K�\�� ex) "sm.*txt"
	 * @return �t�@�C���̈ꗗ <br>
	 *         �s�g�p��t<br>
	 *         <br>
	 *         String[] wList = FileUtil.RegfilteredList(".","SM.*txt"); <br>
	 *         for(int i = 0;i<wList.length;i++){ <br>
	 *         System.out.println("xxx>" + wList[i]); <br>
	 **************************************************************************/
	// import java.util.regex.*;
	public static String[] regixFilteredList(String pDir, final String pRegex) {
		if (!pDir.endsWith("/")) {
			pDir = pDir.trim() + "/";//20170327�@�듮���h�~����
		}
		File fl = new File(pDir);
		return fl.list(new FilenameFilter() {
			@Override
			public boolean accept(File pDir, String pName) {
				//				 System.out.println("��on accept pName:"+pName);
				return pName.toLowerCase().matches(pRegex.toLowerCase());
			}
		});
	}

	/***************************************************************************
	 * �f�B���N�g�����쐬����i�p�X�Ɋ܂܂����̃X�x�e�I�I�j<br>
	 * ��ꂽ�ꍇ�͐e�p�X��Ԃ��A���s������""��Ԃ�<br>
	 * 
	 * @param dir
	 *            �t�@�C���p�X
	 * @return �����Ȃ�,�p�X������ <br>
	 *         �s�g�p��t<br>
	 *         <br>
	 *         String mamaPath =
	 *         FileUtil.makeParents("c:\ga\bba\gabba\hey.txt");
	 **************************************************************************/
	// -------------------------------------------------------------------------------
	// ���Y�f�B���N�g�������݂��Ȃ���΂�����쐬���āA���Ƀt�@�C����؂蕶����Y���ĕԂ�
	// String localDir = FileUtil.mkdir("c:/suzy/cream/cheeze/");
	// String localDir = FileUtil.mkdir("c:/suzy/cream/cheeze");
	// localDir=> c:\suzy\cream\cheeze\
	// ���p�����[�^�̏I��肪�p�X��؂蕶������Ȃ��Ă��n�j
	// -------------------------------------------------------------------------------
	// public static String mkdir(String pPath) {
	// pPath = charsetConv(pPath);
	// File file = new File(pPath);
	// if (!file.exists())
	// file.mkdirs();
	// return file.getPath() + "/";
	// // return file.getPath() + FS;
	// }
	// Directory����肻�̃p�X��Ԃ��i�p�X�̂��K��FS�����I�j
	// kyPkg.uFile.FileUtil.mkdir("c:/hello/")
	public static String mkdir(String dir) {
		return makedir(dir);
	}

	// kyPkg.uFile.FileUtil.makedir(dir);
	public static String makedir(String pPath) {
		// getAbsolutePath("c:/frank/zappa/")
		// �t�@�C���̋�؂蕶���͂��ׂ�FS�ɕϊ����ꂽ��ň�ԍŌ��FS���͂������̂�
		// �Ԃ�͂����Ȃ遁��c:\frank\zappa �̂�FS���R���J�`����E�E�E
		// System.out.println("DEBUG pPath:"+pPath);
		pPath = charsetConv(pPath);
		String abs = new File(pPath).getAbsolutePath() + FS;
		File file = new File(abs);
		if (file.exists() == false) {
			if (file.isDirectory() == false) {
				if (file.mkdirs() == false) {
					System.out.println("@makedir Error :" + abs);
					abs = "";
				}
			}

		}
		abs = abs.replaceAll("\\\\", "/");
		return abs.trim();
	}

	// �� kyPkg.uFile.FileUtil.makeParents("./test/Some.txt");
	public static String makeParents(String path) {
		String wPath = "";
		if (!path.trim().equals("")) {
			File file = new File(path);
			wPath = file.getAbsolutePath();
			//	���͂��h��:\samples\result.txt�h�̎��ɁE�E�E
			//			System.out.println("#1 getPath:" + file.getPath());
			// if (wPath.indexOf(".") < 0) wPath = wPath +
			// System.getProperty("file.separator");
			int pos = wPath.lastIndexOf(FS);
			if (pos > 0)
				wPath = wPath.substring(0, pos);
			// System.out.println("makeParents oya?!:"+wPath);
			makedir(wPath);
			// wFile = new File(wPath);
			// if (wFile.exists() == true) {
			// // System.out.println("# File Path already
			// // existed!\n\t=>"+wPath);
			// wPath = "";
			// } else {
			// if (wFile.mkdirs() == false)
			// wPath = ""; // ����������
			// }
		}
		return wPath;
	}

	/***************************************************************************
	 * �f�B���N�g�����ƃR�s�[���� <br>
	 * �����N���X�Ȃ̂Ŏg�p���@�ɒ��ӁI<br>
	 * 
	 * @param pOld
	 *            ���t�@�C���p�X
	 * @param pNew
	 *            �V�t�@�C���p�X
	 * @return �����Ȃ�True <br>
	 *         �s�g�p��t<br>
	 *         <br>
	 *         new FileUtil.CopyDir(".","c:/Target");
	 **************************************************************************/
	public class CopyDir {
		String gTarget;

		int gLen;

		boolean gMove;

		// -------------------------------------------------------------------------
		// Constructor
		// -------------------------------------------------------------------------
		public CopyDir(String pSource, String pTarget) {
			this(pSource, pTarget, false); // copy !!
		}

		public CopyDir(String pSource, String pTarget, boolean pMove) {
			if (pSource.equals(pTarget)) {
				System.out.println("#Error ���o�͂̃t�@�C���������F" + pSource);
			} else {
				File wFile = new File(pSource);
				if (wFile.exists() == true && wFile.isDirectory() == true) {
					gTarget = pTarget;
					gMove = pMove;
					gLen = pSource.length();
					recurCopy(wFile);
				} else {
					System.out.println("#Error ���̓p�X�ُ�F" + pSource);
				}
			}
		}

		/***********************************************************************
		 * �ċA�I�ɃR�s�[����<BR>
		 * 
		 * @param pParent
		 *            �e�t�@�C���N���X
		 **********************************************************************/
		public void recurCopy(String path) {
			File pParent = new File(path);
			recurCopy(pParent);
		}

		public void recurCopy(File pParent) {
			String[] dLIst = new File(pParent, ".").list();
			for (int i = 0; i < dLIst.length; i++) {
				File wFile = new File(pParent, dLIst[i]); // �����p�����[�^�K�{�I
				if (wFile.isDirectory()) {
					recurCopy(wFile);
				} else {
					String wSorce = wFile.getPath();
					String wTarget = gTarget + wSorce.substring(gLen);
					System.out.println("  ��" + wSorce + "  ��" + wTarget);
					// mkdir & Copy �����Ȃ̂��E�E�E
					makeParents(wTarget);
					if (gMove == true) {
						moveIt(wSorce, wTarget);
					} else {
						fileCopy(wTarget, wSorce);
					}
				}
			}
		}
	} // End of class CopyDir

	/***************************************************************************
	 * ���̓X�g���[���쐬<br>
	 * 
	 * @param pPath
	 *            �t�@�C���p�X
	 * @return ���̓X�g���[��
	 **************************************************************************/
	private static FileInputStream create_I_Stream(File pFile) {
		try {
			FileInputStream wstr = new FileInputStream(pFile);
			return wstr;
		} catch (FileNotFoundException fe) {
			System.out.println("�t�@�C����������܂���F" + pFile.toString());
		} catch (SecurityException se) {
			System.out.println("�Z�L�����e�B��O�F" + pFile.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	/***************************************************************************
	 * �o�̓X�g���[���쐬<br>
	 * 
	 * @param pPath
	 *            �t�@�C���p�X
	 * @return �o�̓X�g���[��
	 **************************************************************************/
	private static FileOutputStream create_O_Stream(File pFile) {
		try {
			FileOutputStream wstr = new FileOutputStream(pFile);
			return wstr;
		} catch (FileNotFoundException fe) {
			System.out.println(
					"#ERROR@create_O_Stream �t�@�C�����J���܂���F" + pFile.toString());
		} catch (SecurityException se) {
			System.out.println(
					"#ERROR@create_O_Stream �Z�L�����e�B��O�F" + pFile.toString());
		} catch (Exception e) {
			System.out.println("#ERROR@create_O_Stream ??�F" + pFile.toString());
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	// /***************************************************************************
	// * �o�̓t�@�C���`�F�b�N<br>
	// * @param pPath
	// * �t�@�C���p�X
	// * @return �t�@�C���N���X if ( FileUtil_.oFileChk(iPath).equals("") ){ //OK File
	// * wFile = new File(path); }
	// **************************************************************************/
	public static void string2file_(String outPath, String strData,
			String encode) {
		ListArrayUtil.array2File(outPath, new String[] { strData }, encode);
	}

	public static void string2file_(String outPath, String strData) {
		ListArrayUtil.array2file(outPath, new String[] { strData });
	}

	// -------------------------------------------------------------------------
	// outFile �̍ŏI�s�ɕ������t��������
	// -------------------------------------------------------------------------
	public static boolean str2FileMod(String outPath, String wRec) {
		if (!new File(outPath).exists()) {
			System.out.println("�o�̓p�X�����݂��܂���:" + outPath);
			return false;
		}
		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(outPath, true));
			bw.write(wRec);
			bw.write(LF);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------
	// outFile �� modFile��Ǐ�������
	// -------------------------------------------------------------------------
	public static int fileMod(String outFile, String modFile) {
		return fileMod(outFile, modFile, true);
	}

	// -------------------------------------------------------------------------
	// outFile �� modFile���������ށiappend��true�Ȃ�Ǐ����Afalse�Ȃ�㏑���j
	// -------------------------------------------------------------------------
	public static int fileMod(String outPath, String path, boolean append) {
		int cnt = 0;
		makeParents(outPath); // �e�p�X��������΍��
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(outPath, append));
			while (br.ready()) {
				cnt++;
				String wRec = br.readLine();
				bw.write(wRec);
				bw.write(LF);
			}
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnt;
	}

	/***************************************************************************
	 * ReadHex �w�L�T�_���v��\������<br>
	 * 
	 * @param pFile
	 *            �ǂݍ��ރt�@�C��
	 * @param wSkip
	 *            �X�L�b�v����s���i�P�U�r�b�g�P�ʁj
	 * @param wMaxl
	 *            �ǂݍ��ލs���i�O���w�肷��ƍŌ�܂œǂݍ��ށj <br>
	 *            �g�p�� wStr = ReadHex(argv[0],wSkip,wMaxl); <br>
	 *            �Ԓl���u�������������ł��Ȃ����낤���H�H�H���킵�Ă݂悤�I <br>
	 *            �ۑ�F���o�C�g������\���ł��Ȃ����낤���H
	 **************************************************************************/
	public static String readAsHex(String pFName, long pSkip, long pMax) {
		int poz = 0;
		long pCnt = 0;
		byte[] buff = new byte[1024];
		boolean cont = true;
		FileInputStream infile = null;
		StringBuffer sBuf1 = new StringBuffer("");
		StringBuffer sBuf2 = new StringBuffer("");
		StringBuffer sBuf3 = new StringBuffer("");

		if (iFileChk(pFName) == null) {
			return "Error";
		}
		File wFile = new File(pFName);

		// System.out.println("File.length:" + wFile.length());
		if (pMax < 1)
			pMax = (wFile.length() / 16) + 1 - pSkip;
		pSkip = pSkip * 16;
		if (wFile.length() < pSkip) {
			System.out.println("skip�p�����[�^���t�@�C���T�C�Y�𒴂��܂���");
			return "";
		}
		// --------------------------------------------------
		try {
			infile = new FileInputStream(pFName);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		// --------------------------------------------------
		try {
			long wSkip = infile.skip(pSkip);
			System.out.println("Skipped:" + wSkip);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// --------------------------------------------------
		while (cont) {
			try {
				int n = infile.read(buff, 0, 16);
				poz += n;
				if (n > 0) {
					pCnt++;
					if (pCnt == pMax)
						cont = false;
					sBuf1.delete(0, sBuf1.length());
					sBuf2.delete(0, sBuf2.length());
					for (int i = 0; i < n; i++) {
						// ----------------------------------
						char hex1, hex2;
						hex1 = (char) (buff[i] & 0xF0);
						hex1 >>= 4;
						hex2 = (char) (buff[i] & 0x0F);
						// ----------------------------------
						if (hex1 >= 10) {
							hex1 = (char) (hex1 + 'A' - 10);
						} else {
							hex1 = (char) (hex1 + '0');
						}
						// ----------------------------------
						if (hex2 >= 10) {
							hex2 = (char) (hex2 + 'A' - 10);
						} else {
							hex2 = (char) (hex2 + '0');
						}
						// ----------------------------------
						// System.out.print( " "+hex1 + hex2);
						sBuf2.append(" " + hex1 + hex2);
						if (buff[i] < 0x20 || 0x7e < buff[i]) {
							sBuf1.append("."); // �\���\�����ȊO
						} else {
							sBuf1.append((char) (buff[i]));
						}
					}
					for (int i = 0; i < (16 - n); i++) {
						sBuf2.append("   "); // padding!
					}
					// System.out.println(sBuf2.toString() + " |" +
					// sBuf1.toString());
					sBuf3.append(
							sBuf2.toString() + " |" + sBuf1.toString() + "\n");
					// System.out.write(buff,0,n);
				} else
					cont = false;
			} catch (EOFException e) {
				// e.printStackTrace();
				System.out.println("<<EOF???>>");
				cont = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			infile.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		// System.out.println(sBuf3.toString());
		return (sBuf3.toString());
	}

	static public byte[] readByte(File objFile, int iStartPos, int iLength) {
		FileInputStream objFIS = null;
		byte byteBuff[] = null;
		if (objFile.length() < iStartPos + iLength) {
			return null;
		}
		try {
			objFIS = new FileInputStream(objFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		try {
			byteBuff = new byte[iLength];
			objFIS.skip(iStartPos);
			objFIS.read(byteBuff, 0, iLength);
			objFIS.close();
		} catch (IOException e) {
			System.out.print("MP3Info::readByte() " + objFile.getName() + "\n");
			e.printStackTrace();
			return null;
		}
		return byteBuff;
	}

	static public boolean readHeader(String path, String key) {
		File objFile = new File(path);
		byte[] byteArray = FileUtil.readByte(objFile, 0, key.length());
		if (byteArray != null
				&& (new String(byteArray, 0, key.length())).matches(key))
			return true;
		return false;
	}

	/***************************************************************************
	 * JTextArea�Ƀf�[�^��ǂݍ���<br>
	 * 
	 * @param pTex
	 *            �ǂݍ��ݐ�ƂȂ�JTextArea
	 * @param pFile
	 *            �ǂݍ��ރt�@�C�� String ans = kyPkg.util.FileUtil.file2String(path);
	 **************************************************************************/
	public static String file2Str(String pPath) {
		int charsRead = 0;
		File file = new File(pPath);
		long lFileSize = file.length();
		if (lFileSize > Integer.MAX_VALUE) {
			System.out.println(
					"#ERROR @file2Str Integer.MAX_VALUE < " + lFileSize);
			return null;
		}
		int fileSize = (int) lFileSize;
		char charArray[] = new char[fileSize];
		try {
			FileReader fr = new FileReader(pPath);
			charsRead = fr.read(charArray);
			System.out.println(
					"charsRead: " + (charsRead / (1024 * 1024)) + "MByte");
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		String rStr = "overFlow";
		System.out.println(
				"x  charsRead: " + (charsRead / (1024 * 1024)) + "MByte");
		try {
			System.out.println("debug1: ");
			rStr = new String(charArray);
			System.out.println("debug2: ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(
				"xx charsRead: " + (charsRead / (1024 * 1024)) + "MByte");
		return rStr.trim();
	}

	public static String file2String(String path) {
		if (path == null)
			return null;
		if (path.equals(""))
			return null;
		return file2String(path, 1, Integer.MAX_VALUE);
	}

	public static void file2Stream(java.io.PrintWriter writer, String path) {
		if (iFileChk(path) == null)
			return;
		File pFile = new File(path);
		long lFileSize = pFile.length();
		System.out.println("xxfile2Stream path: " + path);
		System.out.println("xxfile2Stream canRead: " + pFile.canRead());

		if (lFileSize > Integer.MAX_VALUE) {
			System.out.println(
					"#ERROR @file2Str Integer.MAX_VALUE < " + lFileSize);
			return;
		}
		int fileSize = (int) lFileSize;
		System.out.println(
				"xxfile2Stream fileSize: " + (fileSize / (1024)) + "KByte");
		// System.out.println("xxfile2Stream fileSize:
		// "+(fileSize/(1024*1024))+"MByte");

		InputStreamReader isr = null;
		// ---------------------------------------------------------------------
		// ���ǂݍ��݂��s���A�G���R�[�h�𔻒肵�������E�E�E
		// ---------------------------------------------------------------------
		// unicode�Ɖ��肵�ēǂ�ł݂�A�G���[�Ȃ�JISAutoDetect�Ƃ���
		// �������E�EUTF�̏ꍇException�ƂȂ炸�ɕ����������������iT.T�j
		// ---------------------------------------------------------------------
		// String wCharSet = "UnicodeLittleUnmarked";
		// wCharSet = "JISAutoDetect";

		// ---------------------------------------------------------------------
		try {
			int integer;
			// isr = new InputStreamReader(new FileInputStream(pFile),
			// wCharSet);
			isr = new InputStreamReader(new FileInputStream(pFile));
			while ((integer = isr.read()) != -1) {
				// System.out.print(integer);
				writer.write(integer);
			}
			isr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * JTextArea�Ƀf�[�^��ǂݍ���<br>
	 * 
	 * @param pTex
	 *            �ǂݍ��ݐ�ƂȂ�JTextArea
	 * @param pFile
	 *            �ǂݍ��ރt�@�C��
	 * @param pFrom
	 *            �ǂݍ��݊J�n�s
	 * @param pLimit
	 *            �ǂݍ��ݏI���s <br>
	 *            ��unicode�΍���ǂ����邩�H�H <br>
	 *            �ǂݍ��ޔ͈� ���s�ڂ���A���s�ڂ܂ł̎w���ǉ�����
	 **************************************************************************/
	public static String file2String(String path, int pFrom, int pLimit) {
		File pFile = iFileChk(path);
		if (pFile == null)
			return null;
		long lFileSize = pFile.length();
		if (lFileSize > Integer.MAX_VALUE) {
			System.out.println(
					"#ERROR @file2Str Integer.MAX_VALUE < " + lFileSize);
			return null;
		}
		int fileSize = (int) lFileSize;

		StringBuffer sBuf = new StringBuffer(fileSize);
		FileInputStream fs = null;
		InputStreamReader isr = null;
		// ---------------------------------------------------------------------
		// ���ǂݍ��݂��s���A�G���R�[�h�𔻒肵�������E�E�E
		// ---------------------------------------------------------------------
		// unicode�Ɖ��肵�ēǂ�ł݂�A�G���[�Ȃ�JISAutoDetect�Ƃ���
		// �������E�EUTF�̏ꍇException�ƂȂ炸�ɕ����������������iT.T�j
		// ---------------------------------------------------------------------
		String wCharSet = "UnicodeLittleUnmarked";
		wCharSet = "JISAutoDetect";
		// try {
		// fs = new FileInputStream(pFile);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// try {
		// isr = new InputStreamReader(fs, wCharSet);
		// // System.out.println("InputStreamReader.getEncoding():" +
		// // isr.getEncoding());
		// BufferedReader br = new BufferedReader(isr);
		// if (br.ready()) {
		// br.readLine();
		// // �ȉ��̕��ŃG���[���E���Ȃ����H
		// // wRec = br.readLine();
		// // wRec = new String(s.getBytes("JISAutoDetect"), "SJIS")
		// }
		// br.close();
		// // } catch (sun.io.MalformedInputException me){
		// // wCharSet ="JISAutoDetect";
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// ---------------------------------------------------------------------
		try {
			try {
				fs = new FileInputStream(pFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			isr = new InputStreamReader(fs, wCharSet);
			// -----------------------------------------------------------------
			// �^��Ffilereader���ƃL�����N�^�[�Z�b�g�͎w��ł��Ȃ��̂��H
			// -----------------------------------------------------------------
			BufferedReader br = new BufferedReader(isr);
			int i = 0;
			int iLct = 0;
			String wRec;
			// -----------------------------------------------------------------
			// Skip!!
			// -----------------------------------------------------------------
			for (int j = 0; j < (pFrom - 1); j++) {
				if (br.ready()) {
					iLct++;
					br.readLine();
				}
			}
			// -----------------------------------------------------------------
			// Loop!!
			// -----------------------------------------------------------------
			while (br.ready()) {
				wRec = br.readLine();
				// wRec = new String(s.getBytes("JISAutoDetect"), "SJIS")
				i++;
				iLct++;
				if (wRec != null) {
					// System.out.println(wRec);
					sBuf.append(wRec);
					sBuf.append("\n");
				}
				if (i >= pLimit)
					break;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("@@@" );
		return sBuf.toString();
	}

	// ------------------------------------------------------------------------
	// �s Ufile2String �t�g�p�ӏ���JP_IOtest 279�s��
	// �Ȃ��킴�킴�L�����N�^�[�z��ɓǂݍ���ł���̂��H�H
	// �Ӑ}���킩��Ȃ�
	// ------------------------------------------------------------------------
	public String readUnicode(String path) {
		if (iFileChk(path) == null)
			return "";
		File pFile = new File(path);
		StringBuffer buff = new StringBuffer();
		try {
			DataInputStream dataInput = new DataInputStream(
					new FileInputStream(pFile));
			int nDataLen = dataInput.available();
			for (int nLoop = 0; nLoop < nDataLen; nLoop++) {
				char ch = dataInput.readChar(); // ���̓X�g���[���̎��� 2 �o�C�g��\�� Unicode����
				System.out.print(">>" + ch);
				buff.append(ch);
			}
			dataInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	// ------------------------------------------------------------------------
	// DataOutputStream
	// �f�[�^�o�̓X�g���[�����g���ƁA�v���~�e�B�u�^�� Java �f�[�^��
	// �ڐA���̂���`�ŏo�̓X�g���[���ɏ������ނ��Ƃ��ł��܂��B
	// ���\�b�h
	// void flush()
	// int size()
	// void write(byte[] b, int off, int len)
	// void write(int b)
	// void writeBoolean(boolean v)
	// void writeByte(int v)
	// void writeBytes(String s)
	// void writeChar(int v)
	// void writeChars(String s)
	// void writeDouble(double v)
	// void writeFloat(float v)
	// void writeInt(int v)
	// void writeLong(long v)
	// void writeShort(int v)
	// void writeUTF(String str)
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// ��������w�肳�ꂽ�G���R�[�h�ɕϊ�����
	// <<charsetName �����Z�b�g>>
	// US-ASCII 7 �r�b�g ASCII (ISO646-US/Unicode �����Z�b�g�� Basic Latin �u���b�N)
	// ISO-8859-1 ISO Latin Alphabet No. 1 (ISO-LATIN-1)
	// UTF-8 8 �r�b�g UCS �ϊ��`��
	// UTF-16BE 16 �r�b�g UCS �ϊ��`���A�r�b�O�G���f�B�A���o�C�g��
	// UTF-16BE 16 �r�b�g UCS �ϊ��`���A���g���G���f�B�A���o�C�g��
	// UTF-16 16 �r�b�g UCS �ϊ��`���A�I�v�V�����̃o�C�g���}�[�N�Ŏ��ʂ����o�C�g��
	// ------------------------------------------------------------------------
	public static String encode(String str, String charsetName) {
		// String ���o�C�g�V�[�P���X�ɕ��������A���ʂ�V�K�o�C�g�z��Ɋi�[�B
		try {
			byte[] byteArray = str.getBytes(charsetName);
			return new String(byteArray, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	// -------------------------------------------------------------------------
	// �f�t�H���g�̕����Z�b�g��Ԃ��܂��B(ex windows-31j)
	// System.out.println(kyPkg.uFile.FileUtil.getDefaultCharset());
	// -------------------------------------------------------------------------
	public static String getDefaultCharset() {
		return (java.nio.charset.Charset.defaultCharset()).name();
	}

	/***************************************************************************
	 * �w�肳�ꂽ�G���g���[����菜���iSchema.ini�t�@�C���Ȃǁj<br>
	 * 
	 * @param path
	 *            �ǂݍ��ރt�@�C���ւ̃p�X
	 * @param outPath
	 *            �����o���t�@�C���ւ̃p�X
	 * @param pEntry
	 *            ��������G���g���[ <br>
	 *            �s�g�p��t<br>
	 *            new FileUtil().rmvEnt(ISAM.SCHEMA_INI,"Copy.txt","[K2.TXT]");
	 *            ���̂��ƃ��l�[�����Ƃ��I
	 **************************************************************************/
	public void rmvEnt(String path, String outPath, String pEntry) {
		boolean wFlg = true;
		boolean append = true;
		System.out.println("��rmvEnt pI_Path:" + path);
		System.out.println("         pO_Path:" + outPath);
		System.out.println("         pEntry:" + pEntry);
		try {
			int i = 0;
			String wRec;
			if (new File(path).exists() == false) {
				return; // ���̓t�@�C�������������炳��Ȃ炷��
			}
			// if (new File(pO_Path).exists()==false) pAppend = false;
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(outPath, append));
			// -----------------------------------------------------------------
			while (br.ready()) {
				i++;
				wRec = br.readLine();
				System.out.println("wRec>" + wRec);
				if (wRec.startsWith("[")) {
					System.out.println("[wRec>" + wRec);
					if (wRec.startsWith(pEntry)) {
						wFlg = false;
					} else {
						wFlg = true;
					}
				}
				if (wFlg == false) {
					System.out.println("rejected>" + wRec);
				} else {
					bw.write(wRec, 0, wRec.length());
					bw.write(LF, 0, LF.length()); // ���s�R�[�h
				}
			}
			bw.close();
			br.close();
			// -----------------------------------------------------------------
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	} // end of rmvEnt

	/***************************************************************************
	 * �e�[�u�����f�����̃f�[�^���t�@�C���ɏ����o��
	 * 
	 * @param resultPath
	 *            �t�@�C���̏o�͐�p�X
	 * @param tModel
	 *            ���̓f�[�^�i�e�[�u�����f���j
	 * @param headOption
	 *            �v�w�b�_�[���ǂ���
	 * @param delimiter
	 *            ��؂蕶��
	 * @return �����o�����s��
	 **************************************************************************/
	public static String getTModelHeader(TableModel tModel, String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < tModel.getColumnCount(); j++) {
			if (j > 0)
				buf.append(delimiter);
			buf.append(tModel.getColumnName(j));
		}
		buf.append(LF); // ���s�R�[�h
		return buf.toString();
	}

	/***************************************************************************
	 * �e�[�u�����f�����̃f�[�^���t�@�C���ɏ����o��
	 * 
	 * @param outPath
	 *            �t�@�C���̏o�͐�p�X
	 * @param tModel
	 *            ���̓f�[�^�i�e�[�u�����f���j
	 * @param headOption
	 *            �v�w�b�_�[���ǂ���
	 **************************************************************************/
	public int tmdl2file(String outPath, TableModel tModel,
			boolean headOption) {
		// �g���q�ɂ��A��؂蕶���̔�������悤�E�E
		if (outPath.indexOf(".") == -1)
			outPath = outPath + ".CSV";
		String[] val = outPath.split("\\."); // �� split �̈����� Regix
		String wExt = val[1].toUpperCase(); // �s���I�h�̒���`�i�g���q�j
		String delimiter = "";
		System.out.println("�g���q�F" + wExt + "�ɂ���ċ�؂蕶���𔻒肵�Ă���");
		if (wExt.equals("TXT")) {
			delimiter = "\t";
		} else if (wExt.equals("CSV")) {
			delimiter = ",";
		} else if (wExt.equals("PRN")) {
			delimiter = "\t";
		} else if (wExt.equals("TSV")) {
			delimiter = "\t";
		} else if (wExt.equals("SSV")) {
			delimiter = " ";
		} else {
			delimiter = "";
		}
		return tmdl2file(outPath, tModel, headOption, delimiter);
	}

	public int tmdl2file(String outPath, TableModel tModel, boolean headOption,
			String delimiter) {

		int wCnt = 0;

		//String encode = SHIFT_JIS;
		String encode = MS932;//20161222

		//		String newVal = "�[";
		//		String regex = "�`";

		System.out.println("tmdl2file => outPath:" + outPath);
		try {
			OutputStreamWriter writer = FileUtil.getWriter(outPath, encode);
			String wRec = "";
			StringBuffer buf = new StringBuffer();
			// -----------------------------------------------------------------
			// �g������
			// -----------------------------------------------------------------
			if (headOption == true) {
				wRec = getTModelHeader(tModel, delimiter);
				//				wRec = StringEncoder.utf8ToSjis(wRec);
				wRec = Onbiki.cnv2Similar(wRec, FileUtil.defaultEncoding);
				writer.write(wRec, 0, wRec.length());
				wCnt++;
			}
			// -----------------------------------------------------------------
			// �a������
			// -----------------------------------------------------------------
			// int wRow = tModel.getRowCount(); // �s��
			for (int i = 0; i < tModel.getRowCount(); i++) {
				buf.delete(0, buf.length()); // �o�b�t�@���N���A
				for (int j = 0; j < tModel.getColumnCount(); j++) {
					if (j > 0)
						buf.append(delimiter);
					Object wObj = tModel.getValueAt(i, j);
					if (wObj != null) {
						buf.append(wObj.toString());
					} else {
						buf.append("");
					}
				}
				wCnt++;
				buf.append(LF); // ���s�R�[�h
				wRec = buf.toString();
				//				wRec=wRec.replaceAll(regex, newVal);
				//				wRec = StringEncoder.utf8ToSjis(wRec);
				wRec = Onbiki.cnv2Similar(wRec, FileUtil.defaultEncoding);

				//				wRec = new String(wRec.getBytes(encode), encode);
				// System.out.println("wRec>" + wRec);
				// oBw.write(wRec, 0, wRec.length());
				writer.write(wRec, 0, wRec.length());
			}
			// oBw.close();
			writer.close();
			// -----------------------------------------------------------------
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return wCnt;
	} // tmdl2file

	// ------------------------------------------------------------------------
	// �t�@�C���̏����X�g�����O�ŕԂ�
	// pPath �t�@�C���̃p�X
	// ��t String wFinfo = fileInfo("System.ini");
	// ------------------------------------------------------------------------
	public static String fileInfo(String pPath) {
		pPath = new File(pPath).getAbsolutePath();
		String wMsg;
		StringBuffer sBuf = new StringBuffer();
		sBuf.delete(0, sBuf.length());
		File wFile = new File(pPath);
		sBuf.append("�t�@�C����    : " + wFile.getName());
		sBuf.append((wFile.isFile() ? " �t�@�C���ł�\n" : " ���O�p�C�v���A�f�B���N�g���ł�\n"));
		// sBuf.append("�t�@�C���T�C�Y: " + (wFile.length())+"\n");
		sBuf.append("�t�@�C���T�C�Y: " + (wFile.length()) + " Byte \n");
		sBuf.append("�t�@�C���T�C�Y: " + (wFile.length() / 1024) + " KByte \n");
		sBuf.append("�t�@�C���̃p�X: " + wFile.getPath() + "\n");
		sBuf.append("��΃p�X        : " + wFile.getAbsolutePath() + "\n");
		// �����vimport java.text.*;
//		String wDate = DateFormat.getDateInstance().format(new Date(wFile));
		String wDate = DateCalc.getLastModDate(pPath, "yyyyMMddHHmmss");

		sBuf.append("�ŏI�C��      : " + wDate + "\n");
		sBuf.append((wFile.exists() ? "���݂��܂�" : "���݂��܂���") + "\n");
		sBuf.append((wFile.canRead() ? "�ǂݍ��݉\" : "�ǂݍ��ݕs�\") + "\n");
		// sBuf.append((wFile.canWrite() ? "�������݉\" : "�������ݕs�\")+"\n");
		sBuf.append((wFile.isDirectory() ? "�f�B���N�g���ł�" : "�f�B���N�g���ł͂Ȃ�") + "\n");
		if (wFile.isDirectory()) {
			wMsg = "#<<Directory>>########################################";
			sBuf.append(wMsg + "\n");
			File[] flist = wFile.listFiles();
			for (int i = 0; i < flist.length; i++) {
				sBuf.append(">" + flist[i].getName() + "\n");
			}
		}
		return sBuf.toString();
	}

	/***************************************************************************
	 * BufferedReader��Ԃ�<br>
	 * 
	 * @param path
	 *            �ǂݍ��ރt�@�C��
	 * @return ���s������null���Ԃ� <br>
	 *         �� <br>
	 *         try{ <br>
	 *         BufferedReader br = new
	 *         FileUtil().getISReader("C:\#340018000310.IT2"); <br>
	 *         if (br.ready()) { <br>
	 *         String wRec = br.readLine(); <br>
	 *         <br>
	 *         br.close(); <br>
	 *         catch (Exception e) { <br>
	 *         e.printStackTrace(); <br>
	 **************************************************************************/
	public static BufferedReader getBufferedReader(String path) {
		if (iFileChk(path) == null)
			return null;
		try {
			return new BufferedReader(new FileReader(new File(path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//	public static BufferedReader getBufferedReader(String path) {
	//		try {
	//			return new BufferedReader(new InputStreamReader(
	//					new FileInputStream(path), DEFAULT_ENCODE));
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}

	/***************************************************************************
	 * �g���q����f���~�^�[������<br>
	 * 
	 * @param ext
	 *            �g���q
	 * @return ��؂蕶�� <br>
	 **************************************************************************/
	public static String getDefaultDelimiter(String ext) {
		ext = ext.toUpperCase().trim();
		String delimiter = "";
		if (ext.equals("")) {
			delimiter = " ";
		} else if (ext.equals("BAK")) {
			delimiter = ",";
		} else if (ext.equals("TMP")) {
			delimiter = ",";
		} else if (ext.equals("TXT")) {
			delimiter = ",";
		} else if (ext.equals("CSV")) {
			delimiter = ",";
		} else if (ext.equals("PRN")) {
			delimiter = "\t";
		} else if (ext.equals("TSV")) {
			delimiter = "\t";
		} else if (ext.equals("DAT")) {
			delimiter = "\t";
		} else if (ext.equals("OLD")) {
			delimiter = "\t";
		} else {
			delimiter = "\t";
		}
		return delimiter;
	}

	// -------------------------------------------------------------------------------
	// �p�X��؂蕶�����h/�h�ɕϊ�����
	// String path ="c:\\suzy\\cream\\cheeze\\zappa.txt";
	// path = kyPkg.uFile.FileUtil_.normarizeIt(path);
	// System.out.println("path:"+path);
	// System.out.println("normarizeIt:"+FileUtil_.normarizeIt("c:\\suzy\\cream\\cheeze\\zappa.txt"));
	// -------------------------------------------------------------------------------
	public static String normarizeIt(String path) {
		String PS = System.getProperty("file.separator");
		if (PS.equals("\\"))
			PS = "\\\\";
		path = path.replaceAll(PS, "/");
		return path;
	}

	public static String cnv2localStyle(String path) {
		String PS = System.getProperty("file.separator");
		if (PS.equals("\\"))
			PS = "\\\\";
		path = path.replaceAll("/", PS);
		return path;
	}

	/***************************************************************************
	 * �t�@�C���̊g���q�����o��<br>
	 * 
	 * @param pPath
	 *            �t�@�C���̃p�X
	 * @return �s���I�h�̒���`�i�g���q�j <br>
	 *         �s�g�p��t<br>
	 * <br>
	 *         String wExt = FileUtil_.getExt("c:\test.ext"); <br>
	 *         => EXT
	 **************************************************************************/
	public static String getExt(String pPath) {
		pPath = normarizeIt(pPath);
		String[] splited = pPath.split("/");
		String[] val = splited[splited.length - 1].split("\\.");
		if (val.length > 1) {
			return val[val.length - 1].toUpperCase();
		}
		return "";
	}

	/***************************************************************************
	 * �t�@�C���̊g���q���O�̕�����Ԃ�<br>
	 * 
	 * @param inPath
	 *            �t�@�C���̃p�X
	 * @return �t�@�C���̊g���q���O�̕��� <br>
	 *         �s�g�p��t<br>
	 * <br>
	 *         String wExt = FileUtil_.getPreExt("c:\\test.ext"); <br>
	 *         => c:\test
	 **************************************************************************/
	public static String getPreExt(String inPath) {
		String wExt = getExt(inPath);// �g���q�����o��
		if (wExt.equals("")) {
			return inPath;
		} else {
			return inPath.substring(0, inPath.length() - (wExt.length() + 1));
		}
	}

	/***************************************************************************
	 * �e�̃p�X���擾 (File.getParent()�͎g�����̂ɂȂ�Ȃ��̂ŁE�E)<br>
	 * �����ӂ��K�Ɂ���t���Ȃ��ƑʖځI<br>
	 * 
	 * @param pPath
	 *            �t�@�C���p�X
	 * @return �����Ȃ�,�p�X������ <br>
	 *         �s�g�p��t<br>
	 * <br>
	 *         String mamapath = FileUtil_.getParent("c:\ga\bba\gabba\hey.txt"); <br>=
	 *         > c:\ga\bba\gabba���Ԃ�
	 **************************************************************************/
	public static String getParent(String pPath) {
		// ���I�[�h/�h���܂܂�Ȃ��̂Œ��ӁI�I�i�܂߂����ꍇ�I�v�V�����t�����g���j
		return getParent(pPath, false);
	}

	// option��ture�Ȃ�I�[�h/�h��ǉ�����
	public static String getParent2(String pPath, boolean option) {
		pPath = FileUtil.normarizeIt(pPath);
		if (pPath.indexOf(".") > 0) { // �t�@�C�������܂܂�Ă��邩
			// int pos = pPath.lastIndexOf(FS);
			// if (pos < 0)
			int pos = pPath.lastIndexOf("/");
			if (pos < 0) {
				System.out.println("#Error Not Directory Path!\n\t=>" + pPath);
				return "";
			}
			pPath = pPath.substring(0, pos); // ParentPath��ݒ肵����

		}
		if (option && !pPath.endsWith("/")) {
			return pPath.trim() + "/";
		}
		return pPath.trim();
	}

	//-------------------------------------------------------------------------
	// option��ture�Ȃ�I�[�h/�h��ǉ�����
	//-------------------------------------------------------------------------
	public static String getParent(String pPath, boolean option) {
		pPath = normarizeIt(pPath);
		if (pPath.indexOf(".") > 0) { // �t�@�C�������܂܂�Ă��邩
			// int pos = pPath.lastIndexOf(FS);
			// if (pos < 0)
			int pos = pPath.lastIndexOf("/");
			if (pos < 0) {
				System.out.println("#Error Not Directory Path!\n\t=>" + pPath);
				return "";
			}
			pPath = pPath.substring(0, pos); // ParentPath��ݒ肵����

		}
		if (option && !pPath.endsWith("/")) {
			return pPath.trim() + "/";
		}
		return pPath.trim();
	}

	// �e�p�X��������������Ԃ�
	// String name = kyPkg.uFile.FileUtil_.getName(path);
	public static String getName(String pPath) {
		String wPath = normarizeIt(pPath);
		int p1 = wPath.lastIndexOf("/");
		if (p1 > 0) {
			p1++;
		} else {
			p1 = 0;
		}
		return wPath.substring(p1);
	}

	// ----------------------------------------------------------------
	// �g���q��ύX�����p�X��Ԃ��i�g���q�����݂��Ȃ���΂��̂܂܊g���q������j
	// �g�p�၄�@
	// String path="c:/suzy/cream/cheeze/zappa.txt";
	// System.out.println("extConvert>"+kyPkg.uFile.FileUtil_.getExtConvPath(path,"xls"));
	// ----------------------------------------------------------------
	public static String changeExt(String path, String newExt) {
		return getPreExt(path) + "." + newExt;
	}

	public static String getAbsolutePath(String pPath) {
		return new File(pPath).getAbsolutePath();
	}

	/***************************************************************************
	 * �p�X���w�肵�ăX�g���[�����C�^�[�I�u�W�F�N�g���擾����<br>
	 * 
	 * @param pFile
	 *            �����o���t�@�C��
	 * @param strData
	 *            �ǂݍ��݌��ƂȂ�String FileUtil.string2file("c:/dummy.txt","Hello");
	 **************************************************************************/
	public static OutputStreamWriter getWriterEx(String outPath) {
		return getWriter(outPath, "");
	}

	public static OutputStreamWriter getWriter(String outPath) {
		//		return getWriter(outPath, SHIFT_JIS);
		return getWriter(outPath, MS932);//20161222
	}

	public static OutputStreamWriter getWriter(String outPath, String encode) {
		return getStreamWriter(outPath, encode, true);
	}

	//-------------------------------------------------------------------------
	// String encode = "UTF-8"; utf-8���o�͂����
	// String encode = "MS932";������������
	// String encode = "ISO-2022-JP"; ������������
	// String encode = "Shift_JIS";
	//-------------------------------------------------------------------------
	public static OutputStreamWriter getStreamWriter(String outPath,
			String encode, boolean append) {
		OutputStreamWriter writer = null;
		FileUtil.makeParents(outPath); // �e�p�X��������΍��
		String message = FileUtil.oFileChk(outPath);
		if (!message.equals("")) {
			System.out.println("@getStreamWriter#error:" + message);
			return null;
		}
		try {
			if (encode == null || encode.trim().equals("")) {
				encode = (java.nio.charset.Charset.defaultCharset()).name();
			}
			FileOutputStream fo = new FileOutputStream(new File(outPath));
			// FileOutputStream fo = new FileOutputStream(new File(outPath),
			// append);
			writer = new OutputStreamWriter(fo, encode);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer;
	}

	//	private static void iterator2file(String outPath, Iterator iterator,
	//			String encode) {
	//		try {
	//			OutputStreamWriter writer = FileUtil.getWriter(outPath, encode);
	//			if (writer != null) {
	//				for (Iterator it = iterator; it.hasNext();) {
	//					String line = (String) it.next();
	//					writer.write(line, 0, line.length());
	//					writer.write(LF, 0, LF.length()); // ���s�R�[�h
	//				}
	//				writer.close();
	//			}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	// -------------------------------------------------------------------------------
	// tester
	// -------------------------------------------------------------------------------
	//	public static void test01() {
	//		String iPath = Transfer.F_MMTRANSFER
	//				+ "dataBase/data/qpr_monitor_out.txt";
	//		if (kyPkg.uFile.FileUtil.isExists(iPath) == false) {
	//			System.out.println(iPath + "��������Ȃ��̂Ń��j�^�[�t�@�C���̏������s���܂���ł����B");
	//		} else {
	//			System.out.println(iPath + "�͑��݂��܂�");
	//		}
	//	}

	public static void test03() {
		// ���Y�f�B���N�g���ōŐV�̂��̂��E���グ��
		String dirPath = ResControl.D_DRV + "workspace/QPRweb/�_�ː���/data/";
		// List<String> list =
		// kyPkg.uFile.ListArrayUtil.dir2ListWithDir(dirPath,"\\S*\\.xls");
		List<String> list = kyPkg.uFile.ListArrayUtil.dir2ListWithDir(dirPath,
				"\\.xls$");
		// *.txt
		if (list != null)
			System.out.println("answer:" + list.get(0));
	}

	public static void testMakedir() {

	}

	//20161118
	public static void testfilteredList() {
		String pDir = "C:/@QPR/home/123620000001/tran/";
		String pExt = ".txt";
		String[] ar = filteredList(pDir, pExt);
		for (String element : ar) {
			System.out.println(">>" + element);
		}
	}

	// -------------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------------
	public static void main(String[] args) {
		testfilteredList();
		//		testMakedir();
		//		test03();
	}
}

/*
 * �� save as �̏������E�E�E String wPath = ""; JFileChooser fc = new JFileChooser(".");
 * if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { wPath =
 * fc.getSelectedFile().toString(); }else{ return; }
 * 
 * 
 * jBtXXX.addActionListener(new ActionListener(){ public void
 * actionPerformed(ActionEvent arg0) { jBtFch.setEnabled(false); JFileChooser fc
 * = new JFileChooser("./"); //
 * fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
 * fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //
 * fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); if
 * (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
 * jTfPath.setText(fc.getSelectedFile().toString()); } jBtFch.setEnabled(true);
 * } });
 */
