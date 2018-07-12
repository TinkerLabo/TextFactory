package kyPkg.panelMini;
import kyPkg.util.*;
//import kyPkg.task.*;
//import kyPkg.*;
import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;
//-------------------------------------------------------------------------
// CnvPanel <���i >
//-------------------------------------------------------------------------
public class CnvPanel extends JPanel{
	private static final long serialVersionUID = -8525958693040631676L;
	private JTextField TfBef;
	private JTextField TfAft;
	//---------------------------------------------------------------------
	// �A�N�Z�b�T
	//---------------------------------------------------------------------
	public void setBef(String pVal){
		TfBef.setText(pVal);
	}
	public void setAft(String pVal){
		TfAft.setText(pVal);
	}
	public String getBef(){
		return TfBef.getText();
	}
	public String getAft(){
		return TfAft.getText();
	}
	//---------------------------------------------------------------------
	// �R���X�g���N�^
	//---------------------------------------------------------------------
	public CnvPanel(String pBef,String pAft,int pWidth){
		super();
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(pWidth,20));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		int wSize = (pWidth - 20) / 2;
		TfBef = new JTextField(pBef);    TfBef.setSize(wSize,20);
		TfAft = new JTextField(pAft);    TfAft.setSize(wSize,20);
		JLabel wLb01 = new JLabel("��"); wLb01.setSize(   20,20);
		this.add(TfBef);
		this.add(wLb01);
		this.add(TfAft);
	}
	public CnvPanel(){
		this("","",660);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Ruler.drawRuler(g,this.getSize().width,this.getSize().height,
			new Color(155,187,244));
		//  new Color(128,187,244));
	}
}
