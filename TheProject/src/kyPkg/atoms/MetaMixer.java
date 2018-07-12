package kyPkg.atoms;

import java.util.ArrayList;
import java.util.HashSet;

import kyPkg.filter.MultiMatch;
import kyPkg.uFile.FileUtil;

public class MetaMixer {
	private int[] m_indexs;
	private int[] t_indexs;
	private String m_file;
	private String t_file;
	private Atomics atomics;

	public Atomics getAtomics() {
		return atomics;
	}

	// XXX 同じ名称のMAP項目が存在したとき混乱しないだろうか？？
	// コンストラクタ
	public MetaMixer(String pathM, String pathT) {
		this(pathM, null, null, pathT, null, null);
	}

	public MetaMixer(String pathM, int[] mapM, int[] numM, String pathT,
			int[] mapT, int[] numT) {
		int seq = 0;
		pathM = FileUtil.changeExt(pathM, "txt");
		pathT = FileUtil.changeExt(pathT, "txt");
		this.m_file = pathM;
		this.t_file = pathT;
		Atomics obj_Master = new Atomics(pathM);
		Atomics obj_Tran = new Atomics(pathT);
		// それぞれ何も指定されていなければ全指定
		if (mapM == null)
			mapM = obj_Master.getMapElement();

		if (mapT == null)
			mapT = obj_Tran.getMapElement();

		if (numM == null)
			numM = obj_Master.getNumElement();

		if (numT == null) {
			// 名目が重複する数値項目を発生させない為の処理
			String[] array = obj_Master.getNumTitle();
			if (array != null) {
				HashSet<String> numSet = new HashSet();
				for (int i = 0; i < array.length; i++) {
					numSet.add(array[i]);
				}
				array = obj_Tran.getNumTitle();
				int notContains = 0;
				// 一度含まれていないものをカウントする
				for (int i = 0; i < array.length; i++) {
					if (!numSet.contains(array[i])) {
						notContains++;
					}
				}
				// 含まれていないもののみの配列をつくる
				numT = new int[notContains];
				if (notContains > 0) {
					notContains = 0;
					for (int i = 0; i < array.length; i++) {
						if (!numSet.contains(array[i])) {
							numT[notContains++] = i;
						}
					}
				}
			}
		}
		int numMlen = 0;
		int numTlen = 0;
		int mapMlen = 0;
		int mapTlen = 0;
		if (numM != null)
			numMlen = numM.length;
		if (numT != null)
			numTlen = numT.length;
		if (mapM != null)
			mapMlen = mapM.length;
		if (mapT != null)
			mapTlen = mapT.length;
		this.m_indexs = new int[mapMlen + numMlen];
		this.t_indexs = new int[mapTlen + numTlen];
		seq = 0;
		for (int i = 0; i < mapMlen; i++) {
			m_indexs[seq++] = obj_Master.getMapElement(mapM[i]);
		}
		for (int i = 0; i < numMlen; i++) {
			m_indexs[seq++] = obj_Master.getNumElement(numM[i]);
		}
		seq = 0;
		for (int i = 0; i < mapTlen; i++) {
			t_indexs[seq++] = obj_Tran.getMapElement(mapT[i]);
		}
		for (int i = 0; i < numTlen; i++) {
			t_indexs[seq++] = obj_Tran.getNumElement(numT[i]);
		}
		// タグ名リスト（各Ｍａｐエレメントに対応）
		ArrayList<String[]> tagName = new ArrayList(); // タグ名リスト
		int map_size = mapMlen + mapTlen;
		int[] mapElement = new int[map_size];
		int[] mapOcc = new int[map_size];
		String[] mapTitle = new String[map_size];
		int num_size = numMlen + numTlen;
		int[] numElement = new int[num_size];
		String[] numTitle = new String[num_size];
		String[] numUnit = new String[num_size];
		// ---------------------------------------------------------------------
		// Left
		// ---------------------------------------------------------------------
		// System.out.println("debug@MetaMixer mapM.length="+mapM.length);
		// for (int i = 0; i < mapM.length; i++) {
		// System.out.println("debug@MetaMixer mapM["+i+"]="+mapM[i]);
		// }

		seq = 1;
		for (int i = 0; i < mapMlen; i++) {
			// System.out.println("debug@loop mapM[i]:"+mapM[i]);
			mapElement[i] = seq++; // データ上のセル位置
			// System.out.println("debug@loop mapElement[i]:"+mapElement[i]);
			mapTitle[i] = obj_Master.getMapTitle(i);
			// System.out.println("debug@loop mapTitle[i]:"+mapTitle[i]);
			mapOcc[i] = obj_Master.getMapOcc(i);
			// System.out.println("debug@loop mapOcc[i]:"+mapOcc[i]);
			tagName.add(obj_Master.getTagNa(i));
		}
		for (int i = 0; i < numMlen; i++) {
			numElement[i] = seq++; // データ上のセル位置
			numTitle[i] = obj_Master.getNumTitle(i);
			numUnit[i] = obj_Master.getNumUnit(i);
		}
		// ---------------------------------------------------------------------
		// Right
		// ---------------------------------------------------------------------
		for (int i = 0; i < mapTlen; i++) {
			mapElement[i + mapMlen] = seq++; // データ上のセル位置
			mapTitle[i + mapMlen] = obj_Tran.getMapTitle(i);
			mapOcc[i + mapMlen] = obj_Tran.getMapOcc(i);
			tagName.add(obj_Tran.getTagNa(i));
		}
		for (int i = 0; i < numTlen; i++) {
			numElement[i + numMlen] = seq++; // データ上のセル位置
			numTitle[i + numMlen] = obj_Tran.getNumTitle(i);
			numUnit[i + numMlen] = obj_Tran.getNumUnit(i);
		}
		atomics = new Atomics();
		atomics.setKeyElement(new int[] { 0 });
		atomics.setMapElement(mapElement);
		atomics.setMapOcc(mapOcc);
		atomics.setMapTitle(mapTitle);
		atomics.setNumElement(numElement);
		atomics.setNumTitle(numTitle);
		atomics.setNumUnit(numUnit);
		atomics.setTagName(tagName);
	}

