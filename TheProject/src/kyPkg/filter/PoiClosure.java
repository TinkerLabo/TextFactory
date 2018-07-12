package kyPkg.filter;

import java.io.FileInputStream;
import java.io.IOException;

import globals.ResControlWeb;
import kyPkg.external.PoiMods;

public class PoiClosure implements Inf_BaseClojure {
	private String[] comm1;

	private String[] comm2;

	private String[] comm3;

	private String[] header;

	private String delimiter = "\t";

	private int rowCursor = 0;

	private int baseY = 0;

	private int baseX = 0;

	private PoiMods poi;

	private FileInputStream fis;

	private String sheet = "data";
	private String template = ResControlWeb.getD_Resources_Templates("templates/3-GraphTR.xls");
	private String outPath = ResControlWeb.getD_Resources_PUBLIC("workbook.xls");
	public void setComm1(String[] comm1) {
		this.comm1 = comm1;
	}

	public void setComm2(String[] comm2) {
		this.comm2 = comm2;
	}

	public void setComm3(String[] comm3) {
		this.comm3 = comm3;
	}

	public void setHeader(String[] header) {
		this.header = header;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public void init() {
		poi = new PoiMods();
		fis = poi.openTheBook(template); // ブックを開く
		poi.selectSheet(sheet); // シートを選ぶ
		setComm1();
		setComm2();
		setComm3();
		setHeder();
	}

	public void setComm1() {
		if (comm1 != null) {
			for (int i = 0; i < comm1.length; i++) {
				poi.setCellValueS(4, baseX + i,comm1[i]);
			}
		}
	}

	public void setComm2() {
		if (comm2 != null) {
			for (int i = 0; i < comm2.length; i++) {
				poi.setCellValueS(5, baseX + i,comm2[i]);
			}
		}
	}

	public void setComm3() {
		if (comm3 != null) {
			for (int i = 0; i < comm3.length; i++) {
				poi.setCellValueS(6, baseX + i,comm3[i]);
			}
		}
	}

	public void setHeder() {
		if (header != null) {
			for (int i = 0; i < header.length; i++) {
				poi.setCellValueS(baseY - 1, baseX + i,header[i]);
			}
		}
	}
	@Override
	public void execute(String rec) {
		String[] array = rec.split(delimiter);
		execute(array);
	}
	@Override
	public void execute(String[] array) {
		for (int i = 0; i < array.length; i++) {
			poi.setCellValueX(baseY + rowCursor, baseX + i,array[i]);
		}
		rowCursor++;
	}
	@Override
	public void write() {
		poi.saveAs(outPath);
		try {
			fis.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setBaseYX(int baseY, int baseX) {
		this.baseY = baseY - 1;
		this.baseX = baseX - 1;
	}

	public void setBaseX(int baseX) {
		this.baseX = baseX - 1;
	}

	public void setBaseY(int baseY) {
		this.baseY = baseY - 1;
	}
}
