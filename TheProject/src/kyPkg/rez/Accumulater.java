package kyPkg.rez;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import kyPkg.uFile.FileUtil;
public class Accumulater {

	private  static boolean DEBUG = true;
	final String wLs = System.getProperty("line.separator");
	private String oPath_Pos;
	private String oPath_Neg;
//	private static String[] wFlgs;
//	private static String[] wVals;

	//-------------------------------------------------------------------------
	// ����t�@�C���̔C�ӂ̈ʒu�ɂ���f�[�^�����j�[�N�ȏW���Ɋi�[����
	// ���̏W�����p�����[�^��edebug01�Ńf�[�^�̐U�蕪���������s��
	//-------------------------------------------------------------------------
	public HashList_Occ parmIncore(String path,String pDlm,int pkeyCol,int pValCol) {
		String wRec;
		int lcnt=0;
		File fl = new File(path);
		if ( fl.exists() == false ){
			System.out.println("@Flt_Edebug of parmIncore FileNotFound:"+path);
			return null;
		} 
		HashList_Occ wHSet = new HashList_Occ();
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] wArray = wRec.split(pDlm);
					if (wArray.length > pkeyCol) {
						wHSet.put(wArray[pkeyCol],wArray[pValCol]);
						lcnt++;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return wHSet;
	}

	//-------------------------------------------------------------------------
	// �Y���p�X�̃t�@�C����dlm�ŋ�؂�Acol�ʒu�̒l��wHSet�ɐݒ�@hashtree�ł��悢�����E�E
	//-------------------------------------------------------------------------
	public int matching(String outDir,String path,HashSet pHs,String tDlm,int tCol,int opt1) {
		if(pHs==null)    	return -1;
		if(pHs.size()>0)	return -1;
		File fl = new File(path);
		if ( fl.exists() == false ){
			System.out.println("��FileNotFound:"+path);
			return -1;
		} 
		pHs.remove(null);	// null�͑ΏۊO�Ƃ��Ă���
		if(DEBUG) {
			Iterator it = pHs.iterator();
			while(it.hasNext()) System.out.println("Accumulater�@debug=>"+it.next());
		}
		boolean sw1 = false;
		boolean sw2 = false;
		if ((opt1 & 1) == 1 ) sw1 = true;
		if ((opt1 & 1) == 2 ) sw2 = true;
		String firstname = FileUtil.getFirstName2(path);
		FileWriter fwPos=null;
		FileWriter fwNeg=null;
		String wRec;
		String wKey;
		int negCnt = 0;
		int posCnt = 0;
		try {
			if (sw1){
				oPath_Pos = outDir + firstname + ".pos";  // .pos�������Ƃ����O��
				System.out.println("oPath_Pos��"+oPath_Pos);
				fwPos = new FileWriter(oPath_Pos);
			}
			if (sw2){
				oPath_Neg = outDir + firstname + ".neg";  // .neg�������Ƃ����O��
				System.out.println("oPath_Neg��"+oPath_Neg);
				fwNeg = new FileWriter(oPath_Neg);
			}
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				//System.out.println("��wRec:"+wRec);
				if (wRec != null) {
					String[] wArray = wRec.split(tDlm);
					if (wArray.length > tCol){
						wKey = wArray[tCol];
						if (pHs.contains(wKey)){
							posCnt++;
							System.out.println("Exsist:"+wKey);
							if(fwPos!=null) fwPos.write(wRec + wLs);
						}else{
							negCnt++;
							System.out.println("not Exsist:"+wKey);
							if(fwNeg!=null) fwNeg.write(wRec + wLs);
						}
					}
				}
			}
			br.close();
			if(fwPos!=null) fwPos.close();
			if(fwNeg!=null) fwNeg.close();
			System.out.println("positive:"+posCnt);
			System.out.println("negative:"+negCnt);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		if ( opt1  == 2 ) return  negCnt;
		return  posCnt;
	}

	public static void main(String[] args){


//		//id �������ԃ��[�v
//		wFlgs = new String[list.size()];
//		wVals = new String[list.size()];
//		for (int i = 0; i < list.size(); i++) {
//			Integer x =(Integer)hashMap.get("Code:"+i);
//			int index = x.intValue();
//			wFlgs[index] = "1";
//			wVals[index] = "�l";
//			System.out.println("get1:"+list.get(index));
//		}
	}

}
