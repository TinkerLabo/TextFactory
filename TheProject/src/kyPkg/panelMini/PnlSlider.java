package kyPkg.panelMini;
 
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PnlSlider extends JPanel{
	private static final long serialVersionUID = -3679681341587082210L;
	private JSlider jSlMin;
	private JTextField jTfMin;
	//-----------------------------------------------------------------------
	//　コンストラクタ   
	//-----------------------------------------------------------------------
	public PnlSlider(String title ,int min, int max, int value,int setMajorTickSpacing,int setMinorTickSpacing){
		super();
		this.setLayout(null);
		// ---------------------------------------------------------------------
		// スライダーパネル 
		// ---------------------------------------------------------------------
		jTfMin = new JTextField("");
		jTfMin.setBorder(new javax.swing.border.TitledBorder(title));
		jTfMin.setHorizontalAlignment(JTextField.CENTER);
		// ---------------------------------------------------------------------
		//　JSlider(int orientation, int min, int max, int value) 
		// ---------------------------------------------------------------------
		jSlMin = new JSlider(SwingConstants.HORIZONTAL,min, max, value);
		jSlMin.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		jSlMin.setPaintTicks(true);   	// 目盛りを描く
		jSlMin.setMajorTickSpacing(setMajorTickSpacing);	// 目盛り大の幅
		jSlMin.setMinorTickSpacing(setMinorTickSpacing);	// 目盛り小の幅
		jSlMin.setPaintLabels(true);  	// ラベルを描くかどうか
		jSlMin.setOpaque(false);
		jTfMin.setBounds(  0, 0,  50, 50);
		jSlMin.setBounds( 50, 0, 200, 50);
		this.add(jTfMin);
		this.add(jSlMin);
		// ---------------------------------------------------------------------
		jTfMin.setText(Integer.toString(jSlMin.getValue()));
		jSlMin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				jTfMin.setText(Integer.toString(jSlMin.getValue()));
			}
		});
	}
	@Override
	public void setEnabled(boolean flg){
		jSlMin.setEnabled(flg);
	}
	public int getValue()  {
		return jSlMin.getValue();
	}
}