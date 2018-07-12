package kyPkg.etc;

import java.awt.Component;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.CellStyle;
import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.external.PoiMods;
import kyPkg.filter.EzReader;
import kyPkg.task.LogControl;

//Dimennsion集計の結果をExcel出力する
public class Poi4MashUps {
	public static final String MAXSIZE_MSG = "ファイルサイズが大きいため速度の低下およびメモリ不足でのハングアップが見込まれます。\n処理を続行しますか？";
	private static final int MAX_SIZE = 2000000;// とりあえず2Mを超える場合０の転記を省略する
	private boolean omitMode = false;
	private static final String DOT_ZERO = "0.0";
	private static final String ZERO = "0";
	private String[] resource = null; // 他リソース
	private PoiMods poiMods;
	private FileInputStream stream;
	private String template = "";
	private String outPath = "";
	private int baseV = 10;
	private int baseH = 1;

	private int getBaseV() {
		return baseV;
	}

	private void resetBase(int x, int y) {
		baseH = x;
		baseV = y;
	}

	private void resetBase(Point point) {
		resetBase(point.x - 1, point.y - 1);
	}

	// -------------------------------------------------------------------------
	// アクセッサ
	// -------------------------------------------------------------------------
	public void setResource(String[] resource) {
		this.resource = resource;
		System.out.println("??setResource:" + this.resource.toString());
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Poi4MashUps(String outPath, String template) {
		this.outPath = outPath;
		this.template = template;
		init();
	}

	public void init() {
		if (!(new File(template).isFile())) {
			String wErr = "雛形ファイル「" + template + "」が見つかりませんでした";
			JOptionPane.showMessageDialog((Component) null, wErr, "Message...",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		poiMods = new PoiMods();
		stream = poiMods.openTheBook(template); // ブックを開く
		// パラメータの整合性チェックをここでやっておく！
		// 各シートにコメントを載せる
		// XXX モニター数取得
		// int mCount = 4126;
		// リソースデータを埋め込む
		// embedResources(resource, mCount);
	}

	public boolean sheetRename(String preName, String newName) {
		return poiMods.sheetRename(preName, newName);
	}

	// -------------------------------------------------------------------------
	// ヘッダー（表頭）を書き込む
	// -------------------------------------------------------------------------
	public void plot2Format(String sheetName, String[] colFormat, int row) {
		poiMods.selectSheet(sheetName); // シートを選ぶ
		CellStyle cellStyle = poiMods.setCurrentFont("MS ゴシック", 9, true);
		// ---------------------------------------------------------------------
		// formatを文字列として1行めに埋め込む
		// ---------------------------------------------------------------------
		if (colFormat != null) {
			for (int x = 0; x < colFormat.length; x++) {
				poiMods.setCellValueS(row, x + baseH + 1, colFormat[x],
						cellStyle);
			}
		}
	}

	// -------------------------------------------------------------------------
	// ヘッダー（表頭）を書き込む
	// -------------------------------------------------------------------------
	public void plot2Headers(String sheetName, List<String[]> headList) {
		// ---------------------------------------------------------------------
		poiMods.selectSheet(sheetName); // シートを選ぶ
		// ---------------------------------------------------------------------
		// セル（0,0）に表の最大カラム（幅）を入れておき、マクロ側で利用する
		// ---------------------------------------------------------------------
		String[] header0 = headList.get(0);
		poiMods.setCellValueX(0, 0, String.valueOf(header0.length));
		// ---------------------------------------------------------------------
		// ヘッダー
		//TODO 数字化文字列か判定したうえでプロットするか？　20161219
		// ---------------------------------------------------------------------
		int lev = 0;
		CellStyle cellStyle = poiMods.setCurrentFont("MS ゴシック", 9, true);
		for (String[] header : headList) {
			if (header != null) {
				for (int x = 0; x < header.length; x++) {
					poiMods.setCellValueS(baseV - lev, x + baseH + 1, header[x],
							cellStyle);
				}
			}
			lev++;
		}
	}

	public void plot2SideHeaders(String sheetName, String[] sideArray) {
		poiMods.selectSheet(sheetName); // シートを選ぶ
		CellStyle wrapStyle2 = poiMods.setCurrentFont("MS ゴシック", 9, true);
		// ---------------------------------------------------------------------
		// 表側
		// ---------------------------------------------------------------------
		if (sideArray != null) {
			for (int y = 0; y < sideArray.length; y++) {
				poiMods.setCellValueS(y + baseV + 1, baseH, sideArray[y],
						wrapStyle2);
			}
		}
	}

	// ---------------------------------------------------------------------
	// シートにタグを埋め込んでおき、メタデータにキーが存在したらその値を書き込む
	// ---------------------------------------------------------------------
	public void surveyAndEmbed(String sheetName, HashMap<String, String> meta,
			int depth) {
		if (meta == null || meta.size() == 0)
			return;
		poiMods.selectSheet(sheetName); // シートを選ぶ
		for (int row = 0; row <= 10; row++) {
			for (int col = 0; col <= 20; col++) {
				String tag = poiMods.getCellStr(row, col);
				// System.out.println("row,col(" + row + "," + col + ")" +
				// "tag=>"
				// + tag);
				if (tag != null && !tag.trim().equals("")) {
					String value = meta.get(tag);
					if (value != null) {
						poiMods.setCellValueX(row, col, tag + "　" + value);
						//　poiMods.setCellValueX(row, col + 1, null);//実験隣のカラムを空にできないので、マクロで対応している
						//となりのセルに値を埋め込もうとすると、ヘッダーのデザインバランスがとりずらい・・ので断念20150820
					}
				}
			}
		}
	}

	public void plotTheHmap(int gradeV, int gradeH, String sheetName,
			HashMap map, List list) {
		poiMods.selectSheet(sheetName); // シートを選ぶ
		int index = 0;
		int line = 0;
		int row = 0;
		int col = 0;
		String val = "";
		for (index = 0; index < list.size(); index++) {
			String key = (String) list.get(index);
			// インデックスが一定値を超えたら列を変えようか？
			int x = (line % 4);
			int y = (line / 4) * 5;
			String str = String.valueOf(map.get(key));
			if (str != null && (!str.equals("null"))) {
				row = gradeV + x;
				col = gradeH + y;
				val = key + str;
				poiMods.setCellValueX(row, col, val);
				line++;
			}
		}
	}

	public void strMatrix2Sheet(String sheetName, String[][] matrix) {
		strMatrix2Sheet(0, 0, sheetName, matrix);
	}

	public void intMatrix2Sheet(String sheetName, int[][] matrix) {
		poiMods.selectSheet(sheetName); // シートを選ぶ
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				poiMods.selectCell(baseV + y + 1, baseH + x + 1); // セルを選ぶ
				poiMods.setCellValue(matrix[y][x]);
			}
		}
	}

	// xxx
	public void resetPrevStyles() {
		poiMods.resetPrevStyles();
	}

	// ------------------------------------------------------------------------
	// シートを選ぶ
	// ------------------------------------------------------------------------
	public void plotByMatrix(String sheetName, List<List> matrix) {
		poiMods.selectSheet(sheetName);
		for (int y = 0; y < matrix.size(); y++) {
			List row = (List) matrix.get(y);
			for (int x = 0; x < row.size(); x++) {
				poiMods.setCellValue(baseV + y, baseH + x,
						row.get(x).toString());
			}
		}
	}

	// ------------------------------------------------------------------------
	// シートを選ぶ
	// ------------------------------------------------------------------------
	public void strMatrix2Sheet(int gradeV, int gradeH, String sheetName,
			String[][] matrix) {
		poiMods.selectSheet(sheetName);
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				poiMods.setCellValue(baseV + y + gradeV, baseH + x + gradeH,
						matrix[y][x]);
			}
		}
	}

	// ------------------------------------------------------------------------
	// テキストファイルからシートにデータを転記する
	// ------------------------------------------------------------------------
	public void plotFromFile(String sheetName, String inPath,
			String[] headers) {
		poiMods.selectSheet(sheetName);
		// Inf_iClosure reader = new EzReader(inPath);
		EzReader reader = new EzReader(inPath);
		File inFile = new File(inPath);

		if (omitMode == false && inFile.length() > MAX_SIZE) {
			omitMode = true;//20170531
//			if (new Msgbox(null).confirmYN(MAXSIZE_MSG)) {
//				omitMode = true;
//			} else {
//				System.exit(999);
//			}
		} else {
			omitMode = false;
		}

		if (omitMode) {
			System.err.println("#omit Mode# inPath:" + inPath + " size:"
					+ inFile.length());
		}
		// あいてむからむに　そんざいする　もじれつを　 ぶんかつしてしまえばよさそう
		// @@@20140227
		// Inf_StringCnv stringCnv = new ReplaceFirst("\\$", "\t");
		// Inf_StringCnv stringCnv = new ReplaceFirst(CommonVars.SPACING, "\t");
		// reader.setStringCnv(stringCnv);

		if (headers != null) {
			for (int col = 0; col < headers.length; col++) {
				poiMods.setCellValue(baseV + 0, baseH + col, headers[col]);
			}
			baseV = baseV + 1;
		}

		reader.open();
		String[] cells = null;
		int lCnt = -1;
		while (reader.readLine() != null) {
			lCnt++;
			int wStat = reader.getStat();
			if (wStat >= 0) {
				cells = reader.getSplited();
				//				System.out.println("cells.length:"+cells.length);
				for (int x = 0; x < cells.length; x++) {
					if (omitMode) {//ファイルサイズが大きい場合には自動的にゼロの転記を省略　20161005
						if (!cells[x].equals(DOT_ZERO)
								&& !cells[x].equals(ZERO)) {
							//							System.out.print(">>" + cells[x]);
							poiMods.setCellValue(baseV + lCnt, baseH + x,
									cells[x]);
						}
					} else {
						poiMods.setCellValue(baseV + lCnt, baseH + x, cells[x]);
					}
				}
			}
		}
		reader.close();
	}

	public void fin() {
		poiMods.saveAs(outPath);
		try {
			stream.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		// test0();
		test20140718();
	}

	public static void test0() {
		String xlsTmp = ResControlWeb.getD_Resources_Templates("QPRBASE02.xls");
		String sys200 = ResControlWeb.getD_Resources_Templates("QPRMASHUP.xls");
		String[] resource = { "品目,コーラなど", "対象期間,２００８年1月１日から１０月１０日",
				"天気,雨のち曇り" };
		String[] headArray = { "ビールA", "ビールB", "ビールC", "ビールD", "発泡酒", "その他" };
		String[] captionSide = { "0:00 - 0:59", "1:00 - 1:59", "2:00 - 2:59",
				"3:00 - 3:59", "4:00 - 4:59", "5:00 - 5:59" };
		int[][] matrix = { { 1, 2, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5, 6 },
				{ 1, 2, 3, 4, 5, 6 }, { 6, 5, 4, 3, 2, 1 },
				{ 6, 5, 4, 3, 2, 1 }, { 6, 5, 4, 3, 2, 1 } };

		Poi4MashUps poiMatrix = new Poi4MashUps(sys200, xlsTmp);
		poiMatrix.setResource(resource);
		String sheetName = "sheet1";

		List<String[]> headList = new ArrayList();
		headList.add(headArray);
		poiMatrix.plot2Headers(sheetName, headList);
		poiMatrix.plot2SideHeaders(sheetName, captionSide);

		poiMatrix.intMatrix2Sheet(sheetName, matrix);
		poiMatrix.fin();
	}

	private static void test20140718() {
		String template = "c:/LayoutTmp.xlsx";// 雛形
		String excelOut = "c:/result.xlsx";// 出力ファイル
		String bodyPath = "c:/JICFS_JAN_Layout.txt";// 入力データ（パラメータ？！）
		String sheetName = "text";// 出力先シート名
		Poi4MashUps poiMatrix = new Poi4MashUps(excelOut, template);
		// ----------------------------------------------------------------
		// プロットする
		// ----------------------------------------------------------------
		poiMatrix.resetBase(-1, -1);
		poiMatrix.plotFromFile(sheetName, bodyPath, null);
		poiMatrix.fin();
		// ----------------------------------------------------------------
		// 開く
		// ----------------------------------------------------------------
		UserAgent.openWithUA(excelOut);
	}

	public void plotIt(String sheetName, SheetObject sheetObj) {
		// --------------------------------------------------------------------
		HashMap<String, String> meta = sheetObj.getMeta();// コメント類
		// --------------------------------------------------------------------
		List<String[]> headList = sheetObj.getHeaderList();// ヘッダーリスト
		// --------------------------------------------------------------------
		String[] colWidth = sheetObj.getColWidth();// セルの幅
		// --------------------------------------------------------------------
		String[] colFormat = sheetObj.getColFormat();// セルの書式
		// --------------------------------------------------------------------
		String bodyPath = sheetObj.getBodyPath();// BODY（明細）データ （テキストファイル）へのパス
		// --------------------------------------------------------------------
		// メタデータの並び順をそろえる
		// (１)共通の項目を先頭に持ってくる
		// XXX　雛形シート状にタグを配置して、そのタグと同じ位置にメタデータを貼り付けるように書き換える（20140708）
		// --------------------------------------------------------------------
		// String[] commonHeads = { "クライアント：", "品 目       ：", "対象期間：", "対象期間1：",
		// "対象期間2：", "期間内モニター数：" };
		// --------------------------------------------------------------------
		String[] commonHeads = { "" };

		HashMap<String, String> infoMapx = new HashMap();
		List keys = new ArrayList();

		for (String element : commonHeads) {
			String val = meta.get(element);
			if (val != null) {
				infoMapx.put(element, val);
				keys.add(element);
				meta.remove(element);// 20140227
			}
		}
		// --------------------------------------------------------------------
		// (３)余計な項目を除去
		// --------------------------------------------------------------------
		meta.remove("befYMD:");
		meta.remove("aftYMD:");
		// --------------------------------------------------------------------
		// (４)共通の項目以外のものを追加
		// --------------------------------------------------------------------
		List<String> keyList = new ArrayList(meta.keySet());
		Collections.sort(keyList);
		for (String element : keyList) {
			String val = meta.get(element);
			infoMapx.put(element, val);
			keys.add(element);
		}

		// System.out.println("@plotIt >>> sheetName:" + sheetName);
		resetPrevStyles();

		// ----------------------------------------------------------------
		// メタデータを貼り付ける
		// ----------------------------------------------------------------
		surveyAndEmbed(sheetName, meta, 10);

		String[] captionSide = { "A", "B", "C", "D", "E", "F" };
		captionSide = null;

		// ----------------------------------------------------------------
		// ヘッダーをプロットする
		// ----------------------------------------------------------------
		plot2Headers(sheetName, headList);

		// ----------------------------------------------------------------
		// colFormat & colWidth
		// ----------------------------------------------------------------
		plot2Format(sheetName, colFormat, 0);
		plot2Format(sheetName, colWidth, 1);

		// ----------------------------------------------------------------
		// 表側をプロットする
		// ----------------------------------------------------------------
		plot2SideHeaders(sheetName, captionSide);
		resetPrevStyles();
		// ----------------------------------------------------------------
		// Bodyをプロットする
		// ----------------------------------------------------------------
		plotFromFile(sheetName, bodyPath, null);
	}

	// ------------------------------------------------------------------------
	// 雛形シートに書き出す
	// ------------------------------------------------------------------------
	public static String write2excel(HashMap<String, SheetObject> sheetMap,
			String excelOut, String tempName) {
		//		ITP_Ctrl itpCtrl = ITP_Ctrl.getInstance();
		String template = ResControl.getQPRHomeQpr(tempName);
		LogControl.info("#@write2excel# Excel出力 template:" + template);
		Poi4MashUps poiMatrix = new Poi4MashUps(excelOut, template);
		// シートごとに転記する
		List<String> keyList = new ArrayList(sheetMap.keySet());
		Collections.sort(keyList);
		for (String sheetName : keyList) {
			// System.out.println("#■#■#■ write2excel ■#■#■#" + sheetName);
			SheetObject resultCmp = sheetMap.get(sheetName);
			// ----------------------------------------------------------------
			// plotIt
			// ----------------------------------------------------------------
			poiMatrix.plotIt(sheetName, resultCmp);
		}
		poiMatrix.fin();
		UserAgent.openWithUA(excelOut);
		return excelOut;
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	public static void send2excel(String outPath, String template,
			List<SheetObject> sheetObjs) {
		Poi4MashUps poiObj = new Poi4MashUps(outPath, template);
		for (SheetObject sheetObj : sheetObjs) {
			// --------------------------------------------------------------------
			String sheetName = sheetObj.getSheetName();
			String bodyPath = sheetObj.getBodyPath();
			String[] headers = sheetObj.getHeader();
			HashMap<String, String> meta = sheetObj.getMeta();
			Point point = sheetObj.getPoint();
			// --------------------------------------------------------------------
			// 基準位置をセットする（ヘッダーの開始位置）
			// --------------------------------------------------------------------
			if (point == null) {
				poiObj.resetBase(-1, -1);// offset(point)
			} else {
				poiObj.resetBase(point);
			}
			// ----------------------------------------------------------------
			// メタデータを貼り付ける
			// ----------------------------------------------------------------
			poiObj.surveyAndEmbed(sheetName, meta, poiObj.getBaseV() - 1);
			// --------------------------------------------------------------------
			// ファイルよりプロットする
			// --------------------------------------------------------------------
			poiObj.plotFromFile(sheetName, bodyPath, headers);
		}
		poiObj.fin();
		// --------------------------------------------------------------------
		// 開く
		// --------------------------------------------------------------------
		System.out.println("excelOut debug:" + outPath);
		UserAgent.openWithUA(outPath);
	}

	public static void send2excel_mod(String outPath, String sheetName,
			String bodyPath, String template, String[] header, Point point,
			HashMap<String, String> meta) {
		SheetObject sheetObj = new SheetObject(bodyPath, meta, header);
		Poi4MashUps poiObj = new Poi4MashUps(outPath, template);
		poiObj.plotIt(sheetName, sheetObj);
		poiObj.fin();
		// --------------------------------------------------------------------
		// 開く
		// --------------------------------------------------------------------
		System.out.println("excelOut debug:" + outPath);
		UserAgent.openWithUA(outPath);
	}

}
