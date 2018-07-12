package kyPkg.util;

import java.util.ArrayList;
import java.util.List;

public class BinUtil {
	public static void main(String[] argv) {
		test();
	}
	public static void test2() {
		long option = 0;
		option = setAnd(option,1);
		option = setAnd(option,2);
		
		
		if(BinUtil.check(option, 1)){
			System.out.println("option selected");
		}
		
		
	}

	public static void test() {
		System.out.println("Integer.MAX_VALUE:" + Integer.MAX_VALUE);
		System.out.println("Long.MAX_VALUE:" + Long.MAX_VALUE);
		List<Long> vals = new ArrayList();
		// intは32ビット、longは64ビット
		for (int i = 0; i < 64; i++) {
			vals.add(BinUtil.getMask(i));
			System.out.println("val<" + i + ">:" + vals.get(i));
		}

		long judge = 0;
		judge = BinUtil.setAnd(judge, vals.get(2));
		judge = BinUtil.setAnd(judge, vals.get(4));
		long mask = vals.get(8);
		if (BinUtil.check(judge, mask)) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}
	}

	// ウンバイト目のマスクを作る・・・・
	public static long getMask(int exponent) {
		return (long) Math.pow(2.0, (double) exponent);

	}

	// 指定箇所にフラグを立てる
	public static long setAnd(long judge, long mask) {
		return (judge | mask);
	}

	public static boolean check(long judge, long mask) {
		if ((judge & mask) == mask)
			return true;
		return false;
	}

}
