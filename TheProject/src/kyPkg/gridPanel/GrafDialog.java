package kyPkg.gridPanel;

import static kyPkg.uFile.Tran_SourceObj.TRAN_HOUR;
import static kyPkg.uFile.Tran_SourceObj.TRAN_YMD;
import static kyPkg.util.Joint.join;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import kyPkg.external.PieCharts;
import kyPkg.external.PopulationChart;
import kyPkg.external.TimeSerialChart;
import kyPkg.panel.DialogObj;
import kyPkg.util.MyColor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;

public class GrafDialog {
	public static final String MONITOR_COUNT = "monitorCount";
	private Grid_Panel gridPanel;
	// -------------------------------------------------------------------
	private static final String ATTR = "属性　　";
	private static final String LIMITS = "限定";
	private static final String DATA = "データ";
	private static final String SUMMERRIZE = "簡易集計 　（Escキーで元の画面に戻ります）";
	private static final String NINNZUU = "※人数";
	// private static final String KINNGAKU = "金額";
	// private static final String SUURYOU = "数量";
	private String monitorCount = "";

	public GrafDialog(HashMap<String, String> pMap, Grid_Panel grid) {
		super();
		this.gridPanel = grid;
		monitorCount = pMap.get(MONITOR_COUNT);
		if (monitorCount == null)
			monitorCount = "";
		System.out.println("#GrafDialog# monitorCount:" + monitorCount);
	}

	// ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
	// ※グラフ表示
	// ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
	public void showSumDialog(List<String> header, List<String> sumList1,
			List<String> uqList, String name) {
		System.out.println("# showSumDialog # columnName=>" + name);
		// ---------------------------------------------------------------------
		// サマリーデータテキスト表示部分作成
		// ---------------------------------------------------------------------
		StringBuffer headerBuf = new StringBuffer();
		headerBuf.append(ATTR);
		String leadingStr = sumList1.get(0);
		// 先頭の文字をひとつ拝借しているが、コレが半角だと崩れる
		// （先頭一文字目が半角かどうか調べればよいのだろうケド・・・）

		String delimiter = "@";
		// System.out.println("@@@@ leadingStr:" + leadingStr);
		int len = leadingStr.indexOf(delimiter) - 2;
		for (int i = 0; i < len; i++) {
			headerBuf.append("　");
		}
		headerBuf.append("\t");
		headerBuf.append(NINNZUU);
		for (String element : header) {
			headerBuf.append("\t");
			headerBuf.append("※" + element);
		}
		// headerBuf.append("\t");
		// headerBuf.append(KINNGAKU);
		// headerBuf.append("\t");
		// headerBuf.append(SUURYOU);
		headerBuf.append("\n");

		JTabbedPane tabbedPane = new JTabbedPane();

		String is3D = kyPkg.globals.GlobalCtrl.getValue(OptionPanel.DISPLAY_3D);

		// ---------------------------------------------------------------------
		// １００人当りに換算
		// ---------------------------------------------------------------------
		String suffix = "";
		if (!monitorCount.equals("")) {
			if (kyPkg.globals.GlobalCtrl.isNotBlank(OptionPanel.CALQ100)) {
				suffix = "(100人当り)  Sample " + monitorCount + "人";
				sumList1 = convert2SampleBase(sumList1, "\t", monitorCount);
			}
		}

		String dump = headerBuf.toString() + join(sumList1, "\n");

		// ---------------------------------------------------------------------
		// グラフ部分作成( 足切<25>はチャートの中でしている)
		// ---------------------------------------------------------------------
		// ライブラリを新しいものに差し替えた後、これをやらないと文字化けを起こす（デフォルトフォントが日本語に対応していない？）
		// ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());
		// ChartFactory.setChartTheme(StandardChartTheme.createJFreeTheme());
		// ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		// org.jfree.chart.ChartTheme teheme =ChartFactory.getChartTheme();
		StandardChartTheme theme = null;
		String themeName = kyPkg.globals.GlobalCtrl.getValue(OptionPanel.THEME);
		if (themeName.equals("DarknessTheme")) {
			theme = (StandardChartTheme) StandardChartTheme
					.createDarknessTheme();
		} else if (themeName.equals("LegacyTheme")) {
			theme = (StandardChartTheme) StandardChartTheme.createLegacyTheme();
		} else {
			theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
		}

		String fontFace = "Monospaced";// "MSGothic";
		theme.setExtraLargeFont(new Font(fontFace, Font.BOLD, 20));
		theme.setLargeFont(new Font(fontFace, Font.BOLD, 14));
		theme.setRegularFont(new Font(fontFace, Font.PLAIN, 10));
		theme.setSmallFont(new Font(fontFace, Font.PLAIN, 8));
		ChartFactory.setChartTheme(theme);

		if (!kyPkg.globals.GlobalCtrl.isNotBlank(OptionPanel.EXPERIMENTAL)) {
			if (name.indexOf(TRAN_YMD) >= 0) {
				tabbedPane.add(NINNZUU + "(推移)", new TimeSerialChart(NINNZUU
						+ suffix, sumList1, 1, is3D).getPanel());
			}
			if (name.indexOf(TRAN_HOUR) >= 0) {
				tabbedPane.add(NINNZUU + "(構成)", new PopulationChart(sumList1,
						name, "時間", NINNZUU + suffix, is3D).getPanel());
			}
			if (name.indexOf("年代") >= 0 || name.indexOf("年齢") >= 0) {
				tabbedPane.add(NINNZUU + "(構成)", new PopulationChart(sumList1,
						name, "年代", NINNZUU + suffix, is3D).getPanel());
			}
		}
		// ---------------------------------------------------------------------
		// 円グラフ
		// ---------------------------------------------------------------------
		int pos = 1;
		tabbedPane.add(NINNZUU, new PieCharts(name + NINNZUU + suffix,
				sumList1, pos, is3D).getPanel());
		for (String element : header) {
			pos++;
			tabbedPane.add(element, new PieCharts(name + element + suffix,
					sumList1, pos, is3D).getPanel());
		}

		// tabbedPane.add(KINNGAKU, new PieCharts(name + KINNGAKU + suffix,
		// sumList1, 2, is3D).getPanel());
		// tabbedPane.add(SUURYOU, new PieCharts(name + SUURYOU + suffix,
		// sumList1, 3, is3D).getPanel());
		// ---------------------------------------------------------------------
		// 限定条件パネル
		// ---------------------------------------------------------------------
		JPanel pnlLimiter = new LimiterPanel(uqList, gridPanel);
		tabbedPane.add(LIMITS, new JScrollPane(pnlLimiter));
		// ---------------------------------------------------------------------
		// コピーペースト用　（テキストデータパネル）
		// ---------------------------------------------------------------------
		JTextArea txtArea = new JTextArea(dump);
		txtArea.setForeground(MyColor.purple);
		JScrollPane dataTextPane = new JScrollPane(txtArea);
		dataTextPane.setForeground(MyColor.lime);
		dataTextPane.setPreferredSize(new Dimension(700, 500));
		tabbedPane.add(DATA, dataTextPane);
		// ---------------------------------------------------------------------
		// tabbedPane.add(OPTION, new OptionPanel());
		// ---------------------------------------------------------------------
		tabbedPane.setSize(900, 680);
		new DialogObj().showDialog(tabbedPane, SUMMERRIZE);
	}

	private static List<String> convert2SampleBase(List<String> sumList,
			String delimiter, String sampleCount) {
		if (sampleCount.equals(""))
			return sumList;
		// System.out.println("monitorCount:" + sampleCount);
		int sample = Integer.parseInt(sampleCount);
		StringBuffer buf = new StringBuffer();
		String key = "";
		Double value;
		List<String> resultList = new ArrayList();
		for (String var : sumList) {
			String[] array = var.split(delimiter);
			buf.delete(0, buf.length());
			key = array[0];
			buf.append(key);
			// System.out.println("key:" + key);
			for (int index = 1; index < array.length; index++) {
				// System.out.println(" pre_value:" +
				// Double.parseDouble(array[index]));
				value = Double.parseDouble(array[index]) * 100 / sample;
				// System.out.println(" aft_value:" + value);
				buf.append(delimiter);
				buf.append(String.valueOf(value));
			}
			resultList.add(buf.toString());
		}
		return resultList;
	}

}
