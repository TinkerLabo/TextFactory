package kyPkg.gridPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import kyPkg.mySwing.ListPanelW;
import kyPkg.panel.JP_Ancestor;

public class LimiterPanel extends JP_Ancestor {
	private Grid_Panel gridPanel;
	private static final String APPLY = "������K�p����B";
	// XXX ���K�\���ɂ��I�肪�ł���ƕ֗����i��F�R���r�j���ꊇ�I���ł���Ƃ��j
	private ListPanelW listPanel;
	private JButton btnLimit;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	LimiterPanel(List<String> uqList, Grid_Panel gridpanel) {
		// XXX ���K�\���ɂ�鍀�ڂ̑I���Ȃ�
		//		super(new BorderLayout());
		this.gridPanel = gridpanel;

		listPanel = new ListPanelW(450, 30, 400, 100, true, 200);
		listPanel.setRightClear(true);
		listPanel.setListData(uqList);
		add(listPanel, BorderLayout.CENTER);

		btnLimit = new JButton(APPLY);
		btnLimit.setPreferredSize(new Dimension(100, 30));
		add(btnLimit, BorderLayout.SOUTH);

		btnLimit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAction();
			}
		});
	}

	private void doAction() {
		List<String> list = listPanel.getList();
		Grid grid = gridPanel.getGrid();
		Set set = gridPanel.getLimiter().get(grid.getCurrentCol());
		if (set == null)
			set = new HashSet();
		for (String elemnt : list) {
			set.add(elemnt);
		}
		//�O���b�h�̃��~�b�^�[�ɏ�����ǉ����Ă���
		gridPanel.getLimiter().put(grid.getCurrentCol(), set);
		// XXX ���ɑI�΂�Ă���l�����X�g�̉E���ɕ\���ł��Ȃ����E�E��������
		// XXX �S�̂̌���󋵂�������@��p�ӂ���
		gridPanel.extractIt(gridPanel.getLimiter());
		// XXX daialog����������
		super.closeDialog();
	}

}
