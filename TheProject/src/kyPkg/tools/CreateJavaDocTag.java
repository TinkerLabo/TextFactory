package kyPkg.tools;

//TODO	JavaDoc用のひな型を外部化したい　20151020
//-----------------------------------------------------------------------------
//タ　グ　名	説	明
//@author	クラスの作成者情報を記載
//@param	メソッドの引数の説明
//@return	メソッドの返り値の説明
//@throw	発生する例外クラスを指定
//@see	他のAPIを参照する場合に記載
//@deprecated	推奨されないAPIであることを示す
//@serial	直列化されたフィールドの説明
//@sesrialData	直列化された状態でのデータ型と順序を記載
//@since	導入されたバージョンを記載
//@version	バージョンを記載
//-----------------------------------------------------------------------------
import static kyPkg.util.Joint.join;
import static kyPkg.util.KUtil.list2strArray;
import java.util.ArrayList;

/**************************************************************************
 * <BR>JavaDoc用コメントタグ生成		20151020
 * <BR>クラスに対してのコメントを記述します。
 * <BR>ここでは以下の内容を記述します。
 * <BR><UL>
 * <BR><LI>クラスがどういうことをするのかの説明
 * <BR><LI>インスタンスが取り得る状態についての情報。
 * <BR>    例） ファイルがオープンされている状態とクローズされた状態での振る舞い。
 * <BR><HR>
 * <BR><HR>
 * @author ken yuasa
 * @version 1.0
 * @see "Effective Java第2版"
 **************************************************************************/
//-----------------------------------------------------------------------------
//@author		クラスの作成者情報を記載
//@param		メソッドの引数の説明
//@return		メソッドの返り値の説明
//@throw		発生する例外クラスを指定
//@see			他のAPIを参照する場合に記載
//@deprecated	推奨されないAPIであることを示す
//@serial		直列化されたフィールドの説明
//@sesrialData	直列化された状態でのデータ型と順序を記載
//@since		導入されたバージョンを記載
//@version		バージョンを記載
//-----------------------------------------------------------------------------
public class CreateJavaDocTag extends Tool {
	//-----------------------------------------------------------------------------
	//	関数のシグニチャから引数名を取り出してアノーテーションのひな型を自動生成するプログラム
	//	下記は自動生成されるアノーテーションの例
	//-----------------------------------------------------------------------------
	//	/**
	//	 * コンストラクタ CalcLoy1
	//	 * @param mapLoy　出力パス１：モニター*ブランド ごとの各ロイヤルティ＋各合計
	//	 * @param mTotal　出力パス２：モニターごとの合計レコード（足切り用）
	//	 * @param gTotal　出力パス３：総合計レコード1件のデータ（足切り用）
	//	 */
	//-----------------------------------------------------------------------------
	public static String execute(String str) {
		ArrayList<String> list = new ArrayList();
		ArrayList<String> bufList = new ArrayList();
		int pos1 = str.indexOf('(');
		int pos2 = str.indexOf(')');
		if (pos1 < 0 || pos2 < 0)
			return "";
		String[] func = str.substring(0, pos1).split(" ");
		String name = func[func.length - 1];
		String ans = str.substring(pos1 + 1, pos2);
		if ((pos1 > 0) && (pos1 < pos2)) {
			//			bufList.add("// ------------------------------------------------------------------------");
			bufList.add(
					"/**************************************************************************");
			bufList.add(" * " + name + "\t\t\t\t");
			bufList.add(" * @author\tken yuasa");
			bufList.add(" * @version\t1.0");
			bufList.add(" * <ul>");
			bufList.add(" * <ll>【概要】");
			bufList.add(" * </ul>");
			String[] splited = ans.split(",");
			if (splited.length > 0) {
				for (int i = 0; i < splited.length; i++) {
					String[] tuple = splited[i].trim().split(" ");
					if (tuple.length > 1) {
						list.add(tuple[1].trim());
						bufList.add(" * @param " + tuple[1].trim() + "\t\t\t ");
					}
				}
			}
			String args = join(list2strArray(list), ",");
			bufList.add(
					" **************************************************************************/");
			//			bufList.add("// ------------------------------------------------------------------------");
		}
		String LF = "\n";
		StringBuffer buff = new StringBuffer();
		for (String val : bufList) {
			buff.append(val + LF);
		}
		return buff.toString();
	}

	public static void main(String[] argv) {
		String str = "	public HttpClient(String requestMethod, String url) {";
		str = "	public CalcLoy2X(String bodyPath, int denominator, int cutEx, int cutHi,int cutMed, String calcBase, String sharBase,HashMap<String, String> classMap, HashMap<String, String> typeMap,			String target) {";
		System.out.println(kyPkg.tools.CreateJavaDocTag.execute(str));
	}
}
