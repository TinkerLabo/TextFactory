package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.ListArrayUtil;

public class Header extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private int skip = 0;
	private long limit = Long.MAX_VALUE;

	//TODO writerのメソッドとしたい　【20160219】
	public void setLimit(long limit) {
		this.limit = limit;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	private void setHeader(String header) {
		writer.setHeader(header);
	}

	private void setHeader(List<String> headList, String delimiter) {
		writer.setHeader(headList, delimiter);
	}

	private void setHeader(String[] headArray, String delimiter) {
		writer.setHeader(headArray, delimiter);
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Header(String outPath, List<String> headString, String delimiter,
			String inPath) {
		this(outPath, inPath);
		setHeader(headString, delimiter);
	}

	public Header(String outPath, String inPath) {
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath);
	}

	public Header(Inf_oClosure writer, Inf_iClosure reader) {
		this.reader = reader;
		this.writer = writer;
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("## Header Start ##");
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# Header #");
		elapse.start();
		long lcnt = 0;
		reader.open();
		writer.open();
		// FIXME 実際のレコード件数よりskipが大きいとエラーになるはずなので、修正しておくこと
		while (skip > 0) {
			reader.readLine();
			skip--;
		}
		while (lcnt < limit && writer.write(reader)) {
			lcnt++;
		}
		reader.close();
		writer.close();
		elapse.stop();
	}

	public static void testNoOption() {
		String inPath = "C:/#340018110135/Add_head.txt";
		String outPath = "C:/#340018110135/Add_head.txt";
		Header ins = new Header(outPath, inPath);
		ins.setSkip(2);
		ins.setLimit(5);
		ins.execute();
	}

	// -------------------------------------------------------------------------
	//	複数行あれば区切り文字で連結した文字列を返す
	//	※2段組みヘッダーを考えたい場合これではいけない
	// -------------------------------------------------------------------------
	public static String getFileHeader(String path, String delimiter) {
		List<String> list = ListArrayUtil.file2List(path);
		if (list == null)
			return null;
		if (list.size() == 1) {
			return list.get(0);
		} else {
			StringBuffer buf = new StringBuffer();
			for (String element : list) {
				if (buf.length() > 0)
					buf.append(delimiter);
				buf.append(element);
			}
			return buf.toString();
		}
	}

	public static void testStringHead() {
		String headString = "this is a string header";
		String inPath = "C:/#340018110135/20130401_20140331.mon";
		String outPath = "C:/#340018110135/Add_head.txt";
		Header ins = new Header(outPath, inPath);
		ins.setHeader(headString);
		ins.execute();
	}

	public static void testListHead() {
		// ---------------------------------------------------------------------
		List headList = new ArrayList();
		headList.add("Hello");
		headList.add("This");
		headList.add("is");
		headList.add("A ListHeader");
		// ---------------------------------------------------------------------
		String inPath = "C:/#340018110135/20130401_20140331.mon";
		String outPath = "C:/#340018110135/Add_head.txt";
		new Header(outPath, headList, "\t", inPath).execute();
	}

	public static void testArrayHead() {
		String[] headArray = new String[] { "Hello", "This", "is",
				"ArrayType Header" };
		String inPath = "C:/#340018110135/20130401_20140331.mon";
		String outPath = "C:/#340018110135/Add_head.txt";
		Header ins = new Header(outPath, inPath);
		ins.setHeader(headArray, "\t");
		ins.setSkip(3);
		ins.execute();
	}

	public static void testFileHead() {
		String headPath = "C:/#340018110135/header.txt";
		String headString = getFileHeader(headPath, "\t");
		String inPath = "C:/#340018110135/20130401_20140331.mon";
		String outPath = "C:/#340018110135/Add_head.txt";
		Header ins = new Header(outPath, inPath);
		ins.setHeader(headString);
		ins.execute();
	}

	public static void testFileHeadIIzuka() {
		String headPath = "Z:/S2/rx/qpr/Iizuka/QPR4PLSA/headerType2.txt";
		String headString = getFileHeader(headPath, "\t");
		String inPath = "Z:/S2/rx/qpr/Iizuka/QPR4PLSA/to219999Type2.txt";
		String outPath = "Z:/S2/rx/qpr/Iizuka/QPR4PLSA/to219999Type2Head.txt";
		Header ins = new Header(outPath, inPath);
		ins.setHeader(headString);
		ins.execute();
	}

	//20151225
	public static void header_1() {
		String headPath = "k:/S2/rx/qpr/Iizuka/QPR4PLSA/headerType1.txt";
		String headString = getFileHeader(headPath, ",");
		String inPath = "k:/S2/rx/qpr/Iizuka/QPR4PLSA/stdType1.txt";
		String outPath = "k:/S2/rx/qpr/Iizuka/QPR4PLSA/stdType1Head.txt";
		Header ins = new Header(outPath, inPath);
		ins.setHeader(headString);
		ins.execute();
	}

	//20151225
	public static void header_2() {
		String headPath = "k:/S2/rx/qpr/Iizuka/QPR4PLSA/headerType2.txt";
		String headString = getFileHeader(headPath, ",");
		String inPath = "k:/S2/rx/qpr/Iizuka/QPR4PLSA/stdType2.txt";
		String outPath = "k:/S2/rx/qpr/Iizuka/QPR4PLSA/stdType2Head.txt";
		Header ins = new Header(outPath, inPath);
		ins.setHeader(headString);
		ins.execute();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	// 入出力が同じファイルでも大丈夫か？⇒できる（あとで、ロジックをちゃんと確認しよう！）
	// XXX ヘッダーをファイルで供給したい
	// XXX ヘッダーを2段組みにできないだろうか
	// XXX unixのteilの機能も実装したい
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		System.out.println("start");
		System.out.println("type1");
		header_1();
		System.out.println("type2");
		header_2();
		System.out.println("finish");
	}

}
