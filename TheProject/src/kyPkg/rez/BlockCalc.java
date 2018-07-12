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

	private int[][] iOrg = null; // �W�v�p�}�g���b�N�X Original

	private float[][] mShH; // ����

	private float[][] mShV; // �c��

	private float[][] mPwH; // �W���΍��i���j

	private float[][] mPwV; // �W���΍��i�c�j

	private float[][] m100; // 100�l������䗦

	private float[] sumH100;

	private float[] sumV100;

	private int[] sumH;// �����v

	private int[] sumV;// �c���v

	private float[] avgH;// ������

	private float[] avgV;// �c����

	private float[] stdH;//

	private float[] stdV;//

	private float[] shrH;// ����

	private float[] shrV;// �c��

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
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public BlockCalc(int lenY, int lenX, int gradeY, int gradeX) {
		this.gradeH = gradeX;
		this.gradeV = gradeY;
		this.lenH = lenX;
		this.lenV = lenY;
		this.iOrg = new int[lenY][lenX];
		this.m100 = new float[lenY][lenX]; // 100����q��������
		this.mShH = new float[lenY][lenX]; // ����
		this.mShV = new float[lenY][lenX]; // �c��
		this.mPwH = new float[lenY][lenX]; // �W���΍��i���j
		this.mPwV = new float[lenY][lenX]; // �W���΍��i�c�j
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
	// ������}�g���b�N�X�����o��
	// -------------------------------------------------------------------------
	public String[][] getMatrix() {
		return matrix;
	}

	public String getRowStr(int v) {
		return join(matrix[v], ",");
	}

	// -------------------------------------------------------------------------
	// ������}�g���b�N�X���Z�b�g����i���v�Ȃǂ��������o�[�W�����j
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
			avgH[x] = rdz((sumH[x] + 0.0F) / sumV.length); // ������
			shrH[x] = rdz(sumH[x] * 100.0F / gSum); // ����
		}
		for (int y = 0; y < sumV.length; y++) {
			avgV[y] = (sumV[y] + 0.0F) / sumH.length; // �c����
			shrV[y] = rdz(sumV[y] * 100.0F / gSum); // �c��
		}
		for (int y = 0; y < lenV; y++) {
			rY = y;
			for (int x = 0; x < lenH; x++) {
				rX = x;
				// m100 100����q��������
				if (sampleCount > 0) {
					m100[rY][rX] = rdz((iOrg[y][x] * 100.0F) / sampleCount);
					sumV100[rY] = rdz((sumV[rY] * 100.0F) / sampleCount);
					sumH100[rX] = rdz((sumH[rX] * 100.0F) / sampleCount);
				}
				if (sumV[rY] > 0)
					mShH[rY][rX] = rdz((iOrg[y][x] * 100.0F) / sumV[rY]); // mShH
																			// ����
				if (sumH[rX] > 0)
					mShV[rY][rX] = rdz((iOrg[y][x] * 100.0F) / sumH[rX]); // mShV
																			// �c��
				// mPwH �W���΍��i���j
				mPwH[rY][rX] = rdz(Math.pow((iOrg[y][x] + 0.0F) - avgV[rY], 2));
				// mPwV �W���΍��i�c�j
				mPwV[rY][rX] = rdz(Math.pow((iOrg[y][x] + 0.0F) - avgH[rX], 2));
				stdV[rY] += rdz(Math.pow((iOrg[y][x] + 0.0F) - avgV[rY], 2));
				stdH[rX] += rdz(Math.pow((iOrg[y][x] + 0.0F) - avgH[rX], 2));
			}
		}

		// ���U�̕������Ƃ��ƕ֗����낤
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
			System.out.println("���UX�@stdH:" + join(stdH,","));
			System.out.println("���UY�@stdV:" + join(stdV,","));
		}
	}

	// private double pow(int val) {
	// return Math.pow((val + 0.0d));
	// }

	// ���ăs�A�\���ł̑��֌W�����o���ɂ́A�ǂ�ȃf�[�^���K�v���H�H�l���Ă���
	// �����_�ȉ�newScale���Ŏl�̌ܓ�����
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
