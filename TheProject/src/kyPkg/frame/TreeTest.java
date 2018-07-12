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

	// ���t�B�[���h���A�e�m�[�h�A���
	List<String> testList() {
		List<String> list = new ArrayList();
		list.add("1,�H�i,root,nood");
		list.add("2,���p�i,root,nood");
		list.add("3,�����p�i,root,nood");
		list.add("11,���H�H�i,1,nood");
		list.add("22,���p�G��,2,nood");
		list.add("33,����E�����p�i,3,nood");
		list.add("111,������,11,leaf");
		list.add("112,�H�p��,11,leaf");
		list.add("113,�X�v���b�h��,11,leaf");
		list.add("221,�����q���i,22,leaf");
		list.add("222,�Ό���,22,leaf");
		list.add("331,��񐮗��p�i,33,leaf");
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
