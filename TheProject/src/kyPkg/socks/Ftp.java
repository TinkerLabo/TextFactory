package kyPkg.socks;
import org.apache.commons.logging.*;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.JList;

import kyPkg.uFile.FileUtil;
//------------------------------------------------------------------------------
//�@singleton�ɂ��������悢�����m��Ȃ��E�E�E�y���f�B���O
//------------------------------------------------------------------------------
// ftp�v���O���� Ftp.java
// ���̃v���O�����́Cftp�T�[�o�Ɛڑ����ăt�@�C���]�����s���܂�
// �g���� java Ftp �T�[�o�A�h���X
// �N���̗� java Ftp 10.6.20.7
//------------------------------------------------------------------------------
//�����O�������@���l����E�E
//���v���L�V�o�R�ɂ��čl����
//------------------------------------------------------------------------------
// �s FTP�T�[�r�X�R�}���h�ꗗ �t
//   ABOR    ���s���̃f�[�^�]���̒��~
//   ACCT    �A�J�E���g�̎w��
//   ALLO    �w�肵���T�C�Y�̃t�@�C���̈��]���O�Ɋm�ۂ���B
//   APPE    �����̃t�@�C���ɒǉ��B�Ȃ���ΐV�K�쐬�B
//   CDUP    �e�f�B���N�g���̎w��
// x CWD     ��ƃf�B���N�g���̎w��
//   DELE    �t�@�C���̍폜
// x HELP    �w���v�̕\��
// x LIST    �f�B���N�g�����̃t�@�C�����X�g�܂��̓t�@�C�����
// x MKD     �f�B���N�g���̍쐬
//   MODE    �]�����[�h�̎w��BS:�X�g���[�� B:�u���b�N C:���k
// x NLST    �f�B���N�g�����̃t�@�C�����X�g
// x NOOP    �������Ȃ�
// x PASS    �p�X���[�h�̎w��
//   PASV    �p�b�V�u�E���[�h�̊J�n
// x PORT    �f�[�^�]���p�̃|�[�g�̎w��
// x PWD     �J�����g�E�f�B���N�g���̕\��
// x QUIT    �ڑ��I��
//   REIN    �V�K�ڑ���Ԃɖ߂�
//   REST    �t�@�C���]���̍ĊJ���w��
// x RETR    �f�[�^�]���J�n
// x RMD     �f�B���N�g���̍폜
//   RNFR    ���l�[���̑ΏۂɂȂ�t�@�C���̎w��
//   RNTO    �V�����t�@�C�����̎w��B�q�m�e�q�Ƒ΂Ŏg���B
// x SITE    ����̃T�C�g�p�̃R�}���h�̎w��
//   SMNT    �ʂ̃t�@�C���V�X�e���\���̃}�E���g
// x STAT    �T�[�o�[�̏��
// x STOR    �f�[�^�̎�M�ƕۑ�
//   STOU    ��M�����t�@�C�����J�����g�f�B���N�g���ŏd�����Ȃ����O�ŕۑ�
//   STRU    �t�@�C���X�g���N�`���̎w��BF:File R:Record P:Page
// x SYST    �T�[�o�[�̂n�r�^�C�v�̗v��
// x TYPE    �^�C�v�̎w��BASCII,EBCDIC,IMAGE�Ȃ�
// x USER    ���[�U�[���̎w��
//------------------------------------------------------------------------------
// Ftp�N���X
//------------------------------------------------------------------------------
public class Ftp {
//	public static Log log = LogFactory.getLog("kyPkg.util.Ftp.class");
	public static final String APPENDA_CLASS = "kyPkg.socks";
	public static Log log = LogFactory.getLog(Ftp.APPENDA_CLASS);

