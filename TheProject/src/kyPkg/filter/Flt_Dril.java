package kyPkg.filter;
import java.io.*;
public class Flt_Dril extends Abs_FileFilter {
	/*
	 * --------------------------------------------------------------------
	 * �s�T�v�t�������K�����������`���ɕϊ�����
	 * 
	 * �s�g�p�� ���̈�t import kyPkg.filter.*; FltDril ft = new FltDril();
	 * ft.setReader(jTa1.getText()); ft.convert(); String rtn = ft.getString();
	 * jTa2.setText(rtn);
	 * 
	 * �s�g�p�� ���̓�t import kyPkg.filter.*; String wPath1 = jTf1.getText(); if(new
	 * File(wPath1).exists() == false ) { String wErr = "���̓f�[�^�����݂��Ă��܂���";
	 * JOptionPane.showMessageDialog((Component)null,wErr
	 * ,"Message...",JOptionPane.ERROR_MESSAGE ); return; } FltDril ft = new
	 * FltDril(); ft.openIt(wPath1); String wEndMsg = ft.convert();
	 * ft.closeIt(); JOptionPane.showMessageDialog((Component)null,wEndMsg
	 * ,"Message...",JOptionPane.INFORMATION_MESSAGE); ft = null;
	 * --------------------------------------------------------------------
	 */
 	public Flt_Dril() {
		super(true);
	}
	// -------------------------------------------------------------------------
	// �������Ԃɕϊ�����
	// -------------------------------------------------------------------------
	String convEsc(String pStr) {
		String[][] map001 = { { " ", "&nbsp;" },
				{ "\t", "&nbsp;&nbsp;&nbsp;&nbsp;" } };
		for (int i = 0; i < map001.length; i++) {
			if (map001[i].length == 2) {
				pStr = pStr.replaceAll(map001[i][0], map001[i][1]);
			}
		}
		return pStr;
	}
	// -------------------------------------------------------------------------
	// �e�L�X�g���X�g���[���Ƃ��Ĉ����ɂ́E�E�E
	// StringReader in = new StringReader(jTa1.getText());
	// BufferedReader br = new BufferedReader(in);
	// -------------------------------------------------------------------------
	// filterT �t�B���^�[�v���O����
	// �w�肳�ꂽ�X�g���[����ǂݍ���ŁA�X�g���[���֏����o��
	// �� boolean swt = filterT( new StringReader(jTa1.getText()) );
	// -------------------------------------------------------------------------
	@Override
	public boolean execute() {
		if (getReader() == null) {
			System.out.println("�ŏ���setReader�������K�v�ł�");
			super.setStat("ERROR");
			return false;
		}
		if (getWriter() == null) {

		}
		try {
			BufferedReader br = new BufferedReader(getReader());
			StringBuffer wBuf = new StringBuffer();
			StringBuffer wQes = new StringBuffer();
			StringBuffer wPgm = new StringBuffer();
			StringBuffer wSel = new StringBuffer();
			// ---------------------------------------------------------------------
			// header �����E�E�E Teletyped Text => TT
			// ---------------------------------------------------------------------
			// out.write("<TT>");
			// ---------------------------------------------------------------------
			// body �X�y�[�X�݂̂̃Z�����ǂ��X���H
			// ---------------------------------------------------------------------
			String wRec = "";
			char wChar = 'A' - 1; // = @ �I�����ɕt����񓚋L��
			short wSeq = 0; // �\�[�X�R�[�h�p�s�ԍ�
			String wQSeq = ""; // �ݖ�ԍ�
			String wAnswer = ""; // ��
			while ((wRec = br.readLine()) != null) {
				if (!wRec.equals("")) {
					if (wRec.startsWith("//")) {
					} else if (wRec.startsWith("@Q")) { // �ݖ�ԍ�����
						// -----------------------------------------------------
						// Break!!
						if (wQSeq != "") {
							wBuf.append(wQSeq + "<br>");
							wBuf.append("<input type=Hidden name=qsq value='"
									+ wQSeq + "'>");
							wBuf.append("\t");
							wBuf.append("<input type=Hidden name=ans value='"
									+ wAnswer + "'>");
							wBuf.append("\t");
							wBuf.append(wQes);
							wBuf.append("\t");
							wBuf.append(wPgm);
							wBuf.append("\t");
							wBuf.append(wSel);
							wBuf.append("$$");
							wBuf.append(System.getProperty("line.separator"));
							getWriter().write(wBuf.toString());
							// Clear
							wQes.delete(0, wQes.length());
							wPgm.delete(0, wPgm.length());
							wSel.delete(0, wSel.length());
							wBuf.delete(0, wBuf.length());
						}
						// -----------------------------------------------------
						wChar = 'A' - 1; // = @ �I�����ɕt����񓚋L��
						wSeq = 0; // �\�[�X�R�[�h�p�s�ԍ�
						wQSeq = wRec.trim().substring(3);
					} else if (wRec.startsWith("@A")) { // �𓚕���
						// �𓚂͂������邩�H �ɂ���� �J���}�̐�����������H
						// �������������̂��`�`�H����H�I�тȂ����B�̃R�����g���쐬
						wAnswer = wRec.trim().substring(5);
						String[] wAnsArray = wAnswer.split(",");
						wQes.append("(" + wAnsArray.length + "�I��)");
						wQes.append("<BR>");

					} else {
						if (wRec.startsWith("\t")) { // �\�[�X�R�[�h����
							wRec = convEsc(wRec);
							wSeq++;
							if (wSeq < 10) {
								wPgm.append("0" + wSeq);
							} else {
								wPgm.append("" + wSeq);
							}
							wPgm.append(":");
							wPgm.append(wRec);
							wPgm.append("<BR>");
						} else if (wRec.startsWith(":")) { // �I��������
							wRec = convEsc(wRec);
							wChar++;
							wSel
									.append("<input type='checkbox' name='objP1' value='"
											+ wChar + "'>");
							wSel.append(wChar);
							wSel.append(wRec);
							wSel.append("<BR>");
						} else {
							wRec = convEsc(wRec);
							wQes.append(wRec);
							wQes.append("<BR>");
						}
					}
				}
			}
			// -----------------------------------------------------
			// Break!!
			if (wQSeq != "") {
				wBuf.append(wQSeq + "<br>");
				wBuf.append("<input type=Hidden name=qsq value='" + wQSeq+ "'>");
				wBuf.append("\t");
				wBuf.append("<input type=Hidden name=ans value='" + wAnswer	+ "'>");
				wBuf.append("\t");
				wBuf.append(wQes);
				wBuf.append("\t");
				wBuf.append(wPgm);
				wBuf.append("\t");
				wBuf.append(wSel);
				wBuf.append("$$");
				wBuf.append(System.getProperty("line.separator"));
				getWriter().write(wBuf.toString());
				// Clear
				wQes.delete(0, wQes.length());
				wPgm.delete(0, wPgm.length());
				wSel.delete(0, wSel.length());
				wBuf.delete(0, wBuf.length());
			}
			// -----------------------------------------------------
			// footer
			// -----------------------------------------------------
			// out.write("</TT>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// ---------------------------------------------------------------------
		super.setStat("�ϊ��I��" + "\n �o�̓t�@�C���F " + this.getOutFile());
		// ---------------------------------------------------------------------

		return true;
	}
	public static void tester() {
		Flt_Dril ft = new Flt_Dril();
		String rootDir = globals.ResControl.getQprRootDir();
		ft.open_I(rootDir+"���׋�/�͋[�����P.txt");
		ft.open_O(rootDir+"result.txt",false);
		ft.execute();
		System.out.println("result=>" + ft.getOutFile());
	}
	public static void main(String[] argv) {
		tester();
	}

}
