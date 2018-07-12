package kyPkg.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import globals.ResControlWeb;
import kyPkg.converter.CnvDictionary;
import kyPkg.converter.Inf_StrConverter;
import kyPkg.filter.EzWriter;
import kyPkg.uCodecs.CharConv;
import kyPkg.uFile.HashMapUtil;
public class ChannelCnv {
	private static final String TAB = "\t";

	//	// <<購入先処理>> 店コード変換テーブルインコア処理・・・2011/02/09
	//	System.out.println("## INCORE ##" + resPath);
	//	if (new File(resPath).isFile() == false) {
	//		System.out.println("#ERROR file not found:" + resPath);
	//		System.exit(999);
	//	}

	private static final String CHANNEL_CNV_TXT = "channelCnv.txt";

	public static CnvDictionary setUpConverter() {
		String resPath = ResControlWeb.getD_Resources_QPR_RES()
				+ CHANNEL_CNV_TXT;
		System.out.println("## INCORE ## 変換パラメータファイル " + resPath);
		if (new File(resPath).isFile() == false) {
			System.out.println("#ERROR file not found:" + resPath);
			System.exit(999);
		}
		int keyCol = 1;
		int valCol = 0;
		CnvDictionary dict = new CnvDictionary(resPath, keyCol, valCol);
		// 見つからなかったときの処理・・・
		Inf_StrConverter converter = new Inf_StrConverter() {
			@Override
			public String convert(String val) {
				if (val.matches("4[456789].*")) {
					// System.out.println("44? Type百貨店に割り込んで付番されている地方スーパーとみなす");
					return "19";// 仮に地方スーパーが19というコードとする
				} else {
					// System.out.println("otherType 大店区分を活かして・・・その他処理");
					return val.charAt(0) + "9";
				}
			}
		};
		dict.setConverter(converter);
		return dict;
	}

