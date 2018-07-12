package kyPkg.panelMini;

//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
/*
 * @(#)JP_MashUp.java	1.11 12/06/25
 * Copyright 2004 Ken Yuasa. All rights reserved.
 */
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
/**
 * ２つのリストボックスの汎用パーツ
 * @author  Ken Yuasa
 * @version 2.00 05/01/31
 * @since   1.3
 */
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/******************************************************************************
 * ＪＰ＿Ｌｉｓｔをベースに作成
 *******************************************************************************/
public class JP_MashUp extends JPanel {
	private static final long serialVersionUID = 1619850261375983209L;
	private static final String pref_L = "L|";
	private static final String pref_R = "R|";
	public DefaultListModel dlm_L;
	public DefaultListModel dlm_C;
	public DefaultListModel dlm_R;

	public JList jList_L;
	public JList jList_C;
	public JList jList_R;

	public JButton jBtn_L;
	public JButton jBtn_C;
	public JButton jBtn_R;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public JP_MashUp() {
		super();
		initGUI();
		this.setOpaque(false);
		initListModelxx();
		repaint();
	}

	// -------------------------------------------------------------------------
	// リストの中味をセットする
	// -------------------------------------------------------------------------
	public void setArray(String[] array) {
		setList(Arrays.asList(array));
	}

	public void setList(List list_L) {
		setList(list_L, null);
	}

	public void setMatrix(List<List<String>> matrix) {
		String delimiter = ":";
		List list_L = new ArrayList();
		StringBuffer buff = new StringBuffer();
		for (List<String> list : matrix) {
			buff.delete(0, buff.length());
			for (String element : list) {
				buff.append(element);
				buff.append(delimiter);
			}
			// for (Iterator iterator2 = list.iterator(); iterator2.hasNext();)
			// {
			// buff.append((String) iterator2.next());
			// buff.append(delimiter);
			// }
			buff.delete(buff.length() - 1, buff.length());// 最後の一文字をカットする
			list_L.add(buff.toString());
		}
		// for (Iterator iterator = matrix.iterator(); iterator.hasNext();) {
		// List list = (List) iterator.next();
		// buff.delete(0, buff.length());
		// for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
		// buff.append((String) iterator2.next());
		// buff.append(delimiter);
		// }
		// buff.delete(buff.length() - 1, buff.length());// 最後の一文字をカットする
		// list_L.add(buff.toString());
		// }
		setList(list_L, null);
	}

	public void setList(List<String> list_L, List<String> list_R) {
		dlm_C.removeAllElements();
		dlm_L.removeAllElements();
		dlm_R.removeAllElements();
		if (list_L != null) {
			for (String element : list_L) {
				dlm_L.addElement(pref_L + element);
			}
		}
		if (list_R != null) {
			for (String element : list_R) {
				dlm_R.addElement(pref_R + element);
			}
		}
		repaint();
	}

