package kyPkg.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kyPkg.converter.CelConverter;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_oClosure;
import kyPkg.tools.Onbiki;
import kyPkg.uFile.FileUtil;

public class Query2FileThread extends Thread {
	private boolean trimOpt = false;

	private static HashMap<String, QueryRes> metaCash = new HashMap();

	private boolean keyEnc = true;

	private int counter = 0;

	private JDBC jdbc = null;

	private Inf_oClosure outClosure;

	private String hash;

	private String sql;

	private static final String TAB = "\t";// for query2File

	private boolean headOpt = false; // for query2File

	private boolean preHead = false; // 値の前に フィールド名:を付けるかどうか・・・

	private String suffix = ""; // for query2File

	private String prefix = ""; // for query2File

	private String delimiter = TAB; // for query2File

	private String literal = null;// 文字コードにリテラルを付加するかどうか・・・型判定が必要

	private CelConverter celConverter = null; // 2008/10/31 halloween

	private QueryRes queryRes = null;

	private String value;

	private String encoding=FileUtil.defaultEncoding;
	
	public void setKeyEnc(boolean keyEnc) {
		this.keyEnc = keyEnc;
	}

	public void setPreHead(boolean preHead) {
		this.preHead = preHead;
	}

	// 静的なリストなので一定サイズ以上になったらかならずリセットする
	public static void resetMaps() {
		resetCash(0);
	}

	public static void resetCash(int max) {
		if (metaCash.size() >= max) {
			synchronized (metaCash) {
				Query2FileThread.metaCash = new HashMap();
			}
		}
	}

	public int getCounter() {
		return counter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
		this.outClosure.setDelimiter(delimiter);
	}

