package kyPkg.external;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import kyPkg.uFile.FileUtil;
import kyPkg.util.RuntimeEnv;

//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import globals.ResControlWeb;

//-----------------------------------------------------------------------------
//�����ݒ�̃R�s�[��getCell�ōs���Ă���
//API
//https://poi.apache.org/apidocs/org/apache/poi/hssf/usermodel/HSSFRow.html
//-----------------------------------------------------------------------------
// ��ʃI�u�W�F�N�g��Null�̏ꍇ�̏�����ǉ����Ă���
/**
 * ���[�N�u�b�N�̐V�K�쐬�T���v��
 */
public class PoiMods {
	public static final String MSP_GOTHIC = "�l�r �o�S�V�b�N";
	public static final String MS_GOTHIC = "MS �S�V�b�N";
	public static final String MS_MINCYOU = "�l�r ����";
	private String bookPath;

	// private HSSFWorkbook currentWorkBook;
	private Workbook workbook;

	// private HSSFSheet currentSheet;
	private Sheet currentSheet;

	// private HSSFCell currentCell;
	private Cell currentCell;

	private static Pattern pattern;

	// private HashMap<Integer, HSSFCellStyle> prevStyles;
	public HashMap<Integer, CellStyle> prevStyles;

	// private CellStyle style1;
	// private CellStyle style2;
	// private CellStyle style3;

	// private HSSFCellStyle prevStyle;
	// private HSSFCellStyle currentStyle;
	// public HSSFCellStyle getCurrentStyle() {
	// return currentStyle;
	// }
	// public void setCurrentStyle(HSSFCellStyle currentStyle) {
	// this.currentStyle = currentStyle;
	// }

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public PoiMods() {
		super();
		this.prevStyles = new HashMap();
		pattern = patternIgnoreCase("[+-]*\\d+[.]*\\d*");

	}

	public void resetPrevStyles() {
		this.prevStyles = new HashMap();
	}

	// private HSSFFont getFont(String fontName, short fontSize) {
	private Font getFont(String fontName, short fontSize) {
		// �t�H���g���`����
		// HSSFFont font = currentWorkBook.createFont();
		Font font = workbook.createFont();
		// �E�t�H���g��
		font.setFontName(fontName);
		// �E�t�H���g�T�C�Y
		font.setFontHeightInPoints(fontSize);
		// font.setColor(color);
		return font;
	}

	// -------------------------------------------------------------------------
	// �V�[�g�̊J�n�s
	// -------------------------------------------------------------------------
	// public int getFirstRowNum(HSSFSheet sheetObj) {
	public int getFirstRowNum(Sheet sheetObj) {
		return sheetObj.getFirstRowNum();
	}

	// -------------------------------------------------------------------------
	// �V�[�g�̏I���s
	// -------------------------------------------------------------------------
	// public int getLastRowNum(HSSFSheet sheetObj) {
	public int getLastRowNum(Sheet sheetObj) {
		return sheetObj.getLastRowNum();
	}

	// short lastCellNum = row.getLastCellNum(); // �I����
	// short firstCellNum = row.getFirstCellNum(); // �J�n��
	// if(currentStyle!=null)
	// currentCell.setCellStyle(currentStyle);

	// setFont("MS �S�V�b�N",30);
	// public HSSFCellStyle setCurrentFont(String fontName, int size,
	public CellStyle setCurrentFont(String fontName, int size,
			boolean wrapOpt) {
		// HSSFCellStyle cellStyle = currentWorkBook.createCellStyle();
		CellStyle cellStyle = workbook.createCellStyle();
		// �Z���̃t�H���g��ݒ肷��
		// HSSFFont font = getFont(fontName, (short) size);
		Font font = getFont(fontName, (short) size);
		if (wrapOpt) {
			cellStyle.setWrapText(true);// �܂�Ԃ��đS�̂�\��
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
			cellStyle.setAlignment(CellStyle.ALIGN_LEFT);

			// cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			// cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

			// currentStyle.setAlignment(align);
		}
		cellStyle.setFont(font);
		// cell.setCellStyle(style);
		return cellStyle;
	}

