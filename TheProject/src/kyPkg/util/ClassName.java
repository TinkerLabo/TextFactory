package kyPkg.util;

public class ClassName {
	// 備忘録・・・クラス名を拾いたい場合
	public ClassName() {
		super();
		// ・クラスから取得する方法
		// getClass().getName(); // パッケージ名を含むクラス名
		// getClass().getSimpleName() // クラス名のみ
		//
		// ・スタックトレースから取得する方法
		// Thread.currentThread().getStackTrace()[1].getMethodName(); // メソッド名
		// Thread.currentThread().getStackTrace()[1].getClassName(); //
		// パッケージ名を含むクラス名
		// Thread.currentThread().getStackTrace()[1].getFileName(); //
		// 拡張子(.java)付きファイル名
		// Thread.currentThread().getStackTrace()[1].getLineNumber(); // 行数

		String name = getClass().getName();
		String simpleName = getClass().getSimpleName();
//		System.out.println("name:"+name);
//		System.out.println("simpleName:"+simpleName);

	}

}
