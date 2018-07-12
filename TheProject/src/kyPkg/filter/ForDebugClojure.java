package kyPkg.filter;
public class ForDebugClojure implements Inf_BaseClojure {
	private  String delimiter="\t";
	public ForDebugClojure(String delimiter) {
		this.delimiter=delimiter;
	}
	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}
	@Override
	public void execute(String[] rec) {
		System.out.print("[");
		for (int i = 0; i < rec.length; i++) {
			System.out.print(rec[i]);
		}
		System.out.println("]");
	}

	@Override
	public void init() {
		System.out.println("‰Šú‰»");
	}
	@Override
	public void write() {
		System.out.println("I—¹ˆ—");
	}
	public static void main(String[] args) {
		tester();
	}
	public static void tester() {
		Inf_BaseClojure clojure = new kyPkg.filter.ForDebugClojure("\t");
		clojure.init();
		clojure.execute("	h	e	l	l	o	");
		clojure.write();
	}

}
