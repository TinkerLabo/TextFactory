package kyPkg.uFile;

import java.io.*;
import java.util.*;

import kyPkg.sql.TContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import globals.ResControlWeb;

import static kyPkg.uFile.FileUtil.*;

//XXX schema��Ԃ��Ȃ����H�H2012-05-02�@Happy Birthday Anthony!!
/*******************************************************************************
 * �s File49ers �t 2007-05-11 LastUpdate 2008-06-04 Yuasa<BR>
 * ���ӁI�I�^�u������ƗD�悳��܂� ���́A��؂蕶���̑������ƂȂ�܂�
 * 
 * @quthor Ken Yuasa
 * @version Version 1.0
 * @since SINCE java1.3
 ******************************************************************************/

// BOM�i�o�C�g���}�[�N�j�̎g�p�ɂ���
// UTF-8�ŕ������ꂽ�e�L�X�g�f�[�^�̓G���f�B�A���Ɋւ�炸�������e�ɂȂ�̂ŁA�o�C�g���}�[�N (BOM) �͕K�v�Ȃ��B
// �������A�e�L�X�g�f�[�^��UTF-8�ŕ���������Ă��邱�Ƃ̕W���Ƃ��āA
// �f�[�^�̐擪��EF BB BF�i16�i�BUCS�ł̃o�C�g���}�[�NU+FEFF��UTF-8�ł̕\���j��t�����邱�Ƃ�����B
// �Ȃ��A���̃V�[�P���X���������UTF-8�A�Ȃ�����UTF-8N�ƌĂԂ��Ƃ����邪�A
// ���̂悤�ȌĂѕ����͓��{�ȊO�ł͂قƂ�ǒm���Ă��炸�A�܂����I�K�i�Ȃǂɂ�闠�t�����Ȃ��B
// ���̂��߁AUTF-8�Ƃ����Ăі����g���Ă���Ώ������̑��肪�����擪�ɂ��̃V�[�P���X������ƌ��Ȃ��Ɗ��҂��ׂ��ł͂Ȃ����A
// �����ۂ��AUTF-8N�Ƃ����Ăі��͏������̍ۂɗp����ׂ��ł͂Ȃ��B

// LGPL�� juniversalchardet���g�p���邩�H�H���̏ꍇ���C�u�����̐ݒu���@�𒲂ׂĂ݂�
// http://java.akjava.com/library/juniversalchardet
// kyPkg.uFile.File49ers
public class File49ers {
	public static final String APPENDA_CLASS = "kyPkg.sql";
	public static Log log = LogFactory.getLog(TContainer.APPENDA_CLASS);
	static final boolean DEBUG = false;
	//	private String encoding = System.getProperty("file.encoding");
	private String encoding = FileUtil.getDefaultEncoding();//20161222
	private String delimiter = "";
	private int maxColCount = 0;
	private boolean exists = true;

	public boolean isExists() {
		return exists;
	}

	private boolean file = true;

	public boolean isFile() {
		return file;
	}

	private boolean canRead = true;

	public boolean isCanRead() {
		return canRead;
	}

