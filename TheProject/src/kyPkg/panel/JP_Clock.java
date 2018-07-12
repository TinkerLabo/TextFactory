package kyPkg.panel;

import java.awt.*;
import java.util.*;
import java.lang.Math; // sin, cos
//-----------------------------------------------------------------------------

import kyPkg.frame.BaseFrame;
import kyPkg.util.Ruler;

public class JP_Clock extends JP_Ancestor {
	private static final long serialVersionUID = 2243916145937323016L;
	private int xWidth, xHeight;
	// -------------------------------------------------------------------------
	// Local Values
	// -------------------------------------------------------------------------
	private Image offImg; // �_�u���o�b�t�@�����O�p
	private int CenterX, CenterY; // ���v�̒��S
	private int Radius; // ���v�̔��a
	private Thread gThread; // �^�C�}�[�p�X���b�h
	private Color wBackColor = Color.BLACK; // �����Ղ̐F
	private Color wScaleColor = Color.WHITE; // �ڐ����j�̐F
	private Color wHandColor = Color.RED; // �b�j�̐F

	// -------------------------------------------------------------------------
	// �����Ղ̐F
	// -------------------------------------------------------------------------
	public void setBackColor(Color newColor) {
		if (newColor != null) {
			wBackColor = newColor;
		} else {
			// null���w�肳�ꂽ�ꍇ�����_���ȐF
			int iR, iG, iB;
			iR = (int) Math.floor(Math.random() * 256);
			iG = (int) Math.floor(Math.random() * 256);
			iB = (int) Math.floor(Math.random() * 256);
			wBackColor = new Color(iR, iG, iB);
			System.out.println("�� R:" + iR + " G:" + iG + " B:" + iB);
		}
	}

	// --------------------------------------------------------------------------
	// �R���X�g���N�^
	// --------------------------------------------------------------------------
	public JP_Clock() {
		super();
		resizeMe(300, 300);
		gThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					repaint(); // �� paint
					try {
						Thread.sleep(1000); // ��b�����I
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		};
		gThread.start();
	}

