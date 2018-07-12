/*
 * ListPanelx.java
 *
 * Created on 2007/08/28, 14:38
 */
package kyPkg.mySwing;
import java.awt.*;
//import javax.swing.*;

import java.util.*;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kyPkg.util.*;
//-----------------------------------------------------------------------------
// ListPanel <部品 > 作ってみたものの役にたたず、がっくし
//-----------------------------------------------------------------------------
public class ListPanelX extends JPanel {
    private static final long serialVersionUID = 7965088195657670952L;
    private JList      gJList;
    private List 		listData;
    //-------------------------------------------------------------------------
    // コンストラクタ
    //-------------------------------------------------------------------------
    public ListPanelX(String[] array){
        this(Arrays.asList(array));
    }
    public ListPanelX(List listData){
        super();
        this.listData = listData;
        initComponents();
    }
    private void initComponents() {
        gJList  = new JList(listData.toArray());
        LCRendCkBox listRenderer = new LCRendCkBox();
        gJList.setCellRenderer(listRenderer);
        //gJList.setUI(ListUI ui);
        setLayout(new BorderLayout());
        //setOpaque(false);
        add(new JScrollPane(gJList), java.awt.BorderLayout.CENTER);
    }
    //-------------------------------------------------------------------------
    // 描画
    //-------------------------------------------------------------------------
    @Override
	public void paintComponent(Graphics g){
        super.paintComponent(g);
    }
}
