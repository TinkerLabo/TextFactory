package kyPkg.panelMini;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kyPkg.frame.CalendarDialog;
import kyPkg.uDateTime.DateUtil;
import kyPkg.uFile.HashMapUtil;
import kyPkg.util.FontUtility;
import kyPkg.util.Ruler;

public class DatePanel extends JPanel {
	public static final String AFT = "aft";//"aft"
	public static final String BEF = "bef";
	private static final long serialVersionUID = 1L;
	private static final boolean designMode = false;
	private final String fontName = "Dialog";
	private JTextField befYmd;
	private JTextField aftYmd;

	private class MiniButton extends JButton {
		private static final long serialVersionUID = 1L;

		private MiniButton(String caption, int x, int y, int width, int height) {
			super(caption);
			this.setMargin(new Insets(0, 0, 0, 0));
			this.setFont(new Font(fontName, Font.PLAIN, 9));
			this.setBounds(x, y, width, height);
		}
	}

	private MiniButton btnBef;
	private MiniButton btnAft;

	private MiniButton btnYFr1;
	private MiniButton btnYBk1;
	private MiniButton btnMFr1;
	private MiniButton btnMBk1;

	private MiniButton btnYFr2;
	private MiniButton btnYBk2;
	private MiniButton btnMFr2;
	private MiniButton btnMBk2;

	private MiniButton btnYFr;
	private MiniButton btnYBk;
	private MiniButton btnMFr;
	private MiniButton btnMBk;
	private String wAymd;
	private String wBymd;
	boolean needControls;

	// yyyymmddに変更する
	private String cnvKj2Ymd(String wYmd) {
		return kyPkg.uDateTime.DateCalc.formatYMD("yyyyMMdd", wYmd);
	}

	// 年月日に変換する
	private String cnv2KjYmd(String wYmd) {
		return kyPkg.uDateTime.DateCalc.formatYMD_KJ(wYmd);
	}

	public void setAftYmd(String strYmd) {
		aftYmd.setText(cnv2KjYmd(strYmd));
	}

	public void setBefYmd(String strYmd) {
		befYmd.setText(cnv2KjYmd(strYmd));
	}

	public String getAftYmd() {
		return cnvKj2Ymd(aftYmd.getText());
	}

	public String getBefYmd() {
		return cnvKj2Ymd(befYmd.getText());
	}

	public DatePanel(HashMap<String, Object> paramMap, boolean preMonth,
			boolean needControls) {
		super();
		FontUtility.setFont(new Font("Monospaced", Font.PLAIN, 10),false);// いい感じ！！

		this.setOpaque(false);

		// 開始日
		String first = DateUtil.firstDayOfMonth(); // 今月初日
		String default2 = DateUtil.ymdCalc(first, -3, 'm');
		wBymd = HashMapUtil.getAsString(paramMap, BEF, default2);
		// 終了日
		String default1 = DateUtil.lastDayOfPreMonth();
		// 直近まで必要な場合もある
		if (preMonth == false) {
			default1 = kyPkg.uDateTime.DateCalc.getToday();
		}
		wAymd = HashMapUtil.getAsString(paramMap, AFT, default1);
		this.needControls = needControls;
		initGUI();
	}

