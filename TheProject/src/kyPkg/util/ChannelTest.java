package kyPkg.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import kyPkg.converter.Channel;

public class ChannelTest {
	static boolean DEBUG = true;

	@Test
	public void testChannelCnv2to3() {
		assertEquals("100", Channel.cnv2to3("10"));  //ＩＹ系　　@
		assertEquals("101", Channel.cnv2to3("11"));  //いなげや　@
		assertEquals("102", Channel.cnv2to3("12"));  //サミット　@
		assertEquals("103", Channel.cnv2to3("13"));  //西友系　　@
		assertEquals("104", Channel.cnv2to3("14"));  //ダイエー系@
		assertEquals("105", Channel.cnv2to3("15"));  //東急ストア@
		assertEquals("200", Channel.cnv2to3("20"));  //Ｓイレブン@
		assertEquals("201", Channel.cnv2to3("21"));  //Ｆマート　@
		assertEquals("202", Channel.cnv2to3("22"));  //ローソン　@
		assertEquals("203", Channel.cnv2to3("23"));  //Ｄヤマザキ@
		assertEquals("204", Channel.cnv2to3("24"));  //Ｍストップ@
		assertEquals("206", Channel.cnv2to3("26"));  //サンクス　@
		assertEquals("301", Channel.cnv2to3("31"));  //酒販店　　@
		assertEquals("302", Channel.cnv2to3("32"));  //書籍店　　@
		assertEquals("304", Channel.cnv2to3("34"));  //精肉店　　@
		assertEquals("307", Channel.cnv2to3("37"));  //パン菓子店@
		assertEquals("400", Channel.cnv2to3("40"));  //百貨店　　@
		assertEquals("500", Channel.cnv2to3("50"));  //他薬粧店　@
		assertEquals("601", Channel.cnv2to3("61"));  //Ｏ・Ｓ内自@
		assertEquals("602", Channel.cnv2to3("62"));  //駅構内自販@
		assertEquals("603", Channel.cnv2to3("63"));  //ＳＰ施設自@
		assertEquals("701", Channel.cnv2to3("71"));  //他ＮＴ通販@
		assertEquals("702", Channel.cnv2to3("72"));  //楽天市場　@
		assertEquals("703", Channel.cnv2to3("73"));  //アマゾン　@
		assertEquals("704", Channel.cnv2to3("74"));  //Ｙａｈｏｏ@
		assertEquals("705", Channel.cnv2to3("75"));  //７ネット　@
		assertEquals("706", Channel.cnv2to3("76"));  //イオンＮＳ@
		assertEquals("707", Channel.cnv2to3("77"));  //ＩＹＮＳＰ@
		assertEquals("708", Channel.cnv2to3("78"));  //他ＮＳＰ　@
		assertEquals("800", Channel.cnv2to3("80"));  //訪販・宅配@
		assertEquals("900", Channel.cnv2to3("90"));  //生協店舗　@
		assertEquals("901", Channel.cnv2to3("91"));  //生協共同　@
		assertEquals("902", Channel.cnv2to3("92"));  //他ＨＣ　　@
		assertEquals("903", Channel.cnv2to3("93"));  //他ＤＳ　　@
		assertEquals("904", Channel.cnv2to3("94"));  //家電量販店@
		assertEquals("905", Channel.cnv2to3("95"));  //駅売店　　@
		assertEquals("110", Channel.cnv2to3("1A"));  //イオン系　@
		assertEquals("112", Channel.cnv2to3("1C"));  //ピーコック@
		assertEquals("114", Channel.cnv2to3("1E"));  //ユニー系　@
		assertEquals("116", Channel.cnv2to3("1G"));  //マルエツ系@
		assertEquals("117", Channel.cnv2to3("1H"));  //ライフ　　@
		assertEquals("119", Channel.cnv2to3("1J"));  //イズミヤ　@
		assertEquals("120", Channel.cnv2to3("1K"));  //万代　　　@
		assertEquals("121", Channel.cnv2to3("1L"));  //関西ＳＰ　@
		assertEquals("122", Channel.cnv2to3("1M"));  //成城石井　@
		assertEquals("123", Channel.cnv2to3("1N"));  //つるかめ　@
		assertEquals("124", Channel.cnv2to3("1O"));  //ＯＫストア@
		assertEquals("125", Channel.cnv2to3("1P"));  //ヤオコー　@
		assertEquals("126", Channel.cnv2to3("1Q"));  //ＯＲピック@
		assertEquals("127", Channel.cnv2to3("1R"));  //業務ＳＰ　@
		assertEquals("128", Channel.cnv2to3("1S"));  //オオゼキ　@
		assertEquals("129", Channel.cnv2to3("1T"));  //ベイシア系@
		assertEquals("130", Channel.cnv2to3("1U"));  //平和堂系　@
		assertEquals("131", Channel.cnv2to3("1V"));  //オークワ　@
		assertEquals("132", Channel.cnv2to3("1W"));  //サンディ　@
		assertEquals("133", Channel.cnv2to3("1X"));  //ＳＰ玉出　@
		assertEquals("134", Channel.cnv2to3("1Y"));  //阪急ＯＡＳ@
		assertEquals("135", Channel.cnv2to3("1Z"));  //アークス系@
		assertEquals("136", Channel.cnv2to3("1ｱ"));  //ホクレン　@
		assertEquals("137", Channel.cnv2to3("1ｲ"));  //生鮮市場Ｇ@
		assertEquals("138", Channel.cnv2to3("1ｳ"));  //ＬＵＣＫＹ@
		assertEquals("139", Channel.cnv2to3("1ｴ"));  //バロー　　@
		assertEquals("140", Channel.cnv2to3("1ｵ"));  //ヤマナカ系@
		assertEquals("141", Channel.cnv2to3("1ｶ"));  //ＦＥＥＬ　@
		assertEquals("142", Channel.cnv2to3("1ｷ"));  //イズミ系　@
		assertEquals("143", Channel.cnv2to3("1ｸ"));  //マルキョウ@
		assertEquals("144", Channel.cnv2to3("1ｹ"));  //サンリブ　@
		assertEquals("145", Channel.cnv2to3("1ｺ"));  //ハローデイ@
		assertEquals("146", Channel.cnv2to3("1ｻ"));  //西鉄ストア@
		assertEquals("147", Channel.cnv2to3("1ｼ"));  //タイヨー　@
		assertEquals("148", Channel.cnv2to3("1ｽ"));  //ＳＰ大栄　@
		assertEquals("210", Channel.cnv2to3("2A"));  //ａｍｐｍ　@
		assertEquals("214", Channel.cnv2to3("2E"));  //サークルＫ@
		assertEquals("215", Channel.cnv2to3("2F"));  //ポプラ　　@
		assertEquals("219", Channel.cnv2to3("2J"));  //Ｓマート　@
		assertEquals("220", Channel.cnv2to3("2K"));  //Ｃキオスク@
		assertEquals("221", Channel.cnv2to3("2L"));  //ベルマート@
		assertEquals("222", Channel.cnv2to3("2M"));  //ココストア@
		assertEquals("223", Channel.cnv2to3("2N"));  //エブリワン@
		assertEquals("224", Channel.cnv2to3("2O"));  //生活列車　@
		assertEquals("310", Channel.cnv2to3("3A"));  //たばこ屋　@
		assertEquals("311", Channel.cnv2to3("3B"));  //ペットＳＨ@
		assertEquals("312", Channel.cnv2to3("3C"));  //ＣＤＳＨＰ@
		assertEquals("510", Channel.cnv2to3("5A"));  //マツキヨ　@
		assertEquals("511", Channel.cnv2to3("5B"));  //キリン堂　@
		assertEquals("512", Channel.cnv2to3("5C"));  //スギ薬局　@
		assertEquals("513", Channel.cnv2to3("5D"));  //ツルハＤＲ@
		assertEquals("514", Channel.cnv2to3("5E"));  //カワチ薬局@
		assertEquals("515", Channel.cnv2to3("5F"));  //サンＤＲＧ@
		assertEquals("516", Channel.cnv2to3("5G"));  //ハックＤＲ@
		assertEquals("517", Channel.cnv2to3("5H"));  //クリエイト@
		assertEquals("518", Channel.cnv2to3("5I"));  //ジップＤＲ@
		assertEquals("519", Channel.cnv2to3("5J"));  //ライフォト@
		assertEquals("520", Channel.cnv2to3("5K"));  //ウェルシア@
		assertEquals("521", Channel.cnv2to3("5L"));  //セガミ　　@
		assertEquals("522", Channel.cnv2to3("5M"));  //セイジョー@
		assertEquals("523", Channel.cnv2to3("5N"));  //ダイコクＤ@
		assertEquals("524", Channel.cnv2to3("5O"));  //コクミン　@
		assertEquals("525", Channel.cnv2to3("5P"));  //ＳＥＩＭＳ@
		assertEquals("526", Channel.cnv2to3("5Q"));  //福太郎　　@
		assertEquals("527", Channel.cnv2to3("5R"));  //ぱぱす　　@
		assertEquals("528", Channel.cnv2to3("5S"));  //ダックス　@
		assertEquals("529", Channel.cnv2to3("5T"));  //ＤＲユタカ@
		assertEquals("530", Channel.cnv2to3("5U"));  //サッポロＤ@
		assertEquals("531", Channel.cnv2to3("5V"));  //アインズ系@
		assertEquals("532", Channel.cnv2to3("5W"));  //Ｄスギヤマ@
		assertEquals("533", Channel.cnv2to3("5X"));  //Ｖドラッグ@
		assertEquals("534", Channel.cnv2to3("5Y"));  //杏林堂ＤＲ@
		assertEquals("535", Channel.cnv2to3("5Z"));  //ウインダー@
		assertEquals("536", Channel.cnv2to3("5ｱ"));  //Ｄコスモス@
		assertEquals("537", Channel.cnv2to3("5ｲ"));  //ＤＲ１１　@
		assertEquals("538", Channel.cnv2to3("5ｳ"));  //ＤＲモリ　@
		assertEquals("539", Channel.cnv2to3("5ｴ"));  //ミドリ薬品@
		assertEquals("910", Channel.cnv2to3("9A"));  //他種類ＤＳ@
		assertEquals("911", Channel.cnv2to3("9B"));  //子供専門店@
		assertEquals("912", Channel.cnv2to3("9C"));  //生協個配　@
		assertEquals("913", Channel.cnv2to3("9D"));  //学校会社店@
		assertEquals("914", Channel.cnv2to3("9E"));  //職域販売　@
		assertEquals("915", Channel.cnv2to3("9F"));  //ＳＰ施設店@
		assertEquals("916", Channel.cnv2to3("9G"));  //コーナン　@
		assertEquals("917", Channel.cnv2to3("9H"));  //ケーヨー　@
		assertEquals("918", Channel.cnv2to3("9I"));  //カインズＨ@
		assertEquals("919", Channel.cnv2to3("9J"));  //ドンキ　　@
		assertEquals("920", Channel.cnv2to3("9K"));  //１００円Ｓ@
		assertEquals("921", Channel.cnv2to3("9L"));  //ＤマインＭ@
		assertEquals("922", Channel.cnv2to3("9M"));  //カクヤス　@
		assertEquals("923", Channel.cnv2to3("9N"));  //河内屋　　@
		assertEquals("924", Channel.cnv2to3("9O"));  //やまや　　@
		assertEquals("925", Channel.cnv2to3("9P"));  //東急ハンズ@
		assertEquals("926", Channel.cnv2to3("9Q"));  //島忠系　　@
		assertEquals("927", Channel.cnv2to3("9R"));  //ビバホーム@
		assertEquals("928", Channel.cnv2to3("9S"));  //ＪＦ本田　@
		assertEquals("929", Channel.cnv2to3("9T"));  //ダイキ　　@
		assertEquals("930", Channel.cnv2to3("9U"));  //ロイヤルＨ@
		assertEquals("931", Channel.cnv2to3("9V"));  //コメリ　　@
		assertEquals("932", Channel.cnv2to3("9W"));  //ホーマック@
		assertEquals("933", Channel.cnv2to3("9X"));  //ＪＦＡＫ　@
		assertEquals("934", Channel.cnv2to3("9Y"));  //カーマ　　@
		assertEquals("935", Channel.cnv2to3("9Z"));  //Ｊエンチョ@
		assertEquals("936", Channel.cnv2to3("9ｱ"));  //ＨＣバロー@
		assertEquals("937", Channel.cnv2to3("9ｲ"));  //ＨＰナフコ@
		assertEquals("938", Channel.cnv2to3("9ｳ"));  //ハンズマン@
		assertEquals("939", Channel.cnv2to3("9ｴ"));  //グッディ　@
		assertEquals("940", Channel.cnv2to3("9ｵ"));  //ニシムタ　@

//		assertEquals("000", Channel.cnv2to3("00"));
//		assertEquals("001", Channel.cnv2to3("01"));
//		assertEquals("002", Channel.cnv2to3("02"));
//		assertEquals("003", Channel.cnv2to3("03"));
//		assertEquals("004", Channel.cnv2to3("04"));
//		assertEquals("005", Channel.cnv2to3("05"));
//		assertEquals("006", Channel.cnv2to3("06"));
//		assertEquals("007", Channel.cnv2to3("07"));
//		assertEquals("008", Channel.cnv2to3("08"));
//		assertEquals("009", Channel.cnv2to3("09"));
//		assertEquals("010", Channel.cnv2to3("0A"));
//		assertEquals("011", Channel.cnv2to3("0B"));
//		assertEquals("012", Channel.cnv2to3("0C"));
//		assertEquals("013", Channel.cnv2to3("0D"));
//		assertEquals("014", Channel.cnv2to3("0E"));
//		assertEquals("015", Channel.cnv2to3("0F"));
//		assertEquals("016", Channel.cnv2to3("0G"));
//		assertEquals("017", Channel.cnv2to3("0H"));
//		assertEquals("018", Channel.cnv2to3("0I"));
//		assertEquals("019", Channel.cnv2to3("0J"));
//		assertEquals("020", Channel.cnv2to3("0K"));
//		assertEquals("021", Channel.cnv2to3("0L"));
//		assertEquals("022", Channel.cnv2to3("0M"));
//		assertEquals("023", Channel.cnv2to3("0N"));
//		assertEquals("024", Channel.cnv2to3("0O"));
//		assertEquals("025", Channel.cnv2to3("0P"));
//		assertEquals("026", Channel.cnv2to3("0Q"));
//		assertEquals("027", Channel.cnv2to3("0R"));
//		assertEquals("028", Channel.cnv2to3("0S"));
//		assertEquals("029", Channel.cnv2to3("0T"));
//		assertEquals("030", Channel.cnv2to3("0U"));
//		assertEquals("031", Channel.cnv2to3("0V"));
//		assertEquals("032", Channel.cnv2to3("0W"));
//		assertEquals("033", Channel.cnv2to3("0X"));
//		assertEquals("034", Channel.cnv2to3("0Y"));
//		assertEquals("035", Channel.cnv2to3("0Z"));
//		assertEquals("036", Channel.cnv2to3("0a"));
//		assertEquals("037", Channel.cnv2to3("0b"));
//		assertEquals("038", Channel.cnv2to3("0c"));
//		assertEquals("039", Channel.cnv2to3("0d"));
//		assertEquals("040", Channel.cnv2to3("0e"));
//		assertEquals("041", Channel.cnv2to3("0f"));
//		assertEquals("042", Channel.cnv2to3("0g"));
//		assertEquals("043", Channel.cnv2to3("0h"));
//		assertEquals("044", Channel.cnv2to3("0i"));
//		assertEquals("045", Channel.cnv2to3("0j"));
//		assertEquals("046", Channel.cnv2to3("0k"));
//		assertEquals("047", Channel.cnv2to3("0l"));
//		assertEquals("048", Channel.cnv2to3("0m"));
//		assertEquals("049", Channel.cnv2to3("0n"));
//		assertEquals("050", Channel.cnv2to3("0o"));
//		assertEquals("051", Channel.cnv2to3("0p"));
//		assertEquals("052", Channel.cnv2to3("0q"));
//		assertEquals("053", Channel.cnv2to3("0r"));
//		assertEquals("054", Channel.cnv2to3("0s"));
//		assertEquals("055", Channel.cnv2to3("0t"));
//		assertEquals("056", Channel.cnv2to3("0u"));
//		assertEquals("057", Channel.cnv2to3("0v"));
//		assertEquals("058", Channel.cnv2to3("0w"));
//		assertEquals("059", Channel.cnv2to3("0x"));
//		assertEquals("060", Channel.cnv2to3("0y"));
//		assertEquals("061", Channel.cnv2to3("0z"));
//		assertEquals("062", Channel.cnv2to3("0ｱ"));
//		assertEquals("063", Channel.cnv2to3("0ｲ"));
//		assertEquals("064", Channel.cnv2to3("0ｳ"));
//		assertEquals("065", Channel.cnv2to3("0ｴ"));
//		assertEquals("066", Channel.cnv2to3("0ｵ"));
//		assertEquals("067", Channel.cnv2to3("0ｶ"));
//		assertEquals("068", Channel.cnv2to3("0ｷ"));
//		assertEquals("069", Channel.cnv2to3("0ｸ"));
//		assertEquals("070", Channel.cnv2to3("0ｹ"));
//		assertEquals("071", Channel.cnv2to3("0ｺ"));
//		assertEquals("072", Channel.cnv2to3("0ｻ"));
//		assertEquals("073", Channel.cnv2to3("0ｼ"));
//		assertEquals("074", Channel.cnv2to3("0ｽ"));
//		assertEquals("075", Channel.cnv2to3("0ｾ"));
//		assertEquals("076", Channel.cnv2to3("0ｿ"));
//		assertEquals("077", Channel.cnv2to3("0ﾀ"));
//		assertEquals("078", Channel.cnv2to3("0ﾁ"));
//		assertEquals("079", Channel.cnv2to3("0ﾂ"));
//		assertEquals("080", Channel.cnv2to3("0ﾃ"));
//		assertEquals("081", Channel.cnv2to3("0ﾄ"));
//		assertEquals("082", Channel.cnv2to3("0ﾅ"));
//		assertEquals("083", Channel.cnv2to3("0ﾆ"));
//		assertEquals("084", Channel.cnv2to3("0ﾇ"));
//		assertEquals("085", Channel.cnv2to3("0ﾈ"));
//		assertEquals("086", Channel.cnv2to3("0ﾉ"));
//		assertEquals("087", Channel.cnv2to3("0ﾊ"));
//		assertEquals("088", Channel.cnv2to3("0ﾋ"));
//		assertEquals("089", Channel.cnv2to3("0ﾌ"));
//		assertEquals("090", Channel.cnv2to3("0ﾍ"));
//		assertEquals("091", Channel.cnv2to3("0ﾎ"));
//		assertEquals("092", Channel.cnv2to3("0ﾏ"));
//		assertEquals("093", Channel.cnv2to3("0ﾐ"));
//		assertEquals("094", Channel.cnv2to3("0ﾑ"));
//		assertEquals("095", Channel.cnv2to3("0ﾒ"));
//		assertEquals("096", Channel.cnv2to3("0ﾓ"));
//		assertEquals("097", Channel.cnv2to3("0ﾔ"));
//		assertEquals("098", Channel.cnv2to3("0ﾕ"));
//		assertEquals("099", Channel.cnv2to3("0ﾖ"));
//		assertEquals("100", Channel.cnv2to3("10"));
//		assertEquals("101", Channel.cnv2to3("11"));
//		assertEquals("102", Channel.cnv2to3("12"));
//		assertEquals("103", Channel.cnv2to3("13"));
//		assertEquals("104", Channel.cnv2to3("14"));
//		assertEquals("105", Channel.cnv2to3("15"));
//		assertEquals("106", Channel.cnv2to3("16"));
//		assertEquals("107", Channel.cnv2to3("17"));
//		assertEquals("108", Channel.cnv2to3("18"));
//		assertEquals("109", Channel.cnv2to3("19"));
//		assertEquals("110", Channel.cnv2to3("1A"));
//		assertEquals("111", Channel.cnv2to3("1B"));
//		assertEquals("112", Channel.cnv2to3("1C"));
//		assertEquals("113", Channel.cnv2to3("1D"));
//		assertEquals("114", Channel.cnv2to3("1E"));
//		assertEquals("115", Channel.cnv2to3("1F"));
//		assertEquals("116", Channel.cnv2to3("1G"));
//		assertEquals("117", Channel.cnv2to3("1H"));
//		assertEquals("118", Channel.cnv2to3("1I"));
//		assertEquals("119", Channel.cnv2to3("1J"));
//		assertEquals("120", Channel.cnv2to3("1K"));
//		assertEquals("121", Channel.cnv2to3("1L"));
//		assertEquals("122", Channel.cnv2to3("1M"));
//		assertEquals("123", Channel.cnv2to3("1N"));
//		assertEquals("124", Channel.cnv2to3("1O"));
//		assertEquals("125", Channel.cnv2to3("1P"));
//		assertEquals("126", Channel.cnv2to3("1Q"));
//		assertEquals("127", Channel.cnv2to3("1R"));
//		assertEquals("128", Channel.cnv2to3("1S"));
//		assertEquals("129", Channel.cnv2to3("1T"));
//		assertEquals("130", Channel.cnv2to3("1U"));
//		assertEquals("131", Channel.cnv2to3("1V"));
//		assertEquals("132", Channel.cnv2to3("1W"));
//		assertEquals("133", Channel.cnv2to3("1X"));
//		assertEquals("134", Channel.cnv2to3("1Y"));
//		assertEquals("135", Channel.cnv2to3("1Z"));
//		assertEquals("136", Channel.cnv2to3("1a"));
//		assertEquals("137", Channel.cnv2to3("1b"));
//		assertEquals("138", Channel.cnv2to3("1c"));
//		assertEquals("139", Channel.cnv2to3("1d"));
//		assertEquals("140", Channel.cnv2to3("1e"));
//		assertEquals("141", Channel.cnv2to3("1f"));
//		assertEquals("142", Channel.cnv2to3("1g"));
//		assertEquals("143", Channel.cnv2to3("1h"));
//		assertEquals("144", Channel.cnv2to3("1i"));
//		assertEquals("145", Channel.cnv2to3("1j"));
//		assertEquals("146", Channel.cnv2to3("1k"));
//		assertEquals("147", Channel.cnv2to3("1l"));
//		assertEquals("148", Channel.cnv2to3("1m"));
//		assertEquals("149", Channel.cnv2to3("1n"));
//		assertEquals("150", Channel.cnv2to3("1o"));
//		assertEquals("151", Channel.cnv2to3("1p"));
//		assertEquals("152", Channel.cnv2to3("1q"));
//		assertEquals("153", Channel.cnv2to3("1r"));
//		assertEquals("154", Channel.cnv2to3("1s"));
//		assertEquals("155", Channel.cnv2to3("1t"));
//		assertEquals("156", Channel.cnv2to3("1u"));
//		assertEquals("157", Channel.cnv2to3("1v"));
//		assertEquals("158", Channel.cnv2to3("1w"));
//		assertEquals("159", Channel.cnv2to3("1x"));
//		assertEquals("160", Channel.cnv2to3("1y"));
//		assertEquals("161", Channel.cnv2to3("1z"));
//		assertEquals("162", Channel.cnv2to3("1ｱ"));
//		assertEquals("163", Channel.cnv2to3("1ｲ"));
//		assertEquals("164", Channel.cnv2to3("1ｳ"));
//		assertEquals("165", Channel.cnv2to3("1ｴ"));
//		assertEquals("166", Channel.cnv2to3("1ｵ"));
//		assertEquals("167", Channel.cnv2to3("1ｶ"));
//		assertEquals("168", Channel.cnv2to3("1ｷ"));
//		assertEquals("169", Channel.cnv2to3("1ｸ"));
//		assertEquals("170", Channel.cnv2to3("1ｹ"));
//		assertEquals("171", Channel.cnv2to3("1ｺ"));
//		assertEquals("172", Channel.cnv2to3("1ｻ"));
//		assertEquals("173", Channel.cnv2to3("1ｼ"));
//		assertEquals("174", Channel.cnv2to3("1ｽ"));
//		assertEquals("175", Channel.cnv2to3("1ｾ"));
//		assertEquals("176", Channel.cnv2to3("1ｿ"));
//		assertEquals("177", Channel.cnv2to3("1ﾀ"));
//		assertEquals("178", Channel.cnv2to3("1ﾁ"));
//		assertEquals("179", Channel.cnv2to3("1ﾂ"));
//		assertEquals("180", Channel.cnv2to3("1ﾃ"));
//		assertEquals("181", Channel.cnv2to3("1ﾄ"));
//		assertEquals("182", Channel.cnv2to3("1ﾅ"));
//		assertEquals("183", Channel.cnv2to3("1ﾆ"));
//		assertEquals("184", Channel.cnv2to3("1ﾇ"));
//		assertEquals("185", Channel.cnv2to3("1ﾈ"));
//		assertEquals("186", Channel.cnv2to3("1ﾉ"));
//		assertEquals("187", Channel.cnv2to3("1ﾊ"));
//		assertEquals("188", Channel.cnv2to3("1ﾋ"));
//		assertEquals("189", Channel.cnv2to3("1ﾌ"));
//		assertEquals("190", Channel.cnv2to3("1ﾍ"));
//		assertEquals("191", Channel.cnv2to3("1ﾎ"));
//		assertEquals("192", Channel.cnv2to3("1ﾏ"));
//		assertEquals("193", Channel.cnv2to3("1ﾐ"));
//		assertEquals("194", Channel.cnv2to3("1ﾑ"));
//		assertEquals("195", Channel.cnv2to3("1ﾒ"));
//		assertEquals("196", Channel.cnv2to3("1ﾓ"));
//		assertEquals("197", Channel.cnv2to3("1ﾔ"));
//		assertEquals("198", Channel.cnv2to3("1ﾕ"));
//		assertEquals("199", Channel.cnv2to3("1ﾖ"));
//		assertEquals("200", Channel.cnv2to3("20"));
//		assertEquals("201", Channel.cnv2to3("21"));
//		assertEquals("202", Channel.cnv2to3("22"));
//		assertEquals("203", Channel.cnv2to3("23"));
//		assertEquals("204", Channel.cnv2to3("24"));
//		assertEquals("205", Channel.cnv2to3("25"));
//		assertEquals("206", Channel.cnv2to3("26"));
//		assertEquals("207", Channel.cnv2to3("27"));
//		assertEquals("208", Channel.cnv2to3("28"));
//		assertEquals("209", Channel.cnv2to3("29"));
//		assertEquals("210", Channel.cnv2to3("2A"));
//		assertEquals("211", Channel.cnv2to3("2B"));
//		assertEquals("212", Channel.cnv2to3("2C"));
//		assertEquals("213", Channel.cnv2to3("2D"));
//		assertEquals("214", Channel.cnv2to3("2E"));
//		assertEquals("215", Channel.cnv2to3("2F"));
//		assertEquals("216", Channel.cnv2to3("2G"));
//		assertEquals("217", Channel.cnv2to3("2H"));
//		assertEquals("218", Channel.cnv2to3("2I"));
//		assertEquals("219", Channel.cnv2to3("2J"));
//		assertEquals("220", Channel.cnv2to3("2K"));
//		assertEquals("221", Channel.cnv2to3("2L"));
//		assertEquals("222", Channel.cnv2to3("2M"));
//		assertEquals("223", Channel.cnv2to3("2N"));
//		assertEquals("224", Channel.cnv2to3("2O"));
//		assertEquals("225", Channel.cnv2to3("2P"));
//		assertEquals("226", Channel.cnv2to3("2Q"));
//		assertEquals("227", Channel.cnv2to3("2R"));
//		assertEquals("228", Channel.cnv2to3("2S"));
//		assertEquals("229", Channel.cnv2to3("2T"));
//		assertEquals("230", Channel.cnv2to3("2U"));
//		assertEquals("231", Channel.cnv2to3("2V"));
//		assertEquals("232", Channel.cnv2to3("2W"));
//		assertEquals("233", Channel.cnv2to3("2X"));
//		assertEquals("234", Channel.cnv2to3("2Y"));
//		assertEquals("235", Channel.cnv2to3("2Z"));
//		assertEquals("236", Channel.cnv2to3("2a"));
//		assertEquals("237", Channel.cnv2to3("2b"));
//		assertEquals("238", Channel.cnv2to3("2c"));
//		assertEquals("239", Channel.cnv2to3("2d"));
//		assertEquals("240", Channel.cnv2to3("2e"));
//		assertEquals("241", Channel.cnv2to3("2f"));
//		assertEquals("242", Channel.cnv2to3("2g"));
//		assertEquals("243", Channel.cnv2to3("2h"));
//		assertEquals("244", Channel.cnv2to3("2i"));
//		assertEquals("245", Channel.cnv2to3("2j"));
//		assertEquals("246", Channel.cnv2to3("2k"));
//		assertEquals("247", Channel.cnv2to3("2l"));
//		assertEquals("248", Channel.cnv2to3("2m"));
//		assertEquals("249", Channel.cnv2to3("2n"));
//		assertEquals("250", Channel.cnv2to3("2o"));
//		assertEquals("251", Channel.cnv2to3("2p"));
//		assertEquals("252", Channel.cnv2to3("2q"));
//		assertEquals("253", Channel.cnv2to3("2r"));
//		assertEquals("254", Channel.cnv2to3("2s"));
//		assertEquals("255", Channel.cnv2to3("2t"));
//		assertEquals("256", Channel.cnv2to3("2u"));
//		assertEquals("257", Channel.cnv2to3("2v"));
//		assertEquals("258", Channel.cnv2to3("2w"));
//		assertEquals("259", Channel.cnv2to3("2x"));
//		assertEquals("260", Channel.cnv2to3("2y"));
//		assertEquals("261", Channel.cnv2to3("2z"));
//		assertEquals("262", Channel.cnv2to3("2ｱ"));
//		assertEquals("263", Channel.cnv2to3("2ｲ"));
//		assertEquals("264", Channel.cnv2to3("2ｳ"));
//		assertEquals("265", Channel.cnv2to3("2ｴ"));
//		assertEquals("266", Channel.cnv2to3("2ｵ"));
//		assertEquals("267", Channel.cnv2to3("2ｶ"));
//		assertEquals("268", Channel.cnv2to3("2ｷ"));
//		assertEquals("269", Channel.cnv2to3("2ｸ"));
//		assertEquals("270", Channel.cnv2to3("2ｹ"));
//		assertEquals("271", Channel.cnv2to3("2ｺ"));
//		assertEquals("272", Channel.cnv2to3("2ｻ"));
//		assertEquals("273", Channel.cnv2to3("2ｼ"));
//		assertEquals("274", Channel.cnv2to3("2ｽ"));
//		assertEquals("275", Channel.cnv2to3("2ｾ"));
//		assertEquals("276", Channel.cnv2to3("2ｿ"));
//		assertEquals("277", Channel.cnv2to3("2ﾀ"));
//		assertEquals("278", Channel.cnv2to3("2ﾁ"));
//		assertEquals("279", Channel.cnv2to3("2ﾂ"));
//		assertEquals("280", Channel.cnv2to3("2ﾃ"));
//		assertEquals("281", Channel.cnv2to3("2ﾄ"));
//		assertEquals("282", Channel.cnv2to3("2ﾅ"));
//		assertEquals("283", Channel.cnv2to3("2ﾆ"));
//		assertEquals("284", Channel.cnv2to3("2ﾇ"));
//		assertEquals("285", Channel.cnv2to3("2ﾈ"));
//		assertEquals("286", Channel.cnv2to3("2ﾉ"));
//		assertEquals("287", Channel.cnv2to3("2ﾊ"));
//		assertEquals("288", Channel.cnv2to3("2ﾋ"));
//		assertEquals("289", Channel.cnv2to3("2ﾌ"));
//		assertEquals("290", Channel.cnv2to3("2ﾍ"));
//		assertEquals("291", Channel.cnv2to3("2ﾎ"));
//		assertEquals("292", Channel.cnv2to3("2ﾏ"));
//		assertEquals("293", Channel.cnv2to3("2ﾐ"));
//		assertEquals("294", Channel.cnv2to3("2ﾑ"));
//		assertEquals("295", Channel.cnv2to3("2ﾒ"));
//		assertEquals("296", Channel.cnv2to3("2ﾓ"));
//		assertEquals("297", Channel.cnv2to3("2ﾔ"));
//		assertEquals("298", Channel.cnv2to3("2ﾕ"));
//		assertEquals("299", Channel.cnv2to3("2ﾖ"));
//		assertEquals("300", Channel.cnv2to3("30"));
//		assertEquals("301", Channel.cnv2to3("31"));
//		assertEquals("302", Channel.cnv2to3("32"));
//		assertEquals("303", Channel.cnv2to3("33"));
//		assertEquals("304", Channel.cnv2to3("34"));
//		assertEquals("305", Channel.cnv2to3("35"));
//		assertEquals("306", Channel.cnv2to3("36"));
//		assertEquals("307", Channel.cnv2to3("37"));
//		assertEquals("308", Channel.cnv2to3("38"));
//		assertEquals("309", Channel.cnv2to3("39"));
//		assertEquals("310", Channel.cnv2to3("3A"));
//		assertEquals("311", Channel.cnv2to3("3B"));
//		assertEquals("312", Channel.cnv2to3("3C"));
//		assertEquals("313", Channel.cnv2to3("3D"));
//		assertEquals("314", Channel.cnv2to3("3E"));
//		assertEquals("315", Channel.cnv2to3("3F"));
//		assertEquals("316", Channel.cnv2to3("3G"));
//		assertEquals("317", Channel.cnv2to3("3H"));
//		assertEquals("318", Channel.cnv2to3("3I"));
//		assertEquals("319", Channel.cnv2to3("3J"));
//		assertEquals("320", Channel.cnv2to3("3K"));
//		assertEquals("321", Channel.cnv2to3("3L"));
//		assertEquals("322", Channel.cnv2to3("3M"));
//		assertEquals("323", Channel.cnv2to3("3N"));
//		assertEquals("324", Channel.cnv2to3("3O"));
//		assertEquals("325", Channel.cnv2to3("3P"));
//		assertEquals("326", Channel.cnv2to3("3Q"));
//		assertEquals("327", Channel.cnv2to3("3R"));
//		assertEquals("328", Channel.cnv2to3("3S"));
//		assertEquals("329", Channel.cnv2to3("3T"));
//		assertEquals("330", Channel.cnv2to3("3U"));
//		assertEquals("331", Channel.cnv2to3("3V"));
//		assertEquals("332", Channel.cnv2to3("3W"));
//		assertEquals("333", Channel.cnv2to3("3X"));
//		assertEquals("334", Channel.cnv2to3("3Y"));
//		assertEquals("335", Channel.cnv2to3("3Z"));
//		assertEquals("336", Channel.cnv2to3("3a"));
//		assertEquals("337", Channel.cnv2to3("3b"));
//		assertEquals("338", Channel.cnv2to3("3c"));
//		assertEquals("339", Channel.cnv2to3("3d"));
//		assertEquals("340", Channel.cnv2to3("3e"));
//		assertEquals("341", Channel.cnv2to3("3f"));
//		assertEquals("342", Channel.cnv2to3("3g"));
//		assertEquals("343", Channel.cnv2to3("3h"));
//		assertEquals("344", Channel.cnv2to3("3i"));
//		assertEquals("345", Channel.cnv2to3("3j"));
//		assertEquals("346", Channel.cnv2to3("3k"));
//		assertEquals("347", Channel.cnv2to3("3l"));
//		assertEquals("348", Channel.cnv2to3("3m"));
//		assertEquals("349", Channel.cnv2to3("3n"));
//		assertEquals("350", Channel.cnv2to3("3o"));
//		assertEquals("351", Channel.cnv2to3("3p"));
//		assertEquals("352", Channel.cnv2to3("3q"));
//		assertEquals("353", Channel.cnv2to3("3r"));
//		assertEquals("354", Channel.cnv2to3("3s"));
//		assertEquals("355", Channel.cnv2to3("3t"));
//		assertEquals("356", Channel.cnv2to3("3u"));
//		assertEquals("357", Channel.cnv2to3("3v"));
//		assertEquals("358", Channel.cnv2to3("3w"));
//		assertEquals("359", Channel.cnv2to3("3x"));
//		assertEquals("360", Channel.cnv2to3("3y"));
//		assertEquals("361", Channel.cnv2to3("3z"));
//		assertEquals("362", Channel.cnv2to3("3ｱ"));
//		assertEquals("363", Channel.cnv2to3("3ｲ"));
//		assertEquals("364", Channel.cnv2to3("3ｳ"));
//		assertEquals("365", Channel.cnv2to3("3ｴ"));
//		assertEquals("366", Channel.cnv2to3("3ｵ"));
//		assertEquals("367", Channel.cnv2to3("3ｶ"));
//		assertEquals("368", Channel.cnv2to3("3ｷ"));
//		assertEquals("369", Channel.cnv2to3("3ｸ"));
//		assertEquals("370", Channel.cnv2to3("3ｹ"));
//		assertEquals("371", Channel.cnv2to3("3ｺ"));
//		assertEquals("372", Channel.cnv2to3("3ｻ"));
//		assertEquals("373", Channel.cnv2to3("3ｼ"));
//		assertEquals("374", Channel.cnv2to3("3ｽ"));
//		assertEquals("375", Channel.cnv2to3("3ｾ"));
//		assertEquals("376", Channel.cnv2to3("3ｿ"));
//		assertEquals("377", Channel.cnv2to3("3ﾀ"));
//		assertEquals("378", Channel.cnv2to3("3ﾁ"));
//		assertEquals("379", Channel.cnv2to3("3ﾂ"));
//		assertEquals("380", Channel.cnv2to3("3ﾃ"));
//		assertEquals("381", Channel.cnv2to3("3ﾄ"));
//		assertEquals("382", Channel.cnv2to3("3ﾅ"));
//		assertEquals("383", Channel.cnv2to3("3ﾆ"));
//		assertEquals("384", Channel.cnv2to3("3ﾇ"));
//		assertEquals("385", Channel.cnv2to3("3ﾈ"));
//		assertEquals("386", Channel.cnv2to3("3ﾉ"));
//		assertEquals("387", Channel.cnv2to3("3ﾊ"));
//		assertEquals("388", Channel.cnv2to3("3ﾋ"));
//		assertEquals("389", Channel.cnv2to3("3ﾌ"));
//		assertEquals("390", Channel.cnv2to3("3ﾍ"));
//		assertEquals("391", Channel.cnv2to3("3ﾎ"));
//		assertEquals("392", Channel.cnv2to3("3ﾏ"));
//		assertEquals("393", Channel.cnv2to3("3ﾐ"));
//		assertEquals("394", Channel.cnv2to3("3ﾑ"));
//		assertEquals("395", Channel.cnv2to3("3ﾒ"));
//		assertEquals("396", Channel.cnv2to3("3ﾓ"));
//		assertEquals("397", Channel.cnv2to3("3ﾔ"));
//		assertEquals("398", Channel.cnv2to3("3ﾕ"));
//		assertEquals("399", Channel.cnv2to3("3ﾖ"));
//		assertEquals("400", Channel.cnv2to3("40"));
//		assertEquals("401", Channel.cnv2to3("41"));
//		assertEquals("402", Channel.cnv2to3("42"));
//		assertEquals("403", Channel.cnv2to3("43"));
//		assertEquals("404", Channel.cnv2to3("44"));
//		assertEquals("405", Channel.cnv2to3("45"));
//		assertEquals("406", Channel.cnv2to3("46"));
//		assertEquals("407", Channel.cnv2to3("47"));
//		assertEquals("408", Channel.cnv2to3("48"));
//		assertEquals("409", Channel.cnv2to3("49"));
//		assertEquals("410", Channel.cnv2to3("4A"));
//		assertEquals("411", Channel.cnv2to3("4B"));
//		assertEquals("412", Channel.cnv2to3("4C"));
//		assertEquals("413", Channel.cnv2to3("4D"));
//		assertEquals("414", Channel.cnv2to3("4E"));
//		assertEquals("415", Channel.cnv2to3("4F"));
//		assertEquals("416", Channel.cnv2to3("4G"));
//		assertEquals("417", Channel.cnv2to3("4H"));
//		assertEquals("418", Channel.cnv2to3("4I"));
//		assertEquals("419", Channel.cnv2to3("4J"));
//		assertEquals("420", Channel.cnv2to3("4K"));
//		assertEquals("421", Channel.cnv2to3("4L"));
//		assertEquals("422", Channel.cnv2to3("4M"));
//		assertEquals("423", Channel.cnv2to3("4N"));
//		assertEquals("424", Channel.cnv2to3("4O"));
//		assertEquals("425", Channel.cnv2to3("4P"));
//		assertEquals("426", Channel.cnv2to3("4Q"));
//		assertEquals("427", Channel.cnv2to3("4R"));
//		assertEquals("428", Channel.cnv2to3("4S"));
//		assertEquals("429", Channel.cnv2to3("4T"));
//		assertEquals("430", Channel.cnv2to3("4U"));
//		assertEquals("431", Channel.cnv2to3("4V"));
//		assertEquals("432", Channel.cnv2to3("4W"));
//		assertEquals("433", Channel.cnv2to3("4X"));
//		assertEquals("434", Channel.cnv2to3("4Y"));
//		assertEquals("435", Channel.cnv2to3("4Z"));
//		assertEquals("436", Channel.cnv2to3("4a"));
//		assertEquals("437", Channel.cnv2to3("4b"));
//		assertEquals("438", Channel.cnv2to3("4c"));
//		assertEquals("439", Channel.cnv2to3("4d"));
//		assertEquals("440", Channel.cnv2to3("4e"));
//		assertEquals("441", Channel.cnv2to3("4f"));
//		assertEquals("442", Channel.cnv2to3("4g"));
//		assertEquals("443", Channel.cnv2to3("4h"));
//		assertEquals("444", Channel.cnv2to3("4i"));
//		assertEquals("445", Channel.cnv2to3("4j"));
//		assertEquals("446", Channel.cnv2to3("4k"));
//		assertEquals("447", Channel.cnv2to3("4l"));
//		assertEquals("448", Channel.cnv2to3("4m"));
//		assertEquals("449", Channel.cnv2to3("4n"));
//		assertEquals("450", Channel.cnv2to3("4o"));
//		assertEquals("451", Channel.cnv2to3("4p"));
//		assertEquals("452", Channel.cnv2to3("4q"));
//		assertEquals("453", Channel.cnv2to3("4r"));
//		assertEquals("454", Channel.cnv2to3("4s"));
//		assertEquals("455", Channel.cnv2to3("4t"));
//		assertEquals("456", Channel.cnv2to3("4u"));
//		assertEquals("457", Channel.cnv2to3("4v"));
//		assertEquals("458", Channel.cnv2to3("4w"));
//		assertEquals("459", Channel.cnv2to3("4x"));
//		assertEquals("460", Channel.cnv2to3("4y"));
//		assertEquals("461", Channel.cnv2to3("4z"));
//		assertEquals("462", Channel.cnv2to3("4ｱ"));
//		assertEquals("463", Channel.cnv2to3("4ｲ"));
//		assertEquals("464", Channel.cnv2to3("4ｳ"));
//		assertEquals("465", Channel.cnv2to3("4ｴ"));
//		assertEquals("466", Channel.cnv2to3("4ｵ"));
//		assertEquals("467", Channel.cnv2to3("4ｶ"));
//		assertEquals("468", Channel.cnv2to3("4ｷ"));
//		assertEquals("469", Channel.cnv2to3("4ｸ"));
//		assertEquals("470", Channel.cnv2to3("4ｹ"));
//		assertEquals("471", Channel.cnv2to3("4ｺ"));
//		assertEquals("472", Channel.cnv2to3("4ｻ"));
//		assertEquals("473", Channel.cnv2to3("4ｼ"));
//		assertEquals("474", Channel.cnv2to3("4ｽ"));
//		assertEquals("475", Channel.cnv2to3("4ｾ"));
//		assertEquals("476", Channel.cnv2to3("4ｿ"));
//		assertEquals("477", Channel.cnv2to3("4ﾀ"));
//		assertEquals("478", Channel.cnv2to3("4ﾁ"));
//		assertEquals("479", Channel.cnv2to3("4ﾂ"));
//		assertEquals("480", Channel.cnv2to3("4ﾃ"));
//		assertEquals("481", Channel.cnv2to3("4ﾄ"));
//		assertEquals("482", Channel.cnv2to3("4ﾅ"));
//		assertEquals("483", Channel.cnv2to3("4ﾆ"));
//		assertEquals("484", Channel.cnv2to3("4ﾇ"));
//		assertEquals("485", Channel.cnv2to3("4ﾈ"));
//		assertEquals("486", Channel.cnv2to3("4ﾉ"));
//		assertEquals("487", Channel.cnv2to3("4ﾊ"));
//		assertEquals("488", Channel.cnv2to3("4ﾋ"));
//		assertEquals("489", Channel.cnv2to3("4ﾌ"));
//		assertEquals("490", Channel.cnv2to3("4ﾍ"));
//		assertEquals("491", Channel.cnv2to3("4ﾎ"));
//		assertEquals("492", Channel.cnv2to3("4ﾏ"));
//		assertEquals("493", Channel.cnv2to3("4ﾐ"));
//		assertEquals("494", Channel.cnv2to3("4ﾑ"));
//		assertEquals("495", Channel.cnv2to3("4ﾒ"));
//		assertEquals("496", Channel.cnv2to3("4ﾓ"));
//		assertEquals("497", Channel.cnv2to3("4ﾔ"));
//		assertEquals("498", Channel.cnv2to3("4ﾕ"));
//		assertEquals("499", Channel.cnv2to3("4ﾖ"));
//		assertEquals("500", Channel.cnv2to3("50"));
//		assertEquals("501", Channel.cnv2to3("51"));
//		assertEquals("502", Channel.cnv2to3("52"));
//		assertEquals("503", Channel.cnv2to3("53"));
//		assertEquals("504", Channel.cnv2to3("54"));
//		assertEquals("505", Channel.cnv2to3("55"));
//		assertEquals("506", Channel.cnv2to3("56"));
//		assertEquals("507", Channel.cnv2to3("57"));
//		assertEquals("508", Channel.cnv2to3("58"));
//		assertEquals("509", Channel.cnv2to3("59"));
//		assertEquals("510", Channel.cnv2to3("5A"));
//		assertEquals("511", Channel.cnv2to3("5B"));
//		assertEquals("512", Channel.cnv2to3("5C"));
//		assertEquals("513", Channel.cnv2to3("5D"));
//		assertEquals("514", Channel.cnv2to3("5E"));
//		assertEquals("515", Channel.cnv2to3("5F"));
//		assertEquals("516", Channel.cnv2to3("5G"));
//		assertEquals("517", Channel.cnv2to3("5H"));
//		assertEquals("518", Channel.cnv2to3("5I"));
//		assertEquals("519", Channel.cnv2to3("5J"));
//		assertEquals("520", Channel.cnv2to3("5K"));
//		assertEquals("521", Channel.cnv2to3("5L"));
//		assertEquals("522", Channel.cnv2to3("5M"));
//		assertEquals("523", Channel.cnv2to3("5N"));
//		assertEquals("524", Channel.cnv2to3("5O"));
//		assertEquals("525", Channel.cnv2to3("5P"));
//		assertEquals("526", Channel.cnv2to3("5Q"));
//		assertEquals("527", Channel.cnv2to3("5R"));
//		assertEquals("528", Channel.cnv2to3("5S"));
//		assertEquals("529", Channel.cnv2to3("5T"));
//		assertEquals("530", Channel.cnv2to3("5U"));
//		assertEquals("531", Channel.cnv2to3("5V"));
//		assertEquals("532", Channel.cnv2to3("5W"));
//		assertEquals("533", Channel.cnv2to3("5X"));
//		assertEquals("534", Channel.cnv2to3("5Y"));
//		assertEquals("535", Channel.cnv2to3("5Z"));
//		assertEquals("536", Channel.cnv2to3("5a"));
//		assertEquals("537", Channel.cnv2to3("5b"));
//		assertEquals("538", Channel.cnv2to3("5c"));
//		assertEquals("539", Channel.cnv2to3("5d"));
//		assertEquals("540", Channel.cnv2to3("5e"));
//		assertEquals("541", Channel.cnv2to3("5f"));
//		assertEquals("542", Channel.cnv2to3("5g"));
//		assertEquals("543", Channel.cnv2to3("5h"));
//		assertEquals("544", Channel.cnv2to3("5i"));
//		assertEquals("545", Channel.cnv2to3("5j"));
//		assertEquals("546", Channel.cnv2to3("5k"));
//		assertEquals("547", Channel.cnv2to3("5l"));
//		assertEquals("548", Channel.cnv2to3("5m"));
//		assertEquals("549", Channel.cnv2to3("5n"));
//		assertEquals("550", Channel.cnv2to3("5o"));
//		assertEquals("551", Channel.cnv2to3("5p"));
//		assertEquals("552", Channel.cnv2to3("5q"));
//		assertEquals("553", Channel.cnv2to3("5r"));
//		assertEquals("554", Channel.cnv2to3("5s"));
//		assertEquals("555", Channel.cnv2to3("5t"));
//		assertEquals("556", Channel.cnv2to3("5u"));
//		assertEquals("557", Channel.cnv2to3("5v"));
//		assertEquals("558", Channel.cnv2to3("5w"));
//		assertEquals("559", Channel.cnv2to3("5x"));
//		assertEquals("560", Channel.cnv2to3("5y"));
//		assertEquals("561", Channel.cnv2to3("5z"));
//		assertEquals("562", Channel.cnv2to3("5ｱ"));
//		assertEquals("563", Channel.cnv2to3("5ｲ"));
//		assertEquals("564", Channel.cnv2to3("5ｳ"));
//		assertEquals("565", Channel.cnv2to3("5ｴ"));
//		assertEquals("566", Channel.cnv2to3("5ｵ"));
//		assertEquals("567", Channel.cnv2to3("5ｶ"));
//		assertEquals("568", Channel.cnv2to3("5ｷ"));
//		assertEquals("569", Channel.cnv2to3("5ｸ"));
//		assertEquals("570", Channel.cnv2to3("5ｹ"));
//		assertEquals("571", Channel.cnv2to3("5ｺ"));
//		assertEquals("572", Channel.cnv2to3("5ｻ"));
//		assertEquals("573", Channel.cnv2to3("5ｼ"));
//		assertEquals("574", Channel.cnv2to3("5ｽ"));
//		assertEquals("575", Channel.cnv2to3("5ｾ"));
//		assertEquals("576", Channel.cnv2to3("5ｿ"));
//		assertEquals("577", Channel.cnv2to3("5ﾀ"));
//		assertEquals("578", Channel.cnv2to3("5ﾁ"));
//		assertEquals("579", Channel.cnv2to3("5ﾂ"));
//		assertEquals("580", Channel.cnv2to3("5ﾃ"));
//		assertEquals("581", Channel.cnv2to3("5ﾄ"));
//		assertEquals("582", Channel.cnv2to3("5ﾅ"));
//		assertEquals("583", Channel.cnv2to3("5ﾆ"));
//		assertEquals("584", Channel.cnv2to3("5ﾇ"));
//		assertEquals("585", Channel.cnv2to3("5ﾈ"));
//		assertEquals("586", Channel.cnv2to3("5ﾉ"));
//		assertEquals("587", Channel.cnv2to3("5ﾊ"));
//		assertEquals("588", Channel.cnv2to3("5ﾋ"));
//		assertEquals("589", Channel.cnv2to3("5ﾌ"));
//		assertEquals("590", Channel.cnv2to3("5ﾍ"));
//		assertEquals("591", Channel.cnv2to3("5ﾎ"));
//		assertEquals("592", Channel.cnv2to3("5ﾏ"));
//		assertEquals("593", Channel.cnv2to3("5ﾐ"));
//		assertEquals("594", Channel.cnv2to3("5ﾑ"));
//		assertEquals("595", Channel.cnv2to3("5ﾒ"));
//		assertEquals("596", Channel.cnv2to3("5ﾓ"));
//		assertEquals("597", Channel.cnv2to3("5ﾔ"));
//		assertEquals("598", Channel.cnv2to3("5ﾕ"));
//		assertEquals("599", Channel.cnv2to3("5ﾖ"));
//		assertEquals("600", Channel.cnv2to3("60"));
//		assertEquals("601", Channel.cnv2to3("61"));
//		assertEquals("602", Channel.cnv2to3("62"));
//		assertEquals("603", Channel.cnv2to3("63"));
//		assertEquals("604", Channel.cnv2to3("64"));
//		assertEquals("605", Channel.cnv2to3("65"));
//		assertEquals("606", Channel.cnv2to3("66"));
//		assertEquals("607", Channel.cnv2to3("67"));
//		assertEquals("698", Channel.cnv2to3("68"));
//		assertEquals("699", Channel.cnv2to3("69"));
//		assertEquals("610", Channel.cnv2to3("6A"));
//		assertEquals("611", Channel.cnv2to3("6B"));
//		assertEquals("612", Channel.cnv2to3("6C"));
//		assertEquals("613", Channel.cnv2to3("6D"));
//		assertEquals("614", Channel.cnv2to3("6E"));
//		assertEquals("615", Channel.cnv2to3("6F"));
//		assertEquals("616", Channel.cnv2to3("6G"));
//		assertEquals("617", Channel.cnv2to3("6H"));
//		assertEquals("618", Channel.cnv2to3("6I"));
//		assertEquals("619", Channel.cnv2to3("6J"));
//		assertEquals("620", Channel.cnv2to3("6K"));
//		assertEquals("621", Channel.cnv2to3("6L"));
//		assertEquals("622", Channel.cnv2to3("6M"));
//		assertEquals("623", Channel.cnv2to3("6N"));
//		assertEquals("624", Channel.cnv2to3("6O"));
//		assertEquals("625", Channel.cnv2to3("6P"));
//		assertEquals("626", Channel.cnv2to3("6Q"));
//		assertEquals("627", Channel.cnv2to3("6R"));
//		assertEquals("628", Channel.cnv2to3("6S"));
//		assertEquals("629", Channel.cnv2to3("6T"));
//		assertEquals("630", Channel.cnv2to3("6U"));
//		assertEquals("631", Channel.cnv2to3("6V"));
//		assertEquals("632", Channel.cnv2to3("6W"));
//		assertEquals("633", Channel.cnv2to3("6X"));
//		assertEquals("634", Channel.cnv2to3("6Y"));
//		assertEquals("635", Channel.cnv2to3("6Z"));
//		assertEquals("636", Channel.cnv2to3("6a"));
//		assertEquals("637", Channel.cnv2to3("6b"));
//		assertEquals("638", Channel.cnv2to3("6c"));
//		assertEquals("639", Channel.cnv2to3("6d"));
//		assertEquals("640", Channel.cnv2to3("6e"));
//		assertEquals("641", Channel.cnv2to3("6f"));
//		assertEquals("642", Channel.cnv2to3("6g"));
//		assertEquals("643", Channel.cnv2to3("6h"));
//		assertEquals("644", Channel.cnv2to3("6i"));
//		assertEquals("645", Channel.cnv2to3("6j"));
//		assertEquals("646", Channel.cnv2to3("6k"));
//		assertEquals("647", Channel.cnv2to3("6l"));
//		assertEquals("648", Channel.cnv2to3("6m"));
//		assertEquals("649", Channel.cnv2to3("6n"));
//		assertEquals("650", Channel.cnv2to3("6o"));
//		assertEquals("651", Channel.cnv2to3("6p"));
//		assertEquals("652", Channel.cnv2to3("6q"));
//		assertEquals("653", Channel.cnv2to3("6r"));
//		assertEquals("654", Channel.cnv2to3("6s"));
//		assertEquals("655", Channel.cnv2to3("6t"));
//		assertEquals("656", Channel.cnv2to3("6u"));
//		assertEquals("657", Channel.cnv2to3("6v"));
//		assertEquals("658", Channel.cnv2to3("6w"));
//		assertEquals("659", Channel.cnv2to3("6x"));
//		assertEquals("660", Channel.cnv2to3("6y"));
//		assertEquals("661", Channel.cnv2to3("6z"));
//		assertEquals("662", Channel.cnv2to3("6ｱ"));
//		assertEquals("663", Channel.cnv2to3("6ｲ"));
//		assertEquals("664", Channel.cnv2to3("6ｳ"));
//		assertEquals("665", Channel.cnv2to3("6ｴ"));
//		assertEquals("666", Channel.cnv2to3("6ｵ"));
//		assertEquals("667", Channel.cnv2to3("6ｶ"));
//		assertEquals("668", Channel.cnv2to3("6ｷ"));
//		assertEquals("669", Channel.cnv2to3("6ｸ"));
//		assertEquals("670", Channel.cnv2to3("6ｹ"));
//		assertEquals("671", Channel.cnv2to3("6ｺ"));
//		assertEquals("672", Channel.cnv2to3("6ｻ"));
//		assertEquals("673", Channel.cnv2to3("6ｼ"));
//		assertEquals("674", Channel.cnv2to3("6ｽ"));
//		assertEquals("675", Channel.cnv2to3("6ｾ"));
//		assertEquals("676", Channel.cnv2to3("6ｿ"));
//		assertEquals("677", Channel.cnv2to3("6ﾀ"));
//		assertEquals("678", Channel.cnv2to3("6ﾁ"));
//		assertEquals("679", Channel.cnv2to3("6ﾂ"));
//		assertEquals("680", Channel.cnv2to3("6ﾃ"));
//		assertEquals("681", Channel.cnv2to3("6ﾄ"));
//		assertEquals("682", Channel.cnv2to3("6ﾅ"));
//		assertEquals("683", Channel.cnv2to3("6ﾆ"));
//		assertEquals("684", Channel.cnv2to3("6ﾇ"));
//		assertEquals("685", Channel.cnv2to3("6ﾈ"));
//		assertEquals("686", Channel.cnv2to3("6ﾉ"));
//		assertEquals("687", Channel.cnv2to3("6ﾊ"));
//		assertEquals("688", Channel.cnv2to3("6ﾋ"));
//		assertEquals("689", Channel.cnv2to3("6ﾌ"));
//		assertEquals("690", Channel.cnv2to3("6ﾍ"));
//		assertEquals("691", Channel.cnv2to3("6ﾎ"));
//		assertEquals("692", Channel.cnv2to3("6ﾏ"));
//		assertEquals("693", Channel.cnv2to3("6ﾐ"));
//		assertEquals("694", Channel.cnv2to3("6ﾑ"));
//		assertEquals("695", Channel.cnv2to3("6ﾒ"));
//		assertEquals("696", Channel.cnv2to3("6ﾓ"));
//		assertEquals("697", Channel.cnv2to3("6ﾔ"));
//		assertEquals("698", Channel.cnv2to3("68"));
//		assertEquals("699", Channel.cnv2to3("69"));
//		assertEquals("700", Channel.cnv2to3("70"));
//		assertEquals("701", Channel.cnv2to3("71"));
//		assertEquals("702", Channel.cnv2to3("72"));
//		assertEquals("703", Channel.cnv2to3("73"));
//		assertEquals("704", Channel.cnv2to3("74"));
//		assertEquals("705", Channel.cnv2to3("75"));
//		assertEquals("706", Channel.cnv2to3("76"));
//		assertEquals("707", Channel.cnv2to3("77"));
//		assertEquals("708", Channel.cnv2to3("78"));
//		assertEquals("709", Channel.cnv2to3("79"));
//		assertEquals("710", Channel.cnv2to3("7A"));
//		assertEquals("711", Channel.cnv2to3("7B"));
//		assertEquals("712", Channel.cnv2to3("7C"));
//		assertEquals("713", Channel.cnv2to3("7D"));
//		assertEquals("714", Channel.cnv2to3("7E"));
//		assertEquals("715", Channel.cnv2to3("7F"));
//		assertEquals("716", Channel.cnv2to3("7G"));
//		assertEquals("717", Channel.cnv2to3("7H"));
//		assertEquals("718", Channel.cnv2to3("7I"));
//		assertEquals("719", Channel.cnv2to3("7J"));
//		assertEquals("720", Channel.cnv2to3("7K"));
//		assertEquals("721", Channel.cnv2to3("7L"));
//		assertEquals("722", Channel.cnv2to3("7M"));
//		assertEquals("723", Channel.cnv2to3("7N"));
//		assertEquals("724", Channel.cnv2to3("7O"));
//		assertEquals("725", Channel.cnv2to3("7P"));
//		assertEquals("726", Channel.cnv2to3("7Q"));
//		assertEquals("727", Channel.cnv2to3("7R"));
//		assertEquals("728", Channel.cnv2to3("7S"));
//		assertEquals("729", Channel.cnv2to3("7T"));
//		assertEquals("730", Channel.cnv2to3("7U"));
//		assertEquals("731", Channel.cnv2to3("7V"));
//		assertEquals("732", Channel.cnv2to3("7W"));
//		assertEquals("733", Channel.cnv2to3("7X"));
//		assertEquals("734", Channel.cnv2to3("7Y"));
//		assertEquals("735", Channel.cnv2to3("7Z"));
//		assertEquals("736", Channel.cnv2to3("7a"));
//		assertEquals("737", Channel.cnv2to3("7b"));
//		assertEquals("738", Channel.cnv2to3("7c"));
//		assertEquals("739", Channel.cnv2to3("7d"));
//		assertEquals("740", Channel.cnv2to3("7e"));
//		assertEquals("741", Channel.cnv2to3("7f"));
//		assertEquals("742", Channel.cnv2to3("7g"));
//		assertEquals("743", Channel.cnv2to3("7h"));
//		assertEquals("744", Channel.cnv2to3("7i"));
//		assertEquals("745", Channel.cnv2to3("7j"));
//		assertEquals("746", Channel.cnv2to3("7k"));
//		assertEquals("747", Channel.cnv2to3("7l"));
//		assertEquals("748", Channel.cnv2to3("7m"));
//		assertEquals("749", Channel.cnv2to3("7n"));
//		assertEquals("750", Channel.cnv2to3("7o"));
//		assertEquals("751", Channel.cnv2to3("7p"));
//		assertEquals("752", Channel.cnv2to3("7q"));
//		assertEquals("753", Channel.cnv2to3("7r"));
//		assertEquals("754", Channel.cnv2to3("7s"));
//		assertEquals("755", Channel.cnv2to3("7t"));
//		assertEquals("756", Channel.cnv2to3("7u"));
//		assertEquals("757", Channel.cnv2to3("7v"));
//		assertEquals("758", Channel.cnv2to3("7w"));
//		assertEquals("759", Channel.cnv2to3("7x"));
//		assertEquals("760", Channel.cnv2to3("7y"));
//		assertEquals("761", Channel.cnv2to3("7z"));
//		assertEquals("762", Channel.cnv2to3("7ｱ"));
//		assertEquals("763", Channel.cnv2to3("7ｲ"));
//		assertEquals("764", Channel.cnv2to3("7ｳ"));
//		assertEquals("765", Channel.cnv2to3("7ｴ"));
//		assertEquals("766", Channel.cnv2to3("7ｵ"));
//		assertEquals("767", Channel.cnv2to3("7ｶ"));
//		assertEquals("768", Channel.cnv2to3("7ｷ"));
//		assertEquals("769", Channel.cnv2to3("7ｸ"));
//		assertEquals("770", Channel.cnv2to3("7ｹ"));
//		assertEquals("771", Channel.cnv2to3("7ｺ"));
//		assertEquals("772", Channel.cnv2to3("7ｻ"));
//		assertEquals("773", Channel.cnv2to3("7ｼ"));
//		assertEquals("774", Channel.cnv2to3("7ｽ"));
//		assertEquals("775", Channel.cnv2to3("7ｾ"));
//		assertEquals("776", Channel.cnv2to3("7ｿ"));
//		assertEquals("777", Channel.cnv2to3("7ﾀ"));
//		assertEquals("778", Channel.cnv2to3("7ﾁ"));
//		assertEquals("779", Channel.cnv2to3("7ﾂ"));
//		assertEquals("780", Channel.cnv2to3("7ﾃ"));
//		assertEquals("781", Channel.cnv2to3("7ﾄ"));
//		assertEquals("782", Channel.cnv2to3("7ﾅ"));
//		assertEquals("783", Channel.cnv2to3("7ﾆ"));
//		assertEquals("784", Channel.cnv2to3("7ﾇ"));
//		assertEquals("785", Channel.cnv2to3("7ﾈ"));
//		assertEquals("786", Channel.cnv2to3("7ﾉ"));
//		assertEquals("787", Channel.cnv2to3("7ﾊ"));
//		assertEquals("788", Channel.cnv2to3("7ﾋ"));
//		assertEquals("789", Channel.cnv2to3("7ﾌ"));
//		assertEquals("790", Channel.cnv2to3("7ﾍ"));
//		assertEquals("791", Channel.cnv2to3("7ﾎ"));
//		assertEquals("792", Channel.cnv2to3("7ﾏ"));
//		assertEquals("793", Channel.cnv2to3("7ﾐ"));
//		assertEquals("794", Channel.cnv2to3("7ﾑ"));
//		assertEquals("795", Channel.cnv2to3("7ﾒ"));
//		assertEquals("796", Channel.cnv2to3("7ﾓ"));
//		assertEquals("797", Channel.cnv2to3("7ﾔ"));
//		assertEquals("798", Channel.cnv2to3("7ﾕ"));
//		assertEquals("799", Channel.cnv2to3("7ﾖ"));
//		assertEquals("800", Channel.cnv2to3("80"));
//		assertEquals("801", Channel.cnv2to3("81"));
//		assertEquals("802", Channel.cnv2to3("82"));
//		assertEquals("803", Channel.cnv2to3("83"));
//		assertEquals("804", Channel.cnv2to3("84"));
//		assertEquals("805", Channel.cnv2to3("85"));
//		assertEquals("806", Channel.cnv2to3("86"));
//		assertEquals("807", Channel.cnv2to3("87"));
//		assertEquals("808", Channel.cnv2to3("88"));
//		assertEquals("809", Channel.cnv2to3("89"));
//		assertEquals("810", Channel.cnv2to3("8A"));
//		assertEquals("811", Channel.cnv2to3("8B"));
//		assertEquals("812", Channel.cnv2to3("8C"));
//		assertEquals("813", Channel.cnv2to3("8D"));
//		assertEquals("814", Channel.cnv2to3("8E"));
//		assertEquals("815", Channel.cnv2to3("8F"));
//		assertEquals("816", Channel.cnv2to3("8G"));
//		assertEquals("817", Channel.cnv2to3("8H"));
//		assertEquals("818", Channel.cnv2to3("8I"));
//		assertEquals("819", Channel.cnv2to3("8J"));
//		assertEquals("820", Channel.cnv2to3("8K"));
//		assertEquals("821", Channel.cnv2to3("8L"));
//		assertEquals("822", Channel.cnv2to3("8M"));
//		assertEquals("823", Channel.cnv2to3("8N"));
//		assertEquals("824", Channel.cnv2to3("8O"));
//		assertEquals("825", Channel.cnv2to3("8P"));
//		assertEquals("826", Channel.cnv2to3("8Q"));
//		assertEquals("827", Channel.cnv2to3("8R"));
//		assertEquals("828", Channel.cnv2to3("8S"));
//		assertEquals("829", Channel.cnv2to3("8T"));
//		assertEquals("830", Channel.cnv2to3("8U"));
//		assertEquals("831", Channel.cnv2to3("8V"));
//		assertEquals("832", Channel.cnv2to3("8W"));
//		assertEquals("833", Channel.cnv2to3("8X"));
//		assertEquals("834", Channel.cnv2to3("8Y"));
//		assertEquals("835", Channel.cnv2to3("8Z"));
//		assertEquals("836", Channel.cnv2to3("8a"));
//		assertEquals("837", Channel.cnv2to3("8b"));
//		assertEquals("838", Channel.cnv2to3("8c"));
//		assertEquals("839", Channel.cnv2to3("8d"));
//		assertEquals("840", Channel.cnv2to3("8e"));
//		assertEquals("841", Channel.cnv2to3("8f"));
//		assertEquals("842", Channel.cnv2to3("8g"));
//		assertEquals("843", Channel.cnv2to3("8h"));
//		assertEquals("844", Channel.cnv2to3("8i"));
//		assertEquals("845", Channel.cnv2to3("8j"));
//		assertEquals("846", Channel.cnv2to3("8k"));
//		assertEquals("847", Channel.cnv2to3("8l"));
//		assertEquals("848", Channel.cnv2to3("8m"));
//		assertEquals("849", Channel.cnv2to3("8n"));
//		assertEquals("850", Channel.cnv2to3("8o"));
//		assertEquals("851", Channel.cnv2to3("8p"));
//		assertEquals("852", Channel.cnv2to3("8q"));
//		assertEquals("853", Channel.cnv2to3("8r"));
//		assertEquals("854", Channel.cnv2to3("8s"));
//		assertEquals("855", Channel.cnv2to3("8t"));
//		assertEquals("856", Channel.cnv2to3("8u"));
//		assertEquals("857", Channel.cnv2to3("8v"));
//		assertEquals("858", Channel.cnv2to3("8w"));
//		assertEquals("859", Channel.cnv2to3("8x"));
//		assertEquals("860", Channel.cnv2to3("8y"));
//		assertEquals("861", Channel.cnv2to3("8z"));
//		assertEquals("862", Channel.cnv2to3("8ｱ"));
//		assertEquals("863", Channel.cnv2to3("8ｲ"));
//		assertEquals("864", Channel.cnv2to3("8ｳ"));
//		assertEquals("865", Channel.cnv2to3("8ｴ"));
//		assertEquals("866", Channel.cnv2to3("8ｵ"));
//		assertEquals("867", Channel.cnv2to3("8ｶ"));
//		assertEquals("868", Channel.cnv2to3("8ｷ"));
//		assertEquals("869", Channel.cnv2to3("8ｸ"));
//		assertEquals("870", Channel.cnv2to3("8ｹ"));
//		assertEquals("871", Channel.cnv2to3("8ｺ"));
//		assertEquals("872", Channel.cnv2to3("8ｻ"));
//		assertEquals("873", Channel.cnv2to3("8ｼ"));
//		assertEquals("874", Channel.cnv2to3("8ｽ"));
//		assertEquals("875", Channel.cnv2to3("8ｾ"));
//		assertEquals("876", Channel.cnv2to3("8ｿ"));
//		assertEquals("877", Channel.cnv2to3("8ﾀ"));
//		assertEquals("878", Channel.cnv2to3("8ﾁ"));
//		assertEquals("879", Channel.cnv2to3("8ﾂ"));
//		assertEquals("880", Channel.cnv2to3("8ﾃ"));
//		assertEquals("881", Channel.cnv2to3("8ﾄ"));
//		assertEquals("882", Channel.cnv2to3("8ﾅ"));
//		assertEquals("883", Channel.cnv2to3("8ﾆ"));
//		assertEquals("884", Channel.cnv2to3("8ﾇ"));
//		assertEquals("885", Channel.cnv2to3("8ﾈ"));
//		assertEquals("886", Channel.cnv2to3("8ﾉ"));
//		assertEquals("887", Channel.cnv2to3("8ﾊ"));
//		assertEquals("888", Channel.cnv2to3("8ﾋ"));
//		assertEquals("889", Channel.cnv2to3("8ﾌ"));
//		assertEquals("890", Channel.cnv2to3("8ﾍ"));
//		assertEquals("891", Channel.cnv2to3("8ﾎ"));
//		assertEquals("892", Channel.cnv2to3("8ﾏ"));
//		assertEquals("893", Channel.cnv2to3("8ﾐ"));
//		assertEquals("894", Channel.cnv2to3("8ﾑ"));
//		assertEquals("895", Channel.cnv2to3("8ﾒ"));
//		assertEquals("896", Channel.cnv2to3("8ﾓ"));
//		assertEquals("897", Channel.cnv2to3("8ﾔ"));
//		assertEquals("898", Channel.cnv2to3("8ﾕ"));
//		assertEquals("899", Channel.cnv2to3("8ﾖ"));
//		assertEquals("900", Channel.cnv2to3("90"));
//		assertEquals("901", Channel.cnv2to3("91"));
//		assertEquals("902", Channel.cnv2to3("92"));
//		assertEquals("903", Channel.cnv2to3("93"));
//		assertEquals("904", Channel.cnv2to3("94"));
//		assertEquals("905", Channel.cnv2to3("95"));
//		assertEquals("906", Channel.cnv2to3("96"));
//		assertEquals("907", Channel.cnv2to3("97"));
//		assertEquals("908", Channel.cnv2to3("98"));
//		assertEquals("909", Channel.cnv2to3("99"));
//		assertEquals("910", Channel.cnv2to3("9A"));
//		assertEquals("911", Channel.cnv2to3("9B"));
//		assertEquals("912", Channel.cnv2to3("9C"));
//		assertEquals("913", Channel.cnv2to3("9D"));
//		assertEquals("914", Channel.cnv2to3("9E"));
//		assertEquals("915", Channel.cnv2to3("9F"));
//		assertEquals("916", Channel.cnv2to3("9G"));
//		assertEquals("917", Channel.cnv2to3("9H"));
//		assertEquals("918", Channel.cnv2to3("9I"));
//		assertEquals("919", Channel.cnv2to3("9J"));
//		assertEquals("920", Channel.cnv2to3("9K"));
//		assertEquals("921", Channel.cnv2to3("9L"));
//		assertEquals("922", Channel.cnv2to3("9M"));
//		assertEquals("923", Channel.cnv2to3("9N"));
//		assertEquals("924", Channel.cnv2to3("9O"));
//		assertEquals("925", Channel.cnv2to3("9P"));
//		assertEquals("926", Channel.cnv2to3("9Q"));
//		assertEquals("927", Channel.cnv2to3("9R"));
//		assertEquals("928", Channel.cnv2to3("9S"));
//		assertEquals("929", Channel.cnv2to3("9T"));
//		assertEquals("930", Channel.cnv2to3("9U"));
//		assertEquals("931", Channel.cnv2to3("9V"));
//		assertEquals("932", Channel.cnv2to3("9W"));
//		assertEquals("933", Channel.cnv2to3("9X"));
//		assertEquals("934", Channel.cnv2to3("9Y"));
//		assertEquals("935", Channel.cnv2to3("9Z"));
//		assertEquals("936", Channel.cnv2to3("9a"));
//		assertEquals("937", Channel.cnv2to3("9b"));
//		assertEquals("938", Channel.cnv2to3("9c"));
//		assertEquals("939", Channel.cnv2to3("9d"));
//		assertEquals("940", Channel.cnv2to3("9e"));
//		assertEquals("941", Channel.cnv2to3("9f"));
//		assertEquals("942", Channel.cnv2to3("9g"));
//		assertEquals("943", Channel.cnv2to3("9h"));
//		assertEquals("944", Channel.cnv2to3("9i"));
//		assertEquals("945", Channel.cnv2to3("9j"));
//		assertEquals("946", Channel.cnv2to3("9k"));
//		assertEquals("947", Channel.cnv2to3("9l"));
//		assertEquals("948", Channel.cnv2to3("9m"));
//		assertEquals("949", Channel.cnv2to3("9n"));
//		assertEquals("950", Channel.cnv2to3("9o"));
//		assertEquals("951", Channel.cnv2to3("9p"));
//		assertEquals("952", Channel.cnv2to3("9q"));
//		assertEquals("953", Channel.cnv2to3("9r"));
//		assertEquals("954", Channel.cnv2to3("9s"));
//		assertEquals("955", Channel.cnv2to3("9t"));
//		assertEquals("956", Channel.cnv2to3("9u"));
//		assertEquals("957", Channel.cnv2to3("9v"));
//		assertEquals("958", Channel.cnv2to3("9w"));
//		assertEquals("959", Channel.cnv2to3("9x"));
//		assertEquals("960", Channel.cnv2to3("9y"));
//		assertEquals("961", Channel.cnv2to3("9z"));
//		assertEquals("962", Channel.cnv2to3("9ｱ"));
//		assertEquals("963", Channel.cnv2to3("9ｲ"));
//		assertEquals("964", Channel.cnv2to3("9ｳ"));
//		assertEquals("965", Channel.cnv2to3("9ｴ"));
//		assertEquals("966", Channel.cnv2to3("9ｵ"));
//		assertEquals("967", Channel.cnv2to3("9ｶ"));
//		assertEquals("968", Channel.cnv2to3("9ｷ"));
//		assertEquals("969", Channel.cnv2to3("9ｸ"));
//		assertEquals("970", Channel.cnv2to3("9ｹ"));
//		assertEquals("971", Channel.cnv2to3("9ｺ"));
//		assertEquals("972", Channel.cnv2to3("9ｻ"));
//		assertEquals("973", Channel.cnv2to3("9ｼ"));
//		assertEquals("974", Channel.cnv2to3("9ｽ"));
//		assertEquals("975", Channel.cnv2to3("9ｾ"));
//		assertEquals("976", Channel.cnv2to3("9ｿ"));
//		assertEquals("977", Channel.cnv2to3("9ﾀ"));
//		assertEquals("978", Channel.cnv2to3("9ﾁ"));
//		assertEquals("979", Channel.cnv2to3("9ﾂ"));
//		assertEquals("980", Channel.cnv2to3("9ﾃ"));
//		assertEquals("981", Channel.cnv2to3("9ﾄ"));
//		assertEquals("982", Channel.cnv2to3("9ﾅ"));
//		assertEquals("983", Channel.cnv2to3("9ﾆ"));
//		assertEquals("984", Channel.cnv2to3("9ﾇ"));
//		assertEquals("985", Channel.cnv2to3("9ﾈ"));
//		assertEquals("986", Channel.cnv2to3("9ﾉ"));
//		assertEquals("987", Channel.cnv2to3("9ﾊ"));
//		assertEquals("988", Channel.cnv2to3("9ﾋ"));
//		assertEquals("989", Channel.cnv2to3("9ﾌ"));
//		assertEquals("990", Channel.cnv2to3("9ﾍ"));
//		assertEquals("991", Channel.cnv2to3("9ﾎ"));
//		assertEquals("992", Channel.cnv2to3("9ﾏ"));
//		assertEquals("993", Channel.cnv2to3("9ﾐ"));
//		assertEquals("994", Channel.cnv2to3("9ﾑ"));
//		assertEquals("995", Channel.cnv2to3("9ﾒ"));
//		assertEquals("996", Channel.cnv2to3("9ﾓ"));
//		assertEquals("997", Channel.cnv2to3("9ﾔ"));
//		assertEquals("998", Channel.cnv2to3("9ﾕ"));
//		assertEquals("999", Channel.cnv2to3("9ﾖ"));
	}

