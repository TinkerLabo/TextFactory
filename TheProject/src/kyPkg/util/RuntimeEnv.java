package kyPkg.util;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

public class RuntimeEnv {
	public static final boolean DEBUG = true;

	public static void main(String[] argv) {
		facade();
	}

	// -------------------------------------------------------------------------
	// ユーザのアカウント名(ユーザーID)を取得する
	//	String gJUser = kyPkg.util.RuntimeEnv.getUserID();
	// -------------------------------------------------------------------------
	public static String getUserID() {
		return System.getProperty("user.name").toUpperCase();
	}

	// -------------------------------------------------------------------------
	// コンピューター名（ks6vxx）を取得する
	//	String hostname = kyPkg.util.RuntimeEnv.getHostName();
	// -------------------------------------------------------------------------
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UnknownHost";
	}

	// -------------------------------------------------------------------------
	//	環境変数を拾う（コンピューター名（ks6vxx）を取得　動かず）
	// -------------------------------------------------------------------------
	public static void testHostName() {
		String hostname = System.getenv("HOSTNAME");
		System.out.println("hostname:" + hostname);
	}

	// -------------------------------------------------------------------------
	//	確認用
	// -------------------------------------------------------------------------
	public static void enumProperties() {
		Properties properties = System.getProperties();
		properties.list(System.out);
	}

	// -------------------------------------------------------------------------
	// 環境関連のファサード
	// RTmod.facade();
	// -------------------------------------------------------------------------
	public static void facade() {
		if (DEBUG)
			System.out.println("<Debug Mode>");
		kyPkg.util.RuntimeEnv.envinfo();
		kyPkg.util.RuntimeEnv.memoryInfo();
		kyPkg.util.RuntimeEnv.enumCLASSPATH();
		kyPkg.util.RuntimeEnv.enumPATH();
		kyPkg.util.RuntimeEnv.envInfoEnum();
	}

	// -------------------------------------------------------------------------
	// メモリの使用レート 何％かをintで返す
	// -------------------------------------------------------------------------
	public static int memoryInfo() {
		java.text.DecimalFormat exFormat2 = new java.text.DecimalFormat("0.00");



		int iRate = 0;
		float rate = 0.0f;
		try {
			Runtime runtime = Runtime.getRuntime();
			long total = runtime.totalMemory();
			long free = runtime.freeMemory();
			rate = (float) free / (float) total;
			iRate = (int) (rate * 100.0);
			if (iRate < 20) {
				Double share =rate * 100.0;
				String xShare= String.valueOf(exFormat2.format(share));
				System.err.println("FreeMemory " + free + " / " + total
						+ " (Free/Total)Bytes => " + xShare + "%");
			}
		} catch (Exception e) {
			System.err.println("Error");
		}
		return iRate;
	}

	// -------------------------------------------------------------------------
	// パス環境変数一覧
	// -------------------------------------------------------------------------
	private static void enumPATH() {
		System.out.println("ライブラリパス:");
		TreeSet ts = getEnv2TreeSet("java.library.path");
		Iterator it = ts.iterator();
		while (it.hasNext())
			System.out.println("=>" + it.next());
	}

	// -------------------------------------------------------------------------
	// クラスパス一覧
	// -------------------------------------------------------------------------
	private static void enumCLASSPATH() {
		System.out.println("Java クラスパス:");
		TreeSet ts = getEnv2TreeSet("java.class.path");
		Iterator it = ts.iterator();
		while (it.hasNext())
			System.out.println("=>" + it.next());
	}

	// -------------------------------------------------------------------------
	// パス、クラスパスをTreeSet化（ユニーク＆sortしたいのでTreeSetを使用）
	// -------------------------------------------------------------------------
	// TreeSet ts = getEnv2TreeSet("java.library.path");
	// TreeSet ts = getEnv2TreeSet("java.class.path");
	// -------------------------------------------------------------------------
	private static TreeSet getEnv2TreeSet(String type) {
		String delim = System.getProperty("path.separator");
		String envString = System.getProperty(type);
		String[] array = envString.split(delim);
		TreeSet ts = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			ts.add(array[i]);
		}
		return ts;
	}

	// -------------------------------------------------------------------------
	// 環境変数などチェック
	// プラットフォームのデフォルトの文字セット ?? CP932
	// -------------------------------------------------------------------------
	private static void envinfo() {
		System.out.println("仮想マシンのデフォルトの文字セット =>"
				+ java.nio.charset.Charset.defaultCharset());
		System.out.println("ＯＳ名                             =>"
				+ System.getProperty("os.name"));
		System.out.println("ファイル区切り文字 (UNIX では '/') =>"
				+ System.getProperty("file.separator"));
		System.out.println("パス区切り文字     (UNIX では ':') =>"
				+ System.getProperty("path.separator"));
		System.out.println("Java 仮想マシンの仕様バージョン    =>"
				+ System.getProperty("java.vm.specification.version"));
		System.out.println("Runtimeのバージョン                =>"
				+ System.getProperty("java.version"));
		System.out.println(
				"Java のインストール先ディレクトリ  =>" + System.getProperty("java.home"));
		System.out.println(
				"ユーザのホームディレクトリ         =>" + System.getProperty("user.home"));
		System.out.println(
				"ユーザの現在の作業ディレクトリ     =>" + System.getProperty("user.dir"));
		System.out.println("デフォルト一時ファイルのパス       =>"
				+ System.getProperty("java.io.tmpdir"));
				/*
				 * try{ System.out.println("Java クラスパス    =>" +
				 * System.getProperty("java.class.path") ); System.out.println(
				 * "一時ファイルのパス =>" + System.getProperty("java.io.tmpdir") );
				 * System.out.println("拡張Dirのパス      =>" +
				 * System.getProperty("java.ext.dirs") ); System.out.println(
				 * "ＯＳバージョン     =>" + System.getProperty("os.version") );
				 * System.out.println("行      区切り文字 =>" +
				 * System.getProperty("line.separator") System.out.println(
				 * "ライブラリのロード時に検索するパスのリスト =>" +
				 * System.getProperty("java.library.path") ); }catch(Exception
				 * ee){ ee.printStackTrace(); }
				 */

		// System.out.println("行区切り文字 ex.CRLF =>" +
		// System.getProperty("line.separator") );
		// System.out.println("★ Runtimeの仕様バージョン=>" +
		// System.getProperty("java.specification.version") );
		// System.out.println("JIT コンパイラの名前 "+
		// System.getProperty("java.compiler") );
		// System.out.println("Java ベンダーの URL "+
		// System.getProperty("java.vendor.url") );
		// System.out.println("仮想マシンの仕様ベンダー "+
		// System.getProperty("java.vm.specification.vendor") );
		// System.out.println("仮想マシンの仕様名 "+
		// System.getProperty("java.vm.specification.name") );
		// System.out.println("仮想マシンの実装バージョン"+
		// System.getProperty("java.vm.version") );
		// System.out.println("仮想マシンの実装ベンダー "+
		// System.getProperty("java.vm.vendor") );
		// System.out.println("仮想マシンの実装名 "+ System.getProperty("java.vm.name")
		// );
		// System.out.println("Runtimeのベンダー "+ System.getProperty("java.vendor")
		// );
		// System.out.println("Runtimeの仕様のベンダー "+
		// System.getProperty("java.specification.vendor") );
		// System.out.println("Runtimeの仕様名 "+
		// System.getProperty("java.specification.name") );
		// System.out.println("クラスの形式のバージョン "+
		// System.getProperty("java.class.version") );
		// System.out.println("拡張ディレクトリのパス "+
		// System.getProperty("java.ext.dirs") );
		// System.out.println("OSのバージョン "+ System.getProperty("os.version") );
		// System.out.println("OSのアーキテクチャ "+ System.getProperty("os.arch") );
		// System.out.println("Byte .MAX_VALUE =>"+Byte.MAX_VALUE);
		// System.out.println("Short .MAX_VALUE =>"+Short.MAX_VALUE);
		// System.out.println("Character.MAX_VALUE =>"+Character.MAX_VALUE);
		// System.out.println("Integer .MAX_VALUE =>"+Integer.MAX_VALUE);
		// System.out.println("Long .MAX_VALUE =>"+Long.MAX_VALUE);
	}

	// -------------------------------------------------------------------------
	// プロパティ（環境変数など）の名前と値の一覧
	// System.out.println("■実行環境の情報 \n"+envInfo());
	// -------------------------------------------------------------------------
	private static String envInfoEnum() {
		StringBuffer sBuf = new StringBuffer();
		Properties env = System.getProperties(); // システムのプロパティ取得
		Iterator names = (Iterator) env.propertyNames(); // 名前のリスト取得
		while (names.hasNext()) {
			String name = names.next().toString();
			String value = env.getProperty(name);
			sBuf.append(name + '\t' + ":" + value + "\n");
			// System.out.println( name + '\t' + ":" + value + "\n");
		}
		return sBuf.toString();
	}

}
