package kyPkg.python;

import java.util.HashSet;

public class NegPosFilter {
	private HashSet positive = null;
	private HashSet negative = null;
	public static void main(String[] argv) {
		String paramM = "c:/sample/masterX.txt";
		String paramT = "c:/sample/TranX.txt";
		NegPosFilter npFilter = new NegPosFilter(paramM, paramT);
		System.out.println("Positive  Count:" + npFilter.getPositiveCount());
		System.out.println("Negative  Count:" + npFilter.getNegativeCount());
		System.out.println("Total     Count:" + npFilter.getTotalCount());
	}

	// -------------------------------------------------------------------------
	// 購入有り・無しのマッピング取り出し機構・・・・
	// -------------------------------------------------------------------------
	public NegPosFilter(String parmMaster, String paramTran) {
		this(PythonEmu.file2Set(parmMaster), PythonEmu.file2Set(paramTran));
	}
	public NegPosFilter(HashSet<String> set1, HashSet<String> set2) {
		positive = new HashSet();
		negative = new HashSet();
		PythonEmu.vennMix(set1, set2, positive, negative);
	}
	public int positiveSaveAs(String outPath) {
		return PythonEmu.set2file(outPath, positive);
	}

	public int negativeSaveAs(String outPath) {
		return PythonEmu.set2file(outPath, negative);
	}

	public HashSet<String> getPositiveSet() {
		return positive;
	}

	public HashSet<String> getNegativeSet() {
		return negative;
	}

	public int getPositiveCount() {
		return positive.size();
	}

	public int getNegativeCount() {
		return negative.size();
	}

	public int getTotalCount() {
		return (positive.size() + negative.size());
	}

}
