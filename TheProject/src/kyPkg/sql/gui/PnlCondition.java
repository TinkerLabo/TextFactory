package kyPkg.sql.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

//#####################################################################
//抽出条件
//#####################################################################
public class PnlCondition extends JPanel {
	private DB_Control dbControl;
	private JTabbedPane tabPane;
	private JList lstCnd1 = new JList();
	private JList lstCnd2 = new JList();
	private JButton btnOpt2;
	private JButton btnOpt1;
	private JButton btnResC;
	private JButton btnApply;
	public DefaultListModel model1;
	public DefaultListModel model2;
	public JComboBox cmbFld1;
	private JComboBox cmbExpr;
	private JTextField txtVal1;
	private JTextField txtVal2;
	private JCheckBox chkOrCond;

	public PnlCondition(DB_Control pdbControl) {
		this.dbControl = pdbControl;
		setLayout(new BorderLayout());
		// -----------------------------------------------------------------
		// リストボックス ｘ ２
		// -----------------------------------------------------------------
		JScrollPane jScCnd1 = new JScrollPane(lstCnd1);
		JScrollPane jScCnd2 = new JScrollPane(lstCnd2);
		model1 = new DefaultListModel();
		model2 = new DefaultListModel();
		model1.removeAllElements();
		model2.removeAllElements();
		lstCnd1.setModel(model1);
		lstCnd2.setModel(model2);
		tabPane = new JTabbedPane();
		tabPane.setBounds(10, 50, 400, 90);
		tabPane.addTab("Condition1", jScCnd1);
		tabPane.addTab("Condition2", jScCnd2);
		tabPane.setTabPlacement(JTabbedPane.TOP); // 横にしたいがカッコ悪い
		tabPane.setSelectedIndex(0); // 選択されているタブ ※０から始まる
		JPanel pnlConCtrl = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
			// JPanel pnlConCtrl = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Ruler.drawRuler(g, this.getSize().width,
				// this.getSize().height, new Color(255, 128, 128));
			}

