package kyPkg.sql.gui;

//-------+---------+---------+---------+---------+---------+---------+---------+
/*
 * @(#)SQL_GVDlg.java	1.00 05/01/07
 * Copyright 2004 Ken Yuasa. All rights reserved.
 */
//-------+---------+---------+---------+---------+---------+---------+---------+
/**
 * ＳＱＬの結果をダイアログ上のリストに表示する
 * 抽出条件の設定時にデータ中の値を参考表示させる際に使用している
 * 参考　→　SQL_Login.java
 * @author  Ken Yuasa
 * @version 1.00 05/01/07
 * @since   1.3
 */
//-------+---------+---------+---------+---------+---------+---------+---------+
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class SQL_ValueDlg {
	private String jUrl;
	private String user;
	private String paswd;
	private String table;
	private String field;
	private int sqlType;

	private JDialog dialog;
	private Object targetObj;
	private JList resList;
	private JComboBox cmbExpr;
	private JTextField txtVal1;
	private JButton btnSearch;
	private JButton btnCancel;

	// ------------------------------------------------------------------------
	// 引数
	// target 選択された値が返されるオブジェクト
	// pPosX,int pPosY ダイアログが表示される位置
	// pJurl,pUser,pPass 接続用の一般的なパラメータ
	// pTBL,pFLD 対象テーブルおよびフィールド
	// フィールドの型も必要かもしれない・・・ごちゃごちゃするので割愛
	// 《使用例》 new SQL_GVDlg(jTex,30,30,pURL,pUser,pPass,pTBL,pFLD);
	// ------------------------------------------------------------------------
	SQL_ValueDlg(Object target, String jUrl, String user, String paswd,
			String table, String field, int sqlType) {
		this.targetObj = target;
		this.jUrl = jUrl;
		this.user = user;
		this.paswd = paswd;
		this.table = table;
		this.field = field;
		this.sqlType = sqlType;
		JPanel pnlNorth = new JPanel(new FlowLayout()) {
			{
				cmbExpr = new JComboBox();
				cmbExpr.setPreferredSize(new Dimension(70, 20));
				cmbExpr.addItem(DB_Manage.EQUAL);
				cmbExpr.addItem(DB_Manage.GT);
				cmbExpr.addItem(DB_Manage.LT);
				cmbExpr.addItem(DB_Manage.GE);
				cmbExpr.addItem(DB_Manage.LE);
				cmbExpr.addItem(DB_Manage.NOT_EQUAL);
				cmbExpr.addItem(DB_Manage.LIKE);
				cmbExpr.addItem(DB_Manage.LIKE_XXXQ);
				cmbExpr.addItem(DB_Manage.LIKE_QXXX);
				cmbExpr.addItem(DB_Manage.IN);
				cmbExpr.addItem(DB_Manage.NOT_IN);
				cmbExpr.addItem(DB_Manage.BETWEEN);
				add(cmbExpr);
				// -----------------------------------------------------------
				txtVal1 = new JTextField("");
				txtVal1.setPreferredSize(new Dimension(200, 20));
				add(txtVal1);
				// -----------------------------------------------------------
				btnSearch = new JButton("Search");
				btnSearch.setPreferredSize(new Dimension(100, 20));
				add(btnSearch);
			}
		};
		JPanel pnlSouth = new JPanel(new FlowLayout()) {
			{
				btnCancel = new JButton("Cancel");
				btnCancel.setPreferredSize(new Dimension(100, 20));
				add(btnCancel);
			}
		};
		resList = new JList();
		resList.setModel(new DefaultListModel());

		// ---------------------------------------------------------------------
		// リスト上でマウスをﾀﾞﾌﾞﾙクリックした場合
		// ---------------------------------------------------------------------
		resList.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Object sVal = ((JList) e.getSource()).getSelectedValue();
					System.out.println("#DoubleClick!!# sVal:" + sVal);
					if (targetObj instanceof JTextField) {
						((JTextField) targetObj).setText(sVal.toString());
						dialog.dispose();
					}
				}
			}
		});
		// --------------------------------------------------------------------
		// Search
		// --------------------------------------------------------------------
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		// --------------------------------------------------------------------
		// Cancel
		// --------------------------------------------------------------------
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		search();
		// -----------------------------------------------------------
		dialog = new JDialog((Frame) null, "", true);
		// dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.setSize(new Dimension(400, 300));
		dialog.setPreferredSize(new Dimension(400, 300));
		dialog.setLocationRelativeTo(null);// センタリング
		Container container = dialog.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(BorderLayout.NORTH, pnlNorth);
		container.add(BorderLayout.CENTER, new JScrollPane(resList));
		container.add(BorderLayout.SOUTH, pnlSouth);
		dialog.setVisible(true);
	}

	private void sql2ListModel(String sql) {
		if (sql.trim().equals(""))
			return;
//		System.out.println("@sql2ListModel sql:" + sql);
		JDBC_GUI jdbc = new JDBC_GUI(jUrl, user, paswd);
		if (jdbc.getConnection() != null) {
			ListModel listModel = jdbc.query2Listmodel(sql);
			jdbc.close();
			resList.setModel(listModel);
		} else {
			System.out.println(jdbc.getEmsg());
			JOptionPane.showMessageDialog(null,
					"SQL_GDV Error!!:" + jdbc.getEmsg());
		}
	}

	private void search() {
		String exp = (String) cmbExpr.getSelectedItem();
		String val1 = txtVal1.getText();
		String val2 = "";
		String sql = createSql(table, field, sqlType, exp, val1, val2);
		sql2ListModel(sql);
	}

	// ------------------------------------------------------------------------
	// sql
	// ------------------------------------------------------------------------
	private String createSql(String table, String field, int sqlType,
			String expr, String val1, String val2) {
		String sql = "";
		if (table == null || table.equals(""))
			return "";
		if (field == null || field.equals(""))
			return "";

		String cond = "";
		if (!val1.equals(""))
			cond = DB_Manage.createCond(field, expr, val1, val2, sqlType);
		if (!cond.equals("")) {
			cond = " WHERE(" + cond + ") ";
		}

		String orderBy = " order by " + field;
		String groupBy = " group by " + field;
		String max = "128";
		if (jUrl.indexOf("sqlite") > 0) {
			sql = "Select " + field + " FROM " + table + cond + groupBy
					+ " limit " + max + orderBy;
		} else {
			sql = "Select top " + max + " " + field + " FROM " + table + cond
					+ groupBy + orderBy;
		}
		return sql;
	}

	public static void main(String[] argv) {
	}
}
