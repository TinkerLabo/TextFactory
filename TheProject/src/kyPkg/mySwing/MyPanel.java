package kyPkg.mySwing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;
import kyPkg.sql.IsamConnector;
import kyPkg.uFile.FileUtil;
import kyPkg.util.Msgbox;
import kyPkg.util.Ruler;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected static final String vbCrLf = "\n";
	protected static final String vbTab = "\t";

	private Color bkColor = Color.WHITE;
	private boolean useGrid = false;

	protected List<List> ODBC_SQL2VECTOR(String xSql, String iPath) {
		List<List> matrix = null;
		if (FileUtil.isExists(iPath)) {
			System.out.println("xSql:" + xSql);
			matrix = new IsamConnector(iPath).query2Matrix(xSql);
		}
		return matrix;
	}

	// カーソルを変更
	public void setHourglass(boolean flag) {
		if (flag) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	protected int MsgBox(String msg) {
		// TODO ヤッツケ版
		new kyPkg.util.Msgbox(null).info(msg);
		return 0;
	}

	protected int MsgBox(String msg, int style, String caption) {
		// TODO ヤッツケ版
		new Msgbox(null).info(msg);
		return 0;
	}
	protected int UBound(List list) {
		return list.size();
	}

	protected int UBound(String[] table) {
		return table.length;
	}

	protected String Right(String str, int size) {
		int len = str.length();
		int pos = len - size;
		return str.substring(pos, pos + size);
	}

	public void setGrid(boolean useGrid) {
		this.useGrid = useGrid;
	}

	public MyPanel(int x, int y, int width, int height, LayoutManager layout) {
		super(layout);
		setBounds(x, y, width, height);
	}

	public MyPanel(LayoutManager layout) {
		super(layout);
	}

	public MyPanel() {
		super();
	}

	public MyPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public MyPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public void setLeft(int x) {
		Point currentLoc = getLocation();
		currentLoc.x = x;
		setLocation(currentLoc);
	}

	// -------------------------------------------------------------------------
	// ルーラー表示
	// -------------------------------------------------------------------------
	// public void paintComponent(Graphics g) {
	// super.paintComponent(g);
	// Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
	// new Color(250, 187, 244, 128));
	// }
	@Override
	public void paintComponent(Graphics g) {
		if (bkColor == null)
			bkColor = Color.white;
		if (useGrid)
			Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
					bkColor);
	}

}
