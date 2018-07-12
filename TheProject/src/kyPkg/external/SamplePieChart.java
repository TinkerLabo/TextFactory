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
	 * PieChart�̍쐬�Ǝg�p.
	 */
	public void workPieChart() {
		// �܂�PieChart���쐬����.
		JFreeChart pieChart = getPieChart();

		// �쐬����PieChart��Panel���쐬.
		ChartPanel cPanel = new ChartPanel(pieChart);
		content.add(cPanel);

		// �쐬����PieChart��PNG�t�@�C�����쐬.
		saveAsPng("c:/piechart.png", pieChart);
	}

	// -------------------------------------------------------------------------
	// �쐬����chart��PNG�t�@�C���Ƃ��ĕۑ�����
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
	 * JFreeChart�I�u�W�F�N�g�̍쐬�Ƒ���
	 */
	public JFreeChart getPieChart() {

		// �~�O���t�̃f�[�^
		double[][] data = new double[][] { { 1.0, 2.0, 3.0, 4.0 },
				{ 5.0, 6.0, 7.0, 8.0 } };

		// CategoryDataset�I�u�W�F�N�g�̍쐬
		CategoryDataset cData = DatasetUtilities.createCategoryDataset(
				"RowKey", "ColKey", data);

		// CategoryDataset�I�u�W�F�N�g���~�O���t������PieDataset�I�u�W�F�N�g�ɕϊ�
		PieDataset pieData = DatasetUtilities.createPieDatasetForRow(cData, 0);
		// PieDataset pieData =
		// DatasetUtilities.createPieDatasetForColumn(cData, 0);

		// PieDataset���f�[�^�ɂ���JFreeChart���쐬
		JFreeChart pieChart = ChartFactory.createPieChart("SamplePieChart",
				pieData, true, true, true);

		// PiePlot�I�u�W�F�N�g�����o���ĕ`��f�[�^�𑀍삷��
		PiePlot piePlot = (PiePlot) pieChart.getPlot();

		// �ȉ~��������
		piePlot.setCircular(false);

		// ���a�̎w��
		// piePlot.setRadius(0.6);

		// �~�O���t���狭�����镔�����w�肷��
		// piePlot.setExplodePercent(1, 0.5);

		// �\�̐����ʒu�̐ݒ�
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
