package kyPkg.util;

/** ����`�����̏����L������ Pen */
import java.awt.*;

import kyPkg.panelMini.PaintCanvas;

public class Pen {
	public static final String BUTT = "BUTT";
	public static final String SQUARE = "SQUARE";
	public static final String BEVEL = "BEVEL";
	public static final String MITER = "MITER";
	public static final String ROUND = "ROUND";
	// �ǂ�ȕ`������邩�H
	private String shape;

	public String getShape() {
		return shape;
	}

	public void setShape(String pShape) {
		this.shape = pShape;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// ���̃v���p�e�B
	// -----+---------+---------+---------+---------+---------+---------+---------+
	private Color color;
	/** ���̐F */
	private BasicStroke stroke;

	// public static final int ROUND = 0;
	// public static final int SQUARE = 1;

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// ���̐F�ɃA�N�Z�X���郁�\�b�h */
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// ���̑����A���̒[�̌`��ɃA�N�Z�X���郁�\�b�h */
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public BasicStroke getBasicStroke() {
		return stroke;
	}

	public void setBasicStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	public float getLineWidth() {
		return stroke.getLineWidth();
	}

	// public int getLineCap() {
	// return stroke.getEndCap();
	// }
	// public int getLineJoin() {
	// return stroke.getLineJoin();
	// }
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// CAP_ROUND �y�����̔����̒����𔼌a�Ƃ����ۂ�������t���āA�����Ă��Ȃ������֊s������єj���Z�O�����g���I�����܂��B
	// CAP_BUTT ������t�����ɁA�����Ă��Ȃ������֊s������єj���Z�O�����g���I�����܂��B
	// CAP_SQUARE
	// ���C�����̔����̒����ɓ��������������Z�O�����g�̐�[���������鐳���`��t���āA�����Ă��Ȃ������֊s������єj���Z�O�����g���I�����܂��B
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// JOIN_ROUND ���C�����̔����̒����𔼌a�Ƃ��āA�p���ۂ��؂藎�Ƃ��ė֊s���Z�O�����g��ڍ����܂��B
	// JOIN_BEVEL ���̍L���֊s���̊O���̊p�𒼐��Z�O�����g�ɐڍ�����悤�ɂ��ė֊s���Z�O�����g��ڍ����܂��B
	// JOIN_MITER ���C���Z�O�����g�̊O���̒[���d�Ȃ�܂ŉ������ė֊s���Z�O�����g��ڍ����܂��B
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public void setLineWidth(float width) {
		int cap = stroke.getEndCap();
		int join = stroke.getLineJoin();
		stroke = new BasicStroke(width, cap, join);
	}

	public void setLineCap(String capTyp) {
		int cap;
		if (capTyp.equals(BUTT)) {
			cap = BasicStroke.CAP_BUTT;
		} else if (capTyp.equals(SQUARE)) {
			cap = BasicStroke.CAP_SQUARE;
		} else if (capTyp.equals(ROUND)) {
			cap = BasicStroke.CAP_ROUND;
		} else {
			cap = BasicStroke.CAP_BUTT;
		}
		int join = stroke.getLineJoin();
		float width = stroke.getLineWidth();
		stroke = new BasicStroke(width, cap, join);
	}

	public void setLineJoin(String joinTyp) {
		int join;
		if (joinTyp.equals(BEVEL)) {
			join = BasicStroke.JOIN_BEVEL;
		} else if (joinTyp.equals(MITER)) {
			join = BasicStroke.JOIN_MITER;
		} else if (joinTyp.equals(ROUND)) {
			join = BasicStroke.JOIN_ROUND;
		} else {
			join = BasicStroke.JOIN_BEVEL;
		}
		int cap = stroke.getEndCap();
		float width = stroke.getLineWidth();
		stroke = new BasicStroke(width, cap, join);
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// �R���X�g���N�^ & default
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public Pen() {
		color = Color.black;
		shape = PaintCanvas.RECTANGLE;// "�l�p�`"���[�h���f�t�H���g
		stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND);
	}
}