	@Test
	public void testChannelCnv3to2() {
		assertEquals("10", Channel.cnv3to2("100"));  //ＩＹ系　　@
		assertEquals("11", Channel.cnv3to2("101"));  //いなげや　@
		assertEquals("12", Channel.cnv3to2("102"));  //サミット　@
		assertEquals("13", Channel.cnv3to2("103"));  //西友系　　@
		assertEquals("14", Channel.cnv3to2("104"));  //ダイエー系@
		assertEquals("15", Channel.cnv3to2("105"));  //東急ストア@
		assertEquals("20", Channel.cnv3to2("200"));  //Ｓイレブン@
		assertEquals("21", Channel.cnv3to2("201"));  //Ｆマート　@
		assertEquals("22", Channel.cnv3to2("202"));  //ローソン　@
		assertEquals("23", Channel.cnv3to2("203"));  //Ｄヤマザキ@
		assertEquals("24", Channel.cnv3to2("204"));  //Ｍストップ@
		assertEquals("26", Channel.cnv3to2("206"));  //サンクス　@
		assertEquals("31", Channel.cnv3to2("301"));  //酒販店　　@
		assertEquals("32", Channel.cnv3to2("302"));  //書籍店　　@
		assertEquals("34", Channel.cnv3to2("304"));  //精肉店　　@
		assertEquals("37", Channel.cnv3to2("307"));  //パン菓子店@
		assertEquals("40", Channel.cnv3to2("400"));  //百貨店　　@
		assertEquals("50", Channel.cnv3to2("500"));  //他薬粧店　@
		assertEquals("61", Channel.cnv3to2("601"));  //Ｏ・Ｓ内自@
		assertEquals("62", Channel.cnv3to2("602"));  //駅構内自販@
		assertEquals("63", Channel.cnv3to2("603"));  //ＳＰ施設自@
		assertEquals("71", Channel.cnv3to2("701"));  //他ＮＴ通販@
		assertEquals("72", Channel.cnv3to2("702"));  //楽天市場　@
		assertEquals("73", Channel.cnv3to2("703"));  //アマゾン　@
		assertEquals("74", Channel.cnv3to2("704"));  //Ｙａｈｏｏ@
		assertEquals("75", Channel.cnv3to2("705"));  //７ネット　@
		assertEquals("76", Channel.cnv3to2("706"));  //イオンＮＳ@
		assertEquals("77", Channel.cnv3to2("707"));  //ＩＹＮＳＰ@
		assertEquals("78", Channel.cnv3to2("708"));  //他ＮＳＰ　@
		assertEquals("80", Channel.cnv3to2("800"));  //訪販・宅配@
		assertEquals("90", Channel.cnv3to2("900"));  //生協店舗　@
		assertEquals("91", Channel.cnv3to2("901"));  //生協共同　@
		assertEquals("92", Channel.cnv3to2("902"));  //他ＨＣ　　@
		assertEquals("93", Channel.cnv3to2("903"));  //他ＤＳ　　@
		assertEquals("94", Channel.cnv3to2("904"));  //家電量販店@
		assertEquals("95", Channel.cnv3to2("905"));  //駅売店　　@
		assertEquals("1A", Channel.cnv3to2("110"));  //イオン系　@
		assertEquals("1C", Channel.cnv3to2("112"));  //ピーコック@
		assertEquals("1E", Channel.cnv3to2("114"));  //ユニー系　@
		assertEquals("1G", Channel.cnv3to2("116"));  //マルエツ系@
		assertEquals("1H", Channel.cnv3to2("117"));  //ライフ　　@
		assertEquals("1J", Channel.cnv3to2("119"));  //イズミヤ　@
		assertEquals("1K", Channel.cnv3to2("120"));  //万代　　　@
		assertEquals("1L", Channel.cnv3to2("121"));  //関西ＳＰ　@
		assertEquals("1M", Channel.cnv3to2("122"));  //成城石井　@
		assertEquals("1N", Channel.cnv3to2("123"));  //つるかめ　@
		assertEquals("1O", Channel.cnv3to2("124"));  //ＯＫストア@
		assertEquals("1P", Channel.cnv3to2("125"));  //ヤオコー　@
		assertEquals("1Q", Channel.cnv3to2("126"));  //ＯＲピック@
		assertEquals("1R", Channel.cnv3to2("127"));  //業務ＳＰ　@
		assertEquals("1S", Channel.cnv3to2("128"));  //オオゼキ　@
		assertEquals("1T", Channel.cnv3to2("129"));  //ベイシア系@
		assertEquals("1U", Channel.cnv3to2("130"));  //平和堂系　@
		assertEquals("1V", Channel.cnv3to2("131"));  //オークワ　@
		assertEquals("1W", Channel.cnv3to2("132"));  //サンディ　@
		assertEquals("1X", Channel.cnv3to2("133"));  //ＳＰ玉出　@
		assertEquals("1Y", Channel.cnv3to2("134"));  //阪急ＯＡＳ@
		assertEquals("1Z", Channel.cnv3to2("135"));  //アークス系@
		assertEquals("1ｱ", Channel.cnv3to2("136"));  //ホクレン　@
		assertEquals("1ｲ", Channel.cnv3to2("137"));  //生鮮市場Ｇ@
		assertEquals("1ｳ", Channel.cnv3to2("138"));  //ＬＵＣＫＹ@
		assertEquals("1ｴ", Channel.cnv3to2("139"));  //バロー　　@
		assertEquals("1ｵ", Channel.cnv3to2("140"));  //ヤマナカ系@
		assertEquals("1ｶ", Channel.cnv3to2("141"));  //ＦＥＥＬ　@
		assertEquals("1ｷ", Channel.cnv3to2("142"));  //イズミ系　@
		assertEquals("1ｸ", Channel.cnv3to2("143"));  //マルキョウ@
		assertEquals("1ｹ", Channel.cnv3to2("144"));  //サンリブ　@
		assertEquals("1ｺ", Channel.cnv3to2("145"));  //ハローデイ@
		assertEquals("1ｻ", Channel.cnv3to2("146"));  //西鉄ストア@
		assertEquals("1ｼ", Channel.cnv3to2("147"));  //タイヨー　@
		assertEquals("1ｽ", Channel.cnv3to2("148"));  //ＳＰ大栄　@
		assertEquals("2A", Channel.cnv3to2("210"));  //ａｍｐｍ　@
		assertEquals("2E", Channel.cnv3to2("214"));  //サークルＫ@
		assertEquals("2F", Channel.cnv3to2("215"));  //ポプラ　　@
		assertEquals("2J", Channel.cnv3to2("219"));  //Ｓマート　@
		assertEquals("2K", Channel.cnv3to2("220"));  //Ｃキオスク@
		assertEquals("2L", Channel.cnv3to2("221"));  //ベルマート@
		assertEquals("2M", Channel.cnv3to2("222"));  //ココストア@
		assertEquals("2N", Channel.cnv3to2("223"));  //エブリワン@
		assertEquals("2O", Channel.cnv3to2("224"));  //生活列車　@
		assertEquals("3A", Channel.cnv3to2("310"));  //たばこ屋　@
		assertEquals("3B", Channel.cnv3to2("311"));  //ペットＳＨ@
		assertEquals("3C", Channel.cnv3to2("312"));  //ＣＤＳＨＰ@
		assertEquals("5A", Channel.cnv3to2("510"));  //マツキヨ　@
		assertEquals("5B", Channel.cnv3to2("511"));  //キリン堂　@
		assertEquals("5C", Channel.cnv3to2("512"));  //スギ薬局　@
		assertEquals("5D", Channel.cnv3to2("513"));  //ツルハＤＲ@
		assertEquals("5E", Channel.cnv3to2("514"));  //カワチ薬局@
		assertEquals("5F", Channel.cnv3to2("515"));  //サンＤＲＧ@
		assertEquals("5G", Channel.cnv3to2("516"));  //ハックＤＲ@
		assertEquals("5H", Channel.cnv3to2("517"));  //クリエイト@
		assertEquals("5I", Channel.cnv3to2("518"));  //ジップＤＲ@
		assertEquals("5J", Channel.cnv3to2("519"));  //ライフォト@
		assertEquals("5K", Channel.cnv3to2("520"));  //ウェルシア@
		assertEquals("5L", Channel.cnv3to2("521"));  //セガミ　　@
		assertEquals("5M", Channel.cnv3to2("522"));  //セイジョー@
		assertEquals("5N", Channel.cnv3to2("523"));  //ダイコクＤ@
		assertEquals("5O", Channel.cnv3to2("524"));  //コクミン　@
		assertEquals("5P", Channel.cnv3to2("525"));  //ＳＥＩＭＳ@
		assertEquals("5Q", Channel.cnv3to2("526"));  //福太郎　　@
		assertEquals("5R", Channel.cnv3to2("527"));  //ぱぱす　　@
		assertEquals("5S", Channel.cnv3to2("528"));  //ダックス　@
		assertEquals("5T", Channel.cnv3to2("529"));  //ＤＲユタカ@
		assertEquals("5U", Channel.cnv3to2("530"));  //サッポロＤ@
		assertEquals("5V", Channel.cnv3to2("531"));  //アインズ系@
		assertEquals("5W", Channel.cnv3to2("532"));  //Ｄスギヤマ@
		assertEquals("5X", Channel.cnv3to2("533"));  //Ｖドラッグ@
		assertEquals("5Y", Channel.cnv3to2("534"));  //杏林堂ＤＲ@
		assertEquals("5Z", Channel.cnv3to2("535"));  //ウインダー@
		assertEquals("5ｱ", Channel.cnv3to2("536"));  //Ｄコスモス@
		assertEquals("5ｲ", Channel.cnv3to2("537"));  //ＤＲ１１　@
		assertEquals("5ｳ", Channel.cnv3to2("538"));  //ＤＲモリ　@
		assertEquals("5ｴ", Channel.cnv3to2("539"));  //ミドリ薬品@
		assertEquals("9A", Channel.cnv3to2("910"));  //他種類ＤＳ@
		assertEquals("9B", Channel.cnv3to2("911"));  //子供専門店@
		assertEquals("9C", Channel.cnv3to2("912"));  //生協個配　@
		assertEquals("9D", Channel.cnv3to2("913"));  //学校会社店@
		assertEquals("9E", Channel.cnv3to2("914"));  //職域販売　@
		assertEquals("9F", Channel.cnv3to2("915"));  //ＳＰ施設店@
		assertEquals("9G", Channel.cnv3to2("916"));  //コーナン　@
		assertEquals("9H", Channel.cnv3to2("917"));  //ケーヨー　@
		assertEquals("9I", Channel.cnv3to2("918"));  //カインズＨ@
		assertEquals("9J", Channel.cnv3to2("919"));  //ドンキ　　@
		assertEquals("9K", Channel.cnv3to2("920"));  //１００円Ｓ@
		assertEquals("9L", Channel.cnv3to2("921"));  //ＤマインＭ@
		assertEquals("9M", Channel.cnv3to2("922"));  //カクヤス　@
		assertEquals("9N", Channel.cnv3to2("923"));  //河内屋　　@
		assertEquals("9O", Channel.cnv3to2("924"));  //やまや　　@
		assertEquals("9P", Channel.cnv3to2("925"));  //東急ハンズ@
		assertEquals("9Q", Channel.cnv3to2("926"));  //島忠系　　@
		assertEquals("9R", Channel.cnv3to2("927"));  //ビバホーム@
		assertEquals("9S", Channel.cnv3to2("928"));  //ＪＦ本田　@
		assertEquals("9T", Channel.cnv3to2("929"));  //ダイキ　　@
		assertEquals("9U", Channel.cnv3to2("930"));  //ロイヤルＨ@
		assertEquals("9V", Channel.cnv3to2("931"));  //コメリ　　@
		assertEquals("9W", Channel.cnv3to2("932"));  //ホーマック@
		assertEquals("9X", Channel.cnv3to2("933"));  //ＪＦＡＫ　@
		assertEquals("9Y", Channel.cnv3to2("934"));  //カーマ　　@
		assertEquals("9Z", Channel.cnv3to2("935"));  //Ｊエンチョ@
		assertEquals("9ｱ", Channel.cnv3to2("936"));  //ＨＣバロー@
		assertEquals("9ｲ", Channel.cnv3to2("937"));  //ＨＰナフコ@
		assertEquals("9ｳ", Channel.cnv3to2("938"));  //ハンズマン@
		assertEquals("9ｴ", Channel.cnv3to2("939"));  //グッディ　@
		assertEquals("9ｵ", Channel.cnv3to2("940"));  //ニシムタ　@

//		assertEquals("00", Channel.cnv3to2("000"));
//		assertEquals("01", Channel.cnv3to2("001"));
//		assertEquals("02", Channel.cnv3to2("002"));
//		assertEquals("03", Channel.cnv3to2("003"));
//		assertEquals("04", Channel.cnv3to2("004"));
//		assertEquals("05", Channel.cnv3to2("005"));
//		assertEquals("06", Channel.cnv3to2("006"));
//		assertEquals("07", Channel.cnv3to2("007"));
//		assertEquals("08", Channel.cnv3to2("008"));
//		assertEquals("09", Channel.cnv3to2("009"));
//		assertEquals("0A", Channel.cnv3to2("010"));
//		assertEquals("0B", Channel.cnv3to2("011"));
//		assertEquals("0C", Channel.cnv3to2("012"));
//		assertEquals("0D", Channel.cnv3to2("013"));
//		assertEquals("0E", Channel.cnv3to2("014"));
//		assertEquals("0F", Channel.cnv3to2("015"));
//		assertEquals("0G", Channel.cnv3to2("016"));
//		assertEquals("0H", Channel.cnv3to2("017"));
//		assertEquals("0I", Channel.cnv3to2("018"));
//		assertEquals("0J", Channel.cnv3to2("019"));
//		assertEquals("0K", Channel.cnv3to2("020"));
//		assertEquals("0L", Channel.cnv3to2("021"));
//		assertEquals("0M", Channel.cnv3to2("022"));
//		assertEquals("0N", Channel.cnv3to2("023"));
//		assertEquals("0O", Channel.cnv3to2("024"));
//		assertEquals("0P", Channel.cnv3to2("025"));
//		assertEquals("0Q", Channel.cnv3to2("026"));
//		assertEquals("0R", Channel.cnv3to2("027"));
//		assertEquals("0S", Channel.cnv3to2("028"));
//		assertEquals("0T", Channel.cnv3to2("029"));
//		assertEquals("0U", Channel.cnv3to2("030"));
//		assertEquals("0V", Channel.cnv3to2("031"));
//		assertEquals("0W", Channel.cnv3to2("032"));
//		assertEquals("0X", Channel.cnv3to2("033"));
//		assertEquals("0Y", Channel.cnv3to2("034"));
//		assertEquals("0Z", Channel.cnv3to2("035"));
//		assertEquals("0a", Channel.cnv3to2("036"));
//		assertEquals("0b", Channel.cnv3to2("037"));
//		assertEquals("0c", Channel.cnv3to2("038"));
//		assertEquals("0d", Channel.cnv3to2("039"));
//		assertEquals("0e", Channel.cnv3to2("040"));
//		assertEquals("0f", Channel.cnv3to2("041"));
//		assertEquals("0g", Channel.cnv3to2("042"));
//		assertEquals("0h", Channel.cnv3to2("043"));
//		assertEquals("0i", Channel.cnv3to2("044"));
//		assertEquals("0j", Channel.cnv3to2("045"));
//		assertEquals("0k", Channel.cnv3to2("046"));
//		assertEquals("0l", Channel.cnv3to2("047"));
//		assertEquals("0m", Channel.cnv3to2("048"));
//		assertEquals("0n", Channel.cnv3to2("049"));
//		assertEquals("0o", Channel.cnv3to2("050"));
//		assertEquals("0p", Channel.cnv3to2("051"));
//		assertEquals("0q", Channel.cnv3to2("052"));
//		assertEquals("0r", Channel.cnv3to2("053"));
//		assertEquals("0s", Channel.cnv3to2("054"));
//		assertEquals("0t", Channel.cnv3to2("055"));
//		assertEquals("0u", Channel.cnv3to2("056"));
//		assertEquals("0v", Channel.cnv3to2("057"));
//		assertEquals("0w", Channel.cnv3to2("058"));
//		assertEquals("0x", Channel.cnv3to2("059"));
//		assertEquals("0y", Channel.cnv3to2("060"));
//		assertEquals("0z", Channel.cnv3to2("061"));
//		assertEquals("0ｱ", Channel.cnv3to2("062"));
//		assertEquals("0ｲ", Channel.cnv3to2("063"));
//		assertEquals("0ｳ", Channel.cnv3to2("064"));
//		assertEquals("0ｴ", Channel.cnv3to2("065"));
//		assertEquals("0ｵ", Channel.cnv3to2("066"));
//		assertEquals("0ｶ", Channel.cnv3to2("067"));
//		assertEquals("0ｷ", Channel.cnv3to2("068"));
//		assertEquals("0ｸ", Channel.cnv3to2("069"));
//		assertEquals("0ｹ", Channel.cnv3to2("070"));
//		assertEquals("0ｺ", Channel.cnv3to2("071"));
//		assertEquals("0ｻ", Channel.cnv3to2("072"));
//		assertEquals("0ｼ", Channel.cnv3to2("073"));
//		assertEquals("0ｽ", Channel.cnv3to2("074"));
//		assertEquals("0ｾ", Channel.cnv3to2("075"));
//		assertEquals("0ｿ", Channel.cnv3to2("076"));
//		assertEquals("0ﾀ", Channel.cnv3to2("077"));
//		assertEquals("0ﾁ", Channel.cnv3to2("078"));
//		assertEquals("0ﾂ", Channel.cnv3to2("079"));
//		assertEquals("0ﾃ", Channel.cnv3to2("080"));
//		assertEquals("0ﾄ", Channel.cnv3to2("081"));
//		assertEquals("0ﾅ", Channel.cnv3to2("082"));
//		assertEquals("0ﾆ", Channel.cnv3to2("083"));
//		assertEquals("0ﾇ", Channel.cnv3to2("084"));
//		assertEquals("0ﾈ", Channel.cnv3to2("085"));
//		assertEquals("0ﾉ", Channel.cnv3to2("086"));
//		assertEquals("0ﾊ", Channel.cnv3to2("087"));
//		assertEquals("0ﾋ", Channel.cnv3to2("088"));
//		assertEquals("0ﾌ", Channel.cnv3to2("089"));
//		assertEquals("0ﾍ", Channel.cnv3to2("090"));
//		assertEquals("0ﾎ", Channel.cnv3to2("091"));
//		assertEquals("0ﾏ", Channel.cnv3to2("092"));
//		assertEquals("0ﾐ", Channel.cnv3to2("093"));
//		assertEquals("0ﾑ", Channel.cnv3to2("094"));
//		assertEquals("0ﾒ", Channel.cnv3to2("095"));
//		assertEquals("0ﾓ", Channel.cnv3to2("096"));
//		assertEquals("0ﾔ", Channel.cnv3to2("097"));
//		assertEquals("0ﾕ", Channel.cnv3to2("098"));
//		assertEquals("0ﾖ", Channel.cnv3to2("099"));
//		assertEquals("10", Channel.cnv3to2("100"));
//		assertEquals("11", Channel.cnv3to2("101"));
//		assertEquals("12", Channel.cnv3to2("102"));
//		assertEquals("13", Channel.cnv3to2("103"));
//		assertEquals("14", Channel.cnv3to2("104"));
//		assertEquals("15", Channel.cnv3to2("105"));
//		assertEquals("16", Channel.cnv3to2("106"));
//		assertEquals("17", Channel.cnv3to2("107"));
//		assertEquals("18", Channel.cnv3to2("108"));
//		assertEquals("19", Channel.cnv3to2("109"));
//		assertEquals("1A", Channel.cnv3to2("110"));
//		assertEquals("1B", Channel.cnv3to2("111"));
//		assertEquals("1C", Channel.cnv3to2("112"));
//		assertEquals("1D", Channel.cnv3to2("113"));
//		assertEquals("1E", Channel.cnv3to2("114"));
//		assertEquals("1F", Channel.cnv3to2("115"));
//		assertEquals("1G", Channel.cnv3to2("116"));
//		assertEquals("1H", Channel.cnv3to2("117"));
//		assertEquals("1I", Channel.cnv3to2("118"));
//		assertEquals("1J", Channel.cnv3to2("119"));
//		assertEquals("1K", Channel.cnv3to2("120"));
//		assertEquals("1L", Channel.cnv3to2("121"));
//		assertEquals("1M", Channel.cnv3to2("122"));
//		assertEquals("1N", Channel.cnv3to2("123"));
//		assertEquals("1O", Channel.cnv3to2("124"));
//		assertEquals("1P", Channel.cnv3to2("125"));
//		assertEquals("1Q", Channel.cnv3to2("126"));
//		assertEquals("1R", Channel.cnv3to2("127"));
//		assertEquals("1S", Channel.cnv3to2("128"));
//		assertEquals("1T", Channel.cnv3to2("129"));
//		assertEquals("1U", Channel.cnv3to2("130"));
//		assertEquals("1V", Channel.cnv3to2("131"));
//		assertEquals("1W", Channel.cnv3to2("132"));
//		assertEquals("1X", Channel.cnv3to2("133"));
//		assertEquals("1Y", Channel.cnv3to2("134"));
//		assertEquals("1Z", Channel.cnv3to2("135"));
//		assertEquals("1a", Channel.cnv3to2("136"));
//		assertEquals("1b", Channel.cnv3to2("137"));
//		assertEquals("1c", Channel.cnv3to2("138"));
//		assertEquals("1d", Channel.cnv3to2("139"));
//		assertEquals("1e", Channel.cnv3to2("140"));
//		assertEquals("1f", Channel.cnv3to2("141"));
//		assertEquals("1g", Channel.cnv3to2("142"));
//		assertEquals("1h", Channel.cnv3to2("143"));
//		assertEquals("1i", Channel.cnv3to2("144"));
//		assertEquals("1j", Channel.cnv3to2("145"));
//		assertEquals("1k", Channel.cnv3to2("146"));
//		assertEquals("1l", Channel.cnv3to2("147"));
//		assertEquals("1m", Channel.cnv3to2("148"));
//		assertEquals("1n", Channel.cnv3to2("149"));
//		assertEquals("1o", Channel.cnv3to2("150"));
//		assertEquals("1p", Channel.cnv3to2("151"));
//		assertEquals("1q", Channel.cnv3to2("152"));
//		assertEquals("1r", Channel.cnv3to2("153"));
//		assertEquals("1s", Channel.cnv3to2("154"));
//		assertEquals("1t", Channel.cnv3to2("155"));
//		assertEquals("1u", Channel.cnv3to2("156"));
//		assertEquals("1v", Channel.cnv3to2("157"));
//		assertEquals("1w", Channel.cnv3to2("158"));
//		assertEquals("1x", Channel.cnv3to2("159"));
//		assertEquals("1y", Channel.cnv3to2("160"));
//		assertEquals("1z", Channel.cnv3to2("161"));
//		assertEquals("1ｱ", Channel.cnv3to2("162"));
//		assertEquals("1ｲ", Channel.cnv3to2("163"));
//		assertEquals("1ｳ", Channel.cnv3to2("164"));
//		assertEquals("1ｴ", Channel.cnv3to2("165"));
//		assertEquals("1ｵ", Channel.cnv3to2("166"));
//		assertEquals("1ｶ", Channel.cnv3to2("167"));
//		assertEquals("1ｷ", Channel.cnv3to2("168"));
//		assertEquals("1ｸ", Channel.cnv3to2("169"));
//		assertEquals("1ｹ", Channel.cnv3to2("170"));
//		assertEquals("1ｺ", Channel.cnv3to2("171"));
//		assertEquals("1ｻ", Channel.cnv3to2("172"));
//		assertEquals("1ｼ", Channel.cnv3to2("173"));
//		assertEquals("1ｽ", Channel.cnv3to2("174"));
//		assertEquals("1ｾ", Channel.cnv3to2("175"));
//		assertEquals("1ｿ", Channel.cnv3to2("176"));
//		assertEquals("1ﾀ", Channel.cnv3to2("177"));
//		assertEquals("1ﾁ", Channel.cnv3to2("178"));
//		assertEquals("1ﾂ", Channel.cnv3to2("179"));
//		assertEquals("1ﾃ", Channel.cnv3to2("180"));
//		assertEquals("1ﾄ", Channel.cnv3to2("181"));
//		assertEquals("1ﾅ", Channel.cnv3to2("182"));
//		assertEquals("1ﾆ", Channel.cnv3to2("183"));
//		assertEquals("1ﾇ", Channel.cnv3to2("184"));
//		assertEquals("1ﾈ", Channel.cnv3to2("185"));
//		assertEquals("1ﾉ", Channel.cnv3to2("186"));
//		assertEquals("1ﾊ", Channel.cnv3to2("187"));
//		assertEquals("1ﾋ", Channel.cnv3to2("188"));
//		assertEquals("1ﾌ", Channel.cnv3to2("189"));
//		assertEquals("1ﾍ", Channel.cnv3to2("190"));
//		assertEquals("1ﾎ", Channel.cnv3to2("191"));
//		assertEquals("1ﾏ", Channel.cnv3to2("192"));
//		assertEquals("1ﾐ", Channel.cnv3to2("193"));
//		assertEquals("1ﾑ", Channel.cnv3to2("194"));
//		assertEquals("1ﾒ", Channel.cnv3to2("195"));
//		assertEquals("1ﾓ", Channel.cnv3to2("196"));
//		assertEquals("1ﾔ", Channel.cnv3to2("197"));
//		assertEquals("1ﾕ", Channel.cnv3to2("198"));
//		assertEquals("1ﾖ", Channel.cnv3to2("199"));
//		assertEquals("20", Channel.cnv3to2("200"));
//		assertEquals("21", Channel.cnv3to2("201"));
//		assertEquals("22", Channel.cnv3to2("202"));
//		assertEquals("23", Channel.cnv3to2("203"));
//		assertEquals("24", Channel.cnv3to2("204"));
//		assertEquals("25", Channel.cnv3to2("205"));
//		assertEquals("26", Channel.cnv3to2("206"));
//		assertEquals("27", Channel.cnv3to2("207"));
//		assertEquals("28", Channel.cnv3to2("208"));
//		assertEquals("29", Channel.cnv3to2("209"));
//		assertEquals("2A", Channel.cnv3to2("210"));
//		assertEquals("2B", Channel.cnv3to2("211"));
//		assertEquals("2C", Channel.cnv3to2("212"));
//		assertEquals("2D", Channel.cnv3to2("213"));
//		assertEquals("2E", Channel.cnv3to2("214"));
//		assertEquals("2F", Channel.cnv3to2("215"));
//		assertEquals("2G", Channel.cnv3to2("216"));
//		assertEquals("2H", Channel.cnv3to2("217"));
//		assertEquals("2I", Channel.cnv3to2("218"));
//		assertEquals("2J", Channel.cnv3to2("219"));
//		assertEquals("2K", Channel.cnv3to2("220"));
//		assertEquals("2L", Channel.cnv3to2("221"));
//		assertEquals("2M", Channel.cnv3to2("222"));
//		assertEquals("2N", Channel.cnv3to2("223"));
//		assertEquals("2O", Channel.cnv3to2("224"));
//		assertEquals("2P", Channel.cnv3to2("225"));
//		assertEquals("2Q", Channel.cnv3to2("226"));
//		assertEquals("2R", Channel.cnv3to2("227"));
//		assertEquals("2S", Channel.cnv3to2("228"));
//		assertEquals("2T", Channel.cnv3to2("229"));
//		assertEquals("2U", Channel.cnv3to2("230"));
//		assertEquals("2V", Channel.cnv3to2("231"));
//		assertEquals("2W", Channel.cnv3to2("232"));
//		assertEquals("2X", Channel.cnv3to2("233"));
//		assertEquals("2Y", Channel.cnv3to2("234"));
//		assertEquals("2Z", Channel.cnv3to2("235"));
//		assertEquals("2a", Channel.cnv3to2("236"));
//		assertEquals("2b", Channel.cnv3to2("237"));
//		assertEquals("2c", Channel.cnv3to2("238"));
//		assertEquals("2d", Channel.cnv3to2("239"));
//		assertEquals("2e", Channel.cnv3to2("240"));
//		assertEquals("2f", Channel.cnv3to2("241"));
//		assertEquals("2g", Channel.cnv3to2("242"));
//		assertEquals("2h", Channel.cnv3to2("243"));
//		assertEquals("2i", Channel.cnv3to2("244"));
//		assertEquals("2j", Channel.cnv3to2("245"));
//		assertEquals("2k", Channel.cnv3to2("246"));
//		assertEquals("2l", Channel.cnv3to2("247"));
//		assertEquals("2m", Channel.cnv3to2("248"));
//		assertEquals("2n", Channel.cnv3to2("249"));
//		assertEquals("2o", Channel.cnv3to2("250"));
//		assertEquals("2p", Channel.cnv3to2("251"));
//		assertEquals("2q", Channel.cnv3to2("252"));
//		assertEquals("2r", Channel.cnv3to2("253"));
//		assertEquals("2s", Channel.cnv3to2("254"));
//		assertEquals("2t", Channel.cnv3to2("255"));
//		assertEquals("2u", Channel.cnv3to2("256"));
//		assertEquals("2v", Channel.cnv3to2("257"));
//		assertEquals("2w", Channel.cnv3to2("258"));
//		assertEquals("2x", Channel.cnv3to2("259"));
//		assertEquals("2y", Channel.cnv3to2("260"));
//		assertEquals("2z", Channel.cnv3to2("261"));
//		assertEquals("2ｱ", Channel.cnv3to2("262"));
//		assertEquals("2ｲ", Channel.cnv3to2("263"));
//		assertEquals("2ｳ", Channel.cnv3to2("264"));
//		assertEquals("2ｴ", Channel.cnv3to2("265"));
//		assertEquals("2ｵ", Channel.cnv3to2("266"));
//		assertEquals("2ｶ", Channel.cnv3to2("267"));
//		assertEquals("2ｷ", Channel.cnv3to2("268"));
//		assertEquals("2ｸ", Channel.cnv3to2("269"));
//		assertEquals("2ｹ", Channel.cnv3to2("270"));
//		assertEquals("2ｺ", Channel.cnv3to2("271"));
//		assertEquals("2ｻ", Channel.cnv3to2("272"));
//		assertEquals("2ｼ", Channel.cnv3to2("273"));
//		assertEquals("2ｽ", Channel.cnv3to2("274"));
//		assertEquals("2ｾ", Channel.cnv3to2("275"));
//		assertEquals("2ｿ", Channel.cnv3to2("276"));
//		assertEquals("2ﾀ", Channel.cnv3to2("277"));
//		assertEquals("2ﾁ", Channel.cnv3to2("278"));
//		assertEquals("2ﾂ", Channel.cnv3to2("279"));
//		assertEquals("2ﾃ", Channel.cnv3to2("280"));
//		assertEquals("2ﾄ", Channel.cnv3to2("281"));
//		assertEquals("2ﾅ", Channel.cnv3to2("282"));
//		assertEquals("2ﾆ", Channel.cnv3to2("283"));
//		assertEquals("2ﾇ", Channel.cnv3to2("284"));
//		assertEquals("2ﾈ", Channel.cnv3to2("285"));
//		assertEquals("2ﾉ", Channel.cnv3to2("286"));
//		assertEquals("2ﾊ", Channel.cnv3to2("287"));
//		assertEquals("2ﾋ", Channel.cnv3to2("288"));
//		assertEquals("2ﾌ", Channel.cnv3to2("289"));
//		assertEquals("2ﾍ", Channel.cnv3to2("290"));
//		assertEquals("2ﾎ", Channel.cnv3to2("291"));
//		assertEquals("2ﾏ", Channel.cnv3to2("292"));
//		assertEquals("2ﾐ", Channel.cnv3to2("293"));
//		assertEquals("2ﾑ", Channel.cnv3to2("294"));
//		assertEquals("2ﾒ", Channel.cnv3to2("295"));
//		assertEquals("2ﾓ", Channel.cnv3to2("296"));
//		assertEquals("2ﾔ", Channel.cnv3to2("297"));
//		assertEquals("2ﾕ", Channel.cnv3to2("298"));
//		assertEquals("2ﾖ", Channel.cnv3to2("299"));
//		assertEquals("30", Channel.cnv3to2("300"));
//		assertEquals("31", Channel.cnv3to2("301"));
//		assertEquals("32", Channel.cnv3to2("302"));
//		assertEquals("33", Channel.cnv3to2("303"));
//		assertEquals("34", Channel.cnv3to2("304"));
//		assertEquals("35", Channel.cnv3to2("305"));
//		assertEquals("36", Channel.cnv3to2("306"));
//		assertEquals("37", Channel.cnv3to2("307"));
//		assertEquals("38", Channel.cnv3to2("308"));
//		assertEquals("39", Channel.cnv3to2("309"));
//		assertEquals("3A", Channel.cnv3to2("310"));
//		assertEquals("3B", Channel.cnv3to2("311"));
//		assertEquals("3C", Channel.cnv3to2("312"));
//		assertEquals("3D", Channel.cnv3to2("313"));
//		assertEquals("3E", Channel.cnv3to2("314"));
//		assertEquals("3F", Channel.cnv3to2("315"));
//		assertEquals("3G", Channel.cnv3to2("316"));
//		assertEquals("3H", Channel.cnv3to2("317"));
//		assertEquals("3I", Channel.cnv3to2("318"));
//		assertEquals("3J", Channel.cnv3to2("319"));
//		assertEquals("3K", Channel.cnv3to2("320"));
//		assertEquals("3L", Channel.cnv3to2("321"));
//		assertEquals("3M", Channel.cnv3to2("322"));
//		assertEquals("3N", Channel.cnv3to2("323"));
//		assertEquals("3O", Channel.cnv3to2("324"));
//		assertEquals("3P", Channel.cnv3to2("325"));
//		assertEquals("3Q", Channel.cnv3to2("326"));
//		assertEquals("3R", Channel.cnv3to2("327"));
//		assertEquals("3S", Channel.cnv3to2("328"));
//		assertEquals("3T", Channel.cnv3to2("329"));
//		assertEquals("3U", Channel.cnv3to2("330"));
//		assertEquals("3V", Channel.cnv3to2("331"));
//		assertEquals("3W", Channel.cnv3to2("332"));
//		assertEquals("3X", Channel.cnv3to2("333"));
//		assertEquals("3Y", Channel.cnv3to2("334"));
//		assertEquals("3Z", Channel.cnv3to2("335"));
//		assertEquals("3a", Channel.cnv3to2("336"));
//		assertEquals("3b", Channel.cnv3to2("337"));
//		assertEquals("3c", Channel.cnv3to2("338"));
//		assertEquals("3d", Channel.cnv3to2("339"));
//		assertEquals("3e", Channel.cnv3to2("340"));
//		assertEquals("3f", Channel.cnv3to2("341"));
//		assertEquals("3g", Channel.cnv3to2("342"));
//		assertEquals("3h", Channel.cnv3to2("343"));
//		assertEquals("3i", Channel.cnv3to2("344"));
//		assertEquals("3j", Channel.cnv3to2("345"));
//		assertEquals("3k", Channel.cnv3to2("346"));
//		assertEquals("3l", Channel.cnv3to2("347"));
//		assertEquals("3m", Channel.cnv3to2("348"));
//		assertEquals("3n", Channel.cnv3to2("349"));
//		assertEquals("3o", Channel.cnv3to2("350"));
//		assertEquals("3p", Channel.cnv3to2("351"));
//		assertEquals("3q", Channel.cnv3to2("352"));
//		assertEquals("3r", Channel.cnv3to2("353"));
//		assertEquals("3s", Channel.cnv3to2("354"));
//		assertEquals("3t", Channel.cnv3to2("355"));
//		assertEquals("3u", Channel.cnv3to2("356"));
//		assertEquals("3v", Channel.cnv3to2("357"));
//		assertEquals("3w", Channel.cnv3to2("358"));
//		assertEquals("3x", Channel.cnv3to2("359"));
//		assertEquals("3y", Channel.cnv3to2("360"));
//		assertEquals("3z", Channel.cnv3to2("361"));
//		assertEquals("3ｱ", Channel.cnv3to2("362"));
//		assertEquals("3ｲ", Channel.cnv3to2("363"));
//		assertEquals("3ｳ", Channel.cnv3to2("364"));
//		assertEquals("3ｴ", Channel.cnv3to2("365"));
//		assertEquals("3ｵ", Channel.cnv3to2("366"));
//		assertEquals("3ｶ", Channel.cnv3to2("367"));
//		assertEquals("3ｷ", Channel.cnv3to2("368"));
//		assertEquals("3ｸ", Channel.cnv3to2("369"));
//		assertEquals("3ｹ", Channel.cnv3to2("370"));
//		assertEquals("3ｺ", Channel.cnv3to2("371"));
//		assertEquals("3ｻ", Channel.cnv3to2("372"));
//		assertEquals("3ｼ", Channel.cnv3to2("373"));
//		assertEquals("3ｽ", Channel.cnv3to2("374"));
//		assertEquals("3ｾ", Channel.cnv3to2("375"));
//		assertEquals("3ｿ", Channel.cnv3to2("376"));
//		assertEquals("3ﾀ", Channel.cnv3to2("377"));
//		assertEquals("3ﾁ", Channel.cnv3to2("378"));
//		assertEquals("3ﾂ", Channel.cnv3to2("379"));
//		assertEquals("3ﾃ", Channel.cnv3to2("380"));
//		assertEquals("3ﾄ", Channel.cnv3to2("381"));
//		assertEquals("3ﾅ", Channel.cnv3to2("382"));
//		assertEquals("3ﾆ", Channel.cnv3to2("383"));
//		assertEquals("3ﾇ", Channel.cnv3to2("384"));
//		assertEquals("3ﾈ", Channel.cnv3to2("385"));
//		assertEquals("3ﾉ", Channel.cnv3to2("386"));
//		assertEquals("3ﾊ", Channel.cnv3to2("387"));
//		assertEquals("3ﾋ", Channel.cnv3to2("388"));
//		assertEquals("3ﾌ", Channel.cnv3to2("389"));
//		assertEquals("3ﾍ", Channel.cnv3to2("390"));
//		assertEquals("3ﾎ", Channel.cnv3to2("391"));
//		assertEquals("3ﾏ", Channel.cnv3to2("392"));
//		assertEquals("3ﾐ", Channel.cnv3to2("393"));
//		assertEquals("3ﾑ", Channel.cnv3to2("394"));
//		assertEquals("3ﾒ", Channel.cnv3to2("395"));
//		assertEquals("3ﾓ", Channel.cnv3to2("396"));
//		assertEquals("3ﾔ", Channel.cnv3to2("397"));
//		assertEquals("3ﾕ", Channel.cnv3to2("398"));
//		assertEquals("3ﾖ", Channel.cnv3to2("399"));
//		assertEquals("40", Channel.cnv3to2("400"));
//		assertEquals("41", Channel.cnv3to2("401"));
//		assertEquals("42", Channel.cnv3to2("402"));
//		assertEquals("43", Channel.cnv3to2("403"));
//		assertEquals("44", Channel.cnv3to2("404"));
//		assertEquals("45", Channel.cnv3to2("405"));
//		assertEquals("46", Channel.cnv3to2("406"));
//		assertEquals("47", Channel.cnv3to2("407"));
//		assertEquals("48", Channel.cnv3to2("408"));
//		assertEquals("49", Channel.cnv3to2("409"));
//		assertEquals("4A", Channel.cnv3to2("410"));
//		assertEquals("4B", Channel.cnv3to2("411"));
//		assertEquals("4C", Channel.cnv3to2("412"));
//		assertEquals("4D", Channel.cnv3to2("413"));
//		assertEquals("4E", Channel.cnv3to2("414"));
//		assertEquals("4F", Channel.cnv3to2("415"));
//		assertEquals("4G", Channel.cnv3to2("416"));
//		assertEquals("4H", Channel.cnv3to2("417"));
//		assertEquals("4I", Channel.cnv3to2("418"));
//		assertEquals("4J", Channel.cnv3to2("419"));
//		assertEquals("4K", Channel.cnv3to2("420"));
//		assertEquals("4L", Channel.cnv3to2("421"));
//		assertEquals("4M", Channel.cnv3to2("422"));
//		assertEquals("4N", Channel.cnv3to2("423"));
//		assertEquals("4O", Channel.cnv3to2("424"));
//		assertEquals("4P", Channel.cnv3to2("425"));
//		assertEquals("4Q", Channel.cnv3to2("426"));
//		assertEquals("4R", Channel.cnv3to2("427"));
//		assertEquals("4S", Channel.cnv3to2("428"));
//		assertEquals("4T", Channel.cnv3to2("429"));
//		assertEquals("4U", Channel.cnv3to2("430"));
//		assertEquals("4V", Channel.cnv3to2("431"));
//		assertEquals("4W", Channel.cnv3to2("432"));
//		assertEquals("4X", Channel.cnv3to2("433"));
//		assertEquals("4Y", Channel.cnv3to2("434"));
//		assertEquals("4Z", Channel.cnv3to2("435"));
//		assertEquals("4a", Channel.cnv3to2("436"));
//		assertEquals("4b", Channel.cnv3to2("437"));
//		assertEquals("4c", Channel.cnv3to2("438"));
//		assertEquals("4d", Channel.cnv3to2("439"));
//		assertEquals("4e", Channel.cnv3to2("440"));
//		assertEquals("4f", Channel.cnv3to2("441"));
//		assertEquals("4g", Channel.cnv3to2("442"));
//		assertEquals("4h", Channel.cnv3to2("443"));
//		assertEquals("4i", Channel.cnv3to2("444"));
//		assertEquals("4j", Channel.cnv3to2("445"));
//		assertEquals("4k", Channel.cnv3to2("446"));
//		assertEquals("4l", Channel.cnv3to2("447"));
//		assertEquals("4m", Channel.cnv3to2("448"));
//		assertEquals("4n", Channel.cnv3to2("449"));
//		assertEquals("4o", Channel.cnv3to2("450"));
//		assertEquals("4p", Channel.cnv3to2("451"));
//		assertEquals("4q", Channel.cnv3to2("452"));
//		assertEquals("4r", Channel.cnv3to2("453"));
//		assertEquals("4s", Channel.cnv3to2("454"));
//		assertEquals("4t", Channel.cnv3to2("455"));
//		assertEquals("4u", Channel.cnv3to2("456"));
//		assertEquals("4v", Channel.cnv3to2("457"));
//		assertEquals("4w", Channel.cnv3to2("458"));
//		assertEquals("4x", Channel.cnv3to2("459"));
//		assertEquals("4y", Channel.cnv3to2("460"));
//		assertEquals("4z", Channel.cnv3to2("461"));
//		assertEquals("4ｱ", Channel.cnv3to2("462"));
//		assertEquals("4ｲ", Channel.cnv3to2("463"));
//		assertEquals("4ｳ", Channel.cnv3to2("464"));
//		assertEquals("4ｴ", Channel.cnv3to2("465"));
//		assertEquals("4ｵ", Channel.cnv3to2("466"));
//		assertEquals("4ｶ", Channel.cnv3to2("467"));
//		assertEquals("4ｷ", Channel.cnv3to2("468"));
//		assertEquals("4ｸ", Channel.cnv3to2("469"));
//		assertEquals("4ｹ", Channel.cnv3to2("470"));
//		assertEquals("4ｺ", Channel.cnv3to2("471"));
//		assertEquals("4ｻ", Channel.cnv3to2("472"));
//		assertEquals("4ｼ", Channel.cnv3to2("473"));
//		assertEquals("4ｽ", Channel.cnv3to2("474"));
//		assertEquals("4ｾ", Channel.cnv3to2("475"));
//		assertEquals("4ｿ", Channel.cnv3to2("476"));
//		assertEquals("4ﾀ", Channel.cnv3to2("477"));
//		assertEquals("4ﾁ", Channel.cnv3to2("478"));
//		assertEquals("4ﾂ", Channel.cnv3to2("479"));
//		assertEquals("4ﾃ", Channel.cnv3to2("480"));
//		assertEquals("4ﾄ", Channel.cnv3to2("481"));
//		assertEquals("4ﾅ", Channel.cnv3to2("482"));
//		assertEquals("4ﾆ", Channel.cnv3to2("483"));
//		assertEquals("4ﾇ", Channel.cnv3to2("484"));
//		assertEquals("4ﾈ", Channel.cnv3to2("485"));
//		assertEquals("4ﾉ", Channel.cnv3to2("486"));
//		assertEquals("4ﾊ", Channel.cnv3to2("487"));
//		assertEquals("4ﾋ", Channel.cnv3to2("488"));
//		assertEquals("4ﾌ", Channel.cnv3to2("489"));
//		assertEquals("4ﾍ", Channel.cnv3to2("490"));
//		assertEquals("4ﾎ", Channel.cnv3to2("491"));
//		assertEquals("4ﾏ", Channel.cnv3to2("492"));
//		assertEquals("4ﾐ", Channel.cnv3to2("493"));
//		assertEquals("4ﾑ", Channel.cnv3to2("494"));
//		assertEquals("4ﾒ", Channel.cnv3to2("495"));
//		assertEquals("4ﾓ", Channel.cnv3to2("496"));
//		assertEquals("4ﾔ", Channel.cnv3to2("497"));
//		assertEquals("4ﾕ", Channel.cnv3to2("498"));
//		assertEquals("4ﾖ", Channel.cnv3to2("499"));
//		assertEquals("50", Channel.cnv3to2("500"));
//		assertEquals("51", Channel.cnv3to2("501"));
//		assertEquals("52", Channel.cnv3to2("502"));
//		assertEquals("53", Channel.cnv3to2("503"));
//		assertEquals("54", Channel.cnv3to2("504"));
//		assertEquals("55", Channel.cnv3to2("505"));
//		assertEquals("56", Channel.cnv3to2("506"));
//		assertEquals("57", Channel.cnv3to2("507"));
//		assertEquals("58", Channel.cnv3to2("508"));
//		assertEquals("59", Channel.cnv3to2("509"));
//		assertEquals("5A", Channel.cnv3to2("510"));
//		assertEquals("5B", Channel.cnv3to2("511"));
//		assertEquals("5C", Channel.cnv3to2("512"));
//		assertEquals("5D", Channel.cnv3to2("513"));
//		assertEquals("5E", Channel.cnv3to2("514"));
//		assertEquals("5F", Channel.cnv3to2("515"));
//		assertEquals("5G", Channel.cnv3to2("516"));
//		assertEquals("5H", Channel.cnv3to2("517"));
//		assertEquals("5I", Channel.cnv3to2("518"));
//		assertEquals("5J", Channel.cnv3to2("519"));
//		assertEquals("5K", Channel.cnv3to2("520"));
//		assertEquals("5L", Channel.cnv3to2("521"));
//		assertEquals("5M", Channel.cnv3to2("522"));
//		assertEquals("5N", Channel.cnv3to2("523"));
//		assertEquals("5O", Channel.cnv3to2("524"));
//		assertEquals("5P", Channel.cnv3to2("525"));
//		assertEquals("5Q", Channel.cnv3to2("526"));
//		assertEquals("5R", Channel.cnv3to2("527"));
//		assertEquals("5S", Channel.cnv3to2("528"));
//		assertEquals("5T", Channel.cnv3to2("529"));
//		assertEquals("5U", Channel.cnv3to2("530"));
//		assertEquals("5V", Channel.cnv3to2("531"));
//		assertEquals("5W", Channel.cnv3to2("532"));
//		assertEquals("5X", Channel.cnv3to2("533"));
//		assertEquals("5Y", Channel.cnv3to2("534"));
//		assertEquals("5Z", Channel.cnv3to2("535"));
//		assertEquals("5a", Channel.cnv3to2("536"));
//		assertEquals("5b", Channel.cnv3to2("537"));
//		assertEquals("5c", Channel.cnv3to2("538"));
//		assertEquals("5d", Channel.cnv3to2("539"));
//		assertEquals("5e", Channel.cnv3to2("540"));
//		assertEquals("5f", Channel.cnv3to2("541"));
//		assertEquals("5g", Channel.cnv3to2("542"));
//		assertEquals("5h", Channel.cnv3to2("543"));
//		assertEquals("5i", Channel.cnv3to2("544"));
//		assertEquals("5j", Channel.cnv3to2("545"));
//		assertEquals("5k", Channel.cnv3to2("546"));
//		assertEquals("5l", Channel.cnv3to2("547"));
//		assertEquals("5m", Channel.cnv3to2("548"));
//		assertEquals("5n", Channel.cnv3to2("549"));
//		assertEquals("5o", Channel.cnv3to2("550"));
//		assertEquals("5p", Channel.cnv3to2("551"));
//		assertEquals("5q", Channel.cnv3to2("552"));
//		assertEquals("5r", Channel.cnv3to2("553"));
//		assertEquals("5s", Channel.cnv3to2("554"));
//		assertEquals("5t", Channel.cnv3to2("555"));
//		assertEquals("5u", Channel.cnv3to2("556"));
//		assertEquals("5v", Channel.cnv3to2("557"));
//		assertEquals("5w", Channel.cnv3to2("558"));
//		assertEquals("5x", Channel.cnv3to2("559"));
//		assertEquals("5y", Channel.cnv3to2("560"));
//		assertEquals("5z", Channel.cnv3to2("561"));
//		assertEquals("5ｱ", Channel.cnv3to2("562"));
//		assertEquals("5ｲ", Channel.cnv3to2("563"));
//		assertEquals("5ｳ", Channel.cnv3to2("564"));
//		assertEquals("5ｴ", Channel.cnv3to2("565"));
//		assertEquals("5ｵ", Channel.cnv3to2("566"));
//		assertEquals("5ｶ", Channel.cnv3to2("567"));
//		assertEquals("5ｷ", Channel.cnv3to2("568"));
//		assertEquals("5ｸ", Channel.cnv3to2("569"));
//		assertEquals("5ｹ", Channel.cnv3to2("570"));
//		assertEquals("5ｺ", Channel.cnv3to2("571"));
//		assertEquals("5ｻ", Channel.cnv3to2("572"));
//		assertEquals("5ｼ", Channel.cnv3to2("573"));
//		assertEquals("5ｽ", Channel.cnv3to2("574"));
//		assertEquals("5ｾ", Channel.cnv3to2("575"));
//		assertEquals("5ｿ", Channel.cnv3to2("576"));
//		assertEquals("5ﾀ", Channel.cnv3to2("577"));
//		assertEquals("5ﾁ", Channel.cnv3to2("578"));
//		assertEquals("5ﾂ", Channel.cnv3to2("579"));
//		assertEquals("5ﾃ", Channel.cnv3to2("580"));
//		assertEquals("5ﾄ", Channel.cnv3to2("581"));
//		assertEquals("5ﾅ", Channel.cnv3to2("582"));
//		assertEquals("5ﾆ", Channel.cnv3to2("583"));
//		assertEquals("5ﾇ", Channel.cnv3to2("584"));
//		assertEquals("5ﾈ", Channel.cnv3to2("585"));
//		assertEquals("5ﾉ", Channel.cnv3to2("586"));
//		assertEquals("5ﾊ", Channel.cnv3to2("587"));
//		assertEquals("5ﾋ", Channel.cnv3to2("588"));
//		assertEquals("5ﾌ", Channel.cnv3to2("589"));
//		assertEquals("5ﾍ", Channel.cnv3to2("590"));
//		assertEquals("5ﾎ", Channel.cnv3to2("591"));
//		assertEquals("5ﾏ", Channel.cnv3to2("592"));
//		assertEquals("5ﾐ", Channel.cnv3to2("593"));
//		assertEquals("5ﾑ", Channel.cnv3to2("594"));
//		assertEquals("5ﾒ", Channel.cnv3to2("595"));
//		assertEquals("5ﾓ", Channel.cnv3to2("596"));
//		assertEquals("5ﾔ", Channel.cnv3to2("597"));
//		assertEquals("5ﾕ", Channel.cnv3to2("598"));
//		assertEquals("5ﾖ", Channel.cnv3to2("599"));
//		assertEquals("60", Channel.cnv3to2("600"));
//		assertEquals("61", Channel.cnv3to2("601"));
//		assertEquals("62", Channel.cnv3to2("602"));
//		assertEquals("63", Channel.cnv3to2("603"));
//		assertEquals("64", Channel.cnv3to2("604"));
//		assertEquals("65", Channel.cnv3to2("605"));
//		assertEquals("66", Channel.cnv3to2("606"));
//		assertEquals("67", Channel.cnv3to2("607"));
//		assertEquals("68", Channel.cnv3to2("608"));
//		assertEquals("69", Channel.cnv3to2("609"));
//		assertEquals("6A", Channel.cnv3to2("610"));
//		assertEquals("6B", Channel.cnv3to2("611"));
//		assertEquals("6C", Channel.cnv3to2("612"));
//		assertEquals("6D", Channel.cnv3to2("613"));
//		assertEquals("6E", Channel.cnv3to2("614"));
//		assertEquals("6F", Channel.cnv3to2("615"));
//		assertEquals("6G", Channel.cnv3to2("616"));
//		assertEquals("6H", Channel.cnv3to2("617"));
//		assertEquals("6I", Channel.cnv3to2("618"));
//		assertEquals("6J", Channel.cnv3to2("619"));
//		assertEquals("6K", Channel.cnv3to2("620"));
//		assertEquals("6L", Channel.cnv3to2("621"));
//		assertEquals("6M", Channel.cnv3to2("622"));
//		assertEquals("6N", Channel.cnv3to2("623"));
//		assertEquals("6O", Channel.cnv3to2("624"));
//		assertEquals("6P", Channel.cnv3to2("625"));
//		assertEquals("6Q", Channel.cnv3to2("626"));
//		assertEquals("6R", Channel.cnv3to2("627"));
//		assertEquals("6S", Channel.cnv3to2("628"));
//		assertEquals("6T", Channel.cnv3to2("629"));
//		assertEquals("6U", Channel.cnv3to2("630"));
//		assertEquals("6V", Channel.cnv3to2("631"));
//		assertEquals("6W", Channel.cnv3to2("632"));
//		assertEquals("6X", Channel.cnv3to2("633"));
//		assertEquals("6Y", Channel.cnv3to2("634"));
//		assertEquals("6Z", Channel.cnv3to2("635"));
//		assertEquals("6a", Channel.cnv3to2("636"));
//		assertEquals("6b", Channel.cnv3to2("637"));
//		assertEquals("6c", Channel.cnv3to2("638"));
//		assertEquals("6d", Channel.cnv3to2("639"));
//		assertEquals("6e", Channel.cnv3to2("640"));
//		assertEquals("6f", Channel.cnv3to2("641"));
//		assertEquals("6g", Channel.cnv3to2("642"));
//		assertEquals("6h", Channel.cnv3to2("643"));
//		assertEquals("6i", Channel.cnv3to2("644"));
//		assertEquals("6j", Channel.cnv3to2("645"));
//		assertEquals("6k", Channel.cnv3to2("646"));
//		assertEquals("6l", Channel.cnv3to2("647"));
//		assertEquals("6m", Channel.cnv3to2("648"));
//		assertEquals("6n", Channel.cnv3to2("649"));
//		assertEquals("6o", Channel.cnv3to2("650"));
//		assertEquals("6p", Channel.cnv3to2("651"));
//		assertEquals("6q", Channel.cnv3to2("652"));
//		assertEquals("6r", Channel.cnv3to2("653"));
//		assertEquals("6s", Channel.cnv3to2("654"));
//		assertEquals("6t", Channel.cnv3to2("655"));
//		assertEquals("6u", Channel.cnv3to2("656"));
//		assertEquals("6v", Channel.cnv3to2("657"));
//		assertEquals("6w", Channel.cnv3to2("658"));
//		assertEquals("6x", Channel.cnv3to2("659"));
//		assertEquals("6y", Channel.cnv3to2("660"));
//		assertEquals("6z", Channel.cnv3to2("661"));
//		assertEquals("6ｱ", Channel.cnv3to2("662"));
//		assertEquals("6ｲ", Channel.cnv3to2("663"));
//		assertEquals("6ｳ", Channel.cnv3to2("664"));
//		assertEquals("6ｴ", Channel.cnv3to2("665"));
//		assertEquals("6ｵ", Channel.cnv3to2("666"));
//		assertEquals("6ｶ", Channel.cnv3to2("667"));
//		assertEquals("6ｷ", Channel.cnv3to2("668"));
//		assertEquals("6ｸ", Channel.cnv3to2("669"));
//		assertEquals("6ｹ", Channel.cnv3to2("670"));
//		assertEquals("6ｺ", Channel.cnv3to2("671"));
//		assertEquals("6ｻ", Channel.cnv3to2("672"));
//		assertEquals("6ｼ", Channel.cnv3to2("673"));
//		assertEquals("6ｽ", Channel.cnv3to2("674"));
//		assertEquals("6ｾ", Channel.cnv3to2("675"));
//		assertEquals("6ｿ", Channel.cnv3to2("676"));
//		assertEquals("6ﾀ", Channel.cnv3to2("677"));
//		assertEquals("6ﾁ", Channel.cnv3to2("678"));
//		assertEquals("6ﾂ", Channel.cnv3to2("679"));
//		assertEquals("6ﾃ", Channel.cnv3to2("680"));
//		assertEquals("6ﾄ", Channel.cnv3to2("681"));
//		assertEquals("6ﾅ", Channel.cnv3to2("682"));
//		assertEquals("6ﾆ", Channel.cnv3to2("683"));
//		assertEquals("6ﾇ", Channel.cnv3to2("684"));
//		assertEquals("6ﾈ", Channel.cnv3to2("685"));
//		assertEquals("6ﾉ", Channel.cnv3to2("686"));
//		assertEquals("6ﾊ", Channel.cnv3to2("687"));
//		assertEquals("6ﾋ", Channel.cnv3to2("688"));
//		assertEquals("6ﾌ", Channel.cnv3to2("689"));
//		assertEquals("6ﾍ", Channel.cnv3to2("690"));
//		assertEquals("6ﾎ", Channel.cnv3to2("691"));
//		assertEquals("6ﾏ", Channel.cnv3to2("692"));
//		assertEquals("6ﾐ", Channel.cnv3to2("693"));
//		assertEquals("6ﾑ", Channel.cnv3to2("694"));
//		assertEquals("6ﾒ", Channel.cnv3to2("695"));
//		assertEquals("6ﾓ", Channel.cnv3to2("696"));
//		assertEquals("6ﾔ", Channel.cnv3to2("697"));
//		assertEquals("68", Channel.cnv3to2("698"));
//		assertEquals("69", Channel.cnv3to2("699"));
//		assertEquals("70", Channel.cnv3to2("700"));
//		assertEquals("71", Channel.cnv3to2("701"));
//		assertEquals("72", Channel.cnv3to2("702"));
//		assertEquals("73", Channel.cnv3to2("703"));
//		assertEquals("74", Channel.cnv3to2("704"));
//		assertEquals("75", Channel.cnv3to2("705"));
//		assertEquals("76", Channel.cnv3to2("706"));
//		assertEquals("77", Channel.cnv3to2("707"));
//		assertEquals("78", Channel.cnv3to2("708"));
//		assertEquals("79", Channel.cnv3to2("709"));
//		assertEquals("7A", Channel.cnv3to2("710"));
//		assertEquals("7B", Channel.cnv3to2("711"));
//		assertEquals("7C", Channel.cnv3to2("712"));
//		assertEquals("7D", Channel.cnv3to2("713"));
//		assertEquals("7E", Channel.cnv3to2("714"));
//		assertEquals("7F", Channel.cnv3to2("715"));
//		assertEquals("7G", Channel.cnv3to2("716"));
//		assertEquals("7H", Channel.cnv3to2("717"));
//		assertEquals("7I", Channel.cnv3to2("718"));
//		assertEquals("7J", Channel.cnv3to2("719"));
//		assertEquals("7K", Channel.cnv3to2("720"));
//		assertEquals("7L", Channel.cnv3to2("721"));
//		assertEquals("7M", Channel.cnv3to2("722"));
//		assertEquals("7N", Channel.cnv3to2("723"));
//		assertEquals("7O", Channel.cnv3to2("724"));
//		assertEquals("7P", Channel.cnv3to2("725"));
//		assertEquals("7Q", Channel.cnv3to2("726"));
//		assertEquals("7R", Channel.cnv3to2("727"));
//		assertEquals("7S", Channel.cnv3to2("728"));
//		assertEquals("7T", Channel.cnv3to2("729"));
//		assertEquals("7U", Channel.cnv3to2("730"));
//		assertEquals("7V", Channel.cnv3to2("731"));
//		assertEquals("7W", Channel.cnv3to2("732"));
//		assertEquals("7X", Channel.cnv3to2("733"));
//		assertEquals("7Y", Channel.cnv3to2("734"));
//		assertEquals("7Z", Channel.cnv3to2("735"));
//		assertEquals("7a", Channel.cnv3to2("736"));
//		assertEquals("7b", Channel.cnv3to2("737"));
//		assertEquals("7c", Channel.cnv3to2("738"));
//		assertEquals("7d", Channel.cnv3to2("739"));
//		assertEquals("7e", Channel.cnv3to2("740"));
//		assertEquals("7f", Channel.cnv3to2("741"));
//		assertEquals("7g", Channel.cnv3to2("742"));
//		assertEquals("7h", Channel.cnv3to2("743"));
//		assertEquals("7i", Channel.cnv3to2("744"));
//		assertEquals("7j", Channel.cnv3to2("745"));
//		assertEquals("7k", Channel.cnv3to2("746"));
//		assertEquals("7l", Channel.cnv3to2("747"));
//		assertEquals("7m", Channel.cnv3to2("748"));
//		assertEquals("7n", Channel.cnv3to2("749"));
//		assertEquals("7o", Channel.cnv3to2("750"));
//		assertEquals("7p", Channel.cnv3to2("751"));
//		assertEquals("7q", Channel.cnv3to2("752"));
//		assertEquals("7r", Channel.cnv3to2("753"));
//		assertEquals("7s", Channel.cnv3to2("754"));
//		assertEquals("7t", Channel.cnv3to2("755"));
//		assertEquals("7u", Channel.cnv3to2("756"));
//		assertEquals("7v", Channel.cnv3to2("757"));
//		assertEquals("7w", Channel.cnv3to2("758"));
//		assertEquals("7x", Channel.cnv3to2("759"));
//		assertEquals("7y", Channel.cnv3to2("760"));
//		assertEquals("7z", Channel.cnv3to2("761"));
//		assertEquals("7ｱ", Channel.cnv3to2("762"));
//		assertEquals("7ｲ", Channel.cnv3to2("763"));
//		assertEquals("7ｳ", Channel.cnv3to2("764"));
//		assertEquals("7ｴ", Channel.cnv3to2("765"));
//		assertEquals("7ｵ", Channel.cnv3to2("766"));
//		assertEquals("7ｶ", Channel.cnv3to2("767"));
//		assertEquals("7ｷ", Channel.cnv3to2("768"));
//		assertEquals("7ｸ", Channel.cnv3to2("769"));
//		assertEquals("7ｹ", Channel.cnv3to2("770"));
//		assertEquals("7ｺ", Channel.cnv3to2("771"));
//		assertEquals("7ｻ", Channel.cnv3to2("772"));
//		assertEquals("7ｼ", Channel.cnv3to2("773"));
//		assertEquals("7ｽ", Channel.cnv3to2("774"));
//		assertEquals("7ｾ", Channel.cnv3to2("775"));
//		assertEquals("7ｿ", Channel.cnv3to2("776"));
//		assertEquals("7ﾀ", Channel.cnv3to2("777"));
//		assertEquals("7ﾁ", Channel.cnv3to2("778"));
//		assertEquals("7ﾂ", Channel.cnv3to2("779"));
//		assertEquals("7ﾃ", Channel.cnv3to2("780"));
//		assertEquals("7ﾄ", Channel.cnv3to2("781"));
//		assertEquals("7ﾅ", Channel.cnv3to2("782"));
//		assertEquals("7ﾆ", Channel.cnv3to2("783"));
//		assertEquals("7ﾇ", Channel.cnv3to2("784"));
//		assertEquals("7ﾈ", Channel.cnv3to2("785"));
//		assertEquals("7ﾉ", Channel.cnv3to2("786"));
//		assertEquals("7ﾊ", Channel.cnv3to2("787"));
//		assertEquals("7ﾋ", Channel.cnv3to2("788"));
//		assertEquals("7ﾌ", Channel.cnv3to2("789"));
//		assertEquals("7ﾍ", Channel.cnv3to2("790"));
//		assertEquals("7ﾎ", Channel.cnv3to2("791"));
//		assertEquals("7ﾏ", Channel.cnv3to2("792"));
//		assertEquals("7ﾐ", Channel.cnv3to2("793"));
//		assertEquals("7ﾑ", Channel.cnv3to2("794"));
//		assertEquals("7ﾒ", Channel.cnv3to2("795"));
//		assertEquals("7ﾓ", Channel.cnv3to2("796"));
//		assertEquals("7ﾔ", Channel.cnv3to2("797"));
//		assertEquals("7ﾕ", Channel.cnv3to2("798"));
//		assertEquals("7ﾖ", Channel.cnv3to2("799"));
//		assertEquals("80", Channel.cnv3to2("800"));
//		assertEquals("81", Channel.cnv3to2("801"));
//		assertEquals("82", Channel.cnv3to2("802"));
//		assertEquals("83", Channel.cnv3to2("803"));
//		assertEquals("84", Channel.cnv3to2("804"));
//		assertEquals("85", Channel.cnv3to2("805"));
//		assertEquals("86", Channel.cnv3to2("806"));
//		assertEquals("87", Channel.cnv3to2("807"));
//		assertEquals("88", Channel.cnv3to2("808"));
//		assertEquals("89", Channel.cnv3to2("809"));
//		assertEquals("8A", Channel.cnv3to2("810"));
//		assertEquals("8B", Channel.cnv3to2("811"));
//		assertEquals("8C", Channel.cnv3to2("812"));
//		assertEquals("8D", Channel.cnv3to2("813"));
//		assertEquals("8E", Channel.cnv3to2("814"));
//		assertEquals("8F", Channel.cnv3to2("815"));
//		assertEquals("8G", Channel.cnv3to2("816"));
//		assertEquals("8H", Channel.cnv3to2("817"));
//		assertEquals("8I", Channel.cnv3to2("818"));
//		assertEquals("8J", Channel.cnv3to2("819"));
//		assertEquals("8K", Channel.cnv3to2("820"));
//		assertEquals("8L", Channel.cnv3to2("821"));
//		assertEquals("8M", Channel.cnv3to2("822"));
//		assertEquals("8N", Channel.cnv3to2("823"));
//		assertEquals("8O", Channel.cnv3to2("824"));
//		assertEquals("8P", Channel.cnv3to2("825"));
//		assertEquals("8Q", Channel.cnv3to2("826"));
//		assertEquals("8R", Channel.cnv3to2("827"));
//		assertEquals("8S", Channel.cnv3to2("828"));
//		assertEquals("8T", Channel.cnv3to2("829"));
//		assertEquals("8U", Channel.cnv3to2("830"));
//		assertEquals("8V", Channel.cnv3to2("831"));
//		assertEquals("8W", Channel.cnv3to2("832"));
//		assertEquals("8X", Channel.cnv3to2("833"));
//		assertEquals("8Y", Channel.cnv3to2("834"));
//		assertEquals("8Z", Channel.cnv3to2("835"));
//		assertEquals("8a", Channel.cnv3to2("836"));
//		assertEquals("8b", Channel.cnv3to2("837"));
//		assertEquals("8c", Channel.cnv3to2("838"));
//		assertEquals("8d", Channel.cnv3to2("839"));
//		assertEquals("8e", Channel.cnv3to2("840"));
//		assertEquals("8f", Channel.cnv3to2("841"));
//		assertEquals("8g", Channel.cnv3to2("842"));
//		assertEquals("8h", Channel.cnv3to2("843"));
//		assertEquals("8i", Channel.cnv3to2("844"));
//		assertEquals("8j", Channel.cnv3to2("845"));
//		assertEquals("8k", Channel.cnv3to2("846"));
//		assertEquals("8l", Channel.cnv3to2("847"));
//		assertEquals("8m", Channel.cnv3to2("848"));
//		assertEquals("8n", Channel.cnv3to2("849"));
//		assertEquals("8o", Channel.cnv3to2("850"));
//		assertEquals("8p", Channel.cnv3to2("851"));
//		assertEquals("8q", Channel.cnv3to2("852"));
//		assertEquals("8r", Channel.cnv3to2("853"));
//		assertEquals("8s", Channel.cnv3to2("854"));
//		assertEquals("8t", Channel.cnv3to2("855"));
//		assertEquals("8u", Channel.cnv3to2("856"));
//		assertEquals("8v", Channel.cnv3to2("857"));
//		assertEquals("8w", Channel.cnv3to2("858"));
//		assertEquals("8x", Channel.cnv3to2("859"));
//		assertEquals("8y", Channel.cnv3to2("860"));
//		assertEquals("8z", Channel.cnv3to2("861"));
//		assertEquals("8ｱ", Channel.cnv3to2("862"));
//		assertEquals("8ｲ", Channel.cnv3to2("863"));
//		assertEquals("8ｳ", Channel.cnv3to2("864"));
//		assertEquals("8ｴ", Channel.cnv3to2("865"));
//		assertEquals("8ｵ", Channel.cnv3to2("866"));
//		assertEquals("8ｶ", Channel.cnv3to2("867"));
//		assertEquals("8ｷ", Channel.cnv3to2("868"));
//		assertEquals("8ｸ", Channel.cnv3to2("869"));
//		assertEquals("8ｹ", Channel.cnv3to2("870"));
//		assertEquals("8ｺ", Channel.cnv3to2("871"));
//		assertEquals("8ｻ", Channel.cnv3to2("872"));
//		assertEquals("8ｼ", Channel.cnv3to2("873"));
//		assertEquals("8ｽ", Channel.cnv3to2("874"));
//		assertEquals("8ｾ", Channel.cnv3to2("875"));
//		assertEquals("8ｿ", Channel.cnv3to2("876"));
//		assertEquals("8ﾀ", Channel.cnv3to2("877"));
//		assertEquals("8ﾁ", Channel.cnv3to2("878"));
//		assertEquals("8ﾂ", Channel.cnv3to2("879"));
//		assertEquals("8ﾃ", Channel.cnv3to2("880"));
//		assertEquals("8ﾄ", Channel.cnv3to2("881"));
//		assertEquals("8ﾅ", Channel.cnv3to2("882"));
//		assertEquals("8ﾆ", Channel.cnv3to2("883"));
//		assertEquals("8ﾇ", Channel.cnv3to2("884"));
//		assertEquals("8ﾈ", Channel.cnv3to2("885"));
//		assertEquals("8ﾉ", Channel.cnv3to2("886"));
//		assertEquals("8ﾊ", Channel.cnv3to2("887"));
//		assertEquals("8ﾋ", Channel.cnv3to2("888"));
//		assertEquals("8ﾌ", Channel.cnv3to2("889"));
//		assertEquals("8ﾍ", Channel.cnv3to2("890"));
//		assertEquals("8ﾎ", Channel.cnv3to2("891"));
//		assertEquals("8ﾏ", Channel.cnv3to2("892"));
//		assertEquals("8ﾐ", Channel.cnv3to2("893"));
//		assertEquals("8ﾑ", Channel.cnv3to2("894"));
//		assertEquals("8ﾒ", Channel.cnv3to2("895"));
//		assertEquals("8ﾓ", Channel.cnv3to2("896"));
//		assertEquals("8ﾔ", Channel.cnv3to2("897"));
//		assertEquals("8ﾕ", Channel.cnv3to2("898"));
//		assertEquals("8ﾖ", Channel.cnv3to2("899"));
//		assertEquals("90", Channel.cnv3to2("900"));
//		assertEquals("91", Channel.cnv3to2("901"));
//		assertEquals("92", Channel.cnv3to2("902"));
//		assertEquals("93", Channel.cnv3to2("903"));
//		assertEquals("94", Channel.cnv3to2("904"));
//		assertEquals("95", Channel.cnv3to2("905"));
//		assertEquals("96", Channel.cnv3to2("906"));
//		assertEquals("97", Channel.cnv3to2("907"));
//		assertEquals("98", Channel.cnv3to2("908"));
//		assertEquals("99", Channel.cnv3to2("909"));
//		assertEquals("9A", Channel.cnv3to2("910"));
//		assertEquals("9B", Channel.cnv3to2("911"));
//		assertEquals("9C", Channel.cnv3to2("912"));
//		assertEquals("9D", Channel.cnv3to2("913"));
//		assertEquals("9E", Channel.cnv3to2("914"));
//		assertEquals("9F", Channel.cnv3to2("915"));
//		assertEquals("9G", Channel.cnv3to2("916"));
//		assertEquals("9H", Channel.cnv3to2("917"));
//		assertEquals("9I", Channel.cnv3to2("918"));
//		assertEquals("9J", Channel.cnv3to2("919"));
//		assertEquals("9K", Channel.cnv3to2("920"));
//		assertEquals("9L", Channel.cnv3to2("921"));
//		assertEquals("9M", Channel.cnv3to2("922"));
//		assertEquals("9N", Channel.cnv3to2("923"));
//		assertEquals("9O", Channel.cnv3to2("924"));
//		assertEquals("9P", Channel.cnv3to2("925"));
//		assertEquals("9Q", Channel.cnv3to2("926"));
//		assertEquals("9R", Channel.cnv3to2("927"));
//		assertEquals("9S", Channel.cnv3to2("928"));
//		assertEquals("9T", Channel.cnv3to2("929"));
//		assertEquals("9U", Channel.cnv3to2("930"));
//		assertEquals("9V", Channel.cnv3to2("931"));
//		assertEquals("9W", Channel.cnv3to2("932"));
//		assertEquals("9X", Channel.cnv3to2("933"));
//		assertEquals("9Y", Channel.cnv3to2("934"));
//		assertEquals("9Z", Channel.cnv3to2("935"));
//		assertEquals("9a", Channel.cnv3to2("936"));
//		assertEquals("9b", Channel.cnv3to2("937"));
//		assertEquals("9c", Channel.cnv3to2("938"));
//		assertEquals("9d", Channel.cnv3to2("939"));
//		assertEquals("9e", Channel.cnv3to2("940"));
//		assertEquals("9f", Channel.cnv3to2("941"));
//		assertEquals("9g", Channel.cnv3to2("942"));
//		assertEquals("9h", Channel.cnv3to2("943"));
//		assertEquals("9i", Channel.cnv3to2("944"));
//		assertEquals("9j", Channel.cnv3to2("945"));
//		assertEquals("9k", Channel.cnv3to2("946"));
//		assertEquals("9l", Channel.cnv3to2("947"));
//		assertEquals("9m", Channel.cnv3to2("948"));
//		assertEquals("9n", Channel.cnv3to2("949"));
//		assertEquals("9o", Channel.cnv3to2("950"));
//		assertEquals("9p", Channel.cnv3to2("951"));
//		assertEquals("9q", Channel.cnv3to2("952"));
//		assertEquals("9r", Channel.cnv3to2("953"));
//		assertEquals("9s", Channel.cnv3to2("954"));
//		assertEquals("9t", Channel.cnv3to2("955"));
//		assertEquals("9u", Channel.cnv3to2("956"));
//		assertEquals("9v", Channel.cnv3to2("957"));
//		assertEquals("9w", Channel.cnv3to2("958"));
//		assertEquals("9x", Channel.cnv3to2("959"));
//		assertEquals("9y", Channel.cnv3to2("960"));
//		assertEquals("9z", Channel.cnv3to2("961"));
//		assertEquals("9ｱ", Channel.cnv3to2("962"));
//		assertEquals("9ｲ", Channel.cnv3to2("963"));
//		assertEquals("9ｳ", Channel.cnv3to2("964"));
//		assertEquals("9ｴ", Channel.cnv3to2("965"));
//		assertEquals("9ｵ", Channel.cnv3to2("966"));
//		assertEquals("9ｶ", Channel.cnv3to2("967"));
//		assertEquals("9ｷ", Channel.cnv3to2("968"));
//		assertEquals("9ｸ", Channel.cnv3to2("969"));
//		assertEquals("9ｹ", Channel.cnv3to2("970"));
//		assertEquals("9ｺ", Channel.cnv3to2("971"));
//		assertEquals("9ｻ", Channel.cnv3to2("972"));
//		assertEquals("9ｼ", Channel.cnv3to2("973"));
//		assertEquals("9ｽ", Channel.cnv3to2("974"));
//		assertEquals("9ｾ", Channel.cnv3to2("975"));
//		assertEquals("9ｿ", Channel.cnv3to2("976"));
//		assertEquals("9ﾀ", Channel.cnv3to2("977"));
//		assertEquals("9ﾁ", Channel.cnv3to2("978"));
//		assertEquals("9ﾂ", Channel.cnv3to2("979"));
//		assertEquals("9ﾃ", Channel.cnv3to2("980"));
//		assertEquals("9ﾄ", Channel.cnv3to2("981"));
//		assertEquals("9ﾅ", Channel.cnv3to2("982"));
//		assertEquals("9ﾆ", Channel.cnv3to2("983"));
//		assertEquals("9ﾇ", Channel.cnv3to2("984"));
//		assertEquals("9ﾈ", Channel.cnv3to2("985"));
//		assertEquals("9ﾉ", Channel.cnv3to2("986"));
//		assertEquals("9ﾊ", Channel.cnv3to2("987"));
//		assertEquals("9ﾋ", Channel.cnv3to2("988"));
//		assertEquals("9ﾌ", Channel.cnv3to2("989"));
//		assertEquals("9ﾍ", Channel.cnv3to2("990"));
//		assertEquals("9ﾎ", Channel.cnv3to2("991"));
//		assertEquals("9ﾏ", Channel.cnv3to2("992"));
//		assertEquals("9ﾐ", Channel.cnv3to2("993"));
//		assertEquals("9ﾑ", Channel.cnv3to2("994"));
//		assertEquals("9ﾒ", Channel.cnv3to2("995"));
//		assertEquals("9ﾓ", Channel.cnv3to2("996"));
//		assertEquals("9ﾔ", Channel.cnv3to2("997"));
//		assertEquals("9ﾕ", Channel.cnv3to2("998"));
//		assertEquals("9ﾖ", Channel.cnv3to2("999"));

	}

