package kyPkg.uFile;

import java.util.HashMap;
import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.filter.Common_IO;
import kyPkg.filter.EzReader;

public class MapCnv implements Inf_ArrayCnv {
	// -------------------------------------------------------------------------
	// 20170614
	// �p�����[�^�F
	// -------------------------------------------------------------------------
	private HashMap<String, List<String>> map;
	private int[] keyCols;//�ǂ̃J��������̓f�[�^�̃L�[�Ƃ��邩
	private int[] dstCols;//�C���f�b�N�X�ɑΉ������ʒu�̒l���ǂ̃J�����ɏo�͂��邩�i-1�Ȃ�o�͂��Ȃ��j
	private int maxCol = -1;

	private int[] paramCnv(String param) {
		if (param == null)
			param = "0";
		String[] sArray = param.split(",");
		int[] iArray = new int[sArray.length];
		for (int i = 0; i < iArray.length; i++) {
			iArray[i] = Integer.valueOf(sArray[i]);
		}
		return iArray;
	}

	public MapCnv(String dictPath, String keyCol, String dstCol) {
		this(MapUtil.file2Dict(dictPath), keyCol, dstCol);
	}

	public MapCnv(HashMap<String, List<String>> map, String keyCol,
			String dstCol) {
		super();
		this.map = map;
		this.keyCols = paramCnv(keyCol);
		this.dstCols = paramCnv(dstCol);
		for (int i = 0; i < dstCols.length; i++) {
			if (dstCols[i] > maxCol)
				maxCol = dstCols[i];
		}
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] iRec, int stat) {
		if (iRec.length > maxCol)
			maxCol = iRec.length;
		String[] oRec = new String[maxCol];
		for (int i = 0; i < iRec.length; i++) {
			oRec[i] = iRec[i];
		}
		//---------------------------------------------------------------------
		// key Generate
		//---------------------------------------------------------------------
		StringBuffer keyBuf = new StringBuffer();
		keyBuf.delete(0, keyBuf.length());
		for (Integer keyCol : keyCols) {
			//	System.out.println("array.length:" + array.length + " > " + keyCol);
			if (iRec.length > keyCol)
				keyBuf.append(iRec[keyCol]);
		}
		List<String> mapVals = map.get(keyBuf.toString());
		if (mapVals != null) {
			for (int i = 0; i < dstCols.length; i++) {
				Integer dstCol = dstCols[i];
				if (dstCol >= 0) {
					oRec[dstCol] = mapVals.get(i);
				}
			}
		}
		return oRec;
	}


	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		if (args.length == 5) {
			String oPath = args[0];//�o�̓f�[�^
			String iPath = args[1];//���̓f�[�^
			String dictPath = args[2];//�����t�@�C��
			String keyCol = args[3];//�����Əƍ�����L�[�̈ʒu�i���̓f�[�^���̑ΏۃJ�����j
			String dstCol = args[4];//�����̒l���o�͂���J�����ʒu
			Inf_ArrayCnv cnv = new MapCnv(dictPath, keyCol, dstCol);
			new Common_IO(oPath, new EzReader(iPath, cnv)).execute();
		} else {
			System.out.println(
					"Usage .. �o�̓f�[�^ ���̓f�[�^ �����t�@�C�� 2 �ƍ�����L�[�̈ʒu�@�����̒l���o�͂���J�����ʒu");
			test01();
		}
	}

	// -------------------------------------------------------------------------
	// �P�̃e�X�g�@
	// -------------------------------------------------------------------------
	public static void test01() {
		String dictPath = "c:/gabbagabbahey.txt";
		int[] keyCols = null;
		int[] valCols = new int[] { 1, 2, 3, 4, 5, 6 };
		valCols = null;
		HashMap<String, List<String>> map = MapUtil.file2Dict(dictPath, keyCols,
				valCols);
		String iPath = "C:/in20170613.txt";
		String oPath = "C:/out20170613.txt";
		//		int[] dstCols = new int[] { 2 };
		Inf_ArrayCnv cnv = new MapCnv(map, "0", "2,3");
		EzReader reader = new EzReader(iPath, cnv);
		new Common_IO(oPath, reader).execute();
	}

	public static void test02() {
		String dictPath = "c:/gabbagabbahey.txt";
		int[] keyCols = null;
		int[] valCols = new int[] { 1, 2, 3, 4, 5, 6 };
		valCols = null;
		HashMap<String, List<String>> map = MapUtil.file2Dict(dictPath, keyCols,
				valCols);
		String iPath = "C:/in20170613.txt";
		String oPath = "C:/out20170613.txt";
		//		int[] dstCols = new int[] { 2 };
		Inf_ArrayCnv cnv = new MapCnv(map, "0", "2,3");
		EzReader reader = new EzReader(iPath, cnv);
		new Common_IO(oPath, reader).execute();
	}
}
