package kyPkg.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

//-----------------------------------------------------------------------------
//�w�肳�ꂽ�J�����̒l���L�[�Ƀt�@�C�����������ɘA�����o�͂���
//	�ΑO�N��o�[�W����	Year_on_year 2015-01-27 yuasa
//-----------------------------------------------------------------------------
/**************************************************************************
 * Year_on_year				
 * @author	ken yuasa
 * @version	1.0
 **************************************************************************/
public class Year_on_year {
	java.text.DecimalFormat exFormat = new java.text.DecimalFormat(
			"000000000.000");
	private static final String DUMMY_VAL = "0.00";
	//	String dummy = "0.00";
	private List<Integer> keyCols;
	private int skip = 0;
	private String outPath = "";
	private List<String> pathListBef;
	private List<String> pathListAft;
	private HashMap<Integer, Integer> widthMap = null;// �Z���Ɋi�[����Ă��郊�X�g�̃T�C�Y
	private String delimiter = "\t";
	private int maxCol = 0;
	boolean optDebug = false;
	private StringBuffer keyBuf = null;
	private Set<String> commonKeys;//���ԂP�A�Q��ʂ��ẮA���ʂ̃L�[�Z�b�g

	private List<Integer> avoidCols; //	�I�v�V�����J�����i�P�O�O�l�����肘���A�����V�F�A�A�w�������j���w�肷��
	private int optType = 1;

	public void setOptType(int optType) {
		this.optType = optType;
	}

	// ------------------------------------------------------------------------
	// 	�A�N�Z�b�T
	// ------------------------------------------------------------------------
	//	�I�v�V�����J�����i�P�O�O�l�����肘���A�����V�F�A�A�w�������j���w�肷��
	// ------------------------------------------------------------------------
	public void setAvoidCols(List<Integer> avoidCols) {
		this.avoidCols = avoidCols;
	}

