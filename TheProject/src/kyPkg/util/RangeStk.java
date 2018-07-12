package kyPkg.util;
import java.util.*;
//-----------------------------------------------------------------------------
// �ϐ��͈̔͂��`�F�b�N����N���X
//�i�\�߁A�����W�p�����[�^�𕪐͂��čő�l�A�ŏ��l���n�b�V���e�[�u�������Ă����j
// �s�g�p��t
//	RangeStk rs = new RangeStk();
//	rs.addRange("(01�`16)");
//	System.out.println("a 16?"+ rs.rangeCheck("(01�`16)","16") );
//-----------------------------------------------------------------------------
public class RangeStk{
	//-------------------------------------------------------------------------
	// MyRange
	//-------------------------------------------------------------------------
	class MyRange{
		int max = -1;
		int min = -1;
		public MyRange(int max,int min){
			if ( max > min ){
				this.max = max;
				this.min = min;
			}else{
				this.max = min;
				this.min = max;
			}
		}
		public int getMax(){ return this.max; }
		public int getMin(){ return this.min; }
	}
	//-------------------------------------------------------------------------
	Hashtable ht;
	public RangeStk(){
		ht = new Hashtable();
	}
	public boolean addRange(String wPtn){
		if (ht.contains(wPtn)) return true; // ���ɑ��݂���
		if(wPtn.matches("[(]\\d\\d+[�`]\\d\\d+[)].*")){
			String wAry[] = wPtn.split("[(�`)]");
			if (wAry.length>=3){
				try {
					int iMax = Integer.parseInt(wAry[2]);
					int iMin = Integer.parseInt(wAry[1]);
					//System.out.println(" min:"+iMin+" max:"+iMax);
					ht.put(wPtn,new MyRange(iMin,iMax));
					return true;
				}catch(NumberFormatException ne){
					return false;
				}
			}
			return false;
		}
		return false;
	}
	public int getRangeMax(String wPtn){
		MyRange mr = (MyRange)ht.get(wPtn);
		if (mr==null) return -1; // ���݂��Ȃ��ꍇ
		return mr.getMax();
	}
	public int getRangeMin(String wPtn){
		MyRange mr = (MyRange)ht.get(wPtn);
		if (mr==null) return -1;
		return mr.getMin();
	}
	//-------------------------------------------------------------------------
	//	wVal�������W�p�^�[�����̒l���ǂ������ׂ�
	//	System.out.println("a 16?"+ rs.rangeCheck("(01�`16)","16") );
	//-------------------------------------------------------------------------
	public boolean rangeCheck(String wPtn,String wVal){
		try {
			int iVal = Integer.parseInt(wVal);
			MyRange mr = (MyRange)ht.get(wPtn);
			if (mr==null) {
				System.out.println("�����W�p�^�[�������݂��܂���F"+wPtn);
				return false; 
			}
			return (mr.getMax() >= iVal && iVal >= mr.getMin());
		}catch(NumberFormatException ne){
			System.out.println("NumberFormatException"+wVal);
			return false;
		}
	}
}
