package kyPkg.sql.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import kyPkg.panelMini.JP_MashUp;

// ############################################################
// Mashup:データ合成
// ############################################################

public class PnlMashUp extends JPanel {
	private DB_Control dbControl;
	public JComboBox cmbTable_L;
	public JComboBox cmbKey_L;
	public JComboBox cmbRelation;
	public JComboBox cmbTable_R;
	public JComboBox cmbKey_R;
	public JP_MashUp selFlds_Mup;
	public JCheckBox chkCntOpt_Mup;

	public PnlMashUp(DB_Control pdbControl) {
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
				// gc.fill = GridBagConstraints.BOTH;
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
				JLabel jLabK_L = new JLabel("key:");
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
				HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put("LEFT", "LEFT.png");
				hmap.put("INNER", "INNER.png");
				hmap.put("RIGHT", "RIGHT.png");
				List<String> keyList = new ArrayList();// 表示順
				keyList.add("INNER");
				keyList.add("LEFT");
				keyList.add("RIGHT");
				cmbRelation = new kyPkg.sql.gui.JComboBoxIconMod(hmap, keyList);
				cmbRelation.setPreferredSize(new Dimension(230, 50));
				gc.gridx = 3;
				gc.gridy = 0;
				gc.gridheight = 2;
				gb.setConstraints(cmbRelation, gc);
				this.add(cmbRelation);
				// ---------------------------------------------------------------------
				gc.gridheight = 1;
				// ---------------------------------------------------------------------
				gc.weightx = narrow;
				JLabel jLabT_R = new JLabel("table:");
				gc.gridx = 4;
				gc.gridy = 0;
				gb.setConstraints(jLabT_R, gc);
				this.add(jLabT_R);
				// ---------------------------------------------------------------------
				JLabel jLabK_R = new JLabel("key:");
				gc.gridy = 1;
				gb.setConstraints(jLabK_R, gc);
				this.add(jLabK_R);
				// ---------------------------------------------------------------------
				gc.weightx = wide;
				cmbTable_R = new JComboBox();
				cmbTable_R.setPreferredSize(new Dimension(200, 20));
				gc.gridx = 5;
				gc.gridy = 0;
				gb.setConstraints(cmbTable_R, gc);
				this.add(cmbTable_R);
				// ---------------------------------------------------------------------
				cmbKey_R = new JComboBox();
				cmbKey_R.setPreferredSize(new Dimension(200, 20));
				gc.gridy = 1;
				gb.setConstraints(cmbKey_R, gc);
				this.add(cmbKey_R);
				// ---------------------------------------------------------------------
				chkCntOpt_Mup = new JCheckBox("number of cases", false);// 該当件数を表示
				gc.gridx = 6;
				gc.gridy = 1;
				gb.setConstraints(chkCntOpt_Mup, gc);
				this.add(chkCntOpt_Mup);
			}
		};
		add(selPanel, BorderLayout.NORTH);
		// -----------------------------------------------------------------
		// Fields list
		// -----------------------------------------------------------------
		selFlds_Mup = new JP_MashUp();
		add(selFlds_Mup, BorderLayout.CENTER);

		// -----------------------------------------------------------------
		// Action
		// -----------------------------------------------------------------
		cmbTable_L.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.changeTable_Mup();
			}
		});
		// ---------------------------------------------------------------------
		cmbTable_R.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.changeTable_Mup();
			}
		});
		cmbKey_L.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.genSQL_Mup();
			}
		});
		cmbKey_R.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.genSQL_Mup();
			}
		});
		// ---------------------------------------------------------------------
		cmbRelation.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.genSQL_Mup();
			}
		});
		// ---------------------------------------------------------------------
		chkCntOpt_Mup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dbControl.genSQL_Mup();
			}
		});
		// ---------------------------------------------------------------------
		selFlds_Mup.jList_L
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						dbControl.genSQL_Mup();
					}
				});
		selFlds_Mup.jList_R
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						dbControl.genSQL_Mup();
					}
				});
		// ---------------------------------------------------------------------
		selFlds_Mup.jList_C
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						dbControl.genSQL_Mup();
					}
				});
	}

	public JComboBox getCmbTable_L() {
		return cmbTable_L;
	}

	public JComboBox getCmbTable_R() {
		return cmbTable_R;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
