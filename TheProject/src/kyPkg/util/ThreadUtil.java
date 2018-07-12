package kyPkg.util;
import java.util.*; 
public class ThreadUtil{
	private static Map indentMap = new HashMap(); 
	//-------------------------------------------------------------------------
	// �ǉ����ꂽ���𕝂ɂl�`�o�Ɋi�[�� �C���f���g�������Ԃ�
	//-------------------------------------------------------------------------
	private static String getIndent(Object key) { 
		String s = (String)indentMap.get(key); 
		if (s == null) { 
			int length = indentMap.size(); 
			s = makeIndent(length,"��t"); 
			indentMap.put(key,s); 
		} 
		return s; 
	} 
	//-------------------------------------------------------------------------
	// �n���ꂽ�������w�蕶�����J��Ԃ����������Ԃ�
	//-------------------------------------------------------------------------
	public static String makeIndent(int length,String pPad) { 
		StringBuffer sb = new StringBuffer(); 
		for (int i = 0; i < length; i++) { 
			sb.append(pPad); 
		} 
		return new String(sb); 
	}
	//-------------------------------------------------------------------------
	//�X���b�h���ƂɃC���f���g��ς��ă��b�Z�[�W��\������
	//-------------------------------------------------------------------------
	public static void threadPrintln(String s) { 
		Object key = Thread.currentThread(); 
		String indent = getIndent(key); 
		System.out.println(indent+s); 
	}

	//-------------------------------------------------------------------------
	// ���݃A�N�e�B�u�ȃX���b�h�̈ꗗ
	//-------------------------------------------------------------------------
	public static void printAllThreads(String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("---- "+msg+" ----\n");
		Thread current = Thread.currentThread();

		// ���s�X���b�h�O���[�v���̃A�N�e�B�u�ȃX���b�h����Ԃ�
		int count = Thread.activeCount();
		Thread[] list = new Thread[count];

		//�A�N�e�B�u�ȃX���b�h���A�w�肳�ꂽ�z��ɃR�s�[����
		int n = Thread.enumerate(list);
		for (int i = 0; i < n; i++) {
			if (list[i].equals(current)) {
				sb.append("*");
			} else {
				sb.append(" ");
			}
			sb.append(list[i]+"\n");
		}	
		System.out.print(sb);	
	}
	//-------------------------------------------------------------------------
	// �����p�̃����_���ȃX���[�v
	//-------------------------------------------------------------------------
	static public void randomSleep(long min){
		long s = (long)(Math.random() * min);
		try{
			Thread.sleep(s);
		}catch(InterruptedException ie){
			ie.printStackTrace();
		}
	}
}
