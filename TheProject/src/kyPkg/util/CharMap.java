package kyPkg.util;
public class CharMap {
	private char[] ansArray;
	private char negativeChar =  '0';
	private char positiveChar =  '1';
	//-------------------------------------------------------------------------
	//�@�쐬����z��̃T�C�Y�������Ƃ���
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
	//�@�N���A����
	//-------------------------------------------------------------------------
	public void clear(){
		for(int i = 0;i<ansArray.length;i++){
			ansArray[i] = negativeChar;
		}
	}
	//-------------------------------------------------------------------------
	//�@�w��ʒu���n�m�ɂ���
	//�@���ӁA�w�肷��ʒu�p�����[�^�͂O����n�܂�I
	//-------------------------------------------------------------------------
	public void turnOn(int i){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] = positiveChar;
		}
	}
	//-------------------------------------------------------------------------
	//�@�w��ʒu���n�e�e�ɂ���
	//�@���ӁA�w�肷��ʒu�p�����[�^�͂O����n�܂�I
	//-------------------------------------------------------------------------
	public void turnOff(int i){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] = negativeChar;
		}
	}
	//-------------------------------------------------------------------------
	//�@����
	//-------------------------------------------------------------------------
	public String cnv2String(){
		return new String(ansArray);
	}
	//-------------------------------------------------------------------------
	//�@�g�p��E�E�E
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
