package kyPkg.pmodel;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import globals.ResControlWeb;
import kyPkg.filter.CommonClojure;
import kyPkg.filter.PoiClosure;

public class JsonObject {
	private boolean escape = false;

	private String json = "";

	private List<String> seqList = null;// �������L��������ׁE�E�璷����

	private HashMap<String, String> attrMap = null;

	private HashMap<String, JsonObject> nodeMap = null;

	public List<String> getSeqList() {
		return seqList;
	}

	private HashMap<String, String> getAttrMap() {
		return attrMap;
	}

	private HashMap<String, JsonObject> getNodeMap() {
		return nodeMap;
	}

	public JsonObject getNode(String attr) {
		return nodeMap.get(attr);
	}

	// XXX SaveAsJson �I�u�W�F�N�g��Json�Ƃ��ĕۑ�����B
	// XXX convert2XML json��XML�ɕϊ�����
	// XXX xml2Json XML��Json�ɕϊ�����
	// XXX getJsonArray �w�肵���L�[������json�̔z��Ƃ��Ď��o��
	// XXX getJsonList �w�肵���L�[������json��List�Ƃ��Ď��o��
	// -------------------------------------------------------------------------
	// Inner Class
	// -------------------------------------------------------------------------
	class CharQueue {
		private int cursor = 0;

		private char[] array = null;

		// �R���X�g���N�^
		CharQueue(char[] array) {
			this.array = array;
		}

		public char poll() {// �L���[�̐擪���擾����э폜���܂��B
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
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public JsonObject() {
		seqList = new ArrayList();
		nodeMap = new HashMap();
		attrMap = new HashMap<String, String>();
	}

	public JsonObject(List list) {
		this();
		StringBuffer buff = new StringBuffer();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			buff.append(element);
		}
		String json = buff.toString();
		incore(json);
	}

	public JsonObject(String json) {
		this();
		incore(json);
	}

	private void incore(String json) {
		json = json.trim();
		if (!json.startsWith("{")) {
			json = "{" + json + "}";
		}
		// System.out.println("#debug<@JsonObject> json=>" + json);
		this.json = json;
		// System.out.println("��JSON��" + this.json);
		if (json.length() > 1024 * 1024) {// 8192
			System.out.println("#Error@JsonObject => Json Size Error :"
					+ json.length());
			return;
		}
		char[] array = json.toCharArray();
		CharQueue queue = new CharQueue(array);
		explore(queue, (char) 0, "");
	}

	// -------------------------------------------------------------------------
	// �T��
	// -------------------------------------------------------------------------
	private JsonObject explore(CharQueue queue, char type, String parent) {
		JsonObject stack = null;
		StringBuffer buf = new StringBuffer();
		String wName = "";
		String wParent = "";
		char wChar = 0;
		while ((wChar = queue.poll()) != 0) {
			if (wChar == '"') {
				if (escape == false) {
					escape = true;
				} else {
					escape = false;
				}
			}
			if (escape == true) {
				buf.append(wChar);
			} else {
				switch (wChar) {
				case '{': // ���[�^
					if (wName.equals("")) {
						wName = Integer.toString(nodeMap.size());
					}
					if (parent.equals("")) {
						wParent = wName;
					} else {
						wParent = parent + "." + wName;
					}
					stack = new JsonObject();
					put(wName, stack.explore(queue, '}', wParent));
					break;
				case '[': // index�^
					if (wName.equals("")) {
						wName = Integer.toString(nodeMap.size());
					}
					if (parent.equals("")) {
						wParent = wName;
					} else {
						wParent = parent + "." + wName;
					}
					stack = new JsonObject();
					put(wName, stack.explore(queue, ']', wParent));
					break;
				case ':':
					wName = buf.toString().trim();
					buf.delete(0, buf.length());
					break;
				case ',':
				case ']':
				case '}':
					String val = buf.toString().trim();
					if (!val.equals("")) {
						if (wName.equals("")) {
							wName = Integer.toString(attrMap.size());
						}
						put(wName, val);
					}
					buf.delete(0, buf.length());
					wName = "";
					if (type == wChar) {
						return this;
					}
					break;
				case 0:
					return this;
				default:
					buf.append(wChar);
					break;
				}
			}

		}
		return this;
	}

