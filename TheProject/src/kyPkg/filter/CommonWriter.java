package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class CommonWriter implements Inf_BaseClojure {
	static String wLF = System.getProperty("line.separator");

	private long count = 0;

	public long getCount() {
		return count;
	}

	private String outPath = "";

	private BufferedWriter bw = null;

	public CommonWriter(String outPath) {
		this.outPath = outPath;
		init();
	}

	@Override
	public void init() {
		count = 0;
		try {
			bw = new BufferedWriter(new FileWriter(outPath));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	@Override
	public void execute(String[] rec) {
		execute(join(rec, ","));
	}
	@Override
	public void execute(String rec) {
		try {
			System.out.println("@ComonWriter rec:" + rec);
			bw.write(rec);
			bw.write(wLF);
			count++;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void write() {
		count = 0;
		try {
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}


}
