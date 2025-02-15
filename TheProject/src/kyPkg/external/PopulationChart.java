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
	// String population = "(人数)";
	private static final String MALE = "男性";
	private static final String FEMALE = "女性";
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
		if (name.equals("性・年代（１０歳区分）")) {
			list.add("女１０代");
			list.add("女２０代");
			list.add("女３０代");
			list.add("女４０代");
			list.add("女５０代");
			list.add("女６０代");
			list.add("男１０代");
			list.add("男２０代");
			list.add("男３０代");
			list.add("男４０代");
			list.add("男５０代");
			list.add("男６０代");

		} else if (name.equals("性・年代（５歳区分）")) {
			list.add("女１５〜１９才");
			list.add("女２０〜２４才");
			list.add("女２５〜２９才");
			list.add("女３０〜３４才");
			list.add("女３５〜３９才");
			list.add("女４０〜４４才");
			list.add("女４５〜４９才");
			list.add("女５０〜５４才");
			list.add("女５５〜５９才");
			list.add("女６０〜６４才");
			list.add("女６５〜６９才");
			list.add("男１５〜１９才");
			list.add("男２０〜２４才");
			list.add("男２５〜２９才");
			list.add("男３０〜３４才");
			list.add("男３５〜３９才");
			list.add("男４０〜４４才");
			list.add("男４５〜４９才");
			list.add("男５０〜５４才");
			list.add("男５５〜５９才");
			list.add("男６０〜６４才");
			list.add("男６５〜６９才");
		} else if (name.equals("性・年代（メディア区分）")) {
			list.add("女１５〜１９才");
			list.add("女２０〜３４才");
			list.add("女３５〜４９才");
			list.add("女５０才以上");
			list.add("男１５〜１９才");
			list.add("男２０〜３４才");
			list.add("男３５〜４９才");
			list.add("男５０才以上");
		} else if (name.equals("年代（１０歳区分）")) {
			list.add("１０代");
			list.add("２０代");
			list.add("３０代");
			list.add("４０代");
			list.add("５０代");
			list.add("６０代");
		} else if (name.equals("年代（５歳区分）")) {
			list.add("１５〜１９才");
			list.add("２０〜２４才");
			list.add("２５〜２９才");
			list.add("３０〜３４才");
			list.add("３５〜３９才");
			list.add("４０〜４４才");
			list.add("４５〜４９才");
			list.add("５０〜５４才");
			list.add("５５〜５９才");
			list.add("６０〜６４才");
			list.add("６５〜６９才");
		} else if (name.equals("年代（メディア区分）")) {
			list.add("１５〜１９才");
			list.add("２０〜３４才");
			list.add("３５〜４９才");
			list.add("５０才以上");
		} else if (name.equals("世帯主年代")) {
			list.add("非該当");
			list.add("〜２９才");
			list.add("〜３９才");
			list.add("〜４９才");
			list.add("〜５９才");
			list.add("６０才〜");
		} else if (name.equals("世帯特性…主婦年代")) {
			list.add("非該当");
			list.add("〜２９才");
			list.add("〜３９才");
			list.add("〜４９才");
			list.add("〜５９才");
			list.add("６０才〜");
		} else if (name.equals("年度年齢")) {
			list.add("１２才未満");
			list.add("１２〜１９才");
			list.add("２０〜２４才");
			list.add("２５〜２９才");
			list.add("３０〜３４才");
			list.add("３５〜３９才");
			list.add("４０〜４４才");
			list.add("４５〜４９才");
			list.add("５０〜５４才");
			list.add("５５〜５９才");
			list.add("６０才以上");
		} else if (name.equals("購入時間")) {
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
			if (key.indexOf("男") >= 0) {
				dataset.addValue(sumVal, MALE, key.substring(1));
			} else if (key.indexOf("女") >= 0) {
				dataset.addValue(sumVal * (-1), FEMALE, key.substring(1));
			} else {
				dataset.addValue(sumVal, "購入者", key);
			}
		}
		return dataset;
	}

	private KeyedValues2DDataset createSampleDataset() {
		// addする順番にプロットされるので空値が必要
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
		PopulationChart chartIns = new PopulationChart(null, "年代別構成 ", "年代",
				"(人数)","");

		ApplicationFrame frame = new ApplicationFrame("PopulationChart");
		frame.setContentPane(chartIns.getPanel());
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
}