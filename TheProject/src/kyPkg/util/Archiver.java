package kyPkg.util;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.jar.*;
//-----------------------------------------------------------------------------
//囚人のジレンマ
//http://ccs.cla.kobe-u.ac.jp/Jouhou/95/Takahasi/prisoner.html
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//	追加予定→アーカイブのエントリー内容をリスト化する
//-----------------------------------------------------------------------------
public class Archiver {
	final static int IS_FILE = 1;
	final static int IS_DIRECTORY = 0;
	final static int OTHERKIND = 9;
	//-------------------------------------------------------------------------
	// □使用例□
	//-------------------------------------------------------------------------
	public static void main( String argv[] ) {
		String o_File;
		String i_File;
		System.out.println("argv.length:"+argv.length);
		if (argv.length==1){
			System.out.println("解凍中！");
			i_File = argv[0];
			Archiver.deCompZipAll(i_File);		//解凍
		}else if (argv.length==2){
			System.out.println("圧縮中！");
			o_File = argv[0];
			i_File = argv[1];
			Archiver.enCompZip(o_File,i_File);   //圧縮
		}else{
			//Archiver
			System.out.println("jarよりlibを取り出します");
			String wFile_i = "AppCtr.jar";
			String wKeywrd = "lib/";
			File wKwd = new File(wKeywrd);
			if (wKwd.isDirectory() == false ){
				// Jarアーカイヴからキーワードで始まるエントリーを取り出す
				System.out.println("libを取り出す");
				Archiver.ExtractJarKW(wFile_i,wKeywrd);
			}
		}
	}
	//-------------------------------------------------------------------------
	// すべてのエントリーを解凍する（jarも解凍してくれるようだ・・）
	//-------------------------------------------------------------------------
	public static void deCompZipAll(String wFile_i){
		try{
			if (fileCheck(wFile_i)!=IS_FILE) return; // fileじゃなかったら・・・
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
	// 指定されたエントリーを解凍する
	//-------------------------------------------------------------------------
	public static void deCompZip(ZipFile zFile,ZipEntry zEntry){
		deCompZip(zFile,zEntry,"");  // これでカレントパスになるかね？
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
	// ファイル（指定ディレクトリ以下）をＺＩＰに圧縮する
	//	Archiver.enCompZip(o_File,i_File);   // 圧縮
	//-------------------------------------------------------------------------
	public static void enCompZip(String i_File) {
		String o_File = getOutPath(i_File,".zip");
		enCompZip(o_File,i_File);
	}
	public static void enCompZip(String o_File,String i_File) {
		System.out.println("●~* o:"+o_File);
		System.out.println("●~* i:"+i_File);
		try {
			File zipFile = new File( o_File );
			if( zipFile.exists() ) {
				System.err.println( o_File + "が既に存在するので中断しました" );
				System.exit(0);
			}
			System.out.println  ("■ enCompZip 出力先:" + o_File );
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
				System.out.println  ("＞ 追加:" +  wPath);
				enZip( zos,wPath); // zipに追加！！
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
	// 与えられたファイルを圧縮し、Zipファイルに追加する
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
			System.out.println("どうだ？" + wPath);
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
	// ディレクトリとファイル名の一覧を返すメソッド
	//-------------------------------------------------------------------------
	public static ArrayList pathLists( File pFile )
			throws FileNotFoundException, IOException {
		System.out.println("● " + pFile);
		ArrayList names = new ArrayList();
		try {
			if ( pFile.isFile() ){
				System.out.println("■01■ " + pFile.getPath());
				names.add( pFile.getPath() );
			} else if ( pFile.isDirectory() ){
				String parentName = pFile.getPath();
				String[] fl = pFile.list();
				for( int i=0; i < fl.length; i++ ) {
					String pathName = parentName + File.separator + fl[i];
					File wFile = new File( pathName );
					if( wFile.isFile() ){
						System.out.println("■02■ " + wFile.getPath());
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
	// キーワードで始まるものをｊａｒファイルより取り出します
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
	// 指定されたエントリーをｊａｒファイルより取り出します
	//-------------------------------------------------------------------------
	public static void ExtractJar(JarFile zipFile,JarEntry target ){
		try{
			System.out.print  (" ■ Entry:"+target.getName() );
			System.out.println(" ■ Size :"+target.getSize() );
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
	// ｊａｒファイルのマニフェスト情報を一覧表示します（雛形）
	// Archiver.ManiInfo("AppCtr.jar");
	//-------------------------------------------------------------------------
	public static void ManiInfo(String wFile_i){
		try{
			if (fileCheck(wFile_i)!=IS_FILE) return; // fileじゃなかったら・・・
			File wFile = new File(wFile_i);
			if (wFile .exists() == false ){
				System.out.println("@ ManiInfo Sorry... File Not Exists!!=>"+wFile_i);
				return;
			}
			JarFile jFile = new JarFile(wFile_i);
			Manifest mf = jFile.getManifest();
			//-----------------------------------------------------------------
			// MainAttributes Manifest のメインとなる Attributes を返します。
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
			// getEntries	この Manifest に格納されているエントリの Map を返します。
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
	// getOutPath ファイル名の部分を頂いて出力ファイル名などに使用する
	// ex> getOutPath("c:\test.txt",".zip") => c:\test.zip が返るつもり
	// ??> getOutPath(".\test.txt",".zip")  => .\test.zip になってくれないね
	//-------------------------------------------------------------------------
	private static String getOutPath(String wFile_i,String pSuffix){
		System.out.println("getOutPath  ①=>"+ wFile_i);
		String wPath = wFile_i.trim();
		int pos = wPath.lastIndexOf('.');
		if ( pos > 1 ){
			wPath = wPath.substring(0,pos) + pSuffix;
		}else{
			wPath = wPath + pSuffix;
		}
		System.out.println("getOutPath  ②=>"+ wPath);
		return wPath;
	}
	//-------------------------------------------------------------------------
	// getOutDir
	//-------------------------------------------------------------------------
	private static String getOutDir(String wFile_i){
		String wSep = System.getProperty("file.separator");
		String wPath = getOutPath(wFile_i,wSep);

//		System.out.println("getOutDir  ①=>"+ wFile_i);
//		String wPath = wFile_i.trim() + ".";
//		wPath = wPath.substring(0,wPath.indexOf('.')) + wSep;
//		System.out.println("getOutDir  ②=>"+ wPath);

		File file = new File(wPath);
		file.mkdirs();
		wPath = file.getAbsolutePath() + wSep;
		System.out.println("getOutDir  =>"+ wPath);
		return wPath;
	}
	//-------------------------------------------------------------------------
	// プチファイルの存在チェック
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
	// ランタイムからコマンドの実行・・・ここにあるのもどうなんだろう・・とりあえず
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------
	// デコンパイル
	//-------------------------------------------------------------------------
	public static String deCompileIt(String wPath){
		String ans = "";
		File file = new File(wPath);
		//String dir   = file.getParent();
		String fname = file.getName();
		fname = fname.substring(0,fname.lastIndexOf("."));
		try {
			String cmd = "jad " + fname;
			System.out.println("コマンド＝＞" + cmd);
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
	// ディスアセンブル
	//-------------------------------------------------------------------------
	public static String disAssemble(String wPath){
		String ans = "";
		File file = new File(wPath);
		String dir   = file.getParent();
		String fname = file.getName();
		fname = fname.substring(0,fname.lastIndexOf("."));
		try {
			String cmd = "javap -classpath \""+ dir +"\" -c " + fname;
			System.out.println("コマンド＝＞" + cmd);
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
	// クラスパス上にＸＸＸが存在するかどうか調べる
	//-------------------------------------------------------------------------
}
