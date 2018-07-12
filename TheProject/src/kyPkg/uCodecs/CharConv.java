package kyPkg.uCodecs;

import java.util.*;

import kyPkg.uFile.FileUtil;

import java.io.*;

public class CharConv {
	private static final String TEST_STR1 = "ރ��Ђ炪�ȃJ�^�J�i��������M��������������޲߳�$#123�����߶޷޸�<>*#$%abc:;\\ABC�������t�W�@���j�[�@E-�v���X�ӂ���H���@�U��";
	private static final String TEST_STR2 = "ޥ�����������޲߳�$#123�����߶޷޸�<>*#$%abc:;\\ABC�������t�W�@���j�[�@E-�v���X�ӂ���H���@�U��";

	private static final char SPACE = '�@';
	private static CharConv CnvIns;
	private HashMap n2w;
	private HashMap w2n;
	private HashMap ngf;
	private HashMap ngx;
	private HashMap k2n;
	private HashMap ckj;
	private HashMap opt;
	private static CharConv insCnv = null;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	private CharConv() {
	}

	// -------------------------------------------------------------------------
	// singleton
	// -------------------------------------------------------------------------
	public static CharConv getInstance() {
		if (CnvIns == null)
			CnvIns = new CharConv();
		return CnvIns;
	}

	// -------------------------------------------------------------------------
	// �Œ蒷�ɂ���(�o�C�g���ł͂Ȃ��̂Œ��ӁI)
	// pStr �Ώە�����
	// pLen ��]���钷��
	// pFiller �󔒕������w�肷��
	// -------------------------------------------------------------------------
	public static String cnvFixLength(int iVal, int pLen) {
		return cnvFixLength(String.valueOf(iVal), pLen, ' ');
	}

	public static String cnvFixLength(String pStr, int pLen) {
		return cnvFixLength(pStr, pLen, ' ');
	}

	public static String cnvFixLength(String pStr, int pLen, char pFiller) {
		StringBuffer wBuff = new StringBuffer(pLen);
		if (pStr.length() > pLen) {
			wBuff.append(pStr.substring(0, pLen));
		} else {
			wBuff.append(pStr);
			for (int i = pStr.length(); i < pLen; i++) {
				wBuff.append(pFiller);
			}
		}
		String wRtn = wBuff.toString();
		return wRtn;
	}

	// -----------------------------------------------------------------
	// �S�p������
	// -----------------------------------------------------------------
	public static String[] cnvWide(String[] array, int len) {
		if (insCnv == null)
			insCnv = CharConv.getInstance();
		for (int i = 0; i < array.length; i++) {
			array[i] = insCnv.cnvWideStr(array[i], len);
		}
		return array;
	}

	// CharConv.cnvWide(String str, int len)
	// public static String cnvWide(String str) {
	// return "";
	// }

	public static String cnvWide(String str, int len) {
		if (insCnv == null)
			insCnv = CharConv.getInstance();
		str = insCnv.cnvWideStr(str, len);
		return str;
	}

	// -------------------------------------------------------------------------
	// ���p���S�p
	// syllable�̖��
	// �����A�������ǂ����邩�E�E�E�� �� �J�J�� �K �ƂQ�i�K�ϊ�
	// �����i�K��U��_��o��4�s�j�A�������͖��Ή��E�E��̒������ς��̂Œ���
	// -------------------------------------------------------------------------
	public String cnvWide(String pStr, int pLen, char pFiller) {
		return cnvFixLength(cnvWide(pStr), pLen, pFiller);
	}

	private String cnvWideStr(String pStr, int pLen) {
		return cnvFixLength(cnvWide(pStr), pLen, SPACE);
	}

