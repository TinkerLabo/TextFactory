package kyPkg.panelsc;
import kyPkg.filter.Flt_Venn;
import kyPkg.panel.JP_Ancestor;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
// ============================================================================
/**《プログレスバーの利用雛形》
 * ※以下の二つのクラスに依存しています
 *   kyPkg.util.TskXXX
 *   kyPkg.util.SwingWorker
 */
public class Pn_Matcher  extends JP_Ancestor {
	private static final long serialVersionUID = -1775170390761227057L;
	//-------------------------------------------------------------------------
	// Local変数
	//-------------------------------------------------------------------------
	private		JTextArea 	txtArea;  // forDebug
	private		JTextField 	txtOutPath;  
	private		JButton 	btn_Start;  // 実行ボタン
	protected	Read2GridPanel pnlMast;
	protected	Read2GridPanel pnlTran;
	protected	BorderPanel pnlSouth;
	public Pn_Matcher(){
		super();
		initGUI();
	}
	//-------------------------------------------------------------------------
	//↓■これを変更する■
	//-------------------------------------------------------------------------
	/**《ＧＵＩ関連》 ここと初期化部分を修正する！  */
	void initGUI(){
		this.setPreferredSize(new Dimension(600,330));
		//---------------------------------------------------------------------
		pnlMast = new Read2GridPanel();
		pnlTran = new Read2GridPanel();

		//pnlTran.setDefaultPath("C:/");
		//---------------------------------------------------------------------
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pnlMast, pnlTran);
		splitPane.setResizeWeight(0.5);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		add(splitPane,BorderLayout.CENTER);	
		//---------------------------------------------------------------------
		pnlSouth = new BorderPanel();
		
		btn_Start= new JButton("Matching!");
		btn_Start.setPreferredSize(new Dimension(100, 20));

		pnlSouth.pnlN.add(btn_Start);
		txtOutPath = new JTextField("c:/@qpr/debug.txt"); //XXX 20130315
		txtOutPath.setPreferredSize(new Dimension(500,20));
		pnlSouth.pnlN.add(txtOutPath);

		txtArea= new JTextArea("");
		txtArea.setPreferredSize(new Dimension(500,100));
		pnlSouth.pnlN.add(txtArea);

		
		//		pnlSouth.jPnlN.setLayout(new FlowLayout(FlowLayout.LEFT));
//		pnlSouth.jPnlS.setLayout(new FlowLayout());
		add(pnlSouth,BorderLayout.SOUTH);			
//		this.setOpaque(true);
		btn_Start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(){
					@Override
					public void run(){
						maching();
					}
				};
				thread.start();
			}
		});	
	}
	private void maching(){
		String mastPath = pnlMast.getPath();
		String tranPath = pnlTran.getPath();
		String outPath = txtOutPath.getText();
		int mastCol = pnlMast.getCol();
		int tranCol = pnlTran.getCol();
		txtArea.setText(
				"master:"+mastPath+"\n"+
				"mastCol:"+mastCol+"\n"+
				"tranPath:"+tranPath+"\n"+
				"tranCol:"+tranCol+"\n"+
				"outPath=>"+outPath+"\n"
				);
		
		Flt_Venn venn = new Flt_Venn(outPath, mastPath, tranPath);
		venn.setTranKeyCol(3);
		venn.setTrimOpt(true);
		venn.execute();
	}
//	//-------------------------------------------------------------------------
//	/** ActionListenerインターフェースのメソッドを定義 */
//	public  void actionPerformed(ActionEvent evt) {
//		JButton button = (JButton)evt.getSource( );
//		if (button == btn_Start){
//		}
//	}
	//-------------------------------------------------------------------------
	/** 実行ボタンが押されたら
	* プログレスモニターをセットアップ＆ 実処理のスレッドを開始
	*/
	//-------------------------------------------------------------------------
	// ルーラー表示
	//-------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
//		Ruler.drawRuler(g,this.getSize().width,this.getSize().height,
//			new Color(250,187,244,128));
	}
}