	public void initGUI() {
		// XXX 末日のチェックを行うかどうか・・・考えておく
		// XXX コンボボックスにより　3ヶ月前　一ヶ月前　前年同月などが簡単に指定できるように変更を行う
		int x = 30;
		int y = 20;
		int wide = 120;
		int hight = 22;
		if (needControls) {
			this.setPreferredSize(new Dimension(200, 100));
		} else {
			this.setPreferredSize(new Dimension(200, 25));
		}
		// this.setPreferredSize(new Dimension(300, 50));
		this.setLayout(null);
		befYmd = new JTextField();
		aftYmd = new JTextField();

		JLabel label1 = new JLabel("期間");
		label1.setBounds(5, 0, wide, hight);
		befYmd.setBounds(35, 0, wide, hight);
		btnBef = new MiniButton("...", 35 + wide, 0, 20, hight);
		JLabel label2 = new JLabel("~");
		label2.setBounds(180, 0, wide, hight);
		aftYmd.setBounds(190, 0, wide, hight);
		btnAft = new MiniButton("...", 190 + wide, 0, 20, hight);
		setBefYmd(wBymd);
		setAftYmd(wAymd);

		// ---------------------------------------------------------------------
		wide = 40;
		hight = 20;
		x = 30;
		y = 20;
		btnYBk1 = new MiniButton("<Y", x, y, wide, hight);
		x += wide;
		btnYFr1 = new MiniButton("Y>", x, y, wide, hight);
		x += wide;
		btnMBk1 = new MiniButton("<M", x, y, wide, hight);
		x += wide;
		btnMFr1 = new MiniButton("M>", x, y, wide, hight);
		// ---------------------------------------------------------------------
		x += wide;
		btnYBk2 = new MiniButton("<Y", x, y, wide, hight);
		x += wide;
		btnYFr2 = new MiniButton("Y>", x, y, wide, hight);
		x += wide;
		btnMBk2 = new MiniButton("<M", x, y, wide, hight);
		x += wide;
		btnMFr2 = new MiniButton("M>", x, y, wide, hight);
		// ---------------------------------------------------------------------
		y = 36;
		int btnWid2 = 80;
		x = 30;
		btnYBk = new MiniButton("<<Y", x, y, btnWid2, hight);
		x += btnWid2;
		btnYFr = new MiniButton("Y>>", x, y, btnWid2, hight);
		x += btnWid2;
		btnMBk = new MiniButton("<<M", x, y, btnWid2, hight);
		x += btnWid2;
		btnMFr = new MiniButton("M>>", x, y, btnWid2, hight);
		this.add(label1, null);
		this.add(befYmd, null);
		this.add(btnBef);
		this.add(aftYmd, null);
		this.add(label2, null);
		this.add(btnAft);
		// ---------------------------------------------------------------------
		if (needControls) {
			this.add(btnMBk1);
			this.add(btnMFr1);
			this.add(btnYBk1);
			this.add(btnYFr1);
			// ---------------------------------------------------------------------
			this.add(btnMBk2);
			this.add(btnMFr2);
			this.add(btnYBk2);
			this.add(btnYFr2);
			// ---------------------------------------------------------------------
			this.add(btnMBk);
			this.add(btnMFr);
			this.add(btnYBk);
			this.add(btnYFr);
		}
		// ---------------------------------------------------------------------
		btnBef.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				new CalendarDialog(befYmd, getBefYmd(), true);
			}
		});
		btnAft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				new CalendarDialog(aftYmd, getAftYmd(), true);
			}
		});
		befYmd.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				befYmd.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				befYmd.setText(DateUtil.parseDate(getBefYmd()));
			}
		});
		aftYmd.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				aftYmd.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				aftYmd.setText(DateUtil.parseDate(getAftYmd()));
			}
		});
		// ---------------------------------------------------------------------
		btnMFr1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setBefYmd(DateUtil.ymdCalc(getBefYmd(), 1, 'm'));
			}
		});
		btnMBk1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setBefYmd(DateUtil.ymdCalc(getBefYmd(), -1, 'm'));
			}
		});
		btnYFr1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setBefYmd(DateUtil.ymdCalc(getBefYmd(), 1, 'y'));
			}
		});
		btnYBk1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setBefYmd(DateUtil.ymdCalc(getBefYmd(), -1, 'y'));
			}
		});

		// -----------------------------------------------------------------------------
		btnMFr2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setAftYmd(DateUtil.ymdCalc(getAftYmd(), 1, 'm'));
			}
		});
		btnMBk2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setAftYmd(DateUtil.ymdCalc(getAftYmd(), -1, 'm'));
			}
		});
		btnYFr2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setAftYmd(DateUtil.ymdCalc(getAftYmd(), 1, 'y'));
			}
		});
		btnYBk2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setAftYmd(DateUtil.ymdCalc(getAftYmd(), -1, 'y'));
			}
		});
		// -----------------------------------------------------------------------------
		btnMFr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setBefYmd(DateUtil.ymdCalc(getBefYmd(), 1, 'm'));
				setAftYmd(DateUtil.ymdCalc(getAftYmd(), 1, 'm'));
			}
		});
		btnMBk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setBefYmd(DateUtil.ymdCalc(getBefYmd(), -1, 'm'));
				setAftYmd(DateUtil.ymdCalc(getAftYmd(), -1, 'm'));
			}
		});
		btnYFr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setBefYmd(DateUtil.ymdCalc(getBefYmd(), 1, 'y'));
				setAftYmd(DateUtil.ymdCalc(getAftYmd(), 1, 'y'));
			}
		});
		btnYBk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setBefYmd(DateUtil.ymdCalc(getBefYmd(), -1, 'y'));
				setAftYmd(DateUtil.ymdCalc(getAftYmd(), -1, 'y'));
			}
		});

	}

	public void ruler(Graphics g) {
		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
				new Color(255, 153, 255));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (designMode)
			ruler(g);
	}
}
