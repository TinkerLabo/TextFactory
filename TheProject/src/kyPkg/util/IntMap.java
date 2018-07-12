package kyPkg.util;
public class IntMap {
	private int[] ansArray;
	private String delim = ",";
	private String prefix = "";
	private String suffix = "";
	//-------------------------------------------------------------------------
	//�@�쐬����z��̃T�C�Y�������Ƃ���
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
	//�@�N���A����
	//-------------------------------------------------------------------------
	public void clear(){
		for(int i = 0;i<ansArray.length;i++){
			ansArray[i] = 0;
		}
	}
	//-------------------------------------------------------------------------
	//�@�w��ʒu�ɒl�𑫂�
	//�@���ӁA�w�肷��ʒu�p�����[�^�͂O����n�܂�I
	//-------------------------------------------------------------------------
	public void add(int i,int val){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] += val;
		}
	}
	//-------------------------------------------------------------------------
	//�@�w��ʒu�̒l���Z�b�g����
	//�@���ӁA�w�肷��ʒu�p�����[�^�͂O����n�܂�I
	//-------------------------------------------------------------------------
	public void set(int i,int val){
		if ( i >= 0 && i < ansArray.length ){
			ansArray[i] = val;
		}
	}
	//-------------------------------------------------------------------------
	//�@����
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
	//�@�g�p��E�E�E
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
