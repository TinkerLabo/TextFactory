package kyPkg.util;
import java.util.*;
import java.io.*;
//-----------------------------------------------------------------------------
// �o�̓X�g���[�����p�^�[�����Ƃɏ���������
// �s�g�p��t
//	StreamStk ssObj = new StreamStk(path);
//	ssObj.addPattern("beer.dat");
//	ssObj.write("beer.dat",wRec);
//  ���̂܂܂Ńt�@�C�i���C�Y����������邩�m�F����
//-----------------------------------------------------------------------------
public class StreamStk{
	Hashtable ht;
	String outDir;
	//-------------------------------------------------------------------------
	public StreamStk(String pPath){
		//---------------------------------------------------------------------
		// pPath ���̓t�@�C���Ɠ����̃t�H���_���쐬���Ă�����o��dir�Ƃ���
		//---------------------------------------------------------------------
		String wPathx[] = pPath.split("\\."); //�� split �̈����� Regix 
		File wF = new File(wPathx[0]);
		wF.mkdirs();
		if(wF.exists()){
			this.outDir = wF.getAbsolutePath() + "\\";
			this.ht = new Hashtable();
		}
	}
	//-------------------------------------------------------------------------
	// �p�^�[���ɑΉ�����o�̓X�g���[�����쐬���� ���̂P
	// ���g���q�Ɋ֌W�Ȃ���؂蕶�����w�肵�����ꍇ�Ɏg�p����
	//-------------------------------------------------------------------------
	public boolean addPattern(String wPtn,String delimita){
		if( wPtn.trim().equals("") )return false;
		if (ht.contains(wPtn)) return true; // ���ɑ��݂���
		try {
			String wPath = this.outDir + wPtn;
			ht.put(wPtn,new BuffWriter(wPath,delimita));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	//-------------------------------------------------------------------------
	// �p�^�[���ɑΉ�����o�̓X�g���[�����쐬���� ���̂Q
	// ���g���q�Ɋ֘A������؂蕶�����ݒ肳���
	//-------------------------------------------------------------------------
	public boolean addPattern(String wPtn){
		if( wPtn.trim().equals("") )return false;
		if (ht.contains(wPtn)) return true; // ���ɑ��݂���
		try {
			String wPath = this.outDir + wPtn;
			ht.put(wPtn,new BuffWriter(wPath));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	//-------------------------------------------------------------------------
	// �o�b�t�@�[�𖳎����ăf�[�^�������o���i�o�b�t�@�[�̓N���A�����j
	//-------------------------------------------------------------------------
	public void write(String wPtn,String wRec){
		//System.out.println("��wPtn:"+wPtn);
		//System.out.println("��wRec:"+wRec);
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			mr.write(wRec);
		}else{
			System.out.println("# StreamStk # on write # not found:"+wPtn);
		}
	}
	//-------------------------------------------------------------------------
	// �o�b�t�@�������o��
	//-------------------------------------------------------------------------
	public void write(String wPtn){
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			mr.write();
		}else{
			System.out.println("# StreamStk # on write # not found:"+wPtn);
		}
	}
	//-------------------------------------------------------------------------
	// �o�b�t�@�ɒǉ�����
	//-------------------------------------------------------------------------
	public void append(String wPtn,String wRec){
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			mr.append(wRec);
		}else{
			System.out.println("# StreamStk # on write # not found:"+wPtn);
		}
	}
	//-------------------------------------------------------------------------
	// �����o����������Ԃ�
	//-------------------------------------------------------------------------
	public int getWriteCount(String wPtn){
		int wRtn = -1;
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			wRtn = mr.getWriteCount();
		}else{
			System.out.println("# StreamStk # on write # not found:"+wPtn);
		}
		return wRtn;
	}
	//-------------------------------------------------------------------------
	// �e�h�m�`�k�h�y�d�̂���E�E�E���܂��Ă΂�Ă��Ȃ�
	//-------------------------------------------------------------------------
	@Override
	protected void finalize(){
		System.out.println("# StreamStk # on ���e�h�m�`�k�h�y�d��");
		closeAll();
	}
	//-------------------------------------------------------------------------
	// �X�^�b�N����Ă���Writer���ׂĂ̏�����������E�E�E�����ǁE�E�E
	//-------------------------------------------------------------------------
	public void initAll(String wVal){
		for (Enumeration wenum = ht.elements(); wenum.hasMoreElements() ;) {
			BuffWriter mr = (BuffWriter)wenum.nextElement();
			if ( mr!=null ){
				mr.append(wVal);
			}
		}
	}
	//-------------------------------------------------------------------------
	// �o�b�t�@�[����ł͂Ȃ�Writer�����ׂď����o��
	//-------------------------------------------------------------------------
	public void writeAll(){
		for (Enumeration wenum = ht.elements(); wenum.hasMoreElements() ;) {
			BuffWriter mr = (BuffWriter)wenum.nextElement();
			if ( mr!=null ){ mr.write(); }
		}
	}
	//-------------------------------------------------------------------------
	// ���ׂẴo�b�t�@�[�����Z�b�g����
	//-------------------------------------------------------------------------
	public void resetAll(){
		for (Enumeration wenum = ht.elements(); wenum.hasMoreElements() ;) {
			BuffWriter mr = (BuffWriter)wenum.nextElement();
			if ( mr!=null ){ mr.reset(); }
		}
	}
	//-------------------------------------------------------------------------
	// �o�b�t�@�[�����Z�b�g����i�w�肵���p�^�[���̂݁j
	//-------------------------------------------------------------------------
	public void reset(String wPtn){
		BuffWriter mr = (BuffWriter)ht.get(wPtn);
		if ( mr!=null ){
			mr.reset();
		}else{
			System.out.println("# StreamStk # on reset # not found:"+wPtn);
		}
	}
	//-------------------------------------------------------------------------
	// ���ׂĂ�Writer�����
	//-------------------------------------------------------------------------
	public void closeAll(){
		for (Enumeration wenum = ht.elements(); wenum.hasMoreElements() ;) {
			BuffWriter mr = (BuffWriter)wenum.nextElement();
			if ( mr!=null ){ mr.close(); }
		}
	}
	//#########################################################################
	//# BuffWriter�����N���X
	//#########################################################################
	class BuffWriter{
		String wLS = System.getProperty("line.separator");
		StringBuffer wDefBuf;
		FileWriter fr  = null;
		int writeCount = 0;
		String delimita = "";
		//---------------------------------------------------------------------
		// �R���X�g���N�^
		//---------------------------------------------------------------------
		public BuffWriter(String wPtn,String delimita){
			try{
				fr = new FileWriter(wPtn);
				wDefBuf = new StringBuffer();
			}catch(IOException ie){
				ie.printStackTrace();
			}
			this.delimita = delimita;
		}
		//---------------------------------------------------------------------
		public BuffWriter(String wPtn){
			// �g���q�ɂ��A��؂蕶���̔���
			if (wPtn.indexOf(".")==-1) wPtn = wPtn + ".dat";
			String[] val = wPtn.split("\\.");   // �� split �̈����� Regix 
			String wExt = val[1].toUpperCase(); // �s���I�h�̒���`�i�g���q�j
			delimita = "";
			//System.out.println("�g���q�́F"+wExt);
			if      (wExt.equals("DAT")){ delimita = "";
			}else if(wExt.equals("TXT")){ delimita = "_";
			}else if(wExt.equals("CSV")){ delimita = ",";
			}else if(wExt.equals("PRN")){ delimita = "\t";
			}else{                        delimita = "";
			}
			try{
				fr = new FileWriter(wPtn);
				wDefBuf = new StringBuffer();
			}catch(IOException ie){
				ie.printStackTrace();
			}
		}
		//---------------------------------------------------------------------
		// ���������o�������H
		//---------------------------------------------------------------------
		public int getWriteCount() {return writeCount;}
		//---------------------------------------------------------------------
		// �����̕�����������o��
		//---------------------------------------------------------------------
		public void write(String wRec){
			writeCount++;
			try{
				fr.write(wRec);
				fr.write(wLS);     // ���s�R�[�h
			}catch(IOException ie){
				ie.printStackTrace();
			}
			reset();
			//wDefBuf.delete(0, wDefBuf.length());
		}
		//---------------------------------------------------------------------
		// �o�b�t�@�[�����Z�b�g����
		//---------------------------------------------------------------------
		public void reset(){
			wDefBuf.delete(0, wDefBuf.length());
		}
		//---------------------------------------------------------------------
		// �o�b�t�@�[���f�[�^�������o��
		//---------------------------------------------------------------------
		public void write(){
			if(wDefBuf.length()>0) { write(wDefBuf.toString()); }
		}
		//---------------------------------------------------------------------
		// �o�b�t�@�[�Ƀf�[�^��ǉ�����
		//---------------------------------------------------------------------
		public void append(String wVal){
			wDefBuf.append(wVal);
			if (!delimita.equals("")) wDefBuf.append(delimita);
		}
		//---------------------------------------------------------------------
		public void close(){
			try{ fr.close();
			}catch(IOException ie){ ie.printStackTrace(); }
		}
	}
}
