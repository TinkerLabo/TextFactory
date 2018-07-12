package kyPkg.rez;

public class BlockHeads {
	private BlockCalc[] insCObj;

	public BlockHeads(int face, int yLen, int xLen, int yGrade, int xGrade) {
		insCObj = new BlockCalc[face];
		for (int i = 0; i < insCObj.length; i++) {
			insCObj[i] = new BlockCalc(yLen, xLen, yGrade, xGrade);
		}
	}

	public void calcIt(int sampleCount) {
		insCObj[0].calcIt(sampleCount);
	}

	public int getGradeV() {
		return insCObj[0].getGradeV();
	}

	public int getEndV() {
		return insCObj[0].getEndV();
	}

	public int getGradeH() {
		return insCObj[0].getGradeH();
	}

	public int getEndH() {
		return insCObj[0].getEndH();
	}

	public void addCalcObj(int y, int x, int wNum, int current) {
		insCObj[current].addCalcObj(y, x, wNum);
	}

	// public String[][] getMatrix(int current,int type){
	// return insCObj[current].getMatrix(type);
	// }

	public void setMatrix(int current, int type, String[] option) {
		insCObj[current].setMatrix(type, option);
	}

	public String[][] getMatrix(int current, int type, String[] option) {
		insCObj[current].setMatrix(type, option);
		return insCObj[current].getMatrix();
	}

	public String getBlockStr(int v, int current) {
		return insCObj[current].getRowStr(v);
	};
}
