package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

//指定したセルの値（例えば：区分名など）をハッシュ値に変換する
public class HashEncoder implements Inf_BaseClojure {
	private static final String LF = System.getProperty("line.separator");
	private String dicH2NamePath;
	private String outPath;
	private int col;
	private String delimiter = "\t";
	private BufferedWriter bw;
	private int counter = 0;
	private HashMap<String, String> hmap;
	private String hashCode;
	private int preHash;

	public long getCount() {
		return counter;
	}

	public HashEncoder(String dicPath, String outPath, int col) {
		this.counter = 0;
		this.dicH2NamePath = dicPath;
		this.outPath = outPath;
		this.col = col;
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
		hmap = new HashMap();
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
			int curHash = rec[col].hashCode();
			if (preHash != curHash) {
				preHash = curHash;
				hashCode = String.valueOf(preHash);
				hmap.put(hashCode, rec[col]);
			}
			rec[col] = hashCode;
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
		DictionaryControl.writeDictionary(dicH2NamePath, hmap, delimiter);
	}

	public static void main(String[] argv) {
		hashEncDecTest();
	}

	public static void hashEncDecTest() {
		System.out.println("--start--");
		String userDir = globals.ResControl.getQPRHome();
		String dicH2NamePath = userDir + "828111000507/calc/dictionary.txt";
		String encoded = userDir + "828111000507/calc/encode.txt";
		String inPath = userDir + "828111000507/calc/trrModItp.txt";
		int col = 3;
		HashEncoder encCloj = new HashEncoder(dicH2NamePath, encoded, col);
		new CommonClojure().incore(encCloj, inPath, true);
		// -----------------------------------------------------------
		String decoded = userDir + "828111000507/calc/decode.txt";
		HashDecoder decCloj = new HashDecoder(decoded, dicH2NamePath, col);
		new CommonClojure().incore(decCloj, encoded, true);
		System.out.println("--end--");
	}

	public static void hashEncodeTest2() {
		// モニターｉｄなどを指定するとかえって、データ量が増加してしまう。
		String userDir = globals.ResControl.getQPRHome();
		String dicPath = userDir+"828111000507/calc/monDict.txt";
		String outPath = userDir+"828111000507/calc/encode.txt";
		String inPath = userDir+"828111000507/calc/trrModItp.txt";
		int col = 0;
		HashEncoder closure = new HashEncoder(dicPath, outPath, col);
		new CommonClojure().incore(closure, inPath, true);
	}
}
