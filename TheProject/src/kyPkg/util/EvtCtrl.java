package kyPkg.util;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class EvtCtrl {
	Component me;
	public int Pressed_X; // �}�E�X�������ꂽ�w�ʒu
	public int Pressed_Y; // �}�E�X�������ꂽ�x�ʒu
	public int Released_X; // �}�E�X�������ꂽ�w�ʒu
	public int Released_Y; // �}�E�X�������ꂽ�x�ʒu
	public int xPozX, xPozY; // key�ɂ��R���g���[���ʒu
	public String xEvtName;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public EvtCtrl(Component pComponent) {
		xEvtName = "";
		xPozX = xPozY = 0;
		Pressed_X = Pressed_Y = 0;
		Released_X = Released_Y = 0;
		me = pComponent;
	}

	public void requestKeyF() {
		JComponent jcmp;
		jcmp = (JComponent) me;
		jcmp.requestFocus(true); // ����������Ȃ��ƃL�[�C�x���g���E���Ă���Ȃ��I
	}

	// --------------------------------------------------------
	// evtInfo
	// --------------------------------------------------------
	// public void evtInfo(MouseEvent evt){
	// Graphics gr;
	// gr = offImg.getGraphics();
	// // int r = (int)(Math.random() * 255);
	// // int g = (int)(Math.random() * 255);
	// // int b = (int)(Math.random() * 255);
	// //gr.setColor(Color.white);
	// //gr.fillRect(0,0,600,600);
	// //gr.setColor(Color.black);
	// // int ix = (evt.getX()/10)*10;
	// // int iy = (evt.getY()/10)*10;
	// //gr.setColor(new Color(r,g,b));
	// gr.drawString(evt.toString(),ix+10,iy+10);
	// gr.fillRect(ix,iy,10,10);
	// me.repaint();
	// }
	public void compAction() {
		Component pThis;
		pThis = me;
		pThis.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent evt) { // �L�[�{�[�h�t�H�[�J�X���擾����
				System.out.println("focusGained");
			}
		});
		// ---------------------------------------------------------------------
		// KeyListener java.awt.Component �̃��\�b�h
		// ---------------------------------------------------------------------
		pThis.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) { // �L�[�������Ă��鎞�ɌĂяo�����
				// System.out.println("KeyPressed:");
				// System.out.println("	KeyChar         :" + evt.getKeyChar());
				// System.out.println("	KeyCode         :" + evt.getKeyCode());
				// System.out.println("	KeyLocation     :" +
				// evt.getKeyLocation());
				// System.out.println("	isActionKey()   :" + evt.isActionKey()
				// );
				// System.out.println("	KeyModifiersText:" +
				// evt.getKeyModifiersText());
				// System.out.println("	KeyText         :" + evt.getKeyText());
				// ���l���̓L�[�p�b�h�̉����L�[�p�̒萔�ł��B
				switch (evt.getKeyCode()) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_KP_LEFT:
					xPozX -= 10;
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_KP_RIGHT:
					xPozX += 10;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					xPozY -= 10;
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					xPozY += 10;
					break;
				case KeyEvent.VK_ENTER:
					xPozX = 100;
					xPozY = 100;
					break;
				case KeyEvent.VK_ESCAPE:
					xPozX = 0;
					xPozY = 0;
					break;
				// default;
				}
				me.repaint();
			}
			@Override
			public void keyReleased(KeyEvent evt) { // �L�[�𗣂���
				// System.out.println("keyReleased:");
			}
			@Override
			public void keyTyped(KeyEvent evt) { // �L�[���^�C�v���� ���A�N�V�����L�[�ł͔������Ȃ�
				// System.out.println("keyTyped:");
			}
		});
		// ---------------------------------------------------------------------
		// MouseListener java.awt.Component �̃��\�b�h
		// ---------------------------------------------------------------------
		pThis.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) { // �}�E�X���N���b�N����܂���
				System.out.println("MouseClicked :" + evt.getX() + ","
						+ evt.getY());
				requestKeyF();
				System.out.println("MouseClicked");
				System.out.println("evt.toString:" + evt.toString());
				System.out.println("  :getWhen       :" + evt.getWhen());
				System.out.println("  :isAltDown     :" + evt.isAltDown());
				// System.out.println("  :isAltGraphDown:" +
				// evt.isAltGraphDown());
				// System.out.println("  :isControlDown :" +
				// evt.isControlDown());
				// System.out.println("  :isShiftDown   :" + evt.isShiftDown());
				// System.out.println("  :getButton     :" + evt.getButton());
				// // from ver1.4
				// System.out.println("  :getClickCount :" +
				// evt.getClickCount());
				// System.out.println("  :getPoint      :" + evt.getPoint());
				// System.out.println("  :getX,getY     :" + evt.getX() + "," +
				// evt.getY());
			}

			// public void mouseEntered(MouseEvent evt){ //�}�E�X��������
			// System.out.println("MouseEntered");
			// }
			// public void mouseExited(MouseEvent evt){ //�}�E�X���o��
			// System.out.println("MouseExited");
			// }
			@Override
			public void mousePressed(MouseEvent evt) { // �}�E�X�̃{�^���������ꂽ
				Pressed_X = evt.getX();
				Pressed_Y = evt.getY();
				xEvtName = "MousePressed";
			}

			@Override
			public void mouseReleased(MouseEvent evt) { // �}�E�X�̃{�^���������ꂽ
				Released_X = evt.getX();
				Released_Y = evt.getY();
				xEvtName = "MouseReleased";
				me.repaint();
			}
		});
		// ---------------------------------------------------------------------
		// MouseMotionListener java.awt.Component �̃��\�b�h
		// ---------------------------------------------------------------------
		// pThis.addMouseMotionListener(new MouseMotionAdapter() {
		// public void mouseDragged(MouseEvent evt){ //�}�E�X���h���b�O���ꂽ
		// System.out.println("mouseDragged");
		// me.repaint();
		// }
		// public void mouseMoved(MouseEvent evt){ //�}�E�X���ړ����ꂽ
		// System.out.println("mouseMoved x:" + evt.getX() + " y:" +
		// evt.getY());
		// me.repaint();
		// }
		// });
		// ---------------------------------------------------------------------
		// MouseWheelListener java.awt.Component �̃��\�b�h
		// ---------------------------------------------------------------------
		// pThis.addMouseWheelListener(new MouseWheelListener() {
		// public void mouseWheelMoved(MouseWheelEvent e) { //�z�C�[������]����ƌĂ΂��
		// // int e.getScrollAmount() //���̃C�x���g�ɉ������ăX�N���[������郆�j�b�g����Ԃ��܂��B
		// // int e.getScrollType() //���̃C�x���g�ɉ������Ĕ�������X�N���[���̃^�C�v��Ԃ��܂��B
		// // int e.getUnitsToScroll()
		// // int e.getWheelRotation() //�}�E�X�z�C�[������]��������
		// // String e.paramString() //���̃C�x���g����肷��p�����[�^�̕�����
		// }
		// });
	}

	// -------------------------------------------------------------------------
	// buttonAction
	// -------------------------------------------------------------------------
	public void buttonAction(Button pThis) {
		// ---------------------------------------------------------------------
		// ActionListener �ȉ��̃I�u�W�F�N�g�������\�b�h
		// MenuItem,TextField,List,Button
		// JFileChooser,JComboBox,Timer,DefaultButtonModel,JTextField
		// AbstractButton,BasicComboBoxEditor,ComboBoxEditorButtonModel
		// ---------------------------------------------------------------------
		pThis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) { // �A�N�V�������������܂���
				System.out.println("actionPerformed");
			}
		});
	}

	public void checkboxAction(Checkbox pThis) {
		// ---------------------------------------------------------------------
		// addItemListener
		// List�AChoice�ACheckboxMenuItem�ACheckbox�AJComboBox�A
		// DefaultButtonModel�AButtonModel�AAbstractButton�AItemSelectable �̃��\�b�h
		// ---------------------------------------------------------------------
		pThis.addItemListener(new ItemListener() {
			// ���[�U�ɂ���č��ڂ��I���܂��͑I���������ꂽ�Ƃ��ɌĂяo����܂��B
			@Override
			public void itemStateChanged(ItemEvent e) {
				// Object e.getItem()
				// �C�x���g�ɂ���ĉe�����󂯂����ڂ�Ԃ��܂��B
				// ItemSelectable e.getItemSelectable()
				// �C�x���g�̔�������Ԃ��܂��B
				// int e.getStateChange()
				// ��ԕύX�̃^�C�v (�I���A�܂��͑I������) ��Ԃ��܂� �B
				// String e.paramString()
				// ���̍��ڃC�x���g����肷��p�����[�^�̕������Ԃ��܂��B
			}
		});
		// addAdjustmentListener(AdjustmentListener) Scrollbar �̃��\�b�h
		// addChangeListener(ChangeListener) JSlider,MenuSelectionManager �̃��\�b�h
	}

	public void windowAction(Window pThis) {
		// ---------------------------------------------------------------------
		// WindowListener java.awt.Window �̃��\�b�h
		// ---------------------------------------------------------------------
		pThis.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) { // �A�N�e�B�u�ɐݒ肳�ꂽ
				System.out.println("windowActivated");
			}

			@Override
			public void windowClosed(WindowEvent e) { // �E�B���h�E���N���[�Y���ꂽ
				System.out.println("windowClosed");
			}

			@Override
			public void windowClosing(WindowEvent e) { // �V�X�e�����j���[�ŕ��悤�Ƃ���
				System.out.println("windowClosing");
			}

			@Override
			public void windowDeactivated(WindowEvent e) { // �A�N�e�B�u Window
															// �łȂ��Ȃ���
				System.out.println("windowDeactivated");
			}

			@Override
			public void windowDeiconified(WindowEvent e) { // �ŏ�������ʏ�̏��
				System.out.println("windowDeiconified");
			}

			@Override
			public void windowIconified(WindowEvent e) { // �ʏ킩��ŏ������ꂽ
				System.out.println("windowIconified");
			}

			@Override
			public void windowOpened(WindowEvent e) { // �ŏ��ɉ��ɂȂ���
				System.out.println("windowOpened");
			}
		});
	}
}
