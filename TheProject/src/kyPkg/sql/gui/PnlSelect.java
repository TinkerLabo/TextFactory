package kyPkg.sql.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kyPkg.panelMini.JP_List;

//############################################################
//select:出力項目
//############################################################

public class PnlSelect extends JPanel {
	private DB_Control dbControl;
	public JComboBox cmbTable_X;

	public JP_List selectFlds_x;
	public JCheckBox chkGroup_X;
	public JCheckBox chkCntOpt_X;

	public JComboBox getCmbTable_X() {
		return cmbTable_X;
	}

	public boolean getGroupOpt_X() {
		return chkGroup_X.isSelected();
	}

	public boolean getCntOpt_X() {
		return chkCntOpt_X.isSelected();
	}

	public void selectFlds_x_setList(List<String> columnNames) {
		selectFlds_x.setList(columnNames, null); // Selectフィールド用
	}

	public String getTable() {
		return ((String) cmbTable_X.getSelectedItem()); // 対象テーブル
	}

	public List<String> getFields() {
		return selectFlds_x.getList(); // 出力対象フィールド
	}

	public PnlSelect(DB_Control db_control) {
		this.dbControl = db_control;
		setLayout(new BorderLayout());
		// ---------------------------------------------------------------------
		// Table
		// ---------------------------------------------------------------------
		JPanel selPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
			{
				cmbTable_X = new JComboBox();
				cmbTable_X.setPreferredSize(new Dimension(400, 20));

				chkCntOpt_X = new JCheckBox("number of cases", false);// 該当件数を表示
				chkGroup_X = new JCheckBox("Group", false);
				add(cmbTable_X);

				add(chkGroup_X);
				add(chkCntOpt_X);
			}
		};
		add(selPanel, BorderLayout.NORTH);
		// -----------------------------------------------------------------
		// Fields list
		// -----------------------------------------------------------------
		selectFlds_x = new JP_List();
		add(selectFlds_x, BorderLayout.CENTER);
		// -----------------------------------------------------------------
		// Action
		// -----------------------------------------------------------------
		// ---------------------------------------------------------------------
		// テーブル(コンボボックス) を 変更
		// ---------------------------------------------------------------------
		cmbTable_X.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.changeTable_X();
			}
		});
		// ---------------------------------------------------------------------
		// Group
		// ---------------------------------------------------------------------
		chkGroup_X.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dbControl.genSQL_X();
			}
		});
		// ---------------------------------------------------------------------
		// 該当件数を表示
		// ---------------------------------------------------------------------
		chkCntOpt_X.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dbControl.genSQL_X();
			}
		});
		// ---------------------------------------------------------------------
		selectFlds_x.jList_L
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						dbControl.genSQL_X();
					}
				});
		// ---------------------------------------------------------------------
		selectFlds_x.jList_R
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						dbControl.genSQL_X();
					}
				});

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
