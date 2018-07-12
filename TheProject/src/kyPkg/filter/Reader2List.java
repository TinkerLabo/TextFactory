package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

public class Reader2List   {
	private List<Inf_iClosure> readers;
	private String delimiter = null;

	public String getDelimiter() {
		return delimiter;
	}

	public Reader2List(List<Inf_iClosure> readers) {
		super();
		this.readers = readers;
	}

	public List<String[]> readers2ArrayList() {
		List list = new ArrayList();
		String[] array = null;
		for (Inf_iClosure reader : readers) {
			reader.open();
			delimiter = reader.getDelimiter();
			while ((array = reader.readSplited()) != null) {
				if (array.length > 0)
					list.add(array);
			}
			reader.close();
		}
		return list;
	}
}