	// #########################################################################
	// -------------------------------------------------------------------------
	// xxInit�s JP_Ancestor �t�e����Ă΂��
	// -------------------------------------------------------------------------
	// public void xxInit(int pWidth,int pHeight,Object pParent,LayoutManager
	// layout){
	// //myParent = pParent;
	// xxResize(pWidth,pHeight);
	// this.setLayout(layout);
	// }
	// //-------------------------------------------------------------------------
	// // xxInit�s JP_Ancestor �t�e����Ă΂��
	// //-------------------------------------------------------------------------
	// public void xxInit(int pWidth,int pHeight,Object pParent,LayoutManager
	// layout){
	// myParent = pParent;
	// //xxResize(pWidth,pHeight);
	// gThread = new Thread(){
	// public void run(){
	// while(true){
	// repaint(); // �� paint
	// try{
	// Thread.sleep(1000); //��b�����I
	// }catch (InterruptedException e){
	// e.printStackTrace();
	// System.exit(1);
	// }
	// }
	// }
	// };
	// gThread.start();
	// }
	// -------------------------------------------------------------------------
	// initGui�s InfChilePanel �t
	// -------------------------------------------------------------------------
	@Override
	public void initGui(int pWidth, int pHeight, Object pParent,
			LayoutManager layout) {
		resizeMe(pWidth, pHeight);

	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// xxResize
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public void resizeMe(int pWidth, int pHeight) {
		xWidth = pWidth;
		xHeight = pHeight;
		if (xWidth <= 0)
			xWidth = 300;
		if (xHeight <= 0)
			xHeight = 300;
		// ---------------------------------------------------------------------
		CenterX = xWidth / 2; // ���v�̈ʒu�Ɣ��a
		CenterY = xHeight / 2;
		if (xWidth < xHeight) // �c���C�Z���������v�̔��a
			Radius = (int) (xWidth / 2 * 0.9);
		else
			Radius = (int) (xHeight / 2 * 0.9);
	}

	@Override
	public void paintComponent(Graphics g) {
		// Ruler.drawRuler(g,this.getSize().width,this.getSize().height,Color.white);

		// g.setColor(new Color(50,50,50,100));
		// g.fillRect(10,10, xWidth, xHeight);
		// g.setColor(wBackColor);
		// g.fillOval( (xWidth / 2 - Radius),
		// (xHeight / 2 - Radius),
		// (Radius * 2 ),
		// (Radius * 2 ));

		if (xWidth > 0 && xHeight > 0) {
			offImg = createImage(xWidth / 2, xHeight / 2); // �C���[�W�쐬
			if (offImg != null) {
				DispTime(offImg); // �I�t�X�N���[���ɕ`��
				g.drawImage(offImg, 0, 0, this); // �C���[�W��`��
			}
		}
		Ruler.drawRuler(g, this.getSize().width,
				this.getSize().height, new Color(128, 255, 128, 250));

	}

	// -------------------------------------------------------------------------
	// public void xxpaintComponent(Graphics g) {
	// super.paintComponent(g);
	// g.setColor(new Color(100,50,50,100));
	// g.fillRect(0,0,getWidth(), getHeight());
	// }
	// //-----+---------+---------+---------+---------+---------+---------+---------+
	// // �`��
	// //-----+---------+---------+---------+---------+---------+---------+---------+
	// public void paint(Graphics g){
	// super.paint(g);//Super�o�R��paintComp�����
	// }
	// -------------------------------------------------------------------------
	// paint
	// -------------------------------------------------------------------------
	@Override
	public void paint(Graphics g) {
		super.paint(g);// Super�o�R��paintComp�����
		if (xWidth > 0 && xHeight > 0) {
			offImg = createImage(xWidth, xHeight); // �C���[�W�쐬
			if (offImg != null) {
				DispTime(offImg); // �I�t�X�N���[���ɕ`��
				g.drawImage(offImg, 0, 0, this); // �C���[�W��`��
			}
		}
	}

	// -------------------------------------------------------------------------
	// update
	// -------------------------------------------------------------------------
	// public void update(Graphics g){
	// paint(g);
	// }
	// -------------------------------------------------------------------------
	// dispose
	// -------------------------------------------------------------------------
	// public void dispose(){
	// gThread = null;
	// }
	// -------------------------------------------------------------------------
	// ���v���I�t�X�N���[���ɕ`��
	// -------------------------------------------------------------------------
	void DispTime(Image pOffImg) {
		Graphics g = pOffImg.getGraphics(); // �I�t�X�N���[��
		g.setColor(new Color(50, 50, 50, 100));
		g.fillRect(0, 0, xWidth, xHeight);

		g.setColor(wBackColor);
		g.fillOval((xWidth / 2 - Radius), (xHeight / 2 - Radius), (Radius * 2),
				(Radius * 2));
		// ---------------------------------------------------------------------
		// �ڐ������`��i1����6�x�C60����360�x)
		// ---------------------------------------------------------------------
		for (int angle = 0; angle < 360; angle += 6) {
			double RD = angle * Math.PI / 180; // �p�x�����W�A���ɕϊ�
			int x1 = CenterX + (int) (Math.sin(RD) * Radius); // �ڐ���̊O���̓_�̈ʒu
			int y1 = CenterY - (int) (Math.cos(RD) * Radius);
			int radius2;
			if (angle % 30 == 0) // 5������(5*6=30)
				radius2 = Radius - 8; // ����8�̖ڐ���
			else
				radius2 = Radius; // ����0�̖ڐ���i�܂�_)
			int x2 = CenterX + (int) (Math.sin(RD) * radius2); // �ڐ���̓����̓_�̈ʒu
			int y2 = CenterY - (int) (Math.cos(RD) * radius2);
			g.setColor(wScaleColor);
			g.drawLine(x1, y1, x2, y2); // �ڐ��胉�C��������
		}
		// ---------------------------------------------------------------------
		// ���݂̎���
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		int hour = date.get(Calendar.HOUR); // ���� �擾
		int minute = date.get(Calendar.MINUTE); // �� �擾
		int second = date.get(Calendar.SECOND); // �b �擾
		// ---------------------------------------------------------------------
		// ���j �i�ꎞ�Ԃ�360/12=30�x�j
		double RD = ((hour + minute / 60.0) * 30) * Math.PI / 180; // �p�x�����W�A���ɕϊ�
		int hx = CenterX + (int) (Math.sin(RD) * Radius * 0.6); // �j�̐�[�̈ʒu
		int hy = CenterY - (int) (Math.cos(RD) * Radius * 0.6);
		g.setColor(wScaleColor);
		g.drawLine(CenterX, CenterY, hx, hy); // ���S����j�̐�[�܂�
		// ---------------------------------------------------------------------
		// ���j �i�ꕪ��360/60=6�x�j
		RD = (minute * 6) * Math.PI / 180; // �p�x�����W�A���ɕϊ�
		int mx = CenterX + (int) (Math.sin(RD) * Radius * 0.8); // �j�̐�[�̈ʒu
		int my = CenterY - (int) (Math.cos(RD) * Radius * 0.8);
		g.setColor(wScaleColor);
		g.drawLine(CenterX, CenterY, mx, my);
		// ---------------------------------------------------------------------
		// �b�j �i��b��360/60 =6�x�j
		RD = (second * 6) * Math.PI / 180; // �p�x�����W�A���ɕϊ�
		int sx = CenterX + (int) (Math.sin(RD) * Radius * 0.9); // �j�̐�[�̈ʒu
		int sy = CenterY - (int) (Math.cos(RD) * Radius * 0.9);
		g.setColor(wHandColor);
		g.drawLine(CenterX, CenterY, sx, sy);

	}

	// ----+---------+---------+---------+---------+---------+---------+---------+
	// forTest
	// ----+---------+---------+---------+---------+---------+---------+---------+
	public static void main(String[] argv) {
		asAPart();
	}
	public static void standAlone() {
		new JP_Clock().showDialog("CLOCK");// �i�ڃ}�X�^������ʕ\��
	}
	public static void asAPart() {
		// ---------------------------------------------------------------------
		// ���v�p�l�� ����
		// ---------------------------------------------------------------------
		JP_Clock objClock = new JP_Clock();
		Color xColor = new Color(128, 150, 161);
		objClock.setBackColor(xColor);
		objClock.initGui(100, 100, null, null);
		// objClock.initGui(100, 100, null);
		objClock.resizeMe(150, 150); // ��������Ȃ��ƃo�O��I�I
		objClock.setBounds(0, 0, 150, 150);
		new BaseFrame(objClock, 640, 480);
	}
	
	
	
	
}
