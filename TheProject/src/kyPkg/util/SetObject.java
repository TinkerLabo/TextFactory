package kyPkg.util;

import java.util.HashSet;
import java.util.Set;

//qpr.ssAna.StringSet
public class SetObject {

	// Set<Integer> orSet = kyPkg.util.SetObject.or(a,b);
	public static Set<String> or(Set<String> left, Set<String> right) {
		Set<String> setOr = new HashSet();
		for (String index : right) {
			setOr.add(index);
		}
		for (String index : left) {
			setOr.add(index);
		}
		return setOr;
	}

	// Set<String> andSet = kyPkg.util.SetObject.and(leftSet,rightSet);
	public static Set<String> and(Set<String> left, Set<String> right) {
		Set<String> setAnd = new HashSet();
		for (String index : right) {
			if (left.contains(index)) {
				setAnd.add(index);
			}
		}
		return setAnd;
	}

	// Set<String> rightOnly = kyPkg.util.SetObject.leftOnly(leftSet, rightSet);
	public static Set<String> rightOnly(Set<String> left, Set<String> right) {
		Set<String> setRightOnly = new HashSet();
		for (String index : right) {
			if (!left.contains(index)) {
				setRightOnly.add(index);
			}
		}
		return setRightOnly;
	}

	// Set<String> leftOnly = kyPkg.util.SetObject.leftOnly(leftSet, rightSet);
	public static Set<String> leftOnly(Set<String> left, Set<String> right) {
		Set<String> setLeftOnly = new HashSet();
		for (String index : left) {
			if (!right.contains(index)) {
				setLeftOnly.add(index);
			}
		}
		return setLeftOnly;
	}

	public static void dumpSet(Set<String> set, String comment) {
		System.out.println("<" + comment + ">--------------------");
		for (String val : set) {
			System.out.println("dumpSet>" + val);
		}
	}

	// Set<Integer> orSet = qpr.ssAna.SetObject.or(a,b);
	public static Set<Integer> iOr(Set<Integer> left, Set<Integer> right) {
		Set<Integer> setOr = new HashSet();
		for (Integer index : right) {
			setOr.add(index);
		}
		for (Integer index : left) {
			setOr.add(index);
		}
		return setOr;
	}

	// Set<Integer> andSet = qpr.ssAna.SetObject.and(a,b);
	public static Set<Integer> iAnd(Set<Integer> left, Set<Integer> right) {
		Set<Integer> setAnd = new HashSet();
		for (Integer index : right) {
			if (left.contains(index)) {
				setAnd.add(index);
			}
		}
		return setAnd;
	}

	// Set<Integer> rightOnly = qpr.ssAna.SetObject.rightOnly(a,b);
	public static Set<Integer> iRightOnly(Set<Integer> left, Set<Integer> right) {
		Set<Integer> setRightOnly = new HashSet();
		for (Integer index : right) {
			if (!left.contains(index)) {
				setRightOnly.add(index);
			}
		}
		return setRightOnly;
	}

	// Set<Integer> leftOnly = qpr.ssAna.SetObject.leftOnly(a,b);
	public static Set<Integer> iLeftOnly(Set<Integer> left, Set<Integer> right) {
		Set<Integer> setLeftOnly = new HashSet();
		for (Integer index : left) {
			if (!right.contains(index)) {
				setLeftOnly.add(index);
			}
		}
		return setLeftOnly;
	}

}