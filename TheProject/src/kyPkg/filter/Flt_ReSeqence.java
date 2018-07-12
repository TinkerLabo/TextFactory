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
	//�@�g�����@
	// new Filters().fltSubstring("out","In","\t");
	//-------------------------------------------------------------------------
	public static void main(String[] args){
		System.out.println("�o�b�`�N������ꍇ�̗�");
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
		this.pPath_O 	= pPath_O;  //�@�o�̓t�@�C���̃p�X c\Monitor_Data\test0111.txt	
		this.path 	= pPath_I;  	//�@���̓t�@�C���̃p�X c\Monitor_Data\Qpr_monitor_info_20071126.csv	
		this.delim_i   	= delim;		//�@���̓t�@�C���̋�؂蕶��
		this.target    	= target;		//�@�ΏۂƂ����i�����΂�擪�̗�͂O�j
		this.skip    	= skip;		//�@�ǂݔ�΂��w��i�ꃉ�C���ڂ��R�����g�s�̏ꍇ�ȂǂɎg�p����j
		this.delim_o   	= delim;		//�@�o�̓t�@�C���̋�؂蕶��
		this.format 	= format;  	//�@�V�[�P���X�Ɏg���t�H�[�}�b�g�@�W����0000000	
	}

	//-------------------------------------------------------------------------
	@Override
	public void execute(){
		super.setMessage("ReSequence�J�n");

		
		wCnt = 1;
		DecimalFormat df = new DecimalFormat(format);
		String wRec = "";
		File file  = new File(path);
		StringBuffer buf = new StringBuffer();
		if ( file.isFile() ){
			try{
				String wPath_T = path + ".tmp";  		// .tmp�������Ƃ����O��
				FileWriter fw1 = new FileWriter(wPath_T);
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(file));
				for (int ix = 0 ; ix < skip;ix++){
					System.out.println("#skip!");				// skip�@�w��s������ǂ݂���
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
				// �������̓p�X�Əo�̓p�X�������ꍇ�ɂ́D�D�D
				if(file.equals(pPath_O)){
					file.renameTo(new File(path+".bak"));  // �o�b�N�A�b�v���c���H
				}
				if (wFile_O.exists()) wFile_O.delete(); //�����Ə�����̂��ǂ����A�㏑���������v�m�F����
				wFile_T.renameTo(wFile_O);
				System.out.println("#������"+pPath_O);
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