	public static void testConverter() {
		// 店コード変換処理のプロトタイプ・・・2011/02/09
		CnvDictionary dict = setUpConverter();
		// 通常このように使用する
		String ans = dict.getValue("440");
		System.out.println("ans of 440 =>" + ans);
		System.out
				.println("キー部のみ取り出してみてチェックする-----------------------------------");
		List keys = dict.getKeyList();
		keys.add("439");
		keys.add("440");
		keys.add("451");
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			int seq = dict.getDestPos(key); // 何番目にあたるか
			String val = dict.getValue(key); // 変更先の値は何か
			System.out.println("key:" + key + " => seq:" + seq + " val:" + val);
		}
	}

	public static void test110228() {
		// 店コード変換処理のプロトタイプ・・・2011/02/09
		CnvDictionary dict = setUpConverter();
		// 今回、追加されるコードすべてをテスト
		System.out.println("ギフトセット　　　　　　　　　001=>" + dict.getValue("001"));
		System.out.println("景品（パチンコ等）　　　　　　002=>" + dict.getValue("002"));
		System.out.println("その他贈答品　　　　　　　　　003=>" + dict.getValue("003"));
		System.out.println("相鉄ローゼン　　　　　　　　　149=>" + dict.getValue("149"));
		System.out.println("フジスーパー　　　　　　　　　150=>" + dict.getValue("150"));
		System.out.println("紀ノ国屋　　　　　　　　　　　151=>" + dict.getValue("151"));
		System.out.println("トライアル　　　　　　　　　　152=>" + dict.getValue("152"));
		System.out.println("ダイイチ　　　　　　　　　　　153=>" + dict.getValue("153"));
		System.out.println("Ａコープ（農協）　　　　　　　154=>" + dict.getValue("154"));
		System.out.println("ビッグハウス　　　　　　　　　155=>" + dict.getValue("155"));
		System.out.println("いとく　　　　　　　　　　　　156=>" + dict.getValue("156"));
		System.out.println("ジョイス　　　　　　　　　　　157=>" + dict.getValue("157"));
		System.out.println("ベニーマート、カブセンター　　158=>" + dict.getValue("158"));
		System.out.println("マルト　　　　　　　　　　　　159=>" + dict.getValue("159"));
		System.out.println("ヤマザワ　　　　　　　　　　　160=>" + dict.getValue("160"));
		System.out.println("ユニバース　　　　　　　　　　161=>" + dict.getValue("161"));
		System.out.println("リオン・ドール　　　　　　　　162=>" + dict.getValue("162"));
		System.out.println("ウジエスーパー　　　　　　　　163=>" + dict.getValue("163"));
		System.out.println("マエダ　　　　　　　　　　　　164=>" + dict.getValue("164"));
		System.out.println("カスミ系　　　　　　　　　　　165=>" + dict.getValue("165"));
		System.out.println("エコスグループ　　　　　　　　166=>" + dict.getValue("166"));
		System.out.println("オータニ　　　　　　　　　　　167=>" + dict.getValue("167"));
		System.out.println("とりせん　　　　　　　　　　　168=>" + dict.getValue("168"));
		System.out.println("フレッセイ　　　　　　　　　　169=>" + dict.getValue("169"));
		System.out.println("ベルク　　　　　　　　　　　　170=>" + dict.getValue("170"));
		System.out.println("原信、ナルス　　　　　　　　　171=>" + dict.getValue("171"));
		System.out.println("ウオロク　　　　　　　　　　　172=>" + dict.getValue("172"));
		System.out.println("アルビス　　　　　　　　　　　173=>" + dict.getValue("173"));
		System.out.println("ツルヤ　　　　　　　　　　　　174=>" + dict.getValue("174"));
		System.out.println("マツヤ　　　　　　　　　　　　175=>" + dict.getValue("175"));
		System.out.println("大阪屋ショップ　　　　　　　　176=>" + dict.getValue("176"));
		System.out.println("オギノ　　　　　　　　　　　　177=>" + dict.getValue("177"));
		System.out.println("アップルランド　　　　　　　　178=>" + dict.getValue("178"));
		System.out.println("マルエー　　　　　　　　　　　179=>" + dict.getValue("179"));
		System.out.println("カネスエ　　　　　　　　　　　180=>" + dict.getValue("180"));
		System.out.println("しずてつストア　　　　　　　　181=>" + dict.getValue("181"));
		System.out.println("ナフコ　　　　　　　　　　　　182=>" + dict.getValue("182"));
		System.out.println("遠鉄ストア　　　　　　　　　　183=>" + dict.getValue("183"));
		System.out.println("アオキスーパー　　　　　　　　184=>" + dict.getValue("184"));
		System.out.println("三心　　　　　　　　　　　　　185=>" + dict.getValue("185"));
		System.out.println("一号舘　　　　　　　　　　　　186=>" + dict.getValue("186"));
		System.out.println("ドミー　　　　　　　　　　　　187=>" + dict.getValue("187"));
		System.out.println("ワイストア、ヨシヅヤ　　　　　188=>" + dict.getValue("188"));
		System.out.println("スーパーサンシ　　　　　　　　189=>" + dict.getValue("189"));
		System.out.println("マルアイ　　　　　　　　　　　190=>" + dict.getValue("190"));
		System.out.println("松源　　　　　　　　　　　　　191=>" + dict.getValue("191"));
		System.out.println("フレスコ　　　　　　　　　　　192=>" + dict.getValue("192"));
		System.out.println("マルイグループ　　　　　　　　193=>" + dict.getValue("193"));
		System.out.println("ユアーズ、丸和　　　　　　　　194=>" + dict.getValue("194"));
		System.out.println("天満屋系　　　　　　　　　　　195=>" + dict.getValue("195"));
		System.out.println("フジ系　　　　　　　　　　　　196=>" + dict.getValue("196"));
		System.out.println("ディオ、ラ・ムー　　　　　　　197=>" + dict.getValue("197"));
		System.out.println("ニシナ、フードバスケット　　　198=>" + dict.getValue("198"));
		System.out.println("スリーエフ　　　　　　　　　　225=>" + dict.getValue("225"));
		System.out.println("スパー　　　　　　　　　　　　226=>" + dict.getValue("226"));
		System.out.println("セーブオン　　　　　　　　　　227=>" + dict.getValue("227"));
		System.out.println("トライアルディスカウントコンん228=>" + dict.getValue("228"));
		System.out.println("衣料品店　　　　　　　　　　　313=>" + dict.getValue("313"));
		System.out.println("薬王堂　　　　　　　　　　　　540=>" + dict.getValue("540"));
		System.out.println("アサヒ　　　　　　　　　　　　541=>" + dict.getValue("541"));
		System.out.println("マルエドラッグ　　　　　　　　542=>" + dict.getValue("542"));
		System.out.println("ドラッグてらしま　　　　　　　543=>" + dict.getValue("543"));
		System.out.println("アオキ　　　　　　　　　　　　544=>" + dict.getValue("544"));
		System.out.println("コダマ　　　　　　　　　　　　545=>" + dict.getValue("545"));
		System.out.println("ゲンキー　　　　　　　　　　　546=>" + dict.getValue("546"));
		System.out.println("アメリカンドラッグ　　　　　　547=>" + dict.getValue("547"));
		System.out.println("シメノドラッグ　　　　　　　　548=>" + dict.getValue("548"));
		System.out.println("ドラッグフジイ　　　　　　　　549=>" + dict.getValue("549"));
		System.out.println("ウォンツ　　　　　　　　　　　550=>" + dict.getValue("550"));
		System.out.println("ひまわり　　　　　　　　　　　551=>" + dict.getValue("551"));
		System.out.println("ザグザグ　　　　　　　　　　　552=>" + dict.getValue("552"));
		System.out.println("ウェルネス　　　　　　　　　　553=>" + dict.getValue("553"));
		System.out.println("くすりのラブ　　　　　　　　　554=>" + dict.getValue("554"));
		System.out.println("レディ薬局　　　　　　　　　　555=>" + dict.getValue("555"));
		System.out.println("メディコ21　　　　　　　　　　556=>" + dict.getValue("556"));
		System.out.println("マック　　　　　　　　　　　　557=>" + dict.getValue("557"));
		System.out.println("かもめ薬局　　　　　　　　　　558=>" + dict.getValue("558"));
		System.out.println("職場（オフィス）の自販機　　　610=>" + dict.getValue("610"));
		System.out.println("職場（工場・工事現場）の自販販611=>" + dict.getValue("611"));
		System.out.println("学校内の自販機　　　　　　　　612=>" + dict.getValue("612"));
		System.out.println("パチンコ店内の自販機　　　　　613=>" + dict.getValue("613"));
		System.out.println("娯楽・レジャー施設（パチンコこ614=>" + dict.getValue("614"));
		System.out.println("小売店内の自販機　　　　　　　615=>" + dict.getValue("615"));
		System.out.println("病院内の自販機　　　　　　　　616=>" + dict.getValue("616"));
		System.out.println("サービスエリアの自販機　　　　617=>" + dict.getValue("617"));
		System.out.println("住宅街の道路沿いの自販機　　　618=>" + dict.getValue("618"));
		System.out.println("商店街の道路沿いの自販機　　　619=>" + dict.getValue("619"));
		System.out.println("国道・幹線道路の道路脇沿いのの620=>" + dict.getValue("620"));
		System.out.println("サンデー　　　　　　　　　　　941=>" + dict.getValue("941"));
		System.out.println("ダイユーエイト　　　　　　　　942=>" + dict.getValue("942"));
		System.out.println("ジョイフルヤマシン　　　　　　943=>" + dict.getValue("943"));
		System.out.println("セキチュー　　　　　　　　　　944=>" + dict.getValue("944"));
		System.out.println("ムサシ　　　　　　　　　　　　945=>" + dict.getValue("945"));
		System.out.println("綿半　　　　　　　　　　　　　946=>" + dict.getValue("946"));
		System.out.println("ＰＬＡＮＴ　　　　　　　　　　947=>" + dict.getValue("947"));
		System.out.println("みつわ　　　　　　　　　　　　948=>" + dict.getValue("948"));
		System.out.println("アヤハディオ　　　　　　　　　949=>" + dict.getValue("949"));
		System.out.println("ジャパン　　　　　　　　　　　950=>" + dict.getValue("950"));
		System.out.println("ジュンテンドー　　　　　　　　951=>" + dict.getValue("951"));
		System.out.println("タイム　　　　　　　　　　　　952=>" + dict.getValue("952"));
		System.out.println("ホームセンターいない　　　　　953=>" + dict.getValue("953"));
		System.out.println("ユーホー　　　　　　　　　　　954=>" + dict.getValue("954"));
		System.out.println("ナンバ　　　　　　　　　　　　955=>" + dict.getValue("955"));
		System.out.println("ミスターマックス　　　　　　　956=>" + dict.getValue("956"));
		System.out.println("マルニ　　　　　　　　　　　　957=>" + dict.getValue("957"));
		System.out.println("西村ジョイ　　　　　　　　　　958=>" + dict.getValue("958"));
		System.out.println("ダイレックス　　　　　　　　　959=>" + dict.getValue("959"));
		System.out.println("ホームワイド　　　　　　　　　960=>" + dict.getValue("960"));
		System.out.println("スーパーキッド　　　　　　　　961=>" + dict.getValue("961"));
		System.out.println("ＨＩ　ヒロセ　　　　　　　　　962=>" + dict.getValue("962"));
		System.out.println("ローソンストア１００　　　　　963=>" + dict.getValue("963"));
		System.out.println("ＳＨＯＰ９９　　　　　　　　　964=>" + dict.getValue("964"));
		System.out.println("ビックリッキー　　　　　　　　965=>" + dict.getValue("965"));
		System.out.println("ダイゼン　　　　　　　　　　　966=>" + dict.getValue("966"));
		System.out.println("えびすや　　　　　　　　　　　967=>" + dict.getValue("967"));
		System.out.println("テキサス　　　　　　　　　　　968=>" + dict.getValue("968"));
		System.out.println("ロッキー　　　　　　　　　　　969=>" + dict.getValue("969"));
		System.out.println("メガ　　　　　　　　　　　　　970=>" + dict.getValue("970"));
		System.out.println("一二三屋　　　　　　　　　　　971=>" + dict.getValue("971"));
		System.out.println("セプ・ドール　　　　　　　　　972=>" + dict.getValue("972"));
		System.out.println("ジャパリカ　　　　　　　　　　973=>" + dict.getValue("973"));
		System.out.println("酒のデパートヨシダ　　　　　　974=>" + dict.getValue("974"));
		System.out.println("ヒーロー　　　　　　　　　　　975=>" + dict.getValue("975"));
		System.out.println("万寿屋　　　　　　　　　　　　976=>" + dict.getValue("976"));
		System.out.println("酒のＴＯＰ　　　　　　　　　　977=>" + dict.getValue("977"));
		System.out.println("ＴＡＫＡＧＩ　　　　　　　　　978=>" + dict.getValue("978"));
		System.out.println("戸田酒販　　　　　　　　　　　979=>" + dict.getValue("979"));
		System.out.println("リカーワールド華　　　　　　　980=>" + dict.getValue("980"));
		System.out.println("ヤスブン　　　　　　　　　　　981=>" + dict.getValue("981"));
		System.out.println("酒やビック　　　　　　　　　　982=>" + dict.getValue("982"));
		System.out.println("リカーマウンテン　　　　　　　983=>" + dict.getValue("983"));
		System.out.println("酒のすぎた　　　　　　　　　　984=>" + dict.getValue("984"));
		System.out.println("ＳＡＫＥ市場マルシェ　　　　　985=>" + dict.getValue("985"));
		System.out.println("酒のたんだ　　　　　　　　　　986=>" + dict.getValue("986"));
		System.out.println("ゴリラ　　　　　　　　　　　　987=>" + dict.getValue("987"));
		System.out.println("デイ・リンク　　　　　　　　　988=>" + dict.getValue("988"));
		System.out.println("酒の悟空　　　　　　　　　　　989=>" + dict.getValue("989"));
		System.out.println("フロンティア　　　　　　　　　990=>" + dict.getValue("990"));
		System.out.println("ジン　　　　　　　　　　　　　991=>" + dict.getValue("991"));
		System.out.println("リカオー　　　　　　　　　　　992=>" + dict.getValue("992"));
		System.out.println("アワーリカー　　　　　　　　　993=>" + dict.getValue("993"));
		System.out.println("明治城　　　　　　　　　　　　994=>" + dict.getValue("994"));
		System.out.println("酒のキンコー　　　　　　　　　995=>" + dict.getValue("995"));
		System.out.println("あんくるふじや　　　　　　　　996=>" + dict.getValue("996"));
		System.out.println("ベリーマッチ　　　　　　　　　997=>" + dict.getValue("997"));
		System.out.println("びっくり酒店　　　　　　　　　998=>" + dict.getValue("998"));

		System.out.println("●ハローズ　　　　　　　　　　440=>" + dict.getValue("440"));
		System.out.println("●フレスタ　　　　　　　　　　441=>" + dict.getValue("441"));
		System.out.println("●マルナカ系　　　　　　　　　442=>" + dict.getValue("442"));
		System.out.println("●みしまや、ヴェルデ　　　　　443=>" + dict.getValue("443"));
		System.out.println("●丸合　　　　　　　　　　　　444=>" + dict.getValue("444"));
		System.out.println("●万惣、マルシェー　　　　　　445=>" + dict.getValue("445"));
		System.out.println("●エブリィ　　　　　　　　　　446=>" + dict.getValue("446"));
		System.out.println("●レッドキャベツ　　　　　　　447=>" + dict.getValue("447"));
		System.out.println("●まるきグループ　　　　　　　448=>" + dict.getValue("448"));
		System.out.println("●オンリーワン　　　　　　　　449=>" + dict.getValue("449"));
		System.out.println("●ママイグループ　　　　　　　450=>" + dict.getValue("450"));
		System.out.println("●キョーエイ　　　　　　　　　451=>" + dict.getValue("451"));
		System.out.println("●サニーマート　　　　　　　　452=>" + dict.getValue("452"));
		System.out.println("●サンシャイン　　　　　　　　453=>" + dict.getValue("453"));
		System.out.println("●セブン　　　　　　　　　　　454=>" + dict.getValue("454"));
		System.out.println("●セブンスター　　　　　　　　455=>" + dict.getValue("455"));
		System.out.println("●マルヨシセンター　　　　　　456=>" + dict.getValue("456"));
		System.out.println("●エレナ　　　　　　　　　　　457=>" + dict.getValue("457"));
		System.out.println("●ルミエール　　　　　　　　　458=>" + dict.getValue("458"));
		System.out.println("●マルミヤストア　　　　　　　459=>" + dict.getValue("459"));
	}

	public static void test20120302() {
		//		// 店コード変換処理のプロトタイプ・・・2012/03/02
		//		String resPath = ChannelCnv.getParam();// 変換パラメータファイル
		//ｐｃから見た場合のパス
		//		String resPath = ResControl.T_RES + "qpr/src/channelCnv.txt";
		CnvDictionary dict = setUpConverter();

		// 今回、追加されるコードすべてをテスト
		System.out.println("ヴィドフランス　　　　　　　　321=>" + dict.getValue("321"));
		System.out.println("サンエトワール　　　　　　　　322=>" + dict.getValue("322"));
		System.out.println("ハースブラウン　　　　　　　　323=>" + dict.getValue("323"));
		System.out.println("リトルマーメイド　　　　　　　324=>" + dict.getValue("324"));
		System.out.println("モンタボー　　　　　　　　　　325=>" + dict.getValue("325"));
		System.out.println("ドンク　　　　　　　　　　　　326=>" + dict.getValue("326"));
		System.out.println("アンデルセン　　　　　　　　　327=>" + dict.getValue("327"));
		System.out.println("ポンパドウル　　　　　　　　　328=>" + dict.getValue("328"));
		System.out.println("カンテボーレ　　　　　　　　　329=>" + dict.getValue("329"));
		System.out.println("サンジェルマン　　　　　　　　330=>" + dict.getValue("330"));
		System.out.println("ホルン　　　　　　　　　　　　331=>" + dict.getValue("331"));
		System.out.println("神戸屋キッチン・神戸屋ベーカリ332=>" + dict.getValue("332"));
		System.out.println("サンマルク　　　　　　　　　　333=>" + dict.getValue("333"));
		System.out.println("ベーグル＆ベーグル　　　　　　334=>" + dict.getValue("334"));
		System.out.println("サンメリー　　　　　　　　　　335=>" + dict.getValue("335"));
		System.out.println("パン・菓子店　その他　　　　　307=>" + dict.getValue("307"));
	}

	//---------------------------------------------------------------------
	//　マクロミルのマスターに存在するが、変換テーブルに存在しないものを洗い出し　【20160915】
	// 追加する変換データの雛形を生成する
	//---------------------------------------------------------------------
	//※チェックに使用するマクロミルで定義している（コード、名前をTABで区切ったテキストファイルの例）
	//	"C:/test/MM_Code_NAME.txt""C:/test/MM_Code_NAME.txt"
	//	184	アオキスーパー
	//	185	三心
	//	186	一号舘
	//	187	ドミー
	//	188	ワイストア、ヨシヅヤ
	//	189	スーパーサンシ
	//	190	マルアイ
	//	191	松源
	//---------------------------------------------------------------------
	public static void checkChannelCode() {
		CnvDictionary cnvDict = ChannelCnv.setUpConverter();
		String cnvPath = ResControlWeb.getD_Resources_QPR_RES()
				+ CHANNEL_CNV_TXT;
		HashMap<String, String> cnvMap = HashMapUtil
				.file2HashMap(cnvPath, 1, 2);
		//---------------------------------------------------------------------
		String mmPath = "C:/test/MM_Code_NAME.txt";
		HashMap<String, String> mmMap = HashMapUtil.file2HashMap(mmPath, 0, 1);
		//---------------------------------------------------------------------
		System.out.println("#################################################");
		System.out.println("## 変換テーブルに存在しないチャネルコード");
		System.out.println("## channel.dicを参照して、使用していない短縮コードを割り当てる");
		System.out.println("## channelCnv.txtおよびchannel.dicに追加する");
		System.out.println("#################################################");
		System.out.println("## 存在しない店コード（※注意、2桁店コードの割り当てに注意する）  ");
		System.out.println("#################################################");
		List<String> result = new ArrayList();
		if (mmMap != null) {
			List<String> keyList = new ArrayList(mmMap.keySet());
			Collections.sort(keyList);
			for (String mmCode : keyList) {
				if (!cnvMap.containsKey(mmCode)) {
					String val = mmMap.get(mmCode);
					// -----------------------------------------------------
					// 購入先変換処理 (11/03/07～)
					String code2 = cnvDict.getValue(mmCode);
					String name1 = CharConv.cnvWide(val, 15);
					String name2 = CharConv.cnvWide(val, 5);
					String rec = code2 + TAB + mmCode + TAB + name1 + TAB
							+ name2 + "@";
					result.add(rec);
					System.out.println(rec);
				}
			}
		}
		System.out.println("#################################################");
		String oPath =  "c:/追加する店コード雛形.txt";
		EzWriter.list2File(oPath, result);
	}

	public static void main(String[] argv) {
		//		testConverter() ;
		//		 test110228();
		//		test20120302();
		kyPkg.util.ChannelCnv.checkChannelCode();
	}
}
