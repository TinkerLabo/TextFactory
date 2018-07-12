package kyPkg.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.im.InputContext;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import kyPkg.etc.CndBuilder;
import kyPkg.uCodecs.CharConv;

public class Pn_Cond extends JP_Ancestor {

	private ComboBoxModel cmbModelTyp;
	private ComboBoxModel cmbModelOpe;
	// ------------------------------------------------------------------------
	public static final String SPACE = "　";
	// ------------------------------------------------------------------------
	public static final String GA = "が";
	public static final String NI_ = "　に　";
	public static final String GA_ = "　が　";

	// ------------------------------------------------------------------------
	public static String getPattern2() {
		return CndBuilder.getPattern();
	}

	// ------------------------------------------------------------------------
	// private String opeStr = CONTAINS + "," + BEGIN_WITH + "," + EQUALS;
	// private String default2 = "が含まれる,で始まる,と一致する,が含まれない,で終わる,ではない,未設定である";
	// に、に、が、が、が、が、が、が
	// ------------------------------------------------------------------------
	protected boolean debug = true;
	private static final long serialVersionUID = 1L;
	private JComboBox cmbType;
	private JTextField condValue;
	private JComboBox cmbOpe;
	private String comment;
	private CharConv insCnv;
	private int texWidth;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Pn_Cond(String[] typeArray, int texWidth) {
		super();
		this.cmbModelTyp = new javax.swing.DefaultComboBoxModel(typeArray);
		this.cmbModelOpe = new javax.swing.DefaultComboBoxModel(
				CndBuilder.CONDITIONS);
		this.texWidth = texWidth;
		initComponents();
	}

	private void initComponents() {
		// --------------------------------------------------------------------
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setOpaque(false);
		int height = 25;
		// --------------------------------------------------------------------
		// type
		// --------------------------------------------------------------------
		cmbType = new JComboBox();
		cmbType.setPreferredSize(new Dimension(120, height));
		cmbType.setModel(cmbModelTyp);

		// --------------------------------------------------------------------
		// value
		// --------------------------------------------------------------------
		condValue = new JTextField();
		condValue.setPreferredSize(new Dimension(texWidth, height));
		condValue.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				//20170209	「表示される検索条件の後に、他の区分の条件が付加されるような表記になります」のバグ修正
				//フォーカスが外れたということで、なにか入力されたとみなしている・・・
				//	System.out.println(
				//	"#############################################< condValue.focusLost >####################");
				comment = "";//20170209
			}

			@Override
			public void focusGained(FocusEvent e) {
				Component c = e.getComponent();
				InputContext ic = c.getInputContext();
				String val = (String) cmbType.getSelectedItem();
				if (val.indexOf("名") > 0) {//名称ならIMEモード（FEP）をオン
					ic.setCompositionEnabled(true);
				} else {
					ic.setCompositionEnabled(false);
				}

			}
		});
        cmbType.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	condValue.setText("");
            }
        });
		// --------------------------------------------------------------------
		// Clear button
		// --------------------------------------------------------------------
		JButton btnClr = new JButton("C");
		btnClr.setPreferredSize(new Dimension(40, height));
		// --------------------------------------------------------------------
		// operator
		// --------------------------------------------------------------------
		cmbOpe = new JComboBox();
		cmbOpe.setPreferredSize(new Dimension(120, height));
		cmbOpe.setModel(cmbModelOpe);

		this.add(cmbType, null);
		this.add(condValue);
		this.add(btnClr);
		this.add(cmbOpe, null);
		// ---------------------------------------------------------------------
		// クリアボタン
		// ---------------------------------------------------------------------
		btnClr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				condValue.setText("");
				comment = "";
			}
		});
		condValue.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String val = condValue.getText();
				if (val.length() == 13 & val.matches("[0-9]*")) {
					// public static final Pattern JAN_PAT = Pattern
					// .compile("([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]+)");
					// cmbType.setSelectedIndex(0);//Code
					// System.out.println("Janだね！" + val);
					// cmbType.setSelectedItem("JANコード");//TODO　すこしインチキ
					// cmbOpe.setSelectedItem("と一致する");//TODO　すこしインチキ
				}
			}
		});

	}

	public void setCondValue(String type, String text) {
		this.cmbType.setSelectedItem(type);
		this.condValue.setText(text);
	}

	public void setCondText(String text, String comment) {
		condValue.setText(text);
		this.comment = comment;
	}

	public String getCondText() {
		return condValue.getText();
	}

	public void setCmbType(int index) {
		if (cmbType.getItemCount() > index) {
			cmbType.setSelectedIndex(index);
		}
	}

	public void setCmbType(String item) {
		cmbType.setSelectedItem(item);
	}

	// カッコつきのカウント部分をはずす
	private String removeCount(String comment) {
		int pos = comment.indexOf("(");
		if (pos >= 0) {
			comment = comment.substring(0, pos - 1);
		}
		return comment.trim();
	}

	public void setCmbOpe(String element) {
		if (element != null) {
			cmbOpe.setSelectedItem(element);
		}
	}

	private String getCondComm() {
		String ga = GA_;
		if (cmbOpe.getSelectedItem().toString().startsWith(GA)) {
			ga = NI_;
		}
		String value = getCondText();
		if (!comment.equals("")) {
			comment = removeCount(comment);
			value += "(" + comment + ")";//問題となった部分　20170209
		}
		if (insCnv == null) {
			insCnv = CharConv.getInstance();
		}
		value = insCnv.cnvWide(value);

		return cmbType.getSelectedItem().toString() + ga + value + SPACE
				+ cmbOpe.getSelectedItem().toString();
	}

	public String getCondition() {
		if (getCondText().equals("")) {
			return "";
		}
		return cmbType.getSelectedItem().toString() + TAB + getCondText() + TAB
				+ String.valueOf(cmbOpe.getSelectedIndex() + 1);
	}

	public String getCondstring() {
		String condValue = getCondition();
		String display = getCondComm();
		return display + VTAB + condValue;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setOpaque(false);
	}

	// FOR TEST
	//	public static void dialogView() {
	//		Pn_Cond panel = new Pn_Cond(RepDict.NAME_CODE, 100);
	//		showDialog(panel, "info");
	//	}
}
