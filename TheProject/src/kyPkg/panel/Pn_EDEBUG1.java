package kyPkg.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kyPkg.filter.EdebugX;
import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.TableMouseListener;
import kyPkg.gridPanel.Grid_Panel;
import kyPkg.panelMini.PnlFile;
import kyPkg.panelsc.BorderPanel;
import kyPkg.uFile.File2Matrix;

public class Pn_EDEBUG1 extends BorderPanel {
	private static final long serialVersionUID = -5613170556136966301L;

	private PnlFile pnlFile_P;

	private PnlFile pnlFile_T;

	private Grid_Panel table_P;

	private Grid_Panel table_T;

	private JTextField txtCol_P;

	private JTextField txtCol_T;

	private JButton btnKick;

	private JCheckBox checkBox[];

	private JTextArea textArea;

	public static void main(String[] argv) {
		standAlone();
	}

	public static void standAlone() {
		JFrame frame = new JFrame();
		frame.setBounds(50, 50, 600, 350);
		frame.getContentPane().add(new Pn_EDEBUG1());
		frame.setVisible(true);
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Pn_EDEBUG1() {
		super();
		this.setOpaque(false);
		createGUI();
	}

	// -------------------------------------------------------------------------
	// GUI部作成 部品間の連携をとっていかなければなるまい
	// -------------------------------------------------------------------------
	void createGUI() {
		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, "master");
		pmap1.put(PnlFile.OPT49ER, "true");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, true);
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile_P = new PnlFile(pmap1);

		HashMap pmap2 = new HashMap();
		pmap2.put(PnlFile.CAPTION, "data");
		pmap2.put(PnlFile.OPT49ER, "true");
		pmap2.put(PnlFile.ENCODE, "false");
		pmap2.put(PnlFile.DELIM, "true");
		pmap2.put(PnlFile.DEFAULT_PATH, "");
		pmap2.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile_T = new PnlFile(pmap2);

		
//		pnlFile_P = new PnlFile("master", true, false, true, "", -1);
//		pnlFile_T = new PnlFile("data  ", true, false, true, "", -1);
		table_P = new Grid_Panel(new DefaultTableModelMod());
		table_T = new Grid_Panel(new DefaultTableModelMod());
		txtCol_P = new JTextField("0");
		txtCol_T = new JTextField("0");

		JPanel panelCtrl = new JPanel(new FlowLayout());
		btnKick = new JButton("実行");
		btnKick.setPreferredSize(new Dimension(150, 20));
		panelCtrl.add(btnKick);

		JPanel panelP = new JPanel(new BorderLayout());
		panelP.add(pnlFile_P, BorderLayout.NORTH);
		panelP.add(table_P, BorderLayout.CENTER);
		panelP.add(txtCol_P, BorderLayout.SOUTH);

		JPanel panelT = new JPanel(new BorderLayout());
		panelT.add(pnlFile_T, BorderLayout.NORTH);
		panelT.add(table_T, BorderLayout.CENTER);
		panelT.add(txtCol_T, BorderLayout.SOUTH);

		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				panelP, panelT);
		splitPane2.setDividerLocation(100);

		JPanel panelOpt = new JPanel();
		panelOpt.setLayout(new BoxLayout(panelOpt, BoxLayout.Y_AXIS));

		panelOpt.setPreferredSize(new Dimension(500, 100));
		panelOpt.add(new JLabel("  マスターとデータの任意のカラムのマッチングを行います"));
		checkBox = new JCheckBox[2];
		checkBox[0] = new JCheckBox("マッチレコードを拡張子.posをつけて出力");
		checkBox[0].setSelected(true);
		checkBox[1] = new JCheckBox("アンマッチレコードを拡張子.negをつけて出力");
		for (int n = 0; n < 2; n++) {
			checkBox[n].setBounds(0, (n * 20 + 5), 200, 20);
			checkBox[n].setOpaque(false);
			panelOpt.add(checkBox[n]);
		}
		// JScrollPane sc = new JScrollPane();
		textArea = new JTextArea();
		// textArea.setPreferredSize(new Dimension(500, 100));
		// panelOpt.add(textArea);

		JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				panelOpt, new JScrollPane(textArea));
		splitPane1.setDividerLocation(300);

		pnlC.add(splitPane2);
		pnlN.add(splitPane1);
		pnlS.add(panelCtrl);
		// ---------------------------------------------------------------------
		// ACTION
		// ---------------------------------------------------------------------
		pnlFile_P.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						String path = pnlFile_P.getPath();
						String delimiter = pnlFile_P.getDelimiter();
						int limit = 64;
						Vector wVec = File2Matrix.extract(path, delimiter,
								limit);
						table_P.setDefModel(new DefaultTableModelMod(wVec));
						table_P.autoFit();
					}
				};
				th1.start();
			}
		});
		// ---------------------------------------------------------------------
		pnlFile_T.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						String path = pnlFile_T.getPath();
						String delimiter = pnlFile_T.getDelimiter();
						int limit = 64;
						Vector wVec = File2Matrix.extract(path, delimiter,
								limit);
						table_T.setDefModel(new DefaultTableModelMod(wVec));
						table_T.autoFit();
					}
				};
				th1.start();
			}
		});

		// ---------------------------------------------------------------------
		// btnKickボタン
		// ---------------------------------------------------------------------
		btnKick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnKick.setEnabled(false);
				textArea.setText("start");
				String path_T = pnlFile_T.getPath();
				String path_P = pnlFile_P.getPath();
				// String delim_T = pnlFile_T.getDelimiter();
				// String delim_P = pnlFile_P.getDelimiter();
				int opt = 0;
				if (checkBox[0].isSelected())
					opt += 1;
				if (checkBox[1].isSelected())
					opt += 2;
				int col_T = Integer.parseInt(txtCol_T.getText());
				int col_P = Integer.parseInt(txtCol_P.getText());
				EdebugX ins = new EdebugX(col_T, path_P, col_P);
				ins.setWriteOption(opt);
				ins.execute(path_T);

				textArea.setText(ins.getInfo());
				btnKick.setEnabled(true);
			}
		});
		// ----------------------------------------------------------------------
		// TablePanel
		// ----------------------------------------------------------------------
		table_P.setTableMouseListener(new TableMouseListener() {
			// -------------------------------------------------------------------------
			// ヘッド列クリック
			// -------------------------------------------------------------------------
			@Override
			public void headClickHandle(int columnIndex) {
				txtCol_P.setText(Integer.toString(columnIndex));
			}

			// -------------------------------------------------------------------------
			// マウスボタンを押した
			// -------------------------------------------------------------------------
			@Override
			public void mousePressedHandle(MouseEvent evt, int row, int col) {
				System.out.println("mousePressed @ 行:" + row + " 列:" + col);
				txtCol_P.setText(Integer.toString(col));
			}
		});
		// ----------------------------------------------------------------------
		// TablePanel
		// ----------------------------------------------------------------------
		table_T.setTableMouseListener(new TableMouseListener() {
			// -------------------------------------------------------------------------
			// ヘッド列クリック
			// -------------------------------------------------------------------------
			@Override
			public void headClickHandle(int columnIndex) {
				txtCol_T.setText(Integer.toString(columnIndex));
			}

			// -------------------------------------------------------------------------
			// マウスボタンを押した
			// -------------------------------------------------------------------------
			@Override
			public void mousePressedHandle(MouseEvent evt, int row, int col) {
				System.out.println("mousePressed @ 行:" + row + " 列:" + col);
				txtCol_T.setText(Integer.toString(col));
			}
		});

	}
}
