package kyPkg.external;

import static kyPkg.util.KUtil.enumHashMap;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kyPkg.Sorts.IncoreSort;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.TextFactory;
import kyPkg.filter.Inf_oClosure;
import kyPkg.panelsc.Inf_Context;
import kyPkg.uFile.FileUtil;
import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import globals.ResControl;

//-----------------------------------------------------------------------------
//XXX �_�ː����̃f�[�^��ǂݍ��ޏ������쐬����A�K��̕��ނɏ]���J�e�S���[�R�[�h��t�^����
//-----------------------------------------------------------------------------
public class PoiObj {
	public static final String LF = System.getProperty("line.separator");
	private List<String> sheets = null;
	private HashMap<String, Integer> sheetMap = null;
	// private HSSFWorkbook wkBook;
	private Workbook wkBook;

	private static String delimiter = "\t";
	private String bookPath;

	public String getBookPath() {
		return bookPath;
	}

	public static void main(String[] args) {
		test00();
		// test01();
		// test02();
		// testEx();
	}

	// -------------------------------------------------------------------------
	// �@�၄�@Excel �̃V�[�g��ǂݏo���Ă������e�L�X�g�ɏo�͂���
	// -------------------------------------------------------------------------
	public static void test00() {
		String bookPath1 = "";
		bookPath1 = "C:/result.xlsx";
		PoiObj poiObj1 = new PoiObj(bookPath1);
		String rootDir = globals.ResControl.getQprRootDir();
		System.out.println("rootDir:" + rootDir);
		poiObj1.saveAsCsv(rootDir + "�܂Ƃ�.txt", "result"); // �V�[�g�����X�y�[�X�w��Ȃ��Ԑ擪�̃V�[�g�����Ă���
	}

	// -------------------------------------------------------------------------
	// �@�၄�@Excel �̃V�[�g��̎w���̃f�[�^�����X�g�ɕϊ�����
	// -------------------------------------------------------------------------
	public static void test01() {
		String bookPath1 = "";
		bookPath1 = ResControl.NSEI_DIR
				+ "100616���󂳂�փR�[�v�����ׂi�h�b�e�r�R�[�h/PB100619.xls";
		PoiObj poiObj1 = new PoiObj(bookPath1);
		List chklist = poiObj1.getList("", 4, 0);
		for (Iterator iterator = chklist.iterator(); iterator.hasNext();) {
			String str = (String) iterator.next();
			System.out.println("str:" + str);
		}
	}

	// -------------------------------------------------------------------------
	// �@�၄�@Excel �̃V�[�g��̎w���̃f�[�^���n�b�V���}�b�v�ɕϊ�����
	// -------------------------------------------------------------------------
	public static void test02() {
		System.out.println("Start!");
		String bookPath2 = "";
		bookPath2 = ResControl.NSEI_DIR
				+ "100616���󂳂�փR�[�v�����ׂi�h�b�e�r�R�[�h/�������C���̌n.xls";
		PoiObj poiObj2 = new PoiObj(bookPath2);
		HashMap<String, String> dic = poiObj2.getHmap("�܂Ƃ�", 2, 4, 1);
		poiObj2 = null;
		enumHashMap(dic);
		System.out.println("map.size():" + dic.size());
		System.out.println("Fin!");
	}

