package kyPkg.filter;

import java.io.*;

public class Flt_Html extends Abs_FileFilter {
	//	�R���X�g���N�^
	public Flt_Html() {
		super(true);
	}
	/*
	 * --------------------------------------------------------------------
	 * �s�g�p��t import kyPkg.filter.*; FltHtml ft = new FltHtml();
	 * ft.setReader(jTa1.getText()); ft.convert(); String rtn = ft.getString();
	 * --------------------------------------------------------------------
	 */
	// -------------------------------------------------------------------------
	// filterT �t�B���^�[�v���O����
	// �w�肳�ꂽ�X�g���[����ǂݍ���ŁA�X�g���[���֏����o��
	// �� boolean swt = filterT( new StringReader(jTa1.getText()) );
	// -------------------------------------------------------------------------
	@Override
	public boolean execute() {
		String[][] map001 = { { "<", "&lt;" }, { ">", "&gt;" },
				{ " ", "&nbsp;" }, { "\t", "&nbsp;&nbsp;&nbsp;&nbsp;" } };

		if (getReader() == null) {
			System.out.println("�ŏ���setReader�������K�v�ł�");
			return false;
		}
		if (getWriter() == null) {

		}
		try {
			StringBuffer wBuf = new StringBuffer();
			BufferedReader br = new BufferedReader(getReader());
			// ---------------------------------------------------------------------
			// header �����E�E�E Teletyped Text => TT
			// ---------------------------------------------------------------------
			getWriter().write("<TT>");
			// ---------------------------------------------------------------------
			// body �X�y�[�X�݂̂̃Z�����ǂ��X���H
			// ---------------------------------------------------------------------
			String wRec = "";
			while ((wRec = br.readLine()) != null) {
				if (!wRec.trim().equals("")) {
					// �������Ԃɕϊ�����
					for (int i = 0; i < map001.length; i++) {
						if (map001[i].length == 2) {
							wRec = wRec.replaceAll(map001[i][0], map001[i][1]);
						}
					}
					wBuf.append(wRec);
					wBuf.append("<BR>");
					wBuf.append(System.getProperty("line.separator"));
					getWriter().write(wBuf.toString());
					wBuf.delete(0, wBuf.length());
				}
			}
			// ---------------------------------------------------------------------
			// footer
			// ---------------------------------------------------------------------
			getWriter().write("</TT>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public static void tester() {
		Flt_Html ft = new Flt_Html();
		ft.open_I(" < a > , < b > , < c > , d , e \n < f >,< g > ,< h >", false);
		ft.open_O();
		ft.execute();
		System.out.println("result=>" + ft.getString());
	}
	public static void main(String[] argv) {
		tester();
	}
}
