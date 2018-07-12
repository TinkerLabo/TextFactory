package kyPkg.socks;
import org.apache.commons.logging.*;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.JList;

import kyPkg.uFile.FileUtil;
//------------------------------------------------------------------------------
//　singletonにした方がよいかも知れない・・・ペンディング
//------------------------------------------------------------------------------
// ftpプログラム Ftp.java
// このプログラムは，ftpサーバと接続してファイル転送を行います
// 使い方 java Ftp サーバアドレス
// 起動の例 java Ftp 10.6.20.7
//------------------------------------------------------------------------------
//※ログを取る方法も考える・・
//※プロキシ経由について考える
//------------------------------------------------------------------------------
// 《 FTPサービスコマンド一覧 》
//   ABOR    実行中のデータ転送の中止
//   ACCT    アカウントの指定
//   ALLO    指定したサイズのファイル領域を転送前に確保する。
//   APPE    既存のファイルに追加。なければ新規作成。
//   CDUP    親ディレクトリの指定
// x CWD     作業ディレクトリの指定
//   DELE    ファイルの削除
// x HELP    ヘルプの表示
// x LIST    ディレクトリ内のファイルリストまたはファイル情報
// x MKD     ディレクトリの作成
//   MODE    転送モードの指定。S:ストリーム B:ブロック C:圧縮
// x NLST    ディレクトリ内のファイルリスト
// x NOOP    何もしない
// x PASS    パスワードの指定
//   PASV    パッシブ・モードの開始
// x PORT    データ転送用のポートの指定
// x PWD     カレント・ディレクトリの表示
// x QUIT    接続終了
//   REIN    新規接続状態に戻る
//   REST    ファイル転送の再開を指定
// x RETR    データ転送開始
// x RMD     ディレクトリの削除
//   RNFR    リネームの対象になるファイルの指定
//   RNTO    新しいファイル名の指定。ＲＮＦＲと対で使う。
// x SITE    特定のサイト用のコマンドの指定
//   SMNT    別のファイルシステム構造のマウント
// x STAT    サーバーの状態
// x STOR    データの受信と保存
//   STOU    受信したファイルをカレントディレクトリで重複しない名前で保存
//   STRU    ファイルストラクチャの指定。F:File R:Record P:Page
// x SYST    サーバーのＯＳタイプの要求
// x TYPE    タイプの指定。ASCII,EBCDIC,IMAGEなど
// x USER    ユーザー名の指定
//------------------------------------------------------------------------------
// Ftpクラス
//------------------------------------------------------------------------------
public class Ftp {
//	public static Log log = LogFactory.getLog("kyPkg.util.Ftp.class");
	public static final String APPENDA_CLASS = "kyPkg.socks";
	public static Log log = LogFactory.getLog(Ftp.APPENDA_CLASS);

