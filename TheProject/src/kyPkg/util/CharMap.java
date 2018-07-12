package kyPkg.util;
public class CharMap {
	private char[] ansArray;
	private char negativeChar =  '0';
	private char positiveChar =  '1';
	//-------------------------------------------------------------------------
	//　作成する配列のサイズを引数とする
	//-------------------------------------------------------------------------
	public CharMap(int arraySize){
		ansArray = new char[arraySize];
		clear();
	}
	public void setNegativeChar(char negativeChar) {
		this.negativeChar = negativeChar;
		clear();
	}
	public void setPositiveChar(char positiveChar) {
		this.positiveChar = positiveChar;
	}
	//-------------------------------------------------------------------------
	//　クリアする
	//-------------------------------------------------------------------------
	public void clear(){
		for(int i = 0;i<ansArray.length;i++){
			ansArray[i] = negativeChar;
		}
	}
	//-------------------------------------------------------------------------
	//　指定位置をＯＮにする
	//　注意、指定する位置パラメータは０から始まる！
	//-------------------------------------------------------------------------
	public void turnOn(int i){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] = positiveChar;
		}
	}
	//-------------------------------------------------------------------------
	//　指定位置をＯＦＦにする
	//　注意、指定する位置パラメータは０から始まる！
	//-------------------------------------------------------------------------
	public void turnOff(int i){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] = negativeChar;
		}
	}
	//-------------------------------------------------------------------------
	//　結果
	//-------------------------------------------------------------------------
	public String cnv2String(){
		return new String(ansArray);
	}
	//-------------------------------------------------------------------------
	//　使用例・・・
	//-------------------------------------------------------------------------
	public static void main(String[] argv){
		int arraySize = 7;
		CharMap ins = new kyPkg.util.CharMap(arraySize);
//		ins.setPositiveChar('@');
//		ins.setNegativeChar('x');
		ins.turnOn(0);
		ins.turnOn(1);
		ins.turnOn(2);
		ins.turnOn(3);
		ins.turnOn(4);
		ins.turnOn(5);
		ins.turnOn(6);
		ins.turnOn(7);
		ins.turnOn(8);
		System.out.println("Ans=>"+ins.cnv2String());
	}
}
