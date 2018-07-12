package kyPkg.frame;

import kyPkg.panel.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//-----------------------------------------------------------------------------
/**
 * �C���^�[�i���t���[���̐��`
 * <p>
 * ���̃R���|�[�l���g�ւ̑g�ݍ��݂��ȒP�ł���͂�
 * <p>
 * 
 * @(#)IF_Tmp.java 1.11 04/11/16 Copyright 2004 Ken Yuasa. All rights reserved.
 * @author Ken Yuasa
 * @version 1.00 04/11/16
 * @since 1.3
 */
// -----------------------------------------------------------------------------
// �C���^�[�i���t���[�� Template !!
// -----------------------------------------------------------------------------
public class JInternalFrameMod extends JInternalFrame {
	private static final long serialVersionUID = 8711592840546862133L;
	private JP_Ancestor child;
	private int xWidth = 640;
	private int xHeight = 300;
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	public JInternalFrameMod(String title, JP_Ancestor pPnl) {
	// -------------------------------------------------------------------------
		this(title, pPnl, 640, 300);
	}
	public JInternalFrameMod(String title, JP_Ancestor pannel, int pWidth, int pHeight) {
		super();
		child = pannel; // �����p�l��
		initMe(title);
		xxResize(pWidth, pHeight);
	}
	// -------------------------------------------------------------------------
	public void initMe(String title) {
		this.setClosable(true);
		this.setResizable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setFrameIcon(null);
		this.setTitle(title);
		// this.setOpaque(false);
		this.setVisible(true);
		// ---------------------------------------------------------------------
		// �����p�l���̏�����
		// ---------------------------------------------------------------------
		final Container pane = this.getContentPane();
		child.initGui(xWidth, xHeight, this, null);
//		child.initGui(xWidth, xHeight,  null);
		pane.add(child);
		// ---------------------------------------------------------------------
		// �R���|�[�l���g���X�i�[�ݒ�
		// ---------------------------------------------------------------------
		this.addComponentListener(new ComponentAdapter() {
			// -----------------------------------------------------------------
			// ���T�C�Y���ꂽ�� �� �q�p�l���̃��T�C�Y���Ă�
			// -----------------------------------------------------------------
			@Override
			public void componentResized(ComponentEvent e) {
				// System.out.println("��componentResized");
//				child.resizeMe(pane.getWidth(), pane.getHeight(),true);
			}
			// -----------------------------------------------------------------
			// void componentHidden (ComponentEvent e) �B���ꂽ��E�E�E
			// void componentMoved (ComponentEvent e) �ʒu���ς������E�E�E
			// void componentResized(ComponentEvent e) �T�C�Y���ς������E�E�E
			// void componentShown (ComponentEvent e) ���ɂȂ�����E�E�E
			// -----------------------------------------------------------------
		});
		/**********************************************************************
		 * this.addInternalFrameListener( new InternalFrameAdapter(){ //
		 * �N�����ꂽ�Ƃ��ɌĂяo����܂��B public void
		 * internalFrameActivated(InternalFrameEvent e) {} // �N���[�Y���ꂽ�Ƃ��ɌĂяo����܂��B
		 * public void internalFrameClosed(InternalFrameEvent e) {} //
		 * �N���[�Y�������̂Ƃ��ɌĂяo����܂��B public void
		 * internalFrameClosing(InternalFrameEvent e) {} // �I�����ꂽ�Ƃ��ɌĂяo����܂��B
		 * public void internalFrameDeactivated(InternalFrameEvent e) {} //
		 * �A�C�R�����������ꂽ�Ƃ��ɌĂяo����܂��B public void
		 * internalFrameDeiconified(InternalFrameEvent e) {} //
		 * �A�C�R�������ꂽ�Ƃ��ɌĂяo����܂��B public void
		 * internalFrameIconified(InternalFrameEvent e) {} // �I�[�v�����ꂽ�Ƃ��ɌĂяo����܂��B
		 * public void internalFrameOpened(InternalFrameEvent e) {} });
		 ************************************************************************/
	}
	// ---------+---------+---------+---------+---------+---------+---------+---------+
	// xxResize
	// ---------+---------+---------+---------+---------+---------+---------+---------+
	public void xxResize(int pWidth, int pHeight) {
		xWidth = pWidth;
		xHeight = pHeight;
		if (xWidth <= 0)
			xWidth = 300;
		if (xHeight <= 0)
			xHeight = 300;
		this.setSize(xWidth, xHeight);
	}
	// //---------+---------+---------+---------+---------+---------+---------+---------+
	// // �`��
	// //---------+---------+---------+---------+---------+---------+---------+---------+
	// public void paint(Graphics g){
	// super.paint(g); // Super�o�R��paintComp�����
	// }
	// //-------------------------------------------------------------------------
	// // �`��
	// //-------------------------------------------------------------------------
	// public void paintComponent(Graphics g){
	// kyPkg.util.Ruler.drawRuler(g,this.getSize().width,this.getSize().height,new
	// Color(255,187,244,80));
	// }
}
