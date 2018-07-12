package kyPkg.uFile;

import java.io.File;
import java.util.Arrays;
import kyPkg.uDateTime.DateCalc;

public class BackupUtil {

	private static final String BAK = "bak";

	public BackupUtil() {
	}

	// ------------------------------------------------------------------------
	// 拡張子を変更する
	// ------------------------------------------------------------------------
	public static String changeExt(String path, String ext) {
		int pos = path.lastIndexOf(".");
		return path.substring(0, pos) + ext;
	}

	// ------------------------------------------------------------------------
	// バックアップをコピーを作る
	//20170327 タイムスタンプではなく　対象ファイルの最終修正日付をタイムスタンプとした
	// ------------------------------------------------------------------------
	private static String createBackup(String path, String optStr) {
		if (path.trim().equals("") || new File(path).isFile() == false)
			return null;
		String timeStmp = DateCalc.getLastModDate(path, "yyyyMMddHHmmss");
		//String timeStmp = getTimeStamp();
		String bkup = changeExt(path, "_" + timeStmp + optStr + "." + BAK);
		DosEmu.copy(path, bkup);//		DosEmu.move(orgPath, bkup);
		return bkup;
	}

	// ------------------------------------------------------------------------
	// 拡張子を変更する
	// ------------------------------------------------------------------------
	//出力ファイル名と同じもので始まって_が続くファイルで世代管理を行う
	//	BackupUtil.createBackup(String path, int maxGen)
	// ------------------------------------------------------------------------
	public static String createBackup(String path, int maxGen) {
		//入力ファイルの名称にタイムスタンプを付加した名称のバックアップを作成する
		//実行者のユーザーＩＤも付加するかどうか検討する
		String uerId = kyPkg.util.RuntimeEnv.getUserID();
		String bkPath = createBackup(path, uerId);
		if (bkPath == null)
			return null;
		String dir = FileUtil.getParent(path);
		String firstName = FileUtil.getFirstName2(path);
		System.out.println("Dir:" + dir + " FirstName:" + firstName);
		String[] array = FileUtil.regixFilteredList(dir,
				firstName + "_.*" + BAK);
		Arrays.sort(array); //SORT 日付の新しいものは残す
		int j = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			j++;
			if (j > maxGen) {
				String wPath = dir + "/" + array[i];
				System.err.println("kil=>" + wPath);
				if (new File(wPath).exists())
					new File(wPath).delete();
			} else {
				System.out.println("kpt=>" + array[i]);
			}

		}
		return bkPath;
	}

	public static void main(String[] argv) {
		int maxGen = 5;
		String dstPath = "c:/test.txt";
		createBackup(dstPath, maxGen);//競合した場合どうなるかか・・・
	}

}
