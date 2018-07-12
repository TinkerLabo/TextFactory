package kyPkg.uFile;

import static kyPkg.uFile.FileUtil.getDefaultDelimiter;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FileUtil_Test {
	//	@Test
	//	public void testoFileChk(){
	//		assertEquals("通常ファイルではありません：.", kyPkg.uFile.FileUtil_.oFileChk("."));
	//	}

	@Test
	public void testGetFirstName() {
		String userDir = globals.ResControl.getQPRHome();
		assertEquals("current.atm",
				FileUtil.getFirstName2(userDir + "current.atm.zip"));
		assertEquals("current",
				FileUtil.getFirstName2(userDir + "current.atm"));
		assertEquals("current", FileUtil.getFirstName2("./current.atm"));
	}

	@Test
	public void testgetOtherExt() {
		assertEquals("c:/suzy/cream/cheeze/zappa.xls",
				FileUtil.changeExt("c:/suzy/cream/cheeze/zappa.txt", "xls"));
	}

	public void testNormarizeIt() {
		assertEquals("c:/suzy/cream/cheeze/zappa.txt",
				FileUtil.normarizeIt("c:/suzy/cream/cheeze/zappa.txt"));
	}

	@Test
	public void testGetAbsolutePath() {
		assertEquals("T:\\workspace\\QPRweb\\.",
				FileUtil.getAbsolutePath("./"));
	}

	@Test
	public void testGetPreExt() {
		String rootDir = globals.ResControl.getQprRootDir();
		String userDir = globals.ResControl.getQPRHome();
		assertEquals(rootDir + "test/zapp",
				FileUtil.getPreExt(rootDir + "test/zapp.txt"));
		assertEquals(rootDir + "test",
				FileUtil.getPreExt(rootDir + "test.txt"));
		assertEquals(userDir + "current",
				FileUtil.getPreExt(userDir + "current.atm"));
		assertEquals(userDir + "current",
				FileUtil.getPreExt(userDir + "current"));
	}

	@Test
	public void testGetExt() {
		String rootDir = globals.ResControl.getQprRootDir();
		String userDir = globals.ResControl.getQPRHome();
		assertEquals("EXT", FileUtil.getExt(rootDir + "test.txt.ext"));
		assertEquals("TXT", FileUtil.getExt(rootDir + "test.txt"));
		assertEquals("ATM", FileUtil.getExt(userDir + "current.atm"));
		assertEquals("", FileUtil.getExt(userDir + "current"));
	}

	@Test
	public void testGetDelimByExt() {
		assertEquals(",", getDefaultDelimiter("TXT"));
		assertEquals(",", getDefaultDelimiter("CSV"));
		assertEquals("\t", getDefaultDelimiter("PRN"));
		assertEquals("\t", getDefaultDelimiter("TSV"));
	}

	@Test
	public void testGetParent() {
		String userDir = globals.ResControl.getQPRHome();
		assertEquals(userDir,
				FileUtil.getParent2(userDir + "current.atm.zip", true));
	}

	// 拡張子を除いたファイル名（親パスは含まない）
	public static void testGetFirstNamexxx() {
		System.out.println("## tester of getFirstName ##");
		List<String> list = new ArrayList<String>();
		list.add("./current.atm");
		String userDir = globals.ResControl.getQPRHome();
		list.add(userDir + "current.atm.zip");
		list.add(userDir + "current.atm");
		for (String path : list) {
			System.out.println(path + " => " + FileUtil.getFirstName2(path));
		}

	}

	// 拡張子を書き換える
	public static void testCnvExt() {
		System.out.println("## tester of CnvExt ##");
		List<String> list = new ArrayList<String>();
		list.add("./current.atm");
		String userDir = globals.ResControl.getQPRHome();
		list.add(userDir + "current.atm.zip");
		list.add(userDir + "current.atm");
		for (String path : list) {
			System.out.println(path + " => " + FileUtil.cnvExt(path, "dif"));
		}
	}

	// 親パスを書き換える
	public static void testCnvParent() {
		String userDir = globals.ResControl.getQPRHome();
		System.out.println("## tester of cnvParent ##");
		List<String> list = new ArrayList<String>();
		list.add("./current.atm");
		list.add(userDir + "current.atm.zip");
		list.add(userDir + "current.atm");
		for (String path : list) {
			System.out.println(
					path + " => " + FileUtil.cnvParent("z:/test.txt", path));
		}
	}
	public static void readHeaderTest() {
		String path = "C:/testDataBase/QPR.Db";
		boolean flag = FileUtil.readHeader(path, "SQLite ");
		System.out.println("readHeader test=>" + flag);
	}
	public static void testPath() {
		// 以下の結果が返る（相対ﾊﾟｽ表記を使った場合にAbsolute Pathが冗長）
		// getPath:..\link.lnk
		// getName:link.lnk
		// Absolute Path:T:\workspace\QPRweb\..\link.lnk
		// CanonicalPath:T:\workspace\link.lnk
		try {
			String path = "../link.lnk";
			// path =
			// "c:/@qpr/828111000509/購入水準【購入年月】2013年02月01日～2013年02月28日0405_171955.xls";
			File file = new File(path);
			System.out.println("getPath:" + file.getPath());
			System.out.println("getName:" + file.getName());
			System.out.println("Absolute Path:" + file.getAbsolutePath());
			System.out.println("CanonicalPath:" + file.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] argv) {
		testPath();
	}
}
