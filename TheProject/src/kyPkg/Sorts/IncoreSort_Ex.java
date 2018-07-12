package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.filter.Reader2List;
import kyPkg.task.Abs_BaseTask;

// ----------------------------------------------------------------------------
//　ソートパラメータのカラム指定は0よりはじめる（1ではない！！）
// ----------------------------------------------------------------------------
public class IncoreSort_Ex extends Abs_BaseTask {
	private ArrayComparator multiComparator = null;
	private String delimiter = null;
	private List<Inf_iClosure> readerList;
	private Inf_oClosure writer = null;
	boolean opt1st = false;//１レコード目を退避処理する場合true　（並べ替えの対象にしない）
	//	boolean opt2nd = false;//2レコード目を退避処理する場合true　（並べ替えの対象にしない）
	boolean optLast = false;//最終レコードを退避処理する場合true　（並べ替えの対象にしない）
	boolean optLast2top = false;//最終レコードを退避しておいて先頭に乗せる　（並べ替えの対象にしない）
	boolean optLast2second = false;//最終レコードを退避しておいて先頭に乗せる　（並べ替えの対象にしない）
	private List<String> prefix = null;

	public void setPrefix(List<String> prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(List<String> suffix) {
		this.suffix = suffix;
	}

	private List<String> suffix = null;
	private HashMap<String, Integer> seqMap;//20170525
	public HashMap<String, Integer> getSeqMap() {
		return seqMap;
	}

	public void setOptLast2top(boolean optLast2top) {
		this.optLast2top = optLast2top;
	}

	public void setOptLast2Second(boolean optLast2top) {
		this.optLast2second = optLast2top;
	}

	public void setOpt1st(boolean opt1st) {
		this.opt1st = opt1st;
	}
	//	public void setOpt2nd(boolean opt1st) {
	//		this.opt2nd = opt1st;
	//	}

	public void setOptLast(boolean optLast) {
		this.optLast = optLast;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------

	public IncoreSort_Ex(String inPath) {
		this("0,String,asc", inPath, inPath);
	}

	public IncoreSort_Ex(String sortParam, String ioPath) {
		this(sortParam, ioPath, ioPath);
	}

	public IncoreSort_Ex(String sortParam, String outPath, String inPath) {
		this(sortParam, new EzWriter(outPath), new EzReader(inPath));
	}

	public IncoreSort_Ex(String sortParam, Inf_oClosure writer,
			Inf_iClosure reader) {
		List<Inf_iClosure> readers = new ArrayList();
		readers.add(reader);
		incore(writer, readers, sortParam);
	}

	private void incore(Inf_oClosure writer, List<Inf_iClosure> readerList,
			String sortParam) {
		this.writer = writer;
		this.multiComparator = new ArrayComparator(sortParam);
		this.readerList = readerList;
	}

	public long getWriteCount() {
		return this.writer.getWriteCount();
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("IncoreSort開始");
		// ---------------------------------------------------------------------
		// list in core メモリーオーバーの可能性があるので注意する（対策は先延ばし・・・ummmm）
		// ---------------------------------------------------------------------
		Reader2List preSort = new Reader2List(readerList);
		List<String[]> arrayList = preSort.readers2ArrayList();// 入力データのすべてをリストに読み込む

		String[] line1st = null;
		String[] line2nd = null;
		String[] last = null;
		if (arrayList.size() > 0)
			line1st = arrayList.get(0);//先頭にある合計行を取り出して先頭に付け加える場合
		if (arrayList.size() > 1)
			line2nd = arrayList.get(1);//先頭にある合計行を取り出して先頭に付け加える場合
		last = arrayList.get(arrayList.size() - 1);//Ex:非購入を取り出して最後尾に取り付ける場合

		if (opt1st)
			arrayList.remove(0);//１行め（合計）を取り除いて・・・
		//		if (opt2nd)
		//			arrayList.remove(1);//2行目（非購入）を取り除いて・・・
		//optLast=>最終行（非購入）を取り除いて・・・最終行に追加
		//optLast2top=>最終行（非購入）を取り除いて・・・先頭に追加
		if (optLast || optLast2top || optLast2second) {
			if (arrayList.size() > 0)
				arrayList.remove(arrayList.size() - 1);
		}

		delimiter = preSort.getDelimiter();// 一番最後に読み込んだデータの区切り文字になっている
		// ---------------------------------------------------------------------
		// sort
		// ---------------------------------------------------------------------
		Collections.sort(arrayList, multiComparator);

		if (optLast)
			arrayList.add(last);//退避した最終行（非購入）を最後尾に追加する

		// ---------------------------------------------------------------------
		// output
		// ---------------------------------------------------------------------
		// 重複データの削除をするかどうか？
		// ---------------------------------------------------------------------
		writer.setDelimiter(delimiter);
		writer.open();

		// ---------------------------------------------------------------------
		//	先頭に付け足す行処理	20150917
		// ---------------------------------------------------------------------
		if (prefix != null) {
			for (String element : prefix) {
				writer.write(element);
			}
		}

		if (optLast2top)
			writer.write(last, 1);//退避した最終行（合計）を先頭に追加する

		if (opt1st)
			writer.write(line1st, 1);//退避した先頭行（合計）を先頭に追加する

		if (optLast2second)
			writer.write(last, 1);//退避した最終行（合計）を先頭に追加する

//		EzWriter writer1 = new EzWriter(path);
//		writer1.open();
//		writer1.close();

		seqMap = new HashMap();
		int seq = 0;
		if (arrayList.size() > 0) {
			for (String[] array : arrayList) {
//				System.err.println("#debug20170525#:" + array[0]+" seq:"+seq);
				seqMap.put(array[0], seq++);
				// System.out.println("@ｘｘｘIncoreSort0630 id:" + array[0] +
				// " share:" + array[1]+ " val:" + array[2]);
				writer.write(array, 1);
			}
		}

		// ---------------------------------------------------------------------
		//	先頭に付け足す行処理
		// ---------------------------------------------------------------------
		if (suffix != null) {
			for (String element : suffix) {
				writer.write(element);
			}
		}

		writer.close();
	}

	// -------------------------------------------------------------------------
	// test
	// -------------------------------------------------------------------------
	private static void testIncoreSort() {
		boolean opt1st = true;//１レコード目を退避処理する場合true　（並べ替えの対象にしない）
		boolean optLast = false;//最終レコードを退避処理する場合true　（並べ替えの対象にしない）
		String sortParam = "1,Double,desc";
		String outPath = "C:/@qpr/home/123620000058/calc/#999_result.txt";
		String inPath = "C:/@qpr/home/123620000058/calc/#005_result.txt";
		IncoreSort_Ex ins = new IncoreSort_Ex(sortParam, outPath, inPath);
		ins.setOptLast(true);//最終レコードを退避処理する場合true　（並べ替えの対象にしない）
		ins.execute();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		//		tester2011_1111();
		testIncoreSort();
	}

}
