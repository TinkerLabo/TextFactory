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
	private static final String APPLY = "条件を適用する。";
	// XXX 正規表現による選定ができると便利だ（例：コンビニが一括選択できるとか）
	private ListPanelW listPanel;
	private JButton btnLimit;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	LimiterPanel(List<String> uqList, Grid_Panel gridpanel) {
		// XXX 正規表現による項目の選択など
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
		//グリッドのリミッターに条件を追加している
		gridPanel.getLimiter().put(grid.getCurrentCol(), set);
		// XXX 既に選ばれている値をリストの右側に表示できないか・・検討する
		// XXX 全体の限定状況を見る方法を用意する
		gridPanel.extractIt(gridPanel.getLimiter());
		// XXX daialogを消したい
		super.closeDialog();
	}

}
