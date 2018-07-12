package kyPkg.converter;

import static kyPkg.util.Joint.join;

import kyPkg.filter.Inf_DualConverter;

public class DCnvDefault implements Inf_DualConverter {
	protected String delimiter = "\t";

	public DCnvDefault(String delimiter) {
		super();
		this.delimiter = delimiter;
	}

	// ------------------------------------------------------------------------
	// フィルタ、タイプ１
	// arrayConverter = new DefaultArrayConverter();
	// ------------------------------------------------------------------------
	@Override
	public String convert(String[] lefts, String[] rights, int stat) {

		switch ((int) stat) {
		case 1:
			return join(lefts, delimiter); // Left Only(Master Data)
		case 2:
			return join(rights, delimiter); // Inner Join(Update)
		case 4:
			return join(rights, delimiter); // Right only(Tran Only)
		default:
			return join(rights, delimiter);
		}
	}

	@Override
	public void init() {
	};

	@Override
	public void fin() {
	};

}
