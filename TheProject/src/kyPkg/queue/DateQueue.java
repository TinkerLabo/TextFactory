package kyPkg.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kyPkg.uFile.ListArrayUtil;

// 先頭に挿入して、後方にシフトしていくキュー
public class DateQueue {
	private String pattern = "[0-9]{8}";// 本当はyyyymmddのようなことが指定できればよいのだが
	private int max = -1;
	private String path = "";
	private LinkedList<String> queue;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// 引数にはキューのサイズ、キューに読み込み対象とする正規表現　
	// ------------------------------------------------------------------------
	public DateQueue(int max, String pattern) {
		super();
		queue = new LinkedList<String>();
		this.max = max;
		this.pattern = pattern;
	}

	public DateQueue(int max, String pattern, String path) {
		this(max, pattern);
		importFromFile(path);
	}

	// ------------------------------------------------------------------------
	// ファイルから読み込む《縦並び版》
	// ------------------------------------------------------------------------
	public void importFromFile(String path) {
		List<String> list = ListArrayUtil.file2List(path);
		if (list != null) {
			this.path = path;// 読めたときだけ設定（保存時に使う）
			this.importFromList(list);
		}
	}

	// ------------------------------------------------------------------------
	// ファイルに保存する《縦並び版》
	// ------------------------------------------------------------------------
	public void save() {
		if (!this.path.equals(""))
			exportToFile(this.path);
	}

	public void exportToFile(String path) {
		ListArrayUtil.list2File(path, this.getList());
	}

	// ※最初は横並びで考えたが・・・他に流用することを考えて縦版を採用・・・・exスケジューラーとか
	// ------------------------------------------------------------------------
	// ファイルから読み込む《横並び版》
	// ------------------------------------------------------------------------
	public void importFromFile_H(String path) {
		String str = kyPkg.uFile.FileUtil.file2String(path);
		if (str != null)
			this.importFromArray(str.trim().split("\t"));
	}

	// ------------------------------------------------------------------------
	// ファイルに保存する《横並び版》
	// ------------------------------------------------------------------------
	public void exportToFile_H(String path) {
		ListArrayUtil.string2File(path, this.getString("\t"));
	}

	// ------------------------------------------------------------------------
	// 配列より取り込む ※後ろからaddFirst
	// ------------------------------------------------------------------------
	public void importFromArray(String[] array) {
		if (array == null)
			return;
		for (int i = array.length - 1; i >= 0; i--) {
			// String debug = array[i];
			// System.out.println("importFromArray>" + debug);
			addFirst(array[i]);
		}
	}

	// ------------------------------------------------------------------------
	// リストより取り込む ※後ろからaddFirst
	// ------------------------------------------------------------------------
	public void importFromList(List<String> list) {
		if (list == null)
			return;
		for (int index = list.size() - 1; index >= 0; index--) {
			// String debug = list.get(index);
			// System.out.println("importFromList>" + debug);
			addFirst(list.get(index));
		}
	}

	public List getList() {
		List<String> list = new ArrayList();
		for (String val : queue) {
			list.add(val);
		}
		return list;
	}

	public String getString(String delimiter) {
		StringBuffer buf = new StringBuffer();
		buf.append(queue.get(0));
		for (int index = 1; index < queue.size(); index++) {
			buf.append(delimiter);
			buf.append(queue.get(index));
		}
		return buf.toString();
	}

	// ------------------------------------------------------------------------
	// 先頭の値を返す ※初回は値が存在しないのでnullが返る
	// ------------------------------------------------------------------------
	public String get1st() {
		if (queue.size() > 0) {
			return queue.getFirst();
		} else
			return null;
	}

	public String get2nd() {
		if (queue.size() > 1) {
			return queue.get(1);
		} else
			return null;
	}

	public String get(int index) {
		if (queue.size() > index) {
			return queue.get(index);
		} else
			return null;
	}

	public void addFirst(String val) {
		// 一番目と同じなら挿入しない！！
		String current = get1st();
		if (val == null || val.equals(current) || !val.matches(pattern)) {
			System.out.println("error>" + val);
			return;
		}
		// 追加と削除合わせて一つの動作とする・・・
		queue.addFirst(val);
		while (queue.size() > this.max) {
			queue.removeLast();
		}
	}

	public void enumulate(String message) {
		System.out.println(message);
		for (String val : queue) {
			System.out.println("enumulate>" + val);
		}
	}

	public static void main(String[] args) {
		// tester01();
		tester02();
	}

