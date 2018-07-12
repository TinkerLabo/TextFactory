package kyPkg.gridPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import kyPkg.mySwing.ListPanelW;
import kyPkg.panel.JP_Ancestor;

public class LimitBrowserPanel extends JP_Ancestor {
	protected static final String TEST = "テストテスト";
	protected static final String RESET = "すべての条件を解除する。";
	private Grid_Panel gridPanel;
	private JComboBox cmbColumn;
	private ListPanelW listPanel;
	private JButton btnTest;
	private JButton btnReset;
	private String[] colNames;
	private HashMap<String, Integer> name2ColMap = new HashMap();

	// ---------------------------------------------------------------------
	// コンストラクタ
	// ---------------------------------------------------------------------
	public LimitBrowserPanel(Grid_Panel pGrid) {
		//		super(new BorderLayout());
		this.setSize(400, 300);
		this.gridPanel = pGrid;
		setPreferredSize(new Dimension(700, 200));

		btnReset = new JButton(RESET);
		btnReset.setBounds(10, 30, 100, 20);

		Grid grid = gridPanel.getGrid();
		colNames = grid.getColNames();

		// ---------------------------------------------------------------------
		// XXX カラム名を使用したい
		// XXX 外部から新しい値を取り込みたい
		// ---------------------------------------------------------------------
		JPanel northPanel = new JPanel();
		btnTest = new JButton(TEST);
		btnTest.setPreferredSize(new Dimension(100, 20));
		listPanel = new ListPanelW(450, 30, 400, 100, true, 150);
		cmbColumn = new JComboBox();
		cmbColumn.setPreferredSize(new Dimension(120, 20));
		// ------------------------------------------------------------------------
		// カラム指定コンボボックスが変更された場合
		// ------------------------------------------------------------------------
		cmbColumn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String colName = (String) cmbColumn.getSelectedItem();
				Integer index = name2ColMap.get(colName);
				if (index != null) {
					List uqList = getColumnList(index);
					listPanel.setRightClear(true);
					listPanel.setListData(uqList);
				}
			}
		});

		setColName2Combo();
		northPanel.add(cmbColumn);
		add(northPanel, BorderLayout.NORTH);
		add(btnReset, BorderLayout.SOUTH);
		add(listPanel, BorderLayout.CENTER);

		// ------------------------------------------------------------------------
		// "すべての条件を解除する。" ボタンクリック時
		// ------------------------------------------------------------------------
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionExit();
			}
		});
	}

	private void actionExit() {
		// setColName2Combo();　　？？なんでここでやっているのかわからないのでコメントアウト201300301
		// Vector<Vector> orgMatrix = GridUtil.cnvTableModel2Vector(grid.getModel());
		gridPanel.extractIt((HashMap<Integer, Set<String>>) new HashMap());
		super.closeDialog();
	}

	private List getColumnList(Integer seq) {
		Set set = gridPanel.getLimiter().get(seq);
		if (set != null) {
			return new ArrayList(set);
		}
		return null;
	}

	private void setColName2Combo() {
		cmbColumn.removeAllItems();
		HashMap<Integer, Set<String>> limitter = gridPanel.getLimiter();
		List<Integer> keyList = new ArrayList(limitter.keySet());
		for (Integer index : keyList) {
			if (index < colNames.length) {
				String colName = colNames[index];
				name2ColMap.put(colName, new Integer(index));
				cmbColumn.addItem(colName);
			}
		}
	}

}
