package kyPkg.rez;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//-------------------------------------------------------------------------
// HashList 単純化版
//-------------------------------------------------------------------------
public class HashList {
	private List<String> keyList;
	private HashMap<String, Object> objMap;
	private HashMap<String, Integer> pozMap;//positionMap

	// ---------------------------------------------------------------------
	// コンストラクタ
	// ---------------------------------------------------------------------
	public HashList() {
		keyList = new ArrayList();
		objMap = new HashMap();
	}

	public int size() {
		return keyList.size();
	}

	// ---------------------------------------------------------------------
	// 値を追加
	// ---------------------------------------------------------------------
	public void put(String key, String obj) {
		//		System.out.println(">>>#20150730#   key:" + key + " obj:" + obj);
		if (keyList.contains(key) == false) {
			keyList.add(key);
			objMap.put(key, obj);
		}
	}

	// ---------------------------------------------------------------------
	//	必要な項目のみに限定する
	// ---------------------------------------------------------------------
	public List<Integer> limitByArray(String[] limitkeys) {
		return limitByList(java.util.Arrays.asList(limitkeys));
	}

	public List<Integer> limitByList_org(List<String> limitkeys) {
		List<String> removeKeys = new ArrayList();
		List<Integer> removePos = new ArrayList();
		for (int pos = 0; pos < keyList.size(); pos++) {
			String key = keyList.get(pos);
			if (!limitkeys.contains(key)) {
				removeKeys.add(key);
				removePos.add(pos);
			}
		}
		//		for (String key : keyList) {
		//			if (!limitkeys.contains(key)) {
		//				removeKeys.add(key);
		//			}
		//		}
		for (String key : removeKeys) {
			remove(key);
		}
		return removePos;
	}

	//TODO	リミットに合わせた形の順序に並び替えなければならないので・・・removeではなく・・・どの位置のものをとりだしたかを返すように書き換える
	public List<Integer> limitByList(List<String> limitkeys) {
		HashMap<String,Integer> mapPos= new HashMap();
		for (int pos = 0; pos < keyList.size(); pos++) {
			mapPos.put(keyList.get(pos), pos);
		}
		List<String> removeKeys = new ArrayList();
		List<Integer> pkupPos = new ArrayList();
		for (String key : limitkeys) {
			Integer pos =mapPos.get(key);
			if(pos!=null){
				pkupPos.add(pos);
			}else{
				remove(key);
			}
		}
		keyList = limitkeys;//20151109試しにやってみる
		return pkupPos;
	}

	// ---------------------------------------------------------------------
	//　不要な項目を外す　 20150730
	// ---------------------------------------------------------------------
	public void remove(String key) {
		if (keyList.contains(key))
			keyList.remove(key);
		if (objMap.containsKey(key))
			objMap.remove(key);
		pozMap = recalcPos(keyList);
	}

	// ---------------------------------------------------------------------
	// 対応するインデックス取得
	// ---------------------------------------------------------------------
	public int getIndex(String key) {
		if (pozMap == null)
			pozMap = recalcPos(keyList);
		Integer x = pozMap.get(key);
		if (x == null)
			return -1;
		return x.intValue();
	}

	// ---------------------------------------------------------------------
	// 対応する値取得
	// ---------------------------------------------------------------------
	public Object get(String key) {
		return objMap.get(key);
	}

	// ---------------------------------------------------------------------
	// 対応する値取得
	// ---------------------------------------------------------------------
	public List<String> getKeyList() {
		return keyList;
	}

	public String[] getKeyArray() {
		return (String[]) keyList.toArray(new String[keyList.size()]);
	}

	private List genObjList(List<String> keyList) {
		List objList = new ArrayList();
		for (String key : keyList) {
			objList.add(objMap.get(key));
		}
		return objList;
	}

	public String[] getValStrArray() {
		List objList = genObjList(keyList);
		return (String[]) objList.toArray(new String[objList.size()]);
	}

	// ---------------------------------------------------------------------
	//出力先カラムを再設定する
	// ---------------------------------------------------------------------
	private HashMap<String, Integer> recalcPos(List<String> keyList) {
		HashMap<String, Integer> pozMap = new HashMap();
		int seq = 0;
		for (String key : keyList) {
			pozMap.put(key, seq++);
		}
		return pozMap;
	}

}
