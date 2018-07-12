package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import globals.ResControl;

public class EasyReaderTest {
	public static void template() {
		System.out.println("<debug@EasyReaderTest start>");
		String path = ResControl.D_QPR + "TESTDAT/Area3X.txt";
		int icnt = 0;
		List list = new ArrayList();
		kyPkg.filter.EzReader reader = new kyPkg.filter.EzReader(path);
		reader.open();
		String[] splited = null;
		while (reader.readLine() != null) {
			splited = reader.getSplited();
			if (splited != null && splited.length >= 1) {
				splited = reader.getSplited();
				System.out.println("splited[0]=>" + splited[0]);
				list.add(splited);
				icnt++;
			}
		}
		reader.close();
		System.out.println("<debug@EasyReaderTest end> count:" + icnt);
	}

	public static void main(String[] argv) {
		template();
	}
}
