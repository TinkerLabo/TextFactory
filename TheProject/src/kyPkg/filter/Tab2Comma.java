package kyPkg.filter;

public class Tab2Comma extends Flt_Base {
	private String out_delimiter = ",";

	public Tab2Comma(String outPath, String inPath) {
		super(outPath, inPath);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		super.start("tab=>comma", 2048);
		outClosure.open();
		outClosure.setDelimiter(out_delimiter);
		loop();// loop
		outClosure.close();
		super.stop();// 正常終了
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop() {
		long lCount = -1;
		int stat = 0;
		inClosure.open();
		String[] cells = null;
		while ((cells = inClosure.readSplited()) != null) {
			lCount++;
			outClosure.write(cells, stat);
		}
		inClosure.close();
		return lCount;
	}



	public static void test() {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("DividerVer1");
		elapse.start();
		String outPath = "c:/comma.txt";
		String inPath = "c:/tab.txt";
		System.out.println("start input:" + inPath);
		Tab2Comma ins = new Tab2Comma(outPath, inPath);
		ins.execute();
		System.out.println("finish! result=>" + outPath);
		elapse.stop();
	}

	public static void tab2comma_1() {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse(
				"kyPkg.filter.Tab2Comma");
		elapse.start();
		String inPath = "k:/S2/rx/qpr/Iizuka/QPR4PLSA/stdType1Head.txt";
	//  String inPath =            "P:/20151225_yuasa/stdType1Head.txt";
		String outPath = "P:/20151225_yuasa/stdType1Head.csv";
		System.out.println("start input:" + inPath);
		Tab2Comma ins = new Tab2Comma(outPath, inPath);
		ins.execute();
		System.out.println("finish! result=>" + outPath);
		elapse.stop();
	}

	public static void tab2comma_2() {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse(
				"kyPkg.filter.Tab2Comma");
		elapse.start();
		String inPath = "k:/S2/rx/qpr/Iizuka/QPR4PLSA/stdType2Head.txt";
//		String inPath =            "P:/20151225_yuasa/stdType2Head.txt";
		String outPath = "P:/20151225_yuasa/stdType2Head.csv";
		System.out.println("start input:" + inPath);
		Tab2Comma ins = new Tab2Comma(outPath, inPath);
		ins.execute();
		System.out.println("finish! result=>" + outPath);
		elapse.stop();
	}
	public static void main(String[] args) {
		//先にヘッダーをつけてそれからタブをカンマに変換している
		
		System.out.println("type1");
		tab2comma_1();
		System.out.println("type2");
		tab2comma_2();
		System.out.println("finish");
	}
}
