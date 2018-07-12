package kyPkg.atoms;

import static kyPkg.util.KUtil.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.etc.AliasRes;
import kyPkg.filter.EzWriter;
import kyPkg.uFile.ListArrayUtil;

public class AtomUtil {
	private static final String TAB = "\t";;

	// --------------------------------------------------------------------
	// 《概要》
	// モニターの属性情報をはユーザーのローカルC:/@qpr/home/Personal/MonSets/以下に格納される
	// 源泉の情報を以下のような形式で『currentP.txt』に格納してある
	// --------------------------------------------------------------------
	// ＱＰＲアンケート/01_属性・メディア編/2014/TABQ1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11S1,Q11S2,Q12S1
	// ＱＰＲアンケート/03_属性・性年代編/2014/TABA01,A02,A03,A04,A05,A0601,A0602,A0603,A0604,A0605,A0606,A0608,A07,A08
	// --------------------------------------------------------------------
	// この情報を集計期間に同期させるために集計対象期間の終了年をベースに『currentP.txt』を書き換えて、そのつど
	// 属性データ『current.txt』
	// 属性メタデータ『current.atm』
	// をそれぞれ生成させる
	// --------------------------------------------------------------------
	// パラメータ年を挟み込んだファイル名に書き換えて返す
	// 例＞
	// int baseYear = 2013;
	// String path = "C:/@qpr/home/Personal/MonSets/"+CURRENT_P_TXT
	// 上記のパラメータの場合以下のような値が返る：
	// "C:/@qpr/home/Personal/MonSets/currentP2013.txt";
	// --------------------------------------------------------------------
	public static String getBasePath(String inPath, int baseYear) {
		String[] pathArray = inPath.split("\\.");
		return pathArray[0] + baseYear + ".txt";
	}

	// --------------------------------------------------------------------
	// コンストラクタ
	// --------------------------------------------------------------------
	// ■■■<<AtomUtil>>■■■ baseYear :2011
	// <<AtomUtil>> inPath :C:/@qpr/home/Personal/MonSets/currentP.txt
	// <<AtomUtil>> outPath:C:/@qpr/home/Personal/MonSets/currentP2011.txt
	// --------------------------------------------------------------------
	//	public AtomUtil(int baseYear,boolean debug20161209) {
	public AtomUtil(int baseYear) {
		System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
		System.out.println("☆　　ＣＵＲＲＥＮＴファイル再生成　☆");
		System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");

		System.out.println("■■■<<AtomUtil>>■■■ baseYear :" + baseYear);
		String inPath = ResControl.getCurrentP_TXT();
		String paramPath = AtomUtil.getBasePath(inPath, baseYear);
		System.out.println("<<AtomUtil>> inPath :" + inPath);
		System.out.println("<<AtomUtil>> paramPath:" + paramPath);
		// --------------------------------------------------------------------
		// 各行から、行オブジェクトをつくる（各行をオブジェクト表現したものに変換する）
		// --------------------------------------------------------------------
		// currentP.txtの中身は＜属性種別＞　＜タブ＞　＜フィールドディスクリプタ＞のようになっている　以下に例を示す　
		// ＱＰＲアンケート/03_属性・性年代編/2012/＋TAB＋A01,A02,A03,A04,A05,A0601,A0602,A0603,A0604・・・
		// --------------------------------------------------------------------
		String[] array = ListArrayUtil.file2Array(inPath);
		List<LineObj> objList = new ArrayList();
		for (String element : array) {
			//			System.out.println("element:"+element);
			LineObj lineObj = new LineObj(element);
			objList.add(lineObj);
		}
		// --------------------------------------------------------------------
		// 一番大きい年を見つける（どの要素も年を持たないケースも考えられる・・・）
		// --------------------------------------------------------------------
		int maxYear = 0;
		for (LineObj lineObj : objList) {
			Integer wYear = lineObj.getYear();
			if (wYear != null && maxYear < wYear)
				maxYear = wYear;
		}
		//		System.out.println("maxYear:"+maxYear);
		// --------------------------------------------------------------------
		// 最大年との差をセットする
		// --------------------------------------------------------------------
		List<String> resList = new ArrayList();
		for (LineObj lineObj : objList) {
			lineObj.setDiff(maxYear);
			System.out.println(
					"lineObj.getLine(baseYear)=>" + lineObj.getLine(baseYear));
			resList.add(lineObj.getLine(baseYear));
		}
		// --------------------------------------------------------------------
		// 指定パラメータの年をベースにしたパラメータファイルを出力する
		// --------------------------------------------------------------------
		EzWriter.list2File(paramPath, resList);
		// --------------------------------------------------------------------
		// 書き換えた内容でcurrent.txtを更新する
		// 出力したパラメータをもとに属性ファイルを再生成して作表をする
		// --------------------------------------------------------------------
		HashMap<String, List> fieldMap = AliasRes.paramFile2HashMap(paramPath);
		String xxxx = String.valueOf(baseYear);
		AliasRes.createCurrentData(fieldMap, xxxx);
	}

	// ########################################################################
	// 《内部クラス》
	// ########################################################################
	class LineObj {
		private static final String SEP = "/";
		private Integer diff = 0;
		private Integer year;
		private String[] elements;
		private String[] spliteds;

		// --------------------------------------------------------------------
		// 《内部クラス》コンストラクタ
		// 引数例：ＱＰＲアンケート/01_属性・メディア編/2014/ TAB Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8
		// --------------------------------------------------------------------
		public LineObj(String element) {
			elements = element.split(TAB);
			if (element.length() > 0) {
				pickUpYearElement(elements[0]);
			}
		}

		// --------------------------------------------------------------------
		// パス文字列中の年に当たる要素を取り出す（一番最後の要素が4ケタの年かどうか判定）
		// 引数例：ＱＰＲアンケート/01_属性・メディア編/2014/
		// --------------------------------------------------------------------
		private void pickUpYearElement(String srcDir) {
			spliteds = srcDir.split(SEP);
			String lastElement = spliteds[spliteds.length - 1];
			// 正規表現で4ケタの年かどうか判定
			if (kyPkg.uRegex.Regex.isYear(lastElement)) {
				year = new Integer(lastElement);
			} else {
				year = null;
			}
		}

		// --------------------------------------------------------------------
		// 最大年との差をdiffにセットする
		// --------------------------------------------------------------------
		public void setDiff(int maxYear) {
			if (year != null) {
				this.diff = year - maxYear;
			} else {
				this.diff = null;
			}
		}

		// --------------------------------------------------------------------
		// 年にあたる要素があればそれを返す
		// --------------------------------------------------------------------
		public Integer getYear() {
			return year;
		}

		// --------------------------------------------------------------------
		// ベース年に書き換えて返す（もともと年の要素ではない場合はそのまま何もしない）
		// --------------------------------------------------------------------
		public String getLine(int baseYear) {
			if (diff != null) {
				spliteds[spliteds.length - 1] = String
						.valueOf((baseYear - diff));
			}
			return join(spliteds, SEP) + SEP + TAB + elements[1];
		}
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
		test01();
	}

	// ------------------------------------------------------------------------
	// 集計直前に云々
	// 基準年に合わせてパラメータを書き換えるが、当該年に存在しないパラメータが存在する為にエラーとなってしまう
	// ------------------------------------------------------------------------
	public static void test01() {
		// 集計期間に同期させるため、集計対象期間の終了年をベースに『currentP.txt』を書き換える
		new AtomUtil(2011);
	}

}
