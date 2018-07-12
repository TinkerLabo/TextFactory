package kyPkg.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kyPkg.rez.HashList;
import kyPkg.uFile.HashMapUtil;

// ----------------------------------------------------------------------------
//　文字列を辞書変換するためのマップを（テキストファイルより）生成する
// ----------------------------------------------------------------------------
public class CnvDictionary implements Inf_StrConverter {
	private static final String COMMA = ",";
	private int grade = 0;
	private HashList hashList; // 順序性のあるハッシュマップ？ではない
	private Inf_StrConverter converter;
	private HashSet<String> keyNotFound;

	public HashSet<String> getKeyNotFound() {
		return keyNotFound;
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	protected CnvDictionary() {
		super();
		hashList = new HashList();
		keyNotFound = new HashSet();
	}

	public CnvDictionary(String path, int keyCol, int valCol) {
		this();
		hashList = HashMapUtil.file2HashList(path, keyCol, valCol);
	}

	protected CnvDictionary(String path) {
		this(path, 0, 1); // default、ではゼロ番目がキーカラムとなり、１番目が値のカラムとなる
	}

	protected CnvDictionary(Set<String> set) {
		this();
		List<String> list = new ArrayList(set);
		Collections.sort(list);
		hashList = incoreList(list, COMMA);
	}

	protected CnvDictionary(List list) {
		this();
		hashList = incoreList(list, COMMA);
	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public void setConverter(Inf_StrConverter converter) {
		this.converter = converter;
	}

	// ------------------------------------------------------------------------
	// キー一覧をリストで返す
	// ------------------------------------------------------------------------
	public List<String> getKeyList() {
		return hashList.getKeyList();
	}

	// ------------------------------------------------------------------------
	// 値一覧を配列で返す
	// ------------------------------------------------------------------------
	public String[] getValArray() {
		return hashList.getValStrArray();
	}

	// ------------------------------------------------------------------------
	// 値取得
	// ------------------------------------------------------------------------
	public String getValue(String key) {
		if (hashList == null)
			return "null";
		String ans = (String) hashList.get(key);
		if (ans != null) {
			return ans;
		} else {
			if (converter != null) {
				String val = converter.convert(key);
				keyNotFound.add(val);
				return val;
			} else {
				return null;
			}
		}
	}

	// ------------------------------------------------------------------------
	// 出力先セル位置取得   どのセルに出力するか
	//　辞書編集した場合、出力先も変更しなければならない
	// ------------------------------------------------------------------------
	public int getDestPos(String key) {
		if (hashList == null)
			return -1;
		return hashList.getIndex(key);
	}

	// ------------------------------------------------------------------------
	// getSize
	// ------------------------------------------------------------------------
	public int getSize() {
		if (hashList == null)
			return -1;
		return hashList.size();
	}

	public void remove(String key) {
		hashList.remove(key);
	}

	// ------------------------------------------------------------------------
	//　項目限定処理（必要な項目のみに限定する）
	//TODO	順序制御の変更も行いたいが・・・どうだろう
	//　現状では指定されていない項目を除去する動きになっているが除去ではなく指定された項目だけをその順序で拾いあげる形に書き換えてみる・・・どうだろう
	// ------------------------------------------------------------------------
	public List<Integer> limit(String[] limitKeys) {
		//20150730 test
		//			resDict.remove("0");//不要な項目
		//			resDict.remove("2");
		//			resDict.remove("4");
		//			resDict.remove("0");
		//			resDict.limit(new String[] { "0", "1", "2", "3", "4" });//必要な項目のみに限定
		if (limitKeys == null)
			return null;
		return hashList.limitByArray(limitKeys);
	}

	public void put(String key, String val) {
		hashList.put(key, val);
	}

	private HashList incoreList(List<String> list, String delimiter) {
		HashList hashList = new HashList();
		// Collections.sort(list);
		int counter = -1;
		counter = 0;
		for (String element : list) {
			if (element != null && (!element.equals(""))) {
				counter++;
				String[] splited = element.split(delimiter);
				if (splited.length >= 2) {
					hashList.put(splited[0], splited[1]);
				} else if (splited.length == 1) {
					//１セルしか持たない場合、その位置をキーとして、ゼロ番目が値となる
					//位置→ZEROから始まる？？
					//System.out.println("#TAG# key:" + String.valueOf(counter)	+ " val:" + splited[0]);
					hashList.put(String.valueOf(counter), splited[0]);
				}
			}
		}
		return hashList;
	}

	@Override
	public String convert(String key) {
		int seq = getDestPos(key);
		return String.valueOf(seq + grade);// XXX gradeの所は計算式にした方が便利だと思う
	};

	protected void eumKeys() {
		List<String> keyList = hashList.getKeyList();
		for (String key : keyList) {
			System.out.println("eumKeys key:" + key);
		}
	}

	public void debug() {
		System.out.println("-------------------------------------------------");
		System.out.println("Dictionary dic = new Dictionary();");
		String[] keys = hashList.getKeyArray();
		String[] Vals = hashList.getValStrArray();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				System.out.println("dic.put(\"" + keys[i] + "\",\"" + Vals[i]
						+ "\");");
			}
		}
		System.out.println("-------------------------------------------------");
	}

	public static void main(String[] args) {
	}
}
