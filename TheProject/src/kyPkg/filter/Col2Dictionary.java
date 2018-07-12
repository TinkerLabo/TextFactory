package kyPkg.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kyPkg.uFile.FileUtil;

/**************************************************************************
 * Col2Dictionary　指定したセルの値をインデックス辞書に変更する				
 * @author	ken yuasa
 * @version	1.0
 **************************************************************************/
@SuppressWarnings("unused")
public class Col2Dictionary implements Inf_BaseClojure {
	private String dictDimName;
	private int col;
	private String delimiter = "\t";
	private HashSet<String> keySet;
	private Set<String> dummySet = null;

	//	private List<String> pref=null;
	public void setDummySet(Set<String> dummySet) {
		this.dummySet = dummySet;
	}

	private int dimCount;

	public int getDimCount() {
		return dimCount;
	}

	/**************************************************************************
	 * Col2Dictionary　コンストラクタ
	 * @param dictDimNamePath			 
	 * @param col			 
	 **************************************************************************/
	public Col2Dictionary(String dictDimNamePath, int col) {
		this.dictDimName = dictDimNamePath; // Index to dimName
		this.col = col;
	}

	@Override
	public void init() {
		keySet = new HashSet();
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			keySet.add(rec[col]);
		}
	}

	@Override
	public void write() {
		if (dummySet != null)
			keySet.addAll(dummySet); //TODO ここをよぶ直前に不足分のキーをｈSetに追加してしまえばダミー処理にならないかな・・・・と期待
		//		String[] prefix=new String[]{ModsCommon.NOT_BUY};//"非購入"を先頭に追加
		String[] prefix = null;
		dimCount = DictionaryControl.writeSortedDict2File(dictDimName, keySet,
				prefix);
	}

	//-----------------------------------------------------------
	//指定カラムからインデックス辞書を作成する
	//20151120 非購入を先頭に挿入する？
	//-----------------------------------------------------------
	public static int convert(String outPath, String dictPath, String inPath,
			int col, Set<String> dummySet) {
		Col2Dictionary clojure = new Col2Dictionary(dictPath, col);
		clojure.setDummySet(dummySet);

		new CommonClojure().incore(clojure, inPath, true);
		int dimCount = clojure.getDimCount();
		if (dimCount > 0) {
			//-----------------------------------------------------------
			//インデックス辞書により名称をインデックスに置き換える
			//-----------------------------------------------------------
			HashDecoder idxCloj = new HashDecoder(outPath, dictPath, col, -1);
			new CommonClojure().incore(idxCloj, inPath, true);
		}
		return dimCount;
	}

	public static void main(String[] argv) {
		tester();
	}

	public static void tester() {
		int col = 3;
		String userDir = globals.ResControl.getQPRHome();
		String inPath = userDir + "828111000507/calc/trrModItp.txt";
		String outPath = userDir + "828111000507/calc/indexed.txt";
		String dirPath = FileUtil.getParent(inPath) + "/";
		String dictPath = dirPath + "dimName.txt";
		int dimCount = convert(outPath, dictPath, inPath, col, null);
		System.out.println("dimCount:" + dimCount);
		//-----------------------------------------------------------
		//インデックスから区分名称に変換する
		//-----------------------------------------------------------
		String recover = userDir + "828111000507/calc/recover.txt";
		HashDecoder nameCloj = new HashDecoder(recover, dictPath, col, -2);
		new CommonClojure().incore(nameCloj, outPath, true);
	}

}
