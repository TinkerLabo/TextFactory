package kyPkg.util;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.jar.*;
//-----------------------------------------------------------------------------
//���l�̃W�����}
//http://ccs.cla.kobe-u.ac.jp/Jouhou/95/Takahasi/prisoner.html
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//	�ǉ��\�聨�A�[�J�C�u�̃G���g���[���e�����X�g������
//-----------------------------------------------------------------------------
public class Archiver {
	final static int IS_FILE = 1;
	final static int IS_DIRECTORY = 0;
	final static int OTHERKIND = 9;
	//-------------------------------------------------------------------------
	// ���g�p�ၠ
	//-------------------------------------------------------------------------
	public static void main( String argv[] ) {
		String o_File;
		String i_File;
		System.out.println("argv.length:"+argv.length);
		if (argv.length==1){
			System.out.println("�𓀒��I");
			i_File = argv[0];
			Archiver.deCompZipAll(i_File);		//��
		}else if (argv.length==2){
			System.out.println("���k���I");
			o_File = argv[0];
			i_File = argv[1];
			Archiver.enCompZip(o_File,i_File);   //���k
		}else{
			//Archiver
			System.out.println("jar���lib�����o���܂�");
			String wFile_i = "AppCtr.jar";
			String wKeywrd = "lib/";
			File wKwd = new File(wKeywrd);
			if (wKwd.isDirectory() == false ){
				// Jar�A�[�J�C������L�[���[�h�Ŏn�܂�G���g���[�����o��
				System.out.println("lib�����o��");
				Archiver.ExtractJarKW(wFile_i,wKeywrd);
			}
		}
	}
	//-------------------------------------------------------------------------
	// ���ׂẴG���g���[���𓀂���ijar���𓀂��Ă����悤���E�E�j
	//-------------------------------------------------------------------------
	public static void deCompZipAll(String wFile_i){
		try{
			if (fileCheck(wFile_i)!=IS_FILE) return; // file����Ȃ�������E�E�E
			ZipFile zFile = new ZipFile(wFile_i);
			String wOutputDir = getOutDir(wFile_i);
			Enumeration wEnum = zFile.entries();
			while(wEnum.hasMoreElements()){
				ZipEntry wEntry = (ZipEntry)wEnum.nextElement();
				deCompZip(zFile,wEntry,wOutputDir);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	//-------------------------------------------------------------------------
	// �w�肳�ꂽ�G���g���[���𓀂���
	//-------------------------------------------------------------------------
	public static void deCompZip(ZipFile zFile,ZipEntry zEntry){
		deCompZip(zFile,zEntry,"");  // ����ŃJ�����g�p�X�ɂȂ邩�ˁH
	}
	public static void deCompZip(ZipFile zFile,ZipEntry zEntry,String outDir){
		try{
			System.out.print  (" Entry:" + zEntry.getName() );
			System.out.println(" Size :" + zEntry.getSize() );
			String wPath = outDir + zEntry.getName();
			File file = new File(wPath);
			if( zEntry.isDirectory() ) {
				file.mkdirs();
			} else {
				//-------------------------------------------------------------
				String parentName = file.getParent();
				if( parentName == null ) parentName = ".";
				File dir = new File( parentName );
				dir.mkdirs();
				//-------------------------------------------------------------
				BufferedInputStream bis = new BufferedInputStream(
										zFile.getInputStream(zEntry));
				BufferedOutputStream bos = new BufferedOutputStream(
										new FileOutputStream(file));
				int c;
				while( (c = bis.read()) != -1 ){
					bos.write((byte)c);
				}
				bis.close();
				bos.close();
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	//-------------------------------------------------------------------------
	// �t�@�C���i�w��f�B���N�g���ȉ��j���y�h�o�Ɉ��k����
	//	Archiver.enCompZip(o_File,i_File);   // ���k
	//-------------------------------------------------------------------------
	public static void enCompZip(String i_File) {
		String o_File = getOutPath(i_File,".zip");
		enCompZip(o_File,i_File);
	}
	public static void enCompZip(String o_File,String i_File) {
		System.out.println("��~* o:"+o_File);
		System.out.println("��~* i:"+i_File);
		try {
			File zipFile = new File( o_File );
			if( zipFile.exists() ) {
				System.err.println( o_File + "�����ɑ��݂���̂Œ��f���܂���" );
				System.exit(0);
			}
			System.out.println  ("�� enCompZip �o�͐�:" + o_File );
			//-----------------------------------------------------------------
			// Create Output File
			//-----------------------------------------------------------------
			BufferedOutputStream buffOut
				= new BufferedOutputStream(new FileOutputStream(zipFile) );
			ZipOutputStream zos = new ZipOutputStream(buffOut);
			//-----------------------------------------------------------------
			// Create Input List
			//-----------------------------------------------------------------
			ArrayList names = pathLists(new File(i_File));
			Iterator it = names.iterator();
			while( it.hasNext() ) {
				String wPath = (String)it.next();
				System.out.println  ("�� �ǉ�:" +  wPath);
				enZip( zos,wPath); // zip�ɒǉ��I�I
			}
			zos.close();

//			ZipFile wZip = new ZipFile(o_File);
//			for (Enumeration wEnum = wZip.entries(); wEnum.hasMoreElements() ;) {
//				System.out.println("entry=>"+wEnum.nextElement());
//			}

		} catch( FileNotFoundException fnfE ){
			fnfE.printStackTrace();
			System.err.println( "file not found" );
		} catch( ZipException zipE ){
			zipE.printStackTrace();
			System.err.println( "Zip error..." );
		} catch( IOException ioE ){
			ioE.printStackTrace();
			System.err.println( "IO error..." );
		} catch( Exception e ){
			e.printStackTrace();
			System.out.println( "Error:" + e.toString() );
		}
	}

	//-------------------------------------------------------------------------
	// �^����ꂽ�t�@�C�������k���AZip�t�@�C���ɒǉ�����
	//-------------------------------------------------------------------------
	public static void enZip( ZipOutputStream zos, String pPath)
		throws FileNotFoundException,ZipException,IOException {
		File file = new File(pPath);
		try {
			BufferedInputStream bis
				= new BufferedInputStream(new FileInputStream( file ) );
			String wPath = file.getPath();
			char oldChar = (System.getProperty("file.separator")).charAt(0);
			char newChar = '/';
			wPath = wPath.replace(oldChar,newChar);
			int pos = wPath.indexOf(newChar);
			if (pos > 0){
				wPath = wPath.substring(pos+1);
			}
			System.out.println("�ǂ����H" + wPath);
			//ZipEntry zEntry = new ZipEntry( file.getPath() );
			ZipEntry zEntry = new ZipEntry(wPath);
			zos.putNextEntry( zEntry );
			int c;
			while( ( c = bis.read() ) != -1 ) {
				System.out.println("enZip>"+c);
				zos.write( (byte)c );
			}
			bis.close();
			zos.closeEntry();
		} catch( FileNotFoundException e ){
			e.printStackTrace();
			throw e;
		} catch( ZipException e ){
			e.printStackTrace();
			throw e;
		} catch( IOException e ){
			e.printStackTrace();
			throw e;
		}
	}

	//-------------------------------------------------------------------------
	// �f�B���N�g���ƃt�@�C�����̈ꗗ��Ԃ����\�b�h
	//-------------------------------------------------------------------------
	public static ArrayList pathLists( File pFile )
			throws FileNotFoundException, IOException {
		System.out.println("�� " + pFile);
		ArrayList names = new ArrayList();
		try {
			if ( pFile.isFile() ){
				System.out.println("��01�� " + pFile.getPath());
				names.add( pFile.getPath() );
			} else if ( pFile.isDirectory() ){
				String parentName = pFile.getPath();
				String[] fl = pFile.list();
				for( int i=0; i < fl.length; i++ ) {
					String pathName = parentName + File.separator + fl[i];
					File wFile = new File( pathName );
					if( wFile.isFile() ){
						System.out.println("��02�� " + wFile.getPath());
						names.add( wFile.getPath() );
					} else if( wFile.isDirectory() ) {
						ArrayList sub = pathLists( wFile );
						Iterator it = sub.iterator();
						while( it.hasNext() ) names.add( it.next() );
					}
				}
			}
		} catch( FileNotFoundException e ){
			e.printStackTrace();
			System.err.println( "file not found" );
		} catch( IOException e ){
			e.printStackTrace();
			throw e;
		}
		return names;
	}
	//-------------------------------------------------------------------------
	// �L�[���[�h�Ŏn�܂���̂��������t�@�C�������o���܂�
	//-------------------------------------------------------------------------
	public static void ExtractJarKW(String wFile_i,String wKeywrd){
		try{
			System.out.println("# File:" + wFile_i);
			JarFile jFile = new JarFile(wFile_i);
			for (Enumeration wEnum = jFile.entries() ; wEnum.hasMoreElements() ;) {
				JarEntry wEntry = (JarEntry)wEnum.nextElement();
				System.out.println("# >"+wEntry.getName());
				if (wEntry.getName().startsWith(wKeywrd) ){
					ExtractJar(jFile,wEntry);
				}
			}
		}catch(java.io.FileNotFoundException e){
			e.printStackTrace();
			//System.exit(0);
			return;
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	//-------------------------------------------------------------------------
	// �w�肳�ꂽ�G���g���[���������t�@�C�������o���܂�
	//-------------------------------------------------------------------------
	public static void ExtractJar(JarFile zipFile,JarEntry target ){
		try{
			System.out.print  (" �� Entry:"+target.getName() );
			System.out.println(" �� Size :"+target.getSize() );
			File file = new File(target.getName());
			if( target.isDirectory() ) {
				file.mkdirs();
			} else {
				//---------------------------------------------------
				String parentName = file.getParent();
				if( parentName == null ) parentName = ".";
				File dir = new File( parentName );
				dir.mkdirs();
				//---------------------------------------------------
				BufferedInputStream bis = new BufferedInputStream(
										zipFile.getInputStream(target)	);
				BufferedOutputStream bos = new BufferedOutputStream(
										new FileOutputStream(file));
				int c;
				while( (c = bis.read()) != -1 ){
					bos.write((byte)c);
				}
				bis.close();
				bos.close();
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	//-------------------------------------------------------------------------
	// �������t�@�C���̃}�j�t�F�X�g�����ꗗ�\�����܂��i���`�j
	// Archiver.ManiInfo("AppCtr.jar");
	//-------------------------------------------------------------------------
	public static void ManiInfo(String wFile_i){
		try{
			if (fileCheck(wFile_i)!=IS_FILE) return; // file����Ȃ�������E�E�E
			File wFile = new File(wFile_i);
			if (wFile .exists() == false ){
				System.out.println("@ ManiInfo Sorry... File Not Exists!!=>"+wFile_i);
				return;
			}
			JarFile jFile = new JarFile(wFile_i);
			Manifest mf = jFile.getManifest();
			//-----------------------------------------------------------------
			// MainAttributes Manifest �̃��C���ƂȂ� Attributes ��Ԃ��܂��B
			//-----------------------------------------------------------------
			Attributes atr = mf.getMainAttributes();
			System.out.println("# Attributes Size=>"+atr.size() );

			System.out.println("<keySet   >#############################");
			Iterator itr1 = atr.keySet().iterator();
			while(itr1.hasNext()){
				System.out.println("=>"+itr1.next());
			}
			System.out.println("<entrySet >#############################");
			Iterator itr2 = atr.entrySet().iterator();
			while(itr2.hasNext()){
				System.out.println("=>"+itr2.next());
			}
			System.out.println("<getEntries>#############################");
			//--------------------------------------------------------------------------
			// getEntries	���� Manifest �Ɋi�[����Ă���G���g���� Map ��Ԃ��܂��B
			//--------------------------------------------------------------------------
			Map mp = mf.getEntries();
			System.out.println("# Map Size=>"+mp.size() );
	 			Iterator itr3 = mp.keySet().iterator();
			while(itr3.hasNext()){
				System.out.println("=>"+itr3.next());
			}
			jFile.close();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	//-------------------------------------------------------------------------
	// getOutPath �t�@�C�����̕����𒸂��ďo�̓t�@�C�����ȂǂɎg�p����
	// ex> getOutPath("c:\test.txt",".zip") => c:\test.zip ���Ԃ����
	// ??> getOutPath(".\test.txt",".zip")  => .\test.zip �ɂȂ��Ă���Ȃ���
	//-------------------------------------------------------------------------
	private static String getOutPath(String wFile_i,String pSuffix){
		System.out.println("getOutPath  �@=>"+ wFile_i);
		String wPath = wFile_i.trim();
		int pos = wPath.lastIndexOf('.');
		if ( pos > 1 ){
			wPath = wPath.substring(0,pos) + pSuffix;
		}else{
			wPath = wPath + pSuffix;
		}
		System.out.println("getOutPath  �A=>"+ wPath);
		return wPath;
	}
	//-------------------------------------------------------------------------
	// getOutDir
	//-------------------------------------------------------------------------
	private static String getOutDir(String wFile_i){
		String wSep = System.getProperty("file.separator");
		String wPath = getOutPath(wFile_i,wSep);

//		System.out.println("getOutDir  �@=>"+ wFile_i);
//		String wPath = wFile_i.trim() + ".";
//		wPath = wPath.substring(0,wPath.indexOf('.')) + wSep;
//		System.out.println("getOutDir  �A=>"+ wPath);

		File file = new File(wPath);
		file.mkdirs();
		wPath = file.getAbsolutePath() + wSep;
		System.out.println("getOutDir  =>"+ wPath);
		return wPath;
	}
	//-------------------------------------------------------------------------
	// �v�`�t�@�C���̑��݃`�F�b�N
	//-------------------------------------------------------------------------
	private static int fileCheck(String wFile_i){
		File wFile = new File(wFile_i);
		if (wFile.exists() == false ){
			System.out.println("@fileCheck Sorry... File Not Exists!!=>"+wFile_i);
			return -1;
		}
		if (wFile.isFile() == true ){
			return IS_FILE;
		}else if (wFile.isDirectory() == true ){
			return IS_DIRECTORY;
		}else{
			return OTHERKIND;
		}
	}
	//-------------------------------------------------------------------------
	// �����^�C������R�}���h�̎��s�E�E�E�����ɂ���̂��ǂ��Ȃ񂾂낤�E�E�Ƃ肠����
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------
	// �f�R���p�C��
	//-------------------------------------------------------------------------
	public static String deCompileIt(String wPath){
		String ans = "";
		File file = new File(wPath);
		//String dir   = file.getParent();
		String fname = file.getName();
		fname = fname.substring(0,fname.lastIndexOf("."));
		try {
			String cmd = "jad " + fname;
			System.out.println("�R�}���h����" + cmd);
			Process p = Runtime.getRuntime().exec(cmd);
			byte[] res = new byte[1024];
			InputStream in = p.getInputStream();
			while (in.read(res) > 0){
				String result = new String(res);
				ans += new String(result);
			}
		} catch(Exception ex){}
		return ans;
	}
	//-------------------------------------------------------------------------
	// �f�B�X�A�Z���u��
	//-------------------------------------------------------------------------
	public static String disAssemble(String wPath){
		String ans = "";
		File file = new File(wPath);
		String dir   = file.getParent();
		String fname = file.getName();
		fname = fname.substring(0,fname.lastIndexOf("."));
		try {
			String cmd = "javap -classpath \""+ dir +"\" -c " + fname;
			System.out.println("�R�}���h����" + cmd);
			Process p = Runtime.getRuntime().exec(cmd);
			byte[] res = new byte[1024];
			InputStream in = p.getInputStream();
			while (in.read(res) > 0){
				String result = new String(res);
				ans += new String(result);
			}
		} catch(Exception ex){}
		return ans;
	}
	//-------------------------------------------------------------------------
	// �N���X�p�X��ɂw�w�w�����݂��邩�ǂ������ׂ�
	//-------------------------------------------------------------------------
}
