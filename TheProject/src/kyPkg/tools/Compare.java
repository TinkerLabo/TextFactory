package kyPkg.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import globals.ResControl;
import kyPkg.uFile.FileUtil;

public class Compare {

	/**
	 * *************************************************************************
	 * **
	 */
	// < Encode >--------------------------------------------------------------
	// ������ ����
	// ISO2022JP JIS X 0201�AISO 2022 �`���� 0208�A���{��
	// SJIS Shift-JIS�A���{��
	// JISAutoDetect Shift-JIS�AEUC-JP�AISO 2022 JP �̌��o����ѕϊ�
	// ASCII American Standard Code for Information Interchange
	// MS932 Windows ���{��
	// UTF8 08 �r�b�g Unicode Transformation Format
	// UTF-16 16 �r�b�g Unicode Transformation Format�A
	// �K�{�̏����o�C�g���}�[�N�ɂ���Ďw�肳�ꂽ�o�C�g��
	// UnicodeLittle 16 �r�b�g Unicode Transformation Format
	// ���g���G���f�B�A���o�C�g���A�o�C�g���}�[�N�t��
	// UnicodeLittleUnmarked 16 �r�b�g Unicode Transformation Format
	// ���g���G���f�B�A���o�C�g��
	// UnicodeBig 16 �r�b�g Unicode Transformation Format
	// �r�b�O�G���f�B�A���o�C�g���A�o�C�g���}�[�N�t��
	// UnicodeBigUnmarked 16 �r�b�g Unicode Transformation Format
	// �r�b�O�G���f�B�A���o�C�g��
	// Cp1252 Windows ���e������-1
	// ISO8859_1 ISO 8859-1�A���e���A���t�@�x�b�g No. 1
	// ��intel�n�̓��g���G���f�B�A��
	// ------------------------------------------------------------------------
	/***************************************************************************
	 * �t�@�C�����r����<br>
	 * 
	 * @param sBuf
	 *            ��r���ʂ�\������JTextArea
	 * @param pFile1
	 *            �ǂݍ��ރt�@�C���̃p�X
	 * @param pFile2
	 *            �ǂݍ��ރt�@�C���̃p�X
	 **************************************************************************/
	private List<String> duffList;

	public List<String> getDuffList() {
		return duffList;
	}

	private String status = "";

	private String path1;

	private String path2;

	private int limit = Integer.MAX_VALUE;
	private boolean trim;

	private long lineCount;// ��r�����s��

	private int diffCount;// ���������

	private String name1;

	private String name2;

	// Stat�Ƀt�@�C�������ڂ������Ȃ����̂Œǉ�����
	private String about() {
		String about = name1 + "�@�Ɓ@" + name2 + "�@�ɂ���,�@";
		if (name1.equals(name2)) {
			about = name1 + "�@�ɂ���,�@";
		}
		return about;
	}

	// -------------------------------------------------------------------------
	// �A���}�b�`����
	// -------------------------------------------------------------------------
	public int getDiffCount() {
		return diffCount;
	}

	// -------------------------------------------------------------------------
	// �}�b�`����
	// -------------------------------------------------------------------------
	public long getMatchCount() {
		return lineCount;
	}

	// -------------------------------------------------------------------------
	// ��r��������
	// -------------------------------------------------------------------------
	public String getStatus() {
		return about() + status;
	}

