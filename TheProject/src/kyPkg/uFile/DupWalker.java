package kyPkg.uFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//GUIを作る
//状態別削除カウントをつくる
//同じ名称のファイルのランキング・・・どんなファイルが同名でどれぐらい使われているのか？
//削除対象外とするファイルの拡張子を指定したい・・・
//もしくは削除対象とするファイルのレジックス？

//消してリンクを残す？！
//リンク先がないファイルは消す？
//URLファイルを編集する？？
//メッセージを返す？ログを残す？？
public class DupWalker {
	private static boolean killer = false;

	// ---------------------------------------------------------------
	// cssとかサンプルファイルなど、同じファイルが再利用されているケースを除外する方法がないだろうか？？
	// ディレクトリの構成が似たものをグルーピングする方法はないか？？
	// 　指定したファイルを削除、結果当該ディレクトリにファイルが存在しない場合にそのディレクトリを削除する
	// ---------------------------------------------------------------

	// 例》 String wFinfo = FileUtil.FilelastModified("System.ini");
	public static String filelastModified(String pPath) {
		File wFile = new File(pPath);
		String wDate = DateFormat.getDateInstance()
				.format(new Date(wFile.lastModified()));
		SimpleDateFormat df1 = new SimpleDateFormat("hh:mm");
		String wTime = df1.format(new Date(wFile.lastModified()));
		return wDate + "_" + wTime;
	}

	// 例》 long size = FileUtil.getFileSize("System.ini");
	public static long getFileSize(String pPath) {
		File wFile = new File(pPath);
		long size = wFile.length(); // " KByte
		return size;
	}

	// -------------------------------------------------------------------------
	// 大文字小文字を曖昧にマッチングしたい場合＃２
	// 同じパターンを何回も使う場合はこちらがベター
	// 《使用例》
	// Pattern ptn = Regexx.patternIgnoreCase(pRegex);
	// wFlg = ptn.matcher(pCharSeq).find();
	// -------------------------------------------------------------------------
	// 大文字小文字が曖昧なパターンを作成
	// -------------------------------------------------------------------------
	// private static Pattern patternIgnoreCase(String pRegex) {
	// return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	// }

	// -------------------------------------------------------------------------
	// コマンドを実行する
	// ※実行前に対象を確認する方法が欲しい・・・
	// ※結果をバッファーに貯めるかどうか バーボウズオプション？！
	// ※対象がロックされている、アクセス制限などのエラー処理・・・などの問題
	// -------------------------------------------------------------------------
	private static List xCommand(String pSrc) {
		if (pSrc == null)
			return null;
		pSrc = pSrc.replaceAll("\\\\", "/");
		if (pSrc.lastIndexOf("/") == -1)
			pSrc = pSrc + "/";
		int xIdx = pSrc.lastIndexOf("/");
		String wPath1 = pSrc.substring(0, xIdx + 1);
		System.out.println("xCommand :" + wPath1);
		// Digger2 insDig = new Digger2(wPath1, "", true,killer);
		Digger2 insDig = new Digger2(wPath1, "", true, killer);
		insDig.search();
		String wSrc = "";
		Iterator it = insDig.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			wSrc = it.next().toString();
			boolean wFlg = false;
			list.add(wSrc);
			wFlg = true;
			if (wFlg == false) {
				System.out.println("Error @xCommand :dir");
				System.out.println("wSrc :" + wSrc);
			}
		}
		return list;
	}

	public static void dupWalker(String path, boolean flag) {
		LogWriter logIns = new LogWriter("./dupWalker.log");
		logIns.startMessage("<< dupWalker Start!!>> path:" + path);

		int killCount = 0;
		int killDirCount = 0;
		killer = flag;
		// 結果、空になったディレクトリを除去したい
		HashMap<String, List> hmap = new HashMap();
		List list = xCommand(path);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String pathy = (String) iterator.next();
			String fileName = FileUtil.getFileName(pathy);
			List value = (List) hmap.get(fileName);
			if (value == null) {
				value = new ArrayList();
				hmap.put(fileName, value);
			}
			String lastMod = filelastModified(pathy);
			long size = getFileSize(pathy);
			String wkStr = lastMod + "\t" + size + "\t" + pathy;
			value.add(wkStr);
		}
		//		System.out.println("---------------------------------------------------");
		logIns.println("---------------------------------------------------");
		java.util.Set set = hmap.entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			List valList = (List) ent.getValue();
			// System.out.println("key:" + key + "  size:" + valList.size());
			if (valList.size() > 1) {

				//				System.out.println("size:" + valList.size() + "　key:" + key);
				logIns.println("size:\t" + valList.size() + "\tkey:\t" + key);
				// XXX size update path-length, contents
				// もしサイズと日付が同じなら深いパスの側を削除する
				Collections.sort(valList);
				String prePathx = "";
				String preLastMot = "";
				long preSize = -1;
				for (Iterator iterator = valList.iterator(); iterator
						.hasNext();) {
					String rec = (String) iterator.next();
					String[] splited = rec.split("\t");
					String lastMod = splited[0];
					long size = Long.parseLong(splited[1]);
					String pathx = splited[2];
					//					System.out.println("   lastMod:" + lastMod  + " pathx:" + pathx + " size:" + size);
					//					logIns.println("\t\t   lastMod:" + lastMod  + " pathx:" + pathx + " size:" + size);
					if (size == preSize) {
						if (lastMod.equals(preLastMot)) {
							// サイズが同じで編集日付が同じなら、片方消す
							//							System.out.println("　　　　　=>preLastMot:" + preLastMot + " size:" + preSize + " kill:" + prePathx);
							//							logIns.println("\t\t  =>preLastMot:" + preLastMot + " size:" + preSize + " kill:" + prePathx);
							if (killer) {
								killDirCount += kyPkg.uFile.Digger2
										.chainReaction(prePathx);
							}
							killCount++;
							// この親の階層を調べてその配下のファイル数が０ならば、そのフォルダを削除する

						} else {
							// サイズが同じで日付が違う場合、内容が同じなら、片方消す
							int diff = new kyPkg.tools.HexCompare(prePathx,
									pathx, 1).compare();
							if (diff == 0) {
								//								System.out.println("　　　　　=>preLastMot:" + preLastMot + " size:" + preSize + " kill:" + prePathx);
								//								logIns.println("\t\t  =>preLastMot:" + preLastMot + " size:" + preSize + " kill:" + prePathx);
								if (killer) {
									killDirCount += kyPkg.uFile.Digger2
											.chainReaction(prePathx);
								}
								killCount++;
							}
						}
					}
					// System.out.println("path:"+pathx+" =>lastMod:"+lastMod+" size:"+size);
					prePathx = pathx;
					preLastMot = lastMod;
					preSize = size;
				}

			}
		}
		System.out
				.println("---------------------------------------------------");
		System.out.println("killCount:" + killCount);
		System.out.println("killDirCount:" + killDirCount);
		logIns.println("---------------------------------------------------");
		logIns.println("killCount   :" + killCount);
		logIns.println("killDirCount:" + killDirCount);
		logIns.close();
	}

	public static void main(String argv[]) {
		String path = "/Users/ken/debug/";
		path = "/Volumes/HD20GB/forWin/";
		dupWalker(path, true);
	}

}
