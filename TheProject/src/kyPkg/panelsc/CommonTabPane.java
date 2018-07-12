package kyPkg.panelsc;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTabbedPane;

//-------------------------------------------------------------------------
//　外部部品化 2010/10/18
//-------------------------------------------------------------------------
public class CommonTabPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	protected RWPanel pnlIn;
	protected RWPanel pnlOut;

	public CommonTabPane(String title1, String title2, int option1, int option2) {
		super();
		System.out.println("new commonRes...");
		int Op1 = RWPanel.PASTE + RWPanel.CLEAR;
		int Op2 = RWPanel.COPY + RWPanel.CUT + RWPanel.WRITEIT
				+ RWPanel.TEXAREA;
		int Op3 = RWPanel.READDATA;
		// -1なら　PASTE+CLEAR+COPY+CUT+WRITEIT+TEXAREA+READDATA
		if (option1 < 0)
			option1 = Op1 + Op2 + Op3;
		// -1なら　PASTE+CLEAR+COPY+CUT+WRITEIT+TEXAREA
		if (option2 < 0)
			option2 = Op1 + Op2;
		pnlIn = new RWPanel(option1);
		pnlOut = new RWPanel(option2);
		this.addTab(title1, (Component)pnlIn);
		this.addTab(title2, (Component)pnlOut);
	}

	// -------------------------------------------------------------------------
	// 新しいタブを追加する
	// -------------------------------------------------------------------------
	public void addFPanel(String caption, ActionListener listener,
			boolean optDelim, String defaultPath) {
		pnlIn.addFPanel(caption, listener, optDelim, defaultPath);
	}

	public void setDefaultInPath(String path) {
		if (pnlIn != null)
			pnlIn.setDefaultPath(0, path);
	}

	public String getInPath(int index) {
		return pnlIn.getPath(index);
	}

	public void appendTextIn(String str) {
		pnlIn.append(str);
	}

	public void clearTextIn() {
		pnlIn.setText("");
	}

	public void modTextIn(String text) {
		pnlIn.modText(text);
	}

	public void setTextIn(String str) {
		pnlIn.setText(str);
	}

	public String getTextIn() {
		return pnlIn.getText();
	}

	public String[] getArray() {
		String tex = pnlIn.getText();
		String[] array = tex.split("\n");
		return array;
	}

	public List<String> getList() {
		return java.util.Arrays.asList(getArray());
	}

	public void setTextOut(String str) {
		pnlOut.setText(str);
		tabSelect(1);
	}

	public String getTextOut() {
		return pnlOut.getText();
	}

	public void tabSelect(int which) {
		setSelectedIndex(which);
	}
}
