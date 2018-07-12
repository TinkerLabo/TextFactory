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
// ���я�
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
		// �p�l�����̂R ���я�
		// ���я��Əo�͍��ڂ̓������Ƃ�Ă��Ȃ��ƃG���[�ɂȂ�
		// �ĂP �o�͍��ڂɊY�����ڂ�ǉ�����
		// �ĂQ �o�͍��ڂ̕ω��ɉ����āA���я��Ώۍ��ڂ�ύX����E�E�E
		// �ΏۃO���[�v�Ƃ��łɑI������Ă��鍀�ځE�E�E
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