	//	log.fatal("fatal���b�Z�[�W");
//	log.error("error���b�Z�[�W");
//	log.warn ("warn ���b�Z�[�W");
//	log.info ("info ���b�Z�[�W");
//	log.debug("debug���b�Z�[�W");
//	log.trace("trace���b�Z�[�W");
	private boolean connected = false; 
	private String gOStype = "";
	private String gCDir   = "";
	//private Vector gLVec   = null;
	private String gLs = System.getProperty("line.separator");
	private Socket gSocket;          // ����@�\�P�b�g
	private PrintWriter    stream_o; // ����@�o�͗p�X�g���[��
	private BufferedReader stream_i; // ����@���͗p�X�g���[��
	private final int CTRLPORT = 21; // ftp��WellKnown�|�[�g
	private int cPORT = CTRLPORT;
	private CtrlListen ctrllisten = null;
	private Thread listenerthread;
    private JList list_Srv = null;
    //--------------------------------------------------------------------------
	// �A�N�Z�b�T
	//--------------------------------------------------------------------------
	public void setList_Srv(JList list_Srv) {
		this.list_Srv = list_Srv;
	}
    //--------------------------------------------------------------------------
	// ��getResponse���\�b�h
	//--------------------------------------------------------------------------
	private String getResponse(){
		return getResponse(0);
	}
	private String getResponse(int retryMax){
		String wMessage = "";
		String wStat = "";
		int retry = 0;
		while(wStat.equals("")){
			try{
				if (retry > 1) {
					System.out.print(" (-_-).zZ "+retry);
				}
				retry++;
				Thread.sleep(500*retry);// �~���Z�J���h�w��(��b=1000)
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
			if(retry > retryMax){
				wStat = "999"; // TimeOut�i���j
				System.out.println("## Time Out!! ##");
			}else{
				if (ctrllisten!=null) wStat = ctrllisten.getStatus();
			}
		}
		System.out.println("");
		wMessage = ctrllisten.getMessage();
		return wMessage;
	}
	//--------------------------------------------------------------------------
	// �� checkStat���\�b�h
	// �w�肵��pStat�ȏ�Ȃ�G���[�Ƃ������Ƃ�����̂��E�E�E�ǂ�����H
	//--------------------------------------------------------------------------
	private boolean checkStat(String stat) {
		String message = getResponse(10);
		if (message.startsWith(stat)){
			//System.out.println("OK        ");
			return true;
		}
		System.out.println("NG is not "+stat+" =>"+message);
		return false;
	}
	private boolean checkStat(String stat1,String stat2) {
		String message = getResponse(10);
		if (message.startsWith(stat1)){
			return true;
		}
		if (message.startsWith(stat2)){
			return true;
		}
		System.out.println("NG is not "+stat1+" or "+stat2+" =>"+message);
		return false;
	}
	//--------------------------------------------------------------------------
	// ���R�}���h�𔭍s����i���荞�ށj
	//--------------------------------------------------------------------------
	private void sendCommand(String pCommand) {
		//System.out.println("sendCommand:"+pCommand);
		try{
			if (ctrllisten!=null) {
				ctrllisten.resetStat();
				stream_o.println(pCommand);
				stream_o.flush();
			}else{
				log.error("Command�͎��s����܂���ł����A����:"+pCommand);
				System.out.println("Command�͎��s����܂���ł����A����:"+pCommand);
				System.out.println("");
			}
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
	}
	//--------------------------------------------------------------------------
	// ��dataConnection���\�b�h
	// �T�[�o�Ƃ̃f�[�^�����p�Ƀ\�P�b�g�����܂��D
	// �܂��C�T�[�o�ɑ΂���port�R�}���h�Ń|�[�g��ʒm���܂��D
	//--------------------------------------------------------------------------
	private Socket dataConnection(String ctrlcmd) {
		Socket dataSocket = null;	// �f�[�^�]���p�\�P�b�g
		String cmd = "PORT "; //PORT�R�}���h�ő���f�[�^�̊i�[�p�ϐ�
		int i;
		int port_H;					// �|�[�g�ԍ��̏�ʂW�r�b�g
		int port_L;					// �|�[�g�ԍ��̉��ʂW�r�b�g
		try{
			//-----------------------------------------------------------------
			// �����̃A�h���X�����߂܂��i�W�r�b�g���i�[�����E�E�E�j
			// ���R���X�g���N�^�ł�����ق����ǂ������m��Ȃ�
			//-----------------------------------------------------------------
			byte[] address = InetAddress.getLocalHost().getAddress();
			//-----------------------------------------------------------------
			// ���X�i�[�\�P�b�g�i�쐬�Ɠ����Ƀo�C���h�����E�E�悤���j
			// public ServerSocket(int port,int backlog)
			// port  - �g�p����|�[�g�B0���w�肷��Ƌ󂢂Ă���|�[�g�����蓖�Ă���
			// backlog - �L���[�̍ő咷 (�ő�ڑ����H�H)
			//-----------------------------------------------------------------
			ServerSocket listener = new ServerSocket(0,1);
			//-----------------------------------------------------------------
			// PORT�R�}���h�p�̑��M�f�[�^��p�ӂ��܂�
			//-----------------------------------------------------------------
			for(i = 0; i < 4; ++i) cmd = cmd + (address[i] & 0xff) + ",";
			port_H = ((listener.getLocalPort() / 256) & 0xff);
			port_L = ( listener.getLocalPort()        & 0xff);
			cmd = cmd + port_H + "," + port_L;
			// System.out.println("PORT�R�}���h =>"+cmd);

//			�A�N�e�B�u (�|�[�g) ���[�h	PORT	�T�[�o�[������ڑ��v�����s��
			// PORT�R�}���h�𐧌�p�X�g���[����ʂ��đ���܂�
			sendCommand(cmd);
			if( checkStat("200") ){
				// �����ΏۃR�}���h LIST,RETR,STOR �����T�[�o�ɑ���܂�
				System.out.println("�����ΏۃR�}���h=>"+ctrlcmd);
				dataSocket = null;	// �f�[�^�]���p�\�P�b�g

//				sendCommand("PASV");
//				if ( checkStat("227") ){
//					
//				}else{
//					
//				}

				
				sendCommand(ctrlcmd);
				//20160907
//				if ( checkStat("150") ){
				if ( checkStat("150","125") ){
					// �T�[�o����̐ڑ����󂯕t���܂�
					dataSocket = listener.accept();
				}else{
					//�@450 No files found
					//log.error("<@dataConnection>�R�}���h���s"+ctrlcmd);
				}
				listener.close(); //���X�i�[�\�P�b�g�͔p������
			}else{
				log.error("<PORT>port�R�}���h���s");
			}
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
		return dataSocket;
	}
	//--------------------------------------------------------------------------
	// ������p�̃\�P�b�g�����
	//--------------------------------------------------------------------------
	public void closeConnection() {
		try{
			ctrllisten.quit();
			ctrllisten = null;
			gSocket.close();
		}catch( IOException ie){
			ie.printStackTrace();
		}
		connected = false;
	}
	//--------------------------------------------------------------------------
	// ���ǂ�ȃ^�C�v�̂n�r��
	//--------------------------------------------------------------------------
	public String getOSType() {
		sendCommand("SYST");
		String msg = getResponse();
		if (msg.startsWith("215")){
			gOStype = msg.substring(4);
			System.out.println("gOStype�F"+gOStype);
			return msg;
		}
		return "";
	}
	//--------------------------------------------------------------------------
	// ���T�[�o�[���̍�ƃf�B���N�g�����E��
	//--------------------------------------------------------------------------
	public String getPWD(){
		return getWD();
	}
	public String getWD() {
		sendCommand("PWD");
		String msg = getResponse();
		if (msg.startsWith("257") && msg.indexOf('"',5) > 5 ){
			gCDir = msg.substring(5,msg.indexOf('"',5));
			System.out.println("gCDir�F"+gCDir);
			return msg;
		}
		return "";
	}
	//--------------------------------------------------------------------------
	// �����O�A�E�g
	//--------------------------------------------------------------------------
	public void doQuit() {
		log.info("���O�A�E�g");
		if (list_Srv!=null) list_Srv.setListData( new java.util.Vector() );
		sendCommand("QUIT");    // QUIT�R�}���h�̑��M
		checkStat("221");       // 221 Goodbye
	}
	//--------------------------------------------------------------------------
	// ���V�t�g�i�h�r�w��@�@�@for gwhost   quote site setcode s
	//--------------------------------------------------------------------------
	public void doSjis() {
		log.info("�V�t�g�i�h�r�w��");
		sendCommand("SITE setcode s");// Sjis
		checkStat("200");
	}
	//--------------------------------------------------------------------------
	// ���e�L�X�g�]�����[�h�ɐݒ�
	//--------------------------------------------------------------------------
	public void doAscii() {
		log.info("�A�X�L�[�w��");
		sendCommand("TYPE A");// A���[�h
		checkStat("200");
	}
	//--------------------------------------------------------------------------
	// ���o�C�i���]�����[�h�ɐݒ�
	//--------------------------------------------------------------------------
	public void doBinary() {
		log.info("�o�C�i���]�����[�h�w��");
		sendCommand("TYPE I");// I���[�h
		checkStat("200");
	}
	//--------------------------------------------------------------------------
	// ���T�[�o�[�̏�Ԃ��E��
	//--------------------------------------------------------------------------
	public String getSTAT() {
		sendCommand("STAT");
		String msg = getResponse();
		if (msg.startsWith("211")){
			return msg;
		}
		return "";
	}
	//--------------------------------------------------------------------------
	// ���T�[�o�[�������s�\�ȃR�}���h��m�肽���Ƃ��Ȃ�
	//--------------------------------------------------------------------------
	public String getHELP() {
		sendCommand("HELP");
		String msg = getResponse();
		if (msg.startsWith("214")){
			return msg;
		}
		return "";
	}
	//--------------------------------------------------------------------------
	// ���f�C���N�g���̍쐬
	//--------------------------------------------------------------------------
	public boolean doMKD(String dirName) {
		log.info("MKD " + dirName);
		sendCommand("MKD " + dirName); // MKD�R�}���h
		return checkStat("257");       // 553 �� File exists.
	}
	//--------------------------------------------------------------------------
	// ���f�C���N�g���̍폜
	//--------------------------------------------------------------------------
	public boolean doRMD(String dirName) {
		log.info("RMD " + dirName);
		sendCommand("RMD " + dirName); // RMD�R�}���h
		return checkStat("250");
	}
	//--------------------------------------------------------------------------
	// ���t�@�C���̍폜
	//--------------------------------------------------------------------------
	public boolean doDELE(String fileName) {
		log.info("DELE " + fileName);
		sendCommand("DELE " + fileName); // DELE�R�}���h
		return checkStat("250");	     // 550 No such file or directory.
	}
	//--------------------------------------------------------------------------
	// ���A�h���X�ƃ|�[�g�ԍ�����\�P�b�g����萧��p�X�g���[�����쐬���܂�
	//--------------------------------------------------------------------------
	public boolean connect(String host) {
		return connect(host,CTRLPORT);
	}
	public boolean connect(String host,int port) {
		log.info("## CONNECT ##");
		System.out.println("## CONNECT ##");
		try{
			host = host.trim();
			cPORT = port;
//			cPORT = CTRLPORT;
//			if(host.equals("agcisvw")) cPORT = 8021;  // InterScanVirusWall
//			if(host.equals("agcwall")) cPORT = 8021;
			gSocket = new Socket(host, cPORT);
			stream_o = new PrintWriter(gSocket.getOutputStream());
			stream_i = new BufferedReader(new InputStreamReader(gSocket.getInputStream()));
			try {
				System.out.println("## ���X�i�[�X���b�h�J�n ##");
				ctrllisten = new CtrlListen(stream_i);
				listenerthread = new Thread(ctrllisten);
				listenerthread.start();
				
				checkStat("220");
				connected = true;
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(NoRouteToHostException ne){
			ne.printStackTrace();
		}catch(UnknownHostException ue){
			ue.printStackTrace();
		}catch(IOException ie){
			ie.printStackTrace();
		}
		return false;
	}
	//--------------------------------------------------------------------------
	// �����O�C������
	// �^�C���A�E�g�̃p�����[�^�ƃ��g���C�����E�E�E
	//--------------------------------------------------------------------------
	public boolean doLogin(String loginName,String password) {
		log.info("## doLogin ##");
		System.out.println("## doLogin ##");
		loginName = loginName.trim();
		password  = password.trim();   // ��`�g�������ėǂ����̂�� �O�O
		sendCommand("USER " + loginName);   	// USER�R�}���h
		if ( checkStat("331") ){
			sendCommand("PASS " + password);	// PASS�R�}���h
			if ( checkStat("230") ) return true;
		}
		return false;
	}
	//--------------------------------------------------------------------------
	// ���J�����g�f�B���N�g����ύX����
	//--------------------------------------------------------------------------
	public boolean doCD(String dirName) { return doCd(dirName);}
	public boolean doCd(String dirName) {
		log.info("CWD " + dirName);
		sendCommand("CWD " + dirName); // CWD�R�}���h
		return checkStat("250");
	}
	
	//--------------------------------------------------------------------------
	// �����̊K�w�ֈړ�
	//--------------------------------------------------------------------------
	public boolean doCDUP() {
		log.info("CDUP");
		sendCommand("CDUP"); // CDUP�R�}���h
		return checkStat("250");
	}
	
	//--------------------------------------------------------------------------
	// ��doLIST���\�b�h �f�B���N�g�����𓾂܂��D
	// doLIST("NLST");
	// doLIST("LIST");
	//--------------------------------------------------------------------------
	public List<String> doLIST(String pFunk) {
		log.info("doLIST:"+pFunk);
		List<String> wList = null;
		try{
			String wRec;
			StringBuffer wBuff = new StringBuffer(1024);
			int n;
			byte[] buff = new byte[1024];
			Socket dataSocket = dataConnection(pFunk);	// �f�[�^�p�R�l�N�V����
			if (dataSocket != null){
				// �f�[�^�ǂݎ��p�X�g���[����p��
				BufferedInputStream dataInput = new BufferedInputStream(dataSocket.getInputStream());
				while(( n = dataInput.read(buff) ) > 0){
					//System.out.write(buff,0,n);
					wRec = new String(buff,0,n);
					//System.out.println("�y^0^�z=>"+wRec);
					wBuff.append(wRec);
				}
				//-------------------------------------------------------------
				String[] anArray = wBuff.toString().split("\n");
				if (anArray != null){
					wList = new ArrayList(anArray.length);
					for (int i=0; i < anArray.length; i++) {
						//System.out.println("�y^_^�z=>"+anArray[i].trim());
						wList.add(anArray[i].trim());
					}
				}
				//-------------------------------------------------------------
				dataSocket.close();
				checkStat("226");
			}
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
		if (list_Srv!=null) {
			//System.out.println("���ʂ����X�g�Ɋi�[�ς�");
		    String[] array = (String[]) wList.toArray(new String[wList.size()]);
			list_Srv.setListData( array );
			list_Srv.repaint();
		}
		return wList;
	}
	
	//--------------------------------------------------------------------------
	// doGet���\�b�h
	// �T�[�o��̃t�@�C������荞�݂܂��D(�_�E�����[�h����)
	//--------------------------------------------------------------------------
	public boolean doGet(String fileName) {
		String wFS = System.getProperty("file.separator");
		 return doGet(fileName,"."+wFS);
	}
	//--------------------------------------------------------------------------
	// ���p�����[�^�̂����܂����t�@�C���Z�p���[�^�̏ꍇ�̓f�B���N�g�����w�肳�ꂽ���̂Ƃ���
	//�@�T�[�o�[���̃t�@�C���Ɠ����t�@�C�������̃f�B���N�g���ɓ]�������B
	//�@���o�͐�̃p�X�Ɍ�肪����ꍇ�̏��������Ƃōl����
	//--------------------------------------------------------------------------
	public boolean doGet(String fileName,String outPath) {
		boolean flg = false;
		String wPath = "";
		outPath= FileUtil.normarizeIt(outPath);
		if (outPath.endsWith("/")){
			wPath = outPath +  fileName;
		}else{
			wPath = outPath;
		}
		log.info("doGet:"+fileName+" to=>"+outPath);
		try{
			int n;
			byte[] buff = new byte[1024];
			// �t�@�C���]���p�f�[�^�X�g���[�����쐬���܂�
			Socket dataSocket = dataConnection("RETR " + fileName);
			if (dataSocket != null){
				// �N���C�A���g��Ɏ�M�p�t�@�C�����������܂�
				//kkkTest(wPath);
				FileOutputStream outfile = new FileOutputStream(wPath);
				BufferedInputStream dataInput = new BufferedInputStream(dataSocket.getInputStream());
				// �T�[�o����f�[�^���󂯎��C�t�@�C���Ɋi�[���܂�
				while((n = dataInput.read(buff)) > 0){
					outfile.write(buff,0,n);
				}
				dataSocket.close();
				outfile.close();
				if (checkStat("226")) flg = true;
			}
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
		return flg;
	}
	//--------------------------------------------------------------------------
	// doPut���\�b�h
	// �T�[�o�փt�@�C���𑗂�܂��D(�A�b�v���[�h����)
	//--------------------------------------------------------------------------
//	public void kkkTest(String inFileName){
//		String inDir    ="";
//		String fileName = inFileName;
//		String wFS = System.getProperty("file.separator");
//		//�@�Z�p���[�^�������ꍇ���z�肵�Ă������I�I
//		int idx = inFileName.lastIndexOf(wFS)+1; 
//		if  (idx> 0){
//			inDir    = inFileName.substring(0, idx );
//			fileName = inFileName.substring(idx );
//			System.out.println("��inDir�@	�F"+inDir);
//			System.out.println("��fileName	�F"+fileName);
//		}
//	}
	public boolean doPut(String iFilePath){
		return doPut(iFilePath,false);
	}
	private boolean doPut(String iFilePath,boolean overWrite){
		String oFileName = iFilePath;
		String wFS = System.getProperty("file.separator");
		int idx = iFilePath.lastIndexOf(wFS)+1; //�������Ȃ��ꍇ�� -1
		if  (idx> 0){
			oFileName = iFilePath.substring(idx );
		}else{ //�@�Z�p���[�^�������ꍇ
			oFileName = iFilePath;
		}
		return doPut(oFileName,iFilePath,overWrite);
	}
	public boolean doPut(String oFileName,String iFilePath,boolean overWrite) {
		log.info("doPut:"+oFileName+" From=>"+iFilePath);
		if(!new File(iFilePath).exists()) {
			System.out.println("�t�@�C��������܂���:"+iFilePath);
			return false;
		}
		boolean flg = false;
		try{
			int n;
			byte[] buff = new byte[1024];
			FileInputStream sendfile = null;
			System.out.println("�N���C�A���g��̃t�@�C���̓ǂݏo���������s���܂�");
			try{
				sendfile = new FileInputStream(iFilePath);
			}catch(Exception e){
				System.out.println("�t�@�C��������܂���:"+iFilePath);
				return flg;
			}
			System.out.println("�]���p�f�[�^�X�g���[����p�ӂ��܂�");
			String pFunk;
			if (overWrite){ // �����̃t�@�C�������ꍇ�㏑������Ȃ��E�E
				pFunk = "STOU " + oFileName;
			}else{        // �����̃t�@�C�������ꍇ�㏑�������
				pFunk = "STOR " + oFileName;
			}
			Socket dataSocket = dataConnection(pFunk);
			if (dataSocket != null){
				OutputStream outstr = dataSocket.getOutputStream();
				System.out.println("�t�@�C����ǂݏo���C�l�b�g���[�N�o�R�ŃT�[�o�ɑ���܂�");
				while((n = sendfile.read(buff)) > 0){
					outstr.write(buff,0,n);
				}
				dataSocket.close();
				if (checkStat("226")) flg = true;
			}
			sendfile.close();
		}catch(Exception e)	 {
			e.printStackTrace();
			//System.exit(1);
		}
		return flg;
	}
	//##########################################################################
	// ���W�����͂����͂𑣂��A����
	//  �၄ String dirName = getUserInput("�f�B���N�g��������͂��ĉ������D");
	//##########################################################################
	public String getUserInput(String pMessage) {
		try{
			BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
			System.out.println(pMessage);
			return lineread.readLine();
		}catch(Exception e)	 {
			e.printStackTrace();
			return "";
		}
	}
	//##########################################################################
	// ��getStdin���\�b�h
	// ���p�҂̎w�肵���R�}���h�ԍ���ǂݎ��܂��D
	//##########################################################################
	public String getStdin() {
		String buf = "";
		BufferedReader lineread = new BufferedReader(new InputStreamReader(System.in));
		while(buf.length() != 1){    // 1�����̓��͂��󂯂�܂ŌJ��Ԃ�
			try{
				buf = lineread.readLine();
			}catch(Exception e) {
				e.printStackTrace();
				//System.exit(1);
			}
		}
		return (buf);
	}
	//##########################################################################
	// ��talkWith �Θb�ɂ��e�X�g�E�E�E
	//##########################################################################
	public void talkWith() {
		String wCommand = getUserInput("�R�}���h����͂��ĉ������D");
		sendCommand(wCommand);
	}
	//##########################################################################
	// ��doCd�̃��b�p�[
	//##########################################################################
	public void doCd() {
		String dirName = getUserInput("�f�B���N�g��������͂��ĉ������D");
		doCd(dirName);
	}
	//##########################################################################
	// ��doGet�̃��b�p�[
	//##########################################################################
	public void doGet() {
		String dirName = getUserInput("�t�@�C��������͂��ĉ������D");
		doGet(dirName);
	}
	//##########################################################################
	// ��doPut�̃��b�p�[
	//##########################################################################
	public void doPut() {
		String dirName = getUserInput("�t�@�C��������͂��ĉ������D");
		doGet(dirName);
	}
	//##########################################################################
	// ��doLogin�̃��b�p�[
	//##########################################################################
	public boolean doLoginUI(String user,String pass) {
		if( user == null) user = getUserInput("���O�C��������͂��ĉ������D");
		if( pass == null) pass = getUserInput("�p�X���[�h����͂��ĉ������D");
		return doLogin(user,pass);
	}
	//##########################################################################
	// ��loop���\�b�h
	//   Ftp�̃R�}���h���j���[���o�͂��āC�e�������Ăяo���܂��D
	//##########################################################################
	public void loop(String host, int port,String user,String pass) {
		boolean wCtrl = true;
		try {
			if (connect(host,port)==false) {
				//System.exit(1);       // ����p�R�l�N�V����
				return;
			}
			if (doLoginUI(user,pass)==false){
				//System.exit(1);  // ����p�R�l�N�V����
				return;
			}
			while(wCtrl){
				System.out.print(">? 2 ls 3 cd 4 get 5 put 6 ascii 7 binary 8 help 9 quit "+gLs);
				switch(Integer.parseInt(getStdin())){
					//case 0 : doRecv()  ;     break; // ���X�|���X��ǂݍ���
					case 1 : talkWith();     break; // �R�}���h�𑗂�
					case 2 : doLIST("NLST"); break; // �T�[�o�̃f�B���N�g���\������
					case 3 : doCd()    ;     break; // �T�[�o�̍�ƃf�B���N�g���ύX����
					case 4 : doGet()   ;     break; // �T�[�o����̃t�@�C���擾����
					case 5 : doPut()   ;     break; // �T�[�o�ւ̃t�@�C���]������
					case 6 : doAscii() ;     break; // �e�L�X�g�]�����[�h
					case 7 : doBinary();     break; // �o�C�i���]�����[�h
					case 8 : getHELP()  ;     break; // HELP
					case 9 : doQuit()  ;     wCtrl = false; break;// �����̏I��
					default : System.out.println("�ԍ���I�����ĉ�����");
				}
			}
			closeConnection();
		} catch(Exception e) {
			System.err.print(e);
			//System.exit(1);
		}
	}
	//##########################################################################
	// �� mmBatch << MacroMill >> Batch File
	//##########################################################################
	public static void mmFTP_GetxxxNotUsedxxxx(
			String outDir,
			String host,
			int port,
			String user,
			String pass,
			String dirName,
			String regex
			) {
		String wOutDir = FileUtil.mkdir(outDir);
		Ftp ins = new Ftp();
		log.info("<connect>------------------------------------------");
		System.out.println("<connect>------------------------------------------");
		if (!ins.connect(host,port)){ // ����p�R�l�N�V����
			//System.exit(1);
			return;
		}
		log.info("<doLogin>------------------------------------------");
		System.out.println("<doLogin>------------------------------------------");
		if (!ins.doLogin(user,pass)){
			//System.exit(1);  // login
			return;
		}
		log.info("<getOSType()>------------------------------------------");
		System.out.println("<getOSType()>-----------------------------------------");
		ins.getOSType();
		log.info("<getWD()>------------------------------------------");
		System.out.println("<getWD()>------------------------------------------");
		ins.getWD();

//		System.out.println("<doCd(/)>------------------------------------------");
//		doCd("/") ;            // �T�[�o�̍�ƃf�B���N�g���ύX����

		log.info("<doAscii()>------------------------------------------");
		System.out.println("<doAscii()>-----------------------------------------");
		ins.doAscii() ;          // �e�L�X�g�]�����[�h
		//ins.doBinary();          // �o�C�i���]�����[�h

		List gLVec = null;
		Iterator it  = null;
		if (!dirName.trim().equals("")){
			ins.doCD(dirName);
		}
		gLVec = ins.doLIST("LIST");  // �����������
		if(gLVec.size()>0){
			if(gLVec.contains(".")) gLVec.remove(".");
			it = gLVec.iterator();
			while(it.hasNext())	{
				System.out.println("=>"+it.next());
			}
		}
		gLVec = ins.doLIST("NLST");  // �����������
		if(gLVec.size()>0){
			if(gLVec.contains(".")) gLVec.remove(".");
			it = gLVec.iterator();
			while(it.hasNext())	{
				String wFile = it.next().toString();
				if (wFile.matches(regex)){
					// �T�[�o����̃t�@�C���擾����
					System.out.println("<doGet(" + wFile + ")>-------------------------------------");
					if (!ins.doGet(wFile,wOutDir)){
						System.out.println("������Error!������"+ wFile);
						//mail�����ȂǁE�E�E
						//���������Ȃ̂��A�肪������c���Ă���
						//log�̏������K�v��
					};      
				}else{
					System.out.println("<doGet unMatched(" + wFile + ")>-------------------------------------");
				}
			}
		}
		System.out.println("<doQuit()>-----------------------------------------");
		ins.doQuit()  ;            // �����̏I��
		System.out.println("---------------------------------------------------");
		ins.closeConnection();
	}
	//##########################################################################
	// �� BATCH������
	//##########################################################################
	public static void sample(String host,int port,String user,String pass) {
		Ftp ins = new Ftp();
		System.out.println("<connect>------------------------------------------");
		if (!ins.connect(host,port)){ // ����p�R�l�N�V����
			//System.exit(1);
			return;
		}
		System.out.println("<doLogin>------------------------------------------");
		if (!ins.doLogin(user,pass)){
			//System.exit(1);  // login
			return;
		}
		System.out.println("<getOSType()>-----------------------------------------");
		ins.getOSType();
		System.out.println("<getWD()>------------------------------------------");
		ins.getWD();

		List gList = ins.doLIST("LIST");  // �����������
		System.out.println("size:"+gList.size());
		System.out.println("contains(null):"+gList.contains("."));
		gList.remove(".");
		Iterator it = gList.iterator();
		while(it.hasNext())	System.out.println("=>"+it.next());

		//System.out.println("<doLIST(NLST)>-------------------------------------");
		//doLIST("NLST");        // �T�[�o�̃f�B���N�g���\������
		//System.out.println("<doLIST(LIST)>-------------------------------------");
		//doLIST("LIST");        // �T�[�o�̃f�B���N�g���\������

		//System.out.println("<doDELE(Bear.txt)>----------------------------------");
		//doDELE("Bear.txt");

		System.out.println("<doDELE(BEE.txt)>----------------------------------");
		ins.doDELE("BEE.txt");

		System.out.println("<doPut(Bear.txt)>----------------------------------");
		ins.doPut("Bear.txt");     // �T�[�o�ւ̃t�@�C���]������

		System.out.println("<doMKD(DUBWISE)>----------------------------------");
		ins.doMKD("DUBWISE");

		//System.out.println("<doRMD(DUBWISE)>----------------------------------");
		//doRMD("DUBWISE");

//		System.out.println("<doCd(/)>------------------------------------------");
//		doCd("/") ;            // �T�[�o�̍�ƃf�B���N�g���ύX����

		System.out.println("<doLIST(LIST)>-------------------------------------");
		ins.doLIST("LIST");        // �T�[�o�̃f�B���N�g���\������
		System.out.println("<doGet(.bashrc)>-------------------------------------");
		ins.doGet(".bashrc");      // �T�[�o����̃t�@�C���擾����

		//doAscii() ;          // �e�L�X�g�]�����[�h
		//doBinary();          // �o�C�i���]�����[�h
		System.out.println("<getHELP()>-----------------------------------------");
		ins.getHELP()  ;            // HELP

		System.out.println("<doCDUP()>-----------------------------------------");
		ins.doCDUP();

		System.out.println("<getWD()>------------------------------------------");
		ins.getWD();

		//System.out.println("<getSTAT()>-----------------------------------------");
		//getSTAT();

		System.out.println("<doQuit()>-----------------------------------------");
		ins.doQuit()  ;            // �����̏I��
		System.out.println("---------------------------------------------------");
		ins.closeConnection();
	}
	//--------------------------------------------------------------------------
	// ��main���\�b�h
	// TCP�R�l�N�V�������J���ď������J�n���܂�
	//--------------------------------------------------------------------------
	public static void main(String[] arg){
        System.setErr(System.out);
		try {
			System.out.println("arg.length:"+arg.length);
			if(arg.length < 1){
				System.out.println("usage: java Ftp <host name>");
				return;
			}
//			String host = arg[0];
//			Ftp f = new Ftp();
			if (arg.length >= 2){
				//f.loop(host,arg[1],arg[2]);
				//sample(host,arg[1],arg[2]);
//				mmBatch("192.168.1.2","ken","tonyhika","","reg" );
				// for PowerMac
				//mmBatch("10.6.20.7","ken","anthony","","qpr_.*_.*_20\\d*\\.txt" );
				// for macroMill
			}else{
				//f.loop(host,null,null);
			}
			System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	public boolean isConnected() {
		return connected;
	}
}
//------------------------------------------------------------------------------
// ��CtrlListen �N���X ���b�Z�[�W��񓯊��ɓǂݍ��ށE�E�E
//------------------------------------------------------------------------------
class CtrlListen implements Runnable{
//	public static Log log = LogFactory.getLog("kyPkg.util.CtrlListen");
	public static Log log = LogFactory.getLog(Ftp.APPENDA_CLASS);

	private LinkedList gStat_Que = new LinkedList();   // �V���N���i�C�Y������H�I
	private LinkedList gMsg_Que = new LinkedList();   // �V���N���i�C�Y������H�I
	private String gLs = System.getProperty("line.separator");
	private BufferedReader stream_i = null;
	private boolean gCtrl = true;
	private String message = "";
	private String status  = "";;
	//----------------------------------------------------------------------
	// �A�N�Z�b�T
	//----------------------------------------------------------------------
	public String getStatus(){
		status  = "";
		message = "";
		if (gCtrl){
			if (gStat_Que.size() > 0){
				status  = (String)gStat_Que.removeFirst();
				message = (String)gMsg_Que.removeFirst();
				//System.out.println("\n message :" + message );
				//log.info("@CtrlListen  :"+message);
			}
		}
		return status;
	}
	//----------------------------------------------------------------------
	public String getMessage(){
		return message;
	}
	//----------------------------------------------------------------------
	public String getEmessage(){
//		Emsg = CnvRtnToMsg(stat);
//		System.out.print("\n Emsg:" + Emsg);
		return CnvRtnToMsg(status);
	}
	//----------------------------------------------------------------------
	// �R�}���h���s�O�ɌĂ�
	//----------------------------------------------------------------------
	public void resetStat(){
		gStat_Que.clear();
		gMsg_Que.clear();
	}
	//----------------------------------------------------------------------
	// �R���X�g���N�^ �ǂݎ���̎w��
	//----------------------------------------------------------------------
	public CtrlListen(BufferedReader in){
		stream_i = in;
	}
	//----------------------------------------------------------------------
	// �I��������
	//----------------------------------------------------------------------
	public void quit(){
		System.out.println("��quit��");
		gCtrl = false;
	}
	//----------------------------------------------------------------------
	// QUIT�����SocketException ���������Ă��܂��̂����E�E�E�ǂ����悤�H
	//----------------------------------------------------------------------
	@Override
	public void run(){
		StringBuffer wBuff;
		String wRec = "";
		while(gCtrl){
			try{
				//-------------------------------------------------------------
				// �R�l�N�g���Ă��邠���������ƃ��[�v����̂����A����Ŗ��Ȃ����낤���H�I
				// �Ȃ񂩊ԈႦ�Ă���C������̂����E�E�E�E�ڑ����؂ꂽ��m���Ƀu���[�N�����˂΁E�E�E
				// System.out.println("��gCtrl��"+gCtrl);
				//-------------------------------------------------------------
				wRec = stream_i.readLine();
				if (wRec!=null){
					log.info("(@_@)"+message);
					String Stat = wRec.substring(0,3);
					synchronized(gStat_Que){
						gStat_Que.add(Stat);
					}
					// System.out.println("��=>"+wRec);
					wBuff = new StringBuffer(1024);
					wBuff.append(wRec);
					if(wRec.substring(3,4).equals("-")){
						wRec = "";
						while(!wRec.startsWith(Stat)){
							wRec = stream_i.readLine();
							wBuff.append(gLs);
							wBuff.append(wRec);
							//System.out.print("��=>+wRec);
						}
					}
					synchronized(gMsg_Que){
						gMsg_Que.add(wBuff.toString());
					}
					//System.out.print("�@=>"+gMsg_Que.getLast());
				}
			} catch (java.net.SocketException se){
				//se.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
				//System.exit(1);
			}
		}
	}
	//--------------------------------------------------------------------------
	//��ftp�̃��v���C�R�[�h�ɑΉ����郁�b�Z�[�W��\�����܂��B
	//--------------------------------------------------------------------------
	String CnvRtnToMsg(String pCode){
		// System.out.println("(^_^)");
		int nCode = Integer.parseInt(pCode);
		String rMsg = "";
		switch(nCode) {
			case 110:	rMsg = " ���X�^�[�g�}�[�J�����@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 120:	rMsg = " �T�[�r�X��nnn ���ŏ������ł��܂��B�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 125:	rMsg = " �f�[�^�ڑ��͂��łɃI�[�v�����܂����B�]���͊J�n���Ă��܂��@�@�@�@�@";	break;
			case 150:	rMsg = " �t�@�C���X�e�[�^�X�͗ǍD�ł��B�f�[�^�ڑ����I�[�v�����܂��@�@�@�@�@";	break;
			case 200:	rMsg = " �R�}���h�͂n�j�ł��@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 211:	rMsg = " �V�X�e���X�e�[�^�X�A�܂��̓V�X�e���w���v�����@�@�@�@�@�@�@�@�@�@�@";	break;
			case 212:	rMsg = " �f�B���N�g���X�e�[�^�X�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 213:	rMsg = " �t�@�C���X�e�[�^�X�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 214:	rMsg = " �w���v���b�Z�[�W�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 215:	rMsg = " �m�`�l�d�V�X�e���T�[�r�X�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 220:	rMsg = " �V�K���[�U�[�p�T�[�r�X�������ł��Ă��܂��@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 221:	rMsg = " �T�[�r�X�̓R���g���[���ڑ�����܂��B�K���Ȃ烍�O�A�E�g���܂��@�@";	break;
			case 225:	rMsg = " �f�[�^�ڑ��͊J���Ă��܂��B�i�s���̓]���͂���܂���@�@�@�@�@�@�@�@";	break;
			case 226:	rMsg = " �f�[�^�ڑ����N���[�Y���܂��B�v�����ꂽ�t�@�C�������͊������܂����@";	break;
			case 227:	rMsg = " �p�b�V�u���[�h�ɓ���܂��B�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 230:	rMsg = " ���[�U�����O�C�����܂����A��֐i�݂܂��@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 250:	rMsg = " �v�����ꂽ�t�@�C������������I�����܂����B�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 257:	rMsg = " PATHNAME���쐬����܂����@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 331:	rMsg = " ���[�U���͂n�j�ł��A�p�X���[�h���K�v�ł��@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 332:	rMsg = " ���O�C���p�̃A�J�E���g���K�v�ł��@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 350:	rMsg = " �v�����ꂽ�t�@�C�������́A�ǉ����������Ă��܂��@�@�@�@�@�@�@�@�@";	break;
			case 421:	rMsg = " �T�[�r�X�͗��p�ł��܂���A�R���g���[���ڑ�����܂��@�@�@�@�@�@�@";	break;
			case 425:	rMsg = " �f�[�^�ڑ����I�[�v���ł��܂���@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 426:	rMsg = " �ڑ����N���[�Y����܂����A�]�����ł��؂��܂����@�@�@�@�@�@�@�@�@";	break;
			case 450:	rMsg = " �v�����ꂽ�t�@�C�����������s����܂���ł����A�t�@�C�����g�p�s�\";	break;
			case 451:	rMsg = " �v�����ꂽ�s�����ł��؂��܂����A���[�J���̏����G���[�ł��@�@�@�@";	break;
			case 452:	rMsg = " �v�����ꂽ�s���͎��s����܂���ł����A�V�X�e���̋L����s���@�@�@�@";	break;
			case 500:	rMsg = " �V���^�b�N�X�G���[�A�R�}���h���F������܂���ł����@�@�@�@�@�@�@�@";	break;
			case 501:	rMsg = " �p�����[�^���邢�͈����̃V���^�b�N�X�G���[�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 502:	rMsg = " �R�}���h����������Ă��܂���@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 503:	rMsg = " �R�}���h�̃V�[�P���X���Ԉ���Ă��܂��@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 504:	rMsg = " ���̃R�}���h�p�����[�^�͎�������Ă��܂���@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 530:	rMsg = " ���O�C���Ɏ��s���܂����@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 532:	rMsg = " �t�@�C���i�[�p�̃A�J�E���g���K�v�ł��@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
			case 550:	rMsg = " �v���͎��s����܂���ł����A�t�@�C�����g�p�s�\�ł��@�@�@�@�@�@�@";	break;
			case 551:	rMsg = " �v���͑ł��؂��܂����A�y�[�W�^�C�v���s���ł��@�@�@�@�@�@�@�@�@�@";	break;
			case 552:	rMsg = " �v���͑ł��؂��܂����A�L�����蓖�Ă����߂��܂����@�@�@�@�@�@�@�@";	break;
			case 553:	rMsg = " �v���͎��s����܂���ł����A�t�@�C�������s���ł��@�@�@�@�@�@�@�@�@";	break;
			default:	rMsg = " ����`�ȃ��b�Z�[�W�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@";	break;
		}
		return rMsg;
	}
}
