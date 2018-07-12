package kyPkg.cross;

import static kyPkg.util.SwingUtil.load2Combo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import globals.ResControl;
import kyPkg.mySwing.CondPanel;
import kyPkg.mySwing.ListPanelW;
import kyPkg.mySwing.MyButton;
import kyPkg.mySwing.MyCheckBox;
import kyPkg.mySwing.MyComboBox;
import kyPkg.mySwing.MyLabel;
import kyPkg.mySwing.MyPanel;
import kyPkg.mySwing.MyTextField;
import kyPkg.panelsc.Pn_Scaffold;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;
import kyPkg.util.FontUtility;
import kyPkg.util.SwingUtil;

//============================================================================
// �ėp�N���X�W�v�@java version
//============================================================================
public class Pn_CrossAp extends Pn_Scaffold {

	public static void main(String[] argv) {
		standAlone(new Pn_CrossAp(), "Cross");
	}

	private boolean gDirty1 = false;
	private boolean gDirty2 = false;
	private String gPath_Sys = "";
	private String gPath_Cls = "";
	private String gPath_Idx = "";

	private static final long serialVersionUID = -3066425890995680687L;
	private MyComboBox cmbRepos;
	private MyComboBox comboBox1;
	private MyComboBox comboBox2;

	private MyButton ComQuit;
	private MyButton ComResAll;
	private MyButton comGoGo;
	private MyButton CommandButton1;

	private MyPanel frame1;
	private MyPanel frame0;
	private MyLabel LabInfo2;
	private JTabbedPane multiPage1;
	private MyTextField textTitle;
	private ListPanelW list1;
	private ListPanelW list3;

	private MyCheckBox checkCom1;
	private MyCheckBox checkCom2;

	private MyTextField textCom1;
	private MyTextField textCom2;

	private CondPanel condPanel;
//	private static final String REPOPATH = ResControl.REPOSITORY;

	// -------------------------------------------------------------------------
	// �s�R���X�g���N�^�t
	// -------------------------------------------------------------------------
	public Pn_CrossAp() {
		super(750, 500);
		createGUI(); // GUI���쐬
	}

