package kyPkg.gridPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kyPkg.globals.GlobalCtrl;
import kyPkg.util.Ruler;

class OptionPanel extends JPanel {
	public static final String LEGACY_THEME = "LegacyTheme";
	public static final String STANDARD_THEME = "StandardTheme";
	public static final String DARKNESS_THEME = "DarknessTheme";
	//------------------------------------------------------------
	public static final String CALQ100 = "calq100";
	public static final String EXPERIMENTAL = "Experimental";
	public static final String THEME = "Theme";
	public static final String DISPLAY_3D = "3D";
	private JComboBox cmbTheme;
	private JCheckBox chk3d;
	private JCheckBox chk100;
	private JCheckBox chkExp;

	OptionPanel() {
		super(null);
		List<String> themeList = new ArrayList();
		themeList.add(DARKNESS_THEME);
		themeList.add(STANDARD_THEME);
		themeList.add(LEGACY_THEME);
		int y = 50;
		int x = 100;
		// ------------------------------------------------------------
		JLabel labTeme = new JLabel("Theme:");
		labTeme.setBounds(x, y, 200, 20);
		add(labTeme);
		cmbTheme = new JComboBox();
		cmbTheme.setBounds(x+100, y, 200, 20);
		add(cmbTheme);
		for (String element : themeList) {
			cmbTheme.addItem(element);
		}
		String themeName = GlobalCtrl.getValue(THEME, STANDARD_THEME);
		cmbTheme.setSelectedItem(themeName);

		// ------------------------------------------------------------
		y += 50;
		chk3d = new JCheckBox("3D表示");
		chk3d.setOpaque(false);
		chk3d.setBounds(x+100, y, 200, 20);
		if (GlobalCtrl.isNotBlank(DISPLAY_3D))
			chk3d.setSelected(true);
		add(chk3d);
		// ------------------------------------------------------------
		y += 50;
		chk100 = new JCheckBox("100人あたりの数値を表示する");
		chk100.setOpaque(false);
		chk100.setBounds(x+100, y, 200, 20);
		if (GlobalCtrl.isNotBlank(CALQ100))
			chk100.setSelected(true);
		add(chk100);
		// ------------------------------------------------------------
		y += 50;
		chkExp = new JCheckBox("EXPERIMENTAL");
		chkExp.setOpaque(false);
		chkExp.setBounds(x+100, y, 200, 20);
		if (GlobalCtrl.isNotBlank(EXPERIMENTAL))
			chkExp.setSelected(true);
		add(chkExp);
		// ########################################
		chk3d.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GlobalCtrl.put(DISPLAY_3D, chk3d.isSelected());
			}
		});
		chk100.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GlobalCtrl.put(CALQ100, chk100.isSelected());
			}
		});
		chkExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GlobalCtrl.put(EXPERIMENTAL, chkExp.isSelected());
			}
		});
		cmbTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GlobalCtrl.put(THEME, (String) cmbTheme.getSelectedItem());
			}
		});
	}

	// ------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		// if (wkImg!=null) g.drawImage(wkImg,0,0,this);
		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
				new Color(155, 187, 244));
	}

}
