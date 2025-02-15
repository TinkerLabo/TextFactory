package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.uFile.FileUtil;

public class EzWriter extends EzWriterBase {
	private Inf_ArrayCnv arrayConv;
	private String[] header = null;

	//20160421 Header
	public void setHeader(String[] header) {
		this.header = header;
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public EzWriter(String path) {
		//		super(path);//20170425
		this(path, "");
	}

	public EzWriter(String path, String charsetName) {
		super(path);
		setCharsetName(charsetName);
	}

	public EzWriter(String path, Inf_ArrayCnv converter) {
		//		super(path);//20170425
		this(path, "");
		setConverter(converter);
	}

	public void setConverter(Inf_ArrayCnv converter) {
		this.arrayConv = converter;
	}

	// ------------------------------------------------------------------------
	// open
	// ------------------------------------------------------------------------
	@Override
	public boolean open(boolean append) {
		if (arrayConv != null)
			arrayConv.init();
		boolean stat = super.open(append);
		if (stat)
			super.write(header);//20160421 
		return stat;
	}

	// ------------------------------------------------------------------------
	// close
	// ------------------------------------------------------------------------
	@Override
	public synchronized void close() {
		if (arrayConv != null)
			arrayConv.fin();
		super.close();
	}

	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	@Override
	public boolean write(Inf_iClosure reader) {
		return write(reader.readSplited(), (int) writeCount);
	}

	@Override
	public boolean write(List list, int stat) {
		if (list == null)
			return false;
		if (arrayConv != null) {
			// ここでどのぐらいパフォーマンスが落ちるのか、気になる・・・どのぐらいの頻度でここを通るのか？
			String[] array = (String[]) list.toArray(new String[list.size()]);
			return super.write(join(arrayConv.convert(array, stat), delimiter));
		} else {
			return super.write(list);
		}
	}

	@Override
	public boolean write(String[] array, int stat) {
		if (array == null)
			return false;
		if (arrayConv != null) {
			return super.write(join(arrayConv.convert(array, stat), delimiter));
		} else {
			return super.write(array);
		}
	}

	@Override
	public synchronized boolean write(String rec) {
		if (arrayConv != null) {
			return write(rec.split(inDelimiter, -1), 0);
		} else {
			return super.write(rec);
		}
	}

	// ------------------------------------------------------------------------
	// リストからファイルに書き出す
	// EzWriter.list2File(outPath, list);
	// ------------------------------------------------------------------------
	public static boolean list2File(String outPath, List<String> list) {
		return list2File(outPath, list, false, "");
	}

	// ------------------------------------------------------------------------
	// 追加書き込みならappendをtrueにする
	// エンコードを指定したい場合にはcharSetNameに"UTF-8"などを指定する
	// ------------------------------------------------------------------------
	public static boolean list2File(String outPath, List<String> list,
			boolean append, String charsetName) {
		FileUtil.makeParents(outPath);
		EzWriter writer = new EzWriter(outPath);
		writer.setCharsetName(charsetName);
		writer.setSuppress(false);
		writer.open(append);
		for (String element : list) {
			writer.write(element);
		}
		writer.close();
		return true;
	}

	public static void main(String[] argv) {
		list2File_test1();
	}

	// ------------------------------------------------------------------------
	// 様々な文字コードでファイルを書き出してみるテスト
	// ------------------------------------------------------------------------
	public static void list2File_test1() {
		List<String> list = getSampleDatas();
		List<String> charsetList = FileUtil.getCharsetNames();
		charsetList.remove("JISAutoDetect");
		for (String charSetName : charsetList) {
			String rootDir = globals.ResControl.getQprRootDir();
			String outPath = rootDir + "enc_" + charSetName + ".txt";
			System.out.println("outPath=>" + outPath);
			boolean append = false;
			EzWriter.list2File(outPath, list, append, charSetName);
		}
	}

	// ------------------------------------------------------------------------
	// isamだと他のｏｓでは動いてくれないので要修正かな・・・・ただ今回はクロス集計用のものなので目をつぶる
	// ------------------------------------------------------------------------
	public static void list2File_test2() {
		String cnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir=$;DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
		List list = new ArrayList();
		list.add("connect\t" + cnn);
		list.add("table\tASM#TXT");
		list.add("field\t");
		list.add("key\tID");
		list.add("Cond\t");
		String rootDir = globals.ResControl.getQprRootDir();
		String oPath = rootDir + "alias.txt";
		EzWriter.list2File(oPath, list);
	}

	public static List<String> getSampleDatas() {
		List sampleData = new ArrayList();
		sampleData.add("文字表示：壱弐参肆伍陸漆捌玖拾 月火水木金土日 阿伊宇江於");
		sampleData.add("�T�U�V�W�X�Y�Z�[�\�]�_�`�a�b�c�d＋−±×÷§〒→←↑↓♪†‡¶");
		sampleData.add("機種依存文字 �ｇョх��������援括窮欠合紫順~�@�A�B�C�D��唖娃阿哀愛挨姶逢葵茜穐悪握渥旭葦≡∫�煤�");
		sampleData.add("ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω");
		sampleData.add("あいうえおきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよわをん");
		sampleData.add("アイウエオキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨワヲン");
		sampleData.add("ｱｲｳｴｵｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾜｦﾝ");
		sampleData.add("１２３４５６７８９０");
		sampleData.add("1234567890-^\\@[;:],./!\"#$%&'()=~|`{+*}<>?_");
		sampleData.add("ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ");
		sampleData.add("ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ");
		sampleData.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		sampleData.add("abcdefghijklmnopqrstuvwxyz");
		sampleData
				.add("E 04-YYYY2=〜０９０６２５　　　　　　　　　　　　　　　　　　   000000000000    ");
		return sampleData;
	}
}
