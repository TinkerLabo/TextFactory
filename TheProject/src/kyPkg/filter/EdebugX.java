package kyPkg.filter;

import java.util.*;

import globals.ResControl;
import kyPkg.uFile.FileUtil;

public class EdebugX implements Inf_FileConverter {
	// private static boolean DEBUG = true;
	final String wLs = System.getProperty("line.separator");

	private String info = "";

	private int stat = -1;

	private Inf_oClosure writerP;

	private Inf_oClosure writerN;

	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;

	private HashSet hashSet;

	private int targetCol;

	private int writeOption = 1; // Defaultはpositiveのみ

	public void setTargetCol(int targetCol) {
		this.targetCol = targetCol;
	}

	public void setWriteOption(int writeOption) {
		this.writeOption = writeOption;
	}

	public String getInfo() {
		return info;
	}

	// -------------------------------------------------------------------------
	// 対象データの区切り文字対象カラム
	// マスタの区切り文字対象カラム、および
	// 出力オプションを指定できる
	// opt1&=1 １が含まれている場合・・・・マッチング分を*.posとして出力する
	// opt1&=2 ２が含まれていない場合・・・アンマッチ分を*.negとして出力する
	// 3なら*.pos、*.neg両方、0なら出力されず件数のみのチェックに使用できる
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public EdebugX(int targetCol, String str) {
		this.targetCol = targetCol;
		hashSet = setList2hash(Arrays.asList(new String[] { str }));
	}

	public EdebugX(int targetCol, String[] array) {
		this.targetCol = targetCol;
		hashSet = setList2hash(Arrays.asList(array));
	}

	public EdebugX(int targetCol, List list) {
		this.targetCol = targetCol;
		hashSet = setList2hash(list);
	}

	public EdebugX(int targetCol, String masterPath, int masterCol) {
		this.targetCol = targetCol;
		hashSet = parmIncore(masterPath, masterCol);
	}

	// -------------------------------------------------------------------------
	// リストをハッシュに格納している
	// -------------------------------------------------------------------------
	private HashSet setList2hash(List list) {
		HashSet hashSet = new HashSet(list.size());
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			hashSet.add((String) iter.next());
		}
		hashSet.remove(null); // nullは対象外としている
		return hashSet;
	}

	// -------------------------------------------------------------------------
	// ファイルの任意の位置にあるデータをハッシュに格納
	// -------------------------------------------------------------------------
	private HashSet parmIncore(String path, int col) {
		HashSet hashSet = new HashSet(256);
		try {
			EzReader pReader = new EzReader(path);
			pReader.open();
			while ((pReader.readLine()) != null) {
				String[] splited = pReader.getSplited();
				if (splited.length > col) {
					hashSet.add(splited[col]);
				}
			}
			pReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		hashSet.remove(null); // nullは対象外としている
		return hashSet;
	}

	// if (DEBUG) {
	// Iterator it = hashSet.iterator();
	// while (it.hasNext())
	// System.out.println("hashSet=>" + it.next());
	// }

	// -------------------------------------------------------------------------
	// 出力ディレクトリを省略した場合は入力データと同じ階層
	// -------------------------------------------------------------------------
	public int execute(String inPath) {
		String outDir = FileUtil.getParent(inPath) + "/";
		System.out.println("■outDir:" + outDir);
		return execute(outDir, inPath);
	}

	public int execute(String outDir, String inPath) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("EDEBUG1");
		elapse.start();
		fileConvert(outDir, inPath);
		elapse.stop();
		info = elapse.getInfo(true);
		return stat;
	}

	@Override
	public void fileConvert(String outDir, String inPath) {
		// System.out.println("@fileConvert■outDir:" + outDir);
		outDir = FileUtil.mkdir(outDir);
		writerP = null;
		writerN = null;
		if (hashSet == null) {
			System.out.println("Error hashSet == null");
			stat = -1;
			return;
		}
		int negCnt = 0;
		int posCnt = 0;
		try {
			String firstname = FileUtil.getFirstName2(inPath);
			if ((writeOption & 1) == 1) {
				String oPath_Pos = outDir + firstname + ".pos"; // .posが無いという前提
				writerP = new EzWriter(oPath_Pos);
				writerP.open();
				System.out.println("■oPath_Pos:" + oPath_Pos);
			}
			if ((writeOption & 2) == 2) {
				String oPath_Neg = outDir + firstname + ".neg"; // .negが無いという前提
				writerN = new EzWriter(oPath_Neg);
				writerN.open();
				System.out.println("■oPath_Neg:" + oPath_Neg);
			}
			reader = new EzReader(inPath);
			// System.out.println("■inPath:" + inPath);
			reader.open();
			while (reader.readLine() != null) {
				String[] splited = reader.getSplited();
				if (splited.length > targetCol) {
					if (hashSet.contains(splited[targetCol])) {
						posCnt++;
						if (writerP != null)
							writerP.write(reader.getCurrent());
					} else {
						negCnt++;
						if (writerN != null)
							writerN.write(reader.getCurrent());
					}
				}
			}
			reader.close();
			if (writerP != null)
				writerP.close();
			if (writerN != null)
				writerN.close();
			// System.out.println(info);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		if (writeOption == 2) {
			stat = negCnt;
		} else {
			stat = posCnt;
		}
		info += "positive write:" + posCnt + "\n";
		info += "negative write:" + negCnt + "\n";
	}

	// ここをやる！！！2009/04/16 予定→688の購買データを全部拾い上げるのに必要！！
	// 対象ファイル名をregixで指定したい！！
	public static void dirIter() {
		String srcDir = ResControl.D_DRV
				+ "eclipse/workspace/kyProject/●重要●/2007_20090417(最納品後)";
		kyPkg.filter.DirIterate insConverter = new kyPkg.filter.DirIterate(
				new EdebugX(4, "698"), ResControl.D_DRV + "dst", srcDir,
				"*.qpr_buying_data_*.txt");
		insConverter.execute();
		System.out.println("** finish! **");
	}

}
