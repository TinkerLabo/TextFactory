package kyPkg.etc;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// ------------------------------------------------------------------------
// ��̃V�[�g�ɑΉ�����A�f�[�^�ւ̃p�X�A�w�b�_�[�Ȃǂ̏��R���e�i
// ------------------------------------------------------------------------
public class SheetObject {
	private String sheetName;
	private String bodyPath;
	private HashMap<String, String> meta;
	private List<String[]> headList = null;
	private String[] colFormat = null;
	private String[] colWidth = null;
	private Point point = null;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public SheetObject(String bodyPath, HashMap<String, String> meta) {
		this.headList = new ArrayList();
		this.bodyPath = bodyPath;
		this.meta = (HashMap<String, String>) meta.clone();
	}

	public SheetObject(String bodyPath, HashMap<String, String> meta,
			String[] header) {
		this(bodyPath, meta);
		this.headList.add(header);
	}
	// ------------------------------------------------------------------------
	// �A�N�Z�b�T
	// ------------------------------------------------------------------------
	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void setColFormat(List<String> colFmt) {
		if (colFmt == null)
			return;
		String[] array = (String[]) colFmt.toArray(new String[colFmt.size()]);
		setColFormat(array);
	}

	public void setColFormat(String[] colFmt) {
		if (colFmt == null)
			return;
		this.colFormat = colFmt;
	}

	public String[] getColFormat() {
		return colFormat;
	}

	public void setColWidth(String[] colWid) {
		if (colWid == null)
			return;
		this.colWidth = colWid;
	}

	public void setColWidth(List<String> colWids) {
		if (colWids == null)
			return;
		String[] array = (String[]) colWids.toArray(new String[colWids.size()]);
		setColWidth(array);
	}

	public String[] getColWidth() {
		return colWidth;
	}

	public void addHeader(List<String> list) {
		String[] array = (String[]) list.toArray(new String[list.size()]);
		addHeader(array);
	}

	private void addHeader(String[] header) {
		this.headList.add(header);
	}

	public List<String[]> getHeaderList() {
		return headList;
	}

	public String[] getHeader() {
		String[] header = headList.get(0);
		return header;
	}

	public String getBodyPath() {
		return bodyPath;
	}

	public HashMap<String, String> getMeta() {
		return meta;
	}

	// -----------------------------------------------------------------------------
	// XXX�@�V���A���C�Y���āE�E�E�E���ł��Č��\�ɂł��Ȃ����E�E�E���������������Ȃ����ǂ����l����
	// -----------------------------------------------------------------------------
	public void saveAs(String workDir, String baseType, String seq) {
		// ���쐬�E�E�E
		// �����Ɏg�p������̂����ׂăV���A���C�Y���čė��p�\�Ȍ`�ɏ���������
		// XXX �������A�u���E�U�[������O���b�h�ɓǂݍ���Ŏg�p�ł���悤�ɂ���
		String headerFile = workDir + "header" + baseType + seq + ".txt";
		String metaPath = workDir + "meta" + baseType + ".txt";
		System.out.println("#DEBUG# @saveAs headerFile=>" + headerFile);
		System.out.println("#DEBUG# @saveAs metaPath=>" + metaPath);
		// kyPkg.uFile.ListArrayUtil.array2File(headerFile,
		// headMap.get(SheetObject.ZERO));// header
		kyPkg.uFile.ListArrayUtil.list2File(headerFile,
				Arrays.asList(headList.get(0)));// header
		kyPkg.uFile.HashMapUtil.hashMap2File(metaPath, meta);// metadate
	}

}