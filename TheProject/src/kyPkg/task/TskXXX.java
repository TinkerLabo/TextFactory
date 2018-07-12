package kyPkg.task;

import kyPkg.uFile.Digger;
import kyPkg.uFile.FileUtil;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*; // ���I�v�V�����_�C�A���O���g�p�����̂ŁE�E
import java.util.regex.*;

//-----------------------------------------------------------------------------
// �t�@�C���ҏW���`                                        K.Yuasa 2006.2.13            update 2007.5.16
//-----------------------------------------------------------------------------
public class TskXXX extends Abs_ProgressTask {
	private static final String LF = System.getProperty("line.separator");
	private static final String FS = System.getProperty("file.separator");
	private String[] fileArray;
	private String gParent;
	private String gBef[];
	private String gAft[];
	private String gRgx;
	private ArrayList gArray; // �����t�@�C���ꗗ

	// -------------------------------------------------------------------------
	public TskXXX(String[] args) {
		this(args[0], new String[] { args[1] }, null, args[2], false);
	}

	public TskXXX(String pPath, String pBef[], String pAft[], String pRgx,
			boolean pFlg) {
		super();
		gBef = pBef;
		gAft = pAft;
		gRgx = pRgx; // ex. ".*\\.sql"
		// ---------------------------------------------------------------------
		// �w�肵���f�B���N�g���ȉ��̃t�@�C�����X�g����������T�u���[�`��
		// ---------------------------------------------------------------------
		Digger digger = new Digger(pPath, gRgx, pFlg);
		digger.search();
		fileArray = digger.getFileArray();

		gArray = new ArrayList(fileArray.length); // �\�ߓK���ȑ傫�߂ɍ쐬
		gParent = FileUtil.getParent(new File(pPath).getAbsolutePath());
		gParent = gParent + FS + "conv";
	}

	// ------------------------------------------------------------------------
	// �O������R�[�������g���K�[
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TskXXX", 2048);
		if (isStarted()) {
			final SwingWrk worker = new SwingWrk() {
				@Override
				public Object construct() {
					return new ActualTask(); // ���ۂ̏���
				}
			};
			worker.start();
		}
		super.stop();// ����I��

	};

	// -------------------------------------------------------------------------
	class ActualTask {
		ActualTask() {
			boolean wDirty;
			System.out.println("## ActualTask ## on TskXXX");
			gArray.clear();
			// -----------------------------------------------------------------
			for (int i = 0; i < fileArray.length; i++) {
				// -------------------------------------------------------------
				// �v���O���X�o�[��������ׂ̎��ԉ҂� �E�E�E
				// -------------------------------------------------------------
				try {
					Thread.sleep(1); // sleep for a second
				} catch (InterruptedException e) {
					System.out.println("ActualTask interrupted");
				}
				String wPath_I = (String) fileArray[i];
				setCurrent(i); // make some progress
				setMessage(" (" + getCurrent() + "/" + getTotalLen() + ")"
						+ wPath_I);
				// -------------------------------------------------------------
				// ������ �@
				// -------------------------------------------------------------
				wDirty = filterT(wPath_I, gBef, gAft); // �y�����z
				if (wDirty) {
					gArray.add(wPath_I);
				}
				// �� �����Ώۃt�@�C����Arraylist�Ɋi�[���Ă����ق����ǂ��Ǝv���i�����ɃJ�E���g������j
				// �� �������I��������_�Ō��ʂƂ��Ē񎦂���
				// �� �ϊ����Ȃ��Ō�����������ꍇ���l����
			}
			stop();
			setCurrent(getTotalLen());
			String wMsg = gArray.size() + "���������܂���";
			JOptionPane.showMessageDialog((Component) null, wMsg, "Message...",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	/**
	 * filterT �t�B���^�[�v���O���� �w�肳�ꂽ�t�@�C����ǂݍ���ŁA���C�^�[�֏����o��
	 * 
	 * @param path
	 *            ���̓p�X
	 * @param pBef
	 *            �ϊ��O�A������
	 * @param pAft
	 *            �ϊ���A������
	 * @return �ϊ������ꍇ��true ���\�߃p�^�[�����R���p�C�����āA�z�񉻂��Ă����΁E�E�p�t�H�[�}���X��������I
	 * 
	 */
	private static boolean filterT(String path, String[] pBef, String[] pAft) {
		StringBuffer buff = new StringBuffer(256);
		boolean wDirty = false;
		String rec = "";
		String preRec = "";
		String gBef[] = pBef;
		String gAft[] = pAft;
		File file = new File(path);
		if (file.isFile()) {
			try {
				String oPath = path + ".tmp"; // �������O�̃t�@�C�������ɑ��݂��Ă�����ǂ��Ȃ�H
				FileWriter fw1 = new FileWriter(oPath);
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((rec = br.readLine()) != null) {
					preRec = rec;
					buff.delete(0, buff.length());
					for (int k = 0; k < gBef.length; k++) {
						if (!gBef[k].equals("")) {
							String wRegx1 = gBef[k];
							String wRegx2 = gAft[k];
							if (!wRegx2.equals("")) {
								Pattern ptn = Digger.patternIgnoreCase(wRegx1); // �p�^�[���쐬
								rec = ptn.matcher(rec).replaceAll(wRegx2);
							}
							// wRec = wRec.replaceAll(gBef[k],gAft[k]);
						}
					}
					if (!rec.equals(preRec))
						wDirty = true;
					buff.append(rec);
					buff.append(LF);
					fw1.write(buff.toString());
				}
				br.close();
				fw1.close();
				File wFile_O = new File(oPath);
				if (wDirty) {
					// �I���W�i����������tmp���I���W�i���̖��̂ɕύX
					file.delete();
					// wFile_I.renameTo(new File(pPath_I+".bak")); // �o�b�N�A�b�v���c���H
					wFile_O.renameTo(new File(path));
					System.out.println("Tsjxx ������" + path);
				} else {
					// tmp�������i��������tmp�����Ȃ���Ηǂ��Ǝv�����ǁE�E�������������Ȃ肻���j
					wFile_O.delete();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return wDirty;
	}

}
