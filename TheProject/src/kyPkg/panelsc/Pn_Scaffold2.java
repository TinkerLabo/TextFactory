package kyPkg.panelsc;
import kyPkg.panel.JP_Ancestor;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
// ============================================================================
/**�s�v���O���X�o�[�̗��p���`�t
 * ���ȉ��̓�̃N���X�Ɉˑ����Ă��܂�
 *   kyPkg.util.TskXXX
 *   kyPkg.util.SwingWorker
 */
public class Pn_Scaffold2  extends JP_Ancestor {
	public static void main(String[] argv){
		standAlone(new Pn_Scaffold2(),"scaffold2");
	}

	private static final long serialVersionUID = -1775170390761227057L;
//	private Inf_CommonTask  task;  // ���ۂɏ������s���N���X
	//-------------------------------------------------------------------------
	// Local�ϐ�
	//-------------------------------------------------------------------------
	private		JTabbedPane tabbedPane;
	private		JButton 	btn_Start;  // ���s�{�^��
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
	// �V�����^�u��ǉ�����
	//-------------------------------------------------------------------------
	public void addTab(String title,Component component){
		tabbedPane.addTab(title,component);
	}
	//-------------------------------------------------------------------------
	//���������ύX���遡
	//-------------------------------------------------------------------------
	/**�s�f�t�h�֘A�t �����Ə������������C������I  */
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
	/** ActionListener�C���^�[�t�F�[�X�̃��\�b�h���` */
	public  void actionPerformed(ActionEvent evt) {
		JButton button = (JButton)evt.getSource( );
		if (button == btn_Start){
//			task = new TskXXX(new String[]{".",pnlFile1.getPath(),""});
//			task.execute();      // ���������N������
		}
	}
	//-------------------------------------------------------------------------
	/** ���s�{�^���������ꂽ��
	* �v���O���X���j�^�[���Z�b�g�A�b�v�� �������̃X���b�h���J�n
	*/
	//-------------------------------------------------------------------------
	// ���[���[�\��
	//-------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
//		Ruler.drawRuler(g,this.getSize().width,this.getSize().height,
//			new Color(250,187,244,128));
	}
}