	// -------------------------------------------------------------------------
	// key�ȉ��̔z��v�f����attr�ɊY�����鍀�ڂ𕶎���Ƃ��Ď��o��
	// �ikey�̑��v�f��attr�̂��̂����o���j
	// -------------------------------------------------------------------------
	private String[] getElementsX(String key, String attr) {
		// System.out.println("���� getElementsX ���� key:" + key);
		String[] rtnArray = null;
		JsonObject chile = getNodeObj(key);
		// JsonObject chile = nodeMap.get(key);
		if (chile != null) {
			List list = new ArrayList<String>();
			List seqlist = chile.getSeqList();
			for (Iterator iter = seqlist.iterator(); iter.hasNext();) {
				String xKey = (String) iter.next();
				JsonObject mago = chile.getNode(xKey);
				if (mago != null) {
					// enumList(" magoNodeKey:", mago.getNodeKeyList());
					// enumList(" magoAttrKey:", mago.getAttrKeyList());
					list.add(mago.get(attr));
				}

			}
			rtnArray = (String[]) list.toArray(new String[list.size()]);
		} else {
			System.out.println("#JsonObject@getElements key Not found:" + key);
		}
		return rtnArray;
	}

	private JsonObject getNodeObj(String key) {
		JsonObject obj = null;
		int idx = key.indexOf(".");
		if (idx > 0) {
			String xkey = key.substring(0, idx);
			String xattr = key.substring(idx + 1);
			JsonObject chile = getNodeObj(xkey);
			obj = chile.getNodeObj(xattr);
		} else {
			obj = nodeMap.get(key);
		}
		return obj;
	}

	// -------------------------------------------------------------------------
	// key�ȉ��̗v�f�𕶎���z��Ƃ��Ď��o��
	// -------------------------------------------------------------------------
	public String[] getArray(String key) {
		String[] rtnArray = null;
		List list = getList(key);
		if (list != null) {
			rtnArray = (String[]) list.toArray(new String[list.size()]);
		}
		return rtnArray;
	}

	// -------------------------------------------------------------------------
	// key�ȉ��̗v�f��List�Ƃ��Ď��o��
	// -------------------------------------------------------------------------
	public List<String> getList(String key) {
		// System.out.println("�� getList �� key:" + key);
		JsonObject jsObj = getNodeObj(key);
		if (jsObj != null) {
			List<String> list = new ArrayList<String>();
			List<String> seqlist = jsObj.getSeqList();
			for (String wKey : seqlist) {
				String wVal = jsObj.get(wKey);
				// System.out.println("wKey>"+wKey);
				// System.out.println("wVal>"+wVal);
				if (wVal != null) {
					// 2009/09/16
					if (wVal.startsWith("\"")) {
						wVal = wVal.substring(1, wVal.length() - 1);
					}
					list.add(wVal);
				} else {
					list.add("");
				}
			}
//			for (Iterator iter = seqlist.iterator(); iter.hasNext();) {
//				String wKey = (String) iter.next();
//				String wVal = jsObj.get(wKey);
//				// System.out.println("wKey>"+wKey);
//				// System.out.println("wVal>"+wVal);
//				if (wVal != null) {
//					// 2009/09/16
//					if (wVal.startsWith("\"")) {
//						wVal = wVal.substring(1, wVal.length() - 1);
//					}
//					list.add(wVal);
//				} else {
//					list.add("");
//				}
//			}
			return list;
		} else {
			System.out.println("#JsonObject@getList key Not found:" + key);
			return null;
		}
	}

	public String[] getArray(String key, String attr) {
		String[] rtnArray = null;
		List list = getList(key, attr);
		if (list != null) {
			rtnArray = (String[]) list.toArray(new String[list.size()]);
		}
		return rtnArray;
	}

	public List getList(String key, String attr) {
		// System.out.println("�� getList �� key:" + key);
		JsonObject jsObj = getNodeObj(key);
		if (jsObj != null) {
			// enumList("x attrKey:", jsObj.getAttrKeyList());
			// enumList("x nodeKey:", jsObj.getNodeKeyList());
			List list = new ArrayList<String>();
			List seqlist = jsObj.getSeqList();
			for (Iterator iter = seqlist.iterator(); iter.hasNext();) {
				String wKey = (String) iter.next();
				JsonObject chile = jsObj.getNode(wKey);
				if (chile != null) {
					// enumList(" y attrKey:", chile.getAttrKeyList());
					// enumList(" y nodeKey:", chile.getNodeKeyList());
					String wVal = chile.get(attr);
					if (wVal != null) {
						list.add(wVal);
					} else {
						list.add("");
					}
				} else {
					list.add("");
				}
			}
			return list;
		} else {
			System.out.println("#JsonObject@getList key Not found:" + key);
			return null;
		}
	}