	// ------------------------------------------------------------------------------
	// 購入先コード変換
	// EX 09=>9 10=>A に変換
	// ------------------------------------------------------------------------------
	public static void cnv3to2Test1() {
		String[] array = { "000", "100", "101", "102", "103", "104", "105",
				"106", "107", "108", "109", "110", "111", "112", "113", "114",
				"115", "116", "117", "118", "119", "120", "121", "200", "201",
				"202", "203", "204", "205", "206", "207", "208", "209", "210",
				"211", "212", "213", "214", "215", "216", "217", "218", "219",
				"300", "301", "302", "303", "304", "305", "306", "307", "309",
				"310", "311", "312", "400", "500", "501", "510", "511", "512",
				"513", "514", "515", "516", "517", "518", "519", "520", "521",
				"522", "523", "524", "600", "601", "602", "603", "604", "605",
				"700", "701", "702", "709", "800", "900", "901", "902", "903",
				"904", "905", "906", "907", "909", "910", "911", "912", "913",
				"914", "915", "916", "917", "918", "919", "920", "921", "922",
				"923", "924", "999", };
		for (int i = 0; i < array.length; i++) {
			String val = array[i];
			String ans = Channel.cnv3to2(val);
			System.out.println(" assertEquals(\"" + ans
					+ "\", Channel.cnv3to2(\"" + val + "\"));");
		}
	}

