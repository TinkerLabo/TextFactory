package kyPkg.sql.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import kyPkg.gridPanel.Grid_Panel;

public class DB_Grid extends Grid_Panel {
	private JComboBox cmbExt;
	private DB_Control pnlLogin;
	private JLabel lblInfo;
	private JButton btnWrite;
	private JButton btnRun;
	private JButton btnExe;
	private JButton btnReset;

	public DB_Grid(DB_Control xpnlLogin) {
		super();
		this.pnlLogin = xpnlLogin;
		// -------------------------------------------------------------------------
		// Grid
		// -------------------------------------------------------------------------
		JPanel tblCtrlp = new JPanel(new FlowLayout()) {
			{
				setPreferredSize(new Dimension(520, 30));

				lblInfo = new JLabel(""); // info
				lblInfo.setPreferredSize(new Dimension(350, 20));
				// lblInfo.setOpaque(true);
				// lblInfo.setBackground(Color.white);
				add(lblInfo);

				btnReset = new JButton("Reset"); // テーブルのリセット
				btnReset.setPreferredSize(new Dimension(80, 20));
				add(btnReset);

				// JButton btnSaveAsT = new JButton("Save As Table");
				// btnSaveAsT.setPreferredSize(new Dimension(120, 20));
				// add(btnSaveAsT);
				btnWrite = new JButton("Save As CSV");
				btnWrite.setPreferredSize(new Dimension(120, 20));
				add(btnWrite);
				
				btnExe = new JButton("Execute");
				// btnExe.setPreferredSize(new Dimension(80, 20));
				// add(btnExe);
				btnRun = new JButton("Run");
				btnRun.setPreferredSize(new Dimension(120, 20));
				add(btnRun);
				// --------------------------------------------------------------------------
				// Exe Button
				// --------------------------------------------------------------------------
				btnExe.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						reset();
						lblInfo.setText("Wait... ");
						btnEnable(false);

						repaint();
						Thread thread = new Thread() {
							@Override
							public void run() {

								kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse(
										"Elapse Test", '=');
								elapse.start();
								boolean stat = pnlLogin.getDBHandler()
										.queryExUpdate();
								String elapstr = elapse.stop();
								if (stat) {
									lblInfo.setText("Finished " + elapstr);
									JOptionPane.showMessageDialog(null,
											new JTextArea("Finished!!\n"
													+ elapstr), "OK",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									lblInfo.setText("NG " + elapstr);
								}
								btnEnable(true);
							}
						};
						thread.start();

					}
				});
				// --------------------------------------------------------------------------
				// Run Button
				// --------------------------------------------------------------------------
				btnRun.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						reset();
						lblInfo.setText("Wait... ");
						btnEnable(false);

						Thread thread = new Thread() {
							@Override
							public void run() {
								kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse(
										"Elapse Test", '=');
								elapse.start();
								PnlConnect dbHandler = (PnlConnect) pnlLogin
										.getDBHandler();
								String sql = dbHandler.getSql();

								JDBC_GUI insJDBC = dbHandler.getInsJDBC();
								boolean stat = getGrid().setData(insJDBC.query2MappedList(sql));

								String elapstr = elapse.stop();
								if (stat) {
									lblInfo.setText("Finished " + elapstr);
								} else {
									lblInfo.setText("NG " + elapstr);
								}
								btnEnable(true);

							}
						};
						thread.start();

					}
				});
				// --------------------------------------------------------------------------
				// Reset Button
				// --------------------------------------------------------------------------
				btnReset.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						reset();
					}
				});
				// --------------------------------------------------------------------------
				// WriteIt Button
				// --------------------------------------------------------------------------
				btnWrite.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						btnEnable(false);
						String wPath = "";
						JFileChooser fc = new JFileChooser(".");
						if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
							wPath = fc.getSelectedFile().toString();
						} else {
							return;
						}
						if (wPath.indexOf(".") == 0) {
							String wExt = DelimiterRes.getExt((String) cmbExt
									.getSelectedItem());
							wPath = wPath + wExt;
						}
						getGrid().saveAs(wPath);
						btnEnable(true);
					}
				});
			}
		};
		this.add("South", tblCtrlp);
	}

	public void btnEnable(boolean b) {
		btnWrite.setEnabled(b);
		btnRun.setEnabled(b);
		btnExe.setEnabled(b);
		btnReset.setEnabled(b);
	}

	public void reset() {
	}
}
