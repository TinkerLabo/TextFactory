package kyPkg.rez;

// { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
// "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W","X", "Y", "Z"
// },
public class MatrixControl {
	public static String[][] toStrMatrix(int[][] original) {
		if (original == null) return null;
		String[][] matrix = new String[original.length][original[0].length];
		for (int v = 0; v < original.length; v++) {
			for (int h = 0; h < original[v].length; h++) {
				matrix[v][h] = String.valueOf(original[v][h]);
			}
		}
		return matrix;
	}
	public static String[][] toStrMatrix(float[][] original) {
		if (original == null) return null;
		String[][] matrix = new String[original.length][original[0].length];
		for (int v = 0; v < original.length; v++) {
			for (int h = 0; h < original[v].length; h++) {
				matrix[v][h] = String.valueOf(original[v][h]);
			}
		}
		return matrix;
	}

	
	public static String[][] matrix(int vLen, int hLen, String initial) {
		String[][] matrix = new String[vLen][hLen];
		for (int v = 0; v < matrix.length; v++) {
			for (int h = 0; h < matrix[v].length; h++) {
				matrix[v][h] = initial;
			}
		}
		return matrix;
	}

	public static int[][] matrix(int vLen, int hLen, int initial) {
		int[][] matrix = new int[vLen][hLen];
		for (int v = 0; v < matrix.length; v++) {
			for (int h = 0; h < matrix[v].length; h++) {
				matrix[v][h] = initial;
			}
		}
		return matrix;
	}

	public static float[][] matrix(int vLen, int hLen, float initial) {
		float[][] matrix = new float[vLen][hLen];
		for (int v = 0; v < matrix.length; v++) {
			for (int h = 0; h < matrix[v].length; h++) {
				matrix[v][h] = initial;
			}
		}
		return matrix;
	}

	public static String[][] overLay(String[][] mother, String[] chile,
			int relV, int relH) {
		return overLay(mother, new String[][] { chile }, relV, relH);
	}