	// -------------------------------------------------------------------------
	// create GUI
	// -------------------------------------------------------------------------
	void createGUI() {
		FontUtility.setFont(new Font("Monospaced", Font.PLAIN, 10),false);
		LabInfo2 = new MyLabel(0, 0, 720, 20, "��ނ�I�����Ă�������");

		frame0 = new MyPanel(null);
		frame0.add(new MyLabel(0, 0, 700, 20, "��ށA�����A����ю�����I�����Ă��������B"));
		frame0.add(new MyLabel(0, 20, 100, 20, "��ށF"));
		frame0.add(new MyLabel(0, 40, 100, 20, "�����F"));
		frame0.add(new MyLabel(350, 40, 100, 20, "�����F"));
		cmbRepos = new MyComboBox(50, 20, 250, 20);
		comboBox1 = new MyComboBox(50, 40, 250, 20);
		comboBox2 = new MyComboBox(400, 40, 250, 20);
		frame0.add(cmbRepos);
		frame0.add(comboBox1);
		frame0.add(comboBox2);
		comboBox2.setVisible(false);
		// ---------------------------------------------------------------------
		// �^�u�y�[�
		// ---------------------------------------------------------------------
		multiPage1 = new JTabbedPane();
		multiPage1.setBounds(0, 60, 705, 335);
		// ---------------------------------------------------------------------
		// �\���\���̑I��
		// ---------------------------------------------------------------------
		frame1 = new MyPanel(null);
		frame1.setBounds(0, 0, 1150, 400);
		frame1.setGrid(true);
		// ---------------------------------------------------------------------
		frame1.add(new MyLabel(0, 0, 100, 20, "�\���̑I��"));
		checkCom1 = new MyCheckBox(100, 0, 100, 20, "�R�����g");
		textCom1 = new MyTextField(200, 0, 350, 20, "");
		MyButton ComClr1 = new MyButton(600, 0, 90, 20, "Clear");
		MyButton CommChk1 = new MyButton(700, 0, 90, 20, "check");
		MyButton CommRes1 = new MyButton(800, 0, 90, 20, "unCheck");
		list1 = new ListPanelW(0, 20, 700, 130, false); // + List2
		list1.setRightClear(false);
		// ListDst1
		frame1.add(checkCom1);
		frame1.add(textCom1);
		frame1.add(ComClr1);
		frame1.add(CommChk1);
		frame1.add(CommRes1);
		frame1.add(list1);
		// ---------------------------------------------------------------------
		int geta = 150;
		frame1.add(new MyLabel(0, geta, 100, 20, "�\���̑I��"));
		checkCom2 = new MyCheckBox(100, geta, 100, 20, "�R�����g");
		textCom2 = new MyTextField(200, geta, 350, 20, "");
		MyButton ComClr2 = new MyButton(600, geta, 90, 20, "Clear");
		MyButton CommChk2 = new MyButton(700, geta, 90, 20, "check");
		MyButton CommRes2 = new MyButton(800, geta, 90, 20, "unCheck");
		list3 = new ListPanelW(0, geta + 20, 700, 130, false); // +List4?
		list3.setRightClear(false);
		// ListDst2
		frame1.add(checkCom2);
		frame1.add(textCom2);
		frame1.add(ComClr2);
		frame1.add(CommChk2);
		frame1.add(CommRes2);
		frame1.add(list3);
		// ---------------------------------------------------------------------
		// �����ݒ���
		// ---------------------------------------------------------------------
		condPanel = new CondPanel(gPath_Sys, frame0, LabInfo2);
		// ---------------------------------------------------------------------
		// �W�v�\�^�C�g���Əo�͏���
		// ---------------------------------------------------------------------
		MyPanel frameX = new MyPanel(null);
		frameX.add(new MyLabel(0, 0, 700, 20, "�W�v�[�^�C�g���@(�ҏW�\)"));
		textTitle = new MyTextField(0, 20, 400, 20, "");
		frameX.add(textTitle);
		// ---------------------------------------------------------------------
		// �^�u�y�[�(�^�u�ǉ�)
		// ---------------------------------------------------------------------
		MyPanel tab1 = new MyPanel(null);
		tab1.add(frame1);
		multiPage1.add("�\���\���̑I��", tab1);
		multiPage1.add("���o����", condPanel);
		multiPage1.add("�W�v�\�^�C�g���Əo�͏���", frameX);
		// ---------------------------------------------------------------------
		// �{�^���p�l��
		// ---------------------------------------------------------------------
		MyPanel btnPanel = new MyPanel(0, 400, 700, 50, null);
		ComQuit = new MyButton(0, 0, 100, 20, "Quit");
		ComResAll = new MyButton(100, 0, 100, 20, "�S���Z�b�g");
		comGoGo = new MyButton(250, 0, 150, 20, "��\���s");
		CommandButton1 = new MyButton(400, 0, 300, 20, "�I�����ڂ̊m�F����\��");
		btnPanel.add(ComQuit);
		btnPanel.add(ComResAll);
		btnPanel.add(comGoGo);
		btnPanel.add(CommandButton1);
		frame0.add(multiPage1);
		frame0.add(btnPanel);
		add(frame0, BorderLayout.CENTER);
		pnlSouth.pnlS.add(LabInfo2);
		initComboBox0();
		cmbRepos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initComboBox1();
				repaint();
			}
		});
		comboBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBox1_Click();
				repaint();
			}
		});
		comboBox2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBox2_Click();
				repaint();
			}
		});
		// ---------------------------------------------------------------------
		// Button
		// ---------------------------------------------------------------------
		comGoGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						CommandButton1_Click();
					}
				};
				th1.start();
				repaint();
			}
		});
	}

	private void initComboBox0() {
		List listRepos = ListArrayUtil.file2List(ResControl.REPOSITORY);
		SwingUtil.load2Combo(cmbRepos, listRepos);
	}

	private void initComboBox1() {
		String val = (String) cmbRepos.getSelectedItem();
		String[] array = val.split("\t");
		gPath_Sys = array[1];
		System.out.println("*** gPath_Sys>>" + gPath_Sys);
		String regex = "";
		List listNext = kyPkg.uFile.ListArrayUtil.dir2List_asc(gPath_Sys, regex, false);
		if (listNext != null) {
			this.copyList2Combo1(listNext);
			condPanel.copyList2Combo3(listNext);
		}
	}

	public void copyList2Combo1(List listNext) {
		load2Combo(comboBox1, listNext);
	}

	public void copyList2Combo2(List listNext) {
		load2Combo(comboBox2, listNext);
	}

	private void ComboBox1_Click() {
		// If Me.Frame1.Left <> 0 Then Call CommandButton1_Click
		textTitle.setText("");
		textCom1.setText("");
		textCom2.setText("");
		boolean flag = false;
		if (flag) {
			// If Me.ListQtb3.ListCount <> 0 Then
			String msg = "���o�������N���A���܂����H";
			int option = JOptionPane.showConfirmDialog((Component) null, "�m�F",
					msg, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				// Call ComResAll_Click
			}
			// End If
		}
		list1.clear();
		list3.clear();

		// XXX path �𐳋K�����Ă���

		gPath_Cls = gPath_Sys + "/" + comboBox1.getSelectedItem(); // '��cls��
		gPath_Cls = FileUtil.normarizeIt(gPath_Cls);

		// ���łɏ����ݒ肪�g���Ă�����A����������
		// Me.ComboBox3 = Me.ComboBox1
		System.out.println("gPath_Cls>>" + gPath_Cls);
		String regex = "";
		List listNext = kyPkg.uFile.ListArrayUtil.dir2List_dsc(gPath_Cls, regex,false);// �~���\�[�g
		if (listNext != null) {
			// SwingUtil.copyList2Combo(comboBox2, listNext);
			this.copyList2Combo2(listNext);
			condPanel.copyList2Combo4(listNext);
			// SwingUtil.copyList2Combo(condPanel.getComboBox4(),listNext);
		}
		// '�����ݒ葤�̃R���{������������ �����ӁI�R���{�S�̓R���{�R�Ƃ��������Ă��܂�
		condPanel.clear();
		// frame2.listQtb1.clear();
		// frame2.listQtb2.clear();
		// If Me.ListQtb3.ListCount <> 0 Then
		//  
		// End If
		comboBox2.setVisible(true);
	}

	public boolean msgBox(String title, String msg) {
		int option = JOptionPane.showConfirmDialog((Component) this, title,
				msg, JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	// private void ComResAll_Click() {
	//
	// }

	private void ComboBox2_Click() {
		// If Me.Frame1.Left <> 0 Then Call CommandButton1_Click
		// if (msgBox("�m�F","���o�������N���A���܂����H")){
		// ComResAll_Click();
		// }
		list1.clear();
		list3.clear();
		// XXX path�@�𐳋K������
		gPath_Idx = gPath_Cls + "/" + comboBox2.getSelectedItem(); // ��term��
		gPath_Idx = FileUtil.normarizeIt(gPath_Idx);
		// -----------------------------------------------------------
		// '�����A���P�[�g���g���Ă���ꍇ�̂ݘA��
		// -----------------------------------------------------------
		// if (comboBox1.getSelectedItem() == condPanel.getComboBox3()
		// .getSelectedItem()) {
		// // Me.ComboBox4 = Me.ComboBox2
		// }
		// -----------------------------------------------------------
		LabInfo2.setText("���΂炭���҂����������i�p�����[�^��ǂݍ���ł��܂��j");
		frame1.setVisible(false);
		setHourglass(true);
		// frame2.lPath.setText(gPath_Idx);
		condPanel.setlPath(gPath_Idx);
		list1.setVisible(false);
		list3.setVisible(false);
		kyPkg.mySwing.EnqRes.Dimension_Load2(gPath_Idx, list1, "", "");
		kyPkg.mySwing.EnqRes.Dimension_Load2(gPath_Idx, list3, "", "");
		list1.setVisible(true);
		list3.setVisible(true);
		frame1.setVisible(true);
		LabInfo2.setText("�p�����[�^�ǂݍ��݊������܂����B");
		setHourglass(false);

		multiPage1.setSelectedIndex(0); // tabPane �̑���

		textTitle.setText("�p�o�q�A���P�[�g�N���X");
		textCom1.setText(comboBox1.getSelectedItem() + "_"
				+ comboBox2.getSelectedItem() + "_");
		textCom2.setText(comboBox1.getSelectedItem() + "_"
				+ comboBox2.getSelectedItem() + "_");
		// CommQuick.setVisible(false);
		// If Dir(gPath_Idx + "\qtb2.txt") = "" Then Me.CommQuick.Visible = True
		// if (!FileUtil.isExists(gPath_Idx + "/qtb2.txt"))
		// CommQuick.setVisible(true);

	}

	// Function NameCheck(zStr As String, xLim As Integer, Optional pNarrow) As
	// String
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// ' �T�v : �I�u�W�F�N�g���̂��`�F�b�N�����K�����܂�
	// ' ���� : zStr �������镶���� xLim:�ő啶����
	// ' �߂�l : �ϊ����ꂽ������
	// ' �g�p�� : wStr = NameCheck(Sorce,30)
	// 'Print NameCheck("HML (A�O���[�v����l)", 30)
	// '--------------------------------------------------------------------------------------
	// 1997 YUASA --
	// ' ��ACCESS OBJECT�̖��O��t���邽�߂̃K�C�h���C����
	// ' �����͔��p�� �U�S �����ȉ��ł��B(�S�p�R�Q����)
	// ' 1234567890123456789012345678901234567890123456789012345678901234<���̈ʂ̕�
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// ' ��TRANSACT-SQL��
	// ' �����͔��p�łP�`�R�O�o�C�g�i�S�p�P�T�����j
	// ' SQL:�ŏ��̕����͊���(Space�ȊO)�A�p���A����_@#�ɂȂ�܂�
	// ' ���Ŏn�܂鎯�ʎq�͕ϐ��A���Ŏn�܂鎯�ʎq�͈ꎞ�e�[�u���ł�
	// ' �Q�����ȍ~�́A�����A�p���A�����A�܂��͋L��$#_�ɂȂ�܂�
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// ' ��������ׂ�������
	// ' ���Z�q
	// ' ��؂蕶�� �s���I�h (.)�A���Q�� (!)�A�o�b�N �N�H�[�e�[�V���� (')�Ȃ�
	// ' ���� �p������ ([ ]) �Ȃ�
	// ' ���䕶�����䕶�� (ASCII �l 0 �` 31) ���܂߂邱�Ƃ͂ł��܂���B
	// ' Space�������Ă���
	// ' �L�[���[�h�iIF�ATHEN�AELSE�j�͕s��
	// ' "'`/*-+=<>()[]{}!?|:;.,\&%~^
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// ' xx 1.�S�p�����p�ϊ�
	// ' 2.�g�[�N��������
	// ' 3.�擪�̕����`�F�b�N
	// ' 4.�����񒷂𒲐�
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// 'wStr = Translate(Sorce, "�i�j�����m�n", "()<>[]") '�����񒆂̊��ʂ�S�p�����ɕϊ����܂�
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// Dim xStr As String
	// Dim wPos As Integer
	// Dim wNXT As Integer
	// Dim wStr As String
	// Dim xIdx As Integer
	// If IsMissing(pNarrow) Then pNarrow = False
	// xStr = " ""'`/*-+=<>()[]{}!?|:;.,\&%~^" '�����Ώە���
	// If pNarrow = True Then zStr = Trim(StrConv(zStr, vbNarrow)) '��U���ׂĂ𔼊p������
	// zStr = Translate(zStr, "�y�z�u�v�w�x�y�z�u�v�w�x", "()<>[]�i�j�����m�n")
	// '�����񒆂̊��ʂ�S�p�����ɕϊ����܂�
	// For xIdx = 1 To Len(xStr) Step 1
	// wStr = ""
	// wNXT = 1
	// wPos = InStr(wNXT, zStr, Mid(xStr, xIdx, 1))
	// While wPos > 0
	// Debug.Print "wPos:" & wPos
	// If wPos > wNXT Then wStr = wStr & Mid(zStr, wNXT, wPos - wNXT)
	// wNXT = wPos + 1
	// wPos = InStr(wNXT, zStr, Mid(xStr, xIdx, 1))
	// Wend
	// zStr = wStr & Mid(zStr, wNXT, (Len(zStr) - wNXT + 1))
	// Next xIdx
	// wStr = zStr
	// If Len(wStr) > xLim Then wStr = Mid(wStr, 1, xLim)
	// NameCheck = wStr
	// End Function

	protected int MsgBox(String msg) {
		// TODO ���b�c�P��
		return 0;
	}

	protected int MsgBox(String msg, int style, String caption) {
		// TODO ���b�c�P��
		return 0;
	}

	private void CommandButton1_Click() {

		setHourglass(true);
		comGoGo.setVisible(false);
		// DoEvents
		multiPage1.setSelectedIndex(0);
		// Me.ScrollBar1.value = 0
		// Me.ScrollBar2.value = 0

		// Dim inlet1 As control
		// Dim outlet1 As control
		// Dim inlet2 As control
		// Dim outlet2 As control
		// Set inlet1 = Me.List2
		// Set outlet1 = Me.ListDst1
		// Set inlet2 = Me.List4
		// Set outlet2 = Me.ListDst2
		if (frame1.getLocation().x == 0) {
			for (int i = 0; i < 350; i += 2) {
				frame1.setLeft(0 - i);
				try {
					// Thread.sleep(1);
				} catch (Exception e) {
				}
				frame1.repaint();
			}
			repaint();

			CommandButton1.setText("�\���\���̐ݒ�ɖ߂�");
			frame1.setLeft(-252);
			frame1.setLeft(-350);
			// inlet1.Enabled = False
			// inlet2.Enabled = False
			// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
			if (gDirty1) {
				gDirty1 = false;
				LabInfo2.setText("�\���v�f���������E�E�E");
				// DoEvents
				// outlet1.Clear
				// For i = 0 To inlet1.ListCount - 1
				// xStr = inlet1.List(i, 0) & inlet1.List(i, 1)
				// Call ListDetails(xStr, outlet1)
				// Next i
				// Call setSelectAll(outlet1)
				LabInfo2.setText("�s�v�ȍ��ڂ̓`�F�b�N�}�[�N���O���Ă�������");
				LabInfo2.setText("�W�v�[�^�C�g���Əo�͏������w�肵�Ă�������");
				// DoEvents
			}
			// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
			if (gDirty2) {
				gDirty2 = false;
				LabInfo2.setText("�\���v�f���������E�E�E");
				// DoEvents
				// outlet2.Clear
				// For i = 0 To inlet2.ListCount - 1
				// xStr = inlet2.List(i, 0) & inlet2.List(i, 1)
				// Call ListDetails(xStr, outlet2)
				// Next i
				// Call setSelectAll(outlet2)
				LabInfo2.setText("�s�v�ȍ��ڂ̓`�F�b�N�}�[�N���O���Ă�������");
				LabInfo2.setText("�W�v�[�^�C�g���Əo�͏������w�肵�Ă�������");
				// DoEvents

			}

		} else {
			for (int i = 350; i >= 0; i -= 2) {
				frame1.setLeft(i);
				try {
					// Thread.sleep(1);
				} catch (Exception e) {
				}
				frame1.repaint();
			}
		}
		repaint();
		setHourglass(false);
		comGoGo.setVisible(true);
	}

	// Private Sub CommandButton1_Click()
	// If Me.Frame1.Left = 0 Then

	// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
	// Dim wChunk() As String
	// Dim wStr As String
	// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
	// If (outlet1.ListCount = 0) And (outlet2.ListCount > 0) Then
	// wStr = outlet2.List(0, 1)
	// wChunk = Split(wStr, vbTab)
	// wChunk(3) = "FIXX"
	// wChunk(4) = "1"
	// wChunk(5) = "SINGLE"
	// wChunk(6) = "'1'"
	// wChunk(7) = "Char     Width 1"
	// wStr = Join(wChunk, vbTab)
	// outlet1.AddItem
	// outlet1.List(0, 0) = "�y�f�^�s�z" & vbTab & "�S��" 'outlet2.List(0, 0)
	// outlet1.List(0, 1) = wStr 'outlet2.List(0, 1)
	// Call setSelectAll(outlet1)
	// End If
	// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
	// If (outlet2.ListCount = 0) And (outlet1.ListCount > 0) Then
	// wStr = outlet1.List(0, 1)
	// wChunk = Split(wStr, vbTab)
	// wChunk(3) = "FIXX"
	// wChunk(4) = "1"
	// wChunk(5) = "SINGLE"
	// wChunk(6) = "'1'"
	// wChunk(7) = "Char     Width 1"
	// wStr = Join(wChunk, vbTab)
	// outlet2.AddItem
	// outlet2.List(0, 0) = "�y�f�^�s�z" & vbTab & "�S��" 'outlet1.List(0, 0)
	// outlet2.List(0, 1) = wStr 'outlet1.List(0, 1)
	// Call setSelectAll(outlet2)
	// End If
	// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
	// outlet1.Height = 92.05
	// outlet2.Height = 92.05
	// DoEvents
	// ChkHeadSum = False
	// ChkSideSum = False
	// If outlet1.ListCount = 1 Then
	// ChkFunk6 = True
	// ChkFunk1 = False
	// ChkFunk2 = True
	// ElseIf outlet2.ListCount = 1 Then
	// ChkFunk6 = True
	// ChkFunk1 = True
	// ChkFunk2 = False
	// Else
	// ChkFunk1 = True
	// ChkFunk2 = True
	// End If
	// If outlet1.ListCount > 0 And outlet2.ListCount > 0 Then
	// Me.ComGoGo.Visible = True
	// Else
	// Me.ComGoGo.Visible = False
	// End If
	// Else
	// gDirty1 = True '2008/11/10
	// gDirty2 = True '2008/11/10
	// inlet1.Enabled = True
	// inlet2.Enabled = True
	// For i = 1 To 480 Step 10
	// Me.Frame1.Left = -480 + i
	// DoEvents
	// Next i
	// Me.Frame1.Left = 0
	// CommandButton1.Caption = "�I�����ڂ̊m�F����\��"
	// Me.LabInfo2 = ""
	// End If
	// SetHourglass False
	// DoEvents
	// End Sub

}