	// -------------------------------------------------------------------------
	// key�ȉ��̗v�f����attr�ɊY������v�f�𕶎���Ƃ��Ď��o��
	// -------------------------------------------------------------------------
	public String get(String attr) {
		// System.out.println("��get��"+attr);
		String xkey = attr;
		String xattr = "";
		int idx = attr.indexOf(".");
		if (idx > 0) {
			// Next Level
			xkey = attr.substring(0, idx);
			xattr = attr.substring(idx + 1);
			JsonObject chile = nodeMap.get(xkey);
			if (chile != null) {
				return chile.get(xattr);
			} else {
				System.out.println("chile==null");
			}
		} else {
			String objx = attrMap.get(attr);
			if (objx != null) {
				// System.out.println("��get��:" + attr + "��RTN:" +
				// objx.toString());
				return objx.toString();
			} else {
				if (nodeMap.get(attr) != null) {
					return join(getArray(attr), ",");
				}
			}
		}
		return "";
	}

	public String put(String attr, Object val) {
		if (val instanceof String) {
			// System.out.println("PUTasSTR>attr:" + attr + " =>" + (String)
			// val);
			if (attrMap.get(attr) == null) {
				seqList.add(attr);
			}
			attrMap.put(attr, (String) val);
		} else if (val instanceof JsonObject) {
			// System.out.println("PUTasOBJ>attr:" + attr);
			if (attrMap.get(attr) == null) {
				seqList.add(attr);
			}
			nodeMap.put(attr, (JsonObject) val);
		}
		return "";
	}

	// -------------------------------------------------------------------------
	// saveAsText("c:\\kdebug.txt");//debug
	// -------------------------------------------------------------------------
	public void saveAsText(String parmPath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(parmPath));
			bw.write(json);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	// -------------------------------------------------------------------------
	// Node�}�b�v��key�������X�g�ŕԂ�
	// -------------------------------------------------------------------------
	private List getNodeKeyList() {
		ArrayList list = new ArrayList();
		java.util.Set set = this.getNodeMap().entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			list.add((String) ent.getKey());
		}
		Collections.sort(list);
		return list;
	}