	// -------------------------------------------------------------------------
	// �Z����I�ъ�������Ƃ��Ď��o��
	// -------------------------------------------------------------------------
	public String getCellStr(int pRow, int pCol) {
		String wStr = "";
		currentCell = getCell(currentSheet, pRow, pCol);
		if (currentCell != null) {
			switch (currentCell.getCellType()) {
			// case HSSFCell.CELL_TYPE_STRING:
			case Cell.CELL_TYPE_STRING:
				wStr = currentCell.getStringCellValue();
				// HSSFRichTextString wRtn1 = cell.getRichStringCellValue();
				// wStr = wRtn1.toString();
				break;
			// case HSSFCell.CELL_TYPE_NUMERIC:
			case Cell.CELL_TYPE_NUMERIC:
				double wRtn2 = currentCell.getNumericCellValue();
				wStr = Double.toString(wRtn2);
				break;
			// case HSSFCell.CELL_TYPE_FORMULA:
			case Cell.CELL_TYPE_FORMULA:
				double wRtn3 = currentCell.getNumericCellValue();
				wStr = Double.toString(wRtn3);
				// wStr = cell.getStringCellValue() ;
				// String wRtn3 = cell.getCellFormula();
				// wStr = wRtn3.toString();
				break;
			// case HSSFCell.CELL_TYPE_BOOLEAN:
			case Cell.CELL_TYPE_BOOLEAN:
				boolean wRtn4 = currentCell.getBooleanCellValue();
				wStr = " " + wRtn4;
				break;
			// case HSSFCell.CELL_TYPE_ERROR:
			case Cell.CELL_TYPE_ERROR:
				wStr = "CELL_TYPE_ERROR";
				break;
			}
		}
		return wStr;
	}

	// -------------------------------------------------------------------------
	// ���[�N�u�b�N�Ɋ܂܂��V�[�g���ꗗ�𓾂�
	// -------------------------------------------------------------------------
	public String[] getSheetNames() {
		// System.out.println("SelectedTab:"+ wb.getSelectedTab());
		int n = workbook.getNumberOfSheets(); // �܂܂��V�[�g�̐�
		System.out.println("NumberOfSheets:" + n);
		String[] wRtn = new String[n];
		for (int i = 0; i < n; i++) {
			wRtn[i] = workbook.getSheetName(i);
			System.out.println("Sheet:" + wRtn[i]);
		}
		return wRtn;
	}

