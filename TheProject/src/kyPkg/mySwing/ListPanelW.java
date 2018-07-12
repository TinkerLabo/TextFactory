package kyPkg.mySwing;

/*
 * @(#)ListPanelW.java	1.11 07/07/19
 * Copyright 2007 Ken Yuasa. All rights reserved.
 */
//---------+---------+---------+---------+---------+---------+---------+--------
/**
 * ２つのリストボックスの汎用パーツ
 * 
 * @author Ken Yuasa
 * @version 2.00 07/07/19
 * @since 1.4
 */

// ---------+---------+---------+---------+---------+---------+---------+--------
// 《依存ファイル》
// DefListHandler.java
// LCRendLabel.java
// ProtLModel.java
// Ruler.java
// ---------+---------+---------+---------+---------+---------+---------+--------
import kyPkg.gridModels.KyListModel;
import kyPkg.util.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

// -----------------------------------------------------------------------------
// ListPanelW <部品 >
// 《使用例》
// ListPanelW listPanel_w;
// listPanel_w = new ListPanelW(new ProtLModel(wList.split(",")));
// jTabP01.addTab("ListW",listPanel_w);
// -----------------------------------------------------------------------------
// 標準のListmodelだと、選択された値をvectorで返すのに手数がかかるのでProtLModelに限定した
// -----------------------------------------------------------------------------
public class ListPanelW extends MyPanel implements InfListPanel {
	private static final long serialVersionUID = 7741236283482430228L;
	private static final String MOVEALL_R = ">>";
	private static final String MOVEALL_L = "<<";
	private String fontName = "Dialog";// フォント名　　　"ＭＳ ゴシック"
	private static final int V_SCROLLBAR_ALWAYS = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int H_SCROLLBAR_NEVER = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
	private JSplitPane splitpane;
	private KyListModel model_Src;
	private KyListModel model_Dst;
	private JList list_Src;
	private JList list_Dst;
	private JButton btnLL;
	private JButton btnRR;
	private boolean optMoveALL = true;
	private int fontSize = 12;
	private int clickCount = 1;
	private boolean unique = false;
	boolean rightClear = false;
	private JScrollPane scrollL;
	private JScrollPane scrollR;
	private JTextField regFldL;
	private JTextField regFldR;
	private int regWidth;

	//	private String vtab = null;
	//	private int viewCol = -1;

	// 左側のリストを更新する際、右側のリストをクリアするかどうか（trueなら）
	public void setRightClear(boolean rightClear) {
		this.rightClear = rightClear;
	}

	// --------------------------------------------------------------------------
	// コンストラクタ
	// --------------------------------------------------------------------------
	public ListPanelW() {
		super();
		initListPanelW(new KyListModel((List) null));
		initGUI();
	}

	public ListPanelW(int x, int y, int width, int height, boolean btnOpt) {
		this(null, x, y, width, height, btnOpt, 0);
	}

	public ListPanelW(int x, int y, int width, int height, boolean btnOpt,
			int regWidth) {
		this(null, x, y, width, height, btnOpt, regWidth);
	}

	private ListPanelW(List list, int x, int y, int width, int height,
			boolean btnOpt, int regWidth) {
		super();
		initListPanelW(new KyListModel((List) null));
		//		this.vtab = VTAB;
		//		this.viewCol = 0;
		this.optMoveALL = btnOpt;
		this.regWidth = regWidth;
		initGUI();
		setBounds(x, y, width, height);
	}

	// --------------------------------------------------------------------------
	// private ...
	// --------------------------------------------------------------------------
	private void initListPanelW(KyListModel left) {
		list_Src = new JList();
		list_Dst = new JList();
		this.setModelLeft(left);
		KyListModel right = new KyListModel((List) null);
		this.setModelRight(right);
	}

