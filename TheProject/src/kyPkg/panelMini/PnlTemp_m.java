package kyPkg.panelMini;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class PnlTemp_m extends JPanel{
	private static final long serialVersionUID = -3679681341587082210L;
	JButton jBtnAscii ;
	//-----------------------------------------------------------------------
	//�@�A�N�Z�b�T
	//-----------------------------------------------------------------------
	//-----------------------------------------------------------------------
	//�@�R���X�g���N�^
	//-----------------------------------------------------------------------
	public PnlTemp_m( ){
		super();
		this.setLayout(new FlowLayout());
		jBtnAscii = new JButton("ok");   	jBtnAscii.setPreferredSize(new Dimension(80,20));
		this.add(jBtnAscii );
		//---------------------------------------------------------------------
		// OK�{�^��
		//---------------------------------------------------------------------
		jBtnAscii.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("EasyAction");	   
			}
		});
	}
}