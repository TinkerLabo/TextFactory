package kyPkg.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.python.modules.math;

import kyPkg.uFile.ListArrayUtil;

// 集合知計算用モジュール　Collective Intelligence　2011-10-21 yuasa
//CollectiveIntelligence
public class CollectiveIntelligence {
	// 区切り文字をregix用に変換する（エスケープする）
	private static String delimCnv(String delim) {
		if (delim.equals("|")) {
			delim = "\\|";
		}
		return delim;
	}

	public static HashMap<String, String> file2HashMap(String path, String delim) {
		int key = 0;
		int val = 1;
		int maxCol = val;
		delim = delimCnv(delim);
		HashMap<String, String> hmap = new HashMap();
		for (String wRec : ListArrayUtil.file2List(path)) {
			String[] array = wRec.split(delim);
			if (array.length > maxCol) {
				hmap.put(array[key], array[val]);
			}
		}
		return hmap;
	}

	// 未体験のものを推薦する
	public static List<String> getRecomendations(
			HashMap<String, HashMap<String, Float>> prefs, String person) {
		List<String> rankings = new ArrayList();
		List<String> people = new ArrayList(prefs.keySet());
		List<String> myItems = new ArrayList(prefs.get(person).keySet());
		people.remove(person);// 自分を除外する
		double sim = 0;
		HashMap<String, Double> totals = new HashMap();
		HashMap<String, Double> simsum = new HashMap();
		for (String other : people) {
			// otherとpersonの相関を計算する　similarity
			sim = sim_pearson(prefs, person, other);
			System.out.println("other:" + other + "　similarity:" + sim);
			if (sim == 0)
				continue;

			// personが経験済みのものは除外する
			List<String> othersItems = new ArrayList(prefs.get(other).keySet());
			for (String experienced : myItems) {
				othersItems.remove(experienced);
			}

			for (String notExperienced : othersItems) {
				System.out.println("  notExperienced:" + notExperienced
						+ " val:" + prefs.get(other).get(notExperienced));

				Double wScore = totals.get(notExperienced);
				if (wScore == null)
					wScore = new Double(0);
				wScore = wScore + prefs.get(other).get(notExperienced) * sim;
				totals.put(notExperienced, wScore);

				Double wSim = simsum.get(notExperienced);
				if (wSim == null)
					wSim = new Double(0);
				wSim = wSim + sim;
				simsum.put(notExperienced, wSim);
			}
		}
		for (String item : new ArrayList<String>(totals.keySet())) {
			double total = totals.get(item);
			double score = total / simsum.get(item);
			rankings.add("" + score + "\t" + item);
			// System.out.println("" + score + "\t" + item);
		}
		Collections.sort(rankings, Collections.reverseOrder());
		return rankings;

	}

	public static List<String> topMatches(
			HashMap<String, HashMap<String, Float>> prefs, String person, int n) {
		// String person ="Lisa Rose";
		// int n = 3;
		List<String> people = new ArrayList(prefs.keySet());
		people.remove(person);// 自分を除外する
		double score = 0;
		List<String> scores = new ArrayList();
		for (String other : people) {
			System.out.println("other:" + other);
			score = sim_pearson(prefs, person, other);
			scores.add(score + "\t" + other);
		}
		Collections.sort(scores, Collections.reverseOrder());
		// Collections.sort(list, Collections.reverseOrder())
		return scores.subList(0, n);

	}

