package kyPkg.panelsc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import kyPkg.filter.Flt_Venn;
import kyPkg.panelMini.PnlFile;
import kyPkg.uFile.FileUtil;
import kyPkg.util.ImageCtrl;

//import kyPkg.util.Ruler;
//============================================================================
//雛形！これを元に簡易なＧＵＩを作成する
//============================================================================
public class Pn_Venn extends Pn_Scaffold {

	private static final long serialVersionUID = -3066425890995680687L;
	private static final String EXECUTE = "実行";
	private static final String LINE_BASE = "行単位で比較する。";
	private static final String INNER = "Inner";
	private static final String TRAN = "Tran";
	private static final String MASTER = "Master";
	private JButton btnExec;
	private Image[] images;
	private JToggleButton jTgl_M;
	private JToggleButton jTgl_I;
	private JToggleButton jTgl_T;
	private JComboBox cmbConverter;//出力編集方法
	private int which;
	private PnlFile pnlFile_M;
	private PnlFile pnlFile_T;
	private PnlFile pnlFileO;
	private JCheckBox chkLineCmp;

	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public Pn_Venn() {
		super(540, 440);
		initGUI(); // GUI部作成
	}

	// -------------------------------------------------------------------------
	// create GUI
	// -------------------------------------------------------------------------
	void initGUI() {
		setLayout(null);
		images = new Image[8];
		images[0] = ImageCtrl.getImage("images/venn000.GIF");
		images[1] = ImageCtrl.getImage("images/venn001.GIF");
		images[2] = ImageCtrl.getImage("images/venn010.GIF");
		images[3] = ImageCtrl.getImage("images/venn011.GIF");
		images[4] = ImageCtrl.getImage("images/venn100.GIF");
		images[5] = ImageCtrl.getImage("images/venn101.GIF");
		images[6] = ImageCtrl.getImage("images/venn110.GIF");
		images[7] = ImageCtrl.getImage("images/venn111.GIF");

		int vPos = 40;
		int hPos = 10;
		vPos = 00;
		HashMap hmap1 = new HashMap();
		hmap1.put(PnlFile.CAPTION, MASTER);
		hmap1.put(PnlFile.CAPTION_WIDTH, "40");
		pnlFile_M = new PnlFile(hmap1);
		pnlFile_M.setBounds(hPos, vPos, 500, 20);

		vPos = 20;
		HashMap hmap2 = new HashMap();
		hmap2.put(PnlFile.CAPTION, TRAN);
		hmap2.put(PnlFile.CAPTION_WIDTH, "40");
		pnlFile_T = new PnlFile(hmap2);
		pnlFile_T.setBounds(hPos, vPos, 500, 20);
		// if(listener!=null)
		// pnlFile1.addActionListener(listener);
		add(pnlFile_M);
		add(pnlFile_T);

		//IDのみ出力
		//値を並べて出力

		jTgl_M = new JToggleButton(MASTER);
		jTgl_I = new JToggleButton(INNER);
		jTgl_T = new JToggleButton(TRAN);

		vPos = 310;
		jTgl_M.setBounds(hPos + 0, vPos, 100, 20);
		jTgl_I.setBounds(hPos + 200, vPos, 100, 20);
		jTgl_T.setBounds(hPos + 400, vPos, 100, 20);

		add(jTgl_M);
		add(jTgl_I);
		add(jTgl_T);

		vPos = 350;
		chkLineCmp = new JCheckBox(LINE_BASE);
		chkLineCmp.setOpaque(false);
		chkLineCmp.setBounds(hPos, vPos, 150, 20);
		add(chkLineCmp);

		cmbConverter = new JComboBox();
		cmbConverter.addItem(Flt_Venn.DEFAULT);
		cmbConverter.addItem(Flt_Venn.KEY_ONLY);
		cmbConverter.addItem(Flt_Venn.CONCATINATE);
		hPos += 200;
		cmbConverter.setBounds(hPos, vPos, 150, 20);
		add(cmbConverter);

		vPos = 350 + 30;
		HashMap pmap = new HashMap();
		pmap.put(PnlFile.CAPTION, "out:");
		pmap.put(PnlFile.OPT49ER, "false");
		pmap.put(PnlFile.ENCODE, "false");
		pmap.put(PnlFile.DELIM, "false");
		pmap.put(PnlFile.DEFAULT_PATH, "");
		pmap.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFileO = new PnlFile(pmap);
		hPos = 10;
		pnlFileO.setBounds(hPos, vPos, 350, 20);
		add(pnlFileO);

		btnExec = new JButton(EXECUTE);
		btnExec.setBounds(hPos + 400, vPos, 100, 20);
		add(btnExec);
		// ---------------------------------------------------------------------
		// Button
		// ---------------------------------------------------------------------
		btnExec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						btnExec.setEnabled(false);
						String path_M = pnlFile_M.getPath();
						String path_T = pnlFile_T.getPath();
						String path_O = pnlFileO.getPath();
						System.out.println("outPath:" + path_O);

						String cnvType = (String) cmbConverter.getSelectedItem();

						Flt_Venn venn = new Flt_Venn(path_O, path_M, path_T,which, cnvType);
						if (chkLineCmp.isSelected()) {
							venn.setDelimiter("\n");
						}

						venn.execute();
						// XXX　出力件数を拾いたいが・・・・
						btnExec.setEnabled(true);
						JOptionPane.showMessageDialog((Component) null,
								"Finish!");

					}
				};
				th1.start();
			}
		});

		jTgl_M.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calc();
			}
		});
		jTgl_I.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calc();
			}
		});
		jTgl_T.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calc();
			}
		});
	}

	private void calc() {
		which = 0;
		if (jTgl_M.isSelected())
			which += Flt_Venn.LEFT_ONLY;
		if (jTgl_I.isSelected())
			which += Flt_Venn.INNER_JOIN;
		if (jTgl_T.isSelected())
			which += Flt_Venn.RIGHT_ONLY;
		String comment = "";
		switch (which) {
		case 0:
			comment = "";
			break;
		case 1:
			comment = "RightOnly";
			break;
		case 2:
			comment = "InnerJoin";
			break;
		case 3:
			comment = "Right";
			break;
		case 4:
			comment = "LeftOnly";
			break;
		case 5:
			comment = "XOR";
			break;
		case 6:
			comment = "Left";
			break;
		case 7:
			comment = "FullJoin";
			break;

		default:
			break;
		}
		if (comment.equals("")) {
			pnlFileO.setText("");
		} else {
			String left = pnlFile_M.getPath();
			String right = pnlFile_T.getPath();
			if (!left.equals("") & !right.equals("")) {
				String drive = FileUtil.getParent(left);
				String ext = FileUtil.getExt(left);
				pnlFileO.setText(drive + "/" + comment + "." + ext);
			}
		}
		repaint();
	}

	// -------------------------------------------------------------------------
	// ルーラー表示
	// -------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.WHITE);
		//		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
		//				new Color(250, 187, 244, 128));
		g.drawImage(images[which], 10, 50, this);
		// g.drawImage(images.getScaled(), 200, 100, this);
		// g.drawImage(images.getScaled(), 300, 100, this);
	}

	public static void main(String[] args) {
		System.err.println("args.length...."+args.length);
		if (args.length == 5) {
			String out = args[0];
			String master = args[1];
			String tran = args[2];
			int which = Integer.valueOf(args[3]);
			String cnvType = args[4];
			Flt_Venn ins = new Flt_Venn(out, master, tran, which, cnvType);
			ins.execute();
		} else {
//			System.err.println("usage....");
			standAlone(new Pn_Venn(), "Venn");
		}
	}

}