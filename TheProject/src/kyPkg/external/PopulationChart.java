package kyPkg.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
//import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.general.KeyedValues2DDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PopulationChart {
	// String population = "(�l��)";
	private static final String MALE = "�j��";
	private static final String FEMALE = "����";
	private JFreeChart chart;
	private KeyedValues2DDataset dataset;

	public PopulationChart(List<String> sumList, String name, String groupName,
			String population,String type) {
		
		List<String> typList = whichType(name);
		if (sumList == null) {
			dataset = createSampleDataset();
		} else {
			dataset = list2Dataset(typList, sumList, "\t", 1);
		}
		if(type.equals("3D")){
			chart = ChartFactory.createStackedBarChart3D(name, groupName,
					population, dataset, PlotOrientation.HORIZONTAL, true, true,
					false);
			
		}else{
			chart = ChartFactory.createStackedBarChart(name, groupName,
					population, dataset, PlotOrientation.HORIZONTAL, true, true,
					false);
			
		}
		Plot plot3 = (Plot) chart.getPlot();
		plot3.setForegroundAlpha(0.5f);

//		chart = ChartFactory.createStackedBarChart3D(name, groupName,
//				population, dataset, PlotOrientation.HORIZONTAL, true, true,
//				false);
	}

	public ChartPanel getPanel() {
		return new ChartPanel(chart);
	}

	private List<String> whichType(String name) {
		List<String> list = new ArrayList();
		if (name.equals("���E�N��i�P�O�΋敪�j")) {
			list.add("���P�O��");
			list.add("���Q�O��");
			list.add("���R�O��");
			list.add("���S�O��");
			list.add("���T�O��");
			list.add("���U�O��");
			list.add("�j�P�O��");
			list.add("�j�Q�O��");
			list.add("�j�R�O��");
			list.add("�j�S�O��");
			list.add("�j�T�O��");
			list.add("�j�U�O��");

		} else if (name.equals("���E�N��i�T�΋敪�j")) {
			list.add("���P�T�`�P�X��");
			list.add("���Q�O�`�Q�S��");
			list.add("���Q�T�`�Q�X��");
			list.add("���R�O�`�R�S��");
			list.add("���R�T�`�R�X��");
			list.add("���S�O�`�S�S��");
			list.add("���S�T�`�S�X��");
			list.add("���T�O�`�T�S��");
			list.add("���T�T�`�T�X��");
			list.add("���U�O�`�U�S��");
			list.add("���U�T�`�U�X��");
			list.add("�j�P�T�`�P�X��");
			list.add("�j�Q�O�`�Q�S��");
			list.add("�j�Q�T�`�Q�X��");
			list.add("�j�R�O�`�R�S��");
			list.add("�j�R�T�`�R�X��");
			list.add("�j�S�O�`�S�S��");
			list.add("�j�S�T�`�S�X��");
			list.add("�j�T�O�`�T�S��");
			list.add("�j�T�T�`�T�X��");
			list.add("�j�U�O�`�U�S��");
			list.add("�j�U�T�`�U�X��");
		} else if (name.equals("���E�N��i���f�B�A�敪�j")) {
			list.add("���P�T�`�P�X��");
			list.add("���Q�O�`�R�S��");
			list.add("���R�T�`�S�X��");
			list.add("���T�O�ˈȏ�");
			list.add("�j�P�T�`�P�X��");
			list.add("�j�Q�O�`�R�S��");
			list.add("�j�R�T�`�S�X��");
			list.add("�j�T�O�ˈȏ�");
		} else if (name.equals("�N��i�P�O�΋敪�j")) {
			list.add("�P�O��");
			list.add("�Q�O��");
			list.add("�R�O��");
			list.add("�S�O��");
			list.add("�T�O��");
			list.add("�U�O��");
		} else if (name.equals("�N��i�T�΋敪�j")) {
			list.add("�P�T�`�P�X��");
			list.add("�Q�O�`�Q�S��");
			list.add("�Q�T�`�Q�X��");
			list.add("�R�O�`�R�S��");
			list.add("�R�T�`�R�X��");
			list.add("�S�O�`�S�S��");
			list.add("�S�T�`�S�X��");
			list.add("�T�O�`�T�S��");
			list.add("�T�T�`�T�X��");
			list.add("�U�O�`�U�S��");
			list.add("�U�T�`�U�X��");
		} else if (name.equals("�N��i���f�B�A�敪�j")) {
			list.add("�P�T�`�P�X��");
			list.add("�Q�O�`�R�S��");
			list.add("�R�T�`�S�X��");
			list.add("�T�O�ˈȏ�");
		} else if (name.equals("���ю�N��")) {
			list.add("��Y��");
			list.add("�`�Q�X��");
			list.add("�`�R�X��");
			list.add("�`�S�X��");
			list.add("�`�T�X��");
			list.add("�U�O�ˁ`");
		} else if (name.equals("���ѓ����c��w�N��")) {
			list.add("��Y��");
			list.add("�`�Q�X��");
			list.add("�`�R�X��");
			list.add("�`�S�X��");
			list.add("�`�T�X��");
			list.add("�U�O�ˁ`");
		} else if (name.equals("�N�x�N��")) {
			list.add("�P�Q�˖���");
			list.add("�P�Q�`�P�X��");
			list.add("�Q�O�`�Q�S��");
			list.add("�Q�T�`�Q�X��");
			list.add("�R�O�`�R�S��");
			list.add("�R�T�`�R�X��");
			list.add("�S�O�`�S�S��");
			list.add("�S�T�`�S�X��");
			list.add("�T�O�`�T�S��");
			list.add("�T�T�`�T�X��");
			list.add("�U�O�ˈȏ�");
		} else if (name.equals("�w������")) {
			list.add("6");
			list.add("7");
			list.add("8");
			list.add("9");
			list.add("10");
			list.add("11");
			list.add("12");
			list.add("13");
			list.add("14");
			list.add("15");
			list.add("16");
			list.add("17");
			list.add("18");
			list.add("19");
			list.add("20");
			list.add("21");
			list.add("22");
			list.add("23");
			list.add("0");
			list.add("1");
			list.add("2");
			list.add("3");
			list.add("4");
			list.add("5");
		}
		return list;
	}

	private static DefaultKeyedValues2DDataset list2Dataset(
			List<String> typList, List<String> sumList, String delimiter,
			int targetCol) {
		HashMap<String, Double> sumMap = new HashMap();
		for (String key : typList) {
			sumMap.put(key, new Double(0));
		}

		int cnt = 0;
		double other = 0;
		for (String var : sumList) {
			String[] array = var.split(delimiter);
			// System.out.println("debug20130227 var:"+var);
			// System.out.println("array.length:"+array.length);
			if (array.length > targetCol) {
				String key = array[0];
				Double sumVal = sumMap.get(key);
				Double modVal = Double.parseDouble(array[targetCol]);
				// System.out.println("debug20130227 key:"+key+" modval:"+modVal);
				if (sumVal == null) {
					sumVal = modVal;
				} else {
					sumVal = new Double(sumVal + modVal);
				}
				sumMap.put(key, sumVal);
			}
		}
		DefaultKeyedValues2DDataset dataset = new DefaultKeyedValues2DDataset();
		for (String key : typList) {
			Double sumVal = sumMap.get(key);
			// System.out.println("debug20130227 key:"+key+" val:"+sumVal);
			if (key.indexOf("�j") >= 0) {
				dataset.addValue(sumVal, MALE, key.substring(1));
			} else if (key.indexOf("��") >= 0) {
				dataset.addValue(sumVal * (-1), FEMALE, key.substring(1));
			} else {
				dataset.addValue(sumVal, "�w����", key);
			}
		}
		return dataset;
	}

	private KeyedValues2DDataset createSampleDataset() {
		// add���鏇�ԂɃv���b�g�����̂ŋ�l���K�v
		DefaultKeyedValues2DDataset dataset = new DefaultKeyedValues2DDataset();
		dataset.addValue(-6D, FEMALE, "70+");
		dataset.addValue(-8D, FEMALE, "60-69");
		dataset.addValue(-11D, FEMALE, "50-59");
		// dataset.addValue(-13D, FEMALE, "40-49");
		dataset.addValue(0D, FEMALE, "40-49");
		dataset.addValue(-14D, FEMALE, "30-39");
		dataset.addValue(-15D, FEMALE, "20-29");
		dataset.addValue(-19D, FEMALE, "10-19");
		dataset.addValue(-21D, FEMALE, "0-9");

		dataset.addValue(10D, MALE, "70+");
		dataset.addValue(12D, MALE, "60-69");
		dataset.addValue(13D, MALE, "50-59");
		dataset.addValue(14D, MALE, "40-49");
		dataset.addValue(15D, MALE, "30-39");
		dataset.addValue(17D, MALE, "20-29");
		dataset.addValue(19D, MALE, "10-19");
		dataset.addValue(20D, MALE, "0-9");
		return dataset;
	}

	public static void main(String args[]) {
		PopulationChart chartIns = new PopulationChart(null, "�N��ʍ\�� ", "�N��",
				"(�l��)","");

		ApplicationFrame frame = new ApplicationFrame("PopulationChart");
		frame.setContentPane(chartIns.getPanel());
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
}