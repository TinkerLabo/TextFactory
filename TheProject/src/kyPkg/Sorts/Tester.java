package kyPkg.Sorts;
import java.io.*;
import java.util.*;

import kyPkg.uFile.FileUtil;
/******************************************************************************
*	《 Tester 》 2007-05-11                        <BR>
*	モジュールをＧＵＩを使ってテストする
*	@quthor     Ken Yuasa
*	@version    Version 1.0
*	@since      SINCE java1.3
*******************************************************************************/
public class Tester{
	private static String gPath="";
//	private String encode="";
	private String delim="";
//	private int maxColm=-1;
	// 区切り文字はなにか？優先順位をつけるのがいいだろう スペースが多くなりそうなきがするが
	// 何からむ存在しそうか？（探索レベルで最大のものを返す）
	// エンコーディングはなにか判定できるといいなー
	class LittleObj {
//		private String encode="";
		private String delim="";
		private int maxColm=-1;
		public LittleObj (String delim){this.delim = delim;}
		public String getDelim() {return delim;}
		public void checkIt(String wRec) {
			String[] array = wRec.split(delim);
			if (this.maxColm < array.length) this.maxColm = array.length;
		}
		public int getMaxColm() {return maxColm;}
	}
	public String file49ers(String path,int limit) {
		System.out.println("#fileMining:"+path);
		File fl = new File(path);
		if ( fl.isFile()== false ) return "";
		int wCnt=0;
		String wRec="";
		ArrayList al = new ArrayList();
		al.add( new LittleObj(","));
		al.add( new LittleObj("\t"));
		al.add( new LittleObj(":"));
		al.add( new LittleObj(";"));
		al.add( new LittleObj(">"));
		al.add( new LittleObj(" "));
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready() && wCnt < limit) {
				wRec = br.readLine();
				if (wRec != null) {
					wCnt++;
					for(int i =0;i<al.size() ;i++){
						((LittleObj)al.get(i)).checkIt(wRec);
					}	
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		LittleObj maxObj = (LittleObj)Collections.max(al,new lObjComparator());
		System.out.println("maxObj:"+maxObj.getDelim());
		System.out.println("getMaxColm:"+maxObj.getMaxColm());
		return delim;
	}
	public static String getGPath() {
		return gPath;
	}
	class lObjComparator implements Comparator {
		@Override
		public int compare(Object o1,Object o2){
			LittleObj obj1 = (LittleObj)o1;
			LittleObj obj2 = (LittleObj)o2;
			return obj1.getMaxColm()- obj2.getMaxColm();
		}		
	}
}
