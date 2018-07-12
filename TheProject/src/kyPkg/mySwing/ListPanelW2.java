package kyPkg.mySwing;
/*
 * @(#)ListPanelW.java	1.11 07/07/19
 * Copyright 2007 Ken Yuasa. All rights reserved.
 */
//---------+---------+---------+---------+---------+---------+---------+--------
/**
 * ２つのリストボックスの汎用パーツ
 * @author  Ken Yuasa
 * @version 2.00 07/07/19
 * @since   1.4
 */
//---------+---------+---------+---------+---------+---------+---------+--------
//《依存ファイル》
// DefListHandler.java
// LCRendLabel.java
// ProtLModel.java
// Ruler.java
//---------+---------+---------+---------+---------+---------+---------+--------
import kyPkg.gridModels.KyListModel;
import kyPkg.util.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

//import javax.swing.*;
//import javax.swing.table.*;
//-----------------------------------------------------------------------------
// ListPanelW <部品 >
// 《使用例》
//	ListPanelW listPanel_w;
//	listPanel_w = new ListPanelW(new ProtLModel(wList.split(",")));
//	jTabP01.addTab("ListW",listPanel_w);
//-----------------------------------------------------------------------------
// 標準のListmodelだと、選択された値をvectorで返すのに手数がかかるのでProtLModelに限定した
//-----------------------------------------------------------------------------
public class ListPanelW2 extends JPanel{
//	private String fontName = "ＭＳ ゴシック";
	private String fontName = "Dialog";

