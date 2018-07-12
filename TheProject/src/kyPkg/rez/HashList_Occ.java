package kyPkg.rez;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//-------------------------------------------------------------------------
// HashList
// 順序性のあるハッシュマップ・・・・のつもり
//-------------------------------------------------------------------------
public class HashList_Occ implements Iterator, Serializable {
	private static final long serialVersionUID = 1L;
	private String delimiter = ",";
	private int occ = -1;

	private List<String> keyList;
	private HashMap<String, Integer> hashMap;

	private ArrayList[] arrayLists;

	//	private HashMap getHashMap() {
	//		return hashMap;
	//	}

	private int cursor;

	public Iterator iterator() {
		this.cursor = 0;
		return this;
	}

	// ---------------------------------------------------------------------
	// コンストラクタ
	// ---------------------------------------------------------------------
	public HashList_Occ() {
		this(1);
	}

	// code:name
	public HashList_Occ(String[] array, String delim) {
		this(1);
		for (int i = 0; i < array.length; i++) {
			String[] splited = array[i].split(delim);
			if (splited.length == 1) {
				put(splited[0], splited[0]);
			} else if (splited.length >= 2) {
				put(splited[0], splited[1]);
			}
		}
	}

	public HashList_Occ(int occ) {
		this.occ = occ;
		arrayLists = new ArrayList[this.occ];
		keyList = new ArrayList();
		for (int i = 0; i < arrayLists.length; i++) {
			arrayLists[i] = new ArrayList();
		}
		hashMap = new HashMap();
	}

	public boolean containsKey(Object key) {
		return hashMap.containsKey(key);
	}

	public Set keySet() {
		return hashMap.keySet();
	}

	public int size() {
		return keyList.size();
	}

	//	private Set entrySet() {
	//		return hashMap.entrySet();
	//	}

	// ---------------------------------------------------------------------
	// 値を追加
	// ---------------------------------------------------------------------
	public void put(String key, Object val) {
		put(key, new Object[] { val });
	}

	public int getSize() {
		return keyList.size();
	}

	public void put(String key, Object[] val) {
		if (hashMap.get(key) == null) {
			keyList.add(key);
			int index = keyList.size() - 1;
			for (int i = 0; i < val.length; i++) {
				arrayLists[i].add(val[i]);
			}
			// if (index < 20
			// )System.out.println("HashList key"+key+" index:"+index);
			hashMap.put(key, new Integer(index));
		}
	}

	//20150730
	public void remove(String key) {
		if (keyList.contains(key))
			keyList.remove(key);
		if (hashMap.containsKey(key))
			hashMap.remove(key);
	}

	// ---------------------------------------------------------------------
	// 対応するインデックス取得
	// ---------------------------------------------------------------------
	public int getIndex(String key) {
		Integer x = hashMap.get(key);
		if (x == null)
			return -1;
		return x.intValue();
	}

	// ---------------------------------------------------------------------
	// 対応する値取得
	// ---------------------------------------------------------------------
	public Object get(String key) {
		return get(0, key);
	}

	public Object get(int which, String key) {
		if (arrayLists.length <= which)
			return null;
		int i = getIndex(key);
		if (i < 0)
			return null;
		// System.out.println(valueArray[which].toString());
		return arrayLists[which].get(i);
	}

	// ---------------------------------------------------------------------
	// 対応する値取得
	// ---------------------------------------------------------------------
	public String get(int index) {
		return get(0, index);
	}

	public String get(int which, int index) {
		if (arrayLists.length <= which)
			return null;
		return (String) arrayLists[which].get(index);
	}

	// ---------------------------------------------------------------------
	// 対応する値取得
	// ---------------------------------------------------------------------
	public String getKey(int index) {
		return keyList.get(index);
	}

	public List<String> getKeyList() {
		return keyList;
	}

	public ArrayList getValueList(int which) {
		if (arrayLists.length <= which)
			return null;
		return arrayLists[which];
	}

	public String[] getKeyArray() {
		return (String[]) keyList.toArray(new String[keyList.size()]);
	}

	public String[] getArray() {
		return getArray(this.delimiter);
	}

	public String[] getArray(String delimiter) {
		StringBuffer buff = new StringBuffer();
		String[] rtnArray = new String[keyList.size()];
		for (int i = 0; i < keyList.size(); i++) {
			buff.delete(0, buff.length());
			buff.append(keyList.get(i));
			for (int j = 0; j < arrayLists.length; j++) {
				buff.append(delimiter);
				buff.append(arrayLists[j].get(i));
			}
			rtnArray[i] = buff.toString();
		}
		return rtnArray;
	}

	public String[] getValStrArray() {
		return getValStrArray(0);
	}

	public String[] getValStrArray(int which) {
		if (arrayLists.length <= which)
			return null;
		return (String[]) arrayLists[which]
				.toArray(new String[arrayLists[which].size()]);
	}

	// イテレータメソッド関連
	@Override
	public boolean hasNext() {
		if (cursor < keyList.size()) {
			return true;
		} else {
			return false;
		}
	}

	// イテレータメソッド関連
	@Override
	public Object next() {
		StringBuffer buff = new StringBuffer();
		if (cursor < keyList.size()) {
			buff.delete(0, buff.length());
			buff.append(keyList.get(cursor));
			for (int j = 0; j < arrayLists.length; j++) {
				buff.append(delimiter);
				buff.append(arrayLists[j].get(cursor));
			}
			cursor++;
			return buff.toString();
		} else
			return null;

	}

	// イテレータメソッド関連
	@Override
	public void remove() {
		// 特に実装しない
	}
}
// public void debug() {
// Set collectionView = hashMap.entrySet();
// Iterator iter = collectionView.iterator();
// while (iter.hasNext()) {
// Object obj = iter.next();
// java.util.Map.Entry ent = (java.util.Map.Entry) obj;
// String key = (String) ent.getKey();
// String val = (String) ent.getValue();
// System.out.println("key:"+key+" val:"+val);
// }
// }
