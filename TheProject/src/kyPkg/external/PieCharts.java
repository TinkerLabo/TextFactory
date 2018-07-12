package kyPkg.external;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import org.jfree.chart.*; //import org.jfree.chart.axis.CategoryAxis;
//import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.*;

// -------------------------------------------------------------------------
// 本家=> http://www.jfree.org/jfreechart/
// API=> http://www.jfree.org/jfreechart/api/javadoc/index.html
// -------------------------------------------------------------------------
public class PieCharts {
	private JFreeChart chart;
	private static boolean LEGEND = true;
	private static boolean TOOLTIPS = true;
	private static boolean URLS = false;
	private static int limit = 25;

	public static void setLimit(int limit) {
		PieCharts.limit = limit;
	}

	public ChartPanel getPanel() {
		return new ChartPanel(chart);
	}

	public static void main(String[] args) {
		tester();
	}

	public static void tester() {
		List<String> fList = new ArrayList();
		fList.add("アサヒ\t50");
		fList.add("キリン\t40");
		fList.add("サッポロ\t30");
		fList.add("サントリー\t20");
		fList.add("恵比寿\t10");
		fList.add("ホッピィ\t5");

		PieCharts chartIns1 = new PieCharts("円グラフ　その壱", fList, 1);
		PieCharts chartIns2 = new PieCharts("円グラフ　その弐", fList, 1, "3D");

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("円グラフ  ", chartIns1.getPanel());
		tabbedPane.add("3D円グラフ  ", chartIns2.getPanel());

		JFrame wJf = new JFrame();
		Container wCt = wJf.getContentPane();
		wCt.add(tabbedPane);
		wJf.setSize(640, 480);
		wJf.setVisible(true);
		wJf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * 円グラフ
	 * 
	 * @param targetCol
	 */
	// ------------------------------------------------------------------------
	// List→円グラフのデータに変換
	// ------------------------------------------------------------------------
	private static PieDataset list2Dataset(List<String> sumList,
			String delimiter, int targetCol) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		int cnt = 0;
		double other = 0;
		for (String var : sumList) {
			String[] array = var.split(delimiter);
			if (array.length >= 2) {
				if (cnt++ < limit) {
					dataset.setValue(array[0],
							Double.parseDouble(array[targetCol]));
				} else {
					other = other + Double.parseDouble(array[targetCol]);
				}
			}
			if (cnt > limit) {
				dataset.setValue("other", other);
			}
		}
		return dataset;
	}

	private static PieDataset matrix2Dataset(List<List<String>> matrix) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		int cnt = 0;
		double other = 0;
		for (List<String> list : matrix) {
			if (list.size() >= 2) {
				if (cnt++ < limit) {
					dataset.setValue(list.get(0),
							Double.parseDouble(list.get(1)));
				} else {
					other = other + Double.parseDouble(list.get(1));
				}
			}
			if (cnt > limit) {
				dataset.setValue("other", other);
			}
		}
		return dataset;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public PieCharts(String title, List<String> fList, int targetCol) {
		this(title, fList, targetCol, "");
	}

	public PieCharts(String title, List<String> fList, int targetCol,
			String type) {
		super();
		// type="3D";
		// ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());
		PieDataset dataset = list2Dataset(fList, "\t", targetCol);
		if (!type.equals("3D")) {
			chart = ChartFactory.createPieChart(title, dataset, LEGEND,
					TOOLTIPS, URLS);
			PiePlot plot3 = (PiePlot) chart.getPlot();
			plot3.setForegroundAlpha(0.5f);

		} else {
			chart = ChartFactory.createPieChart3D(title, dataset, LEGEND,
					TOOLTIPS, URLS);
			PiePlot3D plot3 = (PiePlot3D) chart.getPlot();
			// plot3.setForegroundAlpha(0.7f);
			plot3.setForegroundAlpha(0.5f);
			plot3.setCircular(true);
		}
		// PiePlot plot = (PiePlot) chart.getPlot();
		//
		// plot.setOutlineVisible(false);

		// 背景色の設定
		// chart.setBackgroundPaint(ChartColor.WHITE);

		PiePlot plot = (PiePlot) chart.getPlot();

		// 外枠の設定
		// plot.setOutlineVisible(false);

		// 透明度の設定
		plot.setForegroundAlpha(0.5f);
		// plot.setBackgroundAlpha(0.5f);

		// 影の設定
		plot.setShadowPaint(null);

		// ラベルの設定
		plot.setSimpleLabels(false);
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={2}"));
		// plot.setLabelGenerator(new
		// StandardPieSectionLabelGenerator("{0}={2}({1})"));
		// plot.setLabelBackgroundPaint(null);
		plot.setLabelOutlineStroke(null);
		// plot.setLabelShadowPaint(null);

		// 取り出し表示の設定
		// plot.setExplodePercent(dataset.getKey(dataset.getItemCount() - 1),
		// 0.1);

		int cnt = dataset.getItemCount();

		List<java.awt.Color> colorList = ColorControl.getColorList();


		// カテゴリの設定
		for (int i = 0; i < colorList.size(); i++) {
			if (dataset.getItemCount() > i)
				plot.setSectionPaint(dataset.getKey(i), colorList.get(i));
		}

		for (Iterator iter = dataset.getKeys().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			plot.setSectionOutlinePaint(key, ChartColor.WHITE);
			// plot.setSectionOutlineStroke(key, new BasicStroke(5));
		}

		// chart.getTitle().setFont(new Font("MSGothic", Font.PLAIN, 28));
		//
		// chart.getLegend().setItemFont(new Font("MSGothic", Font.PLAIN, 9));
		//
		// CategoryPlot plot = chart.getCategoryPlot();
		//
		// CategoryAxis cAxis = plot.getDomainAxis();
		// cAxis.setLabelFont(new Font("MSGothic", Font.PLAIN, 12));
		// cAxis.setTickLabelFont(new Font("MSGothic", Font.PLAIN, 9));
		//
		// ValueAxis vAxis = plot.getRangeAxis();
		// vAxis.setLabelFont(new Font("MSGothic", Font.PLAIN, 12));
		// vAxis.setTickLabelFont(new Font("MSGothic", Font.PLAIN, 9));

	}

