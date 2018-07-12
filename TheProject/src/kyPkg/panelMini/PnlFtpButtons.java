package kyPkg.panelMini;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import kyPkg.socks.Ftp;
public class PnlFtpButtons extends JPanel{
	private static final long serialVersionUID = -3679681341587082210L;
	private Ftp ftpObj = null;

	private JButton jBtnCD    ;
    private JButton jBtnCDUP  ;
    private JButton jBtnMkdir ;
    private JButton jBtnDEL   ;
    private JLabel jLab_Put;

	//-----------------------------------------------------------------------
	//�@�R���X�g���N�^
	//-----------------------------------------------------------------------
	public PnlFtpButtons(Ftp ftpObj){
		super();
		this.ftpObj = ftpObj;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		jLab_Put = new JLabel("");

		jBtnCD    = new JButton("CD");      	jBtnCD.setPreferredSize(new Dimension(80,20));
		jBtnCDUP  = new JButton("CDUP");    	jBtnCDUP.setPreferredSize(new Dimension(80,20));
		jBtnMkdir = new JButton("Mkdir");   	jBtnMkdir.setPreferredSize(new Dimension(80,20));
		jBtnDEL   = new JButton("Del");     	jBtnDEL.setPreferredSize(new Dimension(80,20));
		this.add(jLab_Put  ,gbc);
		this.add(jBtnCD    ,gbc);
		this.add(jBtnCDUP  ,gbc);
		this.add(jBtnMkdir ,gbc);
		this.add(jBtnDEL   ,gbc);
		//---------------------------------------------------------------------
		// CD �{�^��
		//---------------------------------------------------------------------
		jBtnCD.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String wPath = "QPR";//jTex_Get.getText();
				doCd(wPath);
				listUpdate();
			}
		});
		//---------------------------------------------------------------------
		// CDUP �{�^��
		//---------------------------------------------------------------------
		jBtnCDUP.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				doCDUP();
				listUpdate();
			}
		});
		//---------------------------------------------------------------------
		// Mkdir �{�^��
		//---------------------------------------------------------------------
		jBtnMkdir.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String wPath = "@@@";//jTex_Get.getText();
				doMKD(wPath);
				listUpdate();
			}
		});
		//---------------------------------------------------------------------
		// DEL �{�^��
		//---------------------------------------------------------------------
		jBtnDEL.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String wPath = "@@@";//jTex_Get.getText();
				doDELE(wPath);
//				wFtpCtrl.doRMD(wPath);
				listUpdate();
			}
		});
	}
	//-------------------------------------------------------------------------
	// ���b�p�[���\�b�h
	//-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    // �T�[�o�[���̃t�@�C�����X�g�X�V
    //-------------------------------------------------------------------------
    public void listUpdate(){
//    	ftpObj.doLIST("NLST");  // �����������
    	ftpObj.doLIST("LIST");  // �����������
    }
	public void doAscii(){
		ftpObj.doAscii();
	}
	public void doBinary(){
		ftpObj.doBinary();
	}
	public void doCDUP(){
		ftpObj.doCDUP();
	}
	public void doCd(String dirName){
		ftpObj.doCd(dirName);
	}
	public void doDELE(String fileName){
		ftpObj.doCd(fileName);
	}
	public void doMKD(String dirName){
		ftpObj.doMKD(dirName);
	}
}