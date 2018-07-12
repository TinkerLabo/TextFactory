package kyPkg.util;
import java.util.*; 
public class ThreadUtil{
	private static Map indentMap = new HashMap(); 
	//-------------------------------------------------------------------------
	// 追加された順を幅にＭＡＰに格納＆ インデント文字列を返す
	//-------------------------------------------------------------------------
	private static String getIndent(Object key) { 
		String s = (String)indentMap.get(key); 
		if (s == null) { 
			int length = indentMap.size(); 
			s = makeIndent(length,"￥t"); 
			indentMap.put(key,s); 
		} 
		return s; 
	} 
	//-------------------------------------------------------------------------
	// 渡された長さ分指定文字を繰り返した文字列を返す
	//-------------------------------------------------------------------------
	public static String makeIndent(int length,String pPad) { 
		StringBuffer sb = new StringBuffer(); 
		for (int i = 0; i < length; i++) { 
			sb.append(pPad); 
		} 
		return new String(sb); 
	}
	//-------------------------------------------------------------------------
	//スレッドごとにインデントを変えてメッセージを表示する
	//-------------------------------------------------------------------------
	public static void threadPrintln(String s) { 
		Object key = Thread.currentThread(); 
		String indent = getIndent(key); 
		System.out.println(indent+s); 
	}

	//-------------------------------------------------------------------------
	// 現在アクティブなスレッドの一覧
	//-------------------------------------------------------------------------
	public static void printAllThreads(String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("---- "+msg+" ----\n");
		Thread current = Thread.currentThread();

		// 現行スレッドグループ内のアクティブなスレッド数を返す
		int count = Thread.activeCount();
		Thread[] list = new Thread[count];

		//アクティブなスレッドを、指定された配列にコピーする
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
	// 実験用のランダムなスリープ
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
