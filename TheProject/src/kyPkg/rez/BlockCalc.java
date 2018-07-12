package kyPkg.rez;

import static kyPkg.util.Joint.join;

import java.math.BigDecimal;

public class BlockCalc {
	private boolean DEBUG = true;

	private int gradeH;

	private int gradeV;

	private int lenH;

	private int lenV;

	private int gSum;

	private String[][] matrix = null;

	private int[][] iOrg = null; // 集計用マトリックス Original

	private float[][] mShH; // 横比

	private float[][] mShV; // 縦比

	private float[][] mPwH; // 標準偏差（横）

	private float[][] mPwV; // 標準偏差（縦）

	private float[][] m100; // 100人当たり比率

	private float[] sumH100;

	private float[] sumV100;

	private int[] sumH;// 横合計

	private int[] sumV;// 縦合計

	private float[] avgH;// 横平均

	private float[] avgV;// 縦平均

	private float[] stdH;//

	private float[] stdV;//

	private float[] shrH;// 横比

	private float[] shrV;// 縦比

	public int getGradeH() {
		return gradeH;
	}

	public int getEndH() {
		return (gradeH + lenH);
	}

	public int getGradeV() {
		return gradeV;
	}

	public int getEndV() {
		return (gradeV + lenV);
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public BlockCalc(int lenY, int lenX, int gradeY, int gradeX) {
		this.gradeH = gradeX;
		this.gradeV = gradeY;
		this.lenH = lenX;
		this.lenV = lenY;
		this.iOrg = new int[lenY][lenX];
		this.m100 = new float[lenY][lenX]; // 100当りＲａｔｉｏ
		this.mShH = new float[lenY][lenX]; // 横比
		this.mShV = new float[lenY][lenX]; // 縦比
		this.mPwH = new float[lenY][lenX]; // 標準偏差（横）
		this.mPwV = new float[lenY][lenX]; // 標準偏差（縦）
		this.sumH = new int[lenX];
		this.sumV = new int[lenY];
		this.sumH100 = new float[lenX];
		this.sumV100 = new float[lenY];
		this.avgH = new float[lenX];
		this.avgV = new float[lenY];
		this.shrH = new float[lenX];
		this.shrV = new float[lenY];
		this.stdH = new float[lenX];
		this.stdV = new float[lenY];
	}

	// -------------------------------------------------------------------------
	public void addCalcObj(int v, int h, int wNum) {
		if (iOrg.length > v && iOrg[0].length > h) {
			iOrg[v][h] += wNum;
		} else {
			// XXX @KEN System.out.println("#error @BlockCalc out of bounds x:"
			// + v + " y:" + h + " >:" + wNum);
		}
	}

	// -------------------------------------------------------------------------
	// 文字列マトリックスを取り出す
	// -------------------------------------------------------------------------
	public String[][] getMatrix() {
		return matrix;
	}

	public String getRowStr(int v) {
		return join(matrix[v], ",");
	}

	// -------------------------------------------------------------------------
	// 文字列マトリックスをセットする（合計なども乗っけるバージョン）
	// -------------------------------------------------------------------------
	public void setMatrix(int type, String[] option) {
		String[][] wkMatrix = null;
		switch (type) {
		case 0:
			wkMatrix = MatrixControl.toStrMatrix(iOrg);
			break;
		case 1:
			wkMatrix = MatrixControl.toStrMatrix(m100);
			break;
		case 2:
			wkMatrix = MatrixControl.toStrMatrix(mShH);
			break;
		case 3:
			wkMatrix = MatrixControl.toStrMatrix(mShV);
			break;
		case 4:
			wkMatrix = MatrixControl.toStrMatrix(mPwH);
			break;
		case 5:
			wkMatrix = MatrixControl.toStrMatrix(mPwV);
			break;
		default:
			wkMatrix = MatrixControl.toStrMatrix(iOrg);
		}
		matrix = matrixShifter(wkMatrix, option);
	}

	public String[][] matrixShifter(String[][] matrix, String[] option) {
		int x = 0;
		int y = 0;
		int rCnt = 0;
		if (option[0].equals("1"))
			rCnt++; // sum
		if (option[1].equals("1"))
			rCnt++;// avg
		if (option[2].equals("1"))
			rCnt++;// std
		if (option[3].equals("1"))
			rCnt++;// shr
		String[][] ov = MatrixControl.matrix(matrix.length + rCnt,
				matrix[0].length + rCnt, "");

		if (option[0].equals("1")) {
			// ov = MatrixControl.overLay(ov, sumH, x++, rCnt);
			// ov = MatrixControl.overXray(ov, sumV, rCnt, y++);
			ov = MatrixControl.overLay(ov, sumH100, x++, rCnt);
			ov = MatrixControl.overXray(ov, sumV100, rCnt, y++);
		}
		if (option[1].equals("1")) {
			ov = MatrixControl.overLay(ov, avgH, x++, rCnt);
			ov = MatrixControl.overXray(ov, avgV, rCnt, y++);
		}
		if (option[2].equals("1")) {
			ov = MatrixControl.overLay(ov, stdH, x++, rCnt);
			ov = MatrixControl.overXray(ov, stdV, rCnt, y++);
		}
		if (option[3].equals("1")) {
			ov = MatrixControl.overLay(ov, shrH, x++, rCnt);
			ov = MatrixControl.overXray(ov, shrV, rCnt, y++);
		}
		ov = MatrixControl.overLay(ov, matrix, rCnt, rCnt);
		return ov;
	}

	void calcIt(int sampleCount) {
		System.out.println("# calcIt ########################################");
		System.out.println(" V from:" + gradeV + " to:" + gradeV + lenV
				+ " size:" + iOrg.length);
		System.out.println(" H from:" + gradeH + " to:" + gradeH + lenH
				+ " Size:" + iOrg[0].length);
		System.out.println("  -ORIGINAL----------------------------");
		if (DEBUG) {
			for (int y = 0; y < lenV; y++) {
				System.out.print("  {");
				for (int x = 0; x < lenH; x++) {
					System.out.print("" + iOrg[y][x] + ",");
				}
				System.out.println("},");
			}
		}
		System.out.println("  -ORIGINAL-END------------------------");
		int rX = 0;
		int rY = 0;
		for (int y = 0; y < lenV; y++) {
			rY = y;
			for (int x = 0; x < lenH; x++) {
				rX = x;
				sumH[rX] += iOrg[y][x];
				sumV[rY] += iOrg[y][x];
				gSum += iOrg[y][x];
			}
		}
		// ---------------------------------------------------------------------

		// ---------------------------------------------------------------------
		for (int x = 0; x < sumH.length; x++) {
			avgH[x] = rdz((sumH[x] + 0.0F) / sumV.length); // 横平均
			shrH[x] = rdz(sumH[x] * 100.0F / gSum); // 横比
		}
		for (int y = 0; y < sumV.length; y++) {
			avgV[y] = (sumV[y] + 0.0F) / sumH.length; // 縦平均
			shrV[y] = rdz(sumV[y] * 100.0F / gSum); // 縦比
		}
		for (int y = 0; y < lenV; y++) {
			rY = y;
			for (int x = 0; x < lenH; x++) {
				rX = x;
				// m100 100当りＲａｔｉｏ
				if (sampleCount > 0) {
					m100[rY][rX] = rdz((iOrg[y][x] * 100.0F) / sampleCount);
					sumV100[rY] = rdz((sumV[rY] * 100.0F) / sampleCount);
					sumH100[rX] = rdz((sumH[rX] * 100.0F) / sampleCount);
				}
				if (sumV[rY] > 0)
					mShH[rY][rX] = rdz((iOrg[y][x] * 100.0F) / sumV[rY]); // mShH
																			// 横比
				if (sumH[rX] > 0)
					mShV[rY][rX] = rdz((iOrg[y][x] * 100.0F) / sumH[rX]); // mShV
																			// 縦比
				// mPwH 標準偏差（横）
				mPwH[rY][rX] = rdz(Math.pow((iOrg[y][x] + 0.0F) - avgV[rY], 2));
				// mPwV 標準偏差（縦）
				mPwV[rY][rX] = rdz(Math.pow((iOrg[y][x] + 0.0F) - avgH[rX], 2));
				stdV[rY] += rdz(Math.pow((iOrg[y][x] + 0.0F) - avgV[rY], 2));
				stdH[rX] += rdz(Math.pow((iOrg[y][x] + 0.0F) - avgH[rX], 2));
			}
		}

		// 分散の方があとあと便利だろう
		for (int i = 0; i < stdH.length; i++) {
			// stdH[i] = rdz(Math.sqrt(stdH[i] / sumV.length));
			stdH[i] = rdz(stdH[i] / sumV.length);
		}
		for (int i = 0; i < stdV.length; i++) {
			// stdV[i] = rdz(Math.sqrt(stdV[i] / sumH.length));
			stdV[i] = rdz(stdV[i] / sumH.length);
		}
		if (DEBUG) {
			System.out.println("sumH:" + join(sumH,","));
			System.out.println("sumV:" + join(sumV,","));
			System.out.println("sampleCount:" + sampleCount);
			System.out.println("sumH100:" + join(sumH100,","));
			System.out.println("sumV100:" + join(sumV100,","));
			System.out.println("avgH:" + join(avgH,","));
			System.out.println("avgV:" + join(avgV,","));
			System.out.println("shrH:" + join(shrH,","));
			System.out.println("shrV:" + join(shrV,","));
			System.out.println("分散X　stdH:" + join(stdH,","));
			System.out.println("分散Y　stdV:" + join(stdV,","));
		}
	}

	// private double pow(int val) {
	// return Math.pow((val + 0.0d));
	// }

	// さてピアソンでの相関係数を出すには、どんなデータが必要か？？考えておく
	// 小数点以下newScale桁で四捨五入する
	private float rdz(double val) {
		return roundz(val, 2);
	}

	private float roundz(double val, int newScale) {
		try {
			BigDecimal bd = new BigDecimal(val);
			return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).floatValue();

		} catch (java.lang.NumberFormatException e) {
			return 0;
		}
	}

}
