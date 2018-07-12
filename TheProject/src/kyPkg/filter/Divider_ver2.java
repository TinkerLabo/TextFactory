package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

import kyPkg.uFile.DosEmu;
//ハッシュマップにライターをプールしておくバージョン
//---------------------------------------------------------------
// 指定したセルの値（ex：ID）ごとにファイル分割を行う・・・さてmax5000idぐらいとしてどんなもんか？
//---------------------------------------------------------------
public class Divider_ver2 implements Inf_BaseClojure {
	private static final String LF = System.getProperty("line.separator");
	private String outDir;
	private int col;
	private String delimiter = "\t";
	private HashMap<String, BufferedWriter> hmap;
	private int counter = 0;
	public long getCount() {
		return counter;
	}

	// ---------------------------------------------------------------
	// 指定カラムの値がリストに含まれていた場合ｔのファイルに出力される、それ以外はｆに出力される
	// ---------------------------------------------------------------
	public Divider_ver2(String ofFile, int col) {
		this.counter = 0;
		this.outDir = ofFile;
		this.col = col;
	}
	// ---------------------------------------------------------------
	// open
	// ---------------------------------------------------------------
	private BufferedWriter open(String outPath) {
		// 追加モードで開く
		BufferedWriter bw = null;
		boolean append = true;
		try {
			bw = new BufferedWriter(new FileWriter(outPath, append));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return bw;
	}
	// ---------------------------------------------------------------
	// write
	// ---------------------------------------------------------------
	public void write(BufferedWriter writer, String[] rec) {
		String xrec = join(rec, delimiter);
		try {
			writer.write(xrec);
			writer.write(LF);
			counter++;
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure.write1:" + e.toString());
			e.printStackTrace();
		}
	}

	// ---------------------------------------------------------------
	// close
	// ---------------------------------------------------------------
	private void close(BufferedWriter writer) {
		try {
			writer.close();
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure:" + e.toString());
		}
	}

	@Override
	public void init() {
		hmap=new HashMap<String, BufferedWriter>();
		outDir = kyPkg.uFile.FileUtil.makedir(outDir);
		//  当該ディレクトリ以下のファイルを削除する
		DosEmu.del(outDir + "*.txt");
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			String curKey = rec[col];
			if (!curKey.equals("")) {
				BufferedWriter writer = hmap.get(curKey);
				if (writer == null) {
					writer = open(outDir + curKey + ".txt");
					hmap.put(rec[col], writer);
				}
				if (writer != null)
					write(writer, rec);
			}
		}
	}

	@Override
	public void write() {
		java.util.Set set = hmap.entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
//			String key = (String) ent.getKey();
			BufferedWriter writer = (BufferedWriter) ent.getValue();
//			System.out.println("key:" + key + "  val:" + writer);
			close(writer);
		}
	}

	public static void main(String[] argv) {
		// XXX ファイルポインタープールを作る・・・キューの数を調整する
		// メモリの状態を観察する
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("DividerVer2");
		elapse.start();
		tester();
		elapse.stop();
	}

	public static void tester() {
		String userDir = globals.ResControl.getQPRHome();
		String outDir = userDir+"828111000507/calc/chile";
		String inPath = userDir+"828111000507/calc/trrModItp.txt";
		int col = 0;
		Divider_ver2 closure = new Divider_ver2(outDir, col);
		new CommonClojure().incore(closure, inPath, true);
	}
}
