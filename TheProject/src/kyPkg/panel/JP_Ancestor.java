package kyPkg.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

//*****************************************************************************
// 初期化および、親のサイズ変更を子コンポーネントに伝える為　
//*****************************************************************************
interface InfChilePanel {
	void initGui(int pWidth, int pHeight, Object pParent, LayoutManager layout);
}

public abstract class JP_Ancestor extends JPanel implements InfChilePanel {
	private static final long serialVersionUID = 8073335945621802823L;
	protected static final String TAB = "\t";
	protected static final String VTAB = "" + '\u000b'; // 垂直タブ

	private Component thisCmp;
	private boolean resizeable = false;
	private boolean closeMeOption = false;
	private String title = "";

	private HashMap<String, Object> results = null;
	protected DialogObj dialog;

	public HashMap<String, Object> getResults() {
		return results;
	}

	public void setResults(String key, Object obj) {
		if (results == null)
			results = new HashMap();
		this.results.put(key, obj);
	}

	public void setThisCmp(Component thisCmp) {
		this.thisCmp = thisCmp;
	}

	public Component getThisCmp() {
		return thisCmp;
	}

	public void setResizeable(boolean resizeable) {
		this.resizeable = resizeable;
	}

	public boolean isResizeable() {
		return resizeable;
	}

	public boolean isCloseMeOption() {
		return closeMeOption;
	}

	public void setCloseMeOption(boolean closeMe) {
		this.closeMeOption = closeMe;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title != null)
			this.title = title;
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public JP_Ancestor() {
		this("");
	}

	public JP_Ancestor(String title) {
		super();
		dialog = new DialogObj();
		this.title = title;
	}

	public void showDialog(String title) {
		dialog.showDialog(this, title);
	}

	public void destroyAllDialogs() {
		DialogObj.destroyAllDialogs();
	}

	public void closeDialog() {
		DialogObj.closeDialog();
	}

	public void dispose() {
		dialog.dispose();
	}

	public static void standAlone(JPanel panel, String title) {
		JFrame frame = new JFrame();
		//frame.setIconImage(new javax.swing.ImageIcon("./images/qpr.jpg")); // NOI18N
		frame.setTitle(title);
		Dimension dimension = panel.getSize();
		frame.setSize(panel.getSize());
		// --------------------------------------------------------------------
		// ウィンドウを中央に配置
		// --------------------------------------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height - 20;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width - 20;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setSize(frameSize);
		frame.getContentPane().add(panel);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}

	// -------------------------------------------------------------------------
	// initGui《 InfChilePanel 》
	// -------------------------------------------------------------------------
	@Override
	public void initGui(int pWidth, int pHeight, Object pParent,
			LayoutManager layout) {
	}
}
