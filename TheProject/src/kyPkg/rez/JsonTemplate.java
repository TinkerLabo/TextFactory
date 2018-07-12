package kyPkg.rez;

import java.util.List;

public class JsonTemplate {

	// var json1 =
	// {meta:{cells:[[
	// {name: 'Jan ', styles: 'text-align: left;', width: 8},
	// {name: 'JanName', styles: 'text-align: left;', width:25},
	// {name: 'Code ', styles: 'text-align: left;', width: 4},
	// {name: 'Name ', styles: 'text-align: left;', width:25},
	// {name: 'Price ', styles: 'text-align: right;', width: 5},
	// {name: 'Volume ', styles: 'text-align: right;', width: 5}
	// ]]},
	// data:[
	// ['4901005252264','グリコ 練りココア クリームリッチ 4ｐ','01010','イトーヨーカ堂','500','0'],
	// ['4901002014803','ＳＢ おいしさパック チャーハン ４８ｇ','00001','ＤＲＹ','160','0'],
	// ['4901551230075','カネボウ 周富輝の海鮮五目チャーハンの素 １７．２','00001','ＤＲＹ','140','0'],
	// ['4901577428272','ＱＰ３分炒めてパラッと！チャーハンの素 １４ｇ×４','99999','未設定その他','250','0'],
	// ['4901665006443','真誠 男のチャーハンの素 ５．８ｇ×３','99999','未設定その他','160','0'],
	// ['4901990102636','マルちゃん チャーハンの素 ガーリック ４０ｇ×３','99999','未設定その他','131','0'],
	// ['4902106658610','ミツカン チャーハンの素 えびねぎ １８ｇ','00001','ＤＲＹ','125','0'],
	// ['4902388047225','永谷園 ポケモンチャーハンの素かに味五目 箱 ３Ｐ','00001','ＤＲＹ','130','0'],
	// ]};

	public static void main(String[] argv) {
		String[] title = { "Jan", "JanName", "Code", "Name", "Price", "Volume" };
		String[] align = { "", "", "", "", "r", "r" };
		int[] width = { 8, 25, 4, 25, 5, 5 };
		String rec[] = {
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'",
				"'4901005252264','グリコ　練りココア　クリームリッチ　4ｐ','01010','イトーヨーカ堂','500','0'" };

		System.out.println("=>" + kyPkg.rez.JsonTemplate.jason4Grid(title,align,width,rec));
	}
	private static String[] int2strArray(int[] array){
		String[] Sarray = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			Sarray[i] = String.valueOf(array[i]);
		}
		return Sarray;
	}
	public static String jason4Grid(String[] title, String[] align,int[] width, String rec[]) {
		return jason4Grid(title, align, int2strArray(width), rec);
	}
	public static String jason4Grid(List lTitle, List lAlign,List lWidth, String rec[]) {
	    String[] title = (String[]) lTitle.toArray(new String[lTitle.size()]);
	    String[] align = (String[]) lAlign.toArray(new String[lAlign.size()]);
	    String[] width = (String[]) lWidth.toArray(new String[lWidth.size()]);
	    return jason4Grid(title,align,width,rec);
	}
	public static String jason4Grid(String[] title, String[] align,String[] width, String rec[]) {
		String suffix = "";
		String lf = "\n";
		StringBuffer writer = new StringBuffer();
		StringBuffer buff = new StringBuffer();
		// -----------------------------------------------------------------
		// meta
		// -----------------------------------------------------------------
		if (suffix.equals(""))
			writer.append("{");
		writer.append("meta" + suffix);
		writer.append(":{cells:[[");
		writer.append(lf);
		for (int i = 0; i < title.length; i++) {
			if (i > 0) {
				writer.append(",");
				writer.append(lf);
			}
			if (align[i].toUpperCase().startsWith("R")) {
				align[i] = "right";
			} else {
				align[i] = "left";
			}
			writer.append("{name: '" + title[i] + "', styles: 'text-align: "
					+ align[i] + ";', width: " + width[i] + "}");
		}
		writer.append("]]},");
		writer.append(lf);
		writer.append("data" + suffix);
		writer.append(":[");
		writer.append(lf);
		// -----------------------------------------------------------------
		// data
		// -----------------------------------------------------------------
		for (int v = 0; v < rec.length; v++) {
			String splited[] = rec[v].split(",");
			buff.delete(0, buff.length());
			if (v > 0) {
				writer.append(",");
				writer.append(lf);
			}
			buff.append("[");
			buff.append(splited[0]);
			for (int h = 1; h < splited.length; h++) {
				buff.append(",");
				buff.append(splited[h]);
			}
			writer.append(buff.toString());
			writer.append("]");
		}
		writer.append("]");
		if (suffix.equals(""))
			writer.append("}");
		writer.append(lf);
		return writer.toString();
	}
}