	// -------------------------------------------------------------------------
	// �_�ː����f�[�^�t�H�[�}�b�g���J�e�S���[�ݒ� 2010-07-27 yuasa
	// �y�e�������z
	// �_�ː����X�V�f�[�^�i�d���������j�t�@�C��
	// ��
	// �_�ː����̕��ރR�[�h�ꗗ�i�������C���̌n.xls�j��莫�����J�e�S���[�R�[�h�ϊ��p�쐬���遨HashMap dic
	// ���V�[�g���́u�܂Ƃ߁v
	// ��
	// �_�ː����̍X�V�f�[�^�iPB100619.xls�j���قǍ쐬�����������g���ĕҏW���e�L�X�g�t�@�C���ɕۑ�����
	// ��
	// ���C�A�E�g�ϊ��i�p�����[�^�t�@�C���́uc:/kobeSParam.txt�v�j
	// 5,1,13," + FIX_HALF + ", jan
	// D,�^�u
	// 4,1,6," + FIX_HALF + ", category
	// D,�^�u
	// 6,1,25," + FIX_HALF + ", ��Ҳ���
	// D,�^�u
	// 6,1,20,�Œ蒷�S�p, ��������
	// ��
	// ���ʂ��\�[�g�o��
	// -------------------------------------------------------------------------
	public static void testEx() {
		String masterPath = "";
		String tranPath = "";

		masterPath = ResControl.NSEI_DIR
				+ "100616���󂳂�փR�[�v�����ׂi�h�b�e�r�R�[�h/�������C���̌n.xls";
		PoiObj poiMst = new PoiObj(masterPath);
		HashMap<String, String> dic = poiMst.getHmap("�܂Ƃ�", 4, 6, 1);// �ΏۃV�[�g�A�L�[�ʒu�A�l�ʒu�A�ǂݍ��݊J�n�s
		poiMst = null;
		// KUtil.enumHashMap(dic); // for Debug
		// System.out.println("dictionary.size():" + dic.size());

		tranPath = ResControl.NSEI_DIR
				+ "100616���󂳂�փR�[�v�����ׂi�h�b�e�r�R�[�h/PB100619.xls";
		String rootDir = globals.ResControl.getQprRootDir();
		String wkPath = rootDir + "wkKobe.txt";
		PoiObj poiTrn = new PoiObj(tranPath);
		poiTrn.saveAsCsvMod(wkPath, "", 2, 6, dic, 3, null); // �V�[�g�����X�y�[�X�w��Ȃ��Ԑ擪�̃V�[�g�����Ă���

		// XXX substr�t�B���^�[�Ő��`����E�E�E�]���̂��̂��t�H�[�}�b�g�����ق����������낤
		// XXX �\�[�g���ǂ����邩
		// XXX �}�N���~���ɓn���ہA���̐����ƕ����������@�ǂ�����΂������낤
		// XXX �������ޑO�ɖڌ��̕K�v�͂Ȃ����H�I

		// ---------------------------------------------------------------------
		// Format
		// ---------------------------------------------------------------------
		String outPath2 = rootDir + "pre�_��.txt";
		String outPath3 = rootDir + "srt�_��.txt";
		String param2 = rootDir + "kobeSParam.txt";
		// log.info("	---------------------------------------------------");
		// log.info("	Flt_SUBSTR2�ɂ��t�H�[�}�b�g��������");
		TextFactory substr2 = new TextFactory(outPath2, wkPath,
				new kyPkg.converter.SubstrCnv(param2));
		// substr2.setRedersDelimiter("\t");
		substr2.execute();
		new IncoreSort("0,String,asc", new EzWriter(outPath3),
				new EzReader(outPath2)).execute();

		System.out.println("Finish!");

		// 5,1,13," + FIX_HALF + ", jan
		// D,�^�u
		// 4,1,6," + FIX_HALF + ", category
		// D,�^�u
		// 6,1,25," + FIX_HALF + ", ��Ҳ���
		// D,�^�u
		// 6,1,20,�Œ蒷�S�p, ��������

	}

