package kyPkg.rez;

public class Normalizer {
	public static void main(String[] var) {
		testRemoveInjection();
	}

	public static void testRemoveInjection() {
		String condition = "hello;insert into 'Freak' master;";
		condition = kyPkg.rez.Normalizer.removeInjection(condition);
		System.out.println(condition);
	}

	// XXX 取りあえず作っただけなので・・・あまり検証していません
	// SQLインジェクション対策処理？！
	// ；が入っていたらそれ以降の文字は除去etc
	public static String removeInjection(String condition) {
		// 20140716 trimしない！！区分コードにスペースが含まれている場合に困る（検索できなくなる）
		// condition = condition.trim();
		String[] NGStr = { ";", "'", "\"" };
		int endIndex = 0;
		for (int i = 0; i < NGStr.length; i++) {
			endIndex = condition.indexOf(NGStr[i]);
			if (endIndex > 0) {
				condition = condition.substring(0, endIndex);
			}
		}
		return condition;
	}
}
