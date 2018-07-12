package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Comparator4Delim implements Comparator {
	private int[] columns;
	private int[] direction;
	private String delimiter;

	// -------------------------------------------------------------------------
	// delimには区切り文字を指定する
	// columns には並べ替えたいカラム位置（１から始まる）を指定
	// 　（※このときマイナスにすると降順となる） 2008/02/15 yuasa
	// -------------------------------------------------------------------------
	public Comparator4Delim(String delimiter, int[] columns) {
		super();
		this.delimiter = delimiter;
		direction = new int[columns.length];
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] < 0) {
				direction[i] = -1;
			} else {
				direction[i] = 1;
			}
			// ０から始まると直感的じゃないので・・・
			columns[i] = Math.abs(columns[i]) - 1;
		}
		this.columns = columns;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		int cmp = 0;
		String[] array1 = ((String) obj1).split(delimiter);
		String[] array2 = ((String) obj2).split(delimiter);
		String s1 = "";
		String s2 = "";
		for (int i = 0; i < columns.length; i++) {
			int col = columns[i];
			// 当該カラムが存在しない場合も考慮する・・・
			if (array1.length > col)
				s1 = array1[col];
			if (array2.length > col)
				s2 = array2[col];
			cmp = s1.compareTo(s2) * direction[i];
			if (cmp != 0)
				return cmp;
		}
		return 0;
	}

	// -------------------------------------------------------------------------
	// 使用例
	// -------------------------------------------------------------------------
	public static void test01() {
		List<String> list = new ArrayList();
		list.add("700005964,BRAND1,12100");
		list.add("700005964,BRAND1,11200"); 
		list.add("700005964,BRAND2,10300");
		list.add("700005964,BRAND2,09400"); 
		list.add("700005964,BRAND3");
		list.add("700005964,BRAND3,07600"); 
		list.add("700037282,BRAND1,06100");
		list.add("700037282,BRAND1"); 
		list.add("700037282,BRAND2,04300");
		list.add("700037282,BRAND2,03400"); 
		list.add("700037282,BRAND3,02500");
		list.add("700037282,BRAND3,01600" );
		//区切り文字
		String delimiter=",";
		//並べ替えたいカラム位置　このばあい2カラム目が昇順で3カラム目が降順
		int[] columns=new int[] { 2, -3 };
		Collections.sort(list, new Comparator4Delim(delimiter,columns ));
		for (String element : list) {
			System.out.println(element);
		}
	}	public static void test02() {
		//数値順ではなく、文字コード順であることに注意する！
		List<String> list = new ArrayList();
		list.add("700005964,BRAND1,10000");
		list.add("700005964,BRAND1,2000"); 
		list.add("700005964,BRAND2,300");
		list.add("700005964,BRAND2,40"); 
		list.add("700005964,BRAND3");
		list.add("700005964,BRAND3,50"); 
		list.add("700037282,BRAND1,6");
		list.add("700037282,BRAND1"); 
		list.add("700037282,BRAND2,700");
		list.add("700037282,BRAND2,80"); 
		list.add("700037282,BRAND3,9");
		list.add("700037282,BRAND3,100" );
		//区切り文字
		String delimiter=",";
		//並べ替えたいカラム位置　このばあい2カラム目が昇順で3カラム目が降順
		int[] columns=new int[] { 2, -3 };
		Collections.sort(list, new Comparator4Delim(delimiter,columns ));
		for (String element : list) {
			System.out.println(element);
		}
	}
	public static void main(String[] args) {
		test02();
	}
}