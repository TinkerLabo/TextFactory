package kyPkg.uFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import kyPkg.filter.EzReader;
import kyPkg.filter.Inf_iClosure;
import kyPkg.uCodecs.CodeCnv;
import kyPkg.util.SetObject;

//
public class MatrixUtil {
	private List<List<String>> matrix;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^ �i�����Ȃ��j
	// -------------------------------------------------------------------------
	public MatrixUtil() {
		super();
		matrix = new ArrayList<List<String>>();
	}

	// -------------------------------------------------------------------------
	// split�ɂ��ă���
	// split(regex,limit)
	// split(regex)��split(regex,0)�Ɠ���
	// limit�����ʂ̂������l�̎w��ɂ����
	// �O���w��˖����̋󕶎���͌��ʂɊ܂܂�Ȃ��i�T�v���X�����j
	// ���̒l�����w��˔z��̒��������ȉ��Ɍ��肳���B�i��ԍŌ�Ɏc��̕����񂪂��ׂĊi�[�����j
	// ���̒l�����w��ː����͂����炸�����̋󕶎�����T�v���X����Ȃ��i���̒l�͕��ł���Ή��ł������j
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// �R���X�g���N�^ �i���X�g�ŏ���������ꍇ�j
	// -------------------------------------------------------------------------
	public MatrixUtil(List<String> list, String delimiter) {
		super();
		this.matrix = new ArrayList<List<String>>();
		for (String rec : list) {

			List<String> rowList = Arrays.asList(rec.split(delimiter, -1));
			this.matrix.add(rowList);
		}
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^�i matrix�ŏ���������ꍇ�j
	// -------------------------------------------------------------------------
	public MatrixUtil(List<List<String>> matrix) {
		super();
		this.matrix = matrix;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^ �i�t�@�C����菉��������ꍇ�j
	// -------------------------------------------------------------------------
	public MatrixUtil(String path) {
		super();
		this.matrix = file2StrMatrix(path);
	}

	public List<List<String>> getMatrix() {
		return matrix;
	}

	public void setMatrix(List<List<String>> matrix) {
		this.matrix = matrix;
	}

	public List<String> getRow(Integer index) {
		if (matrix != null && matrix.size() > index)
			return matrix.get(index);
		return null;
	}

	// XXX �O���[�v���Z�Ɏg���Ȃ����낤���H�H
	public Set<Integer> and(Set<Integer> left, Set<Integer> right) {
		Set<Integer> andSet = kyPkg.util.SetObject.iAnd(left, right);
		return andSet;
	}

	public Set<String> andSet(Set<String> left, Set<String> right) {
		Set<String> andSet = kyPkg.util.SetObject.and(left, right);
		return andSet;
	}

	// �O���L�[�ɂ��܂܂�����
	public List<List<String>> dependent(int col, Set<String> rightSet) {
		Set<String> leftSet = unique(col);
		Set<String> andSet = kyPkg.util.SetObject.and(leftSet, rightSet);
		return fullfillMatrix(col, andSet);
	}

	// �O���L�[�Ɋ܂܂�Ȃ�����
	public List<List<String>> inDependent(int col, Set<String> rightSet) {
		Set<String> leftSet = unique(col);
		Set<String> leftOnly = kyPkg.util.SetObject.leftOnly(leftSet, rightSet);
		return fullfillMatrix(col, leftOnly);
	}

	// XXX�@set�����X�g�ɕϊ�����

	// �w�肵���J�����̃��j�[�N��set��Ԃ�
	public Set<String> unique(int col) {
		if (matrix == null)
			return null;
		Set<String> uqSet = new HashSet();
		for (int index = 0; index < matrix.size(); index++) {
			List<String> list = matrix.get(index);
			if (list != null && list.size() > col) {
				String key = list.get(col);
				uqSet.add(key);
			}
		}
		return uqSet;
	}

	// set�ɃL�[���܂܂�Ă���}�g���b�N�X�̃C���f�b�N�X���X�g��Ԃ�
	public List<Integer> matchIndexes(int col, Set<String> uqSet) {
		if (matrix == null)
			return null;
		List<Integer> fullfill = new ArrayList();
		for (int index = 0; index < matrix.size(); index++) {
			List<String> list = matrix.get(index);
			if (list != null && list.size() > col) {
				String key = list.get(col);
				if (uqSet.contains(key)) {
					// System.out.println("key:" + key);
					fullfill.add(index);
				}
			}

		}
		return fullfill;
	}

	// �w�肵��set�Ɠ����l�����}�g���b�N�X�̃C���f�b�N�X��Ԃ�
	public List<List<String>> fullfillMatrix(int col, Set<String> uqSet) {
		return matchIndexes2Matrix(matchIndexes(col, uqSet));
	}

	// �����Ɉ�v�����}�g���b�N�X�̃C���f�b�N�X��Ԃ�
	public List<List<String>> fullfillMatrix(int col, String regex) {
		return matchIndexes2Matrix(matchIndexes(col, regex));
	}

	// �w�肳�ꂽ�C���f�b�N�X�̃}�g���b�N�X��Ԃ�
	public List<List<String>> matchIndexes2Matrix(List<Integer> matchIndexes) {
		List<List<String>> locoMatrix = new ArrayList<List<String>>();
		for (Integer tIndex : matchIndexes) {
			List<String> list = getRow(tIndex);
			locoMatrix.add(list);
		}
		return locoMatrix;
	}

	// �����Ɉ�v�����}�g���b�N�X�̃C���f�b�N�X���X�g��Ԃ�
	public List<Integer> matchIndexes(int col, String regex) {
		// System.out.println("matchIndexes start regex:" + regex);
		if (matrix == null)
			return null;
		List<Integer> fullfill = new ArrayList();
		// System.out.println("matrix.size():" + matrix.size());
		for (int index = 0; index < matrix.size(); index++) {
			List<String> list = matrix.get(index);
			if (list != null && list.size() > col) {
				String key = list.get(col);
				if (key.matches(regex)) {
					// System.out.println("key:" + key+" regex:" +
					// regex+"=>OK!");
					fullfill.add(index);
				} else {
					// System.out.println("key:" + key+" regex:" +
					// regex+"=>NG");
				}
			}
		}
		return fullfill;
	}

	public void add(String rec, String delimiter) {
		List<String> listx = Arrays.asList(rec.split(delimiter, -1));
		matrix.add(listx);
	}

	// XXX �p�����[�^�̃e���v���[�g��p�ӂ��Ă����ā@format���g���đ���ł��Ȃ����낤���H
	// -------------------------------------------------------------------------
	// #�f�o�b�O�p
	// -------------------------------------------------------------------------
	public static void dumpMatrix(List<List<String>> matrix, String comment) {
		System.out.println("<" + comment + ">--------------------");
		StringBuffer buf = new StringBuffer();
		for (List<String> list : matrix) {
			buf.delete(0, buf.length());
			for (String cel : list) {
				buf.append(cel);
				buf.append(",");
			}
			System.out.println("dumpMatrix>" + buf.toString());
		}

	}

	// -------------------------------------------------------------------------
	// �g�p�၄ kyPkg.util.KUtil.cnvMatrixToList(matrix,delimiter);
	// -------------------------------------------------------------------------
	public static List<String> cnvMatrixToList(List<List<Object>> matrix,
			String delimiter, boolean seqOption) {
		int seq = 0;
		List<String> list = new ArrayList();
		StringBuffer buff = new StringBuffer();
		for (List<Object> row : matrix) {
			buff.delete(0, buff.length());
			if (seqOption) {
				seq++;
				buff.append("#" + seq);
				buff.append(delimiter);
			}
			for (Object val : row) {
				if (val != null)
					buff.append(val.toString());
				buff.append(delimiter);
			}
			// �K����؂蕶�����l�ɑ����Ă��邪����Ŗ��Ȃ����ȁE�E�E�E
			list.add(buff.toString());
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// printf �I�ȏo�͎w�肪�ł���ƕ֗��Ƃ������z
	// �i����U������ɕϊ����Ă���̂ŁE�E�E%s�����w��ł��Ȃ��E�E�E��~���Ƃ��ł��Ȃ����ȁj
	// �g�p�၄ kyPkg.util.KUtil.cnvMatrixToFormatedList(matrix,"key:%s %s");
	// -------------------------------------------------------------------------
	public static List<String> cnvMatrixToFormatedList(String format,
			List<List<Object>> matrix) {
		List<String> list = new ArrayList();
		for (List<Object> row : matrix) {
			// List<String> rowStr = new ArrayList();
			// for (Object val : row) {
			// if (val != null) {
			// rowStr.add(val.toString());
			// } else {
			// rowStr.add("");
			// }
			// }
			// Object[] array = (String[]) rowStr
			// .toArray(new String[rowStr.size()]);
			// // Java�����t���o��
			// String rec = String.format(format, array);

			String rec = formatIt(format, row);
			list.add(rec);
		}
		return list;
	}

	// Java�����t���o��
	public static String formatIt(String format, List<Object> list) {
		Object[] array = objList2StrArray(list);
		return String.format(format, array);
	}

	public static Object[] objList2StrArray(List<Object> row) {
		List<String> rowStr = new ArrayList();
		for (Object val : row) {
			if (val != null) {
				rowStr.add(val.toString());
			} else {
				rowStr.add("");
			}
		}
		Object[] array = (String[]) rowStr.toArray(new String[rowStr.size()]);
		return array;
	}

	public static java.util.List<List<String>> file2StrMatrix(String inPath) {
		return file2StrMatrix(inPath, -1);
	}

	public static java.util.List<List<String>> file2StrMatrix(String inPath,
			int limit) {
		if (limit < 0)
			limit = Integer.MAX_VALUE;
		Inf_iClosure reader = new EzReader(inPath);
		String delimiter = null;
		List<List<String>> matrix = new ArrayList();
		int lineCount = 0;
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = null;
		while ((reader.readLine() != null) && (lineCount < limit)) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				splited = reader.getSplited();
				List row = new ArrayList();
				for (int i = 0; i < splited.length; i++) {
					row.add(splited[i]);
				}
				matrix.add(row);
				lineCount++;
			}
		}
		reader.close();
		return matrix;
	}

	public static java.util.List<List> file2ObjMatrix(String inPath) {
		return file2ObjMatrix(inPath, -1);
	}

	private static java.util.List<List> file2ObjMatrix(String inPath,
			int limit) {
		if (limit < 0)
			limit = Integer.MAX_VALUE;
		Inf_iClosure reader = new EzReader(inPath);
		String delimiter = null;
		List<List> matrix = new ArrayList();
		int lineCount = 0;
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = null;
		while ((reader.readLine() != null) && (lineCount < limit)) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				splited = reader.getSplited();
				List row = new ArrayList();
				for (int i = 0; i < splited.length; i++) {
					// ���l�Ȃ�Float�Ƃ��Ċi�[����
					if (CodeCnv.isNumeric(splited[i])) {
						// row.add(Integer.parseInt(splited[i]));
						row.add(Float.parseFloat(splited[i]));
					} else {
						row.add(splited[i]);
					}
				}
				matrix.add(row);
				lineCount++;
			}
		}
		reader.close();
		return matrix;
	}