	public void setCelConverter(CelConverter celConverter) {
		if (celConverter == null)
			return;
		this.celConverter = celConverter;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	// SQLが同じ場合・・最初に指定されたリテラル処理をされてしまう仕様です（BUG?！）
	public void setLiteral(String literal) {
		this.literal = literal;
	}

	public void setHeadOpt(boolean headOpt) {
		this.headOpt = headOpt;
	}

	// コンストラクタ
	public Query2FileThread(JDBC jdbc, Inf_oClosure outClosure, String sql,
			String hash, boolean trimOpt) {
		super();
		this.jdbc = jdbc;
		this.trimOpt = trimOpt;
		this.outClosure = outClosure;
		this.sql = sql;
		if ((hash == null) || (hash.equals("")))
			hash = sql;
		this.hash = hash;
	}

	@Override
	public void run() {
		counter = query2File();
	}

	private int query2File() {
		int writeCount = 0;
		if (outClosure == null) {
			System.out.println("#Error query2File outClosure is null ");
			return -1;
		}
		try {
			Statement stmt = jdbc.createStatement(); // ステートメント生成
			if (stmt == null) {
				System.out.println("#Error @Query2FileThread createStatement ");
				return -1;
			}
			ResultSet rs = null;
			try {
//				System.out.println("sql:"+sql);
				rs = stmt.executeQuery(sql); // クエリー実行
//				System.out.println("#check00#");
			} catch (Exception e) {
				// #error:java.sql.SQLException: [Microsoft][ODBC SQL Server Driver][SQL Server]サーバーはコンパイル中にスタック オーバーフローを検出しました。

				// java.sql.SQLException: [Microsoft][ODBC SQL Server
				// Driver]クエリが時間切れになりました
				System.out.println("#error:@query2File:" + e.toString());
				System.out.println("query2File sql:" + sql);
				this.jdbc.showErrorDialog("検索条件を変更してください  \n" + e.toString());
				System.out.println("#error:=>Query2FileThread.JAVA");
				return -1;
			}

			StringBuffer buff = new StringBuffer();

			queryRes = metaCash.get(hash);
			if (queryRes == null) {
				synchronized (metaCash) {
					// System.out.println("#### hash:"+hash);
					queryRes = new QueryRes(rs.getMetaData(), literal);
					resetCash(8);// 8オカレンス以上スタックさせないようにする
					metaCash.put(hash, queryRes);
				}
			}

			// ---+---------+---------+---------+---------+---------+---------+---------+
			// Header（列名等処理）
			// ---+---------+---------+---------+---------+---------+---------+---------+
			if (headOpt == true) {
				String rec = queryRes.getHeader(prefix, suffix, delimiter);
				outClosure.write(rec);
			}

			List<String> body = new ArrayList();
			// ---+---------+---------+---------+---------+---------+---------+---------+
			// Body（データ処理）
			// ---+---------+---------+---------+---------+---------+---------+---------+
			while (rs.next()) {
				body.clear();
				buff.delete(0, buff.length());
				buff.append(prefix);

				// XXX オーバーヘッドを後で調整する！！
				int col = 0;
				// -------------------------------------------------------------
				if (preHead == true) { // For Json Style
					buff.append(queryRes.getColName(col));
					buff.append(":");
				}
				if ((keyEnc == true) && (queryRes.get(col) != null))
					buff.append(queryRes.get(col));// リテラル設定
				if (celConverter != null) {
					buff.append(
							celConverter.convert(col, rs.getString(col + 1)));
				} else {
					// ここか？？？
					// System.out.println("queryRes.getColMax():"+queryRes.getColMax());
					// System.out.println("col:"+col);
					buff.append(rs.getString(col + 1));
				}
				if ((keyEnc == true) && (queryRes.get(col) != null))
					buff.append(queryRes.get(col));// リテラル設定
				// -------------------------------------------------------------
				for (col = 1; col < queryRes.getColMax(); col++) {
					if (col > 0) {
						//buff.append(delimiter);
						body.add(buff.toString()); //new
						buff.delete(0, buff.length()); //new
					}
					if (preHead == true) { // For Json Style
						buff.append(queryRes.getColName(col));
						buff.append(":");
					}
					if (queryRes.get(col) != null)
						buff.append(queryRes.get(col));// リテラル設定
					if (celConverter != null) {
						buff.append(celConverter.convert(col,
								rs.getString(col + 1)));
					} else {
						value = rs.getString(col + 1);
						if (trimOpt)
							value = value.trim();
						buff.append(value);
					}
					if (queryRes.get(col) != null)
						buff.append(queryRes.get(col));// リテラル設定
					// rowBuff.append(rs.getString(i + 1)); // original
				}
				buff.append(suffix);

				body.add(buff.toString()); //new

				body= bodyCnv(body,encoding);//20170425
				
				
				buff.delete(0, buff.length()); //new

				outClosure.write(body, writeCount);
				//				System.err.println("#20170425# ");
				//				for (String element : body) {
				//					System.err.print(" element:" + element);
				//				}
				//				System.err.println("#20170425#");
				writeCount++;
			}
			stmt.close(); // ※関連するResultSetも同時にクローズされる！！
		} catch (SQLException e) {
			System.out.println("Error" + e.toString());
			e.printStackTrace();
			String sMsg = "";
			do {
				if (sMsg.length() > 0) {
					sMsg += " ";
				}
				sMsg += e.getMessage();
			} while ((e = e.getNextException()) != null);
			System.out.println("@query2fileThread#error:" + sMsg);
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error" + e.toString());
			return -1;
		}
		return writeCount;
	}

	//20170425
	private List<String> bodyCnv(List<String> body,String encoding) {
		List<String> res = new ArrayList();
		for (String elemnt : body) {
//			System.out.println("bef:"+elemnt);
			elemnt = Onbiki.cnv2Similar(elemnt, encoding);
//			System.out.println("aft:"+elemnt);
			res.add(elemnt);
		}
		return res;
	}

	public static int query2File(JDBC jdbc, String outPath, String sql,
			boolean trimOption) {
		EzWriter writer = new EzWriter(outPath);
		writer.open();
		String hash = "";
		Query2FileThread thread = new Query2FileThread(jdbc, writer, sql, hash,
				trimOption);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int count = thread.getCounter();
		writer.close();
		return count;
	}
}