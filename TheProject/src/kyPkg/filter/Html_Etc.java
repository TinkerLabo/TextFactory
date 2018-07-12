package kyPkg.filter;
import java.util.*;
import java.io.*;

/*
	Html出力関連クラス
	@quthor	 Ken Yuasa
	@version	Version.1.0
*/
public class Html_Etc {
	/******************************************************************************
	*	Vectorをhtmlに書き出す<br>
	*	※追加書き込み可能にできないだろうか？？？
	*	@param	pPath	出力先ファイル
	*	@param	pSrc	読み込み元Vector
	*******************************************************************************/
	public static void vector2Html(String pPath,Vector pVec) {
		vector2Html(pPath,pVec,false);
	}
	public static void vector2Html(String outPath,Vector pVec,boolean append) {
		// System.out.println("■vector2Html■pPath:"+pPath);
		// makeParents(pPath);	// 親パスが無ければ作る
		int imgx = -1; 
		String wRec = "";
		String wLs = System.getProperty("line.separator");
		StringBuffer sBuf = new StringBuffer();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath,append));
			if ( bw != null) { 
				//-------------------------------------------------------------
				// HTML HEADER
				//-------------------------------------------------------------
				wRec = "<html><head></head>";	bw.write(wRec,0,wRec.length());
				wRec = "<BODY text='#555500' bgcolor='#dbcb5b' link='#ffffc1' vlink='#999999' alink='#cb9696'>";
				bw.write(wRec,0,wRec.length());
				wRec = "</body></html>";	bw.write(wRec,0,wRec.length());
				wRec = "<table border=1>";	bw.write(wRec,0,wRec.length());
				//-------------------------------------------------------------
				//int rByte = 0;
				String wStr = "";
				for (int i = 0; i < pVec.size(); i++) {
					Object wObj = pVec.get(i);
					sBuf.delete(0,sBuf.length()); //バッファをクリア
					sBuf.append("<tr>");
					if (wObj instanceof Vector){
						for (int j = 0; j< ((Vector)wObj).size(); j++) {
							wStr = ((Vector)wObj).get(j).toString();
							if (i==0 && wStr.equals("image")){imgx = j;} 
							if (i!=0 && imgx == j){
								String wPath = "./image/"+wStr;
								if ( new File(wPath).exists() ){
									wStr = "<img src=\"./image/" + wStr + "\" height=100 width=100>";
								}else{
									wStr = " ";
								}
							}
							sBuf.append("<td>");
							sBuf.append(wStr);
							sBuf.append("</td>");
						}
					} else {
							wStr = wObj.toString();
							sBuf.append("<td>");
							sBuf.append(wStr);
							sBuf.append("</td>");
					}
					sBuf.append("</tr>");
					sBuf.append(wLs);		//改行文字
					wRec = sBuf.toString();
					bw.write(wRec,0,wRec.length());
				}
				bw.close();
			}
		} catch (IOException ie){
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	} // End of vector2Html
}
