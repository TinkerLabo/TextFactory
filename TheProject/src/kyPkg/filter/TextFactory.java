package kyPkg.filter;

import java.io.File;

import kyPkg.converter.Inf_LineConverter;
import kyPkg.task.Abs_BaseTask;
import kyPkg.util.Joint;

//XXX	バッチ起動対応をする（単体で動く）・・・できればログも吐くようにする
//XXX	@FirstSundayとあったら・・・処理日の第一日曜日の日付を埋め込む

//テキストファイルを変換する、汎用プログラム
public class TextFactory extends Abs_BaseTask {
	private static String comment = "";
	private static final String ARG_ERROR = "#Error 引数が３つ必要です =>";
	private static final String SAMPLE = "例＞java -jar TextFactory.jar outPath　inPath　parmPath　";
	private static final String NOT_A_FILE = "#Error　ファイルが存在しません。=>";
	private Inf_LineConverter converter;
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private int skip = 0;
	private long limit = Long.MAX_VALUE;
	private boolean withHeader = false;
	private int writeCount;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public TextFactory(String outPath, String inPath,
			Inf_LineConverter converter) {
		// System.out.println("@createTester--------------------------------------------------");
		// System.out.println("public static void tester() {");
		// System.out.println("    String outPath = \"" + outPath + "\";");
		// System.out.println("    String inPath = \"" + inPath + "\";");
		// System.out.println("    Inf_LineConverter converter = " +
		// converter.toString() + ";");
		// System.out.println("    new TextFactory(outPath,inPath,converter);");
		// System.out.println("}");
		// System.out.println("--------------------------------------------------");
		// this.reader = new EzReader(inPath);
		// this.writer = new EzWriter(outPath);
		// this.converter = converter;
		this(new EzWriter(outPath), new EzReader(inPath), converter);
	}

	//20150309 汎化させるために、ロジック変更した
	// private TextFactory(String outPath, String inPath,
	// Inf_LineConverter converter, String oEncoding, String iEncoding) {
	// EzWriter ezWriter = new EzWriter(outPath);
	// ezWriter.setCharsetName(oEncoding);
	// ezWriter.setSuppress(false);
	// this.reader = new EzReader(inPath, iEncoding);
	// this.writer = ezWriter;
	// this.converter = converter;
	// }

	public TextFactory(Inf_oClosure writer, Inf_iClosure reader,
			Inf_LineConverter converter) {
		this.reader = reader;
		this.writer = writer;
		this.converter = converter;
	}

	public int getWriteCount() {
		return writeCount;
	}

	public void setRedersDelimiter(String delimiter) {
		reader.setDelimiter(delimiter);
	}

	public void setLimit(long limit) {
		if (limit < 0) {
			this.limit = Long.MAX_VALUE;
		} else {
			this.limit = limit;
		}
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public void headerOption(boolean withHeader) {
		this.withHeader = withHeader;
	}

	// -------------------------------------------------------------------------
	// 入力データを限定するためのフィルタを設定する
	// "1,0,1,True,16,^2" → 1セルめの、開始位置0文字目から、長さ1文字、がレジックスにマッチするなら、を設定する、値、レジックス
	// -------------------------------------------------------------------------
	public void setFilter(RegChecker filter, int stat) {
		reader.setFilter(filter, stat);
	}

	private String convert2Str(String[] array, int lineNumber) {
		// System.out.println("Write !!>>> array[234]:"+ array[234]);

		String[] debug = converter.convert(array, lineNumber);
		// System.out.println("Write !!>>> debug[234]:"+ debug[234]);

		return Joint.join(converter.convert(array, lineNumber), "\t");
	}

	// -------------------------------------------------------------------------
	// 入力パスと出力パスが同じ場合は念のためバックアップを残す。??
	// File wFile_I = new File(inPath);
	// if (inPath.equals(outPath))
	// wFile_I.renameTo(new File(inPath + ".bak"));
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("TextFactory　start");
		writeCount = 0;
		boolean wDirty = false;
		String[] splited;
		converter.init();
		// converter.setSuffix(LF);
		reader.open();
		writer.open();
		for (int ix = 0; ix < skip; ix++) {
			reader.readLine();// skip 指定行数分空読みする
		}
		if (withHeader)
			writer.write(converter.getHeader());// コメント部よりヘッダーを作成
		while ((splited = reader.readSplited()) != null) {
			if (splited.length > 0) {
				// System.out.println("Write !!>>> splited.length:"+
				// splited.length);
				wDirty = true; // 変更があった場合フラグを立てる

				writer.write(convert2Str(splited, writeCount));
				writeCount++;
				if (writeCount >= limit)
					break;
			}
		}
		reader.close();
		writer.close();
		if (wDirty) {
			stop();
		} else {
			abend();
		}
	}

	// バッチ起動用
	public static void kickAsBatch(String[] args) {
		// XXX ヘッダーの有無のオプションを設定したい
		// XXX @firstsunday
		if (args.length != 3) {
			System.out.println(ARG_ERROR + SAMPLE);
			System.exit(9);
		}
		String outPath = args[0];
		String inPath = args[1];
		String parmPath = args[2];
		comment += " parmPath:" + parmPath + "\n";
		comment += " inPath  :" + inPath + "\n";
		comment += " outPath :" + outPath + "";
		// XXX 出力ファイルは存在しなくても良いが、ディレクトリの存在チェックなどはした方が良いかもしれない
		// if (!new File(outPath).isFile()) {
		// System.out.println(NOT_A_FILE + " out :" + outPath);
		// System.exit(9);
		// }
		if (!new File(inPath).isFile()) {
			System.out.println(NOT_A_FILE + " in :" + inPath);
			System.exit(9);
		}
		if (!new File(parmPath).isFile()) {
			System.out.println(NOT_A_FILE + " parm :" + parmPath);
			System.exit(9);
		}
		kyPkg.converter.SubstrCnv converter = new kyPkg.converter.SubstrCnv(parmPath);
		TextFactory ins = new TextFactory(outPath, inPath, converter);
		// XXX スキップ処理も対応した方が良いかもしれない
		// setSkip(int skip)
		ins.headerOption(false);
		ins.execute();
	}

	public static void kickAss(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse(
				"TextFactory Elapse");
		elapse.start();
		kickAsBatch(args);
		elapse.setComment(comment);
		elapse.stop();
	}

	public static void main(String[] args) {
		if (args.length == 3) {
			String oPath = args[0];
			String iPath = args[1];
			String parmPath = args[2];
			Inf_LineConverter converter = new kyPkg.converter.SubstrCnv(parmPath);
			TextFactory insSub = new TextFactory(oPath, iPath, converter);
			insSub.headerOption(true);
			insSub.execute();
		} else {
			System.out.println("Usage .. oPath,iPath,parmPath");
		}
	}

	public static void tester01(String[] args) {
		String parmPath = "N:/PowerBX/2014/T1050_ヨーグルト①/subParm.csv";
		String outPath = "c:/ASM.TXT";
		String inPath = "N:/datas/QBR14/1050.csv";
		Inf_LineConverter converter = new kyPkg.converter.SubstrCnv(parmPath);
		TextFactory insSub = new TextFactory(outPath, inPath, converter);
		insSub.headerOption(true);
		insSub.execute();
	}
	// -------------------------------------------------------------------------
	// パラメータの解説が必要じゃ！！０１・１１
	// new Filters().fltSubstring("out","In",Vector(),"\t");
	// T:\PBrand\PowerB\2007\
	// T1010\B101\RESULT.TXT
	// T8012\B119\RESULT.TXT
	// -------------------------------------------------------------------------
}