	// ------------------------------------------------------------------------
	//	�w�b�_�[�����X�L�b�v����Ƃ����Ӗ����H
	// ------------------------------------------------------------------------
	public void setSkip(int skip) {
		//#createTester--------------------------------------------------
		System.out.println("����������������������������������������������������");
		System.out.println("    int skip = " + skip + ";");
		System.out.println("    ins.setSkip(skip);");
		System.out.println("����������������������������������������������������");
		//--------------------------------------------------
		this.skip = skip;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public Year_on_year(String outPath, List<String> pathList, int keyCol,
			boolean optDebug) {
		this(outPath, pathList, getKeyCols(keyCol, 1), optDebug);
	}

	/**************************************************************************
	 * Year_on_year				
	 * @param outPath			 
	 * @param pathList			 
	 * @param keyCols			 
	 * @param optDebug			 
	 **************************************************************************/
	private Year_on_year(String outPath, List<String> pathList,
			List<Integer> keyCols, boolean optDebug) {
		//#createTester--------------------------------------------------
//		System.out.println("public static void testYear_on_year() {");
//		System.out.println("    String outPath = \"" + outPath + "\";");
//		System.out.println("    List<String> pathList = new ArrayList();");
//		for (String element : pathList) {
//		System.out.println("    pathList.add(\""+element+"\");");
//		}
//		System.out.println("    List<Integer> keyCols = new ArrayList();");
//		for (Integer element : keyCols) {
//		System.out.println("    keyCols.add("+element+");");
//		}
//		System.out.println("    boolean optDebug = " + optDebug + ";");
//		System.out.println("    Year_on_year ins = new Year_on_year(outPath,pathList,keyCols,optDebug);");
//		System.out.println("}");
		//--------------------------------------------------

		this.optDebug = optDebug;
		this.outPath = outPath;
		this.pathListBef = new ArrayList<String>();
		this.pathListAft = new ArrayList<String>();
		// �܂��A�p�X���X�g��O��Q�ɐU�蕪����H
		//�ΑO�N�ݒ肳��Ă���ꍇ�A�t�@�C���͕K���O������ꂼ��ɕ�������
		int maxCol = pathList.size() / 2;
		System.out.println("#20150728debug# maxCol�F" + maxCol);
		for (String path : pathList) {
			if (pathListBef.size() < maxCol) {
				System.out.println("#20150730# �yBef�z:" + path);
				pathListBef.add(path);
			} else {
				System.out.println("#20150730# {Aft}:" + path);
				pathListAft.add(path);
			}
		}
		this.keyCols = keyCols;
		//		System.out.println("#20150728debug# keyCols�F" + keyCols);

	}

	private static List<Integer> getKeyCols(int keyFrom, int occ) {
		List<Integer> list = new ArrayList();
		int cnt = 1;
		for (int val = keyFrom; cnt <= occ; val++) {
			list.add(val);
			cnt++;
		}
		return list;
	}

	public int execute() {
		commonKeys = new HashSet();
		widthMap = new HashMap<Integer, Integer>();

		int current = 0;// ���݉��Z���߁i���V�[�g�߁j��A�����Ă��邩
		maxCol = pathListBef.size();
		HashMap<String, List<List<String>>> mapBef = new HashMap();
		for (String path : pathListBef) {
			mapBef = file2Map(mapBef, path, current, keyCols);
			current++;
		}

		current = 0;// ���݉��Z���߁i���V�[�g�߁j��A�����Ă��邩
		maxCol = pathListAft.size();
		HashMap<String, List<List<String>>> mapAft = new HashMap();
		for (String path : pathListAft) {
			mapAft = file2Map(mapAft, path, current, keyCols);
			current++;
		}

		map2File(outPath, mapBef, mapAft, delimiter, true, false);
		return 0;
	}

	// ----------------------------------------------------------------
	// �t�@�C����ǂݍ��݃}�b�v��Ńf�[�^���i�[�i�A���j����
	// ----------------------------------------------------------------
	private HashMap<String, List<List<String>>> file2Map(
			HashMap<String, List<List<String>>> map, String path, int current,
			List<Integer> keyCols) {

		StringBuffer keyBuf = new StringBuffer();
		String key = "";
		String[] recs = null;
		// --------------------------------------------------------------------
		File49ers f49 = new File49ers(path);
		String delimiter_L = f49.getDelimiter();

		// --------------------------------------------------------------------
		//skip?�v������E�E�E
		// --------------------------------------------------------------------
		int xFrom = 0 + skip;
		int xTo = f49.getMaxColCount() - 1;
		List<Integer> colList = paramGen(xFrom, xTo);// �ǂ̃J�������K�v�����w�肷��p�����[�^
		// �J�����w�肪�Ȃ���ΑS�̂��w���p�����[�^�𐶐�����
		//		System.out.println("#20150728debug# From:" + xFrom + " To:" + xTo
		//				+ " path=" + path);
		// --------------------------------------------------------------------
		widthMap.put(current, colList.size());// �Z�����Ƃ̕��i���X�g�̃T�C�Y�j���L�^����E�E�_�~�[�������Ɏg�p����
		//		System.out.println("#20150728debug#  current:" + current
		//				+ " colList.size():" + colList.size());
		// --------------------------------------------------------------------
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			String rec = br.readLine();
			while (rec != null) {
				recs = rec.split(delimiter_L, -1);
				key = getKey(recs);
				//				System.out.println("#20150728debug#  key:" + key);
				// ------------------------------------------------------------
				List<String> curList = new ArrayList();
				for (Integer col : colList) {
					//					System.out.println("#20150728debug#  col:" + col);
					if (recs != null && recs.length > col) {
						curList.add(recs[col]);
					} else {
						curList.add(DUMMY_VAL);
					}
				}
				// ------------------------------------------------------------
				//	�����L�[�̂��̂ɂ��đΏۃJ�����̒l�̃��X�g��A������
				// ------------------------------------------------------------
				List<List<String>> cells = map.get(key);
				if (cells == null) {
					cells = new ArrayList<List<String>>();
					// --------------------------------------------------------
					// �I���W�i���̕��я����L�^����ׂ̃��X�g
					// --------------------------------------------------------
					commonKeys.add(key);
				}
				// ------------------------------------------------------------
				//	���O���Ԃ����݂��Ȃ��ꍇ��null��ǉ����Ă���
				// ------------------------------------------------------------
				while (cells.size() < current)
					cells.add(null);
				if (cells.size() == current)
					cells.add(curList);
				map.put(key, cells);
				// ------------------------------------------------------------
				rec = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	// ------------------------------------------------------------------------
	// key�����𔲂��o��
	// ------------------------------------------------------------------------
	private String getKey(String[] recs) {
		if (keyBuf == null)
			keyBuf = new StringBuffer();
		keyBuf.delete(0, keyBuf.length());
		for (Integer keyCol : keyCols) {
			if (recs != null && recs.length > keyCol) {
				if (keyBuf.length() > 0)
					keyBuf.append(delimiter);
				keyBuf.append(recs[keyCol]);
			}
		}
		return keyBuf.toString();
	}

	// ------------------------------------------------------------------------
	// �n�b�V���}�b�v����t�@�C���ɏ����o���i�_�~�[���Ă񂷂�j
	// ------------------------------------------------------------------------
	public void map2File(String outPath,
			HashMap<String, List<List<String>>> mapBef,
			HashMap<String, List<List<String>>> mapAft, String delimiter,
			boolean keyOpt, boolean seqOpt) {

		if (mapBef == null) {
			System.out.println("#ERROR@map2File mapBef==null");
			return;
		}
		if (mapAft == null) {
			System.out.println("#ERROR@map2File mapAft==null");
			return;
		}
		long seq = 0;
		EzWriter writer = new EzWriter(outPath);
		writer.open();
		for (String key : commonKeys) {
			// System.out.println("key:" + key);
			List<List<String>> cellsBef = mapBef.get(key);
			List<List<String>> cellsAft = mapAft.get(key);

			//20151207 �ǂ��炩�̊��Ԃ����݂��Ȃ��ꍇ���V�~�����[�g���Ă݂�E�E�E�ǂ��Ȃ邩�킩��Ȃ�����
			if (cellsBef == null || cellsAft == null) {
				if (cellsBef == null) {
					cellsBef = new ArrayList<List<String>>();
				}
			}

			if (cellsBef != null && cellsAft != null) {
				StringBuffer buf = new StringBuffer();
				// ----------------------------------------------------------------
				if (seqOpt) {//Seq���o�͂���
					buf.append(String.valueOf(seq++));
					buf.append(delimiter);
				}
				// ----------------------------------------------------------------
				if (keyOpt) {//key�������o�͂���
					buf.append(key);
					buf.append(delimiter);
				}
				// ----------------------------------------------------------------
				// �����U����
				// ----------------------------------------------------------------
				while (cellsBef.size() < this.maxCol)
					cellsBef.add(null);
				while (cellsAft.size() < this.maxCol)
					cellsAft.add(null);
				// ----------------------------------------------------------------
				for (int xCol = 0; xCol < this.maxCol; xCol++) {
					int width = widthMap.get(xCol);
					List<String> listBef = getList(cellsBef, xCol, width);
					List<String> listAft = getList(cellsAft, xCol, width);
					for (int i = 0; i < width; i++) {
						String elementBef = listBef.get(i);
						String elementAft = listAft.get(i);
						String val;
						//						if (xCol == 0 && avoidCols.contains(i)) {
						if (avoidCols != null && avoidCols.contains(i)) { //20160215
							//�I�v�V�����J�����̒l���o��
							//							System.out.println("contain:" + i);
							if (optType == 1) {
								//								buf.append("��"+elementAft+"��");
								buf.append(elementAft);
								buf.append(delimiter);
							}
							val = calcIt(elementBef, elementAft, optDebug);
						} else {
							//���׃J�����̒l���o��
							val = elementAft;
							//	buf.append(delimiter);
							//	val = calcIt(elementBef, elementAft, optDebug);
						}
						buf.append(val);
						buf.append(delimiter);
					}
				}
				String rec = buf.toString();
				// System.out.println("#Year_on_year # rec:" + rec);
				writer.write(rec);
			}
		}
		writer.close();
	}

	//-------------------------------------------------------------------------
	//	�ΑO�N�v�Z
	//-------------------------------------------------------------------------
	private String calcIt(String elementBef, String elementAft,
			boolean optDebug) {
		//		optDebug = true;//20160212
		String result = "";
		String debug = "(100 * " + elementAft + ")/" + elementBef;//  �f�o�b�O�p�ɕ�����ɂ��Ă���
		//		System.out.println(">>>�ΑO�N�v�Z:" + debug);
		if (elementBef.equals("0") || elementAft.equals("0")) {
			result = "0";
		} else {
			// �ΑO�N��P�@(�i���N�|�O�N�j*100)���O�N
			// �ΑO�N��Q�@(���N*100)���O�N
			if (optDebug) {
				result = debug;
			} else {
				try {
					// ���ꂼ��̒l��double�ɂ��Čv�Z����E�E�E
					Double aft = Double.parseDouble(elementAft);
					Double bef = Double.parseDouble(elementBef);
					if (bef > 0) {
						result = exFormat.format((100.0 * aft) / bef);
					} else {
						result = "      - ";//���H�@�ǂ�ȕ��ɕ\��������悢�̂��낤�H�H�i��r���ԂɃf�[�^�����݂��Ȃ������ꍇ�j
					}
				} catch (Exception e) {
					//	System.out.println("#ERROR @�ΑO�N�v�Z=> " + debug);
					//	result = "#ERROR @�ΑO�N�v�Z=> " + debug;
					result = "0";
				}
			}
		}
		return result;
	}

	// ------------------------------------------------------------------------
	// ���Y�Z���ɂ����镔���̃f�[�^���Ȃ���΂O���Ă񂷂�
	// ------------------------------------------------------------------------
	private List<String> getList(List<List<String>> cells, int col, int width) {
		List<String> list = null;
		if (cells.size() > col)
			list = cells.get(col);
		if (list == null)
			list = new ArrayList<String>();
		while (list.size() < width) {
			//	list.add("0");
			list.add(DUMMY_VAL);
		}
		return list;
	}

	// ------------------------------------------------------------------------
	// �J�n�I���p�����[�^����ʒu�p�����[�^�𐶐�����
	// ------------------------------------------------------------------------
	public static List<Integer> paramGen(int bef, int aft) {
		int from = 0;
		int to = 0;
		if (bef < aft) {
			from = bef;
			to = aft;
		} else {
			from = aft;
			to = bef;
		}
		List list = new ArrayList();
		for (int i = from; i <= to; i++) {
			list.add(i);
		}
		return list;
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		testYear_on_year();
	}

	public static void testYear_on_year() {
		String outPath = "C:/@qpr/home/828111099999/calc/#005_MX2_���ʃx�[�X.txt";
		List<String> pathList = new ArrayList();
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N01��01���`2016�N01��31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N01��01���`31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N02��01���`28��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N03��01���`31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N04��01���`30��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N05��01���`31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N06��01���`30��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N07��01���`31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N08��01���`31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N09��01���`30��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N10��01���`31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N11��01���`30��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N12��01���`31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2016�N01��01���`31��_����001.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N01��01���`2016�N01��31��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N01��01���`31��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N02��01���`28��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N03��01���`31��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N04��01���`30��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N05��01���`31��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N06��01���`30��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N07��01���`31��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N08��01���`31��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N09��01���`30��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N10��01���`31��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N11��01���`30��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2015�N12��01���`31��_����002.txt");
		pathList.add(
				"C:/@qpr/home/828111099999/calc/#004_MX1_����_000_2016�N01��01���`31��_����002.txt");
		List<Integer> keyCols = new ArrayList();
		keyCols.add(0);

		boolean optDebug = false;
		Year_on_year ins = new Year_on_year(outPath, pathList, keyCols,
				optDebug);

		int skip = 1;
		ins.setSkip(skip);

		List<Integer> optionCols = new ArrayList();
		optionCols.add(0);
		optionCols.add(1);
		ins.setAvoidCols(optionCols);

		ins.execute();
	}

	public static void testYear_on_year_old() {
		String outPath = "C:/@qpr/home/123620000049/calc/���ʃx�[�X.txt";
		List<String> pathList = new ArrayList();
		pathList.add(
				"C:/@qpr/home/123620000049/calc/#004_MXD_QTY_0002014�N05��01���`30��_001.txt");
		pathList.add(
				"C:/@qpr/home/123620000049/calc/#004_MXD_QTY_0012014�N05��01���`30��_001.txt");
		pathList.add(
				"C:/@qpr/home/123620000049/calc/#004_MXD_QTY_0002015�N05��01���`30��_002.txt");
		pathList.add(
				"C:/@qpr/home/123620000049/calc/#004_MXD_QTY_0012015�N05��01���`30��_002.txt");
		List<Integer> keyCols = new ArrayList();
		keyCols.add(0);

		boolean optDebug = false;
		Year_on_year ins = new Year_on_year(outPath, pathList, keyCols,
				optDebug);
		//		ins.setSkip(2); /???
		ins.execute();
	}

}
