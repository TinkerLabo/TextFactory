package kyPkg.etc;

import java.awt.Component;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.CellStyle;
import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.external.PoiMods;
import kyPkg.filter.EzReader;
import kyPkg.task.LogControl;

//Dimennsion�W�v�̌��ʂ�Excel�o�͂���
public class Poi4MashUps {
	public static final String MAXSIZE_MSG = "�t�@�C���T�C�Y���傫�����ߑ��x�̒ቺ����у������s���ł̃n���O�A�b�v�������܂�܂��B\n�����𑱍s���܂����H";
	private static final int MAX_SIZE = 2000000;// �Ƃ肠����2M�𒴂���ꍇ�O�̓]�L���ȗ�����
	private boolean omitMode = false;
	private static final String DOT_ZERO = "0.0";
	private static final String ZERO = "0";
	private String[] resource = null; // �����\�[�X
	private PoiMods poiMods;
	private FileInputStream stream;
	private String template = "";
	private String outPath = "";
	private int baseV = 10;
	private int baseH = 1;

	private int getBaseV() {
		return baseV;
	}

	private void resetBase(int x, int y) {
		baseH = x;
		baseV = y;
	}

	private void resetBase(Point point) {
		resetBase(point.x - 1, point.y - 1);
	}

	// -------------------------------------------------------------------------
	// �A�N�Z�b�T
	// -------------------------------------------------------------------------
	public void setResource(String[] resource) {
		this.resource = resource;
		System.out.println("??setResource:" + this.resource.toString());
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Poi4MashUps(String outPath, String template) {
		this.outPath = outPath;
		this.template = template;
		init();
	}

	public void init() {
		if (!(new File(template).isFile())) {
			String wErr = "���`�t�@�C���u" + template + "�v��������܂���ł���";
			JOptionPane.showMessageDialog((Component) null, wErr, "Message...",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		poiMods = new PoiMods();
		stream = poiMods.openTheBook(template); // �u�b�N���J��
		// �p�����[�^�̐������`�F�b�N�������ł���Ă����I
		// �e�V�[�g�ɃR�����g���ڂ���
		// XXX ���j�^�[���擾
		// int mCount = 4126;
		// ���\�[�X�f�[�^�𖄂ߍ���
		// embedResources(resource, mCount);
	}

	public boolean sheetRename(String preName, String newName) {
		return poiMods.sheetRename(preName, newName);
	}

	// -------------------------------------------------------------------------
	// �w�b�_�[�i�\���j����������
	// -------------------------------------------------------------------------
	public void plot2Format(String sheetName, String[] colFormat, int row) {
		poiMods.selectSheet(sheetName); // �V�[�g��I��
		CellStyle cellStyle = poiMods.setCurrentFont("MS �S�V�b�N", 9, true);
		// ---------------------------------------------------------------------
		// format�𕶎���Ƃ���1�s�߂ɖ��ߍ���
		// ---------------------------------------------------------------------
		if (colFormat != null) {
			for (int x = 0; x < colFormat.length; x++) {
				poiMods.setCellValueS(row, x + baseH + 1, colFormat[x],
						cellStyle);
			}
		}
	}

	// -------------------------------------------------------------------------
	// �w�b�_�[�i�\���j����������
	// -------------------------------------------------------------------------
	public void plot2Headers(String sheetName, List<String[]> headList) {
		// ---------------------------------------------------------------------
		poiMods.selectSheet(sheetName); // �V�[�g��I��
		// ---------------------------------------------------------------------
		// �Z���i0,0�j�ɕ\�̍ő�J�����i���j�����Ă����A�}�N�����ŗ��p����
		// ---------------------------------------------------------------------
		String[] header0 = headList.get(0);
		poiMods.setCellValueX(0, 0, String.valueOf(header0.length));
		// ---------------------------------------------------------------------
		// �w�b�_�[
		//TODO �����������񂩔��肵�������Ńv���b�g���邩�H�@20161219
		// ---------------------------------------------------------------------
		int lev = 0;
		CellStyle cellStyle = poiMods.setCurrentFont("MS �S�V�b�N", 9, true);
		for (String[] header : headList) {
			if (header != null) {
				for (int x = 0; x < header.length; x++) {
					poiMods.setCellValueS(baseV - lev, x + baseH + 1, header[x],
							cellStyle);
				}
			}
			lev++;
		}
	}

	public void plot2SideHeaders(String sheetName, String[] sideArray) {
		poiMods.selectSheet(sheetName); // �V�[�g��I��
		CellStyle wrapStyle2 = poiMods.setCurrentFont("MS �S�V�b�N", 9, true);
		// ---------------------------------------------------------------------
		// �\��
		// ---------------------------------------------------------------------
		if (sideArray != null) {
			for (int y = 0; y < sideArray.length; y++) {
				poiMods.setCellValueS(y + baseV + 1, baseH, sideArray[y],
						wrapStyle2);
			}
		}
	}

	// ---------------------------------------------------------------------
	// �V�[�g�Ƀ^�O�𖄂ߍ���ł����A���^�f�[�^�ɃL�[�����݂����炻�̒l����������
	// ---------------------------------------------------------------------
	public void surveyAndEmbed(String sheetName, HashMap<String, String> meta,
			int depth) {
		if (meta == null || meta.size() == 0)
			return;
		poiMods.selectSheet(sheetName); // �V�[�g��I��
		for (int row = 0; row <= 10; row++) {
			for (int col = 0; col <= 20; col++) {
				String tag = poiMods.getCellStr(row, col);
				// System.out.println("row,col(" + row + "," + col + ")" +
				// "tag=>"
				// + tag);
				if (tag != null && !tag.trim().equals("")) {
					String value = meta.get(tag);
					if (value != null) {
						poiMods.setCellValueX(row, col, tag + "�@" + value);
						//�@poiMods.setCellValueX(row, col + 1, null);//�����ׂ̃J��������ɂł��Ȃ��̂ŁA�}�N���őΉ����Ă���
						//�ƂȂ�̃Z���ɒl�𖄂ߍ������Ƃ���ƁA�w�b�_�[�̃f�U�C���o�����X���Ƃ肸�炢�E�E�̂Œf�O20150820
					}
				}
			}
		}
	}

	public void plotTheHmap(int gradeV, int gradeH, String sheetName,
			HashMap map, List list) {
		poiMods.selectSheet(sheetName); // �V�[�g��I��
		int index = 0;
		int line = 0;
		int row = 0;
		int col = 0;
		String val = "";
		for (index = 0; index < list.size(); index++) {
			String key = (String) list.get(index);
			// �C���f�b�N�X�����l�𒴂�������ς��悤���H
			int x = (line % 4);
			int y = (line / 4) * 5;
			String str = String.valueOf(map.get(key));
			if (str != null && (!str.equals("null"))) {
				row = gradeV + x;
				col = gradeH + y;
				val = key + str;
				poiMods.setCellValueX(row, col, val);
				line++;
			}
		}
	}

	public void strMatrix2Sheet(String sheetName, String[][] matrix) {
		strMatrix2Sheet(0, 0, sheetName, matrix);
	}

	public void intMatrix2Sheet(String sheetName, int[][] matrix) {
		poiMods.selectSheet(sheetName); // �V�[�g��I��
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				poiMods.selectCell(baseV + y + 1, baseH + x + 1); // �Z����I��
				poiMods.setCellValue(matrix[y][x]);
			}
		}
	}

	// xxx
	public void resetPrevStyles() {
		poiMods.resetPrevStyles();
	}

	// ------------------------------------------------------------------------
	// �V�[�g��I��
	// ------------------------------------------------------------------------
	public void plotByMatrix(String sheetName, List<List> matrix) {
		poiMods.selectSheet(sheetName);
		for (int y = 0; y < matrix.size(); y++) {
			List row = (List) matrix.get(y);
			for (int x = 0; x < row.size(); x++) {
				poiMods.setCellValue(baseV + y, baseH + x,
						row.get(x).toString());
			}
		}
	}

	// ------------------------------------------------------------------------
	// �V�[�g��I��
	// ------------------------------------------------------------------------
	public void strMatrix2Sheet(int gradeV, int gradeH, String sheetName,
			String[][] matrix) {
		poiMods.selectSheet(sheetName);
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				poiMods.setCellValue(baseV + y + gradeV, baseH + x + gradeH,
						matrix[y][x]);
			}
		}
	}

	// ------------------------------------------------------------------------
	// �e�L�X�g�t�@�C������V�[�g�Ƀf�[�^��]�L����
	// ------------------------------------------------------------------------
	public void plotFromFile(String sheetName, String inPath,
			String[] headers) {
		poiMods.selectSheet(sheetName);
		// Inf_iClosure reader = new EzReader(inPath);
		EzReader reader = new EzReader(inPath);
		File inFile = new File(inPath);

		if (omitMode == false && inFile.length() > MAX_SIZE) {
			omitMode = true;//20170531
//			if (new Msgbox(null).confirmYN(MAXSIZE_MSG)) {
//				omitMode = true;
//			} else {
//				System.exit(999);
//			}
		} else {
			omitMode = false;
		}

		if (omitMode) {
			System.err.println("#omit Mode# inPath:" + inPath + " size:"
					+ inFile.length());
		}
		// �����Ăނ���ނɁ@���񂴂�����@��������@ �Ԃ񂩂��Ă��܂��΂悳����
		// @@@20140227
		// Inf_StringCnv stringCnv = new ReplaceFirst("\\$", "\t");
		// Inf_StringCnv stringCnv = new ReplaceFirst(CommonVars.SPACING, "\t");
		// reader.setStringCnv(stringCnv);

		if (headers != null) {
			for (int col = 0; col < headers.length; col++) {
				poiMods.setCellValue(baseV + 0, baseH + col, headers[col]);
			}
			baseV = baseV + 1;
		}

		reader.open();
		String[] cells = null;
		int lCnt = -1;
		while (reader.readLine() != null) {
			lCnt++;
			int wStat = reader.getStat();
			if (wStat >= 0) {
				cells = reader.getSplited();
				//				System.out.println("cells.length:"+cells.length);
				for (int x = 0; x < cells.length; x++) {
					if (omitMode) {//�t�@�C���T�C�Y���傫���ꍇ�ɂ͎����I�Ƀ[���̓]�L���ȗ��@20161005
						if (!cells[x].equals(DOT_ZERO)
								&& !cells[x].equals(ZERO)) {
							//							System.out.print(">>" + cells[x]);
							poiMods.setCellValue(baseV + lCnt, baseH + x,
									cells[x]);
						}
					} else {
						poiMods.setCellValue(baseV + lCnt, baseH + x, cells[x]);
					}
				}
			}
		}
		reader.close();
	}

	public void fin() {
		poiMods.saveAs(outPath);
		try {
			stream.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		// test0();
		test20140718();
	}

	public static void test0() {
		String xlsTmp = ResControlWeb.getD_Resources_Templates("QPRBASE02.xls");
		String sys200 = ResControlWeb.getD_Resources_Templates("QPRMASHUP.xls");
		String[] resource = { "�i��,�R�[���Ȃ�", "�Ώۊ���,�Q�O�O�W�N1���P������P�O���P�O��",
				"�V�C,�J�̂��܂�" };
		String[] headArray = { "�r�[��A", "�r�[��B", "�r�[��C", "�r�[��D", "���A��", "���̑�" };
		String[] captionSide = { "0:00 - 0:59", "1:00 - 1:59", "2:00 - 2:59",
				"3:00 - 3:59", "4:00 - 4:59", "5:00 - 5:59" };
		int[][] matrix = { { 1, 2, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5, 6 },
				{ 1, 2, 3, 4, 5, 6 }, { 6, 5, 4, 3, 2, 1 },
				{ 6, 5, 4, 3, 2, 1 }, { 6, 5, 4, 3, 2, 1 } };

		Poi4MashUps poiMatrix = new Poi4MashUps(sys200, xlsTmp);
		poiMatrix.setResource(resource);
		String sheetName = "sheet1";

		List<String[]> headList = new ArrayList();
		headList.add(headArray);
		poiMatrix.plot2Headers(sheetName, headList);
		poiMatrix.plot2SideHeaders(sheetName, captionSide);

		poiMatrix.intMatrix2Sheet(sheetName, matrix);
		poiMatrix.fin();
	}

	private static void test20140718() {
		String template = "c:/LayoutTmp.xlsx";// ���`
		String excelOut = "c:/result.xlsx";// �o�̓t�@�C��
		String bodyPath = "c:/JICFS_JAN_Layout.txt";// ���̓f�[�^�i�p�����[�^�H�I�j
		String sheetName = "text";// �o�͐�V�[�g��
		Poi4MashUps poiMatrix = new Poi4MashUps(excelOut, template);
		// ----------------------------------------------------------------
		// �v���b�g����
		// ----------------------------------------------------------------
		poiMatrix.resetBase(-1, -1);
		poiMatrix.plotFromFile(sheetName, bodyPath, null);
		poiMatrix.fin();
		// ----------------------------------------------------------------
		// �J��
		// ----------------------------------------------------------------
		UserAgent.openWithUA(excelOut);
	}

	public void plotIt(String sheetName, SheetObject sheetObj) {
		// --------------------------------------------------------------------
		HashMap<String, String> meta = sheetObj.getMeta();// �R�����g��
		// --------------------------------------------------------------------
		List<String[]> headList = sheetObj.getHeaderList();// �w�b�_�[���X�g
		// --------------------------------------------------------------------
		String[] colWidth = sheetObj.getColWidth();// �Z���̕�
		// --------------------------------------------------------------------
		String[] colFormat = sheetObj.getColFormat();// �Z���̏���
		// --------------------------------------------------------------------
		String bodyPath = sheetObj.getBodyPath();// BODY�i���ׁj�f�[�^ �i�e�L�X�g�t�@�C���j�ւ̃p�X
		// --------------------------------------------------------------------
		// ���^�f�[�^�̕��я������낦��
		// (�P)���ʂ̍��ڂ�擪�Ɏ����Ă���
		// XXX�@���`�V�[�g��Ƀ^�O��z�u���āA���̃^�O�Ɠ����ʒu�Ƀ��^�f�[�^��\��t����悤�ɏ���������i20140708�j
		// --------------------------------------------------------------------
		// String[] commonHeads = { "�N���C�A���g�F", "�i ��       �F", "�Ώۊ��ԁF", "�Ώۊ���1�F",
		// "�Ώۊ���2�F", "���ԓ����j�^�[���F" };
		// --------------------------------------------------------------------
		String[] commonHeads = { "" };

		HashMap<String, String> infoMapx = new HashMap();
		List keys = new ArrayList();

		for (String element : commonHeads) {
			String val = meta.get(element);
			if (val != null) {
				infoMapx.put(element, val);
				keys.add(element);
				meta.remove(element);// 20140227
			}
		}
		// --------------------------------------------------------------------
		// (�R)�]�v�ȍ��ڂ�����
		// --------------------------------------------------------------------
		meta.remove("befYMD:");
		meta.remove("aftYMD:");
		// --------------------------------------------------------------------
		// (�S)���ʂ̍��ڈȊO�̂��̂�ǉ�
		// --------------------------------------------------------------------
		List<String> keyList = new ArrayList(meta.keySet());
		Collections.sort(keyList);
		for (String element : keyList) {
			String val = meta.get(element);
			infoMapx.put(element, val);
			keys.add(element);
		}

		// System.out.println("@plotIt >>> sheetName:" + sheetName);
		resetPrevStyles();

		// ----------------------------------------------------------------
		// ���^�f�[�^��\��t����
		// ----------------------------------------------------------------
		surveyAndEmbed(sheetName, meta, 10);

		String[] captionSide = { "A", "B", "C", "D", "E", "F" };
		captionSide = null;

		// ----------------------------------------------------------------
		// �w�b�_�[���v���b�g����
		// ----------------------------------------------------------------
		plot2Headers(sheetName, headList);

		// ----------------------------------------------------------------
		// colFormat & colWidth
		// ----------------------------------------------------------------
		plot2Format(sheetName, colFormat, 0);
		plot2Format(sheetName, colWidth, 1);

		// ----------------------------------------------------------------
		// �\�����v���b�g����
		// ----------------------------------------------------------------
		plot2SideHeaders(sheetName, captionSide);
		resetPrevStyles();
		// ----------------------------------------------------------------
		// Body���v���b�g����
		// ----------------------------------------------------------------
		plotFromFile(sheetName, bodyPath, null);
	}

	// ------------------------------------------------------------------------
	// ���`�V�[�g�ɏ����o��
	// ------------------------------------------------------------------------
	public static String write2excel(HashMap<String, SheetObject> sheetMap,
			String excelOut, String tempName) {
		//		ITP_Ctrl itpCtrl = ITP_Ctrl.getInstance();
		String template = ResControl.getQPRHomeQpr(tempName);
		LogControl.info("#@write2excel# Excel�o�� template:" + template);
		Poi4MashUps poiMatrix = new Poi4MashUps(excelOut, template);
		// �V�[�g���Ƃɓ]�L����
		List<String> keyList = new ArrayList(sheetMap.keySet());
		Collections.sort(keyList);
		for (String sheetName : keyList) {
			// System.out.println("#��#��#�� write2excel ��#��#��#" + sheetName);
			SheetObject resultCmp = sheetMap.get(sheetName);
			// ----------------------------------------------------------------
			// plotIt
			// ----------------------------------------------------------------
			poiMatrix.plotIt(sheetName, resultCmp);
		}
		poiMatrix.fin();
		UserAgent.openWithUA(excelOut);
		return excelOut;
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	public static void send2excel(String outPath, String template,
			List<SheetObject> sheetObjs) {
		Poi4MashUps poiObj = new Poi4MashUps(outPath, template);
		for (SheetObject sheetObj : sheetObjs) {
			// --------------------------------------------------------------------
			String sheetName = sheetObj.getSheetName();
			String bodyPath = sheetObj.getBodyPath();
			String[] headers = sheetObj.getHeader();
			HashMap<String, String> meta = sheetObj.getMeta();
			Point point = sheetObj.getPoint();
			// --------------------------------------------------------------------
			// ��ʒu���Z�b�g����i�w�b�_�[�̊J�n�ʒu�j
			// --------------------------------------------------------------------
			if (point == null) {
				poiObj.resetBase(-1, -1);// offset(point)
			} else {
				poiObj.resetBase(point);
			}
			// ----------------------------------------------------------------
			// ���^�f�[�^��\��t����
			// ----------------------------------------------------------------
			poiObj.surveyAndEmbed(sheetName, meta, poiObj.getBaseV() - 1);
			// --------------------------------------------------------------------
			// �t�@�C�����v���b�g����
			// --------------------------------------------------------------------
			poiObj.plotFromFile(sheetName, bodyPath, headers);
		}
		poiObj.fin();
		// --------------------------------------------------------------------
		// �J��
		// --------------------------------------------------------------------
		System.out.println("excelOut debug:" + outPath);
		UserAgent.openWithUA(outPath);
	}

	public static void send2excel_mod(String outPath, String sheetName,
			String bodyPath, String template, String[] header, Point point,
			HashMap<String, String> meta) {
		SheetObject sheetObj = new SheetObject(bodyPath, meta, header);
		Poi4MashUps poiObj = new Poi4MashUps(outPath, template);
		poiObj.plotIt(sheetName, sheetObj);
		poiObj.fin();
		// --------------------------------------------------------------------
		// �J��
		// --------------------------------------------------------------------
		System.out.println("excelOut debug:" + outPath);
		UserAgent.openWithUA(outPath);
	}

}
