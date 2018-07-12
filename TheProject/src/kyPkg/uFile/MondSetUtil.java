package kyPkg.uFile;

import static kyPkg.uFile.YamlControl.YML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

import globals.ResControl;
import kyPkg.atoms.AtomDB;

public class MondSetUtil {
	// ------------------------------------------------------------------------
	// 旧ＲＥＰのモニター抽出条件より、新ﾓﾆﾀｰ抽出条件をインポートする
	// 抽出条件のコメントはメタデータとして別ファイルに出力している
	// ------------------------------------------------------------------------
	public static void openMonSetDir() {
		String monSetsDir = ResControl.getMonSetsDir();
		DosEmu.openWithExplorer(" /n,/e,/root," + monSetsDir);
	}

	// ------------------------------------------------------------------------
	// 既存のﾓﾆﾀｰセレクト条件よりlimitリソースを生成する
	// XXX あとでインタフェースを準備する
	// ------------------------------------------------------------------------
	public static int importFromMonBank(String bankName) {
		if (bankName == null || bankName.equals("指定しない"))
			return -1;
		// TODO ﾓﾆﾀｰID以外のものが指定されているケースがあるので注意
		// ※たとえば　東海地方モニター群　などはidではなくホストのバッチステップが記述されている

		// XXX 110811 地方別ﾓﾆﾀｰ群はバッチ生成して共用してもよいかもしれない
		// XXX 110811 他のアンケート情報も一般的なものはまとめてバッチ生成して共用するのがよいだろう

		String userDir = ResControl.getQPRHome();
		String limitPath = userDir + "Limit" + AtomDB.LIM;
		String ymlPath = userDir + "Limit." + YML;

		String monSetsDir = ResControl.getMonSetsDir();
		//	String bankPath =ResControl.getMonBankDir(bankName);
		String path = monSetsDir + bankName;
		System.out.println("monBank:" + path);
		System.out.println("limitPath:" + limitPath);

		// ------------------------------------------------------------------------
		// コレを呼んでlimit何がしに変換すりゃいいのさ
		// 簡易読み込みを行い先頭とおしまいのいらないところをちょん切ればｏｋかな？
		// ------------------------------------------------------------------------
		// //MONSCPY.COMMENT DD *
		// ＮＱ単身者　　　　　　　　　　
		// //MONSCPY.SYSIN DD *
		// /*
		// ------------------------------------------------------------------------
		String comment = "";
		StringBuffer buff = new StringBuffer();
		String key = "//MONSCPY.COMMENT DD * ";
		String begin = "//MONSCPY.SYSIN DD * ";
		String end = "/* ";
		String LE = "\n";
		int cnt = 0;
		try {
			boolean flag1 = false;
			boolean flag2 = false;
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter bw = new BufferedWriter(new FileWriter(limitPath));
			while (br.ready()) {
				String rec = br.readLine();
				if (rec.startsWith(begin)) {
					flag1 = false;
					flag2 = true;
				} else if (rec.startsWith(end)) {
					flag1 = false;
					flag2 = false;
				} else if (rec.startsWith(key)) {
					flag1 = true;
				} else {
					// 抽出条件コメント
					if (flag1) {
						buff.append(rec.trim());
					}
					// Monitor ID
					if (flag2) {
						cnt++;
						bw.write(rec);
						bw.write(LE);
					}
				}
			}
			bw.close();
			br.close();
		} catch (Exception e) {
		}
		comment = buff.toString();
		System.out.println("comment:" + comment);
		// comment => 同名のymlに保存・・・かな
		HashMap<String, String> infoMap = new HashMap();
		infoMap.put(AtomDB.COMMENT1, comment);
		new YamlControl(infoMap).saveAs(ymlPath);
		return cnt;
	}
}