	public static void testNewType() {
		List<String> list3 = new ArrayList();
		List<String> list2 = new ArrayList();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 100; j++) {
				String val = "";
				if (j < 10) {
					val = String.valueOf(i) + "0" + String.valueOf(j);
				} else {
					val = String.valueOf(i) + String.valueOf(j);
				}
				list3.add(val);
				// System.out.println(val);
			}
		}
		if (DEBUG)
			System.out.println("#########################################");
		for (Iterator iterator = list3.iterator(); iterator.hasNext();) {
			String val = (String) iterator.next();
			String ans = Channel.cnv3to2(val);
			list2.add(ans);
			if (DEBUG) {
				System.out.println(" assertEquals(\"" + ans
						+ "\", Channel.cnv3to2(\"" + val + "\"));");
			}
		}
		if (DEBUG)
			System.out.println("#########################################");
		for (Iterator iterator = list2.iterator(); iterator.hasNext();) {
			String val = (String) iterator.next();
			String ans = Channel.cnv2to3(val);
			if (DEBUG) {
				System.out.println(" assertEquals(\"" + ans
						+ "\", Channel.cnv2to3(\"" + val + "\"));");

			}
		}

	}

	public static void testClassic() {
		List<String> list3 = new ArrayList();
		List<String> list2 = new ArrayList();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 100; j++) {
				String val = "";
				if (j < 10) {
					val = String.valueOf(i) + "0" + String.valueOf(j);
				} else {
					val = String.valueOf(i) + String.valueOf(j);
				}
				list3.add(val);
				// System.out.println(val);
			}
		}
		if (DEBUG)
			System.out.println("#########################################");
		for (Iterator iterator = list3.iterator(); iterator.hasNext();) {
			String val = (String) iterator.next();
			String ans = Channel_Classic.cnv3to2(val);
			list2.add(ans);
			if (DEBUG) {
				System.out.println(" assertEquals(\"" + ans
						+ "\", Channel.cnv3to2(\"" + val + "\"));");
			}
		}
		if (DEBUG)
			System.out.println("#########################################");
		for (Iterator iterator = list2.iterator(); iterator.hasNext();) {
			String val = (String) iterator.next();
			String ans = Channel_Classic.cnv2to3(val);
			if (DEBUG) {
				System.out.println(" assertEquals(\"" + ans
						+ "\", Channel.cnv2to3(\"" + val + "\"));");

			}
		}
	}

	public static void performanceTest() {
		//ほんのちょっとの差だ・・
		DEBUG = false;
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("ChannelTest");
		elapse.start();
		for (int i = 0; i <= 1000; i++) {
			testNewType();
		}
		elapse.stop();
		kyPkg.tools.Elapse elapse2 = new kyPkg.tools.Elapse("ChannelTest");
		elapse2.start();
		for (int i = 0; i <= 1000; i++) {
			testClassic();
		}
		elapse2.stop();

	}
	public static void main(String[] argv) {
//		performanceTest();
		testNewType();
	}

}