	// �@�S�p�������@�J�^�J�i�ϊ�����������ۂ̌꒷����������@���@�L�����N�^�z��g�p��
	public String cnvWide(String pStr) {
		if (pStr.equals(""))
			return "";
		String filler = "�@";
		if (n2w == null)
			incoreN2W();
		char[] cDes = new char[pStr.length()];
		int j = 0;
		char wCnv;
		for (int i = 0; i < pStr.length(); i++) {
			String wDic = (String) n2w.get(String.valueOf(pStr.charAt(i)));
			if (wDic != null) {
				wCnv = wDic.charAt(0);
			} else {
				wCnv = pStr.charAt(i);
			}
			char wChr = 0;
			if ((j > 0) && (cDes[j - 1] > 0)) {
				switch (wCnv) {
				case '�J':
					switch (cDes[j - 1]) {
					case '�J':
						wChr = '�K';
						break;
					case '�L':
						wChr = '�M';
						break;
					case '�N':
						wChr = '�O';
						break;
					case '�P':
						wChr = '�Q';
						break;
					case '�R':
						wChr = '�S';
						break;
					case '�T':
						wChr = '�U';
						break;
					case '�V':
						wChr = '�W';
						break;
					case '�X':
						wChr = '�Y';
						break;
					case '�Z':
						wChr = '�[';
						break;
					case '�\':
						wChr = '�]';
						break;
					case '�^':
						wChr = '�_';
						break;
					case '�`':
						wChr = '�W';
						break;
					case '�c':
						wChr = '�d';
						break;
					case '�e':
						wChr = '�f';
						break;
					case '�g':
						wChr = '�h';
						break;
					case '�n':
						wChr = '�o';
						break;
					case '�q':
						wChr = '�r';
						break;
					case '�t':
						wChr = '�u';
						break;
					case '�w':
						wChr = '�x';
						break;
					case '�z':
						wChr = '�{';
						break;
					case '�E':
						wChr = '��';
						break;
					}
					if (wChr > 0) {
						cDes[j - 1] = 0;
						wCnv = wChr;
					}
					break;
				case '�K':
					switch (cDes[j - 1]) {
					case '�n':
						wChr = '�p';
						break;
					case '�q':
						wChr = '�s';
						break;
					case '�t':
						wChr = '�v';
						break;
					case '�w':
						wChr = '�y';
						break;
					case '�z':
						wChr = '�|';
						break;
					}
					if (wChr > 0) {
						cDes[j - 1] = 0;
						wCnv = wChr;
					}
					break;
				default:
					break;
				}
			}
			cDes[j++] = wCnv;
		}
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < cDes.length; i++) {
			if (cDes[i] > 0)
				wBuff.append(cDes[i]);
		}
		// filler����
		for (int i = wBuff.length(); i < cDes.length; i++) {
			wBuff.append(filler);
		}
		return wBuff.toString();
	}

	public String cnvWide_classic(String pStr) {
		if (n2w == null)
			incoreN2W();
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) n2w.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			// System.out.println("in:"+pStr.charAt(i)+" => out:"+wCnv);
			wBuff.append(wCnv);
		}
		String wRtn = wBuff.toString();
		if (wRtn.indexOf('�J') >= 0) {
			wRtn = wRtn.replaceAll("�J�J", "�K");
			wRtn = wRtn.replaceAll("�L�J", "�M");
			wRtn = wRtn.replaceAll("�N�J", "�O");
			wRtn = wRtn.replaceAll("�P�J", "�Q");
			wRtn = wRtn.replaceAll("�R�J", "�S");
			wRtn = wRtn.replaceAll("�T�J", "�U");
			wRtn = wRtn.replaceAll("�V�J", "�W");
			wRtn = wRtn.replaceAll("�X�J", "�Y");
			wRtn = wRtn.replaceAll("�Z�J", "�[");
			wRtn = wRtn.replaceAll("�\�J", "�]");
			wRtn = wRtn.replaceAll("�^�J", "�_");
			wRtn = wRtn.replaceAll("�`�J", "�W");
			wRtn = wRtn.replaceAll("�c�J", "�d");
			wRtn = wRtn.replaceAll("�e�J", "�f");
			wRtn = wRtn.replaceAll("�g�J", "�h");
			wRtn = wRtn.replaceAll("�n�J", "�o");
			wRtn = wRtn.replaceAll("�q�J", "�r");
			wRtn = wRtn.replaceAll("�t�J", "�u");
			wRtn = wRtn.replaceAll("�w�J", "�x");
			wRtn = wRtn.replaceAll("�z�J", "�{");
			wRtn = wRtn.replaceAll("�E�J", "��");
		}
		if (wRtn.indexOf('�K') >= 0) {
			wRtn = wRtn.replaceAll("�n�K", "�p");
			wRtn = wRtn.replaceAll("�q�K", "�s");
			wRtn = wRtn.replaceAll("�t�K", "�v");
			wRtn = wRtn.replaceAll("�w�K", "�y");
			wRtn = wRtn.replaceAll("�z�K", "�|");
		}
		return wRtn;
	}

	// ------------------------------------------------------------------------------
	// �I�����ꂽ�l���Œ�̒����ŕԂ�(fixStr)
	// pStr ���͕�����
	// pLen �o�͒��i���o�C�g�����ł͂Ȃ��A�������j
	// �s�g�p��t
	// wAns = EnqChk.fixStr("��bcd",10);
	// System.out.println("��fixStr =>"+wAns);
	// ------------------------------------------------------------------------------
	public static String[] fixStrArray(String[] array, int len) {
		for (int i = 0; i < array.length; i++) {
			array[i] = fixStr(array[i].trim(), len);
		}
		return array;
	}

	public static String fixStr(String src, int len) {
		String ans = "";
		if (len > 0) {
			if (src == null)
				src = "";
			// DecimalFormat df = new DecimalFormat("00000");���g���悤�Ɍ�ŕύX������
			char[] cSrc = src.toCharArray(); // �L�����N�^�[�z��ɂ���
			char[] cDes = new char[len]; // �o�̓o�b�t�@
			for (int i = 0; i < cDes.length; i++)
				cDes[i] = ' ';
			for (int i = 0; (i < cDes.length && i < cSrc.length); i++) {
				cDes[i] = cSrc[i];
			}
			ans = new String(cDes);
		}
		return ans;
	}

	// ------------------------------------------------------------------------------
	// ��������E�񂹌Œ蒷����
	// ------------------------------------------------------------------------------
	public static String fixRight(String src, int len, char stuff) {
		String ans = "";
		if (len > 0) {
			char[] cSrc = src.toCharArray(); // �L�����N�^�[�z��ɂ���
			char[] cDes = new char[len]; // �o�̓o�b�t�@
			for (int i = 0; i < cDes.length; i++)
				cDes[i] = stuff; // ���ߕ���
			for (int i = cDes.length - 1, j = cSrc.length - 1; (i >= 0
					&& j >= 0); i--, j--) {
				cDes[i] = cSrc[j];
			}
			ans = new String(cDes);
		}
		return ans;
	}

	// ------------------------------------------------------------------------------
	// ����������񂹌Œ蒷����
	// ------------------------------------------------------------------------------
	public static String fixLeft(String src, int len, char stuff) {
		String ans = "";
		if (len > 0) {
			char[] cSrc = src.toCharArray(); // �L�����N�^�[�z��ɂ���
			char[] cDes = new char[len]; // �o�̓o�b�t�@
			for (int i = 0; i < cDes.length; i++)
				cDes[i] = stuff; // ���ߕ���
			for (int i = 0; (i < cDes.length && i < cSrc.length); i++) {
				cDes[i] = cSrc[i];
			}
			ans = new String(cDes);
		}
		return ans;
	}

	// -------------------------------------------------------------------------
	// �Œ蒷�S�p������Ԃ�
	// -------------------------------------------------------------------------
	public static String cnvFixWide(String str, int len) {
		CharConv charConv = CharConv.getInstance();
		str = fixStr(str, len);
		return charConv.cnvWide(str);
	}

	// -------------------------------------------------------------------------
	// �Œ蒷�S�pPlus(�z�X�g��������)
	// -------------------------------------------------------------------------
	public String cnvFixWide2(String cel, int len) {
		cel = fixStr(cel, len);
		cel = cnvWide(cel);
		return cnvCKJ(cel);// debug�@�z�X�g���������E�E�E�Ƃق�
	}

	// -------------------------------------------------------------------------
	// �Œ蒷���p
	// -------------------------------------------------------------------------
	public String cnvFixHalf(String cel, int len) {
		cel = cnvNarrow(cel);
		return fixStr(cel, len);
	}

	// -------------------------------------------------------------------------
	// �S�p�����p�ϊ�
	// pAlter �͊Y�����镶�������݂��Ȃ��ꍇ�̑�֕���
	// -------------------------------------------------------------------------
	public String cnvNarrow(String pStr) {
		return cnvNarrow(pStr, '_');
	}

	// �Ԉ���Ă���E�E�E�V���b�N�@char�͈̔͂Ŕ��肵���ق����������낤
	public String cnvNarrow(String pStr, char pAlter) {
		if (w2n == null)
			incoreW2N();
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			// String wStr = String.valueOf(pStr.substring(i,i+1));
			// String wStr = String.valueOf(pStr.charAt(i));
			char wChr = pStr.charAt(i);
			String wStr = String.valueOf(wChr);
			// if (wStr.matches("[A-Za-z0-9\\s]")){
			// �
			// �L��������Ă����̂Ŕ͈͔���ɕύX�����E�E�E�Ƃق�2009/10/21
			if ((wChr >= 32) && (wChr <= 126)) {
				wBuff.append(wStr);
			} else {
				String wCnv = (String) w2n.get(wStr);
				if (wCnv == null)
					wCnv = String.valueOf(pAlter);
				// System.out.println("in:"+pStr.charAt(i)+" => out:"+wCnv);
				wBuff.append(wCnv);
			}

		}
		String wRtn = wBuff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// �ŕ������m�[�}���C�Y for Host(kana => normal)
	// -------------------------------------------------------------------------
	public String cnvK2N(String pStr) {
		if (k2n == null)
			incoreK2N();
		StringBuffer buff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) k2n.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			buff.append(wCnv);
		}
		String wRtn = buff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// �������g�������p�Ƀm�[�}���C�Y for Host(���� => normal)
	// -------------------------------------------------------------------------
	public String cnvCKJ(String pStr) {
		if (pStr.equals(""))
			return "";
		if (ckj == null)
			incoreCKJ();
		StringBuffer buff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) ckj.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			buff.append(wCnv);
		}
		String wRtn = buff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// �t�@�C�����Ɏg���Ȃ�������ϊ�
	// -------------------------------------------------------------------------
	public String cnvNGFnm(String pStr) {
		if (ngf == null)
			incoreNGF();
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) ngf.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			wBuff.append(wCnv);
		}
		String wRtn = wBuff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// �G�N�Z���̃V�[�g���ȂǂŎg���Ȃ�������ϊ�
	// plus�R�P�����ȓ� �󔒂��ʖڂ炵��
	// -------------------------------------------------------------------------
	public String cnvNGExcel(String pStr) {
		if (ngx == null)
			incoreNGX();
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) ngx.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			wBuff.append(wCnv);
		}
		String wRtn = wBuff.toString();
		wRtn = wRtn.trim();
		if (wRtn.length() > 31)
			wRtn = wRtn.substring(0, 31);
		if (wRtn == "")
			wRtn = "NG";
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// �ϊ��}�b�v���t�@�C�������荞�ޏꍇ
	// �t�@�C���`���F��̕����{�^�u�{�ϊ����镶����
	// -------------------------------------------------------------------------
	public void incoreOPT(String pPath) {
		opt = CharConv.file2HashMap(pPath);
	}

	// -------------------------------------------------------------------------
	// �O���}�b�v���g�����ϊ�
	// wCnv.incoreOPT("Caesar"); // Caesar�Í��E�E���p�̂�
	// String Ans = wCnv.optConvert(wStr);
	// System.out.println("#Option     =>"+Ans );
	// -------------------------------------------------------------------------
	public String optConvert(String pStr) {
		StringBuffer wBuff = new StringBuffer(pStr.length());
		for (int i = 0; i < pStr.length(); i++) {
			String wStr = String.valueOf(pStr.charAt(i));
			String wCnv = (String) opt.get(wStr);
			if (wCnv == null)
				wCnv = wStr;
			wBuff.append(wCnv);
		}
		String wRtn = wBuff.toString();
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// �ϊ��}�b�v�h����������
	// -------------------------------------------------------------------------
	public void incoreN2W() {
		n2w = new HashMap(); // ���p���S�p
		// ---------------------------------------------------------------------
		n2w.put(" ", "�@");
		n2w.put("!", "�I");
		n2w.put("\"", "�h");
		n2w.put("#", "��");
		n2w.put("$", "��");
		n2w.put("%", "��");
		n2w.put("&", "��");
		n2w.put("\'", "�f");
		n2w.put("(", "�i");
		n2w.put(")", "�j");
		n2w.put("*", "��");
		n2w.put("+", "�{");
		n2w.put(",", "�C");
		// wN2W.put("-","�|");�@//���܂��ϊ��ł��Ȃ��悤���E�E�E2009/07/31�@16�i�\�L����ŏE���Ă�����
		n2w.put(".", "�D");
		n2w.put("/", "�^");
		n2w.put("0", "�O");
		n2w.put("1", "�P");
		n2w.put("2", "�Q");
		n2w.put("3", "�R");
		n2w.put("4", "�S");
		n2w.put("5", "�T");
		n2w.put("6", "�U");
		n2w.put("7", "�V");
		n2w.put("8", "�W");
		n2w.put("9", "�X");
		n2w.put(":", "�F");
		n2w.put(";", "�G");
		n2w.put("<", "��");
		n2w.put("=", "��");
		n2w.put(">", "��");
		n2w.put("?", "�H");
		n2w.put("@", "��");
		n2w.put("A", "�`");
		n2w.put("B", "�a");
		n2w.put("C", "�b");
		n2w.put("D", "�c");
		n2w.put("E", "�d");
		n2w.put("F", "�e");
		n2w.put("G", "�f");
		n2w.put("H", "�g");
		n2w.put("I", "�h");
		n2w.put("J", "�i");
		n2w.put("K", "�j");
		n2w.put("L", "�k");
		n2w.put("M", "�l");
		n2w.put("N", "�m");
		n2w.put("O", "�n");
		n2w.put("P", "�o");
		n2w.put("Q", "�p");
		n2w.put("R", "�q");
		n2w.put("S", "�r");
		n2w.put("T", "�s");
		n2w.put("U", "�t");
		n2w.put("V", "�u");
		n2w.put("W", "�v");
		n2w.put("X", "�w");
		n2w.put("Y", "�x");
		n2w.put("Z", "�y");
		n2w.put("[", "�m");
		n2w.put("\\", "��");
		n2w.put("]", "�n");
		n2w.put("^", "�O");
		n2w.put("_", "�Q");
		n2w.put("`", "�M");
		n2w.put("a", "��");
		n2w.put("b", "��");
		n2w.put("c", "��");
		n2w.put("d", "��");
		n2w.put("e", "��");
		n2w.put("f", "��");
		n2w.put("g", "��");
		n2w.put("h", "��");
		n2w.put("i", "��");
		n2w.put("j", "��");
		n2w.put("k", "��");
		n2w.put("l", "��");
		n2w.put("m", "��");
		n2w.put("n", "��");
		n2w.put("o", "��");
		n2w.put("p", "��");
		n2w.put("q", "��");
		n2w.put("r", "��");
		n2w.put("s", "��");
		n2w.put("t", "��");
		n2w.put("u", "��");
		n2w.put("v", "��");
		n2w.put("w", "��");
		n2w.put("x", "��");
		n2w.put("y", "��");
		n2w.put("z", "��");
		n2w.put("{", "�o");
		n2w.put("|", "�b");
		n2w.put("}", "�p");
		n2w.put("~", "�P");
		n2w.put("�", "�B");
		n2w.put("�", "�u");
		n2w.put("�", "�v");
		n2w.put("�", "�A");
		n2w.put("�", "�E");
		n2w.put("�", "��");
		n2w.put("�", "�@");
		n2w.put("�", "�B");
		n2w.put("�", "�D");
		n2w.put("�", "�F");
		n2w.put("�", "�H");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "�b");
		n2w.put("-", "�|");
		n2w.put("�", "�A");
		n2w.put("�", "�C");
		n2w.put("�", "�E");
		n2w.put("�", "�G");
		n2w.put("�", "�I");
		n2w.put("�", "�J");
		n2w.put("�", "�L");
		n2w.put("�", "�N");
		n2w.put("�", "�P");
		n2w.put("�", "�R");
		n2w.put("�", "�T");
		n2w.put("�", "�V");
		n2w.put("�", "�X");
		n2w.put("�", "�Z");
		n2w.put("�", "�\");
		n2w.put("�", "�^");
		n2w.put("�", "�`");
		n2w.put("�", "�c");
		n2w.put("�", "�e");
		n2w.put("�", "�g");
		n2w.put("�", "�i");
		n2w.put("�", "�j");
		n2w.put("�", "�k");
		n2w.put("�", "�l");
		n2w.put("�", "�m");
		n2w.put("�", "�n");
		n2w.put("�", "�q");
		n2w.put("�", "�t");
		n2w.put("�", "�w");
		n2w.put("�", "�z");
		n2w.put("�", "�}");
		n2w.put("�", "�~");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "��");
		n2w.put("�", "�J");
		n2w.put("�", "�K");
		n2w.put("�", "�K");
		n2w.put("-", "�]");
		n2w.put("-", "�[");//20150312
		n2w.put("�", "�[");

	}

	// ---------------------------------------------------------------------
	// �S�p�����p
	// ---------------------------------------------------------------------
	public void incoreW2N() {
		w2n = new HashMap();
		// .
		w2n.put("�", "�"); // 2010/08/18 �ǉ��E�E�E���[��
		w2n.put("�@", " ");
		w2n.put("�I", "!");
		w2n.put("�h", "\"");
		w2n.put("��", "#");
		w2n.put("��", "$");
		w2n.put("��", "%");
		w2n.put("��", "&");
		w2n.put("�f", "\'");
		w2n.put("�i", "(");
		w2n.put("�j", ")");
		w2n.put("��", "*");
		w2n.put("�{", "+");
		w2n.put("�C", ",");
		w2n.put("�[", "-");
		w2n.put("�D", ".");
		w2n.put("�^", "/");
		w2n.put("�O", "0");
		w2n.put("�P", "1");
		w2n.put("�Q", "2");
		w2n.put("�R", "3");
		w2n.put("�S", "4");
		w2n.put("�T", "5");
		w2n.put("�U", "6");
		w2n.put("�V", "7");
		w2n.put("�W", "8");
		w2n.put("�X", "9");
		w2n.put("�F", ":");
		w2n.put("�G", ";");
		w2n.put("��", "<");
		w2n.put("��", "=");
		w2n.put("��", ">");
		w2n.put("�H", "?");
		w2n.put("��", "@");
		w2n.put("�`", "A");
		w2n.put("�a", "B");
		w2n.put("�b", "C");
		w2n.put("�c", "D");
		w2n.put("�d", "E");
		w2n.put("�e", "F");
		w2n.put("�f", "G");
		w2n.put("�g", "H");
		w2n.put("�h", "I");
		w2n.put("�i", "J");
		w2n.put("�j", "K");
		w2n.put("�k", "L");
		w2n.put("�l", "M");
		w2n.put("�m", "N");
		w2n.put("�n", "O");
		w2n.put("�o", "P");
		w2n.put("�p", "Q");
		w2n.put("�q", "R");
		w2n.put("�r", "S");
		w2n.put("�s", "T");
		w2n.put("�t", "U");
		w2n.put("�u", "V");
		w2n.put("�v", "W");
		w2n.put("�w", "X");
		w2n.put("�x", "Y");
		w2n.put("�y", "Z");
		w2n.put("�m", "[");
		w2n.put("��", "\\");
		w2n.put("�n", "]");
		w2n.put("�O", "^");
		w2n.put("�Q", "_");
		w2n.put("�M", "`");
		w2n.put("��", "a");
		w2n.put("��", "b");
		w2n.put("��", "c");
		w2n.put("��", "d");
		w2n.put("��", "e");
		w2n.put("��", "f");
		w2n.put("��", "g");
		w2n.put("��", "h");
		w2n.put("��", "i");
		w2n.put("��", "j");
		w2n.put("��", "k");
		w2n.put("��", "l");
		w2n.put("��", "m");
		w2n.put("��", "n");
		w2n.put("��", "o");
		w2n.put("��", "p");
		w2n.put("��", "q");
		w2n.put("��", "r");
		w2n.put("��", "s");
		w2n.put("��", "t");
		w2n.put("��", "u");
		w2n.put("��", "v");
		w2n.put("��", "w");
		w2n.put("��", "x");
		w2n.put("��", "y");
		w2n.put("��", "z");
		w2n.put("�o", "{");
		w2n.put("�b", "|");
		w2n.put("�p", "}");
		w2n.put("�P", "~");
		w2n.put("�B", "�");
		w2n.put("�u", "�");
		w2n.put("�v", "�");
		w2n.put("�A", "�");
		w2n.put("�E", "�");
		w2n.put("��", "�");
		w2n.put("�@", "�");
		w2n.put("�B", "�");
		w2n.put("�D", "�");
		w2n.put("�F", "�");
		w2n.put("�H", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("�b", "�");
		w2n.put("�|", "-");
		w2n.put("�A", "�");
		w2n.put("�C", "�");
		w2n.put("�E", "�");
		w2n.put("�G", "�");
		w2n.put("�I", "�");
		w2n.put("�J", "�");
		w2n.put("�L", "�");
		w2n.put("�N", "�");
		w2n.put("�P", "�");
		w2n.put("�R", "�");
		w2n.put("�T", "�");
		w2n.put("�V", "�");
		w2n.put("�X", "�");
		w2n.put("�Z", "�");
		w2n.put("�\", "�");
		w2n.put("�^", "�");
		w2n.put("�`", "�");
		w2n.put("�c", "�");
		w2n.put("�e", "�");
		w2n.put("�g", "�");
		w2n.put("�i", "�");
		w2n.put("�j", "�");
		w2n.put("�k", "�");
		w2n.put("�l", "�");
		w2n.put("�m", "�");
		w2n.put("�n", "�");
		w2n.put("�q", "�");
		w2n.put("�t", "�");
		w2n.put("�w", "�");
		w2n.put("�z", "�");
		w2n.put("�}", "�");
		w2n.put("�~", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("�]", "-");
		w2n.put("�J", "�");
		w2n.put("�K", "�");
		// �Qsyllable�͂Q�����ɕϊ������E�E�E
		w2n.put("��", "��");
		w2n.put("�K", "��");
		w2n.put("�M", "��");
		w2n.put("�O", "��");
		w2n.put("�Q", "��");
		w2n.put("�S", "��");
		w2n.put("�U", "��");
		w2n.put("�W", "��");
		w2n.put("�Y", "��");
		w2n.put("�[", "��");
		w2n.put("�]", "��");
		w2n.put("�_", "��");
		w2n.put("�W", "��");
		w2n.put("�d", "��");
		w2n.put("�f", "��");
		w2n.put("�h", "��");
		w2n.put("�o", "��");
		w2n.put("�r", "��");
		w2n.put("�u", "��");
		w2n.put("�x", "��");
		w2n.put("�{", "��");
		w2n.put("�p", "��");
		w2n.put("�s", "��");
		w2n.put("�v", "��");
		w2n.put("�y", "��");
		w2n.put("�|", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "��");
		// ---------------------------------------------------------------------
		// TODO �b��I�ɂ�����������ŏC�����Ă����I�I�ݶ����ł̃����W�͂ǂ��Ȃ��Ă���́H
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("-", "-");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		w2n.put("�", "�");
		// ---------------------------------------------------------------------
		// ��������I�}�P
		w2n.put("��", "1");
		w2n.put("��", "2");
		w2n.put("�O", "3");
		w2n.put("�l", "4");
		w2n.put("��", "5");
		w2n.put("�Z", "6");
		w2n.put("��", "7");
		w2n.put("��", "8");
		w2n.put("��", "9");
		w2n.put("�\", "0");
		w2n.put("��", "�");
		w2n.put("��", "��");
		w2n.put("��", "��");
		w2n.put("��", "�");
		w2n.put("��", "ϲ");
		w2n.put("��", "*");
		w2n.put("��", "^");
		w2n.put("��", "#");
		w2n.put("�w", "{");
		w2n.put("�x", "}");
		w2n.put("�y", "[");
		w2n.put("�z", "]");
		w2n.put("�s", "<");
		w2n.put("�t", ">");
		// �� �����܂ŃI�}�P
		// �X���@2010-07-27�ǉ�
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
		w2n.put("��", "�");
	}

	// ---------------------------------------------------------------------
	// �t�@�C�����֑�����(���܂̂Ƃ���Windows)
	// ����͂n�r�ɂ���Ĉ���Ă���
	// ���ƂŌ������A�����d���������ƃI���i�W���H�I
	// ---------------------------------------------------------------------
	public void incoreNGF() {
		ngf = new HashMap(); // �t�@�C�����Ɏg���Ȃ�����
		ngf.put(" ", "");
		ngf.put("\\", "_");
		ngf.put("/", "_");
		ngf.put(":", "_");
		ngf.put("*", "_");
		ngf.put("?", "_");
		ngf.put("\"", "_");
		ngf.put("<", "_");
		ngf.put(">", "_");
		ngf.put("|", "_");
	}

	// ---------------------------------------------------------------------
	// Excel�֑�����
	// ---------------------------------------------------------------------
	public void incoreNGX() {
		ngx = new HashMap(); // �G�N�Z���V�[�g���Ɏg���Ȃ������Ȃ�
		ngx.put("\\", "_");
		ngx.put("/", "_");
		ngx.put("*", "_");
		ngx.put("[", "_");
		ngx.put("]", "_");
	}

	// ---------------------------------------------------------------------
	// ���p�J�^�J�inormalize(jicfs��Ҳ�����Host�ɑ����)2010/08/19
	// ---------------------------------------------------------------------
	public void incoreK2N() {
		k2n = new HashMap();
		k2n.put("!", " ");
		k2n.put("�", "�");
		k2n.put("�", "-");
		k2n.put("�", "�");
		k2n.put("�", "�");
		k2n.put("�", "�");
		k2n.put("�", "�");
		k2n.put("�", "�");
		k2n.put("�", "�");
		k2n.put("�", "�");
		k2n.put("�", "�");
		k2n.put("�", "�");
		k2n.put("~", "_");
	}

	// ---------------------------------------------------------------------
	// �����R�[�h��ϊ��@HOST�@2010/08/19 ��
	// ---------------------------------------------------------------------
	public void incoreCKJ() {
		ckj = new HashMap();
		// �i�ϊ����A�ϊ���j
		//	ckj.put("�|", "�[");//20161102=>20170628

		ckj.put("�M", "��");
		ckj.put("��", "��");
		ckj.put("�n", "�n");
		ckj.put("��", "��");
		ckj.put("��", "��");
		ckj.put("�g", "�g");
		ckj.put("�x", "��");

		ckj.put("�|", "��");
		ckj.put("��", "�|");

		// �N���X�R���o�[�g����Ă���H�H�悤��
		ckj.put("��", "��");
		ckj.put("��", "��");

		ckj.put("�y", "�a");
		ckj.put("�a", "�y");

		ckj.put("�z", "��");
		ckj.put("��", "�z");

		ckj.put("��", "�U");
		ckj.put("�U", "��");

		ckj.put("�v", "��");
		ckj.put("��", "�v");

		ckj.put("��", "��");
		ckj.put("��", "��");

	}

	// -------------------------------------------------------------------------
	// file2HashMap �t�@�C�����n�b�V���e�[�u���ɓǂݍ���
	// -------------------------------------------------------------------------
	// file2HashMap wHtbl = FileUtil.file2HashMap("hosts","\t");
	// System.out.println("size:"+wHtbl.size());
	// System.out.println("---------------------------------------------------");
	// for (Enumeration enum = wHtbl.elements(); enum.hasMoreElements() ;) {
	// System.out.println("enum:"+enum.nextElement());
	// }
	// -------------------------------------------------------------------------
	public static HashMap file2HashMap(String path) {
		String pDlm = "\t";
		// System.out.println("��FileUtil.file2HashMap:"+pPath);
		File fl = new File(path);
		if (fl.exists() == false)
			return null;
		HashMap wHtbl = new HashMap();
		String wRec;
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] wArray = wRec.split(pDlm);
					if (wArray.length > 1) {
						wHtbl.put(wArray[0], wArray[1]);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return wHtbl;
	}

	// -------------------------------------------------------------------------
	// filterT �t�B���^�[�v���O���� �������p�����[�^���H �g�p��ƁE�E
	// �� boolean swt = filterT(wPath_I);
	// -------------------------------------------------------------------------
	public static boolean filterT(String path) {
		String wLs = System.getProperty("line.separator");
		String wDlm = ",";
		long l = System.currentTimeMillis();
		String sCel0 = "";
		String sCel1 = "";
		String sCel2 = "";
		String sCel3 = "";
		File file = new File(path);
		if (!file.exists())
			return false;

		CharConv wCnv = CharConv.getInstance();
		// CharConv wCnv = new CharConv();

		String wRec = "";
		int wSize = 1024;
		StringBuffer sBuf = new StringBuffer(wSize);
		try {
			FileWriter pFr = new FileWriter("catbrd.CSV");
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((wRec = br.readLine()) != null) {
				String[] wCel = wRec.split("\t");
				// for(int i = 0;i< wCel.length ; i++){
				// System.out.println(""+i+">"+wCel[i]);
				// }
				if (wCel.length >= 2) {
					if (!wCel[0].equals("")) {
						sCel0 = wCel[0].trim();
						if (!wCel[1].equals("")) {
							sCel1 = wCel[1].trim();
							sCel1 = wCnv.cnvWide(sCel1, 25, '�@');
						}
					}
				}

				if (wCel.length > 3) {

					if (!wCel[2].equals(""))
						sCel2 = wCel[2].trim();
					if (!wCel[3].equals("")) {
						sCel3 = wCel[3].trim();
						sCel3 = wCnv.cnvWide(sCel3, 25, '�@');
					}

					sBuf.delete(0, sBuf.length());
					sBuf.append(sCel0);
					sBuf.append(wDlm);
					sBuf.append(sCel1);
					sBuf.append(wDlm);
					sBuf.append(sCel2);
					sBuf.append(wDlm);
					sBuf.append(sCel3);
					sBuf.append(wLs);

					// System.out.println("r>"+sBuf.toString());
					pFr.write(sBuf.toString());
				}

			}
			br.close();
			pFr.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		l -= System.currentTimeMillis();
		System.out.println("Elapse:" + (-l));
		return true;
	}

	// -------------------------------------------------------------------------
	// for test
	// -------------------------------------------------------------------------
	public static void test02() {
		CharConv.filterT("Fujii.txt");
	}

	public static void test01() {

		// CharConv conv = new CharConv();
		kyPkg.uCodecs.CharConv conv = kyPkg.uCodecs.CharConv.getInstance();

		String wWide = conv.cnvWide(TEST_STR1);
		String wWide2 = conv.cnvWide_classic(TEST_STR1);
		String wFix = CharConv.cnvFixLength(TEST_STR1, 50, '��');
		String wNarrow = conv.cnvNarrow(wWide, '?');
		String wNgf = conv.cnvNGFnm(wNarrow);
		String wNgx = conv.cnvNGExcel(wNarrow);
		String wk2n = conv.cnvK2N(TEST_STR1);
		String wckj = conv.cnvCKJ(TEST_STR1);// �z�X�g�p�����ɃR���o�[�g���郍�W�b�N

		System.out.println("#Original   =>" + TEST_STR1);
		System.out.println("#Wide2      =>" + wWide2);
		System.out.println("#Wide       =>" + wWide);
		System.out.println("#Narrow     =>" + wNarrow);
		System.out.println("#Fix        =>" + wFix);
		System.out.println("#cnvNGFnm   =>" + wNgf);
		System.out.println("#cnvNGExcel =>" + wNgx);
		System.out.println("#��forHost =>" + wk2n);
		System.out.println("#����forHost =>" + wckj);
		conv = null;
	}

	//�Œ蒷�S�p������ɕϊ�����
	public static void testcnvWide20150309() {
		String wWide = CharConv.cnvWide(TEST_STR2, 50);
		System.out.println("#Original   =>" + TEST_STR2);
		System.out.println("#Wide       =>" + wWide);
	}

	public static void main(String[] argv) {
		// test01();
		testcnvWide20150309();
	}

}
