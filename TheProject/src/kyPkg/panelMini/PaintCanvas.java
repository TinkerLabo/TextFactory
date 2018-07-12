package kyPkg.panelMini;

import kyPkg.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
//import java.awt.event.*;
import javax.swing.*;

//import java.awt.geom.*;
//-------+---------+---------+---------+---------+---------+---------+---------+
// �`��\�ȃL�����o�X PaintCanvas �N���X 
//-------+---------+---------+---------+---------+---------+---------+---------+
//public class PaintCanvas extends Canvas {
public class PaintCanvas extends JPanel {
	public static final String RECTANGLE = "�l�p�`";
	public static final String ROUND_RECTANGLE = "�ێl�p�`";
	public static final String ARC = "�~��";
	public static final String ELLIPSE = "�ȉ~";
	public static final String POLYGON = "���p�`�N���[�Y";
	public static final String POLYLINE = "���p�`";
	private static final long serialVersionUID = -3475498145361403500L;
	private static Dimension dimension = new Dimension(640, 480);
	private static Dimension oldSize = dimension;
	private BufferedImage offScreenImage; // �I�t�X�N���[�C���[�W
	// private Graphics2D offGraphics;
	private Pen pen; // �`��ɗ��p���� Pen �̃I�u�W�F�N�g
	private EvtCtrl eC2; // �C�x���g���R���g���[������N���X

	private Dimension getDefaultsize() {
		return new Dimension(640, 480);
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public PaintCanvas(Pen pen, Dimension pDimension) {
		super();
		this.setPen(pen);
		this.setBackground(Color.white);
		if (pDimension == null) {
			dimension = getDefaultsize();
		} else {
			dimension = pDimension;
		}
		setSize(dimension);
		setPreferredSize(dimension);
		oldSize = dimension;

		// �I�t�X�N���[�C���[�W�쐬 �� ������
		offScreenImage = newImage();
		clearScreen();

		eC2 = new EvtCtrl(this);
		eC2.compAction(); // �C�x���g���R���g���[��
	}

	// -------------------------------------------------------------------------
	// image �֘A getter setter
	// -------------------------------------------------------------------------
	public BufferedImage getImage() {
		return offScreenImage;
	}

	public void setImage(BufferedImage pimage) {
		offScreenImage = pimage;
	}

	public Graphics getGraphics2() {
		return offScreenImage.createGraphics();
	}

	// -------------------------------------------------------------------------
	// pen�֘A
	// -------------------------------------------------------------------------
	public void setPen(Pen pen) {
		this.pen = pen;
	}

	public Pen getPen() {
		return pen;
	}

	// -------------------------------------------------------------------------
	// �I�t�X�N���[�C���[�W�쐬
	// -------------------------------------------------------------------------
	private BufferedImage newImage() {
		System.out.println("## newImage() ##");
		Dimension newSize = getSize();
		BufferedImage offScreenImage = new BufferedImage(newSize.width,
				newSize.height, BufferedImage.TYPE_INT_RGB);
		return offScreenImage;
	}

	// -------------------------------------------------------------------------
	// �I�t�X�N���[�C���[�W ������
	// -------------------------------------------------------------------------
	public void clearScreen() {
		Graphics graphics = offScreenImage.createGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, dimension.width, dimension.height);
	}

	// -------------------------------------------------------------------------
	// paintComponent�F�N�������ĕ`�掞�Ăяo����܂� paint�ł��̂Ɠ��Ⴄ���H
	// -------------------------------------------------------------------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// super.paint(g); // Super�o�R��paintComp�����
//		System.out.println("��paintComponent��");
		Dimension newSize = getSize();
		if (offScreenImage == null | !(newSize.equals(oldSize))) {
			System.out.println("## resized?=>new BufferedImage ##");
			offScreenImage = newImage();
			// image = new BufferedImage(newSize.width, newSize.height,
			// BufferedImage.TYPE_INT_RGB);

			// �ό`�����ꍇ�̓r�b�g�P�ʂŃR�s�[����H
			WritableRaster raster = offScreenImage.getRaster();
			int[] data = { 255, 255, 255 };
			for (int v = 0; v < newSize.height; v++)
				for (int h = 0; h < newSize.width; h++)
					raster.setPixel(h, v, data); // ������
			oldSize = newSize;
		}

		// �ŐV�̃C�x���g���E�E�E����Ȃ�
		if (eC2.xEvtName.equals("MouseReleased")) {
			System.out.println("��Update & MouseReleased��");
			paint2D(pen.getShape());
		}

