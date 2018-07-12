package kyPkg.atoms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.converter.DefaultConverter;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.task.Abs_BaseTask;

// ※重要　区分の掛け合わせを作る 2009/07/17
public class Flt_AtomX extends Abs_BaseTask {
	private int destCol = -1;
	private String destTitle = "noName";

	private String[] tag1 = null;

	private String[] tag2 = null;

	private int targetX = -1;

	private int targetY = -1;

	private HashMap<String, String> dict = null;

	private Inf_iClosure reader = null;

	private Inf_oClosure writer = null;

	private Atomics insAtomics = null;

	private String outName;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_AtomX(String outName, String inNamePath, String comm1, String comm2) {
		this.outName = outName;
		this.destTitle = comm1.trim() + "Ｘ" + comm2.trim();
		this.reader = new EzReader(inNamePath + ".txt");
		this.writer = new EzWriter(outName + ".txt", new LocoConverter());
		System.out.println("20121004debug @Flt_AtomX inName:" + inNamePath);
		System.out.println("# checkpoint 004 #20130401@ of Atomics() caller");

		this.insAtomics = new Atomics(inNamePath);
		this.destCol = insAtomics.getLastmapCol() + 1;
		this.targetX = insAtomics.getColSeq(comm1);
		this.targetY = insAtomics.getColSeq(comm2);
		this.tag1 = insAtomics.getTagsByName(comm1);
		this.tag2 = insAtomics.getTagsByName(comm2);
		System.out.println("destCol:" + destCol);
		System.out.println("targetX:" + targetX);
		System.out.println("targetY:" + targetY);
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	public List incore() {
		HashMap<Integer, String> map4Reduce = new HashMap();
		reader.open();
		System.out.println("Current:" + reader.getCurrent());
		while ((reader.readLine()) != null) {
			String[] splited = reader.getSplited();
			if ((splited.length > targetX) && (splited.length > targetY)) {
				String strX = splited[targetX].trim();
				String strY = splited[targetY].trim();
				System.out.println("strX:" + strX);
				System.out.println("strY:" + strY);
				int iX = Integer.parseInt(strX);
				int iY = Integer.parseInt(strY);
				map4Reduce.put((iX * 10000 + iY), (strX + "\t" + strY));
			}
		}
		reader.close();
		return createDict(map4Reduce);
		// return createDict_org(map4Reduce);
	}

	public List createDict(HashMap<Integer, String> map4Reduce) {
		List<Integer> keyList = new ArrayList();
		java.util.Set set = map4Reduce.entrySet(); // 直接iteratorを呼べないので一旦SETを取得する
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			keyList.add((Integer) ent.getKey());
		}
		Collections.sort(keyList);
		dict = new HashMap();
		List<String> tagList = new ArrayList();
		for (int seq = 0; seq < keyList.size(); seq++) {
			String name1 = "?";
			String name2 = "?";
			Integer key = keyList.get(seq);
			int index1 = (key / 10000);
			int index2 = (key % 10000);
			String strKey = String.valueOf(index1) + "\t"
					+ String.valueOf(index2);
			index1--;
			index2--;
			if (index1 < tag1.length)
				name1 = tag1[index1];
			if (index2 < tag2.length)
				name2 = tag2[index2];
			String newTag = name1 + " x " + name2;
			// System.out.println(strKey+" "+index1 + ":" + index2 + "=>" +
			// newTag);
			tagList.add(newTag);// list上での位置とseqが一致するように注意する
			dict.put(strKey, String.valueOf((seq + 1)));

			int debug = Integer.parseInt(dict.get(strKey)) - 1;
			System.out.println(strKey + " debug:" + debug + " name:"
					+ tagList.get(debug));

		}
		System.out.println("list.size():" + tagList.size());
		return tagList;
	}

	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");
		List tagList = incore();
		insAtomics.addTagName(destCol, destTitle, tagList);// メタデータにタグ情報を追加する

		reader.open();
		writer.open();
		while (writer.write(reader))
			;
		reader.close();
		writer.close();
		insAtomics.saveAsATM(outName);

	}

	public class LocoConverter extends DefaultConverter {
		@Override
		public String[] convert(String[] splited, int stat) {
			String newVile = null;
			List<String> list = new ArrayList();
			if (splited == null)
				return null;
			if ((splited.length > targetX) && (splited.length > targetY)) {
				String strX = splited[targetX].trim();
				String strY = splited[targetY].trim();
				String strKey = strX + "\t" + strY;
				newVile = dict.get(strKey);
				// System.out.println("key:" + key + " newVile:" + newVile);
			}
			if (newVile == null)
				newVile = "0";
			for (int i = 0; i <= destCol; i++) {
				if (i == destCol) {
					list.add(newVile);
				} else {
					if (i < splited.length) {
						list.add(splited[i]);
					} else {
						list.add("");
					}
				}
			}
			String[] array = (String[]) list.toArray(new String[list.size()]);
			return array;
		}
//		@Override
//		public String convert2Str(String[] splited, int stat) {
//			splited = convert(splited, stat);
//			return join(splited, delimiter);
//		};
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1("aカンロ／ポケット菓子(2008年1月1日から6月30日)", "shop", "区分３");
		test1("bカンロ／ポケット菓子(2009年1月1日から6月30日)", "shop", "区分３");
		test1("aカンロ／キャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ(2008年1月1日から6月30日)", "shop", "区分２");
		test1("bカンロ／キャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ(2009年1月1日から6月30日)", "shop", "区分２");

		// test1("test01","shop","区分３");
		// test1("大店08カンロキャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ","shop","区分３");
		// test1("aカンロ／ポケット菓子(2008年1月1日ー6月30日)","shop","区分３");
		// test1("bカンロ／ポケット菓子(2009年1月1日ー6月30日)","shop","区分３");
		// test1("aカンロ／キャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ(2008年1月1日ー6月30日)","shop","区分２");
		// test1("bカンロ／キャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ(2009年1月1日ー6月30日)","shop","区分２");
	}

	public static void test1(String name, String target1, String target2) {
		// String inName = "大店08カンロキャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ";
		// String target1 = "shop";
		// String target2 = "区分３";
		String atxDir = ResControl.D_DRV + "resources/atom/";
		String inName = atxDir + name;
		String outName = atxDir + name + "Mod";
		Flt_AtomX catFormater = new Flt_AtomX(outName, inName, target1, target2);// 002キログラム
		catFormater.execute();
	}

}