	// --------------------------------------------------------------------------------------------------------
	// 2013-07-17
	// poi2.5�@���@poi3.9�ɕύX�@excel�@2008�ȍ~�̃t�@�C���͂w�e�e�r�`���Łi����ȑO��HSSF�`���j�Ŏ�񂳂�Ȃ���΂Ȃ�Ȃ��悤��
	// �ɏ՗p��WorkbookFactory.create�@�Ƃ����t�@�N�g�����\�b�h������A�C���^�[�t�F�[�X�Ŏ󂯂đ��삷��`�ɏ���������΂悳����
	// �Ƃ肠�������������������̂��C���^�t�F�[�X�ɕς��Ă݂��E�E�E�ǂ��ň��������邩�킩��Ȃ��B
	// ���C�u�����̓���ւ��ɂ����ӂ��K�v�i�T�u�t�H���_�ɂ��܂���jar�������Ă����肷��j
	// �Q�l�ɂ����� http://www.javadrive.jp/poi/install/
	// --------------------------------------------------------------------------------------------------------
	// ���Ɓu�G���R�[�f�B���O�ݒ�(ENCODING_UTF_16)�͕s�v�ɂȂ����v�炵���H�I�̂ŃG���[���o��Y���ӏ����R�����g�A�E�g���Ă݂�
	// org.apache.poi.hssf.usermodel -> HSSFCell -> setEncoding(short encoding)
	// �Q�l�ɂ����� http://d.hatena.ne.jp/papa33/20071001
	// --------------------------------------------------------------------------------------------------------
	// �Ȃ������܂ňȏ�Ƀ��[�W������H���悤�ɂȂ����C������
	// JVM�̃R�}���h���C���p�����^
	// -server -Xms256M -Xmx256M
	// --------------------------------------------------------------------------------------------------------
	// Office Open XML�`���ɑΉ�����POI�̊T�v�Ɗ��\�z
	// http://codezine.jp/article/detail/4783?p=2
	// POIFS for OLE 2 Documents
	// HSSF for Excel Documents
	// HWPF for Word Documents
	// HSLF for PowerPoint Documents
	// HDGF for Visio Documents
	// HPSF for Document Properties
	// --------------------------------------------------------------------------------------------------------

	public PoiObj(String bookPath) {
		super();
		this.bookPath = bookPath;
		// Excel�̃��[�N�u�b�N��ǂݍ��݂܂��B
		System.out.println("bookPath:" + bookPath);
		try {
			// POIFSFileSystem filein = new POIFSFileSystem(new
			// FileInputStream(bookPath));
			// wkBook = new HSSFWorkbook(filein);
			wkBook = WorkbookFactory.create(new FileInputStream(bookPath));
			enumSheets();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���������s���܂���");
		}
	}

	// -------------------------------------------------------------------------
	// �V�[�g���̈ꗗ�����X�g���n�b�V���}�b�v�ɗ��Ƃ�����
	// -------------------------------------------------------------------------
	private void enumSheets() {
		sheets = new ArrayList();
		sheetMap = new HashMap();
		int sheetCount = wkBook.getNumberOfSheets();
		// System.out.println("sheetCount:" + sheetCount);
		for (int i = 0; i < sheetCount; i++) {
			String sheetName = wkBook.getSheetName(i);
			// System.out.println("enum name:" + sheetName);
			sheetMap.put(sheetName, i);
			sheets.add(sheetName);
		}
	}

	private Sheet openTheSheet(String sheetName) {
		if (sheetName.equals("")) {
			sheetName = sheets.get(0);
		} else {
			Integer i = sheetMap.get(sheetName);
			if (i == null) {
				System.out.println("#ERROR �V�[�g'" + sheetName + "'�͑��݂��܂���I�I ");
				// XXX ���Y�V�[�g�����݂��Ȃ�������H�H
				return null;
			}
		}
		// �V�[�g��ǂݍ��݂܂��B

		Sheet sheet = wkBook.getSheet(sheetName);
		return sheet;
	}

	// private HSSFSheet openTheSheet(String sheetName) {
	// if (sheetName.equals("")) {
	// sheetName = sheets.get(0);
	// } else {
	// Integer i = sheetMap.get(sheetName);
	// if (i == null) {
	// System.out.println("#ERROR �V�[�g'" + sheetName + "'�͑��݂��܂���I�I ");
	// // XXX ���Y�V�[�g�����݂��Ȃ�������H�H
	// return null;
	// }
	// }
	// // �V�[�g��ǂݍ��݂܂��B
	// HSSFSheet sheet = wkBook.getSheet(sheetName);
	// return sheet;
	// }

