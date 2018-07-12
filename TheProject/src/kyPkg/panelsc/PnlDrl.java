package kyPkg.panelsc;
import java.io.*;
import java.util.HashMap;

import kyPkg.filter.*;
import kyPkg.uFile.FileUtil;
import kyPkg.util.*;
import kyPkg.panelMini.PnlFile;
 
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
// ============================================================================
/**《プログレスバーの利用雛形》
 * ※以下の二つのクラスに依存しています
 *   kyPkg.util.TskXXX
 *   kyPkg.util.SwingWorker
 */
//public class PnlDrl extends JP_Ancestor implements ActionListener {
public class PnlDrl extends BorderPanel  {
	private static final long serialVersionUID = -2145282247704048664L;
	//-------------------------------------------------------------------------
	// Local変数
	//-------------------------------------------------------------------------
	private PnlFile    pnlFile1;
	private JButton jBtDrlCnv;   // Drill変換

	private JButton V_Btn1;
	private JButton R_Btn1;
	private JButton C_Btn2;
	private JButton X_Btn2;

	private JTextArea  jTa1;
	private JTextArea  jTa2;
	//-------------------------------------------------------------------------
	//↓■これを変更する■
	//-------------------------------------------------------------------------
	/**《ＧＵＩ関連》 ここと初期化部分を修正する！  */
	void createGUI(){
		
		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, "Read Parm");
		pmap1.put(PnlFile.OPT49ER, "false");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, "true");
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile1 = new PnlFile(pmap1);

		
//    	pnlFile1   = new PnlFile("Read Parm",false, false, true, "", -1);
        this.pnlN.add(pnlFile1,BorderLayout.NORTH);
    	//---------------------------------------------------------------------
		// ACTION
    	//---------------------------------------------------------------------
    	pnlFile1.setActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
	            Thread th1 = new Thread(){
	                @Override
					public void run(){
	                    String wPath = pnlFile1.getPath();
	    				jTa1.setText(FileUtil.file2String(wPath));
	    			}
	            };
	            th1.start();
			}
		});

		this.setPreferredSize(new Dimension(600,430));
		//---------------------------------------------------------------------
		JButton jBtWrite = new JButton("Write It");  jBtWrite.setBounds(  0,400,100,20); 
		jTa1 = new JTextArea("");
		jTa2 = new JTextArea("");
		JScrollPane jSp1 = new JScrollPane(jTa1);jSp1.setBounds(0, 50,600,150); 
		JScrollPane jSp2 = new JScrollPane(jTa2);jSp2.setBounds(0,260,600,140); 
		V_Btn1    = new JButton("Paste");     V_Btn1.setBounds(  0,200,100,20); 
		R_Btn1    = new JButton("Clear");     R_Btn1.setBounds(100,200,100,20); 
		C_Btn2    = new JButton("Copy");      C_Btn2.setBounds(100,400,100,20); 
		X_Btn2    = new JButton("Cut");       X_Btn2.setBounds(200,400,100,20); 
		jBtDrlCnv = new JButton("Drill変換");      jBtDrlCnv.setBounds(400,200,100,20); 

		BorderPanel pnlXB1 = new BorderPanel();
		BorderPanel pnlXB2 = new BorderPanel();
		pnlXB1.pnlS.setLayout(new FlowLayout());
		pnlXB2.pnlS.setLayout(new FlowLayout());
		pnlXB1.pnlS.add(jBtWrite);
		pnlXB1.pnlC.add(jSp1);
		pnlXB2.pnlC.add(jSp2);
		pnlXB1.pnlS.add(V_Btn1);
		pnlXB1.pnlS.add(R_Btn1);
		pnlXB2.pnlS.add(C_Btn2);
		pnlXB2.pnlS.add(X_Btn2);
		pnlXB1.pnlS.add(jBtDrlCnv);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,pnlXB1, pnlXB2);
	    splitPane.setDividerLocation(200);
		JPanel basePnl = new JPanel(){
			private static final long serialVersionUID = 6114884479112031169L;
			@Override
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				Ruler.drawRuler(g,this.getSize().width,this.getSize().height,
					new Color(155,187,244));
			}
		};
		basePnl.setLayout(new BorderLayout());
		this.add(basePnl,BorderLayout.CENTER);	
		this.setOpaque(true);
	    basePnl.add(splitPane);

		//---------------------------------------------------------------------
		// ドリルパラメータ→CSV変換
		//---------------------------------------------------------------------
		jBtDrlCnv.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
			/*
				FltDril ft = new FltDril();
				ft.setReader(jTa1.getText());
				ft.convert();
				String rtn = ft.getString();
				jTa2.setText(rtn);
			*/
                String wPath1 = pnlFile1.getPath();
				//String wPath1 = jTf1.getText();
				if(new File(wPath1).exists() == false ) {
					String wErr = "入力データが存在していません";
					JOptionPane.showMessageDialog((Component)null,wErr
								,"Message...",JOptionPane.ERROR_MESSAGE );
					return;
				}
				Flt_Dril ft = new Flt_Dril();
				ft.open_I(wPath1);
				ft.open_O(wPath1,false);
				ft.execute();
				ft.close();
				JOptionPane.showMessageDialog((Component)null,ft.getStat()
						,"Message...",JOptionPane.INFORMATION_MESSAGE);
				ft = null;

			}
		});

		//---------------------------------------------------------------------
		// Write It処理
		//---------------------------------------------------------------------
		jBtWrite.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser fc = new JFileChooser(".");
				if ( fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
				String wPath = fc.getSelectedFile().toString();

				if ( new File(wPath).exists() ){
				int result = JOptionPane.showConfirmDialog((Component)null
					,"すでに同名のファイルが存在します、上書きしますか？"
					,"確認!",JOptionPane.YES_NO_OPTION);
					if (result==JOptionPane.NO_OPTION) return;
				}

				if ( wPath.indexOf(".") == 0 ){ wPath = wPath.trim() + ".txt"; }
				FileUtil.string2file_(wPath,jTa2.getText());
			}
		});

		//---------------------------------------------------------------------
		// テキストエリア１編集
		//---------------------------------------------------------------------
		V_Btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				jTa1.selectAll();
				jTa1.paste();
			}
		});
		R_Btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				jTa1.selectAll();
				jTa1.setText("");
			}
		});
		//---------------------------------------------------------------------
		// テキストエリア２編集
		//---------------------------------------------------------------------
		C_Btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				jTa2.selectAll();
				jTa2.copy();
			}
		});
		X_Btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				jTa2.selectAll();
				jTa2.cut();
			}
		});
	}
	//-------------------------------------------------------------------------
	/** 実行ボタンが押されたら
	* プログレスモニターをセットアップ＆ 実処理のスレッドを開始
	*/
	//-------------------------------------------------------------------------
	/** コンストラクタ */
	public PnlDrl() {
		super(); 
		createGUI();    // GUI部作成
	}
	//-------------------------------------------------------------------------
	// ルーラー表示
	//-------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Ruler.drawRuler(g,this.getSize().width,this.getSize().height,
			new Color(155,187,244));
	}
}
