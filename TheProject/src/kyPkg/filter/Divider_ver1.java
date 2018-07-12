package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;

import kyPkg.uFile.DosEmu;
//ライターをひとつしか使わないバージョン
//---------------------------------------------------------------
// 指定したセルの値（ex：ID）ごとにファイル分割を行う・・・さてmax5000idぐらいとしてどんなもんか？
//---------------------------------------------------------------
public class Divider_ver1 implements Inf_BaseClojure {
	private static final String LF = System.getProperty("line.separator");
	private String preKey="";
	private String outDir;
	private int col;
	private String delimiter = "\t";
	private BufferedWriter bw;
	private int counter = 0;
	public long getCount() {
		return counter;
	}

	//---------------------------------------------------------------
	// 指定カラムの値がリストに含まれていた場合ｔのファイルに出力される、それ以外はｆに出力される
	//---------------------------------------------------------------
	public Divider_ver1(String ofFile, int col) {
		this.counter = 0;
		this.outDir = ofFile;
		this.col = col;
	}
	//---------------------------------------------------------------
	// open
	//---------------------------------------------------------------
	private void open(String outPath) {
		//追加モードで開く
		boolean append = true;
		try {
			bw = new BufferedWriter(new FileWriter(outPath,append));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	//---------------------------------------------------------------
	// write
	//---------------------------------------------------------------
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
	//---------------------------------------------------------------
	// close
	//---------------------------------------------------------------
	private void close() {
		try {
			bw.close();
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure:" + e.toString());
		}
	}
	@Override
	public void init() {
		outDir = kyPkg.uFile.FileUtil.makedir(outDir);
		// 当該ディレクトリ以下のファイルを削除する
		DosEmu.del(outDir + "*.txt");
	}
	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}
	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			if (!rec[col].equals(preKey)){
				//　Break処理・・・
				if (!preKey.equals("") ) close();
				preKey = rec[col].trim();
				if (!preKey.equals("") ) open(outDir+preKey+".txt");
			}
			write(rec);
		}
	}
	@Override
	public void write() {
		if (!preKey.equals("") ) close();
	}
	public static void main(String[] argv) {
		//XXX ファイルポインタープールを作る・・・キューの数を調整する
		//メモリの状態を観察する
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("DividerVer1");
		elapse.start();
		tester();
		elapse.stop();
	}
	public static void tester() {
		String userDir = globals.ResControl.getQPRHome();
		String outDir = userDir+"828111000507/calc/chile";
		String inPath = userDir+"828111000507/calc/trrModItp.txt";
		int col = 0;
		Divider_ver1 closure = new Divider_ver1(outDir, col);
		new CommonClojure().incore(closure, inPath, true);
	}
}