	private List lm2List(DefaultListModel model) {
		List list = new ArrayList();
		if (model == null)
			return null;
		for (int i = 0; i < model.size(); i++) {
			list.add(model.getElementAt(i));
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// リスト左の中味を取り出す
	// -------------------------------------------------------------------------
	public List getList_L() {
		return lm2List(dlm_L);
	}

	// -------------------------------------------------------------------------
	// Destination リスト右の中味を取り出す
	// -------------------------------------------------------------------------
	public List getList() {
		return lm2List(dlm_C);
	}

	// -------------------------------------------------------------------------
	// リスト左の中味を取り出す
	// -------------------------------------------------------------------------
	public List getList_R() {
		return lm2List(dlm_R);
	}

	// -------------------------------------------------------------------------
	// テスト用のダミー値をセットする
	// -------------------------------------------------------------------------
	public void initListModelxx() {
		List<String> list_L = new ArrayList();
		List<String> list_R = new ArrayList();
		list_L.add("Joey    (vocals; born Jeffrey Hyman     May  19,1951)");
		list_L.add("Johnny  (guitar; born John    Cummings  Oct  08,1951)");
		list_L.add("Dee Dee (bass;   born Douglas Colvin    Sep  18,1952)");
		list_L.add("Tommy   (drums;  born Tom     Erdelyi   Jan  29,1952)");
		list_R.add("Marky   (drums;  born Marc    Bell      Jul  15,1956)");
		list_R.add("Richie  (drums;  born Richard Reinhardt Aug  11,1957)");
		list_R.add("Elvis   (drums;  born Clem    Burke     Nov  24,1954)");
		list_R.add("C. J.   (bass;   born C.J     Ward      Oct   8,1965)");
		setList(list_L, list_R);
	}

	// -------------------------------------------------------------------------
	// init 初期化する
	// -------------------------------------------------------------------------
	public void initGUI() {
		dlm_L = new DefaultListModel();
		dlm_C = new DefaultListModel();
		dlm_R = new DefaultListModel();
		GridBagLayout gb = new GridBagLayout();
		this.setLayout(gb);
		jList_L = new JList(dlm_L);
		jList_C = new JList(dlm_C);
		jList_R = new JList(dlm_R);
		jBtn_L = new JButton("=>>");
		jBtn_C = new JButton("<<= Reset =>>");
		jBtn_R = new JButton("<<=");

		JScrollPane jScp_L = new JScrollPane();
		jScp_L.getViewport().add(jList_L, null);
		jScp_L
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScp_L
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollPane jScp_C = new JScrollPane();
		jScp_C.getViewport().add(jList_C, null);
		jScp_C
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScp_C
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollPane jScp_R = new JScrollPane();
		jScp_R.getViewport().add(jList_R, null);
		jScp_R
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScp_R
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// ---------------------------------------------------------------------
		GridBagConstraints gc = new GridBagConstraints();
		// gc.insets = new Insets(5, 5, 5, 5); // top,left,bottom,right
		gc.weightx = 1;
		gc.weighty = 8;
		gc.fill = GridBagConstraints.BOTH;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		// ---------------------------------------------------------------------
		gc.gridx = 0;
		gc.gridy = 0;
		gb.setConstraints(jScp_L, gc);
		this.add(jScp_L);
		// ---------------------------------------------------------------------
		gc.gridx = 1;
		gc.gridy = 0;
		gb.setConstraints(jScp_C, gc);
		this.add(jScp_C);
		// ---------------------------------------------------------------------
		gc.gridx = 2;
		gc.gridy = 0;
		gb.setConstraints(jScp_R, gc);
		this.add(jScp_R);
		// ---------------------------------------------------------------------
		// gc.weightx = 1;
		gc.weighty = 1;
		// gc.gridwidth = 1;
		// gc.gridheight = 1;
		// ---------------------------------------------------------------------
		gc.fill = GridBagConstraints.HORIZONTAL;

		// ---------------------------------------------------------------------
		gc.gridx = 0;
		gc.gridy = 1;
		gb.setConstraints(jBtn_L, gc);
		jBtn_L.setPreferredSize(new Dimension(100, 20));
		this.add(jBtn_L);
		// ---------------------------------------------------------------------
		gc.gridx = 1;
		gc.gridy = 1;
		gb.setConstraints(jBtn_C, gc);
		jBtn_C.setPreferredSize(new Dimension(100, 20));
		this.add(jBtn_C);
		// ---------------------------------------------------------------------
		gc.gridx = 2;
		gc.gridy = 1;
		gb.setConstraints(jBtn_R, gc);
		jBtn_R.setPreferredSize(new Dimension(100, 20));
		this.add(jBtn_R);
		// ---------------------------------------------------------------------
		// Action
		// ---------------------------------------------------------------------
		jList_L.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Object sVal = ((JList) e.getSource()).getSelectedValue();
				int sIdx = ((JList) e.getSource()).getSelectedIndex();
				// センターに追加する
				dlm_C.addElement(sVal);
				if ((dlm_L.size() > sIdx) && (sIdx >= 0))
					dlm_L.removeElementAt(sIdx);
			}
		});
		jList_R.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Object sVal = ((JList) e.getSource()).getSelectedValue();
				int sIdx = ((JList) e.getSource()).getSelectedIndex();
				// センターに追加する
				dlm_C.addElement(sVal);
				if ((dlm_R.size() > sIdx) && (sIdx >= 0))
					dlm_R.removeElementAt(sIdx);
			}
		});

		jList_C.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String sVal = (String) ((JList) e.getSource())
						.getSelectedValue();
				int sIdx = ((JList) e.getSource()).getSelectedIndex();
				if (sVal == null)
					return;
				// センターから戻す（プリフィックスを見る！！）
				if (sVal.startsWith(pref_L)) {
					dlm_L.addElement(sVal);
				} else {
					dlm_R.addElement(sVal);
				}

				if ((dlm_C.size() > sIdx) && (sIdx >= 0))
					dlm_C.removeElementAt(sIdx);
			}
		});

		jBtn_C.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sz = dlm_C.getSize();
				for (int i = 0; i < sz; i++) {
					String sVal = (String) dlm_C.getElementAt(i);
					// センターから戻す（プリフィックスを見る！！）
					if (sVal == null)
						return;
					if (sVal.startsWith(pref_L)) {
						dlm_L.addElement(sVal);
					} else {
						dlm_R.addElement(sVal);
					}
				}
				dlm_C.clear();
			}
		});

		jBtn_L.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sz = dlm_L.getSize();
				for (int i = 0; i < sz; i++) {
					dlm_C.addElement(dlm_L.getElementAt(i));
				}
				dlm_L.clear();
			}
		});
		jBtn_R.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sz = dlm_R.getSize();
				for (int i = 0; i < sz; i++) {
					dlm_C.addElement(dlm_R.getElementAt(i));
				}
				dlm_R.clear();
			}
		});

		jScp_C.getViewport().revalidate(); // ←果たして意味があるのか？？
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// kyPkg.util.Ruler.drawRuler(g, this.getSize().width,
		// this.getSize().height, Color.WHITE);
	}
}
