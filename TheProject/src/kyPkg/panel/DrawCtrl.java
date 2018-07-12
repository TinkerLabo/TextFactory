package kyPkg.panel;

import kyPkg.panelMini.PaintCanvas;
import kyPkg.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;

//------------------------------------------------------------------------------
// ペイントツールの操作パネル DrawCtrl カッコ悪いプログラムだなぁ
//------------------------------------------------------------------------------
public class DrawCtrl extends JPanel {
	private static final long serialVersionUID = -7086883358134499156L;
	private JTextArea txtArea;
	private Pen pen;
	private JColorChooser chooser;
	private int seq = 0;

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// コンストラクタ
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public DrawCtrl(Pen ppen) {
		super();
		this.setLayout(new BorderLayout());
		this.setSize(500, 250);
		this.setPreferredSize(new Dimension(500, 250));
		this.pen = ppen;
		seq = 0;// スクリプトの変数名に使用

		// ---------+---------+---------+---------+---------+---------+---------+
		// カラーチューザー palette
		// ---------+---------+---------+---------+---------+---------+---------+
		chooser = new JColorChooser(Color.black);

		// --------------------------------------------------------------------
		// Layout
		// --------------------------------------------------------------------
		JScrollPane scroll_L = new JScrollPane(chooser);
		scroll_L.setPreferredSize(new Dimension(450, 250));
		// --------------------------------------------------------------------
		txtArea = new JTextArea("");
		JPanel panel_R = new Panel_R();
		this.add(BorderLayout.WEST, scroll_L);
		this.add(BorderLayout.CENTER, new JScrollPane(txtArea));
		this.add(BorderLayout.EAST, panel_R);

		// ---------------------------------------------------------------------
		// Action
		// ---------------------------------------------------------------------
		chooser.getSelectionModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				String str = getColorScript() + "\n";
				txtArea.append(str);
				System.out.println(str);
			}
		});
		// ---------+---------+---------+---------+---------+---------+---------+
		// 描画色を変更
		// ---------+---------+---------+---------+---------+---------+---------+
		chooser.getSelectionModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				ColorSelectionModel model = (ColorSelectionModel) evt.getSource();
				pen.setColor(model.getSelectedColor());
			}
		});
	}

	// 選ばれている色について　生成文を返す
	private String getColorScript() {
		Color color = chooser.getColor();
		int valueR = color.getRed();
		int valueG = color.getGreen();
		int valueB = color.getBlue();
		return String.format("Color color" + (seq++)
				+ " = new Color(%d,%d,%d);", valueR, valueG, valueB);
	}

	class Panel_R extends JPanel {
		private List<Color> colors = new ArrayList();
		private JSlider jSlider;

		Panel_R() {
			setLayout(null);
			setPreferredSize(new Dimension(200, 250));
			incoreColors();
			// ---------+---------+---------+---------+---------+---------+---------+
			JPanel Jp_Paint = new JPanel();
			Jp_Paint.setPreferredSize(new Dimension(250, 30));
			Jp_Paint.setBounds(0, 0, 200, 30);
			Jp_Paint.setBackground(colors.get(0));
			final JRadioButton radA1 = new JRadioButton("Fill", true);
			final JRadioButton radA2 = new JRadioButton("DRAW", false);
			radA1.setOpaque(false);
			radA2.setOpaque(false);
			ButtonGroup wBg1 = new ButtonGroup();
			wBg1.add(radA1);
			wBg1.add(radA2);
			Jp_Paint.add(radA1);
			Jp_Paint.add(radA2);
			this.add(Jp_Paint);
			// ---------+---------+---------+---------+---------+---------+---------+
			final JComboBox cmbShape = new JComboBox();
			cmbShape.addItem(PaintCanvas.RECTANGLE); // "四角形";
			cmbShape.addItem(PaintCanvas.ROUND_RECTANGLE); // "丸四角形";
			cmbShape.addItem(PaintCanvas.ARC); // "円弧";
			cmbShape.addItem(PaintCanvas.ELLIPSE); // "楕円";
			cmbShape.addItem(PaintCanvas.POLYLINE); // "多角形クローズ";
			cmbShape.addItem(PaintCanvas.POLYGON); // "多角形";
			cmbShape.setPreferredSize(new Dimension(250, 20));
			cmbShape.setBounds(0, 30, 100, 20);
			this.add(cmbShape);
			// ---------+---------+---------+---------+---------+---------+---------+
			final JComboBox cmbStyle = new JComboBox();
			cmbStyle.addItem("点線");
			cmbStyle.addItem("実線");
			cmbStyle.addItem("ワイド");
			cmbStyle.setPreferredSize(new Dimension(250, 20));
			cmbStyle.setBounds(100, 30, 100, 20);
			this.add(cmbStyle);

			// ---------+---------+---------+---------+---------+---------+---------+
			JPanel Jp_Cap = new JPanel();
			Jp_Cap.setBackground(colors.get(1));
			ButtonGroup group2 = new ButtonGroup();
			final JRadioButton radC1 = new JRadioButton(Pen.ROUND, true);
			final JRadioButton radC2 = new JRadioButton(Pen.BUTT, false);
			final JRadioButton radC3 = new JRadioButton(Pen.SQUARE, false);
			radC1.setOpaque(false);
			radC2.setOpaque(false);
			radC3.setOpaque(false);
			group2.add(radC1);
			group2.add(radC2);
			group2.add(radC3);
			Jp_Cap.add(radC1);
			Jp_Cap.add(radC2);
			Jp_Cap.add(radC3);
			Jp_Cap.setPreferredSize(new Dimension(250, 30));
			Jp_Cap.setBounds(0, 50, 200, 30);
			this.add(Jp_Cap);

			// ---------+---------+---------+---------+---------+---------+---------+
			JPanel Jp_Join = new JPanel();
			Jp_Join.setBackground(colors.get(2));
			ButtonGroup wBg3 = new ButtonGroup();
			final JRadioButton radJ1 = new JRadioButton(Pen.ROUND, true);
			final JRadioButton radJ2 = new JRadioButton(Pen.BEVEL, false);
			final JRadioButton radJ3 = new JRadioButton(Pen.MITER, false);
			radJ1.setOpaque(false);
			radJ2.setOpaque(false);
			radJ3.setOpaque(false);
			wBg3.add(radJ1);
			wBg3.add(radJ2);
			wBg3.add(radJ3);
			Jp_Join.add(radJ1);
			Jp_Join.add(radJ2);
			Jp_Join.add(radJ3);
			Jp_Join.setPreferredSize(new Dimension(250, 30));
			Jp_Join.setBounds(0, 70, 200, 30);
			this.add(Jp_Join);

			// ---------+---------+---------+---------+---------+---------+---------+
			jSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 1);
			jSlider.setPaintLabels(true);
			jSlider.setPaintTicks(true);
			jSlider.setMajorTickSpacing(10);
			jSlider.setMinorTickSpacing(5);
			jSlider.setPreferredSize(new Dimension(250, 60));
			jSlider.setBounds(0, 100, 200, 60);
			this.add(jSlider);

			// ---------+---------+---------+---------+---------+---------+---------+
			// スライダーによる線の太さ指定
			// ---------+---------+---------+---------+---------+---------+---------+
			jSlider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent evt) {
					JSlider jSlider = (JSlider) evt.getSource();
					pen.setLineWidth(jSlider.getValue());
				}
			});
			cmbShape.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String pShape = ((String) cmbShape.getSelectedItem());
					pen.setShape(pShape);
				}
			});
			// ---------+---------+---------+---------+---------+---------+---------+
			// ラジオボタンの処理
			// ---------+---------+---------+---------+---------+---------+---------+
			ItemListener CapAdp = new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent evt) {
					JRadioButton wRbC = (JRadioButton) evt.getSource();
					String cap = (wRbC.getText()).toUpperCase();
					pen.setLineCap(cap);
				}
			};
			radC1.addItemListener(CapAdp);
			radC2.addItemListener(CapAdp);
			radC3.addItemListener(CapAdp);
			// ---------+---------+---------+---------+---------+---------+---------+
			ItemListener JoinAdp = new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent evt) {
					JRadioButton wRbJ = (JRadioButton) evt.getSource();
					String join = (wRbJ.getText()).toUpperCase();
					pen.setLineJoin(join);
				}
			};
			radJ1.addItemListener(JoinAdp);
			radJ2.addItemListener(JoinAdp);
			radJ3.addItemListener(JoinAdp);

		}

		@Override
		public void paint(Graphics g) {
			super.paint(g); // Super経由でpaintCompをよぶ
		}

		@Override
		public void paintComponent(Graphics g) {
			// new Color(120,244,120)); //明るいグリーン
			Ruler.drawRuler(g, this.getSize().width,
					this.getSize().height, new Color(128, 255, 128, 250));
		}

		private void incoreColors() {
			colors.add(new Color(255, 153, 153));
			colors.add(new Color(255, 204, 153));
			colors.add(new Color(255, 255, 153));

			colors.add(new Color(153, 255, 255));
			colors.add(new Color(153, 153, 255));
			colors.add(new Color(204, 153, 255));
			colors.add(new Color(255, 153, 153));
			colors.add(new Color(255, 204, 153));
			colors.add(new Color(255, 255, 153));
			colors.add(new Color(204, 255, 153));
			colors.add(new Color(153, 255, 153));
		}

	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// 描画
	// ---+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void paint(Graphics g) {
		super.paint(g);// Super経由でpaintCompをよぶ
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void paintComponent(Graphics g) {
		System.out.println("## on paintComponent ##");
		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
				new Color(155, 187, 244));
	}
}
