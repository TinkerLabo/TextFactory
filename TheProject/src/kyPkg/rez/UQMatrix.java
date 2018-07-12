package kyPkg.rez;

import java.util.HashMap;
public class UQMatrix {
	private StringBuffer buff = null;
	private HashMap<String, String> hmap = null;
	//-------------------------------------------------------------------------
	// ���j�[�N�ȃT���v�������J�E���g����ׂ̂�����
	//-------------------------------------------------------------------------
	public static void main(String[] argv){
		String[] monitor = {"john","john","john","Paul","Paul","Paul","Paul","george","ringo","ringo"}; 
		UQMatrix ins = new UQMatrix();
		int count = 0;
		for (int i = 0; i < monitor.length; i++) {
			if (!ins.isExists(1, 1, monitor[i])){
				count++;
			}
		}
		System.out.println("UQMatrix count:"+count);
	}
	public UQMatrix(){
		reset();
	}
	//-------------------------------------------------------------------------
	// �}�g���b�N�X���̂��̂��傫���ꍇ�͂������ς�邽��reset()����̂��x�^�[
	//-------------------------------------------------------------------------
	public void reset(){
		buff = new StringBuffer();
		hmap = new HashMap();
	}
	//-------------------------------------------------------------------------
	// �������j�^�[���������Y�}�g���b�N�X�Ƀ}�b�s���O����Ă���Ȃ�ς݁itrue�j���Ԃ�
	//-------------------------------------------------------------------------
	public boolean isExists(int dimX,int dimY,String monitorId){
		buff.delete(0, buff.length());
		buff.append(String.valueOf(dimX));
		buff.append("-");
		buff.append(String.valueOf(dimY));
		String key = buff.toString();
		String val = hmap.get(key);
		if (val!=null && val.equals(monitorId)){
			return true;
		}else{
			hmap.put(key,monitorId);
			return false;
		}
	}
}
