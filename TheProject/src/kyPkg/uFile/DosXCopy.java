package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DosXCopy {
//	private static final String optI = "/i";
//	private static final String optE = "/e";
	private static final String optK = "/K";
	private static final String optY = "/Y";
	private static final String XCOPY = "xcopy";

	private static void printInputStream(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			for (;;) {
				String line = br.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}
		} finally {
			br.close();
		}
	}


	//-------------------------------------------------------------------------
	// xcopy /e /i source target ディレクトリを再帰的にコピー
	//	/K	属性（ファイル作成日付、更新日付など）もコピーする
	//	/Y	「上書きして良い？」って訊いてこない
	//-------------------------------------------------------------------------
	//コピー先にファイル名を指定すると以下のように応答待ちになってしまう為コピー先はフォルダを指定する
	//	C:\xxxx.txt は受け側のファイル名ですか、
	//	またはディレクトリ名ですか
	//	(F= ファイル、D= ディレクトリ)?
	//-------------------------------------------------------------------------
	public static void xcopy_ky(String srcPath, String dstPath) {
		//#createTester--------------------------------------------------
		//		System.out.println("public static void testXcopy_ky() {");
		//		System.out.println("    String srcPath = \"" + srcPath + "\";");
		//		System.out.println("    String dstPath = \"" + dstPath + "\";");
		//		System.out.println("    xcopy_ky ins = new xcopy_ky(srcPath,dstPath);");
		//		System.out.println("}");
		//--------------------------------------------------
		String dstDir = FileUtil.getParent(dstPath, true);
		srcPath = FileUtil.cnv2localStyle(srcPath);
		dstDir = FileUtil.cnv2localStyle(dstDir);
		//		System.out.println(">>> " + XCOPY + " " + srcPath + " " + dstDir + " "
		//				+ optK + " " + optY);
		ProcessBuilder pb = new ProcessBuilder(XCOPY, srcPath, dstDir, optK,
				optY);
		pb.redirectErrorStream(true);// 標準出力と標準エラーをマージする (Defualt false)
		try {
			Process process = pb.start();
			process.waitFor();
			InputStream is = process.getInputStream(); // 結果の出力
			printInputStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//xcopy_ky(String target, String source) 
	public static void main(String args[]) {
		test20170616();
	}

	public static void test20170616() {
		//	xcopy \\ks1s014\itpBANKs\#110098000002.IT2 C:\@res\ /K /Y
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		// -----------------------------------------------------------------
		String srcPath = "\\\\ks1s014\\itpBANKs\\#110098000002.IT2";
		String dstDir = "C:\\@res\\";
		DosXCopy.xcopy_ky(srcPath, dstDir);//20170616
		// -----------------------------------------------------------------
		elapse.stop();
	}

}