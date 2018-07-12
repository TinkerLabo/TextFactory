package kyPkg.sql.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

// ############################################################
// Cross:クロス集計
// ############################################################

public class PnlCross extends JPanel {
	private DB_Control dbControl;
	public JComboBox cmbTable_L;
	public JComboBox cmbKey_L;
	public JComboBox cmbKey_R;
	public JCheckBox chkCntOpt_Mup;

	public PnlCross(DB_Control pdbControl) {
		this.dbControl = pdbControl;
		setLayout((new BorderLayout()));
		// ---------------------------------------------------------------------
		// Table
		// ---------------------------------------------------------------------
		JPanel selPanel = new JPanel() {
			{
				GridBagLayout gb = new GridBagLayout();
				this.setLayout(gb);
				int wide = 10;
				int narrow = 1;
				GridBagConstraints gc = new GridBagConstraints();

				gc.weightx = wide;
				gc.weighty = 1;
				gc.gridwidth = 1;
				gc.gridheight = 1;
				// ---------------------------------------------------------------------
				gc.gridheight = 1;
				// ---------------------------------------------------------------------
				gc.weightx = narrow;
				JLabel jLabT_D = new JLabel("                  ");
				jLabT_D.setPreferredSize(new Dimension(120, 20));
				gc.gridx = 0;
				gc.gridy = 0;
				gb.setConstraints(jLabT_D, gc);
				this.add(jLabT_D);
				// ---------------------------------------------------------------------
				gc.weightx = narrow;
				JLabel jLabT_L = new JLabel("table:");
				gc.gridx = 1;
				gc.gridy = 0;
				gb.setConstraints(jLabT_L, gc);
				this.add(jLabT_L);
				// ---------------------------------------------------------------------
				JLabel jLabK_L = new JLabel("side:");
				gc.gridy = 1;
				gb.setConstraints(jLabK_L, gc);
				this.add(jLabK_L);
				// ---------------------------------------------------------------------
				gc.weightx = wide;
				cmbTable_L = new JComboBox();
				cmbTable_L.setPreferredSize(new Dimension(200, 20));
				gc.gridx = 2;
				gc.gridy = 0;
				gb.setConstraints(cmbTable_L, gc);
				this.add(cmbTable_L);
				// ---------------------------------------------------------------------
				cmbKey_L = new JComboBox();
				cmbKey_L.setPreferredSize(new Dimension(200, 20));
				gc.gridy = 1;
				gb.setConstraints(cmbKey_L, gc);
				this.add(cmbKey_L);
				// ---------------------------------------------------------------------
				gc.gridheight = 1;
				// ---------------------------------------------------------------------
				JLabel jLabK_R = new JLabel("head:");
				gc.gridx = 3;
				gc.gridy = 0;
				gb.setConstraints(jLabK_R, gc);
				this.add(jLabK_R);
				// ---------------------------------------------------------------------
				JLabel jLabK_S = new JLabel("Sum:");
				gc.gridy = 1;
				gb.setConstraints(jLabK_S, gc);
				this.add(jLabK_S);
				// ---------------------------------------------------------------------
				cmbKey_R = new JComboBox();
				cmbKey_R.setPreferredSize(new Dimension(200, 20));
				gc.gridx = 4;
				gc.gridy = 0;
				gb.setConstraints(cmbKey_R, gc);
				this.add(cmbKey_R);
				// ---------------------------------------------------------------------
				chkCntOpt_Mup = new JCheckBox("number of cases", false);// 該当件数を表示
				gc.gridx = 5;
				gc.gridy = 1;
				gb.setConstraints(chkCntOpt_Mup, gc);
				this.add(chkCntOpt_Mup);
			}
		};
		add(selPanel, BorderLayout.CENTER);

		// -----------------------------------------------------------------
		// Action
		// -----------------------------------------------------------------
		cmbTable_L.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.changeTable_Crs();
			}
		});
		cmbKey_L.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.genSQL_Crs();
			}
		});
		cmbKey_R.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.genSQL_Crs();
			}
		});
		// ---------------------------------------------------------------------
		chkCntOpt_Mup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.genSQL_Crs();
			}
		});
	}

	public JComboBox getCmbTable_L() {
		return cmbTable_L;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
