package kyPkg.panelsc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import kyPkg.panelMini.PnlFile;
import kyPkg.uFile.FileUtil;

public class RWPanel extends BorderPanel implements Inf_Context {
	//	こんな風に複数のオプションの組み合わせパラメータを一つの数字で表現する Op2 = COPY +　CUT + WRITEIT　+　TEXAREA;

	public static final int PASTE = 1;
	public static final int CLEAR = 2;
	public static final int COPY = 4;
	public static final int CUT = 8;
	public static final int WRITEIT = 16;
	public static final int READDATA = 32;
	public static final int TEXAREA = 64;

	private static final long serialVersionUID = 1L;
	protected List<PnlFile> listFpanel = new ArrayList();
	private JButton btn_V; // Paste
	private JButton btn_R; // Clear
	private JButton btn_C; // Copy
	private JButton btn_X; // Cut
	private JButton btnWrite;
	private JTextArea texArea;

	@Override
	public void append(String str) {
		texArea.append(str + "\n");
	}

	@Override
	public void set(String str) {
		texArea.setText("");
		texArea.append(str + "\n");
	}

	@Override
	public void clear() {
		texArea.setText("");
	}

	public void modText(String text) {
		String pre = texArea.getText();
		texArea.setText(pre + "\n" + text);
	}

	public void setText(String text) {
		texArea.setText(text);
	}

	public String getText() {
		return texArea.getText();
	}

	public RWPanel(int option) {
		super();
		boolean[] flag = kyPkg.util.KUtil.int2flags(option, 8);
		this.pnlS.setLayout(new FlowLayout());
		texArea = new JTextArea("");

		JScrollPane scrollP = new JScrollPane(texArea);
		scrollP.setBounds(0, 260, 600, 140);

		JPanel optPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel optPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		if (flag[0]) {
			btn_V = new JButton("Paste");
			btn_V.setBounds(0, 200, 100, 20);
			optPanel1.add(btn_V);
			btn_V.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					texArea.selectAll();
					texArea.paste();
				}
			});
		}
		if (flag[1]) {
			btn_R = new JButton("Clear");
			btn_R.setBounds(100, 200, 100, 20);
			optPanel1.add(btn_R);
			btn_R.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					texArea.selectAll();
					texArea.setText("");
				}
			});
		}
		if (flag[2]) {
			btn_C = new JButton("Copy");
			btn_C.setBounds(100, 400, 100, 20);
			btn_C.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					texArea.selectAll();
					texArea.copy();
				}
			});
			optPanel2.add(btn_C);
		}
		if (flag[3]) {
			btn_X = new JButton("Cut");
			btn_X.setBounds(200, 400, 100, 20);
			btn_X.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					texArea.selectAll();
					texArea.cut();
				}
			});
			optPanel2.add(btn_X);
		}
		if (flag[4]) {
			btnWrite = new JButton("Write It");
			btnWrite.setBounds(0, 400, 100, 20);
			btnWrite.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(".");
					if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
						return;
					String wPath = fc.getSelectedFile().toString();
					if (new File(wPath).exists()) {
						int result = JOptionPane.showConfirmDialog(
								(Component) null, "すでに同名のファイルが存在します、上書きしますか？",
								"確認!", JOptionPane.YES_NO_OPTION);
						if (result == JOptionPane.NO_OPTION)
							return;
					}
					if (wPath.indexOf(".") == 0) {
						wPath = wPath.trim() + ".txt";
					}
					FileUtil.string2file_(wPath, texArea.getText());
				}
			});
			optPanel2.add(btnWrite);
		}
		if (flag[5]) {
			//			ActionListener listener = new ActionListener() {
			//				@Override
			//				public void actionPerformed(ActionEvent e) {
			//					Thread th1 = new Thread() {
			//						@Override
			//						public void run() {
			//							String path = getPath(0);
			//							setPath(0, path);
			//							setText(FileUtil.file2String(path));
			//						}
			//					};
			//					th1.start();
			//				}
			//			};
			ActionListener listener = null;
			addFPanel("Read Data", listener, true, "");
		}
		if (flag[6]) {
			this.pnlC.add(scrollP);
		}
		this.pnlS.add(optPanel1);
		this.pnlS.add(optPanel2);
	}

	public void addFPanel(String caption, ActionListener listener,
			boolean optDelim, String defaultPath) {
		
		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, caption);
		pmap1.put(PnlFile.OPT49ER, "false");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, String.valueOf(optDelim));///だいじょうぶか後で調べる　20170201
		pmap1.put(PnlFile.DEFAULT_PATH, defaultPath);
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		PnlFile pnlFile1 = new PnlFile(pmap1);
//		PnlFile pnlFile1 = new PnlFile(caption, false, false, optDelim,defaultPath, -1);

		if (listener != null)
			pnlFile1.setActionListener(listener);
		listFpanel.add(pnlFile1);
		this.pnlN.add(pnlFile1);
	}

	public String getPath(int index) {
		// XXX outofRange?
		PnlFile filePanel = listFpanel.get(index);
		return filePanel.getPath();
	}
	@SuppressWarnings("unused")
	private void setPath(int index, String path) {
		// XXX outofRange?
		PnlFile filePanel = listFpanel.get(index);
		filePanel.setText(path);
	}

	public void setDefaultPath(int index, String path) {
		// XXX outofRange?
		PnlFile filePanel = listFpanel.get(0);
		filePanel.setText(path);
	}

}
