package kyPkg.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kyPkg.uFile.DosEmu;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

//-----------------------------------------------------------------------------
//�w�肳�ꂽ�t�@�C�����������ɘA���o�͂���
//-----------------------------------------------------------------------------
public class TexMerge_H {
	private static final String DUMMY = "";
	private kyPkg.filter.EzWriter writer;
	private String outPath;
	private String path_L = "";
	private String path_R = "";
	private String delimiter = "\t";// �o�͗p��؂蕶��
	private String delimiter_L;
	private String delimiter_R;
	private List<Integer> list_L = null;
	private List<Integer> list_R = null;

	// ------------------------------------------------------------------------
	// �z�񂩂烊�X�g�ɕϊ�����
	// ------------------------------------------------------------------------
	private List array2List(Integer[] array) {
		return java.util.Arrays.asList(array);
	}

	public void setArray_L(Integer[] array_L) {
		this.list_L = array2List(array_L);
	}

	public void setArray_R(Integer[] array_R) {
		this.list_R = array2List(array_R);
	}

	public void setList_L(List<Integer> list_L) {
		this.list_L = list_L;
	}

	public void setList_R(List<Integer> list_R) {
		this.list_R = list_R;
	}

	private int bef_L = 0;// ��bef,aft��0����n�܂�C���f�b�N�Ŏw�肷��
	private int bef_R = 0;
	private int aft_L = 0;
	private int aft_R = 0;

	public void setBef_L(int bef_L) {
		this.bef_L = bef_L;
	}

	public void setAft_L(int aft_L) {
		this.aft_L = aft_L;
	}

	public void setBef_R(int bef_R) {
		this.bef_R = bef_R;
	}

