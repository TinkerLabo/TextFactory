package kyPkg.filter;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.sql.Query;

//2015-03-11
public class SQLReader implements Inf_ArrayCnv {
	private Query query;

	// ----------------------------------------------------------------
	// コンストラクタ
	// ----------------------------------------------------------------
	public SQLReader() {
		super();
	}

	@Override
	public void init() {
		System.out.println("#<init of SQLReader>#");
		query = new Query();
	}

	@Override
	public void fin() {
		System.out.println("#<fin of SQLReader>#");
		query.fin();
	}

	@Override
	// ----------------------------------------------------------------
	// convert
	// ----------------------------------------------------------------
	public String[] convert(String[] rec, int stat) {
		String sql = rec[0].trim();
		System.out.println("=>" + sql);
		query.executeUpdate(sql);
		return rec;
	}

	public static void main(String[] argv) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SQLCMD Elapse");
		elapse.start();
		test20150316();
		elapse.stop();
		
	}

	// ------------------------------------------------------------------------
	// TODO パラメータクエリーバージョンも作っておくとよいかもしれない（大量に処理する場合には遅いので向いていない）
	// ------------------------------------------------------------------------
	// JICFSｶﾅ名称変換処理関連 （テキストで準備したｓｑｌを実行する　⇒漢字名称を上書きする）
	// ------------------------------------------------------------------------
	public static void test20150316() {
		System.out.println("Start");
		SQLReader flt = new kyPkg.filter.SQLReader();
		String inPath = "c:/jicfsKCNV_sql.txt";
		EzReader reader = new EzReader(inPath, flt);
		reader.setDelimiter("\t");
		new Basic_Reader(reader).execute();
		System.out.println("end");

	}

}
