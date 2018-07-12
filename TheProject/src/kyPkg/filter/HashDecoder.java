package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
//すでにハッシュ値に変換されているセルの値を辞書ファイルを元に復号する
public class HashDecoder implements Inf_BaseClojure {
	private static final String LF = System.getProperty("line.separator");
	private String dicPath;
	private String outPath;
	private int col;
	private String delimiter = "\t";
	private BufferedWriter bw;
	private int counter = 0;
	private HashMap<String, String> hashDic;
	private int dictKeyCol = 0;
	public HashDecoder(String outPath,String dicPath, int col) {
		this(outPath,dicPath, col,0);
	}
	public long getCount() {
		return counter;
	}

	public HashDecoder(String outPath,String dicPath, int col,int dictKeyCol) {
		this.counter = 0;
		this.dicPath = dicPath;
		this.outPath = outPath;
		this.col = col;
		this.dictKeyCol = dictKeyCol;
	}
	public void write(String[] rec) {
		String xrec = join(rec, delimiter);
		try {
			bw.write(xrec);
			bw.write(LF);
			counter++;
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure.write1:" + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		hashDic = DictionaryControl.readDictionary(dictKeyCol,dicPath);
		// open
		try {
			bw = new BufferedWriter(new FileWriter(outPath));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}
	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			String val = hashDic.get(rec[col]);
			if (val!=null ) {
				rec[col] = val;
			}else{
				rec[col] = "?!";
			}
			write(rec);
		}
	}

	@Override
	public void write() {
		// close
		try {
			bw.close();
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure:" + e.toString());
		}
	}

	public static void main(String[] argv) {
		hashDecodeTest();
	}

	public static void hashDecodeTest() {
		String userDir = globals.ResControl.getQPRHome();
		String dicPath = userDir+"828111000507/calc/dictionary.txt";
		String outPath = userDir+"828111000507/calc/decode.txt";
		String inPath = userDir+"828111000507/calc/encode.txt";
		int col = 3;
		HashDecoder closure = new HashDecoder(outPath,dicPath, col);
		new CommonClojure().incore(closure, inPath, true);
	}
}