			// インスタンス初期化子
			{
				setLayout(null);
				setPreferredSize(new Dimension(800, 50));
				int top = 5;
				int left = 10;
				JLabel jLabV0 = new JLabel("Field1:");
				jLabV0.setBounds(left, top, 60, 20);
				add(jLabV0);
				left += 60;
				// -----------------------------------------------------------------
				cmbFld1 = new JComboBox();
				cmbFld1.setBounds(left, top, 200, 20);
				add(cmbFld1);
				left += 200 + 10;
				// -----------------------------------------------------------------
				cmbExpr = new JComboBox();
				cmbExpr.addItem(DB_Manage.EQUAL);
				cmbExpr.addItem(DB_Manage.GT);
				cmbExpr.addItem(DB_Manage.LT);
				cmbExpr.addItem(DB_Manage.GE);
				cmbExpr.addItem(DB_Manage.LE);
				cmbExpr.addItem(DB_Manage.NOT_EQUAL);
				cmbExpr.addItem(DB_Manage.LIKE);
				cmbExpr.addItem(DB_Manage.LIKE_XXXQ);
				cmbExpr.addItem(DB_Manage.LIKE_QXXX);
				cmbExpr.addItem(DB_Manage.BETWEEN);
				cmbExpr.addItem(DB_Manage.IN);
				cmbExpr.addItem(DB_Manage.NOT_IN);
				cmbExpr.setBounds(left, top, 100, 20);
				add(cmbExpr);
				left += 100 + 10;
				// -----------------------------------------------------------------
				left = 400;
				JLabel jLabV1 = new JLabel("value1:");
				jLabV1.setBounds(left, top, 50, 20);
				add(jLabV1);
				left += 50;
				// -----------------------------------------------------------------
				txtVal1 = new JTextField("");
				txtVal1.setBounds(left, top, 180, 20);
				add(txtVal1);
				left += 180;
				// -----------------------------------------------------------------
				btnOpt1 = new JButton("...");
				btnOpt1.setBounds(left, top, 20, 20);
				add(btnOpt1);
				left += 20 + 10;
				// -----------------------------------------------------------------
				left = 700;

				btnApply = new JButton("Apply");
				btnApply.setBounds(left, top, 100, 20);
				add(btnApply);
				left += 100;
				// -----------------------------------------------------------------
				btnResC = new JButton("Reset");
				btnResC.setBounds(left, top, 100, 20);
				add(btnResC, null);
				left += 100;
				// -----------------------------------------------------------------
				top += 20;
				left = 70;
				// -----------------------------------------------------------------
				// JCheckBox jCkFld = new JCheckBox("Field2:");
				// jCkFld.setOpaque(false);
				// jCkFld.setBounds(left, top, 70, 20);
				// add(jCkFld);
				// left += 70;
				// -----------------------------------------------------------------
				// cmbFld2 = new JComboBox();
				// cmbFld2.setEditable(true);
				// cmbFld2.setBounds(left, top, 200, 20);
				// add(cmbFld2);
				// left += 200 + 10;
				// -----------------------------------------------------------------
				left = 400;
				JLabel jLabV2 = new JLabel("value2:");
				jLabV2.setBounds(left, top, 50, 20);
				add(jLabV2);
				left += 50;
				// -----------------------------------------------------------------
				txtVal2 = new JTextField("");
				txtVal2.setBounds(left, top, 180, 20);
				add(txtVal2);
				left += 180;
				// -----------------------------------------------------------------
				btnOpt2 = new JButton("...");
				btnOpt2.setBounds(left, top, 20, 20);
				add(btnOpt2);
				left += 20 + 10;
				// -----------------------------------------------------------------
				left = 700;
				chkOrCond = new JCheckBox("Or Conditions");
				chkOrCond.setOpaque(false);
				chkOrCond.setBounds(left, top, 200, 20);
				add(chkOrCond);

				// #####################################################################
				// パネルその５ 抽出条件
				// ---------------------------------------------------------------------
				// 対象フィールド変更時
				// ---------------------------------------------------------------------
				cmbFld1.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent event) {
						txtVal1.setText("");
						txtVal2.setText("");
					}
				});
			}

		};
		add(pnlConCtrl, BorderLayout.NORTH);
		add(tabPane, BorderLayout.CENTER);
		// ---------------------------------------------------------------------
		// 条件リセット ボタン
		// ---------------------------------------------------------------------
		btnResC.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// cmbFld2.setSelectedItem("");
				txtVal1.setText("");
				txtVal2.setText("");
				model1.removeAllElements();
				model2.removeAllElements();
				dbControl.genSQL_X();
			}
		});
		// ---------------------------------------------------------------------
		// 抽出条件 適用"Apply"ボタン押下時
		// ---------------------------------------------------------------------
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String field = (String) cmbFld1.getSelectedItem(); // フィールド名
				String expr = (String) cmbExpr.getSelectedItem(); // 比較表現式
				String val1 = txtVal1.getText();
				String val2 = txtVal2.getText();
				int sqlType = dbControl.getSqlType(field);

				String cond = DB_Manage.createCond(field, expr, val1, val2,
						sqlType);

				if (!cond.equals("")) {
					if (tabPane.getSelectedIndex() == 0) { // 選択されているタブ
						DB_Manage.addUq2Model(model1, cond);
					} else {
						DB_Manage.addUq2Model(model2, cond);
					}
				}
				dbControl.genSQL_X();

			}
		});
		// ---------------------------------------------------------------------
		// 抽出条件リスト１ ダブルクリック時
		// ---------------------------------------------------------------------
		lstCnd1.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int sIdx = ((JList) e.getSource()).getSelectedIndex();
					model1.removeElementAt(sIdx);
					dbControl.genSQL_X();
				}
			}
		});
		// ---------------------------------------------------------------------
		// 抽出条件リスト２ ダブルクリック時
		// ---------------------------------------------------------------------
		lstCnd2.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int sIdx = ((JList) e.getSource()).getSelectedIndex();
					model2.removeElementAt(sIdx);
					dbControl.genSQL_X();
				}
			}
		});
		// ---------------------------------------------------------------------
		// 条件設定の値 １＆２設定用
		// SQL_GVDlgクラスを作り存在する値を表示する
		// ---------------------------------------------------------------------
		btnOpt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String field = (String) cmbFld1.getSelectedItem();
				int sqlType = dbControl.getSqlType(field);
				dbControl.valueDialog(txtVal1, field, sqlType);
			}
		});
		// ---------------------------------------------------------------------
		btnOpt2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String field = (String) cmbFld1.getSelectedItem();
				int sqlType = dbControl.getSqlType(field);
				dbControl.valueDialog(txtVal2, field, sqlType);
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
