/*
 * ListPanelx.java
 *
 * Created on 2007/08/28, 14:38
 */
package kyPkg.mySwing;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import kyPkg.gridModels.KyListModel;
import kyPkg.util.LCRendLabel;

// -----------------------------------------------------------------------------
// ListPanel <部品 >
// ----------------------------------------------------------------------
// ListPanelの使い方
// 1.リストモデルを作成
// 2.リストモデルを引数にインスタンス作成
// 3.クリックされたものを出力するオブジェクトを指定する
// ※リストモデルには色々な形式でデータを流し込みやすくしておく。
// ----------------------------------------------------------------------
// listP_Dir = new ListPanel(new ProtLModel());
// 《使用例》
// ListPanel listPanel = new ListPanel();
// listPanel.addElement("UNIX,Linux,OSX,WindowsVista".split(","));
// -----------------------------------------------------------------------------


//TODO yen-mark を表示できるようにしたいが・・・よくわからなくなってしまった　FontUtility.setFont(new Font("メイリオ", Font.PLAIN, 10));


public class ListPanel extends JPanel implements InfListPanel {
	private static final long serialVersionUID = 7965088195657670952L;
	private JList jList;
	private KyListModel listModelK;
	private boolean unique = false; // ユニーク値しか追加できないようにする

	public int listCount() {
		return listModelK.getSize();
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	// array リストに流し込む配列
	// unique trueならリストに流し込む値をユニークにする
	// remove trueならダブルクリックした場合値をリストからはずす
	// -------------------------------------------------------------------------
	public ListPanel(Rectangle rect, boolean unique, boolean remove) {
		this(unique, remove);
		setBounds(rect);
	}

	public ListPanel(boolean unique, boolean remove) {
		this((String[]) null, unique, remove);
	}

	public ListPanel(String[] array, boolean unique, boolean remove) {
		super();
		this.unique = unique;
		this.listModelK = new KyListModel(array);
		jList = new JList(listModelK);
		// jList.setModel(listModelK);
		// ---------------------------------------------------------------------
		// レンダラー
		// ---------------------------------------------------------------------
		// LCRendLabel listRenderer = new LCRendLabel();
		jList.setCellRenderer(new LCRendLabel());
		setLayout(new BorderLayout());
		setOpaque(false);
		add(new JScrollPane(jList), java.awt.BorderLayout.CENTER);
		addMouseListener(new ListMouseAdapter(remove));
	}

	@Override
	public void addMouseListener(MouseListener mouseListener) {
		jList.addMouseListener(mouseListener);
	}

	public int getSelectedIndex() {
		return jList.getSelectedIndex();
	}

	public Object getSelectedValue() {
		return jList.getSelectedValue();
	}

	public void setSelectedIndex(int index) {
		jList.setSelectedIndex(index);
	}

	public void setListModel(KyListModel pLModel) {
		listModelK = pLModel;
		jList.setModel(listModelK);
	}

	// --------------------------------------------------------------------------
	// ラッパー
	// --------------------------------------------------------------------------
	public void setListData(Object[] listData) {
		listModelK.clear();
		for (int i = 0; i < listData.length; i++) {
			listModelK.addElement(listData[i], unique);
		}
		// うまく再描画されないのでモデルの再セットをしてみる　2012-12-07
		jList.setModel(listModelK);
		jList.repaint();
		repaint();
	}

	@Override
	public void setListData(List list) {
		listModelK.clear();
		if (list == null || list.size() <= 0)
			return;
		for (Object element : list) {

			listModelK.addElement(element, unique);
		}
		// ここでトラブル発生　2013-02-01（バックカラーのα値をいじっていたのが原因ぽい）
		// α値をいじると描画がおかしくなるようだ・・残念・
		// int iR, iG, iB, alpha;
		// iR = (int) Math.floor(Math.random() * 256);
		// iG = (int) Math.floor(Math.random() * 256);
		// iB = (int) Math.floor(Math.random() * 256);
		// alpha = 30;
		// Color wBackColor = new Color(iR, iG, iB, alpha);
		// jList.setBackground(wBackColor);
		// うまく再描画されないのでモデルの再セットをしてみる　2012-12-07
		jList.setModel(listModelK);
		jList.updateUI();// 20130516
	}

	// 必要になったら追加することにする↓
	// // ----------------------------------------------------------------------
	// public void setListMouseListener(MouseListener mouseListener) {
	// jList.addMouseListener(mouseListener);
	// }
	// // ----------------------------------------------------------------------
	// list_Src.addMouseListener(new java.awt.event.MouseAdapter() {
	// public void mouseClicked(MouseEvent e) {
	// if (e.getClickCount() == clickCount) {
	// int sIdx = ((JList) e.getSource()).getSelectedIndex();
	// Object sVal = model_Src.getValueX(sIdx);
	// model_Dst.addElement(sVal, unique);
	// model_Src.removeElementAt(sIdx);
	// }
	// }
	// });

	@Override
	public void remove(int index) {
		listModelK.remove(index); // 2010/02/08
	}

	// -------------------------------------------------------------------------
	// アクセッサ
	// -------------------------------------------------------------------------
	public int getListSize() {
		return listModelK.size();
	}

	@Override
	public void addElement(String sVal) {
		listModelK.addElement(sVal, unique);
	}

	public void addElement(String[] array) {
		for (String element : array) {
			addElement(element);
		}
	}

	public void setElementAt(Object obj, int index) {
		if (index >= 0 && listModelK.size() >= index) {
			listModelK.setElementAt(obj, index);
		}
	}

	public void insertElementAt(Object obj, int index) {
		listModelK.insertElementAt(obj, index);
	}

	public Object getElementAt(int index) {
		if (index >= 0 && listModelK.size() >= index) {
			return listModelK.getElementAt(index);// どっちが欲しいんだい＿？
		}
		return null;
	}

	public Object getValueX(int index) {
		if (index >= 0 && listModelK.size() >= index) {
			return listModelK.getValueX(index);
		}
		return null;
	}

	public Object getValueX(int index, int seq) {
		if (index >= 0 && listModelK.size() >= index) {
			return listModelK.getValueX(index, seq);
		}
		return null;
	}

	@Override
	public void clear() {
		listModelK.clear();
	}

	@Override
	public List<Object> getList() {
		if (listModelK == null)
			return null;
		return listModelK.getData();
	}

	public void setList(List list) {
		if (listModelK != null && list != null) {
			listModelK.setDataList(list);
		}
	}

	public List getOriginalRamones() {
		List<String> list = new ArrayList();
		list.add("Joey    Ramone  (vocals; born Jeffrey Hyman    May  19,1951)");
		list.add("Johnny  Ramone  (guitar; born John    Cummings Oct  08,1951)");
		list.add("Tommy   Ramone  (drums;  born Tom     Erdelyi  Jan. 29,1952)");
		list.add("Dee Dee Ramone  (bass;   born Douglas Colvin   Sept 18,1952)");
		return list;
	}

	// -------------------------------------------------------------------------
	// 描画
	// -------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
