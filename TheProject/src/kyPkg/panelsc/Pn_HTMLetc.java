package kyPkg.panelsc;
import java.awt.*;
import kyPkg.uFile.FileUtil;
import kyPkg.filter.*;
import java.awt.event.*;
import javax.swing.*;
// ============================================================================
public class Pn_HTMLetc extends Pn_Scaffold {
	public static void main(String[] argv){
		standAlone(new Pn_HTMLetc(),"HTML etc");
	}
	private static final long serialVersionUID = -1775170390761227057L;
	//-------------------------------------------------------------------------
	// Local�ϐ�
	//-------------------------------------------------------------------------
	private JButton btnTagCnv;   // �e�[�u���^�O��
	private JButton btnHtmCnv;   // �������ԕϊ�
	private CommonTabPane commonRes;
	//-------------------------------------------------------------------------
	void createGUI(){
    	//---------------------------------------------------------------------
		// ACTION
    	//---------------------------------------------------------------------
		commonRes.addFPanel("Read parm",new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
	            Thread th1 = new Thread(){
	                @Override
					public void run(){
	                    String path = commonRes.getInPath(1);
	                    commonRes.setTextOut(FileUtil.file2String(path));
	    			}
	            };
	            th1.start();
			}
		},false, "");
		this.setPreferredSize(new Dimension(600,430));
		btnTagCnv = new JButton("Table�^�O��");    btnTagCnv.setBounds(200,200,150,20); 
		btnHtmCnv = new JButton("�������ԕϊ�");   btnHtmCnv.setBounds(400,200,150,20); 
		//---------------------------------------------------------------------
		pnlSouth.pnlS.add(btnTagCnv);
		pnlSouth.pnlS.add(btnHtmCnv);
		//---------------------------------------------------------------------
		// �e�[�u���^�O��
		//---------------------------------------------------------------------
		btnTagCnv.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Flt_TableTag ft = new Flt_TableTag();
				ft.setDelimiter(","); // ��؂蕶�����J���}�ɁE�E���f�B�t�@�C�\
				ft.open_I(commonRes.getTextOut(), false);
				ft.open_O();
				ft.execute();
				String rtn = ft.getString();
				commonRes.setTextOut(rtn);
			}
		});
		//---------------------------------------------------------------------
		// �������ԕϊ�
		//---------------------------------------------------------------------
		btnHtmCnv.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Flt_Html ft = new Flt_Html();
				ft.open_I(commonRes.getTextOut(), false);
				ft.open_O();
				ft.execute();
				String rtn = ft.getString();
				commonRes.setTextOut(rtn);
			}
		});
	}
	//-------------------------------------------------------------------------
	/** �R���X�g���N�^ */
	public Pn_HTMLetc() {
		super(640, 480); 
		int option1 = -1;
		int option2 = -1;
		if (option1 != 0 & option2 != 0) {
			commonRes = new CommonTabPane("input", "outout", option1, option2);
			add(commonRes, BorderLayout.CENTER);
		}

		
		createGUI();    // GUI���쐬
	}
}
