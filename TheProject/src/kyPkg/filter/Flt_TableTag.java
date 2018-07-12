package kyPkg.filter;

import java.io.*;

public class Flt_TableTag extends Abs_FileFilter {
	public Flt_TableTag() {
		super(true);
	}

	/*
	 * --------------------------------------------------------------------
	 * 《使用例》 import kyPkg.filter.*; FltTable ft = new FltTable();
	 * ft.setReader(jTa1.getText()); ft.convert(); String rtn = ft.getString();
	 * --------------------------------------------------------------------
	 */
	private String delimiter = ","; // ※注意　splitの引数はRegix！！

	@Override
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// -------------------------------------------------------------------------
	@Override
	public boolean execute() {
		String[][] map001 = { { "<", "&lt;" }, { ">", "&gt;" },
				{ " ", "&nbsp;" } };

		if (getReader() == null) {
			System.out.println("最初にsetReader処理が必要です");
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
			getWriter().write("<TT><table border=1>");
			// ---------------------------------------------------------------------
			// body スペースのみのセルをどうスル？
			// ---------------------------------------------------------------------
			String wRec = "";
			while ((wRec = br.readLine()) != null) {
				if (!wRec.trim().equals("")) {
					wBuf.append("<tr>");
					for (int i = 0; i < map001.length; i++) {
						if (map001[i].length == 2) {
							wRec = wRec.replaceAll(map001[i][0], map001[i][1]);
						}
					}
					String[] wCel = wRec.split(delimiter);// ※注意　splitの引数はRegix！！
					for (int k = 0; k < wCel.length; k++) {
						wBuf.append("<td>");
						if (!wCel[k].equals("")) {
							wBuf.append(wCel[k]);
						} else {
							wBuf.append("　");
						}
						wBuf.append("</td>");
					}
					wBuf.append("</tr>");
					wBuf.append(System.getProperty("line.separator"));
					getWriter().write(wBuf.toString());
					wBuf.delete(0, wBuf.length());
				}
			}
			// ---------------------------------------------------------------------
			// footer
			// ---------------------------------------------------------------------
			getWriter().write("</table></TT>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public static void main(String[] argv) {
		Flt_TableTag ft = new Flt_TableTag();
		ft.open_I(" < a > , < b > , < c > , d , e \n < f >,< g > ,< h >", false);
		ft.open_O();
		ft.execute();
		String result = ft.getString();
		System.out.println("result=>" + result);
	}

}
