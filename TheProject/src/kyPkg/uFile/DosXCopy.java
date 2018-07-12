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
	// xcopy /e /i source target �f�B���N�g�����ċA�I�ɃR�s�[
	//	/K	�����i�t�@�C���쐬���t�A�X�V���t�Ȃǁj���R�s�[����
	//	/Y	�u�㏑�����ėǂ��H�v���Đu���Ă��Ȃ�
	//-------------------------------------------------------------------------
	//�R�s�[��Ƀt�@�C�������w�肷��ƈȉ��̂悤�ɉ����҂��ɂȂ��Ă��܂��׃R�s�[��̓t�H���_���w�肷��
	//	C:\xxxx.txt �͎󂯑��̃t�@�C�����ł����A
	//	�܂��̓f�B���N�g�����ł���
	//	(F= �t�@�C���AD= �f�B���N�g��)?
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
		pb.redirectErrorStream(true);// �W���o�͂ƕW���G���[���}�[�W���� (Defualt false)
		try {
			Process process = pb.start();
			process.waitFor();
			InputStream is = process.getInputStream(); // ���ʂ̏o��
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