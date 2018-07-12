package kyPkg.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

//-----------------------------------------------------------------------------
//�w�肳�ꂽ�J�����̒l���L�[�Ƀt�@�C�����������ɘA�����o�͂���
//-----------------------------------------------------------------------------
public class TexMerge_H2 {
	private String DOT_ZERO = "0.00";
	private List<Integer> keyCols;
	private int skip = 0;
	private String outPath = "";
	private int maxCol = 0;
	private List<String> pathList;
	private List<String> keyList;// �f�[�^�̕��я����L�^
	private HashMap<String, List<List<String>>> map = null;// (key,Value)
	private HashMap<Integer, Integer> widthMap = null;// �Z���Ɋi�[����Ă��郊�X�g�̃T�C�Y
	private String delimiter = "\t";
	private int width;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public TexMerge_H2(String outPath, List<String> pathList, int keyCol,int skip) {
		//#createTester--------------------------------------------------
//		System.out.println("public static void testTexMerge_H2() {");
//		System.out.println("    String outPath = \"" + outPath + "\";");
//		System.out.println("    List<String> pathList = new ArrayList();");
//		for (String element : pathList) {
//		System.out.println("    pathList.add(\""+element+"\");");
//		}
//		System.out.println("    int keyCol = " + keyCol + ";");
//		System.out.println("    int skip = " + skip + ";");
//		System.out.println("    TexMerge_H2 ins = new TexMerge_H2(outPath,pathList,keyCol,skip);");
//		System.out.println("    ins.execute();");
//		System.out.println("}");
		//--------------------------------------------------
		this.outPath = outPath;
		this.pathList = pathList;
		this.keyCols = getKeyCols(keyCol, 1);
		this.maxCol = pathList.size();
		this.skip = skip;
		this.width = 2;//�t�@�C���������Ă���Ɗ��҂��Ă���J�������i��̃t�@�C�������݂��邽�߁j
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
		keyList = null;
		map = null;
		widthMap = null;
		int current = 0;// ���݉��Z���߁i���V�[�g�߁j��A�����Ă��邩
		for (String path : pathList) {
			file2Map(current, keyCols, path);
			current++;
		}
		map2File(outPath, delimiter, true, false);
		return 0;
	}

	// ----------------------------------------------------------------
	// �t�@�C������n�b�V���}�b�v�Ƀf�[�^���i�[�i�A���j����
	// ----------------------------------------------------------------
	public int file2Map(int current, List<Integer> keyCols, String path) {
//		System.out.println("path:" + path);
		// --------------------------------------------------------------------
		File49ers f49 = new File49ers(path);
		String delimiter = f49.getDelimiter();
		int bef = 0 + skip;
		int maxCol = f49.getMaxColCount();
		// --------------------------------------------------------------------
//		System.out.println("maxCol:" + maxCol);
		if (maxCol <= 0)
			maxCol = width;
		mapit(current, bef, maxCol, path, delimiter);
		return 0;
	}

