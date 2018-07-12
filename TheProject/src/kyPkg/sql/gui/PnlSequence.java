package kyPkg.sql.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kyPkg.panelMini.JP_List;

// ############################################################
// 並び順
// ############################################################
public class PnlSequence extends JPanel {
	private DB_Control dbControl;

	public PnlSequence(DB_Control pdbControl) {
		this.dbControl = pdbControl;
		setLayout(new BorderLayout());
		orderFlds = new JP_List();
		add(orderFlds, BorderLayout.CENTER);

		// ---------------------------------------------------------------------
		// orderOption
		// ---------------------------------------------------------------------
		JPanel orderPanle = new JPanel(new FlowLayout(FlowLayout.RIGHT)) {
			{
				chkOrder = new JCheckBox("Ascending Order", false);
				chkOrder.setPreferredSize(new Dimension(200, 20));
				add(chkOrder);
			}
		};
		add(orderPanle, BorderLayout.SOUTH);
		// #####################################################################
		// パネルその３ 並び順
		// 並び順と出力項目の同期がとれていないとエラーになる
		// 案１ 出力項目に該当項目を追加する
		// 案２ 出力項目の変化に応じて、並び順対象項目を変更する・・・
		// 対象グループとすでに選択されている項目・・・
		// ---------------------------------------------------------------------
		orderFlds.jList_L.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				dbControl.genSQL_X();
			}
		});
		orderFlds.jList_R.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				dbControl.genSQL_X();
			}
		});

	}

	public JP_List orderFlds;
	public JCheckBox chkOrder;

}