	public String getEncoding() {
		return encoding;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public int getMaxColCount() {
		return maxColCount;
	}

	// ��؂蕶���͂Ȃɂ��H�D�揇�ʂ�����̂��������낤 �X�y�[�X�������Ȃ肻���Ȃ������邪
	// ������ޑ��݂��������H�i�T�����x���ōő�̂��̂�Ԃ��j
	// �G���R�[�f�B���O�͂Ȃɂ�����ł���Ƃ����ȁ[
	class LittleObj {
		private String delim = "";
		private int mxCol = 0;

		public LittleObj(String delim) {
			this.delim = delim;
		}

		public String getDelim() {
			return delim;
		}

		public void checkIt(String wRec) {
			String[] array = wRec.split(delim, -1);
			// XXX ���ׂċ�؂蕶���̃f�[�^�ł�split����肭�����Ȃ��@�P���ɂȂ��Ă��܂��o�O����
			// System.out.println("checkIt=============> array.length:"+array.length);
			// ��؂蕶�������݂��Ȃ��Ă����������̂ŕK���P�ȏ�ƂȂ�
			if (this.mxCol < array.length)
				this.mxCol = array.length;
		}

		public int getMaxColm() {
			return mxCol;
		}
	}

	// �g�p��
	// -------------------------------------------------------------------------
	// File49ers f49 = new File49ers(pnlFile_J.getPath(),20);
	// System.out.println("getDelim:"+f49.getDelim());
	// System.out.println("getMaxColm:"+f49.getMaxColm());
	// -------------------------------------------------------------------------
	// 1 �s�̃e�L�X�g��ǂݍ��݂܂��B1 �s�̏I�[�́A���s (�u\n�v) ���A���A (�u\r�v)�A�܂��͕��s�Ƃ���ɑ������s�̂ǂꂩ�ŔF������܂��B
	public File49ers(String path) {
		this(path, 20, "", null);
	}

	public File49ers(String path, int limit, String pCharsetName,
			String defDelimiter) {
		int priority1 = -1;
		int priority2 = -1;
		File wFile = new File(path);
		if (!wFile.exists()) {
			exists = false;
			System.out.println("#error @File49ers  Not exists:" + path);
			log.info("#error @File49ers  Not exists:" + path);
			return;
		}
		if (!wFile.isFile()) {
			file = false;
			System.out.println("#error @File49ers  Not File:" + path);
			log.info("#error @File49ers  Not File:" + path);
			return;
		}
		if (!wFile.canRead()) {
			canRead = false;
			System.out.println("#error @File49ers  Can't Read:" + path);
			return;
		}

		//		if (!wFile.isHidden()) {
		//			canRead = false;
		//			System.out.println("#error @File49ers  Can't Read:" + path);
		//			return;
		//		}
		encoding = pCharsetName.trim();

		// kyPkg.tools.FileUtil_.isLocked(path, "<500 ok>");

		if (encoding.equals("")) {
			// JISAutoDetect�ɂ���ƁA�o�C�g�z��EUC��JIS��ShiftJIS�����������ʂ��Ă����B
			// charsetName = "JISAutoDetect";
			// charsetName = System.getProperty("file.encoding");//
			// windows=>MS932
			encoding = EncodeDetector.getEncode(path);

			if (DEBUG)
				System.out.println("charsetName:" + encoding);
			if (encoding.equals("WINDOWS-1252")) {
				//				encoding = SHIFT_JIS;
				encoding = defaultEncoding2;//20161222
				// TODO �w���f�[�^�������Ă��܂��̂ł����������E�E�E�{���ɂ���ł�������������
			}

		}
		// kyPkg.tools.FileUtil_.isLocked(path, "<501 ng>");

		int wCnt = 0;
		String wRec = "";
		List list = new ArrayList();
		if (defDelimiter == null) {
			list.add(new LittleObj(","));
			list.add(new LittleObj("\t"));
			list.add(new LittleObj(":"));
			list.add(new LittleObj(";"));
			// list.add( new LittleObj(">"));
			// list.add( new LittleObj("@"));
			//			list.add(new LittleObj(" "));//20161028
			list.add(new LittleObj("\n"));//20161028
		} else {
			list.add(new LittleObj(defDelimiter));
		}
		// kyPkg.tools.FileUtil_.isLocked(path, "<502>");

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), encoding));
			while (br.ready() && wCnt < limit) {
				wRec = br.readLine();
				// System.out.println("wRec=>" + wRec);
				// System.out.println("wRec.indexOf =>" + wRec.indexOf("?"));
				if (wRec != null) {
					wCnt++;
					for (int i = 0; i < list.size(); i++) {
						LittleObj lObj = (LittleObj) list.get(i);
						lObj.checkIt(wRec);
						if (lObj.getDelim().equals("\t")
								&& lObj.getMaxColm() > 1) {
							priority1 = i; // �^�u�����݂���ꍇ�����D�悳����E�E�E
						}
						if (lObj.getDelim().equals(",")
								&& lObj.getMaxColm() > 1) {
							priority2 = i; // ���̎��ɃJ���}��D�悳����E�E�E
						}
					}
				}
			}
			br.close();
		} catch (java.io.FileNotFoundException e) {
			canRead = false;
			System.out.println("#error @File49ers  Can't Read:" + path + " "
					+ e.toString());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		//		if (!wFile.canRead()) {
		//		canRead = false;
		//		System.out.println("#error @File49ers  Can't Read:" + path);
		//		return;
		//		}

		// kyPkg.tools.FileUtil_.isLocked(path, "<503>");
		// System.out.println("--------------------------------------------------");
		// for (int i = 0; i < list.size(); i++) {
		// LittleObj wkObj = (LittleObj) list.get(i);
		// delim = wkObj.getDelim();
		// int maxColm = wkObj.getMaxColm();
		// System.out.println(">> delim=>" + delim + "<= getMaxColm:"+ maxColm);
		// }

		LittleObj maxObj = null;
		if (priority1 >= 0) {
			maxObj = (LittleObj) list.get(priority1);
		} else {
			if (priority2 >= 0) {
				maxObj = (LittleObj) list.get(priority2);
			} else {
				maxObj = (LittleObj) Collections.max(list,
						new lObjComparator());
			}
		}
		delimiter = maxObj.getDelim();
		maxColCount = maxObj.getMaxColm();
		// System.out.println("pPath:" + path);
		// System.out.println("delim:" + delim);
		// System.out.println("@File49 maxColm:" + maxColCount);
	}

	// JISAutoDetect�𗘗p���ăG���R�[�h�𔻒�
	public static String getEncodeType(String rStr) {
		ArrayList<String> encList = new ArrayList();
		encList.add(UTF_8);
		encList.add(UTF_16);
		encList.add(MS932);
		encList.add(EUC_JP);
		encList.add(SJIS);
		encList.add(ISO2022JP);
		encList.add(JIS0201);
		encList.add(JIS0208);
		encList.add(JIS0212);
		encList.add(CP930);
		encList.add(CP939);
		encList.add(CP942);
		encList.add(CP943);
		encList.add(CP33722);
		String autoEnc = "";
		try {
			autoEnc = new String(rStr.getBytes("iso-8859-1"), "JISAutoDetect");
			for (String encode : encList) {
				String specific = new String(rStr.getBytes("iso-8859-1"),
						encode);
				if (specific.equals(autoEnc))
					return encode;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private class lObjComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			LittleObj obj1 = (LittleObj) o1;
			LittleObj obj2 = (LittleObj) o2;
			return obj1.getMaxColm() - obj2.getMaxColm();
		}
	}

	// �g�p��A
	public static void test01() {
		// 2�̃C���X�^���X�������Ȃ����ǂ����m�F
		String path_L = ResControlWeb
				.getD_Resources_Templates("cluster2007.txt");
		String path_R = ResControlWeb
				.getD_Resources_Templates("cluster2008.txt");

		File49ers f49_L = new File49ers(path_L);
		String delim_L = f49_L.getDelimiter();
		int max_L = f49_L.getMaxColCount();
		System.out.println("delim:" + delim_L);
		System.out.println("max:" + max_L);

		File49ers f49_R = new File49ers(path_R);
		String delim_R = f49_R.getDelimiter();
		int max_R = f49_R.getMaxColCount();
		System.out.println("delim:" + delim_R);
		System.out.println("max:" + max_R);
	}

	public static void test02() {
		String path = ResControlWeb.getD_Resources_Templates("cluster2007.txt");
		File49ers insF49 = new File49ers(path);
		// String encoding = insF49.getEncoding();
		String delimiter = insF49.getDelimiter();
		int maxCol = insF49.getMaxColCount();
		System.out.println("delim:" + delimiter);
		System.out.println("max:" + maxCol);
	}

	public static void test03() {
		String path = "C:/encode/ShiftJisCRLF.txt";
		File49ers insF49 = new kyPkg.uFile.File49ers(path);
		System.out.println("delimiter:" + insF49.getDelimiter());
		System.out.println("maxColumn:" + insF49.getMaxColCount());
	}

	public static void test00() {
		checkCRLF("C:/encode/ShiftJisCRLF.txt");
		checkCRLF("C:/encode/ShiftJisCR.txt");
		checkCRLF("C:/encode/ShiftJisLF.txt");
	}

	public static void testcheckBOM() {
		checkBOM("C:/encode/ShiftJisLF.txt");
		checkBOM("C:/encode/UTF8CR.txt");
		checkBOM("C:/encode/UTF8NCR.txt");
		checkBOM("C:/encode/UTF8CRLF.txt");
		checkBOM("C:/encode/UTF8NCRLF.txt");
		checkBOM("C:/encode/UTF8LF.txt");
		checkBOM("C:/encode/UTF8NLF.txt");
	}

	// BOM�Ŏn�܂��Ă��邩�ǂ������ׂ� EF(239) BB(187) BF(191)�@CS�ł̃o�C�g���}�[�NU+FEFF��UTF-8�ł̕\��
	public static boolean checkBOM(String path) {
		System.out.println("checkBOM path=>" + path);
		final int EF = 239;
		final int BB = 187;
		final int BF = 191;
		List<Integer> list = new ArrayList();
		try {
			FileInputStream in = new FileInputStream(path);
			int iVal;
			while (((iVal = in.read()) != -1) && list.size() < 3) {
				list.add(iVal);
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		if (list.get(0) == EF && list.get(1) == BB && list.get(2) == BF) {
			System.out.println("BOM exist");
			return true;
		} else {
			return false;
		}
	}

	// ���s�R�[�h�Ƃn�r�̊֌W
	// unix LF \n 0x0A 8�i�� 012 10�i��10
	// Mac CR \r 0x0D 8�i�� 015 10�i��13
	// Windows CR+LF \r\n
	// XXX ���ɉ��s�̔�������Ȃ��Ă����C�����[�_�����肵�Ă����悤���E�E�E�ł͉���mac�ŏ�肭�����Ȃ������̂��H�H
	public static void checkCRLF(String path) {
		System.out.println("path=>" + path);
		final int CR = 13;
		final int LF = 10;
		int xLF = -1;
		int xCR = -1;
		int xCRLF = -1;
		int pre = -1;
		try {
			FileInputStream in = new FileInputStream(path);
			int iVal;
			while ((iVal = in.read()) != -1) {
				switch (iVal) {
				case CR:// CR
					xCR++;
					break;
				case LF: // LF
					if (pre == CR) {
						xCR--;
						xCRLF++;
					} else {
						xLF++;
					}
					break;
				default:
					break;
				}
				// System.out.print( "=>"+iVal+"<=");
				// System.out.print(Integer.toHexString(iVal) + " ");
				pre = iVal;
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("xLF  :" + xLF);
		System.out.println("xCR  :" + xCR);
		System.out.println("xCRLF:" + xCRLF);
	}

	public static void main(String[] argv) {
		// test01();
		test00();
		// test03();
	}
	// XXX�@���s�������������肵����
	// XXX�@�����G���R�[�h���������肵����
	// XXX�@BOM������ꍇ���肷��H�H

	// http://code.google.com/p/juniversalchardet/

}