	// -------------------------------------------------------------------------
	// �w�肵���V�[�g�̓��e���������o�͂���
	// -------------------------------------------------------------------------
	public void saveAsCsv(String outPath, String sheetName) {
		try {
			// HSSFSheet sheet = openTheSheet(sheetName);
			Sheet sheet = openTheSheet(sheetName);
			if (sheet == null)
				return;
			int lastRow = sheet.getLastRowNum();
			System.out.println("saveAsCsv@poiObj�ŏI�s:" + lastRow);
			StringBuffer buf = new StringBuffer();
			outPath = FileUtil.normarizeIt(outPath);
			Inf_oClosure writer = new EzWriter(outPath);
			writer.setLF("");
			writer.open();
			for (int index = 0; index <= lastRow; index++) {
				Row row = sheet.getRow(index);
				// HSSFRow row = sheet.getRow(index);
				if (row != null) {
					int lastCell = row.getLastCellNum();
					// System.out.println("lastCell:"+lastCell);
					for (int j = 0; j < lastCell; j++) {
						// HSSFCell cell = row.getCell((short) j);
						Cell cell = row.getCell((short) j);
						String val = getCellValue(cell);
						if (val != null)
							buf.append(val);
						buf.append(delimiter);
					}
					writer.write(buf.toString());
					writer.write(LF);
					// System.out.println("rec:" + buf.toString());
					buf.delete(0, buf.length());
				} else {
					// ��s�̏ꍇnull�ɂȂ�悤���E�E�E
					// System.out.println("row == null??? @index:" + index);
				}
			}
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���������s���܂���");
		}
	}

