package kyPkg.mySwing;

import static kyPkg.sql.ISAM.SCHEMA_INI;
import static kyPkg.util.SwingUtil.load2Combo;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTabbedPane;

import kyPkg.cross.CondPanel1;
import kyPkg.cross.CondPanel3;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;

public class CondPanel extends MyPanel {

	private static final long serialVersionUID = 1L;
	protected static final String VTAB = "" + '\u000b'; // �����^�u
	private String gPath_Sys = "";
	private MyPanel frame0;
	private MyLabel LabInfo2;
	private String gCndPath_Idx = "";
	private String gCndPath_Cls = "";
	private MyButton CommandButton5;
	private MyButton cmdSaveAsCond;
	private MyButton ComSaveAs;
	private MyButton CommandButton3;
	private MyButton ComJamin;
	private MyComboBox CondCAL;
	private MyCheckBox checkCom3;
	private MyButton CommAdd;
	private MyTextField textCom3;
	private MyTextField TextCond;
	private ListPanel listQtb1;
	private ListPanel listQtb2;
	private JTabbedPane CondePages;
	private MyLabel lTable;
	private CondPanel3 condPanel3;
	private CondPanel1 condPanel1;
	private CondPanel1 condPanel2;
	private MyLabel lPath;
	private MyLabel lKey;
	private MyLabel lTyp;
	private MyLabel lCom;
	private MyComboBox comboBox3;
	private MyComboBox comboBox4;

	public void copyList2Combo3(List listNext) {
		load2Combo(comboBox3, listNext);
		repaint();
	}

	public void copyList2Combo4(List listNext) {
		load2Combo(comboBox4, listNext);
		repaint();
	}

	public void clear() {
		listQtb1.clear();
		listQtb2.clear();
	}

	public void setlPath(String str) {
		this.lPath.setText(str);
	}

	public CondPanel(String gPath_Sys, MyPanel frame0, MyLabel LabInfo2) {
		super();
		this.gPath_Sys = gPath_Sys;
		this.frame0 = frame0;
		this.LabInfo2 = LabInfo2;
		initGUI();
	}

