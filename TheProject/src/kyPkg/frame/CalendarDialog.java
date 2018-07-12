package kyPkg.frame;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

import javax.swing.*;

import kyPkg.uDateTime.DateCalc;

public class CalendarDialog extends JDialog {
	private static final String TODAY = "Today";
	private static final String NEXT = "Next>>";
	private static final String PREV = "<<Prev";
	private static final long serialVersionUID = 8689974610721637381L;
	private static String fontName = "ＭＳ ゴシック";
	final int holyday[] = { // 休日設定
			101, // 元旦 年の始めを祝う
			100, // 成人の日＜未定＞ 大人になったことを自覚し自ら生き抜こうとする青年を励ます
			211, // 建国記念日 建国をしのび、国を愛する心を養う。
			300, // 春分の日＜未定＞ 自然をたたえ、生物を慈しむ
			429, // みどりの日 自然に親しむとともにその恩恵に感謝し豊かな心を育む
			503, // 憲法記念日 憲法の施行を記念し国の成長を期する
			504, // 国民の休日 一日間隔の祝日と祝日の間
			505, // 子供の日 子供の人格を重んじ子供の幸福をはかるとともに、母に感謝する。
			720, // 海 の日＜未定＞ 海の恩恵に感謝するとともに、海洋国日本の繁栄を願う・・・ 2003以降
			915, // 敬老の日＜未定＞ 多年にわたり社会に尽くしてきた老人を敬愛し、長寿を祝う・・・2003以降
			900, // 秋分の日＜未定＞ 祖先を敬い、亡くなった人々を偲ぶ
			1000, // 体育の日＜未定＞ スポーツにしたしみ、健康な心身を培う
			1103, // 文化の日 自由と平和を愛し、文化を奨める。
			1123, // 勤労感謝の日 勤労を貴び、生産を祝い、国民たがいに感謝しあう
			1223 // 天皇誕生日 天皇の誕生を祝う
	};
	// -------------------------------------------------------------------------
	// 国民の祝日・・・祝日の間隔が１日であった場合に挟まれた日も休日となる
	// 春分＆秋分は官報によって報告されるので計算値とは違う可能性がある。
	// -------------------------------------------------------------------------
	// 日付をシンボライズしたボタンのクラス・・・う～ん
	// オブジェクトにする意味って・・？？なんだったけ？？うひゃ～忘れてるし
	// -------------------------------------------------------------------------

	static int gYY;
	static int gMM;
	static int gDD;

	static int qYY;
	static int qMM;
	static int qDD;
	static String qComment;
	static JPanel gCalendar;

	static JTextField rtnField;
	// static JFrame thisFrame; //static JWindow thisFrame; //
	static JDialog thisFrame;

	// booleam kanjiOpt=false;
	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public CalendarDialog(JTextField pRtnField, String pYMD, boolean debug) {
		this("Calendar", 200, 100, pRtnField, pYMD, debug);
	}

	public CalendarDialog(String pTitle, int pX, int pY, JTextField pRtnField,
			String pYMD, boolean debug) {
		super((Frame) null, pTitle, true);
		thisFrame = this;
		rtnField = pRtnField;
		setLocation(pX, pY);
		setSize(400, 300);
		// setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		// ---------------------------------------------------------------------
		// どこかで日付チェックのロジックを作っておこう！！
		// ---------------------------------------------------------------------
		pYMD = kyPkg.uDateTime.DateUtil.parseDate(pYMD);//20150417追加
		System.out.println("@calendarDialog pYMD:" + pYMD.length());
		if (pYMD.length() == 8) {
			String sYY = pYMD.substring(0, 4);
			String sMM = pYMD.substring(4, 6);
			String sDD = pYMD.substring(6, 8);
			System.out.println("@calendarDialog sYY:" + sYY);
			System.out.println("@calendarDialog sMM:" + sMM);
			System.out.println("@calendarDialog sDD:" + sDD);
			gYY = Integer.parseInt(sYY);
			gMM = Integer.parseInt(sMM);
			gDD = Integer.parseInt(sDD);
		} else {
			Calendar today = Calendar.getInstance(TimeZone.getTimeZone("JST"));
			gYY = today.get(Calendar.YEAR);
			gMM = (today.get(Calendar.MONTH) + 1);
			gDD = today.get(Calendar.DAY_OF_MONTH);
		}
		gCalendar = iniCalendar(gYY, gMM, gDD);
		getContentPane().add(gCalendar, BorderLayout.CENTER);

		JPanel px = iniControl();
		getContentPane().add(px, BorderLayout.SOUTH);
		// show();
		this.setVisible(true);

		/*
		 * addWindowListener(new WindowAdapter(){ public void
		 * windowClosing(WindowEvent e){ System.exit(0); } });
		 */
	}

