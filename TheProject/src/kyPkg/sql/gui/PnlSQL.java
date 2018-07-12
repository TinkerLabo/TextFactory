package kyPkg.sql.gui;

//import static kyPkg.sql.gui.PnlSQL.CREATE_INDEX;
//import static kyPkg.sql.gui.PnlSQL.SELECT_ALL;
//import static kyPkg.sql.gui.PnlSQL.SELECT_COUNT;
//import static kyPkg.sql.gui.PnlSQL.SELECT_GROUP_COUNT;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//#####################################################################
//SQL表示
//#####################################################################
import javax.swing.JTextField;

public class PnlSQL extends JPanel {
	private static final String SELECT_ALL = "SELECT *";
	private static final String SELECT_COUNT = "SELECT count(*)";
	private static final String SELECT_GROUP_COUNT = "SELECT column,count(*)";
	private static final String SELECT_100 = "SELECT 100";
	private static final String SELECT_SUBSTR = "SELECT SUBSTR";
	private static final String SELECT_SUBSTR_COUNT = "SELECT SUBSTR count(*)";
	private static final String SELECT = "SELECT";
	private static final String INSERT = "INSERT";
	private static final String DELETE = "DELETE";
	private static final String TRUNCATE = "TRUNCATE";
	private static final String UPDATE = "UPDATE";
	// ---------------------------------------------------------------------
	private static final String CREATE_BACKUP = "CREATE_BACKUP";
	private static final String INSERT_INTO = "INSERT INTO";
//	private static final String CREATE_TABLE = "CREATE TABLE";
	private static final String CREATE_INDEX = "CREATE INDEX";
	// ---------------------------------------------------------------------
//	private static final String TEXT_T_TO_TEXT = "@TEXT_T toText";
//	private static final String TEXT_T_DROP = "@TEXT_T drop";
//	private static final String TEXT_T_SET = "@TEXT_T Set";
//	private static final String TEXT_T_CREATE = "@TEXT_T Create";
//	private static final String SHUTDOWN = "SHUTDOWN";
//	private static final String SET = "SET";
	// ---------------------------------------------------------------------
	private static final String DROP_INDEX = "DROP INDEX";
	private static final String DROP_TABLE = "DROP TABLE";
	private JButton btnQueryExe;
	private JComboBox cmbTemplate;

	public JTextField txtCol1;
	public JTextField txtCol2;

	// FIXME　privateに変えたい・・・　Pnlselectと機能を統合する
	public String getTemplateType() {
		return ((String) cmbTemplate.getSelectedItem()); // ＳＱＬ雛形の種類
	}

	public String getTemplate(String table, String column) {
		String col1 = txtCol1.getText();
		String col2 = txtCol2.getText();
		String template = getTemplateType(); // ＳＱＬ雛形の種類

		String substrColumn = "SUBSTRING( column ," + col1 + "," + col2 + ")";

		System.out.println("### genSQL_TMP# template:" + template);
		String sqltmp = "Sorry not defined.... @PnlSQL";
		if (template.equals("")) {
		} else if (template.equals(SELECT_ALL)) {
			sqltmp = "SELECT * FROM table;";
		} else if (template.equals(SELECT_COUNT)) {
			sqltmp = "SELECT count(*) FROM table;";
		} else if (template.equals(SELECT_GROUP_COUNT)) {
			sqltmp = "SELECT column,count(*) FROM table GROUP BY column ORDER BY column;";
		} else if (template.equals(SELECT_100)) {
			sqltmp = "SELECT TOP 100 * FROM table;";
		} else if (template.equals(SELECT)) {
			sqltmp = "SELECT column FROM table;";
		} else if (template.equals(SELECT_SUBSTR)) {
			sqltmp = "SELECT DISTINCT SUBSTRING FROM table ORDER BY SUBSTRING;";
		} else if (template.equals(SELECT_SUBSTR_COUNT)) {
			sqltmp = "SELECT SUBSTRING,count(*) FROM table GROUP BY SUBSTRING ORDER BY SUBSTRING;";
		} else if (template.equals(INSERT)) {
			sqltmp = "INSERT INTO table (column) Values(values);";
		} else if (template.equals(DELETE)) {
			sqltmp = "DELETE FROM table WHERE( column = values );";
		} else if (template.equals(TRUNCATE)) {
			sqltmp = "TRUNCATE TABLE table;";
		} else if (template.equals(UPDATE)) {
			sqltmp = "UPDATE table SET column = value WHERE( column = values );";
		} else if (template.equals(CREATE_BACKUP)) {
			sqltmp = "SELECT * INTO table_backup FROM table";
		} else if (template.equals(INSERT_INTO)) {
			sqltmp = "INSERT INTO target_table select column FROM table";
		} else if (template.equals(CREATE_INDEX)) {
			sqltmp = "CREATE [UNIQUE] INDEX index ON  table (column [, ...]) ";
		}
		sqltmp = sqltmp.replaceAll("SUBSTRING", substrColumn);
		sqltmp = sqltmp.replaceAll("table", table);
		sqltmp = sqltmp.replaceAll("column", column);
		sqltmp = sqltmp.replaceAll("values", column);

		return sqltmp;
	}

