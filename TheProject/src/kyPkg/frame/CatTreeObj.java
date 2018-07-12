package kyPkg.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import kyPkg.mySwing.ListPanel;
import kyPkg.uFile.ListArrayUtil;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class CatTreeObj implements TreeSelectionListener {
	private static final String TAB = "\t";
	private String reafRegex = "leaf"; // regex
	private static String BEF = "【";
	private static String AFT = "】 ";
	private static final String ROOT = "root";
	private JTextField targetFld;
	private ListPanel listPanel;
	private HashMap<String, Object> ctrlHash = null;
	private String node = "";//直近で選択したノードの値
	private HashMap<String, String> chileMap;

	public static String getCodeNameElement(String code, String name) {
		return new String(BEF + code + AFT + name);
	}

	public void setReafRegex(String regex) {
		this.reafRegex = regex;
	}

	// 2010/09/29 yuasa
	public void setListPanel(ListPanel listPanel) {
		this.listPanel = listPanel;
	}

	public void setTargetFld(JTextField targetFld) {
		this.targetFld = targetFld;
	}

	public List<String> getChiles() {
		String[] array = node.split(AFT);
		if (array.length == 1)
			return null;
		String nodeKey = array[0].substring(1).trim();
		List<String> fitKeys = getFitkeys(chileMap, nodeKey);
		return chileElements(chileMap, fitKeys);
	}

	public List<String> chileElements(HashMap<String, String> chileMap,
			List<String> fitKeys) {
		if (fitKeys == null)
			return null;
		List<String> elements = new ArrayList();
		for (String key : fitKeys) {
			String val = chileMap.get(key);
			if (val != null) {
				elements.add(val);
			}
		}
		return elements;
	}

	// リストの3番目の要素を正規表現によって変換するフィルタがあるとよい・・・かな

	// ファイルを入力にするコンストラクタもあると良い・・・ファイル⇒リスト
	public CatTreeObj(String path) {
		List<String> list = ListArrayUtil.file2List(path);
		reafRegex = "1";
		incore(list, TAB);
		chileMap = getChileMap(list);
	}

	public List<String> getFitkeys(HashMap<String, String> chileMap,
			String prefix) {
		if (prefix.length() < 4)
			return null;
		prefix = prefix + ".+";
		//		System.out.println("prefix=>" + prefix + "<=");
		Pattern pattern = Pattern.compile(prefix);
		List<String> fitKeys = new ArrayList();
		Set<String> keySet = chileMap.keySet();
		for (String key : keySet) {
			if (pattern.matcher(key).matches()) {
				fitKeys.add(key);
			}
		}
		return fitKeys;
	}

	private HashMap<String, String> getChileMap(List<String> list) {
		HashMap<String, String> map = new HashMap();
		for (String element : list) {
			String[] array = element.split(TAB);
			if (list.size() >= 4) {
				String code = array[0];
				String name = array[1];
				String type = array[3];
				String val = getCodeNameElement(code, name);
				if (type.equals("1")) {
					//					System.out.println("code:" + code + " val:" + val);
					map.put(code, val);//leafのみ格納する
				}
			}
		}
		return map;
	}

	public CatTreeObj(List<String> list, String delimiter) {
		incore(list, delimiter);
	}

	private void incore(List<String> list, String delimiter) {
		for (String element : list) {
			String[] array = element.split(delimiter);
			// array[2]はleafまたはnodeが入っている前提・・・実際はレベルから変換する予定
			if (list.size() == 3) {
				mod(array[0], "", array[1], array[2]);
			} else if (list.size() >= 4) {
				mod(array[0], array[1], array[2], array[3]);
			}
		}
	}

	public Vector getVector() {
		return (Vector) ctrlHash.get(ROOT);
	}

	public void mod(String code, String name, String parent, String type) {
		code = code.trim();
		parent = parent.trim();
		if (parent == null || parent.equals("NULL") || parent.equals("ROOT")
				|| parent.equals(""))
			parent = ROOT;
		if (ctrlHash == null) {
			ctrlHash = new HashMap();
			ctrlHash.put(ROOT, new MyVector(ROOT));
		}
		Object obj = ctrlHash.get(parent);
		if (obj != null && obj instanceof Vector) {
			// chileがすでに辞書に登録されていたら上書きされる
			String element = getCodeNameElement(code, name);
			if (type.matches(reafRegex)) {
				ctrlHash.put(code, element);// Leaf
			} else {
				//				ctrlHash.put(code, new MyVector(BEF + code + AFT + name));// Node
				ctrlHash.put(code, new MyVector(element));// Node
			}
			((Vector) obj).add(ctrlHash.get(code));
		} else {
			// 親ノードが存在しない場合は無視する
		}
	}

	class MyVector extends Vector {
		private static final long serialVersionUID = 1L;
		private String myName;

		public MyVector(String myName) {
			super();
			this.myName = myName;
		}

		@Override
		public String toString() {
			return myName;
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree tree = (JTree) e.getSource();
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (treeNode == null)
			return;
		Object nodeInfo = treeNode.getUserObject();
		node = nodeInfo.toString();
		if (treeNode.isLeaf()) {
			if (targetFld != null) {
				targetFld.setText(node);
			} else if (listPanel != null) {
				listPanel.addElement(node);
			} else {
				JOptionPane.showMessageDialog(null,
						"targetFld is Null!! Leaf:" + node);
			}
		} else {
//			System.out.println("valueChanged: not Leaf:" + node);
		}
	}
}
