package kyPkg.filter;

import java.io.*;

public class Flt_Scema extends Abs_FileFilter {
	private static final String LF = System.getProperty("line.separator");
	// �R���X�g���N�^
	public Flt_Scema() {
		super(true);
	}
	/*
	 * --------------------------------------------------------------------
	 * �s�g�p��t import kyPkg.filter.*; 
	 * FltScema ft = new FltScema();
	 * ft.setReader(jTa1.getText()); 
	 * ft.convert(); 
	 * String rtn = ft.getString();
	 * --------------------------------------------------------------------
	 */
	private String delimiter = "[,]"; // �����Ӂ@split�̈�����Regix�I�I

	@Override
	public String getDelimiter() {
		return delimiter;
	}

	private String delimiter1 = "\\s+"; // �����Ӂ@split�̈�����Regix�I�I
	private String delimiter2 = "[\"\\s+]"; // �����Ӂ@split�̈�����Regix�I�I

	@Override
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// -------------------------------------------------------------------------
	@Override
	public boolean execute() {
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
			// header
			// ---------------------------------------------------------------------
			String wTbl = "";
			String wFld = "";
			String wTyp = "";
			String wLen = "";
			String wRec = "";
			while ((wRec = br.readLine()) != null) {
				wRec = wRec.trim();
				if (!wRec.equals("")) {
					if (wRec.startsWith("[") && wRec.endsWith("]")) {
						int wPos = wRec.indexOf(".");
						if (wPos > 0)
							wTbl = wRec.substring(1, wPos);
						getWriter().write("CREATE TABLE " + wTbl + " (");
						getWriter().write(LF);
					}
					if (wRec.startsWith("ColNameHeader")) {
					} else if (wRec.startsWith("Col")) {
						if (wBuf.length() > 0) {
							wBuf.append(",");
							wBuf.append(LF);
							getWriter().write(wBuf.toString());
							wBuf.delete(0, wBuf.length());
						}
						wFld = "";
						wTyp = "";
						wLen = "";
						String[] wCel = wRec.split(delimiter1); // �����Ӂ@split�̈�����Regix�I�I
						String[] wCel2 = wCel[0].split(delimiter2); // �����Ӂ@split�̈�����Regix�I�I
						wFld = wCel2[1];
						wTyp = wCel[1];
						if (wCel.length > 2 && wCel[2].equals("Width")) {
							if (!wCel[3].trim().equals(""))
								wLen = "(" + wCel[3] + ")";
						}
						wBuf.append("    f");
						wBuf.append(wFld);
						wBuf.append("    ");
						wBuf.append(wTyp);
						if (!wLen.equals(""))
							wBuf.append(wLen);
					}
				}
			}
			if (wBuf.length() > 0) {
				wBuf.append(LF);
				wBuf.append(")");
				wBuf.append(LF);
				getWriter().write(wBuf.toString());
				wBuf.delete(0, wBuf.length());
			}
			// ---------------------------------------------------------------------
			// footer
			// ---------------------------------------------------------------------
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}
}