	// ---------------------------------------------------------------------
	// ＳＱＬの雛形を変更
	// ---------------------------------------------------------------------
	public void cmbTemplateAddActionListener(ActionListener action) {
		cmbTemplate.addActionListener(action);
	}

	// --------------------------------------------------------------------------
	// 《 Execute 》 SQL文を実行する
	// --------------------------------------------------------------------------
	public void btnQueryExeAddActionListener(ActionListener action) {
		btnQueryExe.addActionListener(action);
	}

	private JTextArea txaSQL;

	public String getSql() {
		return txaSQL.getText();
	}

	public void setSql(String sql) {
		txaSQL.setText(sql);
	}

	{
		setLayout(new BorderLayout());
		txaSQL = new JTextArea("");// sp_help 接続するｄｂによって異なるかも・・・
		txaSQL.setColumns(20);
		txaSQL.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(txaSQL);
		// scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add("Center", scroll);

		JPanel exeQueryPnl = new JPanel(new FlowLayout()) {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Ruler.drawRuler(g, this.getSize().width,
				// this.getSize().height, new Color(255, 128, 128));
			}

			// インスタンス初期化子
			protected List<String> sqlTmpl = new ArrayList();
			{
				setPreferredSize(new Dimension(520, 30));

				sqlTmpl = new ArrayList();
				sqlTmpl.add(SELECT_ALL);
				sqlTmpl.add(SELECT_COUNT);
				sqlTmpl.add(SELECT_GROUP_COUNT);
				sqlTmpl.add(SELECT_100);
				sqlTmpl.add(SELECT);
				sqlTmpl.add(SELECT_SUBSTR);
				sqlTmpl.add(SELECT_SUBSTR_COUNT);

				sqlTmpl.add(INSERT);
				sqlTmpl.add(DELETE);
				sqlTmpl.add(TRUNCATE);
				sqlTmpl.add(UPDATE);

				sqlTmpl.add(CREATE_BACKUP);
				sqlTmpl.add(INSERT_INTO);

				sqlTmpl.add(CREATE_INDEX);
				sqlTmpl.add(DROP_TABLE);
				sqlTmpl.add(DROP_INDEX);

				// sqlTmpl.add(TEXT_T_CREATE);
				// sqlTmpl.add(TEXT_T_SET);
				// sqlTmpl.add(TEXT_T_DROP);
				// sqlTmpl.add(TEXT_T_TO_TEXT);

				// -----------------------------------------------------------------
				// SQL Templates
				// -----------------------------------------------------------------
				JLabel lvTemp = new JLabel("Template:");
				lvTemp.setPreferredSize(new Dimension(60, 20));
				add(lvTemp);
				cmbTemplate = new JComboBox();
				for (String element : sqlTmpl) {
					cmbTemplate.addItem(element);
				}
				cmbTemplate.setPreferredSize(new Dimension(150, 20));

				txtCol1 = new JTextField("1");
				txtCol1.setPreferredSize(new Dimension(50, 20));

				txtCol2 = new JTextField("1");
				txtCol2.setPreferredSize(new Dimension(50, 20));

				btnQueryExe = new JButton("Execute");
				btnQueryExe.setPreferredSize(new Dimension(120, 20));
				JButton btnSaveSQL = new JButton("Save as SQL");
				btnSaveSQL.setPreferredSize(new Dimension(120, 20));

				add(cmbTemplate);
				add(txtCol1);
				add(txtCol2);
				add(btnQueryExe);
				add(btnSaveSQL);
				// //
				// ---------------------------------------------------------------------
				// // ＳＱＬの雛形を変更
				// //
				// ---------------------------------------------------------------------
				// cmbTemplate
				// .addActionListener(new java.awt.event.ActionListener() {
				// public void actionPerformed(ActionEvent e) {
				// // genSQL_TMP();
				//
				// }
				// });
				// //
				// --------------------------------------------------------------------------
				// // 《 Execute 》 SQL文を実行する
				// //
				// --------------------------------------------------------------------------
				// btnQueryExe.addActionListener(new ActionListener() {
				// public void actionPerformed(ActionEvent e) {
				// // pnlConnect.queryExUpdate();
				// }
				// });
				// --------------------------------------------------------------------------
				// SQL文を保存する
				// --------------------------------------------------------------------------
				btnSaveSQL.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					}
				});
			}
		};
		add("South", exeQueryPnl);
	}
}
