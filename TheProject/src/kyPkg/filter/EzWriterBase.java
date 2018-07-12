package kyPkg.filter;

import static globals.ResControl.getCurDir;
import static kyPkg.util.Joint.join;
import static kyPkg.util.KUtil.array2String;
import static kyPkg.util.KUtil.list2String;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import kyPkg.uFile.FileUtil;

public abstract class EzWriterBase implements Inf_BaseClojure, Inf_oClosure {
	//	protected String charsetName = System.getProperty("file.encoding");
	private String charsetName = FileUtil.getDefaultEncoding();//20161222

	protected String inDelimiter = "\t";
	protected String delimiter = "\t";
	protected String LF = System.getProperty("line.separator");
	protected OutputStreamWriter writer;
	protected String preRec = "";
	protected boolean suppress = false;
	protected boolean append = false;
	protected long writeCount = 0;
	protected String outPath = getCurDir() + "tempolary.tmp";
	protected String header = null;
	// ------------------------------------------------------------------------
	// ※通常テンポラリに書き出してcloseする段階で本来の出力パスにリネームする
	// ↓但し、追加書き込みモードのときはスワップ処理をしないので注意する
	// ------------------------------------------------------------------------
	protected boolean swapMode = true;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public EzWriterBase(String path) {
		super();
		FileUtil.makeParents(path);
		this.outPath = path;
		//		DosEmu.del(this.outPath);//20160822 なんとなく追加・・・ファイル生成されず前回の結果を拾ってしまうと嫌なので・・・
	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public void setCharsetName(String charsetName) {
		charsetName = charsetName.trim();
		if (charsetName.equals("")) {
			charsetName = FileUtil.getDefaultEncoding();//20161222
		}
		this.charsetName = charsetName;
	}

	public void setSuppress(boolean suppress) {
		this.suppress = suppress;
	}

	public void setAppendMode(boolean append) {
		this.append = append; // もし上書きモードならスワップ処理しない・・・・
	}

	@Override
	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public void setHeader(List<String> headList, String delimiter) {
		this.header = list2String(headList, delimiter);
	}

	@Override
	public void setHeader(String[] headArray, String delimiter) {
		this.header = array2String(headArray, delimiter);
	}

	// XXX どうして・・・OutputStreamWriterをextendしていないのだっけ？？？（@20120511）
	public OutputStreamWriter getWriter() {
		return writer;
	}

	public void setEncoding(String encoding) {
		this.charsetName = encoding;
	}

	@Override
	public void setLF(String lf) {
		LF = lf;
	}

	@Override
	public long getWriteCount() {
		return writeCount;
	}

	@Override
	public String getPath() {
		return outPath;
	}

	@Override
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
		open();
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void write() {
		close();
	}

	// ------------------------------------------------------------------------
	// execute
	// ------------------------------------------------------------------------
	@Override
	public void execute(String[] rec) {
		execute(join(rec, ","));
	}

	@Override
	public void execute(String rec) {
		writeLoco(rec);
	}

	// ------------------------------------------------------------------------
	// open
	// ------------------------------------------------------------------------
	@Override
	public synchronized boolean open() {
		return open(append);
	}

	@Override
	public synchronized boolean open(boolean append) {
		if (append)
			swapMode = false;
		preRec = null;
		writeCount = 0;
		String tmpPath = outPath + ".tmp";// .tmpが無いという前提
		if (!swapMode)
			tmpPath = outPath;
		if (writer != null)
			return true;// 同じインスタンスを重複して登録する場合を考慮している
		try {
			File oFile = new File(tmpPath);
			if (append) {
				if (oFile.exists() && !oFile.canWrite()) {
					System.out.println("#error File Can't Write!! :" + tmpPath);
					return false;
				}
			} else {
				if (append == false && oFile.exists())
					oFile.delete(); // ちゃんと消せるのかどうか、上書きｏｋか要確認かも
			}
			//			System.out.println("#20161222# charsetName:"+charsetName);
			writer = new OutputStreamWriter(
					new FileOutputStream(tmpPath, append), charsetName);
		} catch (IOException ie) {
			ie.printStackTrace();
			System.out.println("#ERROR IOException EzWriter.open()" + tmpPath);
		} catch (Exception e) {
			System.out.println("#ERROR Exception   EzWriter.open()" + tmpPath);
			e.printStackTrace();
		}
		if (header != null)
			writeLoco(header);// ヘッダーが指定されていたら書き出す
		return true;
	}

	// ------------------------------------------------------------------------
	// close
	// ------------------------------------------------------------------------
	@Override
	public synchronized void close() {
		if (writer == null)
			return;// 同じインスタンスを重複して登録する場合を考慮している
		try {
			// System.out.println("close");
			writer.close();
			writer = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (swapMode) {
			// 目的のファイルにリネーム・・・
			File wFile_O = new File(outPath);
			if (wFile_O.exists())
				wFile_O.delete(); // ちゃんと消せるのかどうか、上書きｏｋか要確認かも
			String tmpPath = outPath + ".tmp";// .tmpが無いという前提
			File wFile_T = new File(tmpPath);
			wFile_T.renameTo(wFile_O);
		}
	}

	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	public boolean write(List list) {
		if (list == null)
			return false;
		return writeLoco(join(list, delimiter));
	}

	public boolean write(String[] array) {
		if (array == null)
			return false;
		return writeLoco(join(array, delimiter));
	}

	@Override
	public synchronized boolean write(String rec) {
		return writeLoco(rec);
	}

	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	private synchronized boolean writeLoco(String rec) {
		if (rec == null) {
			return true;
			// 受け取ったレコードがnullなら何も書き出さない（空行は出力しない！）
			// つまり、出力したくないレコードなら、フィルターでnullを返せばよい
		}
		if (writer == null) {
			System.out.println("#ERROR @ezWriter writer == null ?!");
			return false;
		}
		try {
			if (suppress && rec.equals(preRec)) {
				// サプレスオプションありで直前のレコードと同じ内容なら書き出さない
			} else {
				writer.write(rec);
				writer.write(LF);
				writeCount += 1;
			}
			preRec = rec;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}