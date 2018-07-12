package kyPkg.sql;

import java.util.List;

import globals.ResControl;

public class Keiri {
	//取引先マスター（CSV）
	private static final String TRHKCD_TXT = "trhkcd.txt";
	public static String KEIRI_JURL = "JDBC:ODBC:DRIVER={SQL Server};SERVER=agcks07;DATABASE=keiri_db1";
	public static String KEIRI_USER = "sa";
	public static String KEIRI_PASS = "";

	// XXX ・・・アクセス権限の問題で読めないことがある・・・その場合どこかにキャッシュしておいてそれをよませるかね？
	// 取引先マスターを読み込む
	public static String getTorihikisaki() {
		String path = ResControl.getQPRHomeQprRes(TRHKCD_TXT);
		String regex = null;
		if (regex == null || regex.trim().equals(""))
			regex = "";
		String sql = "";
		String where = "";
		// XXX limitに％とか入るとまずいのだ、インジェクション対応はするのか？！
		if (regex.equals("")) {
		} else if (regex.matches("[0-9]*")) {
			where = "and trhkcd like '" + regex + "%'";
		} else {
			// XXX ビール　⇒　ビ－ルじゃないとダメだったりする
			where = "and kanjnm like '%" + regex + "%'";
		}
		String jURL = KEIRI_JURL;
		String user = KEIRI_USER; // ユーザ名
		String pass = KEIRI_PASS; // パスワード
		sql = "SELECT trhkcd, + '' + kanjnm FROM trms01a WHERE delflag <> 9 "
				+ where + "  ORDER BY trhkcd";
		ServerConnecter cnn = new ServerConnecter(jURL, user, pass);
		int count = cnn.query2File(path, sql);
		return path;
	}

	public static List<List> searchTRHKxxx_org(String regex) {
		List<List> matrix = null;
		if (regex == null || regex.trim().equals(""))
			regex = "";

		String sql = "";
		String where = "";

		// XXX limitに％とか入るとまずいのだ、インジェクション対応はするのか？！
		if (regex.equals("")) {
		} else if (regex.matches("[0-9]*")) {
			where = "and trhkcd like '" + regex + "%'";
		} else {
			// XXX ビール　⇒　ビ－ルじゃないとダメだったりする
			where = "and kanjnm like '%" + regex + "%'";
		}
		String jURL = KEIRI_JURL;
		String user = KEIRI_USER; // ユーザ名
		String pass = KEIRI_PASS; // パスワード
		sql = "SELECT trhkcd + ' ' + kanjnm FROM trms01a WHERE delflag <> 9 "
				+ where + "  ORDER BY trhkcd";

		ServerConnecter cnn = new ServerConnecter(jURL, user, pass);
		matrix = cnn.query2Matrix(sql);
		return matrix;
	}

	public static void testQuery2File() {
		String path = "c:/result.txt";
		String jURL = KEIRI_JURL;
		String user = KEIRI_USER; // ユーザ名
		String pass = KEIRI_PASS; // パスワード
		String sql = "SELECT trhkcd, + '' + kanjnm FROM trms01a WHERE delflag <> 9 ";
		ServerConnecter cnn = new ServerConnecter(jURL, user, pass);
		int count = cnn.query2File(path, sql);
	}

	public static void main(String[] argv) {
		Keiri.getTorihikisaki();

	}

}