	// -------------------------------------------------------------------------
	// �d���������̃��[�N�u�b�N���J��
	// -------------------------------------------------------------------------
	public FileInputStream openTheBook(String pPath) {
		System.out.println("openTheBook:" + pPath);
		if (!new File(pPath).isFile()) {
			System.out.println("##ERROR FileNot Exist!:" + pPath);
			return null;
		}
		FileInputStream fis = null;
		try {
			bookPath = pPath;
			fis = new FileInputStream(pPath);

			//			https://poi.apache.org/apidocs/org/apache/poi/xssf/usermodel/XSSFWorkbook.html

			//			String type="SXSSF";
			//	        switch (type) {
			//	        case "XSSF":
			//	            workbook = new XSSFWorkbook();
			//	            break;
			//	        case "SXSSF":
			//	            workbook = new SXSSFWorkbook();
			//	            break;
			//	        default:
			//	            workbook = new XSSFWorkbook();
			//	            break;
			//	        }

			workbook = WorkbookFactory.create(fis);// ??20140227�������H
			//			workbook = new XSSFWorkbook(fis);// HSSF�̃o�[�W�����A�b�v�� ����ł��q�[�v������Ȃ��E�E�E

			// �w�i�p�^�[���Ƃ�
			// style1 = currentWorkBook.createCellStyle();
			// style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// style1.setFillForegroundColor(IndexedColors.MAROON.getIndex());
			//
			// style2 = currentWorkBook.createCellStyle();
			// style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// style2.setFillForegroundColor(IndexedColors.RED.getIndex());
			//
			// style3 = currentWorkBook.createCellStyle();
			// style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// style3.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			// ------------------------------------------------------------------------------------------
			// �uorg.apache.poi.ss.usermodel.IndexedColors�v
			// AQUA AUTOMATIC BLACK BLUE
			// BLUE_GREY BRIGHT_GREEN BROWN CORAL
			// CORNFLOWER_BLUE DARK_BLUE DARK_GREEN DARK_RED
			// DARK_TEAL DARK_YELLOW GOLD GREEN
			// GREY_25_PERCENT GREY_40_PERCENT GREY_50_PERCENT GREY_80_PERCENT
			// INDIGO LAVENDER LEMON_CHIFFON LIGHT_BLUE
			// LIGHT_CORNFLOWER_BLUE LIGHT_GREEN LIGHT_ORANGE
			// LIGHT_TURQUOISE LIGHT_YELLOW LIME MAROON
			// OLIVE_GREEN ORANGE ORCHID PALE_BLUE
			// PINK PLUM RED ROSE
			// ROYAL_BLUE SEA_GREEN SKY_BLUE TAN
			// TEAL TURQUOISE VIOLET WHITE
			// YELLOW
			// ------------------------------------------------------------------------------------------

			// POIFSFileSystem poiFs = new POIFSFileSystem(fis);
			// currentWorkBook = new HSSFWorkbook(poiFs);

			// currentWorkBook.createFont().setFontName(MS_GOTHIC);
			// currentWorkBook.getFontAt((short) 0).setFontName(MS_GOTHIC);
			// currentWorkBook.getFontAt((short) 0).setFontHeightInPoints(
			// (short) 10);

		} catch (IOException ie) {
			ie.printStackTrace();
			//		} catch (InvalidFormatException e) {
			//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fis;
	}

	// -------------------------------------------------------------------------
	// �d���������̃��[�N�u�b�N��ۑ�����
	// -------------------------------------------------------------------------
	public void saveAs(String pPath) {
		System.out.println(
				"debug0316:saveAs memory rate:" + RuntimeEnv.memoryInfo());

		FileUtil.makeParents(pPath);
		try {
			File wFile = new File(pPath);
			if (wFile.isFile()) {
				if (!wFile.canWrite()) {
					System.out.println(
							"#ERROR PoiMods@save as File Can't Write:" + pPath);
					return;
				}
			}
			FileOutputStream fileOut = new FileOutputStream(pPath);
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// �V�[�g���Z���N�g����
	// -------------------------------------------------------------------------
	public void selectSheet(int pSeq) {
		currentSheet = workbook.getSheetAt(pSeq);// ���V�[�g �I�u�W�F�N�g
		// �G���[��������ǂ�����H�H
		System.out.println("Sheet:" + workbook.getSheetName(0));
	}

	public void selectSheet(String pName) {
		pName = charsetConv(pName);
		if (workbook == null)
			return;
		currentSheet = workbook.getSheet(pName);
		if (currentSheet == null) {
			currentSheet = workbook.createSheet(pName);
		}
	}

	// ���݂��Ȃ��V�[�g�̏ꍇ
	// ���ɓ����̃V�[�g�����݂���΂���
	// ���ꂼ��̂Ȃɂ��N���邩�m�F����
	public boolean sheetRename(String preName, String newName) {
		if (preName.equals(newName))
			return true;
		int preSheetIndex = workbook.getSheetIndex(preName);
		System.out.println("@sheetRename preName:" + preName);
		System.out.println("@sheetRename newName:" + newName);
		System.out.println("@sheetRename preSheetIndex:" + preSheetIndex);

		int newSheetIndex = workbook.getSheetIndex(newName);
		System.out.println("@sheetRename newSheetIndex:" + newSheetIndex);
		if (newSheetIndex >= 0) {
			System.out.println("Already existed error�Ǝv���E�E�E");
			return false;
		}

		workbook.setSheetName(preSheetIndex, newName);
		// 2013-07-17�G���R�[�h�͎����F�������悤�ɂȂ����̂��H
		// currentWorkBook.setSheetName(sheetIndex, newName,
		// HSSFWorkbook.ENCODING_UTF_16);
		return true;
	}

	// -------------------------------------------------------------------------
	// �Z�����Z���N�g����
	// -------------------------------------------------------------------------
	public void selectCell(int pRow, int pCol) {
		currentCell = getCell(currentSheet, pRow, pCol);
	}

	// -------------------------------------------------------------------------
	// �Z���̒l��ݒ�
	// String value = "45.1";
	// myStyle = wb.createCellStyle();
	// myStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
	// currentCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
	// currentCell.setCellValue(value);
	// currentCell.setCellStyle(myStyle);
	// -------------------------------------------------------------------------
	// �������
	// -------------------------------------------------------------------------
	public void setCellValueS(int pRow, int pCol, String pVal) {
		setCellValueS(pRow, pCol, pVal, null);
	}

	// public void setCellValueS(int pRow, int pCol, String pVal,HSSFCellStyle
	// style) {
	public void setCellValueS(int pRow, int pCol, String pVal,
			CellStyle style) {
		currentCell = getCell(currentSheet, pRow, pCol);
		if (currentCell == null)
			return;
		// Font�̐ݒ�ȂǁE�E�E
		// if (currentStyle != null) {
		// // ���̎��_�ł��łɌ��̏����͉��Ă���ƌ��Ă悳�����E�E�E�E
		// // prevStyle = currentCell.getCellStyle();
		// //
		// System.out.println("DEBUG prevStyle.getWrapText():"+prevStyle.getWrapText());
		// //
		// System.out.println("DEBUG prevStyle.getAlignment():"+prevStyle.getAlignment());
		// currentCell.setCellStyle(currentStyle);
		// }
		if (pVal == null)
			pVal = "NULL";
		try {
			// 2017-07-17 Deprecated. As of 3-Jan-06 POI now automatically
			// handles Unicode without forcing the encoding.
			// currentCell.setEncoding(HSSFCell.ENCODING_UTF_16);

			// currentCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			currentCell.setCellType(Cell.CELL_TYPE_STRING);

			currentCell.setCellValue(charsetConv(pVal));
			if (style != null)
				currentCell.setCellStyle(style);
		} catch (Exception e) {
			System.out.println("## Exception @setCellValue pVal:" + pVal);
		}
	}

	// -------------------------------------------------------------------------
	// ���l��
	// -------------------------------------------------------------------------
	public void setCellValueN(int pRow, int pCol, String pVal) {
		currentCell = getCell(currentSheet, pRow, pCol);
		if (currentCell == null)
			return;
		if (pVal == null)
			return;
		try {
			double value = Double.parseDouble(pVal);
			// currentCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			currentCell.setCellValue(value);

		} catch (Exception e) {
			System.out.println("## Exception @setCellValue pVal:" + pVal);
		}
	}

	public static String charsetConv(String src) {
		String charsetName = java.nio.charset.Charset.defaultCharset().name();
		// System.out.println("charsetName:"+charsetName);
		String dest = src + "<UnsupportedEncoding>";
		try {
			dest = new String(src.getBytes(), charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dest;
	}

	private static boolean isNumeric(String pStr) {
		if (pStr == null)
			return false;
		if (pStr.equals(""))
			return false;
		pStr = pStr.trim();
		if (pStr.equals(""))
			return false;
		if (pattern.matcher(pStr).matches()) {
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------
	// �����p�^�[�����R���p�C��
	// -------------------------------------------------------------------------
	public static Pattern patternIgnoreCase(String pRegex) {
		return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	}

	// public void setCellValue(short pVal) {
	// currentCell.setCellNum(pVal);
	// //HSSFRow.moveCell(HSSFCell, short) instead
	// }

	public void setCellValue(int pVal) {
		currentCell.setCellValue(pVal);
	}

	public void setCellValue(Double pVal) {
		currentCell.setCellValue(pVal.doubleValue());
	}

	public void setCellValue(double pVal) {
		currentCell.setCellValue(pVal);
	}

	public void setCellValue(boolean pVal) {
		currentCell.setCellValue(pVal);
	}

	// -------------------------------------------------------------------------
	// For writeTo ���e�X�g
	// -------------------------------------------------------------------------
	public void writeTo(String wBook, String wSheet, int x1, int y1,
			Vector pVec) {
		FileInputStream fis = this.openTheBook(wBook);// �u�b�N���J��
		this.selectSheet(wSheet);// �V�[�g��I��
		Vector wCol;
		System.out.println("---------------------------------------------");
		for (int i = 0; i < pVec.size(); i++) {
			wCol = (Vector) pVec.get(i);
			if (wCol != null) {
				System.out.print(" [" + i + " ");
				for (int j = 0; j < wCol.size(); j++) {
					if (wCol.get(j) != null) {
						System.out
								.print(" <" + j + ">" + wCol.get(j).toString());
						this.setCellValueX(x1 + i - 1, y1 + j - 1,
								wCol.get(j).toString());
					}
				}
				System.out.println(" ]");
			}
		}
		System.out.println("---------------------------------------------");
		this.saveAs(wBook); // �J���Ă���u�b�N�ɏ������߂�̂��ǂ������I
		try {
			fis.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// For readFrom ������e�[�u���ɗ������߂Ȃ����낤���H�H
	// -------------------------------------------------------------------------
	public Vector readFrom(String wBook, String wSheet, int x1, int y1, int x2,
			int y2) {
		FileInputStream fis = this.openTheBook(wBook); // �u�b�N���J��
		this.selectSheet(wSheet); // �V�[�g��I��
		Vector wVec1 = new Vector();
		for (int y = y1; y <= y2; y++) {
			Vector wVec2 = new Vector();
			for (int x = x1; x <= x2; x++) {
				wVec2.add(this.getCellStr(y, x));
			}
			wVec1.add(wVec2);
		}
		try {
			fis.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return wVec1;
	}

	public String getBookPath() {
		return bookPath;
	}

	// ########################################################################
	// For Test
	// ########################################################################
	public static void main(String args[]) {
		// readFromTester();
		csv2ExcelTemplate("data");
	}

	// ########################################################################
	// For Test1
	// ########################################################################
	public static void csv2ExcelTemplate(String targetSheet) {
		// String srcFile =FileUtil.getResDir("templates/temp01.txt";
		System.out.print("�y�J�n�z");
		PoiMods pMod = new PoiMods();
		String path = ResControlWeb.getD_Resources_Templates("3-GraphTR.xls");
		FileInputStream fis = pMod.openTheBook(path);// �u�b�N���J��
		pMod.selectSheet(targetSheet);// �V�[�g��I��
		for (int x = 1; x < 10; x++) {
			for (int y = 1; y < 5; y++) {
				System.out.print("_" + pMod.getCellStr(y, x));
				// �Z����I�ъ�������Ƃ��Ď��o��
				pMod.setCellValueS(y, x, "�䂠��", null);
			}
		}
		pMod.saveAs(ResControlWeb.getD_Resources_PUBLIC("workbook.xls"));
		try {
			fis.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		System.out.println("�y�I���z");
	}

	// ########################################################################
	// �s For readFrom�̎g�p�� �texcel��ǂݎ��
	// ########################################################################
	public static void readFromTester() {
		String wBook = ResControlWeb.getD_Resources_Templates("3-GraphTR.xls");
		String wSheet = "data";
		int x1 = 1;
		int y1 = 57;
		int x2 = 7;
		int y2 = 59;
		PoiMods pMod = new PoiMods();
		Vector wRow = pMod.readFrom(wBook, wSheet, x1, y1, x2, y2);
		Vector wCol;
		System.out.println("---------------------------------------------");
		for (int i = 0; i < wRow.size(); i++) {
			wCol = (Vector) wRow.get(i);
			if (wCol != null) {
				System.out.print(" [" + i + " ");
				for (int j = 0; j < wCol.size(); j++) {
					if (wCol.get(j) != null) {
						System.out
								.print(" <" + j + ">" + wCol.get(j).toString());
					}
				}
				System.out.println(" ]");
			}
		}
		System.out.println("---------------------------------------------");
	}

	// -------------------------------------------------------------------------
	// �l�����
	// -------------------------------------------------------------------------
	public void setCellValue(int row, int col, String val) {
		// 20140822���̕���
		setCellValueX(row + 1, col + 1, val);
	}

	// -------------------------------------------------------------------------
	// �s�A����w�肵�ăZ���I�u�W�F�N�g���擾����(�����ݒ�̃R�s�[���s��)
	// -------------------------------------------------------------------------
	// private HSSFCell getCell(HSSFSheet sheetObj, int pRow, int pCol) {
	private Cell getCell(Sheet sheetObj, int pRow, int pCol) {
		Cell cellObj = null;
		//		if (pCol > 255 || pRow > 65535) {
		if (pCol > 4096 || pRow > 65535) {
			System.out.println("# ERROR @PoiMods.getCell range error");
			System.out.println("# pCol:" + pCol);
			System.out.println("# pRow:" + pRow);
			return null;
		}
		Row rowObj = sheetObj.getRow(pRow);
		if (rowObj == null) {
			// �����͒ʂ��Ă��Ȃ��HSystem.out.println("rowObj is null row:"+pRow+" col:"+pCol);
			rowObj = sheetObj.createRow(pRow);
		}

		// 20140822
		// �Z�������݂��Ȃ��E�E�E�Ƃ������Ƃ͔w�i�F�Ȃǂ͂ǂ��ɍڂ��Ă���񂾂��H�H
		cellObj = rowObj.getCell(pCol);
		if (cellObj == null) {
			// �����ŃX�^�C��������������Ă��܂��悤���E�E�E2014
			// System.out.println("cellObj is null row:" + pRow + " col:" +
			// pCol);

			cellObj = rowObj.createCell(pCol);
			cellObj.setCellType(Cell.CELL_TYPE_STRING);

			// ���O�̓����J�����̏������R�s�[����̂��~�߂Ă݂��i20140822�j
			// CellStyle currentStyle = prevStyles.get(pCol);
			// if (currentStyle != null){
			// cellObj.setCellStyle(currentStyle);
			// }
		} else {
			// prevStyles.put(pCol, cellObj.getCellStyle());
		}
		return cellObj;
	}

	// private Cell getCell_org(Sheet sheetObj, int pRow, int pCol) {
	// Cell cellObj = null;
	// if (pCol > 255 || pRow > 65535) {
	// System.out.println("# ERROR @PoiMods.getCell range error");
	// System.out.println("# pCol:" + pCol);
	// System.out.println("# pRow:" + pRow);
	// return null;
	// }
	// Row rowObj = sheetObj.getRow(pRow);
	// if (rowObj == null)
	// rowObj = sheetObj.createRow(pRow);
	// cellObj = rowObj.getCell(pCol);
	// // �����ŃX�^�C��������������Ă��܂��悤���E�E�E2014
	// if (cellObj != null) {
	// prevStyles.put(pCol, cellObj.getCellStyle());
	// } else {
	// cellObj = rowObj.createCell(pCol);
	// cellObj.setCellType(Cell.CELL_TYPE_STRING);
	// CellStyle currentStyle = prevStyles.get(pCol);
	// if (currentStyle != null)
	// cellObj.setCellStyle(currentStyle);
	// }
	// return cellObj;
	// }

	// -------------------------------------------------------------------------
	// ����ł͐��`��ɐݒ肵���w�i�F����������Ă��܂�
	// �Z���I�u�W�F�N�g�𐶐����Ă���̂��e�����Ă���̂ł͂Ȃ���
	// -------------------------------------------------------------------------
	public void setCellValueX(int row, int col, String val) {
		// if(true)
		// return;

		// 20140822���̕���
		// 20140822�@getCell�i�j�̓����ł��������Ȃ�悤��
		currentCell = getCell(currentSheet, row, col);

		if (currentCell == null) {
			System.out.println("#ERROR @setCellValue cell is NULL row:" + row
					+ " col:" + col);

			return;
		}

		// Font�̐ݒ�ȂǁE�E�E
		// if (currentStyle != null)
		// currentCell.setCellStyle(currentStyle);

		if (val == null)
			val = "";
		try {
			if (val == null) {
				// currentCell.removeCellComment();
				// currentCell.setCellValue("");
				currentCell.setCellType(Cell.CELL_TYPE_BLANK);
				// currentCell.removeCellComment();
			} else if (isNumeric(val)) {
				double value = Double.parseDouble(val);
				// currentCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				// currentCell.setCellValue(Integer.parseInt(pVal));
				currentCell.setCellValue(value);
			} else {
				// System.out.println("## not num1 ...:"+pVal);
				// 2017-07-17 Deprecated. As of 3-Jan-06 POI now automatically
				// handles Unicode without forcing the encoding.
				// currentCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				// System.out.println("## not num3 ...:"+pVal);
				currentCell.setCellValue("");// ��x�A��ɂ��Ă��炶��Ȃ��ƑʖځH�I
				// currentCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				currentCell.setCellType(Cell.CELL_TYPE_STRING);
				// System.out.println("## not num2 ...:"+pVal);
				currentCell.setCellValue(charsetConv(val));
			}
		} catch (Exception e) {
			System.out.println("# ERROR " + e.toString());
			System.out.println("# Exception @setCellValue pVal:" + val);
			System.exit(0);
		}
		// cell.setCellValue(new Date());
		// cell.setCellValue("a string");
	}

}