	private void mapit(int current, int startCol, int endCol, String path,
			String delimiter) {
		StringBuffer keyBuf = new StringBuffer();
		endCol = endCol - 1;
		String[] array = null;
		String key = "";
		List<Integer> colList = paramGen(startCol, endCol);// �J�����w�肪�Ȃ���ΑS�̂��w���p�����[�^�𐶐�����
		// --------------------------------------------------------------------
		if (keyList == null)
			keyList = new ArrayList<String>();
		if (map == null)
			map = new HashMap<String, List<List<String>>>();
		// --------------------------------------------------------------------
		if (widthMap == null)
			widthMap = new HashMap<Integer, Integer>();
		widthMap.put(current, colList.size());// �Z�����Ƃ̕��i���X�g�̃T�C�Y�j���L�^����E�E�_�~�[�������Ɏg�p����
		// --------------------------------------------------------------------
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			String rec = br.readLine();
			while (rec != null) {
				// List<String> curList = new ArrayList<String>();//20150213
				if (rec != null) {
					array = rec.split(delimiter, -1);
				} else {
					array = null;
				}
				// --------------------------------------------------------
				// getKey �ikey����g�ݗ��Ă�j
				// --------------------------------------------------------
				keyBuf.delete(0, keyBuf.length());
				for (Integer keyCol : keyCols) {
					// System.out.println("### keyCol:" + keyCol);
					if (array != null && array.length > keyCol) {
						if (keyBuf.length() > 0)
							keyBuf.append(delimiter);
						keyBuf.append(array[keyCol]);
					}
				}
				key = keyBuf.toString();
				// --------------------------------------------------------
				// System.out.println("### key:" + key);
				// colList�ɂ���J�����݂̂Ńf�[�^����g�ݗ��Ă�
				// --------------------------------------------------------
				List<String> curList = new ArrayList<String>();// 20150213�@
				for (Integer col : colList) {
					if (array != null && array.length > col) {
						curList.add(array[col]);
					} else {
						curList.add(DOT_ZERO);
					}
				}
				List<List<String>> cells = map.get(key);
				if (cells == null)
					cells = new ArrayList<List<String>>();
				while (cells.size() < current)
					cells.add(null);
				// --------------------------------------------------------
				// System.out.println("### cells.size():" + cells.size());
				// --------------------------------------------------------
				if (cells.size() == current)
					cells.add(curList);
				// --------------------------------------------------------
				// �I���W�i���̕��я����L�^����ׂ̃��X�g�i�s�v���H�j
				// --------------------------------------------------------
				if (!keyList.contains(key))
					keyList.add(key);
				map.put(key, cells);
				rec = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// �n�b�V���}�b�v����t�@�C���ɏ����o���i�_�~�[���Ă񂷂�j
	// ------------------------------------------------------------------------
	public void map2File(String outPath, String delimiter, boolean keyOpt,
			boolean seqOpt) {
		if (map == null) {
			System.out.println("#ERROR map==null");
			return;
		}
		long seq = 0;
		EzWriter writer = new EzWriter(outPath);
		writer.open();
		// --------------------------------------------------------
		// �I���W�i���̕��я����L�^�������̂ŁA�ʂɃL�[�̃��X�g��p�ӂ���=>keyList
		// --------------------------------------------------------
		for (String key : keyList) {
			// System.out.println("key:" + key);
			StringBuffer buf = new StringBuffer();
			// ----------------------------------------------------------------
			if (seqOpt) {
				buf.append(String.valueOf(seq++));
				buf.append(delimiter);
			}
			// ----------------------------------------------------------------
			if (keyOpt) {
				buf.append(key);
				buf.append(delimiter);
			}
			List<List<String>> cells = map.get(key);
			// 20150126 �����U�����E�E
			while (cells.size() < this.maxCol)
				cells.add(null);
			int col = 0;
			for (List<String> list : cells) {
				if (list != null) {
					for (String element : list) {
						buf.append(element);
						buf.append(delimiter);
					}
				} else {
					// null�Ȃ瓯���Z���ʒu�̂��̂��Q�Ƃ��ē������̃_�~�[�f�[�^�𐶐�����
					Integer width = widthMap.get(col);
					if (width == null) {

					}
					for (int i = 0; i < width; i++) {
						buf.append(DOT_ZERO);
						buf.append(delimiter);
					}
				}
				col++;
			}
			writer.write(buf.toString());
		}
		writer.close();
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

	//20161005 for debug
	public static void testTexMerge_H2() {
	    String outPath = "C:/@qpr/home/828111000630/calc/#005_MX2_���z�x�[�X.txt";
	    List<String> pathList = new ArrayList();
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N09��28���`2016�N07��31��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N09��28���`10��04��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N10��05���`11��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N10��12���`18��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N10��19���`25��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N10��26���`11��01��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N11��02���`08��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N11��09���`15��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N11��16���`22��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N11��23���`29��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N11��30���`12��06��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N12��07���`13��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N12��14���`20��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N12��21���`27��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2015�N12��28���`2016�N01��03��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N01��04���`10��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N01��11���`17��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N01��18���`24��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N01��25���`31��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N02��01���`07��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N02��08���`14��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N02��15���`21��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N02��22���`28��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N02��29���`03��06��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N03��07���`13��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N03��14���`20��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N03��21���`27��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N03��28���`04��03��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N04��04���`10��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N04��11���`17��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N04��18���`24��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N04��25���`05��01��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N05��02���`08��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N05��09���`15��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N05��16���`22��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N05��23���`29��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N05��30���`06��05��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N06��06���`12��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N06��13���`19��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N06��20���`26��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N06��27���`07��03��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N07��04���`10��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N07��11���`17��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N07��18���`24��_����001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_���z_000_2016�N07��25���`31��_����001.txt");
	    int keyCol = 0;
	    int skip = 1;
	    TexMerge_H2 ins = new TexMerge_H2(outPath,pathList,keyCol,skip);
	    ins.execute();
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		testTexMerge_H2();
	}
}