	// -------------------------------------------------------------------------
	// Attr�}�b�v��key�������X�g�ŕԂ�
	// -------------------------------------------------------------------------
	private List getAttrKeyList() {
		ArrayList list = new ArrayList();
		java.util.Set set = this.getAttrMap().entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			list.add((String) ent.getKey());
		}
		Collections.sort(list);
		return list;
	}

	// -------------------------------------------------------------------------
	// ���X�g�̈ꗗ�\���i�f�o�b�O�p�j
	// -------------------------------------------------------------------------
	private static void enumList(String comment, List list) {
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String chileKey = (String) iter.next();
			System.out.println(comment + chileKey);
		}
	}

	public static void tester_org(String json) {
		JsonObject jsonObj = new JsonObject(json);
		System.out.println("---------------------------------------------");
		System.out.println("@Tester json=>" + json);
		System.out
				.println("-step0--------------------------------------------");
		enumList("  attrKey:", jsonObj.getAttrKeyList());
		enumList("  nodeKey:", jsonObj.getNodeKeyList());
		// for (Iterator iter = jsonObj.getNodeKeyList().iterator();
		// iter.hasNext();) {
		// String chileKey = (String) iter.next();
		// System.out.println("nodeKey:" + chileKey);
		// }

		String[] wArray = jsonObj.getArray("DIM1");
		if (wArray != null) {
			for (int i = 0; i < wArray.length; i++) {
				System.out.println("DIM1:[" + i + "]:" + wArray[i]);
			}
		}
		// System.out.println("field:"+jsonObj.get("field"));
		// System.out.println("name :"+jsonObj.get("name"));
		// System.out.println("occ :"+jsonObj.get("occ"));
		// System.out.println("occ.0:"+jsonObj.get("occ.0"));
		// System.out.println("occ.1:"+jsonObj.get("occ.1"));
		// System.out.println("occ.2:"+jsonObj.get("occ.2"));
		// System.out.println("occ.3:"+jsonObj.get("occ.3"));

		System.out.println("cells:" + jsonObj.get("cells"));
		System.out.println("cells.0:" + jsonObj.get("cells.0"));
		System.out.println("cells.0.name:" + jsonObj.get("cells.0.name"));
		// cells: [{name

		System.out.println("-step1--------------------------------");
		java.util.Set set = jsonObj.getNodeMap().entrySet(); //
		// ����iterator���ĂׂȂ��̂ň�USET���擾����
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			System.out.println("key:" + key);
			// String val = (ent.getValue()).toString();
			JsonObject objx = (JsonObject) ent.getValue();

			List nodeList = objx.getNodeKeyList();
			enumList("  nodeKey:", nodeList);

			List attrList = objx.getAttrKeyList();
			enumList("  attrKey:", attrList);

			// getAttrKeyList
			// System.out.println("key:" + key + " val:" + objx.get(key));
		}
		System.out.println("-step2-------------- getElements");
		// System.out.println("# size of cells =>" + jsonObj.size("cells"));
		String[] array1 = jsonObj.getArray("cells");
		if (array1 != null) {
			for (int i = 0; i < array1.length; i++) {
				System.out.println("#=>" + array1[i]);
			}
		}
		System.out.println("-step3-------------- getElementsX");
		array1 = jsonObj.getElementsX("cells", "width");
		if (array1 != null) {
			for (int i = 0; i < array1.length; i++) {
				System.out.println("#=>" + array1[i]);
			}
		}
	}

	public static void main(String[] argv) {
		tester02();
	}

	public static void tester01() {
		String json = "";
		json = "{cells: [{name: '���B', field: '���' },{name: '���',field: '����',width: '30%' },{name: '����',field: '����'  ,width: '50%' }]}";
		// json =
		// "{F:REP,USERID:ejqp7,REPORT:Cross,ATM1:atom/�������N��ʂO�W,ATM2:atom/���F�O�W�N�O�P�ʏ`�P�O�O��,MSK1:atom/�������N��ʂO�W,MSK2:atom/�������N��ʂO�W,MTYP:1,NUM1:0,NUM2:0,BASE:2,PMASK:2,WMASK:2,CB1:1,CB2:2,CB3:2,CB4:2,ATM_BYMD:2009/5/22,ATM_AYMD:2009/6/21,DIM1:[1,3],DIM2:[4]}";
		// json = "{name: '�l�[���X�y�[�X', field: 'namespace',occ:['spam','egg',3,4,5]
		// }";
		// tester01(json);
		// tester01("[]");
		// tester01("{ken:001}");
		// tester01("[1,2,3,4,5,6]");
		// tester01("['a','b','c','d']");
		tester_org(json);
	}

	public static void tester03() {
		String json = "";
		json = "F:REP,USERID:ejqp7,REPORT:SnapShot,SAVEAS:�����q����,JSON:({meta:{cells:[[{name:'className', styles:'text-align: left;', width:30}, {name:'Heavy', styles:'text-align: right;', width:5}, {name:'Middle', styles:'text-align: right;', width:5}, {name:'Light', styles:'text-align: right;', width:5}, {name:'NonUser', styles:'text-align: right;', width:5}]]}, data:[['\uFF11\uFF10\u4EE3', 3, 0, 0, 371], ['\uFF12\uFF10\u4EE3', 46, 2, 1, 1003], ['\uFF13\uFF10\u4EE3', 121, 4, 2, 1270], ['\uFF14\uFF10\u4EE3', 123, 4, 7, 993], ['\uFF15\uFF10\u4EE3', 100, 4, 1, 1152], ['\uFF16\uFF10\u4EE3', 61, 3, 2, 754]]})";
		JsonObject jsonObj = new JsonObject(json);
		System.out.println("---------------------------------------------");
		System.out.println("@Tester json=>" + json);
		System.out
				.println("-step0--------------------------------------------");
		System.out.println("JSON.meta.cells:" + jsonObj.get("JSON.meta.cells"));

		String[] array = jsonObj.getArray("JSON.data");
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				System.out.println("data:[" + i + "]:" + array[i]);
			}
		}
		String[] header = jsonObj.getArray("JSON.meta.cells.0", "name");
		if (header != null) {
			for (int i = 0; i < header.length; i++) {
				System.out.println("name:[" + i + "]:" + header[i]);
			}
		}

	}

	public static void tester02() {
		String json = "";
		// json =
		// "F:REP,USERID:ejqp7,REPORT:SnapShot,SAVEAS:�����q����,JSON:({meta:{cells:[[{name:'className',
		// styles:'text-align: left;', width:30}, {name:'Heavy',
		// styles:'text-align: right;', width:5}, {name:'Middle',
		// styles:'text-align: right;', width:5}, {name:'Light',
		// styles:'text-align: right;', width:5}, {name:'NonUser',
		// styles:'text-align: right;', width:5}]]},
		// data:[['\uFF11\uFF10\u4EE3', 3, 0, 0, 371], ['\uFF12\uFF10\u4EE3',
		// 46, 2, 1, 1003], ['\uFF13\uFF10\u4EE3', 121, 4, 2, 1270],
		// ['\uFF14\uFF10\u4EE3', 123, 4, 7, 993], ['\uFF15\uFF10\u4EE3', 100,
		// 4, 1, 1152], ['\uFF16\uFF10\u4EE3', 61, 3, 2, 754]]})";
		json = "{F:REP,USERID:ejqp7,REPORT:SnapShot,SAVEAS:zapp,COMMENT:kinngakukei ,JSON:{meta:{cells:[[{name:'className', styles:'text-align: left;', width:30}, {name:'Heavy', styles:'text-align: right;', wdth:5}, {name:'Middle', styles:'text-align: right;', width:5}, {name:'Light', styles:'text-align: right;', width:5}, {name:'NonUser', styles:'text-align: right;', width:5}]]}, data:[['�j�P�T����P�X��' 3, 0, 0, 171], ['�j�Q�O����Q�S��', 3, 2, 0, 183], ['�j�Q�T����Q�X��', 20, 1, 0, 246], ['�j�R�O����R�S��', 28, 1, 2, 241], ['�j�R�T����R�X��', 27, 3, 0, 245], ['�j�S�O����S�S��', 26, 2, 2, 193], ['�j�S�T����S�X��', 23, 1, 2, 157], ['�j�T�O����T�S��', 23, 2, 0, 204], ['�j�T�T����T�X��', 21, 1, 0, 202], ['�j�U�O����U�S��', 17, 0, 0, 152], ['�j�U�T����U�X��', 19, 2, 0, 162], ['���P�T����P�X��', 0, 0, 0, 163],['���Q�O����Q�S��', 4, 0, 1, 168], ['���Q�T����Q�X��', 16, 0, 1, 331], ['���R�O����R�S��', 27, 0, 0, 378], ['���R�T����R�X��', 36, 2, 0, 372], ['���S�O����S�S��', 41, 0, 0, 361], ['���S�T����S�X��', 34, 0,2, 255], ['���T�O����T�S��', 28, 1, 1, 371], ['���T�T����T�X��', 27, 0, 0, 350], ['���U�O����U�S��', 17, 1, 0, 240], ['���U�T����U�X��', 7, 0, 0, 123]]}}";

		JsonObject jsonObj = new JsonObject(json);
		System.out.println("---------------------------------------------");
		System.out.println("@Tester json=>" + json);
		System.out
				.println("-step0--------------------------------------------");
		System.out.println("JSON:" + jsonObj.get("JSON"));
		System.out.println("JSON.meta.cells:" + jsonObj.get("JSON.meta.cells"));

		String[] array = jsonObj.getArray("JSON.data");
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				System.out.println("data:[" + i + "]:" + array[i]);
			}
		}
		String[] header = jsonObj.getArray("JSON.meta.cells.0", "name");
		if (header != null) {
			for (int i = 0; i < header.length; i++) {
				System.out.println("name:[" + i + "]:" + header[i]);
			}
		}
		PoiClosure closure = new PoiClosure();
		closure.setTemplate(ResControlWeb.getD_Resources_Templates("QPRSSHOT.xls"));
		closure.setOutPath(ResControlWeb.getD_Resources_PUBLIC("Public/snapShot.xls"));
		closure.setSheet("data");
		closure.setBaseYX(10, 2);
		closure.setDelimiter(",");
		closure.setHeader(header);
		new CommonClojure().incore(closure, array);
	}

}