	private void initGUI() {
		setLayout(null);
		this.setGrid(true);
		comboBox3 = new MyComboBox(0, 0, 250, 20);
		this.add(comboBox3);
		comboBox4 = new MyComboBox(250, 0, 150, 20);
		this.add(comboBox4);
		CondCAL = new MyComboBox(400, 0, 150, 20);
		this.add(CondCAL);
		CommAdd = new MyButton(550, 0, 150, 20, "������ǉ�����");
		this.add(CommAdd);
		checkCom3 = new MyCheckBox(0, 20, 100, 20, "�R�����g");
		textCom3 = new MyTextField(100, 20, 300, 20, "");
		TextCond = new MyTextField(400, 20, 300, 20, "");
		listQtb1 = new ListPanel(new Rectangle(0, 40, 400, 100), false, false);
		listQtb2 = new ListPanel(new Rectangle(400, 40, 300, 100), false,
				false);
		this.add(listQtb1);
		this.add(listQtb2);
		this.add(checkCom3);
		this.add(textCom3);
		this.add(TextCond);
		// ---------------------------------------------------------------------
		CondePages = new JTabbedPane();
		// CondPage1(l)
		CondePages.setBounds(0, 150, 650, 125);
		condPanel1 = new CondPanel1(0, 0, 400, 100, null);
		CondePages.add("�����`", condPanel1);

		condPanel2 = new CondPanel1(0, 0, 400, 100, null);
		CondePages.add("�����a", condPanel2);

		condPanel3 = new CondPanel3(0, 0, 400, 100, null);
		CondePages.add("�`�a�̊֘A", condPanel3);
		// ---------------------------------------------------------------------
		CondePages.setSelectedIndex(0); // �^�u�y�[�W�̑I��
		this.add(CondePages);
		int stab1 = 275;
		lPath = new MyLabel(0, stab1, 200, 20, "LPath  ");
		lTable = new MyLabel(200, stab1, 100, 20, "LTable ");
		lKey = new MyLabel(300, stab1, 50, 20, "LKey   ");
		lTyp = new MyLabel(350, stab1, 50, 20, "LTyp   ");
		lCom = new MyLabel(400, stab1, 200, 20, "LCom   ");
		this.add(lPath);
		this.add(lTable);
		this.add(lKey);
		this.add(lTyp);
		this.add(lCom);
		// ---------------------------------------------------------------------
		CommandButton5 = new MyButton(0, 290, 150, 20, "HML���MyData�쐬");
		cmdSaveAsCond = new MyButton(150, 290, 150, 20, "���j�^�[���o�Ƃ��ĕۑ�");
		ComSaveAs = new MyButton(300, 290, 150, 20, "�Y���h�c�ۑ�");
		CommandButton3 = new MyButton(450, 290, 150, 20, "�������N���A");
		ComJamin = new MyButton(600, 290, 150, 20, "�����m�F");
		this.add(CommandButton5);
		this.add(cmdSaveAsCond);
		this.add(ComSaveAs);
		this.add(CommandButton3);
		this.add(ComJamin);
		comboBox3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBox3_Click();
				repaint();
			}
		});
		comboBox4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBox4_Click();
				repaint();
			}
		});

		listQtb1.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					listQtb1_DblClick(listQtb2);
					repaint();
				}
			}
		});

		listQtb2.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					listQtb2_DblClick();
					repaint();
				}
			}
		});
	}

	private void ComboBox3_Click() {
		textCom3.setText("");
		gCndPath_Cls = gPath_Sys + "/" + comboBox3.getSelectedItem(); // ��cls��
		gCndPath_Cls = FileUtil.normarizeIt(gCndPath_Cls);
		listQtb1.clear();
		listQtb2.clear();
		String regex = "";
		List listNext = ListArrayUtil.dir2List_asc(gCndPath_Cls, regex, false);
		if (listNext != null) {
			load2Combo(comboBox4, listNext);
		}
	}

	private void ComboBox4_Click() {
		gCndPath_Idx = gCndPath_Cls + "/" + comboBox4.getSelectedItem();// '��term��
		// String xStr = (String)comboBox4.getSelectedItem();
		// int xPoz = xStr.indexOf(vbTab);
		// ---------------------------------------------------------------------
		// �����ݒ�p�̐ݖ⁕�I�������[�h
		// ---------------------------------------------------------------------
		setHourglass(true);
		LabInfo2.setText("�ݖ⍀�ڌ������E�E�E");
		setEnabled(true);
		setVisible(true);
		listQtb1.clear();
		listQtb2.clear();
		listQtb1.setVisible(false);
		kyPkg.mySwing.EnqRes.Dimension_Load2(gCndPath_Idx, listQtb1, "", "");
		listQtb1.setVisible(true);
		setHourglass(false);
		frame0.setVisible(true);
		LabInfo2.setText("�n�j�I");
		textCom3.setText(
				comboBox3.getSelectedItem() + "" + comboBox4.getSelectedItem());
	}

	private void listQtb1_DblClick(InfListPanel outlet) {
		// String xCnn="";
		String xSql = "";
		List xVector;
		int xmax;
		// ---------+-------
		String xCom;
		String xComm;
		String xParm;
		// ---------+-------
		String mKey;
		String mTyp;
		// ---------+-------
		String wChunk[];
		String xPrm;
		int xLdx;
		// ---------+-------
		String mPth = "";
		String mTbl = "";
		String xFld = "";
		String xKey = "";
		String xVal = "";
		String xTyp = "";
		String xCol = "";
		String xLen = "";
		// ---------+-------
		String wProxy;
		String wOpts[];
		String wOpt;
		xLdx = listQtb1.getSelectedIndex();
		xCom = (String) listQtb1.getElementAt(xLdx);
		xPrm = (String) listQtb1.getValueX(xLdx);
		// �၄
		// xCom:�N��~����
		// xPrm:�N��~����  Z:/s2/rx/Enquetes/NQ/�s�`�f��������/2009 ASM CELL SINGLE
		System.out.println("xCom:" + xCom);
		System.out.println("xPrm:" + xPrm);
		wChunk = xPrm.split(vbTab);
		if (wChunk.length == 0)
			return;
		mPth = wChunk[1]; // �C���f�b�N�X�̃p�X�Ɠ���
		mTbl = wChunk[2]; // ALIAS ASM�Ȃ�
		mKey = wChunk[3]; // AQA <Mother KEY>
		wProxy = wChunk[4]; // $
		mTyp = wChunk[5]; // SINGLE
		System.out.println("mPth   :" + mPth);
		System.out.println("mTbl   :" + mTbl);
		System.out.println("mKey   :" + mKey);
		System.out.println("wProxy :" + wProxy);
		System.out.println("mTyp   :" + mTyp);
		String wQtb = "";
		if (FileUtil.isExists(mPth + "/QTB1.TXT"))
			wQtb = "QTB1#TXT";
		if (FileUtil.isExists(mPth + "/QTB2.TXT"))
			wQtb = "QTB2#TXT";
		if (wQtb.equals("")) {
			// MsgBox "�C���f�b�N�X�t�@�C����������܂���ł����B"
			System.out.println("#ERROR! �C���f�b�N�X�t�@�C����������܂���ł����B:" + mPth);
			return;
		}
		// -----------------------------------------------------------
		lCom.setText(xCom);
		lKey.setText(mKey);
		lTyp.setText(mTyp);
		// -----------------------------------------------------------
		listQtb2.clear();
		// -----------------------------------------------------------
		if (!FileUtil.isExists(mPth + "/" + SCHEMA_INI)) {
			String msg = "�J�e�S���[�f�[�^�����݂��܂���ł���" + vbCrLf + mPth + "/"
					+ SCHEMA_INI + vbCrLf + "���̃J�e�S���[��I��ł��������B";
			System.out.println("#ERROR! " + msg);
			frame0.setVisible(true);
			return;
		}
		listQtb1.setVisible(false);
		listQtb2.setVisible(false);
		TextCond.setVisible(false);
		setHourglass(true);
		// -----------------------------------------------------------
		// �I�����ꂽ���ڃ^�C�v���A
		// -----------------------------------------------------------
		if (mTyp.equals("SINGLE") || mTyp.equals("MULTI")) {
			CondCAL.setVisible(true);
			CommAdd.setVisible(false);
			TextCond.setVisible(false);
			listQtb2.setVisible(true);
			TextCond.setText("");
			CondCAL.setSelectedItem("=");
			// -------------------------------------------------------
			xSql = "SELECT Nam,Key,Val,Typ,Col,Len,Opt FROM " + wQtb
					+ " WHERE Mot = '" + mKey + "' ORDER BY Srt";
			// xCnn =
			// "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir=" +
			// mPth +
			// ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
			xVector = ODBC_SQL2VECTOR(xSql, mPth);
			xmax = xVector.size();
			listQtb2.clear();
			if (xmax > 0) {
				for (Iterator iterator = xVector.iterator(); iterator
						.hasNext();) {
					List xVlist = (List) iterator.next();
					xKey = (String) xVlist.get(1);
					xVal = (String) xVlist.get(2);
					xTyp = (String) xVlist.get(3);
					xCol = ((Integer) xVlist.get(4)).toString();
					xLen = ((Integer) xVlist.get(5)).toString();
					wOpt = (String) xVlist.get(6);
					if (wOpt != null) {
						wOpts = wOpt.split(":");
						if (wOpts.length >= 0) {
							if (!wOpts[0].trim().equals(""))
								xFld = wOpts[0];
						}
					}
					xFld = wProxy;
					xComm = xVlist.get(0) + "                             ";
					xParm = mPth + vbTab + mTbl + vbTab + xFld + vbTab + xKey
							+ vbTab + xVal + vbTab + xTyp + vbTab + xCol + vbTab
							+ xLen;
					if (!xComm.equals("")) {
						outlet.addElement(xComm + VTAB + xParm);
					}
				}
				// -------------------------------------------------------
				// Formula for �������o
				// -------------------------------------------------------
				CondCAL.removeAll();
				CondCAL.addItem("=");
				CondCAL.addItem("<>");
				CondCAL.setSelectedIndex(0);
			}
		} else if (mTyp.equals("COUNT") || mTyp.equals("INTEGER")
				|| mTyp.equals("REAL")) {
			CondCAL.setVisible(true);
			CommAdd.setVisible(true);
			TextCond.setVisible(true);
			listQtb2.setVisible(false);
			TextCond.setText("0");
			// -------------------------------------------------------
			// Formula for �������o
			// -------------------------------------------------------
			CondCAL.removeAll();
			CondCAL.addItem("=");
			CondCAL.addItem("<>");
			CondCAL.addItem(">");
			CondCAL.addItem(">=");
			CondCAL.addItem("<");
			CondCAL.addItem("<=");
			CondCAL.addItem("IN");
			CondCAL.addItem("NOT IN");
			CondCAL.setSelectedIndex(0);
		} else if (mTyp.equals("ALPHA")) {
			// ���Ƃōl���悤�I�I���̂܂܂ł͑ʖځI
			String qCnn = ""; // �A���P�[�g�f�[�^�ւ̐ڑ��p�����[�^
			String qTbl = ""; // �A���P�[�g�f�[�^�A�e�[�u��
			String qDat = ""; // �A���P�[�g�f�[�^�A�f�[�^�t�B�[���h
			String qKey = ""; // �A���P�[�g�f�[�^�A�L�[
			String qCnd = ""; // �A���P�[�g�f�[�^�A�R���f�B�V����
			if (mTbl.toUpperCase().equals("ALIAS")) {
				// ParseAlias �G�C���A�X�t�@�C����ǂݍ���
				if (!FileUtil.isExists(mPth + "/ALIAS.TXT")) {
					String msg = "�G�C���A�X��������܂���ł����F" + mPth;
					// MsgBox msg
					System.out.println("#ERROR! " + msg);
					return;
				}
				ParseAlias(mPth + "/ALIAS.TXT", qCnn, qTbl, qDat, qKey, qCnd);
			}
			CondCAL.setVisible(true);
			CommAdd.setVisible(true);
			TextCond.setVisible(true);
			listQtb2.setVisible(true);
			TextCond.setText("");
			// -------------------------------------------------------
			if (qCnn.equals("")) {
				qCnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir="
						+ mPth
						+ ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
				qTbl = "[" + mTbl + "#TXT]";
			}
			xSql = "SELECT [" + mKey + "] FROM " + qTbl + " " + " GROUP BY ["
					+ mKey + "] ORDER BY [" + mKey + "] ;";
//			System.out.println("DEBUG@20110627 Sql:" + xSql);
			// -------------------------------------------------------
			xVector = ODBC_SQL2VECTOR(xSql, mPth);
			xmax = xVector.size();

			listQtb2.setVisible(false);
			listQtb2.clear();
			if (xmax > 0) {
				for (Iterator iterator = xVector.iterator(); iterator
						.hasNext();) {
					List<String> xVlist = (List) iterator.next();
					xComm = xVlist.get(0) + "                             ";
					xKey = mKey;
					xVal = xVlist.get(0);
					xTyp = "ALPHA";
					// xCol = "???"
					// xLen = "???"
					// //xParm = xComm + vbTab + mKey + vbTab + xVal
					xParm = mPth + vbTab + mTbl + vbTab + xFld + vbTab + xKey
							+ vbTab + xVal + vbTab + xTyp + vbTab + xCol + vbTab
							+ xLen;
					if (!xComm.equals("")) {
						outlet.addElement(xComm + VTAB + xParm);
					}
				}
			}
			listQtb2.setVisible(true);
			// -------------------------------------------------------
			// Formula for �������o
			// -------------------------------------------------------
			CondCAL.removeAll();
			CondCAL.addItem("=");
			CondCAL.addItem("<>");
			CondCAL.addItem(">");
			CondCAL.addItem(">=");
			CondCAL.addItem("<");
			CondCAL.addItem("<=");
			CondCAL.addItem("Like");
			CondCAL.addItem("Like XXX?");
			CondCAL.addItem("Like ?XXX");
			// CondCAL.addItem("IN");
			// CondCAL.addItem("NOT IN");
			CondCAL.setSelectedIndex(0);
		} else {
			// MsgBox "type error!!!" & mTyp
		}
		listQtb1.setVisible(true);
		setHourglass(false);
		repaint();
	}

	private void listQtb2_DblClick() {
		String xPfx = "";
		if (checkCom3.isSelected()) {
			xPfx = textCom3.getText();
			// xPfx = NameCheck(xPfx, 20) //TODO ��Ŏ������Ă���
			textCom3.setText(xPfx);
		}
		String pPrm1 = "";
		String pPrm2 = "";
		int xLdx = listQtb2.getSelectedIndex();
		pPrm1 = (String) listQtb2.getValueX(xLdx, 0);
		pPrm2 = (String) listQtb2.getValueX(xLdx, 1);
		System.out.println("pPrm1:" + pPrm1);
		System.out.println("pPrm2:" + pPrm2);
		// pPrm1 = listQtb2.List(xLdx, 0)
		// pPrm2 = listQtb2.List(xLdx, 1)
		if (CondePages.getSelectedIndex() > 1)
			CondePages.setSelectedIndex(0);
		if (CondePages.getSelectedIndex() == 0) {
			Add2pCtr(pPrm1, pPrm2, condPanel1.getListPanel(),
					CondCAL.getSelectedItem(), xPfx + lCom.getText());
		} else if (CondePages.getSelectedIndex() == 1) {
			Add2pCtr(pPrm1, pPrm2, condPanel2.getListPanel(),
					CondCAL.getSelectedItem(), xPfx + lCom.getText());
		}
	}

	private void Add2pCtr(String pPrm1, String pPrm2, InfListPanel pCtr,
			String pCLQ, String pComm) {
		String[] wChunk = null;
		String xPth = "";
		String xTbl = "";
		String xDat = "";
		String xKey = "";
		String xVal = "";
		String xTyp = "";
		String xCol = "";
		String xLen = "";
		String xCm1 = "";
		String xCm2 = "";
		xCm1 = "�w" + pComm.trim() + "�x";
		xCm2 = "�w" + pPrm1.trim() + "�x";
		wChunk = pPrm2.split(vbTab);
		if (wChunk == null || wChunk.length == 0)
			return;
		xPth = wChunk[0];
		xTbl = wChunk[1];
		xDat = wChunk[2];
		xKey = wChunk[3];
		xVal = wChunk[4];
		xTyp = wChunk[5];
		xCol = wChunk[6];
		xLen = wChunk[7];
		System.out.println(
				"debug 20101128 ########################################");
		System.out.println("xCm1:" + xCm1);
		System.out.println("xCm2:" + xCm2);

		System.out.println("xPth:" + xPth);
		System.out.println("xTbl:" + xTbl);
		System.out.println("xDat:" + xDat);
		System.out.println("xKey:" + xKey);
		System.out.println("xVal:" + xVal);
		System.out.println("xTyp:" + xTyp);
		System.out.println("xCol:" + xCol);
		System.out.println("xLen:" + xLen);

		String xComm = "";
		String xParm = "";
		String xCnd = "";
		String Msg = "";
		int vbOKOnly = 2;
		int vbCritical = 4;
		int Style = 0;
		if (xTyp.equals("INTEGER") || xTyp.equals("REAL")
				|| xTyp.equals("COUNT")) {
			// ���l�̏ꍇ
			if (IsNumeric(xVal) == false) {
				Msg = "��r����l�����l�ł͂���܂���I\n���l���w�肵�ĉ������B";
				Style = vbOKOnly + vbCritical;
				MsgBox(Msg, Style, "�x��!");
				return;
			}
			if (pCLQ.equals("Like") || pCLQ.equals("Like XXX?")
					|| pCLQ.equals("Like ?XXX")) {
				Msg = "��r����l�����l�Ȃ̂�Like���Z�q�͎g���܂���I\n���̉��Z�q���w�肵�ĉ������B";
				Style = vbOKOnly + vbCritical;
				MsgBox(Msg, Style, "�x��!");
				return;
			}
		} else if (xTyp.equals("MULTI") || xTyp.equals("SINGLE")
				|| xTyp.equals("ALPHA")) {
			if (pCLQ.equals("Like")) {
				xVal = "'*" + StripChar(xVal, "'").trim() + "*'";
			} else if (pCLQ.equals("Like XXX?")) {
				xVal = "'" + StripChar(xVal, "'").trim() + "*'";
			} else if (pCLQ.equals("Like ?XXX")) {
				xVal = "'*" + StripChar(xVal, "'").trim() + "'";
			} else {
				if (xVal.equals(""))
					xVal = "NULL";
				if (xVal.toUpperCase().equals("NULL")) {
					xVal = "NULL";
				} else {
					xVal = "'" + StripChar(xVal, "'") + "'";
				}
			}
		} else {
			return;
		}
		xCnd = "";
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+;
		if (pCLQ.equals("Like")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "��" + xCm2 + "�Ƃ����������܂܂�����";
		} else if (pCLQ.equals("Like XXX?")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "���A" + xCm2 + "�Ƃ��������Ŏn�܂����";
		} else if (pCLQ.equals("Like ?XXX")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "���A" + xCm2 + "�Ƃ��������ŏI������";
		} else if (pCLQ.equals("IN")) {
			xCnd = " IN (" + xVal + ") ";
			xComm = xCm1 + "���A" + xCm2 + " �̂����ǂꂩ";
		} else if (pCLQ.equals("NOT IN")) {
			xCnd = " NOT IN (" + xVal + ") ";
			xComm = xCm1 + "���A" + xCm2 + "�̂����ǂ�ł��Ȃ�����";
		} else if (pCLQ.equals("=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "���A" + xCm2 + "�ł������";
		} else if (pCLQ.equals("<>")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "���A" + xCm2 + "�ł͂Ȃ�����";
		} else if (pCLQ.equals(">")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "���A" + xCm2 + "���傫������";
		} else if (pCLQ.equals(">=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "���A" + xCm2 + "�ȏ�ł������";
		} else if (pCLQ.equals("<")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " ���A" + xCm2 + "��菬��������";
		} else if (pCLQ.equals("<=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " ���A" + xCm2 + "�ȉ��ł������";
		} else {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " " + pCLQ + " " + xCm2 + "�ł������";
		}
		xParm = pPrm2 + vbTab + xCnd;
		// '-----+---------+---------+---------+---------+---------+---------+---------+---------+---------+;
		// int i ;
		boolean xFlg = false;
		// �������̂��ǉ�����Ȃ��悤�ɂȂ��Ă���E�E�E�̂��Ȃ��Ԃ�
		List list = pCtr.getList();
		System.out.println("forDEBUG xComm ==> " + xComm);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String object = (String) iterator.next();
			System.out.println("forDEBUG selected ==> " + object);
		}
		// For i = 0 To pCtr.ListCount - 1;
		// if pCtr.List(i, 0) = xComm Then;
		// xFlg = True;
		// Exit For;
		// End if;
		// Next i;
		if (xFlg == false) {
			pCtr.addElement(xComm + VTAB + xParm); // �ǂ����낤�H�H��2010-11-28
			// pCtr.AddItem //wStr;
			// pCtr.List(pCtr.ListCount - 1, 0) = xComm;
			// pCtr.List(pCtr.ListCount - 1, 1) = xParm;
		}
	}

	private boolean IsNumeric(String val) {
		// TODO ���b�c�P��
		try {
			Integer.parseInt(val);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private String StripChar(String src, String strip) {
		// TODO ���b�c�P��
		return src;
	}

	private void ParseAlias(String pFile, String gd_Cnn, String gd_Tbl,
			String gd_Fld, String gd_Key, String gd_Cnd) {

	}
	// private String NameCheck(String zStr, int xLim, boolean pNarrow) {
	// return "";
	// }
	// private MyPanel getCondPanel() {
	// MyPanel thisP = new MyPanel(null);
	// return thisP;
	// }

}
