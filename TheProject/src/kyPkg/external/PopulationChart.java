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
	// String population = "(l”)";
	private static final String MALE = "’j«";
	private static final String FEMALE = "—«";
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
		if (name.equals("«E”N‘ãi‚P‚OÎ‹æ•ªj")) {
			list.add("—‚P‚O‘ã");
			list.add("—‚Q‚O‘ã");
			list.add("—‚R‚O‘ã");
			list.add("—‚S‚O‘ã");
			list.add("—‚T‚O‘ã");
			list.add("—‚U‚O‘ã");
			list.add("’j‚P‚O‘ã");
			list.add("’j‚Q‚O‘ã");
			list.add("’j‚R‚O‘ã");
			list.add("’j‚S‚O‘ã");
			list.add("’j‚T‚O‘ã");
			list.add("’j‚U‚O‘ã");

		} else if (name.equals("«E”N‘ãi‚TÎ‹æ•ªj")) {
			list.add("—‚P‚T`‚P‚XË");
			list.add("—‚Q‚O`‚Q‚SË");
			list.add("—‚Q‚T`‚Q‚XË");
			list.add("—‚R‚O`‚R‚SË");
			list.add("—‚R‚T`‚R‚XË");
			list.add("—‚S‚O`‚S‚SË");
			list.add("—‚S‚T`‚S‚XË");
			list.add("—‚T‚O`‚T‚SË");
			list.add("—‚T‚T`‚T‚XË");
			list.add("—‚U‚O`‚U‚SË");
			list.add("—‚U‚T`‚U‚XË");
			list.add("’j‚P‚T`‚P‚XË");
			list.add("’j‚Q‚O`‚Q‚SË");
			list.add("’j‚Q‚T`‚Q‚XË");
			list.add("’j‚R‚O`‚R‚SË");
			list.add("’j‚R‚T`‚R‚XË");
			list.add("’j‚S‚O`‚S‚SË");
			list.add("’j‚S‚T`‚S‚XË");
			list.add("’j‚T‚O`‚T‚SË");
			list.add("’j‚T‚T`‚T‚XË");
			list.add("’j‚U‚O`‚U‚SË");
			list.add("’j‚U‚T`‚U‚XË");
		} else if (name.equals("«E”N‘ãiƒƒfƒBƒA‹æ•ªj")) {
			list.add("—‚P‚T`‚P‚XË");
			list.add("—‚Q‚O`‚R‚SË");
			list.add("—‚R‚T`‚S‚XË");
			list.add("—‚T‚OËˆÈã");
			list.add("’j‚P‚T`‚P‚XË");
			list.add("’j‚Q‚O`‚R‚SË");
			list.add("’j‚R‚T`‚S‚XË");
			list.add("’j‚T‚OËˆÈã");
		} else if (name.equals("”N‘ãi‚P‚OÎ‹æ•ªj")) {
			list.add("‚P‚O‘ã");
			list.add("‚Q‚O‘ã");
			list.add("‚R‚O‘ã");
			list.add("‚S‚O‘ã");
			list.add("‚T‚O‘ã");
			list.add("‚U‚O‘ã");
		} else if (name.equals("”N‘ãi‚TÎ‹æ•ªj")) {
			list.add("‚P‚T`‚P‚XË");
			list.add("‚Q‚O`‚Q‚SË");
			list.add("‚Q‚T`‚Q‚XË");
			list.add("‚R‚O`‚R‚SË");
			list.add("‚R‚T`‚R‚XË");
			list.add("‚S‚O`‚S‚SË");
			list.add("‚S‚T`‚S‚XË");
			list.add("‚T‚O`‚T‚SË");
			list.add("‚T‚T`‚T‚XË");
			list.add("‚U‚O`‚U‚SË");
			list.add("‚U‚T`‚U‚XË");
		} else if (name.equals("”N‘ãiƒƒfƒBƒA‹æ•ªj")) {
			list.add("‚P‚T`‚P‚XË");
			list.add("‚Q‚O`‚R‚SË");
			list.add("‚R‚T`‚S‚XË");
			list.add("‚T‚OËˆÈã");
		} else if (name.equals("¢‘Ñå”N‘ã")) {
			list.add("”ñŠY“–");
			list.add("`‚Q‚XË");
			list.add("`‚R‚XË");
			list.add("`‚S‚XË");
			list.add("`‚T‚XË");
			list.add("‚U‚OË`");
		} else if (name.equals("¢‘Ñ“Á«cå•w”N‘ã")) {
			list.add("”ñŠY“–");
			list.add("`‚Q‚XË");
			list.add("`‚R‚XË");
			list.add("`‚S‚XË");
			list.add("`‚T‚XË");
			list.add("‚U‚OË`");
		} else if (name.equals("”N“x”N—î")) {
			list.add("‚P‚QË–¢–");
			list.add("‚P‚Q`‚P‚XË");
			list.add("‚Q‚O`‚Q‚SË");
			list.add("‚Q‚T`‚Q‚XË");
			list.add("‚R‚O`‚R‚SË");
			list.add("‚R‚T`‚R‚XË");
			list.add("‚S‚O`‚S‚SË");
			list.add("‚S‚T`‚S‚XË");
			list.add("‚T‚O`‚T‚SË");
			list.add("‚T‚T`‚T‚XË");
			list.add("‚U‚OËˆÈã");
		} else if (name.equals("w“üŠÔ")) {
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
			if (key.indexOf("’j") >= 0) {
				dataset.addValue(sumVal, MALE, key.substring(1));
			} else if (key.indexOf("—") >= 0) {
				dataset.addValue(sumVal * (-1), FEMALE, key.substring(1));
			} else {
				dataset.addValue(sumVal, "w“üÒ", key);
			}
		}
		return dataset;
	}

	private KeyedValues2DDataset createSampleDataset() {
		// add‚·‚é‡”Ô‚Éƒvƒƒbƒg‚³‚ê‚é‚Ì‚Å‹ó’l‚ª•K—v
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
		PopulationChart chartIns = new PopulationChart(null, "”N‘ã•Ê\¬ ", "”N‘ã",
				"(l”)","");

		ApplicationFrame frame = new ApplicationFrame("PopulationChart");
		frame.setContentPane(chartIns.getPanel());
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
}