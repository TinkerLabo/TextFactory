package kyPkg.socks;

import static kyPkg.uFile.ListArrayUtil.file2List;
import java.net.*;
import java.io.*;
import java.util.*;
import globals.AGC_SMTP;
import globals.ResControl;

//----------------------------------------------------------------------------------------
// 《メール送信プログラム》
// 外部コマンドの実行ログをそのままメール転送してみる
// ログは一旦ファイルにしておく
// ということで・・送信先、タイトル、ファイル名を指定することでメール送信する
// プログラムとする
//----------------------------------------------------------------------------------------
public class SMTPmini {
	private static final String DEFAULT_EMAIL = "mi-gotoh@tokyu-agc.co.jp"; //TODO	メーリングリストのアカウントにしてもらえればベストか
	private static final String ARG_ERROR = "#Error 引数が３つ必要です =>";
	private static final String SAMPLE = "例＞java -jar SMTPmini.jar emailAddr subject MailFilePath　";
	private Socket sock;
	private BufferedReader smtpIn;
	private PrintWriter smtpOut;

	// -----------------------------------------------------------------------------------
	// setPortAndServer サーバー名およびポート番号を外部化（ＭＴＡをyml化）
	// -----------------------------------------------------------------------------------
	// 20150309　MTA変更に伴う作業　 SMTP_SERVER
	// がnullなら外部ファイルを参照する、ファイルが存在しなければデフォルトを設定する
	// -----------------------------------------------------------------------------------
	// コンストラクタ
	// -----------------------------------------------------------------------------------
	public SMTPmini(String frm_email, String to_email, String subject,
			List msgBody) {
		// XXX ｃｃができるようにしたい
		this(frm_email, Arrays.asList(to_email.split(",")), subject, msgBody);
		//#createTester--------------------------------------------------
		System.out.println("public static void testSMTPmini() {");
		System.out.println("    String to_email = \"" + to_email + "\";");
		System.out.println("    String subject = \"" + subject + "\";");
		System.out.println("    List msgBody = " + msgBody + ";");
		System.out.println(
				"    SMTPmini ins = new SMTPmini(to_email,subject,msgBody);");
		System.out.println("}");
		//--------------------------------------------------
	}

	// -----------------------------------------------------------------------------------
	// メッセージは外部ファイルを使用する版
	// -----------------------------------------------------------------------------------
	public SMTPmini(String frm_email, String to_email, String subject,
			String path) {
		this(frm_email, Arrays.asList(to_email.split(",")), subject,
				file2List(path));
	}

