package kyPkg.util;
public class BitMapArray {
	private BitMap[] ansArray;
	private String delim = ",";
	private String prefix = "";
	private String suffix = "";
	//-------------------------------------------------------------------------
	//�@�쐬����z��̃T�C�Y�������Ƃ���
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
	//�@�N���A����
	//-------------------------------------------------------------------------
	public void clear(){
		for(int i = 0; i < ansArray.length ; i++){
			ansArray[i] = null;
		}
	}
	//-------------------------------------------------------------------------
	//�@�w��ʒu�ɒl�𑫂�
	//�@���ӁA�w�肷��ʒu�p�����[�^�͂O����n�܂�I(�͈͊O�͔�Ώۉ��ł���j
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
	//�@�w��ʒu�̒l���Z�b�g����
	//�@���ӁA�w�肷��ʒu�p�����[�^�͂O����n�܂�I
	//-------------------------------------------------------------------------
	public void set(int i,BitMap val){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] = val;
		}
	}
	//-------------------------------------------------------------------------
	//�@����
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
	//�@�g�p��E�E�E
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
