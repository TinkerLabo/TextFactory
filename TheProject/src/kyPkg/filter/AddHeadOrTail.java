package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.task.Abs_BaseTask;

// ------------------------------------------------------------------------
// 2016-05-17 yuasa ファイルの先頭あるいは末尾にレコードを追加する
// ------------------------------------------------------------------------
public class AddHeadOrTail extends Abs_BaseTask {
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private List<String> head;
	private List<String> tail;

	public void setHead(List<String> head) {
		this.head = head;
	}

	public void setTail(List<String> tail) {
		this.tail = tail;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public AddHeadOrTail(String outPath, String inPath) {
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath);
	}

	public AddHeadOrTail(String outPath, String inPath, List<String> head) {
		this(outPath, inPath);
		setHead(head);
	}

	public AddHeadOrTail(String outPath, String inPath, List<String> head,
			List<String> tail) {
		this(outPath, inPath);
		setHead(head);
		setTail(tail);
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("## Start ##");
		reader.open();
		writer.open();
		if (head != null) {
			for (String element : head)
				writer.write(element);
		}
		while (writer.write(reader))
			;
		if (tail != null) {
			for (String element : tail)
				writer.write(element);
		}
		reader.close();
		writer.close();

	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		//---------------------------------------------------------------------
		testAddHeadOrTail02();
		//---------------------------------------------------------------------
		elapse.stop();
	}

	public static void testAddHeadOrTail01() {
		String dir = "C:/samples/AddHeadOrTail/";
		String inPath = dir + "#007_trrSortOut_target.txt";
		String outPath = dir + "#007_trrSortOut_target_res.txt";

		List<String> head = new ArrayList();
		String headTmp = "head\ttarget\t%s\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0";
		for (int i = 0; i < 20; i++) {
			head.add(String.format(headTmp, i));
		}
		List<String> tail = new ArrayList();
		String tailTmp = "tail\ttarget\t%s\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0";
		for (int i = 0; i < 20; i++) {
			tail.add(String.format(tailTmp, i));
		}
		AddHeadOrTail ins = new AddHeadOrTail(outPath, inPath);
		ins.setHead(head);
		ins.setTail(tail);
		ins.execute();
	}

	public static void testAddHeadOrTail02() {
		String dir = "C:/samples/AddHeadOrTail/";
		String inPath = dir + "#007_trrSortOut_target.txt";
		String outPath = dir + "#007_trrSortOut_target_res.txt";
		List<String> head = new ArrayList();
		String headTmp = "head\ttarget\t%s\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0";
		for (int i = 0; i < 20; i++) {
			head.add(String.format(headTmp, i));
		}
		AddHeadOrTail ins = new AddHeadOrTail(outPath, inPath, head);
		ins.execute();
	}

}
