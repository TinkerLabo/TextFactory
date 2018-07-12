package kyPkg.external;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.jfree.chart.*;
//import org.jfree.chart.plot.PiePlot;
//import org.jfree.chart.plot.PiePlot3D;
//import org.jfree.data.general.*;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

// -------------------------------------------------------------------------
// 本家=> http://www.jfree.org/jfreechart/
// API=> http://www.jfree.org/jfreechart/api/javadoc/index.html
// -------------------------------------------------------------------------
public class TimeSerialChart {
	private static final String title = "対象データ";
	private static final String TITLE = title;
	private JFreeChart chart;

	public ChartPanel getPanel() {
		return new ChartPanel(chart);
	}

	// Javaのグラフ描画用ライブラリJFreeChartで、チャートを表示するChartPanel上でのコンテキストメニューを日本語化する方法。
	//
	// org.jfree.chart.LocalozationBandle_ja.properties を作成し、プロパティの値を日本語で記述する。
	// 「ja」の部分はLocale名。標準では日本語リソースが提供されていないが、規定の位置にローカライズしたリソースをおけば自動的に読み込まれる。

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public TimeSerialChart(String title, List<String> fList, int targetCol) {
		this(title, fList, targetCol, "");
	}

	private static TimeSeriesCollection list2Dataset(List<String> sumList,
			String delimiter, int targetCol) {
		HashMap<String, Double> sumMap = new HashMap();
		// for (String key : typList) {
		// sumMap.put(key, new Double(0));
		// }
		int cnt = 0;
		double other = 0;
		for (String var : sumList) {
			String[] array = var.split(delimiter);
			// System.out.println("debug20130227 var:"+var);
			// System.out.println("array.length:"+array.length);
			if (array.length > targetCol) {
				String key = array[0].substring(0, 8);
				Double sumVal = sumMap.get(key);
				Double modVal = Double.parseDouble(array[targetCol]);
//				System.out.println("debug20130227 key:" + key + " modval:"
//						+ modVal);
				if (sumVal == null) {
					sumVal = new Double(modVal);
				} else {
					sumVal = new Double(sumVal + modVal);
				}
				sumMap.put(key, sumVal);
			}
		}
		List<String> keyList = new ArrayList(sumMap.keySet());
		Collections.sort(keyList);
//		TimeSeries series = new TimeSeries(TITLE, Day.class);
		TimeSeries series = new TimeSeries(TITLE);

		for (String element : keyList) {
			int year = Integer.parseInt(element.substring(0, 4));
			int month = Integer.parseInt(element.substring(4, 6).trim());
			int day = Integer.parseInt(element.substring(6).trim());
			System.out.println("@TimeSerialChart element:" + element + "#year#:" + year
					+ "#month#:" + month + "#day#:" + day + " val:"
					+ sumMap.get(element));
			series.add(new Day(day, month, year), sumMap.get(element));
			// series.add(new Month(month, year), sumMap.get(element));
			// series.add(new Day(new
			// Date("2010/9/1"),TimeZone.getDefault(),Locale.JAPANESE), 27.4)
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}

	public TimeSeriesCollection testData() {
		// （1）データセットの作成
//		TimeSeries s1 = new TimeSeries(TITLE, Month.class);
		TimeSeries s1 = new TimeSeries(TITLE);
		s1.add(new Month(2, 2004), 104.8);
		s1.add(new Month(3, 2004), 103.3);
		s1.add(new Month(4, 2004), 105.8);
		s1.add(new Month(5, 2004), 110.6);
		s1.add(new Month(6, 2004), 120.8);
		s1.add(new Month(7, 2004), 115.3);
		s1.add(new Month(8, 2004), 130.9);
		s1.add(new Month(9, 2004), 131.7);
		s1.add(new Month(10, 2004), 140.2);
		s1.add(new Month(11, 2004), 141.8);
		s1.add(new Month(12, 2004), 160.6);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(s1);
		return dataset;
	}

	public TimeSerialChart(String title, List<String> fList, int targetCol,
			String type) {
		super();
		// ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());

		TimeSeriesCollection dataset = list2Dataset(fList, "\t", 1);

		// （2）JFreeChartオブジェクトの生成
		String xName = "day-month";
		String yName = title;
		chart = ChartFactory.createTimeSeriesChart("期間推移", xName, yName,
				dataset, true, true, false);

//		chart.getTitle().setFont(new Font("MSGothic", Font.PLAIN, 28));
//
//		chart.getLegend().setItemFont(new Font("MSGothic", Font.PLAIN, 9));
//
////		CategoryPlot plot = chart.getCategoryPlot();
//
//		CategoryAxis cAxis = chart.getCategoryPlot().getDomainAxis();
//		cAxis.setLabelFont(new Font("MSGothic", Font.PLAIN, 12));
//		cAxis.setTickLabelFont(new Font("MSGothic", Font.PLAIN, 9));
//
//		ValueAxis vAxis = chart.getCategoryPlot().getRangeAxis();
//		vAxis.setLabelFont(new Font("MSGothic", Font.PLAIN, 12));
//		vAxis.setTickLabelFont(new Font("MSGothic", Font.PLAIN, 9));

		
	}

	// -------------------------------------------------------------------------
	// 作成したchartをPNGファイルとして保存する
	// ex : saveAsPng("c:/piechart.png", chart);
	// -------------------------------------------------------------------------
	public void saveAsPng(String path) {
		try {
			File outFile = new File(path);
			ChartUtilities.saveChartAsPNG(outFile, chart, 500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
