package kyPkg.util;
public class BitMapArray {
	private BitMap[] ansArray;
	private String delim = ",";
	private String prefix = "";
	private String suffix = "";
	//-------------------------------------------------------------------------
	//　作成する配列のサイズを引数とする
	//-------------------------------------------------------------------------
	public BitMapArray(int motherRange){
		ansArray = new BitMap[motherRange];
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
		for(int i = 0; i < ansArray.length ; i++){
			ansArray[i] = null;
		}
	}
	//-------------------------------------------------------------------------
	//　指定位置に値を足す
	//　注意、指定する位置パラメータは０から始まる！(範囲外は非対象化できる）
	//-------------------------------------------------------------------------
	public void add(int i ,int val){
		if ( i >= 0 && i < ansArray.length ){
			if (ansArray[i] ==null){
				ansArray[i] = new BitMap();
			}
			ansArray[i].turnOn(val);
		}else{
//			System.out.println("#RangeError ansArray.length:"+ansArray.length);
		}
	}
	//-------------------------------------------------------------------------
	//　指定位置の値をセットする
	//　注意、指定する位置パラメータは０から始まる！
	//-------------------------------------------------------------------------
	public void set(int i,BitMap val){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] = val;
		}
	}
	//-------------------------------------------------------------------------
	//　結果
	//-------------------------------------------------------------------------
	public String cnv2String(int arraySize){
		StringBuffer buf = new  StringBuffer();
//		int ttl = ansArray.length -1;
         for (int i = 0; i < ansArray.length; i++) {
 			if ( i > 0 ) buf.append(delim);
			buf.append(prefix);
			if (ansArray[i]!=null)		buf.append(ansArray[i].cnv2String(arraySize));
			buf.append(suffix);
		}
		return buf.toString();
	}
	//-------------------------------------------------------------------------
	//　使用例・・・
	//-------------------------------------------------------------------------
	public static void main(String[] argv){
		int ibmpArray = 7;
		BitMapArray bmpArray = new kyPkg.util.BitMapArray(ibmpArray);

		bmpArray.add(0,1);
		bmpArray.add(0,2);
		bmpArray.add(1,3);
		bmpArray.add(2,4);
		bmpArray.add(4,6);
		bmpArray.add(5,7);
		bmpArray.add(6,8);
		bmpArray.add(7,9);

		bmpArray.setEnclosure("'");
		System.out.println("Ans=>"+bmpArray.cnv2String(10));
	}
}
