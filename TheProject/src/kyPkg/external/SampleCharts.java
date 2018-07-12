package kyPkg.external;

import java.awt.*;
import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.*; // 時系列要
import org.jfree.data.gantt.*; // ガントチャート要

import org.jfree.data.xy.DefaultHighLowDataset; //ろうそく
import org.jfree.data.xy.OHLCDataset; //ろうそく
import org.jfree.date.DateUtilities; //ろうそく

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartFrame;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.ChartUtilities;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.data.time.Month;
//import org.jfree.data.time.TimeSeries;
//import org.jfree.data.time.TimeSeriesCollection;
//import org.jfree.data.gantt.Task;
//import org.jfree.data.gantt.TaskSeries;
//import org.jfree.data.gantt.TaskSeriesCollection;

/**
 * jfreechartサンプル
 */
public class SampleCharts {
	// -------------------------------------------------------------------------
	/** ロウソク足グラフ */
	JFreeChart getCandlestickChart() {
		// （1）データセットの作成
		// 日付
		Date[] date = new Date[5];
		// 高値
		double[] high = new double[5];
		// 安値
		double[] low = new double[5];
		// 始値
		double[] open = new double[5];
		// 終値
		double[] close = new double[5];
		// ボリューム
		// 設定したボリュームの最大値を100とし、最大値に対する割合で棒グラフが表示される
		double[] volume = new double[5];

		date[0] = DateUtilities.createDate(2004, 10, 1);
		high[0] = 85.0;
		low[0] = 55.0;
		open[0] = 60.0;
		close[0] = 80.0;
		volume[0] = 20.0;

		date[1] = DateUtilities.createDate(2004, 11, 1);
		high[1] = 100.0;
		low[1] = 650.0;
		open[1] = 80.0;
		close[1] = 90.0;
		volume[1] = 10.0;

		date[2] = DateUtilities.createDate(2004, 12, 1);
		high[2] = 115.0;
		low[2] = 85.0;
		open[2] = 90.0;
		close[2] = 105.0;
		volume[2] = 40.0;

		date[3] = DateUtilities.createDate(2005, 1, 1);
		high[3] = 110.0;
		low[3] = 100.0;
		open[3] = 105.0;
		close[3] = 110.0;
		volume[3] = 30.0;

		date[4] = DateUtilities.createDate(2005, 2, 1);
		high[4] = 110.0;
		low[4] = 85.0;
		open[4] = 110.0;
		close[4] = 90.0;
		volume[4] = 20.0;

		OHLCDataset dataset = new DefaultHighLowDataset("Series 1", date, high,
				low, open, close, volume);

		// （2）JFreeChartオブジェクトの生成
		JFreeChart chart = ChartFactory.createCandlestickChart(
				"Sample Candlestick Chart", "Time", "Value", dataset, true);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** 時系列グラフ */
	JFreeChart getTimeSeriesChart() {
		// （1）データセットの作成
//		TimeSeries s1 = new TimeSeries("2004年度売上", Month.class);
		TimeSeries s1 = new TimeSeries("2004年度売上");
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

		// （2）JFreeChartオブジェクトの生成
		JFreeChart chart = ChartFactory.createTimeSeriesChart("月別売上集計", "年月",
				"金額(単位 千万円)", dataset, true, true, false);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** ガントチャート */
	JFreeChart getGanttChart() {
		TaskSeries s1 = new TaskSeries("予定");
		// 4/1 ? 4/30
		s1
				.add(new Task("設計", new SimpleTimePeriod(date(1,
						Calendar.APRIL, 2004), date(30, Calendar.APRIL, 2004))));
		// 5/1 ? 7/31
		s1.add(new Task("開発", new SimpleTimePeriod(date(1, Calendar.MAY, 2004),
				date(31, Calendar.JULY, 2004))));
		// 8/1 ? 9/30
		s1.add(new Task("テスト", new SimpleTimePeriod(date(1, Calendar.AUGUST,
				2004), date(30, Calendar.SEPTEMBER, 2004))));

		TaskSeries s2 = new TaskSeries("実績");
		// 4/1 ? 4/20
		s2
				.add(new Task("設計", new SimpleTimePeriod(date(1,
						Calendar.APRIL, 2004), date(20, Calendar.APRIL, 2004))));
		// 4/21 ? 7/31
		s2.add(new Task("開発", new SimpleTimePeriod(date(21, Calendar.APRIL,
				2004), date(31, Calendar.JULY, 2004))));
		// 8/1 ? 9/15
		s2.add(new Task("テスト", new SimpleTimePeriod(date(1, Calendar.AUGUST,
				2004), date(15, Calendar.SEPTEMBER, 2004))));

		TaskSeriesCollection taskSeriesCollec = new TaskSeriesCollection();
		taskSeriesCollec.add(s1);
		taskSeriesCollec.add(s2);

		// （2）JFreeChartオブジェクトの生成
		JFreeChart chart = ChartFactory.createGanttChart("Sample Gantt Chart",
				"フェーズ", "日付", taskSeriesCollec, true, true, false);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** 円グラフ */
	JFreeChart getPieChart3D() {
		// （1）データセットの作成
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Category 1", 50);
		data.setValue("Category 2", 40);
		data.setValue("Category 3", 30);
		data.setValue("Category 4", 20);
		data.setValue("Category 5", 10);
		data.setValue("Category 6", 5);

		// （2）JFreeChartオブジェクトの生成

		// JFreeChart chart = ChartFactory.createPieChart("Sample Pie Chart",
		JFreeChart chart = ChartFactory.createPieChart3D("Sample Pie Chart",
				data, true, true, false);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** 棒グラフ */
	JFreeChart getBarChart3D() {
		// （1）データセットの作成
		String series1 = "First";
		String series2 = "Second";
		String series3 = "Third";

		// カテゴリーの設定
		String category1 = "Category 1";
		String category2 = "Category 2";
		String category3 = "Category 3";

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(1.0, series1, category1);
		dataset.addValue(4.0, series1, category2);
		dataset.addValue(5.0, series1, category3);

		dataset.addValue(5.0, series2, category1);
		dataset.addValue(7.0, series2, category2);
		dataset.addValue(7.0, series2, category3);

		dataset.addValue(6.0, series3, category1);
		dataset.addValue(8.0, series3, category2);
		dataset.addValue(8.0, series3, category3);

		// （2）JFreeChartオブジェクトの生成
		JFreeChart chart = ChartFactory.createBarChart3D("Sample Bar Chart",
				"Category", "Value", dataset, PlotOrientation.VERTICAL, true,
				true, false);

		// JFreeChart chart = ChartFactory.createBarChart("Sample Bar Chart",
		// "Category", "Value", dataset, PlotOrientation.VERTICAL, true,true,
		// false);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** 折れ線 */
	JFreeChart getLineChart3D() {
		// （1）データセットの作成
		String series1 = "First";
		String series2 = "Second";
		String series3 = "Third";
		// カテゴリーの設定
		String category1 = "Category 1";
		String category2 = "Category 2";
		String category3 = "Category 3";
		String category4 = "Category 4";
		String category5 = "Category 5";
		String category6 = "Category 6";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(3.0, series1, category1);
		dataset.addValue(2.0, series1, category2);
		dataset.addValue(4.0, series1, category3);
		dataset.addValue(4.0, series1, category4);
		dataset.addValue(4.0, series1, category5);
		dataset.addValue(6.0, series1, category6);

		dataset.addValue(5.0, series2, category1);
		dataset.addValue(3.0, series2, category2);
		dataset.addValue(6.0, series2, category3);
		dataset.addValue(5.0, series2, category4);
		dataset.addValue(6.0, series2, category5);
		dataset.addValue(5.0, series2, category6);

		dataset.addValue(6.0, series3, category1);
		dataset.addValue(7.0, series3, category2);
		dataset.addValue(7.0, series3, category3);
		dataset.addValue(6.0, series3, category4);
		dataset.addValue(5.0, series3, category5);
		dataset.addValue(7.0, series3, category6);

		// （2）JFreeChartオブジェクトの生成

		// JFreeChart chart = ChartFactory.createLineChart("Sample Line Chart",
		// "Category", "Value", dataset, PlotOrientation.VERTICAL, true,true,
		// false);
		JFreeChart chart = ChartFactory.createLineChart3D("Sample Line Chart",
				"Category", "Value", dataset, PlotOrientation.VERTICAL, true,
				true, false);

		return chart;
	}

	// -------------------------------------------------------------------------
	/** エリアグラフ */
	JFreeChart getAreaChart() {
		// （1）データセットの作成
		String series1 = "First";
		String series2 = "Second";
		String series3 = "Third";
		// カテゴリーの設定
		String category1 = "Category 1";
		String category2 = "Category 2";
		String category3 = "Category 3";
		String category4 = "Category 4";
		String category5 = "Category 5";
		String category6 = "Category 6";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(6.0, series1, category1);
		dataset.addValue(7.0, series1, category2);
		dataset.addValue(7.0, series1, category3);
		dataset.addValue(6.0, series1, category4);
		dataset.addValue(5.0, series1, category5);
		dataset.addValue(7.0, series1, category6);

		dataset.addValue(5.0, series2, category1);
		dataset.addValue(3.0, series2, category2);
		dataset.addValue(6.0, series2, category3);
		dataset.addValue(5.0, series2, category4);
		dataset.addValue(6.0, series2, category5);
		dataset.addValue(5.0, series2, category6);

		dataset.addValue(3.0, series3, category1);
		dataset.addValue(2.0, series3, category2);
		dataset.addValue(4.0, series3, category3);
		dataset.addValue(4.0, series3, category4);
		dataset.addValue(4.0, series3, category5);
		dataset.addValue(6.0, series3, category6);

		// （2）JFreeChartオブジェクトの生成
		JFreeChart chart = ChartFactory.createAreaChart("Sample Area Chart",
				"Category", "Value", dataset, PlotOrientation.VERTICAL, true,
				true, false);
		return chart;
	}

	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		SampleCharts wObj = new SampleCharts();
		JFrame wJf = new JFrame();
		Container wCt = wJf.getContentPane();
		JTabbedPane wJtp = new JTabbedPane();

		JFreeChart wChart1 = wObj.getPieChart3D();
		JFreeChart wChart2 = wObj.getBarChart3D();
		JFreeChart wChart3 = wObj.getLineChart3D();
		JFreeChart wChart4 = wObj.getAreaChart();
		JFreeChart wChart5 = wObj.getTimeSeriesChart();
		JFreeChart wChart6 = wObj.getGanttChart();
		JFreeChart wChart7 = wObj.getCandlestickChart();

		wJtp.add("円グラフ  ", new ChartPanel(wChart1));
		wJtp.add("棒グラフ  ", new ChartPanel(wChart2));
		wJtp.add("折れ線    ", new ChartPanel(wChart3));
		wJtp.add("領域      ", new ChartPanel(wChart4));
		wJtp.add("時系列    ", new ChartPanel(wChart5));
		wJtp.add("ガント    ", new ChartPanel(wChart6));
		wJtp.add("ロウソク足", new ChartPanel(wChart7));

		wCt.add(wJtp);
		wJf.setSize(640, 480);
		wJf.setVisible(true);
		wJf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	// date for ガントチャート用
	private static Date date(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar.getTime();
	}
}
