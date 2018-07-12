package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;

//---------------------------------------------------------------
// 雛形用・・・・
//---------------------------------------------------------------
public class AAA_TemplateOf implements Inf_BaseClojure {
	private static final String LF = System.getProperty("line.separator");
	private String outPath;
	private int col;
	private String delimiter = "\t";
	private BufferedWriter writer;
	private int counter = 0;

	public int getCounter() {
		return counter;
	}

	// ---------------------------------------------------------------
	// 指定カラムの値がリストに含まれていた場合ｔのファイルに出力される、それ以外はｆに出力される
	// ---------------------------------------------------------------
	public AAA_TemplateOf(String outPath, int col) {
		this.counter = 0;
		this.outPath = outPath;
		this.col = col;
	}

	// ---------------------------------------------------------------
	// open
	// ---------------------------------------------------------------
	private BufferedWriter open(String outPath) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outPath));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return bw;
	}

	// ---------------------------------------------------------------
	// write
	// ---------------------------------------------------------------
	public void write(String[] rec) {
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
	private void close() {
		try {
			writer.close();
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure:" + e.toString());
		}
	}

	@Override
	public void init() {
		writer = open(outPath);
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			write(rec);
		}
	}
	@Override
	public void write() {
		close();
	}
	public static void main(String[] argv) {
		tester();
	}

	public static void tester() {
		String userDir = globals.ResControl.getQPRHome();
		String outPath = userDir+"828111000507/calc/divider.txt";
		String inPath = userDir+"828111000507/calc/trrModItp.txt";
		int col = 3;
		AAA_TemplateOf closure = new AAA_TemplateOf(outPath, col);
		new CommonClojure().incore(closure, inPath, true);
	}
}
