package kyPkg.panelMini;

import kyPkg.socks.Ftp;
import kyPkg.uFile.HashMapUtil;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class PnlFtpConnect extends JPanel {
	private Ftp ftpObj = null;
	private static final long serialVersionUID = 34094798084018518L;
	private String status = "Disconnected";
	private String osType;
	private String wrkDir;
	private HashMap<String, String> gHtbl;
	private JTextField user; // user
	private JPasswordField pass; // pass
	private JComboBox host; // 接続先IP
	private JToggleButton jBtnCnn;
	private JToggleButton jBtnMode;
	private JLabel lbStat; // 外部状態表示用ラベル

	// -----------------------------------------------------------------------
	// 　アクセッサ
	// -----------------------------------------------------------------------
	public String getWrkDir() {
		return wrkDir;
	}

	public String getOsType() {
		return osType;
	}

	public void setUser(String pUser) {
		this.user.setText(pUser);
	}

	public String getUser() {
		return user.getText();
	}

	public void setPass(String pass) {
		this.pass.setText(pass);
	}

	public String getPass() {
		char[] charPass = pass.getPassword();
		return new String(charPass);
	}

	public String getHost() {
		return (String) (host.getSelectedItem());
	}

	// -----------------------------------------------------------------------
	// 　コンストラクタ
	// -----------------------------------------------------------------------
	public PnlFtpConnect(Ftp ftpObj) {
		super();
		this.ftpObj = ftpObj;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setOpaque(false);
		JLabel wLab1 = new JLabel("Host");
		wLab1.setSize(80, 20);
		JLabel wLab2 = new JLabel("User");
		wLab2.setSize(80, 20);
		JLabel wLab3 = new JLabel("Pass");
		wLab3.setSize(80, 20);
		host = new JComboBox();
		host.setPreferredSize(new Dimension(100, 20));
		user = new JTextField("");
		user.setPreferredSize(new Dimension(100, 20));
		pass = new JPasswordField("");
		pass.setPreferredSize(new Dimension(100, 20));
		jBtnCnn = new JToggleButton("Connect");
		jBtnCnn.setPreferredSize(new Dimension(100, 20));
		jBtnMode = new JToggleButton("Ascii");
		jBtnMode.setPreferredSize(new Dimension(100, 20));
		host.setEditable(true);
		host.removeAllItems();
		incore("Hosts");
		this.add(wLab1);
		this.add(host);
		this.add(wLab2);
		this.add(user);
		this.add(wLab3);
		this.add(pass);
		this.add(jBtnCnn);
		this.add(jBtnMode);
		host.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String wVal = (String) cb.getSelectedItem();
				if (gHtbl != null && gHtbl.containsKey(wVal)) {
					String[] wArray = ((String) gHtbl.get(wVal)).split("\t");
					if (wArray.length >= 2)
						user.setText(wArray[2].trim());
					if (wArray.length >= 3)
						pass.setText(wArray[3].trim());
				}
			}
		});
		// ---------------------------------------------------------------------
		// Connectボタン
		// ---------------------------------------------------------------------
		jBtnCnn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jBtnCnn.getText().equals("Connect")) {
					jBtnCnn.setText("PnlFtpConnect Wait...");
					final int port = 21;
					connect(getHost(), getUser(), getPass(), port);
				} else {
					disconnect();
					jBtnCnn.setText("Connect");
				}
			}
		});
		// ---------------------------------------------------------------------
		// jBtnModeボタン
		// ---------------------------------------------------------------------
		jBtnMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeChange(jBtnMode.getText());
			}
		});
	}

	// ---------------------------------------------------------------------
	// 転送モード変更
	// ---------------------------------------------------------------------
	private void modeChange(String mode) {
		if (mode.equals("Ascii")) {
			ftpObj.doAscii();
			jBtnMode.setText("Bin");
			jBtnMode.repaint();
		} else {
			ftpObj.doBinary();
			jBtnMode.setText("Ascii");
			jBtnMode.repaint();
		}
	}

	// ---------------------------------------------------------------------
	// "Hosts"をコンボ＆ハッシュテーブルに読み込む
	// ---------------------------------------------------------------------
	private void incore(String paraPath) {
		gHtbl = new HashMap();
		if (HashMapUtil.file2HashMapX(gHtbl, paraPath, 1)) {
			//20130823mod
			for (String key : gHtbl.keySet()) {
				host.addItem(key);
			}
//			for (Enumeration wEnum = gHtbl.keys(); wEnum.hasMoreElements();) {
//				host.addItem(wEnum.nextElement());
//			}
		}
	}

	// -------------------------------------------------------------------------
	// Connect and Login
	// -------------------------------------------------------------------------
	private void connect(final String host, final String user,
			final String pass, final int port) {
		if (ftpObj != null)
			disconnect();
		Thread th1 = new Thread() {
			@Override
			public void run() {
				try {
					if (!ftpObj.connect(host, port)) {
						System.out.println("Connection Error!!");
						return; // 制御用コネクション
					}
					if (!ftpObj.doLogin(user, pass.trim())) {
						System.out.println("Login Error!!");
						return; // login
					}
					osType = ftpObj.getOSType();
					// ftpObj.doLIST("NLST");
					ftpObj.doLIST("LIST");
					modeChange("Ascii");
					setStatus("Connected");
					jBtnCnn.setText("DisConnect");
				} catch (Exception e) {
					e.printStackTrace();
					setStatus("Error:" + e.toString());
				}
			}
		};
		th1.start();
	}

	// -------------------------------------------------------------------------
	// DisConnect
	// -------------------------------------------------------------------------
	private void disconnect() {
		if (ftpObj == null)
			return;
		Thread th1 = new Thread() {
			@Override
			public void run() {
				try {
					if (ftpObj.isConnected()) {
						System.out.println("disconnectボタン");
						ftpObj.doQuit();
						ftpObj.closeConnection();
						setStatus("Disconnected");
					}
				} catch (Exception e) {
					e.printStackTrace();
					setStatus("Error:" + e.toString());
				}
			}
		};
		th1.start();
	}

	// -------------------------------------------------------------------------
	// アクセッサ
	// -------------------------------------------------------------------------
	public String getStatus() {
		return status;
	}

	private void setStatus(String stat) {
		this.status = stat;
		if (this.lbStat != null) {
			this.lbStat.setText(stat);
			this.lbStat.repaint();
		}
	}

	public void setInfoDisplay(JLabel lbStat) {
		this.lbStat = lbStat;
	}
}