	// -----------------------------------------------------------------------------------
	// 宛先アドレス 複数可
	// -----------------------------------------------------------------------------------
	private SMTPmini(String frm_email, List addrList, String subject,
			List msgBody) {
		if (addrList.size() > 0) {
			try {
				session(frm_email, subject, addrList, msgBody);
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	// -----------------------------------------------------------------------------------
	// sendメソッド 送信
	// -----------------------------------------------------------------------------------
	private void send(String command) {
		smtpOut.print(command + "\r\n"); // コマンドの送信
		smtpOut.flush();
		// System.out.println("send> " + command); // 送信内容の表示
	}

	// -----------------------------------------------------------------------------------
	// recvメソッド 受信
	// -----------------------------------------------------------------------------------
	private boolean recv(int success_code) {
		try {
			String res = smtpIn.readLine(); // 応答コードの読み取り
			// System.out.println("recv> " + res);
			if (Integer.parseInt(res.substring(0, 3)) != success_code) {
				sock.close();// コネクションを閉じます
				throw new RuntimeException(res);
			}
		} catch (IOException ie) {
			ie.printStackTrace();
			System.out.println("コマンド受信時ioエラー：");
			// throw new RuntimeException(res);
			return false;
		}
		return true;
	}

	// -----------------------------------------------------------------------------------
	// SMTPのセッション
	// -----------------------------------------------------------------------------------
	private void session(String frm_email, String subject, List<String> toAddr,
			List<String> msgBody) throws IOException {
		if (msgBody == null) {
			msgBody = new ArrayList();
			msgBody.add("#ERROR message Body is NULL...!?");
		}
		String myname = "";
		try {
			myname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException ue) {
			ue.printStackTrace();
			return;
		}
		// -------------------------------------------------------------------------------
		// CONNECT
		// -------------------------------------------------------------------------------
		sock = new Socket(AGC_SMTP.getHost(), AGC_SMTP.getPort());
		smtpIn = new BufferedReader(
				new InputStreamReader(sock.getInputStream()));
		smtpOut = new PrintWriter(sock.getOutputStream());
		if (recv(220) == false) {
			System.out.println("接続エラー");
			return;
		}
		// -------------------------------------------------------------------------------
		// HELOコマンド 送信側コンピュータ名の通知
		// -------------------------------------------------------------------------------
		// send("HELO HOST.COM");
		send("HELO " + myname);
		recv(250);
		// -------------------------------------------------------------------------------
		// MAIL FROMコマンド 返信先を相手に通知
		// -------------------------------------------------------------------------------
		send("MAIL FROM:" + frm_email);
		recv(250);
		// -------------------------------------------------------------------------------
		// RCPT TOコマンド メール受信者の指定
		// -------------------------------------------------------------------------------
		for (Iterator iterator = toAddr.iterator(); iterator.hasNext();) {
			String mailAddr = (String) iterator.next();
			if (mailAddr == null)
				mailAddr = "";
			mailAddr = mailAddr.trim();
			if (!mailAddr.equals("")) {
				send("RCPT TO:" + mailAddr);
				recv(250);
			}
		}
		// for (int i = 0; i < toAddr.length; i++) {
		// send("RCPT TO:" + toAddr[i]);
		// recv(250);
		// }
		// -------------------------------------------------------------------------------
		// DATAコマンド メールデータ送信開始の通知
		// -------------------------------------------------------------------------------
		send("DATA");
		recv(354);
		// -------------------------------------------------------------------------------
		// Mail_Header
		// -------------------------------------------------------------------------------
		send("From: ## QPR Message ####### <" + frm_email + "> ");
		// send("To: " + to[0]);

		//2016-07-20 ccを実装してみる・・・これでいいのだろうか 
		send("To:" + toAddr.get(0));
		for (int i = 1; i < toAddr.size(); i++) {
			send("CC:" + toAddr.get(i));
		}

		send("Subject:" + subject);
		send("MIME-Version: 1.0");
		send("X-Mailer: P.E.A.C.E. mail ver.777 ");
		send("Content-Type: text/plain; charset=iso-2022-jp");
		send(""); // ヘッダの後には空白の行が必要
		// -------------------------------------------------------------------------------
		// Mail_Body
		// -------------------------------------------------------------------------------
		for (Iterator iterator = msgBody.iterator(); iterator.hasNext();) {
			String message = (String) iterator.next();
			send(message);
		}
		// for(int i = 0;i < msgBody.length;i++) {
		// send(msgBody[i]);
		// }
		send("\r\n.");
		recv(250); // メールメッセージの終了
		// -------------------------------------------------------------------------------
		// QUITコマンド セッションの終了
		// -------------------------------------------------------------------------------
		send("QUIT");
		recv(221);
		// -------------------------------------------------------------------------------
		// コネクションのクローズ
		// -------------------------------------------------------------------------------
		sock.close();
		System.out.println("<fin>");
	}

	// -----------------------------------------------------------------------------------
	// mainメソッド 《このクラスの使用例》
	// -----------------------------------------------------------------------------------
	public static void main(String[] args) {
		//		testSMTPmini1(); //for test
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SMTPmini Elapse");
		elapse.start();
		kickAsBatch(args);
		elapse.setComment("COMMENT");
		elapse.stop();
	}

	public static void kickAsBatch(String[] args) {
		if (args.length < 3) {
			System.out.println(ARG_ERROR + SAMPLE);
			System.exit(9);
		}
		String frm_email = DEFAULT_EMAIL;
		if (args.length == 4) {
			frm_email = args[3];
		}
		new kyPkg.socks.SMTPmini(frm_email, args[0], args[1], args[2]);
	}

	// -----------------------------------------------------------------------------------
	// XXX 外部のテスト用クラスに以下のメソッドを移動させておく
	// testSMTPmini1();
	// testSMTPmini2();
	// -----------------------------------------------------------------------------------
	public static void testSMTPmini2() {
		String mailTo = DEFAULT_EMAIL; // 送信先メールアドレス（臨時）
		String mailFrm = DEFAULT_EMAIL; // 送信先メールアドレス（臨時）
		String subject = "苺のお手紙２３";
		String path = ResControl.D_QPR + "LOG/ITEM_MM_clan.log";
		new kyPkg.socks.SMTPmini(mailFrm, mailTo, subject, path);
	}

	public static void testSMTPmini1() {
		String mailAddr1 = DEFAULT_EMAIL; // 送信先メールアドレス（臨時）
		List<String> mailList = new ArrayList();
		mailList.add(mailAddr1);
		String subject = "苺のお手紙２３Ｌｉｒｉｃｓ";
		String[] msgBody = { "苺のお手紙２３　 ", "", "Hello, my love",
				"I heard a kiss from you", "Red magic satin playing near, too",
				"All through the morning rain",
				"I gaze - the sun doesn't shine -",
				"Rainbows and waterfalls run through my mind",
				"In the garden - I see west", "Purple shower, bells and tea",
				"Orange birds and river cousins dressed in green",
				"Pretty music I hear - so happy", "And loud - blue flower echo",
				"From a cherry cloud", "Feel sunshine sparkle pink and blue",
				"Playgrounds will laugh", "if you try to ask",
				"Is it cool?, is it cool?", "If you arrive and don't see me",
				"I'm going to be with my baby",
				"I am free - flying in her arms, over the sea",
				"Stained window, yellow candy screen",
				"See speakers of kite - with velvet roses diggin' freedom flight",
				"A present from you - Strawberry letter 22",
				"The music plays, I sit in for a few",
				"Ooh...ooh...ooh...ooh...ooh...",
				"A present from you - Strawberry letter 22",
				"The music plays, I sit in for a few",
				"Ooh...ooh...ooh...ooh...ooh...", "\n\n", "\t\tＢｙ　シュギー・オーティス" };
		new kyPkg.socks.SMTPmini(mailAddr1, mailList, subject,
				Arrays.asList(msgBody));
	}
}
