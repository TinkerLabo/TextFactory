package kyPkg.sql.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//############################################################
//グループ
//############################################################
public class PnlGroup extends JPanel {
	private DB_Control dbControl;
	public JComboBox cmbNumFld;
	private JComboBox cmbSig;
	public DefaultListModel dLmGrp;
	private JList jLstCalc;
	private JButton btnApply;
	private JButton btnClear;

	public PnlGroup(DB_Control pdbControl) {
		this.dbControl = pdbControl;
		setLayout(new BorderLayout());
		this.add(pnlGroupNorth(), BorderLayout.NORTH);

		dLmGrp = new DefaultListModel();
		dLmGrp.removeAllElements();
		jLstCalc = new JList();
		jLstCalc.setModel(dLmGrp);
		this.add(new JScrollPane(jLstCalc), BorderLayout.CENTER);

		// ---------------------------------------------------------------------
		// グループ計算値 ダブルクリック時
		// ---------------------------------------------------------------------
		jLstCalc.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int sIdx = ((JList) e.getSource()).getSelectedIndex();
					dLmGrp.removeElementAt(sIdx);
					dbControl.genSQL_X();
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	private JPanel pnlGroupNorth() {
		return new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Ruler.drawRuler(g, this.getSize().width,
				// this.getSize().height,
				// new Color(255, 128, 128));
			}

			// インスタンス初期化子
			{
				setLayout(null);
				setPreferredSize(new Dimension(800, 40));
				// -----------------------------------------------------------------
				// チェックボックス
				// -----------------------------------------------------------------
				int top = 10;
				cmbNumFld = new JComboBox();
				cmbNumFld.setPreferredSize(new Dimension(200, 20));
				cmbNumFld.setBounds(10, top, 200, 20);
				add(cmbNumFld);
				// -----------------------------------------------------------------
				// ◆ グループ化式のコンボ
				// -----------------------------------------------------------------
				cmbSig = new JComboBox();
				cmbSig.setPreferredSize(new Dimension(100, 20));
				cmbSig.addItem(DB_Manage.SUM);// "合計"
				cmbSig.addItem(DB_Manage.MIN);// "最小"
				cmbSig.addItem(DB_Manage.MAX);// "最大"
				cmbSig.addItem(DB_Manage.AVG);// "平均"
				cmbSig.addItem(DB_Manage.STDEV);// "標準偏差"
				cmbSig.addItem(DB_Manage.VARP);// "分散"
				cmbSig.addItem(DB_Manage.COUNT);// "カウント"
				cmbSig.setBounds(220, top, 100, 20);
				add(cmbSig);
				// -----------------------------------------------------------------
				btnApply = new JButton("Apply");
				btnApply.setPreferredSize(new Dimension(100, 20));
				btnApply.setBounds(350, top, 100, 20);
				add(btnApply);
				// -----------------------------------------------------------------
				btnClear = new JButton("Clear");
				btnClear.setPreferredSize(new Dimension(100, 20));
				btnClear.setBounds(350+100, top, 100, 20);
				add(btnClear);
				// -----------------------------------------------------------------
				// Action
				// -----------------------------------------------------------------
				btnApply.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String wNum = (String) cmbNumFld.getSelectedItem();
						String wFnc = (String) cmbSig.getSelectedItem();
						if (wNum.equals("") == false) {
							String wGFld = wNum + ":" + wFnc;
							DB_Manage.addUq2Model(dLmGrp, wGFld);
						}
						dbControl.genSQL_X();
					}
				});
				// -----------------------------------------------------------------
				// Clear
				// -----------------------------------------------------------------
				btnClear.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dLmGrp.clear();
						dbControl.genSQL_X();
					}
				});

			}
		};
	}
}
