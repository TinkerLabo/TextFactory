package kyPkg.filter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;
public class Flt_ReSeqence extends Abs_BaseTask {
	private static final String LF = System.getProperty("line.separator");
	private String format = "00000000";
	private String pPath_O = "";
	private String path = "";
	private String delim_i = "";
	private String delim_o = "";
	private int target = 0;
	private int skip = 0;
	private long wCnt = 0;
	//-------------------------------------------------------------------------
	//　使い方　
	// new Filters().fltSubstring("out","In","\t");
	//-------------------------------------------------------------------------
	public static void main(String[] args){
		System.out.println("バッチ起動する場合の例");
		String rootDir = globals.ResControl.getQprRootDir();
		Flt_ReSeqence ins = new Flt_ReSeqence(rootDir+"Qtb2.TXT");
		ins.execute();
	}

	public Flt_ReSeqence(String pPath_I){
//		File49ers f49 = new File49ers(pPath_I,20);  
//		System.out.println("getDelim:"+f49.getDelim());
		this(pPath_I,pPath_I,new File49ers(pPath_I).getDelimiter(),0,0,"0000000");
	}
	public Flt_ReSeqence(String pPath_O,String pPath_I,String delim,int target,int skip,String format){
		this.pPath_O 	= pPath_O;  //　出力ファイルのパス c\Monitor_Data\test0111.txt	
		this.path 	= pPath_I;  	//　入力ファイルのパス c\Monitor_Data\Qpr_monitor_info_20071126.csv	
		this.delim_i   	= delim;		//　入力ファイルの区切り文字
		this.target    	= target;		//　対象とする列（いちばん先頭の列は０）
		this.skip    	= skip;		//　読み飛ばし指定（一ライン目がコメント行の場合などに使用する）
		this.delim_o   	= delim;		//　出力ファイルの区切り文字
		this.format 	= format;  	//　シーケンスに使うフォーマット　標準は0000000	
	}

	//-------------------------------------------------------------------------
	@Override
	public void execute(){
		super.setMessage("ReSequence開始");

		
		wCnt = 1;
		DecimalFormat df = new DecimalFormat(format);
		String wRec = "";
		File file  = new File(path);
		StringBuffer buf = new StringBuffer();
		if ( file.isFile() ){
			try{
				String wPath_T = path + ".tmp";  		// .tmpが無いという前提
				FileWriter fw1 = new FileWriter(wPath_T);
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(file));
				for (int ix = 0 ; ix < skip;ix++){
					System.out.println("#skip!");				// skip　指定行数分空読みする
					wRec = br.readLine();
				}
				while( (wRec = br.readLine()) != null ){
					buf.delete(0, buf.length());
					String[] splited = wRec.split(delim_i);
					for (int i = 0; i < splited.length; i++) {
						if (i==target){
							System.out.println("=>"+ df.format(wCnt));
							buf.append(df.format(wCnt));
						}else{
							buf.append(splited[i]);
						}
						buf.append(delim_o);
					}
					buf.append(LF);
					fw1.write(buf.toString());
					wCnt++;
				}
				br.close();
				fw1.close();
				File wFile_T  = new File(wPath_T);
				File wFile_O  = new File(pPath_O);
				// もし入力パスと出力パスが同じ場合には．．．
				if(file.equals(pPath_O)){
					file.renameTo(new File(path+".bak"));  // バックアップを残す？
				}
				if (wFile_O.exists()) wFile_O.delete(); //ちゃんと消せるのかどうか、上書きｏｋか要確認かも
				wFile_T.renameTo(wFile_O);
				System.out.println("#処理→"+pPath_O);
			}catch(IOException ie){
				ie.printStackTrace();
				abend();
			}catch(Exception e){
				e.printStackTrace();
				abend();
			}
		}
	}
}
