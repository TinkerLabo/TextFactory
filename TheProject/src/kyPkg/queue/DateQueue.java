package kyPkg.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kyPkg.uFile.ListArrayUtil;

// �擪�ɑ}�����āA����ɃV�t�g���Ă����L���[
public class DateQueue {
	private String pattern = "[0-9]{8}";// �{����yyyymmdd�̂悤�Ȃ��Ƃ��w��ł���΂悢�̂���
	private int max = -1;
	private String path = "";
	private LinkedList<String> queue;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// �����ɂ̓L���[�̃T�C�Y�A�L���[�ɓǂݍ��ݑΏۂƂ��鐳�K�\���@
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
	// �t�@�C������ǂݍ��ށs�c���єŁt
	// ------------------------------------------------------------------------
	public void importFromFile(String path) {
		List<String> list = ListArrayUtil.file2List(path);
		if (list != null) {
			this.path = path;// �ǂ߂��Ƃ������ݒ�i�ۑ����Ɏg���j
			this.importFromList(list);
		}
	}

	// ------------------------------------------------------------------------
	// �t�@�C���ɕۑ�����s�c���єŁt
	// ------------------------------------------------------------------------
	public void save() {
		if (!this.path.equals(""))
			exportToFile(this.path);
	}

	public void exportToFile(String path) {
		ListArrayUtil.list2File(path, this.getList());
	}

	// ���ŏ��͉����тōl�������E�E�E���ɗ��p���邱�Ƃ��l���ďc�ł��̗p�E�E�E�Eex�X�P�W���[���[�Ƃ�
	// ------------------------------------------------------------------------
	// �t�@�C������ǂݍ��ށs�����єŁt
	// ------------------------------------------------------------------------
	public void importFromFile_H(String path) {
		String str = kyPkg.uFile.FileUtil.file2String(path);
		if (str != null)
			this.importFromArray(str.trim().split("\t"));
	}

	// ------------------------------------------------------------------------
	// �t�@�C���ɕۑ�����s�����єŁt
	// ------------------------------------------------------------------------
	public void exportToFile_H(String path) {
		ListArrayUtil.string2File(path, this.getString("\t"));
	}

	// ------------------------------------------------------------------------
	// �z�����荞�� ����납��addFirst
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
	// ���X�g����荞�� ����납��addFirst
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
	// �擪�̒l��Ԃ� ������͒l�����݂��Ȃ��̂�null���Ԃ�
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
		// ��ԖڂƓ����Ȃ�}�����Ȃ��I�I
		String current = get1st();
		if (val == null || val.equals(current) || !val.matches(pattern)) {
			System.out.println("error>" + val);
			return;
		}
		// �ǉ��ƍ폜���킹�Ĉ�̓���Ƃ���E�E�E
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

	// �C���X�^���X�̐����ƃG���[�l�i�d���l�j�ɂ��ăe�X�g
	public static void tester01() {
		// �R���X�g���N�^ �i�����ɂ̓L���[�̃T�C�Y���Ƃ�j
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
	// �����T���H�i�T�̎n�܂肩�H�j
	public static boolean isSameWeek(String bef, String aft) {
		int week1 = kyPkg.uDateTime.DateCalc.getWeekOfYear(bef);
		int week2 = kyPkg.uDateTime.DateCalc.getWeekOfYear(aft);
		System.out.println("bef>" + bef + " aft>" + aft + " week1>" + week1
				+ " week2>" + week2);
		return (week1 == week2);
	}

	// ���������H�i���̎n�܂肩�H�j
	public static boolean isSameMonth(String bef, String aft) {
		int month1 = kyPkg.uDateTime.DateCalc.getMonth(bef);
		int month2 = kyPkg.uDateTime.DateCalc.getMonth(aft);
		System.out.println("bef>" + bef + " aft>" + aft + " month1>" + month1
				+ " month2>" + month2);
		return (month1 == month2);
	}

	// �t�@�C������ǂݍ���ŁA�X�V���s���A�ۑ�����e�X�g
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
				//�P����Date�@�̑��݂���T�̓��j�������߂ā[�V������ƑO�̏T�̓��j���ɂȂ�E�E�E����
				int dayOfweek = kyPkg.uDateTime.DateCalc.getDayOfWeek(queue.get1st());
				
				// �Ώۊ��Ԃ𐶐�����@���O�̏T�����T�����t
			}
			if (!isSameMonth(queue.get1st(), queue.get2nd())) {
				System.out.println("#Monthly#");
				// �Ώۊ��Ԃ𐶐�����@�����̓��t�̒��O�̏������������t
			}
		}

		// Date date1st = cnvStr2Date(queue.get1st());
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(date1st);
		// int y = calendar.get(Calendar.YEAR); //�N�ް����擾
		// int m = calendar.get(Calendar.MONTH); //���ް����擾
		// int d = calendar.get(Calendar.DATE); //���ް����擾
		// int h = calendar.get(Calendar.HOUR_OF_DAY); //�����ް����擾(24���Ԑ�)
		// int min = calendar.get(Calendar.MINUTE); //���ް����擾
		// int sec = calendar.get(Calendar.SECOND); //�b�ް����擾
		// int msec = calendar.get(Calendar.MILLISECOND); //�ؕb�ް����擾
		// int ampm = calendar.get(Calendar.AM_PM); //�ߑO�^�ߌ�̕ʁB0�^1 �̐��l�ŕԂ�
		// int aph = calendar.get(Calendar.HOUR); //�����ް����擾(12���Ԑ�)
		// int w = calendar.get(Calendar.DAY_OF_WEEK); //1(��)�`7(�y)�̐��l��Ԃ�
		// int w_m = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH); //���̉��x�ڂ̗j����
		// int wm = calendar.get(Calendar.WEEK_OF_MONTH); //���̉��T�ڂ�
		// int wy = calendar.get(Calendar.WEEK_OF_YEAR); //�N�̉��T�ڂ�
		// int dy = calendar.get(Calendar.DAY_OF_YEAR); //�N�̉����ڂ�

		queue.save();
	}

}
