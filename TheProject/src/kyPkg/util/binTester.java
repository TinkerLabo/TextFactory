package kyPkg.util;

public class binTester {
	static final int mask_L = 4;
	static final int mask_I = 2;
	static final int mask_R = 1;
	public static void binTest01() {
		boolean flag_L = false;
		boolean flag_I = false;
		boolean flag_R = false;
		for (int option = 0; option < 8; option++) {
			flag_L = ((option & mask_L) == mask_L);
			flag_I = ((option & mask_I) == mask_I);
			flag_R = ((option & mask_R) == mask_R);
			System.out.println("i:" + option + " => " + flag_L + "-" + flag_I + "-"+ flag_R);
		}
	}
	public static void main(String[] args) {
		binTest01();
	}
}