	private boolean unique = false;
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	private String btnCaption ="OK";
	private static final long serialVersionUID = -2680155764115473058L;
	private JButton    btnCommit;
	private JButton    btnReset;
	private JButton    btnAddAll;
	private JTextField txtDebug;
	private JList      list_Src;
	private JList      list_Dst;
	private KyListModel model_Src;
	private KyListModel model_Dst;
	private boolean 	useGrid = false;
	private int      	fontSize  = 12;
	private int      	clickCount  = 1;
	private Color   	bkColor = Color.WHITE;
	private ActionListener actionListener;
	//-------------------------------------------------------------------------
	// アクセッサ
	//-------------------------------------------------------------------------
	public void setBtnCaption(String btnCaption) {
		this.btnCaption = btnCaption;
		btnCommit.setText(this.btnCaption);
	}
	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	public JList getList_Src() {
		return list_Src;
	}
	public JList getList_Dst() {
		return list_Dst;
	}
	public void setClickCount(int clickCount){
		this.clickCount = clickCount; // 何クリックで反応させるか
	}
	public void setBtnName(String caption) {
		this.btnCommit.setText(caption) ;
	}
	public JTextField getTxtCurrent() {
		return txtDebug;
	}
	public void setEditable(boolean flag) {
		txtDebug.setEditable(flag);
	}
	//--------------------------------------------------------------------------
	// リストの値を途中で交換する場合など・・
	//--------------------------------------------------------------------------
//	public void setListData(Vector listData){
//	model_Src = new KyListModel(listData);
//	model_Dst = new KyListModel();
//	list_Src.setModel(model_Src);
//	list_Dst.setModel(model_Dst);
//}
	public void setListData(List listData) {
		model_Src = new KyListModel(listData);
		model_Dst = new KyListModel();
		list_Src.setModel(model_Src);
		list_Dst.setModel(model_Dst);
	}
	//--------------------------------------------------------------------------
	public void setListData(Object[] anArray){
		Vector wVec = null;
		if (anArray != null){
			wVec = new Vector(anArray.length);
			for (int i=0; i < anArray.length; i++) {
				wVec.addElement(anArray[i]);
			}
		}
		setListData(wVec);
	}
	//-------------------------------------------------------------------------
	public void setListModel_L(KyListModel pLModel_L){
		if (pLModel_L!= null){
			model_Src = pLModel_L;
		}else{
			model_Src = new KyListModel();
		}
		list_Src.setModel(model_Src);
	}
	//-------------------------------------------------------------------------
	public void setListModel_R(KyListModel pLModel_R){
		if (pLModel_R!= null){
			model_Dst = pLModel_R;
		}else{
			model_Dst = new KyListModel();
		}
		list_Dst.setModel(model_Dst);
	}
	//--------------------------------------------------------------------------
	// リストの値をＶｅｃｔｏｒとして受け取る
	//--------------------------------------------------------------------------
	public List getVector(){
		return model_Dst.getData();
	}
	//--------------------------------------------------------------------------
//	private List getVector_L(){
//		return model_Src.getData();
//	}
	//--------------------------------------------------------------------------
	// リストの値をobject配列として受け取る
	//--------------------------------------------------------------------------
	public Object[] getArray(){
		return model_Dst.getArray();
	}
//	private Object[] getArray_L(){
//		return model_Src.getStringArray();
//	}
	//--------------------------------------------------------------------------
	// コンストラクタ
	//--------------------------------------------------------------------------
	public ListPanelW2(){
		this((Vector)null,(Vector)null);
	}
	//--------------------------------------------------------------------------
	public ListPanelW2(Vector listData_L){
		this(listData_L,(Vector)null);
	}
	//--------------------------------------------------------------------------
	public ListPanelW2(KyListModel pLModel_L){
		this(pLModel_L,(KyListModel)null);
	}
	//--------------------------------------------------------------------------
	public ListPanelW2(Vector listData_L,Vector listData_R){
		this(new KyListModel(listData_L),new KyListModel(listData_R));
	}
	//--------------------------------------------------------------------------
	public ListPanelW2(KyListModel pLModel_L,KyListModel pLModel_R){
		super();
		list_Src = new JList();
		list_Dst = new JList();
		setListModel_L(pLModel_L);
		setListModel_R(pLModel_R);
		initMe();
	}
	//--------------------------------------------------------------------------
	// init 初期化する
	//--------------------------------------------------------------------------
	public void initMe(){
		Font ft1 = new Font(fontName,Font.BOLD,fontSize);
		Font ft2 = new Font(fontName,Font.PLAIN,10);
		//---------------------------------------------------------------------
		// Get Panel
		//---------------------------------------------------------------------
		JScrollPane scroll_L = new JScrollPane(list_Src);
		JScrollPane scroll_R = new JScrollPane(list_Dst);
		scroll_L.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_R.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_L.setVerticalScrollBarPolicy  (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll_R.setVerticalScrollBarPolicy  (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel pnlGet = new JPanel();
		pnlGet.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill  = GridBagConstraints.BOTH;
		gbc.weightx = 1;gbc.weighty = 1;
		gbc.gridy = 0;	gbc.gridx = 0; gbc.gridheight = 1; gbc.gridwidth = 2;
		pnlGet.add(scroll_L ,gbc);
		gbc.gridy = 1;	gbc.gridx = 0; gbc.gridheight = 1; gbc.gridwidth = 2;
		pnlGet.add(scroll_R ,gbc);
		//---------------------------------------------------------------------
		// Button Panel
		//---------------------------------------------------------------------
		btnCommit = new JButton(btnCaption);	btnCommit.setPreferredSize(new Dimension(100,20));
		btnAddAll = new JButton("AddAll");	btnAddAll.setPreferredSize(new Dimension(100,20));
		btnReset  = new JButton("Reset ");	btnCommit.setPreferredSize(new Dimension(100,20));
		txtDebug  = new JTextField("");		txtDebug.setPreferredSize(new Dimension(300,20));
		JPanel pnlBtn = new JPanel();
		pnlBtn.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlBtn.add(btnAddAll);  btnAddAll.setFont(ft2);
		pnlBtn.add(btnReset );  btnReset.setFont(ft2);
		pnlBtn.add(btnCommit);  btnCommit.setFont(ft2);
		pnlBtn.add(txtDebug);
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(pnlGet,BorderLayout.CENTER);
		this.add(pnlBtn,BorderLayout.SOUTH);
		//----------------------------------------------------------------------
		// レンダラー（オマケ）
		//----------------------------------------------------------------------
		LCRendLabel listRenderer = new LCRendLabel();
		list_Src.setCellRenderer(listRenderer);
		list_Dst.setCellRenderer(listRenderer);
		list_Src.setFont(ft1);
		list_Dst.setFont(ft1);

		scroll_R.getViewport().revalidate(); // ？？←果たして意味があるのか？？

		//----------------------------------------------------------------------
		// Action  
		//----------------------------------------------------------------------
		list_Src.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == clickCount){
					Object sVal = ((JList)e.getSource()).getSelectedValue();
					int sIdx = ((JList)e.getSource()).getSelectedIndex();
					txtDebug.setText((String)sVal);
					model_Dst.addElement(sVal,unique);
					model_Src.removeElementAt(sIdx);
				}
			}
		});
		//----------------------------------------------------------------------
		list_Dst.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == clickCount){
					Object sVal = ((JList)e.getSource()).getSelectedValue();
					int sIdx = ((JList)e.getSource()).getSelectedIndex();
					model_Src.addElement(sVal,unique);
					model_Dst.removeElementAt(sIdx);
				}
			}
		});
		//----------------------------------------------------------------------
		// Resetボタン
		//----------------------------------------------------------------------
		btnReset.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sz = model_Dst.getSize();
				for (int i = 0 ; i < sz ; i++ ){
					model_Src.addElement(model_Dst.getElementAt(i),unique);//どっちが欲しいんだい＿？
				}
				model_Dst.clear();
			}
		});

		//----------------------------------------------------------------------
		// ＞＞ ボタン
		//----------------------------------------------------------------------
		btnAddAll.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sz = model_Src.getSize();
				for (int i = 0 ; i < sz ; i++ ){
					model_Dst.addElement(model_Src.getElementAt(i),unique);//どっちが欲しいんだい＿？
				}
				model_Src.clear();
			}
		});
		//---------------------------------------------------------------------
		// Downloadボタン
		//---------------------------------------------------------------------
		btnCommit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (actionListener!=null){
					actionListener.actionPerformed(arg0);
				}
			}
		});

	}
	//--------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g){
		if (bkColor == null)	bkColor = Color.white;
		if (useGrid ) Ruler.drawRuler(g,this.getSize().width,this.getSize().height,bkColor);
	}
}
