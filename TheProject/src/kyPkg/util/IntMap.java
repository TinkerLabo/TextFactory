package kyPkg.util;
public class IntMap {
	private int[] ansArray;
	private String delim = ",";
	private String prefix = "";
	private String suffix = "";
	//-------------------------------------------------------------------------
	//　作成する配列のサイズを引数とする
	//-------------------------------------------------------------------------
	public IntMap(int arraySize){
		ansArray = new int[arraySize];
		clear();
	}
	public void setDelim(String delim) {
		this.delim = delim;
	}
	public void setEnclosure(String enc) {
		this.prefix = enc;
		this.suffix = enc;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	//-------------------------------------------------------------------------
	//　クリアする
	//-------------------------------------------------------------------------
	public void clear(){
		for(int i = 0;i<ansArray.length;i++){
			ansArray[i] = 0;
		}
	}
	//-------------------------------------------------------------------------
	//　指定位置に値を足す
	//　注意、指定する位置パラメータは０から始まる！
	//-------------------------------------------------------------------------
	public void add(int i,int val){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] += val;
		}
	}
	//-------------------------------------------------------------------------
	//　指定位置の値をセットする
	//　注意、指定する位置パラメータは０から始まる！
	//-------------------------------------------------------------------------
	public void set(int i,int val){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] = val;
		}
	}
	//-------------------------------------------------------------------------
	//　結果
	//-------------------------------------------------------------------------
	public String cnv2String(){

		StringBuffer buf = new  StringBuffer();
         for (int i = 0; i < ansArray.length; i++) {
 			if (i>0) buf.append(delim);
			buf.append(prefix);
			buf.append(ansArray[i]);
			buf.append(suffix);
		}
		return buf.toString();
	}
	//-------------------------------------------------------------------------
	//　使用例・・・
	//-------------------------------------------------------------------------
	public static void main(String[] argv){
		int arraySize = 7;
		IntMap ins = new kyPkg.util.IntMap(arraySize);

		ins.add(0,20);
		ins.add(1,30);
		ins.add(2,40);
		ins.add(3,50);
		ins.add(4,60);
		ins.add(5,70);
		ins.add(6,80);
		ins.add(7,90);
		ins.add(8,10);

		ins.setEnclosure("'");
		System.out.println("Ans=>"+ins.cnv2String());
	}
}