	// インスタンスの生成とエラー値（重複値）についてテスト
	public static void tester01() {
		// コンストラクタ （引数にはキューのサイズをとる）
		DateQueue queue = new DateQueue(5, "[0-9]{8}");
		String param = "20120104,20120103,20120102,20120101,20111231,20111230,20111229";
		queue.importFromArray(param.split(","));
		queue.addFirst("2012");
		queue.addFirst("hello");
		queue.addFirst("");
		queue.addFirst(null);
		queue.enumulate("debug--------------------");
		queue.addFirst("20120105");
		queue.enumulate("debug--------------------");
		queue.addFirst("20120105");
		queue.enumulate("debug--------------------");
		String rootDir = globals.ResControl.getQprRootDir();
		queue.exportToFile(rootDir+"BaseDate.txt");
	}

	// private static Date cnvStr2Date(String dateStr) {
	// SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// "yyyyMMdd"
	// ParsePosition pos = new ParsePosition(0);
	// return df.parse(dateStr, pos);
	// }
	// 同じ週か？（週の始まりか？）
	public static boolean isSameWeek(String bef, String aft) {
		int week1 = kyPkg.uDateTime.DateCalc.getWeekOfYear(bef);
		int week2 = kyPkg.uDateTime.DateCalc.getWeekOfYear(aft);
		System.out.println("bef>" + bef + " aft>" + aft + " week1>" + week1
				+ " week2>" + week2);
		return (week1 == week2);
	}

	// 同じ月か？（月の始まりか？）
	public static boolean isSameMonth(String bef, String aft) {
		int month1 = kyPkg.uDateTime.DateCalc.getMonth(bef);
		int month2 = kyPkg.uDateTime.DateCalc.getMonth(aft);
		System.out.println("bef>" + bef + " aft>" + aft + " month1>" + month1
				+ " month2>" + month2);
		return (month1 == month2);
	}

	// ファイルから読み込んで、更新を行い、保存するテスト
	public static void tester02() {
		List<String> testData = new ArrayList();
		testData.add("20111229");
		testData.add("20111230");
		testData.add("20111231");
		testData.add("20120101");
		testData.add("20120102");
		testData.add("20120103");
		testData.add("20120104");
		testData.add("20120105");
		testData.add("20120106");
		testData.add("20120107");
		String rootDir = globals.ResControl.getQprRootDir();
		DateQueue queue = new DateQueue(5, "[0-9]{8}", rootDir+"BaseDate.txt");

		for (String dateStr : testData) {
			queue.addFirst(dateStr); // XXX Today()
			queue.enumulate("debug--------------------");
			if (!isSameWeek(queue.get1st(), queue.get2nd())) {
				System.out.println("#Weekly#");
				//１ｓｔDate　の存在する週の日曜日を求めてー７日すると前の週の日曜日になる・・・かな
				int dayOfweek = kyPkg.uDateTime.DateCalc.getDayOfWeek(queue.get1st());
				
				// 対象期間を生成する　直前の週頭＆週末日付
			}
			if (!isSameMonth(queue.get1st(), queue.get2nd())) {
				System.out.println("#Monthly#");
				// 対象期間を生成する　今日の日付の直前の初月＆月末日付
			}
		}

		// Date date1st = cnvStr2Date(queue.get1st());
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(date1st);
		// int y = calendar.get(Calendar.YEAR); //年ﾃﾞｰﾀを取得
		// int m = calendar.get(Calendar.MONTH); //月ﾃﾞｰﾀを取得
		// int d = calendar.get(Calendar.DATE); //日ﾃﾞｰﾀを取得
		// int h = calendar.get(Calendar.HOUR_OF_DAY); //時間ﾃﾞｰﾀを取得(24時間制)
		// int min = calendar.get(Calendar.MINUTE); //分ﾃﾞｰﾀを取得
		// int sec = calendar.get(Calendar.SECOND); //秒ﾃﾞｰﾀを取得
		// int msec = calendar.get(Calendar.MILLISECOND); //ﾐﾘ秒ﾃﾞｰﾀを取得
		// int ampm = calendar.get(Calendar.AM_PM); //午前／午後の別。0／1 の数値で返す
		// int aph = calendar.get(Calendar.HOUR); //時間ﾃﾞｰﾀを取得(12時間制)
		// int w = calendar.get(Calendar.DAY_OF_WEEK); //1(日)～7(土)の数値を返す
		// int w_m = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH); //月の何度目の曜日か
		// int wm = calendar.get(Calendar.WEEK_OF_MONTH); //月の何週目か
		// int wy = calendar.get(Calendar.WEEK_OF_YEAR); //年の何週目か
		// int dy = calendar.get(Calendar.DAY_OF_YEAR); //年の何日目か

		queue.save();
	}

}
