package kyPkg.external;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ApachUtil {

	public ApachUtil() {
		// TODO Auto-generated constructor stub
	}

	//-------------------------------------------------------------------------
	//	�ċN�I�Ƀf�B���N�g���������i���߂�explorer�ŊJ���Ă����肷��ƃG���[�ƂȂ�j
	//-------------------------------------------------------------------------
	public static void rmdir(String path) {
		try {
			FileUtils.deleteDirectory(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//�T�[�o�[�p�X�͏����Ȃ��̂��H
	public static void testRmDir() {
		System.out.println("##testRmDir## start");
		String itpDir = globals.ResControlWeb.getD_Resources_ItpDir();
		System.out.println("itpDir:" + itpDir);
		ApachUtil.rmdir(itpDir);
		System.out.println("##testRmDir## end");
	}

	public static void main(String[] args) {
		//		Itp_Parse.testItp_Parse();
		testRmDir();
	}
}
