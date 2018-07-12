package kyPkg.external;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.*;
import org.jfree.data.category.*;

public class SamplePieChart extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel content;

	public SamplePieChart() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		content = new JPanel(new BorderLayout());
		setContentPane(content);
	}

	/**
	 * PieChartの作成と使用.
	 */
	public void workPieChart() {
		// まずPieChartを作成する.
		JFreeChart pieChart = getPieChart();

		// 作成したPieChartでPanelを作成.
		ChartPanel cPanel = new ChartPanel(pieChart);
		content.add(cPanel);

		// 作成したPieChartでPNGファイルを作成.
		saveAsPng("c:/piechart.png", pieChart);
	}

	// -------------------------------------------------------------------------
	// 作成したchartをPNGファイルとして保存する
	// ex : saveAsPng("c:/piechart.png", chart);
	// -------------------------------------------------------------------------
	public void saveAsPng(String path, JFreeChart chart) {
		try {
			File outFile = new File(path);
			ChartUtilities.saveChartAsPNG(outFile, chart, 500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * JFreeChartオブジェクトの作成と操作
	 */
	public JFreeChart getPieChart() {

		// 円グラフのデータ
		double[][] data = new double[][] { { 1.0, 2.0, 3.0, 4.0 },
				{ 5.0, 6.0, 7.0, 8.0 } };

		// CategoryDatasetオブジェクトの作成
		CategoryDataset cData = DatasetUtilities.createCategoryDataset(
				"RowKey", "ColKey", data);

		// CategoryDatasetオブジェクトを円グラフ向けのPieDatasetオブジェクトに変換
		PieDataset pieData = DatasetUtilities.createPieDatasetForRow(cData, 0);
		// PieDataset pieData =
		// DatasetUtilities.createPieDatasetForColumn(cData, 0);

		// PieDatasetをデータにしてJFreeChartを作成
		JFreeChart pieChart = ChartFactory.createPieChart("SamplePieChart",
				pieData, true, true, true);

		// PiePlotオブジェクトを取り出して描画データを操作する
		PiePlot piePlot = (PiePlot) pieChart.getPlot();

		// 楕円を許可する
		piePlot.setCircular(false);

		// 半径の指定
		// piePlot.setRadius(0.6);

		// 円グラフから強調する部分を指定する
		// piePlot.setExplodePercent(1, 0.5);

		// 表の説明位置の設定
		// Legend legend = pieChart.getLegend();
		// legend.setAnchor (Legend.EAST);
		return pieChart;
	}

	public static void main(String args[]) {
		SamplePieChart spc = new SamplePieChart();
		spc.pack();
		spc.workPieChart();
		// RefineryUtilities.centerFrameOnScreen(spc);
		spc.setSize(500, 500);
		spc.setVisible(true);
	}
}