	//	log.fatal("fatalメッセージ");
//	log.error("errorメッセージ");
//	log.warn ("warn メッセージ");
//	log.info ("info メッセージ");
//	log.debug("debugメッセージ");
//	log.trace("traceメッセージ");
	private boolean connected = false; 
	private String gOStype = "";
	private String gCDir   = "";
	//private Vector gLVec   = null;
	private String gLs = System.getProperty("line.separator");
	private Socket gSocket;          // 制御　ソケット
	private PrintWriter    stream_o; // 制御　出力用ストリーム
	private BufferedReader stream_i; // 制御　入力用ストリーム
	private final int CTRLPORT = 21; // ftpのWellKnownポート
	private int cPORT = CTRLPORT;
	private CtrlListen ctrllisten = null;
	private Thread listenerthread;
    private JList list_Srv = null;
    //--------------------------------------------------------------------------
	// アクセッサ
	//--------------------------------------------------------------------------
	public void setList_Srv(JList list_Srv) {
		this.list_Srv = list_Srv;
	}
    //--------------------------------------------------------------------------
	// ◎getResponseメソッド
	//--------------------------------------------------------------------------
	private String getResponse(){
		return getResponse(0);
	}
	private String getResponse(int retryMax){
		String wMessage = "";
		String wStat = "";
		int retry = 0;
		while(wStat.equals("")){
			try{
				if (retry > 1) {
					System.out.print(" (-_-).zZ "+retry);
				}
				retry++;
				Thread.sleep(500*retry);// ミリセカンド指定(一秒=1000)
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
			if(retry > retryMax){
				wStat = "999"; // TimeOut（仮）
				System.out.println("## Time Out!! ##");
			}else{
				if (ctrllisten!=null) wStat = ctrllisten.getStatus();
			}
		}
		System.out.println("");
		wMessage = ctrllisten.getMessage();
		return wMessage;
	}
	//--------------------------------------------------------------------------
	// ◎ checkStatメソッド
	// 指定したpStat以上ならエラーということもあるのだ・・・どうする？
	//--------------------------------------------------------------------------
	private boolean checkStat(String stat) {
		String message = getResponse(10);
		if (message.startsWith(stat)){
			//System.out.println("OK        ");
			return true;
		}
		System.out.println("NG is not "+stat+" =>"+message);
		return false;
	}
	private boolean checkStat(String stat1,String stat2) {
		String message = getResponse(10);
		if (message.startsWith(stat1)){
			return true;
		}
		if (message.startsWith(stat2)){
			return true;
		}
		System.out.println("NG is not "+stat1+" or "+stat2+" =>"+message);
		return false;
	}
	//--------------------------------------------------------------------------
	// ◎コマンドを発行する（送り込む）
	//--------------------------------------------------------------------------
	private void sendCommand(String pCommand) {
		//System.out.println("sendCommand:"+pCommand);
		try{
			if (ctrllisten!=null) {
				ctrllisten.resetStat();
				stream_o.println(pCommand);
				stream_o.flush();
			}else{
				log.error("Commandは実行されませんでした、うぅ:"+pCommand);
				System.out.println("Commandは実行されませんでした、うぅ:"+pCommand);
				System.out.println("");
			}
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
	}
	//--------------------------------------------------------------------------
	// ◎dataConnectionメソッド
	// サーバとのデータ交換用にソケットを作ります．
	// また，サーバに対してportコマンドでポートを通知します．
	//--------------------------------------------------------------------------
	private Socket dataConnection(String ctrlcmd) {
		Socket dataSocket = null;	// データ転送用ソケット
		String cmd = "PORT "; //PORTコマンドで送るデータの格納用変数
		int i;
		int port_H;					// ポート番号の上位８ビット
		int port_L;					// ポート番号の下位８ビット
		try{
			//-----------------------------------------------------------------
			// 自分のアドレスを求めます（８ビットずつ格納される・・・）
			// ※コンストラクタでやったほうが良いかも知れない
			//-----------------------------------------------------------------
			byte[] address = InetAddress.getLocalHost().getAddress();
			//-----------------------------------------------------------------
			// リスナーソケット（作成と同時にバインドされる・・ようだ）
			// public ServerSocket(int port,int backlog)
			// port  - 使用するポート。0を指定すると空いているポートが割り当てられる
			// backlog - キューの最大長 (最大接続数？？)
			//-----------------------------------------------------------------
			ServerSocket listener = new ServerSocket(0,1);
			//-----------------------------------------------------------------
			// PORTコマンド用の送信データを用意します
			//-----------------------------------------------------------------
			for(i = 0; i < 4; ++i) cmd = cmd + (address[i] & 0xff) + ",";
			port_H = ((listener.getLocalPort() / 256) & 0xff);
			port_L = ( listener.getLocalPort()        & 0xff);
			cmd = cmd + port_H + "," + port_L;
			// System.out.println("PORTコマンド =>"+cmd);

//			アクティブ (ポート) モード	PORT	サーバー側から接続要求を行う
			// PORTコマンドを制御用ストリームを通して送ります
			sendCommand(cmd);
			if( checkStat("200") ){
				// 処理対象コマンド LIST,RETR,STOR 等をサーバに送ります
				System.out.println("処理対象コマンド=>"+ctrlcmd);
				dataSocket = null;	// データ転送用ソケット

//				sendCommand("PASV");
//				if ( checkStat("227") ){
//					
//				}else{
//					
//				}

				
				sendCommand(ctrlcmd);
				//20160907
//				if ( checkStat("150") ){
				if ( checkStat("150","125") ){
					// サーバからの接続を受け付けます
					dataSocket = listener.accept();
				}else{
					//　450 No files found
					//log.error("<@dataConnection>コマンド失敗"+ctrlcmd);
				}
				listener.close(); //リスナーソケットは廃棄する
			}else{
				log.error("<PORT>portコマンド失敗");
			}
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
		return dataSocket;
	}
	//--------------------------------------------------------------------------
	// ●制御用のソケットを閉じる
	//--------------------------------------------------------------------------
	public void closeConnection() {
		try{
			ctrllisten.quit();
			ctrllisten = null;
			gSocket.close();
		}catch( IOException ie){
			ie.printStackTrace();
		}
		connected = false;
	}
	//--------------------------------------------------------------------------
	// ●どんなタイプのＯＳか
	//--------------------------------------------------------------------------
	public String getOSType() {
		sendCommand("SYST");
		String msg = getResponse();
		if (msg.startsWith("215")){
			gOStype = msg.substring(4);
			System.out.println("gOStype："+gOStype);
			return msg;
		}
		return "";
	}
	//--------------------------------------------------------------------------
	// ●サーバー側の作業ディレクトリを拾う
	//--------------------------------------------------------------------------
	public String getPWD(){
		return getWD();
	}
	public String getWD() {
		sendCommand("PWD");
		String msg = getResponse();
		if (msg.startsWith("257") && msg.indexOf('"',5) > 5 ){
			gCDir = msg.substring(5,msg.indexOf('"',5));
			System.out.println("gCDir："+gCDir);
			return msg;
		}
		return "";
	}
	//--------------------------------------------------------------------------
	// ●ログアウト
	//--------------------------------------------------------------------------
	public void doQuit() {
		log.info("ログアウト");
		if (list_Srv!=null) list_Srv.setListData( new java.util.Vector() );
		sendCommand("QUIT");    // QUITコマンドの送信
		checkStat("221");       // 221 Goodbye
	}
	//--------------------------------------------------------------------------
	// ●シフトＪＩＳ指定　　　for gwhost   quote site setcode s
	//--------------------------------------------------------------------------
	public void doSjis() {
		log.info("シフトＪＩＳ指定");
		sendCommand("SITE setcode s");// Sjis
		checkStat("200");
	}
	//--------------------------------------------------------------------------
	// ●テキスト転送モードに設定
	//--------------------------------------------------------------------------
	public void doAscii() {
		log.info("アスキー指定");
		sendCommand("TYPE A");// Aモード
		checkStat("200");
	}
	//--------------------------------------------------------------------------
	// ●バイナリ転送モードに設定
	//--------------------------------------------------------------------------
	public void doBinary() {
		log.info("バイナリ転送モード指定");
		sendCommand("TYPE I");// Iモード
		checkStat("200");
	}
	//--------------------------------------------------------------------------
	// ●サーバーの状態を拾う
	//--------------------------------------------------------------------------
	public String getSTAT() {
		sendCommand("STAT");
		String msg = getResponse();
		if (msg.startsWith("211")){
			return msg;
		}
		return "";
	}
	//--------------------------------------------------------------------------
	// ●サーバー側が実行可能なコマンドを知りたいときなど
	//--------------------------------------------------------------------------
	public String getHELP() {
		sendCommand("HELP");
		String msg = getResponse();
		if (msg.startsWith("214")){
			return msg;
		}
		return "";
	}
	//--------------------------------------------------------------------------
	// ●デイレクトリの作成
	//--------------------------------------------------------------------------
	public boolean doMKD(String dirName) {
		log.info("MKD " + dirName);
		sendCommand("MKD " + dirName); // MKDコマンド
		return checkStat("257");       // 553 ← File exists.
	}
	//--------------------------------------------------------------------------
	// ●デイレクトリの削除
	//--------------------------------------------------------------------------
	public boolean doRMD(String dirName) {
		log.info("RMD " + dirName);
		sendCommand("RMD " + dirName); // RMDコマンド
		return checkStat("250");
	}
	//--------------------------------------------------------------------------
	// ●ファイルの削除
	//--------------------------------------------------------------------------
	public boolean doDELE(String fileName) {
		log.info("DELE " + fileName);
		sendCommand("DELE " + fileName); // DELEコマンド
		return checkStat("250");	     // 550 No such file or directory.
	}
	//--------------------------------------------------------------------------
	// ●アドレスとポート番号からソケットを作り制御用ストリームを作成します
	//--------------------------------------------------------------------------
	public boolean connect(String host) {
		return connect(host,CTRLPORT);
	}
	public boolean connect(String host,int port) {
		log.info("## CONNECT ##");
		System.out.println("## CONNECT ##");
		try{
			host = host.trim();
			cPORT = port;
//			cPORT = CTRLPORT;
//			if(host.equals("agcisvw")) cPORT = 8021;  // InterScanVirusWall
//			if(host.equals("agcwall")) cPORT = 8021;
			gSocket = new Socket(host, cPORT);
			stream_o = new PrintWriter(gSocket.getOutputStream());
			stream_i = new BufferedReader(new InputStreamReader(gSocket.getInputStream()));
			try {
				System.out.println("## リスナースレッド開始 ##");
				ctrllisten = new CtrlListen(stream_i);
				listenerthread = new Thread(ctrllisten);
				listenerthread.start();
				
				checkStat("220");
				connected = true;
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(NoRouteToHostException ne){
			ne.printStackTrace();
		}catch(UnknownHostException ue){
			ue.printStackTrace();
		}catch(IOException ie){
			ie.printStackTrace();
		}
		return false;
	}
	//--------------------------------------------------------------------------
	// ●ログインする
	// タイムアウトのパラメータとリトライ処理・・・
	//--------------------------------------------------------------------------
	public boolean doLogin(String loginName,String password) {
		log.info("## doLogin ##");
		System.out.println("## doLogin ##");
		loginName = loginName.trim();
		password  = password.trim();   // ん～トリムして良いものやら ＾＾
		sendCommand("USER " + loginName);   	// USERコマンド
		if ( checkStat("331") ){
			sendCommand("PASS " + password);	// PASSコマンド
			if ( checkStat("230") ) return true;
		}
		return false;
	}
	//--------------------------------------------------------------------------
	// ●カレントディレクトリを変更する
	//--------------------------------------------------------------------------
	public boolean doCD(String dirName) { return doCd(dirName);}
	public boolean doCd(String dirName) {
		log.info("CWD " + dirName);
		sendCommand("CWD " + dirName); // CWDコマンド
		return checkStat("250");
	}
	
	//--------------------------------------------------------------------------
	// ●一つ上の階層へ移動
	//--------------------------------------------------------------------------
	public boolean doCDUP() {
		log.info("CDUP");
		sendCommand("CDUP"); // CDUPコマンド
		return checkStat("250");
	}
	
	//--------------------------------------------------------------------------
	// ●doLISTメソッド ディレクトリ情報を得ます．
	// doLIST("NLST");
	// doLIST("LIST");
	//--------------------------------------------------------------------------
	public List<String> doLIST(String pFunk) {
		log.info("doLIST:"+pFunk);
		List<String> wList = null;
		try{
			String wRec;
			StringBuffer wBuff = new StringBuffer(1024);
			int n;
			byte[] buff = new byte[1024];
			Socket dataSocket = dataConnection(pFunk);	// データ用コネクション
			if (dataSocket != null){
				// データ読み取り用ストリームを用意
				BufferedInputStream dataInput = new BufferedInputStream(dataSocket.getInputStream());
				while(( n = dataInput.read(buff) ) > 0){
					//System.out.write(buff,0,n);
					wRec = new String(buff,0,n);
					//System.out.println("【^0^】=>"+wRec);
					wBuff.append(wRec);
				}
				//-------------------------------------------------------------
				String[] anArray = wBuff.toString().split("\n");
				if (anArray != null){
					wList = new ArrayList(anArray.length);
					for (int i=0; i < anArray.length; i++) {
						//System.out.println("【^_^】=>"+anArray[i].trim());
						wList.add(anArray[i].trim());
					}
				}
				//-------------------------------------------------------------
				dataSocket.close();
				checkStat("226");
			}
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
		if (list_Srv!=null) {
			//System.out.println("結果をリストに格納済み");
		    String[] array = (String[]) wList.toArray(new String[wList.size()]);
			list_Srv.setListData( array );
			list_Srv.repaint();
		}
		return wList;
	}
	
	//--------------------------------------------------------------------------
	// doGetメソッド
	// サーバ上のファイルを取り込みます．(ダウンロードする)
	//--------------------------------------------------------------------------
	public boolean doGet(String fileName) {
		String wFS = System.getProperty("file.separator");
		 return doGet(fileName,"."+wFS);
	}
	//--------------------------------------------------------------------------
	// 第二パラメータのおしまいがファイルセパレータの場合はディレクトリが指定されたものとして
	//　サーバー側のファイルと同じファイルがそのディレクトリに転送される。
	//　※出力先のパスに誤りがある場合の処理もあとで考える
	//--------------------------------------------------------------------------
	public boolean doGet(String fileName,String outPath) {
		boolean flg = false;
		String wPath = "";
		outPath= FileUtil.normarizeIt(outPath);
		if (outPath.endsWith("/")){
			wPath = outPath +  fileName;
		}else{
			wPath = outPath;
		}
		log.info("doGet:"+fileName+" to=>"+outPath);
		try{
			int n;
			byte[] buff = new byte[1024];
			// ファイル転送用データストリームを作成します
			Socket dataSocket = dataConnection("RETR " + fileName);
			if (dataSocket != null){
				// クライアント上に受信用ファイルを準備します
				//kkkTest(wPath);
				FileOutputStream outfile = new FileOutputStream(wPath);
				BufferedInputStream dataInput = new BufferedInputStream(dataSocket.getInputStream());
				// サーバからデータを受け取り，ファイルに格納します
				while((n = dataInput.read(buff)) > 0){
					outfile.write(buff,0,n);
				}
				dataSocket.close();
				outfile.close();
				if (checkStat("226")) flg = true;
			}
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
		return flg;
	}
	//--------------------------------------------------------------------------
	// doPutメソッド
	// サーバへファイルを送ります．(アップロードする)
	//--------------------------------------------------------------------------
//	public void kkkTest(String inFileName){
//		String inDir    ="";
//		String fileName = inFileName;
//		String wFS = System.getProperty("file.separator");
//		//　セパレータが無い場合も想定しておこう！！
//		int idx = inFileName.lastIndexOf(wFS)+1; 
//		if  (idx> 0){
//			inDir    = inFileName.substring(0, idx );
//			fileName = inFileName.substring(idx );
//			System.out.println("■inDir　	："+inDir);
//			System.out.println("■fileName	："+fileName);
//		}
//	}
	public boolean doPut(String iFilePath){
		return doPut(iFilePath,false);
	}
	private boolean doPut(String iFilePath,boolean overWrite){
		String oFileName = iFilePath;
		String wFS = System.getProperty("file.separator");
		int idx = iFilePath.lastIndexOf(wFS)+1; //文字がない場合は -1
		if  (idx> 0){
			oFileName = iFilePath.substring(idx );
		}else{ //　セパレータが無い場合
			oFileName = iFilePath;
		}
		return doPut(oFileName,iFilePath,overWrite);
	}
	public boolean doPut(String oFileName,String iFilePath,boolean overWrite) {
		log.info("doPut:"+oFileName+" From=>"+iFilePath);
		if(!new File(iFilePath).exists()) {
			System.out.println("ファイルがありません:"+iFilePath);
			return false;
		}
		boolean flg = false;
		try{
			int n;
			byte[] buff = new byte[1024];
			FileInputStream sendfile = null;
			System.out.println("クライアント上のファイルの読み出し準備を行います");
			try{
				sendfile = new FileInputStream(iFilePath);
			}catch(Exception e){
				System.out.println("ファイルがありません:"+iFilePath);
				return flg;
			}
			System.out.println("転送用データストリームを用意します");
			String pFunk;
			if (overWrite){ // 同名のファイルが或場合上書きされない・・
				pFunk = "STOU " + oFileName;
			}else{        // 同名のファイルが或場合上書きされる
				pFunk = "STOR " + oFileName;
			}
			Socket dataSocket = dataConnection(pFunk);
			if (dataSocket != null){
				OutputStream outstr = dataSocket.getOutputStream();
				System.out.println("ファイルを読み出し，ネットワーク経由でサーバに送ります");
				while((n = sendfile.read(buff)) > 0){
					outstr.write(buff,0,n);
				}
				dataSocket.close();
				if (checkStat("226")) flg = true;
			}
			sendfile.close();
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
		return flg;
	}
	//##########################################################################
	// ●標準入力より入力を促し、受取る
	//  例＞ String dirName = getUserInput("ディレクトリ名を入力して下さい．");
	//##########################################################################
	public String getUserInput(String pMessage) {
		try{
			BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
			System.out.println(pMessage);
			return lineread.readLine();
		}catch(Exception e)	 {
			e.printStackTrace();
			return "";
		}
	}
	//##########################################################################
	// ●getStdinメソッド
	// 利用者の指定したコマンド番号を読み取ります．
	//##########################################################################
	public String getStdin() {
		String buf = "";
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		while(buf.length() != 1){    // 1文字の入力を受けるまで繰り返し
			try{
				buf = lineread.readLine();
			}catch(Exception e) {
				e.printStackTrace();
				//System.exit(1);
			}
		}
		return (buf);
	}
	//##########################################################################
	// ●talkWith 対話によるテスト・・・
	//##########################################################################
	public void talkWith() {
		String wCommand = getUserInput("コマンドを入力して下さい．");
		sendCommand(wCommand);
	}
	//##########################################################################
	// ●doCdのラッパー
	//##########################################################################
	public void doCd() {
		String dirName = getUserInput("ディレクトリ名を入力して下さい．");
		doCd(dirName);
	}
	//##########################################################################
	// ●doGetのラッパー
	//##########################################################################
	public void doGet() {
		String dirName = getUserInput("ファイル名を入力して下さい．");
		doGet(dirName);
	}
	//##########################################################################
	// ●doPutのラッパー
	//##########################################################################
	public void doPut() {
		String dirName = getUserInput("ファイル名を入力して下さい．");
		doGet(dirName);
	}
	//##########################################################################
	// ●doLoginのラッパー
	//##########################################################################
	public boolean doLoginUI(String user,String pass) {
		if( user == null) user = getUserInput("ログイン名を入力して下さい．");
		if( pass == null) pass = getUserInput("パスワードを入力して下さい．");
		return doLogin(user,pass);
	}
	//##########################################################################
	// ●loopメソッド
	//   Ftpのコマンドメニューを出力して，各処理を呼び出します．
	//##########################################################################
	public void loop(String host, int port,String user,String pass) {
		boolean wCtrl = true;
		try {
			if (connect(host,port)==false) {
				//System.exit(1);       // 制御用コネクション
				return;
			}
			if (doLoginUI(user,pass)==false){
				//System.exit(1);  // 制御用コネクション
				return;
			}
			while(wCtrl){
				System.out.print(">? 2 ls 3 cd 4 get 5 put 6 ascii 7 binary 8 help 9 quit "+gLs);
				switch(Integer.parseInt(getStdin())){
					//case 0 : doRecv()  ;     break; // レスポンスを読み込む
					case 1 : talkWith();     break; // コマンドを送る
					case 2 : doLIST("NLST"); break; // サーバのディレクトリ表示処理
					case 3 : doCd()    ;     break; // サーバの作業ディレクトリ変更処理
					case 4 : doGet()   ;     break; // サーバからのファイル取得処理
					case 5 : doPut()   ;     break; // サーバへのファイル転送処理
					case 6 : doAscii() ;     break; // テキスト転送モード
					case 7 : doBinary();     break; // バイナリ転送モード
					case 8 : getHELP()  ;     break; // HELP
					case 9 : doQuit()  ;     wCtrl = false; break;// 処理の終了
					default : System.out.println("番号を選択して下さい");
				}
			}
			closeConnection();
		} catch(Exception e) {
			System.err.print(e);
			//System.exit(1);
		}
	}
	//##########################################################################
	// ● mmBatch << MacroMill >> Batch File
	//##########################################################################
	public static void mmFTP_GetxxxNotUsedxxxx(
			String outDir,
			String host,
			int port,
			String user,
			String pass,
			String dirName,
			String regex
			) {
		String wOutDir = FileUtil.mkdir(outDir);
		Ftp ins = new Ftp();
		log.info("<connect>------------------------------------------");
		System.out.println("<connect>------------------------------------------");
		if (!ins.connect(host,port)){ // 制御用コネクション
			//System.exit(1);
			return;
		}
		log.info("<doLogin>------------------------------------------");
		System.out.println("<doLogin>------------------------------------------");
		if (!ins.doLogin(user,pass)){
			//System.exit(1);  // login
			return;
		}
		log.info("<getOSType()>------------------------------------------");
		System.out.println("<getOSType()>-----------------------------------------");
		ins.getOSType();
		log.info("<getWD()>------------------------------------------");
		System.out.println("<getWD()>------------------------------------------");
		ins.getWD();

//		System.out.println("<doCd(/)>------------------------------------------");
//		doCd("/") ;            // サーバの作業ディレクトリ変更処理

		log.info("<doAscii()>------------------------------------------");
		System.out.println("<doAscii()>-----------------------------------------");
		ins.doAscii() ;          // テキスト転送モード
		//ins.doBinary();          // バイナリ転送モード

		List gLVec = null;
		Iterator it  = null;
		if (!dirName.trim().equals("")){
			ins.doCD(dirName);
		}
		gLVec = ins.doLIST("LIST");  // これを見せる
		if(gLVec.size()>0){
			if(gLVec.contains(".")) gLVec.remove(".");
			it = gLVec.iterator();
			while(it.hasNext())	{
				System.out.println("=>"+it.next());
			}
		}
		gLVec = ins.doLIST("NLST");  // これを見せる
		if(gLVec.size()>0){
			if(gLVec.contains(".")) gLVec.remove(".");
			it = gLVec.iterator();
			while(it.hasNext())	{
				String wFile = it.next().toString();
				if (wFile.matches(regex)){
					// サーバからのファイル取得処理
					System.out.println("<doGet(" + wFile + ")>-------------------------------------");
					if (!ins.doGet(wFile,wOutDir)){
						System.out.println("■■■Error!■■■"+ wFile);
						//mail処理など・・・
						//何が原因なのか、手がかりを残しておく
						//logの処理が必要だ
					};      
				}else{
					System.out.println("<doGet unMatched(" + wFile + ")>-------------------------------------");
				}
			}
		}
		System.out.println("<doQuit()>-----------------------------------------");
		ins.doQuit()  ;            // 処理の終了
		System.out.println("---------------------------------------------------");
		ins.closeConnection();
	}
	//##########################################################################
	// ● BATCH処理例
	//##########################################################################
	public static void sample(String host,int port,String user,String pass) {
		Ftp ins = new Ftp();
		System.out.println("<connect>------------------------------------------");
		if (!ins.connect(host,port)){ // 制御用コネクション
			//System.exit(1);
			return;
		}
		System.out.println("<doLogin>------------------------------------------");
		if (!ins.doLogin(user,pass)){
			//System.exit(1);  // login
			return;
		}
		System.out.println("<getOSType()>-----------------------------------------");
		ins.getOSType();
		System.out.println("<getWD()>------------------------------------------");
		ins.getWD();

		List gList = ins.doLIST("LIST");  // これを見せる
		System.out.println("size:"+gList.size());
		System.out.println("contains(null):"+gList.contains("."));
		gList.remove(".");
		Iterator it = gList.iterator();
		while(it.hasNext())	System.out.println("=>"+it.next());

		//System.out.println("<doLIST(NLST)>-------------------------------------");
		//doLIST("NLST");        // サーバのディレクトリ表示処理
		//System.out.println("<doLIST(LIST)>-------------------------------------");
		//doLIST("LIST");        // サーバのディレクトリ表示処理

		//System.out.println("<doDELE(Bear.txt)>----------------------------------");
		//doDELE("Bear.txt");

		System.out.println("<doDELE(BEE.txt)>----------------------------------");
		ins.doDELE("BEE.txt");

		System.out.println("<doPut(Bear.txt)>----------------------------------");
		ins.doPut("Bear.txt");     // サーバへのファイル転送処理

		System.out.println("<doMKD(DUBWISE)>----------------------------------");
		ins.doMKD("DUBWISE");

		//System.out.println("<doRMD(DUBWISE)>----------------------------------");
		//doRMD("DUBWISE");

//		System.out.println("<doCd(/)>------------------------------------------");
//		doCd("/") ;            // サーバの作業ディレクトリ変更処理

		System.out.println("<doLIST(LIST)>-------------------------------------");
		ins.doLIST("LIST");        // サーバのディレクトリ表示処理
		System.out.println("<doGet(.bashrc)>-------------------------------------");
		ins.doGet(".bashrc");      // サーバからのファイル取得処理

		//doAscii() ;          // テキスト転送モード
		//doBinary();          // バイナリ転送モード
		System.out.println("<getHELP()>-----------------------------------------");
		ins.getHELP()  ;            // HELP

		System.out.println("<doCDUP()>-----------------------------------------");
		ins.doCDUP();

		System.out.println("<getWD()>------------------------------------------");
		ins.getWD();

		//System.out.println("<getSTAT()>-----------------------------------------");
		//getSTAT();

		System.out.println("<doQuit()>-----------------------------------------");
		ins.doQuit()  ;            // 処理の終了
		System.out.println("---------------------------------------------------");
		ins.closeConnection();
	}
	//--------------------------------------------------------------------------
	// ●mainメソッド
	// TCPコネクションを開いて処理を開始します
	//--------------------------------------------------------------------------
	public static void main(String[] arg){
        System.setErr(System.out);
		try {
			System.out.println("arg.length:"+arg.length);
			if(arg.length < 1){
				System.out.println("usage: java Ftp <host name>");
				return;
			}
//			String host = arg[0];
//			Ftp f = new Ftp();
			if (arg.length >= 2){
				//f.loop(host,arg[1],arg[2]);
				//sample(host,arg[1],arg[2]);
//				mmBatch("192.168.1.2","ken","tonyhika","","reg" );
				// for PowerMac
				//mmBatch("10.6.20.7","ken","anthony","","qpr_.*_.*_20\\d*\\.txt" );
				// for macroMill
			}else{
				//f.loop(host,null,null);
			}
			System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	public boolean isConnected() {
		return connected;
	}
}
//------------------------------------------------------------------------------
// ●CtrlListen クラス メッセージを非同期に読み込む・・・
//------------------------------------------------------------------------------
class CtrlListen implements Runnable{
//	public static Log log = LogFactory.getLog("kyPkg.util.CtrlListen");
	public static Log log = LogFactory.getLog(Ftp.APPENDA_CLASS);

	private LinkedList gStat_Que = new LinkedList();   // シンクロナイズさせる？！
	private LinkedList gMsg_Que = new LinkedList();   // シンクロナイズさせる？！
	private String gLs = System.getProperty("line.separator");
	private BufferedReader stream_i = null;
	private boolean gCtrl = true;
	private String message = "";
	private String status  = "";;
	//----------------------------------------------------------------------
	// アクセッサ
	//----------------------------------------------------------------------
	public String getStatus(){
		status  = "";
		message = "";
		if (gCtrl){
			if (gStat_Que.size() > 0){
				status  = (String)gStat_Que.removeFirst();
				message = (String)gMsg_Que.removeFirst();
				//System.out.println("\n message :" + message );
				//log.info("@CtrlListen  :"+message);
			}
		}
		return status;
	}
	//----------------------------------------------------------------------
	public String getMessage(){
		return message;
	}
	//----------------------------------------------------------------------
	public String getEmessage(){
//		Emsg = CnvRtnToMsg(stat);
//		System.out.print("\n Emsg:" + Emsg);
		return CnvRtnToMsg(status);
	}
	//----------------------------------------------------------------------
	// コマンド発行前に呼ぶ
	//----------------------------------------------------------------------
	public void resetStat(){
		gStat_Que.clear();
		gMsg_Que.clear();
	}
	//----------------------------------------------------------------------
	// コンストラクタ 読み取り先の指定
	//----------------------------------------------------------------------
	public CtrlListen(BufferedReader in){
		stream_i = in;
	}
	//----------------------------------------------------------------------
	// 終了させる
	//----------------------------------------------------------------------
	public void quit(){
		System.out.println("■quit■");
		gCtrl = false;
	}
	//----------------------------------------------------------------------
	// QUITするとSocketException が発生してしまうのだが・・・どうしよう？
	//----------------------------------------------------------------------
	@Override
	public void run(){
		StringBuffer wBuff;
		String wRec = "";
		while(gCtrl){
			try{
				//-------------------------------------------------------------
				// コネクトしているあいだずっとループするのだが、これで問題ないだろうか？！
				// なんか間違えている気もするのだが・・・・接続が切れたら確実にブレークさせねば・・・
				// System.out.println("■gCtrl■"+gCtrl);
				//-------------------------------------------------------------
				wRec = stream_i.readLine();
				if (wRec!=null){
					log.info("(@_@)"+message);
					String Stat = wRec.substring(0,3);
					synchronized(gStat_Que){
						gStat_Que.add(Stat);
					}
					// System.out.println("■=>"+wRec);
					wBuff = new StringBuffer(1024);
					wBuff.append(wRec);
					if(wRec.substring(3,4).equals("-")){
						wRec = "";
						while(!wRec.startsWith(Stat)){
							wRec = stream_i.readLine();
							wBuff.append(gLs);
							wBuff.append(wRec);
							//System.out.print("●=>+wRec);
						}
					}
					synchronized(gMsg_Que){
						gMsg_Que.add(wBuff.toString());
					}
					//System.out.print("　=>"+gMsg_Que.getLast());
				}
			} catch (java.net.SocketException se){
				//se.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
				//System.exit(1);
			}
		}
	}
	//--------------------------------------------------------------------------
	//●ftpのリプライコードに対応するメッセージを表示します。
	//--------------------------------------------------------------------------
	String CnvRtnToMsg(String pCode){
		// System.out.println("(^_^)");
		int nCode = Integer.parseInt(pCode);
		String rMsg = "";
		switch(nCode) {
			case 110:	rMsg = " リスタートマーカ応答　　　　　　　　　　　　　　　　　　　　　　　";	break;
			case 120:	rMsg = " サービスはnnn 分で準備ができます。　　　　　　　　　　　　　　　　";	break;
			case 125:	rMsg = " データ接続はすでにオープンしました。転送は開始しています　　　　　";	break;
			case 150:	rMsg = " ファイルステータスは良好です。データ接続をオープンします　　　　　";	break;
			case 200:	rMsg = " コマンドはＯＫです　　　　　　　　　　　　　　　　　　　　　　　　";	break;
			case 211:	rMsg = " システムステータス、またはシステムヘルプ応答　　　　　　　　　　　";	break;
			case 212:	rMsg = " ディレクトリステータス　　　　　　　　　　　　　　　　　　　　　　";	break;
			case 213:	rMsg = " ファイルステータス　　　　　　　　　　　　　　　　　　　　　　　　";	break;
			case 214:	rMsg = " ヘルプメッセージ　　　　　　　　　　　　　　　　　　　　　　　　　";	break;
			case 215:	rMsg = " ＮＡＭＥシステムサービス　　　　　　　　　　　　　　　　　　　　　";	break;
			case 220:	rMsg = " 新規ユーザー用サービスが準備できています　　　　　　　　　　　　　";	break;
			case 221:	rMsg = " サービスはコントロール接続を閉じます。適当ならログアウトします　　";	break;
			case 225:	rMsg = " データ接続は開いています。進行中の転送はありません　　　　　　　　";	break;
			case 226:	rMsg = " データ接続をクローズします。要求されたファイル処理は完了しました　";	break;
			case 227:	rMsg = " パッシブモードに入ります。　　　　　　　　　　　　　　　　　　　　";	break;
			case 230:	rMsg = " ユーザがログインしました、先へ進みます　　　　　　　　　　　　　　";	break;
			case 250:	rMsg = " 要求されたファイル処理が正常終了しました。　　　　　　　　　　　　";	break;
			case 257:	rMsg = " PATHNAMEが作成されました　　　　　　　　　　　　　　　　　　　　　";	break;
			case 331:	rMsg = " ユーザ名はＯＫです、パスワードが必要です　　　　　　　　　　　　　";	break;
			case 332:	rMsg = " ログイン用のアカウントが必要です　　　　　　　　　　　　　　　　　";	break;
			case 350:	rMsg = " 要求されたファイル処理は、追加情報を持っています　　　　　　　　　";	break;
			case 421:	rMsg = " サービスは利用できません、コントロール接続を閉じます　　　　　　　";	break;
			case 425:	rMsg = " データ接続をオープンできません　　　　　　　　　　　　　　　　　　";	break;
			case 426:	rMsg = " 接続がクローズされました、転送が打ち切られました　　　　　　　　　";	break;
			case 450:	rMsg = " 要求されたファイル処理が実行されませんでした、ファイルが使用不可能";	break;
			case 451:	rMsg = " 要求された行動が打ち切られました、ローカルの処理エラーです　　　　";	break;
			case 452:	rMsg = " 要求された行動は実行されませんでした、システムの記憶域不足　　　　";	break;
			case 500:	rMsg = " シンタックスエラー、コマンドが認識されませんでした　　　　　　　　";	break;
			case 501:	rMsg = " パラメータあるいは引数のシンタックスエラー　　　　　　　　　　　　";	break;
			case 502:	rMsg = " コマンドが実装されていません　　　　　　　　　　　　　　　　　　　";	break;
			case 503:	rMsg = " コマンドのシーケンスが間違っています　　　　　　　　　　　　　　　";	break;
			case 504:	rMsg = " このコマンドパラメータは実装されていません　　　　　　　　　　　　";	break;
			case 530:	rMsg = " ログインに失敗しました　　　　　　　　　　　　　　　　　　　　　　";	break;
			case 532:	rMsg = " ファイル格納用のアカウントが必要です　　　　　　　　　　　　　　　";	break;
			case 550:	rMsg = " 要求は実行されませんでした、ファイルが使用不可能です　　　　　　　";	break;
			case 551:	rMsg = " 要求は打ち切られました、ページタイプが不明です　　　　　　　　　　";	break;
			case 552:	rMsg = " 要求は打ち切られました、記憶割り当てが超過しました　　　　　　　　";	break;
			case 553:	rMsg = " 要求は実行されませんでした、ファイル名が不当です　　　　　　　　　";	break;
			default:	rMsg = " 未定義なメッセージ　　　　　　　　　　　　　　　　　　　　　　　　";	break;
		}
		return rMsg;
	}
}