	public void setAft_R(int aft_R) {
		this.aft_R = aft_R;
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public TexMerge_H(String outPath, String path_L, String path_R) {
		this.outPath = outPath;
		this.path_L = path_L;
		this.path_R = path_R;
	}

	private List<Integer> parse(List<Integer> list, int bef, int aft, int max) {
		int from = 0;
		int to = 0;
		if (list == null) {
			if (aft == 0)
				aft = max;
			if (bef < aft) {
				from = bef;
				to = aft;
			} else {
				from = aft;
				to = bef;
			}
			System.out.println("##  from:" + from + " to:" + to);
			list = new ArrayList();
			for (int i = from; i <= to; i++) {
				// System.out.println("col=>" + i);
				list.add(i);
			}
		}
		return list;
	}

	public int execute() {
		String[] arrays_L = null;
		String[] arrays_R = null;
		// --------------------------------------------------------------------
		// check left
		// --------------------------------------------------------------------
		File49ers f49_L = new File49ers(path_L);
		delimiter_L = f49_L.getDelimiter();
		list_L = parse(list_L, bef_L, aft_L, f49_L.getMaxColCount() - 1);
		// --------------------------------------------------------------------
		// check right
		// --------------------------------------------------------------------
		File49ers f49_R = new File49ers(path_R);
		delimiter_R = f49_R.getDelimiter();
		list_R = parse(list_R, bef_R, aft_R, f49_R.getMaxColCount() - 1);
		// --------------------------------------------------------------------
		StringBuffer buf = new StringBuffer();
		writer = new kyPkg.filter.EzWriter(outPath);
		writer.open();
		System.out.println("path_L=>" + path_L);
		System.out.println("path_R=>" + path_R);
		try {
			BufferedReader br_L = FileUtil.getBufferedReader(path_L);
			BufferedReader br_R = FileUtil.getBufferedReader(path_R);
//			BufferedReader br_L = new BufferedReader(new FileReader(path_L));
//			BufferedReader br_R = new BufferedReader(new FileReader(path_R));
			String rec_L = br_L.readLine();
			String rec_R = br_R.readLine();
			while (rec_L != null || rec_R != null) {
				buf.delete(0, buf.length());
				arrays_L = null;
				arrays_R = null;
				if (rec_L != null)
					arrays_L = rec_L.split(delimiter_L);
				// ------------------------------------------------------------
				// �����̃t�@�C���������o��
				// ------------------------------------------------------------
				for (Integer col : list_L) {
					if (arrays_L != null && arrays_L.length > col) {
						buf.append(arrays_L[col]);
					} else {
						buf.append(DUMMY);
					}
					buf.append(delimiter);
				}
				if (rec_R != null)
					arrays_R = rec_R.split(delimiter_R);
				// ------------------------------------------------------------
				// �E���̃t�@�C���������o��
				// ------------------------------------------------------------
				for (Integer col : list_R) {
					if (arrays_R != null && arrays_R.length > col) {
						buf.append(arrays_R[col]);
					} else {
						buf.append(DUMMY);
					}
					buf.append(delimiter);
				}
				writer.write(buf.toString());
				rec_L = br_L.readLine();
				rec_R = br_R.readLine();
			}
			br_L.close();
			br_R.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.close();
		return 0;
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		test00();
	}

	// ------------------------------------------------------------------------
	// Q �ǂ��炩�̃t�@�C���̃��R�[�h�������Ȃ��ꍇ�ǂ��Ȃ邩�˒����ق��ɍ��킹�ďo�͂����i�_�~�[�Z���o�͂���j
	// ���X�g�i�܂��͔z��j�̎w�肪�D��I�Ɏg�p�����i�J�n�I���J�����̎w��͖��������j
	// ------------------------------------------------------------------------
	// 20141107 �t�@�C������������bind����E�E�E
	// ------------------------------------------------------------------------
	public static void test00() {
		// XXX �A������t�@�C���̕��т��\�[�g���ɂɂȂ�悤�ɂ��Ă����iSEQ��U��j
		String outPath = "C:/@qpr/home/238881000301/calc/Mixed.txt";
		String regex = "C:/@qpr/home/238881000301/calc/#004_sorted_AMT_*.txt";
		int modCol = 1;
		TexMerge_H.modByRegex(outPath, regex, modCol);
	}

	// ------------------------------------------------------------------------
	// �ŏ��̃t�@�C���͂��ׂĂ̍��ڂ��o�͂����E�E
	// regex�@���Y�f�B���N�g���ȉ��̃p�^�[���Ƀ}�b�`����t�@�C���ꗗ��A������
	// modCol�@2�Ԗڂ̃t�@�C���́A���̃J�����ȍ~�̃J������A��
	// ------------------------------------------------------------------------
	public static void modByRegex(String outPath, String regex, int modCol) {
		List<String> list = DosEmu.dir(regex, false);
		Collections.sort(list);

		modWithList(outPath, list, modCol);

		// String path_L = "";
		// String path_R = "";
		// String prevPath = "";
		// for (String element : list) {
		// System.out.println("---------------------------------------------");
		// if (prevPath.equals("")) {
		// prevPath = element;
		// } else {
		// path_L = prevPath;
		// path_R = element;
		// TexMerge_H merger = new TexMerge_H(outPath, path_L, path_R);
		// merger.setBef_R(modCol);
		// merger.execute();
		// prevPath = outPath;
		// }
		// }

	}

	public static void modWithList(String outPath, List<String> list, int skip) {
		// �A������Ώۂ��P�̏ꍇ
		if (list.size() == 1) {
			String inPath = list.get(0);
			FileUtil.fileCopy(outPath, inPath);
		} else {
			String path_L = "";
			String path_R = "";
			String prevPath = "";
			for (String element : list) {
				System.out.println("----------------------------------------");
				if (prevPath.equals("")) {
					prevPath = element;
				} else {
					path_L = prevPath;
					path_R = element;
					TexMerge_H merger = new TexMerge_H(outPath, path_L, path_R);
					merger.setBef_R(skip);
					merger.execute();
					prevPath = outPath;
				}
			}
		}
	}

	// 20141219 �P���ɉ��ɂȂ��邾���ł͂��߂Ȃ̂ŁA����̗��key�ɘA������悤�ɏ���������i�n�b�V���}�b�v���g���Ƃ������Ƃ��낤�ˁE�E�E�j

	public static void test01() {
		// �܂��A�P���ɉ��ɂȂ���
		String outPath = "C:/@qpr/home/238881000301/calc/Mixed.txt";
		String path_L = "C:/@qpr/home/238881000301/calc/#004_sorted_AMT_�N��i�T�΋敪�j.txt";
		String path_R = "C:/@qpr/home/238881000301/calc/#004_sorted_AMT_�w����i��敪�j.txt";
		TexMerge_H merger = new TexMerge_H(outPath, path_L, path_R);
		merger.setBef_L(1);// �����̓ǂݍ��݊J�n�J�����i�O�X�^�[�g�j�A�w�肳��Ă��Ȃ��ꍇ�O�J��������
		merger.setAft_L(1);// �����̓ǂݍ��ݏI���J�����i�O�X�^�[�g�j�A�w�肳��Ă��Ȃ��ꍇ�ŏI�J�����܂�
		merger.setArray_L(new Integer[] { 1, 0 });// �����̓ǂݍ��ݑΏۃJ�����E�E�E������w�肷��ƊJ�n�I���J�����͖��������
		merger.setArray_R(new Integer[] { 1, 0 });// �E���̓ǂݍ��ݑΏۃJ����
		merger.execute();
	}

}