	// -------------------------------------------------------------------------
	// InnerClass DButton
	// -------------------------------------------------------------------------
	public static class DButton extends JButton {
		private static final long serialVersionUID = -2607169948430440750L;
		boolean holyday;
		private int YY;
		private int MM;
		private int DD;
		int week; // 曜日
		int row; // 何週目･何段目
		int col; // 曜日
		String comment;

		DButton(int pYY, int pMM, int pDD) {
			super(("" + pDD));
			YY = pYY;
			MM = pMM;
			DD = pDD;
			comment = "";
			// -----------------------------------------------------------------
			// カレンダーボタンが押されたら・・・
			// -----------------------------------------------------------------
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					System.out.println("Button =>" + YY + "年" + MM + "月" + DD
							+ "日 " + comment);
					qComment = comment;
					String sYY = "0000" + YY;
					String sMM = "00" + MM;
					String sDD = "00" + DD;
					sYY = sYY.substring(sYY.length() - 4, sYY.length());
					sMM = sMM.substring(sMM.length() - 2, sMM.length());
					sDD = sDD.substring(sDD.length() - 2, sDD.length());
					String wRtn = sYY + sMM + sDD;

					String wKNJ = kyPkg.uDateTime.DateCalc.formatYMD_KJ(wRtn);
					rtnField.setText(wKNJ);
					// rtnField.setText(wRtn);
					// thisFrame.hide();
					thisFrame.setVisible(false);
				}
			});
		}

		public boolean setholyday(String pComm) {
			holyday = true;
			comment = pComm;
			setToolTipText(comment);
			setName(pComm);
			return (week == Calendar.SUNDAY);
		}

		public boolean isholyday() {
			return holyday;
		}

		public void setComm(String pComm) {
			comment = pComm;
		}

		public String getComm() {
			return comment;
		}

		public void setWeek(int pWeek) {
			week = pWeek;
		}

		public int getWeek() {
			return week;
		}
	}

	private static int getLastDay(int year, int month) {
		int lastDay = 0;
		switch (month) {
		case 2:
			lastDay = isLeap(year); // 閏年チェック
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			lastDay = 30;
			break;
		default:
			lastDay = 31;
			break;
		}
		return lastDay;
	}

	// -------------------------------------------------------------------------
	// iniCalendar
	// -------------------------------------------------------------------------
	public static JPanel iniCalendar(int pYY, int pMM, int pDD) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(pYY, (pMM - 1), pDD);
		pYY = rightNow.get(Calendar.YEAR);
		pMM = (rightNow.get(Calendar.MONTH) + 1);
		pDD = rightNow.get(Calendar.DAY_OF_MONTH);
		rightNow.set(pYY, (pMM - 1), 1);
		// ----------------------------------------------------------------------
		// 対象月の最終日決定（二四六九サムライ ＆ 閏年チェキ！！）
		// ----------------------------------------------------------------------
		int mm = rightNow.get(Calendar.MONTH) + 1;

		int lastDay = getLastDay(pYY, mm);

		//		int lastDay = 0;
		//		switch (mm) {
		//		case 2:
		//			lastDay = isLeap(pYY); // 閏年チェック
		//			break;
		//		case 4:
		//		case 6:
		//		case 9:
		//		case 11:
		//			lastDay = 30;
		//			break;
		//		default:
		//			lastDay = 31;
		//			break;
		//		}
		System.out.println(" lastDay:" + lastDay);
		// ---------------------------------------------------------------------
		// カレンダー用日付ボタンオブジェクトを作成！！
		// ---------------------------------------------------------------------
		Vector wTheseDays = new Vector();
		int wWeek = rightNow.get(Calendar.DAY_OF_WEEK);
		for (int i = 0; i < lastDay; i++) {
			DButton wDbt = new CalendarDialog.DButton(pYY, pMM, (i + 1));
			wDbt.setWeek(wWeek);
			wTheseDays.add(wDbt);
			wWeek++;
			if (wWeek > 7)
				wWeek = 1;
		}
		hollydayCnv(pYY, pMM, wTheseDays); // 祝日に関する処理

		// ---------------------------------------------------------------------
		// デバッグ情報
		// ---------------------------------------------------------------------
		System.out
				.println("---------------------------------------------------");
		System.out.println(" 年 YEAR        :" + rightNow.get(Calendar.YEAR));
		System.out.println(
				" 月 MONTH       :" + (rightNow.get(Calendar.MONTH) + 1));
		System.out.println(
				" 日 DAY_OF_MONTH:" + rightNow.get(Calendar.DAY_OF_MONTH));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" 現在の月の何度目の曜日 DAY_OF_WEEK_IN_MONTH :"
				+ rightNow.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println(" 現在の月の何週目か     WEEK_OF_MONTH        :"
				+ rightNow.get(Calendar.WEEK_OF_MONTH));
		System.out.println(" 現在の年の何週目か     WEEK_OF_YEAR         :"
				+ rightNow.get(Calendar.WEEK_OF_YEAR));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" 曜日（SUNDAY=1,MONDAY=2...）DAY_OF_WEEK     :"
				+ rightNow.get(Calendar.DAY_OF_WEEK));
		System.out.println(" 現在の年の何日目か          DAY_OF_YEAR     :"
				+ rightNow.get(Calendar.DAY_OF_YEAR));
		System.out
				.println("---------------------------------------------------");

		// ---------------------------------------------------------------------
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gc;
		JPanel rtnPanel = new JPanel();
		rtnPanel.setLayout(new BorderLayout());

		// ---------------------------------------------------------------------
		// タイトル部分
		// ---------------------------------------------------------------------
		Font fontMS16 = new Font(fontName, Font.BOLD, 24);
		gc = new GridBagConstraints();
		// gc.weightx = 1;
		gc.weighty = 1;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		// ---------------------------------------------------------------------
		// 年タイトル用パネル作成
		// ---------------------------------------------------------------------
		JPanel jpHead = new JPanel();
		jpHead.setLayout(gb);
		jpHead.setBackground(Color.LIGHT_GRAY);
		// ---------------------------------------------------------------------
		JLabel lbYear = new JLabel("" + pYY + "年");
		lbYear.setFont(fontMS16);
		lbYear.setForeground(Color.GRAY);
		gc.gridx = 2;
		gc.gridy = 1;
		gb.setConstraints(lbYear, gc);
		jpHead.add(lbYear);
		// ---------------------------------------------------------------------
		JLabel lbMonth = new JLabel("" + pMM + "月");
		lbMonth.setFont(fontMS16);
		lbMonth.setForeground(Color.GRAY);
		gc.gridx = 3;
		gc.gridy = 1;
		gb.setConstraints(lbMonth, gc);
		jpHead.add(lbMonth);
		// ---------------------------------------------------------------------
		jpHead.setBounds(0, 0, 200, 20);

		// ---------------------------------------------------------------------
		// 曜日タイトル用パネル作成
		// ---------------------------------------------------------------------
		JPanel jpBody = new JPanel();
		jpBody.setLayout(gb);
		jpBody.setBackground(Color.GRAY);
		String WeekNm[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		JLabel lbWeek;
		gc = new GridBagConstraints();
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.ipadx = 20;
		gc.ipady = 20;
		gc.fill = GridBagConstraints.BOTH;
		for (int i = 0; i < WeekNm.length; i++) {
			lbWeek = new JLabel(WeekNm[i]);
			gc.weightx = 1;
			gc.weighty = 1;
			gc.gridx = (i + 1);
			gc.gridy = 1;
			lbWeek.setSize(80, 20);
			// lbWeek.setForeground(Color.WHITE);
			lbWeek.setHorizontalAlignment(JLabel.CENTER);
			int week = (i % 8);
			if (week == 0) {
				lbWeek.setForeground(Color.PINK);
			} else if (week == 6) {
				lbWeek.setForeground(Color.LIGHT_GRAY);
			} else {
				lbWeek.setForeground(Color.WHITE);
			}
			gb.setConstraints(lbWeek, gc);
			jpBody.add(lbWeek);
		}
		// ---------------------------------------------------------------------
		// 日付
		// ---------------------------------------------------------------------
		int row = 2;
		int col = rightNow.get(Calendar.DAY_OF_WEEK); // 何曜日か?日:1月:2
		for (int i = 0; i < wTheseDays.size(); i++) {
			gc.weightx = 1;
			gc.weighty = 1;
			gc.gridx = col;
			gc.gridy = row;
			gc.insets = new Insets(3, 2, 3, 2); // top,left,bottom,right)
			DButton bt = (DButton) wTheseDays.get(i);
			int week = (col % 8);
			if (week == 1) {
				bt.setBackground(Color.PINK);
				bt.setForeground(Color.WHITE);
			} else if (week == 7) {
				bt.setBackground(Color.LIGHT_GRAY);
				bt.setForeground(Color.GRAY);
			} else {
				bt.setBackground(Color.WHITE);
				bt.setForeground(Color.GRAY);
			}
			if (bt.isholyday()) {
				// bt.setForeground(Color.YELLOW);
				// bt.setBackground(Color.ORANGE);
				bt.setBackground(Color.PINK);
				bt.setForeground(Color.WHITE);
			}

			if ((pDD - 1) == i) {
				bt.setForeground(Color.RED);
				bt.setBackground(Color.YELLOW);
			}

			gb.setConstraints(bt, gc);
			jpBody.add(bt);
			if (week == 7) {
				col = 1;
				row++;
			} else {
				col++;
			}
		}
		// ---------------------------------------------------------------------
		// 配置 BorderLayout!!
		// ---------------------------------------------------------------------
		// コンポーネントはそれらの推奨サイズや
		// コンテナサイズの制約を満たすように配置されます。
		// ---------------------------------------------------------------------
		// NORTH & SOUTH は 水平方向に引き伸ばされます。
		// （なのでFlowLayoutを入れ子にしても思い通りにはなりません・・とほほ）
		// EAST & WEST は 垂直方向に引き伸ばされます。
		// CENTER は、スペースを残さないように、水平&垂直の両方向に引き伸ばされます
		// ---------------------------------------------------------------------
		rtnPanel.add(jpHead, BorderLayout.NORTH);
		rtnPanel.add(jpBody, BorderLayout.CENTER);
		return rtnPanel;
	}

	// -------------------------------------------------------------------------
	// 祝日処理
	// -------------------------------------------------------------------------
	public static void hollydayCnv(int pYY, int pMM, Vector wTheseDays) {
		int wHd;
		int wSv;
		Calendar holly = Calendar.getInstance();
		holly.set(Calendar.YEAR, pYY); // 年をセットする
		holly.set(Calendar.MONTH, pMM - 1); // 月ー１をセットする
		if (pMM == 1) {
			((DButton) wTheseDays.get(1 - 1)).setholyday("元旦");
			// -------------------------------------------------------------------------------
			// 成人の日　1月の第2月曜 ※2000年以降 (旧成人の日は 1月15日)
			// -------------------------------------------------------------------------------
			if (pYY < 2000) {
				wHd = 15;
			} else {
				holly.set(Calendar.DAY_OF_WEEK, 2); // ｘ曜日 ＝ ２：月曜日
				holly.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2); // ２回目のｘ曜日
				wHd = holly.get(Calendar.DAY_OF_MONTH);
			}
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("成人の日") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("振替");
		} else if (pMM == 2) {
			if (((DButton) wTheseDays.get(11 - 1)).setholyday("建国記念日") == true)
				((DButton) wTheseDays.get(11)).setholyday("振替");
		} else if (pMM == 3) {
			// -------------------------------------------------------------------------------
			// 春分の日
			// -------------------------------------------------------------------------------
			wHd = (int) (20.8431 + 0.242194 * (pYY - 1980)
					- ((pYY - 1980) / 4));
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("春分の日") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("振替");
		} else if (pMM == 4) {
			if (((DButton) wTheseDays.get(29 - 1)).setholyday("みどりの日") == true)
				((DButton) wTheseDays.get(29)).setholyday("振替");
		} else if (pMM == 5) {
			((DButton) wTheseDays.get(3 - 1)).setholyday("憲法記念日");
			// 祝日と祝日の間ゆえ・・・ここは固定処理とした
			((DButton) wTheseDays.get(4 - 1)).setholyday("国民の休日");
			if (((DButton) wTheseDays.get(5 - 1)).setholyday("子供の日") == true)
				((DButton) wTheseDays.get(5)).setholyday("振替");
		} else if (pMM == 7) {
			// -------------------------------------------------------------------------------
			// 海の日　07月の第3月曜 ※2003年以降 (旧海の日 7月20日)
			// -------------------------------------------------------------------------------
			if (pYY < 2003) {
				wHd = 20;
			} else {
				holly.set(Calendar.DAY_OF_WEEK, 2); // ｘ曜日 ＝ ２：月曜日
				holly.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3); // ３回目のｘ曜日
				wHd = holly.get(Calendar.DAY_OF_MONTH);
			}
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("海の日") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("振替");
		} else if (pMM == 9) {
			// -------------------------------------------------------------------------------
			// 敬老の日　09月の第3月曜 ※2003年以降 (敬老の日は9月15日)
			// -------------------------------------------------------------------------------
			if (pYY < 2003) {
				wHd = 15;
			} else {
				holly.set(Calendar.DAY_OF_WEEK, 2); // ｘ曜日 ＝ ２：月曜日
				holly.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3); // ３回目のｘ曜日
				wHd = holly.get(Calendar.DAY_OF_MONTH);
			}
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("敬老の日") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("振替");
			wSv = wHd;
			// -------------------------------------------------------------------------------
			// 秋分の日
			// -------------------------------------------------------------------------------
			wHd = (int) (23.2488 + 0.242194 * (pYY - 1980)
					- ((pYY - 1980) / 4));
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("秋分の日") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("振替");
			// 祝日と祝日の間が一日だった場合は国民の祝日となる
			if ((wHd - wSv) == 2)
				((DButton) wTheseDays.get(wSv)).setholyday("国民の祝日");

		} else if (pMM == 10) {
			// -------------------------------------------------------------------------------
			// 体育の日　10月の第2月曜 ※2000年以降 (旧体育の日は10月10日)
			// -------------------------------------------------------------------------------
			if (pYY < 2000) {
				wHd = 10;
			} else {
				holly.set(Calendar.DAY_OF_WEEK, 2); // ｘ曜日 ＝ ２：月曜日
				holly.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2); // ２回目のｘ曜日
				wHd = holly.get(Calendar.DAY_OF_MONTH);
			}
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("体育の日") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("振替");
		} else if (pMM == 11) {
			if (((DButton) wTheseDays.get(3 - 1)).setholyday("文化の日") == true)
				((DButton) wTheseDays.get(3)).setholyday("振替");
			if (((DButton) wTheseDays.get(23 - 1)).setholyday("勤労感謝の日") == true)
				((DButton) wTheseDays.get(23)).setholyday("振替");
		} else if (pMM == 12) {
			if (((DButton) wTheseDays.get(23 - 1)).setholyday("天皇誕生日") == true)
				((DButton) wTheseDays.get(23)).setholyday("振替");
		}
	}

	// -------------------------------------------------------------------------
	JPanel iniControl() {
		// ---------------------------------------------------------------------
		// コントロール部分
		// ---------------------------------------------------------------------
		Font font9 = new Font("Courier", Font.PLAIN, 9); // フォント設定
		JPanel px = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gc;
		px.setLayout(gb);
		px.setBackground(Color.GRAY);
		gc = new GridBagConstraints();
		gc.insets = new Insets(5, 1, 1, 1); // top,left,bottom,right)
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.fill = GridBagConstraints.BOTH;
		// ---------------------------------------------------------------------
		final JComboBox cmbYear = new JComboBox();
		for (int i = 1980; i <= 2020; i++) {
			cmbYear.addItem("" + i + "年");
		}
		gc.gridx = 1;
		gc.gridy = 1;
		gb.setConstraints(cmbYear, gc);
		cmbYear.setFont(font9);
		cmbYear.setBackground(Color.WHITE);
		px.add(cmbYear);
		// ---------------------------------------------------------------------
		final JComboBox cmbMonth = new JComboBox();
		for (int i = 1; i <= 12; i++) {
			cmbMonth.addItem("" + i + "月");
		}
		gc.gridx = 2;
		gc.gridy = 1;
		gb.setConstraints(cmbMonth, gc);
		cmbMonth.setFont(font9);
		cmbMonth.setBackground(Color.WHITE);
		px.add(cmbMonth);
		// ---------------------------------------------------------------------
		final JComboBox cmbDay = new JComboBox();
		for (int i = 1; i <= 31; i++) {
			cmbDay.addItem("" + i + "日");
		}
		gc.gridx = 3;
		gc.gridy = 1;
		gb.setConstraints(cmbDay, gc);
		cmbDay.setFont(font9);
		cmbDay.setBackground(Color.WHITE);
		px.add(cmbDay);
		// ---------------------------------------------------------------------
		final JButton jbToday = new JButton(TODAY);
		jbToday.setFont(font9);
		gc.gridx = 4;
		gc.gridy = 1;
		gb.setConstraints(jbToday, gc);
		px.add(jbToday);
		// ---------------------------------------------------------------------
		final JButton jbPrev = new JButton(PREV);
		jbPrev.setFont(font9);
		gc.gridx = 5;
		gc.gridy = 1;
		gb.setConstraints(jbPrev, gc);
		px.add(jbPrev);
		// ---------------------------------------------------------------------
		final JButton jbNext = new JButton(NEXT);
		jbNext.setFont(font9);
		gc.gridx = 6;
		gc.gridy = 1;
		gb.setConstraints(jbNext, gc);
		px.add(jbNext);
		// ---------------------------------------------------------------------
		// ↓ここから二段目のボタン
		// ---------------------------------------------------------------------
		final JComboBox cmbPM = new JComboBox();
		cmbPM.addItem("+");
		cmbPM.addItem("-");
		cmbPM.setFont(font9);
		cmbPM.setBackground(Color.WHITE);
		gc.gridx = 1;
		gc.gridy = 2;
		gb.setConstraints(cmbPM, gc);
		px.add(cmbPM);
		// ---------------------------------------------------------------------
		final JComboBox cmbVal = new JComboBox();
		for (int i = 1; i <= 365; i++) {
			cmbVal.addItem("" + i);
		}
		cmbVal.setFont(font9);
		cmbVal.setBackground(Color.WHITE);
		gc.gridx = 2;
		gc.gridy = 2;
		gb.setConstraints(cmbVal, gc);
		px.add(cmbVal);
		// ---------------------------------------------------------------------
		final JComboBox cmbTrm = new JComboBox();
		cmbTrm.addItem("Day");
		cmbTrm.addItem("Month");
		cmbTrm.addItem("Week");
		cmbTrm.addItem("Year");
		cmbTrm.setFont(font9);
		cmbTrm.setBackground(Color.WHITE);
		gc.gridx = 3;
		gc.gridy = 2;
		gb.setConstraints(cmbTrm, gc);
		px.add(cmbTrm);
		// ---------------------------------------------------------------------
		JButton jbCancel = new JButton("Cancel");
		jbCancel.setFont(font9);
		gc.gridx = 4;
		gc.gridy = 2;
		gb.setConstraints(jbCancel, gc);
		px.add(jbCancel);
		// ---------------------------------------------------------------------
		final JButton jbGogo = new JButton("Go!");
		jbGogo.setFont(font9);
		gc.gridwidth = 2;
		gc.gridx = 5;
		gc.gridy = 2;
		gb.setConstraints(jbGogo, gc);
		px.add(jbGogo);
		// ---------------------------------------------------------------------
		// default！！
		// ---------------------------------------------------------------------
		cmbYear.setSelectedItem(gYY + "年");
		cmbMonth.setSelectedItem(gMM + "月");
		cmbDay.setSelectedItem(gDD + "日");
		cmbVal.setSelectedItem("1");
		cmbTrm.setSelectedItem("Day");
		cmbPM.setSelectedItem("-");
		// ---------------------------------------------------------------------
		// Action 《 Todayボタン 》
		// ---------------------------------------------------------------------
		jbToday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println(TODAY);
				Calendar today = Calendar
						.getInstance(TimeZone.getTimeZone("JST"));
				gYY = today.get(Calendar.YEAR);
				gMM = (today.get(Calendar.MONTH) + 1);
				gDD = today.get(Calendar.DAY_OF_MONTH);
				cmbYear.setSelectedItem(gYY + "年");
				cmbMonth.setSelectedItem(gMM + "月");
				cmbDay.setSelectedItem(gDD + "日");
				cmbVal.setSelectedItem("1");
				cmbTrm.setSelectedItem("Day");
				cmbPM.setSelectedItem("+");
				repaintMe();
			}
		});
		// ---------------------------------------------------------------------
		// Action 《 Prevボタン 》
		// ---------------------------------------------------------------------
		jbPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Prev");
				//TODO	日付が月末日付なら月末日付をアサインしてあげる　　20151008
				gDD = lastDayCheck(gYY, gMM, gDD, -1);

				gMM--;
				if (gMM < 1) {
					gYY--;
					gMM = 12;
				}
				repaintMe();
			}
		});
		// ---------------------------------------------------------------------
		// Action 《 Nextボタン 》
		// ---------------------------------------------------------------------
		jbNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Next");
				//TODO	日付が月末日付なら月末日付をアサインしてあげる　　20151008
				gDD = lastDayCheck(gYY, gMM, gDD, 1);
				gMM++;
				if (gMM > 12) {
					gYY++;
					gMM = 1;
				}
				repaintMe();
			}
		});
		// ---------------------------------------------------------------------
		// Action 《 Cancelボタン 》
		// ---------------------------------------------------------------------
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Bye Bye!!");
				// thisFrame.hide();
				thisFrame.setVisible(false);

				// System.exit(0);
			}
		});
		// ---------------------------------------------------------------------
		// Action 《 Go ボタン 》
		// ---------------------------------------------------------------------
		jbGogo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String sYear, sMonth, sDay, sTrm;
				int iYear, iMonth, iDay, iVal;

				sYear = (String) cmbYear.getSelectedItem();
				sMonth = (String) cmbMonth.getSelectedItem();
				sDay = (String) cmbDay.getSelectedItem();

				iYear = Integer
						.parseInt(sYear.substring(0, (sYear.length() - 1)));
				iMonth = Integer
						.parseInt(sMonth.substring(0, (sMonth.length() - 1)));
				iDay = Integer.parseInt(sDay.substring(0, (sDay.length() - 1)));

				iVal = Integer.parseInt((String) cmbVal.getSelectedItem());
				iVal--;
				sTrm = ((String) cmbTrm.getSelectedItem());
				if (((String) cmbPM.getSelectedItem()).equals("-")) {
					iVal = iVal * -1;
				}
				if (sTrm.equals("Day")) {
					iDay = iDay + iVal;
				} else if (sTrm.equals("Week")) {
					iDay = iDay + (iVal * 7);
				} else if (sTrm.equals("Month")) {
					iMonth = iMonth + iVal;
				} else if (sTrm.equals("Year")) {
					iYear = iYear + iVal;
				}
				Calendar rightNow = Calendar.getInstance();
				rightNow.set(iYear, (iMonth - 1), iDay);
				gYY = rightNow.get(Calendar.YEAR);
				gMM = (rightNow.get(Calendar.MONTH) + 1);
				gDD = rightNow.get(Calendar.DAY_OF_MONTH);

				System.out.println(" Year :" + iYear);
				System.out.println(" Month:" + iMonth);
				System.out.println(" Day  :" + iDay);
				System.out.println(" Val  :" + iVal);
				System.out.println(" Trm  :" + sTrm);
				repaintMe();
			}
		});
		return px;
	}

	private static int lastDayCheck(int year, int month, int day, int diff) {
		int currentLastDay = getLastDay(year, month);
		if (day == currentLastDay) //もしカレントが月末なら・・・
			return getLastDay(year, month + diff);//移動先の月の最終日を返す
		return day;//それ以外は変更なしなので・・・そのまま返す
	}

	public void test(Calendar calendar) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
		String bef1 = df1.format(DateCalc.beginningOfTheMonth(calendar, -1)); // 前月月初
		String aft1 = df1.format(DateCalc.endOfTheMonth(calendar, -1)); // 前月の末日
	}

	// -------------------------------------------------------------------------
	// repaintMe
	// -------------------------------------------------------------------------
	void repaintMe() {
		remove(gCalendar);
		gCalendar = iniCalendar(gYY, gMM, gDD);
		getContentPane().add(gCalendar, BorderLayout.CENTER);
		// show();
		this.setVisible(true);
	}

	// -------------------------------------------------------------------------
	// 閏年Check
	// -------------------------------------------------------------------------
	public static int isLeap(int Year) { // 閏年判定
		int n = -1;
		if (Year % 4 == 0 && Year % 100 != 0 || Year % 400 == 0) {
			n = 29;
		} else {
			n = 28;
		}
		return n;
	}

	// -------------------------------------------------------------------------
	// Color.WHITE Color.BLACK Color.DARK_GRAY Color.GRAY Color.LIGHT_GRAY
	// Color.CYAN Color.MAGENTA Color.ORANGE Color.PINK
	// Color.RED Color.BLUE Color.YELLOW Color.GREEN
	// -------------------------------------------------------------------------
	public void test() {
		Calendar rightNow = Calendar.getInstance();
		System.out
				.println("---------------------------------------------------");
		System.out.println(" today is...");
		System.out
				.println("---------------------------------------------------");
		System.out.println(" get系 ");
		System.out
				.println("---------------------------------------------------");
		System.out.println(" AM_PM :" + rightNow.get(Calendar.AM_PM));
		System.out
				.println("---------------------------------------------------");
		System.out
				.println(" HOUR_OF_DAY :" + rightNow.get(Calendar.HOUR_OF_DAY));
		System.out.println(" HOUR        :" + rightNow.get(Calendar.HOUR));
		System.out.println(" MINUTE      :" + rightNow.get(Calendar.MINUTE));
		System.out.println(" SECOND      :" + rightNow.get(Calendar.SECOND));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" 年 YEAR        :" + rightNow.get(Calendar.YEAR));
		System.out.println(
				" 月 MONTH       :" + (rightNow.get(Calendar.MONTH) + 1));
		System.out.println(
				" 日 DAY_OF_MONTH:" + rightNow.get(Calendar.DAY_OF_MONTH));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" 現在の月の何度目の曜日 DAY_OF_WEEK_IN_MONTH :"
				+ rightNow.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println(" 現在の月の何週目か     WEEK_OF_MONTH        :"
				+ rightNow.get(Calendar.WEEK_OF_MONTH));
		System.out.println(" 現在の年の何週目か     WEEK_OF_YEAR         :"
				+ rightNow.get(Calendar.WEEK_OF_YEAR));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" 曜日（SUNDAY=1,MONDAY=2...）DAY_OF_WEEK     :"
				+ rightNow.get(Calendar.DAY_OF_WEEK));
		System.out.println(" 現在の年の何日目か          DAY_OF_YEAR     :"
				+ rightNow.get(Calendar.DAY_OF_YEAR));

		System.out
				.println("---------------------------------------------------");
		System.out.println(" set系 ");
		System.out
				.println("---------------------------------------------------");
	}

	// --------------------------------------------------------------------
	// main for StndAlone Test
	// --------------------------------------------------------------------
	public static void main(String[] argv) {
		standalone();
		//		testIt();
	}

	public static void testIt() {
		int year = 2015;
		int month = 10;
		int day = 31;
		int diff = -1;
		int val = lastDayCheck(year, month, day, diff);
		System.out.println("result:" + val);
	}

	public static void standalone() {
		JFrame frmBase = new JFrame();
		frmBase.setSize(400, 300);
		frmBase.getContentPane().setLayout(null);

		final JTextField txtRtn = new JTextField("????");
		txtRtn.setBounds(10, 10, 100, 20);
		frmBase.getContentPane().add(txtRtn);

		final JButton btnTest = new JButton("Test");
		btnTest.setBounds(10, 30, 100, 20);
		frmBase.getContentPane().add(btnTest);

		frmBase.setVisible(true);
		// --------------------------------------------------------------------
		btnTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Calendar today = Calendar
						.getInstance(TimeZone.getTimeZone("JST"));
				int YY = today.get(Calendar.YEAR);
				int MM = (today.get(Calendar.MONTH) + 1);
				int DD = today.get(Calendar.DAY_OF_MONTH);
				String sYY = "0000" + YY;
				String sMM = "00" + MM;
				String sDD = "00" + DD;
				sYY = sYY.substring(sYY.length() - 4, sYY.length());
				sMM = sMM.substring(sMM.length() - 2, sMM.length());
				sDD = sDD.substring(sDD.length() - 2, sDD.length());
				String wYMD = sYY + sMM + sDD;
				new CalendarDialog("Calendar", 200, 100, txtRtn, wYMD, true);
			}
		});
		// --------------------------------------------------------------------
		frmBase.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

}