	// --------------------------------------------------------------------------
	// Init
	// --------------------------------------------------------------------------
	public void initGUI() {
		this.setLayout(new BorderLayout());
		MyPanel panel_L = new MyPanel();
		MyPanel panel_R = new MyPanel();
		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel_L,
				panel_R);
		panel_L.setLayout(new BoxLayout(panel_L, BoxLayout.Y_AXIS));
		panel_R.setLayout(new BoxLayout(panel_R, BoxLayout.Y_AXIS));
		splitpane.setDividerLocation((this.getWidth() / 2));
		this.add(splitpane, BorderLayout.CENTER);
		// ----------------------------------------------------------------------
		// レンダラー（オマケ）
		// ----------------------------------------------------------------------
		LCRendLabel listRenderer = new LCRendLabel();
		list_Src.setCellRenderer(listRenderer);
		list_Dst.setCellRenderer(listRenderer);
		btnLL = new JButton(MOVEALL_L);
		btnRR = new JButton(MOVEALL_R);
		regFldL = new JTextField();
		regFldR = new JTextField();
		// ----------------------------------------------------------------------
		Font ft1 = new Font(fontName, Font.BOLD, fontSize);
		Font ft2 = new Font(fontName, Font.PLAIN, 7);
		list_Src.setFont(ft1);
		list_Dst.setFont(ft1);
		btnLL.setFont(ft2);
		btnRR.setFont(ft2);
		scrollL = new JScrollPane(list_Src);
		scrollR = new JScrollPane(list_Dst);
		scrollL.setHorizontalScrollBarPolicy(H_SCROLLBAR_NEVER);
		scrollR.setHorizontalScrollBarPolicy(H_SCROLLBAR_NEVER);
		scrollL.setVerticalScrollBarPolicy(V_SCROLLBAR_ALWAYS);
		scrollR.setVerticalScrollBarPolicy(V_SCROLLBAR_ALWAYS);
		panel_R.add(scrollR);
		panel_L.add(scrollL);
		if (optMoveALL) {
			MyPanel pnlButtonL = new MyPanel(new BorderLayout());
			MyPanel pnlButtonR = new MyPanel(new BorderLayout());
			pnlButtonL.setMaximumSize(new Dimension(800, 12));
			pnlButtonR.setMaximumSize(new Dimension(800, 12));

			if (regWidth > 0) {
				regFldL.setPreferredSize(new Dimension(regWidth, 20));
				regFldR.setPreferredSize(new Dimension(regWidth, 20));
				pnlButtonL.add(regFldL, BorderLayout.WEST);
				pnlButtonR.add(regFldR, BorderLayout.WEST);
			}

			pnlButtonL.add(btnLL, BorderLayout.CENTER);
			pnlButtonR.add(btnRR, BorderLayout.CENTER);
			panel_L.add(pnlButtonR);
			panel_R.add(pnlButtonL);
		}
		// ----------------------------------------------------------------------
		// Action
		// ----------------------------------------------------------------------
		list_Src.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == clickCount) {
					int sIdx = ((JList) e.getSource()).getSelectedIndex();
					Object sVal = model_Src.getValueX(sIdx);
					String[] array = sVal.toString().split("…");
					regFldR.setText(array[0]);
					model_Dst.addElement(sVal, unique);
					model_Src.removeElementAt(sIdx);
				}
			}
		});
		// ----------------------------------------------------------------------
		list_Dst.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == clickCount) {
					int sIdx = ((JList) e.getSource()).getSelectedIndex();
					Object sVal = model_Dst.getValueX(sIdx);
					String[] array = sVal.toString().split("…");
					regFldL.setText(array[0]);
					model_Src.addElement(sVal, unique);
					model_Dst.removeElementAt(sIdx);
				}
			}
		});
		// ----------------------------------------------------------------------
		// ＞＞ ボタン
		// ----------------------------------------------------------------------
		btnRR.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (regWidth == 0) {
					int sz = model_Src.getSize();
					for (int index = 0; index < sz; index++) {
						model_Dst.addElement(model_Src.getValueX(index),
								unique);
					}
					model_Src.clear();
				} else {
					String regex = regFldR.getText() + ".*";
					int sz = model_Src.getSize();
					List<Integer> removeIdx = new ArrayList();
					for (int index = 0; index < sz; index++) {
						String val = String.valueOf(model_Src.getValueX(index));
						if (val.matches(regex)) {
							model_Dst.addElement(val, unique);
							removeIdx.add(index);
						}
					}
					// 後ろから消していく
					for (int pos = removeIdx.size(); pos > 0; pos--) {
						int index = removeIdx.get(pos - 1);
						model_Src.removeElementAt(index);
					}
				}
			}
		});
		// ----------------------------------------------------------------------
		// ＜＜ ボタン
		// ----------------------------------------------------------------------
		btnLL.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (regWidth == 0) {
					int sz = model_Dst.getSize();
					for (int index = 0; index < sz; index++) {
						model_Src.addElement(model_Dst.getValueX(index),
								unique);
					}
					model_Dst.clear();
				} else {
					String regex = regFldL.getText() + ".*";
					int sz = model_Dst.getSize();
					List<Integer> removeIdx = new ArrayList();
					for (int index = 0; index < sz; index++) {
						String val = String.valueOf(model_Dst.getValueX(index));
						if (val.matches(regex)) {
							model_Src.addElement(val, unique);
							removeIdx.add(index);
						}
					}
					// 後ろから消していく
					for (int pos = removeIdx.size(); pos > 0; pos--) {
						int index = removeIdx.get(pos - 1);
						model_Dst.removeElementAt(index);
					}
				}
			}
		});
		scrollR.getViewport().revalidate(); // ？？←果たして意味があるのか？？
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	// 右側のリストをクリアする
	public void clearRight() {
		this.setModelRight(null);
	}

	// 左側のリストをクリアする
	@Override
	public void clear() {
		this.setModelLeft(null);
	}

	public void setRight(String[] array) {
		this.setModelLeft(null);
		this.setModelRight(new KyListModel(array));
	}

	// ---------------------------------------------------------------
	// 一次元を流し込みたい場合(未使用？)
	// ---------------------------------------------------------------
	public void setListData(String[] array, String delimiter) {
		List<String> list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i] + delimiter + array[i]);
		}
		setListData(list);
	}

	@Override
	public void addElement(String element) {
		model_Src.addElement(element, unique);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see kyPkg.mySwing.infListPanel#setListData(java.util.List)
	 */
	@Override
	public void setListData(List listData) {
		if (listData == null)
			listData = new ArrayList();
		model_Src = new KyListModel(listData);
		if (rightClear == true)
			model_Dst = new KyListModel();
		//		resetView();
		list_Src.setModel(model_Src);
		if (rightClear == true)
			list_Dst.setModel(model_Dst);
	}

	public void setListData(String[] listData) {
		model_Src = new KyListModel(listData);
		model_Dst = new KyListModel();
		//		resetView();
		list_Src.setModel(model_Src);
		list_Dst.setModel(model_Dst);
	}

	private void setModelLeft(KyListModel model) {
		if (model == null) {
			model = new KyListModel();
		}
		model_Src = model;
		//		resetView();
		list_Src.setModel(model_Src);
	}

	private void setModelRight(KyListModel model) {
		if (model == null) {
			model = new KyListModel();
		}
		model_Dst = model;
		//		resetView();
		list_Dst.setModel(model_Dst);
	}

	public void setListData(Object[] array) {
		List list = null;
		if (array != null) {
			list = new ArrayList(array.length);
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
		}
		setListData(list);
	}

	// -------------------------------------------------------------------------
	// 何クリックで反応させるか
	// -------------------------------------------------------------------------
	public void setClickCount(int pCount) {
		clickCount = pCount;
	}

	// --------------------------------------------------------------------------
	// リストの値を途中で交換する場合
	// --------------------------------------------------------------------------
	public void setLeft(String[] array) {
		this.setModelLeft(new KyListModel(array));
		this.setModelRight(null);
	}

	// --------------------------------------------------------------------------
	// リストの値を配列として受け取る
	// --------------------------------------------------------------------------
	public String[] getStringArray() {
		return model_Dst.getArray();
	}

	@Override
	public List getList() {
		return model_Dst.getDataList();
	}

	public List getList_L() {
		return model_Src.getDataList();
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		list_Src.setEnabled(b);
		list_Dst.setEnabled(b);
		btnLL.setEnabled(b);
		btnRR.setEnabled(b);
		scrollL.setEnabled(b);
		scrollR.setEnabled(b);
	}

	// -------------------------------------------------------------------------
	// Methods
	// --------------------------------------------------------------------------
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		splitpane.setDividerLocation((width / 2));
	}

}
