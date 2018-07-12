package kyPkg.pmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JsonObject_Old {
	private HashMap<String, HashMap> hmap = null;
	// -------------------------------------------------------------------------
	// Inner Class
	// -------------------------------------------------------------------------
	class CharQueue {
		private int cursor = 0;
		private char[] array = null;
		// コンストラクタ
		CharQueue(char[] array) {
			this.array = array;
		}
		public char poll() {// キューの先頭を取得および削除します。
			if (array == null)
				return 0;
			cursor++;
			if (cursor < array.length)
				return array[cursor];
			else {
				cursor--;
				return 0;
			}
		}
	}

	// -------------------------------------------------------------------------
	// rootマップを返す
	// -------------------------------------------------------------------------
	public HashMap<String, HashMap> getMap() {
		return hmap;
	}

	// -------------------------------------------------------------------------
	// keyにあたるマップを返す
	// -------------------------------------------------------------------------
	public HashMap<String, Object> getMap(String key) {
		if (hmap != null) {
			return hmap.get(key);
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// key以下の要素数を返す
	// -------------------------------------------------------------------------
	public int size(String key) {
		if (hmap != null) {
			HashMap lhmap = hmap.get(key);
			if (lhmap != null) {
				return lhmap.size();
			}
		}
		return -1;
	}

	// -------------------------------------------------------------------------
	// key以下の配列要素中のattrに該当する項目を文字列として取り出す
	// （keyの孫要素がattrのものを取り出す）
	// -------------------------------------------------------------------------
	public String[] getElementsX(String key, String attr) {
		String[] array = null;
		if (hmap != null) {
			HashMap lhmap = hmap.get(key);
			if (lhmap != null) {
				int wMax = lhmap.size();
				ArrayList list = new ArrayList<String>();
				for (int i = 0; i < wMax; i++) {
					String wKey = key + "." + Integer.toString(i);
					lhmap = hmap.get(wKey);
					if (lhmap != null) {
						Object obj = lhmap.get(attr);
						if (obj != null) {
							list.add(obj.toString());
						}
					}
				}
				array = (String[]) list.toArray(new String[list.size()]);
			}
		}
		return array;
	}

	// -------------------------------------------------------------------------
	// key以下の要素を文字列配列として取り出す
	// -------------------------------------------------------------------------
	public String[] getElements(String key) {
		String[] array = null;
		if (hmap != null) {
			HashMap lhmap = hmap.get(key);
			if (lhmap != null) {
				ArrayList list = new ArrayList<String>();
				java.util.Set set = lhmap.entrySet();
				java.util.Iterator it = set.iterator();
				while (it.hasNext()) {
					java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
					list.add((ent.getValue()).toString());
				}
				array = (String[]) list.toArray(new String[list.size()]);
			}

		}
		return array;
	}

	// -------------------------------------------------------------------------
	// 
	// -------------------------------------------------------------------------
	public String put(String arg, String val) {
		// System.out.println("#debug @JsonObject put arg:"+arg);
		String key = "";
		String attr = arg;
		int idx = arg.lastIndexOf(".");
		if (idx > 0) {
			key = arg.substring(0, idx);
			attr = arg.substring(idx);
		}
		if (hmap != null) {
			HashMap lhmap = hmap.get(key);
			if (lhmap != null) {
				lhmap.put(attr, val);
			}
		}
		return "";
	}

	// key部をリストで返す
	public List getKeyList() {
		ArrayList list = new ArrayList();
		java.util.Set set = this.getMap().entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			list.add((String) ent.getKey());
		}
		Collections.sort(list);
		return list;
	}

	// -------------------------------------------------------------------------
	// key以下の要素中のattrに該当する要素を文字列として取り出す
	// -------------------------------------------------------------------------
	public String get(String arg) {
		// System.out.println("#debug @JsonObject get arg:"+arg);
		String key = "";
		String attr = arg;
		int idx = arg.lastIndexOf(".");
		if (idx > 0) {
			key = arg.substring(0, idx);
			attr = arg.substring(idx);
		}
		// System.out.println("#debug @JsonObject get key :"+key);
		// System.out.println("#debug @JsonObject get attr:"+attr);
		return get(key, attr);
	}
	// -------------------------------------------------------------------------
	// key以下の要素中のattrに該当する要素を文字列として取り出す
	// -------------------------------------------------------------------------
	public String get(String key, String attr) {
		if (hmap != null) {
			HashMap lhmap = hmap.get(key);
			if (lhmap != null) {
				Object obj = lhmap.get(attr);
				if (obj != null) {
					return obj.toString();
				}
			}
		}
		return "null";
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public JsonObject_Old(String json) {
		json = json.trim();
		if (json.startsWith("{") == false) {
			json = "{" + json + "}";
		}
		// System.out.println("#debug<@JsonObject> json=>" + json);
		System.out.println("#error@JsonObject => Json Size:" + json.length());
//		if (json.length() > 1048576) {//8192??
//			System.out.println("#error@JsonObject => Json Size Error :"
//					+ json.length());
//			return;
//		}
		hmap = new HashMap();
		char[] array = json.toCharArray();
		CharQueue queue = new CharQueue(array);
		explore(queue, (char) 0, "");
	}

	// -------------------------------------------------------------------------
	// 探索
	// -------------------------------------------------------------------------
	private HashMap<String, Object> explore(CharQueue queue, char type,
			String parent) {
		HashMap<String, Object> rmap = null;
		HashMap<String, Object> lhmap = new HashMap();
		hmap.put(parent, lhmap);
		// System.out.println(" parent:" + parent);

		StringBuffer buf = new StringBuffer();
		char wChar = 0;
		String wName = "";
		String wParent = "";
		while ((wChar = queue.poll()) != 0) {
			switch (wChar) {
			case '{': // 名票型
				if (wName.equals("")) {
					wName = Integer.toString(lhmap.size());
				}
				if (parent.equals("")) {
					wParent = wName;
				} else {
					wParent = parent + "." + wName;
				}
				rmap = explore(queue, '}', wParent);
				break;
			case '[': // index型
				if (wName.equals("")) {
					wName = Integer.toString(lhmap.size());
				}
				if (parent.equals("")) {
					wParent = wName;
				} else {
					wParent = parent + "." + wName;
				}
				rmap = explore(queue, ']', wParent);
				break;
			case ':':
				wName = buf.toString().trim();
				buf.delete(0, buf.length());
				break;
			case ',':
			case ']':
			case '}':
				Object value = null;
				if (wName.equals("")) {
					wName = Integer.toString(lhmap.size());
				}
				if (rmap == null) {
					value = buf.toString().trim();
				} else {
					value = rmap;
				}
				buf.delete(0, buf.length());
				lhmap.put(wName, value);
				// System.out.println(" name:" + name + " value:"
				// + value.toString());
				wName = "";
				if (type == wChar) {
					return lhmap;
				}
				break;
			case 0:
				return lhmap;
			default:
				buf.append(wChar);
				break;
			}
		}
		return lhmap;
	}

	public static void main(String[] argv) {
		String json = "";
		json = "{cells: [{name: 'ネームスペース', field: 'namespace' },{name: 'クラス',field: 'className',width: '30%' },{name: '要約',           field: 'summary'  ,width: '50%' }]}";
		// tester01("{}");
		// tester01("[]");
		// tester01("{ken:001}");
		// tester01("[1,2,3,4,5,6]");
		// tester01("['a','b','c','d']");
		tester01(json);
	}

	public static void tester01(String json) {
		JsonObject_Old jsonObj = new JsonObject_Old(json);
		System.out.println("---------------------------------------------");
		System.out.println("@Tester json=>" + json);
		List list = jsonObj.getKeyList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			jsonObj.get(key);
			// String val = (ent.getValue()).toString();
			// System.out.println("key:" + key + " val:" + val);
			System.out.println("key:" + key);
		}
		System.out.println("---------------------------------------------");
	}

	public static void tester_org(String json) {
		JsonObject_Old jsonObj = new JsonObject_Old(json);
		System.out.println("---------------------------------------------");
		System.out.println("@Tester json=>" + json);
		System.out.println("---------------------------------------------");
		java.util.Set set = jsonObj.getMap().entrySet(); // 直接iteratorを呼べないので一旦SETを取得する
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			String val = (ent.getValue()).toString();
			System.out.println("key:" + key + "  val:" + val);
		}
		System.out.println("---------------------------------------------");
		System.out.println("# size of cells =>" + jsonObj.size("cells"));
		String[] array1 = jsonObj.getElements("cells");
		for (int i = 0; i < array1.length; i++) {
			System.out.println("#=>" + array1[i]);
		}
		System.out.println("---------------------------------------------");
		array1 = jsonObj.getElementsX("cells", "width");
		for (int i = 0; i < array1.length; i++) {
			System.out.println("#=>" + array1[i]);
		}
	}
}
