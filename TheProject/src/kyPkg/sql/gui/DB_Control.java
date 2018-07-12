package kyPkg.sql.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class DB_Control extends DB_Manage {
	private static final String CONNECT = "Connect";
	private static final String SELECT = "Select";
	private static final String ORDERING = "Ordering";
	private static final String GROUPING = "Grouping";
	private static final String CONDITION = "Condition";
	private static final String MASH_UP = "MashUp";
	private static final String IMPORT = "Import";
	private static final String SQL = "SQL";
	private static final String CROSS = "Cross";
	// -------------------------------------------------------------------------
	private PnlConnect pnlConnect;
	// -------------------------------------------------------------------------
	private JTabbedPane tabPane;
	private PnlSQL pnlSql;
	private PnlSelect pnlSelect;
	private PnlMashUp pnlMashUp;
	private PnlCross pnlCross;
	private PnlSequence pnlSequence;
	private PnlGroup pnlGroup;
	private HashMap<String, Integer> typeMap;
	private PnlCondition pnlCondition;

	public InfDBHandler getDBHandler() {
		return pnlConnect;
	}

	public int getSqlType(String fieldName) {
		return typeMap.get(fieldName);
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public DB_Control() {
		super();
		tabPane = this;
		pnlSql = new PnlSQL();
		pnlSelect = new PnlSelect(this);
		pnlSequence = new PnlSequence(this);
		pnlGroup = new PnlGroup(this);
		pnlCondition = new PnlCondition(this);
		pnlMashUp = new PnlMashUp(this);
		pnlCross = new PnlCross(this);
		// ---------------------------------------------------------------------
		List<JComboBox> cmbList = new ArrayList();
		cmbList.add(pnlSelect.getCmbTable_X());
		cmbList.add(pnlMashUp.getCmbTable_L());
		cmbList.add(pnlMashUp.getCmbTable_R());
		cmbList.add(pnlCross.getCmbTable_L());
		// ---------------------------------------------------------------------
		pnlConnect = new PnlConnect(pnlSql, tabPane, cmbList);
		// ---------------------------------------------------------------------
		addTab(CONNECT, pnlConnect); // 接続
		addTab(SELECT, pnlSelect); // 出力項目
		addTab(ORDERING, pnlSequence); // 並び順
		addTab(GROUPING, pnlGroup); // グループ化
		addTab(CONDITION, pnlCondition);// 抽出条件
		addTab(MASH_UP, pnlMashUp); // データ合成
		addTab(IMPORT, new ImportPnl(pnlConnect, null));
		addTab(SQL, pnlSql);
		addTab(CROSS, pnlCross);// クロス集計
		// ---------------------------------------------------------------------
		pnlConnect.connect(tabPane);
		System.out.println("# DB_Control # after connect");
		genSQL_X();
		System.out.println("# DB_Control # end");
		this.setSelectedIndex(0);
		// ---------------------------------------------------------------------
		// ＳＱＬの雛形を変更
		// ---------------------------------------------------------------------
		pnlSql.cmbTemplateAddActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				genSQL_TMP();
			}
		});
		// --------------------------------------------------------------------------
		// 《 Execute 》 SQL文を実行する
		// --------------------------------------------------------------------------
		pnlSql.btnQueryExeAddActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pnlConnect.queryExUpdate();
			}
		});
	}

	public void valueDialog(JTextField targetObj, String field, int sqlType) {
		String table = (String) (pnlSelect.getTable());
		String jUrl = pnlConnect.getJurl();
		String user = pnlConnect.getUser();
		String paswd = pnlConnect.getPass();
		new SQL_ValueDlg(targetObj, jUrl, user, paswd, table, field, sqlType);
	}

	public void changeTable_X() {
		// 抽出条件などをクリアする
		// リソースに埋め込まれているそれぞれのフィールドリストを書き換える
		// XXX グループ計算リストをクリアしておく
		String table = (String) (pnlSelect.getTable());
		pnlSql.setSql("select * from " + table);
		// -----------------------------------------------------
		JDBC_GUI jDbc = pnlConnect.getJDBC_GUI();
		if (jDbc.getConnection() != null) {
			// -------------------------------------------------
			typeMap = jDbc.getTypeMap(table);
			List<String> columnNames = jDbc.getColumnNames(table);
			// -------------------------------------------------
			pnlSelect.selectFlds_x_setList(columnNames); // Selectフィールド用
			pnlSequence.orderFlds.setList(columnNames, null); // 並 び 順 用
			pnlCondition.cmbFld1.removeAllItems();
			// pnlCondition.cmbFld2.removeAllItems();
			pnlGroup.cmbNumFld.removeAllItems();
			pnlGroup.dLmGrp.removeAllElements();
			for (String colName : columnNames) {
				pnlCondition.cmbFld1.addItem(colName);
				// pnlCondition.cmbFld2.addItem(colName);
				if (isNumType(typeMap.get(colName)))
					pnlGroup.cmbNumFld.addItem(colName);// Numeric
			}
			jDbc.close();
			repaint();
		} else {
			System.out.println(jDbc.getEmsg());
			JOptionPane.showMessageDialog(null,
					"LoginP Error!:" + jDbc.getEmsg());
		}
		genSQL_X();
		// private JP_MashUp selFlds_Mup;
	}

	private void genSQL_TMP() {

		String table = ((String) pnlSelect.getTable()); // 対象テーブル
		List<String> fields = pnlSelect.getFields(); // 出力対象フィールド
		String column = "column";
		if (fields != null && fields.size() > 0) {
			StringBuffer bufColumn = new StringBuffer();
			for (String field : fields) {
				if (bufColumn.length() > 0) {
					bufColumn.append(",");
				}
				bufColumn.append(field);
			}
			column = bufColumn.toString();
		}

		// String template = pnlSql.getTemplateType(); // ＳＱＬ雛形の種類
		String sqltmp = pnlSql.getTemplate(table, column);

		System.out.println("### genSQL_TMP#" + sqltmp);
		pnlSql.setSql(sqltmp);
		repaint();
	}

	//FIXME まとめたい・・・
	public void genSQL_X() {
		// System.out.println("#generateSQL#");
		final String OR = " or ";
		String table = ((String) pnlSelect.getTable()); // 対象テーブル
		List fields = pnlSelect.getFields(); // 出力対象フィールド
		List orders = pnlSequence.orderFlds.getList(); // 並び順指定フィールド
		// ---------------------------------------------------------------------
		// 抽出条件1
		// ---------------------------------------------------------------------
		String where1 = "";
		for (int i = 0; i < pnlCondition.model1.getSize(); i++) {
			if (where1.equals("") == false) {
				where1 = where1 + OR;
			}
			where1 = where1 + (String) pnlCondition.model1.getElementAt(i);
		}
		// ---------------------------------------------------------------------
		// 抽出条件2
		// ---------------------------------------------------------------------
		String where2 = "";
		for (int i = 0; i < pnlCondition.model2.getSize(); i++) {
			if (where2.equals("") == false) {
				where2 = where2 + OR;
			}
			where2 = where2 + (String) pnlCondition.model2.getElementAt(i);
		}
		// ---------------------------------------------------------------------
		// グループ
		// ---------------------------------------------------------------------
		List<String> grpCalcs = new ArrayList();
		if (pnlSelect.getGroupOpt_X() && pnlGroup.dLmGrp.getSize() == 0) {
		} else {
			for (int i = 0; i < pnlGroup.dLmGrp.getSize(); i++) {
				grpCalcs.add(grpCalc((String) pnlGroup.dLmGrp.getElementAt(i),
						':'));
			}
		}
		boolean countOpt = pnlSelect.getCntOpt_X();
		boolean groupOpt = pnlSelect.getGroupOpt_X();
		String template = pnlSql.getTemplateType();// ＳＱＬ雛形の種類

		String sql = genSql01(template, table, fields, orders, where1, where2,
				grpCalcs, countOpt, groupOpt);
		pnlSql.setSql(sql);
		repaint();
	}

	public void changeTable_Mup() {
		// フィールドリストを書き換える
		// XXX 抽出条件などをクリアする
		// XXX グループ計算リストをクリアしておく
		String table_L = (String) (pnlMashUp.cmbTable_L.getSelectedItem());
		String table_R = (String) (pnlMashUp.cmbTable_R.getSelectedItem());
		// -----------------------------------------------------
		JDBC_GUI jDbc = pnlConnect.getJDBC_GUI();
		if (jDbc.getConnection() != null) {
			List<String> columnNames_L = jDbc.getColumnNames(table_L);
			List<String> columnNames_R = jDbc.getColumnNames(table_R);
			jDbc.close();
			pnlMashUp.selFlds_Mup.setList(columnNames_L, columnNames_R); // Selectフィールド用
			pnlMashUp.cmbKey_L.removeAllItems();
			if (columnNames_L != null) {
				for (String colName : columnNames_L) {
					pnlMashUp.cmbKey_L.addItem(colName);
				}
			}
			pnlMashUp.cmbKey_R.removeAllItems();
			if (columnNames_R != null) {
				for (String colName : columnNames_R) {
					pnlMashUp.cmbKey_R.addItem(colName);
				}
			}
			repaint();
		} else {
			System.out.println(jDbc.getEmsg());
			JOptionPane.showMessageDialog(null,
					"LoginP Error!:" + jDbc.getEmsg());
		}
		genSQL_Mup();
	}

	public void genSQL_Mup() {
		// System.out.println("#generateSQL<2>#");
		// final String OR = " or ";
		String table_L = (String) (pnlMashUp.cmbTable_L.getSelectedItem());
		String table_R = (String) (pnlMashUp.cmbTable_R.getSelectedItem());
		String key_L = (String) (pnlMashUp.cmbKey_L.getSelectedItem());
		String key_R = (String) (pnlMashUp.cmbKey_R.getSelectedItem());
		List fields = pnlMashUp.selFlds_Mup.getList(); // 出力対象フィールド
		List orders = pnlSequence.orderFlds.getList(); // 並び順指定フィールド
		String relation = pnlMashUp.cmbRelation.getSelectedItem().toString();
		// ---------------------------------------------------------------------
		// 抽出条件1
		// ---------------------------------------------------------------------
		String where1 = "";
		// ---------------------------------------------------------------------
		// 抽出条件2
		// ---------------------------------------------------------------------
		String where2 = "";
		// ---------------------------------------------------------------------
		// // グループ
		// ---------------------------------------------------------------------
		List<String> grpCalcs = new ArrayList();
		boolean countOpt = pnlMashUp.chkCntOpt_Mup.isSelected();
		boolean groupOpt = false;// pnlMashUp.chkGroup_X.isSelected();
		String sql = sql_Mup(table_L, table_R, relation, key_L, key_R, fields,
				orders, where1, where2, grpCalcs, countOpt, groupOpt);
		pnlSql.setSql(sql);
		repaint();
	}

	public void changeTable_Crs() {
		// フィールドリストを書き換える
		// XXX 抽出条件などをクリアする
		// XXX グループ計算リストをクリアしておく
		String table_L = (String) (pnlCross.cmbTable_L.getSelectedItem());
		// -----------------------------------------------------
		JDBC_GUI jDbc = pnlConnect.getJDBC_GUI();
		if (jDbc.getConnection() != null) {
			List<String> columnNames_L = jDbc.getColumnNames(table_L);
			jDbc.close();
			pnlCross.cmbKey_L.removeAllItems();
			pnlCross.cmbKey_R.removeAllItems();
			if (columnNames_L != null) {
				for (String colName : columnNames_L) {
					// XXX 文字フィールドのみに限定したほうが良いだろうか？？
					pnlCross.cmbKey_L.addItem(colName);
					pnlCross.cmbKey_R.addItem(colName);
				}
			}
			repaint();
		} else {
			System.out.println(jDbc.getEmsg());
			JOptionPane.showMessageDialog(null,
					"LoginP Error!:" + jDbc.getEmsg());
		}
		genSQL_Crs();
	}

	public void genSQL_Crs() {
		// System.out.println("#genSQL_Crs#");
		String table_L = (String) (pnlCross.cmbTable_L.getSelectedItem());
		String key_L = (String) (pnlCross.cmbKey_L.getSelectedItem());
		String key_R = (String) (pnlCross.cmbKey_R.getSelectedItem());
		String sql = sql_Crs(table_L, key_L, key_R);
		pnlSql.setSql(sql);
		repaint();
	}

}