	public static java.util.Vector<Vector> file2VectorMatrixPlus(String inPath,
			int limit) {
		if (limit < 0)
			limit = Integer.MAX_VALUE;
		Inf_iClosure reader = new EzReader(inPath);
		String delimiter = null;
		Vector<Vector> matrix = new Vector();
		int lineCount = 0;
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = null;
		while ((reader.readLine() != null) && (lineCount < limit)) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				splited = reader.getSplited();
				Vector row = new Vector();
				for (int i = 0; i < splited.length; i++) {
					// ���l�Ȃ�Float�Ƃ��Ċi�[����
					if (CodeCnv.isNumeric(splited[i])) {
						// row.add(Integer.parseInt(splited[i]));
						row.add(Float.parseFloat(splited[i]));
					} else {
						row.add(splited[i]);
					}
				}
				matrix.add(row);
				lineCount++;
			}
		}
		reader.close();
		return matrix;
	}

	public static java.util.Vector<Vector> file2VectorMatrix(String inPath) {
		return file2VectorMatrix(inPath);
	}

	public static java.util.Vector<Vector> file2VectorMatrix(String inPath,
			int limit) {
		if (limit < 0)
			limit = Integer.MAX_VALUE;
		Inf_iClosure reader = new EzReader(inPath);
		String delimiter = null;
		Vector<Vector> matrix = new Vector();
		int lineCount = 0;
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = null;
		while ((reader.readLine() != null) && (lineCount < limit)) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				splited = reader.getSplited();
				Vector row = new Vector();
				for (int i = 0; i < splited.length; i++) {
					row.add(splited[i]);
				}
				matrix.add(row);
				lineCount++;
			}
		}
		reader.close();
		return matrix;
	}

	// �i�O���́j�}�g���b�N�X���t�@�C���o�͂���
	public static int saveAs(String path, List<List> matrix) {
		return saveAs(path, matrix, "\t");
	}

	public static int saveAs(String path, List<List> matrix, String delimiter) {
		return objMatrix2File(path, matrix, delimiter);
	}

	// (�I�u�W�F�N�g������)�}�g���b�N�X���t�@�C���o�͂���
	public int saveAs(String path) {
		return saveAs(path, "\t");
	}

	public int saveAs(String path, String delimiter) {
		return strMatrix2File(path, getMatrix(), delimiter);
	}

	public static int strMatrix2File(String pPath, List<List<String>> matrix,
			String delimiter) {
		return strMatrix2File(pPath, matrix, delimiter, false);
	}

	private static int strMatrix2File(String path, List<List<String>> matrix,
			String delimiter, boolean append) {
		int count = 0;
		FileUtil.makeParents(path); // �e�p�X��������΍��
		StringBuffer buff = new StringBuffer();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, append));
			for (List<String> list : matrix) {
				buff.delete(0, buff.length()); // �o�b�t�@���N���A
				for (String str : list) {
					if (buff.length() > 0)
						buff.append(delimiter);
					buff.append(str);
				}
				buff.append(FileUtil.LF); // ���s����
				String wRec = buff.toString();
				bw.write(wRec, 0, wRec.length());
				count++;
			}
			bw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return count;
	}

	// ex. kyPkg.uFile.FileUtil.matrix2File(pPath, list, "\t");
	public static int objMatrix2File(String pPath, List<List> list,
			String delimiter) {
		return objMatrix2File(pPath, list, delimiter, false);
	}

	private static int objMatrix2File(String path, List<List> matrix,
			String delimiter, boolean append) {
		int count = 0;
		FileUtil.makeParents(path); // �e�p�X��������΍��
		StringBuffer buff = new StringBuffer();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, append));
			for (List<Object> list : matrix) {
				buff.delete(0, buff.length()); // �o�b�t�@���N���A
				for (Object object : list) {
					if (buff.length() > 0)
						buff.append(delimiter);
					buff.append(object.toString());
				}
				buff.append(FileUtil.LF); // ���s����
				String wRec = buff.toString();
				bw.write(wRec, 0, wRec.length());
				count++;
			}
			bw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return count;
	}

	public static List partOfMatrix(List<List> matrix, int pCol,
			int pCheckCnt) {
		return partOfMatrix(matrix, pCol, pCheckCnt, 0);
	}

	public static Vector<Vector> partOfVectorMatrix(Vector<Vector> matrix,
			int pCol, int pCheckCnt, int pSkip) {
		int wMax = -1;
		wMax = 4;
		System.out.println("pCheckCnt:" + pCheckCnt);
		if (matrix == null)
			return null;
		Vector<Vector> subMatrix = new Vector();
		// -----------------------------------------------------------------
		// getMax�ꏄ���Ȃ��ƍő啝���킩��Ȃ��̂��E�E�E
		// -----------------------------------------------------------------
		for (int i = pSkip; i < matrix.size(); i++) {
			List qVec = (List) matrix.get(i);
			if (qVec != null && pCol >= 0 && qVec.size() > pCol) {
				Object obj = qVec.get(pCol);
				if (obj != null) {
					String wRec = obj.toString();
					List<String> list = ListArrayUtil.eachChar2List(wRec);
					if (list != null) {
						Vector rowList = new Vector(list);
						if (rowList != null) {
							if (wMax < rowList.size())
								wMax = rowList.size();
							subMatrix.add(rowList);
							// System.out.println("rVec>>" + rVec);
						}
					} else {
//						System.out.println("#20161014#partOfVectorMatrix list==null ?!");
					}
				}
			}
			if (i > pCheckCnt)
				i = matrix.size();
		}
		// System.out.println("�ő�J����>>"+wMax);
		// -----------------------------------------------------------------
		// Dummy�ǉ��E�E�E
		// -----------------------------------------------------------------
		for (int i = 0; i < subMatrix.size(); i++) {
			int size = ((List) subMatrix.get(i)).size();
			for (int k = size; k < wMax; k++) {
				((List) subMatrix.get(i)).add("");
			}
		}
		return subMatrix;
	}

	public static List partOfMatrix(List<List> matrix, int pCol, int pCheckCnt,
			int pSkip) {
		int wMax = -1;
		wMax = 4;
		System.out.println("pCheckCnt:" + pCheckCnt);
		if (matrix == null)
			return null;
		List subMatrix = new ArrayList();
		// -----------------------------------------------------------------
		// getMax�ꏄ���Ȃ��ƍő啝���킩��Ȃ��̂��E�E�E
		// -----------------------------------------------------------------
		for (int i = pSkip; i < matrix.size(); i++) {
			List qVec = (List) matrix.get(i);
			if (qVec != null && pCol >= 0 && qVec.size() > pCol) {
				Object obj = qVec.get(pCol);
				if (obj != null) {
					String wRec = obj.toString();
					List rowList = ListArrayUtil.eachChar2List(wRec);
					if (rowList != null) {
						if (wMax < rowList.size())
							wMax = rowList.size();
						subMatrix.add(rowList);
						// System.out.println("rVec>>" + rVec);
					}
				}
			}
			if (i > pCheckCnt)
				i = matrix.size();
		}
		// System.out.println("�ő�J����>>"+wMax);
		// -----------------------------------------------------------------
		// Dummy�ǉ��E�E�E
		// -----------------------------------------------------------------
		for (int i = 0; i < subMatrix.size(); i++) {
			int size = ((List) subMatrix.get(i)).size();
			for (int k = size; k < wMax; k++) {
				((List) subMatrix.get(i)).add("");
			}
		}
		return subMatrix;
	}

	// ########################################################################
	// # M a i n
	// ########################################################################
	public static void main(String[] argv) {
		// System.out.println("test start");
		// testTheoryOfSets();
		// System.out.println("test end");
	}

	// -------------------------------------------------------------------------
	// �W�����Z�̃e�X�g
	// -------------------------------------------------------------------------
	public static void testTheoryOfSets() {
		MatrixUtil externalIns = testTrigger();
		Set<String> externalSet = externalIns.unique(1);
		SetObject.dumpSet(externalSet, "�O���L�[�̓��e");

		MatrixUtil regulars = testRegular();
		int col = 2;

		externalIns.saveAs("c:/trigger.txt");
		regulars.saveAs("c:/regular.txt");

		List<List<String>> dependent = regulars.dependent(col, externalSet);
		dumpMatrix(dependent, "�O���L�[�ɂ��܂܂�����");

		List<List<String>> inDependent = regulars.inDependent(col, externalSet);
		dumpMatrix(inDependent, "�O���L�[�Ɋ܂܂�Ȃ�����");
	}

	public static MatrixUtil testTrigger() {
		MatrixUtil matrix = new MatrixUtil();
		matrix.add("20120101,Y,20111101,20120131,", ",");
		matrix.add("20120227,Q,20111201,20120231,", ",");
		matrix.add("20120228,M,20111301,20120331,", ",");
		// ��Ins.add("20120227,W,20111401,20120431,", ",");
		return matrix;
	}

	public static MatrixUtil testRegular() {
		MatrixUtil matrix = new MatrixUtil();
		matrix.add("340018,000010,Y,murakami,Mydata,", ",");
		matrix.add("340018,000110,Q,murakami,Mydata,", ",");
		matrix.add("340018,000310,M,murakami,Mydata,", ",");
		matrix.add("340018,000510,W,murakami,Mydata,", ",");
		matrix.add("840018,000010,Y,suzuki,Mydata,", ",");
		matrix.add("840018,000110,Q,suzuki,Mydata,", ",");
		matrix.add("840018,000310,M,suzuki,Mydata,", ",");
		matrix.add("840018,000510,W,suzuki,Mydata,", ",");
		return matrix;
	}
}