	// ユークリッド係数（距離を基にした類似性スコア）を返す
	public static double sim_distance(
			HashMap<String, HashMap<String, Float>> prefs, String person1,
			String person2) {
		List<String> list1 = new ArrayList(prefs.get(person1).keySet());
		List<String> list2 = new ArrayList(prefs.get(person2).keySet());
		List<String> si = getAND(list1, list2);
		if (si.size() == 0)
			return 0;
		float sumOfSquares = 0;
		for (String key : si) {
			float f1 = prefs.get(person1).get(key);
			float f2 = prefs.get(person2).get(key);
			System.out.println("@sim_distance element:" + key + " f1:" + f1 + " f2:" + f2);
			// sumOfSquares += Math.sqrt(Math.pow((f1 - f2), 2.0));
			sumOfSquares += (Math.pow((f1 - f2), 2.0));
			System.out.println(" pow:" + Math.pow((f1 - f2), 2.0));
			System.out.println(" sumOfSquares:" + sumOfSquares);
		}
		return 1 / (1 + sumOfSquares);
	}

	// ピアソン相関係数を返す
	public static double sim_pearson(
			HashMap<String, HashMap<String, Float>> prefs, String person1,
			String person2) {
		List<String> list1 = new ArrayList(prefs.get(person1).keySet());
		List<String> list2 = new ArrayList(prefs.get(person2).keySet());
		// 共通の要素に絞り込む
		List<String> si = getAND(list1, list2);
		int n = si.size();
		if (n == 0)
			return 0;
		float sum1 = 0;
		float sum2 = 0;
		float sum1Sq = 0;
		float sum2Sq = 0;
		float pSum = 0;
		for (String key : si) {
			// すべての嗜好を合計する
			sum1 += prefs.get(person1).get(key);
			sum2 += prefs.get(person2).get(key);
			// 平方をを合計する
			sum1Sq += Math.pow((prefs.get(person1).get(key)), 2.0);
			sum2Sq += Math.pow((prefs.get(person2).get(key)), 2.0);
			// 積をを合計する
			pSum += prefs.get(person1).get(key) * prefs.get(person2).get(key);
		}
		// ピアソンによるスコアを計算する
		float num = pSum - (sum1 * sum2 / n);
		double den = math.sqrt((sum1Sq - Math.pow(sum1, 2.0) / n)
				* (sum2Sq - Math.pow(sum2, 2.0) / n));
		if (den == 0)
			return 0;
		double x = num / den;
		return x;
	}

	// -------------------------------------------------------------------------
	// 2つのリストに共通する項目をリストで返す
	// -------------------------------------------------------------------------
	public static List getAND(List<String> list1, List<String> list2) {
		Set<String> set1 = new HashSet();
		Set<String> set2 = new HashSet();
		for (String element : list1) {
			set1.add(element);
		}
		for (String element : list2) {
			if (set1.contains(element)) {
				set2.add(element);
			} else {
				// System.out.println("## not found :" + element);
			}
		}
		List andList = new ArrayList(set2);
		return andList;
	}
//	public static HashMap<String, HashMap<String, Float>> matrix2HashMatrix_f(matrix){
//		HashMap<String, Float> inner = null;
//		HashMap<String, HashMap<String, Float>> prefs = new HashMap();
//
//		
//		return prefs;
//	}

	// -------------------------------------------------------------------------
	// file2HashMatrix_f
	// key,subKey,valueの並びのデータをハッシュマップのマトリックスに変換する（値はfloatに変換）
	// -------------------------------------------------------------------------
	public static HashMap<String, HashMap<String, Float>> file2HashMatrix_f(
			String path, String delim) {
		int id = 0;
		int key = 1;
		int val = 2;
		int maxCol = val;
		delim = delimCnv(delim);
		HashMap<String, Float> inner = null;
		HashMap<String, HashMap<String, Float>> prefs = new HashMap();
		for (String wRec : ListArrayUtil.file2List(path)) {
			String[] array = wRec.split(delim);
			if (array.length > maxCol) {
				inner = prefs.get(array[id]);
				if (inner == null) {
					inner = new HashMap();
					prefs.put(array[id], inner);
				}
				inner.put(array[key], Float.parseFloat(array[val]));
			}
		}
		return prefs;
	}

	public static void main(String[] argv) {
		// testFile2HashMap();
		// test_sim_pearson();
		// test_topMatches();
		// test_getRecomendations();
		testTransformPrefs();
	}

