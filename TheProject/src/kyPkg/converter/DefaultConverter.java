package kyPkg.converter;

import static kyPkg.util.Joint.join;

import java.util.List;

public abstract class DefaultConverter implements Inf_LineConverter {
	protected static final String BUY = "Buy";
	protected static final String NON = "Non";

	protected String delimiter = "\t";

	@Override
	public void init() {
	}

	@Override
	public void fin() {
	}

	@Override
	public void setPrefix(String prefix) {
	}

	@Override
	public void setSuffix(String suffix) {
	}

	@Override
	public String getHeader() {
		return null;
	}

	@Override
	public String[] convert(String[] array, int lineNumber) {
		return array;
	}
	@Override
	public List<String> convert(List list, int lineNumber) {
		return list;
	}
	
	@Override
	public final String convert2Str(String[] splited, int stat) {
		return join(convert(splited, stat), delimiter);
	}

	@Override
	public final String convert2Str(String str) {
		return convert2Str(new String[] { str }, 0);
	}

	@Override
	public final String convert2Str(List list, int lineNumber) {
		String[] array = (String[]) list.toArray(new String[list.size()]);
		return convert2Str(array, lineNumber);
	}



}
