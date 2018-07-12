package kyPkg.converter;

import static kyPkg.util.Joint.join;

import kyPkg.filter.Inf_DualConverter;

public class DCnvConcatinate implements Inf_DualConverter {
	protected String delimiter = "\t";

	public DCnvConcatinate(String delimiter) {
		super();
		this.delimiter = delimiter;
	}

	// ------------------------------------------------------------------------
	// フィルタ、タイプ１
	// arrayConverter = new DefaultArrayConverter();
	// ------------------------------------------------------------------------
	@Override
	public String convert(String[] lefts, String[] rights, int stat) {
		//XXX 20170725 暫定テスト　　　 
		if (lefts == null && rights == null) {
			return "";
		}
		if (lefts == null) {
			lefts = new String[rights.length];
			lefts[0]=rights[0];
		}
		if (rights == null) {
			rights = new String[lefts.length];
			rights[0]=lefts[0];
		}
		return lefts[0]+ delimiter +  join(lefts, delimiter,1) + delimiter +  join(rights, delimiter,1);
//		TODO	合成する場合とIDのみを出力する場合を考えてみる・・・　クラスター処理に使うのだが・・・
//		TODO	藤居さんの処理・・・品目マスタマッチング
//		つまり、コンバーターを複数選択可能にする・・・

	}

	@Override
	public void init() {
	};

	@Override
	public void fin() {
	};

}