	public PieCharts(String title, List<List<String>> matrix, String type,
			boolean debug) {
		super();
		// type="3D";
		PieDataset dataset = matrix2Dataset(matrix);
		if (!type.equals("3D")) {
			chart = ChartFactory.createPieChart(title, dataset, LEGEND,
					TOOLTIPS, URLS);
		} else {
			chart = ChartFactory.createPieChart3D(title, dataset, LEGEND,
					TOOLTIPS, false);
		}
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

	// ------------------------------------------------------------------------
	// // 円グラフのデータ #1
	// //
	// -------------------------------------------------------------------------
	// private static PieDataset getSampleDataset1() {
	// DefaultPieDataset dataset = new DefaultPieDataset();
	// dataset.setValue("アサヒ", 50);
	// dataset.setValue("キリン", 40);
	// dataset.setValue("サッポロ", 30);
	// dataset.setValue("サントリー", 20);
	// dataset.setValue("恵比寿", 10);
	// dataset.setValue("ホッピィ", 5);
	// return dataset;
	// }
	// ------------------------------------------------------------------------
	// // 円グラフのデータ #2
	// // 指標の名称はどうやって指定するんだかよくわからない
	// ------------------------------------------------------------------------
	// private static PieDataset getSampleDataset2() {
	// double[][] dMatrix = new double[][] { { 11.0, 22.0, 33.0, 44.0 },
	// { 5.0, 6.0, 7.0, 8.0 } };
	//
	// // CategoryDatasetオブジェクトの作成
	// CategoryDataset catData = DatasetUtilities.createCategoryDataset(
	// "RowKey", "ColKey", dMatrix);
	//
	// // PieDatasetオブジェクトに変換
	// PieDataset dataset = DatasetUtilities
	// .createPieDatasetForRow(catData, 0);
	// // PieDataset pieData =
	// // DatasetUtilities.createPieDatasetForColumn(cData, 0);
	// return dataset;
	// }

	// ------------------------------------------------------------------------
	// public static JFreeChart createPieChart(java.lang.String title,
	// PieDataset dataset,
	// boolean legend,
	// boolean tooltips,
	// boolean urls)
	// Creates a pie chart with default settings.
	// The chart object returned by this method uses a PiePlot instance as
	// the plot.
	// Parameters:
	// title - the chart title (null permitted).
	// dataset - the dataset for the chart (null permitted).
	// legend - a flag specifying whether or not a legend is required.
	// tooltips - configure chart to generate tool tips?
	// urls - configure chart to generate URLs?
	// ------------------------------------------------------------------------
	// JFreeChart getPieChart_old(String title, PieDataset dataset) {
	// boolean legend = false;
	// boolean tooltips = false;
	// boolean urls = false;
	// JFreeChart pieChart = ChartFactory.createPieChart(title, dataset,
	// legend, tooltips, urls);
	//
	// // PiePlotオブジェクトを取り出して描画データを操作する
	// PiePlot piePlot = (PiePlot) pieChart.getPlot();
	// // 楕円を許可する
	// piePlot.setCircular(true);
	// // 半径の指定
	// //
	// ------------------------------------------------------------------------
	// // piePlot.setRadius(0.6);
	// // 円グラフから強調する部分を指定する
	// int seq = 0;// 何番目の指標か（この場合アサヒ）
	// double flowting = 0.2;// どの程度浮かせるか
	// piePlot.setExplodePercent(seq, flowting);
	//
	// // 表の説明位置の設定
	// // Legend legend = pieChart.getLegend();
	// // legend.setAnchor(Legend.EAST);
	//
	// return pieChart;
	// }

}