	// -----------------------------------------------------------------------------
	// saveAsCsvMod�@�i�������Ƃ��ĕۑ�����E�E�E�g���Łj
	// ���e�J�����ʒucol��0����n�܂�
	// outPath �o�͐�p�X
	// sheetName �ΏۃV�[�g��
	// srcCol �����ϊ��ΏۃJ����
	// srcLen ����
	// dic ����
	// dstCol �����ŕϊ��������̂��������ރJ����
	// -----------------------------------------------------------------------------
	public void saveAsCsvMod(String outPath, String sheetName, int srcCol,
			int srcLen, HashMap<String, String> dict, int dstCol,
			Inf_Context context) {
		int lcnt =0;
		Set<String> errorSet = new HashSet();// �d������G���[�hCode Not Found�h���W�񂷂�
		String dstValue = "";
		String dstCode = "";
		try {
			Sheet sheet = openTheSheet(sheetName);
			// HSSFSheet sheet = openTheSheet(sheetName);
			if (sheet == null)
				return;
			int lastRow = sheet.getLastRowNum();
			System.out.println("saveAsCsvMod@poiObj�ŏI�s:" + lastRow);
			StringBuffer buf = new StringBuffer();
			outPath = FileUtil.normarizeIt(outPath);
			Inf_oClosure writer = new EzWriter(outPath);
			writer.setLF("");
			writer.open();
			for (int index = 0; index <= lastRow; index++) {
				// HSSFRow row = sheet.getRow(index);
				Row row = sheet.getRow(index);
				if (row != null) {
					lcnt++;
					int lastCell = row.getLastCellNum();
					System.out.println("lcnt:"+lcnt+" lastCell:"+lastCell);
//					 System.out.println("lastCell:"+lastCell);
					for (int iCol = 0; iCol < lastCell; iCol++) {
						// HSSFCell cell = row.getCell((short) iCol);
						Cell cell = row.getCell((short) iCol);
						String val = getCellValue(cell);
						if(val!=null){
							if (iCol == srcCol) {
								dstValue = "";
								if (val.length() > srcLen) {
									dstCode = val.substring(0, srcLen);
									dstValue = dict.get(dstCode);
									if (dstValue == null) {
										//									context.append("Code Not Found :" + dstCode);
										errorSet.add("Code Not Found :" + dstCode);
										dstValue = "";
									}
								}
							}
						}
						if (iCol == dstCol) {
							val = dstValue;
						}
						if (val != null)
							buf.append(val);
						buf.append(delimiter);
					}
					writer.write(buf.toString());
					writer.write(LF);
					// System.out.println("rec:" + buf.toString());
					buf.delete(0, buf.length());
				} else {
					// ��s�̏ꍇnull�ɂȂ�悤���E�E�E
					// System.out.println("row == null??? @index:" + index);
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���������s���܂���");
		}
		List<String> errorList = new ArrayList(errorSet);
		Collections.sort(errorList);
		for (String element : errorList) {
			context.append(">>" + element);

		}

	}

	// -------------------------------------------------------------------------
	// �w�肵���V�[�g�̎w��J�����̓��e�����X�g�ɂ��ĕԂ�
	// �J�����ʒucol��0����n�܂�
	// -------------------------------------------------------------------------
	public List getList(String sheetName, int col, int offSet) {
		List<String> list = new ArrayList();
		try {
			// HSSFSheet sheet = openTheSheet(sheetName);
			Sheet sheet = openTheSheet(sheetName);
			if (sheet == null)
				return null;
			int lastRow = sheet.getLastRowNum();
			System.out.println("getList@poiObj�ŏI�s:" + lastRow);
			for (int index = offSet; index <= lastRow; index++) {
				// HSSFRow row = sheet.getRow(index);
				Row row = sheet.getRow(index);
				if (row != null) {
					int lastCell = row.getLastCellNum();
					// System.out.println("lastCell:"+lastCell);
					if (col < lastCell) {
						// HSSFCell cell = row.getCell((short) col);
						Cell cell = row.getCell((short) col);
						String val = getCellValue(cell);
						if (val != null)
							list.add(val);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���������s���܂���");
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// �w�肵���V�[�g�̎w��J�����̓��e���n�b�V���}�b�v�ɂ��ĕԂ�
	// ���e�J�����ʒucol��0����n�܂�
	// sheetName �����̑ΏۂƂ���V�[�g
	// keyCol �@�L�[�Ƃ���J�����ʒu
	// valCol�@�@�@�l�Ƃ���J�����̈ʒu
	// offSet�@�@�@�ǂݍ��݊J�n�s�i���s�ڂ���������ΏۂƂ��邩�A���w�肷��j
	// -------------------------------------------------------------------------
	public HashMap<String, String> getHmap(String sheetName, int keyCol,
			int valCol, int offSet) {
		HashMap<String, String> hmap = new HashMap();
		try {
			// HSSFSheet sheet = openTheSheet(sheetName);
			Sheet sheet = openTheSheet(sheetName);
			if (sheet == null)
				return null;
			int lastRow = sheet.getLastRowNum();
			System.out.println("getHmap@poiObj�ŏI�s:" + lastRow);
			for (int index = offSet; index <= lastRow; index++) {
				// HSSFRow row = sheet.getRow(index);
				Row row = sheet.getRow(index);
				if (row != null) {
					int lastCell = row.getLastCellNum();
					if (keyCol <= lastCell && valCol <= lastCell) {
						// HSSFCell keyCell = row.getCell((short) keyCol);
						// HSSFCell valCell = row.getCell((short) valCol);
						Cell keyCell = row.getCell((short) keyCol);
						Cell valCell = row.getCell((short) valCol);
						String keyVal = getCellValue(keyCell);
						String valVal = getCellValue(valCell);
						if (keyVal != null && valVal != null) {
							hmap.put(keyVal, valVal);
							System.out.println(
									" keyVal:" + keyVal + " valVal:" + valVal);
						}
					} else {
						System.out.println("lastCell:" + lastCell);
						System.out.println("keyCol:" + keyCol);
						System.out.println("valCol:" + valCol);
						System.out.println("�p�����[�^�̃����W���Z���̕��ƃ}�b�`���Ȃ��̂ŏ����𒆒f���܂���");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���������s���܂���");
		}
		return hmap;
	}

	// -------------------------------------------------------------------------
	// �Z���̌^��������āA������Ƃ��ĕԂ�
	// -------------------------------------------------------------------------
	private String getCellValue(Cell cell) {
		String rtn = null;
		if (cell != null) {
			int type = cell.getCellType();
			switch (type) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				// �����_�ȉ������݂���ꍇ���l�����Ă������
				double dNum = cell.getNumericCellValue();
				int iNum = (int) dNum;
				if ((dNum - iNum) > 0) {
					rtn = String.valueOf(dNum);
				} else {
					rtn = String.valueOf(iNum);
				}
				break;
			case HSSFCell.CELL_TYPE_STRING:
				String str = cell.getStringCellValue();
				rtn = str;
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				boolean bool = cell.getBooleanCellValue();
				rtn = String.valueOf(bool);
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				break;
			default:
				System.out.println("type??:" + type);
				break;
			}
		}
		return rtn;
	}

	// public static void testRead(String path) {
	// try {
	// // Excel�̃��[�N�u�b�N��ǂݍ��݂܂��B
	// POIFSFileSystem filein = new POIFSFileSystem(new FileInputStream(
	// path));
	// HSSFWorkbook wkBook = new HSSFWorkbook(filein);
	//
	// int sheetCount = wkBook.getNumberOfSheets();
	// System.out.println("sheetCount:" + sheetCount);
	// String sheetName = "";
	// for (int i = 0; i < sheetCount; i++) {
	// sheetName = wkBook.getSheetName(i);
	// System.out.println("enum name:" + sheetName);
	// }
	//
	// // �V�[�g��ǂݍ��݂܂��B
	// HSSFSheet sheet = wkBook.getSheet(sheetName);
	// int lastRow = sheet.getLastRowNum();
	// System.out.println("lastRow:" + lastRow);
	// String delimiter = "@";
	// StringBuffer buf = new StringBuffer();
	// for (int index = 0; index < lastRow; index++) {
	// HSSFRow row = sheet.getRow(index + 1);
	// int lastCell = row.getLastCellNum();
	// // System.out.println("lastCell:"+lastCell);
	// for (int j = 0; j < lastCell; j++) {
	// HSSFCell cell = row.getCell((short) j);
	// if (cell != null) {
	// int type = cell.getCellType();
	// // System.out.println("type:"+type);
	// if (type == 1) {
	// String str = cell.getStringCellValue();
	// buf.append(str);
	// } else {
	// // �����_�ȉ������݂���ꍇ�͂ǂ����邩�H�I
	// int num = cell.getCellNum();
	// buf.append(String.valueOf(num));
	// }
	// } else {
	// // XXX�@�ǂ�ȂƂ�NULL�Ȃ񂾂낤�H
	// buf.append("NULL");
	// }
	// buf.append(delimiter);
	// }
	// System.out.println("rec:" + buf.toString());
	// buf.delete(0, buf.length());
	// }
	//
	// // //3�s�ڂ̒l��ǂݍ��݂܂��B
	// // HSSFRow row = sheet.getRow(2);
	// // //getStringCellValue�ɂĕ������ǂݍ��݂܂��B
	// // HSSFCell cell = row.getCell((short) 1);
	// // String shopName = cell.getStringCellValue();
	// // //getDateCellValue�ɂē��t��ǂݍ��݂܂��B
	// // cell = row.getCell((short) 2);
	// // Date inputDate = cell.getDateCellValue();
	// // //6�s�ڂ���17�s�ڂ̒l��ǂݍ��݂܂��B
	// // int sum = 0; //���v���z��ۑ�����ϐ�
	// // for (int i = 5; i <= 16; i++) {
	// // //getNumericCellValue�ɂĐ�����ǂݍ��݂܂��B
	// // row = sheet.getRow(i);
	// // cell = row.getCell((short) 2);
	// // sum = sum + (int) cell.getNumericCellValue();
	// // }
	// // //Excel����ǂݍ��񂾌��ʂ��o�͂��܂��B
	// // System.out.println(shopName + "�̔N�Ԕ����"
	// // + sum + "�~�ł��B");
	// // System.out.println(new SimpleDateFormat
	// // ("yyyy/MM/dd").format(inputDate) + "����" );
	// } catch (Exception e) {
	// e.printStackTrace();
	// System.out.println("���������s���܂���");
	// }
	// }

}