	// -------------------------------------------------------------------------
	// ��r�������ʂ���т��̓��e
	// -------------------------------------------------------------------------
	public String getResultAsString() {
		return kyPkg.util.Joint.join(duffList, "\n");
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Compare(String path1, String path2) {
		this.path1 = path1;
		this.path2 = path2;
		name1 = FileUtil.getName(path1);
		name2 = FileUtil.getName(path2);
	}

	public Compare(String path1, String path2, int limit, boolean trim) {
		this(path1, path2);
		this.limit = limit;
		this.trim = trim;
	}

	// -------------------------------------------------------------------------
	// ��r�����Ă��̌��ʂ�Ԃ�
	// -------------------------------------------------------------------------
	public String compareAndGetStatRez() {
		compare();
		if (duffList.size() == 0) {
			return getStatus();
		} else {
			return getStatus() + "\n" + getResultAsString();
		}
	}

	public String compareAndGetStat() {
		compare();
		return getStatus();
	}

	// -------------------------------------------------------------------------
	// 0:�A���}�b�`���O���A����ł͂Ȃ�(�܂�A���ׂĈ�v����)
	// -1:path1�t�@�C�������݂��Ȃ�����
	// -2:path2�t�@�C�������݂��Ȃ�����
	// -3:�ǂ��炩�̃��R�[�h����
	// 1�ȏ�E�E�A���}�b�`�i����Ă��錏���j
	// -------------------------------------------------------------------------
	public int compare() {
		this.lineCount = 0;
		this.diffCount = 0;
		this.status = "";
		// this.result = "";
		if (FileUtil.iFileChk(path1) == null) {
			status = "�t�@�C��1�����݂��܂���i�����𒆒f���܂����j->" + path1;
			this.diffCount = -1;
			return this.diffCount;
		}
		if (FileUtil.iFileChk(path2) == null) {
			status = "�t�@�C��2�����݂��܂���i�����𒆒f���܂����j->" + path2;
			this.diffCount = -2;
			return this.diffCount;
		}
		// StringBuffer sBuf = new StringBuffer(2048);
		duffList = new ArrayList();
		File file1 = new File(path1);
		File file2 = new File(path2);
		try {
			String pRec1;
			String pRec2;
			String wRec1;
			String wRec2;
			BufferedReader br1 = FileUtil.getBufferedReader(path1);
			BufferedReader br2 = FileUtil.getBufferedReader(path2);
//			BufferedReader br1 = new BufferedReader(new FileReader(file1));
//			BufferedReader br2 = new BufferedReader(new FileReader(file2));
			// -----------------------------------------------------------------
			while ((br1.ready() == true) && (br2.ready() == true)
					&& (diffCount < limit)) {
				lineCount++;
				pRec1 = br1.readLine();
				pRec2 = br2.readLine();
				wRec1 = new String(pRec1.getBytes("SJIS"), "SJIS");
				wRec2 = new String(pRec2.getBytes("SJIS"), "SJIS");
				if (trim) {
					wRec1 = wRec1.trim();
					wRec2 = wRec2.trim();
				}
				if (wRec1.equals(wRec2) == false) {
					diffCount++;
					int pos = kyPkg.tools.Compare.diffPos(wRec1, wRec2) + 1;
					if (pos < wRec1.length()) {
						duffList.add("Line:" + lineCount + " Col:" + pos + " <"
								+ wRec1.substring(pos - 1, pos) + ">");
					} else {
						duffList.add("Line:" + lineCount + " Col:" + pos + "");
					}
					duffList.add("Rec1:" + wRec1);
					duffList.add("Rec2:" + wRec2);
				}
			}
			br1.close();
			br2.close();
			// -----------------------------------------------------------------
			// �����ׂă}�b�`�����ꍇ�ǂ�����H�H�H�H
			// -----------------------------------------------------------------
			if (lineCount == 0) {
				this.diffCount = -3;
				status = "�ǂ��炩�̃��R�[�h����Ȃ̂Ŕ�r���s���܂���ł����B";
			} else {
				if (diffCount == 0) {
					status = lineCount + "���R�[�h�̔�r���s���A�S���R�[�h����v�������܂����B";
				} else {
					if (diffCount >= limit) {
						status = "�s��v�̏��" + limit
								+ "���ɒB�����̂Ŕ�r�����𒆒f���܂����i��v���܂���ł����j";
					} else {
						status = lineCount + "���R�[�h�̔�r���s��" + diffCount
								+ "���A��v���܂���ł���";
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return this.diffCount;
	}

	// -------------------------------------------------------------------------
	// ��������r���ĈقȂ镶�����ŏ��ɏo�������ʒu(�[������n�܂�)��Ԃ�
	// -------------------------------------------------------------------------
	public static int diffPos(String str1, String str2) {
		char[] array1 = str1.toCharArray();
		char[] array2 = str2.toCharArray();
		int len = 0;
		if (array1.length > array2.length) {
			len = array2.length;
		} else {
			len = array1.length;
		}
		for (int i = 0; i < len; i++) {
			// System.out.println("array1[i]:"+array1[i]);
			// System.out.println("array2[i]:"+array2[i]);
			if (array1[i] != array2[i])
				return i;
		}
		if (array1.length != array2.length) {
			return len;
		}
		return -1;
	}

	public static void main(String[] argv) {
		test2();
	}

	public static void test2() {
		System.out.println("test0:" + kyPkg.tools.Compare.diffPos("a", "a"));
		System.out.println(
				"test1:" + kyPkg.tools.Compare.diffPos("test", "test"));
		System.out.println("test2:" + kyPkg.tools.Compare.diffPos("a", "b"));
		System.out.println("test3:" + kyPkg.tools.Compare.diffPos("", "b"));
		System.out.println(
				"test4:" + kyPkg.tools.Compare.diffPos("testA", "testB"));
	}

	public static void test() {
		String path1 = ResControl.D_DAT + "NQFACE1.DAT";
		String path2 = ResControl.D_DAT + "NQFACE2.DAT";
		String result = new kyPkg.tools.Compare(path2, path1)
				.compareAndGetStatRez();
		System.out.println(result);
	}

}
