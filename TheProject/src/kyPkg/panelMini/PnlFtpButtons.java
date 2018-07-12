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
	//　コンストラクタ
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
		// CD ボタン
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
		// CDUP ボタン
		//---------------------------------------------------------------------
		jBtnCDUP.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				doCDUP();
				listUpdate();
			}
		});
		//---------------------------------------------------------------------
		// Mkdir ボタン
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
		// DEL ボタン
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
	// ラッパーメソッド
	//-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    // サーバー側のファイルリスト更新
    //-------------------------------------------------------------------------
    public void listUpdate(){
//    	ftpObj.doLIST("NLST");  // これを見せる
    	ftpObj.doLIST("LIST");  // これを見せる
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