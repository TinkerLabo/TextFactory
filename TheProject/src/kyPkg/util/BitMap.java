package kyPkg.util;
public class BitMap {
	//�� long = 64bit�Ȃ̂ł���ȏ�͊i�[�ł��܂���
	private static char negativeChar =  '0';
	private static char positiveChar =  '1';	
	private long longMap;
	//-------------------------------------------------------------------------
	//�@�쐬����z��̃T�C�Y�������Ƃ���
	//-------------------------------------------------------------------------
	public BitMap(){
		longMap = 0L;
		clear();
	}
	public void setNegativeChar(char xchar) {
		negativeChar = xchar;
		clear();
	}
	public void setPositiveChar(char xchar) {
		positiveChar = xchar;
	}
	//-------------------------------------------------------------------------
	//�@�N���A����
	//-------------------------------------------------------------------------
	public void clear(){
		longMap = 0L;
	}
	public void or(long val){
		longMap = longMap | (val);
	}
	//-------------------------------------------------------------------------
	//�@�w��ʒu���n�m�ɂ���(OR)
	//�@���ӁA�w�肷��ʒu�p�����[�^�͂O����n�܂�I
	//-------------------------------------------------------------------------
	public void turnOn(int val){
		longMap = longMap | (1L <<val);
	}
	//-------------------------------------------------------------------------
	//�@�w��ʒu���n�e�e�ɂ���(XOR)
	//�@���ӁA�w�肷��ʒu�p�����[�^�͂O����n�܂�I
	//-------------------------------------------------------------------------
	public void turnOff(int val){
		longMap = longMap ^  (1L <<val);
	}
	//-------------------------------------------------------------------------
	//�@����
	//-------------------------------------------------------------------------
	public String cnv2String(int arraySize){
		//�� long = 64bit
		if (arraySize> 64) arraySize=64;
		return bitMap2String(longMap,arraySize);
	}
	//-------------------------------------------------------------------------
	//�@long   => char[] => String
	//-------------------------------------------------------------------------
	public static String bitMap2String(long longMap,int arraySize){
		long mask = 1L;
		//System.out.println("longMap=>"+longMap);
		char[] ansArray= new char[arraySize];
		for (int i = 0; i < arraySize; i++) {
			mask = 1L <<i;
			//System.out.println("mask=>"+mask);
//			if ((longMap & mask)==0L){
//				ansArray[(arraySize-1)-i]=negativeChar;
//			}else{
//				ansArray[(arraySize-1)-i]=positiveChar;
//			}
			if ((longMap & mask)==0L){
				ansArray[i]=negativeChar;
			}else{
				ansArray[i]=positiveChar;
			}
		}
		return new String(ansArray);
	}
	//-------------------------------------------------------------------------
	//�@String => char[] => long
	//-------------------------------------------------------------------------
	public static long string2BitMap(String str){
		long mask = 1L;
		char[] ansArray = str.toCharArray() ;
		int arraySize=ansArray.length;
		long longMap = 0L;
		for (int i = 0; i < ansArray.length; i++) {
			mask = 1L <<i;
			if(ansArray[(arraySize-1)-i]==positiveChar){
				longMap =  (longMap | mask);
			}
		}
		return longMap;
	}
	//-------------------------------------------------------------------------
	//�@�g�p��E�E�E
	//-------------------------------------------------------------------------
	public static void main(String[] argv){
		int arraySize = 64;
		BitMap insMap = new kyPkg.util.BitMap();
		insMap.turnOn(0);
		insMap.turnOn(1);
		insMap.turnOn(2);
		insMap.turnOn(3);
		insMap.turnOn(4);
		//ins.turnOn(5);
//		ins.turnOn(6);
		//ins.turnOn(7);
//		ins.turnOn(8);
		insMap.turnOn(61);
		insMap.turnOn(62);
		insMap.turnOn(63);
		
		String ans = insMap.cnv2String(arraySize);
		System.out.println("Ans1=>"+ans);
		
		long longMap = string2BitMap(ans);
		System.out.println("lmap=>"+longMap);

		ans = bitMap2String(longMap,32);
		System.out.println("Ans2=>"+ans);
	}
}
