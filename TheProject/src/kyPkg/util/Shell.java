package kyPkg.util;

import java.io.*;
import java.util.*;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.uCodecs.UnicodeMod;

//---------------------------------------------------------------------------------
// 実行環境でのシエルコマンドを実行する
//---------------------------------------------------------------------------------
public class Shell {
	private static boolean DEBUG = false;

	// private static boolean DEBUG = true;
	/***************************************************************************
	 * インナークラス (XShellで使用)プロセスのタイムアウトを監視する
	 **************************************************************************/
	// ***********************************************************************
	// * 《使用例》
	// * Process proc = Runtime.getRuntime().exec("notepad");
	// * ElapseCtrl ec = new ElapseCtrl((1000*30),proc);
	// **********************************************************************/
	public class ElapseCtrl extends Thread {
		private Process process;
		private long beginT = 0; // longのデフォルトは何だっけね？？
		private long timeout = 1000 * 30; // とりあえず30秒とする
		private long elapse = 0;
		private boolean alive = true;

		public ElapseCtrl(Process proc) {
			this(1000 * 30, proc);
		}

		public ElapseCtrl(int timeout, Process proc) {
			// もしprocがヌルだったらどうする？？
			this.process = proc;
			this.timeout = timeout;
			this.beginT = System.currentTimeMillis();
			if (DEBUG)
				System.out.println("# this.timeout:" + this.timeout);
			this.start();
			if (DEBUG)
				System.out.println("# check point1");
		}

		// タイムアウト前に処理が終わった場合にこれをコールする
		public long quit() {
			elapse = System.currentTimeMillis() - beginT;
			alive = false;
			// this.interrupt(); // どちらがよいだろうか？
			return elapse;
		}

		// タイムアウトを監視させる
		@Override
		public void run() {
			if (DEBUG)
				System.out.println("# check point2 Run!");
			while (alive) {
				elapse = System.currentTimeMillis() - beginT;
				if (DEBUG)
					System.out.println("# in Loop elapse:" + elapse);
				if (process == null)
					return;
				if (elapse > this.timeout) {
					System.out.println("# TimeOUT!! " + elapse);
					alive = false;
					process.destroy(); // 終了させられない場合もある
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
					return;
				}
			}
		}

		public long getElapse() {
			return elapse;
		}
	}

	/***************************************************************************
	 * (XShellで使用)プロセスの標準出力の受けざら Runtime#exec(String)メソッドでプロセスを走らせる際にその
	 * getInpuStream や getErrorStream を別スレッド処理で非同期に読み取る （読まないと外部プロセスが停止してハングする）
	 **************************************************************************/
	class AutoReader extends Thread {
		InputStream stream;

		StringBuffer sBuff;

		AutoReader(InputStream sr) {
			stream = sr;
		}

