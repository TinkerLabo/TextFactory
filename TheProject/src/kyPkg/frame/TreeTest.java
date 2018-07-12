package kyPkg.frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import kyPkg.mySwing.ListPanel;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;

	// 自フィールド名、親ノード、種別
	List<String> testList() {
		List<String> list = new ArrayList();
		list.add("1,食品,root,nood");
		list.add("2,日用品,root,nood");
		list.add("3,文化用品,root,nood");
		list.add("11,加工食品,1,nood");
		list.add("22,日用雑貨,2,nood");
		list.add("33,文具・事務用品,3,nood");
		list.add("111,調味料,11,leaf");
		list.add("112,食用油,11,leaf");
		list.add("113,スプレッド類,11,leaf");
		list.add("221,口中衛生品,22,leaf");
		list.add("222,石鹸類,22,leaf");
		list.add("331,情報整理用品,33,leaf");
		return list;
	}

	private JTree tree;

	// private JTextField targetField;

	public TreeTest() {
		super();
		init1();
	}

	private void init1() {
		this.setBounds(100, 100, 900, 380);
		String rootDir = globals.ResControl.getQprRootDir();
		String categoryFile = rootDir + "catTree.txt";
		if (new File(categoryFile).isFile()) {
			CatTreeObj treeObj = new CatTreeObj(categoryFile);
			tree = new JTree(treeObj.getVector());
			tree.addTreeSelectionListener(treeObj);

			ListPanel listPanel = new ListPanel(false, false);
			listPanel.setBounds(10, 50, 400, 100);
			treeObj.setListPanel(listPanel);

			// targetField = new JTextField();
			// targetField.setBounds(10, 10, 400, 20);
			// panel.add(targetField);
			// treeObj.setTargetFld(targetField);

			JPanel panel = new JPanel(null);
			panel.add(listPanel);

			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					new JScrollPane(tree), panel);

			splitPane.setOneTouchExpandable(false);
			splitPane.setDividerLocation(400);
			this.add(splitPane);

		}
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public static void main(String[] args) {
		new TreeTest();
	}

}
