package kyPkg.sql.gui;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kyPkg.etc.Ez_Layout;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;
import kyPkg.util.DnDAdapter;

public class ImportPnl extends JPanel {
	public InfDBHandler dbHandler;
	private JTextField txfPath;
	private JTextField txfTableName;
	private JComboBox cmbDelimiter;
	private JCheckBox chkHeader;
	private JButton btnImport;
	private JButton btnEzLayout;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// kyPkg.util.Ruler.drawRuler(g, this.getSize().width,
		// this.getSize().height, new Color(128, 128, 255));
	}

	// #####################################################################
	// 《Import》 Import
	// #####################################################################
	public ImportPnl(InfDBHandler pDbHandler, LayoutManager layout) {
		super(layout);
		this.dbHandler = pDbHandler;

		txfPath = new JTextField("data.csv");
		// ファイルドラッグドロップの仕掛け
		new java.awt.dnd.DropTarget(txfPath, new DnDAdapter(txfPath) {
			// オーバーライドしている
			@Override
			public void setTargetText(String path) {
				txfPath.setText(path);
				String firstName = FileUtil.getFirstName(path);
				txfTableName.setText(firstName);
				System.out.println("#DBG# firstName:" + firstName);

				File49ers insF49 = new File49ers(path);
				String encoding = insF49.getEncoding();
				String delimiter = insF49.getDelimiter();

				System.out.println("#DBG# encoding:" + encoding);
				System.out.println("#DBG# delimiter:" + delimiter);

				String name = DelimiterRes.getName(delimiter);
				System.out.println("#DBG# name:" + name);
				cmbDelimiter.setSelectedItem(name);// XXX なかったらどうするんだ？
			}
		});

		int top = 10;

		JLabel lab1 = new JLabel("DataPath:");
		lab1.setBounds(10, top, 100, 20);
		add(lab1);

		txfPath.setBounds(100, 10, 400, 20);
		add(txfPath);

		chkHeader = new JCheckBox("Option Header", false);// 先頭行をヘッダーとする
		chkHeader.setOpaque(false);
		chkHeader.setBounds(550, top, 200, 20);
		add(chkHeader);

		top += 30;
		JLabel lab2 = new JLabel("TableName:");
		lab2.setBounds(10, top, 100, 20);
		add(lab2);

		txfTableName = new JTextField("TableName");
		txfTableName.setBounds(100, top, 200, 20);
		add(txfTableName);

		JLabel lab3 = new JLabel("Delimiter:");
		lab3.setBounds(550, top, 100, 20);
		add(lab3);

		cmbDelimiter = new JComboBox();
		cmbDelimiter.setBounds(650, top, 100, 20);
		for (String element : DelimiterRes.getNameSet()) {
			cmbDelimiter.addItem(element);
		}
		add(cmbDelimiter, null);

		top += 30;
		btnImport = new JButton("Import It");
		btnImport.setBounds(550, top, 200, 30);
		add(btnImport);

		top += 30;
		btnEzLayout = new JButton("EzLayout");
		btnEzLayout.setBounds(550, top, 200, 30);
		add(btnEzLayout);

		// -----------------------------------------------------------------
		// createSample ボタン
		// -----------------------------------------------------------------
		top += 40;
		JButton brnSample = new JButton("Create Sample");// Sampleテーブルの作成
		brnSample.setBounds(550, top, 200, 30);
		add(brnSample);
		// ---------------------------------------------------------------------
		// createSample ボタン
		// ---------------------------------------------------------------------
		brnSample.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dbHandler.createSample();
			}
		});
		// --------------------------------------------------------------------------
		// ezLayout
		// --------------------------------------------------------------------------
		btnEzLayout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final String path = txfPath.getText();
				String res = Ez_Layout.goAhead(path);
				System.out.println("res=>" + res);
			}
		});

		// --------------------------------------------------------------------------
		// btnImport テキストファイルをテーブルにインポートする
		// --------------------------------------------------------------------------
		btnImport.addActionListener(new ActionListener() {
			private String delimiter;

			@Override
			public void actionPerformed(ActionEvent ae) {
				final String path = txfPath.getText();
				final String table = txfTableName.getText();
				final boolean optHeader = chkHeader.isSelected();

				delimiter = DelimiterRes.getDlm((String) cmbDelimiter
						.getSelectedItem());
				btnImport.setEnabled(false);
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							dbHandler.importFromText(path, table, delimiter,
									optHeader);
						} catch (Exception e) {
							e.printStackTrace();
						}
						btnImport.setEnabled(true);
					}
				};
				thread.start();
			}
		});
		// --------------------------------------------------------------------------
		// ACTION《05 Data Import》◆ Import ◆
		// リターンしない限りイベントは発生しないのであった・・ 課題ですね・・トホホ
		// --------------------------------------------------------------------------
		txfPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String wPath = txfPath.getText();

				// jTfImpFをウオッチして・・・jTfImpAsを自動入力させたい
				int pos1 = wPath.lastIndexOf(System
						.getProperty("file.separator")) + 1;
				int pos2 = wPath.indexOf(".");
				if (pos1 < pos2) {
					txfTableName.setText(wPath.substring(pos1, pos2));
				}
			}
		});
	}
}
