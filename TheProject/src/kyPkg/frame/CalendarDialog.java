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
	private static String fontName = "�l�r �S�V�b�N";
	final int holyday[] = { // �x���ݒ�
			101, // ���U �N�̎n�߂��j��
			100, // ���l�̓������聄 ��l�ɂȂ������Ƃ����o�����琶���������Ƃ���N���܂�
			211, // �����L�O�� ���������̂сA����������S��{���B
			300, // �t���̓������聄 ���R���������A������������
			429, // �݂ǂ�̓� ���R�ɐe���ނƂƂ��ɂ��̉��b�Ɋ��ӂ��L���ȐS�����
			503, // ���@�L�O�� ���@�̎{�s���L�O�����̐�����������
			504, // �����̋x�� ����Ԋu�̏j���Əj���̊�
			505, // �q���̓� �q���̐l�i���d�񂶎q���̍K�����͂���ƂƂ��ɁA��Ɋ��ӂ���B
			720, // �C �̓������聄 �C�̉��b�Ɋ��ӂ���ƂƂ��ɁA�C�m�����{�̔ɉh���肤�E�E�E 2003�ȍ~
			915, // �h�V�̓������聄 ���N�ɂ킽��Љ�ɐs�����Ă����V�l���h�����A�������j���E�E�E2003�ȍ~
			900, // �H���̓������聄 �c����h���A�S���Ȃ����l�X���Â�
			1000, // �̈�̓������聄 �X�|�[�c�ɂ������݁A���N�ȐS�g��|��
			1103, // �����̓� ���R�ƕ��a�������A���������߂�B
			1123, // �ΘJ���ӂ̓� �ΘJ���M�сA���Y���j���A�����������Ɋ��ӂ�����
			1223 // �V�c�a���� �V�c�̒a�����j��
	};
	// -------------------------------------------------------------------------
	// �����̏j���E�E�E�j���̊Ԋu���P���ł������ꍇ�ɋ��܂ꂽ�����x���ƂȂ�
	// �t�����H���͊���ɂ���ĕ񍐂����̂Ōv�Z�l�Ƃ͈Ⴄ�\��������B
	// -------------------------------------------------------------------------
	// ���t���V���{���C�Y�����{�^���̃N���X�E�E�E���`��
	// �I�u�W�F�N�g�ɂ���Ӗ����āE�E�H�H�Ȃ񂾂������H�H���Ђ�`�Y��Ă邵
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
		// �ǂ����œ��t�`�F�b�N�̃��W�b�N������Ă������I�I
		// ---------------------------------------------------------------------
		pYMD = kyPkg.uDateTime.DateUtil.parseDate(pYMD);//20150417�ǉ�
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
		int week; // �j��
		int row; // ���T�ڥ���i��
		int col; // �j��
		String comment;

		DButton(int pYY, int pMM, int pDD) {
			super(("" + pDD));
			YY = pYY;
			MM = pMM;
			DD = pDD;
			comment = "";
			// -----------------------------------------------------------------
			// �J�����_�[�{�^���������ꂽ��E�E�E
			// -----------------------------------------------------------------
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					System.out.println("Button =>" + YY + "�N" + MM + "��" + DD
							+ "�� " + comment);
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
			lastDay = isLeap(year); // �[�N�`�F�b�N
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
		// �Ώی��̍ŏI������i��l�Z��T�����C �� �[�N�`�F�L�I�I�j
		// ----------------------------------------------------------------------
		int mm = rightNow.get(Calendar.MONTH) + 1;

		int lastDay = getLastDay(pYY, mm);

		//		int lastDay = 0;
		//		switch (mm) {
		//		case 2:
		//			lastDay = isLeap(pYY); // �[�N�`�F�b�N
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
		// �J�����_�[�p���t�{�^���I�u�W�F�N�g���쐬�I�I
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
		hollydayCnv(pYY, pMM, wTheseDays); // �j���Ɋւ��鏈��

		// ---------------------------------------------------------------------
		// �f�o�b�O���
		// ---------------------------------------------------------------------
		System.out
				.println("---------------------------------------------------");
		System.out.println(" �N YEAR        :" + rightNow.get(Calendar.YEAR));
		System.out.println(
				" �� MONTH       :" + (rightNow.get(Calendar.MONTH) + 1));
		System.out.println(
				" �� DAY_OF_MONTH:" + rightNow.get(Calendar.DAY_OF_MONTH));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" ���݂̌��̉��x�ڂ̗j�� DAY_OF_WEEK_IN_MONTH :"
				+ rightNow.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println(" ���݂̌��̉��T�ڂ�     WEEK_OF_MONTH        :"
				+ rightNow.get(Calendar.WEEK_OF_MONTH));
		System.out.println(" ���݂̔N�̉��T�ڂ�     WEEK_OF_YEAR         :"
				+ rightNow.get(Calendar.WEEK_OF_YEAR));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" �j���iSUNDAY=1,MONDAY=2...�jDAY_OF_WEEK     :"
				+ rightNow.get(Calendar.DAY_OF_WEEK));
		System.out.println(" ���݂̔N�̉����ڂ�          DAY_OF_YEAR     :"
				+ rightNow.get(Calendar.DAY_OF_YEAR));
		System.out
				.println("---------------------------------------------------");

		// ---------------------------------------------------------------------
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gc;
		JPanel rtnPanel = new JPanel();
		rtnPanel.setLayout(new BorderLayout());

		// ---------------------------------------------------------------------
		// �^�C�g������
		// ---------------------------------------------------------------------
		Font fontMS16 = new Font(fontName, Font.BOLD, 24);
		gc = new GridBagConstraints();
		// gc.weightx = 1;
		gc.weighty = 1;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		// ---------------------------------------------------------------------
		// �N�^�C�g���p�p�l���쐬
		// ---------------------------------------------------------------------
		JPanel jpHead = new JPanel();
		jpHead.setLayout(gb);
		jpHead.setBackground(Color.LIGHT_GRAY);
		// ---------------------------------------------------------------------
		JLabel lbYear = new JLabel("" + pYY + "�N");
		lbYear.setFont(fontMS16);
		lbYear.setForeground(Color.GRAY);
		gc.gridx = 2;
		gc.gridy = 1;
		gb.setConstraints(lbYear, gc);
		jpHead.add(lbYear);
		// ---------------------------------------------------------------------
		JLabel lbMonth = new JLabel("" + pMM + "��");
		lbMonth.setFont(fontMS16);
		lbMonth.setForeground(Color.GRAY);
		gc.gridx = 3;
		gc.gridy = 1;
		gb.setConstraints(lbMonth, gc);
		jpHead.add(lbMonth);
		// ---------------------------------------------------------------------
		jpHead.setBounds(0, 0, 200, 20);

		// ---------------------------------------------------------------------
		// �j���^�C�g���p�p�l���쐬
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
		// ���t
		// ---------------------------------------------------------------------
		int row = 2;
		int col = rightNow.get(Calendar.DAY_OF_WEEK); // ���j����?��:1��:2
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
		// �z�u BorderLayout!!
		// ---------------------------------------------------------------------
		// �R���|�[�l���g�͂����̐����T�C�Y��
		// �R���e�i�T�C�Y�̐���𖞂����悤�ɔz�u����܂��B
		// ---------------------------------------------------------------------
		// NORTH & SOUTH �� ���������Ɉ����L�΂���܂��B
		// �i�Ȃ̂�FlowLayout�����q�ɂ��Ă��v���ʂ�ɂ͂Ȃ�܂���E�E�Ƃقفj
		// EAST & WEST �� ���������Ɉ����L�΂���܂��B
		// CENTER �́A�X�y�[�X���c���Ȃ��悤�ɁA����&�����̗������Ɉ����L�΂���܂�
		// ---------------------------------------------------------------------
		rtnPanel.add(jpHead, BorderLayout.NORTH);
		rtnPanel.add(jpBody, BorderLayout.CENTER);
		return rtnPanel;
	}

	// -------------------------------------------------------------------------
	// �j������
	// -------------------------------------------------------------------------
	public static void hollydayCnv(int pYY, int pMM, Vector wTheseDays) {
		int wHd;
		int wSv;
		Calendar holly = Calendar.getInstance();
		holly.set(Calendar.YEAR, pYY); // �N���Z�b�g����
		holly.set(Calendar.MONTH, pMM - 1); // ���[�P���Z�b�g����
		if (pMM == 1) {
			((DButton) wTheseDays.get(1 - 1)).setholyday("���U");
			// -------------------------------------------------------------------------------
			// ���l�̓��@1���̑�2���j ��2000�N�ȍ~ (�����l�̓��� 1��15��)
			// -------------------------------------------------------------------------------
			if (pYY < 2000) {
				wHd = 15;
			} else {
				holly.set(Calendar.DAY_OF_WEEK, 2); // ���j�� �� �Q�F���j��
				holly.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2); // �Q��ڂ̂��j��
				wHd = holly.get(Calendar.DAY_OF_MONTH);
			}
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("���l�̓�") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("�U��");
		} else if (pMM == 2) {
			if (((DButton) wTheseDays.get(11 - 1)).setholyday("�����L�O��") == true)
				((DButton) wTheseDays.get(11)).setholyday("�U��");
		} else if (pMM == 3) {
			// -------------------------------------------------------------------------------
			// �t���̓�
			// -------------------------------------------------------------------------------
			wHd = (int) (20.8431 + 0.242194 * (pYY - 1980)
					- ((pYY - 1980) / 4));
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("�t���̓�") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("�U��");
		} else if (pMM == 4) {
			if (((DButton) wTheseDays.get(29 - 1)).setholyday("�݂ǂ�̓�") == true)
				((DButton) wTheseDays.get(29)).setholyday("�U��");
		} else if (pMM == 5) {
			((DButton) wTheseDays.get(3 - 1)).setholyday("���@�L�O��");
			// �j���Əj���̊Ԃ䂦�E�E�E�����͌Œ菈���Ƃ���
			((DButton) wTheseDays.get(4 - 1)).setholyday("�����̋x��");
			if (((DButton) wTheseDays.get(5 - 1)).setholyday("�q���̓�") == true)
				((DButton) wTheseDays.get(5)).setholyday("�U��");
		} else if (pMM == 7) {
			// -------------------------------------------------------------------------------
			// �C�̓��@07���̑�3���j ��2003�N�ȍ~ (���C�̓� 7��20��)
			// -------------------------------------------------------------------------------
			if (pYY < 2003) {
				wHd = 20;
			} else {
				holly.set(Calendar.DAY_OF_WEEK, 2); // ���j�� �� �Q�F���j��
				holly.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3); // �R��ڂ̂��j��
				wHd = holly.get(Calendar.DAY_OF_MONTH);
			}
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("�C�̓�") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("�U��");
		} else if (pMM == 9) {
			// -------------------------------------------------------------------------------
			// �h�V�̓��@09���̑�3���j ��2003�N�ȍ~ (�h�V�̓���9��15��)
			// -------------------------------------------------------------------------------
			if (pYY < 2003) {
				wHd = 15;
			} else {
				holly.set(Calendar.DAY_OF_WEEK, 2); // ���j�� �� �Q�F���j��
				holly.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3); // �R��ڂ̂��j��
				wHd = holly.get(Calendar.DAY_OF_MONTH);
			}
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("�h�V�̓�") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("�U��");
			wSv = wHd;
			// -------------------------------------------------------------------------------
			// �H���̓�
			// -------------------------------------------------------------------------------
			wHd = (int) (23.2488 + 0.242194 * (pYY - 1980)
					- ((pYY - 1980) / 4));
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("�H���̓�") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("�U��");
			// �j���Əj���̊Ԃ�����������ꍇ�͍����̏j���ƂȂ�
			if ((wHd - wSv) == 2)
				((DButton) wTheseDays.get(wSv)).setholyday("�����̏j��");

		} else if (pMM == 10) {
			// -------------------------------------------------------------------------------
			// �̈�̓��@10���̑�2���j ��2000�N�ȍ~ (���̈�̓���10��10��)
			// -------------------------------------------------------------------------------
			if (pYY < 2000) {
				wHd = 10;
			} else {
				holly.set(Calendar.DAY_OF_WEEK, 2); // ���j�� �� �Q�F���j��
				holly.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2); // �Q��ڂ̂��j��
				wHd = holly.get(Calendar.DAY_OF_MONTH);
			}
			if (((DButton) wTheseDays.get(wHd - 1)).setholyday("�̈�̓�") == true)
				((DButton) wTheseDays.get(wHd)).setholyday("�U��");
		} else if (pMM == 11) {
			if (((DButton) wTheseDays.get(3 - 1)).setholyday("�����̓�") == true)
				((DButton) wTheseDays.get(3)).setholyday("�U��");
			if (((DButton) wTheseDays.get(23 - 1)).setholyday("�ΘJ���ӂ̓�") == true)
				((DButton) wTheseDays.get(23)).setholyday("�U��");
		} else if (pMM == 12) {
			if (((DButton) wTheseDays.get(23 - 1)).setholyday("�V�c�a����") == true)
				((DButton) wTheseDays.get(23)).setholyday("�U��");
		}
	}

	// -------------------------------------------------------------------------
	JPanel iniControl() {
		// ---------------------------------------------------------------------
		// �R���g���[������
		// ---------------------------------------------------------------------
		Font font9 = new Font("Courier", Font.PLAIN, 9); // �t�H���g�ݒ�
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
			cmbYear.addItem("" + i + "�N");
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
			cmbMonth.addItem("" + i + "��");
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
			cmbDay.addItem("" + i + "��");
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
		// �����������i�ڂ̃{�^��
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
		// default�I�I
		// ---------------------------------------------------------------------
		cmbYear.setSelectedItem(gYY + "�N");
		cmbMonth.setSelectedItem(gMM + "��");
		cmbDay.setSelectedItem(gDD + "��");
		cmbVal.setSelectedItem("1");
		cmbTrm.setSelectedItem("Day");
		cmbPM.setSelectedItem("-");
		// ---------------------------------------------------------------------
		// Action �s Today�{�^�� �t
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
				cmbYear.setSelectedItem(gYY + "�N");
				cmbMonth.setSelectedItem(gMM + "��");
				cmbDay.setSelectedItem(gDD + "��");
				cmbVal.setSelectedItem("1");
				cmbTrm.setSelectedItem("Day");
				cmbPM.setSelectedItem("+");
				repaintMe();
			}
		});
		// ---------------------------------------------------------------------
		// Action �s Prev�{�^�� �t
		// ---------------------------------------------------------------------
		jbPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Prev");
				//TODO	���t���������t�Ȃ猎�����t���A�T�C�����Ă�����@�@20151008
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
		// Action �s Next�{�^�� �t
		// ---------------------------------------------------------------------
		jbNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Next");
				//TODO	���t���������t�Ȃ猎�����t���A�T�C�����Ă�����@�@20151008
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
		// Action �s Cancel�{�^�� �t
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
		// Action �s Go �{�^�� �t
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
		if (day == currentLastDay) //�����J�����g�������Ȃ�E�E�E
			return getLastDay(year, month + diff);//�ړ���̌��̍ŏI����Ԃ�
		return day;//����ȊO�͕ύX�Ȃ��Ȃ̂ŁE�E�E���̂܂ܕԂ�
	}

	public void test(Calendar calendar) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
		String bef1 = df1.format(DateCalc.beginningOfTheMonth(calendar, -1)); // �O������
		String aft1 = df1.format(DateCalc.endOfTheMonth(calendar, -1)); // �O���̖���
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
	// �[�NCheck
	// -------------------------------------------------------------------------
	public static int isLeap(int Year) { // �[�N����
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
		System.out.println(" get�n ");
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
		System.out.println(" �N YEAR        :" + rightNow.get(Calendar.YEAR));
		System.out.println(
				" �� MONTH       :" + (rightNow.get(Calendar.MONTH) + 1));
		System.out.println(
				" �� DAY_OF_MONTH:" + rightNow.get(Calendar.DAY_OF_MONTH));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" ���݂̌��̉��x�ڂ̗j�� DAY_OF_WEEK_IN_MONTH :"
				+ rightNow.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println(" ���݂̌��̉��T�ڂ�     WEEK_OF_MONTH        :"
				+ rightNow.get(Calendar.WEEK_OF_MONTH));
		System.out.println(" ���݂̔N�̉��T�ڂ�     WEEK_OF_YEAR         :"
				+ rightNow.get(Calendar.WEEK_OF_YEAR));
		System.out
				.println("---------------------------------------------------");
		System.out.println(" �j���iSUNDAY=1,MONDAY=2...�jDAY_OF_WEEK     :"
				+ rightNow.get(Calendar.DAY_OF_WEEK));
		System.out.println(" ���݂̔N�̉����ڂ�          DAY_OF_YEAR     :"
				+ rightNow.get(Calendar.DAY_OF_YEAR));

		System.out
				.println("---------------------------------------------------");
		System.out.println(" set�n ");
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
