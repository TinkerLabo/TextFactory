package kyPkg.tools;

import java.io.FileInputStream;
import java.io.IOException;

import kyPkg.uFile.FileUtil;

public class HexCompare {
	private String result = "";
	private String status = "";
	private String path1;
	private String path2;
	private int limit = Integer.MAX_VALUE;
	private long matchCount;

	public long getMatchCount() {
		return matchCount;
	}

	private int diffCount;

	public HexCompare(String path1, String path2) {
		this.path1 = path1;
		this.path2 = path2;
	}

	public HexCompare(String path1, String path2, int limit) {
		this(path1, path2);
		this.limit = limit;
	}

	// kyPkg.tools.HexCompare.hexCompare(path1,path2)
	/***************************************************************************
	 * �t�@�C�����r�����̈Ⴂ���w�L�T�_���v�ŕԂ� yuasa <br>
	 * 
	 * @param path1
	 *            ��r����t�@�C�����̈�
	 * @param path2
	 *            ��r����t�@�C�����̓�
	 * @return ��r���� <br>
	 *         �s�g�p��t<br>
	 **************************************************************************/

	/***************************************************************************
	 * HexCompare�̃T�u���[�`��
	 * 
	 * @param buff_L
	 *            ��
	 * @param n
	 *            ��
	 * @return ��r����
	 **************************************************************************/
	public static String buffDump(byte[] buff_L, int n) {
		StringBuffer sBuf1 = new StringBuffer(""); // �����C���[�W�\����
		StringBuffer sBuf2 = new StringBuffer(""); // 16�i�\����
		StringBuffer sBuf3 = new StringBuffer("");
		sBuf1.delete(0, sBuf1.length());
		sBuf2.delete(0, sBuf2.length());
		for (int i = 0; i < n; i++) {
			// ----------------------------------
			char hex1, hex2;
			hex1 = (char) (buff_L[i] & 0xF0);
			hex1 >>= 4;
			hex2 = (char) (buff_L[i] & 0x0F);
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
			sBuf2.append(" " + hex1 + hex2);
			if (buff_L[i] < 0x20 || 0x7e < buff_L[i]) {
				sBuf1.append("."); // �\���\�����ȊO
			} else {
				sBuf1.append((char) (buff_L[i]));
			}
		}
		for (int i = 0; i < (16 - n); i++) {
			sBuf2.append("   "); // �w�L�T�\������16�����ɖ����Ȃ��ꍇ�̏���
		}
		sBuf3.append(sBuf2.toString());
		sBuf3.append(" |");
		sBuf3.append(sBuf1.toString());
		return sBuf3.toString();
	} // End of buffDump

	public String compareAndGetStat() {
		compare();
		return status;
	}

	public int compare() {
		this.matchCount = 0;
		this.diffCount = 0;
		this.status = "";
		this.status = "";
		double lcnt = 0;
		int diffCount = 0;
		boolean EOF = true;
		StringBuffer sBuf3 = new StringBuffer("");
		byte[] buff_L = new byte[1024];
		byte[] buff_R = new byte[1024];
		FileInputStream stream1 = null;
		FileInputStream stream2 = null;
		if (FileUtil.iFileChk(path1) == null) {
			status = "�t�@�C���P�����݂��܂���i�����𒆒f���܂����j->" + path1;
			this.diffCount = -1;
			return this.diffCount;
		}
		if (FileUtil.iFileChk(path2) == null) {
			status = "�t�@�C���Q�����݂��܂���i�����𒆒f���܂����j->" + path2;
			this.diffCount = -2;
			return this.diffCount;
		}
		// --------------------------------------------------
		try {
			String rec1 = "";
			String rec2 = "";
			stream1 = new FileInputStream(path1);
			stream2 = new FileInputStream(path2);
			while (EOF && (diffCount < limit)) {
				lcnt++;
				int n1 = stream1.read(buff_L, 0, 16);
				if (n1 > 0) {
					rec1 = buffDump(buff_L, n1);
				} else {
					EOF = false;
				}
				int n2 = stream2.read(buff_R, 0, 16);
				if (n2 > 0) {
					rec2 = buffDump(buff_R, n2);
				} else {
					EOF = false;
				}
				if (!rec1.equals(rec2)) {
					diffCount++;
					sBuf3.append("on Line:" + lcnt + "\n" + rec1 + "\n" + rec2
							+ "\n");
				} else {
					matchCount++;
				}
			}
			stream1.close();
			stream2.close();
			if (diffCount == 0) {
				status = ((int) lcnt) + "�o�C�g�̔�r���s���S���R�[�h����v�������܂����B";
			} else {
				status = "��v���܂���ł���";
			}
			// System.out.println(status);

		} catch (IOException e) {
			e.printStackTrace();
			EOF = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		result = sBuf3.toString();
		status = result + "\n" + status;
		return this.diffCount;
	} // end of HexCompare
}