		// ((Graphics2D)g).drawRenderedImage( image, null );
		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.drawImage(offScreenImage, 0, 0, this);

	}

	// -------------------------------------------------------------------------
	public void paint2D(String wType) {
		// Graphics g = getOffScreenGraphics();
		Graphics2D g2 = (Graphics2D) getGraphics2();
		;

		System.out.println("��paint2D�� wType:" + wType);
		// ---------------------------------------------------------------------
		// ������������
		// ---------------------------------------------------------------------
		int x, y;
		int rectWidth, rectHeight;
		if (eC2.Pressed_X > eC2.Released_X) {
			x = eC2.Released_X;
			rectWidth = eC2.Pressed_X - eC2.Released_X;
		} else {
			x = eC2.Pressed_X;
			rectWidth = eC2.Released_X - eC2.Pressed_X;
		}
		if (eC2.Pressed_Y > eC2.Released_Y) {
			y = eC2.Released_Y;
			rectHeight = eC2.Pressed_Y - eC2.Released_Y;
		} else {
			y = eC2.Pressed_Y;
			rectHeight = eC2.Released_Y - eC2.Pressed_Y;
		}

		Color fgColor = Color.black;
		// Color selectColor = pen.getColor();

		// ---------------------------------------------------------------------
		// ���̑���
		// ---------------------------------------------------------------------
		// float dash1[] = {10.0f};
		// BasicStroke stroke = new BasicStroke(2.0f);
		// BasicStroke wideStroke = new BasicStroke(8.0f);
		// BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_MITER,
		// 10.0f, dash1, 0.0f);
		// ---------------------------------------------------------------------
		// �h��̎��
		// ---------------------------------------------------------------------
		// GradientPaint grdColor = new
		// GradientPaint(x,y,selectColor,x+rectWidth, y,white);

		g2.setPaint(fgColor);
		g2.setColor(pen.getColor()); // �`��F
		g2.setStroke(pen.getBasicStroke()); // ���̎��

		// g2.setPaint(fgColor);
		// g2.setPaint(grdColor);
		// g2.setPaint(red);

		// g2.setStroke(stroke);
		// g2.setStroke(dashed);
		// g2.setStroke(wideStroke);

		// Geom ----------------------------------------
		// Point2D.Double start,end,control,point;
		// QuadCurve2D.Double quad = new QuadCurve2D.Double();

		// ---------------------------------------------------------------------
		// Shape�F�`�悷��}�`
		// ---------------------------------------------------------------------
		Shape shapeObj = null;
		if (wType.equals(RECTANGLE)) {
			// -----------------------------------------------------------------
			// �s�l�p�`�tRectangle2D
			// -----------------------------------------------------------------
			shapeObj = new Rectangle2D.Double(x, y, rectWidth, rectHeight);
		} else if (wType.equals(ROUND_RECTANGLE)) {
			// -----------------------------------------------------------------
			// �s�p�̂܂邢�l�p�`�tRoundRectangle2D
			// -----------------------------------------------------------------
			shapeObj = new RoundRectangle2D.Double(x, y, rectWidth, rectHeight,
					10, 10);
		} else if (wType.equals(ARC)) {
			// -----------------------------------------------------------------
			// �s�~�ʁtArc2D
			// -----------------------------------------------------------------
			shapeObj = new Arc2D.Double(x, y, rectWidth, rectHeight, 90, 135,
					Arc2D.OPEN);
		} else if (wType.equals(ELLIPSE)) {
			// -----------------------------------------------------------------
			// �s�ȉ~�tEllipse2D
			// -----------------------------------------------------------------
			shapeObj = new Ellipse2D.Double(x, y, rectWidth, rectHeight);
		} else if (wType.equals(POLYGON)) {
			// -----------------------------------------------------------------
			// �s���p�`�t GeneralPath
			// -----------------------------------------------------------------
			int x1Points[] = { x, x + rectWidth, x, x + rectWidth };
			int y1Points[] = { y, y + rectHeight, y + rectHeight, y };
			GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
					x1Points.length);
			polygon.moveTo(x1Points[0], y1Points[0]);
			for (int index = 1; index < x1Points.length; index++) {
				polygon.lineTo(x1Points[index], y1Points[index]);
			}
			;
			polygon.closePath();
			shapeObj = polygon;
		} else if (wType.equals(POLYLINE)) {
			// -----------------------------------------------------------------
			// �s���p���t GeneralPath
			// -----------------------------------------------------------------
			int x2Points[] = { x, x + rectWidth, x, x + rectWidth };
			int y2Points[] = { y, y + rectHeight, y + rectHeight, y };
			GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
					x2Points.length);
			polyline.moveTo(x2Points[0], y2Points[0]);
			for (int index = 1; index < x2Points.length; index++) {
				polyline.lineTo(x2Points[index], y2Points[index]);
			}
			;
			shapeObj = polyline;
		}
		g2.fill(shapeObj);
		g2.draw(shapeObj);
		g2.setPaint(fgColor);
	}
	// -------------------------------------------------------------------------
	// update�͂ǂ�����Ă΂��̂��H
	// repaint��update��paint�Ȃ񂶂�Ȃ��̂��H�H�C�x���g�̏��Ԃ��s����
	// -------------------------------------------------------------------------
	// public void xxupdate(Graphics g) {
	// super.update(g);
	// }

}