	public static String[][] overLay(String[][] mother, String[][] chile,
			int relV, int relH) {
		String[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > v + relV) {
					if (ovLay[v + relV].length > h + relH) {
						ovLay[v + relV][h + relH] = chile[v][h];
					}
				}
			}
		}
		return ovLay;
	}
	public static String[][] overLay(String[][] mother, int[] chile,
			int relV, int relH) {
		return overLay(mother, new int[][] { chile }, relV, relH);
	}

	public static String[][] overLay(String[][] mother, int[][] chile,
			int relV, int relH) {
		String[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > v + relV) {
					if (ovLay[v + relV].length > h + relH) {
						ovLay[v + relV][h + relH] = String.valueOf(chile[v][h]);
					}
				}
			}
		}
		return ovLay;
	}
	public static String[][] overLay(String[][] mother, float[] chile,
			int relV, int relH) {
		return overLay(mother, new float[][] { chile }, relV, relH);
	}

	public static String[][] overLay(String[][] mother, float[][] chile,
			int relV, int relH) {
		String[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > v + relV) {
					if (ovLay[v + relV].length > h + relH) {
						ovLay[v + relV][h + relH] = String.valueOf(chile[v][h]);
					}
				}
			}
		}
		return ovLay;
	}

	
	public static int[][] overLay(int[][] mother, int[] chile, int relV,
			int relH) {
		return overLay(mother, new int[][] { chile }, relV, relH);
	}

	public static int[][] overLay(int[][] mother, int[][] chile, int relV,
			int relH) {
		int[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > v + relV) {
					if (ovLay[v + relV].length > h + relH) {
						ovLay[v + relV][h + relH] = chile[v][h];
					}
				}
			}
		}
		return ovLay;
	}

	public static float[][] overLay(float[][] mother, float[] chile, int relV,
			int relH) {
		return overLay(mother, new float[][] { chile }, relV, relH);
	}

	public static float[][] overLay(float[][] mother, float[][] chile,
			int relV, int relH) {
		float[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > v + relV) {
					if (ovLay[v + relV].length > h + relH) {
						ovLay[v + relV][h + relH] = chile[v][h];
					}
				}
			}
		}
		return ovLay;
	}

	public static float[][] overLay(float[][] mother, int[] chile, int relV,
			int relH) {
		return overLay(mother, new int[][] { chile }, relV, relH);
	}

	public static float[][] overLay(float[][] mother, int[][] chile,
			int relV, int relH) {
		float[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > v + relV) {
					if (ovLay[v + relV].length > h + relH) {
						ovLay[v + relV][h + relH] = chile[v][h];
					}
				}
			}
		}
		return ovLay;
	}
	
	
	
	//子供のマトリックスの軸が反転するバージョン
	public static String[][] overXray(String[][] mother, String[] chile,int relV, int relH) {
		return overXray(mother, new String[][]{chile},relV, relH);
	}
	public static String[][] overXray(String[][] mother, String[][] chile,
			int relV, int relH) {
		String[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > h + relV) {
					if (ovLay[h + relV].length > v + relH) {
						ovLay[h + relV][v + relH] = chile[v][h];
					}
				}
			}
		}
		return ovLay;
	}	

	public static String[][] overXray(String[][] mother, int[] chile,int relV, int relH) {
		return overXray(mother, new int[][]{chile},relV, relH);
	}
	public static String[][] overXray(String[][] mother, int[][] chile,
			int relV, int relH) {
		String[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > h + relV) {
					if (ovLay[h + relV].length > v + relH) {
						ovLay[h + relV][v + relH] = String.valueOf(chile[v][h]);
					}
				}
			}
		}
		return ovLay;
	}	
	public static String[][] overXray(String[][] mother, float[] chile,int relV, int relH) {
		return overXray(mother, new float[][]{chile},relV, relH);
	}
	public static String[][] overXray(String[][] mother, float[][] chile,
			int relV, int relH) {
		String[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > h + relV) {
					if (ovLay[h + relV].length > v + relH) {
						ovLay[h + relV][v + relH] = String.valueOf(chile[v][h]);
					}
				}
			}
		}
		return ovLay;
	}	

	
	
	public static int[][] overXray(int[][] mother, int[] chile, int relV,
			int relH) {
		return overXray(mother, new int[][] { chile }, relV, relH);
	}
	public static int[][] overXray(int[][] mother, int[][] chile, int relV,
			int relH) {
		int[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > h + relV) {
					if (ovLay[h + relV].length > v + relH) {
						ovLay[h + relV][v + relH] = chile[v][h];
					}
				}
			}
		}
		return ovLay;
	}
	public static float[][] overXray(float[][] mother, float[] chile, int relV,
			int relH) {
		return overXray(mother, new float[][] { chile }, relV, relH);
	}
	public static float[][] overXray(float[][] mother, float[][] chile,
			int relV, int relH) {
		float[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > h + relV) {
					if (ovLay[h + relV].length > v + relH) {
						ovLay[h + relV][v + relH] = chile[v][h];
					}
				}
			}
		}
		return ovLay;
	}	
	public static float[][] overXray(float[][] mother, int[] chile, int relV,
			int relH) {
		return overXray(mother, new int[][] { chile }, relV, relH);
	}
	public static float[][] overXray(float[][] mother, int[][] chile,
			int relV, int relH) {
		float[][] ovLay = mother;
		for (int v = 0; v < chile.length; v++) {
			for (int h = 0; h < chile[v].length; h++) {
				if (ovLay.length > h + relV) {
					if (ovLay[h + relV].length > v + relH) {
						ovLay[h + relV][v + relH] = chile[v][h];
					}
				}
			}
		}
		return ovLay;
	}	

	public static void test00() {
		String[] chile = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String[][] ov = overLay(matrix(5, 20, "□"), chile, 0, 1);
		for (int v = 0; v < ov.length; v++) {
			System.out.print("[" + v + "]");
			for (int h = 0; h < ov[v].length; h++) {
				System.out.print("_" + ov[v][h]);
			}
			System.out.println("");
		}
	}

	public static void test01() {
		String[][] chile = {
				{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" },
				{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" },
				{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" } };
		String[][] ov = overLay(matrix(5, 20, "□"), chile, 1, 1);
		for (int v = 0; v < ov.length; v++) {
			System.out.print("[" + v + "]");
			for (int h = 0; h < ov[v].length; h++) {
				System.out.print("_" + ov[v][h]);
			}
			System.out.println("");
		}
	}

	public static void test02() {
		int[][] chile = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 } };
		int[][] ov = overLay(matrix(5, 20, 0), chile, 1, 1);
		for (int v = 0; v < ov.length; v++) {
			System.out.print("[" + v + "]");
			for (int h = 0; h < ov[v].length; h++) {
				System.out.print("_" + ov[v][h]);
			}
			System.out.println("");
		}
	}

	public static void test03() {
		float[][] chile = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 } };
		float[][] ov = overLay(matrix(5, 20, (float) 0), chile, 1, 1);
		for (int v = 0; v < ov.length; v++) {
			System.out.print("[" + v + "]");
			for (int h = 0; h < ov[v].length; h++) {
				System.out.print("_" + ov[v][h]);
			}
			System.out.println("");
		}
	}

	public static void test04() {
		int[] sumH = { 100, 200, 300, 400, 500, 600, 700, 800, 900 };
		int[] avgH = { 50, 100, 150, 200, 250, 300, 450, 400, 450 };
		int[][] ov = matrix(5, 10, 0);
		ov = overLay(ov, sumH, 0, 2);
		ov = overLay(ov, avgH, 1, 2);
		for (int v = 0; v < ov.length; v++) {
			System.out.print("[" + v + "]");
			for (int h = 0; h < ov[v].length; h++) {
				System.out.print("_" + ov[v][h]);
			}
			System.out.println("");
		}
	}

	public static void test05(boolean fSum,boolean fAvg,boolean fStd,boolean fShr) {
		int[] sumH = { 100, 200, 300, 400, 500, 600, 700, 800, 900 };
		int[] sumV = { 100, 200, 300};
		float[] avgH = { 11, 22, 33, 44, 55, 66, 77, 88, 99 };
		float[] avgV = { 11, 22, 33};
		float[] stdH = { 111, 222, 333, 444, 555, 666, 777, 888, 999 };
		float[] stdV = { 111, 222,333};
		float[] shrH = { 1111, 2222, 3333, 4444, 5555, 6666, 7777, 8888, 9999 };
		float[] shrV = { 1111, 2222, 3333};
		int[][] chile = { 
				{  1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{  1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{  1, 2, 3, 4, 5, 6, 7, 8, 9 } };
		int x=0;
		int y=0;
		int rCnt = 0;
		if (fSum == true)rCnt++;
		if (fAvg == true)rCnt++;
		if (fStd == true)rCnt++;
		if (fShr == true)rCnt++;
//		float[][] ov = matrix(3+rCnt, 9+rCnt, (float)0);
		String[][] ov = matrix(3+rCnt, 9+rCnt, "□");
		if (fSum == true) ov = overLay(ov, sumH, x++, rCnt);
		if (fAvg == true) ov = overLay(ov, avgH, x++, rCnt);
		if (fStd == true) ov = overLay(ov, stdH, x++, rCnt);
		if (fShr == true) ov = overLay(ov, shrH, x++, rCnt);
		if (fSum == true) ov = overXray(ov, sumV, rCnt, y++);
		if (fAvg == true) ov = overXray(ov, avgV, rCnt, y++);
		if (fStd == true) ov = overXray(ov, stdV, rCnt, y++);
		if (fShr == true) ov = overXray(ov, shrV, rCnt, y++);
		ov = overLay(ov, chile, rCnt, rCnt);
		for (int v = 0; v < ov.length; v++) {
			System.out.print("[" + v + "]");
			for (int h = 0; h < ov[v].length; h++) {
				System.out.print("_" + ov[v][h]);
			}
			System.out.println("");
		}
	}

	public static void main(String[] argv) {
		test05(true,true,true,true);
	}
}