	public String getM_file() {
		return m_file;
	}

	public int[] getM_indexs() {
		return m_indexs;
	}

	public String getT_file() {
		return t_file;
	}

	public int[] getT_indexs() {
		return t_indexs;
	}

	public static void test1() {
		String meta1 = "c:/sample/masterX";
		String meta2 = "c:/sample/tranX";
		String dest = "c:/sample/mixed";
		int[] m_map = { 0, 1 };
		int[] m_num = {};
		int[] t_map = { 0 };
		int[] t_num = { 0, 1, 2 };
		MetaMixer metaMixer = new MetaMixer(meta1, m_map, m_num, meta2, t_map,
				t_num);
		Atomics atomics = metaMixer.getAtomics();
		atomics.saveAsATM(dest);

		// MultiMatch dataMixer = new MultiMatch("full", metaMixer.getM_file(),
		// metaMixer.getM_indexs(), metaMixer.getT_file(), metaMixer
		// .getT_indexs());
	}

	public int saveAsATM(String dest) {
		Atomics atomics = this.getAtomics();
		return atomics.saveAsATM(dest);
	}

	public static void test1019() {
		String meta1 = "./atom/ＱＰＲアンケート/属性・性年代編/2009/A01,A02,A03,A04";
		String meta2 = "./atom/ＱＰＲアンケート/属性・メディア編/2009/q4,q5,q6";
		String dest = "./debug/mixed";
		MetaMixer metaMixer = new MetaMixer(meta1, meta2);
		metaMixer.saveAsATM(dest);
		MultiMatch dataMixer = new MultiMatch("./output", "full",
				metaMixer.getM_file(), metaMixer.getT_file(), true);
		dataMixer.execute();
	}

	public static void test20100826() {
		String personal = globals.ResControl.getPersonalDir();
		String atomDir = globals.ResControl.getAtomDir();
		String meta1 = personal + "current";
		String meta2 = atomDir + "ＱＰＲアンケート/属性・メディア編/2009/q36,q37";
		String dest = "./debug/mixed";
		MetaMixer metaMixer = new MetaMixer(meta1, meta2);
		metaMixer.saveAsATM(dest);
		MultiMatch dataMixer = new MultiMatch("./output", "full",
				metaMixer.getM_file(), metaMixer.getT_file(), true);
		dataMixer.execute();
	}

	public static void main(String[] argv) {
		// test1019();
		test20100826();
	}
}