	public static void testTransformPrefs() {
		String path = "C:/CI/critics.txt";
		String delim = "\t";
		HashMap<String, HashMap<String, Float>> prefs = file2HashMatrix_f(path,
				delim);

		HashMap<String, HashMap<String, Float>> result = transformPrefs(prefs);

		debugPrefs(result);

	}

	public static void debugPrefs(HashMap<String, HashMap<String, Float>> prefs) {
		List<String> keylist = new ArrayList(prefs.keySet());
		for (String key : keylist) {
			System.out.println("key:" + key);
			List<String> subkeylist = new ArrayList(prefs.get(key).keySet());
			for (String subkey : subkeylist) {
				System.out.println("     subkey:" + subkey + "  val:"
						+ prefs.get(key).get(subkey));
			}
		}

	}

	public static HashMap<String, HashMap<String, Float>> transformPrefs(
			HashMap<String, HashMap<String, Float>> prefs) {
		HashMap<String, HashMap<String, Float>> result = new HashMap();
		List<String> keylist = new ArrayList(prefs.keySet());
		for (String key : keylist) {
			List<String> subkeylist = new ArrayList(prefs.get(key).keySet());
			for (String subkey : subkeylist) {
				HashMap<String, Float> inner = result.get(subkey);
				if (inner == null) {
					inner = new HashMap();
					result.put(subkey, inner);
				}
				inner.put(key, prefs.get(key).get(subkey));
			}
		}
		return result;
	}

	public static void test_file2HashMatrix_f() {
		String path = "C:/CI/critics.txt";
		String delim = "\t";
		HashMap<String, HashMap<String, Float>> prefs = file2HashMatrix_f(path,delim);
		debugPrefs(prefs);
	}

	public static void test_getRecomendations() {
		String path = "C:/CI/critics.txt";
		String delim = "\t";
		HashMap<String, HashMap<String, Float>> prefs = file2HashMatrix_f(path,delim);

		List<String> rankings = getRecomendations(prefs, "'Toby'");
		for (String element : rankings) {
			System.out.println("ranking:" + element);
		}
	}

	public static void testGetAND() {
		String path = "C:/CI/critics.txt";
		String delim = "\t";
		HashMap<String, HashMap<String, Float>> prefs = file2HashMatrix_f(path,delim);

		List<String> list1 = new ArrayList(prefs.get("'Lisa Rose'").keySet());
		List<String> list2 = new ArrayList(prefs.get("'Gene Seymour'").keySet());

		List<String> andList = getAND(list1, list2);
		for (String element : andList) {
			System.out.println("@testGetAND element:" + element);
		}
	}

	public static void test_sim_pearson() {
		String path = "C:/CI/critics.txt";
		String delim = "\t";
		HashMap<String, HashMap<String, Float>> prefs = file2HashMatrix_f(path,
				delim);
		double rel1 = sim_distance(prefs, "'Lisa Rose'", "'Gene Seymour'");
		System.out.println("rel1:" + rel1);

		double rel2 = sim_pearson(prefs, "'Lisa Rose'", "'Gene Seymour'");
		System.out.println("rel2:" + rel2);
	}

	public static void test_topMatches() {
		String path = "C:/CI/critics.txt";
		String delim = "\t";
		HashMap<String, HashMap<String, Float>> prefs = file2HashMatrix_f(path,delim);

		List<String> scores = topMatches(prefs, "'Toby'", 3);
		for (String element : scores) {
			System.out.println("@test_topMatches element:" + element);
		}
	}

	public static void testFile2HashMap() {
		String path = "C:/CI/movielends/u.item";
		String delim = "|";
		HashMap<String, String> hmap = file2HashMap(path, delim);
		// System.out.println(" hmap.size:" + hmap.size());
		List<String> keylist = new ArrayList(hmap.keySet());
		for (String key : keylist) {
			System.out.println("key:" + key + " val:" + hmap.get(key));
		}
	}
}
