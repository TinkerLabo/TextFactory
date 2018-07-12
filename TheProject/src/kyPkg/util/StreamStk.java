package kyPkg.util;
import java.util.*;
import java.io.*;
//-----------------------------------------------------------------------------
// 出力ストリームをパターンごとに準備＆処理
// 《使用例》
//	StreamStk ssObj = new StreamStk(path);
//	ssObj.addPattern("beer.dat");
//	ssObj.write("beer.dat",wRec);
//  そのままでファイナライズ処理がされるか確認する
//-----------------------------------------------------------------------------
public class StreamStk{
	Hashtable ht;
	String outDir;
	//-------------------------------------------------------------------------
	public StreamStk(String pPath){
		//---------------------------------------------------------------------
		// pPath 入力ファイルと同名のフォルダを作成してそれを出力dirとする
		//---------------------------------------------------------------------
		String wPathx[] = pPath.split("\\."); //※ split の引数は Regix 
		File wF = new File(wPathx[0]);
		wF.mkdirs();
		if(wF.exists()){
			this.outDir = wF.getAbsolutePath() + "\\";
			this.ht = new Hashtable();
		}
	}
	//-------------------------------------------------------------------------
	// パターンに対応する出力ストリームを作成する その１
	// ※拡張子に関係ない区切り文字を指定したい場合に使用する
	//-------------------------------------------------------------------------
	public boolean addPattern(String wPtn,String delimita){
		if( wPtn.trim().equals("") )return false;
		if (ht.contains(wPtn)) return true; // 既に存在する
		try {
			String wPath = this.outDir + wPtn;
			ht.put(wPtn,new BuffWriter(wPath,delimita));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	//-------------------------------------------------------------------------
	// パターンに対応する出力ストリームを作成する その２
	// ※拡張子に関連した区切り文字が設定される
	//-------------------------------------------------------------------------
	public boolean addPattern(String wPtn){
		if( wPtn.trim().equals("") )return false;
		if (ht.contains(wPtn)) return true; // 既に存在する
		try {
			String wPath = this.outDir + wPtn;
			ht.put(wPtn,new BuffWriter(wPath));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	//-------------------------------------------------------------------------
	// バッファーを無視してデータを書き出す（バッファーはクリアされる）
	//-------------------------------------------------------------------------
	public void write(String wPtn,String wRec){
		//System.out.println("●wPtn:"+wPtn);
		//System.out.println("●wRec:"+wRec);
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			mr.write(wRec);
		}else{
			System.out.println("# StreamStk # on write # not found:"+wPtn);
		}
	}
	//-------------------------------------------------------------------------
	// バッファを書き出す
	//-------------------------------------------------------------------------
	public void write(String wPtn){
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			mr.write();
		}else{
			System.out.println("# StreamStk # on write # not found:"+wPtn);
		}
	}
	//-------------------------------------------------------------------------
	// バッファに追加する
	//-------------------------------------------------------------------------
	public void append(String wPtn,String wRec){
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			mr.append(wRec);
		}else{
			System.out.println("# StreamStk # on write # not found:"+wPtn);
		}
	}
	//-------------------------------------------------------------------------
	// 書き出した件数を返す
	//-------------------------------------------------------------------------
	public int getWriteCount(String wPtn){
		int wRtn = -1;
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			wRtn = mr.getWriteCount();
		}else{
			System.out.println("# StreamStk # on write # not found:"+wPtn);
		}
		return wRtn;
	}
	//-------------------------------------------------------------------------
	// ＦＩＮＡＬＩＺＥのつもり・・・うまく呼ばれていない
	//-------------------------------------------------------------------------
	@Override
	protected void finalize(){
		System.out.println("# StreamStk # on ■ＦＩＮＡＬＩＺＥ■");
		closeAll();
	}
	//-------------------------------------------------------------------------
	// スタックされているWriterすべての初期化をする・・・だけど・・・
	//-------------------------------------------------------------------------
	public void initAll(String wVal){
		for (Enumeration wenum = ht.elements(); wenum.hasMoreElements() ;) {
			BuffWriter mr = (BuffWriter)wenum.nextElement();
			if ( mr!=null ){
				mr.append(wVal);
			}
		}
	}
	//-------------------------------------------------------------------------
	// バッファーが空ではないWriterをすべて書き出す
	//-------------------------------------------------------------------------
	public void writeAll(){
		for (Enumeration wenum = ht.elements(); wenum.hasMoreElements() ;) {
			BuffWriter mr = (BuffWriter)wenum.nextElement();
			if ( mr!=null ){ mr.write(); }
		}
	}
	//-------------------------------------------------------------------------
	// すべてのバッファーをリセットする
	//-------------------------------------------------------------------------
	public void resetAll(){
		for (Enumeration wenum = ht.elements(); wenum.hasMoreElements() ;) {
			BuffWriter mr = (BuffWriter)wenum.nextElement();
			if ( mr!=null ){ mr.reset(); }
		}
	}
	//-------------------------------------------------------------------------
	// バッファーをリセットする（指定したパターンのみ）
	//-------------------------------------------------------------------------
	public void reset(String wPtn){
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			mr.reset();
		}else{
			System.out.println("# StreamStk # on reset # not found:"+wPtn);
		}
	}
	//-------------------------------------------------------------------------
	// すべてのWriterを閉じる
	//-------------------------------------------------------------------------
	public void closeAll(){
		for (Enumeration wenum = ht.elements(); wenum.hasMoreElements() ;) {
			BuffWriter mr = (BuffWriter)wenum.nextElement();
			if ( mr!=null ){ mr.close(); }
		}
	}
	//#########################################################################
	//# BuffWriter内部クラス
	//#########################################################################
	class BuffWriter{
		String wLS = System.getProperty("line.separator");
		StringBuffer wDefBuf;
		FileWriter fr  = null;
		int writeCount = 0;
		String delimita = "";
		//---------------------------------------------------------------------
		// コンストラクタ
		//---------------------------------------------------------------------
		public BuffWriter(String wPtn,String delimita){
			try{
				fr = new FileWriter(wPtn);
				wDefBuf = new StringBuffer();
			}catch(IOException ie){
				ie.printStackTrace();
			}
			this.delimita = delimita;
		}
		//---------------------------------------------------------------------
		public BuffWriter(String wPtn){
			// 拡張子による、区切り文字の判定
			if (wPtn.indexOf(".")==-1) wPtn = wPtn + ".dat";
			String[] val = wPtn.split("\\.");   // ※ split の引数は Regix 
			String wExt = val[1].toUpperCase(); // ピリオドの直後～（拡張子）
			delimita = "";
			//System.out.println("拡張子は："+wExt);
			if      (wExt.equals("DAT")){ delimita = "";
			}else if(wExt.equals("TXT")){ delimita = "_";
			}else if(wExt.equals("CSV")){ delimita = ",";
			}else if(wExt.equals("PRN")){ delimita = "\t";
			}else{                        delimita = "";
			}
			try{
				fr = new FileWriter(wPtn);
				wDefBuf = new StringBuffer();
			}catch(IOException ie){
				ie.printStackTrace();
			}
		}
		//---------------------------------------------------------------------
		// 何件書き出したか？
		//---------------------------------------------------------------------
		public int getWriteCount() {return writeCount;}
		//---------------------------------------------------------------------
		// 引数の文字列を書き出す
		//---------------------------------------------------------------------
		public void write(String wRec){
			writeCount++;
			try{
				fr.write(wRec);
				fr.write(wLS);     // 改行コード
			}catch(IOException ie){
				ie.printStackTrace();
			}
			reset();
			//wDefBuf.delete(0, wDefBuf.length());
		}
		//---------------------------------------------------------------------
		// バッファーをリセットする
		//---------------------------------------------------------------------
		public void reset(){
			wDefBuf.delete(0, wDefBuf.length());
		}
		//---------------------------------------------------------------------
		// バッファーよりデータを書き出す
		//---------------------------------------------------------------------
		public void write(){
			if(wDefBuf.length()>0) { write(wDefBuf.toString()); }
		}
		//---------------------------------------------------------------------
		// バッファーにデータを追加する
		//---------------------------------------------------------------------
		public void append(String wVal){
			wDefBuf.append(wVal);
			if (!delimita.equals("")) wDefBuf.append(delimita);
		}
		//---------------------------------------------------------------------
		public void close(){
			try{ fr.close();
			}catch(IOException ie){ ie.printStackTrace(); }
		}
	}
}
