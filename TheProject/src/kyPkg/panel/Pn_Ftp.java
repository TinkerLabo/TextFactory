package kyPkg.panel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import kyPkg.mySwing.ListPanelW2;
import kyPkg.panelMini.*;
import kyPkg.panelsc.BorderPanel;
import kyPkg.socks.Ftp;
public class Pn_Ftp extends BorderPanel {
	private static final long serialVersionUID = -5613170556136966301L;
    //-------------------------------------------------------------------------
    // Local変数
    //-------------------------------------------------------------------------
    private Ftp           ftpObj = null;
    private PnlFile       pnlFile;
    private PnlFtpConnect pnlConnect;
    private ListPanelW2   pnlList_S;
    private ListPanelW2   pnlList_L;
    private PnlFtpButtons pnlManu;
	private JLabel lbInfo;
    //-------------------------------------------------------------------------
    // コンストラクタ
    //-------------------------------------------------------------------------
    public Pn_Ftp() {
    	super();
    	this.setOpaque(false);
        createGUI();
    }
    //-------------------------------------------------------------------------
    // GUI部作成   部品間の連携をとっていかなければなるまい
    //-------------------------------------------------------------------------
    void createGUI(){
    	ftpObj = new kyPkg.socks.Ftp();
    	pnlList_S = new ListPanelW2();
    	pnlList_L = new ListPanelW2();
    	pnlList_S.setBtnCaption("DownLoad");
    	pnlList_L.setBtnCaption("UpLoad  ");
    	ftpObj.setList_Srv(pnlList_S.getList_Src());
    	pnlConnect   = new PnlFtpConnect(ftpObj);
    	lbInfo = new JLabel("Revel Yell");
    	pnlConnect.setInfoDisplay(lbInfo);
    	
		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, " ...");
		pmap1.put(PnlFile.OPT49ER, "false");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, "true");
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile = new PnlFile(pmap1);
		pnlFile.setTargetList(pnlList_L.getList_Src());
//    	pnlFile   = new PnlFile(pnlList_L.getList_Src());

    	pnlManu   = new PnlFtpButtons (ftpObj);
    	JTabbedPane listTab = new JTabbedPane();
    	
    	JPanel pnlServer = new JPanel(new BorderLayout());
    	JPanel pnlLocal  = new JPanel(new BorderLayout());
    	pnlServer.add(pnlList_S,BorderLayout.CENTER);
    	pnlServer.add(pnlManu,BorderLayout.SOUTH);

    	pnlLocal.add(pnlFile,BorderLayout.NORTH);
    	pnlLocal.add(pnlList_L,BorderLayout.CENTER);
    	
    	listTab.addTab("Local  Side",pnlLocal);
    	listTab.addTab("Server Side",pnlServer);

    	pnlN.add(pnlConnect);
    	pnlC.add(listTab);
    	lbInfo.setText("not Connected");
    	pnlS.add(lbInfo);
    	//---------------------------------------------------------------------
		// サーバからのファイル取得処理
    	//---------------------------------------------------------------------
    	pnlList_S.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("DOWNLOAD");
				final Object[] wArray = pnlList_S.getArray();
				Thread th1 = new Thread(){
					@Override
					public void run(){
						try{
							for (int i = 0;i<wArray.length;i++){
								String pPath = wArray[i].toString();
								System.out.println("→"+pPath);
						    	lbInfo.setText("DOWNLOAD→"+pPath);

//								String[] wOcc = pPath.split("\\s+");
//								System.out.println("wOcc.length:"+wOcc.length);
//								for (int j = 0;j<wOcc.length;j++){
//									System.out.println("wOcc["+j+"]:"+wOcc[j]);
//								}
								
								//ftpObj.doGet(pPath); 
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				};
				th1.start();
			}
		});
    	//---------------------------------------------------------------------
    	// サーバへのファイル転送処理
    	//---------------------------------------------------------------------
		pnlList_L.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("UPLOAD");
				final Object[] wArray = pnlList_S.getArray();
				Thread th1 = new Thread(){
					@Override
					public void run(){
						try{
							for (int i = 0;i<wArray.length;i++){
								String pPath = wArray[i].toString();
						    	lbInfo.setText("UPLOAD→"+pPath);
								//ftpObj.doPut(pPath);  
							}
						}catch(Exception e){
							e.printStackTrace();
						}
//						ftpObj.listUpdate();
					}
				};
				th1.start();			}
		});
    }
}
