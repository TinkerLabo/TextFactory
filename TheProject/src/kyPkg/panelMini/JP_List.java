package kyPkg.panelMini;

//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
/*
 * @(#)JPanel_?.java	1.11 04/09/15
 * Copyright 2004 Ken Yuasa. All rights reserved.
 */
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
/**
 * ２つのリストボックスの汎用パーツ
 *
 * @author Ken Yuasa
 * @version 2.00 05/01/31
 * @since 1.3
 */
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//XXX 表示する値と内部で保持する値を別にもてるように変更する
//XXX 選択済みリストはクリアしたくない場合もある
//XXX 選択済みにする際に付加したい状態情報がある
/**
 * ****************************************************************************
 * ＪＰ＿Ｌｉｓｔ
 ******************************************************************************
 */
public class JP_List extends JPanel {

    private static final long serialVersionUID = 1619850261375983209L;
    public DefaultListModel dlm_L;
    public DefaultListModel dlm_R;
    public JList jList_L;
    public JList jList_R;
    public JButton jBtn_L;
    public JButton jBtn_R;
    public List<Inf_Reloader_LR> reloaders = new ArrayList();

    public void addReloader(Inf_Reloader_LR reloader) {
        this.reloaders.add(reloader);
    }

    public void resetReloader() {
        reloaders = new ArrayList();
    }

    private void updated() {
        for (Inf_Reloader_LR reloader : reloaders) {
            if (reloader != null) {
                reloader.reLoad(getList_L(), getList());
            }
        }
    }

