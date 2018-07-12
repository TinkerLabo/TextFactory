package kyPkg.filter;

public class Basic_Reader {
	private Inf_iClosure reader;

	public Basic_Reader(Inf_iClosure reader) {
		this.reader = reader;
	}

	public void execute() {
		reader.open();
		// String[] splited = reader.readSplited();
		// while (splited != null) {
		// // System.out.println("#### splited.length:" + splited.length);
		// splited = reader.readSplited();
		// }
		// ;
		while (reader.readSplited() != null)
			;
		reader.close();
	}

	public static void main(String[] argv) {
	}
}
