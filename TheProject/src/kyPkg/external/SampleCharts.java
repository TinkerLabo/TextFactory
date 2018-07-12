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
import org.jfree.data.time.*; // ���n��v
import org.jfree.data.gantt.*; // �K���g�`���[�g�v

import org.jfree.data.xy.DefaultHighLowDataset; //�낤����
import org.jfree.data.xy.OHLCDataset; //�낤����
import org.jfree.date.DateUtilities; //�낤����

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
 * jfreechart�T���v��
 */
public class SampleCharts {
	// -------------------------------------------------------------------------
	/** ���E�\�N���O���t */
	JFreeChart getCandlestickChart() {
		// �i1�j�f�[�^�Z�b�g�̍쐬
		// ���t
		Date[] date = new Date[5];
		// ���l
		double[] high = new double[5];
		// ���l
		double[] low = new double[5];
		// �n�l
		double[] open = new double[5];
		// �I�l
		double[] close = new double[5];
		// �{�����[��
		// �ݒ肵���{�����[���̍ő�l��100�Ƃ��A�ő�l�ɑ΂��銄���Ŗ_�O���t���\�������
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

		// �i2�jJFreeChart�I�u�W�F�N�g�̐���
		JFreeChart chart = ChartFactory.createCandlestickChart(
				"Sample Candlestick Chart", "Time", "Value", dataset, true);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** ���n��O���t */
	JFreeChart getTimeSeriesChart() {
		// �i1�j�f�[�^�Z�b�g�̍쐬
//		TimeSeries s1 = new TimeSeries("2004�N�x����", Month.class);
		TimeSeries s1 = new TimeSeries("2004�N�x����");
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

		// �i2�jJFreeChart�I�u�W�F�N�g�̐���
		JFreeChart chart = ChartFactory.createTimeSeriesChart("���ʔ���W�v", "�N��",
				"���z(�P�� �疜�~)", dataset, true, true, false);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** �K���g�`���[�g */
	JFreeChart getGanttChart() {
		TaskSeries s1 = new TaskSeries("�\��");
		// 4/1 ? 4/30
		s1
				.add(new Task("�݌v", new SimpleTimePeriod(date(1,
						Calendar.APRIL, 2004), date(30, Calendar.APRIL, 2004))));
		// 5/1 ? 7/31
		s1.add(new Task("�J��", new SimpleTimePeriod(date(1, Calendar.MAY, 2004),
				date(31, Calendar.JULY, 2004))));
		// 8/1 ? 9/30
		s1.add(new Task("�e�X�g", new SimpleTimePeriod(date(1, Calendar.AUGUST,
				2004), date(30, Calendar.SEPTEMBER, 2004))));

		TaskSeries s2 = new TaskSeries("����");
		// 4/1 ? 4/20
		s2
				.add(new Task("�݌v", new SimpleTimePeriod(date(1,
						Calendar.APRIL, 2004), date(20, Calendar.APRIL, 2004))));
		// 4/21 ? 7/31
		s2.add(new Task("�J��", new SimpleTimePeriod(date(21, Calendar.APRIL,
				2004), date(31, Calendar.JULY, 2004))));
		// 8/1 ? 9/15
		s2.add(new Task("�e�X�g", new SimpleTimePeriod(date(1, Calendar.AUGUST,
				2004), date(15, Calendar.SEPTEMBER, 2004))));

		TaskSeriesCollection taskSeriesCollec = new TaskSeriesCollection();
		taskSeriesCollec.add(s1);
		taskSeriesCollec.add(s2);

		// �i2�jJFreeChart�I�u�W�F�N�g�̐���
		JFreeChart chart = ChartFactory.createGanttChart("Sample Gantt Chart",
				"�t�F�[�Y", "���t", taskSeriesCollec, true, true, false);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** �~�O���t */
	JFreeChart getPieChart3D() {
		// �i1�j�f�[�^�Z�b�g�̍쐬
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Category 1", 50);
		data.setValue("Category 2", 40);
		data.setValue("Category 3", 30);
		data.setValue("Category 4", 20);
		data.setValue("Category 5", 10);
		data.setValue("Category 6", 5);

		// �i2�jJFreeChart�I�u�W�F�N�g�̐���

		// JFreeChart chart = ChartFactory.createPieChart("Sample Pie Chart",
		JFreeChart chart = ChartFactory.createPieChart3D("Sample Pie Chart",
				data, true, true, false);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** �_�O���t */
	JFreeChart getBarChart3D() {
		// �i1�j�f�[�^�Z�b�g�̍쐬
		String series1 = "First";
		String series2 = "Second";
		String series3 = "Third";

		// �J�e�S���[�̐ݒ�
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

		// �i2�jJFreeChart�I�u�W�F�N�g�̐���
		JFreeChart chart = ChartFactory.createBarChart3D("Sample Bar Chart",
				"Category", "Value", dataset, PlotOrientation.VERTICAL, true,
				true, false);

		// JFreeChart chart = ChartFactory.createBarChart("Sample Bar Chart",
		// "Category", "Value", dataset, PlotOrientation.VERTICAL, true,true,
		// false);
		return chart;
	}

	// -------------------------------------------------------------------------
	/** �܂�� */
	JFreeChart getLineChart3D() {
		// �i1�j�f�[�^�Z�b�g�̍쐬
		String series1 = "First";
		String series2 = "Second";
		String series3 = "Third";
		// �J�e�S���[�̐ݒ�
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

		// �i2�jJFreeChart�I�u�W�F�N�g�̐���

		// JFreeChart chart = ChartFactory.createLineChart("Sample Line Chart",
		// "Category", "Value", dataset, PlotOrientation.VERTICAL, true,true,
		// false);
		JFreeChart chart = ChartFactory.createLineChart3D("Sample Line Chart",
				"Category", "Value", dataset, PlotOrientation.VERTICAL, true,
				true, false);

		return chart;
	}

	// -------------------------------------------------------------------------
	/** �G���A�O���t */
	JFreeChart getAreaChart() {
		// �i1�j�f�[�^�Z�b�g�̍쐬
		String series1 = "First";
		String series2 = "Second";
		String series3 = "Third";
		// �J�e�S���[�̐ݒ�
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

		// �i2�jJFreeChart�I�u�W�F�N�g�̐���
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

		wJtp.add("�~�O���t  ", new ChartPanel(wChart1));
		wJtp.add("�_�O���t  ", new ChartPanel(wChart2));
		wJtp.add("�܂��    ", new ChartPanel(wChart3));
		wJtp.add("�̈�      ", new ChartPanel(wChart4));
		wJtp.add("���n��    ", new ChartPanel(wChart5));
		wJtp.add("�K���g    ", new ChartPanel(wChart6));
		wJtp.add("���E�\�N��", new ChartPanel(wChart7));

		wCt.add(wJtp);
		wJf.setSize(640, 480);
		wJf.setVisible(true);
		wJf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	// date for �K���g�`���[�g�p
	private static Date date(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar.getTime();
	}
}