	// -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    public JP_List() {
        this(Color.white);
    }

    public JP_List(Color bgColor) {
        super();
        initMe();
        // this.setOpaque(false);
        initListModel();
        repaint();
    }

    public String getDimComment() {
        List<Integer> mapCols = new ArrayList();
        StringBuffer buff = new StringBuffer();
        for (String element : getList()) {
            if (buff.length() > 0) {
                buff.append("ｘ");
            }
            buff.append("【");
            buff.append(element);
            buff.append("】");
        }
        return buff.toString();
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

    public void setList_R(List list_R) {
        setList(null, list_R);
    }

	// -------------------------------------------------------------------------
    // TODO 画面の仕様が甘すぎるので・・・もっと限定的な画面にする必要がある
    // ここを指定品目の区分を設定する箇所と同じ名称になるように編集する
    // -------------------------------------------------------------------------
    public void setMatrix(List<List> matrix, String colon) {
        List list_L = new ArrayList();
        StringBuffer buff = new StringBuffer();
        for (List<String> list : matrix) {
            buff.delete(0, buff.length());
            for (String element : list) {
                if (buff.length() > 0) {
                    buff.append(colon);
                }
                buff.append(element);
            }
            list_L.add(buff.toString());
        }
        setList(list_L, null);
    }

	// -------------------------------------------------------------------------
    // リスト左の中味を取り出す
    // -------------------------------------------------------------------------
    public List getList_L() {
        List list = new ArrayList();
        for (int i = 0; i < dlm_L.size(); i++) {
            list.add(dlm_L.getElementAt(i));
        }
        return list;
    }

	// -------------------------------------------------------------------------
    // リスト右の中味を取り出す
    // -------------------------------------------------------------------------
    public List<String> getList() {
        List list = new ArrayList();
        for (int i = 0; i < dlm_R.size(); i++) {
            list.add(dlm_R.getElementAt(i));
        }
        return list;
    }

    public void initListModel() {
        List list_L = getOriginalRamones();
        List list_R = new ArrayList();
        list_R.add("Marky   Ramone  (drums;  born Marc    Bell     July 15,1956)");
        setList(list_L, list_R);
    }

    //20140415 
    public void clear() {
        setList(null, null);
    }

    public void setList(List<String> list_L, List<String> list_R) {
        dlm_L = new DefaultListModel();
        if (list_L != null) {
            for (String element : list_L) {

                dlm_L.addElement(element);
            }
        }
        jList_L.setModel(dlm_L);

        dlm_R = new DefaultListModel();
        if (list_R != null) {
            for (String element : list_R) {
                dlm_R.addElement(element);
            }
        }
        jList_R.setModel(dlm_R);
        // なぜか上手く再描画されないので・・しつこくリペイントしている
        jList_L.repaint();
        jList_R.repaint();
        repaint();
        updated();
    }

	// -------------------------------------------------------------------------
    // init 初期化する
    // -------------------------------------------------------------------------
    public void initMe() {
        this.setOpaque(false);
        GridBagLayout gb = new GridBagLayout();
        this.setLayout(gb);
        jList_L = new JList();
        jList_R = new JList();
        dlm_L = new DefaultListModel();
        dlm_R = new DefaultListModel();
		// dlm_L.removeAllElements();
        // dlm_R.removeAllElements();
        jList_L.setModel(dlm_L);
        jList_R.setModel(dlm_R);
        jBtn_L = new JButton("<<");
        jBtn_R = new JButton(">>");
        JScrollPane jScp1 = new JScrollPane();
        JScrollPane jScp2 = new JScrollPane();
        jScp1.getViewport().add(jList_L, null);
        jScp1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScp2.getViewport().add(jList_R, null);
        jScp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // ---------------------------------------------------------------------
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5); // top,left,bottom,right
        gc.weightx = 8;
        gc.weighty = 6;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridwidth = 1;
        gc.gridheight = 6;
        // ---------------------------------------------------------------------
        gc.gridx = 0;
        gc.gridy = 0;
        gb.setConstraints(jScp1, gc);
        this.add(jScp1);
        // ---------------------------------------------------------------------
        gc.gridx = 3;
        gc.gridy = 0;
        gb.setConstraints(jScp2, gc);
        this.add(jScp2);
        // ---------------------------------------------------------------------
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        // ---------------------------------------------------------------------
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(10, 0, 0, 0); // top,left,bottom,right
        gc.gridx = 1;
        gc.gridy = 1;
        gb.setConstraints(jBtn_R, gc);
        this.add(jBtn_R);
        // ---------------------------------------------------------------------
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(0, 0, 40, 0); // top,left,bottom,right
        gc.gridx = 1;
        gc.gridy = 3;
        gb.setConstraints(jBtn_L, gc);
        this.add(jBtn_L);

		// ---------------------------------------------------------------------
        // Action
        // ---------------------------------------------------------------------
        jList_L.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Object sVal = ((JList) e.getSource()).getSelectedValue();
                int sIdx = ((JList) e.getSource()).getSelectedIndex();
                dlm_R.addElement(sVal);
                if ((dlm_L.size() > sIdx) && (sIdx >= 0)) {
                    dlm_L.removeElementAt(sIdx);
                }
                updated();
            }
        });
        jList_R.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Object sVal = ((JList) e.getSource()).getSelectedValue();
                int sIdx = ((JList) e.getSource()).getSelectedIndex();
                dlm_L.addElement(sVal);
                if ((dlm_R.size() > sIdx) && (sIdx >= 0)) {
                    dlm_R.removeElementAt(sIdx);
                }
                updated();
            }
        });
        jBtn_L.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sz = dlm_R.getSize();
                for (int i = 0; i < sz; i++) {
                    dlm_L.addElement(dlm_R.getElementAt(i));
                }
                dlm_R.clear();
                updated();
            }
        });
        jBtn_R.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sz = dlm_L.getSize();
                for (int i = 0; i < sz; i++) {
                    dlm_R.addElement(dlm_L.getElementAt(i));
                }
                dlm_L.clear();
                updated();
            }
        });
        jScp2.getViewport().revalidate(); // ←果たして意味があるのか？？
    }

	// -------------------------------------------------------------------------
    // テスト用のダミー値をセットする
    // -------------------------------------------------------------------------
    public List<String> getOriginalRamones() {
        List<String> list = new ArrayList();
        list.add("Joey    Ramone  (vocals; born Jeffrey Hyman    May  19,1951)");
        list.add("Johnny  Ramone  (guitar; born John    Cummings Oct  08,1951)");
        list.add("Tommy   Ramone  (drums;  born Tom     Erdelyi  Jan. 29,1952)");
        list.add("Dee Dee Ramone  (bass;   born Douglas Colvin   Sept 18,1952)");
        return list;
    }

	// public void paintComponent(Graphics g) {
    // super.paintComponent(g);
    // kyPkg.util.Ruler.drawRuler(g, this.getSize().width,
    // this.getSize().height, Color.WHITE);
    // }
    // public void paint(Graphics g) {
    // super.paint(g); // Super経由でpaintCompをよぶ
    // }
    //
    // public void paintComponent(Graphics g) {
    // kyPkg.util.Ruler.paintIt(g, this.getSize().width,
    // this.getSize().height, bgColor);
    // }
}
