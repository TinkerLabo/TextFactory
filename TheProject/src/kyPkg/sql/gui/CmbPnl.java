package kyPkg.sql.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import kyPkg.util.Ruler;

public class CmbPnl extends JPanel {
	private JComboBox cmbRez;
	public InfDBHandler dbHandler;

	public CmbPnl(InfDBHandler pDbHandler, LayoutManager layout) {
		super(layout);
		this.dbHandler = pDbHandler;
		
		cmbRez = new JComboBox();
		cmbRez.setBounds(10, 10, 600, 20);
		cmbRez.setPreferredSize(new Dimension(500, 20));
		add(cmbRez, null);

		JButton btnQuery2Cmb = new JButton("Combo ");
		btnQuery2Cmb.setPreferredSize(new Dimension(80, 20));
		btnQuery2Cmb.setBounds(10, 40, 80, 20);
		add(btnQuery2Cmb);
		// --------------------------------------------------------------------------
		// ACTIONÅs01 Sql to ComboÅtÅü sqlÇÃåãâ ÇCombobox Åü
		// --------------------------------------------------------------------------
		btnQuery2Cmb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBoxModel wDmdl = dbHandler.query2Cmodel();
				cmbRez.setModel(wDmdl);
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
				new Color(255, 128, 128));
	}

}
