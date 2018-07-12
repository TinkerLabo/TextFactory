package kyPkg.tools;

import java.io.*;
import java.util.*;

import kyPkg.uFile.FileUtil;

public class UrlMixer {
	final static String TAB = "\t";
	final static String REF1 = "<BR><A HREF=\"";
	final static String REF2 = "\">";
	final static String REF3 = "</A>";
	final static String LS = System.getProperty("line.separator");

	// -------------------------------------------------------------------------
	// ���K�w�ɂ���*.url�t�@�C�������ׂēǂݍ���ŁE�E
	// fab.html�� �܂Ƃ߂�E�E���ꂾ��
	// -------------------------------------------------------------------------
	// �w�肳�ꂽ�t�@�C����ǂݍ���ŁA���K�\���Ƀ}�b�`����p�^�[������������
	// �g�p�� boolean swt = checkIt(wPath_I,".*static\\s*void\\s*main.*");
	// -------------------------------------------------------------------------
	public static String checkURL(String path, FileWriter fw) {
		//System.out.println("checkIt >>" + iPath);
		File iFile = new File(path);
		String wName = iFile.getName();
		String splited[] = wName.split("\\.");
		wName = splited[0]; // �t�@�C�����̓�
		String wRec;
		String wUrl = "";
		String wInfo = "";
		boolean wFlg = false;
		// --------------------------------------------------------------------
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			wUrl = "";
			while ((wRec = br.readLine()) != null) {
				if (wRec.startsWith("[DEFAULT]")) {
					wFlg = true;
				}
				if (wRec.startsWith("[InternetShortcut]")) {
					wFlg = true;
				}
				if (wFlg == true) {
					if (wRec.matches("BASEURL=*.*")) {
						wUrl = wRec.substring(8, wRec.length());
						wInfo = REF1 + TAB + wUrl + TAB + REF2 + TAB + wName
								+ TAB + REF3 + LS;
						break;
					}
					if (wRec.matches("URL=*.*")) {
						wUrl = wRec.substring(4, wRec.length());
//						System.out.println("  wName >>" + wName);
//						System.out.println("  BASEURL >>" + wRec);
						wInfo = REF1 + TAB + wUrl + TAB + REF2 + TAB + wName
								+ TAB + REF3 + LS;
						break;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wInfo;
	}

	// ----------------------------------------------------------------
	// Get����
	// ----------------------------------------------------------------
	public static String getYY_MM() {
		String wRtn;
		Calendar wCal = Calendar.getInstance();
		int sMM = wCal.get(Calendar.MONTH) + 1;
		int sDD = wCal.get(Calendar.DATE);
		wRtn = "" + sMM + "_" + sDD;
		return wRtn;
	}

	// ----------------------------------------------------------------
	// #xxx �y�A�����g�p�X���擾(File.getParent()���g���Ȃ����E�E)
	// �s�g�p��t
	// String mamapath = makeParents("c:\ga\bba\gabba\hey.txt");
	// ----------------------------------------------------------------
	public static String getPpath(String pPath) {
		if (pPath.indexOf(".") > 0) { // �t�@�C�������܂܂�Ă��邩
			int pos = pPath.lastIndexOf(System.getProperty("file.separator"));
			if (pos < 0) {
				System.out.println("#Error Not Directory Path!\n" + "       =>"
						+ pPath);
				return "";
			}
			pPath = pPath.substring(0, pos); // ParentPath��ݒ肵����
		}
		return pPath;
	}

	// -----------------------------------------------------------
	// �{�^���������ꂽ
	// btnFetch.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent arg0) {
	// btnFetch.setEnabled(false);
	// JFileChooser fc = new JFileChooser(gPath);
	// // fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	// // fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	// fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	// if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	// setDefaultPath(fc.getSelectedFile().toString());
	// if (actionListener != null) {
	// actionListener.actionPerformed(arg0);
	// }
	// }
	// btnFetch.setEnabled(true);
	// }
	// });

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		String iPath;
		File f = new File(".");
		try {
			String wkDir = getPpath(f.getAbsolutePath());
			String oPath = wkDir + System.getProperty("file.separator")
					+ getYY_MM() + ".html";
			System.out.println("oPath >>" + oPath);
			FileWriter writer = new FileWriter(new File(oPath));
			// -----------------------------------------------------------------
			// �J�����g�p�X��ɂ���t�@�C����z��ɗ�������
			// -----------------------------------------------------------------
			String[] array = f.list();
			for (int i = 0; i < array.length; i++) {
				iPath = array[i];
				// �g���q�𔻒肵�Ď�����
				if ((iPath.toLowerCase()).endsWith(".url")) {
					String wInfo = checkURL(iPath, writer);
					if (writer != null && !wInfo.equals("")) {
						writer.write(wInfo);
					}

				}
			}
			// -----------------------------------------------------------------
			writer.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
