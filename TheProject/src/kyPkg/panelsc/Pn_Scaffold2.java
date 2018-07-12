package kyPkg.panelsc;
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
public class Pn_Scaffold2  extends JP_Ancestor {
	public static void main(String[] argv){
		standAlone(new Pn_Scaffold2(),"scaffold2");
	}

	private static final long serialVersionUID = -1775170390761227057L;
//	private Inf_CommonTask  task;  // 実際に処理を行うクラス
	//-------------------------------------------------------------------------
	// Local変数
	//-------------------------------------------------------------------------
	private		JTabbedPane tabbedPane;
	private		JButton 	btn_Start;  // 実行ボタン
	protected	Read2GridPanel pnlInput;
	protected	RWPanel pnlOutput;
	protected	BorderPanel pnlSouth;
	public Pn_Scaffold2(){
		super();
		setSize(940, 520);
		this.setPreferredSize(new Dimension(940,520));
		initGUI();
	}
	//-------------------------------------------------------------------------
	// 新しいタブを追加する
	//-------------------------------------------------------------------------
	public void addTab(String title,Component component){
		tabbedPane.addTab(title,component);
	}
	//-------------------------------------------------------------------------
	//↓■これを変更する■
	//-------------------------------------------------------------------------
	/**《ＧＵＩ関連》 ここと初期化部分を修正する！  */
	void initGUI(){
		//---------------------------------------------------------------------
		pnlInput = new Read2GridPanel();
		pnlOutput = new RWPanel(RWPanel.COPY+RWPanel.CUT+RWPanel.WRITEIT+RWPanel.READDATA);
		pnlOutput.setDefaultPath(0, "C:/");
		//---------------------------------------------------------------------
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pnlInput, pnlOutput);
		splitPane.setResizeWeight(0.5);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		add(splitPane,BorderLayout.CENTER);	
		
		
//	    tabbedPane = new JTabbedPane();
//		tabbedPane.addTab("input",pnlInput);
//		tabbedPane.addTab("output",pnlOutput);
//		add(tabbedPane,BorderLayout.CENTER);	
		//---------------------------------------------------------------------
		pnlSouth = new BorderPanel();
//		pnlSouth.jPnlN.setLayout(new FlowLayout(FlowLayout.LEFT));
//		pnlSouth.jPnlS.setLayout(new FlowLayout());
		add(pnlSouth,BorderLayout.SOUTH);			
//		this.setOpaque(true);
	}
	//-------------------------------------------------------------------------
	/** ActionListenerインターフェースのメソッドを定義 */
	public  void actionPerformed(ActionEvent evt) {
		JButton button = (JButton)evt.getSource( );
		if (button == btn_Start){
//			task = new TskXXX(new String[]{".",pnlFile1.getPath(),""});
//			task.execute();      // 実処理を起動する
		}
	}
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