		@Override
		public void run() {
			// System.out.println("## AutoReader ##");
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(stream));
				sBuff = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null) {
					sBuff.append(line).append("\n");
				}
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}

		public String getBuff() {
			return sBuff.toString();
		}
	}

	/***************************************************************************
	 * (XShellで使用)プロセスの標準出力の受けざら 入力待ちを回避するのに使用できないか・・・ 《使用例》 AutoWriter
	 * sOtThread = new AutoWriter(proc.getOutputStream());
	 **************************************************************************/
	class AutoWriter extends Thread {
		OutputStream stream;

		BufferedWriter bw;

		String[] message = null;

		public void setMessage(String message) {
			this.setMessage(new String[] { message });
		}

		public void setMessage(String[] message) {
			this.message = message;
		}

		AutoWriter(OutputStream sr) {
			stream = sr;
			bw = new BufferedWriter(new OutputStreamWriter(stream));
		}

		@Override
		public void run() {
			// System.out.println("## AutoWriter ##");
			if (bw == null) {
				System.out.println("AutoWriter:oBw is Null");
				return;
			}
			try {
				for (int i = 0; i < message.length; i++) {
					if (DEBUG)
						System.out.println("AutoWriter　<message>" + message[i]
								+ "</message>");
					bw.write(message[i], 0, message[i].length());
					// oBw.flush();
				}
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}

		public void close() {
			try {
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/***************************************************************************
	 * 外部コマンドを別スレッドで実行する<br>
	 * 《使用例》 cmd.exe ipconfig cmd.exe net use
	 **************************************************************************/
	private class XShell extends Thread {
		private Process proc; // プロセス

		private AutoWriter stiWriter = null;

		private AutoReader stoReader = null;

		private AutoReader errreader = null;

		// ---------------------------------------------------------------------
		private String command = ""; // 文字配列にしたいが・・・どうでしょうね

		private String stdout = ""; // 標準データ出力に掃き出された内容

		private String stderr = ""; // 標準エラー出力に掃き出された内容

		// private int status;

		// ---------------------------------------------------------------------
		// アクセッサ
		// ---------------------------------------------------------------------
		public String getStdout() {
			return stdout;
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		// ---------------------------------------------------------------------
		// コンストラクタ
		// ---------------------------------------------------------------------
		// public XShell() {
		// super();
		// }
		// public XShell(String command) {
		// super();
		// this.command = command;
		// }

		public XShell(ThreadGroup group, String name) {
			super(group, name);
		}

		// public String getStderr() {
		// return stderr;
		// }
		// public int getStatus() {
		// return status;
		// }
		// //
		// ---------------------------------------------------------------------
		// // プロセスの中断（できないこともあるらしい・・）
		// //
		// ---------------------------------------------------------------------
		// public void destroyProccess() {
		// if (proc != null) {
		// System.out.println("■DESTOROY");
		// proc.destroy();
		// } else {
		// System.out.println("■プロセスが見つかりませんでした");
		// }
		// }

		// ---------------------------------------------------------------------
		@Override
		public void run() {
			try {
				// ここでコマンドが空かどうか確認したほうがよいか？
				if (DEBUG)
					System.out.println("<<@Shell>>■command:" + command);
				// プロセス実行前に空きメモリを確認するほうがよいだろうか？？
				proc = Runtime.getRuntime().exec(command);
				// System.out.println("# プロセスを監視 start");
				// プロセスを監視、タイムアウトした場合強制終了させる
				ElapseCtrl ec = new ElapseCtrl((1000 * 30), proc);
				// プロセスの標準入出力ストリームを取得
				stoReader = new AutoReader(proc.getInputStream()); // 標準出力
				errreader = new AutoReader(proc.getErrorStream()); // 標準エラー
				stiWriter = new AutoWriter(proc.getOutputStream());// 標準入力
				// 入力待ち状態になるのを検知できないか？＆回避できないか検討する
				stiWriter.setMessage("\n");// 空送信
				// System.out.println("■ start() ■");
				stiWriter.start();
				stoReader.start();
				errreader.start();
				// System.out.println("■ join() ■");
				stoReader.join(); // 標準 出力の読み込みスレッドを待つ
				errreader.join(); // エラー出力の読み込みスレッドを待つ
				stiWriter.join(); // エラー出力の読み込みスレッドを待つ
				// System.out.println("■proc.waitFor before ");
				proc.waitFor(); // プロセスの終了を待つ ...
				long elapse = ec.quit(); // 監視プログラムを終わらせる
				if (DEBUG)
					System.out.println("<<@Shell>>■elapse:" + elapse);
				ec = null;
				stdout = stoReader.getBuff();
				stderr = errreader.getBuff();
				if (DEBUG)
					System.out.println("<<@Shell>>■stdout:\n" + stdout);
				if (DEBUG)
					System.out.println("<<@Shell>>■stderr:\n" + stderr);
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	// 実行条件を持たせられないか？？
	// 条件 => 処理 => 結果 => 誰に？=>メール
	public String execute(List list) {
		String[] array = (String[]) list.toArray(new String[list.size()]);
		return execute(array);
	}

	public String execute(String command, boolean option) {
		if (option) {
			// ／を￥に変更してから実行させるオプション
			return execute(new String[] { command.replaceAll("/", "\\\\") });

		} else {
			return execute(new String[] { command });

		}
	}

	public String execute(String command) {
		return execute(new String[] { command });
	}

	public String execute(String[] commands) {
		int count = 0;
		// -------------------------------------------------------------------------------
		// String osName = System.getProperty("os.name");
		// ＯＳの種別を判定する必要はあるか？
		// たとえばＯＳの種別によって・・
		// NT baseの場合
		// cmd.exe /c dir
		// 95 baseの場合
		// command.com /c dir
		// -------------------------------------------------------------------------------
		if (RuntimeEnv.memoryInfo() > 98) {
			System.out.println("メモリがアップアップです");
			System.out.println("過負荷ではなかろうか？");
		}
		// -------------------------------------------------------------------------------
		// スレッドグループにする意味はあるのか？
		// -------------------------------------------------------------------------------
		// XShell をスレッドグループにして、ベクターに入れ
		// 全完了後に其々が内包するメッセージをとりだす。
		// -------------------------------------------------------------------------------
		ThreadGroup thGroup = new ThreadGroup("freakoutGroup");
		List jQue = new ArrayList();
		for (int k = 0; k < commands.length; k++) {
			if (!commands[k].trim().equals("")) {
				XShell xCom = new XShell(thGroup, "#" + k);

				String command = UnicodeMod.charsetConv(commands[k]);
				// if (DEBUG)
				//	System.out.println("DEBUG <<@Shell>> command:" + command);
				xCom.setCommand(command);
				xCom.start();

				// //タイムラグがある感じなので・・・ちょっと寝かしてみる・・・?!
				// try {
				// Thread.sleep(1000);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }

				jQue.add(xCom);
				try {
					xCom.join(); // 待ち合わせ？！
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				;
			}
		}
		// -------------------------------------------------------------------------------
		// この時点ではシェルプロセスは走っていない、クラスのアロケーションの負荷か？
		// -------------------------------------------------------------------------------
		count = thGroup.activeCount();
		if (DEBUG)
			System.out.println("<<@Shell>>全部終わりました　現在アクテイブなスレッドの数:" + count);
		// -------------------------------------------------------------------------------
		// Listから一個ずつステータスを取り出して
		// -------------------------------------------------------------------------------
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < jQue.size(); i++) {
			XShell xCom = (XShell) (jQue.get(i));
			// System.out.println("jQue #" + i + " " +xCom.getStdout());
			sb.append("【" + xCom.getCommand() + "】\n");
			sb.append("---------------------------------------\n");
			sb.append(xCom.getStdout());
		}
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// Shell関連
	// -------------------------------------------------------------------------
	// ≪使用例≫
	// myShell("notepad.exe");
	// java Exec cmd.exe ipconfig
	// java Exec cmd.exe net use
	// etc...
	// 但し！dosコマンドは実際以下のように呼び出されているので・・・
	// NT baseの場合
	// java Exec cmd.exe /c dir
	// 95 baseの場合
	// java Exec command.com /c dir
	// 動いた例
	// java Exec "C:\Program Files\Microsoft Office\Office10\EXCEL.EXE"
	// -------------------------------------------------------------------------
	// エクセルなんかはフルパスで切らないと動かないカモ？？
	// フルパスで検索するのは骨
	// そうだwhichを実装して置こう
	// 拡張子による起動というのは、どういうカラクリなのか・・・win,macなど調べる
	// レジストリ云々だは解るが、代替する方法があればbestなんだ
	// -------------------------------------------------------------------------
	public static void exec(String command) {
		exec(new String[] { command });
	}

	public static void exec(String[] commands) {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(commands);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = br.readLine()) != null)
				System.out.println(line);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Usage:java Exec command [args...]");
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error:" + e);
		}
	}

	// -------------------------------------------------------------------------
	private static void TestGnuplot() {
		try {
			/*
			 * "set terminal table \n" + "set output \"" + GRAPH_FILE + "\"\n" +
			 * "plot sin(x) \n" + "quit";
			 */
			String GRAPH_FILE = "\"test.gif\"";
			String gnuplot_cmd = "set terminal gif size 300,300　\n"
					+ "set output " + GRAPH_FILE + "　\n"
					+ "plot x*sin(x)                　　　　　　　　\n" + "quit ";
			Process p = Runtime.getRuntime().exec("pgnuplot");
			PrintWriter gp = new PrintWriter(p.getOutputStream());
			gp.print(gnuplot_cmd);
			gp.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		// test0();
		// testPython();
		//		test20130314();
		test20160926();
	}

	public static void test20160926() {
		// NG 
		String command = "cmd.exe /c start c:\\xxmixit.bat";
		//		String command="cmd.exe /c c:\\xxmixit.bat";
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//		List<String> list = new ArrayList();
		//		list.add("cmd.exe date"); // NG?!
		//		list.add("cmd.exe /c dir");
		//		list.add("ipconfig");
		////		list.add("netstat -a");
		////		list.add("net use");
		////		list.add("cmd.exe tree > xtree.txt");
		////		list.add("sqlite3 QPRDB \".read sql.txt\"");
		//		new Shell().execute(list);
	}

	public static void test20130314() {
		String dir = ResControl.getUsersItpDir() + "#340018212101/";
		String dbpath = dir + "QPR.Db";
		List<String> list = new ArrayList();
		list.add("sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "create.sql\"");
		list.add("sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "inport.sql\"");
		new Shell().execute(list);
	}

	public static void test0() {
		String dir = ResControl.getUsersItpDir() + "504191000002/";
		String dbpath = dir + "QPR.Db";
		List<String> list = new ArrayList();
		list.add("sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "create.sql\"");
		list.add("sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "inport.sql\"");
		new Shell().execute(list);
	}

	public static void test0_s() {
		// バッチでやっても同じエラーとなったのでエンコードの問題ではなくショートパスの問題だと思う
		// javaでショートパスを拾うことは可能か？？
		String command = "";
		// String dir = "./forTest/";
		// フォルダー名の漢字の問題？？　　unable to open database file？
		String commonDir = ResControlWeb.getD_Resources_USER_ITP_COMMON();
		String dir = commonDir + "110098000002_東京急行電鉄／０９０７ふって飲むゼリー飲料/";
		String dbpath = dir + "QPR.Db";
		List<String> list = new ArrayList();
		// # sqlite3 -separator , DBNAME ".import FILENAME TABLENAME"
		// list.add("sqlite3 QPRDB \".read sql.txt\""});
		// list.add("sqlite3 -separator , QPRDB \".read sql.txt\"");
		command = "sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "sql.txt\"";
		command = UnicodeMod.charsetConv(command);
		System.out.println(command);
		list.add(command);
		// list.add("sqlite3 -separator , " + dbpath + " \".import " + dir +
		// "JAN.txt JAN\"");
		// list.add("sqlite3 -separator , " + dbpath + " \".import " + dir +
		// "MK.txt MK\"");
		new Shell().execute(list);
	}

	public static void testx() {
		List<String> list = new ArrayList();
		list.add("cmd.exe date"); // NG?!
		list.add("cmd.exe /c dir");
		list.add("ipconfig");
		list.add("netstat -a");
		list.add("net use");
		list.add("cmd.exe tree > xtree.txt");
		list.add("sqlite3 QPRDB \".read sql.txt\"");
		new Shell().execute(list);
	}

	public static void test1() {
		Shell.TestGnuplot();
	}

	public static void test2() {
		// Shell.exec(new String[]{"cmd.exe","ipconfig"}); NG
		// Shell.exec(new String[]{"cmd.exe","netstat"}); NG
		// Shell.exec(new String[]{"cmd.exe","net","use"}); NG
		Shell.exec(new String[] { "cmd.exe", "/c", "dir" });
	}

	public static void test3() {
		try {
			Process proc = Runtime.getRuntime().exec("notepad");
			new Shell().new ElapseCtrl((1000 * 30), proc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testPython() {
		try {
			Process proc = Runtime.getRuntime().exec("python hello.py");
			new Shell().new ElapseCtrl((1000 * 30), proc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
